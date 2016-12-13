package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.SearchLogActivity;
import com.hemaapp.wnw.adapter.MemberShipAdapter;
import com.hemaapp.wnw.db.SearchDBHelper;
import com.hemaapp.wnw.model.DistrictListModel;
import com.hemaapp.wnw.model.VipListModel;
import com.hemaapp.wnw.popupwindow.MemberShipPopUpWindow;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.result.VipListResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 会员管理搜索结果界面
 * Created by HuHu on 2016-08-17.
 */
public class MemberShipSearchActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageToTop;
    private ListView listView;
    private EditText editText;
    private TextView txtSearch;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private MemberShipAdapter adapter;
    private ArrayList<VipListModel> vipData = new ArrayList<>();

    private int page = 0;
    private String keyword;
    private SearchDBHelper searchDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
        showProgressDialog(R.string.loading);
        editText.setText(keyword);
        getNetWorker().vipList(MyApplication.getInstance().getUser().getToken(), "", "", "", keyword, String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case VIP_LIST:
                cancelProgressDialog();
                VipListResult vipListResult = (VipListResult) hemaBaseResult;
                if (page == 0) {
                    vipData.clear();
                }

                vipData.addAll(vipListResult.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.setLoadmoreable(vipListResult.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
        cancelProgressDialog();
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void findView() {
        editText = (EditText) findViewById(R.id.editText);
        txtSearch = (TextView) findViewById(R.id.txtSearch);

        listView = (ListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        adapter = new MemberShipAdapter(mContext, vipData);
        listView.setAdapter(adapter);
        searchDBHelper = new SearchDBHelper(mContext);
    }

    @Override
    protected void getExras() {
        keyword = mIntent.getStringExtra("keyword");
    }

    @Override
    protected void setListener() {
        txtSearch.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().vipList(MyApplication.getInstance().getUser().getToken(), "", "", "", keyword, String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().vipList(MyApplication.getInstance().getUser().getToken(), "", "", "", keyword, String.valueOf(page));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSearch:
                String content = editText.getEditableText().toString().trim();
                if (isNull(content)) {
                    showMyOneButtonDialog("搜索", "请输入搜索内容");
                    return;
                }
                doSearch();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
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
        if (!searchDBHelper.insertSearch(content, MyConfig.SEARCH_MEMBER)) {
            showTextDialog("数据库写入失败");
            return;
        }
        showProgressDialog(R.string.loading);
        keyword = content;
        getNetWorker().vipList(MyApplication.getInstance().getUser().getToken(), "", "", "", content, String.valueOf(page));
    }
}
