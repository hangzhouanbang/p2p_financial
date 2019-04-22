package com.anbang.p2p.web.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.anbang.p2p.constants.CommonRecordState;
import com.anbang.p2p.cqrs.q.dao.mongodb.MongodbLoanOrderDao;
import com.anbang.p2p.cqrs.q.dbo.AgentPayRecord;
import com.anbang.p2p.cqrs.q.dbo.OrderContract;
import com.anbang.p2p.cqrs.q.service.AgentPayRecordService;
import com.anbang.p2p.cqrs.q.service.LoanOrderExportService;
import com.anbang.p2p.cqrs.q.service.UserService;
import com.anbang.p2p.util.AgentPay;
import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.web.vo.RepayImport;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;
import com.anbang.p2p.cqrs.q.dbo.LoanOrder;
import com.anbang.p2p.cqrs.q.service.OrderQueryService;
import com.anbang.p2p.web.vo.CommonVO;
import com.anbang.p2p.web.vo.LoanOrderQueryVO;
import com.highto.framework.web.page.ListPage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/ordermanager")
public class OrderManagerController {

	@Autowired
	private OrderCmdService orderCmdService;

	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private AgentPayRecordService agentPayRecordService;

	@Autowired
	private UserService userService;

	@Autowired
	private LoanOrderExportService loanOrderExportService;

