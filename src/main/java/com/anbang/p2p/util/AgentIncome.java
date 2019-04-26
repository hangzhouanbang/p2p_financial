package com.anbang.p2p.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.conf.AgentConfig;
import javafx.util.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 代收
 */
public class AgentIncome {
    private static final String host = "http://jhpay168.com";
    private static final String path = "/pay";
    private static final String query_path = "/pay/query";
    private static final String merchant = "1916232533";

    private static final String notify_url = "http://47.91.219.7:2020/agentNotifyInfo/incomeNotify";
    private static final String return_url = "http://47.91.219.7/p2p/#/paycallback";
//    private static final String return_url = "index.html";

    /**
     *  获取收款url
     */
    public static Map income (double amount, String order_no, String pay_code) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String order_time = simpleDateFormat.format(new Date());
        String signStr = String.format("amount=%s&merchant=%s&notify_url=%s&order_no=%s&order_time=%s&pay_code=%s&return_url=%s&key=%s",
                amount, merchant, notify_url, order_no, order_time, pay_code, return_url, AgentConfig.REPAY_KEY);
        String sign = MD5Utils.getMD5(signStr, "utf-8");

        Map<String, String> querys = new HashMap();
        querys.put("merchant", merchant);
        querys.put("amount", String.valueOf(amount));
        querys.put("pay_code", pay_code);
        querys.put("order_no", order_no);
        querys.put("notify_url", notify_url);
        querys.put("return_url", return_url);
//        querys.put("json", "json");
        querys.put("order_time", order_time);
        querys.put("sign", sign);

        Map result = new HashMap();
        result.put("url", host + path);
        result.put("params" , querys);

//        String josn = HttpUtil.httpForm(host+ path, querys);
//        System.out.println(josn);

        return result;
    }

    /**
     *  获取收款结果
     */
    public static JSONObject queryIncome (String out_order_no) {
        String signStr = String.format("merchant=%s&out_order_no=%s&key=%s",
                merchant, out_order_no, AgentConfig.REPAY_KEY);
        String sign = MD5Utils.getMD5(signStr, "utf-8");

        Map headers = new HashMap();
        Map<String, String> querys = new HashMap();
        querys.put("merchant", merchant);
        querys.put("out_order_no", out_order_no);
        querys.put("sign", sign);

        JSONObject json = new JSONObject();

        try {
            HttpResponse response = HttpUtil.doGet(host, query_path, headers, querys);
            String entity = EntityUtils.toString(response.getEntity());
            json = JSON.parseObject(entity);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("payerror-------->" + JSON.toJSONString(querys));
        System.out.println("payerror-------->" + json);

        return json;
}

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(income(0.15, "0028", "alipay")));

//        System.out.println(queryIncome("5cb9d1b8bf89a21bfe8b45c6"));
        System.out.println("5cb9dc7abf89a2283ad3b3a0".length());
    }

}
