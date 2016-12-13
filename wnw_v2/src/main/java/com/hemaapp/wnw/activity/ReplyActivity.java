package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
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
 * 评价界面
 * Created by Hufanglin on 2016/3/18.
 */
public class ReplyActivity extends SelectImageActivity implements View.OnClickListener {

    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private Button btnConfirm;
    private EditText editText;
    private MeasureGridView gridView;
    private AddImageAdapter adapter;
    private RatingBar ratingBar;
    private String id;
    private String keyid;

    private ArrayList<String> imageStrings = new ArrayList<>();

    private boolean isCurrentReply = true;//标记是否是普通评价，否则是团购订单评价

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reply);
        super.onCreate(savedInstanceState);
        if (isNull(id)) {
            showTextDialog("id空了");
        }
//        getNetWorker().generalList("2");
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GENERAL_LIST:
                showProgressDialog(R.string.loading);
                break;
            case BLOG_REPLY_ADD:
            case GROUP_REPLY_ADD:
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
            case FILE_UPLOAD:
                cancelProgressDialog();
                int orderby = Integer.valueOf(hemaNetTask.getParams().get("orderby")) + 1;
                if (orderby >= imageStrings.size()) {
                    ShowSuccessdDialog();
                    return;
                }
                uploadImage(orderby);
                break;
            case BLOG_REPLY_ADD:
            case GROUP_REPLY_ADD:
                EventBus.getDefault().post(new EventBusModel(true, MyConfig.ORDER_REPLY));//发送评价完成的广播，订单列表和订单详情都需要刷新
                HemaArrayResult<String> ids = (HemaArrayResult<String>) hemaBaseResult;
                keyid = ids.getObjects().get(0);
                if (imageStrings == null || imageStrings.size() == 0) {//没选择图片
                    cancelProgressDialog();
                    ShowSuccessdDialog();
                } else {//选择了图片，上传图片，从头上传的那种
                    uploadImage(0);
                }
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
                showMyOneButtonDialog("评价详情", hemaBaseResult.getMsg());
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
        txtTitle.setText("评价详情");
        editText = (EditText) findViewById(R.id.editText);
        gridView = (MeasureGridView) findViewById(R.id.gridView);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        adapter = new AddImageAdapter(mContext, gridView, images);
        gridView.setAdapter(adapter);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
        isCurrentReply = mIntent.getBooleanExtra("isCurrentReply", true);
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
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
                "2", keyid, "0", String.valueOf(orderby), "无", imageStrings.get(orderby));
    }


    /**
     * 点击提交了
     */
    public void clickConfirm() {
        int stars = (int) ratingBar.getRating();
        if (isNull(id)) {
            showMyOneButtonDialog("评价详情", "请退出该界面重新进入");
            return;
        }
        imageStrings = adapter.getImageStrings();
        String description = MyUtil.replaceBlank(editText.getEditableText().toString().trim());
        if (stars == 0) {
            showMyOneButtonDialog("评价详情", "请选择星级评价");
            return;
        }
        if (isCurrentReply) {
            getNetWorker().blogReplyAdd(getApplicationContext().getUser().getToken(),
                    id, description, String.valueOf(stars));
        } else {
            getNetWorker().groupReplyAdd(getApplicationContext().getUser().getToken(),
                    id, description, String.valueOf(stars));
        }
    }

    /**
     * 点击即退出
     */
    public void ShowSuccessdDialog() {
        setResult(RESULT_OK);
        MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
        dialog.setTitle("评价详情").setText("评价成功").hideCancel().hideIcon().
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
        dialog.setTitle("评价详情").setText("图片上传失败，是否立即上传？").hideCancel().hideIcon().
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
