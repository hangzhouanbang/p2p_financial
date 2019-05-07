package com.anbang.p2p.plan.service;

import com.anbang.p2p.plan.bean.ImportRecord;
import com.anbang.p2p.plan.bean.ImportState;
import com.anbang.p2p.plan.bean.RepayRecord;
import com.anbang.p2p.plan.bean.RepayRecordState;
import com.anbang.p2p.plan.dao.ImportRecordDao;
import com.anbang.p2p.util.ImprotExcelUtil;
import com.google.gson.reflect.TypeToken;
import com.highto.framework.web.page.ListPage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ImportRecordService {
    @Autowired
    private ImportRecordDao importRecordDao;

    public void save(ImportRecord order){
        importRecordDao.save(order);
    }

    public void delelte(String id){
        importRecordDao.delelte(id);
    }

    public ImportRecord getById(String id){
        return importRecordDao.getById(id);
    }

    public long getAmount(ImportRecord importRecord){
        return importRecordDao.getAmount(importRecord);
    }

    public ListPage findByBean(int page, int size, ImportRecord importRecord){
        long count = importRecordDao.getAmount(importRecord);
        List<ImportRecord> records = importRecordDao.findByBean(page, size, importRecord);
        if (CollectionUtils.isNotEmpty(records)) {
            records.forEach((record) -> record.setRepayRecords(null));
        }
        return new ListPage(records, page, size, (int)count);
    }

    public void saveImprotMaterial(Workbook workbook, String fileName) throws Exception {
        //导入数据列数,与实体字符一样
        String[] cellName = {"id", "userId", "actualRate", "realName", "amount", "repayAmount"};
        //SheetAt 位置，和第几行几列开始
        List<Map<String, Object>> listMap = ImprotExcelUtil.excelToListMap(workbook.getSheetAt(0),1,0, cellName);
        //转实体
        List<RepayRecord> list = ImprotExcelUtil.listFromJson(ImprotExcelUtil.objFromJson(listMap),new TypeToken<List<RepayRecord>>(){}.getType());
        ImportRecord importRecord = new ImportRecord();
        importRecord.setName(fileName);

        list.forEach(p -> p.setState(RepayRecordState.wait));
        importRecord.setRepayRecords(list);

        importRecord.setImportState(ImportState.wait);
        importRecord.setCauseBy("");
        importRecord.setCreateTime(System.currentTimeMillis());
        importRecord.setRecordCount(list.size());

        importRecordDao.save(importRecord);

    }



}
