package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.AddGoodsParamsAdapter;
import com.hemaapp.wnw.dialog.InputParamDialog;
import com.hemaapp.wnw.model.GoodsParamsModel;

import java.util.ArrayList;

/**
 * 商品规格列表
 * Created by HuHu on 2016-09-07.
 */
public class GoodsParamsActivity extends MyActivity implements View.OnClickListener {
    private ImageView imageQuitActivity, imageAdd;
    private TextView txtTitle;
    private GridView gridView1, gridView2;
    private EditText editText1, editText2;
    private View layoutBottom;
    private Button btnConfirm;

    private AddGoodsParamsAdapter adapter1, adapter2;
    private ArrayList<GoodsParamsModel.ParamsChild> list1 = new ArrayList<>();
    private ArrayList<GoodsParamsModel.ParamsChild> list2 = new ArrayList<>();
    private ArrayList<GoodsParamsModel> listData = new ArrayList<>();
    private boolean IsReadOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_params);
        super.onCreate(savedInstanceState);
        setData();
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
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageAdd = (ImageView) findViewById(R.id.imageAdd);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("商品规格");
        gridView1 = (GridView) findViewById(R.id.gridView1);
        gridView2 = (GridView) findViewById(R.id.gridView2);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        layoutBottom = findViewById(R.id.layoutBottom);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        adapter1 = new AddGoodsParamsAdapter(mContext, IsReadOnly, list1, 1);
        adapter2 = new AddGoodsParamsAdapter(mContext, IsReadOnly, list2, 2);
        gridView1.setAdapter(adapter1);
        gridView2.setAdapter(adapter2);
    }

    @Override
    protected void getExras() {
        listData = (ArrayList<GoodsParamsModel>) mIntent.getSerializableExtra("listData");
        IsReadOnly = mIntent.getBooleanExtra("IsReadOnly", false);
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        imageAdd.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.imageAdd:
                imageAdd.setVisibility(View.GONE);
                layoutBottom.setVisibility(View.VISIBLE);
                break;
            case R.id.btnConfirm:
                clickConfirm();
                break;
        }
    }

    /**
     * 新增
     *
     * @param index 1,第一个
     */
    public void showAddDialog(final int index) {
        InputParamDialog dialog = new InputParamDialog(mContext, "");
        dialog.setButtonListener(new InputParamDialog.OnButtonListener() {
            @Override
            public void onButtonClick(InputParamDialog inputParamDialog, String text) {
                inputParamDialog.cancel();
                if (index == 1) {
                    list1.add(new GoodsParamsModel.ParamsChild("0", text));
                    adapter1.notifyDataSetChanged();
                } else {
                    list2.add(new GoodsParamsModel.ParamsChild("0", text));
                    adapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelClick(InputParamDialog inputParamDialog) {
                inputParamDialog.cancel();
            }
        });
        dialog.show();
    }

    /**
     * 编辑
     *
     * @param position
     * @param index
     */
    public void showEditDialog(final int position, final int index) {
        final GoodsParamsModel.ParamsChild model = index == 1 ? list1.get(position) : list2.get(position);
        InputParamDialog dialog = new InputParamDialog(mContext, model.getName());
        String cancel = "0".equals(model.getId()) ? "删除" : "取消";
        dialog.setCancelText(cancel);
        dialog.setButtonListener(new InputParamDialog.OnButtonListener() {
            @Override
            public void onButtonClick(InputParamDialog inputParamDialog, String text) {
                inputParamDialog.cancel();
                if (index == 1) {
                    model.setName(text);
                    adapter1.notifyDataSetChanged();
                } else {
                    model.setName(text);
                    adapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelClick(InputParamDialog inputParamDialog) {
                inputParamDialog.cancel();
                if (!"0".equals(model.getId())) {//删除
                    return;
                }
                if (index == 1) {
                    list1.remove(position);
                    adapter1.notifyDataSetChanged();
                } else {
                    list2.remove(position);
                    adapter2.notifyDataSetChanged();
                }
            }
        });
        dialog.show();
    }

    private void clickConfirm() {
        String id = "0";
        String param1 = editText1.getEditableText().toString().trim();
        String param2 = editText2.getEditableText().toString().trim();
        if (isNull(param1)) {
            showTextDialog("请输入规格名称");
            return;
        }
        if (list1.size() == 0) {
            showTextDialog("请输入规格内容");
            return;
        }
        if (isNull(param2) && list2.size() > 0) {
            showTextDialog("请输入规格名称");
            return;
        }
        if (!isNull(param2) && list2.size() == 0) {
            showTextDialog("请输入规格内容");
            return;
        }
        GoodsParamsModel model1 = new GoodsParamsModel(id, param1, list1);
        GoodsParamsModel model2 = new GoodsParamsModel(id, param2, list2);
        listData.clear();
        listData.add(model1);
        if (!isNull(param2)) {
            listData.add(model2);
        }
        Intent intent = new Intent();
        intent.putExtra("result", listData);
        setResult(RESULT_OK, intent);
        MyFinish();
//        Gson gson = new Gson();
//        log_e(gson.toJson(listData));
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    private void setData() {
        if (listData == null) {
            return;
        }
        if (listData.size() > 0) {
            editText1.setText(listData.get(0).getParam_name());
            list1.addAll(listData.get(0).getChildren());
            adapter1.notifyDataSetChanged();
        }
        if (listData.size() > 1) {
            editText2.setText(listData.get(1).getParam_name());
            list2.addAll(listData.get(1).getChildren());
            adapter2.notifyDataSetChanged();
            layoutBottom.setVisibility(View.VISIBLE);
            imageAdd.setVisibility(View.GONE);
        }
        if (IsReadOnly) {
            imageAdd.setVisibility(View.GONE);
        }

    }

}
