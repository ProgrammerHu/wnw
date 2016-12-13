package com.hemaapp.wnw.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.OpenGroupGridAdapter;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.model.GroupGetModel;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.wnw.util.ShareParams;
import com.hemaapp.wnw.view.MeasureGridView;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 开团界面
 * Created by HuHu on 2016/3/24.
 */
public class OpenGroupActivity extends MyActivity implements View.OnClickListener {

    private TextView txtTitle, txtName, txtRule, txtPrice, txtOldPrice, txtAction, txtLeftCount;
    private ImageView imageQuitActivity, imageView;
    private MeasureGridView gridView;
    private LinearLayout layoutConfirm;
    private OpenGroupGridAdapter adapter;

    private String id;
    private GroupGetModel model;
    private ArrayList<GroupGetModel.ChildItemsEntity> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_open_group);
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(mContext);
        if (isNull(id)) {
            showTextDialog("id空了");
            return;
        }
        getNetWorker().groupGet(id);

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_GET:
                showProgressDialog(R.string.loading);
                break;
            case GROUP_BILL_ADD:
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
            case GROUP_GET:
                HemaArrayResult<GroupGetModel> modelResult = (HemaArrayResult<GroupGetModel>) hemaBaseResult;
                this.model = modelResult.getObjects().get(0);
                listData.clear();
                listData.addAll(model.getChildItems());
                adapter.notifyDataSetChanged();
                changeSelected(0);
                setData();
                break;
            case GROUP_BILL_ADD:
                HemaArrayResult<String> addResult = (HemaArrayResult<String>) hemaBaseResult;
                String id = addResult.getObjects().get(0);
                String shareUrl = MyConfig.SYS_ROOT + "index.php?g=webwnw&m=user&a=validate&id=" + id;
                shareToWechat(model.getImgurlbig(), MyApplication.getInstance().getSysInitInfo().getGroup_memo(), model.getName(), shareUrl);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        if (hemaBaseResult.getMsg().contains("结束")) {
            MyOneButtonDialog oneButtonDialog = new MyOneButtonDialog(mContext);
            oneButtonDialog.hideIcon().hideCancel().setTitle("开团").setText(hemaBaseResult.getMsg()).setButtonListener(new MyOneButtonDialog.OnButtonListener() {
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
            oneButtonDialog.show();
        } else {
            showMyOneButtonDialog("开团", hemaBaseResult.getMsg());
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("开团");
        txtName = (TextView) findViewById(R.id.txtName);
        txtRule = (TextView) findViewById(R.id.txtRule);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtOldPrice = (TextView) findViewById(R.id.txtOldPrice);
        txtOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        txtAction = (TextView) findViewById(R.id.txtAction);
        txtLeftCount = (TextView) findViewById(R.id.txtLeftCount);
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageView = (ImageView) findViewById(R.id.imageView);
        gridView = (MeasureGridView) findViewById(R.id.gridView);
        layoutConfirm = (LinearLayout) findViewById(R.id.layoutConfirm);
        adapter = new OpenGroupGridAdapter(mContext, listData);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        layoutConfirm.setOnClickListener(this);
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.layoutConfirm:
                if (!MyUtil.isAppAvailible(mContext, "com.tencent.mm")) {//判断是否已安装微信
                    showMyOneButtonDialog("开团", "请先安装微信");
                    return;
                }
                String id = adapter.getSelectId();
                if (isNull(id)) {
                    showMyOneButtonDialog("开团", "请选择团购人数");
                    return;
                }
                int leftcount = 0;
                try {
                    leftcount = Integer.parseInt(model.getLeftcount());
                } catch (Exception e) {
                }
                if (leftcount < adapter.getSelectNum()) {
                    showMyOneButtonDialog("开团", "库存不足");
                    return;
                }
                getNetWorker().groupBillAdd(
                        getApplicationContext().getUser().getToken(), id);
                break;
        }
    }

    /**
     * 修改选项之后续操作
     *
     * @param position
     */
    public void changeSelected(int position) {
        if (model == null || position > model.getChildItems().size()) {
            return;
        }
        txtAction.setText("注:达到" + model.getChildItems().get(position).getLimit_person() + "人即可成团，建议最少" +
                model.getChildItems().get(position).getPerson() + "人。");
        try {
            double person = Double.parseDouble(model.getChildItems().get(position).getPerson());
            double oldPrice = Double.parseDouble(model.getChildItems().get(position).getPrice());
            double price = Double.parseDouble(model.getChildItems().get(position).getGroup_price());
            DecimalFormat df = new DecimalFormat("0.00");
            txtPrice.setText("￥" + df.format(price * person));
            txtOldPrice.setText("￥" + df.format(oldPrice * person));
        } catch (Exception e) {
            txtPrice.setText("￥0.00");
            txtOldPrice.setText("￥0.00");
        }
    }

    private void setData() {
        txtName.setText(model.getName());
        txtRule.setText(model.getRule_name());
        txtLeftCount.setText("库存:" + model.getLeftcount());
        try {
            URL url = new URL(model.getImgurl());
            ImageTask task = new ImageTask(imageView, url, mContext, R.drawable.logo_default_square);
            imageWorker.loadImage(task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            imageView.setImageResource(R.drawable.logo_default_square);
        }
    }

    /**
     * 单独分享到微信
     *
     * @param path
     */
    public void shareToWechat(String imageUrl, String title, String content, String path) {
        cn.sharesdk.framework.Platform.ShareParams sp = new cn.sharesdk.framework.Platform.ShareParams();
        if (isNull(imageUrl)) {
            imageUrl = getApplicationContext().getSysInitInfo().getLogo();
        }
        sp.setTitle(title);
        sp.setText(content);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setImageUrl(imageUrl);
        sp.setUrl(path);
        Platform plat = ShareSDK.getPlatform(mContext, Wechat.NAME);
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                showTextDialog("分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                showTextDialog("分享失败");
                log_e(throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                showTextDialog("已取消");
            }
        });
        plat.share(sp);
    }
}
