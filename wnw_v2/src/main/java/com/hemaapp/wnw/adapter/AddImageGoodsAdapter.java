package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.MyShowLargePicActivity;
import com.hemaapp.wnw.activity.SelectImageEditActivity;
import com.hemaapp.wnw.activity.business.PublicGoodsActivity;
import com.hemaapp.wnw.model.Image;

import java.io.File;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;

/**
 * 添加商品图片的适配器
 * Created by Hufanglin on 2016/3/18.
 */
public class AddImageGoodsAdapter extends MyAdapter implements View.OnClickListener {

    private final int MAX_COUNT = 5;
    private static final int TYPE_ADD = 0;
    private static final int TYPE_IMAGE = 1;
    private int ImageWidth;

    private View rootView;
    private ArrayList<String[]> images;
    private ShowLargeImageView mView;
    private SelectImageEditActivity activity;

    public AddImageGoodsAdapter(Context mContext, View rootView, ArrayList<String[]> images) {
        super(mContext);
        this.rootView = rootView;
        this.images = images;
        activity = (SelectImageEditActivity) mContext;
        ImageWidth = (MyUtil.getScreenWidth(mContext) - MyUtil.dip2px(mContext, 5) * MAX_COUNT
                - MyUtil.dip2px(mContext, 15) * 2) / MAX_COUNT;
    }

    @Override
    public int getCount() {
        int count;
        int size = images == null ? 0 : images.size();
        if (size < MAX_COUNT)
            count = size + 1;
        else
            count = MAX_COUNT;
        return count;
    }

    @Override
    public boolean isEmpty() {
        int size = images == null ? 0 : images.size();
        return size == 0;
    }

    @Override
    public int getItemViewType(int position) {
        int size = images == null ? 0 : images.size();
        int count = getCount();
        if (size < MAX_COUNT) {
            if (position == count - 1)
                return TYPE_ADD;
            else
                return TYPE_IMAGE;
        } else {
            return TYPE_IMAGE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
        int type = getItemViewType(position);
        ViewHolder holder = null;
        if (convertView == null) {
            switch (type) {
                case TYPE_ADD:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem_cardview_add, parent, false);
                    holder = new ViewHolder();
                    holder.addButton = (ImageView) convertView.findViewById(R.id.addButton);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.addButton.getLayoutParams();
                    params.width = params.height = ImageWidth;
                    holder.addButton.setLayoutParams(params);
                    convertView.setTag(holder);
                    break;
                case TYPE_IMAGE:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem_cardview_addimage, parent, false);
                    holder = new ViewHolder();
                    holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                    holder.btnDelete = (ImageView) convertView.findViewById(R.id.btnDelete);

                    FrameLayout.LayoutParams imageViewParams = (FrameLayout.LayoutParams) holder.imageView.getLayoutParams();
                    imageViewParams.width = imageViewParams.height = ImageWidth;
                    holder.imageView.setLayoutParams(imageViewParams);
                    convertView.setTag(holder);
                    break;
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case TYPE_ADD:
                setAdd(position, holder);
                break;
            case TYPE_IMAGE:
                setImage(position, holder);
                break;
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton://添加
                activity.showSelectImageDialog();
                break;
            default:
                break;
        }
    }


    private class ViewHolder {
        private ImageView addButton;
        private ImageView imageView;
        private ImageView btnDelete;
    }

    private void setAdd(int position, ViewHolder holder) {
        holder.addButton.setOnClickListener(this);
    }

    /**
     * 设置图片数据
     *
     * @param position
     * @param holder
     */
    private void setImage(final int position, ViewHolder holder) {
        String path = images.get(position)[1];
        holder.btnDelete.setTag(path);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images.remove(position);
                notifyDataSetChanged();
            }
        });
//        XtomImageTask imageTask = new XtomImageTask(holder.imageView, path, mContext);
//        activity.imageWorker.loadImage(imageTask);
        Glide.with(MyApplication.getInstance()).load(path).placeholder(R.drawable.logo_default_square).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sIt = new Intent(mContext, MyShowLargePicActivity.class);
                sIt.putExtra("position", position);
                sIt.putExtra("images", getImages());
                activity.startActivity(sIt);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
            }
        });
    }

    /**
     * 格式化数据
     *
     * @return
     */
    private ArrayList<Image> getImages() {
        ArrayList<Image> imageList = new ArrayList<>();
        int i = 0;
        for (String[] image : this.images) {
            imageList.add(new Image(String.valueOf(i), String.valueOf(i), String.valueOf(i + 1), "", image[1], image[0], String.valueOf(i)));
            i++;
        }
        return imageList;
    }

    public String getImageUrlStr() {
        String result = "";
        for (String[] strings : images) {
            if (isNull(result)) {
                result = strings[0] + "," + strings[1];
            } else {
                result += ";" + strings[0] + "," + strings[1];
            }
        }
        return result;
    }

//    public ArrayList<String[]> getImageStrings() {
//        return images;
//    }

}
