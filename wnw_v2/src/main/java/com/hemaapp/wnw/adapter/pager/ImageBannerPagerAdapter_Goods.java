package com.hemaapp.wnw.adapter.pager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.hemaapp.MyConfig;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.MyShowLargePicActivity;
import com.hemaapp.wnw.model.BannerModel;
import com.hemaapp.wnw.model.GoodsDetailModel;
import com.hemaapp.wnw.model.Image;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;

/**
 * 图片轮播适配器
 * Created by Hufanglin on 2016/2/22.
 */
public class ImageBannerPagerAdapter_Goods extends PagerAdapter {
    private Context mContext;
    private MyFragmentActivity activity;
    private List<GoodsDetailModel.ImgItemsEntity> adListData;
    private ArrayList<Image> listImage = new ArrayList<>();
    private int imageCount = 0;
    private int Type = -1;//

    public ImageBannerPagerAdapter_Goods(Context mContext, List<GoodsDetailModel.ImgItemsEntity> adListData) {
        this.mContext = mContext;
        this.adListData = adListData;
        activity = (MyFragmentActivity) mContext;
        imageCount = adListData.size();
        for (GoodsDetailModel.ImgItemsEntity imgItemsEntity : adListData) {
            listImage.add(new
                    Image(imgItemsEntity.getId(), imgItemsEntity.getClient_id(), "", "",
                    imgItemsEntity.getImgurl(), imgItemsEntity.getImgurlbig(), imgItemsEntity.getOrderby()));
        }
    }


    @Override
    public void notifyDataSetChanged() {
        imageCount = adListData.size();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageCount <= 1 ? 1 : imageCount;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView view = new ImageView(mContext);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        if (imageCount == 0) {
            view.setImageResource(R.drawable.image_main_temp);
            return view;
        }
        final int index = position % imageCount;
        if (MyConfig.DEBUG) {
            Log.e("index", index + "");
        }

        Glide.with(MyApplication.getInstance()).load(adListData.get(index).getImgurlbig()).placeholder(R.drawable.image_main_temp).into(view);
        view.setTag(index);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//点击看大图
                Intent sIt = new Intent(activity, MyShowLargePicActivity.class);
                sIt.putExtra("position", position);
                sIt.putExtra("images", listImage);
                activity.startActivity(sIt);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
            }
        });
        container.addView(view);

        return view;
    }

}
