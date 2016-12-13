package com.hemaapp.wnw.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.R;
import com.hemaapp.wnw.fragment.FirstloadFragment;

import java.io.IOException;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 引导页界面
 * Created by HuHu on 2016/5/6.
 */
public class WelcomeActivity extends FragmentActivity {
    private ViewPager viewPager;
    private ImageView imagePoints;
    private TextView txtConfirm;
    private ImageWelcomePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideBar();
        setContentView(R.layout.activity_welcome);
        super.onCreate(savedInstanceState);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imagePoints = (ImageView) findViewById(R.id.imagePoints);
        txtConfirm = (TextView) findViewById(R.id.txtConfirm);
        txtConfirm.setVisibility(View.GONE);
        String[] imgs = new String[]{"image_welcome1.jpg",
                "image_welcome2.jpg", "image_welcome3.jpg"};
        adapter = new ImageWelcomePagerAdapter(getSupportFragmentManager(), imgs, this);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        imagePoints.setVisibility(View.VISIBLE);
                        txtConfirm.setVisibility(View.GONE);
                        imagePoints.setImageResource(R.drawable.page_1);
                        break;
                    case 1:
                        imagePoints.setVisibility(View.VISIBLE);
                        txtConfirm.setVisibility(View.GONE);
                        imagePoints.setImageResource(R.drawable.page_2);
                        break;
                    case 2:
                        imagePoints.setVisibility(View.GONE);
                        txtConfirm.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XtomSharedPreferencesUtil.save(WelcomeActivity.this, "FirstLoadFlag", "你管我写什么");
                Intent intent = new Intent(WelcomeActivity.this, MainFragmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                finish();
            }
        });
    }

    private class ImageWelcomePagerAdapter extends FragmentPagerAdapter {
        private BitmapDrawable[] imgs = new BitmapDrawable[3];

        public ImageWelcomePagerAdapter(FragmentManager fm, String[] imgs, Context context) {
            super(fm);
            try {
                this.imgs[0] = new BitmapDrawable(context.getAssets().open(imgs[0]));
                this.imgs[1] = new BitmapDrawable(context.getAssets().open(imgs[1]));
                this.imgs[2] = new BitmapDrawable(context.getAssets().open(imgs[2]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Fragment getItem(int position) {
            return FirstloadFragment.getInstance(position, imgs);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @SuppressLint("NewApi")
    private void hideBar() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // 透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 5.0的全透明设置
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 导航栏
            // 加
            // |
            // WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | // 导航栏
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);// 导航栏
        }
    }
}
