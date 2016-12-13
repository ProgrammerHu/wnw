package com.hemaapp.luna_framework.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;

/**
 * 不支持瀑布流格式
 */
public class XtomRecyclerView extends RecyclerView {

    private SparseArray<SparseArray<XtomImageTask>> tasks = new SparseArray<SparseArray<XtomImageTask>>();
    private XtomImageWorker imageWorker;// 图片下载器
    private OnScrollListener onScrollListener;// 由于设置了自定义滑动监听,如果再设置滑动监听的话只需记录一下,在自定义监听中调用即可

    public XtomRecyclerView(Context context) {
        this(context, null);
    }

    public XtomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XtomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        imageWorker = new XtomImageWorker(context.getApplicationContext());
        setOnScrollListener(new ScrollListener());
    }

    /**
     * add a imageTask
     *
     * @param position
     *            the position in the ListView
     * @param index
     *            the index of the task in the position
     * @param task
     *            the task
     */
    public void addTask(int position, int index, XtomImageTask task) {
        if (!imageWorker.isThreadControlable()) {
            imageWorker.loadImage(task);
        }

        // 需要异步执行的任务，添加进任务队列
        SparseArray<XtomImageTask> tasksInPosition = tasks.get(position);
        if (imageWorker.loadImage(task)) {
            if (tasksInPosition == null) {
                tasksInPosition = new SparseArray<XtomImageTask>();
                tasks.put(position, tasksInPosition);
            }
            tasksInPosition.put(index, task);
        } else {
            if (tasksInPosition != null)
                tasksInPosition.remove(index);
        }
    }

    // 执行任务
    private void excuteTasks(int first, int last) {
        for (int i = first; i <= last; i++) {
            SparseArray<XtomImageTask> tasksInPosition = tasks.get(i);
            if (tasksInPosition != null) {
                int size = tasksInPosition.size();
                for (int index = 0; index < size; index++) {
                    int key = tasksInPosition.keyAt(index);
                    XtomImageTask task = tasksInPosition.get(key);
                    imageWorker.loadImageByThread(task);
                }
            }
        }

        // 清空不可见条目的任务
        int size = tasks.size();
        for (int index = 0; index < size; index++) {
            int key = tasks.keyAt(index);
            if (key < first || key > last) {
                tasks.remove(key);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (xtomSizeChangedListener != null)
            xtomSizeChangedListener.onSizeChanged(this, w, h, oldw, oldh);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        if (l instanceof ScrollListener) {
            onScrollListener = null;
            super.setOnScrollListener(l);
        } else {
            onScrollListener = l;
        }
    }

    // 自定义滑动监听
    private class ScrollListener extends OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView view, int scrollState) {
            if (onScrollListener != null)
                onScrollListener.onScrollStateChanged(view, scrollState);
            switch (scrollState) {
			 /*
		     * SCROLL_STATE_IDLE: 处于空闲状态
		     * SCROLL_STATE_DRAGGING：处于正在拖拽中
		     * SCROLL_STATE_SETTLING：正在自动沉降，相当于松手后，恢复到一个完整的过程
		     */
                case SCROLL_STATE_IDLE:
                    LayoutManager layoutManager = view.getLayoutManager();
                    int first=0;
                    int last=0;
                    if (layoutManager instanceof LinearLayoutManager) {
                        first = ((LinearLayoutManager) layoutManager)
                                .findFirstVisibleItemPosition();
                        last = ((LinearLayoutManager) layoutManager)
                                .findLastVisibleItemPosition();
                    }
                    else if (layoutManager instanceof GridLayoutManager) {
                        first = ((GridLayoutManager) layoutManager)
                                .findFirstVisibleItemPosition();
                        last = ((GridLayoutManager) layoutManager)
                                .findLastVisibleItemPosition();
                    }
                    excuteTasks(first, last);
                    imageWorker.setThreadControlable(false);
                    if (xtomScrollListener != null)
                        xtomScrollListener.onStop(XtomRecyclerView.this);
                    break;
                case SCROLL_STATE_DRAGGING:
                case SCROLL_STATE_SETTLING:
                    imageWorker.clearTasks();
                    imageWorker.setThreadControlable(true);
                    if (xtomScrollListener != null)
                        xtomScrollListener.onStart(XtomRecyclerView.this);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (onScrollListener != null)
                onScrollListener.onScrolled(recyclerView, dx, dy);
        }

    }

    private XtomScrollListener xtomScrollListener;

    /**
     * @return 滑动监听
     */
    public XtomScrollListener getXtomScrollListener() {
        return xtomScrollListener;
    }

    /**
     * 设置 滑动监听
     *
     * @param xtomScrollListener
     */
    public void setXtomScrollListener(XtomScrollListener xtomScrollListener) {
        this.xtomScrollListener = xtomScrollListener;
    }

    /**
     * 滑动监听
     */
    public interface XtomScrollListener {
        /**
         * 开始滑动
         *
         * @param view
         */
        public void onStart(XtomRecyclerView view);

        /**
         * 停止滑动
         *
         * @param view
         */
        public void onStop(XtomRecyclerView view);
    }

    private XtomSizeChangedListener xtomSizeChangedListener;

    /**
     * @return 大小改变监听
     */
    public XtomSizeChangedListener getXtomSizeChangedListener() {
        return xtomSizeChangedListener;
    }

    /**
     * 设置大小改变监听
     *
     * @param xtomSizeChangedListener
     */
    public void setXtomSizeChangedListener(
            XtomSizeChangedListener xtomSizeChangedListener) {
        this.xtomSizeChangedListener = xtomSizeChangedListener;
    }

    /**
     * 大小改变监听
     */
    public interface XtomSizeChangedListener {
        public void onSizeChanged(XtomRecyclerView view, int w, int h,
                                  int oldw, int oldh);
    }
}
