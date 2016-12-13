package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.OverseasGoodsActivity;
import com.hemaapp.wnw.model.CountryListModel;

import java.util.ArrayList;

/**
 * 海外优品适配器
 * Created by HuHu on 2016-08-12.
 */
public class OverseasRecyclerAdapter extends RecyclerView.Adapter<OverseasRecyclerAdapter.OverseasRecyclerViewHolder> {
    private Context mContext;
    private ArrayList<CountryListModel> countries = new ArrayList<>();

    public OverseasRecyclerAdapter(Context mContext, ArrayList<CountryListModel> countries) {
        this.mContext = mContext;
        this.countries = countries;
    }

    @Override
    public OverseasRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycleritem_overseas, parent, false);
        OverseasRecyclerViewHolder holder = new OverseasRecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OverseasRecyclerViewHolder holder, int position) {
        final CountryListModel model = countries.get(position);
        Glide.with(MyApplication.getInstance()).load(model.getImgurlbig()).placeholder(R.drawable.logo_default_rectangle).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentActivity activity = (MyFragmentActivity) mContext;
                Intent intent = new Intent(activity, OverseasGoodsActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("title", model.getName());
                activity.startActivity(intent);
                activity.changeAnim();
            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public static class OverseasRecyclerViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;

        public OverseasRecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = (RoundedImageView) itemView.findViewById(R.id.imageView);
            imageView.setCornerRadius(5);
        }
    }
}
