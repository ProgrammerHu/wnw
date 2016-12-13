package com.hemaapp.wnw;

import android.content.Context;

import com.hemaapp.wnw.nettask.AccountListTask;
import com.hemaapp.wnw.nettask.AttrListTask;
import com.hemaapp.wnw.nettask.BillListTask;
import com.hemaapp.wnw.nettask.BlogAddTask;
import com.hemaapp.wnw.nettask.CountListTask;
import com.hemaapp.wnw.nettask.CountryListTask;
import com.hemaapp.wnw.nettask.CurrentTask;
import com.hemaapp.wnw.nettask.FansListTask;
import com.hemaapp.wnw.nettask.GroupListTask;
import com.hemaapp.wnw.nettask.GroupTypeListTask;
import com.hemaapp.wnw.nettask.MerchantAccountListTask;
import com.hemaapp.wnw.nettask.MerchantBillRedTask;
import com.hemaapp.wnw.nettask.MerchantBlogGetTask;
import com.hemaapp.wnw.nettask.MerchantGoodsListTask;
import com.hemaapp.wnw.nettask.MyCodeTask;
import com.hemaapp.wnw.nettask.TimeGetTask;
import com.hemaapp.wnw.nettask.TimeListTask;
import com.hemaapp.wnw.nettask.VipListTask;

import java.util.HashMap;

/**
 * 网络请求工具类
 */
public class MyNetWorker extends MyNetWorkerV100 {

    public MyNetWorker(Context mContext) {
        super(mContext);
    }

