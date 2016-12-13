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

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.GroupTypeListModel;
import com.hemaapp.wnw.result.MyArrayResult;

import java.util.ArrayList;

/**
 * 选择团购类型的界面
 * Created by HuHu on 2016-09-09.
 */
public class SelectGroupTypeActivity extends MyActivity implements View.OnClickListener {

    private ImageView imageQuitActivity;
    private TextView txtTitle, txtNext;
    private ListView listView;
    private ListAdapter adapter;
    private ArrayList<GroupTypeListModel> listData = new ArrayList<>();
    private String history;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_listview);
        super.onCreate(savedInstanceState);
        getNetWorker().groupTypeList();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        showProgressDialog(R.string.loading);
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_TYPE_LIST:
                MyArrayResult<GroupTypeListModel> result = (MyArrayResult<GroupTypeListModel>) hemaBaseResult;
                listData.clear();
                listData.addAll(result.getObjects());
                String[] historys = history.split(",");
                for (String id : historys) {
                    for (int i = 0; i < listData.size(); i++) {
                        if (id.equals(listData.get(i).getId())) {
                            listData.remove(i);
                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
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
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("团购类型");
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtNext.setText("确定");
        txtNext.setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListAdapter(mContext);
        listView.setAdapter(adapter);

    }

    @Override
    protected void getExras() {
        history = mIntent.getStringExtra("history");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtNext.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listData.get(position).setSelected(!listData.get(position).isSelected());
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.txtNext:
                clickConfirm();
                break;
        }
    }

    private void clickConfirm() {
        String id = "";
        String name = "";
        for (GroupTypeListModel model : listData) {
            if (!model.isSelected()) {
                continue;
            }
            if (isNull(id)) {
                id = model.getId();
                name = model.getPerson() + "人团";
            } else {
                id += "," + model.getId();
                name += "," + model.getPerson() + "人团";
            }
        }

        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        setResult(RESULT_OK, intent);
        MyFinish();
    }

    private class ListAdapter extends MyAdapter {

        public ListAdapter(Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_textview_checkimage, null, false);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(listData.get(position).getPerson() + "人团");

            ImageView imageCheck = (ImageView) convertView.findViewById(R.id.imageCheck);
            int imageRes = listData.get(position).isSelected() ? R.drawable.icon_choice_check : R.drawable.icon_choice;
            imageCheck.setImageResource(imageRes);

            return convertView;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }
}
