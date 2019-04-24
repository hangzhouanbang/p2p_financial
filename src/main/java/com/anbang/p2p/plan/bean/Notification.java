package com.anbang.p2p.plan.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 */
public class Notification {
    private static Map<String, String> notification = new HashMap<>();

    static {
        notification.put("applySuccess","尊敬的用户您好，您申请的白条提现已经到账成功，请查看收款账户！");
        notification.put("repaySuccess","您的账单已结清！");
        notification.put("repayFront","尊敬的用户您好，距离您的还款日还剩余1天时间，请提前安排好还款资金，以便按时还款！");
        notification.put("repayDay","尊敬的用户您好，今天是您的还款日，为保证良好的个人征信，务必按时还款！");
        notification.put("beyond1","尊敬的用户您好，您的待还款项已逾期1天，为保证良好的个人征信，务必及时还款！");
        notification.put("beyond3","尊敬的用户您好，您的待还款项已逾期3天，为保证良好的个人征信，务必及时还款！");
        notification.put("beyond7","尊敬的用户您好，您的待还款项已逾期7天，为保证良好的个人征信，务必及时还款！");
        notification.put("beyond15","尊敬的用户您好，您的待还款项已逾期15天，为保证良好的个人征信，务必及时还款！");
    }

    public static void update(Map<String, String> map){
        notification = map;
    }
    public static Map getMap(){
        return new HashMap(notification);
    }

}
