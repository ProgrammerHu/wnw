package com.hemaapp.wnw.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.luna_framework.dialog.SelectDateDialog;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.DistrictListModel;

import java.util.ArrayList;

import xtom.frame.XtomObject;

/**
 * 会员管理的筛选
 * Created by HuHu on 2016-08-17.
 */
public class MemberShipPopUpWindow extends XtomObject {
    private Context mContext;
    private PopupWindow mWindow;
    private TextView txtBeginDate, txtEndDate, txtTitle;
    private GridView gridView;
    private Button btnReset, btnConfirm;
    private GridAdapter adapter;
    private View layoutDistricts;
    private OnClickConfirmListener onClickConfirmListener;
    private ArrayList<DistrictListModel> districts = new ArrayList<>();

    public MemberShipPopUpWindow(Context mContext, ArrayList<DistrictListModel> districts) {
        this.mContext = mContext;
        this.districts = districts;
        mWindow = new PopupWindow(mContext);
        mWindow.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
//        mWindow.setHeight(MyUtil.getScreenHeight(mContext) / 2);
        mWindow.setBackgroundDrawable(new BitmapDrawable());
        mWindow.setFocusable(true);
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_member_ship, null, false);

        init(rootView);
        mWindow.setContentView(rootView);
    }

    public void showAsDropDown(View anchor) {
        mWindow.showAsDropDown(anchor, 0, MyUtil.dip2px(mContext, 1) / 2);
    }

    public void dismiss() {
        mWindow.dismiss();
    }

    public void hideLayoutDistricts() {
        layoutDistricts.setVisibility(View.GONE);
    }

    private void init(View rootView) {
        rootView.findViewById(R.id.father).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        adapter = new GridAdapter(mContext);
        gridView.setAdapter(adapter);
        txtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
        txtBeginDate = (TextView) rootView.findViewById(R.id.txtBeginDate);
        txtEndDate = (TextView) rootView.findViewById(R.id.txtEndDate);
        btnReset = (Button) rootView.findViewById(R.id.btnReset);
        btnConfirm = (Button) rootView.findViewById(R.id.btnConfirm);
        layoutDistricts = rootView.findViewById(R.id.layoutDistricts);

        txtBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBeginDialog();
            }
        });
        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDialog();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEndDate.setText("");
                txtBeginDate.setText("");
                for (DistrictListModel model : districts) {
                    model.setSelected(false);
                }
                adapter.notifyDataSetChanged();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickConfirmListener == null) {
                    return;
                }
                onClickConfirmListener.OnClickConfirm(
                        txtBeginDate.getText().toString(),
                        txtEndDate.getText().toString(),
                        "呵呵");
            }
        });
    }

    public String getBeginDate() {
        return txtBeginDate.getText().toString();
    }

    public String getEndDate() {
        return txtEndDate.getText().toString();
    }

    public String getDistrict() {
        for (DistrictListModel model : districts) {
            if (model.isSelected()) {
                return model.getName();
            }
        }
        return "";
    }

    public void setTitleContent(String title) {
        txtTitle.setText(title);
    }


    private SelectDateDialog beginDialog, endDialog;

    /**
     * 选择开始日期
     */
    private void showBeginDialog() {
        if (beginDialog == null) {
            beginDialog = new SelectDateDialog(mContext);
            beginDialog.setButtonListener(new SelectDateDialog.OnButtonListener() {
                @Override
                public void onLeftButtonClick(SelectDateDialog dialog) {
                    dialog.cancel();
                }

                @Override
                public void onRightButtonClick(SelectDateDialog dialog) {
                    dialog.cancel();
                    txtBeginDate.setText(dialog.getDate1());
                }
            });
        }
        beginDialog.show();
    }

    /**
     * 选择结束日期
     */
    private void showEndDialog() {
        if (endDialog == null) {
            endDialog = new SelectDateDialog(mContext);
            endDialog.setButtonListener(new SelectDateDialog.OnButtonListener() {
                @Override
                public void onLeftButtonClick(SelectDateDialog dialog) {
                    dialog.cancel();
                }

                @Override
                public void onRightButtonClick(SelectDateDialog dialog) {
                    dialog.cancel();
                    txtEndDate.setText(dialog.getDate1());
                }
            });
        }
        endDialog.show();
    }

    public void setOnClickConfirmListener(OnClickConfirmListener onClickConfirmListener) {
        this.onClickConfirmListener = onClickConfirmListener;
    }

    public interface OnClickConfirmListener {
        void OnClickConfirm(String beginDate, String endDate, String selectDistricts);
    }

    private class GridAdapter extends MyAdapter {

        public GridAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getCount() {
            return districts.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_textview_7dp, null, false);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            int bgResource = districts.get(position).isSelected() ? R.drawable.bg_purple_light_radius_2dp : R.drawable.bg_grey_radius_2dp;
            textView.setBackgroundResource(bgResource);
            textView.setText(districts.get(position).getName());
            int textColor = districts.get(position).isSelected() ? R.color.white : R.color.black_light;
            textView.setTextColor(mContext.getResources().getColor(textColor));

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (districts.get(position).isSelected()) {
                        districts.get(position).setSelected(false);
                    } else {
                        for (DistrictListModel model : districts) {
                            model.setSelected(false);
                            districts.get(position).setSelected(true);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
}
