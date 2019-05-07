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
import com.anbang.p2p.plan.bean.RepayRecordState;
import com.anbang.p2p.plan.service.ImportRecordService;
import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.util.FileUtils;
import com.anbang.p2p.util.ImprotExcelUtil;
import com.anbang.p2p.util.bean.FileEntity;
import com.anbang.p2p.web.vo.CommonVO;
import com.highto.framework.web.page.ListPage;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
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

        ImportState importState = ImportState.finish;

        for (RepayRecord list : importRecord.getRepayRecords()) {
            try {
                OrderValueObject order = orderCmdService.changeOrderStateClean(list.getUserId());
                orderQueryService.updateLoanOrderByImport(order, list.getRepayAmount());
                list.setState(RepayRecordState.finish);
                continue;
            } catch (OrderNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalOperationException e) {
                e.printStackTrace();
            }
            list.setState(RepayRecordState.error);
            importState = ImportState.error;
        }

        importRecord.setImportState(importState);
        importRecordService.save(importRecord);

        return CommonVOUtil.success("success");
    }



    /**
     * excel 销账导入
     */
    @PostMapping("/repayImport")
    public CommonVO repayImport(@RequestParam("file") MultipartFile file) {
        try {
//            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//            MultipartFile file = multipartRequest.getFile("template1");

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

            Workbook workbook = ImprotExcelUtil.checkExcel(fileEntity);
            importRecordService.saveImprotMaterial(workbook, fileEntity.getFileName());
            return CommonVOUtil.success("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonVOUtil.error("上传错误，请检查文件格式是否正确");
    }

    /**
     * 下载模板
     */
    @RequestMapping("/getTemplate")
    public String getTemplate(HttpServletResponse response) {

        String fileName = "催收销账.xlsx";//被下载文件的名称// 设置文件名，根据业务需要替换成要下载的文件名
        if (fileName != null) {
            //设置文件路径
            String realPath = "/data/files/";
            File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
}
