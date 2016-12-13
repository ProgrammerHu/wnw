package com.hemaapp.luna_demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hemaapp.luna_demo.R;

/**
 * Created by HuHu on 2016/4/24.
 */
public class GlideActivity extends AppCompatActivity {
    private ImageView imageView1, imageView2;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        button = (Button) findViewById(R.id.button);
        button.setText(Glide.get(getApplicationContext()).getBitmapPool().toString());
        Glide.with(getApplicationContext()).load("http://192.168.2.28:8081/images/DAimG_2016041633804752R374.jpg")
                .into(imageView1);
        Glide.with(getApplicationContext()).load("http://192.168.2.28:8081/images/DAimG_2016041533804734Y2R0.jpg")
                .into(imageView2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.get(getApplicationContext()).clearMemory();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Glide.get(getApplicationContext()).clearDiskCache();
                    }
                }.start();
            }
        });
    }
}
