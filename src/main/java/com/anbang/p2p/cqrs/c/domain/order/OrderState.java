package com.anbang.p2p.cqrs.c.domain.order;

public enum OrderState {
	check, // 审核中
	loan, // 放款中
	refund, // 待还款
	settlement, // 结清
	overdue, // 逾期
	collection;// 催收中
}
