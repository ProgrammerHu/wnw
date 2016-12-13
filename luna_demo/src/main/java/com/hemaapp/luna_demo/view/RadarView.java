package com.hemaapp.luna_demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * 雷达
 * Created by HuHu on 2016-06-24.
 */
public class RadarView extends View {
    private float circleWidth = 10.0f;
    private Paint paint;//外层圈的画笔

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

//        paintSector.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.reset();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(circleWidth);
        paint.setStyle(Paint.Style.STROKE);
        int height = getMeasuredHeight();//获取控件高度
        int width = getMeasuredWidth();//获取控件宽度
        int length = height < width ? height : width;//去较小的边
        if (length < 30) {
            length = 30;
        }
        RectF rectFCircle = new RectF(circleWidth, circleWidth, length - circleWidth, length - circleWidth);
        canvas.drawArc(rectFCircle, 0, 360, false, paint);

        paint.reset();
        paint.setStrokeWidth(circleWidth);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        Shader shader = new SweepGradient(length / 2, length / 2,
//                new int[]{Color.BLUE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE}, null);
        Shader shader = new SweepGradient(length / 2, length / 2,
                new int[]{Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLUE}, null);
        paint.setShader(shader);
//        RectF rectFSector = new RectF(2 * circleWidth, 2 * circleWidth, length - 2 * circleWidth, length - 2 * circleWidth);
        canvas.drawCircle(length / 2, length / 2, length / 2 - circleWidth * 2, paint);

    }
}
