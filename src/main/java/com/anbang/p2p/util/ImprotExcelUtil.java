package com.anbang.p2p.util;

import com.anbang.p2p.util.bean.FileEntity;
import com.google.gson.Gson;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImprotExcelUtil {

    public static Workbook checkExcel(FileEntity fileEntity) throws Exception{
        Workbook workbook = null;
        // 判断文件类型
        if (fileEntity.getFileType().equalsIgnoreCase("xls")) {
            workbook = new HSSFWorkbook(fileEntity.getInputStream());
        } else if (fileEntity.getFileType().equalsIgnoreCase("xlsx")) {
            //workbook = new XSSFWorkbook(fileEntity.getInputStream());
            workbook = WorkbookFactory.create(fileEntity.getInputStream());
        } else {
            throw new RuntimeException("上传文件格式不正确！");
        }
        return workbook;
    }

    /**
     * 数据转成List<Map>
     * @param sheet  流
     * @param cellName 对象数据列，和实体字符一样
     * @param  rowNum 从第几行开始
     * @param cellNum 从第几列开始
     * @return 返回List<Map>
     */
    public static List<Map<String,Object>> excelToListMap(Sheet sheet,int rowNum,int cellNum,String [] cellName){
        List<Map<String,Object>> listMap =new ArrayList<>();
        for (int i = rowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Map<String,Object> map = new HashMap<>();
            for(int v=cellNum;v<row.getLastCellNum();v++){
                Object obj = row.getCell(v);
                if(obj != null){
                    row.getCell(v).setCellType(Cell.CELL_TYPE_STRING);
                    map.put(cellName[v],row.getCell(v).getStringCellValue());
                }else {
                    map.put(cellName[v],"");
                }

            }
            listMap.add(map);
        }
        return  listMap;
    }
    public static String objFromJson(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
    public static <T> T fromJson(String json,Class<T> type){
        Gson gson = new Gson();
        return gson.fromJson(json,type);
    }
    public static <T> List <T> listFromJson(String json,Type type){
        Gson gson = new Gson();
        return gson.fromJson(json,type);
    }

}