package com.anbang.p2p.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.conf.XinyanConfig;
import com.anbang.p2p.util.xinyan.HttpUtils;
import com.anbang.p2p.util.xinyan.RsaCodingUtil;
import com.anbang.p2p.util.xinyan.SecurityUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 */
public class XinyanUtil {
//    static final String path = "https://qz.xinyan.com/h5?apiUser=%s&timeMark=%s&apiEnc=%s&apiName=%s&taskId=%s&jumpUrl=%s";
    static final String path = "https://qz.xinyan.com/h5/%s/%s/%s/%s/%s?jumpUrl=%s";
    static final String jumpUrl = "http://47.91.219.7/p2p/#/yunyingshang";
    static final String reportPath = "https://qz.xinyan.com/#/portraitCarrier?apiUser=%s&apiEnc=%s&token=%s";
    static final String queryPath = "https://qz.xinyan.com/api/user/data?apiUser=%s&apiEnc=%s&token=%s";

    static String test_apiuser = "8150716192";
    static String test_key = "f275fde017e44bb29f79f186dcfe3422";

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

    public static String getReportUrl(String token) {
        String apiUser = XinyanConfig.ApiUser;
        String apiKey = XinyanConfig.AccessKey;

        String signStr = apiUser + apiKey;
        String apiEnc = MD5Utils.getMD5(signStr, "utf-8");

        String url = String.format(reportPath, apiUser, apiEnc, token);
        return url;
    }

    public static String getQueryUrl(String token) {
        String apiUser = XinyanConfig.ApiUser;
        String apiKey = XinyanConfig.AccessKey;

        String signStr = apiUser + apiKey;
        String apiEnc = MD5Utils.getMD5(signStr, "utf-8");

        String url = String.format(queryPath, apiUser, apiEnc, token);
        return url;
    }

