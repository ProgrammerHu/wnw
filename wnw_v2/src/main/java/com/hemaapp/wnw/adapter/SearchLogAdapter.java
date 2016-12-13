package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.SearchLogActivity;
import com.hemaapp.wnw.db.SearchDBHelper;

import java.util.ArrayList;

/**
 * 商品搜索记录适配器
 * Created by HuHu on 2016/3/23.
 */
public class SearchLogAdapter extends MyAdapter {
    private String SearchType;
    private ArrayList<String> listData;
    private SearchDBHelper dbHelper;

    public SearchLogAdapter(Context mContext, ArrayList<String> listData, String SearchType) {
        super(mContext);
        this.listData = listData;
        this.SearchType = SearchType;
        dbHelper = new SearchDBHelper(mContext);
    }

    @Override
    public int getCount() {
        int count = listData == null ? 0 : listData.size();
        return count == 0 ? 1 : count;
    }

    @Override
    public boolean isEmpty() {
        int count = listData == null ? 0 : listData.size();
        return count == 0;
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
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_searchlog, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        holder.txtContent.setText(listData.get(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHelper.DeleteContent(listData.get(position), SearchType)) {
                    //删除成功
                    listData.remove(position);
                    notifyDataSetChanged();
                } else {
                    //删除失败
                    Toast.makeText(mContext, "删除记录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public ViewHolder(View view) {
            txtContent = (TextView) view.findViewById(R.id.txtContent);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }

        private TextView txtContent;
        private ImageView imageView;
    }
}
