package com.anbang.p2p.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.plan.bean.BaseLoan;
import com.anbang.p2p.plan.bean.BaseRateOfInterest;
import com.anbang.p2p.plan.bean.UserBaseLoan;
import com.anbang.p2p.plan.bean.UserBaseRateOfInterest;
import com.anbang.p2p.plan.service.BaseRateService;
import com.anbang.p2p.web.vo.CommonVO;

@RestController
@RequestMapping("/base")
public class BaseLoanAndRateController {

	@Autowired
	private BaseRateService baseRateService;

	/**
	 * 查询基础设置
	 */
	@RequestMapping("/baseloan_query")
	public CommonVO queryBaseLoan() {
		CommonVO vo = new CommonVO();
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("baseLimit", BaseLoan.baseLimit);
		data.put("service_charge", BaseLoan.service_charge_rate * 1000);
		data.put("overdue", BaseLoan.overdue / 24 / 60 / 60 / 1000);
		data.put("freeOfInterest", BaseLoan.freeTimeOfInterest / 24 / 60 / 60 / 1000);
		return vo;
	}

	/**
	 * 修改基础设置
	 */
	@RequestMapping("/baseloan_update")
	public CommonVO updateBaseLoan(double baseLimit, double service_charge, long overdue, long freeTimeOfInterest) {
		CommonVO vo = new CommonVO();
		baseRateService.changeBaseLoan(baseLimit, service_charge, overdue, freeTimeOfInterest);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("baseLimit", BaseLoan.baseLimit);
		data.put("service_charge", BaseLoan.service_charge_rate * 1000);
		data.put("overdue", BaseLoan.overdue / 24 / 60 / 60 / 1000);
		data.put("freeOfInterest", BaseLoan.freeTimeOfInterest / 24 / 60 / 60 / 1000);
		return vo;
	}

	/**
	 * 查询利率设置
	 */
	@RequestMapping("/baserateofinterest_query")
	public CommonVO queryBaseRateOfInterest() {
		CommonVO vo = new CommonVO();
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("seven_rate", BaseRateOfInterest.seven_rate * 1000);
		data.put("fifteen_rate", BaseRateOfInterest.fifteen_rate * 1000);
		data.put("thirty_rate", BaseRateOfInterest.thirty_rate * 1000);
		data.put("overdue_rate", BaseRateOfInterest.overdue_rate * 1000);
		return vo;
	}

	/**
	 * 修改利率设置
	 */
	@RequestMapping("/baserateofinterest_update")
	public CommonVO updateBaseRateOfInterest(double seven_rate, double fifteen_rate, double thirty_rate,
			double overdue_rate) {
		CommonVO vo = new CommonVO();
		baseRateService.changeBaseRateOfInterest(seven_rate, fifteen_rate, thirty_rate, overdue_rate);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("seven_rate", BaseRateOfInterest.seven_rate * 1000);
		data.put("fifteen_rate", BaseRateOfInterest.fifteen_rate * 1000);
		data.put("thirty_rate", BaseRateOfInterest.thirty_rate * 1000);
		data.put("overdue_rate", BaseRateOfInterest.overdue_rate * 1000);
		return vo;
	}

	/**
	 * 玩家基本设置
	 */
	@RequestMapping("/user_baseloan_save")
	public CommonVO saveUserBaseLoan(UserBaseLoan loan) {
		CommonVO vo = new CommonVO();
		baseRateService.saveUserBaseLoan(loan);
		return vo;
	}

	/**
	 * 查询玩家基本设置
	 */
	@RequestMapping("/user_baseloan_query")
	public CommonVO queryUserBaseLoan(String userId) {
		CommonVO vo = new CommonVO();
		UserBaseLoan userLoan = baseRateService.findUserBaseLoanByUserId(userId);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("userLoan", userLoan);
		return vo;
	}

	/**
	 * 玩家利率设置
	 */
	@RequestMapping("/user_baserateofinterest_save")
	public CommonVO saveUserBaseRateOfInterest(UserBaseRateOfInterest rate) {
		CommonVO vo = new CommonVO();
		baseRateService.saveUserBaseRateOfInterest(rate);
		return vo;
	}

	/**
	 * 查询玩家利率设置
	 */
	@RequestMapping("/user_baserateofinterest_query")
	public CommonVO queryUserBaseRateOfInterest(String userId) {
		CommonVO vo = new CommonVO();
		UserBaseRateOfInterest userRate = baseRateService.findUserBaseRateOfInterestByUserId(userId);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("userRate", userRate);
		return vo;
	}
}
