package com.anbang.p2p.util;

public class RiskUtil {
    /**
     * 生成MD5签名
     */
    public static String getMD5Sign (String pub_key, String partner_order_id, String sign_time, String security_key) {
        String signStr = String.format("pub_key=%s|partner_order_id=%s|sign_time=%s|security_key=%s", pub_key, partner_order_id, sign_time, security_key);
        return MD5Utils.MD5Encrpytion(signStr);
    }
}
