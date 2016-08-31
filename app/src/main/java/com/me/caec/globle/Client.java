package com.me.caec.globle;

/**
 *
 * Created by yin on 2016/8/30.
 */
public class Client {
    //服务器根地址
    public static final String SERVICE_URL = "http://61.186.243.111";

    //登录
    public static final String LOGIN_URL = SERVICE_URL + "/main/user/login";

    //用户信息
    public static final String USER_INFO_URL = SERVICE_URL + "/member/user/info";

    //验证手机是否注册
    public static final String PHONE_ISREGISTER_URL = SERVICE_URL + "/main/auth/phone";

    //图片验证码
    public static final String IMAGE_CODE_URL = SERVICE_URL + "/main/auth/picCode";

    //验证图片验证码
    public static final String CHECK_IMAGE_CODE_URL = SERVICE_URL + "/main/auth/checkPicCode";

    //忘记密码
    public static final String FORGET_PSD_URL = SERVICE_URL + "/member/user/forget";
}
