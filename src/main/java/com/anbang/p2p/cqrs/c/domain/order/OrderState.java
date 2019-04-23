package com.anbang.p2p.cqrs.c.domain.order;

public enum OrderState {
	init,	//初始状态，无意义
	check_by_fengkong, // 风控审核
	check_by_admin, // 管理员审核
	wait, // 等待放款
	refuse, // 拒绝放款
	refund, // 等待还款
	clean, // 结清
	overdue, // 逾期
	collection;// 催收中
}
