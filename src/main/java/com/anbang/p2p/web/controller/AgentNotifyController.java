package com.anbang.p2p.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.constants.CommonRecordState;
import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;
import com.anbang.p2p.cqrs.q.dbo.RefundInfo;
import com.anbang.p2p.cqrs.q.service.OrderQueryService;
import com.anbang.p2p.cqrs.q.service.RefundInfoService;
import com.anbang.p2p.util.AgentIncome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 代收代付回调
 */
@CrossOrigin
@RestController
@RequestMapping("/agentNotify")
public class AgentNotifyController {

    @Autowired
    private OrderCmdService orderCmdService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private RefundInfoService refundInfoService;

    @RequestMapping("incomeNotify")
    public String incomeNotify(String merchant, Double amount, String sys_order_no, String out_order_no, String order_time, String sign){
        JSONObject object = AgentIncome.queryIncome(out_order_no);
        String status = object.getString("status");
        if ("1".equals(status)) {

            RefundInfo refundInfo = refundInfoService.getById(out_order_no);
            refundInfo.setStatus(CommonRecordState.SUCCESS);
            refundInfoService.save(refundInfo);

            OrderValueObject orderValueObject = null;
            try {
                orderValueObject = orderCmdService.cleanOrder(refundInfo.getUserId(), amount, System.currentTimeMillis());
            } catch (OrderNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalOperationException e) {
                e.printStackTrace();
            }
            orderQueryService.updateLoanOrder(orderValueObject);
        } else {
            RefundInfo refundInfo = refundInfoService.getById(out_order_no);
            refundInfo.setStatus(CommonRecordState.ERROR);
            refundInfoService.save(refundInfo);
        }
        return "success";
    }

    @RequestMapping("incomeNotify")
    public String incomeNotify(String user_order_no){
        JSONObject object = AgentIncome.queryIncome(user_order_no);
        String status = object.getString("status");
        if ("1".equals(status)) {

        }
        return "success";
    }
}
