package com.anbang.p2p.cqrs.q.service;

import java.math.BigDecimal;
import java.util.List;

import com.anbang.p2p.constants.Operator;
import com.anbang.p2p.constants.RecordState;
import com.anbang.p2p.cqrs.q.dao.UserDboDao;
import com.anbang.p2p.plan.bean.LoanStateRecord;
import com.anbang.p2p.plan.dao.LoanStateRecordDao;
import com.anbang.p2p.util.CalAmountUtil;
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
	private UserDboDao userDboDao;

	@Autowired
	private OrderContractDao orderContractDao;

	@Autowired
	private LoanStateRecordDao loanStateRecordDao;

	public void updateLoanOrderExpand(OrderValueObject orderValueObject) {
		LoanOrder loanOrder = loanOrderDao.findById(orderValueObject.getId());

		loanOrder.setExpandTotal(orderValueObject.getExpandTotal());
		loanOrder.setExpandTimes(orderValueObject.getExpandTimes());
		loanOrder.setMaxLimitTime(orderValueObject.getMaxLimitTime());

		// 判断现在的订单状态/应还金额
		loanOrder.checkState();
		CalAmountUtil.shouldRepayAmount(loanOrder, System.currentTimeMillis());

		loanOrderDao.save(loanOrder);
		userDboDao.updateUserState(loanOrder.getUserId(), loanOrder.getState().name());
	}


	public void updateLoanOrderState(String id, OrderState orderState, Double amount) {
		loanOrderDao.updateLoanOrderState(id, orderState, amount);
	}

	public void updateLoanOrderAmount(String id, int overdueDay, double interest, double shouldRepayAmount) {
		loanOrderDao.updateLoanOrderAmount(id, overdueDay, interest, shouldRepayAmount);
	}

	public LoanOrder saveLoanOrder(OrderValueObject orderValueObject, UserDbo user, OrderContract contract,
			UserBaseInfo baseInfo, String loginIp, String ipAddress) {
		LoanOrder loanOrder = new LoanOrder(orderValueObject, user, contract, baseInfo);
		loanOrder.setLoginIp(loginIp);
		loanOrder.setIpAddress(ipAddress);

		loanOrder.setShouldRepayAmount(loanOrder.getAmount());
		loanOrderDao.save(loanOrder);

		LoanStateRecord record = new LoanStateRecord();
		record.setOrderId(loanOrder.getId());
		record.setToState(RecordState.applay);
		record.setOperator(Operator.USER);
		record.setCreateTime(System.currentTimeMillis());
		record.setDesc("申请卡密");
		record.setAmount(loanOrder.getAmount());
		loanStateRecordDao.save(record);

		return loanOrder;
	}

	public LoanOrder updateLoanOrder(OrderValueObject orderValueObject) {
		LoanOrder loanOrder = loanOrderDao.findById(orderValueObject.getId());
		loanOrder.setState(orderValueObject.getState());
		loanOrder.setDeliverTime(orderValueObject.getDeliverTime());
		loanOrder.setRealRefundAmount(orderValueObject.getRealRefundAmount());
		loanOrder.setRefundTime(orderValueObject.getRefundTime());
		loanOrderDao.save(loanOrder);

		userDboDao.updateUserState(loanOrder.getUserId(), loanOrder.getState().name());

		return loanOrder;
	}

	// 需要更新还款金额的更新
	public LoanOrder updateLoanOrderPlus(OrderValueObject orderValueObject) {
		LoanOrder loanOrder = loanOrderDao.findById(orderValueObject.getId());
		loanOrder.setState(orderValueObject.getState());
		loanOrder.setDeliverTime(orderValueObject.getDeliverTime());
		loanOrder.setRealRefundAmount(orderValueObject.getRealRefundAmount());
		loanOrder.setRefundTime(orderValueObject.getRefundTime());
		CalAmountUtil.shouldRepayAmount(loanOrder, System.currentTimeMillis());
		loanOrderDao.save(loanOrder);

		userDboDao.updateUserState(loanOrder.getUserId(), loanOrder.getState().name());

		return loanOrder;
	}

	public LoanOrder updateLoanOrderByImport(OrderValueObject orderValueObject, double repayAmount) {
		LoanOrder loanOrder = loanOrderDao.findById(orderValueObject.getId());
		loanOrder.setState(orderValueObject.getState());
		loanOrder.setDeliverTime(orderValueObject.getDeliverTime());
		loanOrder.setRealRefundAmount(repayAmount);
		loanOrder.setRefundTime(orderValueObject.getRefundTime());
//		CalAmountUtil.shouldRepayAmount(loanOrder, System.currentTimeMillis());

		// TODO: 2019/4/23
		loanOrder.setRepayType("催收销账");
		loanOrderDao.save(loanOrder);

		userDboDao.updateUserState(loanOrder.getUserId(), loanOrder.getState().name());

		return loanOrder;
	}

	/**
	 * 查询应还款
	 */
	public double queryRefundAmount(String userId, long currentTime)
			throws OrderNotFoundException, IllegalOperationException {
		LoanOrder order = loanOrderDao.findLastOrderByUserId(userId);
		if (order == null) {
			throw new OrderNotFoundException();
		}
		if (!OrderState.refund.equals(order.getState()) && !OrderState.overdue.equals(order.getState()) &&
				!OrderState.collection.equals(order.getState())){
			throw new IllegalOperationException();
		}
		if (currentTime < order.getDeliverTime()) {
			throw new IllegalOperationException();
		}
		return order.getShouldRepayAmount();
	}

	public LoanOrder findLoanOrderByUserIdAndState(String userId, OrderState state) {
		return loanOrderDao.findByUserIdAndState(userId, state);
	}

	public LoanOrder findLoanOrderById(String orderId) {
		return loanOrderDao.findById(orderId);
	}

	public LoanOrder findLastOrderByUserId(String userId){ return loanOrderDao.findLastOrderByUserId(userId); }

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

	public void saveOrderContract(OrderContract contract) {
		orderContractDao.save(contract);
	}

	public void saveStateRecord(LoanStateRecord loanStateRecord){
		loanStateRecordDao.save(loanStateRecord);
	}

	public List<LoanStateRecord> listStateRecord(String orderId){
		return loanStateRecordDao.list(orderId);
	}
}
