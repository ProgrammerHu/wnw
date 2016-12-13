package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.AddImageAdapter;
import com.hemaapp.wnw.dialog.ListViewDialog;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.model.GeneralListModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MeasureGridView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 退款界面
 * Created by Hufanglin on 2016/3/18.
 */
public class RefundActivity extends SelectImageActivity implements View.OnClickListener {

    private ImageView imageQuitActivity;
    private TextView txtTitle, txtReason;
    private Button btnConfirm;
    private EditText editText;
    private MeasureGridView gridView;
    private AddImageAdapter adapter;

    private View layoutTop;
    private String id;

    private ArrayList<GeneralListModel> listData = new ArrayList<>();
    private ListViewDialog listViewDialog;
    private ArrayList<String> imageStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refund);
        super.onCreate(savedInstanceState);
        if (isNull(id)) {
            showTextDialog("id空了");
        }
        getNetWorker().generalList("2");
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GENERAL_LIST:
                showProgressDialog(R.string.loading);
                break;
            case REFUND:
            case FILE_UPLOAD:
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
            case GENERAL_LIST:
                MyArrayResult<GeneralListModel> listResult = (MyArrayResult<GeneralListModel>) hemaBaseResult;
                listData.clear();
                listData.addAll(listResult.getObjects());
                break;
            case REFUND:
                //退款申请完成的广播，订单列表和订单详情都需要刷新，如果订单详情空了，要关闭订单详情界面
                EventBus.getDefault().post(new EventBusModel(true, MyConfig.ORDER_REFUND));
                if (imageStrings == null || imageStrings.size() == 0) {
                    ShowSuccessdDialog();
                    return;
                }
                uploadImage(0);
                break;
            case FILE_UPLOAD:
                int orderby = Integer.valueOf(hemaNetTask.getParams().get("orderby")) + 1;
                if (orderby >= imageStrings.size()) {
                    ShowSuccessdDialog();
                    return;
                }
                uploadImage(orderby);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FILE_UPLOAD:
                int orderby = Integer.valueOf(hemaNetTask.getParams().get("orderby"));
                FileUploadFailedDialog(orderby);
                break;
            default:
                showMyOneButtonDialog("退款申请", hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FILE_UPLOAD:
                int orderby = Integer.valueOf(hemaNetTask.getParams().get("orderby"));
                FileUploadFailedDialog(orderby);
                break;
        }
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("退款申请");
        txtReason = (TextView) findViewById(R.id.txtReason);
        layoutTop = findViewById(R.id.layoutTop);
        editText = (EditText) findViewById(R.id.editText);
        gridView = (MeasureGridView) findViewById(R.id.gridView);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        adapter = new AddImageAdapter(mContext, gridView, images);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        layoutTop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.btnConfirm:
                clickConfirm();
                break;
            case R.id.layoutTop:
                showReasonDialog();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }


    @Override
    protected void refreshAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 上传图片的方法
     */
    private void uploadImage(int orderby) {
        getNetWorker().fileUpload(getApplicationContext().getUser().getToken(),
                "3", id, "0", String.valueOf(orderby), "无", imageStrings.get(orderby));
    }


    /**
     * 显示退款原因的dialog
     */
    public void showReasonDialog() {
        if (listData.size() == 0) {
            return;
        }
        if (listViewDialog == null) {
            listViewDialog = new ListViewDialog(this, listData);
        }
        listViewDialog.show();
    }

    /**
     * 设置原因
     *
     * @param reason
     */
    public void setReason(String reason) {
        txtReason.setText(reason);
    }

    /**
     * 点击提交了
     */
    public void clickConfirm() {
        String reasonAction = getResources().getString(R.string.refund_action);
        String reason = txtReason.getText().toString();
        if (isNull(id)) {
            showMyOneButtonDialog("退款申请", "请退出该界面重新进入");
            return;
        }
        if (reasonAction.equals(reason)) {
            showMyOneButtonDialog("退款申请", "请选择退款原因！");
            return;
        }
        imageStrings = adapter.getImageStrings();
        String description = MyUtil.replaceBlank(editText.getEditableText().toString().trim());
        getNetWorker().refund(getApplicationContext().getUser().getToken(),
                id, reason, description);
    }

    /**
     * 点击即退出
     */
    public void ShowSuccessdDialog() {
        MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
        dialog.setTitle("退款申请").setText("申请成功").hideCancel().hideIcon().
                setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                    @Override
                    public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        finish(R.anim.my_left_in, R.anim.right_out);
                    }

                    @Override
                    public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        finish(R.anim.my_left_in, R.anim.right_out);
                    }
                });
        dialog.show();
    }

    /**
     * 点击即退出
     */
    public void FileUploadFailedDialog(final int orderby) {
        MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
        dialog.setTitle("退款申请").setText("图片上传失败，是否立即上传？").hideCancel().hideIcon().
                setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                    @Override
                    public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        uploadImage(orderby);
                    }

                    @Override
                    public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                        finish(R.anim.my_left_in, R.anim.right_out);
                    }
                });
        dialog.show();
    }
}
