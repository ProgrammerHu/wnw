package com.hemaapp.wnw.adapter.pager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.hemaapp.MyConfig;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.activity.PostDetailActivity;
import com.hemaapp.wnw.activity.SellerDetailActivity;
import com.hemaapp.wnw.activity.WebviewActivity;
import com.hemaapp.wnw.model.AdModel;
import com.hemaapp.wnw.model.BannerModel;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;

/**
 * 图片轮播适配器
 * Created by Hufanglin on 2016/2/22.
 */
public class ImageBannerPagerAdapter extends PagerAdapter {
    private Context mContext;
    private MyFragmentActivity activity;
    private List<BannerModel> adListData;
    private int imageCount = 0;

    public ImageBannerPagerAdapter(Context mContext, List<BannerModel> adListData) {
        this.mContext = mContext;
        this.adListData = adListData;
        activity = (MyFragmentActivity) mContext;
        imageCount = adListData.size();
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
    public Object instantiateItem(ViewGroup container, int position) {
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


        try {//加载图片
            URL url = new URL(adListData.get(index).getImgurlbig());
            ImageTask imageTask = new ImageTask(view, url, mContext, R.drawable.image_main_temp);
            XtomImageWorker imageWorker = new XtomImageWorker(mContext);
            imageWorker.loadImage(imageTask);
        } catch (MalformedURLException e) {
            view.setImageResource(R.drawable.image_main_temp);
        }

        view.setTag(index);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BannerModel bannerModel = adListData.get(index);
                Intent intent;
                switch (bannerModel.getType()) {//1：新闻 2：商品 3.帖子 4.店铺 5.链接
                    case "1":
                        intent = new Intent(activity, WebviewActivity.class);
                        intent.putExtra("URL", activity.getApplicationContext().getSysInitInfo().getSys_plugins()
                                + "/share/article.php?id=" + bannerModel.getId());
                        intent.putExtra("Title", "新闻");
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        break;
                    case "2":
                        intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("goods_id", bannerModel.getBlog_id());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        break;
                    case "3":
                        intent = new Intent(activity, PostDetailActivity.class);
                        intent.putExtra("id", bannerModel.getBlog_id());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        break;
                    case "4":
                        intent = new Intent(activity, SellerDetailActivity.class);
                        intent.putExtra("client_id", bannerModel.getBlog_id());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        break;
                    case "5":
                        intent = new Intent(activity, WebviewActivity.class);
                        intent.putExtra("URL", bannerModel.getBlog_id());
                        intent.putExtra("Title", "新闻");
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        break;
                }
            }
        });
        container.addView(view);

        return view;
    }


}
