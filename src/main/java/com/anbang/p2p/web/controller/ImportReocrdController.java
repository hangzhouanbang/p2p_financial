package com.anbang.p2p.web.controller;

import com.anbang.p2p.plan.bean.ImportRecord;
import com.anbang.p2p.plan.bean.ImportState;
import com.anbang.p2p.plan.bean.RepayRecord;
import com.anbang.p2p.plan.service.ImportRecordService;
import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.web.vo.CommonVO;
import com.highto.framework.web.page.ListPage;
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
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/importReocrd")
public class ImportReocrdController {
    @Autowired
    private ImportRecordService importRecordService;

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


        return CommonVOUtil.success("success");
    }



    /**
     * excel 销账导入
     */
    @RequestMapping("/repayImport")
    public CommonVO repayImport(MultipartHttpServletRequest request, HttpServletResponse response) {
//
//        ReturnStandardDataFormat standardData = new ReturnStandardDataFormat(CustomConstants.CUSTOM_SELECT_EXCEPTION,"导入客户信息失败",null);
//
//        MultipartFile file = request.getFile("file");
//        ExcelReader er = new ExcelReader();
//        int count =0;
//        int error =0;
//        int success = 0;
//
//        List<Custom> list_ = new ArrayList<Custom>();
//        User u = getUser(request.getSession());//SessionUtils.getUser(request.getSession());
//        Long corpId = Long.valueOf(u.getCorpId());
//        Date date = new Date();
//        String returnMsg = "";
//        int index = 1;
//
//        try {
//
//            List<Map<Integer,String>> list = er.readExcelContentByList(file.getInputStream()); //读取Excel数据内容
//            count = list.size();
//
//            for(Map<Integer,String> map : list){
//
//                if(map.get(0)==null || "".equals(map.get(0))){
//                    returnMsg += "第"+index+"行：【客户简称(必填)】列不能为空;";
//                } else if(map.get(1)==null || "".equals(map.get(1))){
//                    returnMsg += "第"+index+"行：【客户全称(必填)】列不能为空;";
//                } else {
//                    int num = 0;
//                    QueryCustomParam params = new QueryCustomParam();
//                    params.setShortName(map.get(0));
//                    params.setCorpId(Long.valueOf(u.getCorpId()));
//                    num = customService.checkCustom(params); //查询相同客户
//
//                    if(num==0){
//                        Custom custom = new Custom();
//                        custom.setId(UUIDUtil.getLongUUID());
//                        custom.setShortName(map.get(0)==null? null : map.get(0));
//                        custom.setName(map.get(1)==null? null : map.get(1));
//                        custom.setNumber(map.get(2)==null? null : map.get(2));
//                        custom.setAddress(map.get(3)==null? null : map.get(3));
//                        custom.setUrl(map.get(4)==null? null : map.get(4));
//                        custom.setDescription(map.get(5)==null? null : map.get(5));
//                        custom.setCustomStatusId(map.get(6)==null? null : basedataService.getLabelId("custom_status", map.get(6), corpId) );
//                        custom.setCustomLevelId(map.get(7)==null? null : basedataService.getLabelId("custom_level", map.get(7), corpId) );
//                        custom.setCreaterId(Long.valueOf(u.getUserId()));
//                        custom.setCreateDate(date);
//                        custom.setUpdaterId(Long.valueOf(u.getUserId()));
//                        custom.setUpdateDate(date);
//                        custom.setCorpId(Long.valueOf(u.getCorpId()));
//
//                        list_.add(custom);
//
//                    } else {
//                        returnMsg += "第"+index+"行：【客户简称(必填)】列："+ map.get(0)+"已存在;";
//                    }
//                    index++;
//                }
//            }
//
//            int cuccess = customService.batchInsert(list_); //批量导入客户信息
//
//            standardData.setReturnCode(0);
//            standardData.setReturnData(null);
//
//            error = count - success;
//            standardData.setReturnMessage(returnMsg);
//
//        } catch (Exception e) {
//            log.error("批量导入客户信息异常：" + e.getMessage());
//            standardData.setReturnMessage(e.getMessage());
//        }
//
//        return JsonHelper.encodeObject2Json(standardData, "yyyy-MM-dd HH:mm:ss");
        return null;
    }



    public static List<RepayRecord> readExcel(String fileName) throws Exception{

//
//		InputStream is = new FileInputStream(new File(fileName));
//		Workbook hssfWorkbook = null;
//		if (fileName.endsWith("xlsx")){
//			hssfWorkbook = new XSSFWorkbook(is);//Excel 2007
//		}else if (fileName.endsWith("xls")){
//			hssfWorkbook = new HSSFWorkbook(is);//Excel 2003
//
//		}
//		// HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
//		// XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
//		List<RepayRecord> imports = new ArrayList<>();
//
//		// 循环工作表Sheet
//		for (int numSheet = 0; numSheet <hssfWorkbook.getNumberOfSheets(); numSheet++) {
//			//HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
//			Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
//			if (hssfSheet == null) {
//				continue;
//			}
//			// 循环行Row
//			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
//				//HSSFRow hssfRow = hssfSheet.getRow(rowNum);
//				Row hssfRow = hssfSheet.getRow(rowNum);
//				if (hssfRow != null) {
//					//HSSFCell name = hssfRow.getCell(0);
//					//HSSFCell pwd = hssfRow.getCell(1);
//					Cell orderId = hssfRow.getCell(0);
//					Cell userId = hssfRow.getCell(1);
//
//					// TODO: 2019/4/22
//					RepayRecord repayImport = new RepayRecord();
//					repayImport.setId(orderId.toString());
//					imports.add(repayImport);
//
//				}
//			}
//		}
        return null;
    }
}
