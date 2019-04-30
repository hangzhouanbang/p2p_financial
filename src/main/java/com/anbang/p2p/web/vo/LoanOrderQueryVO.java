package com.anbang.p2p.web.vo;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class LoanOrderQueryVO {
	private String userId;
	private String phone;
	private String realName;
	private OrderState state;

	private Long nowTime;	//用于和应还时间比较maxLimitTime

	private String id;	//卡密id
	private Boolean export; //是否导出
	private Long startTime; //逾期范围
	private Long endTime; //逾期范围

	private Integer startDay; //逾期天数
	private Integer endDay; //逾期天数

	private String createTimeSort;


	public Sort getSort() {
		List<Sort.Order> orderList = new ArrayList<>();
		if ("ASC".equals(createTimeSort)) {
			orderList.add(new Sort.Order(Sort.Direction.ASC, "createTime"));
		} else if ("DESC".equals(createTimeSort)) {
			orderList.add(new Sort.Order(Sort.Direction.DESC, "createTime"));
		}
		if (!orderList.isEmpty()) {
			Sort sort = new Sort(orderList);
			return sort;
		}
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "createTime"));
		return sort;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	public Long getNowTime() {
		return nowTime;
	}

	public void setNowTime(Long nowTime) {
		this.nowTime = nowTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getExport() {
		return export;
	}

	public void setExport(Boolean export) {
		this.export = export;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getCreateTimeSort() {
		return createTimeSort;
	}

	public void setCreateTimeSort(String createTimeSort) {
		this.createTimeSort = createTimeSort;
	}

	public Integer getStartDay() {
		return startDay;
	}

	public void setStartDay(Integer startDay) {
		this.startDay = startDay;
	}

	public Integer getEndDay() {
		return endDay;
	}

	public void setEndDay(Integer endDay) {
		this.endDay = endDay;
	}
}
