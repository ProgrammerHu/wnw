package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.luna_framework.view.CircleIndicator.CircleIndicator;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.GoodsDetailListAdapter;
import com.hemaapp.wnw.adapter.pager.ImageBannerPagerAdapter_Goods;
import com.hemaapp.wnw.dialog.GoodsSelectDialog;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.MyOneButtonDialog.OnButtonListener;
import com.hemaapp.wnw.model.CartListModel;
import com.hemaapp.wnw.model.GoodsDetailModel;
import com.hemaapp.wnw.model.ReplyCommentModel;
import com.hemaapp.wnw.result.ReplyListResult;
import com.hemaapp.wnw.util.ShareParams;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 商品详情页
 * Created by Hufanglin on 2016/2/23.
 */
public class GoodsDetailActivity extends MyFragmentActivity implements
        View.OnClickListener {

    private ImageView imageQuitActivity, imageShare;
    private TextView txtTitle;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private ViewPager viewPager;
    private ImageBannerPagerAdapter_Goods bannerAdapter;
    private CircleIndicator indicator;

    private ImageView imageToTop, imageGroup, imageChat,
            imageCollect, imageLeft, imageRight;
    private TextView txtTag, txtGoodsName, txtPrize, txtOldPrize, txtHasSell,
            txtStocks, txtLeft, txtRight, txtBuyNow, txtAddCar, txtCartCount, txtSendTime;
//    private TextView txtName, txtFansCount;

    private View layoutLeft, layoutRight, layoutLeftTop, layoutRightTop, layoutCartCount, layoutShopCar, layoutSendTime,
            line1, line2, line3;
    private GoodsDetailListAdapter adapter;

    private GoodsSelectDialog goodsSelectDialog;
    private GoodsDetailModel goodsDetailModel;// 记录数据
    private List<GoodsDetailModel.ImgItemsEntity> listImage = new ArrayList<>();
    private ArrayList<ReplyCommentModel> replyList = new ArrayList<>();

    private int page = 0;
    private String goods_id;
    private boolean ShowDialog = false;
    private boolean isLeft = true;
    private boolean beginFlashSale;//给限时抢购用的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goodsdetail);
        super.onCreate(savedInstanceState);
        if (isNull(goods_id)) {
            showTextDialog("id空了");
            return;
        }
        String token = "";
        if (MyUtil.IsLogin(this)) {
            token = getApplicationContext().getUser().getToken();
            getNetWorker().recordAdd(token, "0", goods_id);
        }
        showProgressDialog(R.string.loading);
        getNetWorker().blogGet(token, goods_id);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (isNull(goods_id)) {
            showTextDialog("id空了");
            return;
        }
        String token = "";
        if (MyUtil.IsLogin(this)) {
            token = getApplicationContext().getUser().getToken();
            getNetWorker().recordAdd(token, "0", goods_id);
        }
        showProgressDialog(R.string.loading);
        getNetWorker().blogGet(token, goods_id);
        refreshLoadmoreLayout.setLoadmoreable(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (MyUtil.IsLogin(this)) {//如果登录了，再去请求购物车数量
            getNetWorker().cartGet(getApplicationContext().getUser().getToken());
        }

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation infomation = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (infomation) {
            case LOVE_OPERATE:
                showProgressDialog(R.string.loading);
                break;
            case CART_ADD:
                showProgressDialog(R.string.committing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult hemaBaseResult) {
        MyHttpInformation infomation = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (infomation) {
            case BLOG_GET://获取商品详情->获取评论列表->获取购物车数量
                HemaArrayResult<GoodsDetailModel> result = (HemaArrayResult<GoodsDetailModel>) hemaBaseResult;
                goodsDetailModel = result.getObjects().get(0);
                setData();
                getNetWorker().replyList("3", goods_id, String.valueOf(page));

                break;
            case LOVE_OPERATE:
                cancelProgressDialog();
                String changeFlag = "0".equals(goodsDetailModel.getLove_flag()) ? "1"
                        : "0";
                goodsDetailModel.setLove_flag(changeFlag);
                setData();
                break;
            case CART_ADD:
                getNetWorker().cartGet(getApplicationContext().getUser().getToken());//获取购物车中商品的数量
                Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                break;
            case REPLY_LIST://获取评论列表
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                ReplyListResult replyListResult = (ReplyListResult) hemaBaseResult;
                if (page == 0) {
                    replyList.clear();
                }
                adapter.setTotalCount(replyListResult.getTotalCount());
                adapter.setLevel((float) replyListResult.getLevel());
                replyList.addAll(replyListResult.getListData());
                adapter.notifyDataSetChanged();

                if (MyUtil.IsLogin(this)) {//如果登录了，再去请求购物车数量
                    getNetWorker().cartGet(getApplicationContext().getUser().getToken());
                } else {//没登录，隐藏进度条，判断是否立即购买
                    cancelProgressDialog();
                    if (ShowDialog) {// 在列表中点击购买了
                        showGoodsSelectDialog(true);
                        ShowDialog = false;
                    }
                }
                if (isLeft) {
                    refreshLoadmoreLayout.setLoadmoreable(false);
                } else {
                    refreshLoadmoreLayout.setLoadmoreable(replyListResult.getListData().size() >=
                            getApplicationContext().getSysInitInfo().getSys_pagesize());
                }
                if (replyListResult.getListData().size() < getApplicationContext().getSysInitInfo().getSys_pagesize()
                        && page > 0) {
                    showNone("没有更多评论了");
                }
                page++;
                break;
            case CART_GET:
                cancelProgressDialog();
                if (ShowDialog) {// 在列表中点击购买了
                    showGoodsSelectDialog(true);
                    ShowDialog = false;
                }
                HemaArrayResult<String> cartResult = (HemaArrayResult<String>) hemaBaseResult;
                String goodscount = cartResult.getObjects().get(0);
                if (isNull(goodscount) || "0".equals(goodscount) || "null".equals(goodscount)) {//购物车是空的
                    layoutCartCount.setVisibility(View.GONE);
                } else {//设置购物车数量
                    layoutCartCount.setVisibility(View.VISIBLE);
                    int goodscountInt = Integer.parseInt(goodscount);
                    if (goodscountInt > 9) {
                        txtCartCount.setText(9 + "+");
                    } else {
                        txtCartCount.setText(goodscount);
                    }
                }
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
        if (hemaBaseResult.getError_code() == 404) {
            MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
            dialog.setTitle(R.string.goods_page)
                    .setText(hemaBaseResult.getMsg()).hideCancel().hideIcon()
                    .setButtonListener(new OnButtonListener() {

                        @Override
                        public void onCancelClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                            finish(R.anim.my_left_in, R.anim.right_out);
                        }

                        @Override
                        public void onButtonClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                            finish(R.anim.my_left_in, R.anim.right_out);
                        }
                    });
            dialog.show();
            return;
        }
        showMyOneButtonDialog(getResources().getString(R.string.goods_page),
                hemaBaseResult.getMsg());

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        imageShare = (ImageView) findViewById(R.id.imageShare);
        imageGroup = (ImageView) findViewById(R.id.imageGroup);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.goods_page);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        txtBuyNow = (TextView) findViewById(R.id.txtBuyNow);

        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new GoodsDetailListAdapter(mContext, listView, goods_id, replyList);

        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_goodsdetail, listView, false);
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);
//        imageHead = (ImageView) listView.findViewById(R.id.imageHead);
        imageChat = (ImageView) findViewById(R.id.imageChat);
        imageCollect = (ImageView) listView.findViewById(R.id.imageCollect);

        imageLeft = (ImageView) listView.findViewById(R.id.imageLeft);
        imageRight = (ImageView) listView.findViewById(R.id.imageRight);
        viewPager = (ViewPager) listView.findViewById(R.id.viewPager);
        bannerAdapter = new ImageBannerPagerAdapter_Goods(mContext, listImage);
        viewPager.setAdapter(bannerAdapter);
        indicator = (CircleIndicator) listView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        viewPager.getLayoutParams().height = 330 * MyUtil.getScreenWidth(mContext) / 640;

        txtTag = (TextView) listView.findViewById(R.id.txtTag);
        txtGoodsName = (TextView) listView.findViewById(R.id.txtGoodsName);
        txtPrize = (TextView) listView.findViewById(R.id.txtPrize);
        txtOldPrize = (TextView) listView.findViewById(R.id.txtOldPrize);
        txtOldPrize.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 加横线
        txtOldPrize.getPaint().setAntiAlias(true);
        txtHasSell = (TextView) listView.findViewById(R.id.txtHasSell);
        txtStocks = (TextView) listView.findViewById(R.id.txtStocks);
        txtLeft = (TextView) listView.findViewById(R.id.txtLeft);
        txtRight = (TextView) listView.findViewById(R.id.txtRight);
