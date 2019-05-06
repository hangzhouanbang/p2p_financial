package com.anbang.p2p.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.conf.VerifyConfig;
import com.anbang.p2p.plan.bean.RiskInfo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

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
        String url = "http://static.udcredit.com/id/v43/index.html?apiparams=" + encrypt;
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

    /**
     * 用户画像
     */
    public static JSONObject getRiskInfo(String id_no, String out_order_id) {
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("id_no", id_no);
        JSONObject result = null;
        try {
            result = DataServiceUtil.dataservice(reqJSON ,"Y1001005" ,out_order_id);
            String ret_code = result.getJSONObject("header").getString("ret_code");
            if ("000000".equals(ret_code)) {
                // 获取风控信息
                JSONObject data = result.getJSONObject("body");
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(reqJSON);
            System.out.println(result);
        }
        return null;
    }

    public static void main(String[] args) {
//        String json = "{\"body\":{\"device_summary\":{\"device_loan_app_count\":\"41\",\"device_loan_app_count_15d\":\"17\",\"device_loan_app_count_1m\":\"22\",\"device_loan_app_count_3m\":\"33\",\"device_loan_app_count_6m\":\"39\",\"device_loan_app_count_7d\":\"5\",\"device_summary_rule\":{\"loan_app_too_many\":\"1\"}},\"devices_list\":[{\"device_id\":\"97_2da15832-8bec-4272-b1a4-acbd7bbb1234\",\"device_last_use_date\":\"2018-12-24\"}],\"graph_detail\":{\"bankcard_count\":\"19\",\"graph_partner_user_info\":[{\"hit_dishonest_of_court\":\"0\",\"hit_dishonest_of_online_loan\":\"0\",\"hit_living_attack\":\"0\",\"id_number_mask\":\"4114******0X\",\"loan_platform_count\":\"0\",\"name_mask\":\"王*\",\"order_no\":\"1511316547668670\",\"partner_mark_user\":\"0\",\"partner_user_latest_auth_order_time\":\"2017-11-22\",\"partner_user_latest_deviceId\":\"97_6e2dbebe-5603-439f-a1ad-f1afe29b1234\"}],\"link_device_count\":\"49\",\"link_device_detail\":{\"frand_device_count\":\"6\",\"living_attack_device_count\":\"2\"},\"link_user_count\":\"44\",\"link_user_detail\":{\"court_dishonest_count\":\"0\",\"living_attack_count\":\"1\",\"online_dishonest_count\":\"0\",\"partner_mark_count\":\"2\"},\"mobile_count\":\"0\",\"other_link_device_count\":\"434\",\"other_link_device_detail\":{\"other_frand_device_count\":\"32\",\"other_living_attack_device_count\":\"0\"},\"partner_user_count\":\"3\",\"user_have_bankcard_count\":\"0\"},\"id_detail\":{\"birthday\":\"1989.02.26\",\"city\":\"杭州\",\"gender\":\"男\",\"id_number_mask\":\"3303************0X\",\"name_credible\":\"小盾\",\"names\":\"小盾\",\"province\":\"浙江\"},\"last_modified_time\":\"2019-03-26 19:48:03\",\"loan_detail\":{\"actual_loan_platform_count\":\"3\",\"actual_loan_platform_count_1m\":\"0\",\"actual_loan_platform_count_3m\":\"0\",\"actual_loan_platform_count_6m\":\"0\",\"loan_detail_rule\":{\"loan_platform_count_7d_too_many\":\"0\",\"loan_platform_count_too_many\":\"0\"},\"loan_industry\":[{\"actual_loan_platform_count\":\"3\",\"loan_platform_count\":\"3\",\"name\":\"小额现金贷\",\"repayment_platform_count\":\"3\",\"repayment_times_count\":\"35\"},{\"actual_loan_platform_count\":\"0\",\"loan_platform_count\":\"1\",\"name\":\"分期行业\",\"repayment_platform_count\":\"0\",\"repayment_times_count\":\"0\"},{\"loan_platform_count\":\"1\",\"name\":\"大学生分期\"},{\"loan_platform_count\":\"1\",\"name\":\"电商分期\"},{\"loan_platform_count\":\"0\",\"name\":\"旅游分期\"},{\"loan_platform_count\":\"0\",\"name\":\"教育分期\"},{\"loan_platform_count\":\"0\",\"name\":\"汽车分期\"},{\"loan_platform_count\":\"0\",\"name\":\"租房分期\"},{\"loan_platform_count\":\"0\",\"name\":\"农业消金\"},{\"loan_platform_count\":\"1\",\"name\":\"医美分期\"}],\"loan_platform_count\":\"7\",\"loan_platform_count_15d\":\"2\",\"loan_platform_count_1m\":\"2\",\"loan_platform_count_3m\":\"2\",\"loan_platform_count_6m\":\"4\",\"loan_platform_count_7d\":\"1\",\"repayment_platform_count\":\"3\",\"repayment_platform_count_1m\":\"0\",\"repayment_platform_count_3m\":\"0\",\"repayment_platform_count_6m\":\"0\",\"repayment_times_count\":\"35\"},\"request_info\":{\"first_request_date\":\"2018-08-13\",\"last_request_date\":\"2019-03-22\",\"max_request_count_on_same_platform\":\"613\",\"max_request_count_on_same_platform_15d\":\"44\",\"max_request_count_on_same_platform_1m\":\"74\",\"max_request_count_on_same_platform_3m\":\"167\",\"max_request_count_on_same_platform_6m\":\"524\",\"max_request_count_on_same_platform_7d\":\"32\",\"max_request_platform_count_on_same_day\":\"2\",\"max_request_platform_count_on_same_day_15d\":\"2\",\"max_request_platform_count_on_same_day_1m\":\"2\",\"max_request_platform_count_on_same_day_3m\":\"2\",\"max_request_platform_count_on_same_day_6m\":\"2\",\"max_request_platform_count_on_same_day_7d\":\"1\",\"request_count\":\"665\",\"request_count_15d\":\"46\",\"request_count_1m\":\"76\",\"request_count_3m\":\"169\",\"request_count_6m\":\"565\",\"request_count_7d\":\"32\",\"request_count_on_same_day\":\"83\",\"request_count_on_same_day_15d\":\"27\",\"request_count_on_same_day_1m\":\"28\",\"request_count_on_same_day_3m\":\"28\",\"request_count_on_same_day_6m\":\"83\",\"request_count_on_same_day_7d\":\"27\",\"request_days\":\"53\",\"request_days_15d\":\"6\",\"request_days_1m\":\"9\",\"request_days_3m\":\"20\",\"request_days_6m\":\"44\",\"request_days_7d\":\"3\",\"request_detail_rule\":{\"request_count_7d_too_many\":\"0\",\"request_count_on_same_day_too_many\":\"1\",\"request_count_too_many\":\"1\",\"request_days_too_many\":\"0\",\"request_platform_count_on_same_day_too_many\":\"0\"},\"request_living_attack\":{\"device_living_attack_count\":\"13\",\"id_no_living_attack_count\":\"15\",\"id_no_living_attack_platform_count\":\"2\",\"id_no_mask_attack_count\":\"3\",\"id_no_software_simulation_attack_count\":\"9\",\"id_no_video_attack_count\":\"7\"}},\"score_detail\":{\"risk_evaluation\":\"中低风险\",\"score\":\"48\"},\"ud_order_no\":\"438191831298080768\",\"user_features\":[{\"last_modified_date\":\"2019-01-09\",\"user_feature_type\":\"11\"},{\"last_modified_date\":\"2019-03-18\",\"user_feature_type\":\"0\"},{\"last_modified_date\":\"2019-02-26\",\"user_feature_type\":\"5\"},{\"last_modified_date\":\"2018-12-02\",\"user_feature_type\":\"8\"},{\"last_modified_date\":\"2019-03-21\",\"user_feature_type\":\"10\"}]},\"header\":{\"req_time\":\"2019-03-26 20:08:06\",\"resp_time\":\"2019-03-26 20:08:08\",\"ret_code\":\"000000\",\"ret_msg\":\"操作成功\",\"version\":\"4.1\"},\"sign\":\"17EC00EADD4F9A7A622764BC77D5CE83\"}";
//        JSONObject object = JSONObject.parseObject(json);
//        System.out.println(JSON.toJSONString(object));




        // 第三方风控，有盾用户画像
//        JSONObject object = RiskUtil.getRiskInfo("230804199610160531", "test001");
//        System.out.println(JSON.toJSONString(object));
    }

}
