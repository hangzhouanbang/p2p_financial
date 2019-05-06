package com.anbang.p2p.plan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.conf.VerifyConfig;
import com.anbang.p2p.cqrs.q.dao.LoanOrderDao;
import com.anbang.p2p.cqrs.q.dao.UserBaseInfoDao;
import com.anbang.p2p.cqrs.q.dao.UserDboDao;
import com.anbang.p2p.cqrs.q.dbo.LoanOrder;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserDbo;
import com.anbang.p2p.plan.bean.RiskInfo;
import com.anbang.p2p.plan.dao.RiskInfoDao;
import com.anbang.p2p.util.ImgSaveUtil;
import com.anbang.p2p.util.RiskUtil;
import com.anbang.p2p.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RiskService {
    //订单查询接口地址
    static final String Order_Query = "https://idsafe-auth.udcredit.com/front/4.3/api/order_query/pub_key/" + VerifyConfig.PUB_KEY;

    @Autowired
    private UserBaseInfoDao userBaseInfoDao;

    @Autowired
    private LoanOrderDao loanOrderDao;

    @Autowired
    private RiskInfoDao riskInfoDao;

    @Autowired
    private UserDboDao userDboDao;

    static final String imgUrl = "http://47.91.219.7:2020/admin/getImg?imgName=";

    /**
     * 订单查询
     *
     * @param partner_order_id 商户唯一订单号
     */
    public JSONObject orderQuery(String partner_order_id, String userId) throws Exception {
        JSONObject reqJson = new JSONObject();
        JSONObject header = new JSONObject();
        String sign_time = TimeUtils.getStringDate(new Date());
        String sign = RiskUtil.getMD5Sign(partner_order_id, sign_time);
//        System.out.println(sign);
        header.put("partner_order_id", partner_order_id);
        header.put("sign", sign);
        header.put("sign_time", sign_time);
        reqJson.put("header", header);
//        System.out.println("订单查询接口-输入参数：" + JSON.toJSONString(reqJson, true));

        JSONObject data = RiskUtil.doHttpRequest(Order_Query, reqJson).getJSONObject("data");
//        System.out.println("订单查询接口-输出结果：" + JSON.toJSONString(resJson, true));

        String id_number = data.getString("id_number");
        String id_name = data.getString("id_name");
        String idcard_front_photo = data.getString("idcard_front_photo");
        String idcard_back_photo = data.getString("idcard_back_photo");
        String living_photo = data.getString("living_photo");

        String front = id_number + "_front.jpg";
        String back = id_number + "_back.jpg";
        String living = id_number + "_living.jpg";
        ImgSaveUtil.generateImage(idcard_front_photo, front);
        ImgSaveUtil.generateImage(idcard_back_photo, back);
        ImgSaveUtil.generateImage(living_photo, living);



        UserBaseInfo baseInfo = userBaseInfoDao.findById(userId);
        if (baseInfo == null) {
            baseInfo = new UserBaseInfo();
        }

        baseInfo.setId(userId);
        baseInfo.setIDcard(id_number);
        baseInfo.setRealName(id_name);
        baseInfo.setFaceImgUrl(imgUrl + living);
        baseInfo.setIDcardImgUrl_front(imgUrl + front);
        baseInfo.setIDcardImgUrl_reverse(imgUrl + back);
        baseInfo.setCreateTime(System.currentTimeMillis());
        userBaseInfoDao.save(baseInfo);

//        LoanOrder loanOrder = loanOrderDao.findLastOrderByUserId(userId);
//        loanOrder.setRealName(id_name);
//        loanOrderDao.save(loanOrder);

        // 更新用户正式姓名
        userDboDao.updataVerify(userId, true, id_name, id_number);

        return data;
    }

    public void save(RiskInfo riskInfo){
        riskInfoDao.save(riskInfo);
    }

    public RiskInfo getById(String id){
        return riskInfoDao.getById(id);
    }

    public RiskInfo getByUserId(String userId){
        return riskInfoDao.getByUserId(userId);
    }
}
