package com.anbang.p2p.web.controller;

import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;
import com.anbang.p2p.cqrs.q.dao.LoanOrderDao;
import com.anbang.p2p.cqrs.q.service.OrderQueryService;
import com.anbang.p2p.plan.bean.ImportRecord;
import com.anbang.p2p.plan.bean.ImportState;
import com.anbang.p2p.plan.bean.RepayRecord;
import com.anbang.p2p.plan.service.ImportRecordService;
import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.util.FileUtils;
import com.anbang.p2p.util.ImprotExcelUtil;
import com.anbang.p2p.util.bean.FileEntity;
import com.anbang.p2p.web.vo.CommonVO;
import com.highto.framework.web.page.ListPage;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/importReocrd")
public class ImportReocrdController {
    @Autowired
    private ImportRecordService importRecordService;

    @Autowired
    private OrderCmdService orderCmdService;

    @Autowired
    private OrderQueryService orderQueryService;


    /**
     * excel 查询导入记录
     */
    @RequestMapping("/queryImport")
    public CommonVO qrueryImport(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                 @RequestParam(name = "size", defaultValue = "20") Integer size, ImportRecord importRecord) {
        if (importRecord == null) {
            importRecord = new ImportRecord();
        }

        ListPage listPage = importRecordService.findByBean(page, size, importRecord);
        return CommonVOUtil.success(listPage, "success");
    }

    /**
     * 导入详情
     */
    @RequestMapping("/qrueryDetail")
    public CommonVO qrueryDetail(String id) {
        ImportRecord importRecord = importRecordService.getById(id);
        return CommonVOUtil.success(importRecord, "success");
    }

    /**
     * 删除导入记录
     */
    @RequestMapping("/deleteRecord")
        public CommonVO deleteRecord(String id) {
        ImportRecord importRecord = importRecordService.getById(id);
        if (importRecord == null || !importRecord.getImportState().equals(ImportState.wait)) {
            return CommonVOUtil.error("state error");
        }

        importRecordService.delelte(id);
        return CommonVOUtil.success("success");
    }

    /**
     * 销账
     */
    @RequestMapping("/updateRecord")
    public CommonVO updateRecord(String id) {
        ImportRecord importRecord = importRecordService.getById(id);
        if (importRecord == null || !importRecord.getImportState().equals(ImportState.wait)) {
            return CommonVOUtil.error("state error");
        }

        for (RepayRecord list : importRecord.getRepayRecords()) {
            try {
                OrderValueObject order = orderCmdService.changeOrderStateClean(list.getUserId());
                orderQueryService.updateLoanOrderByImport(order);
            } catch (OrderNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalOperationException e) {
                e.printStackTrace();
            }
        }

        return CommonVOUtil.success("success");
    }



    /**
     * excel 销账导入
     */
    @RequestMapping("/repayImport")
    public CommonVO repayImport(@RequestParam(value="filename") MultipartFile file) {
        try {

            InputStream inputstream = file.getInputStream();
            if (!(inputstream.markSupported())) {
                inputstream = new PushbackInputStream(inputstream, 8);
            }

            String fileName = file.getOriginalFilename();
            String prefix =
                    fileName.lastIndexOf(".") >= 1 ? fileName.substring(fileName.lastIndexOf(".") + 1)
                            : null;
            FileEntity fileEntity = new FileEntity();
            fileEntity.setInputStream(inputstream);
            fileEntity.setFileType(prefix);
            fileEntity.setFileName(fileName);

//            List<FileEntity> list = FileUtils.getFilesFromRequest(request);
//            if (list == null || list.size() == 0) {
//                return CommonVOUtil.error("文件错误");
//            }
//            FileEntity fileEntity = list.get(0);

            Workbook workbook = ImprotExcelUtil.checkExcel(fileEntity);
            importRecordService.saveImprotMaterial(workbook, fileEntity.getFileName());
            return CommonVOUtil.success("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonVOUtil.error("上传错误，请检查文件格式是否正确");
    }


}
