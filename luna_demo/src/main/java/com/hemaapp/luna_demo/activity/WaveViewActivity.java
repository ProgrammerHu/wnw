package com.hemaapp.luna_demo.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.animation.AccelerateInterpolator;

import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_demo.view.WaveView;

/**
 * Created by HuHu on 2016-06-21.
 */
public class WaveViewActivity extends Activity {
    private WaveView mWaveView1, mWaveView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_view);
        mWaveView1 = (WaveView) findViewById(R.id.waveView1);
        mWaveView1.setDuration(5000);
        mWaveView1.setStyle(Paint.Style.STROKE);
        mWaveView1.setSpeed(400);
        mWaveView1.setColor(Color.parseColor("#ff0000"));
        mWaveView1.setInterpolator(new AccelerateInterpolator(1.2f));
        mWaveView1.start();

        mWaveView2 = (WaveView) findViewById(R.id.waveView2);
        mWaveView2.setDuration(5000);
        mWaveView2.setStyle(Paint.Style.FILL);
        mWaveView2.setColor(Color.parseColor("#ff0000"));
        mWaveView2.setInterpolator(new LinearOutSlowInInterpolator());
        mWaveView2.start();
    }
}