    /**
     * 帖子添加接口
     *
     * @param token
     * @param title
     * @param content
     * @param keyid    关联id	如果是编辑需传，新增传0
     * @param table_id 标签id	例如：3,5
     * @param good_id  商品id	例如：3,5
     * @param flag     类型	1.草稿2.发布
     */
    public void noteAdd(String token, String title, String content, String memo, String keyid,
                        String table_id, String good_id, String flag, String imgurl_str) {
        MyHttpInformation information = MyHttpInformation.NOTE_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("title", title);
        params.put("content", content);
        params.put("keyid", keyid);
        params.put("table_id", table_id);
        params.put("good_id", good_id);
        params.put("flag", flag);
        params.put("memo", memo);
        params.put("imgurl_str", imgurl_str);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 帖子操作接口
     *
     * @param token
     * @param keytype 1.更新帖子
     *                2.推荐精华
     *                3.添加商品
     *                4.已发布帖子删除
     *                5.草稿帖子删除
     *                6.发布草稿帖子
     * @param keyid
     * @param good_id 只有keytype=4时传，格式1,2,3 其他传0
     */
    public void noteOperate(String token, String keytype, String keyid, String good_id) {
        MyHttpInformation information = MyHttpInformation.NOTE_OPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("good_id", good_id);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 我的二维码
     *
     * @param token
     */
    public void myCode(String token) {
        MyHttpInformation information = MyHttpInformation.MY_CODE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        MyNetTask task = new MyCodeTask(information, params);
        executeTask(task);
    }

    /**
     * 海外城市列表接口
     */
    public void countryList() {
        MyHttpInformation information = MyHttpInformation.COUNTRY_LIST;
        HashMap<String, String> params = new HashMap<>();
        MyNetTask task = new CountryListTask(information, params);
        executeTask(task);
    }

    /**
     * @param token
     * @param keyid       关联id	如果是编辑需传，新增传0，必填
     * @param keytype     关联类型	1.普通3.抢购4.预售
     * @param saleflag    上架状态	0.不上1.上架
     * @param name
     * @param type_id     类别
     * @param attr        规格	json，非必填,如果编辑时照片没改，可不传
     * @param oldprice
     * @param expressfee  运费
     * @param country_id  国家id	没有传0
     * @param content
     * @param imgurl_str  图片拼串	格式“大图,小图;大图,小图” 非必填,如果编辑时照片没改，可不传
     * @param time        时间	keytype=3：具体时间，keytype=4,发货时间 非必填
     * @param time_id     档期id	keytype=3时需要
     * @param limit_count 限购数量
     * @param oldid       原商品id	没有填0
     */
    public void blogAdd(String token, String keyid, String keytype, String saleflag, String name,
                        String type_id, String attr, String oldprice, String expressfee,
                        String country_id, String content, String imgurl_str, String time,
                        String time_id, String limit_count, String oldid) {
        MyHttpInformation information = MyHttpInformation.BLOG_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keyid", keyid);
        params.put("keytype", keytype);
        params.put("saleflag", saleflag);
        params.put("name", name);
        params.put("type_id", type_id);
        params.put("attr", attr);
        params.put("oldprice", oldprice);
        params.put("expressfee", expressfee);
        params.put("country_id", country_id);
        params.put("content", content);
        params.put("imgurl_str", imgurl_str);
        params.put("time", time);
        params.put("time_id", time_id);
        params.put("limit_count", limit_count);
        params.put("oldid", oldid);
        MyNetTask task = new BlogAddTask(information, params);
        executeTask(task);
    }

    /**
     * 商家商品列表接口
     *
     * @param token
     * @param keytype  关联类型	1.普通商品2.团购商品3.抢购4.预售
     * @param keyid
     * @param keywords
     * @param page
     */
    public void merchantGoodsList(String token, String keytype, String keyid, String keywords, String page) {
        MyHttpInformation information = MyHttpInformation.MERCHANT_GOODS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keyid", keyid);
        params.put("keytype", keytype);
        params.put("keywords", keywords);
        params.put("page", page);
        MyNetTask task = new MerchantGoodsListTask(information, params);
        executeTask(task);
    }

    /**
     * 商家获取商品详情信息接口
     *
     * @param token
     * @param id
     */
    public void merchantBlogGet(String token, String id) {
        MyHttpInformation information = MyHttpInformation.MERCHANT_BLOG_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        MyNetTask task = new MerchantBlogGetTask(information, params);
        executeTask(task);
    }

    /**
     * 属性规格列表接口
     *
     * @param token
     * @param id
     */
    public void attrList(String token, String id) {
        MyHttpInformation information = MyHttpInformation.ATTR_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        MyNetTask task = new AttrListTask(information, params);
        executeTask(task);
    }

    /**
     * 属性保存接口
     *
     * @param token
     * @param attr
     */
    public void attrSave(String token, String attr) {
        MyHttpInformation information = MyHttpInformation.ATTR_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("attr", attr);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 团类型列表接口
     */
    public void groupTypeList() {
        MyHttpInformation information = MyHttpInformation.GROUP_TYPE_LIST;
        HashMap<String, String> params = new HashMap<>();
        MyNetTask task = new GroupTypeListTask(information, params);
        executeTask(task);
    }

    /**
     * 添加团购接口
     *
     * @param token
     * @param blog_id     商品id	编辑可不传
     * @param type_id     关联类型	用逗号拼接，编辑可不传
     * @param attr_id     规格id	编辑可不传
     * @param hour        时间间隔
     * @param group_price 团购价格
     * @param keyid       关联id	新增时传0
     */
    public void groupAdd(String token, String blog_id, String type_id, String attr_id,
                         String hour, String group_price, String keyid) {
        MyHttpInformation information = MyHttpInformation.GROUP_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("blog_id", blog_id);
        params.put("type_id", type_id);
        params.put("attr_id", attr_id);
        params.put("hour", hour);
        params.put("group_price", group_price);
        params.put("keyid", keyid);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 团类型列表接口
     *
     * @param blog_id 商品id
     */
    public void groupList(String blog_id) {
        MyHttpInformation information = MyHttpInformation.GROUP_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("blog_id", blog_id);
        MyNetTask task = new GroupListTask(information, params);
        executeTask(task);
    }

    /**
     * 团购删除接口
     *
     * @param token
     * @param id
     */
    public void groupDelete(String token, String id) {
        MyHttpInformation information = MyHttpInformation.GROUP_DELETE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 团购取消接口
     *
     * @param token
     * @param blog_id
     */
    public void groupCancel(String token, String blog_id) {
        MyHttpInformation information = MyHttpInformation.GROUP_CANCEL;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("blog_id", blog_id);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 获取档期接口
     */
    public void timeGet() {
        MyHttpInformation information = MyHttpInformation.TIME_GET;
        HashMap<String, String> params = new HashMap<>();
        MyNetTask task = new TimeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 商品删除接口
     *
     * @param token
     * @param blog_id
     */
    public void blogDelete(String token, String blog_id) {
        MyHttpInformation information = MyHttpInformation.BLOG_DELETE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("blog_id", blog_id);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 商家分类数量接口
     *
     * @param token
     * @param keytype 1.普通3.限时抢购4.预售
     */
    public void countList(String token, String keytype) {
        MyHttpInformation information = MyHttpInformation.COUNT_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        MyNetTask task = new CountListTask(information, params);
        executeTask(task);
    }

    /**
     * 档期列表接口
     */
    public void timeList() {
        MyHttpInformation information = MyHttpInformation.TIME_LIST;
        HashMap<String, String> params = new HashMap<>();
        MyNetTask task = new TimeListTask(information, params);
        executeTask(task);
    }

    /**
     * 商家订单红点是否显示接口
     *
     * @param token
     */
    public void merchantBillRed(String token) {
        MyHttpInformation information = MyHttpInformation.MERCHANT_BILL_RED;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        MyNetTask task = new MerchantBillRedTask(information, params);
        executeTask(task);
    }

    /**
     * 商家订单列表接口
     *
     * @param token
     * @param keytype 0.已完成
     *                1.待付款
     *                2.待发货
     *                3.待收货
     *                4.待评价
     *                5.已关闭
     * @param keyword
     * @param page
     */
    public void merchantBillList(String token, String keytype, String keyword, String page) {
        MyHttpInformation information = MyHttpInformation.MERCHANT_BILL_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyword", keyword);
        params.put("page", page);
        MyNetTask task = new BillListTask(information, params);
        executeTask(task);
    }

    /**
     * 商家订单操作接口
     *
     * @param token
     * @param keytype       1发货2删除订单
     * @param id
     * @param shipping_name
     * @param shipping_num
     */
    public void merchantBillOperate(String token, String keytype, String id, String shipping_name, String shipping_num) {
        MyHttpInformation information = MyHttpInformation.MERCHANT_BILL_OPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("id", id);
        params.put("shipping_name", shipping_name);
        params.put("shipping_num", shipping_num);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 会员列表接口
     *
     * @param token
     * @param date_start
     * @param date_end
     * @param district
     * @param page
     */
    public void vipList(String token, String date_start, String date_end, String district, String keyword, String page) {
        MyHttpInformation information = MyHttpInformation.VIP_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("date_start", date_start);
        params.put("date_end", date_end);
        params.put("district", district);
        params.put("keyword", keyword);
        params.put("page", page);
        MyNetTask task = new VipListTask(information, params);
        executeTask(task);
    }

    /**
     * 入账记录接口
     *
     * @param token
     * @param date_start
     * @param date_end
     * @param page
     */
    public void merchantAccountList(String token, String date_start, String date_end, String page) {
        MyHttpInformation information = MyHttpInformation.MERCHANT_ACCOUNT_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("date_start", date_start);
        params.put("date_end", date_end);
        params.put("page", page);
        MyNetTask task = new MerchantAccountListTask(information, params);
        executeTask(task);
    }

    /**
     * 商家回复评论接口
     *
     * @param token
     * @param id
     * @param content
     */
    public void answerAdd(String token, String id, String content) {
        MyHttpInformation information = MyHttpInformation.ANSWER_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("content", content);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 删除评论接口
     *
     * @param token
     * @param id
     */
    public void replyDelete(String token, String id) {
        MyHttpInformation information = MyHttpInformation.REPLY_DELETE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 商家团购订单操作接口
     *
     * @param token
     * @param keytype       关联类型	1发货2删除订单
     * @param id
     * @param shipping_name
     * @param shipping_num
     */
    public void merchantGroupOperate(String token, String keytype, String id, String shipping_name, String shipping_num) {
        MyHttpInformation information = MyHttpInformation.MERCHANT_GROUP_OPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("id", id);
        params.put("shipping_name", shipping_name);
        params.put("shipping_num", shipping_num);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 商家退款操作接口
     *
     * @param token
     * @param id
     * @param itemtype 2.成功3.失败
     */
    public void merchantRefundOperate(String token, String id, String itemtype) {
        MyHttpInformation information = MyHttpInformation.MERCHANT_REFUND_OPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("itemtype", itemtype);
        params.put("memo", "");
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 商家退款删除接口
     */
    public void refundDelete(String token, String id) {
        MyHttpInformation information = MyHttpInformation.REFUND_DELETE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 粉丝列表接口
     *
     * @param token
     * @param page
     */
    public void fansList(String token, String page) {
        MyHttpInformation information = MyHttpInformation.FANS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("page", page);
        MyNetTask task = new FansListTask(information, params);
        executeTask(task);
    }

    /**
     * 留言回复接口
     *
     * @param token
     * @param id
     * @param content
     */
    public void wordsReply(String token, String id, String content) {
        MyHttpInformation information = MyHttpInformation.WORDS_REPLY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("content", content);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }

    /**
     * 修改上级接口
     *
     * @param token
     * @param mobile
     */
    public void invitorSave(String token, String mobile) {
        MyHttpInformation information = MyHttpInformation.INVITER_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("mobile", mobile);
        MyNetTask task = new CurrentTask(information, params);
        executeTask(task);
    }
}