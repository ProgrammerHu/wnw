package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaImageWay;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.luna_framework.dialog.MyBottomThreeButtonDialog;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.db.UserDBHelper;
import com.hemaapp.wnw.dialog.AreaDialog;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.model.User;
import com.hemaapp.wnw.result.ClientAddResult;
import com.hemaapp.wnw.result.DistrictAllGetResult;

import java.io.File;
import java.io.IOException;

import xtom.frame.XtomActivityManager;
import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 充实个人信息 Created by Hufanglin on 2016/2/21.
 */
public class FixdataActivity extends DistrictActivity implements View.OnClickListener {
    private final int REQUEST_CODE_PICK_IMAGE = 1;// 相册获取
    private final int REQUEST_CODE_CAPTURE_CAMEIA = 2;// 相机获取
    private final int EDIT_IMAGE = 3;// 编辑照片

    private ImageView imageQuitActivity, imageHead;
    private TextView txtTitle, txtNext, txtSex, txtCity;
    private EditText editNickname;
    //    private EditText editInviteCode;
    private View layoutNickname, layoutSex, layoutCity, layoutInviteCode;

    private MyBottomThreeButtonDialog sexDialog, phptpDialog;
    private MyOneButtonDialog failedDialog;
    private AreaDialog areaDialog;

    private String imagePathCamera;// 相机拍照路径
    private String tempPath;// 临时路径
    private HemaImageWay imageWay;
    private String token, infor;

    private int ImageWidth;
    private int ImageHeight;
    private ShowLargeImageView mView;// 展示大图

    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fixdata);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            imageWay = new HemaImageWay(mContext, 1, 2) {
                public void album() {
                    Intent it1 = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (mContext != null)
                        mContext.startActivityForResult(it1, albumRequestCode);

                }
            };
            token = "";
        } else {
            imagePathCamera = savedInstanceState.getString("imagePathCamera");
            imageWay = new HemaImageWay(mContext, 1, 2) {
                public void album() {
                    Intent it1 = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (mContext != null)
                        mContext.startActivityForResult(it1, albumRequestCode);

                }
            };
            token = savedInstanceState.getString("token");
            tempPath = savedInstanceState.getString("tempPath");
            infor = savedInstanceState.getString("infor");
        }
        String district = XtomSharedPreferencesUtil.get(mContext, "District");
        if (isNull(district)) {
            getNetWorker().districtAllGet();
        } else if (getApplicationContext().getCityInfo() == null) {
            formatDistrict(district);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageWay != null) {
            outState.putString("imagePathCamera", imageWay.getCameraImage());
            imagePathCamera = imageWay.getCameraImage();// 保存相机路径
        }
        outState.putString("token", token);// 保存临时token
        outState.putString("tempPath", tempPath);// 保存临时头像路径
        outState.putString("infor", infor);// 保存临时infor

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_ALL_GET:
                showProgressDialog(R.string.loading);
                break;
            case CLIENT_ADD:
                showProgressDialog(R.string.committing);
                break;
            case FILE_UPLOAD:
                showProgressDialog(R.string.uploading_head);
                break;
            case CLIENT_LOGIN:
                showProgressDialog(R.string.logining);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult hemaBaseResult) {

        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_ALL_GET:
                DistrictAllGetResult result = (DistrictAllGetResult) hemaBaseResult;
                getApplicationContext().setCityInfo(result.getObjects().get(0));
                XtomSharedPreferencesUtil.save(mContext, "District",
                        result.getJsonObject());
                break;
            case CLIENT_ADD:
                ClientAddResult clientAddTask = (ClientAddResult) hemaBaseResult;
                token = clientAddTask.getToken();
                infor = clientAddTask.getInfor();
                if (isNull(tempPath)) {// 未设置头像
                    getNetWorker().clientLogin(username, password);
                } else {// 已设置头像
                    getNetWorker().fileUpload(token, "1", "0", "0", "0", "无",
                            tempPath);
                }
                break;
            case FILE_UPLOAD:
                getNetWorker().clientLogin(username, password);
                break;
            case CLIENT_LOGIN:
                HemaArrayResult<User> sUser = (HemaArrayResult<User>) hemaBaseResult;
                getApplicationContext().setUser(sUser.getObjects().get(0));
                XtomSharedPreferencesUtil.save(mContext, "username", username);
                XtomSharedPreferencesUtil.save(mContext, "password", password);
                XtomSharedPreferencesUtil.save(mContext, "savePassword", "1");
                // 保存用户信息
                UserDBHelper helper = new UserDBHelper(mContext);
                helper.insertOrUpdate(sUser.getObjects().get(0));
                Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                gotoMainActivity();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case FILE_UPLOAD:
                UploadImageFailed();
                break;
            default:
                showMyOneButtonDialog(getResources().getString(R.string.fixData),
                        hemaBaseResult.getMsg());
                break;
        }

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case FILE_UPLOAD:
                UploadImageFailed();
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtNext.setText(R.string.commit);
        txtNext.setVisibility(View.VISIBLE);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.fixData);
        imageHead = (ImageView) findViewById(R.id.imageHead);

        layoutNickname = findViewById(R.id.layoutNickname);
        layoutSex = findViewById(R.id.layoutSex);
        layoutCity = findViewById(R.id.layoutCity);
        layoutInviteCode = findViewById(R.id.layoutInviteCode);
        editNickname = (EditText) findViewById(R.id.editNickname);
        txtSex = (TextView) findViewById(R.id.txtSex);
        txtCity = (TextView) findViewById(R.id.txtCity);
