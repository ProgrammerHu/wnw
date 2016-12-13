package com.hemaapp.luna_demo.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_framework.MyUtil;

/**
 * 试试属性动画
 * Created by HuHu on 2016/5/3.
 */
public class PropertyAnimationActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private ImageView imageView;
    private Button button, btnRotate;
    private View linearLayout, father;
    private TextView textView;
    private int deltaY;

    private int screenHeight, imageHeight, linearHeight, y0, y1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        mContext = this;
        father = findViewById(R.id.father);
        father.post(new Runnable() {
            @Override
            public void run() {
                screenHeight = father.getHeight();

//        screenHeight = getMeasuredHeight(father);
                imageHeight = imageView.getHeight();
                linearHeight = linearLayout.getHeight();
                y0 = screenHeight - linearHeight;//位移最高点
                y1 = screenHeight - imageHeight;//位移最低点
                ObjectAnimator.ofPropertyValuesHolder(linearLayout, PropertyValuesHolder.ofFloat("y", 0, y1)).setDuration(0).start();//初始化高度
                deltaY = y0;
            }
        });
        linearLayout = findViewById(R.id.linearLayout);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);
        btnRotate = (Button) findViewById(R.id.btnRotate);
        textView = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                int y = (int) linearLayout.getY();//获取布局在可用布局中的相对高度
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", y, deltaY);
                ObjectAnimator.ofPropertyValuesHolder(linearLayout, pvhY).setDuration(300).start();
                deltaY = deltaY == y0 ? y1 : y0;
                break;
            case R.id.btnRotate:
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat("rotation", 0, 180);
                ObjectAnimator.ofPropertyValuesHolder(textView, pvhR).setDuration(1000).start();
                break;
        }
    }
}
