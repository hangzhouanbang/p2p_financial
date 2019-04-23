package com.anbang.p2p.web.controller;

import java.util.HashMap;
import java.util.Map;

import com.anbang.p2p.cqrs.q.dbo.UserDbo;
import com.anbang.p2p.cqrs.q.service.UserService;
import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.web.vo.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.cqrs.q.dbo.LoanOrder;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.service.OrderQueryService;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.web.vo.CommonVO;
import com.anbang.p2p.web.vo.LoanOrderQueryVO;
import com.highto.framework.web.page.ListPage;

@CrossOrigin
@RestController
@RequestMapping("/usermanager")
public class UserManagerController {

	@Autowired
	private UserAuthQueryService userAuthQueryService;

	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private UserService userService;

	/**
	 * 查询用户
	 */
	@RequestMapping("/user_query")
	public CommonVO queryUser(@RequestParam(required = true, defaultValue = "1") int page,
							  @RequestParam(required = true, defaultValue = "20") int size,
							  UserQuery userQuery) {
		CommonVO vo = new CommonVO();
		ListPage listPage = userAuthQueryService.findUserDbo(page, size, userQuery);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 用户身份证信息
	 */
	@RequestMapping("/user_baseinfo")
	public CommonVO queryUserBaseInfo(String userId) {
		UserBaseInfo baseInfo = userAuthQueryService.findUserBaseInfoByUserId(userId);
		return CommonVOUtil.success( baseInfo, "success");
	}

	/**
	 * 用户基本信息
	 */
	@RequestMapping("/queryUserDbo")
	public CommonVO queryUserDbo(String userId) {
		UserDbo userDbo = userService.getById(userId);
		return CommonVOUtil.success( userDbo, "success");
	}

	/**
	 * 查询用户白条
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
	public CommonVO queryOrderDetail(String orderId) {
		CommonVO vo = new CommonVO();
		LoanOrder loanOrder = orderQueryService.findLoanOrderById(orderId);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("loanOrder", loanOrder);
		return vo;
	}
}
