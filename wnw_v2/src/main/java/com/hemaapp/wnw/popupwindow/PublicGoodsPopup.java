package com.hemaapp.wnw.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.CountListModel;

import java.util.ArrayList;

import xtom.frame.XtomObject;

/**
 * 选择发布商品的对话框
 * Created by HuHu on 2016-08-16.
 */
public class PublicGoodsPopup extends XtomObject implements View.OnClickListener {
    private Context mContext;
    private PopupWindow mWindow;
    private TextView txtPublicGoods, txtPublicFlashSale, txtPublicPreSell;

    public PublicGoodsPopup(Context mContext) {
        this.mContext = mContext;
        mWindow = new PopupWindow(mContext);
        mWindow.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
//        mWindow.setHeight(MyUtil.getScreenHeight(mContext) / 2);
        mWindow.setBackgroundDrawable(new BitmapDrawable());
        mWindow.setFocusable(true);
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_public_goods, null, false);
        rootView.findViewById(R.id.father).setOnClickListener(this);
        txtPublicGoods = (TextView) rootView.findViewById(R.id.txtPublicGoods);
        txtPublicFlashSale = (TextView) rootView.findViewById(R.id.txtPublicFlashSale);
        txtPublicPreSell = (TextView) rootView.findViewById(R.id.txtPublicPreSell);

        txtPublicGoods.setOnClickListener(this);
        txtPublicFlashSale.setOnClickListener(this);
        txtPublicPreSell.setOnClickListener(this);

        mWindow.setContentView(rootView);
    }

    public void showAsDropDown(View anchor) {
        mWindow.showAsDropDown(anchor, 0, MyUtil.dip2px(mContext, -15));
    }

    public void dismiss() {
        mWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.txtPublicGoods:
                if (onClickItemListener != null) {
                    onClickItemListener.clickPublicGoods();
                }
                break;
            case R.id.txtPublicFlashSale:
                if (onClickItemListener != null) {
                    onClickItemListener.clickPublicFlashSale();
                }
                break;
            case R.id.txtPublicPreSell:
                if (onClickItemListener != null) {
                    onClickItemListener.clickPublicPreSell();
                }
                break;
        }
    }

    private OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void clickPublicGoods();

        void clickPublicFlashSale();

        void clickPublicPreSell();
    }
}
