package com.hemaapp.wnw.activity.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.GroupListModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.result.MyArrayResult;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 添加为团购商品
 * Created by HuHu on 2016-09-09.
 */
public class AddGroupSaleActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity, imageAdd;
    private ListView listView;
    private ListAdapter adapter;
    private ArrayList<GroupListModel> listData = new ArrayList<>();
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_group);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getNetWorker().groupList(id);
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
            case MyConfig.REFRESH_GROUP_LIST:
                getNetWorker().groupList(id);
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_LIST:
                showProgressDialog(R.string.loading);
                break;
            case GROUP_DELETE:
                showProgressDialog(R.string.committing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_LIST:
            case GROUP_DELETE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_LIST:
                MyArrayResult<GroupListModel> result = (MyArrayResult<GroupListModel>) hemaBaseResult;
                listData.clear();
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                break;
            case GROUP_DELETE:
                String id = hemaNetTask.getParams().get("id");
                for (int i = 0; i < listData.size(); i++) {
                    if (id.equals(listData.get(i).getId())) {
                        listData.remove(i);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showMyOneButtonDialogFinish("设为团购", hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        showMyOneButtonDialogFinish("设为团购", "请求失败");
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageAdd = (ImageView) findViewById(R.id.imageAdd);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListAdapter(mContext);
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageAdd.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, AddGroupSaleDetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("isAdd", false);
                intent.putExtra("model", listData.get(position));
                startActivity(intent);
                changeAnim();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.imageAdd:
                String history = "";
                for (GroupListModel model : listData) {
                    if (isNull(history)) {
                        history = model.getType_id();
                    } else {
                        history += "," + model.getType_id();
                    }
                }
                Intent intent = new Intent(mContext, AddGroupSaleDetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("history", history);
                if (listData.size() > 0) {
                    GroupListModel model = new GroupListModel(listData.get(0).getRule_id(), listData.get(0).getRule_name());
                    intent.putExtra("model", model);
                }
                startActivity(intent);
                changeAnim();
                break;
        }
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (isEmpty()) {
                setEmptyString("还没设置团购");
                return getEmptyView(parent);
            }
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_group_list, null, false);
            ViewHolder holder = new ViewHolder(convertView);
            GroupListModel model = listData.get(position);
            holder.txtType.setText(model.getPerson() + "人团");
            holder.txtName.setText(model.getRule_name());
            holder.txtPrice.setText("￥" + model.getGroup_price());
            holder.txtTime.setText(model.getHour() + "小时");
            int color = R.color.colorPrimary;
            switch (position % 3) {
                case 0:
                    color = R.color.colorPrimary;
                    break;
                case 1:
                    color = R.color.purple_red;
                    break;
                case 2:
                    color = R.color.orange_yellow;
                    break;
            }
            holder.txtType.setBackgroundColor(mContext.getResources().getColor(color));
            holder.txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNetWorker().groupDelete(getApplicationContext().getUser().getToken(),
                            listData.get(position).getId());
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private TextView txtType, txtName, txtPrice, txtTime, txtDelete;

            public ViewHolder(View convertView) {
                txtType = (TextView) convertView.findViewById(R.id.txtType);
                txtName = (TextView) convertView.findViewById(R.id.txtName);
                txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
                txtTime = (TextView) convertView.findViewById(R.id.txtTime);
                txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
            }
        }
    }
}