//        txtFansCount = (TextView) listView.findViewById(R.id.txtFansCount);
        txtAddCar = (TextView) findViewById(R.id.txtAddCar);
//        txtName = (TextView) findViewById(R.id.txtName);
        txtCartCount = (TextView) findViewById(R.id.txtCartCount);

        layoutLeft = listView.findViewById(R.id.layoutLeft);
        layoutRight = listView.findViewById(R.id.layoutRight);
        layoutLeftTop = listView.findViewById(R.id.layoutLeftTop);
        layoutRightTop = listView.findViewById(R.id.layoutRightTop);
        layoutCartCount = findViewById(R.id.layoutCartCount);
        layoutShopCar = findViewById(R.id.layoutShopCar);

        txtSendTime = (TextView) listView.findViewById(R.id.txtSendTime);
        layoutSendTime = listView.findViewById(R.id.layoutSendTime);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
    }

    @Override
    protected void getExras() {
        goods_id = mIntent.getStringExtra("goods_id");
        ShowDialog = mIntent.getBooleanExtra("ShowDialog", false);
        beginFlashSale = mIntent.getBooleanExtra("beginFlashSale", true);
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageShare.setOnClickListener(this);
        imageChat.setOnClickListener(this);
        imageCollect.setOnClickListener(this);
        layoutLeft.setOnClickListener(this);
        layoutRight.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        imageGroup.setOnClickListener(this);
        txtBuyNow.setOnClickListener(this);
        txtAddCar.setOnClickListener(this);
        layoutShopCar.setOnClickListener(this);
//        imageHead.setOnClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                int visibility = firstVisibleItem == 0 ? View.INVISIBLE
                        : View.VISIBLE;
                imageToTop.setVisibility(visibility);
            }
        });

        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                String token = "";
                if (MyUtil.IsLogin(GoodsDetailActivity.this)) {
                    token = getApplicationContext().getUser().getToken();
                    getNetWorker().recordAdd(token, "0", goods_id);
                }
                getNetWorker().blogGet(token, goods_id);
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().replyList("3", goods_id, String.valueOf(page));
            }
        });
        refreshLoadmoreLayout.setLoadmoreable(false);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                setResult(RESULT_OK);
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.imageShare:
                String client_id = "";
                if (MyUtil.IsLogin(this)) {
                    client_id = MyApplication.getInstance().getUser().getId();
                }
                new ShareParams(mContext, getApplicationContext()).DoShare("share", goods_id, client_id, goodsDetailModel.getImgurlbig(),
                        goodsDetailModel.getName(), MyApplication.getInstance().getSysInitInfo().getGoods_memo());
                break;
            case R.id.imageChat:
                if (!MyUtil.IsLogin(this)) {
                    gotoLogin();
                    return;
                }
                intent = new Intent(this, LeaveMessageActivity.class);
                intent.putExtra("id", goodsDetailModel.getMerchant_id());
                intent.putExtra("client_id", getApplicationContext().getUser()
                        .getId());
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.imageCollect:
                if (!MyUtil.IsLogin(this)) {
                    gotoLogin();
                    return;
                }
                String flag = "0".equals(goodsDetailModel.getLove_flag()) ? "1"
                        : "2";
                getNetWorker().loveOperate(
                        getApplicationContext().getUser().getToken(), "3", flag,
                        goods_id);
                break;
            case R.id.layoutLeft:// 点击左键
                isLeft = true;
                refreshLoadmoreLayout.setLoadmoreable(false);
                adapter.changeToLeft();
                layoutRightTop.setVisibility(View.INVISIBLE);
                layoutLeftTop.setVisibility(View.VISIBLE);
                imageLeft.setImageResource(R.drawable.icon_goods_check);
                imageRight.setImageResource(R.drawable.icon_goods_comment);
                txtLeft.setTextColor(getResources().getColor(R.color.main_purple));
                txtRight.setTextColor(getResources().getColor(R.color.grey_text));
                break;
            case R.id.layoutRight:// 点击右键
                isLeft = false;
                if (adapter.getCount() % getApplicationContext().getSysInitInfo().getSys_pagesize() == 0) {//还是有希望可以加载更多的
                    refreshLoadmoreLayout.setLoadmoreable(true);
                } else {
                    refreshLoadmoreLayout.setLoadmoreable(false);
                }
                adapter.changeToRight();
                layoutRightTop.setVisibility(View.VISIBLE);
                layoutLeftTop.setVisibility(View.INVISIBLE);
                imageLeft.setImageResource(R.drawable.icon_goods);
                imageRight.setImageResource(R.drawable.icon_goods_comment_check);
                txtLeft.setTextColor(getResources().getColor(R.color.grey_text));
                txtRight.setTextColor(getResources().getColor(R.color.main_purple));
                break;
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;
            case R.id.imageGroup:
                if (!MyUtil.IsLogin(this)) {
                    gotoLogin();
                    return;
                }
                intent = new Intent(this, OpenGroupActivity.class);
                intent.putExtra("id", goods_id);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.txtBuyNow:
                if (!beginFlashSale) {
                    showMyOneButtonDialog("商品详情", "抢购活动尚未开始");
                    return;
                }
                showGoodsSelectDialog(true);
                break;
            case R.id.txtAddCar:
                showGoodsSelectDialog(false);
                break;
            case R.id.layoutShopCar:
                if (!MyUtil.IsLogin(this)) {
                    gotoLogin();
                    return;
                }
                intent = new Intent(this, MyCartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.imageHead:
                intent = new Intent(this, SellerDetailActivity.class);
                intent.putExtra("client_id", goodsDetailModel.getMerchant_id());
                startActivity(intent);
                break;
        }

    }

    @Override
    protected boolean onKeyBack() {
        setResult(RESULT_OK);
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    /**
     * 显示选择参数对话框
     *
     * @param isBuyNow
     */
    private void showGoodsSelectDialog(boolean isBuyNow) {
        if (goodsDetailModel == null) {
            return;
        }
        if (goodsSelectDialog == null) {
            goodsSelectDialog = new GoodsSelectDialog(mContext,
                    goodsDetailModel);
            goodsSelectDialog.setImageUrl(goodsDetailModel.getImgurl(),
                    goodsDetailModel.getImgurlbig());
        }
        goodsSelectDialog.setIsBuyNow(isBuyNow);
        goodsSelectDialog.show();
    }

    /**
     * 充实数据
     */
    private void setData() {
        if (goodsDetailModel == null) {
            showMyOneButtonDialog(R.string.goods_page, R.string.data_null);
            return;
        }
        /*更新图片轮播*/
        listImage.clear();
        listImage.addAll(goodsDetailModel.getImgItems());
        bannerAdapter = new ImageBannerPagerAdapter_Goods(mContext, listImage);
        viewPager.setAdapter(bannerAdapter);
        indicator.setViewPager(viewPager);
        indicator.refreshDrawableState();

        txtGoodsName.setText(goodsDetailModel.getName());
        txtPrize.setText("￥" + goodsDetailModel.getPrice());
        txtOldPrize.setText("￥" + goodsDetailModel.getOldprice());
//        txtFansCount.setText(goodsDetailModel.getFans());
//        txtName.setText(goodsDetailModel.getNickname());
        txtHasSell.setText("已售" + goodsDetailModel.getPaycount());
        txtStocks.setText("库存" + goodsDetailModel.getLeftcount());
        int loveRes = "0".equals(goodsDetailModel.getLove_flag()) ? R.drawable.icon_collect
                : R.drawable.icon_collected;
        imageCollect.setImageResource(loveRes);

        int visible = "0".equals(goodsDetailModel.getGroup_flag()) ? View.INVISIBLE
                : View.VISIBLE;
        imageGroup.setVisibility(visible);

        if (!isNull(goodsDetailModel.getPromote_price())
                && !"0".equals(goodsDetailModel.getPromote_price())) {// 包邮价格不为空且不为0时，可包邮
            txtTag.setVisibility(View.VISIBLE);
            txtTag.setText("满" + goodsDetailModel.getPromote_price() + "元包邮");
        } else {
            txtTag.setVisibility(View.GONE);
        }

        switch (goodsDetailModel.getKeytype()) {//1.正常3.限时4.预售
            case "1":
                txtBuyNow.setText("立即购买");
                imageCollect.setVisibility(View.VISIBLE);
                break;
            case "3":
                line1.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                layoutShopCar.setVisibility(View.GONE);
                txtAddCar.setVisibility(View.GONE);
                layoutSendTime.setVisibility(View.VISIBLE);
                if (isNull(goodsDetailModel.getLimit_count())) {
                    txtSendTime.setText("不限制购买数量");
                } else {
                    txtSendTime.setText("限购" + goodsDetailModel.getLimit_count() + "件");
                }

                txtBuyNow.setText("立即抢购");
                imageCollect.setVisibility(View.GONE);
                break;
            case "4":
                line1.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                layoutShopCar.setVisibility(View.GONE);
                txtAddCar.setVisibility(View.GONE);
                layoutSendTime.setVisibility(View.VISIBLE);
                txtSendTime.setText("商品发货时间: " + goodsDetailModel.getTime());
                txtBuyNow.setText("立即预定");
                imageCollect.setVisibility(View.GONE);
                break;
        }

//        try {
//            URL url = new URL(goodsDetailModel.getAvatar());
//            ImageTask task = new ImageTask(imageHead, url, mContext,
//                    R.drawable.icon_register_head);
//            imageWorker.loadImage(task);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            imageHead.setImageResource(R.drawable.icon_register_head);
//        }
    }

    /**
     * 去登录
     */
    private void gotoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("ActivityType", MyConfig.LOGIN);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
    }

    /**
     * 添加购物车
     *
     * @param buycount
     * @param rule_id
     */
    public void cartAdd(String buycount, String rule_id) {
        if (!MyUtil.IsLogin(this)) {
            gotoLogin();
            return;
        }
        getNetWorker().cartAdd(getApplicationContext().getUser().getToken(),
                buycount, rule_id, goodsDetailModel.getId());
    }

    /**
     * 立即购买去确认订单界面
     */
    public void gotoCheckOrder() {
        if (!MyUtil.IsLogin(this)) {
            gotoLogin();
            return;
        }
        ArrayList<CartListModel> list = new ArrayList<>();
        CartListModel cartListModel = new CartListModel(goodsDetailModel.getMerchant_id(),
                goodsDetailModel.getNickname(), goodsDetailModel.getPromote_price(), goodsSelectDialog.getCartGoodsModel(), goodsDetailModel.getKeytype());
        list.add(cartListModel);
        Intent intent = new Intent(mContext, CheckOrderActivity.class);
        intent.putExtra("CartList", list);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.none);
    }

    public void showBigImage(String url) {
        ShowLargeImageView mView = new ShowLargeImageView(mContext,
                findViewById(R.id.father));
        mView.setImageURL(url);
        mView.show();
    }


}
