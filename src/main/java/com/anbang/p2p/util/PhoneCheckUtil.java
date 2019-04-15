package com.anbang.p2p.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.conf.VerifyConfig;
import com.anbang.p2p.exception.CheckAPIException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 手机号二要素验证
 */
public class PhoneCheckUtil {
    /**
     *
     * @param realname 姓名
     * @param phone 银行卡预留手机号
     * @return
     */
    public static boolean checkPhoneInfo(String realname, String phone){
        String host = "https://way.jd.com";
        String path = "/shyxwlkjyxgs/phonetwo";

        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = new HashMap<String, String>();

        querys.put("appkey", VerifyConfig.PHONE_APP_KEY);
        querys.put("realname", realname);
        querys.put("phone", phone);

        try {
            HttpResponse response = HttpUtil.doGet(host, path, headers, querys);
            String entity = EntityUtils.toString(response.getEntity());
            JSONObject json = JSON.parseObject(entity);

            String code = json.getString("code");
            String message = json.getString("msg");
            if (!"10000".equals(code)) {
                throw new CheckAPIException("getRiskInfo--->>>code:" + code + " message:" + message);
            }

            String respCode = json.getJSONObject("result").getString("code");
            if ("200".equals(respCode)) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("CheckAPIException ----->>>>>");
            System.out.println("realname:" + realname);
            System.out.println("phone:" + phone);
            e.printStackTrace();
        }
        return false;
    }
}
