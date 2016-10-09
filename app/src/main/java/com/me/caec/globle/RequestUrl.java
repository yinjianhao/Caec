package com.me.caec.globle;

/**
 * 请求地址
 * <p>
 * Created by yin on 2016/8/30.
 */
public class RequestUrl {
    //服务器根地址
    public static final String SERVICE_URL = "http://ssl.mall.changan.com.cn";
//    public static final String SERVICE_URL = "http://172.26.1.79";

    //首页
    public static final String HOME_ACTIVITY = SERVICE_URL + "/main/actives/list";

    //图片上传
    public static final String UPLOAD_IMAGE_URL = SERVICE_URL + "/member/attachment/base64/upload";

    //注册
    public static final String REGISTER_URL = SERVICE_URL + "/main/user/register";

    //登录
    public static final String LOGIN_URL = SERVICE_URL + "/main/user/login";

    //登录加密
    public static final String LOGIN_ENCODE_URL = SERVICE_URL + "/main/user/loginEncrypt";

    //获取公钥
    public static final String PUBILIC_KEY_URL = SERVICE_URL + "/main/user/generatePublicKey";

    //续期token
    public static final String REFRESH_TOKEN_URL = SERVICE_URL + "/main/user/relogin";

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

    //订单详情
    public static final String ORDER_DETAIL_URL = SERVICE_URL + "/member/order/detail";

    //订单详情(未付款,已关闭)
    public static final String ORDER_DETAIL_PAY_URL = SERVICE_URL + "/member/order/notpay";

    //订单详情(已取消,审核中)
    public static final String ORDER_DETAIL_CANCEL_URL = SERVICE_URL + "/member/order/canceled";

    //提车验证码
    public static final String CAR_CODE_URL = SERVICE_URL + "/main/order/code";

    //确认收货
    public static final String CONFIRM_RECEIPT_URL = SERVICE_URL + "/member/order/complete";

    //取消订单
    public static final String CANCEL_ORDER_URL = SERVICE_URL + "/member/order/cancel";

    //评价列表
    public static final String COMMENT_LIST_URL = SERVICE_URL + "/member/assess/goodsAll";

    //发表评价
    public static final String PUBILSH_COMMENT_URL = SERVICE_URL + "/member/assess/publishAll";

    //购物车列表
    public static final String CART_LIST_URL = SERVICE_URL + "/shoppingcart/cart/info";

    //购物车条目修改
    public static final String CART_MODIFY_URL = SERVICE_URL + "/shoppingcart/cart/modify";

    //加入购物车
    public static final String ADD_CART_URL = SERVICE_URL + "/shoppingcart/cart/shopping";

    //删除购物车商品
    public static final String CART_DELETE_URL = SERVICE_URL + "/shoppingcart/cart/clean";

    //获取购物车总数
    public static final String CART_NUM_URL = SERVICE_URL + "/shoppingcart/cart/overview";

    //获取确认订单数据
    public static final String CONFIRM_LIST_URL = SERVICE_URL + "/main/order/unconfirm";

    //提交订单
    public static final String CONFIRM_ORDER_URL = SERVICE_URL + "/main/order/confirm";

    //经销商列表
    public static final String DISTRIBUTOR_LIST_URL = SERVICE_URL + "/main/dealer/list";
}
