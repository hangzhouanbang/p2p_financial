package com.anbang.p2p.util;

import com.anbang.p2p.conf.VerifyConfig;

import java.util.Date;

public class RiskUtil {
    /**
     * 生成MD5签名
     */
    public static String getMD5Sign (String partner_order_id, String sign_time) {
        String pub_key = VerifyConfig.PUB_KEY;
        String security_key = VerifyConfig.SECURITY_KEY;
        String signStr = String.format("pub_key=%s|partner_order_id=%s|sign_time=%s|security_key=%s", pub_key, partner_order_id, sign_time, security_key);
        return MD5Utils.MD5Encrpytion(signStr);
    }

    /**
     * aes加密
     */
    public static String getAESSign (String params) throws Exception {
        String pub_key = VerifyConfig.PUB_KEY;
        String encrypt = EncryptUtils.aesEncrypt(params, "4c43a8be85b64563a32244db9caf8454");
        String url = "https://static.udcredit.com/id/v43/index.html?apiparams=" + encrypt;
        return url;
    }


}