	/**
	 * 查询卡密
	 */
	@RequestMapping("/order_query")
	public CommonVO queryOrder(@RequestParam(required = true, defaultValue = "1") int page,
			@RequestParam(required = true, defaultValue = "20") int size, LoanOrderQueryVO query) {
		CommonVO vo = new CommonVO();
		ListPage listPage = orderQueryService.findLoanOrder(page, size, query);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 查询待审核卡密
	 */
	@RequestMapping("/check_order_query")
	public CommonVO queryCheckOrder(@RequestParam(required = true, defaultValue = "1") int page,
			@RequestParam(required = true, defaultValue = "20") int size, LoanOrderQueryVO query) {
		CommonVO vo = new CommonVO();
		query.setState(OrderState.check_by_admin);
		ListPage listPage = orderQueryService.findLoanOrder(page, size, query);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 通过待审核卡密
	 */
	@RequestMapping("/check_order_pass")
	public CommonVO checkOrderPass(@RequestParam(required = true) String orderId) {
		CommonVO vo = new CommonVO();
		LoanOrder order = orderQueryService.findLoanOrderById(orderId);
		if (order == null) {
			vo.setSuccess(false);
			vo.setMsg("order not found");
			return vo;
		}
		try {
			OrderValueObject orderValueObject = orderCmdService.changeOrderStateToWait(order.getUserId());
			LoanOrder loanOrder = orderQueryService.updateLoanOrder(orderValueObject);

			//  放款,失败后重新审核
			AgentPayRecord agentPayRecord = new AgentPayRecord(orderValueObject, loanOrder);
			agentPayRecord.setStatus(CommonRecordState.INIT);
			agentPayRecordService.save(agentPayRecord);

			String realAmount = String.valueOf(loanOrder.getRealAmount());
			boolean flag = AgentPay.pay(loanOrder.getPayAccount(), loanOrder.getRealName(), realAmount, agentPayRecord.getId());
			if (flag) {
				OrderValueObject payResult = orderCmdService.changeOrderStateToRefund(order.getUserId(), System.currentTimeMillis());
				orderQueryService.updateLoanOrder(payResult);
				agentPayRecordService.updataStatus(agentPayRecord.getId(), CommonRecordState.SUCCESS);
				return CommonVOUtil.success("success");
			} else {
				OrderValueObject payError = orderCmdService.changeOrderStateToCheck_by_admin(order.getUserId());
				orderQueryService.updateLoanOrder(payError);
				agentPayRecordService.updataStatus(agentPayRecord.getId(), CommonRecordState.ERROR);
			}
			return CommonVOUtil.error("代付失败，请检查代付平台信息");
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
	}

	/**
	 * 拒绝待审核卡密
	 */
	@RequestMapping("/check_order_refuse")
	public CommonVO checkOrderRefuse(@RequestParam(required = true) String orderId) {
		CommonVO vo = new CommonVO();
		LoanOrder order = orderQueryService.findLoanOrderById(orderId);
		if (order == null) {
			vo.setSuccess(false);
			vo.setMsg("order not found");
			return vo;
		}
		try {
			OrderValueObject orderValueObject = orderCmdService.refuseOrder(order.getUserId());
			orderQueryService.updateLoanOrder(orderValueObject);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		return vo;
	}

	/**
	 * 查询待催收卡密
	 */
	@RequestMapping("/collect_order_query")
	public CommonVO queryCollectOrder(@RequestParam(required = true, defaultValue = "1") int page,
			@RequestParam(required = true, defaultValue = "20") int size, LoanOrderQueryVO query) {
		CommonVO vo = new CommonVO();
		query.setState(OrderState.collection);
		ListPage listPage = orderQueryService.findLoanOrder(page, size, query);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 待还款转逾期
	 */
	@Scheduled(cron = "0 20 0 * * ?") // 每天凌晨20
	@RequestMapping("/refund_to_overdue")
	public void refundTransferToOverdue() {
		LoanOrderQueryVO query = new LoanOrderQueryVO();
		query.setNowTime(System.currentTimeMillis());
		query.setState(OrderState.refund);
		int size = 2000;
		long amount = orderQueryService.countAmount(query);
		long pageCount = amount % size > 0 ? amount / size + 1 : amount / size;
		for (int page = 1; page <= pageCount; page++) {
			List<LoanOrder> orderList = orderQueryService.findLoanOrderList(page, size, query);
			try {
				for (LoanOrder order : orderList) {
					OrderValueObject orderValueObject = orderCmdService.changeOrderStateToOverdue(order.getUserId());
					orderQueryService.updateLoanOrder(orderValueObject);

					// 更新用户逾期次数
					userService.getAndUpdateOverdueCount(order.getUserId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 逾期转催收
	 */
	@Scheduled(cron = "0 40 0 * * ?") // 每天凌晨40
	@RequestMapping("/overdue_to_collection")
	public void overdueTransferToCollection() {
		LoanOrderQueryVO query = new LoanOrderQueryVO();
		query.setState(OrderState.overdue);
		int size = 2000;
		long amount = orderQueryService.countAmount(query);
		long pageCount = amount % size > 0 ? amount / size + 1 : amount / size;
		for (int page = 1; page <= pageCount; page++) {
			List<LoanOrder> orderList = orderQueryService.findLoanOrderList(page, size, query);
			try {
				for (LoanOrder order : orderList) {
					OrderValueObject orderValueObject = orderCmdService.changeOrderStateToCollection(order.getUserId());
					orderQueryService.updateLoanOrder(orderValueObject);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 新增合同号 todo
	 */
	@RequestMapping("/addContract")
	public CommonVO addContract(OrderContract contract) {
		if (contract == null) {
			return CommonVOUtil.invalidParam();
		}
		orderQueryService.saveOrderContract(contract);
		return CommonVOUtil.success("success");
	}

	/**
	 * 催收导出
	 * @param ids 卡密id list
	 * @param exportType 导出类型 simple/detail
	 * @return
	 */
	@RequestMapping("/collectionExport")
	public CommonVO collectionExport(@RequestParam(value = "ids") String[] ids, String exportType, HttpServletResponse response) {
		if (ids == null) {
			return CommonVOUtil.invalidParam();
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		OutputStream output = null;

		if ("simple".equals(exportType)) {
			String fileName = format.format(date) + exportType + "Order.xlsx";
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			response.setContentType("application/msexcel");

			try {
				output = response.getOutputStream();
				loanOrderExportService.exportSimple(ids, output);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return CommonVOUtil.success("success");
		}
		if ("detail".equals(exportType)) {
			String fileName = format.format(date) + "催收批量详情文件" + ".zip";
			response.setContentType("application/octet-stream ");
			response.setHeader("Connection", "close"); // 表示不能用浏览器直接打开
			response.setHeader("Accept-Ranges", "bytes");// 告诉客户端允许断点续传多线程连接下载
			try {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
				response.setCharacterEncoding("UTF-8");

				output = response.getOutputStream();
				ZipOutputStream zipOutputStream = new ZipOutputStream(output);

				// 业务数据封装
				loanOrderExportService.exportDetail(ids, zipOutputStream);

				// 关闭输出流
				zipOutputStream.flush();
				zipOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return CommonVOUtil.systemException();
	}

	/**
	 * 催收导出
	 * @param queryVO 查询条件
	 * @param exportType 导出类型 simple/detail
	 * @return
	 */
	@RequestMapping("/collectionExportBatch")
	public CommonVO collectionExportBatch(LoanOrderQueryVO queryVO, String exportType, HttpServletResponse response) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();

		if ("simple".equals(exportType)) {
			String fileName = format.format(date) + exportType + "Order.xlsx";
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			response.setContentType("application/msexcel");
			OutputStream output = null;
			try {
				output = response.getOutputStream();
				loanOrderExportService.exportSimpleBatch(queryVO, output);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return CommonVOUtil.success("success");
		}
		if ("detail".equals(exportType)) {

		}
		return CommonVOUtil.systemException();
	}

	/**
	 * excel 销账导入
	 */
	@RequestMapping("/repayImport")
	public CommonVO repayImport(@RequestParam MultipartFile file, HttpServletRequest request) {

		if(!file.isEmpty()){
			String filePath = file.getOriginalFilename();
			//windows
//			String savePath = request.getSession().getServletContext().getRealPath(filePath);

			//linux
			String savePath = "/data/tomcat/apache-tomcat-9.0.10/webapps/p2p/excel";

			File targetFile = new File(savePath);

			if(!targetFile.exists()){
				targetFile.mkdirs();
			}

			try {
				file.transferTo(targetFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return CommonVOUtil.success("success");
		}

		return CommonVOUtil.error("");
	}

	public static List<RepayImport> readExcel(String fileName) throws Exception{

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
//		List<RepayImport> imports = new ArrayList<>();
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
//					RepayImport repayImport = new RepayImport();
//					repayImport.setId(orderId.toString());
//					imports.add(repayImport);
//
//				}
//			}
//		}
		return null;
	}



}
