package com.hemaapp.wnw.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.R;

/**
 * 引导页分页图片
 * Created by HuHu on 2016/5/6.
 */
public class FirstloadFragment extends MyFragment {
    private int position;
    private BitmapDrawable[] imgs;
    private ImageView imageView;

    /**
     * 初始化
     *
     * @return
     */
    public static FirstloadFragment getInstance(int position, BitmapDrawable[] imgs) {
        FirstloadFragment fragment = new FirstloadFragment();
        fragment.position = position;
        fragment.imgs = imgs;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_firstimage);
        super.onCreate(savedInstanceState);
        imageView.setImageDrawable(imgs[position]);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageView = (ImageView) findViewById(R.id.imageView);

    }

    @Override
    protected void setListener() {

    }
}
