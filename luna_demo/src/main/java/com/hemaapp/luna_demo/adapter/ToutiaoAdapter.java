package com.hemaapp.luna_demo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_demo.model.ToutiaoItemBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class ToutiaoAdapter extends RecyclerView.Adapter<ToutiaoAdapter.ViewHolder> {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<ToutiaoItemBean> toutiaoList;

    public ToutiaoAdapter(Activity activity, ArrayList<ToutiaoItemBean> toutiaoList) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.toutiaoList = toutiaoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_toutiao, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return toutiaoList == null ? 0 : toutiaoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //@BindView(R.id.tv_title)
        TextView tvTitle;
        //@BindView(R.id.iv_pic1)
        ImageView ivPic1;
        //@BindView(R.id.iv_pic2)
        ImageView ivPic2;
        //@BindView(R.id.iv_pic3)
        ImageView ivPic3;
        //@BindView(R.id.tv_date)
        TextView tvDate;
        //@BindView(R.id.tv_auther)
        TextView tvAuther;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivPic1 = (ImageView) itemView.findViewById(R.id.iv_pic1);
            ivPic2 = (ImageView) itemView.findViewById(R.id.iv_pic2);
            ivPic3 = (ImageView) itemView.findViewById(R.id.iv_pic3);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvAuther = (TextView) itemView.findViewById(R.id.tv_auther);
            //ButterKnife.bind(this, itemView);
//            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) ivPic1.getLayoutParams();
//            mlp.height = ivPic1.getMeasuredWidth();
//            ivPic1.setLayoutParams(mlp);
//            ivPic2.setLayoutParams(mlp);
//            ivPic3.setLayoutParams(mlp);
        }

        public void setData(int position) {
            ToutiaoItemBean item = toutiaoList.get(position);
            tvTitle.setText(item.getTitle());
            tvDate.setText(item.getDate());
            tvAuther.setText(item.getAuthor_name());
            Glide.with(activity).load(item.getThumbnail_pic_s()).crossFade().into(ivPic1);
            Glide.with(activity).load(item.getThumbnail_pic_s02()).crossFade().into(ivPic2);
            Glide.with(activity).load(item.getThumbnail_pic_s03()).crossFade().into(ivPic3);
        }
    }
}
