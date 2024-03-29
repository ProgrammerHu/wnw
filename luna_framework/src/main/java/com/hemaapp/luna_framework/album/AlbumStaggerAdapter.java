package com.hemaapp.luna_framework.album;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.hemaapp.hm_FrameWork.album.CommonAdapter;
import com.hemaapp.hm_FrameWork.album.ViewHolder;
import com.hemaapp.luna_framework.R;
import com.hemaapp.luna_framework.view.AnimateCheckBox;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.util.XtomToastUtil;

/**
 * 相册适配器
 * Created by HuHu on 2016/4/4.
 */
public class AlbumStaggerAdapter extends CommonAdapter<String> {

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public ArrayList<String> mSelectedImage = new ArrayList<String>();

    /**
     * 文件夹路径
     */
    private String mDirPath;
    /**
     * 图片选择张数限制
     */
    private int limitCount;

    public AlbumStaggerAdapter(Context context, List<String> mDatas,
                               int itemLayoutId, String dirPath, int limitCount) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
        this.limitCount = limitCount;
    }

    /**
     * @param mDirPath the mDirPath to set
     */
    public void setDirPath(String mDirPath) {
        this.mDirPath = mDirPath;
    }

    @Override
    public void convert(final ViewHolder helper, final String item) {
        // 设置no_pic
        helper.setImageResource(R.id.id_item_image,
                R.drawable.album_pictures_no);
        // 设置图片
        helper.setImageByUrl(R.id.id_item_image, item);

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final AnimateCheckBox checkBox = helper.getView(R.id.checkBox);
        checkBox.setCircleColor(mContext.getResources().getColor(R.color.colorPrimary));
        mImageView.setColorFilter(null);
        // 设置ImageView的点击事件
        mImageView.setOnClickListener(new View.OnClickListener() {
            // 选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                // 已经选择过该图片
                if (mSelectedImage.contains(item)) {
                    mSelectedImage.remove(item);
                    mImageView.setColorFilter(null);
                    checkBox.setChecked(false);
                } else
                // 未选择该图片
                {
                    if (limitCount > 0) {
                        int size = mSelectedImage.size();
                        if (size >= limitCount) {
                            XtomToastUtil.showShortToast(mContext, "最多只能选择"
                                    + limitCount + "张图片哦。");
                            return;
                        }
                    }
                    mSelectedImage.add(item);
                    mImageView.setColorFilter(Color.parseColor("#77000000"));
                    checkBox.setChecked(true);
                }

            }
        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(item)) {
            mImageView.setColorFilter(Color.parseColor("#77000000"));
            checkBox.setChecked(true);
        } else {
            mImageView.setColorFilter(null);
            checkBox.setChecked(false);
        }

    }

}