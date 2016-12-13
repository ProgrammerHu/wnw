package com.hemaapp.wnw.nettask;

/**
 * 带有默认图片的图片加载线程
 * Created by Hufanglin on 2016/2/23.
 */

import android.view.View;
import android.widget.ImageView;


import java.net.URL;

import xtom.frame.image.load.XtomImageTask;

/**
 * 图片加载任务
 *
 * @author Wen
 * @author HuFanglin
 */
public class ImageTask extends XtomImageTask {

    private int beforeImage;

    public ImageTask(ImageView imageView, URL url, Object context, int beforeImage) {
        super(imageView, url, context);
        this.beforeImage = beforeImage;
    }

    public ImageTask(ImageView imageView, URL url, Object context, View fatherView, int beforeImage) {
        super(imageView, url, context, fatherView);
        this.beforeImage = beforeImage;
    }


    @Override
    public void beforeload() {
        super.beforeload();
        imageView.setImageResource(beforeImage);
    }

    @Override
    public void failed() {
        super.failed();
        imageView.setImageResource(beforeImage);
    }


}
