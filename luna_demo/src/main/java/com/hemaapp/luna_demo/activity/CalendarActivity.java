package com.hemaapp.luna_demo.activity;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaHttpInfomation;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_demo.calendar.CalendarFragmentAdapter;
import com.hemaapp.luna_demo.calendar.WorkDay;

/**
 * Created by HuHu on 2016-05-17.
 */
public class CalendarActivity extends MyActivity {

    private TextView dateText;
    private Button lastButt, nextButt;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_manager);

        dateText = (TextView) findViewById(R.id.date_text);
        lastButt = (Button) findViewById(R.id.last_month);
        nextButt = (Button) findViewById(R.id.next_month);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CalendarFragmentAdapter(getSupportFragmentManager(), dateText));
//        dateText.setText((STATE_MONTH + 1) + "/" + STATE_DAY + "/" + STATE_YEAR);


//        lastButt.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                monthLists.clear();
//                if (STATE_MONTH == 0) {
//                    STATE_MONTH = 11;
//                    STATE_YEAR--;
//                } else {
//                    STATE_MONTH--;
//                }
//                monthLists = getDate(STATE_YEAR, STATE_MONTH);
//                setLastNextMonth(STATE_YEAR, STATE_MONTH);
//                adapter.notifyDataSetChanged();
//            }
//        });
//        nextButt.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                monthLists.clear();
//                if (STATE_MONTH == 11) {
//                    STATE_MONTH = 0;
//                    STATE_YEAR++;
//                } else {
//                    STATE_MONTH++;
//                }
//                monthLists = getDate(STATE_YEAR, STATE_MONTH);
//                setLastNextMonth(STATE_YEAR, STATE_MONTH);
//                adapter.notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public void onError(HemaHttpInfomation hemaHttpInfomation) {

    }

    @Override
    public void onSuccess(HemaBaseResult hemaBaseResult, HemaHttpInfomation hemaHttpInfomation) {

    }


    public void setLastNextMonth(int year, int month) {
//        setLastMonth(year, month);
//        setNextMonth(year, month);
    }

    // 设置上个月数据
//    public void setLastMonth(int year, int month) {
//        if (allLists.size() != 0) {
//            allLists.clear();
//            allLists.addAll(monthLists);
//        } else {
//            allLists.addAll(monthLists);
//        }
//        int startPostion = Integer.parseInt(monthLists.get(0).getWeek());
//        Calendar calendar = Calendar.getInstance();
//        if (month == 0) {
//            year--;
//            calendar.set(Calendar.YEAR, year);
//            calendar.set(Calendar.MONTH, 11);// 注意,Calendar对象默认一月为0
//        } else {
//            calendar.set(Calendar.YEAR, year);
//            calendar.set(Calendar.MONTH, month - 1);// 注意
//        }
//        int dayNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
//        for (int i = 0; i < startPostion; i++) {
//            WorkDay day = new WorkDay();
//            day.setYera(year + "");
//            if (month == 0) {
//                day.setMonth(11 + "");
//            } else {
//                day.setMonth((month - 1) + "");
//            }
//            day.setDay((dayNum--) + "");
//            day.setWeek(i + "");
//            day.setWork(true);
//            day.setEnable(false);
//            day.setChecked(false);
//            allLists.add(0, day);
//        }
//    }

    // 设置下个月数据
//    public void setNextMonth(int year, int month) {
//        int endPostion = Integer.parseInt(monthLists.get(monthLists.size() - 1)
//                .getWeek());
//        int nums = 7 - endPostion;
//        Calendar calendar = Calendar.getInstance();
//        if (month == 11) {
//            year++;
//            calendar.set(year, 0, 1);// 注意,Calendar对象默认一月为0
//        } else {
//            calendar.set(year, (month + 1), 1);// 注意,Calendar对象默认一月为0
//        }
//        int n_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
//        for (int i = 1; i < nums; i++) {
//            WorkDay day = new WorkDay();
//            day.setYera(year + "");
//            if (month == 11) {
//                day.setMonth(0 + "");
//            } else {
//                day.setMonth((month + 1) + "");
//            }
//            day.setDay(i + "");
//            day.setWeek((n_week++) + "");
//            day.setWork(true);
//            day.setEnable(false);
//            day.setChecked(false);
//            allLists.add(day);
//        }
//    }


}

