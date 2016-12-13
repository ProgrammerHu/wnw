/*
 * Copyright (C) 2014 The Android Client Of Demo Project
 * 
 *     The BeiJing PingChuanJiaHeng Technology Co., Ltd.
 * 
 * Author:Yang ZiTian
 * You Can Contact QQ:646172820 Or Email:mail_yzt@163.com
 */
package com.hemaapp.luna_demo;

/**
 * 该项目配置信息Config
 */
public class MyConfig {
    /**
     * 设备类型 1:苹果；2:安卓； 接口路由使用，必须传此参数
     */
    public static final int DEVICETYPE = 2;
    /**
     * 是否打印信息开关
     */
    public static final boolean DEBUG = true;
    /**
     * 是否启用友盟统计
     */
    public static final boolean UMENG_ENABLE = true;
    /**
     * 网络请求连接超时时限(单位:毫秒)
     */
    public static final int TIMEOUT_HTTP = 20000;
    /**
     * 网络请求尝试次数
     */
    public static final int TRYTIMES_HTTP = 5;
    /**
     * 图片压缩的最大宽度
     */
    public static final int IMAGE_WIDTH = 1080;
    /**
     * 图片压缩的最大高度
     */
    public static final int IMAGE_HEIGHT = 3000;
    /**
     * 图片压缩的最大宽度
     */
    public static final int IMAGE_SIZE = 1080;
    /**
     * 头像的
     */
    public static final int IMAGE_WIDTH_HEAD = 640;
    /**
     * 图片压缩的失真率
     */
    public static final int IMAGE_QUALITY = 100;
    /**
     * 银联支付环境--"00"生产环境,"01"测试环境
     */
    public static final String UNIONPAY_TESTMODE = "00";
    /**
     * 微信appid 胡胡临时的wxe35e9c8b5b3f9ac3
     */
    public static final String APPID_WEIXIN = "wxe6f01aaa5512cd14";
    /**
     * 后台服务接口根路径
     */
    public static final String SYS_ROOT = "http://124.128.23.74:8008/group11/hm_wnw/";
    /**
     * 普通登录
     */
    public static final int LOGIN = 5;
    /**
     * 登录之后关闭所有Activity
     */
    public static final int LOGIN_CLOSEALL = 6;

    /**
     * 注册
     */
    public static final int REGISTER = 0;
    /**
     * 找回密码
     */
    public static final int FIND_PWD = 1;
    /**
     * 设置支付密码
     */
    public static final int SET_PAY_PWD = 2;
    /**
     * 第三方登录之后绑定手机号和邀请码
     */
    public static final int AFTER_THIRD_LOGIN = 3;
    /**
     * 订单已关闭
     */
    public static final String ORDER_CLOSE = "~(>_<)~订单已关闭！";
    /**
     * 待收货
     */
    public static final String ORDER_SEND = "\\(^o^)/~已发货，请等待收货！";
    /**
     * 待发货
     */
    public static final String ORDER_PAY_SUCCESS = "\\(^o^)/~ 支付成功，请等待卖家发货！";
    /**
     * 待评价
     */
    public static final String ORDER_RECIVE = "\\(^o^)/~ 商品已签收成功，快来评价吧！";
    /**
     * 待付款
     */
    public static final String ORDER_NEED_PAY = "\\(^o^)/~ 订单已提交，等待买家付款！";
    /**
     * 已完成
     */
    public static final String ORDER_END = "\\(^o^)/~ 订单已完成！";
    /**
     * 商品评价完成
     */
    public static final String ORDER_REFUND = "Refund";
    /**
     * 退款完成
     */
    public static final String ORDER_REPLY = "Reply";
    /**
     * 修改订单的操作
     */
    public static final String ORDER_BILL_CHANGE = "BillChange";
    /**
     * 支付的统一管理
     */
    public static final int PAY = 100;
    /**
     * 退款的统一管理
     */
    public static final int REFUND = 101;
    /**
     * 评价的统一管理
     */
    public static final int REPLY = 102;
}
