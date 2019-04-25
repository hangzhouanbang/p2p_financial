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
        if (currentTime < order.getDeliverTime()) {
            return;
        }
        // 如果需要精确计算，非要用String来够造BigDecimal不可
        // 逾期应还:到期应还金额+逾期天数X本金X逾期利率
        if (currentTime > order.getMaxLimitTime()) {
            // 借款金额
            BigDecimal b_amount = new BigDecimal(Double.toString(order.getAmount()));

            // 逾期时间
            long day = (currentTime - order.getMaxLimitTime()) / 1000 / 60 / 60 / 24;
            BigDecimal b_day = new BigDecimal(Long.toString(day));

            // 逾期利率
            BigDecimal b_overdue_rate = new BigDecimal(Double.toString(order.getOverdue_rate()));

            // 逾期费用
            BigDecimal interest = b_amount.multiply(b_overdue_rate.multiply(b_day));

            order.setOverdueDay((int)day);
            order.setInterest(interest.doubleValue());
            order.setShouldRepayAmount(b_amount.add(interest).doubleValue());
            return ;
        } else {
            order.setOverdueDay(0);
            order.setInterest(0);
            order.setShouldRepayAmount(order.getAmount());
            return;
        }
    }
}
