package com.hemaapp.wnw.activity.business;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_framework.view.MeasureGridView;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.RichTextActivity;
import com.hemaapp.wnw.activity.SelectImageEditActivity;
import com.hemaapp.wnw.adapter.AddImageGoodsAdapter;
import com.hemaapp.wnw.model.FileUploadResult;
import com.hemaapp.wnw.model.GoodsParamsModel;
import com.hemaapp.wnw.model.MerchantGoodsGetModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 发布商品的界面
 * Created by HuHu on 2016-08-16.
 */
public class PublicGoodsActivity extends SelectImageEditActivity implements View.OnClickListener {
    private final int SELECT_TYPE = 101;//选择商品类别
    private final int INPUT_PARAMS = 102;//输入商品规格
    private final int INPUT_HTML = 103;//输入输入图文详情
    private final int SELECT_AREA = 104;//选择地区

    private ImageView imageQuitActivity;
    private TextView txtTitle, txtPutOn, txtPutOff, txtKind, txtStandard, txtArea;
    private EditText editGoodsName, editPrice, editTravelFee;
    private View layoutKind, layoutStandard, layoutArea, layoutContent;
    private MeasureGridView gridView;
    private AddImageGoodsAdapter adapter;
    private Button btnConfirm;
    private ArrayList<String[]> imagesList = new ArrayList<>();

