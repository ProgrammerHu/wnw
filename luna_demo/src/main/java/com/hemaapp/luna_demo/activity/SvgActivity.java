package com.hemaapp.luna_demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_demo.view.SVGPathView;

/**
 * Created by HuHu on 2016-07-01.
 */
public class SvgActivity extends Activity {
    private SVGPathView svgPathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);
        svgPathView = (SVGPathView) findViewById(R.id.svgPathView);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        svgPathView.startTransform();
        return super.onTouchEvent(event);
    }
}
