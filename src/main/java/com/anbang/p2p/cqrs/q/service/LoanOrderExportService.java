package com.anbang.p2p.cqrs.q.service;

import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;
import com.anbang.p2p.cqrs.q.dao.UserBaseInfoDao;
import com.anbang.p2p.cqrs.q.dao.UserContactsDao;
import com.anbang.p2p.cqrs.q.dao.mongodb.MongodbLoanOrderDao;
import com.anbang.p2p.cqrs.q.dbo.LoanOrder;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserContacts;
import com.anbang.p2p.util.ExcelUtils;
import com.anbang.p2p.web.vo.LoanOrderQueryVO;
import com.anbang.p2p.web.vo.LoanOrderVO;
import com.anbang.p2p.web.vo.RepayImport;
import javafx.util.Pair;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 卡密导入导出
 */
@Service
public class LoanOrderExportService {

    @Autowired
    private MongodbLoanOrderDao mongodbLoanOrderDao;

    @Autowired
    private UserBaseInfoDao userBaseInfoDao;

    @Autowired
    private UserContactsDao userContactsDao;

    @Autowired
    private OrderCmdService orderCmdService;


    // 导出单页
    public void exportSimple(String[] ids, OutputStream output) throws IOException {
        Integer rowid = 0;
        Integer sheetNum = 1;
        XSSFWorkbook workbook = new XSSFWorkbook();
        long amount = ids.length;
        int size = ids.length;
        long pageNum = amount % size > 0 ? amount / size + 1 : amount / size;
        for (int page = 1; page <= pageNum; page++) {
            List<LoanOrder> orderList = mongodbLoanOrderDao.listByIds(ids);

            // 结果转换
            List<LoanOrderVO> vos = new ArrayList<>();
            for (LoanOrder list : orderList) {
                LoanOrderVO vo = new LoanOrderVO(list);
                try {
                    shouldRepayAmount(list, System.currentTimeMillis(), vo);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                vos.add(vo);
            }

            ExcelUtils.baseInfoExcel(rowid, sheetNum, vos, workbook);
            sheetNum++;
        }
        workbook.write(output);
        workbook.close();
    }

    // 导出单页详情
    public void exportDetail(String[] ids, ZipOutputStream zipOutputStream) throws IOException {
        List<LoanOrder> orderList = mongodbLoanOrderDao.listByIds(ids);
        for (LoanOrder list : orderList) {
            XSSFWorkbook workbook = new XSSFWorkbook();

            // 四页：身份证信息、卡密详情、紧急联系人、通讯录
            UserBaseInfo baseInfo = userBaseInfoDao.findById(list.getUserId());
            LoanOrderVO vo = new LoanOrderVO(list);
            shouldRepayAmount(list, System.currentTimeMillis(), vo);
            UserContacts contacts = userContactsDao.findById(list.getUserId());

            ExcelUtils.detailBaseInfo(workbook, baseInfo);
            ExcelUtils.detailLoanOrderVO(workbook, vo);
            ExcelUtils.detailContacts(workbook, contacts);
            ExcelUtils.detailAddressBook(workbook, contacts.getAddressBook());

            ZipEntry entry = new ZipEntry(list.getRealName() + list.getIDcard() + ".xls");
            zipOutputStream.putNextEntry(entry);
            workbook.write(zipOutputStream);
        }
    }

    // 批量导出
    public void exportSimpleBatch(LoanOrderQueryVO queryVO, OutputStream output) throws IOException {
        Integer rowid = 0;
        Integer sheetNum = 1;
        XSSFWorkbook workbook = new XSSFWorkbook();
        long amount = mongodbLoanOrderDao.getAmount(queryVO);
        int size = 300;
        long pageNum = amount % size > 0 ? amount / size + 1 : amount / size;
        for (int page = 1; page <= pageNum; page++) {
            List<LoanOrder> orderList = mongodbLoanOrderDao.find(page, size, queryVO);

            // 结果转换
            List<LoanOrderVO> vos = new ArrayList<>();
            for (LoanOrder list : orderList) {
                LoanOrderVO vo = new LoanOrderVO(list);
                try {
                    shouldRepayAmount(list, System.currentTimeMillis(), vo);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                vos.add(vo);
            }

            ExcelUtils.baseInfoExcel(rowid, sheetNum, vos, workbook);
            sheetNum++;
        }
        workbook.write(output);
        workbook.close();
    }

    public void repay(List<RepayImport> imports) {
        for (RepayImport list : imports) {
            LoanOrder order = mongodbLoanOrderDao.findById(list.getId());
            if (order != null) {

                try {
                    orderCmdService.changeOrderStateClean(list.getUserId());

                    order.setState(OrderState.clean);
                    order.setRealRefundAmount(list.getRepayAmount());
                    order.setRefundTime(System.currentTimeMillis());
                    // TODO: 2019/4/22
                    order.setRepayType("催收销账");
                    mongodbLoanOrderDao.save(order);
                } catch (OrderNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalOperationException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * ----------------计算应还款
     */
    public void shouldRepayAmount(LoanOrder order, long currentTime, LoanOrderVO vo) {
        Map map = new HashMap();

        if (!OrderState.overdue.equals(order.getState()) && !OrderState.collection.equals(order.getState())){
            return ;
        }
        if (currentTime < order.getDeliverTime()) {
            return;
        }
        // 如果需要精确计算，非要用String来够造BigDecimal不可
        BigDecimal b_amount = new BigDecimal(Double.toString(order.getAmount()));
        BigDecimal b_rate = new BigDecimal(Double.toString(order.getRate()));
        BigDecimal b_freeOfInterest = new BigDecimal(
                Long.toString(order.getFreeTimeOfInterest() / 24 / 60 / 60 / 1000));
        if (currentTime > order.getMaxLimitTime()) {
            // 逾期应还:到期应还金额+逾期天数X本金X逾期利率
            long limitDay = (order.getMaxLimitTime() - order.getDeliverTime()) / 1000 / 60 / 60 / 24;
            BigDecimal b_limitDay = new BigDecimal(Long.toString(limitDay));
            BigDecimal limit_num = b_limitDay.subtract(b_freeOfInterest);
            BigDecimal amount = b_amount.add(b_amount.multiply(b_rate.multiply(limit_num)));

            long day = (currentTime - order.getMaxLimitTime()) / 1000 / 60 / 60 / 24;
            BigDecimal b_day = new BigDecimal(Long.toString(day));
            BigDecimal b_overdue_rate = new BigDecimal(Double.toString(order.getOverdue_rate()));

            BigDecimal interest = b_amount.multiply(b_overdue_rate.multiply(b_day));

            vo.setOverdueTime(day);
            vo.setInterest(interest.doubleValue());
            vo.setInterest(amount.add(interest).doubleValue());
            return ;
        } else {
            return;
        }
    }
}
