package com.hemaapp.wnw.view;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.GridView;

/**
 * 全部显示图片的GridView
 * Created by Hufanglin on 2016/3/18.
 */
public class MeasureGridView extends GridView {
    private SparseArray<SparseArray<XtomImageTask>> tasks = new SparseArray<SparseArray<XtomImageTask>>();
    private XtomImageWorker imageWorker;// 图片下载器

    @SuppressLint("NewApi")
    public MeasureGridView(Context context, AttributeSet attrs, int defStyleAttr,
                           int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public MeasureGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MeasureGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeasureGridView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        imageWorker = new XtomImageWorker(context.getApplicationContext());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * add a imageTask
     *
     * @param position the position in the ListView
     * @param index    the index of the task in the position
     * @param task     the task
     */
    public void addTask(int position, int index, XtomImageTask task) {
        if (!imageWorker.isThreadControlable()) {
            imageWorker.loadImage(task);
            return;
        }
        // 需要异步执行的任务，添加进任务队列
        if (imageWorker.loadImage(task)) {
            SparseArray<XtomImageTask> tasksInPosition = tasks.get(position);
            if (tasksInPosition == null) {
                tasksInPosition = new SparseArray<XtomImageTask>();
                tasks.put(position, tasksInPosition);
            }
            tasksInPosition.put(index, task);
        }
    }
}