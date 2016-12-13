package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.business.MemberShipSearchActivity;
import com.hemaapp.wnw.activity.business.SearchMyGoodsResultActivity;
import com.hemaapp.wnw.activity.business.SearchMyOrderResultActivity;
import com.hemaapp.wnw.adapter.SearchLogAdapter;
import com.hemaapp.wnw.db.SearchDBHelper;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * 搜索界面
 * Created by HuHu on 2016/3/23.
 */
public class SearchLogActivity extends MyActivity implements View.OnClickListener {
    public String SEARCH_TYPE;
    public String hint, text;
    public String keytype;//商家搜索订单时使用

    private TextView txtSearch;
    private EditText editText;

    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private SearchLogAdapter searchLogAdapter;
    private View footView;

    private SearchDBHelper searchDBHelper;
    private ArrayList<String> searchLogs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        txtSearch = (TextView) findViewById(R.id.txtSearch);
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(text);
        editText.setHint(hint);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        refreshLoadmoreLayout.setRefreshable(false);
        refreshLoadmoreLayout.setLoadmoreable(false);
        listView = (XtomListView) findViewById(R.id.listView);
        searchDBHelper = new SearchDBHelper(mContext);
        searchLogAdapter = new SearchLogAdapter(mContext, searchLogs, SEARCH_TYPE);
        searchLogAdapter.setEmptyString("还没有搜索记录");

        footView = LayoutInflater.from(mContext).inflate(R.layout.footer_searchlog, listView, false);
        listView.addFooterView(footView);
        listView.setAdapter(searchLogAdapter);
        hideFooterView();

        if (MyConfig.SEARCH_BUSINESS_MY_ORDER.equals(SEARCH_TYPE)) {//限制输入框为数字类型
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadThread().run();
    }

    @Override
    protected void getExras() {
        SEARCH_TYPE = mIntent.getStringExtra("SearchType");
        hint = mIntent.getStringExtra("hint");
        text = mIntent.getStringExtra("text");
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        txtSearch.setOnClickListener(this);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearchLog();//清空
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String content = editText.getEditableText().toString().trim();
                doSearch(content);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchLogs.size() <= 0 || position >= searchLogs.size()) {
                    return;
                }
                String content = searchLogs.get(position);
                doSearch(content);
            }
        });
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSearch:
                String content = editText.getEditableText().toString().trim();
                doSearch(content);
                break;

        }
    }

    /**
     * 执行搜索
     */
    private void doSearch(String content) {
        if (isNull(content)) {
            showMyOneButtonDialog("搜索", "请输入搜索内容");
            return;
        }
        if (!searchDBHelper.insertSearch(content, SEARCH_TYPE)) {
            showTextDialog("数据库写入失败");
            return;
        }
        new LoadThread().run();
        Intent intent;
        switch (SEARCH_TYPE) {
            case MyConfig.SEARCH_USER://首页搜索
                intent = new Intent(SearchLogActivity.this, SearchResultActivity.class);
                intent.putExtra("keyword", content);
                startActivity(intent);
                changeAnim();
                break;
            case MyConfig.SEARCH_MY_GOODS:
//                intent = new Intent(SearchLogActivity.this, SearchResultActivity.class);
//                intent.putExtra("keyword", content);
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case MyConfig.SEARCH_BUSINESS_GOODS_CURRENT:
                intent = new Intent(SearchLogActivity.this, SearchMyGoodsResultActivity.class);
                intent.putExtra("keytype", "1");
                intent.putExtra("keyword", content);
                startActivity(intent);
                changeAnim();
                break;
            case MyConfig.SEARCH_BUSINESS_GOODS_FLASH_SALE:
                intent = new Intent(SearchLogActivity.this, SearchMyGoodsResultActivity.class);
                intent.putExtra("keytype", "3");
                intent.putExtra("keyword", content);
                startActivity(intent);
                changeAnim();
                break;
            case MyConfig.SEARCH_BUSINESS_GOODS_PRE_SALE:
                intent = new Intent(SearchLogActivity.this, SearchMyGoodsResultActivity.class);
                intent.putExtra("keytype", "4");
                intent.putExtra("keyword", content);
                startActivity(intent);
                changeAnim();
                break;
            case MyConfig.SEARCH_BUSINESS_MY_ORDER:
                intent = new Intent(SearchLogActivity.this, SearchMyOrderResultActivity.class);
                intent.putExtra("keytype", keytype);
                intent.putExtra("keyword", content);
                startActivity(intent);
                changeAnim();
                break;
            case MyConfig.SEARCH_MEMBER:
                intent = new Intent(SearchLogActivity.this, MemberShipSearchActivity.class);
                intent.putExtra("keytype", keytype);
                intent.putExtra("keyword", content);
                startActivity(intent);
                changeAnim();
                break;
            case MyConfig.SEARCH_DISCOVERY_GOODS:
                intent = new Intent(SearchLogActivity.this, SearchGoodsActivity.class);
                intent.putExtra("keyword", content);
                startActivity(intent);
                changeAnim();
                break;
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//检索完之后更新界面数据，都放在子线程中完成
            super.handleMessage(msg);
            ArrayList<String> mSearchLogs = (ArrayList<String>) msg.obj;
            searchLogs.clear();
            searchLogs.addAll(mSearchLogs);
            searchLogAdapter.notifyDataSetChanged();
            hideFooterView();
        }
    };

    private class LoadThread extends Thread {//执行数据库检索的线程

        @Override
        public void run() {
            super.run();
            SearchDBHelper dbHelper = new SearchDBHelper(mContext);
            ArrayList<String> mSearchLogs = dbHelper.getSearch(SEARCH_TYPE);
            Message message = new Message();
            message.obj = mSearchLogs;
            mHandler.sendMessage(message);
        }
    }

    private void hideFooterView() {
        if (searchLogAdapter.isEmpty()) {
            footView.setVisibility(View.GONE);
        } else {
            footView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 清除搜索记录
     */
    private void clearSearchLog() {
        searchDBHelper.ClearTable(SEARCH_TYPE);
        new LoadThread().run();
    }
}
