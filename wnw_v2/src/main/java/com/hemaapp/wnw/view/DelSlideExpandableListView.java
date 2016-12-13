package com.hemaapp.wnw.view;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;
import xtom.frame.util.XtomSharedPreferencesUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;

/**
 * 侧滑删除的列表
 * 来自张茂宽
 * Created by Hufanglin on 2016/3/16.
 */
public class DelSlideExpandableListView extends ExpandableListView implements
        GestureDetector.OnGestureListener, View.OnTouchListener {
//    private SparseArray<SparseArray<XtomImageTask>> tasks = new SparseArray<SparseArray<XtomImageTask>>();
//    private XtomImageWorker imageWorker;// 图片下载器
//    private OnScrollListener onScrollListener;// 由于设置了自定义滑动监听,如果再设置滑动监听的话只需记录一下,在自定义监听中调用即可


    private GestureDetector mDetector;
    private String TAG = "DelSlideListView";

    private int px = 0;
    private Context context = null;

    public DelSlideExpandableListView(Context context) {
        super(context);
        init(context);
        this.context = context;
    }

    public DelSlideExpandableListView(Context context, AttributeSet att) {
        super(context, att);
        init(context);
        this.context = context;
    }

    private int standard_touch_target_size = 0;
    private float mLastMotionX;
    // 有item被拉出

    public boolean deleteView = false;
    // 当前拉出的view

    private ScrollLinerLayout mScrollLinerLayout = null;
    // 滑动着

    private boolean scroll = false;
    // 禁止拖动

    private boolean forbidScroll = false;
    // 禁止拖动

    private boolean clicksameone = false;
    // 当前拉出的位置

    private int position;
    // 消息冻结

    private boolean freeze = false;

    private void init(Context mContext) {
        mDetector = new GestureDetector(mContext, this);
        // mDetector.setIsLongpressEnabled(false);

        standard_touch_target_size = (int) getResources().getDimension(
                R.dimen.delete_action_len);
        this.setOnTouchListener(this);
    }

    public void reset() {
        reset(false);
    }

    public void reset(boolean noaction) {
        position = -1;
        deleteView = false;
        if (mScrollLinerLayout != null) {
            if (!noaction) {
                mScrollLinerLayout.snapToScreen(0);
            } else {
                mScrollLinerLayout.scrollTo(0, 0);
            }
            mScrollLinerLayout = null;
        }
        scroll = false;
    }

    public boolean onDown(MotionEvent e) {
        Log.i(TAG, "onDown");

        mLastMotionX = e.getX();
        int p = this.pointToPosition((int) e.getX(), (int) e.getY())
                - this.getFirstVisiblePosition();
        if (deleteView) {
            if (p != position) {
                // 吃掉，不在有消息

                freeze = true;
                return true;
            } else {
                clicksameone = true;
            }
        }
        position = p;
        scroll = false;
        return false;
    }

    public void onLongPress(MotionEvent e) {
        Log.i(TAG, "onLongPress");
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // Log.i(TAG, "onScroll" + e1.getX() + ":" + distanceX);

        // 第二次

        if (scroll) {
            int deltaX = (int) (mLastMotionX - e2.getX());
            if (deleteView) {
                deltaX += standard_touch_target_size;
            }
            if (deltaX >= 0 && deltaX <= standard_touch_target_size) {
                mScrollLinerLayout.scrollBy(
                        deltaX - mScrollLinerLayout.getScrollX(), 0);
            }
            return true;
        }
        if (!forbidScroll) {
            forbidScroll = true;
            // x方向滑动，才开始拉动

            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                View v = this.getChildAt(position);
                boolean ischild = v instanceof ScrollLinerLayout;
                if (ischild) {
                    mScrollLinerLayout = (ScrollLinerLayout) v;
                    scroll = true;
                    int deltaX = (int) (mLastMotionX - e2.getX());
                    if (deleteView) {
                        // 再次点击的时候，要把deltax增加

                        deltaX += standard_touch_target_size;
                    }
                    if (deltaX >= 0 && deltaX <= standard_touch_target_size) {
                        mScrollLinerLayout.scrollBy(
                                (int) (e1.getX() - e2.getX()), 0);
                    }
                }
            }
        }
        return false;
    }

    public void onShowPress(MotionEvent e) {
        Log.i(TAG, "onShowPress");
    }

    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp");
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scroll || deleteView) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
            boolean isfreeze = freeze;
            boolean isclicksameone = clicksameone;
            forbidScroll = false;
            clicksameone = false;
            freeze = false;
            if (isfreeze) {
                // 上一个跟当前点击不一致 还原

                reset();
                return true;
            }
            int deltaX2 = (int) (mLastMotionX - event.getX());
            // 不存在

            Log.i(TAG, "scroll:" + scroll + "deltaX2:" + deltaX2);

            if (scroll && deltaX2 >= standard_touch_target_size / 2) {
                // mScrollLinerLayout.snapToScreen(standard_touch_target_size*5/3);

                px = MyUtil.dip2px(getContext(), 60);
                if (XtomSharedPreferencesUtil.get(context, "w") != null) {
                    px = Integer.parseInt(XtomSharedPreferencesUtil.get(
                            context, "w")) / 6;
                }
                mScrollLinerLayout.snapToScreen(px); // 120

                deleteView = true;
                scroll = false;
                return true;
            }
            if (deleteView && scroll
                    && deltaX2 >= -standard_touch_target_size / 2) {
                // mScrollLinerLayout.snapToScreen(standard_touch_target_size*3);
                px = MyUtil.dip2px(getContext(), 60);
                if (XtomSharedPreferencesUtil.get(context, "w") != null) {
                    px = Integer.parseInt(XtomSharedPreferencesUtil.get(
                            context, "w")) / 6;
                }
                mScrollLinerLayout.snapToScreen(px);
                deleteView = true;
                scroll = false;
                return true;
            }
            if (isclicksameone || scroll) {
                reset();
                return true;
            }
            reset();
        }
        if (freeze) {
            return true;
        }
        Log.i(TAG, "onTouchEvent");
        return mDetector.onTouchEvent(event);

    }

    public void deleteItem() {
        Log.i(TAG, "deleteItem");
        reset(true);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.
     * MotionEvent, android.view.MotionEvent, float, float)
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

