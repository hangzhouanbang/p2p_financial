package com.anbang.p2p.util.checkservice;

import com.anbang.p2p.exception.CheckAPIException;
import com.anbang.p2p.util.common.AbstractCredit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:运营商
 * @author kx
 * @version v1.2.0
 */
//@Service
public class MobileServiceUtil extends AbstractCredit {

    //业务参数
    public static final String method = "api.mobile.get";//请求接口
    public static final String bizType = "mobile";//业务类型
    public static final String callBackUrl = "http://47.91.219.7:2020/agentNotifyInfo/contactNotify";//回调地址

//    public static final String username = "1832568545";//账号---需客户指定
//    public static final String password = "123456";//密码---需客户指定
//    public static final String contentType = "all";//内容类型---需客户指定

    public static void main(String[] args) throws Exception {

        //启动信服务
//        MobileServiceUtil service  = new MobileServiceUtil();
//        service.startTask("123458678","230804199610160531","张正阳","15645181047","303823");

//        service.input("c0f04adc8cb04293863c94994a817a9f","498637");
//
//        String data =  service.query_reprot("c0f04adc8cb04293863c94994a817a9f");
//        String data =  service.query_data("c0f04adc8cb04293863c94994a817a9f");
//        System.out.println(data);
    }


	public String startTask(String uid, String identityCardNo, String identityName, String username, String password) throws Exception{
        //提交受理请求对象
        List<BasicNameValuePair> reqParam = new ArrayList<BasicNameValuePair>();
        reqParam.add(new BasicNameValuePair("apiKey", apiKey));//API授权
        reqParam.add(new BasicNameValuePair("callBackUrl", callBackUrl));//callBackUrl
        reqParam.add(new BasicNameValuePair("method", method));//用户标识

        reqParam.add(new BasicNameValuePair("uid", uid));//账号
        reqParam.add(new BasicNameValuePair("identityCardNo", identityCardNo));//账号
        reqParam.add(new BasicNameValuePair("identityName", identityName));//账号
        reqParam.add(new BasicNameValuePair("username", username));//账号
        reqParam.add(new BasicNameValuePair("password",  new String(Base64.encodeBase64(password.getBytes("UTF-8")))));//密码
        reqParam.add(new BasicNameValuePair("sign", getSign(reqParam)));//请求参数签名

        //提交受理请求
        String json = doProcess("/mobile_report/v1/task", reqParam);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readValue(json, JsonNode.class);
        String code = rootNode.get("code").textValue();

        if("0010".equals(code)) {//受理成功
            String query_token = rootNode.get("token").textValue();

            // TODO: 2019/4/24
            System.out.println(query_token);

            return query_token;
        } else {
            System.out.println("查询失败");
            throw new CheckAPIException(json);
        }
	}

    public String input(String key, String input) throws Exception{
        //提交受理请求对象
        List<BasicNameValuePair> reqParam = new ArrayList<BasicNameValuePair>();
        reqParam.add(new BasicNameValuePair("apiKey", apiKey));//API授权

        reqParam.add(new BasicNameValuePair("input", input));//用户标识
        reqParam.add(new BasicNameValuePair("token", key));//用户标识
        reqParam.add(new BasicNameValuePair("sign", getSign(reqParam)));//请求参数签名

        //提交受理请求
        String json = doProcess("/mobile_report/v1/task/input", reqParam);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readValue(json, JsonNode.class);
        String code = rootNode.get("code").textValue();

        System.out.println(code);

        if("0009".equals(code)) {//写入成功
            String query_token = rootNode.get("token").textValue();
            return query_token;
        } else {
            System.out.println("写入失败");
            throw new CheckAPIException(json);
        }
    }

    public String query_report(String key) throws Exception{
        //提交受理请求对象
        List<BasicNameValuePair> reqParam = new ArrayList<BasicNameValuePair>();
        reqParam.add(new BasicNameValuePair("apiKey", apiKey));//API授权

        reqParam.add(new BasicNameValuePair("token", key));
        reqParam.add(new BasicNameValuePair("sign", getSign(reqParam)));//请求参数签名

        //提交受理请求
        String json = doProcess("/mobile_report/v1/task/report", reqParam);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readValue(json, JsonNode.class);
        String code = rootNode.get("code").textValue();

        if("0000".equals(code)) {//受理成功
            String data = rootNode.get("data").textValue();
            return data;
        } else {
            System.out.println("查询失败");
            throw new CheckAPIException(json);
        }
    }

    /**
     * 获取业务类型
     */
    @Override
    public String getBizType(){
        return bizType;
    }
}
