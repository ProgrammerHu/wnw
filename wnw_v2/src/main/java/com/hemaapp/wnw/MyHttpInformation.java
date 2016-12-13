package com.hemaapp.wnw;

import com.hemaapp.HemaConfig;
import com.hemaapp.MyConfig;
import com.hemaapp.wnw.model.SysInitInfo;
import com.hemaapp.hm_FrameWork.HemaHttpInfomation;

/**
 * 网络请求信息枚举类
 */
public enum MyHttpInformation implements HemaHttpInfomation {
    /**
     * 登录
     */
    CLIENT_LOGIN(HemaConfig.ID_LOGIN, "client_login", "登录", false),
    // 注意登录接口id必须为HemaConfig.ID_LOGIN
    /**
     * 后台服务接口根路径
     */
    SYS_ROOT(0, MyConfig.SYS_ROOT, "后台服务接口根路径", true),
    /**
     * 系统初始化
     */
    INIT(1, "index.php/webservice/index/init", "系统初始化", false),
    /**
     * 验证用户名是否合法
     */
    CLIENT_VERIFY(2, "client_verify", "验证用户名是否合法", false),
    /**
     * 申请随机验证码
     */
    CODE_GET(3, "code_get", "申请随机验证码", false),
    /**
     * 验证随机码
     */
    CODE_VERIFY(4, "code_verify", "验证随机码", false),
    /**
     * 用户注册
     */
    CLIENT_ADD(5, "client_add", "用户注册", false),
    /**
     * 上传文件（图片，音频，视频）
     */
    FILE_UPLOAD(6, "file_upload", "上传文件（图片，音频，视频）", false),
    /**
     * 重设密码
     */
    PASSWORD_RESET(7, "password_reset", "重设密码", false),
    /**
     * 退出登录
     */
    CLIENT_LOGINOUT(8, "client_loginout", "退出登录", false),

