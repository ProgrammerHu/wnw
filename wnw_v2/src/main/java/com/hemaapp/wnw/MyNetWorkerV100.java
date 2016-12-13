package com.hemaapp.wnw;

import android.content.Context;
import android.widget.Toast;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.wnw.nettask.AccountListTask;
import com.hemaapp.wnw.nettask.AddressListTask;
import com.hemaapp.wnw.nettask.AlipayTradeTask;
import com.hemaapp.wnw.nettask.BankListTask;
import com.hemaapp.wnw.nettask.BannerTask;
import com.hemaapp.wnw.nettask.BillGetTask;
import com.hemaapp.wnw.nettask.BillListTask;
import com.hemaapp.wnw.nettask.BillRedTask;
import com.hemaapp.wnw.nettask.BillSaveTask;
import com.hemaapp.wnw.nettask.CartGetTask;
import com.hemaapp.wnw.nettask.CartListTask;
import com.hemaapp.wnw.nettask.ClientAddTask;
import com.hemaapp.wnw.nettask.ClientGetTask;
import com.hemaapp.wnw.nettask.ClientLoginTask;
import com.hemaapp.wnw.nettask.CodeVerifyTask;
import com.hemaapp.wnw.nettask.CouponListTask;
import com.hemaapp.wnw.nettask.CurrentStringTask;
import com.hemaapp.wnw.nettask.CurrentTask;
import com.hemaapp.wnw.nettask.DiscoveryTypeListTask;
import com.hemaapp.wnw.nettask.DistrictAllGetTask;
import com.hemaapp.wnw.nettask.DistrictListTask;
import com.hemaapp.wnw.nettask.EditThirdTask;
import com.hemaapp.wnw.nettask.FansTask;
import com.hemaapp.wnw.nettask.FileUploadTask;
import com.hemaapp.wnw.nettask.GeneralListTask;
import com.hemaapp.wnw.nettask.GoodsBigListTask;
import com.hemaapp.wnw.nettask.GoodsDetailTask;
import com.hemaapp.wnw.nettask.GoodsListTask;
import com.hemaapp.wnw.nettask.GroupBillGetTask;
import com.hemaapp.wnw.nettask.GroupBillListTask;
import com.hemaapp.wnw.nettask.GroupGetTask;
import com.hemaapp.wnw.nettask.InitTask;
import com.hemaapp.wnw.nettask.MerchantListTask;
import com.hemaapp.wnw.nettask.MerchantTask;
import com.hemaapp.wnw.nettask.NoteGetTask;
import com.hemaapp.wnw.nettask.NoteListTask;
import com.hemaapp.wnw.nettask.NoticeListTask;
import com.hemaapp.wnw.nettask.RedDisplayTask;
import com.hemaapp.wnw.nettask.RefundGetTask;
import com.hemaapp.wnw.nettask.RefundListTask;
import com.hemaapp.wnw.nettask.ReplyListTask;
import com.hemaapp.wnw.nettask.ReturnCouponTask;
import com.hemaapp.wnw.nettask.SubscribeListTask;
import com.hemaapp.wnw.nettask.TableListTask;
import com.hemaapp.wnw.nettask.TypeGoodsListTask;
import com.hemaapp.wnw.nettask.UnionTradeTask;
import com.hemaapp.wnw.nettask.WeixinTradeTask;
import com.hemaapp.wnw.nettask.WordsListTask;

import java.util.HashMap;

import xtom.frame.util.XtomDeviceUuidFactory;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 整合一期的接口
 * Created by HuHu on 2016-09-01.
 */
public class MyNetWorkerV100 extends HemaNetWorker {
    private Context mContext;

