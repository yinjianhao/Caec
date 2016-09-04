package com.me.caec.globle;

/**
 * 请求地址
 *
 * Created by yin on 2016/8/30.
 */
public class Client {
    //服务器根地址
    public static final String SERVICE_URL = "http://61.186.243.111";

    //注册
    public static final String REGISTER_URL = SERVICE_URL + "/main/user/register";

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

    //修改密码
    public static final String MODIFY_PSD_URL = SERVICE_URL + "/member/user/password";

    //忘记密码
    public static final String FORGET_PSD_URL = SERVICE_URL + "/member/user/forget";

    //发送短信验证码
    public static final String MSG_CODE_URL = SERVICE_URL + "/main/auth/sendSMSCode";

    //验证短信验证码
    public static final String CHECK_MSG_CODE_URL = SERVICE_URL + "/main/auth/checkSMSCode";

    //获取收货地址列表
    public static final String ADDRESS_LIST_URL = SERVICE_URL + "/member/receiving/list";

    //删除收货地址
    public static final String DELETE_ADDRESS_URL = SERVICE_URL + "/member/receiving/delete";

    //新建收货地址
    public static final String CREATE_ADDRESS_URL = SERVICE_URL + "/member/receiving/new";

    //修改收货地址
    public static final String EDIT_ADDRESS_URL = SERVICE_URL + "/member/receiving/edit";

    //修改收货地址
    public static final String SET_DEFAULT_ADDRESS_URL = SERVICE_URL + "/member/receiving/default";

    //订单相关
    //订单数量总览
    public static final String ORDER_NUM_URL = SERVICE_URL + "/member/order/overview";

    //订单列表
    public static final String ORDER_LIST_URL = SERVICE_URL + "/member/order/list";
}
