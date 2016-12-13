package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaImageWay;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.luna_framework.dialog.MyBottomThreeButtonDialog;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.dialog.AreaDialog;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.MyOneButtonDialog.OnButtonListener;
import com.hemaapp.wnw.model.InputParams;
import com.hemaapp.wnw.model.UserClient;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.wnw.result.DistrictAllGetResult;

import de.greenrobot.event.EventBus;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 修改用户资料 Created by Hufanglin on 2016/3/4.
 */
public class ClientSaveActivity extends DistrictActivity implements
        View.OnClickListener {
    private final int REQUEST_CODE_PICK_IMAGE = 1;// 相册获取
    private final int REQUEST_CODE_CAPTURE_CAMEIA = 2;// 相机获取
    private final int EDIT_IMAGE = 3;// 编辑照片
    private final int INPUT_NICKNAME = 4;// 输入昵称

    private ImageView imageQuitActivity, imageHead;
    private TextView txtTitle, txtNext, txtSex, txtCity;
    private EditText txtNickname;
    private View layoutNickname, layoutSex, layoutCity;

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
    private boolean saveData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_data);
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
            case CLIENT_SAVE:
                showProgressDialog(R.string.committing);
                break;
            case FILE_UPLOAD:
                showProgressDialog(R.string.uploading_head);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case DISTRICT_ALL_GET:
                cancelProgressDialog();
                DistrictAllGetResult result = (DistrictAllGetResult) hemaBaseResult;
                getApplicationContext().setCityInfo(result.getObjects().get(0));
                XtomSharedPreferencesUtil.save(mContext, "District",
                        result.getJsonObject());
                break;
            case CLIENT_SAVE:
                getNetWorker().clientGet(
                        getApplicationContext().getUser().getToken());
                break;
            case FILE_UPLOAD:
                getNetWorker().clientGet(
                        getApplicationContext().getUser().getToken());
                saveData = false;
                break;
            case CLIENT_GET:
                cancelProgressDialog();
                HemaArrayResult<UserClient> clientGetResult = (HemaArrayResult<UserClient>) hemaBaseResult;
                getApplicationContext().getUser().changeUserData(
                        clientGetResult.getObjects().get(0));
                EventBus.getDefault().post(new EventBusModel(true, "ClientSave"));// 发送已修改用户信息的广播
                if (saveData) {
                    MyOneButtonDialog dialog = new MyOneButtonDialog(mContext);
                    dialog.setTitle("个人信息").setText("修改成功").hideCancel().hideIcon()
                            .setButtonListener(new OnButtonListener() {
                                @Override
                                public void onCancelClick(
                                        MyOneButtonDialog OneButtonDialog) {
                                    OneButtonDialog.cancel();
                                    finish(R.anim.my_left_in, R.anim.right_out);
                                }

                                @Override
                                public void onButtonClick(
                                        MyOneButtonDialog OneButtonDialog) {
                                    OneButtonDialog.cancel();
                                    finish(R.anim.my_left_in, R.anim.right_out);
                                }
                            });
                    dialog.show();
                }
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
                showMyOneButtonDialog(
                        getResources().getString(R.string.personal_data),
                        hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtNext.setText(R.string.commit);
        txtNext.setVisibility(View.VISIBLE);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.personal_data);
        imageHead = (ImageView) findViewById(R.id.imageHead);

        try {
            URL url = new URL(getApplicationContext().getUser().getAvatar());
            ImageTask task = new ImageTask(imageHead, url, mContext,
                    R.drawable.icon_register_head);
            imageWorker.loadImage(task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            imageHead.setImageResource(R.drawable.icon_register_head);
        }

        layoutNickname = findViewById(R.id.layoutNickname);
        layoutSex = findViewById(R.id.layoutSex);
        layoutCity = findViewById(R.id.layoutCity);
        txtNickname = (EditText) findViewById(R.id.txtNickname);
        txtNickname.setText(getApplicationContext().getUser().getNickname());
        txtSex = (TextView) findViewById(R.id.txtSex);
        txtSex.setText(getApplicationContext().getUser().getSex());
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtCity.setText(getApplicationContext().getUser().getDistrict_name());
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
                String imagePath = getApplicationContext().getUser()
                        .getAvatarbig();
                if (!isNull(imagePath)) {
                    mView = new ShowLargeImageView(mContext,
                            findViewById(R.id.father));
                    mView.show();
                    mView.setImageURL(imagePath);
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

    /**
     * 提交
     */
    private void clickConfirm() {
        String nickname, sex, district;
        nickname = txtNickname.getEditableText().toString().trim();
        sex = txtSex.getText().toString().trim();
        district = txtCity.getText().toString().trim();
        if (isNull(nickname)) {
            showMyOneButtonDialog(R.string.personal_data,
                    R.string.error_nickname_empty);
            return;
        }
        if (nickname.length() > 8) {
            showMyOneButtonDialog(getResources().getString(R.string.personal_data), "请确认昵称长度不超过8位");
            return;
        }
        saveData = true;
        getNetWorker().clientSave(getApplicationContext().getUser().getToken(),
                nickname, sex, district, "0", getApplicationContext().getUser().getLike(), getApplicationContext().getUser().getMajor());

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
            phptpDialog.setButtonListener(new MyBottomThreeButtonDialog.OnButtonListener() {
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
//                            getImageFromAlbum();// 相册获取
                    imageWay.album();
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
     * 从相册获取图片
     */
    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 上传图片失败的后续处理
     */
    private void UploadImageFailed() {
        if (failedDialog == null) {
            failedDialog = new MyOneButtonDialog(mContext);
            failedDialog.setTitle(R.string.personal_data);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case INPUT_NICKNAME:
                String nickname = data.getStringExtra("content");
                txtNickname.setText(nickname);
                break;
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
                getNetWorker().fileUpload(
                        getApplicationContext().getUser().getToken(), "1", "0",
                        "0", "0", "无", tempPath);
                break;
            default:
                break;
        }
    }
}
