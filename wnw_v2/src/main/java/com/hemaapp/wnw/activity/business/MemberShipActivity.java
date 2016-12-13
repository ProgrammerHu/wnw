package com.hemaapp.wnw.activity.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.SearchLogActivity;
import com.hemaapp.wnw.adapter.MemberShipAdapter;
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
 * 会员管理
 * Created by HuHu on 2016-08-17.
 */
public class MemberShipActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity, imageToTop, imageFilter, imageSearch;
    private TextView txtTitle, txtTotalFee;
    private ListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private MemberShipAdapter adapter;
    private MemberShipPopUpWindow memberShipPopUpWindow;
    private ArrayList<DistrictListModel> districts = new ArrayList<>();
    private ArrayList<VipListModel> vipData = new ArrayList<>();

    private int page = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_member_ship);
        super.onCreate(savedInstanceState);
        showProgressDialog(R.string.loading);
        getNetWorker().districtList("0");
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
            case DIS_LIST:
                MyArrayResult<DistrictListModel> districtResult = (MyArrayResult<DistrictListModel>) hemaBaseResult;
                districts.clear();
                districts.addAll(districtResult.getObjects());
                memberShipPopUpWindow = new MemberShipPopUpWindow(mContext, districts);
                memberShipPopUpWindow.setOnClickConfirmListener(new MemberShipPopUpWindow.OnClickConfirmListener() {
                    @Override
                    public void OnClickConfirm(String beginDate, String endDate, String selectDistricts) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date begin = sdf.parse(beginDate);
                            Date end = sdf.parse(endDate);
                            if (end.getTime() - begin.getTime() < 0) {
                                showTextDialog("结束时间不可小于开始时间");
                                return;
                            }
                        } catch (ParseException e) {
                        }
                        postList();
                        memberShipPopUpWindow.dismiss();
                    }
                });
                page = 0;
                postList();
                break;
            case VIP_LIST:
                cancelProgressDialog();
                VipListResult vipListResult = (VipListResult) hemaBaseResult;
                if (page == 0) {
                    txtTotalFee.setText(vipListResult.getTotalFee());
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
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageFilter = (ImageView) findViewById(R.id.imageFilter);
        imageSearch = (ImageView) findViewById(R.id.imageSearch);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("会员管理");
        listView = (ListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_member_ship, listView, false);
        listView.addHeaderView(headerView);
        adapter = new MemberShipAdapter(mContext, vipData);
        listView.setAdapter(adapter);

        txtTotalFee = (TextView) listView.findViewById(R.id.txtTotalFee);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageFilter.setOnClickListener(this);
        imageSearch.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                if (districts == null || districts.size() == 0) {
                    getNetWorker().districtList("0");
                } else {
                    page = 0;
                    postList();
                }
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                postList();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.imageFilter:
                showMemberShipPopUpWindow();
                break;
            case R.id.imageSearch:
                Intent intent = new Intent(mContext, SearchLogActivity.class);
                intent.putExtra("SearchType", MyConfig.SEARCH_MEMBER);
                intent.putExtra("hint", "搜索会员");
                intent.putExtra("text", "");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    private void showMemberShipPopUpWindow() {
        if (memberShipPopUpWindow != null)
            memberShipPopUpWindow.showAsDropDown(imageFilter);
    }

    private void postList() {
        String date_start = "", date_end = "", district = "";
        if (memberShipPopUpWindow != null) {
            date_start = memberShipPopUpWindow.getBeginDate();
            date_end = memberShipPopUpWindow.getEndDate();
            district = memberShipPopUpWindow.getDistrict();
        }
        getNetWorker().vipList(MyApplication.getInstance().getUser().getToken(), date_start, date_end, district, "", String.valueOf(page));

    }
}
