package com.hemaapp.wnw.activity.business;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.DistrictListModel;
import com.hemaapp.wnw.model.FansListModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 查看粉丝
 * Created by HuHu on 2016-08-17.
 */
public class FansListActivity extends MyActivity {
    private ImageView imageQuitActivity, imageToTop;
    private TextView txtTitle;
    private ListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private ListAdapter adapter;
    private ArrayList<FansListModel> listData = new ArrayList<>();
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refresh_listview);
        super.onCreate(savedInstanceState);
        showProgressDialog(R.string.loading);
        page = 0;
        getNetWorker().fansList(getApplicationContext().getUser().getToken(), String.valueOf(page));
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
            case FANS_LIST:
                cancelProgressDialog();
                MyArrayResult<FansListModel> result = (MyArrayResult<FansListModel>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                refreshLoadmoreLayout.setLoadmoreable(result.getObjects().size() >=
                        getApplicationContext().getSysInitInfo().getSys_pagesize());
                page++;
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        refreshLoadmoreLayout.loadmoreFailed();
        refreshLoadmoreLayout.refreshFailed();
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("查看粉丝");
        listView = (ListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        adapter = new ListAdapter(mContext);
        listView.setAdapter(adapter);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
        imageToTop.setVisibility(View.VISIBLE);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFinish();
            }
        });
        imageToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(0);
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().fansList(getApplicationContext().getUser().getToken(), String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().fansList(getApplicationContext().getUser().getToken(), String.valueOf(page));
            }
        });
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    private class ListAdapter extends MyAdapter {

        public ListAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getCount() {
            return listData.size() == 0 ? 1 : listData.size();
        }

        @Override
        public boolean isEmpty() {
            return listData.size() == 0;
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
            if (isEmpty()) {
                setEmptyString("一个粉丝都没有");
                return getEmptyView(parent);
            }
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_fans_my, null, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
            } else {
                holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
            }
            setData(position, holder);
            return convertView;
        }

        private void setData(int position, ViewHolder holder) {
            FansListModel model = listData.get(position);
            if (isNull(model.getSex())) {
                holder.imageSex.setVisibility(View.GONE);
            } else {
                holder.imageSex.setVisibility(View.VISIBLE);
            }
            int sex = "男".equals(model.getSex()) ? R.drawable.icon_male : R.drawable.icon_female;
            holder.imageSex.setImageResource(sex);
            holder.txtName.setText(model.getNickname());
            holder.txtTel.setText("(" + model.getUsername() + ")");
            if (isNull(model.getInviter_username()) || "null".equals(model.getInviter_username())) {
                holder.txtInvitor.setText("邀请人：无");
            } else {
                holder.txtInvitor.setText("邀请人：" + model.getInviter_nickname() + "(" + model.getInviter_username() + ")");
            }
            Glide.with(MyApplication.getInstance()).load(model.getAvatar()).placeholder(R.drawable.icon_register_head).into(holder.imageView);
        }

        private class ViewHolder {
            public ViewHolder(View convertView) {
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                imageSex = (ImageView) convertView.findViewById(R.id.imageSex);
                txtName = (TextView) convertView.findViewById(R.id.txtName);
                txtTel = (TextView) convertView.findViewById(R.id.txtTel);
                txtInvitor = (TextView) convertView.findViewById(R.id.txtInvitor);
            }

            private ImageView imageView, imageSex;
            private TextView txtName, txtTel, txtInvitor;
        }
    }
}
