package com.hemaapp.wnw.activity;

import android.content.Intent;
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
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.PagerAdapterNotice;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog.OnButtonListener;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import de.greenrobot.event.EventBus;

/**
 * 我的消息界面
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月8日
 */
public class NoticeActivity extends MyFragmentActivity implements
        OnClickListener {
    private ImageView imageQuitActivity, imageLeft, imageRight;
    private View layoutLeft, layoutRight, layoutLeftTop, layoutRightTop;
    private TextView txtClear, txtLeft, txtRight;
    private ViewPager viewPager;
    private PagerAdapterNotice adapter;
    private MyTwoButtonDialog ClearDialog;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notice);
        super.onCreate(savedInstanceState);
        if (position == 1) {
            onClick(layoutRight);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        EventBus.getDefault().post(new EventBusModel(true, "RefreshReply"));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        MyHttpInformation information = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case REMOVE_ROOT:
                showProgressDialog(R.string.delete);
                break;

            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        MyHttpInformation information = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case REMOVE_ROOT:
                cancelProgressDialog();
                break;

            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        MyHttpInformation information = (MyHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case REMOVE_ROOT:
                if (viewPager.getCurrentItem() == 0) {
                    EventBus.getDefault().post(new EventBusModel(true, "IsReply"));//
                } else {
                    EventBus.getDefault().post(new EventBusModel(false, "IsReply"));//
                }
                break;

            default:
                break;
        }
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
        txtClear = (TextView) findViewById(R.id.txtClear);
        txtLeft = (TextView) findViewById(R.id.txtLeft);
        txtRight = (TextView) findViewById(R.id.txtRight);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new PagerAdapterNotice(getSupportFragmentManager());
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
        position = mIntent.getIntExtra("position", 0);

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtClear.setOnClickListener(this);
        layoutLeft.setOnClickListener(this);
        layoutRight.setOnClickListener(this);
        txtClear.setOnClickListener(this);
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
                EventBus.getDefault().post(new EventBusModel(true, "NoticeDestroy"));
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.txtClear:
                showClearDialog();
                break;
            case R.id.layoutLeft:
                layoutLeftTop.setVisibility(View.VISIBLE);
                layoutRightTop.setVisibility(View.INVISIBLE);
                txtLeft.setTextColor(getResources().getColor(R.color.main_purple));
                txtRight.setTextColor(getResources().getColor(R.color.grey_text));
                imageLeft.setImageResource(R.drawable.icon_notice_checked);
                imageRight.setImageResource(R.drawable.icon_earth);
                viewPager.setCurrentItem(0);
                break;
            case R.id.layoutRight:
                layoutLeftTop.setVisibility(View.INVISIBLE);
                layoutRightTop.setVisibility(View.VISIBLE);
                txtLeft.setTextColor(getResources().getColor(R.color.grey_text));
                txtRight.setTextColor(getResources().getColor(R.color.main_purple));
                imageLeft.setImageResource(R.drawable.icon_notice);
                imageRight.setImageResource(R.drawable.icon_earth_check);
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        EventBus.getDefault().post(new EventBusModel(true, "NoticeDestroy"));
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    private void showClearDialog() {
        String text = viewPager.getCurrentItem() == 0 ? "留言回复" : "系统消息";
        if (ClearDialog == null) {
            ClearDialog = new MyTwoButtonDialog(mContext);
            ClearDialog.setLeftButtonText("马上清空").setRightButtonText("我再等等")
                    .setTitle("清空").setText("(>_<)确定要清空" + text + "吗");
            ClearDialog.setOnButtonClickListener(new OnButtonListener() {

                @Override
                public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                    twoButtonDialog.cancel();
                }

                @Override
                public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {
                    twoButtonDialog.cancel();
                    String keytype = viewPager.getCurrentItem() == 0 ? "1"
                            : "2";
                    getNetWorker().removeRoot(
                            getApplicationContext().getUser().getToken(),
                            keytype, "2", "0");
                }

                @Override
                public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                    twoButtonDialog.cancel();
                }
            });
        }
        ClearDialog.show();
    }
}