    public MyNetWorkerV100(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public void clientLogin() {
        String username = XtomSharedPreferencesUtil.get(mContext, "username");
        String password = XtomSharedPreferencesUtil.get(mContext, "password");
        if (isNull(password) || isNull(username)) {
            Toast.makeText(mContext, "用户名缓存失效，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        this.clientLogin(username, password);
    }

    @Override
    public boolean thirdSave() {
        String thirdtype = XtomSharedPreferencesUtil.get(mContext, "thirdtype");
        String thirduid = XtomSharedPreferencesUtil.get(mContext, "thirduid");
        String avatar = XtomSharedPreferencesUtil.get(mContext, "avatar");
        String nickname = XtomSharedPreferencesUtil.get(mContext, "nickname");
        String sex = XtomSharedPreferencesUtil.get(mContext, "sex");
        String unionid = XtomSharedPreferencesUtil.get(mContext, "unionid");
        if (isNull(thirdtype) || isNull(thirduid)) {
            return false;
        }
        this.thirdSave(thirdtype, thirduid, avatar, nickname, sex, unionid);
        return true;
    }

    /**
     * 系统初始化
     */
    public void init() {
        MyHttpInformation information = MyHttpInformation.INIT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lastloginversion", MyUtil.getAppVersion(mContext));// 版本号码(默认：1.0.0)
        params.put("devicetype", String.valueOf(MyConfig.DEVICETYPE));// 登陆所用的系统版本号
        params.put("device_sn", XtomDeviceUuidFactory.get(mContext));// 客户端硬件串号
        MyNetTask task = new InitTask(information, params);
        executeTask(task);
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    public void clientLogin(String username, String password) {
        MyHttpInformation information = MyHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);// 用户登录名 手机号或邮箱
        params.put("password", MyUtil.encryptPwd(password, MyConfig.USE_MD5)); // 登陆密码
        // 服务器端存储的是32位的MD5加密串
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = MyUtil.getAppVersion(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号
        params.put("submit", "提交");
        MyNetTask task = new ClientLoginTask(information, params);
        executeTask(task);
    }

    /**
     * 验证用户名是否合法
     *
     * @param username
     */
    public void clientVerify(String username) {
        MyHttpInformation information = MyHttpInformation.CLIENT_VERIFY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 用户退出
     *
     * @param token
     */
    public void clientLoginOut(String token) {
        MyHttpInformation information = MyHttpInformation.CLIENT_LOGINOUT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 硬件注册保存接口
     *
     * @param token      登录令牌
     * @param deviceid   登陆手机硬件码 对应百度推送userid
     * @param devicetype 登陆手机类型 1:苹果 2:安卓
     * @param channelid  百度推送渠道id 方便直接从百度后台进行推送测试
     */
    public void deviceSave(String token, String deviceid, String devicetype,
                           String channelid) {
        MyHttpInformation information = MyHttpInformation.DEVICE_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("deviceid", deviceid);
        params.put("devicetype", devicetype);
        params.put("channelid", channelid);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 验证随机码服务接口
     *
     * @param username 用户登录名
     * @param code     4位随机号码 测试阶段固定向服务器提交“1234”
     */
    public void codeVerify(String username, String code) {
        MyHttpInformation information = MyHttpInformation.CODE_VERIFY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("code", code);
        MyNetTask task = new CodeVerifyTask(information, params);
        executeTask(task);
    }

    /**
     * 申请随机验证码接口
     *
     * @param username
     */
    public void codeGet(String username) {
        MyHttpInformation information = MyHttpInformation.CODE_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 上传文件（图片，音频，视频）
     *
     * @param token     登录令牌
     * @param keytype   上传操作类型 1：个人头像图片
     * @param keyid     主键id
     * @param duration  播放时长 上传图片时，此值固定传"0"即可 单位：S(秒)
     * @param orderby   排序上传多副图片时，传递上传次序 从0开始，依次递增
     * @param content   内容描述 有的项目中，展示性图片需要附属一段文字说明信息。 默认传"无"
     * @param temp_file 文件 临时需要上传的文件控件名称 对应表单type="file" 中的name值
     *                  ，相关文件请先在客户端压缩再上传（压缩尺寸宽度固定640）
     */
    public void fileUpload(String token, String keytype, String keyid,
                           String duration, String orderby, String content, String temp_file) {
        MyHttpInformation information = MyHttpInformation.FILE_UPLOAD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("keytype", keytype); //
        params.put("keyid", keyid); //
        params.put("duration", duration); //
        params.put("orderby", orderby); //
        params.put("content", content);// 内容描述 有的项目中，展示性图片需要附属一段文字说明信息。默认传"无"
        HashMap<String, String> files = new HashMap<>();
        files.put("temp_file", temp_file); //

        MyNetTask task = new FileUploadTask(information, params, files);
        executeTask(task);
    }

    /**
     * 重设密码接口
     *
     * @param temp_token   临时令牌 必须填写正确
     * @param keytype      密码类型 1：登陆密码 2：支付密码
     * @param new_password 新密码
     */
    public void passwordReset(String temp_token, String keytype,
                              String new_password) {
        MyHttpInformation information = MyHttpInformation.PASSWORD_RESET;
        HashMap<String, String> params = new HashMap<>();
        params.put("temp_token", temp_token);
        params.put("keytype", keytype);
        params.put("new_password", MyUtil.encryptPwd(new_password, MyConfig.USE_MD5));

        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 获取所有城市接口
     */
    public void districtAllGet() {
        MyHttpInformation information = MyHttpInformation.DISTRICT_ALL_GET;
        HashMap<String, String> params = new HashMap<>();
        MyNetTask task = new DistrictAllGetTask(information, params);
        executeTask(task);
    }

    /**
     * 用户注册接口
     *
     * @param temp_token    临时令牌 可以有效防止机器人恶意注册（该值从 验证随机码 接口获取）
     * @param username      用户注册名
     * @param password      登陆密码
     * @param nickname      用户昵称
     * @param sex           性别 男或女
     * @param district_name 注册地区 将全称传过来
     */
    public void clientAdd(String temp_token, String username, String password,
                          String nickname, String sex,
                          String district_name) {

        MyHttpInformation information = MyHttpInformation.CLIENT_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("temp_token", temp_token);
        params.put("username", username);
        params.put("password", MyUtil.encryptPwd(password, MyConfig.USE_MD5));
        params.put("nickname", nickname);
        params.put("sex", sex);
        params.put("district_name", district_name);
        MyNetTask task = new ClientAddTask(information, params);
        executeTask(task);
    }

    /**
     * 广告接口
     */
    public void banner(String keytype) {
        MyHttpInformation information = MyHttpInformation.BANNER;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        MyNetTask task = new BannerTask(information, params);
        executeTask(task);
    }

    /**
     * 热门标签接口
     */
    public void tableList() {
        MyHttpInformation information = MyHttpInformation.TABLE_LIST;
        HashMap<String, String> params = new HashMap<>();
        MyNetTask task = new TableListTask(information, params);
        executeTask(task);
    }

    /**
     * 获取帖子列表信息接口
     *
     * @param keytype 关联类型
     *                1.最新帖子
     *                2.店铺帖子
     *                3.搜索
     *                4.标签选择
     *                5我的发布
     *                6我的草稿
     * @param keyid   关联id
     *                keytype=1时，keyid=0
     *                keytype=2时，店铺id
     *                keytype=3时，搜索关键字
     *                keytype=4时，标签id
     *                keytype=5时，0
     *                keytype=6时，0
     * @param page    当前列表翻页索引 第一页时请传递page=0，翻页时依次递增。
     */
    public void noteList(String token, String keytype, String keyid, String page) {

        MyHttpInformation information = MyHttpInformation.NOTE_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("page", page);
        MyNetTask task = new NoteListTask(information, params);
        executeTask(task);
    }

    /**
     * 获取帖子详情信息接口
     *
     * @param token 登录令牌
     * @param id    帖子主键id 从 帖子列表 获取
     */
    public void noteGet(String token, String id) {
        MyHttpInformation information = MyHttpInformation.NOTE_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        MyNetTask task = new NoteGetTask(information, params);
        executeTask(task);
    }

    /**
     * 点赞操作接口
     *
     * @param token   登陆令牌
     * @param keytype 关联类型 2.帖子
     * @param flag    操作类型 1.点赞2.取消点赞
     * @param keyid   关联id keytype=2,keyid=帖子id
     */
    public void praiseOperate(String token, String keytype, String flag,
                              String keyid) {
        MyHttpInformation information = MyHttpInformation.PRAISE_OPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("flag", flag);
        params.put("keyid", keyid);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 店铺订阅添加接口
     *
     * @param token
     * @param id
     */
    public void subAdd(String token, String id, String position) {
        MyHttpInformation information = MyHttpInformation.SUB_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("position", position);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 收藏操作接口
     *
     * @param token   登陆令牌
     * @param keytype 关联类型 2.帖子 3.商品
     * @param flag    操作类型 1.添加收藏 2.取消收藏
     * @param keyid   关联id keytype=2,keyid=帖子id keytype=3,keyid=商品id
     */
    public void loveOperate(String token, String keytype, String flag,
                            String keyid) {
        MyHttpInformation information = MyHttpInformation.LOVE_OPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("flag", flag);
        params.put("keyid", keyid);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 删除数据汇总接口
     *
     * @param token           登陆令牌
     * @param keytype         列表类型 1：删除留言 2：删除系统消息 3：取消店铺订阅 4：删除地址 5：删除足迹
     * @param operate_keytype 列表类型 1：单条删除/取消 2：清空全部
     * @param keyid           关联主键 如果是清空模式则传0
     */
    public void removeRoot(String token, String keytype,
                           String operate_keytype, String keyid) {
        MyHttpInformation information = MyHttpInformation.REMOVE_ROOT;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("operate_keytype", operate_keytype);
        params.put("keyid", keyid);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 获取用户个人资料接口
     *
     * @param token
     */
    public void clientGet(String token) {
        MyHttpInformation information = MyHttpInformation.CLIENT_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        MyNetTask task = new ClientGetTask(information, params);
        executeTask(task);
    }

    /**
     * 增加足迹接口
     *
     * @param token
     * @param keytype 关联类型 0商品1.帖子
     * @param keyid   商品或帖子id
     */
    public void recordAdd(String token, String keytype, String keyid) {
        MyHttpInformation information = MyHttpInformation.RECORD_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 获取店铺详情信息接口
     *
     * @param token
     * @param id    店铺主键id
     */
    public void merchantGet(String token, String id) {
        MyHttpInformation information = MyHttpInformation.MERCHANT_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        MyNetTask task = new MerchantTask(information, params);
        executeTask(task);
    }

    /**
     * 保存当前用户坐标接口
     *
     * @param token 登录令牌
     * @param lng   用户当前所处经度
     * @param lat   用户当前所处纬度
     */
    public void positionSave(String token, String lng, String lat) {
        MyHttpInformation information = MyHttpInformation.POSITION_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("lng", lng);
        params.put("lat", lat);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 店铺订阅列表接口
     *
     * @param token    登陆令牌
     * @param keytype  业务类型 1：我的订阅 2：未订阅
     * @param keywords 关键词 keytype=2 时传，传标签id其余传无
     * @param orderby  排序 1.最新 2.粉丝升序3粉丝降
     * @param page     当前列表翻页索引
     */
    public void subscribeList(String token, String keytype, String keywords,
                              String orderby, String page) {
        MyHttpInformation information = MyHttpInformation.SUBSCRIBE_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keywords", keywords);
        params.put("orderby", orderby);
        params.put("page", page);
        MyNetTask task = new SubscribeListTask(information, params);
        executeTask(task);
    }

    /**
     * 保存用户资料接口
     *
     * @param token         登录令牌
     * @param nickname      用户昵称
     * @param sex           性别 男或女
     * @param district_name 注册地区
     */
    public void clientSave(String token, String nickname, String sex,
                           String district_name, String flag, String like, String major) {
        MyHttpInformation information = MyHttpInformation.CLIENT_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("nickname", nickname);
        params.put("sex", sex);
        params.put("district_name", district_name);
        params.put("flag", flag);
        params.put("like", like);
        params.put("major", major);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 修改并保存密码接口
     *
     * @param token        登录令牌
     * @param old_password 旧密码
     * @param new_password 新密码
     */
    public void passwordSave(String token, String old_password,
                             String new_password) {
        MyHttpInformation information = MyHttpInformation.PASSWORD_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("old_password", MyUtil.encryptPwd(old_password, MyConfig.USE_MD5));
        params.put("new_password", MyUtil.encryptPwd(new_password, MyConfig.USE_MD5));
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 获取商品详情信息接口
     *
     * @param id
     */
    public void blogGet(String token, String id) {
        MyHttpInformation information = MyHttpInformation.BLOG_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        MyNetTask task = new GoodsDetailTask(information, params);
        executeTask(task);
    }

    /**
     * 添加留言接口
     *
     * @param token
     * @param id        商家id
     * @param client_id 用户id
     * @param content   留言内容
     */
    public void wordsAdd(String token, String id, String client_id,
                         String content) {
        MyHttpInformation information = MyHttpInformation.WORDS_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("client_id", client_id);
        params.put("content", content);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 收货地址列表接口
     *
     * @param token
     */
    public void addressList(String token) {
        MyHttpInformation information = MyHttpInformation.ADDRESS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        MyNetTask task = new AddressListTask(information, params);
        executeTask(task);
    }

    /**
     * 编辑收货地址接口
     *
     * @param token
     * @param id          主键id 新增时传0
     * @param name        收货名
     * @param tel
     * @param province_id
     * @param city_id
     * @param district_id
     * @param address     手动填写的具体地址
     */
    public void addressSave(String token, String id, String name, String tel,
                            String province_id, String city_id, String district_id,
                            String address) {
        MyHttpInformation information = MyHttpInformation.ADDRESS_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("name", name);
        params.put("tel", tel);
        params.put("province_id", province_id);
        params.put("city_id", city_id);
        params.put("district_id", district_id);
        params.put("address", address);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 设置默认地址接口
     *
     * @param token
     * @param id
     */
    public void addressOperate(String token, String id) {
        MyHttpInformation information = MyHttpInformation.ADDRESS_OPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 获取支付宝交易签名串
     */
    public void alipay(String token, String paytype, String keytype,
                       String keyid, String total_fee, String coupon_id) {
        MyHttpInformation information = MyHttpInformation.ALIPAY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("paytype", paytype);// 支付类型 固定传1
        params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
        params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
        params.put("total_fee", total_fee);// 支付交易金额,单位：元(测试时统一传递0.01元)
        params.put("coupon_id", coupon_id);// 没有时传0

        MyNetTask task = new AlipayTradeTask(information, params);
        executeTask(task);
    }

    /**
     * 获取银联交易签名串
     *
     * @param token
     * @param paytype   支付类型 固定传2
     * @param keytype   业务类型 1：账户余额充值 2：商品立即购买
     * @param keyid     业务相关id 当keytype=1时, keyid=0 当keytype=2时, keyid=blog_id
     * @param total_fee 支付交易金额 单位：元(测试时统一传递0.01元)
     * @param coupon_id 优惠券id	没有时传0
     */
    public void unionpay(String token, String paytype, String keytype,
                         String keyid, String total_fee, String coupon_id) {
        MyHttpInformation information = MyHttpInformation.UNIONPAY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("paytype", paytype);// 支付类型 固定传2
        params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
        params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
        params.put("total_fee", total_fee);// 支付交易金额,单位：元(测试时统一传递0.01元)
        params.put("coupon_id", coupon_id);// 没有时传0
        MyNetTask task = new UnionTradeTask(information, params);
        executeTask(task);
    }

    /**
     * 获取微信预支付交易会话标识(内含我方交易单号)接口
     *
     * @param token
     * @param paytype   支付类型 固定传2
     * @param keytype   业务类型 1：账户余额充值 2：商品立即购买
     * @param keyid     业务相关id 当keytype=1时, keyid=0 当keytype=2时, keyid=blog_id
     * @param total_fee 支付交易金额 单位：元(测试时统一传递0.01元)
     * @param coupon_id 优惠券id	没有时传0
     */
    public void weixinPay(String token, String paytype, String keytype,
                          String keyid, String total_fee, String coupon_id) {
        MyHttpInformation information = MyHttpInformation.WEIXINPAY_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("paytype", paytype);// 支付类型 固定传2
        params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
        params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
        params.put("total_fee", total_fee);// 支付交易金额,单位：元(测试时统一传递0.01元)
        params.put("coupon_id", coupon_id);// 没有时传0
        MyNetTask task = new WeixinTradeTask(information, params);
        executeTask(task);
    }

    /**
     * 获取消息通知列表接口
     *
     * @param token
     * @param page
     */
    public void noticeList(String token, String page) {
        MyHttpInformation information = MyHttpInformation.NOTICE_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("page", page);//
        MyNetTask task = new NoticeListTask(information, params);
        executeTask(task);
    }

    /**
     * 消息操作接口
     *
     * @param keytype 关联类型 1.消息2.留言
     * @param id      id 多个可用逗号分开
     */
    public void noticeOperate(String keytype, String id) {
        MyHttpInformation information = MyHttpInformation.NOTICE_OPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);//
        params.put("id", id);//
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 留言回复列表接口
     *
     * @param token
     * @param page
     */
    public void wordsList(String token, String page) {
        MyHttpInformation information = MyHttpInformation.WORDS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("page", page);//
        MyNetTask task = new WordsListTask(information, params);
        executeTask(task);
    }

    /**
     * 商品列表接口
     *
     * @param keytype 关联类型 1.店铺下的商品2.收藏下的商品3.购物记录4.足迹5.发现中最新商品6.海外7.限时8.预售9.买手推荐10.搜索关键字
     * @param token   登陆令牌 keytype=1 时可不传
     * @param keyid   keytype=1:店铺id
     *                keytype=3,用户id
     *                keytype=10,关键字
     *                其余传0
     * @param page
     */
    public void goodsList(String keytype, String token, String keyid, String page) {
        MyHttpInformation information = MyHttpInformation.GOODS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);//
        params.put("token", token);//
        params.put("keyid", keyid);//
        params.put("page", page);//
        MyNetTask task = new GoodsListTask(information, params);
        executeTask(task);
    }

    /**
     * 商品列表接口
     *
     * @param keytype 关联类型 2.收藏下的商品 4.足迹
     * @param token   登陆令牌 keytype=1 时可不传
     * @param keyid
     * @param page
     */
    public void goodsBigList(String token, String keytype, String keyid, String page) {
        MyHttpInformation information = MyHttpInformation.GOODS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);//
        params.put("token", token);//
        params.put("keyid", keyid);//
        params.put("page", page);//
        MyNetTask task = new GoodsBigListTask(information, params);
        executeTask(task);
    }

    /**
     * 获取收藏和订阅帖子列表信息接口
     *
     * @param token
     * @param keytype 关联类型 1.收藏2.订阅3.足迹
     * @param page    当前列表翻页索引 第一页时请传递page=0，翻页时依次递增。
     */
    public void blogList(String token, String keytype, String page) {
        MyHttpInformation information = MyHttpInformation.BLOG_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("keytype", keytype);//
        params.put("page", page);//
        MyNetTask task = new NoteListTask(information, params);
        executeTask(task);
    }

    /**
     * 足迹删除接口
     *
     * @param token
     * @param id      商品或帖子id
     * @param keytype 关联类型 1删除单条2清空
     * @param type    关联类型 1.商品2.贴子
     */
    public void recordRemove(String token, String id, String keytype,
                             String type) {
        MyHttpInformation information = MyHttpInformation.RECORD_REMOVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("id", id);//
        params.put("keytype", keytype);//
        params.put("type", type);//
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 添加评论接口
     *
     * @param token
     * @param keytype 业务类型 keytype=2：帖子模型，只允许文字
     * @param keyid   主键id 当keytype=2时：keyid=帖子id
     * @param content 回复内容
     */
    public void replyAdd(String token, String keytype, String keyid,
                         String content) {
        MyHttpInformation information = MyHttpInformation.REPLY_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("keytype", keytype);//
        params.put("keyid", keyid);//
        params.put("content", content);//
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 评论列表接口
     *
     * @param keytype 业务类型 keytype=2：帖子模型 keytype=3：商品模型
     * @param keyid   主键id 当keytype=2时：keyid=帖子id keytype=3：商品模型 其余依次扩展...
     * @param page
     */
    public void replyList(String keytype, String keyid, String page) {
        MyHttpInformation information = MyHttpInformation.REPLY_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);//
        params.put("keyid", keyid);//
        params.put("page", page);//
        MyNetTask task = new ReplyListTask(information, params);
        executeTask(task);
    }

    /**
     * 修改并保存支付密码接口
     *
     * @param token        登录令牌
     * @param temp_token   临时令牌
     * @param new_password 新密码
     */
    public void payPasswordSave(String token, String temp_token,
                                String new_password) {
        MyHttpInformation information = MyHttpInformation.PAYPASSWORD_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("temp_token", temp_token);//
        params.put("new_password", MyUtil.encryptPwd(new_password, MyConfig.USE_MD5));//
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 订单列表接口
     *
     * @param token   登录令牌
     * @param keytype 关联类型	0.全部 1.待付款 2.待发货 3.待收货 4.待评价 5.已关闭
     */
    public void billList(String token, String keytype, String page) {
        MyHttpInformation information = MyHttpInformation.BILL_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("keytype", keytype);//
        params.put("page", page);//
        MyNetTask task = new BillListTask(information, params);
        executeTask(task);
    }

    /**
     * 订单操作接口
     *
     * @param token
     * @param keytype 关联类型	1取消订单2删除订单3确认收货4提醒发货
     * @param id      订单id
     */
    public void billOperate(String token, String keytype, String id) {
        MyHttpInformation information = MyHttpInformation.BILL_OPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("keytype", keytype);//
        params.put("id", id);//
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 余额支付接口
     *
     * @param token
     * @param keyid       订单id
     * @param coupon_id   优惠券id	没有时传0
     * @param paypassword 支付密码
     */
    public void feeAccountPay(String token, String keyid, String keytype, String coupon_id, String paypassword) {
        MyHttpInformation information = MyHttpInformation.FEEACCOUNT_PAY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("keyid", keyid);//
        params.put("keytype", keytype);//
        params.put("coupon_id", coupon_id);//
        params.put("paypassword", MyUtil.encryptPwd(paypassword, MyConfig.USE_MD5));//要加密的
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 添加购物车接口
     *
     * @param token
     * @param buycount 购买数量
     * @param rule_id  商品规格id
     * @param keyid    商品id
     */
    public void cartAdd(String token, String buycount, String rule_id, String keyid) {
        MyHttpInformation information = MyHttpInformation.CART_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("buycount", buycount);//
        params.put("rule_id", rule_id);//
        params.put("keyid", keyid);//
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 购物车商品数量接口
     *
     * @param token
     */
    public void cartGet(String token) {
        MyHttpInformation information = MyHttpInformation.CART_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        MyNetTask task = new CartGetTask(information, params);
        executeTask(task);
    }

    /**
     * 订单保存接口
     *
     * @param token
     * @param keytype    关联类型	1.从购物车购买2.直接购买
     * @param keyid      关联id
     *                   当keytype=1时：keyid=cart_id (多个用逗号分隔)
     *                   当keytype=2时：keyid=blog_id,rule_id,buycount (用逗号分隔)
     * @param address_id 地址id
     */
    public void billSave(String token, String keytype, String keyid, String address_id, String type) {
        MyHttpInformation information = MyHttpInformation.BILL_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("keytype", keytype);//
        params.put("keyid", keyid);//
        params.put("address_id", address_id);//
        params.put("type", type);
        MyNetTask task = new BillSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 购物车列表接口
     *
     * @param token
     */
    public void cartList(String token) {
        MyHttpInformation information = MyHttpInformation.CART_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        MyNetTask task = new CartListTask(information, params);
        executeTask(task);
    }

    /**
     * 购物车操作接口
     *
     * @param token
     * @param keytype     关联类型
     *                    1.单个删除
     *                    2.清空购物车
     *                    3.更改数量
     *                    4.按店铺名删除
     * @param id          商品id	1，3情况时用到，其他情况无需传
     * @param merchant_id 店铺id	4时用到，其余无需传
     * @param buycount    购买数量	3.时用到，其余无需传
     */
    public void cartSaveOoperate(String token, String keytype, String id,
                                 String merchant_id, String buycount) {

        MyHttpInformation information = MyHttpInformation.CART_SAVEOPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);//
        params.put("id", id);//
        params.put("merchant_id", merchant_id);
        params.put("buycount", buycount);//
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 订单详情接口
     *
     * @param token
     * @param id    订单id
     */
    public void billGet(String token, String id, String keytype) {
        MyHttpInformation information = MyHttpInformation.BILL_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("id", id);//
        params.put("keytype", keytype);//
        MyNetTask task = new BillGetTask(information, params);
        executeTask(task);
    }

    /**
     * 银行和退款列表接口
     *
     * @param keytype 关联类型	1.银行2.退款理由
     */
    public void generalList(String keytype) {
        MyHttpInformation information = MyHttpInformation.GENERAL_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        MyNetTask task = new GeneralListTask(information, params);
        executeTask(task);
    }

    /**
     * 退款接口
     *
     * @param token
     * @param id          退款id	必传
     * @param reason      退款理由	 必传，直接传文字
     * @param description 描述	非必传
     */
    public void refund(String token, String id, String reason, String description) {
        MyHttpInformation information = MyHttpInformation.REFUND;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("id", id);//
        params.put("reason", reason);//
        params.put("description", description);//
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 退款列表接口
     *
     * @param token
     * @param page
     */
    public void refundList(String token, String keytype, String keyword, String page) {
        MyHttpInformation information = MyHttpInformation.REFUND_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyword", keyword);
        params.put("page", page);//
        MyNetTask task = new RefundListTask(information, params);
        executeTask(task);
    }

    /**
     * 退款详情接口
     *
     * @param token
     * @param id
     */
    public void refundGet(String token, String id, String keytype) {
        MyHttpInformation information = MyHttpInformation.REFUND_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);//
        params.put("keytype", keytype);//
        MyNetTask task = new RefundGetTask(information, params);
        executeTask(task);
    }


    /**
     * 分类商品列表接口
     *
     * @param type_id  分类id
     * @param good     按好评划分	0.未选1.按好评递减2.按好评递增
     * @param paycount 销量	0.未选1.按销量递减2.按销量递增
     * @param price    价格	0.未选1.按价格递减2.按价格递增
     * @param page
     */
    public void typeGoodsList(String type_id, String good, String paycount, String price, String page) {
        MyHttpInformation information = MyHttpInformation.TYPE_GOODS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("type_id", type_id);
        params.put("good", good);//
        params.put("paycount", paycount);
        params.put("price", price);//
        params.put("page", page);
        MyNetTask task = new TypeGoodsListTask(information, params);
        executeTask(task);
    }

    /**
     * 月销量列表接口
     */
    public void monthSale() {
        MyHttpInformation information = MyHttpInformation.MONTH_SALE;
        HashMap<String, String> params = new HashMap<>();
        MyNetTask task = new TypeGoodsListTask(information, params);
        executeTask(task);
    }

    /**
     * 商品评价接口
     *
     * @param token   登录令牌
     * @param id      此id在订单详情中已标出
     * @param content
     * @param star    星级	几颗星就传几 整数
     */
    public void blogReplyAdd(String token, String id, String content, String star) {
        MyHttpInformation information = MyHttpInformation.BLOG_REPLY_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("content", content);
        params.put("star", star);
        MyNetTask task = new CurrentStringTask(information, params);
        executeTask(task);
    }

    /**
     * 添加评论接口
     *
     * @param token   登录令牌
     * @param id      此id在订单详情中已标出
     * @param content
     * @param star    星级	几颗星就传几 整数
     */
    public void groupReplyAdd(String token, String id, String content, String star) {
        MyHttpInformation information = MyHttpInformation.GROUP_REPLY_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("content", content);
        params.put("star", star);
        MyNetTask task = new CurrentStringTask(information, params);
        executeTask(task);
    }

    /**
     * 商品团购详情接口
     *
     * @param blog_id 商品id
     */
    public void groupGet(String blog_id) {
        MyHttpInformation information = MyHttpInformation.GROUP_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("blog_id", blog_id);
        MyNetTask task = new GroupGetTask(information, params);
        executeTask(task);
    }

    /**
     * 开团到微信好友
     *
     * @param token
     * @param group_id
     */
    public void groupBillAdd(String token, String group_id) {
        MyHttpInformation information = MyHttpInformation.GROUP_BILL_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("group_id", group_id);
        MyNetTask task = new CurrentStringTask(information, params);
        executeTask(task);
    }

    /**
     * 第三方登录接口
     *
     * @param thirdtype 平台类型	1：微信 2：QQ 3：微博
     * @param thirduid  平台用户id	该平台唯一的id
     * @param avatar    平台用户头像	图片地址
     * @param nickname  平台用户昵称
     * @param sex       性别	"男"或"女"
     */
    public void thirdSave(String thirdtype, String thirduid, String avatar,
                          String nickname, String sex, String unionid) {
        MyHttpInformation information = MyHttpInformation.THIRD_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("devicetype", "2");//1：苹果 2：安卓（方便服务器运维统计）
        params.put("lastloginversion", MyUtil.getAppVersion(mContext));
        params.put("thirdtype", thirdtype);
        params.put("thirduid", thirduid);
        params.put("avatar", avatar);
        params.put("nickname", nickname);
        params.put("sex", sex);
        params.put("unionid", unionid);
        MyNetTask task = new ClientLoginTask(information, params);
        executeTask(task);
    }

    /**
     * 团购订单列表接口
     *
     * @param token   登录令牌
     * @param keytype 关联类型	0.全部 1.待付款 2.待发货 3.待收货 4.待评价 5.已关闭
     * @param page
     */
    public void groupBillList(String token, String keytype, String flag, String page) {
        MyHttpInformation information = MyHttpInformation.GROUP_BILL_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("flag", flag);
        params.put("page", page);
        MyNetTask task = new GroupBillListTask(information, params);
        executeTask(task);
    }

    /**
     * 团购订单详情接口
     *
     * @param token
     * @param id    订单id
     */
    public void groupBillGet(String token, String id, String flag) {
        MyHttpInformation information = MyHttpInformation.GROUP_BILL_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("flag", flag);
        MyNetTask task = new GroupBillGetTask(information, params);
        executeTask(task);
    }

    /**
     * 团购订单操作接口
     *
     * @param token
     * @param keytype 关联类型	2删除订单3确认收货4提醒发货
     * @param id      团购订单id
     */
    public void groupBillOperate(String token, String keytype, String id) {
        MyHttpInformation information = MyHttpInformation.GROUP_BILL_OPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("id", id);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 后台商铺列表接口
     *
     * @param page
     */
    public void merchantList(String page) {
        MyHttpInformation information = MyHttpInformation.MERCHANT_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        MyNetTask task = new MerchantListTask(information, params);
        executeTask(task);
    }

    /**
     * 商品分类列表接口
     */
    public void typeList() {
        MyHttpInformation information = MyHttpInformation.TYPE_LIST;
        HashMap<String, String> params = new HashMap<>();
        MyNetTask task = new DiscoveryTypeListTask(information, params);
        executeTask(task);
    }

    /**
     * 红点是否显示接口
     *
     * @param token
     */
    public void redDisplay(String token) {
        MyHttpInformation information = MyHttpInformation.RED_DISPLAY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        MyNetTask task = new RedDisplayTask(information, params);
        executeTask(task);
    }

    /**
     * 订单红点是否显示接口
     *
     * @param token
     */
    public void billRed(String token) {
        MyHttpInformation information = MyHttpInformation.BILL_RED;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        MyNetTask task = new BillRedTask(information, params);
        executeTask(task);
    }

    /**
     * 抵用券列表接口
     *
     * @param token
     * @param fee   金额	0和非0
     * @param page
     */
    public void couponList(String token, String fee, String page) {
        MyHttpInformation information = MyHttpInformation.COUPON_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("fee", fee);
        params.put("page", page);
        MyNetTask task = new CouponListTask(information, params);
        executeTask(task);
    }

    /**
     * 交易明细表接口
     *
     * @param token
     * @param page
     */
    public void accountList(String token, String page) {
        MyHttpInformation information = MyHttpInformation.ACCOUNT_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("page", page);
        MyNetTask task = new AccountListTask(information, params);
        executeTask(task);
    }

    /**
     * 第三方登录编辑信息接口
     *
     * @param token
     * @param temp_token    临时token
     * @param nickname      昵称	允许为空
     * @param sex           性别	允许为空
     * @param district_name 地区	允许为空
     * @param inviter_code  邀请码	必填
     */
    public void editThird(String token, String temp_token, String nickname, String sex,
                          String district_name, String inviter_code) {
        MyHttpInformation information = MyHttpInformation.EDIT_THIRD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("temp_token", temp_token);
        params.put("nickname", nickname);
        params.put("sex", sex);
        params.put("district_name", district_name);
        MyNetTask task = new EditThirdTask(information, params);
        executeTask(task);
    }

    /**
     * 月粉丝排行接口
     */
    public void fans() {
        MyHttpInformation information = MyHttpInformation.FANS;
        HashMap<String, String> params = new HashMap<>();
        MyNetTask task = new FansTask(information, params);
        executeTask(task);
    }

    /**
     * 抵用券接口
     *
     * @param fee
     */
    public void returnCoupon(String fee) {
        MyHttpInformation information = MyHttpInformation.RETURN_COUPON;
        HashMap<String, String> params = new HashMap<>();
        params.put("fee", fee);
        MyNetTask task = new ReturnCouponTask(information, params);
        executeTask(task);
    }

    /**
     * 银行列表接口
     */
    public void bankList() {
        MyHttpInformation information = MyHttpInformation.BANK_LIST;
        HashMap<String, String> params = new HashMap<>();
        MyNetTask task = new BankListTask(information, params);
        executeTask(task);
    }

    /**
     * 银行信息添加接口
     *
     * @param token
     * @param keytype     关联类型	1.支付宝 2.银行卡
     * @param bankname
     * @param bankuser
     * @param bankaddress
     * @param bankcard
     * @param alipay
     */
    public void bankAdd(String token, String keytype, String bankname, String bankuser,
                        String bankaddress, String bankcard, String alipay) {
        MyHttpInformation information = MyHttpInformation.BANK_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("bankname", bankname);
        params.put("bankuser", bankuser);
        params.put("bankaddress", bankaddress);
        params.put("bankcard", bankcard);
        params.put("alipay", alipay);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 余额提现接口
     *
     * @param token
     * @param keytype     类型	1.支付宝 2.银行卡
     * @param totalfee
     * @param paypassword
     */
    public void cashAdd(String token, String keytype, String totalfee, String paypassword) {
        MyHttpInformation information = MyHttpInformation.CASH_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("totalfee", totalfee);
        params.put("paypassword", MyUtil.encryptPwd(paypassword, MyConfig.USE_MD5));
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 地区列表接口
     *
     * @param parentid 0：表示获取第一级别（省份或直辖市或自治区）
     *                 -1：表示获取所有地级以上级别城市（含地级）
     */
    public void districtList(String parentid) {
        MyHttpInformation information = MyHttpInformation.DIS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("parentid", parentid);
        MyNetTask task = new DistrictListTask(information, params);
        executeTask(task);
    }


}