    /**
     * 获取支付宝交易签名串
     */
    ALIPAY(9, "OnlinePay/Alipay/alipaysign_get.php", "获取支付宝交易签名串", false),
    /**
     * 获取银联交易签名串
     */
    UNIONPAY(10, "OnlinePay/Unionpay/unionpay_get.php", "获取银联交易签名串", false),
    /**
     * 用户账户余额付款
     */
    CLIENT_ACCOUNTPAY(11, "client_accountpay", "用户账户余额付款", false),
    /**
     * 硬件注册保存
     */
    DEVICE_SAVE(12, "device_save", "硬件注册保存", false),
    /**
     * 获取微信交易签名串接口
     */
    WEIXINPAY_GET(13, "OnlinePay/Weixinpay/weixinpay_get.php", "获取微信交易签名串接口",
            false),
    /**
     * 获取所有城市接口
     */
    DISTRICT_ALL_GET(14, "district_all_get", "获取所有城市接口", false),
    /**
     * 广告接口
     */
    BANNER(15, "banner", "广告接口", false),
    /**
     * 热门标签接口
     */
    TABLE_LIST(16, "table_list", "热门标签接口", false),
    /**
     * 获取帖子列表信息接口
     */
    NOTE_LIST(17, "note_list", "获取帖子列表信息接口", false),
    /**
     * 获取收藏和订阅帖子列表信息接口
     */
    BLOG_LIST(18, "blog_list", "获取收藏和订阅帖子列表信息接口", false),
    /**
     * 获取帖子详情信息接口
     */
    NOTE_GET(19, "note_get", "获取帖子详情信息接口", false),
    /**
     * 点赞操作接口
     */
    PRAISE_OPERATE(20, "praise_operate", "点赞操作接口", false),
    /**
     * 收藏操作接口
     */
    LOVE_OPERATE(21, "love_operate", "收藏操作接口", false),
    /**
     * 删除数据汇总接口
     */
    REMOVE_ROOT(22, "remove_root", "删除数据汇总接口", false),
    /**
     * 获取用户个人资料接口
     */
    CLIENT_GET(23, "client_get", "获取用户个人资料接口", false),
    /**
     * 增加足迹接口
     */
    RECORD_ADD(24, "record_add", "增加足迹接口", false),
    /**
     * 获取店铺详情信息接口
     */
    MERCHANT_GET(25, "merchant_get", "获取店铺详情信息接口", false),
    /**
     * 商品列表接口
     */
    GOODS_LIST(26, "goods_list", "商品列表接口", false),
    /**
     * 店铺订阅添加接口
     */
    SUB_ADD(27, "sub_add", "店铺订阅添加接口", false),
    /**
     * 保存当前用户坐标接口
     */
    POSITION_SAVE(28, "position_save", "保存当前用户坐标接口", false),
    /**
     * 店铺订阅列表接口
     */
    SUBSCRIBE_LIST(29, "subscribe_list", "店铺订阅列表接口", false),
    /**
     * 保存用户资料接口
     */
    CLIENT_SAVE(30, "client_save", "保存用户资料接口", false),
    /**
     * 修改并保存密码接口
     */
    PASSWORD_SAVE(31, "password_save", "修改并保存密码接口", false),
    /**
     * 获取商品详情信息接口
     */
    BLOG_GET(32, "blog_get", "获取商品详情信息接口", false),
    /**
     * 添加留言接口
     */
    WORDS_ADD(33, "words_add", "添加留言接口", false),
    /**
     * 收货地址列表接口
     */
    ADDRESS_LIST(34, "address_list", "收货地址列表接口", false),
    /**
     * 编辑收货地址接口
     */
    ADDRESS_SAVE(35, "address_save", "编辑收货地址接口", false),
    /**
     * 设置默认地址接口
     */
    ADDRESS_OPERATE(36, "address_operate", "设置默认地址接口", false),
    /**
     * 获取消息通知列表接口
     */
    NOTICE_LIST(37, "notice_list", "获取消息通知列表接口", false),
    /**
     * 消息操作接口
     */
    NOTICE_OPERATE(38, "notice_operate", "消息操作接口", false),
    /**
     * 留言回复列表接口
     */
    WORDS_LIST(39, "words_list", "留言回复列表接口", false),
    /**
     * 足迹删除接口
     */
    RECORD_REMOVE(40, "record_remove", "足迹删除接口", false),
    /**
     * 评论列表接口
     */
    REPLY_LIST(41, "reply_list", "评论列表接口", false),
    /**
     * 添加评论接口
     */
    REPLY_ADD(42, "reply_add", "添加评论接口", false),
    /**
     * 修改并保存支付密码接口
     */
    PAYPASSWORD_SAVE(43, "paypassword_save", "修改并保存支付密码接口", false),
    /**
     * 订单列表接口
     */
    BILL_LIST(44, "bill_list", "订单列表接口", false),
    /**
     * 订单操作接口
     */
    BILL_OPERATE(45, "bill_operate", "订单操作接口", false),
    /**
     * 余额支付接口
     */
    FEEACCOUNT_PAY(46, "feeaccount_pay", "余额支付接口", false),
    /**
     * 添加购物车接口
     */
    CART_ADD(47, "cart_add", "添加购物车接口", false),
    /**
     * 购物车商品数量接口
     */
    CART_GET(48, "cart_get", "购物车商品数量接口", false),
    /**
     * 订单保存接口
     */
    BILL_SAVE(49, "bill_save", "订单保存接口", false),
    /**
     * 商品评价接口
     */
    BLOG_REPLY_ADD(50, "blog_reply_add", "商品评价接口", false),
    /**
     * 购物车列表接口
     */
    CART_LIST(51, "cart_list", "购物车列表接口", false),
    /**
     * 购物车操作接口
     */
    CART_SAVEOPERATE(52, "cart_saveoperate", "购物车操作接口", false),
    /**
     * 订单详情接口
     */
    BILL_GET(53, "bill_get", "订单详情接口", false),
    /**
     * 银行和退款列表接口
     */
    GENERAL_LIST(54, "general_list", "银行和退款列表接口", false),
    /**
     * 退款接口
     */
    REFUND(55, "refund", "退款接口", false),
    /**
     * 退款列表接口
     */
    REFUND_LIST(56, "refund_list", "退款列表接口", false),
    /**
     * 退款接口
     */
    REFUND_GET(57, "refund_get", "退款详情接口", false),
    /**
     * 分类商品列表接口
     */
    TYPE_GOODS_LIST(58, "type_goods_list", "分类商品列表接口", false),
    /**
     * 月销量列表接口
     */
    MONTH_SALE(59, "month_sale", "月销量列表接口", false),
    /**
     * 商品团购详情接口
     */
    GROUP_GET(60, "group_get", "商品团购详情接口", false),
    /**
     * 开团到微信好友
     */
    GROUP_BILL_ADD(61, "group_bill_add", "开团到微信好友", false),
    /**
     * 第三方登录接口
     */
    THIRD_SAVE(62, "third_save", "第三方登录接口", false),
    /**
     * 团购订单列表接口
     */
    GROUP_BILL_LIST(63, "group_bill_list", "团购订单列表接口", false),
    /**
     * 团购订单详情接口
     */
    GROUP_BILL_GET(64, "group_bill_get", "团购订单详情接口", false),
    /**
     * 团购订单操作接口
     */
    GROUP_BILL_OPERATE(65, "group_bill_operate", "团购订单操作接口", false),
    /**
     * 团购商品评价接口
     */
    GROUP_REPLY_ADD(66, "group_reply_add", "团购商品评价接口", false),
    /**
     * 后台商铺列表接口
     */
    MERCHANT_LIST(67, "merchant_list", "后台商铺列表接口", false),
    /**
     * 商品分类列表接口
     */
    TYPE_LIST(68, "type_list", "商品分类列表接口", false),
    /**
     * 红点是否显示接口
     */
    RED_DISPLAY(69, "red_display", "红点是否显示接口", false),
    /**
     * 订单红点是否显示接口
     */
    BILL_RED(70, "bill_red", "订单红点是否显示接口", false),
    /**
     * 抵用券列表接口
     */
    COUPON_LIST(71, "coupon_list", "抵用券列表接口", false),
    /**
     * 交易明细表接口
     */
    ACCOUNT_LIST(72, "account_list", "交易明细表接口", false),
    /**
     * 编辑信息接口
     */
    EDIT_THIRD(73, "edit_third", "编辑信息接口", false),
    /**
     * 月粉丝排行接口
     */
    FANS(74, "fans", "月粉丝排行接口", false),
    /**
     * 抵用券接口
     */
    RETURN_COUPON(75, "return_coupon", "抵用券接口", false),
    /**
     * 银行列表接口
     */
    BANK_LIST(76, "bank_list", "银行列表接口", false),
    /**
     * 银行信息添加接口
     */
    BANK_ADD(77, "bank_add", "银行信息添加接口", false),
    /**
     * 余额提现接口
     */
    CASH_ADD(76, "cash_add", "余额提现接口", false),
    /**
     * 地区列表接口
     */
    DIS_LIST(77, "dis_list", "地区列表接口", false),


