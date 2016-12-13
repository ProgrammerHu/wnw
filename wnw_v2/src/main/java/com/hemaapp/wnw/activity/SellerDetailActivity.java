package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.GoodsListAdapter;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.model.GoodsListModel;
import com.hemaapp.wnw.model.Merchant;
import com.hemaapp.wnw.model.NoteList;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 店铺主页 默认进来跳商品，帖子进来看帖子
 * Created by Hufanglin on 2016/2/23.
 */
public class SellerDetailActivity extends MyActivity implements
        View.OnClickListener {

    private ImageView imageQuitActivity, imageToTop, imageSex;
    private TextView txtTitle;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;

    private TextView txtDistrict, txtMainProduct, txtHobby, txtName,
            txtFansCount, txtChangeSubscribe;
    //    private TextView txtLeft, txtRight;
    private ImageView imageHead;
    //    private ImageView imageLeft, imageRight;
    //    private View layoutLeft, layoutLeftTop, layoutRight, layoutRightTop;
    private View father;

    // private TagListWithoutHeadAdapter blogAdapter;
    private ShowLargeImageView mView;// 展示大图
    private GoodsListAdapter goodsAdapter;
    private String client_id;//
    private Merchant merchant;

    private List<GoodsListModel> goodsList = new ArrayList<>();
    private List<NoteList> noteList = new ArrayList<NoteList>();

    private int leftPage = 0;
    private int rightPage = 0;
    private boolean isFirstLoad = true;// 标记是否是初次加载，初次加载时加载完商品列表要加载帖子列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refresh_listview);
        super.onCreate(savedInstanceState);
        isFirstLoad = true;
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            client_id = savedInstanceState.getString("client_id");
        }

        if (isNull(client_id)) {
            showTextDialog(R.string.id_empty);
        } else {
            String token = "";
            if (MyUtil.IsLogin(this)) {
                token = getApplicationContext().getUser().getToken();
            }
            showProgressDialog(R.string.loading);
            getNetWorker().merchantGet(token, client_id);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("client_id", client_id);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case MERCHANT_GET:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case MERCHANT_GET:
                cancelProgressDialog();
                break;
        }

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case MERCHANT_GET:
                HemaArrayResult<Merchant> merchantResult = (HemaArrayResult<Merchant>) hemaBaseResult;
                merchant = merchantResult.getObjects().get(0);
                setData();
                String token = "";
                if (MyUtil.IsLogin(this)) {
                    token = getApplicationContext().getUser().getToken();
                }
                getNetWorker().noteList(token, "2", client_id, String.valueOf(rightPage));
                break;
            /*case GOODS_LIST:
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                if (isFirstLoad) {
                    getNetWorker().noteList(hemaNetTask.getParams().get("token"), "2", client_id, String.valueOf(rightPage));
                    isFirstLoad = false;
                }
                MyArrayResult<GoodsListModel> goodsResult = (MyArrayResult<GoodsListModel>) hemaBaseResult;
                if (leftPage == 0) {
                    goodsList.clear();
                }
                goodsList.addAll(goodsResult.getObjects());
                goodsAdapter.notifyDataSetChanged();
                if (goodsResult.getObjects().size() > 0) {
                    leftPage++;
                }
                refreshLoadmoreLayout.setLoadmoreable(goodsAdapter.getCount() %
                        getApplicationContext().getSysInitInfo().getSys_pagesize() == 0);
                break;*/
            case NOTE_LIST:
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                MyArrayResult<NoteList> noteResult = (MyArrayResult<NoteList>) hemaBaseResult;
                if (rightPage == 0) {
                    noteList.clear();
                }
                noteList.addAll(noteResult.getObjects());
                goodsAdapter.notifyDataSetChanged();
                if (noteResult.getObjects().size() > 0) {
                    rightPage++;
                }
                refreshLoadmoreLayout.setLoadmoreable(goodsAdapter.getCount() %
                        getApplicationContext().getSysInitInfo().getSys_pagesize() == 0);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
        cancelProgressDialog();
        if (hemaBaseResult.getError_code() == 500 || "该商家不存在".equals(hemaBaseResult.getMsg())) {
            MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
            dialog.hideCancel().hideIcon().setText(hemaBaseResult.getMsg())
                    .setTitle(getResources().getString(R.string.seller_page))
                    .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                        @Override
                        public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                            finish(R.anim.my_left_in, R.anim.right_out);
                        }

                        @Override
                        public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                            finish(R.anim.my_left_in, R.anim.right_out);
                        }
                    });
            dialog.show();
        } else {
            showMyOneButtonDialog(getResources().getString(R.string.seller_page),
                    hemaBaseResult.getMsg());
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
        cancelProgressDialog();
    }

    @Override
    protected void findView() {
        father = findViewById(R.id.father);
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("我是VWOW");
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        View rootView = LayoutInflater.from(mContext).inflate(
                R.layout.header_seller_detail, listView, false);
        listView.addHeaderView(rootView);
        // blogAdapter = new
        // TagListWithoutHeadAdapter(SellerDetailActivity.this, listView);
        goodsAdapter = new GoodsListAdapter(SellerDetailActivity.this,
                listView, goodsList, noteList);
        listView.setAdapter(goodsAdapter);

        txtDistrict = (TextView) rootView.findViewById(R.id.txtDistrict);
        txtMainProduct = (TextView) rootView.findViewById(R.id.txtMainProduct);
        txtHobby = (TextView) rootView.findViewById(R.id.txtHobby);
        txtName = (TextView) rootView.findViewById(R.id.txtName);
        txtFansCount = (TextView) rootView.findViewById(R.id.txtFansCount);
        txtChangeSubscribe = (TextView) rootView
                .findViewById(R.id.txtChangeSubscribe);
        txtChangeSubscribe.setTag(R.id.TAG, false);
//        txtLeft = (TextView) rootView.findViewById(R.id.txtLeft);
//        txtRight = (TextView) rootView.findViewById(R.id.txtRight);

        imageHead = (ImageView) rootView.findViewById(R.id.imageHead);
//        imageLeft = (ImageView) rootView.findViewById(R.id.imageLeft);
//        imageRight = (ImageView) rootView.findViewById(R.id.imageRight);
        imageSex = (ImageView) rootView.findViewById(R.id.imageSex);

//        layoutLeft = rootView.findViewById(R.id.layoutLeft);
//        layoutLeftTop = rootView.findViewById(R.id.layoutLeftTop);
//        layoutRight = rootView.findViewById(R.id.layoutRight);
//        layoutRightTop = rootView.findViewById(R.id.layoutRightTop);
    }

    @Override
    protected void getExras() {
        client_id = mIntent.getStringExtra("client_id");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtChangeSubscribe.setOnClickListener(this);
//        layoutLeft.setOnClickListener(this);
//        layoutRight.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        imageHead.setOnClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int visibity = listView.getFirstVisiblePosition() == 0 ? View.INVISIBLE
                        : View.VISIBLE;
                imageToTop.setVisibility(visibity);
            }
        });

        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(
                    XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                rightPage = 0;
                String token = "";
                if (MyUtil.IsLogin(SellerDetailActivity.this)) {
                    token = getApplicationContext().getUser()
                            .getToken();
                }
                getNetWorker().merchantGet(token, client_id);
            }

            @Override
            public void onStartLoadmore(
                    XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                String token = "";
                if (MyUtil.IsLogin(SellerDetailActivity.this)) {
                    token = getApplicationContext().getUser()
                            .getToken();
                }
                getNetWorker().noteList(token, "2", client_id, String.valueOf(rightPage));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.txtChangeSubscribe:
                if (merchant == null) {
                    return;
                }
                if (!MyUtil.IsLogin(this)) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("ActivityType", MyConfig.LOGIN);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    return;
                }
                if ("1".equals(merchant.getSubscribe_flag())) {// 已经订阅，点击取消订阅
                    txtChangeSubscribe.setTextColor(getResources().getColor(
                            R.color.main_purple));
                    txtChangeSubscribe
                            .setBackgroundResource(R.drawable.bg_subscribe_border);
                    txtChangeSubscribe.setText(R.string.subscribe_add);
                    merchant.setSubscribe_flag("0");
                    getNetWorker().removeRoot(
                            getApplicationContext().getUser().getToken(), "3", "1",
                            client_id);
                } else {// 尚未订阅，点击订阅
                    txtChangeSubscribe.setTextColor(getResources().getColor(
                            R.color.white));
                    txtChangeSubscribe
                            .setBackgroundResource(R.drawable.bg_subscribe_purple);
                    txtChangeSubscribe.setText(R.string.subscribe_cancel);
                    merchant.setSubscribe_flag("1");
                    getNetWorker()
                            .subAdd(getApplicationContext().getUser().getToken(),
                                    client_id, "");
                }
                break;
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;
            case R.id.imageHead:
                if (!isNull(merchant.getAvatar())) {
                    mView = new ShowLargeImageView(mContext, father);
                    mView.show();
                    mView.setImagePath(merchant.getAvatar());
                }
                break;
        }

    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    private void setData() {
        txtName.setText(merchant.getNickname());
        // txtFansCount.setText(getResources().getString(R.string.fans) +
        // merchant.getFans());
        txtFansCount.setText(merchant.getFans());

        txtDistrict.setText(Html.fromHtml("<font>"
                + getResources().getString(R.string.seller_district)
                + "</font><font color=#1a1a1a>" + merchant.getAddress()
                + "</font>"));
        txtMainProduct.setText(Html.fromHtml("<font>"
                + getResources().getString(R.string.seller_main_product)
                + "</font><font color=#1a1a1a>" + merchant.getMajor()
                + "</font>"));
        txtHobby.setText(Html.fromHtml("<font>"
                + getResources().getString(R.string.seller_hobby)
                + "</font><font color=#1a1a1a>" + merchant.getLike()
                + "</font>"));
        imageSex.setVisibility(View.VISIBLE);
        if ("男".equals(merchant.getSex())) {
            imageSex.setImageResource(R.drawable.icon_male);
        } else if ("女".equals(merchant.getSex())) {
            imageSex.setImageResource(R.drawable.icon_female);
        } else {
            imageSex.setVisibility(View.INVISIBLE);
        }
        if ("0".equals(merchant.getSubscribe_flag())) {
            txtChangeSubscribe.setText(R.string.subscribe_add);
            txtChangeSubscribe
                    .setBackgroundResource(R.drawable.bg_subscribe_border);
            txtChangeSubscribe.setTextColor(getResources().getColor(
                    R.color.main_purple));
        } else {
            txtChangeSubscribe.setText(R.string.subscribe_cancel);
            txtChangeSubscribe
                    .setBackgroundResource(R.drawable.bg_subscribe_purple);
            txtChangeSubscribe.setTextColor(getResources().getColor(
                    R.color.white));
        }

        try {
            URL url = new URL(merchant.getAvatar());
            ImageTask task = new ImageTask(imageHead, url, mContext,
                    R.drawable.icon_register_head);
            imageWorker.loadImage(task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            imageHead.setImageResource(R.drawable.icon_register_head);
        }

    }

    /**
     * 执行点赞
     *
     * @param keyid
     * @param flag  1.点赞2.取消点赞
     */
    public boolean changePraise(String keyid, String flag) {
        if (MyUtil.IsLogin(this)) {
            getNetWorker().praiseOperate(
                    getApplicationContext().getUser().getToken(), "2", flag,
                    keyid);
            return true;
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("ActivityType", MyConfig.LOGIN);
            startActivity(intent);
            return false;
        }
    }
}
