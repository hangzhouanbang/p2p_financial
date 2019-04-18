package com.anbang.p2p.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.conf.AgentConfig;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 代付
 */
public class AgentPay {
    static final String appid = AgentConfig.PAY_APPID;
    static final String host = "https://transfer.112922.top";
    static final String path = "/index.php/index/index/Transfer";
    static final String qurey_path = "/index.php/index/index/transfer_find";



    public static boolean pay(String recipients, String name, String amount, String order_id) {
        String signStr = String.format("amount=%s&appid=%s&name=%s&order_id=%s&recipients=%s&key=%s",
                amount, appid, name, order_id, recipients, AgentConfig.PAY_KEY);
        String sign = MD5Utils.getMD5(signStr,"utf-8");
        Map headers = new HashMap();
        Map querys = new HashMap();
        querys.put("appid", appid);
        querys.put("recipients", recipients);
        querys.put("name", name);
        querys.put("amount", amount);
        querys.put("order_id", order_id);
        querys.put("sign", sign);

        JSONObject json = new JSONObject();

        try {
            HttpResponse response = HttpUtil.doGet(host, path, headers, querys);
            String entity = EntityUtils.toString(response.getEntity());
            json = JSON.parseObject(entity);

            if ("0".equals(json.getString("code"))) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("payerror-------->" + JSON.toJSONString(querys));
        System.out.println("payerror-------->" + json);
        return false;
    }

    public static void queryPay(String order_id) {
        String signStr = String.format("appid=%s&order_id=%s&key=%s", appid, order_id, AgentConfig.PAY_KEY);
        String sign = MD5Utils.getMD5(signStr,"utf-8");
        Map headers = new HashMap();
        Map querys = new HashMap();
        querys.put("appid", appid);
        querys.put("order_id", order_id);
        querys.put("sign", sign);

        JSONObject json = new JSONObject();

        try {
            HttpResponse response = HttpUtil.doGet(host, qurey_path, headers, querys);
            String entity = EntityUtils.toString(response.getEntity());
            json = JSON.parseObject(entity);

            if ("0".equals(json.getString("code"))) {
//                System.out.println(json);
                return ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("payerror-------->" + JSON.toJSONString(querys));
        System.out.println("payerror-------->" + json);
        return ;
    }

    public static void main(String[] args) {
//        pay("1044380522@qq.com","尹森","0.5","001");
//        System.out.println(AgentConfig.PAY_KEY.length());

        queryPay("001");
    }


}
