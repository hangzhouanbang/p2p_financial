package com.anbang.p2p.cqrs.q.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.q.dao.LoanOrderDao;
import com.anbang.p2p.cqrs.q.dao.OrderContractDao;
import com.anbang.p2p.cqrs.q.dao.RefundInfoDao;
import com.anbang.p2p.cqrs.q.dbo.LoanOrder;
import com.anbang.p2p.cqrs.q.dbo.OrderContract;
import com.anbang.p2p.cqrs.q.dbo.RefundInfo;
import com.anbang.p2p.cqrs.q.dbo.UserBankCardInfo;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserDbo;
import com.anbang.p2p.web.vo.LoanOrderQueryVO;
import com.highto.framework.web.page.ListPage;

@Service
public class OrderQueryService {

	@Autowired
	private LoanOrderDao loanOrderDao;

	@Autowired
	private OrderContractDao orderContractDao;

	@Autowired
	private RefundInfoDao refundInfoDao;

	public LoanOrder saveLoanOrder(OrderValueObject orderValueObject, UserDbo user, OrderContract contract,
			UserBaseInfo baseInfo) {
		LoanOrder loanOrder = new LoanOrder(orderValueObject, user, contract, baseInfo);
		loanOrderDao.save(loanOrder);
		return loanOrder;
	}

	public void updateLoanOrder(OrderValueObject orderValueObject, UserBankCardInfo cardInfo) {
		LoanOrder loanOrder = loanOrderDao.findById(orderValueObject.getId());
		loanOrder.setState(orderValueObject.getState());
		loanOrder.setDeliverTime(orderValueObject.getDeliverTime());
		loanOrder.setRealRefundAmount(orderValueObject.getRealRefundAmount());
		loanOrder.setRefundTime(orderValueObject.getRefundTime());
		loanOrderDao.save(loanOrder);

		// 还款
		if (orderValueObject.getState().equals(OrderState.clean)) {
			RefundInfo info = new RefundInfo(orderValueObject, loanOrder, cardInfo.getBankCardNo());
			refundInfoDao.save(info);
		}
	}

	/**
	 * 查询应还款
	 */
	public double queryRefundAmount(String userId, long currentTime)
			throws OrderNotFoundException, IllegalOperationException {
		LoanOrder order = loanOrderDao.findByUserIdAndState(userId, OrderState.refund);
		if (order == null) {
			throw new OrderNotFoundException();
		}
		if (!order.getState().equals(OrderState.refund)) {
			throw new IllegalOperationException();
		}
		if (currentTime < order.getDeliverTime()) {
			throw new IllegalOperationException();
		}
		// 如果需要精确计算，非要用String来够造BigDecimal不可
		BigDecimal b_amount = new BigDecimal(Double.toString(order.getAmount()));
		BigDecimal b_rate = new BigDecimal(Double.toString(order.getRate()));
		BigDecimal b_freeOfInterest = new BigDecimal(
				Long.toString(order.getFreeTimeOfInterest() / 24 / 60 / 60 / 1000));
		if (currentTime > order.getMaxLimitTime()) {
			// 逾期应还:到期应还金额+逾期天数X本金X逾期利率
			long limitDay = (order.getMaxLimitTime() - order.getDeliverTime()) / 1000 / 60 / 60 / 24;
			BigDecimal b_limitDay = new BigDecimal(Long.toString(limitDay));
			BigDecimal limit_num = b_limitDay.subtract(b_freeOfInterest);
			BigDecimal amount = b_amount.add(b_amount.multiply(b_rate.multiply(limit_num)));

			long day = (currentTime - order.getMaxLimitTime()) / 1000 / 60 / 60 / 24;
			BigDecimal b_day = new BigDecimal(Long.toString(day));
			BigDecimal b_overdue_rate = new BigDecimal(Double.toString(order.getOverdue_rate()));
			return amount.add(b_amount.multiply(b_overdue_rate.multiply(b_day))).doubleValue();
		} else {
			// 到期应还:本金X相应借款天数利率X（借款天数-免息天数）+本金
			long day = (currentTime - order.getDeliverTime()) / 1000 / 60 / 60 / 24;
			BigDecimal b_day = new BigDecimal(Long.toString(day));
			BigDecimal num = b_day.subtract(b_freeOfInterest);
			return b_amount.add(b_amount.multiply(b_rate.multiply(num))).doubleValue();
		}
	}

	public LoanOrder findLoanOrderByUserIdAndState(String userId, OrderState state) {
		return loanOrderDao.findByUserIdAndState(userId, state);
	}

	public LoanOrder findLoanOrderById(String orderId) {
		return loanOrderDao.findById(orderId);
	}

	public long countAmount(LoanOrderQueryVO query) {
		return loanOrderDao.getAmount(query);
	}

	public List<LoanOrder> findLoanOrderList(int page, int size, LoanOrderQueryVO query) {
		return loanOrderDao.find(page, size, query);
	}

	public ListPage findLoanOrder(int page, int size, LoanOrderQueryVO query) {
		int amount = (int) loanOrderDao.getAmount(query);
		List<LoanOrder> orderList = loanOrderDao.find(page, size, query);
		return new ListPage(orderList, page, size, amount);
	}

	public OrderContract findOrderContractById(String contractId) {
		return orderContractDao.findById(contractId);
	}
}
