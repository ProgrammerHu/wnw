package com.hemaapp.wnw.activity.business;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.model.AttrListModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.result.MyArrayResult;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 设置商品规格参数界面（价格和库存）
 * Created by HuHu on 2016-08-16.
 */
public class SetGoodsParamsActivity extends MyActivity implements View.OnClickListener, TextWatcher {

    private ImageView imageQuitActivity;
    private TextView txtTitle, txtNext;
    private LinearLayout layoutContent;
    private String id;
    private ArrayList<AttrListModel> listData = new ArrayList<>();
    private ArrayList<ViewHolder> holders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_goods_params);
        super.onCreate(savedInstanceState);
        getNetWorker().attrList(getApplicationContext().getUser().getToken(), id);

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ATTR_LIST:
                showProgressDialog(R.string.loading);
                break;
            case ATTR_SAVE:
                showProgressDialog(R.string.committing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ATTR_LIST:
                MyArrayResult<AttrListModel> getResult = (MyArrayResult<AttrListModel>) hemaBaseResult;
                listData.clear();
                listData.addAll(getResult.getObjects());
                initView();
                break;
            case ATTR_SAVE:
                MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
                dialog.setText(hemaBaseResult.getMsg()).hideIcon().hideCancel().setTitle("商品规格");
                dialog.setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                    @Override
                    public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        MyFinish();
                    }

                    @Override
                    public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        MyFinish();
                    }
                });
                dialog.show();
                EventBus.getDefault().post(new EventBusModel(true, MyConfig.ADD_NEW_GOODS));
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        showTextDialog(hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("商品规格");
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtNext.setVisibility(View.VISIBLE);
        txtNext.setText("保存");
        layoutContent = (LinearLayout) findViewById(R.id.layoutContent);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtNext.setOnClickListener(this);
    }

    private void initView() {
        layoutContent.removeAllViews();
        holders.clear();
        for (int i = 0; i < listData.size(); i++) {
            AttrListModel model = listData.get(i);
            View rootView = LayoutInflater.from(mContext).inflate(R.layout.scrollitem_goods_params, null, false);
            ViewHolder holder = new ViewHolder(rootView);
            holder.txtParams.setText(isNull(model.getMix_two_name()) ? model.getMix_name() : model.getMix_name() + " | " + model.getMix_two_name());
            holder.editPrice.setText(model.getPrice());
            holder.editBalance.setText(model.getLeftcount());
            holder.editPrice.addTextChangedListener(this);
            holder.editBalance.addTextChangedListener(this);
            layoutContent.addView(rootView);
            holders.add(holder);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.txtNext:
                clickConfirm();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    private class ViewHolder {
        public ViewHolder(View view) {
            txtParams = (TextView) view.findViewById(R.id.txtParams);
            editPrice = (EditText) view.findViewById(R.id.editPrice);
            editBalance = (EditText) view.findViewById(R.id.editBalance);
        }

        private TextView txtParams;
        private EditText editPrice, editBalance;
    }

    private void clickConfirm() {
        if (listData.size() == 0) {
            return;
        }
        for (int i = 0; i < listData.size() && i < holders.size(); i++) {
            AttrListModel model = listData.get(i);
            ViewHolder holder = holders.get(i);
            model.setPrice(holder.editPrice.getEditableText().toString());
            model.setLeftcount(holder.editBalance.getEditableText().toString());
        }
        getNetWorker().attrSave(getApplicationContext().getUser().getToken(), new Gson().toJson(listData));
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable edt) {
        if (".".equals(edt.toString())) {
            edt.replace(0, 1, "0.");
            return;
        }
        String temp = edt.toString();
        int posDot = temp.indexOf(".");
        if (posDot <= 0) return;
        if (temp.length() - posDot - 1 > 2) {
            edt.delete(posDot + 3, posDot + 4);
        }
    }
}

