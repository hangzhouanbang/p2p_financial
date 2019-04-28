package com.anbang.p2p.util;

import java.util.List;

import com.anbang.p2p.cqrs.q.dbo.LoanOrder;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserContacts;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelUtils {

	public static void userLoanList(XSSFWorkbook workbook, List<LoanOrder> orders) {
		XSSFSheet spreadsheet = workbook.createSheet("orderInfo");
		XSSFRow row = spreadsheet.createRow(0);
		row.createCell(0).setCellValue("卡密编号");
		row.createCell(1).setCellValue("用户姓名");
		row.createCell(2).setCellValue("身份证号");
		row.createCell(3).setCellValue("联系方式");
		row.createCell(4).setCellValue("欠款本息金额");
		row.createCell(5).setCellValue("逾期时长");
		row.createCell(6).setCellValue("逾期利息");
		row.createCell(7).setCellValue("逾期利率");
		row.createCell(8).setCellValue("当前应还");
		row.createCell(9).setCellValue("登录ip");
		row.createCell(10).setCellValue("ip归属地");

		int rowNum = 1;
		for (LoanOrder vo : orders) {
			XSSFRow row1 = spreadsheet.createRow(rowNum);
			row1.createCell(0).setCellValue(vo.getId());
			row1.createCell(1).setCellValue(vo.getRealName());
			row1.createCell(2).setCellValue(vo.getIDcard());
			row1.createCell(3).setCellValue(vo.getPhone());
			row1.createCell(4).setCellValue(vo.getAmount());
			row1.createCell(5).setCellValue(vo.getOverdueDay() + "天");
			row1.createCell(6).setCellValue(vo.getInterest());
			row1.createCell(7).setCellValue(vo.getOverdue_rate());
			row1.createCell(8).setCellValue(vo.getShouldRepayAmount());
			row1.createCell(9).setCellValue(vo.getLoginIp());
			row1.createCell(10).setCellValue(vo.getIpAddress());
			rowNum++;
		}
	}

	public static void baseInfoExcel(Integer rowid, Integer sheetNum, List<LoanOrder> list,
										   XSSFWorkbook workbook) {
		XSSFSheet spreadsheet = null;
		if (rowid > 20000) {
			sheetNum++;
			spreadsheet = workbook.createSheet("OrderInfo" + sheetNum);
			rowid = 0;
		} else {
			spreadsheet = workbook.createSheet("OrderInfo" + sheetNum);
			XSSFRow row = spreadsheet.createRow(rowid);
			row.createCell(0).setCellValue("卡密编号");
			row.createCell(1).setCellValue("用户姓名");
			row.createCell(2).setCellValue("身份证号");
			row.createCell(3).setCellValue("联系方式");
			row.createCell(4).setCellValue("欠款本息金额");
			row.createCell(5).setCellValue("逾期时长");
			row.createCell(6).setCellValue("逾期利息");
			row.createCell(7).setCellValue("逾期利率");
			row.createCell(8).setCellValue("当前应还");
			rowid++;
		}
		for (LoanOrder vo : list) {
			XSSFRow row = spreadsheet.createRow(rowid);
			row.createCell(0).setCellValue(vo.getId());
			row.createCell(1).setCellValue(vo.getRealName());
			row.createCell(2).setCellValue(vo.getIDcard());
			row.createCell(3).setCellValue(vo.getPhone());
			row.createCell(4).setCellValue(vo.getAmount());
			row.createCell(5).setCellValue(vo.getOverdueDay() + "天");
			row.createCell(6).setCellValue(vo.getInterest());
			row.createCell(7).setCellValue(vo.getOverdue_rate());
			row.createCell(8).setCellValue(vo.getShouldRepayAmount());
			rowid++;
		}
	}

	public static void detailBaseInfo(XSSFWorkbook workbook, UserBaseInfo userBaseInfo) {
		XSSFSheet spreadsheet = workbook.createSheet("idCardInfo");
		XSSFRow row = spreadsheet.createRow(0);
		row.createCell(0).setCellValue("用户id");
		row.createCell(1).setCellValue("身份证号码");
		row.createCell(2).setCellValue("真实姓名");
		row.createCell(3).setCellValue("人脸图片");
		row.createCell(4).setCellValue("身份证正面");
		row.createCell(5).setCellValue("身份证反面");

		XSSFRow row1 = spreadsheet.createRow(1);
		row1.createCell(0).setCellValue(userBaseInfo.getId());
		row1.createCell(1).setCellValue(userBaseInfo.getIDcard());
		row1.createCell(2).setCellValue(userBaseInfo.getRealName());
		row1.createCell(3).setCellValue(userBaseInfo.getFaceImgUrl());
		row1.createCell(4).setCellValue(userBaseInfo.getIDcardImgUrl_front());
		row1.createCell(5).setCellValue(userBaseInfo.getIDcardImgUrl_reverse());
	}

	public static void detailLoanOrderVO(XSSFWorkbook workbook, LoanOrder vo) {
		XSSFSheet spreadsheet = workbook.createSheet("orderInfo");
		XSSFRow row = spreadsheet.createRow(0);
		row.createCell(0).setCellValue("卡密编号");
		row.createCell(1).setCellValue("用户姓名");
		row.createCell(2).setCellValue("身份证号");
		row.createCell(3).setCellValue("联系方式");
		row.createCell(4).setCellValue("欠款本息金额");
		row.createCell(5).setCellValue("逾期时长");
		row.createCell(6).setCellValue("逾期利息");
		row.createCell(7).setCellValue("逾期利率");
		row.createCell(8).setCellValue("当前应还");
		row.createCell(9).setCellValue("登录ip");
		row.createCell(10).setCellValue("ip归属地");

		XSSFRow row1 = spreadsheet.createRow(1);
		row1.createCell(0).setCellValue(vo.getId());
		row1.createCell(1).setCellValue(vo.getRealName());
		row1.createCell(2).setCellValue(vo.getIDcard());
		row1.createCell(3).setCellValue(vo.getPhone());
		row1.createCell(4).setCellValue(vo.getAmount());
		row1.createCell(5).setCellValue(vo.getOverdueDay() + "天");
		row1.createCell(6).setCellValue(vo.getInterest());
		row1.createCell(7).setCellValue(vo.getOverdue_rate());
		row1.createCell(8).setCellValue(vo.getShouldRepayAmount());
		row1.createCell(9).setCellValue(vo.getLoginIp());
		row1.createCell(10).setCellValue(vo.getIpAddress());
	}

	public static void detailContacts(XSSFWorkbook workbook, UserContacts contacts) {
		XSSFSheet spreadsheet = workbook.createSheet("phoneInfo");
		XSSFRow row = spreadsheet.createRow(0);
		row.createCell(0).setCellValue("用户id");
		row.createCell(1).setCellValue("直接联系人手机号码");
		row.createCell(2).setCellValue("直接联系人姓名");
		row.createCell(3).setCellValue("关系");
		row.createCell(4).setCellValue("一般联系人手机号码");
		row.createCell(5).setCellValue("一般联系人姓名");
		row.createCell(6).setCellValue("关系");

		XSSFRow row1 = spreadsheet.createRow(1);
		row1.createCell(0).setCellValue(contacts.getUserId());
		row1.createCell(1).setCellValue(contacts.getDirectContactsPhone());
		row1.createCell(2).setCellValue(contacts.getDirectContactsName());
		row1.createCell(3).setCellValue(contacts.getDireactRelation());
		row1.createCell(4).setCellValue(contacts.getCommonContactsPhone());
		row1.createCell(5).setCellValue(contacts.getCommonContactsName());
		row1.createCell(6).setCellValue(contacts.getCommonRelation());
	}

	public static void detailAddressBook(XSSFWorkbook workbook, String json) {
		XSSFSheet spreadsheet = workbook.createSheet("addressBook");
		XSSFRow row = spreadsheet.createRow(0);
		row.createCell(0).setCellValue("姓名(手机号)");

		String[] strArray = json.split(",");

		for (int i = 0; i < strArray.length; i ++ ) {
			XSSFRow row1 = spreadsheet.createRow(i+1);
			row1.createCell(0).setCellValue(strArray[i]);
		}
	}
}
