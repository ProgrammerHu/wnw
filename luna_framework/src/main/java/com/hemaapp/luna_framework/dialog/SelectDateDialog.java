package com.hemaapp.luna_framework.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hemaapp.luna_framework.R;
import com.hemaapp.luna_framework.view.wheelview.OnWheelChangedListener;
import com.hemaapp.luna_framework.view.wheelview.WheelView;
import com.hemaapp.luna_framework.view.wheelview.adapters.ArrayWheelAdapter;
import com.hemaapp.luna_framework.view.wheelview.adapters.NumericWheelAdapter;

import java.util.Calendar;

import xtom.frame.XtomObject;

/**
 * 选择日期的dialog
 * Created by HuHu on 2016/5/9.
 */
public class SelectDateDialog extends XtomObject implements OnWheelChangedListener {
    private final int MAX_YEAR = 2099;
    private final int MIN_YEAR = 1900;

    private Activity mContext;
    private Dialog mDialog;
    private TextView txtCancel, txtCheck;
    private WheelView wheelYear, wheelMonth, wheelDay;
    private NumericWheelAdapter yearsAdpter, monthsAdapter, daysAdapter;
    private OnButtonListener onButtonListener;
    private int ColumnCount = 3;

    public SelectDateDialog(Context mContext) {
        this.mContext = (Activity) mContext;
        mDialog = new Dialog(mContext, R.style.custom_dialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_date, null);
        wheelYear = (WheelView) view.findViewById(R.id.wheelYear);
        wheelMonth = (WheelView) view.findViewById(R.id.wheelMonth);
        wheelDay = (WheelView) view.findViewById(R.id.wheelDay);
        txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        txtCheck = (TextView) view.findViewById(R.id.txtCheck);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonListener == null) {
                    return;
                }
                onButtonListener.onLeftButtonClick(SelectDateDialog.this);
            }
        });
        txtCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonListener == null) {
                    return;
                }
                onButtonListener.onRightButtonClick(SelectDateDialog.this);
            }
        });
        /*设置为显示7个滚动轴*/
        wheelYear.setVisibleItems(7);
        wheelMonth.setVisibleItems(7);
        wheelDay.setVisibleItems(7);
        /*
        初始化年月日适配器
         */
        yearsAdpter = new NumericWheelAdapter(mContext, MIN_YEAR, MAX_YEAR);
        yearsAdpter.setLabel("年");
        monthsAdapter = new NumericWheelAdapter(mContext, 1, 12, "%02d");
        monthsAdapter.setLabel("月");
        daysAdapter = new NumericWheelAdapter(mContext, 1, 31, "%02d");
        daysAdapter.setLabel("日");

        wheelYear.setViewAdapter(yearsAdpter);
        wheelMonth.setViewAdapter(monthsAdapter);
        wheelDay.setViewAdapter(daysAdapter);
        refreshDays();
        mDialog.setCancelable(true);
        mDialog.setContentView(view);


        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.x = 0;// 设置x坐标
        params.y = this.mContext.getWindowManager().getDefaultDisplay().getHeight()
                - params.height;// 设置y坐标
        params.width = this.mContext.getWindowManager().getDefaultDisplay()
                .getWidth();
        dialogWindow.setAttributes(params);
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) > MAX_YEAR) {
            selectYearPosition = MAX_YEAR - MIN_YEAR;
        } else if (calendar.get(Calendar.YEAR) < MIN_YEAR) {
            selectYearPosition = 0;
        } else {
            selectYearPosition = calendar.get(Calendar.YEAR) - MIN_YEAR;
        }
