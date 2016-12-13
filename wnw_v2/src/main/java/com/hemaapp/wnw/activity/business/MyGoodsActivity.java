package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_framework.dialog.MyBottomThreeButtonDialog;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.activity.SearchLogActivity;
import com.hemaapp.wnw.adapter.MyGoodsListAdapter;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.model.CountListModel;
import com.hemaapp.wnw.model.GroupListModel;
import com.hemaapp.wnw.model.MerchantGoodsListModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.popupwindow.ClassificationPopupWindow;
import com.hemaapp.wnw.popupwindow.PublicGoodsPopup;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 我的商品列表
 * Created by HuHu on 2016-08-15.
 */
public class MyGoodsActivity extends MyActivity implements View.OnClickListener, MyGoodsListAdapter.OnEditListener {
    private ImageView imageQuitActivity, imageSearch, imageAdd, imageToTop;
    private TextView txtTitle;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private MyGoodsListAdapter adapter;
    private ClassificationPopupWindow classificationPopupWindow;
    //    private PublicGoodsPopup publicGoodsPopup;
    private TextView txtAllClassification;
    private ArrayList<MerchantGoodsListModel> listData = new ArrayList<>();
    private ArrayList<CountListModel> countListModels = new ArrayList<>();
    private int page = 0;
    private String keyid = "";
    private String keytype = "1";//	1.普通商品3.抢购4.预售

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_goods);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        showProgressDialog(R.string.loading);
        getNetWorker().countList(getApplicationContext().getUser().getToken(), keytype);

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 接收广播（必须的）
     *
     * @param event
     */
    public void onEventMainThread(EventBusModel event) {
        if (!event.getState()) {
            return;
        }
        switch (event.getType()) {
            case MyConfig.REFRESH_GROUP_LIST://找到特定条目修改其状态
                String blog_id = event.getContent();
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getId().equals(blog_id)) {
                        listData.get(i).setGroup_flag("1");
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                break;
            case MyConfig.ADD_NEW_GOODS:
                page = 0;
                getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), keytype, keyid, "", String.valueOf(page));
                listView.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_CANCEL:
                showProgressDialog(R.string.committing);
                break;
            case BLOG_DELETE:
                showProgressDialog(R.string.deleteing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_CANCEL:
            case BLOG_DELETE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case COUNT_LIST:
                MyArrayResult<CountListModel> countResult = (MyArrayResult<CountListModel>) hemaBaseResult;
                countListModels.clear();
                countListModels.addAll(countResult.getObjects());
                if (classificationPopupWindow == null) {
                    classificationPopupWindow = new ClassificationPopupWindow(mContext, countListModels);
                    classificationPopupWindow.setOnItemClickListener(new ClassificationPopupWindow.OnItemClickListener() {
                        @Override
                        public void ClickItem(CountListModel model) {
                            keyid = model.getId();
                            page = 0;
                            showProgressDialog(R.string.loading);
                            getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), keytype, keyid, "", String.valueOf(page));
                            classificationPopupWindow.dismiss();
                            txtAllClassification.setText(classificationPopupWindow.getShowText());
                        }
                    });
                } else {
                    classificationPopupWindow.refreshData(countResult.getObjects());
                }
                txtAllClassification.setText(classificationPopupWindow.getShowText());
                page = 0;
                getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), keytype, keyid, "", String.valueOf(page));
                break;
            case MERCHANT_GOODS_LIST:
                MyArrayResult<MerchantGoodsListModel> goodsResult = (MyArrayResult<MerchantGoodsListModel>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                page++;
                listData.addAll(goodsResult.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                cancelProgressDialog();
                refreshLoadmoreLayout.setLoadmoreable(goodsResult.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
            case GROUP_CANCEL: {
                String id = hemaNetTask.getParams().get("blog_id");
                for (MerchantGoodsListModel model : listData) {
                    if (model.getId().equals(id)) {
                        model.setGroup_flag("0");
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
            break;
            case BLOG_DELETE: {
                String id = hemaNetTask.getParams().get("blog_id");
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getId().equals(id)) {
                        listData.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        switch (keytype) {
            case "1":
                txtTitle.setText("我的商品");
                break;
            case "3":
                txtTitle.setText("限时抢购");
                break;
            case "4":
                txtTitle.setText("预售商品");
                break;
        }
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageSearch = (ImageView) findViewById(R.id.imageSearch);
        imageAdd = (ImageView) findViewById(R.id.imageAdd);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_my_goods, listView, false);
        listView.addHeaderView(headerView);
        adapter = new MyGoodsListAdapter(mContext, listData, keytype);
        listView.setAdapter(adapter);
        txtAllClassification = (TextView) findViewById(R.id.txtAllClassification);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageSearch.setOnClickListener(this);
        imageAdd.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        txtAllClassification.setOnClickListener(this);
        adapter.setOnEditListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().countList(getApplicationContext().getUser().getToken(), keytype);
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), keytype, keyid, "", String.valueOf(page));
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.imageSearch:
                intent = new Intent(mContext, SearchLogActivity.class);
                switch (keytype) {
                    case "1":
                        intent.putExtra("SearchType", MyConfig.SEARCH_BUSINESS_GOODS_CURRENT);
                        break;
                    case "3":
                        intent.putExtra("SearchType", MyConfig.SEARCH_BUSINESS_GOODS_FLASH_SALE);
                        break;
                    case "4":
                        intent.putExtra("SearchType", MyConfig.SEARCH_BUSINESS_GOODS_PRE_SALE);
                        break;
                }
                intent.putExtra("hint", "搜索" + txtTitle.getText().toString());
                intent.putExtra("text", "");
                startActivity(intent);
                changeAnim();
                break;
            case R.id.imageAdd:
                switch (keytype) {
                    case "1":
                        intent = new Intent(MyGoodsActivity.this, PublicGoodsActivity.class);
                        intent.putExtra("id", "0");
                        startActivity(intent);
                        changeAnim();
                        break;
                    case "3":
                        intent = new Intent(mContext, PublicGoodsFlashSaleActivity.class);
                        intent.putExtra("id", "0");
                        startActivity(intent);
                        changeAnim();
                        break;
                    case "4":
                        intent = new Intent(mContext, PublicGoodsPreSaleActivity.class);
                        intent.putExtra("id", "0");
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        break;
                }
                break;
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;
            case R.id.txtAllClassification:
                showClassificationPopupWindow();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    /**
     * 显示分类列表
     */
    private void showClassificationPopupWindow() {
        if (classificationPopupWindow != null) {
            classificationPopupWindow.showAsDropDown(txtAllClassification);
        }
    }


    @Override
    public void toGoodsDetail(int position) {
        Intent intent = new Intent(mContext, GoodsDetailActivity.class);
        intent.putExtra("goods_id", listData.get(position).getId());
        startActivity(intent);
        changeAnim();
    }

    @Override
    public void deleteItem(final int position) {
        MyTwoButtonDialog dialog = new MyTwoButtonDialog(mContext);
        dialog.setText("确定要删除吗？");
        dialog.setTitle("我的商品");
        dialog.setLeftButtonText("确定");
        dialog.setRightButtonText("取消");
        dialog.setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
            @Override
            public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
            }

            @Override
            public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
                getNetWorker().blogDelete(getApplicationContext().getUser().getToken(), listData.get(position).getId());
            }

            @Override
            public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void editItem(final int position) {
        MerchantGoodsListModel model = listData.get(position);
        if ("0".equals(model.getGroup_flag())) {//未设置团购
            editItemGoods(position);
        } else {
            MyBottomThreeButtonDialog dialog = new MyBottomThreeButtonDialog(mContext);
            dialog.setTopButtonText("编辑商品");
            dialog.setMiddleButtonText("编辑团购");
            dialog.setButtonListener(new MyBottomThreeButtonDialog.OnButtonListener() {
                @Override
                public void onTopButtonClick(MyBottomThreeButtonDialog dialog) {
                    dialog.cancel();
                    editItemGoods(position);
                }

                @Override
                public void onMiddleButtonClick(MyBottomThreeButtonDialog dialog) {
                    dialog.cancel();
                    editItemGroup(position);
                }
            });
            dialog.show();
        }
    }

    /**
     * 根据position编辑商品
     *
     * @param position
     */
    private void editItemGoods(int position) {
        Intent intent;
        switch (keytype) {
            case "1":
                intent = new Intent(mContext, PublicGoodsActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                startActivity(intent);
                changeAnim();
                break;
            case "3":
                intent = new Intent(mContext, PublicGoodsFlashSaleActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                startActivity(intent);
                changeAnim();
                break;
            case "4":
                intent = new Intent(mContext, PublicGoodsPreSaleActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                startActivity(intent);
                changeAnim();
                break;
        }
    }

    /**
     * 根据position编辑团购
     *
     * @param position
     */
    private void editItemGroup(int position) {
        Intent intent = new Intent(mContext, AddGroupSaleActivity.class);
        intent.putExtra("id", listData.get(position).getId());
        startActivity(intent);
        changeAnim();
    }

    @Override
    public void setGroupBuy(int position) {
        final MerchantGoodsListModel model = listData.get(position);
        if ("0".equals(model.getGroup_flag())) {//	0.普通商品
            editItemGroup(position);
        } else {//1.团购，取消团购
            MyTwoButtonDialog dialog = new MyTwoButtonDialog(mContext);
            dialog.setText("确定取消团购？").setTitle("取消团购").setLeftButtonText("确定").setRightButtonText("取消");
            dialog.setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
                @Override
                public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                    twoButtonDialog.cancel();
                }

                @Override
                public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {
                    twoButtonDialog.cancel();
                    getNetWorker().groupCancel(getApplicationContext().getUser().getToken(), model.getId());
                }

                @Override
                public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                    twoButtonDialog.cancel();
                }
            });
            dialog.show();
        }
    }

    @Override
    public void serFlashSale(int position) {
        Intent intent = new Intent(mContext, PublicGoodsFlashSaleActivity.class);
        intent.putExtra("id", listData.get(position).getId());
        intent.putExtra("IsCopy", true);
        startActivity(intent);
        changeAnim();
    }

    @Override
    public void setPreSale(int position) {
        Intent intent = new Intent(mContext, PublicGoodsPreSaleActivity.class);
        intent.putExtra("id", listData.get(position).getId());
        intent.putExtra("IsCopy", true);
        startActivity(intent);
        changeAnim();
    }
}
