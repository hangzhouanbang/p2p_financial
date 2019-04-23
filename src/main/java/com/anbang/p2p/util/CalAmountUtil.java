package com.anbang.p2p.util;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.q.dbo.LoanOrder;
import com.anbang.p2p.web.vo.LoanOrderVO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 计算利息等
 */
public class CalAmountUtil {
    public static void shouldRepayAmount(LoanOrder order, long currentTime) {
        Map map = new HashMap();

        if (!OrderState.collection.equals(order.getState())){
            return ;
        }
        if (currentTime < order.getDeliverTime()) {
            return;
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

            BigDecimal interest = b_amount.multiply(b_overdue_rate.multiply(b_day));

            order.setOverdueTime(day);
            order.setInterest(interest.doubleValue());
            order.setInterest(amount.add(interest).doubleValue());
            return ;
        } else {
            return;
        }
    }
}