    /**
     * 全景雷达
     */
    public static String getLeida(String trans_id, String id_no, String id_name,String phone_no) {
        /** 1、 商户号 **/
        String member_id = XinyanConfig.ApiUser;
        /** 2、终端号 **/
        String terminal_id = XinyanConfig.keyNo;
        /** 3、请求地址 **/
        String url = "https://api.xinyan.com/product/radar/v3/report";
        Map<String, String> headers = new HashMap<>();
        String PostString = null;

        String versions = "1.3.0";

        id_no = MD5Utils.encode(id_no.trim());
        id_name = MD5Utils.encode(id_name.trim());
        phone_no = MD5Utils.encode(phone_no.trim());

        String trade_date = TimeUtils.getStringDate(new Date());// 订单日期

        String XmlOrJson = "";
        /** 组装参数 **/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("member_id", member_id);
        jsonObject.put("terminal_id", terminal_id);
        jsonObject.put("trade_date", trade_date);
        jsonObject.put("trans_id", trans_id);
        jsonObject.put("phone_no", phone_no);
        jsonObject.put("versions", versions);
        jsonObject.put("encrypt_type", "MD5");// MD5：标准32位小写(推荐) SHA256：标准64位

        jsonObject.put("id_no", id_no);
        jsonObject.put("id_name", id_name);

        XmlOrJson = JSON.toJSONString(jsonObject);

        /** base64 编码 **/
        String base64str = null;
        try {
            base64str = SecurityUtil.Base64Encode(XmlOrJson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        base64str=base64str.replaceAll("\r\n", "");//重要 避免出现换行空格符

        /** rsa加密 **/
//        String pfxpath = "D:/xinyan/keyfile_pri.pfx";// 商户私钥
        String pfxpath = "/data/xinyan/keyfile_pri.pfx";// 商户私钥

        File pfxfile = new File(pfxpath);
        if (!pfxfile.exists()) {
            System.out.println("私钥文件不存在");
            throw new RuntimeException("私钥文件不存在！");
        }
        String pfxpwd = "123456";// 私钥密码

        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, pfxpath, pfxpwd);// 加密数据

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("member_id", member_id);
        params.put("terminal_id", terminal_id);
        params.put("data_type", "json");
        params.put("data_content", data_content);

        PostString = HttpUtils.doPostByForm(url, headers, params);


        /** ================处理返回结果============= **/
        if (PostString.isEmpty()) {// 判断参数是否为空
            throw new RuntimeException("返回数据为空");
        }

        JSONObject object = JSON.parseObject(PostString);
        boolean success = object.getBoolean("success");
        if (false == success) {
            System.out.println("新颜全景雷达查询异常---->" + object.getString("errorCode") + object.getString("errorMsg") );
            System.out.println(String.format("%s|%s|%s|%s", trans_id, id_no, id_name, phone_no));
            return "";
        }
        return object.getString("data");
    }

    /**
     * 新颜探针A
     */
    public static String getTanzhengA(String trans_id, String id_no, String id_name,String phone_no) {
        /** 1、 商户号 **/
        String member_id = XinyanConfig.ApiUser;
        /** 2、终端号 **/
        String terminal_id = XinyanConfig.keyNo;
        /** 3、请求地址 **/
        String url = "https://api.xinyan.com/product/negative/v4/black";
        Map<String, String> headers = new HashMap<>();
        String PostString = null;

        String versions = "1.3.0";

        id_no = MD5Utils.encode(id_no.trim());
        id_name = MD5Utils.encode(id_name.trim());
        phone_no = MD5Utils.encode(phone_no.trim());

        String trade_date = TimeUtils.getStringDate(new Date());// 订单日期

        String XmlOrJson = "";
        /** 组装参数 **/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("member_id", member_id);
        jsonObject.put("terminal_id", terminal_id);
        jsonObject.put("trade_date", trade_date);
        jsonObject.put("trans_id", trans_id);
        jsonObject.put("phone_no", phone_no);
        jsonObject.put("versions", versions);
        jsonObject.put("encrypt_type", "MD5");// MD5：标准32位小写(推荐) SHA256：标准64位

        jsonObject.put("id_no", id_no);
        jsonObject.put("id_name", id_name);

        XmlOrJson = JSON.toJSONString(jsonObject);

        /** base64 编码 **/
        String base64str = null;
        try {
            base64str = SecurityUtil.Base64Encode(XmlOrJson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        base64str=base64str.replaceAll("\r\n", "");//重要 避免出现换行空格符

        /** rsa加密 **/
//        String pfxpath = "D:/xinyan/keyfile_pri.pfx";// 商户私钥
        String pfxpath = "/data/xinyan/keyfile_pri.pfx";// 商户私钥

        File pfxfile = new File(pfxpath);
        if (!pfxfile.exists()) {
            System.out.println("私钥文件不存在");
            throw new RuntimeException("私钥文件不存在！");
        }
        String pfxpwd = "123456";// 私钥密码

        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, pfxpath, pfxpwd);// 加密数据

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("member_id", member_id);
        params.put("terminal_id", terminal_id);
        params.put("data_type", "json");
        params.put("data_content", data_content);

        PostString = HttpUtils.doPostByForm(url, headers, params);


        /** ================处理返回结果============= **/
        if (PostString.isEmpty()) {// 判断参数是否为空
            throw new RuntimeException("返回数据为空");
        }

        JSONObject object = JSON.parseObject(PostString);
        boolean success = object.getBoolean("success");
        if (false == success) {
            System.out.println("新颜探针A查询异常---->" + object.getString("errorCode") + object.getString("errorMsg") );
            System.out.println(String.format("%s|%s|%s|%s", trans_id, id_no, id_name, phone_no));
            return "";
        }
        return object.getString("data");
    }

    /**
     * 贷后雷达
     */
    public static String daiHou(String trans_id, String id_no, String id_name) {
        /** 1、 商户号 **/
        String member_id = XinyanConfig.ApiUser;
        /** 2、终端号 **/
        String terminal_id = XinyanConfig.keyNo;
        /** 3、请求地址 **/
        String url = "https://api.xinyan.com/product/rating/v3/assets";
        Map<String, String> headers = new HashMap<>();
        String PostString = null;

        String versions = "1.3.0";

        id_no = MD5Utils.encode(id_no.trim());
        id_name = MD5Utils.encode(id_name.trim());

        String trade_date = TimeUtils.getStringDate(new Date());// 订单日期

        String XmlOrJson = "";
        /** 组装参数 **/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("member_id", member_id);
        jsonObject.put("terminal_id", terminal_id);
        jsonObject.put("trade_date", trade_date);
        jsonObject.put("trans_id", trans_id);
        jsonObject.put("versions", versions);
        jsonObject.put("encrypt_type", "MD5");// MD5：标准32位小写(推荐) SHA256：标准64位

        jsonObject.put("id_no", id_no);
        jsonObject.put("id_name", id_name);

        XmlOrJson = JSON.toJSONString(jsonObject);

        /** base64 编码 **/
        String base64str = null;
        try {
            base64str = SecurityUtil.Base64Encode(XmlOrJson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        base64str=base64str.replaceAll("\r\n", "");//重要 避免出现换行空格符

        /** rsa加密 **/
        String pfxpath = "D:/xinyan/keyfile_pri.pfx";// 商户私钥
//        String pfxpath = "/data/xinyan/keyfile_pri.pfx";// 商户私钥

        File pfxfile = new File(pfxpath);
        if (!pfxfile.exists()) {
            System.out.println("私钥文件不存在");
            throw new RuntimeException("私钥文件不存在！");
        }
        String pfxpwd = "123456";// 私钥密码

        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, pfxpath, pfxpwd);// 加密数据

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("member_id", member_id);
        params.put("terminal_id", terminal_id);
        params.put("data_type", "json");
        params.put("data_content", data_content);

        PostString = HttpUtils.doPostByForm(url, headers, params);

        System.out.println(PostString);


        /** ================处理返回结果============= **/
        if (PostString.isEmpty()) {// 判断参数是否为空
            throw new RuntimeException("返回数据为空");
        }

        JSONObject object = JSON.parseObject(PostString);
        boolean success = object.getBoolean("success");
        if (false == success) {
            System.out.println("新颜贷后雷达查询异常---->" + object.getString("errorCode") + object.getString("errorMsg") );
            System.out.println(String.format("%s|%s|%s", trans_id, id_no, id_name));
            return "";
        }
        return object.getString("data");
    }


    public static void main(String[] args) {
//        System.out.println(getCarrierUrl("003"));
//        System.out.println(getTaobaowebUrl("004"));


//        System.out.println(getReportUrl("123"));
//        System.out.println(getQueryUrl("123"));


//        getLeida("00001", "411422199408165414", "黄晨光", "15738510522");
//        String json = getTanzhengA("00012", "411422199408165414", "黄晨光", "15738510522");
        String json = daiHou("00014", "411422199408165414", "黄晨光");
        System.out.println(json);
    }
}
