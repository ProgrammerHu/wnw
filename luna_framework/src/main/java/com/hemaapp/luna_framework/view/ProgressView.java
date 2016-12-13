package com.hemaapp.luna_framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hemaapp.luna_framework.R;


/**
 * 自己写个小小加载框
 * Created by HuHu on 2016-06-23.
 */
public class ProgressView extends View {
    private float WidthOut;//外圈厚度
    private float WidthInside;//内圈厚度
    private int Speed;//基础速度
    private Paint mPaintBig;//外圈画笔
    private Paint mPaintSmall;//内圈画笔
    private int ColorOut;//外圈颜色
    private int ColorInside;//内圈颜色
    private int MaxAngel;//最大角度

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaintBig = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSmall = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        WidthOut = a.getFloat(R.styleable.ProgressView_width_out, 7.0f);//默认外圈厚度：7
        WidthInside = a.getFloat(R.styleable.ProgressView_width_inside, 5.0f);//默认内圈厚度：5
        Speed = a.getInteger(R.styleable.ProgressView_speed, 4);//默认速度4
        ColorOut = a.getColor(R.styleable.ProgressView_color_out, Color.RED);
        ColorInside = a.getColor(R.styleable.ProgressView_color_inside, Color.RED);
        MaxAngel = a.getInteger(R.styleable.ProgressView_max_angel, 200);//最大角度默认200

        mPaintBig.setColor(ColorOut);
        mPaintSmall.setColor(ColorInside);
        mPaintBig.setStrokeWidth(WidthOut);
        mPaintSmall.setStrokeWidth(WidthInside);
        mPaintBig.setStyle(Paint.Style.STROKE);
        mPaintSmall.setStyle(Paint.Style.STROKE);
    }

    int angel = 0;
    int sweepAngel = 10;
    boolean isAdd = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getMeasuredHeight();//获取控件高度
        int width = getMeasuredWidth();//获取控件宽度
        int length = height < width ? height : width;//去较小的边
        if (length < 30) {
            length = 30;
        }
        RectF rectFOut = new RectF(WidthOut, WidthOut, length - WidthOut, length - WidthOut);//外层线区域
        int widthSpacing = (int) WidthInside + (int) WidthOut + 5;
        RectF rectFInside = new RectF(widthSpacing, widthSpacing, length - widthSpacing, length - widthSpacing);//内层线区域
        if (sweepAngel < 10) {
            isAdd = true;
        }
        if (sweepAngel > MaxAngel) {
            isAdd = false;
        }
        sweepAngel += (isAdd ? Speed : -Speed);
        angel += (isAdd ? Speed : Speed * 2);
        canvas.drawArc(rectFOut, angel, sweepAngel, false, mPaintBig);
        canvas.drawArc(rectFInside, angel + 180, sweepAngel, false, mPaintSmall);
        postInvalidateDelayed(10);
    }

}
