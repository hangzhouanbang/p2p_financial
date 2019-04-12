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
 * 四要素验证
 */
public class BankInfoCheckUtil {

    /**
     *
     * @param accName 姓名
     * @param cardPhone 银行卡预留手机号
     * @param certificateNo 身份证号
     * @param cardNo 银行卡卡号
     * @return
     */
    public static boolean getRiskInfo(String accName, String cardPhone, String certificateNo, String cardNo){
        String host = "https://way.jd.com";
        String path = "YOUYU365/keyelement";

        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = new HashMap<String, String>();

        querys.put("appkey", VerifyConfig.BANK_APP_KEY);
        querys.put("accName", accName);
        querys.put("cardPhone", cardPhone);
        querys.put("certificateNo", certificateNo);
        querys.put("cardNo", cardNo);

        try {
            HttpResponse response = HttpUtil.doGet(host, path, headers, querys);
            String entity = EntityUtils.toString(response.getEntity());
            JSONObject json = JSON.parseObject(entity);

            String code = json.getString("code");
            String message = json.getString("msg");
            if (!"10000".equals(code)) {
                throw new CheckAPIException("getRiskInfo--->>>code:" + code + " message:" + message);
            }

            String respCode = json.getJSONObject("result").getString("respCode");
            if ("000000".equals(respCode)) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("CheckAPIException ----->>>>>");
            System.out.println("accName:" + accName);
            System.out.println("cardPhone:" + cardPhone);
            System.out.println("certificateNo:" + certificateNo);
            System.out.println("cardNo:" + cardNo);
            e.printStackTrace();
        }
        return false;
    }
}
