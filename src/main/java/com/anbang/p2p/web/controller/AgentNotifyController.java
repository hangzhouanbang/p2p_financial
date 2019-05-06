package com.anbang.p2p.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.conf.XinyanConfig;
import com.anbang.p2p.constants.CommonRecordState;
import com.anbang.p2p.constants.ExpandType;
import com.anbang.p2p.constants.PaymentType;
import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;
import com.anbang.p2p.cqrs.q.dbo.RefundInfo;
import com.anbang.p2p.cqrs.q.service.OrderQueryService;
import com.anbang.p2p.cqrs.q.service.RefundInfoService;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.plan.bean.MobileVerify;
import com.anbang.p2p.plan.bean.ShoppingVerify;
import com.anbang.p2p.plan.bean.VerifyRecord;
import com.anbang.p2p.plan.service.RiskService;
import com.anbang.p2p.plan.service.VerifyRecordService;
import com.anbang.p2p.util.AgentIncome;
import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.util.MD5Utils;
import com.anbang.p2p.util.checkservice.MobileServiceUtil;
import com.anbang.p2p.web.vo.CommonVO;
import com.anbang.p2p.web.vo.XinyanParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 代收代付回调
 */
@CrossOrigin
@RestController
@RequestMapping("/agentNotifyInfo")
public class AgentNotifyController {

    @Autowired
    private OrderCmdService orderCmdService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private RefundInfoService refundInfoService;

    @Autowired
    private VerifyRecordService verifyRecordService;

    @Autowired
    private RiskService riskService;

    @Autowired
    private UserAuthQueryService userAuthQueryService;

    @RequestMapping("/incomeNotify")
    public String incomeNotify(String merchant, Double amount, String sys_order_no, String out_order_no, String order_time, String sign){
        JSONObject object = AgentIncome.queryIncome(out_order_no);
        System.out.println("收款回调>>>>>>>>>>>>>>>>>>>>>>>" + JSON.toJSONString(object));
        String status = object.getString("status");
        String respCode = object.getString("respCode");
        if ("0000".equals(respCode) && "1".equals(status)) {

            RefundInfo refundInfo = refundInfoService.getById(out_order_no);
            refundInfo.setStatus(CommonRecordState.SUCCESS);
            refundInfoService.save(refundInfo);

            OrderValueObject orderValueObject = null;
            try {

                if (PaymentType.expand.equals(refundInfo.getPaymentType())) {  //客户延期
                    orderValueObject = orderCmdService.addExpand(refundInfo.getLoanOrderId(), refundInfo.getUserId(), ExpandType.CLIENT);
                    orderQueryService.updateLoanOrderExpand(orderValueObject);
                } else { // 付款
                    orderValueObject = orderCmdService.cleanOrder(refundInfo.getUserId(), amount, System.currentTimeMillis());
                    orderQueryService.updateLoanOrder(orderValueObject);
                }
            } catch (OrderNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalOperationException e) {
                e.printStackTrace();
            }
            return "success";
        }
        RefundInfo refundInfo = refundInfoService.getById(out_order_no);
        refundInfo.setStatus(CommonRecordState.ERROR);
        refundInfoService.save(refundInfo);
        return "success";
    }

    @RequestMapping("/incomeCheck")
    public String incomeCheck(String out_order_no){
        RefundInfo refundInfo = refundInfoService.getById(out_order_no);
        if (refundInfo != null && CommonRecordState.SUCCESS.equals(refundInfo.getStatus())) {
            return "success";
        }
        return "wait";
    }

    /**
     * 云慧眼身份认证回调
     */
    @RequestMapping("/verifyCallback")
    public String verifyCallback(String partner_order_id, String result_auth, String result_status, String errorcode, String message) {
        System.out.println(String.format("有盾身份证识别回调：%s|%s|%s|%s|%s", partner_order_id, result_auth, result_status, errorcode,message));

        if (StringUtils.isBlank(partner_order_id)) {
            return "error";
        }

        VerifyRecord record = verifyRecordService.getById(partner_order_id);
        if (record != null) {
            // 人脸认证成功
            if ("T".equals(result_auth)) {
                verifyRecordService.updateStateAndCause(record.getId(), CommonRecordState.SUCCESS, result_auth, message);

                // 查询并保存
                try {
                    riskService.orderQuery(partner_order_id, record.getUerId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                verifyRecordService.updateStateAndCause(record.getId(), CommonRecordState.ERROR, result_auth, message);
            }
        }
        return "success";
    }

    @RequestMapping("/incomeResult")
    public CommonVO incomeNotify(String user_order_no){
        RefundInfo refundInfo = refundInfoService.getById(user_order_no);

        if (refundInfo!= null && CommonRecordState.SUCCESS.equals(refundInfo.getStatus())){
            return CommonVOUtil.success("success");
        }

        if (refundInfo!= null && CommonRecordState.ERROR.equals(refundInfo.getStatus())) {
            return CommonVOUtil.success("error");
        }
        return CommonVOUtil.success("waiting");
    }

    /**
     * 新颜回调
     */
    @RequestMapping(value = "/checkNotify", produces = "application/json;charset=UTF-8")
    public String checkNotify(@RequestBody XinyanParam bean){
        if (!XinyanConfig.ApiUser.equals(bean.getApiUser())) {
            return "success";
        }

        String taskId = bean.getTaskId();

        String signStr =bean.getApiUser() + XinyanConfig.AccessKey + bean.getToken();
        String sign = MD5Utils.getMD5(signStr, "utf-8");
        if (!sign.equals(bean.getApiEnc())) {
            System.out.println("验签未通过的新颜回调-------->" + JSON.toJSONString(bean));
            return "success";
        }

        String userId = taskId.substring(0, taskId.indexOf("_"));
        if ("carrier".equals(bean.getApiName())) {
            MobileVerify mobileVerify = new MobileVerify();
            mobileVerify.setId(userId);
            mobileVerify.setState(CommonRecordState.SUCCESS);
            mobileVerify.setToken(bean.getToken());
            mobileVerify.setTaskId(taskId);
            mobileVerify.setCreateTime(System.currentTimeMillis());
            userAuthQueryService.saveMobileVerify(mobileVerify);
            return "success";
        }
        if ("taobaoweb".equals(bean.getApiName())) {
            ShoppingVerify shoppingVerify = new ShoppingVerify();
            shoppingVerify.setId(userId);
            shoppingVerify.setState(CommonRecordState.SUCCESS);
            shoppingVerify.setToken(bean.getToken());
            shoppingVerify.setTaskId(taskId);
            shoppingVerify.setCreateTime(System.currentTimeMillis());
            userAuthQueryService.saveShoppingVerify(shoppingVerify);
            return "success";
        }

        System.out.println("验签未通过的新颜回调-------->" + JSON.toJSONString(bean));
        return "success";
    }


    // 立木征信
    //    @RequestMapping("/contactNotify")
//    public String contactNotify(String uid, String bizType, String code, String msg, String token){
//
//        if ("0000".equals(code)) {
//            try {
//                String json = mobileServiceUtil.query_report(token);
//                userAuthQueryService.updateStateAndData(uid, CommonRecordState.SUCCESS, json);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println("运营商验证回调>>>>>>>>>>" + String.format("%s|%s|%s|%s|%s",uid,bizType,code,msg,token));
//        return "success";
//    }
}