//        editInviteCode = (EditText) findViewById(R.id.editInviteCode);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        txtNext.setOnClickListener(this);
        layoutSex.setOnClickListener(this);
        layoutCity.setOnClickListener(this);
        imageHead.setOnClickListener(this);
        imageHead.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isNull(tempPath)) {
                    mView = new ShowLargeImageView(mContext,
                            findViewById(R.id.father));
                    mView.show();
                    mView.setImagePath(tempPath);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.txtNext:
                clickConfirm();
                break;
            case R.id.imageHead:
                ImageWidth = imageHead.getWidth();
                ImageHeight = imageHead.getHeight();
                selectPhoto();
                break;
            case R.id.layoutSex:
                selectSex();
                break;
            case R.id.layoutCity:
                selectDistrict();
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_CAPTURE_CAMEIA:
                String imagepath = imageWay.getCameraImage();
                if (!isNull(imagepath))
                    imagePathCamera = imagepath;
                editImage(imagePathCamera, EDIT_IMAGE);
                break;
            case REQUEST_CODE_PICK_IMAGE:
                album(data);
                break;
            case EDIT_IMAGE:
                imageWorker.loadImage(new XtomImageTask(imageHead, tempPath,
                        mContext, new XtomImageTask.Size(ImageWidth, ImageHeight)));
                break;
            default:
                break;
        }
    }

    /**
     * 选择照片
     */
    private void selectPhoto() {
        if (phptpDialog == null) {
            phptpDialog = new MyBottomThreeButtonDialog(mContext);
            phptpDialog.setCancelable(true);
            phptpDialog.setTopButtonText(R.string.photograph);
            phptpDialog.setMiddleButtonText(R.string.album);
            phptpDialog
                    .setButtonListener(new MyBottomThreeButtonDialog.OnButtonListener() {
                        @Override
                        public void onTopButtonClick(
                                MyBottomThreeButtonDialog dialog) {
                            dialog.cancel();
                            imageWay.camera();// 相机获取
                        }

                        @Override
                        public void onMiddleButtonClick(
                                MyBottomThreeButtonDialog dialog) {
                            dialog.cancel();
                            imageWay.album();// 相册获取
                        }
                    });
        }
        phptpDialog.show();
    }

    /**
     * 选择性别
     */
    private void selectSex() {
        if (sexDialog == null) {
            sexDialog = new MyBottomThreeButtonDialog(mContext);
            sexDialog.setCancelable(true);
            sexDialog.setTopButtonText(R.string.male);
            sexDialog.setMiddleButtonText(R.string.female);
            sexDialog
                    .setButtonListener(new MyBottomThreeButtonDialog.OnButtonListener() {
                        @Override
                        public void onTopButtonClick(
                                MyBottomThreeButtonDialog dialog) {
                            dialog.cancel();
                            txtSex.setText(R.string.male);
                        }

                        @Override
                        public void onMiddleButtonClick(
                                MyBottomThreeButtonDialog dialog) {
                            dialog.cancel();
                            txtSex.setText(R.string.female);
                        }
                    });
        }
        sexDialog.show();
    }

    /**
     * 选择城市
     */
    public void selectDistrict() {
        if (getApplicationContext().getCityInfo() == null) {
            getNetWorker().districtAllGet();
            return;
        }
        if (areaDialog == null) {
            areaDialog = new AreaDialog(mContext);
            areaDialog.setButtonListener(new AreaDialog.OnButtonListener() {
                @Override
                public void onLeftButtonClick(AreaDialog dialog) {
                    dialog.cancel();
                }

                @Override
                public void onRightButtonClick(AreaDialog dialog) {
                    dialog.cancel();
                    txtCity.setText(dialog.getArea());
                }
            });
        }
        areaDialog.show();

    }

	/* 处理图片开始 */

    private void editImage(String path, int requestCode) {
        File file = new File(path);
        startPhotoZoom(Uri.fromFile(file), requestCode);
    }

    private void startPhotoZoom(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", MyConfig.IMAGE_WIDTH_HEAD);
        intent.putExtra("aspectY", MyConfig.IMAGE_WIDTH_HEAD);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", MyConfig.IMAGE_WIDTH_HEAD);
        intent.putExtra("outputY", MyConfig.IMAGE_WIDTH_HEAD);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, requestCode);
    }

    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        String savedir = XtomFileUtil.getTempFileDir(mContext);
        File dir = new File(savedir);
        if (!dir.exists())
            dir.mkdirs();
        // 保存入sdCard
        tempPath = savedir + XtomBaseUtil.getFileName() + ".jpg";// 保存路径
        File file = new File(tempPath);

        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return file;
    }

    /**
     * 通过相册获取图片的后续处理
     *
     * @param data
     */
    private void album(Intent data) {
        if (data == null)
            return;
        Uri selectedImageUri = data.getData();

        startPhotoZoom(selectedImageUri, 3);
    }

    /**
     * 上传图片失败的后续处理
     */
    private void UploadImageFailed() {
        if (failedDialog == null) {
            failedDialog = new MyOneButtonDialog(mContext);
            failedDialog.setTitle(R.string.fixData);
            failedDialog.setText(R.string.photoFailed);
            failedDialog.hideIcon();
            failedDialog
                    .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                        @Override
                        public void onButtonClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();
                            getNetWorker().fileUpload(token, "1", "0", "0",
                                    "0", "无", tempPath);
                        }

                        @Override
                        public void onCancelClick(
                                MyOneButtonDialog OneButtonDialog) {
                            OneButtonDialog.cancel();

                        }
                    });
        }
        failedDialog.show();
    }

	/* 处理图片结束 */

    /**
     * 点击提交
     */
    public void clickConfirm() {
        String temp_token, nickname, sex, district;
//        String invite_code;
        username = XtomSharedPreferencesUtil.get(mContext, "username");
        temp_token = XtomSharedPreferencesUtil.get(mContext, "temp_token");
        password = XtomSharedPreferencesUtil.get(mContext,
                "register_password");
        nickname = editNickname.getEditableText().toString().trim();
        sex = txtSex.getText().toString().trim();
        district = txtCity.getText().toString().trim();
//        invite_code = editInviteCode.getEditableText().toString().trim();
        if (isNull(temp_token)) {
            showMyOneButtonDialog(R.string.fixData,
                    R.string.error_temp_token_empty);
            return;
        }
        if (isNull(password)) {
            showMyOneButtonDialog(R.string.fixData,
                    R.string.error_register_pwd_empty);
            return;
        }
        if (isNull(nickname)) {
            showMyOneButtonDialog(R.string.fixData,
                    R.string.error_nickname_empty);
            return;
        }
//        if (isNull(invite_code)) {
//            showMyOneButtonDialog(R.string.fixData,
//                    R.string.error_invite_code_empty);
//            return;
//        }
        getNetWorker().clientAdd(temp_token, username, password,
                nickname, sex, district);
    }

    /**
     * 跳转到主页
     */
    private void gotoMainActivity() {
//        XtomActivityManager.finishAll();
        Intent intent = new Intent(this, MainFragmentActivity.class);
        if (!isNull(infor)) {
            intent.putExtra("infor", infor);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
    }
}
