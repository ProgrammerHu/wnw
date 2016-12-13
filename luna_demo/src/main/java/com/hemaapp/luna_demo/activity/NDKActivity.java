package com.hemaapp.luna_demo.activity;

import android.app.Activity;
import android.os.Bundle;

import com.hemaapp.luna_demo.R;

/**
 * Created by HuHu on 2016-06-27.
 */
public class NDKActivity extends Activity{

    public static native String getStringFromC();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download1);

    }
}
