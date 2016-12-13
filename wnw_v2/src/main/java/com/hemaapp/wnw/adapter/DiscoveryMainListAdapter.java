package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.model.GoodsListModel;

import java.util.ArrayList;

/**
 * 发现-商品首页-列表
 * Created by HuHu on 2016-08-12.
 */
public class DiscoveryMainListAdapter extends MyAdapter {
    private int smallImageWidth;//保存小图片的变长，动态设置图片的高度
    private int bigImageHeight;//保存大图片的高度，动态设置

    private ArrayList<GoodsListModel> goodsListModels = new ArrayList<>();

    public DiscoveryMainListAdapter(Context mContext, ArrayList<GoodsListModel> goodsListModels) {
        super(mContext);
        this.goodsListModels = goodsListModels;
        smallImageWidth = (MyUtil.getScreenWidth(mContext) - MyUtil.dip2px(mContext, 10 * 5)) / 4;
        bigImageHeight = MyUtil.getScreenWidth(mContext) * 330 / 640;
    }

    @Override
    public int getCount() {
        return goodsListModels.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_goods_discovery, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(position, holder);
        return convertView;
    }

    private void setData(int position, ViewHolder holder) {
        final GoodsListModel model = goodsListModels.get(position);
        holder.txtName.setText(model.getName());
        holder.txtPrize.setText("￥" + model.getPrice());
        holder.txtOldPrize.setText("￥" + model.getOldprice());
        if (model.getImgItems() == null || model.getImgItems().size() == 0) {
            holder.layoutImages.setVisibility(View.GONE);
            holder.imageView.setImageResource(R.drawable.image_main_temp);
        } else {
            holder.layoutImages.setVisibility(View.VISIBLE);
            Glide.with(MyApplication.getInstance()).load(model.getImgItems().get(model.getSelectImgIndex()).
                    getImgurlbig()).placeholder(R.drawable.image_main_temp).into(holder.imageView);
            for (int i = 0; i < holder.imageViews.size(); i++) {
                if (i < model.getImgItems().size()) {
                    holder.imageViews.get(i).setVisibility(View.VISIBLE);
                    Glide.with(MyApplication.getInstance()).load(model.getImgItems().get(i).getImgurl()).
                            placeholder(R.drawable.logo_default_square).into(holder.imageViews.get(i));
                    final int index = i;
                    holder.imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            model.setSelectImgIndex(index);
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    holder.imageViews.get(i).setVisibility(View.INVISIBLE);
                }
            }
        }

        holder.txtBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("goods_id", model.getId());
                intent.putExtra("ShowDialog", true);
                MyFragmentActivity activity = (MyFragmentActivity) mContext;
                activity.startActivity(intent);
                activity.changeAnim();
            }
        });
        holder.father.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("goods_id", model.getId());
                intent.putExtra("ShowDialog", false);
                MyFragmentActivity activity = (MyFragmentActivity) mContext;
                activity.startActivity(intent);
                activity.changeAnim();
            }
        });

    }

    private class ViewHolder {
        public ViewHolder(View convertView) {
            father = convertView.findViewById(R.id.father);
            layoutImages = convertView.findViewById(R.id.layoutImages);
            layoutImages.getLayoutParams().height = smallImageWidth;
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.getLayoutParams().height = bigImageHeight;
            imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
            imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
            imageView3 = (ImageView) convertView.findViewById(R.id.imageView3);
            imageView4 = (ImageView) convertView.findViewById(R.id.imageView4);

            txtName = (TextView) convertView.findViewById(R.id.txtName);
//            txtTimeDiff = (TextView) convertView.findViewById(R.id.txtTimeDiff);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtOldPrize = (TextView) convertView.findViewById(R.id.txtOldPrize);
            txtOldPrize.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txtOldPrize.getPaint().setAntiAlias(true);
            txtBuy = (TextView) convertView.findViewById(R.id.txtBuy);
//            txtPayCount = (TextView) convertView.findViewById(R.id.txtPayCount);
//            txtStockCount = (TextView) convertView.findViewById(R.id.txtStockCount);
            imageViews.add(imageView1);
            imageViews.add(imageView2);
            imageViews.add(imageView3);
            imageViews.add(imageView4);
        }

        private View layoutImages, father;
        private ImageView imageView, imageView1, imageView2, imageView3, imageView4;
        private TextView txtName, txtPrize, txtOldPrize, txtBuy;
        private ArrayList<ImageView> imageViews = new ArrayList<>();
    }
}
