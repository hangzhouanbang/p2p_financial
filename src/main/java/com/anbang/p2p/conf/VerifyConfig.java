package com.anbang.p2p.conf;

/**
 * @Description: 风控接口配置
 */
public class VerifyConfig {

    // 有盾公钥
    public static final String PUB_KEY = "ba0fea87-d098-41b9-a59f-c93fb41f21bd";

    // 有盾秘钥
    public static final String SECURITY_KEY = "5585ee38-7fc8-43ba-9308-009980d80a19";

    // 有盾回显
    public static final String RETURN_URL = "https://www.baidu.com/";

    // 有盾回调
    public static final String CALLBACK_URL = "http://47.96.20.47:2020/verify/verifyCallback";

    // 银行卡四要素 appkey
    public static final String BANK_APP_KEY = "cf8046f434dc84614a81eda0a5f6ff9b";

    // 手机号二要素 appkey
    public static final String PHONE_APP_KEY = "cf8046f434dc84614a81eda0a5f6ff9b";

    public static final String IMG_PATH = "/data/tomcat/apache-tomcat-9.0.10/webapps/p2p/imgs/";


}