    /**
     * 帖子添加接口
     */
    NOTE_ADD(100, "note_add", "帖子添加接口", false),
    /**
     * 帖子操作接口
     */
    NOTE_OPERATE(101, "note_operate", "帖子操作接口", false),
    /**
     * 我的二维码
     */
    MY_CODE(102, "my_code", "我的二维码", false),
    /**
     * 海外城市列表接口
     */
    COUNTRY_LIST(103, "country_list", "海外城市列表接口", false),
    /**
     * 商品添加接口
     */
    BLOG_ADD(104, "blog_add", "商品添加接口", false),
    /**
     * 商家商品列表接口
     */
    MERCHANT_GOODS_LIST(105, "merchant_goods_list", "商家商品列表接口", false),
    /**
     * 商家获取商品详情信息接口
     */
    MERCHANT_BLOG_GET(106, "merchant_blog_get", "商家获取商品详情信息接口", false),
    /**
     * 属性规格列表接口
     */
    ATTR_LIST(107, "attr_list", "属性规格列表接口", false),
    /**
     * 属性保存接口
     */
    ATTR_SAVE(108, "attr_save", "属性保存接口", false),
    /**
     * 团类型列表接口
     */
    GROUP_TYPE_LIST(109, "group_type_list", "团类型列表接口", false),
    /**
     * 添加团购接口
     */
    GROUP_ADD(110, "group_add", "添加团购接口", false),
    /**
     * 团类型列表接口
     */
    GROUP_LIST(111, "group_list", "团类型列表接口", false),
    /**
     * 团购删除接口
     */
    GROUP_DELETE(112, "group_delete", "团购删除接口", false),
    /**
     * 团购取消接口
     */
    GROUP_CANCEL(113, "group_concel", "团购取消接口", false),
    /**
     * 获取档期
     */
    TIME_GET(114, "time_get", "获取档期", false),
    /**
     * 商品删除接口
     */
    BLOG_DELETE(115, "blog_delete", "商品删除接口", false),
    /**
     * 商家分类数量接口
     */
    COUNT_LIST(116, "count_list", "商家分类数量接口", false),
    /**
     * 档期列表接口
     */
    TIME_LIST(117, "time_list", "档期列表接口", false),
    /**
     * 商家订单红点是否显示接口
     */
    MERCHANT_BILL_RED(118, "merchant_bill_red", "商家订单红点是否显示接口", false),
    /**
     * 商家订单列表接口
     */
    MERCHANT_BILL_LIST(119, "merchant_bill_list", "商家订单列表接口", false),
    /**
     * 商家订单操作接口
     */
    MERCHANT_BILL_OPERATE(120, "merchant_bill_operate", "商家订单操作接口", false),
    /**
     * 会员列表接口
     */
    VIP_LIST(121, "vip_list", "会员列表接口", false),
    /**
     * 入账记录接口
     */
    MERCHANT_ACCOUNT_LIST(122, "merchant_account_list", "入账记录接口", false),
    /**
     * 商家回复评论接口
     */
    ANSWER_ADD(123, "answer_add", "商家回复评论接口", false),
    /**
     * 删除评论接口
     */
    REPLY_DELETE(124, "reply_delete", "删除评论接口", false),
    /**
     * 商家团购订单操作接口
     */
    MERCHANT_GROUP_OPERATE(125, "merchant_group_operate", "商家团购订单操作接口", false),
    /**
     * 商家退款操作接口
     */
    MERCHANT_REFUND_OPERATE(126, "merchant_refund_operate", "商家退款操作接口", false),
    /**
     * 商家退款删除接口
     */
    REFUND_DELETE(127, "refund_delete", "商家退款删除接口", false),
    /**
     * 粉丝列表接口
     */
    FANS_LIST(128, "fans_list", "粉丝列表接口", false),
    /**
     * 留言回复接口
     */
    WORDS_REPLY(129, "words_reply", "留言回复接口", false),
    /**
     * 修改上级接口
     */
    INVITER_SAVE(130, "inviter_save", "修改上级接口", false),






    ;

    private int id;// 对应NetTask的id
    private String urlPath;// 请求地址
    private String description;// 请求描述
    private boolean isRootPath;// 是否是根路径

    MyHttpInformation(int id, String urlPath, String description,
                      boolean isRootPath) {
        this.id = id;
        this.urlPath = urlPath;
        this.description = description;
        this.isRootPath = isRootPath;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getUrlPath() {
        if (isRootPath)
            return urlPath;

        String path = SYS_ROOT.urlPath + urlPath;

        if (this.equals(INIT))
            return path;

        MyApplication application = MyApplication.getInstance();
        SysInitInfo info = application.getSysInitInfo();
        path = info.getSys_web_service() + urlPath;

        if (this.equals(ALIPAY))
            path = info.getSys_plugins() + urlPath;

        if (this.equals(UNIONPAY))
            path = info.getSys_plugins() + urlPath;

        if (this.equals(WEIXINPAY_GET))
            path = info.getSys_plugins() + urlPath;

        return path;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isRootPath() {
        return isRootPath;
    }

}
