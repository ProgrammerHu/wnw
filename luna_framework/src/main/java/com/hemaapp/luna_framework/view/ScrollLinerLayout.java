package com.hemaapp.luna_framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.hemaapp.luna_framework.MyUtil;


/**
 * 配合侧滑删除使用
 * 来自张茂宽
 * Created by Hufanglin on 2016/3/16.
 */
public class ScrollLinerLayout extends LinearLayout {


    public ScrollLinerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }

    private Scroller mScroller;

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    public void snapToScreen(int whichScreen) {
        int curscrollerx = getScrollX();
        mScroller.startScroll(curscrollerx, 0, whichScreen - curscrollerx, 0, MyUtil.dip2px(getContext(), 70));
        invalidate();
    }


}