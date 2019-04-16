package com.anbang.p2p.util;

import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.conf.VerifyConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Date;

import static com.anbang.p2p.util.DataServiceUtil.CHASET_UTF_8;

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
        String encrypt = EncryptUtils.aesEncrypt(params, "4c43a8be85b64563a32244db9caf8454");
        String url = "https://static.udcredit.com/id/v43/index.html?apiparams=" + encrypt;
        return url;
    }

    /**
     * Http请求
     */
    public static JSONObject doHttpRequest(String url, JSONObject reqJson) {
        CloseableHttpClient client = HttpClients.createDefault();
        //设置传入参数
        StringEntity entity = new StringEntity(reqJson.toJSONString(), CHASET_UTF_8);
        entity.setContentEncoding(CHASET_UTF_8);
        entity.setContentType("application/json");
        System.out.println(url);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);

        HttpResponse resp = null;
        try {
            resp = client.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == 200) {
                HttpEntity he = resp.getEntity();
                String respContent = EntityUtils.toString(he, CHASET_UTF_8);
                return (JSONObject) JSONObject.parse(respContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
