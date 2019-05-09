package com.anbang.p2p.web.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipOutputStream;

import com.anbang.p2p.constants.CommonRecordState;
import com.anbang.p2p.constants.ExpandType;
import com.anbang.p2p.constants.Operator;
import com.anbang.p2p.constants.RecordState;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.q.dbo.AgentPayRecord;
import com.anbang.p2p.cqrs.q.dbo.OrderContract;
import com.anbang.p2p.cqrs.q.service.AgentPayRecordService;
import com.anbang.p2p.cqrs.q.service.LoanOrderExportService;
import com.anbang.p2p.cqrs.q.service.UserService;
import com.anbang.p2p.plan.bean.ImportRecord;
import com.anbang.p2p.plan.bean.LoanStateRecord;
import com.anbang.p2p.plan.bean.RepayRecord;
import com.anbang.p2p.plan.bean.RiskInfo;
import com.anbang.p2p.plan.service.ImportRecordService;
import com.anbang.p2p.plan.service.RiskService;
import com.anbang.p2p.util.AgentPay;
import com.anbang.p2p.util.CalAmountUtil;
import com.anbang.p2p.util.CommonVOUtil;
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

	@Autowired
	private RiskService riskService;

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
	 * 查询卡密详情
	 */
	@RequestMapping("/order_detail_query")
	public CommonVO queryOrderDetail(String id) {
		LoanOrder loanOrder = orderQueryService.findLoanOrderById(id);
		return CommonVOUtil.success(loanOrder, "success");
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
	 * 查询风控信息
	 */
	@RequestMapping("/queryRiskInfo")
	public CommonVO queryRiskInfo(String userId) {
		RiskInfo riskInfo = riskService.getByUserId(userId);
		return CommonVOUtil.success(riskInfo, "success");
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

				LoanStateRecord record = new LoanStateRecord();
				record.setOrderId(loanOrder.getId());
				record.setToState(RecordState.pay);
				record.setOperator(Operator.ADMIN);
				record.setCreateTime(System.currentTimeMillis());
				record.setDesc("通过审核");
				orderQueryService.saveStateRecord(record);

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

			LoanStateRecord record = new LoanStateRecord();
			record.setOrderId(orderValueObject.getId());
			record.setToState(RecordState.refuse);
			record.setOperator(Operator.ADMIN);
			record.setCreateTime(System.currentTimeMillis());
			record.setDesc("管理员拒绝");
			orderQueryService.saveStateRecord(record);

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
	@Scheduled(cron = "0 1 0 * * ?") // 每天凌晨20
	@RequestMapping("/refund_to_overdue")
	public void refundTransferToOverdue() {
		LoanOrderQueryVO query = new LoanOrderQueryVO();
		query.setNowTime(System.currentTimeMillis());
		query.setState(OrderState.refund);
		int size = 1000;
		long amount = orderQueryService.countAmount(query);
		long pageCount = amount % size > 0 ? amount / size + 1 : amount / size;
		for (int page = 1; page <= pageCount; page++) {
			List<LoanOrder> orderList = orderQueryService.findLoanOrderList(page, size, query);
			try {
				for (LoanOrder order : orderList) {
					OrderValueObject orderValueObject = orderCmdService.changeOrderStateToOverdue(order.getUserId());
					orderQueryService.updateLoanOrderPlus(orderValueObject);

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
	@Scheduled(cron = "0 10 0 * * ?") // 每天凌晨30
	@RequestMapping("/overdue_to_collection")
	public void overdueTransferToCollection() {
		LoanOrderQueryVO query = new LoanOrderQueryVO();
		query.setState(OrderState.overdue);
		int size = 1000;
		long amount = orderQueryService.countAmount(query);
		long pageCount = amount % size > 0 ? amount / size + 1 : amount / size;
		for (int page = 1; page <= pageCount; page++) {
			List<LoanOrder> orderList = orderQueryService.findLoanOrderList(page, size, query);
			try {
				for (LoanOrder order : orderList) {

					// 不需要转催收,只计算应还钱
					if (System.currentTimeMillis() < order.getMaxLimitTime() + order.getOverdue()) {
						CalAmountUtil.shouldRepayAmount(order, System.currentTimeMillis());
						orderQueryService.updateLoanOrderAmount(order.getId(), order.getOverdueDay(), order.getInterest(),
								order.getShouldRepayAmount());
						continue;
					}

					OrderValueObject orderValueObject = orderCmdService.changeOrderStateToCollection(order.getUserId());
					orderQueryService.updateLoanOrderPlus(orderValueObject);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 催收更新应还款
	 */
	@Scheduled(cron = "0 20 0 * * ?") // 每天凌晨40
	@RequestMapping("/collection_cal")
	public void collectionCal() {
		LoanOrderQueryVO query = new LoanOrderQueryVO();
		query.setState(OrderState.collection);
		int size = 1000;
		long amount = orderQueryService.countAmount(query);
		long pageCount = amount % size > 0 ? amount / size + 1 : amount / size;
		for (int page = 1; page <= pageCount; page++) {
			List<LoanOrder> orderList = orderQueryService.findLoanOrderList(page, size, query);
			try {
				for (LoanOrder order : orderList) {
					CalAmountUtil.shouldRepayAmount(order, System.currentTimeMillis());
					orderQueryService.updateLoanOrderAmount(order.getId(), order.getOverdueDay(), order.getInterest(),
							order.getShouldRepayAmount());
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
	 * 用户详情导出
	 */
	@RequestMapping("/exportUserInfo")
	public CommonVO exportUserInfo(String userId, HttpServletResponse response) {
		if (userId == null) {
			return CommonVOUtil.invalidParam();
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String fileName = format.format(date) + "_userInfo_" + userId + ".xlsx";
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType("application/msexcel");

		try {
			OutputStream output = response.getOutputStream();
			// 业务数据封装
			loanOrderExportService.exportUserInfo(userId, output);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CommonVOUtil.systemException();
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


		if ("simple".equals(exportType)) {
			String fileName = format.format(date) + exportType + "Order.xlsx";
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			response.setContentType("application/msexcel");

			try {
				OutputStream output = response.getOutputStream();
				loanOrderExportService.exportSimple(ids, output);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return CommonVOUtil.success("success");
		}
		if ("detail".equals(exportType)) {
			response.setHeader("Content-Type","application/octect-stream");
			response.setHeader("Content-Disposition","attachment;filename=order.zip");

			try {
				OutputStream output = response.getOutputStream();
				// 业务数据封装
				loanOrderExportService.exportDetail(ids, output);
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
		queryVO.setState(OrderState.collection);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();

		if ("simple".equals(exportType)) {

			String fileName = format.format(date) + exportType + "Order.xlsx";
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			response.setContentType("application/msexcel");
			try {
				OutputStream output = response.getOutputStream();
				loanOrderExportService.exportSimpleBatch(queryVO, output);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return CommonVOUtil.success("success");
		}
		if ("detail".equals(exportType)) {
			response.setHeader("Content-Type","application/octect-stream");
			response.setHeader("Content-Disposition","attachment;filename=orderDetail.zip");
			try {

				OutputStream output = response.getOutputStream();
				// 业务数据封装
				loanOrderExportService.exportDetailBatch(queryVO, output);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return CommonVOUtil.systemException();
	}

	/**
	 * 变更卡密状态
	 */
	@RequestMapping("/changeOrderStateByAdmin")
	public CommonVO changeOrderStateByAdmin(String id, String userId, OrderState orderState, Double amount) {
		try {
			if (amount == null || amount <= 0) {
				return CommonVOUtil.success("amount error");
			}

			OrderValueObject object = orderCmdService.changeOrderStateByAdmin(id, userId, orderState);
			orderQueryService.updateLoanOrderState(object.getId(), object.getState(), amount);

			userService.updateUserState(object.getUserId(), object.getState().name());
			return CommonVOUtil.success("success");
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return CommonVOUtil.error("order error");
		}
	}

	/**
	 * 增加延期次数
	 */
	@RequestMapping("/addExpand")
	public CommonVO addExpand(String id, String userId) {
		try {
			OrderValueObject object = orderCmdService.addExpand(id, userId, ExpandType.ADMIN);
			orderQueryService.updateLoanOrderExpand(object);
			return CommonVOUtil.success("success");
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return CommonVOUtil.error("order error");
		}
	}

	/**
	 * 补缴延期费
	 */
	@RequestMapping("/addExpandFee")
	public CommonVO addExpandFee(String id, String userId, Double amount) {
		try {
			if (amount == null || amount <= 0) {
				return CommonVOUtil.success("success");
			}

			OrderValueObject object = orderCmdService.changeExpandFee(id, userId, amount);
			orderQueryService.updateLoanOrderExpand(object);
			return CommonVOUtil.success("success");
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return CommonVOUtil.error("order error");
		}
	}
}
