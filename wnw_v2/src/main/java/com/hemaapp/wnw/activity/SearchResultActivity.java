package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.SearchLogAdapter;
import com.hemaapp.wnw.adapter.TagListAdapter;
import com.hemaapp.wnw.db.SearchDBHelper;
import com.hemaapp.wnw.model.Image;
import com.hemaapp.wnw.model.NoteList;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 搜索结果界面
 * Created by HuHu on 2016/3/23.
 */
public class SearchResultActivity extends MyActivity implements
        View.OnClickListener {

    private TextView txtSearch;
    private EditText editText;
    private ImageView imageToTop;

    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private TagListAdapter adapter;
    private ArrayList<NoteList> listData = new ArrayList<>();
    private SearchDBHelper searchDBHelper;

    private int page;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
        if (isNull(keyword)) {
            showTextDialog("keyword空了");
            return;
        }
        String token = "";
        if (MyUtil.IsLogin(this)) {
            token = getApplicationContext().getUser().getToken();
        }
        showProgressDialog(R.string.loading);
        getNetWorker().noteList(token, "3", keyword, String.valueOf(page));

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case NOTE_LIST:
                cancelProgressDialog();
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.refreshSuccess();
                MyArrayResult<NoteList> result = (MyArrayResult<NoteList>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                page++;
                refreshLoadmoreLayout.setLoadmoreable(result.getObjects().size() >= getApplicationContext()
                        .getSysInitInfo().getSys_pagesize());
                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
    }

    @Override
    protected void findView() {
        txtSearch = (TextView) findViewById(R.id.txtSearch);
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(keyword);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new TagListAdapter(this, listView, listData);
        adapter.setEmptyString("什么都没有搜到");
        listView.setAdapter(adapter);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        searchDBHelper = new SearchDBHelper(mContext);
    }

    @Override
    protected void getExras() {
        keyword = mIntent.getStringExtra("keyword");
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
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int visibility = firstVisibleItem == 0 ? View.GONE : View.VISIBLE;
                imageToTop.setVisibility(visibility);

            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                page = 0;
                doSearch();
                return false;
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
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
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
        String token = "";
        if (MyUtil.IsLogin(this)) {
            token = getApplicationContext().getUser().getToken();
        }
        showProgressDialog(R.string.loading);
        getNetWorker().noteList(token, "3", content, String.valueOf(page));
    }
}
