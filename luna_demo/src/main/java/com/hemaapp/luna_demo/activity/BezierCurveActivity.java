package com.hemaapp.luna_demo.activity;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hemaapp.luna_demo.R;

/**
 * Created by HuHu on 2016-06-29.
 */
public class BezierCurveActivity extends AppCompatActivity {
    private ImageView imageView, imageView2;
    private Button button;
    private LinearLayout father;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bazier_curve);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        father = (LinearLayout) findViewById(R.id.father);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hehe();
            }
        });
    }

    public class PathAnimation extends Animation {
        private PathMeasure measure;
        private float[] pos = new float[2];

        public PathAnimation(Path path) {
            measure = new PathMeasure(path, false);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            measure.getPosTan(measure.getLength() * interpolatedTime, pos, null);
            t.getMatrix().setTranslate(pos[0], pos[1]);

        }
    }

    private void hehe() {
        //得到起始点坐标
        int parentLoc[] = new int[2];
        father.getLocationInWindow(parentLoc);
        int startLoc[] = new int[2];
        imageView.getLocationInWindow(startLoc);
        int endLoc[] = new int[2];
        imageView2.getLocationInWindow(endLoc);
        float startX = startLoc[0] - parentLoc[0];
        float startY = startLoc[1] - parentLoc[1];
        float toX = endLoc[0] - parentLoc[0];
        float toY = endLoc[1] - parentLoc[1];

        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo((startX + toX) / 2, startY, toX, toY);

        PathAnimation animation = new PathAnimation(path);
        animation.setDuration(5000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                i++;
//                txt.setText(String.valueOf(i));
                father.removeView(imageView);
//                relativeLayout.removeView(goods);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animation);
    }

}
