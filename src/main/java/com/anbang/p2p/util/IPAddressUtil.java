package com.anbang.p2p.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.conf.AliyunConfig;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class IPAddressUtil {

    public static String getIPAddress (String loginIP) {
        String host = "http://ipquery.market.alicloudapi.com";
        String path = "/query";
        String appcode = AliyunConfig.IP_APPCODE;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("ip", loginIP);

        try {
            HttpResponse response = HttpUtil.doGet(host, path, headers, querys);
            String entity = EntityUtils.toString(response.getEntity());
            Map map = JSON.parseObject(entity, Map.class);
            JSONObject data = (JSONObject) map.get("data");
            String prov = (String)data.get("prov");
            String city = (String)data.get("city");
            String isp = (String)data.get("isp");
            return prov + city + isp;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

//    public static void main(String[] args) {
//        String ipAddress = getIPAddress("183.156.9.95");
//        System.out.println(ipAddress);
//    }

}
