package com.anbang.p2p.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

@RestController
@RequestMapping("/ordermanager")
public class OrderManagerController {

	@Autowired
	private OrderCmdService orderCmdService;

	@Autowired
	private OrderQueryService orderQueryService;

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
			orderQueryService.updateLoanOrder(orderValueObject, null);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		return vo;
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
			orderQueryService.updateLoanOrder(orderValueObject, null);
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
		query.setState(OrderState.refund);
		int size = 2000;
		long amount = orderQueryService.countAmount(query);
		long pageCount = amount % size > 0 ? amount / size + 1 : amount / size;
		for (int page = 1; page <= pageCount; page++) {
			List<LoanOrder> orderList = orderQueryService.findLoanOrderList(page, size, query);
			try {
				for (LoanOrder order : orderList) {
					OrderValueObject orderValueObject = orderCmdService.changeOrderStateToOverdue(order.getUserId());
					orderQueryService.updateLoanOrder(orderValueObject, null);
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
					orderQueryService.updateLoanOrder(orderValueObject, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
