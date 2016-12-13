package com.hemaapp.wnw.activity.business;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.AccountListModel;
import com.hemaapp.wnw.model.DistrictListModel;
import com.hemaapp.wnw.popupwindow.MemberShipPopUpWindow;
import com.hemaapp.wnw.result.MerchantAccountListResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 入账记录
 * Created by HuHu on 2016-09-19.
 */
public class FinanceManagementActivity extends MyActivity implements View.OnClickListener {

    private ImageView imageQuitActivity, imageFilter, imageToTop;
    private TextView txtTotalFee, txtHeaderTitle;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private ListView listView;
    private ArrayList<AccountListModel> listData = new ArrayList<>();
    private MyListAdapter adapter;
    private int page = 0;

    private MemberShipPopUpWindow memberShipPopUpWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_finance_manage);
        super.onCreate(savedInstanceState);
        showProgressDialog(R.string.loading);
        page = 0;
        doPost();

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MERCHANT_ACCOUNT_LIST:
                cancelProgressDialog();
                MerchantAccountListResult merchantAccountListResult = (MerchantAccountListResult) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                    txtTotalFee.setText(merchantAccountListResult.getTotalFee());
                }
                listData.addAll(merchantAccountListResult.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.setLoadmoreable(merchantAccountListResult.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());
                page++;

                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageFilter = (ImageView) findViewById(R.id.imageFilter);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        listView = (ListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);

        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_member_ship, listView, false);
        listView.addHeaderView(headerView);
        txtTotalFee = (TextView) listView.findViewById(R.id.txtTotalFee);
        txtHeaderTitle = (TextView) listView.findViewById(R.id.txtHeaderTitle);
        txtHeaderTitle.setText("当前收入");
        adapter = new MyListAdapter(mContext);
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageFilter.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                doPost();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                doPost();
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
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    private class MyListAdapter extends MyAdapter {

        public MyListAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_finance_manage, null, false);
            ViewHolder holder = new ViewHolder(convertView);
            AccountListModel model = listData.get(position);
            /*switch (model.getKeytype()) {
                case "5":
                    holder.txtTitle.setText("盈利");
                    break;
                case "6":
                    holder.txtTitle.setText("佣金");
                    break;
                case "7":
                    holder.txtTitle.setText("退款获得");
                    break;
            }*/
            holder.txtTitle.setText(model.getContent());
            holder.txtDatetime.setText(model.getRegdate());
            String amount = model.getAmount().indexOf("-") >= 0 ? model.getAmount() : "+" + model.getAmount();
            holder.txtPrice.setText(amount);
            return convertView;
        }

        private class ViewHolder {
            private TextView txtTitle, txtDatetime, txtPrice;

            public ViewHolder(View convertView) {
                txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
                txtDatetime = (TextView) convertView.findViewById(R.id.txtDatetime);
                txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            }
        }
    }

    private void showMemberShipPopUpWindow() {
        if (memberShipPopUpWindow == null) {
            memberShipPopUpWindow = new MemberShipPopUpWindow(mContext, new ArrayList<DistrictListModel>());
            memberShipPopUpWindow.hideLayoutDistricts();
            memberShipPopUpWindow.setTitleContent("入账时间");
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
                    memberShipPopUpWindow.dismiss();
                    page = 0;
                    showProgressDialog(R.string.loading);
                    doPost();
                }
            });
        }
        memberShipPopUpWindow.showAsDropDown(imageFilter);
    }

    private void doPost() {
        String start = "", end = "";
        if (memberShipPopUpWindow != null) {
            start = memberShipPopUpWindow.getBeginDate();
            end = memberShipPopUpWindow.getEndDate();
        }
        getNetWorker().merchantAccountList(getApplicationContext().getUser().getToken(), start, end, String.valueOf(page));
    }
}
