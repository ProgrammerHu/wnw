package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.RichTextActivity;
import com.hemaapp.wnw.activity.SelectImageEditActivity;
import com.hemaapp.wnw.adapter.AddImageGoodsAdapter;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.model.FileUploadResult;
import com.hemaapp.wnw.model.Image;
import com.hemaapp.wnw.model.NoteDetail;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 发布帖子界面
 * Created by HuHu on 2016-08-16.
 */
public class SendPostActivity extends SelectImageEditActivity implements View.OnClickListener {
    private final int SELECT_TAGS = 100;//选择相关标签
    private final int SELECT_GOODS = 101;//选择相关商品
    private final int INPUT_HTML = 102;//编辑富文本
    private ImageView imageQuitActivity;
    private GridView gridView;
    private EditText editTitle;//editContent;
    private View layoutTags, layoutGoods, layoutContent, txtSave, txtPublic;
    private TextView txtTitle, txtTags, txtGoods;
    private AddImageGoodsAdapter adapter;
    private ArrayList<String[]> imagesList = new ArrayList<>();

    private String id;//为空时就是新增
    private NoteDetail model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_post);
        super.onCreate(savedInstanceState);
        if (!isNull(id))
            getNetWorker().noteGet(getApplicationContext().getUser().getToken(), id);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTE_GET:
                showProgressDialog(R.string.loading);
                break;
            case FILE_UPLOAD:
                showProgressDialog("图片上传中");
                break;
            case NOTE_ADD:
                showProgressDialog(R.string.committing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTE_GET:
            case FILE_UPLOAD:
            case NOTE_ADD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTE_GET:
                HemaArrayResult<NoteDetail> noteGetResult = (HemaArrayResult<NoteDetail>) hemaBaseResult;
                model = noteGetResult.getObjects().get(0);
                setData();
                break;
            case FILE_UPLOAD:
                HemaArrayResult<FileUploadResult> fileUploadResult = (HemaArrayResult<FileUploadResult>) hemaBaseResult;
                imagesList.add(fileUploadResult.getObjects().get(0).getItems());
                adapter.notifyDataSetChanged();
                break;
            case NOTE_ADD:
                MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
                dialog.setText(hemaBaseResult.getMsg()).hideCancel().hideIcon().setTitle("发布帖子");
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
                EventBus.getDefault().post(new EventBusModel(true, MyConfig.REFRESH_POST_LIST));
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
        txtTitle.setText("发布帖子");
        gridView = (GridView) findViewById(R.id.gridView);
        editTitle = (EditText) findViewById(R.id.editTitle);
//        editContent = (EditText) findViewById(R.id.editContent);
        layoutTags = findViewById(R.id.layoutTags);
        layoutTags.setTag(R.id.TAG, "");
        layoutGoods = findViewById(R.id.layoutGoods);
        layoutGoods.setTag(R.id.TAG, "");
        layoutContent = findViewById(R.id.layoutContent);
        layoutContent.setTag(R.id.TAG, "");
        txtTags = (TextView) findViewById(R.id.txtTags);
        txtGoods = (TextView) findViewById(R.id.txtGoods);
        adapter = new AddImageGoodsAdapter(mContext, gridView, imagesList);
        gridView.setAdapter(adapter);

        txtSave = findViewById(R.id.txtSave);
        txtPublic = findViewById(R.id.txtPublic);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        txtPublic.setOnClickListener(this);
        layoutTags.setOnClickListener(this);
        layoutGoods.setOnClickListener(this);
        layoutContent.setOnClickListener(this);
    }

    @Override
    protected void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void addNewImage(String path) {
        getNetWorker().fileUpload(getApplicationContext().getUser().getToken(),
                "11", "0", "0", "0", "无", path);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.txtSave://保存到草稿箱
                doPost("1");
                break;
            case R.id.txtPublic://发布
                doPost("2");
                break;
            case R.id.layoutTags:
                intent = new Intent(this, SelectTagsActivity.class);
                intent.putExtra("ids", (String) layoutTags.getTag(R.id.TAG));
                startActivityForResult(intent, SELECT_TAGS);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutGoods:
                intent = new Intent(this, SelectRecommendGoodsActivity.class);
                intent.putExtra("goods_id", (String) layoutGoods.getTag(R.id.TAG));
                startActivityForResult(intent, SELECT_GOODS);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutContent:
                intent = new Intent(this, RichTextActivity.class);
                String defaultHtml = (String) layoutContent.getTag(R.id.TAG);
                log_e(defaultHtml);
                intent.putExtra("defaultHtml", defaultHtml);
                startActivityForResult(intent, INPUT_HTML);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        MyFinish();
        return super.onKeyBack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SELECT_TAGS: {
                String table_id = data.getStringExtra("table_id");
                int table_count = data.getIntExtra("table_count", 0);
                layoutTags.setTag(R.id.TAG, table_id);
                txtTags.setText("已选择" + table_count + "种");
            }
            break;
            case INPUT_HTML:
                layoutContent.setTag(R.id.TAG, data.getStringExtra("html"));
                break;
            case SELECT_GOODS: {
                String goods_id = data.getStringExtra("goods_id");
                int goods_count = data.getIntExtra("goods_count", 0);
                layoutGoods.setTag(R.id.TAG, goods_id);
                if (goods_count == 0) {
                    txtGoods.setText("");
                } else {
                    txtGoods.setText("已选择" + goods_count + "种");
                }

            }
            break;
        }
    }

    /**
     * 执行请求
     *
     * @param flag
     */
    private void doPost(String flag) {
        String title, content, memo, keyid, table_id, good_id, imgurl_str;
        title = editTitle.getEditableText().toString().trim();
        content = (String) layoutContent.getTag(R.id.TAG);
//        memo = editContent.getEditableText().toString().trim();
        memo = "";
        keyid = isNull(id) ? "0" : id;
        table_id = (String) layoutTags.getTag(R.id.TAG);
        good_id = (String) layoutGoods.getTag(R.id.TAG);
        imgurl_str = adapter.getImageUrlStr();
        if (isNull(imgurl_str) || imgurl_str.split(",").length < 2) {
            showTextDialog("请至少选择2张图片");
            return;
        }
        if (isNull(title)) {
            showTextDialog("请编辑帖子标题");
            return;
        }
        getNetWorker().noteAdd(getApplicationContext().getUser().getToken(), title, content, memo,
                keyid, table_id, good_id, flag, imgurl_str);
    }

    /**
     * 充实数据
     */
    private void setData() {
        if (model == null) {
            return;
        }
        editTitle.setText(model.getTitle());

        layoutTags.setTag(R.id.TAG, model.getTable_id());
        int tagsCount = model.getTable_id().split(",").length;
        if (tagsCount == 0 || isNull(model.getTable_id())) {
            txtTags.setText("");
        } else {
            txtTags.setText("已选择" + tagsCount + "种");
        }

        layoutGoods.setTag(R.id.TAG, model.getGoods_id());
        int goodsCount = model.getGoods_id().split(",").length;
        if (goodsCount == 0 || isNull(model.getGoods_id())) {
            txtGoods.setText("");
        } else {
            txtGoods.setText("已选择" + tagsCount + "种");
        }

        layoutContent.setTag(R.id.TAG, model.getCon());
        imagesList.clear();
        String[] imgs = new String[2];
        imgs[0] = model.getImgurlbig();
        imgs[1] = model.getImgurl();
        imagesList.add(imgs);

        for (Image image : model.getImgItems()) {
            String[] strings = new String[2];
            strings[0] = image.getImgurlbig();
            strings[1] = image.getImgurl();
            imagesList.add(strings);
        }
        adapter.notifyDataSetChanged();
    }
}
