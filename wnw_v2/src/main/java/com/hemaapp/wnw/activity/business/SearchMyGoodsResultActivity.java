package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_framework.dialog.MyBottomThreeButtonDialog;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.adapter.MyGoodsListAdapter;
import com.hemaapp.wnw.db.SearchDBHelper;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.model.MerchantGoodsListModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 搜索商品结果
 * Created by HuHu on 2016-09-12.
 */
public class SearchMyGoodsResultActivity extends MyActivity implements View.OnClickListener {
    private TextView txtSearch;
    private EditText editText;
    private ImageView imageToTop;

    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private MyGoodsListAdapter adapter;
    private ArrayList<MerchantGoodsListModel> listData = new ArrayList<>();

    private SearchDBHelper searchDBHelper;

    private int page;
    private String keyword;
    private String keytype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (isNull(keyword)) {
            showTextDialog("keyword空了");
            return;
        }
        showProgressDialog(R.string.loading);
        page = 0;
        getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), keytype, "", keyword, String.valueOf(page));
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
                getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), keytype, "", "", String.valueOf(page));
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
            case BLOG_DELETE:
                String id = hemaNetTask.getParams().get("blog_id");
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getId().equals(id)) {
                        listData.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void findView() {
        txtSearch = (TextView) findViewById(R.id.txtSearch);
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(keyword);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new MyGoodsListAdapter(mContext, listData, keytype);
        adapter.setEmptyString("什么都没有搜到");
        listView.setAdapter(adapter);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        searchDBHelper = new SearchDBHelper(mContext);
    }

    @Override
    protected void getExras() {
        keyword = mIntent.getStringExtra("keyword");
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        txtSearch.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int visibility = firstVisibleItem == 0 ? View.INVISIBLE
                        : View.VISIBLE;
                imageToTop.setVisibility(visibility);
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                doSearch();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                doSearch();
            }
        });
        adapter.setOnEditListener(new MyGoodsListAdapter.OnEditListener() {
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

            @Override
            public void setGroupBuy(int position) {
                final MerchantGoodsListModel model = listData.get(position);
                if ("0".equals(model.getGroup_flag())) {//0.普通商品
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
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;
            case R.id.txtSearch:
                page = 0;
                doSearch();
                break;
        }
    }

    /**
     * 执行搜索
     */
    private void doSearch() {
        String content = editText.getEditableText().toString().trim();
        if (isNull(content)) {
            showMyOneButtonDialog("搜索", "请输入搜索内容");
            return;
        }
        if (!searchDBHelper.insertSearch(content, MyConfig.SEARCH_USER)) {
            showTextDialog("数据库写入失败");
            return;
        }
        showProgressDialog(R.string.loading);
        getNetWorker().merchantGoodsList(getApplicationContext().getUser().getToken(), keytype, "", content, String.valueOf(page));
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }
}