    private String id = "0";
    private MerchantGoodsGetModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_public_goods);
        super.onCreate(savedInstanceState);
        if ("0".equals(id)) {//新增
            onClick(txtPutOn);
        } else {//编辑
            getNetWorker().merchantBlogGet(getApplicationContext().getUser().getToken(), id);
        }

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FILE_UPLOAD:
                showProgressDialog("图片上传中");
                break;
            case BLOG_ADD:
                showProgressDialog(R.string.committing);
                break;
            case MERCHANT_BLOG_GET:
                showProgressDialog(R.string.loading);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FILE_UPLOAD:
            case BLOG_ADD:
            case MERCHANT_BLOG_GET:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case FILE_UPLOAD:
                HemaArrayResult<FileUploadResult> fileUploadResult = (HemaArrayResult<FileUploadResult>) hemaBaseResult;
                imagesList.add(fileUploadResult.getObjects().get(0).getItems());
                adapter.notifyDataSetChanged();
                break;
            case BLOG_ADD:
                EventBus.getDefault().post(new EventBusModel(true, MyConfig.ADD_NEW_GOODS));
                HemaArrayResult<String> idResult = (HemaArrayResult<String>) hemaBaseResult;
                String id = idResult.getObjects().get(0);
                Intent intent = new Intent(this, SetGoodsParamsActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                finish();
                break;
            case MERCHANT_BLOG_GET:
                HemaArrayResult<MerchantGoodsGetModel> goodsGetResult = (HemaArrayResult<MerchantGoodsGetModel>) hemaBaseResult;
                model = goodsGetResult.getObjects().get(0);
                setData();
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
        txtTitle.setText("发布商品");
        gridView = (MeasureGridView) findViewById(R.id.gridView);
        adapter = new AddImageGoodsAdapter(mContext, gridView, imagesList);
        gridView.setAdapter(adapter);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        txtPutOn = (TextView) findViewById(R.id.txtPutOn);
        txtPutOff = (TextView) findViewById(R.id.txtPutOff);
        txtKind = (TextView) findViewById(R.id.txtKind);
        txtKind.setTag(R.id.TAG, "");//保存商品类别id
        txtStandard = (TextView) findViewById(R.id.txtStandard);
        txtStandard.setTag(R.id.TAG, new ArrayList<GoodsParamsModel>());//保存商品规格
        txtArea = (TextView) findViewById(R.id.txtArea);
        txtArea.setTag(R.id.TAG, "");

        editGoodsName = (EditText) findViewById(R.id.editGoodsName);
        editPrice = (EditText) findViewById(R.id.editPrice);
        editTravelFee = (EditText) findViewById(R.id.editTravelFee);

        layoutKind = findViewById(R.id.layoutKind);
        layoutStandard = findViewById(R.id.layoutStandard);
        layoutArea = findViewById(R.id.layoutArea);
        layoutContent = findViewById(R.id.layoutContent);
        layoutContent.setTag(R.id.TAG, "");
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        txtPutOn.setOnClickListener(this);
        txtPutOff.setOnClickListener(this);
        layoutKind.setOnClickListener(this);
        layoutStandard.setOnClickListener(this);
        layoutArea.setOnClickListener(this);
        layoutContent.setOnClickListener(this);
        editPrice.addTextChangedListener(decimal2TextWatcher);
        editTravelFee.addTextChangedListener(decimal2TextWatcher);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                MyFinish();
                break;
            case R.id.btnConfirm:
                clickConfirm();
                break;
            case R.id.txtPutOn:
                txtPutOn.setSelected(true);
                txtPutOff.setSelected(false);
                txtPutOn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                txtPutOff.getPaint().setFlags(Paint.SUBPIXEL_TEXT_FLAG);
                txtPutOn.getPaint().setAntiAlias(true);
                txtPutOff.getPaint().setAntiAlias(true);
                break;
            case R.id.txtPutOff:
                txtPutOn.setSelected(false);
                txtPutOff.setSelected(true);
                txtPutOn.getPaint().setFlags(Paint.SUBPIXEL_TEXT_FLAG);
                txtPutOff.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                txtPutOn.getPaint().setAntiAlias(true);
                txtPutOff.getPaint().setAntiAlias(true);
                break;
            case R.id.layoutKind://选择商品类别
                intent = new Intent(mContext, SelectGoodsTypeActivity.class);
                startActivityForResult(intent, SELECT_TYPE);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutStandard:
                ArrayList<GoodsParamsModel> list = (ArrayList<GoodsParamsModel>) txtStandard.getTag(R.id.TAG);
                intent = new Intent(mContext, GoodsParamsActivity.class);
                intent.putExtra("listData", list);
                intent.putExtra("IsReadOnly", !"0".equals(id));
                startActivityForResult(intent, INPUT_PARAMS);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutArea:
                intent = new Intent(mContext, SelectCountryActivity.class);
                startActivityForResult(intent, SELECT_AREA);
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
    protected void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void addNewImage(String path) {
        getNetWorker().fileUpload(getApplicationContext().getUser().getToken(),
                "11", "0", "0", "0", "无", path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SELECT_TYPE://设置商品规格
                txtKind.setText(data.getStringExtra("name"));
                txtKind.setTag(R.id.TAG, data.getStringExtra("id"));
                break;
            case INPUT_PARAMS://输入商品规格
                ArrayList<GoodsParamsModel> goodsParams = (ArrayList<GoodsParamsModel>) data.getSerializableExtra("result");
                String param_names = "";
                for (GoodsParamsModel model : goodsParams) {
                    if (isNull(param_names)) {
                        param_names = model.getParam_name();
                    } else {
                        param_names += "," + model.getParam_name();
                    }
                }
                txtStandard.setText(param_names);
                txtStandard.setTag(R.id.TAG, goodsParams);
                break;
            case SELECT_AREA://选择地区
                txtArea.setText(data.getStringExtra("name"));
                txtArea.setTag(R.id.TAG, data.getStringExtra("id"));
                break;
            case INPUT_HTML:
                layoutContent.setTag(R.id.TAG, data.getStringExtra("html"));
                break;
        }
    }

    /**
     * 加载数据之后充实数据
     */
    private void setData() {
        /*充实图片*/
        imagesList.clear();
        imagesList.addAll(model.getImgItem());
        adapter.notifyDataSetChanged();
        /*识别商品状态*/
        if ("1".equals(model.getSaleflag())) {//0.下架1.上架
            onClick(txtPutOn);
        } else {
            onClick(txtPutOff);
        }
        /*充实商品名称*/
        editGoodsName.setText(model.getName());
        /*商品类别*/
        txtKind.setText(model.getType_name());
        txtKind.setTag(R.id.TAG, model.getType_id());
        /*商品规格*/
        txtStandard.setText(model.getatrrItemsStr());
        txtStandard.setTag(R.id.TAG, model.getAtrrItems());
        /*其他数据*/
        editPrice.setText(model.getOldprice());
        editTravelFee.setText(model.getExpressfee());
        txtArea.setText(model.getCountry_name());
        txtArea.setTag(R.id.TAG, model.getCountry_id());
        layoutContent.setTag(R.id.TAG, model.getContent());
    }

    public void clickConfirm() {
        String token = getApplicationContext().getUser().getToken();
        String keyid = id;
        String keytype = "1";
        String saleflag = txtPutOn.isSelected() ? "1" : "0";//0.不上1.上架
        String name = editGoodsName.getEditableText().toString().trim();
        if (isNull(name)) {
            showTextDialog("请输入商品名称");
            return;
        }
        String type_id = (String) txtKind.getTag(R.id.TAG);
        if (isNull(type_id)) {
            showTextDialog("请选择商品类别");
            return;
        }
        ArrayList<GoodsParamsModel> attrList = (ArrayList<GoodsParamsModel>) txtStandard.getTag(R.id.TAG);
        if (attrList.size() == 0) {
            showTextDialog("请输入商品规格");
            return;
        }
        String attr = "";
        try {
            attr = URLDecoder.decode(new Gson().toJson(attrList), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String oldprice = editPrice.getEditableText().toString().trim();
        if (isNull(oldprice)) {
            oldprice = "0";
        }
        String expressfee = editTravelFee.getEditableText().toString().trim();
        if (isNull(expressfee)) {
            expressfee = "0";
        }
        String country_id = (String) txtArea.getTag(R.id.TAG);
        if (isNull(country_id)) {
            country_id = "0";
        }
        String content = (String) layoutContent.getTag(R.id.TAG);
        if (imagesList.size() < 2) {
            showTextDialog("请至少选择2张图片");
            return;
        }
        String imgurl_str = "";
        for (String[] image : imagesList) {
            if (isNull(imgurl_str)) {
                imgurl_str = image[0] + "," + image[1];
            } else {
                imgurl_str += ";" + image[0] + "," + image[1];
            }
        }
        String time = "";
        String time_id = "";
        String limit_count = "";
        String oldid = "";
        getNetWorker().blogAdd(token, keyid, keytype, saleflag, name, type_id,
                attr, oldprice, expressfee, country_id, content, imgurl_str,
                time, time_id, limit_count, oldid);
    }
}
