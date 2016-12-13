package com.hemaapp.luna_demo.calendar;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaFragment;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_demo.MyFragment;
import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_demo.activity.MyActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by HuHu on 2016-05-17.
 */
public class CalendarFragment extends MyFragment {
    private TextView dateText;
    private GridView gridView;
    private CalendarAdapter adapter;
    private ArrayList<WorkDay> allLists = new ArrayList<WorkDay>();
    private ArrayList<WorkDay> monthLists = new ArrayList<WorkDay>();
    private String today;
    private MyActivity activity;
    private int STATE_YEAR, STATE_MONTH, STATE_DAY;

    public CalendarFragment() {
        super();
    }

    public static CalendarFragment getInstance(int AddMonth, TextView dateText) {
        CalendarFragment fragment = new CalendarFragment();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, AddMonth);
        fragment.STATE_YEAR = calendar.get(Calendar.YEAR);
        fragment.STATE_MONTH = calendar.get(Calendar.MONTH);
        fragment.STATE_DAY = calendar.get(Calendar.DAY_OF_MONTH);
        fragment.today = fragment.STATE_MONTH + "/" + fragment.STATE_DAY + "/" + fragment.STATE_YEAR;
        fragment.dateText = dateText;

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_calendar);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        activity = (MyActivity) getActivity();
        gridView = (GridView) findViewById(R.id.calendar_gridview);
        monthLists = getDate(STATE_YEAR, STATE_MONTH);
        setLastNextMonth(STATE_YEAR, STATE_MONTH);

        adapter = new CalendarAdapter(activity, allLists, itemOnlickListener);
        gridView.setAdapter(adapter);

    }

    @Override
    protected void setListener() {

    }

    private View.OnClickListener itemOnlickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.calendar_day_butt:
                    v.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.calenar_day_animal));
                    int tag = Integer.parseInt(v.getTag().toString());
                    if (dateText != null) {
                        dateText.setText((Integer.valueOf(allLists.get(tag).getMonth()) + 1) + "/"
                                + allLists.get(tag).getDay() + "/"
                                + allLists.get(tag).getYera());
                    }

                    for (int i = 0; i < allLists.size(); i++) {
                        if (i == tag) {
                            allLists.get(i).setChecked(true);
                        } else {
                            allLists.get(i).setChecked(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };

    public ArrayList<WorkDay> getDate(int year, int month) {
        ArrayList<WorkDay> lists = new ArrayList<WorkDay>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);// 注意,Calendar对象默认一月为0
        int monthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
        for (int i = 0; i < monthDays; i++) {
            WorkDay day = new WorkDay();
            calendar.clear();
            calendar.set(year, month, i + 1);
            int c_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            day.setYera(year + "");
            day.setMonth(month + "");
            day.setDay((i + 1) + "");
            day.setWeek(c_week + "");
            day.setWork(true);
            day.setEnable(true);
            if (today.equals(day.getString())) {
                day.setChecked(true);
            } else {
                day.setChecked(false);
            }
            lists.add(day);
        }
        return lists;
    }

    public void setLastNextMonth(int year, int month) {
        setLastMonth(year, month);
        setNextMonth(year, month);
    }

    // 设置上个月数据
    public void setLastMonth(int year, int month) {
        if (allLists.size() != 0) {
            allLists.clear();
            allLists.addAll(monthLists);
        } else {
            allLists.addAll(monthLists);
        }
        int startPostion = Integer.parseInt(monthLists.get(0).getWeek());
        Calendar calendar = Calendar.getInstance();
        if (month == 0) {
            year--;
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, 11);// 注意,Calendar对象默认一月为0
        } else {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);// 注意
        }
        int dayNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
        for (int i = 0; i < startPostion; i++) {
            WorkDay day = new WorkDay();
            day.setYera(year + "");
            if (month == 0) {
                day.setMonth(11 + "");
            } else {
                day.setMonth((month - 1) + "");
            }
            day.setDay((dayNum--) + "");
            day.setWeek(i + "");
            day.setWork(true);
            day.setEnable(false);
            day.setChecked(false);
            allLists.add(0, day);
        }
    }

    //设置下个月数据
    public void setNextMonth(int year, int month) {
        int endPostion = Integer.parseInt(monthLists.get(monthLists.size() - 1)
                .getWeek());
        int nums = 7 - endPostion;
        Calendar calendar = Calendar.getInstance();
        if (month == 11) {
            year++;
            calendar.set(year, 0, 1);// 注意,Calendar对象默认一月为0
        } else {
            calendar.set(year, (month + 1), 1);// 注意,Calendar对象默认一月为0
        }
        int n_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        for (int i = 1; i < nums; i++) {
            WorkDay day = new WorkDay();
            day.setYera(year + "");
            if (month == 11) {
                day.setMonth(0 + "");
            } else {
                day.setMonth((month + 1) + "");
            }
            day.setDay(i + "");
            day.setWeek((n_week++) + "");
            day.setWork(true);
            day.setEnable(false);
            day.setChecked(false);
            allLists.add(day);
        }
    }
}
