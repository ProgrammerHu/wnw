package com.hemaapp.wnw.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.SellerDetailActivity;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;

import xtom.frame.view.XtomListView;

/**
 * 主页Fragment适配器
 * Created by Hufanglin on 2016/2/22.
 */
public class TagListWithoutHeadAdapter extends MyAdapter implements View.OnClickListener {
    private XtomListView listView;
    private MyActivity activity;

    //    private List<Image>
    public TagListWithoutHeadAdapter(MyActivity activity, XtomListView listView) {
        super(activity);
        this.listView = listView;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return 100;
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_blog_seller, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }

        setData(holder, position);
        setListener(holder, position);

        return convertView;
    }

    private class ViewHolder {

        public ViewHolder(View convertView) {
            imageBackground = (ImageView) convertView.findViewById(R.id.imageBackground);
            imageComment = (ImageView) convertView.findViewById(R.id.imageComment);
            imagePraise = (ImageView) convertView.findViewById(R.id.imagePraise);
            txtAction = (TextView) convertView.findViewById(R.id.txtAction);
            txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            txtTimeDiff = (TextView) convertView.findViewById(R.id.txtTimeDiff);
            txtComment = (TextView) convertView.findViewById(R.id.txtComment);
            txtPraise = (TextView) convertView.findViewById(R.id.txtPraise);
        }

        private ImageView imageBackground, imageComment, imagePraise;
        private TextView txtAction, txtContent, txtTimeDiff, txtComment, txtPraise;
    }

    private void setData(ViewHolder holder, int position) {

        try {
            URL url = new URL("http://7tszkm.com1.z0.glb.clouddn.com/queen1.png");
            ImageTask imageTask = new ImageTask(holder.imageBackground, url, mContext, R.drawable.image_main_temp);
            listView.addTask(position, 0, imageTask);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定事件
     *
     * @param holder
     * @param position
     */
    private void setListener(ViewHolder holder, int position) {

    }


    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.TAG);
        Intent intent;
        switch (v.getId()) {
            case R.id.imageHead:
                intent = new Intent(mContext, SellerDetailActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
        }

    }

}
