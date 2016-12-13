package com.hemaapp.wnw.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.MyRecordAdapter;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import de.greenrobot.event.EventBus;

/**
 * 我的足迹和我的收藏共用界面
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月10日
 */
public class MyRecordAndCollectionActivity extends MyFragmentActivity implements
        OnClickListener {

    private ImageView imageQuitActivity, imageLeft, imageRight;
    private TextView txtNext, txtTitle;
    private View layoutLeft, layoutRight, layoutLeftTop, layoutRightTop;
    private TextView txtLeft, txtRight;
    private ViewPager viewPager;
    private MyRecordAdapter adapter;

    private boolean IsRecord = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_viewpager);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(IsRecord ? "我的足迹" : "我的收藏");
        if (IsRecord)
            txtNext.setVisibility(View.VISIBLE);
        txtNext.setText("清空");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new MyRecordAdapter(getSupportFragmentManager(), IsRecord);
        viewPager.setAdapter(adapter);

        imageLeft = (ImageView) findViewById(R.id.imageLeft);
        imageRight = (ImageView) findViewById(R.id.imageRight);
        layoutLeft = findViewById(R.id.layoutLeft);
        layoutRight = findViewById(R.id.layoutRight);
        layoutLeftTop = findViewById(R.id.layoutLeftTop);
        layoutRightTop = findViewById(R.id.layoutRightTop);
        txtLeft = (TextView) findViewById(R.id.txtLeft);
        txtRight = (TextView) findViewById(R.id.txtRight);

    }

    @Override
    protected void getExras() {
        IsRecord = mIntent.getBooleanExtra("IsRecord", true);
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtNext.setOnClickListener(this);
        imageQuitActivity.setOnClickListener(this);
        imageQuitActivity.setOnClickListener(this);
        layoutLeft.setOnClickListener(this);
        layoutRight.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    onClick(layoutLeft);
                } else {
                    onClick(layoutRight);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.txtNext:
                if (viewPager.getCurrentItem() == 0) {
                    EventBus.getDefault().post(
                            new EventBusModel(true, "ClearGoods"));// 清空足迹中的商品
                } else {
                    EventBus.getDefault()
                            .post(new EventBusModel(true, "ClearNote"));// 清空足迹中的商品
                }
                break;
            case R.id.layoutLeft:
                layoutLeftTop.setVisibility(View.VISIBLE);
                layoutRightTop.setVisibility(View.INVISIBLE);
                txtLeft.setTextColor(getResources().getColor(R.color.main_purple));
                txtRight.setTextColor(getResources().getColor(R.color.grey_text));
                imageLeft.setImageResource(R.drawable.icon_goods_check);
                imageRight.setImageResource(R.drawable.icon_blog);
                viewPager.setCurrentItem(0);
                break;
            case R.id.layoutRight:
                layoutLeftTop.setVisibility(View.INVISIBLE);
                layoutRightTop.setVisibility(View.VISIBLE);
                txtLeft.setTextColor(getResources().getColor(R.color.grey_text));
                txtRight.setTextColor(getResources().getColor(R.color.main_purple));
                imageLeft.setImageResource(R.drawable.icon_goods);
                imageRight.setImageResource(R.drawable.icon_blog_check);
                viewPager.setCurrentItem(1);
                break;
        }

    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }
}
