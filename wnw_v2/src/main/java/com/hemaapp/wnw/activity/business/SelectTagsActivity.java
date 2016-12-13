package com.hemaapp.wnw.activity.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.Tag;
import com.hemaapp.wnw.result.MyArrayResult;

import java.util.ArrayList;

/**
 * 选择相关标签界面
 * Created by HuHu on 2016-08-16.
 */
public class SelectTagsActivity extends MyActivity implements View.OnClickListener {

    private ImageView imageQuitActivity;
    private TextView txtTitle, txtNext;
    private GridView gridView;
    private GridViewAdapter adapter;
    private ArrayList<Tag> tags = new ArrayList<>();
    private String ids;
    private String[] idsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_tags);
        super.onCreate(savedInstanceState);
        getNetWorker().tableList();
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
            case TABLE_LIST:
                MyArrayResult<Tag> tagResult = (MyArrayResult<Tag>) hemaBaseResult;
                tags.clear();
                tags.addAll(tagResult.getObjects());
                adapter.initSelected();
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("选择相关标签");
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtNext.setVisibility(View.VISIBLE);
        txtNext.setText("保存");
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new GridViewAdapter(mContext);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {
        ids = mIntent.getStringExtra("ids");
        if (!isNull(ids)) {
            idsArray = ids.split(",");
        }
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtNext.setOnClickListener(this);
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

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    /**
     * 点保存
     */
    private void clickConfirm() {
        String id = "";
        int count = 0;
        for (Tag tag : tags) {
            if (tag.isSelected() && isNull(id)) {
                id = tag.getId();
                count++;
            } else if (tag.isSelected()) {
                id += "," + tag.getId();
                count++;
            }
        }
        if (isNull(id)) {
            showTextDialog("请选择标签");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("table_id", id);
        intent.putExtra("table_count", count);
        setResult(RESULT_OK, intent);
        MyFinish();
    }

    private class GridViewAdapter extends MyAdapter {

        public GridViewAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getCount() {
            return tags.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_textview, null, false);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(tags.get(position).getName());
            int textColor = tags.get(position).isSelected() ? R.color.white : R.color.black_light;
            int background = tags.get(position).isSelected() ? R.drawable.bg_purple_radius_5dp : R.drawable.bg_white_border_grey_radius_5dp;
            textView.setBackgroundResource(background);
            textView.setTextColor(mContext.getResources().getColor(textColor));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tags.get(position).setSelected(!tags.get(position).isSelected());
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        public void initSelected() {
            if (idsArray == null) {
                return;
            }
            for (Tag tag : tags) {
                for (String id : idsArray) {
                    if (id.equals(tag.getId())) {
                        tag.setSelected(true);
                    }
                }
            }
        }
    }
}