//        log_e(calendar.get(Calendar.YEAR) + "");
//        log_e(calendar.get(Calendar.MONTH) + "");
//        log_e(calendar.get(Calendar.DAY_OF_MONTH) + "");

        selectMonthPosition = calendar.get(Calendar.MONTH);
        selectDaysPosition = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        wheelYear.setCurrentItem(selectYearPosition);
        wheelMonth.setCurrentItem(selectMonthPosition);
        wheelDay.setCurrentItem(selectDaysPosition);

        wheelYear.addChangingListener(this);
        wheelMonth.addChangingListener(this);
        wheelDay.addChangingListener(this);
    }

    /**
     * @param mContext
     * @param ColumnCount 1，年；2，年月；3年月日
     */
    public SelectDateDialog(Context mContext, int ColumnCount) {
        this(mContext);
        this.ColumnCount = ColumnCount;
        switch (ColumnCount) {
            case 1:
                wheelMonth.setVisibility(View.GONE);
                wheelDay.setVisibility(View.GONE);
                break;
            case 2:
                wheelDay.setVisibility(View.GONE);
                break;
            default:

                break;
        }
    }


    private int selectYearPosition, selectMonthPosition, selectDaysPosition;

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wheelYear) {
            selectYearPosition = newValue;
            Log.e("selectYearPosition", selectYearPosition + "");
            refreshDays();
        } else if (wheel == wheelMonth) {
            selectMonthPosition = newValue;
            Log.e("selectMonthPosition", selectMonthPosition + "");
            refreshDays();
        } else {
            selectDaysPosition = newValue;
            Log.e("selectDaysPosition", selectDaysPosition + "");
        }
    }

    /**
     * 刷新天的数据
     */
    private void refreshDays() {
        int year = MIN_YEAR + selectYearPosition;
        int month = selectMonthPosition + 1;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysAdapter = new NumericWheelAdapter(mContext, 1, 31, "%02d");
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysAdapter = new NumericWheelAdapter(mContext, 1, 30, "%02d");
                break;
            default:
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {//是闰年
                    daysAdapter = new NumericWheelAdapter(mContext, 1, 29, "%02d");
                } else {
                    daysAdapter = new NumericWheelAdapter(mContext, 1, 28, "%02d");
                }
                break;
        }
        daysAdapter.setLabel("日");
        wheelDay.setViewAdapter(daysAdapter);
        if (selectDaysPosition >= daysAdapter.getItemsCount()) {
            wheelDay.setCurrentItem(daysAdapter.getItemsCount() - 1);
            selectDaysPosition = daysAdapter.getItemsCount() - 1;
        }

    }


    public void show() {
        mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }

    /**
     * 获取日期20160502的格式
     *
     * @return
     */
    public String getDate() {
        switch (ColumnCount) {
            case 1:
                return String.valueOf(yearsAdpter.getItemText(selectYearPosition)) + "年";
            case 2:
                return String.valueOf(yearsAdpter.getItemText(selectYearPosition)) + "年"
                        + String.valueOf(monthsAdapter.getItemText(selectMonthPosition)) + "月";
            default:
                return String.valueOf(yearsAdpter.getItemText(selectYearPosition)) + "年"
                        + String.valueOf(monthsAdapter.getItemText(selectMonthPosition)) + "月"
                        + String.valueOf(daysAdapter.getItemText(selectDaysPosition)) + "日";
        }
    }

    /**
     * 获取日期2016-01-01
     *
     * @return
     */
    public String getDate1() {
        switch (ColumnCount) {
            case 1:
                return String.valueOf(yearsAdpter.getItemText(selectYearPosition));
            case 2:
                return String.valueOf(yearsAdpter.getItemText(selectYearPosition)) + "-"
                        + String.valueOf(monthsAdapter.getItemText(selectMonthPosition));
            default:
                return String.valueOf(yearsAdpter.getItemText(selectYearPosition)) + "-"
                        + String.valueOf(monthsAdapter.getItemText(selectMonthPosition)) + "-"
                        + String.valueOf(daysAdapter.getItemText(selectDaysPosition));
        }
    }

    /**
     * 获取年份
     *
     * @return
     */
    public String getYear() {
        return String.valueOf(yearsAdpter.getItemText(selectYearPosition));
    }

    public int getYearInteger() {
        return Integer.parseInt(String.valueOf(yearsAdpter.getItemText(selectYearPosition)));
    }

    public int getMonthInteger() {
        return Integer.parseInt(String.valueOf(monthsAdapter.getItemText(selectMonthPosition)));
    }

    public int getDayInteger() {
        return Integer.parseInt(String.valueOf(daysAdapter.getItemText(selectDaysPosition)));
    }

    public OnButtonListener getButtonListener() {
        return onButtonListener;
    }

    public void setButtonListener(OnButtonListener buttonListener) {
        this.onButtonListener = buttonListener;
    }

    public interface OnButtonListener {
        public void onLeftButtonClick(SelectDateDialog dialog);

        public void onRightButtonClick(SelectDateDialog dialog);
    }

}
