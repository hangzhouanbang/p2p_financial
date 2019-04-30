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
import com.anbang.p2p.plan.bean.RepayRecord;
import com.anbang.p2p.util.ExcelUtils;
import com.anbang.p2p.web.vo.LoanOrderQueryVO;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
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

    // 导出用户详情
    public void exportUserInfo(String userId, OutputStream output) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 用户身份证信息
        UserBaseInfo baseInfo = userBaseInfoDao.findById(userId);
        if (baseInfo != null) {
            ExcelUtils.detailBaseInfo(workbook, baseInfo);
        }

        LoanOrderQueryVO queryVO = new LoanOrderQueryVO();
        queryVO.setUserId(userId);
        List<LoanOrder> orderList = mongodbLoanOrderDao.find(1, 500, queryVO);
        ExcelUtils.userLoanList(workbook, orderList);

        UserContacts contacts = userContactsDao.findById(userId);
        if (contacts != null) {
            ExcelUtils.detailContacts(workbook, contacts);
            ExcelUtils.detailAddressBook(workbook, contacts.getAddressBook());
        }

        workbook.write(output);
        workbook.close();
    }

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

            ExcelUtils.baseInfoExcel(rowid, sheetNum, orderList, workbook);
            sheetNum++;
        }
        workbook.write(output);
        workbook.close();
    }

    // 导出单页详情
    public void exportDetail(String[] ids, OutputStream output) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(output);
        // 设置注释
        zipOutputStream.setComment("welcome to here to look look");

        byte[] buffer = new byte[5*1024];
        int length = 0;

        List<LoanOrder> orderList = mongodbLoanOrderDao.listByIds(ids);
        for (LoanOrder list : orderList) {
            // 四页：身份证信息、卡密详情、紧急联系人、通讯录
            XSSFWorkbook workbook = new XSSFWorkbook();
            UserBaseInfo baseInfo = userBaseInfoDao.findById(list.getUserId());
            UserContacts contacts = userContactsDao.findById(list.getUserId());
            ExcelUtils.detailBaseInfo(workbook, baseInfo);
            ExcelUtils.detailLoanOrderVO(workbook, list);
            ExcelUtils.detailContacts(workbook, contacts);
            ExcelUtils.detailAddressBook(workbook, contacts.getAddressBook());

            //导出文件设置
            String fileName = list.getId() +".xlsx";
            zipOutputStream.putNextEntry(new ZipEntry(fileName));

            //将生成excel转化为输入流返回
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            while ((length = inputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer,0,length);
            }
            inputStream.close();
        }
        // 关闭输出流
        zipOutputStream.close();
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

            ExcelUtils.baseInfoExcel(rowid, sheetNum, orderList, workbook);
            sheetNum++;
        }
        workbook.write(output);
        workbook.close();
    }

    // 批量导出详情
    public void exportDetailBatch(LoanOrderQueryVO queryVO, OutputStream output) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(output);
        // 设置注释
        zipOutputStream.setComment("welcome to here to look look");

        byte[] buffer = new byte[5*1024];
        int length = 0;

        List<LoanOrder> orderList = mongodbLoanOrderDao.find(1, 10000, queryVO);
        for (LoanOrder list : orderList) {
            XSSFWorkbook workbook = new XSSFWorkbook();

            // 四页：身份证信息、卡密详情、紧急联系人、通讯录
            UserBaseInfo baseInfo = userBaseInfoDao.findById(list.getUserId());
            UserContacts contacts = userContactsDao.findById(list.getUserId());

            ExcelUtils.detailBaseInfo(workbook, baseInfo);
            ExcelUtils.detailLoanOrderVO(workbook, list);
            ExcelUtils.detailContacts(workbook, contacts);
            ExcelUtils.detailAddressBook(workbook, contacts.getAddressBook());

            //导出文件设置
            String fileName = list.getId() +".xlsx";
            zipOutputStream.putNextEntry(new ZipEntry(fileName));

            //将生成excel转化为输入流返回
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            while ((length = inputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer,0,length);
            }
            inputStream.close();
        }
        // 关闭输出流
        zipOutputStream.close();
    }

}