//
//    /**
//     * add a imageTask
//     *
//     * @param position the position in the ListView
//     * @param index    the index of the task in the position
//     * @param task     the task
//     */
//    public void addTask(int position, int index, XtomImageTask task) {
//        if (!imageWorker.isThreadControlable()) {
//            imageWorker.loadImage(task);
//        }
//
//        // 需要异步执行的任务，添加进任务队列
//        SparseArray<XtomImageTask> tasksInPosition = tasks.get(position);
//        if (imageWorker.loadImage(task)) {
//            if (tasksInPosition == null) {
//                tasksInPosition = new SparseArray<XtomImageTask>();
//                tasks.put(position, tasksInPosition);
//            }
//            tasksInPosition.put(index, task);
//        } else {
//            if (tasksInPosition != null)
//                tasksInPosition.remove(index);
//        }
//    }
//
//
//    // 执行任务
//    private void excuteTasks(int first, int last) {
//        for (int i = first; i <= last; i++) {
//            SparseArray<XtomImageTask> tasksInPosition = tasks.get(i);
//            if (tasksInPosition != null) {
//                int size = tasksInPosition.size();
//                for (int index = 0; index < size; index++) {
//                    int key = tasksInPosition.keyAt(index);
//                    XtomImageTask task = tasksInPosition.get(key);
//                    imageWorker.loadImageByThread(task);
//                }
//            }
//        }
//
//        // 清空不可见条目的任务
//        int size = tasks.size();
//        for (int index = 0; index < size; index++) {
//            int key = tasks.keyAt(index);
//            if (key < first || key > last) {
//                tasks.remove(key);
//            }
//        }
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        if (xtomSizeChangedListener != null)
//            xtomSizeChangedListener.onSizeChanged(this, w, h, oldw, oldh);
//    }
//
//    @Override
//    public void setOnScrollListener(OnScrollListener l) {
//        if (l instanceof ScrollListener) {
//            onScrollListener = null;
//            super.setOnScrollListener(l);
//        } else {
//            onScrollListener = l;
//        }
//    }
//
//    // 自定义滑动监听
//    private class ScrollListener implements OnScrollListener {
//
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            if (onScrollListener != null)
//                onScrollListener.onScrollStateChanged(view, scrollState);
//            switch (scrollState) {
//                case SCROLL_STATE_IDLE:
//                    int first = getFirstVisiblePosition();
//                    int last = getLastVisiblePosition();
//                    excuteTasks(first, last);
//                    imageWorker.setThreadControlable(false);
//                    if (xtomScrollListener != null)
//                        xtomScrollListener.onStop(DelSlideExpandableListView.this);
//                    break;
//                case SCROLL_STATE_TOUCH_SCROLL:
//                    imageWorker.clearTasks();
//                    imageWorker.setThreadControlable(true);
//                    if (xtomScrollListener != null)
//                        xtomScrollListener.onStart(DelSlideExpandableListView.this);
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem,
//                             int visibleItemCount, int totalItemCount) {
//            if (onScrollListener != null)
//                onScrollListener.onScroll(view, firstVisibleItem,
//                        visibleItemCount, totalItemCount);
//        }
//
//    }
//
//    private XtomScrollListener xtomScrollListener;
//
//    /**
//     * @return 滑动监听
//     */
//    public XtomScrollListener getXtomScrollListener() {
//        return xtomScrollListener;
//    }
//
//    /**
//     * 设置 滑动监听
//     *
//     * @param xtomScrollListener
//     */
//    public void setXtomScrollListener(XtomScrollListener xtomScrollListener) {
//        this.xtomScrollListener = xtomScrollListener;
//    }
//
//    /**
//     * 滑动监听
//     */
//    public interface XtomScrollListener {
//        /**
//         * 开始滑动
//         *
//         * @param view
//         */
//        public void onStart(DelSlideExpandableListView view);
//
//        /**
//         * 停止滑动
//         *
//         * @param view
//         */
//        public void onStop(DelSlideExpandableListView view);
//    }
//
//    private XtomSizeChangedListener xtomSizeChangedListener;
//
//    /**
//     * @return 大小改变监听
//     */
//    public XtomSizeChangedListener getXtomSizeChangedListener() {
//        return xtomSizeChangedListener;
//    }
//
//    /**
//     * 设置大小改变监听
//     *
//     * @param xtomSizeChangedListener
//     */
//    public void setXtomSizeChangedListener(
//            XtomSizeChangedListener xtomSizeChangedListener) {
//        this.xtomSizeChangedListener = xtomSizeChangedListener;
//    }
//
//    /**
//     * 大小改变监听
//     */
//    public interface XtomSizeChangedListener {
//        public void onSizeChanged(DelSlideExpandableListView view, int w, int h, int oldw,
//                                  int oldh);
//    }

}

