package com.anbang.p2p.web.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.util.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.plan.bean.BaseLoan;
import com.anbang.p2p.plan.bean.UserBaseLoan;
import com.anbang.p2p.plan.service.BaseRateService;
import com.anbang.p2p.web.vo.CommonVO;

@CrossOrigin
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
		data.put("service_charge", BaseLoan.service_charge);
		data.put("expand_charge", BaseLoan.expand_charge);
		data.put("overdue", BaseLoan.overdue / 24 / 60 / 60 / 1000);
		data.put("freeTimeOfInterest", BaseLoan.freeTimeOfInterest / 24 / 60 / 60 / 1000);
		data.put("overdue_rate", BaseLoan.overdue_rate * 1000);
		return vo;
	}

	/**
	 * 修改基础设置
	 */
	@RequestMapping("/baseloan_update")
	public CommonVO updateBaseLoan(double baseLimit, double service_charge, double expand_charge,
								   long overdue, long freeTimeOfInterest, double overdue_rate) {
		overdue = overdue * 24L * 60 * 60 * 1000;
		freeTimeOfInterest = freeTimeOfInterest * 24L * 60 * 60 * 1000;

		BigDecimal rate = new BigDecimal(overdue_rate);
		BigDecimal divide = new BigDecimal(1000);
		overdue_rate = rate.divide(divide).doubleValue();

		CommonVO vo = new CommonVO();
		baseRateService.changeBaseLoan(baseLimit, service_charge, expand_charge, overdue, freeTimeOfInterest, overdue_rate);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("baseLimit", BaseLoan.baseLimit);
		data.put("service_charge", BaseLoan.service_charge);
		data.put("expand_charge", BaseLoan.expand_charge);
		data.put("overdue", BaseLoan.overdue / 24 / 60 / 60 / 1000);
		data.put("freeTimeOfInterest", BaseLoan.freeTimeOfInterest / 24 / 60 / 60 / 1000);
		data.put("overdue_rate", BaseLoan.overdue_rate * 1000);
		return vo;
	}


	/**
	 * ----------------- 玩家个人设置
	 */

	/**
	 * 玩家基本设置
	 */
	@RequestMapping("/user_baseloan_save")
	public CommonVO saveUserBaseLoan(UserBaseLoan loan) {
		if (loan == null || StringUtils.isBlank(loan.getId())) {
			return CommonVOUtil.invalidParam();
		}

		loan.setFreeTimeOfInterest(loan.getFreeTimeOfInterest() * 24L * 60 * 60 * 1000);
		loan.setOverdue(loan.getOverdue() * 24L * 60 * 60 * 1000);

		BigDecimal rate = new BigDecimal(loan.getOverdue_rate());
		BigDecimal divide = new BigDecimal(1000);
		loan.setOverdue_rate(rate.divide(divide).doubleValue());

		CommonVO vo = new CommonVO();
		baseRateService.saveUserBaseLoan(loan);
		return vo;
	}

	/**
	 * 查询玩家基本设置
	 */
	@RequestMapping("/user_baseloan_query")
	public CommonVO queryUserBaseLoan(String userId) {
		UserBaseLoan loan = baseRateService.findUserBaseLoanByUserId(userId);
		Map data = new HashMap();
		if (loan == null) {
			data.put("id", userId);
			data.put("baseLimit", BaseLoan.baseLimit);
			data.put("service_charge", BaseLoan.service_charge);
			data.put("expand_charge", BaseLoan.expand_charge);
			data.put("overdue", BaseLoan.overdue / 24 / 60 / 60 / 1000);
			data.put("freeTimeOfInterest", BaseLoan.freeTimeOfInterest / 24 / 60 / 60 / 1000);
			data.put("overdue_rate", BaseLoan.overdue_rate * 1000);
			return CommonVOUtil.success(data, "success");
		} else {
			data.put("id", userId);
			data.put("baseLimit", loan.getBaseLimit());
			data.put("service_charge", loan.getService_charge());
			data.put("expand_charge", loan.getExpand_charge());
			data.put("overdue", loan.getOverdue() / 24 / 60 / 60 / 1000);
			data.put("freeTimeOfInterest", loan.getFreeTimeOfInterest() / 24 / 60 / 60 / 1000);
			data.put("overdue_rate", loan.getOverdue_rate() * 1000);
			return CommonVOUtil.success(data, "success");
		}
	}
}
