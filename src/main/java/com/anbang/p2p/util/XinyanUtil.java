package com.anbang.p2p.util;

import com.anbang.p2p.conf.XinyanConfig;

import java.util.Date;

/**
 * @Description:
 */
public class XinyanUtil {
//    static final String path = "https://qz.xinyan.com/h5?apiUser=%s&timeMark=%s&apiEnc=%s&apiName=%s&taskId=%s&jumpUrl=%s";
    static final String path = "https://qz.xinyan.com/h5/%s/%s/%s/%s/%s?jumpUrl=%s";
    static final String jumpUrl = "https://www.baidu.com/";
    static final String reportPath = "https://qz.xinyan.com/#/portraitCarrier?apiUser=%s&apiEnc=%s&token=%s";



    public static String getCarrierUrl(String taskId){
        String apiUser = XinyanConfig.ApiUser;
        String timeMark = TimeUtils.getStringDate(new Date());
        String apiKey = XinyanConfig.AccessKey;
        String apiName = "carrier";

        String signStr = apiUser + timeMark + apiName + taskId + apiKey;
        String apiEnc = MD5Utils.getMD5(signStr, "utf-8");

        String url = String.format(path, apiUser, apiEnc, timeMark, apiName, taskId, jumpUrl);
        return url;
    }

    public static String getTaobaowebUrl(String taskId){
        String apiUser = XinyanConfig.ApiUser;
        String timeMark = TimeUtils.getStringDate(new Date());
        String apiKey = XinyanConfig.AccessKey;
        String apiName = "taobaoweb";

        String signStr = apiUser + timeMark + apiName + taskId + apiKey;
        String apiEnc = MD5Utils.getMD5(signStr, "utf-8");

        String url = String.format(path, apiUser, apiEnc, timeMark, apiName, taskId, jumpUrl);
        return url;
    }

    public static String getReport(String token) {
        String apiUser = XinyanConfig.ApiUser;
        String apiKey = XinyanConfig.AccessKey;

        String signStr = apiUser + apiKey;
        String apiEnc = MD5Utils.getMD5(signStr, "utf-8");

        String url = String.format(reportPath, apiUser, apiEnc, token);
        return url;
    }

    public static void main(String[] args) {
//        System.out.println(getCarrierUrl("003"));
//        System.out.println(getTaobaowebUrl("004"));


        System.out.println(getReport("123"));
    }
}
