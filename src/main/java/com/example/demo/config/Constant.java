package com.example.demo.config;

public class Constant {
    public static String USER_INFO = "userInfo";
    public static String REGISTER_CODE = "verifyCode";
    public static String SERVER_URL = "https://api.netease.im/sms/sendcode.action";
    //网易云信分配的账号，请替换你在管理后台应用下申请的Appkey
    public static final String
            APP_KEY = "4001b66039cf92e3f1a09f2adf4ec491";
    //网易云信分配的密钥，请替换你在管理后台应用下申请的appSecret
    public static final String APP_SECRET = "77ea4cc87d34";
    //随机数
    public static final String NONCE = "123456";
    //短信模板ID
    public static final String TEMPLATEID = "3057527";
    //手机号，接收者号码列表，JSONArray格式，限制接收者号码个数最多为100个
    public static final String MOBILES = "['15867162548']";
    //短信参数列表，用于依次填充模板，JSONArray格式，每个变量长度不能超过30字,对于不包含变量的模板，不填此参数表示模板即短信全文内容
    public static final String PARAMS = "['xxxx','xxxx']";


    public static final String USER_MESSAGE = "UserMessage";
    public static final String USER_DIRECT_EXCHANGE = "UserDirectExchange";
    public static final String DIRECT_ROUTING = "DirectRouting";



    public static final String ORDER_CALLBACK_MESSAGE = "orderCallback";
    public static final String ORDER_CALLBACK_DIRECT_EXCHANGE = "orderCallbackDirectExchange";
    public static final String ORDER_CALLBACK_DIRECT_ROUTING = "orderCallbackDirectRouting";

}
