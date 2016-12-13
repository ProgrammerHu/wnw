package com.hemaapp.wnw.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaImageWay;
import com.hemaapp.luna_framework.album.AlbumActivity;
import com.hemaapp.luna_framework.dialog.MyBottomThreeButtonDialog;
import com.hemaapp.wnw.MyActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;

/**
 * 需要选择图片的界面
 * Created by Hufanglin on 2016/3/18.
 */
public abstract class SelectImageEditActivity extends MyActivity {
//    protected final int MAX_COUNT = 4;//最大图片数量

    protected final int REQUEST_CODE_PICK_IMAGE = 1;//相册获取
    protected final int REQUEST_CODE_CAPTURE_CAMEIA = 2;//相机获取
    protected final int EDIT_IMAGE = 3;//编辑照片

    protected MyBottomThreeButtonDialog selectImageDialog;
    protected ArrayList<String> images = new ArrayList<>();
    protected HemaImageWay imageWay;
    protected String imagePathCamera;//相机拍照路径
    protected String tempPath;//裁剪的临时路径

    private boolean isFixedSize = true;//标记是否固定尺寸

    protected void changeFixedSize(boolean isFixedSize) {
        this.isFixedSize = isFixedSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null || isNull(savedInstanceState.getString("imagePathCamera"))) {
            imageWay = new HemaImageWay(mContext, 1, 2) {
                public void album() {
                    Intent it1 = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (mContext != null)
                        mContext.startActivityForResult(it1, albumRequestCode);

                }
            };
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
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageWay != null) {
            outState.putString("imagePathCamera", imageWay.getCameraImage());
            imagePathCamera = imageWay.getCameraImage();
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * 显示选择图片的dialog
     */
    public void showSelectImageDialog() {
        if (selectImageDialog == null) {
            selectImageDialog = new MyBottomThreeButtonDialog(mContext);
            selectImageDialog.setTopButtonText("拍照");
            selectImageDialog.setMiddleButtonText("从相册选择");
            selectImageDialog.setButtonListener(new MyBottomThreeButtonDialog.OnButtonListener() {
                @Override
                public void onTopButtonClick(MyBottomThreeButtonDialog dialog) {
                    dialog.cancel();
                    imageWay.camera();
                }

                @Override
                public void onMiddleButtonClick(MyBottomThreeButtonDialog dialog) {
                    dialog.cancel();
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);
                    } else {
                        imageWay.album();//相册获取
                    }
                }
            });
        }
        selectImageDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            imageWay.album();//相册获取
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_CODE_CAPTURE_CAMEIA://相机的
                camera();
                break;
            case REQUEST_CODE_PICK_IMAGE://相册的
                album(data);
                break;
            case EDIT_IMAGE://剪裁
                log_e(tempPath);
                if (isFixedSize) {
                    addNewImage(tempPath);
                } else {
                    new CompressPicTask().execute(tempPath);
                }
//                images.add(tempPath);
//                refreshAdapter();
                break;
        }
    }

    /**
     * 调用相机的后续处理
     */
    private void camera() {
        String imagepath = imageWay.getCameraImage();
        if (!isNull(imagepath))
            imagePathCamera = imagepath;
        editImage(imagePathCamera, EDIT_IMAGE);
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
        if (isFixedSize) {
            startPhotoZoomFixedSize(selectedImageUri, EDIT_IMAGE);
        } else {
            startPhotoZoom(selectedImageUri, EDIT_IMAGE);
        }

    }
    /* 处理图片开始 */

    private void editImage(String path, int requestCode) {
        File file = new File(path);
        if (isFixedSize) {
            startPhotoZoomFixedSize(Uri.fromFile(file), requestCode);
        } else {
            startPhotoZoom(Uri.fromFile(file), requestCode);
        }
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
//        intent.putExtra("aspectX", MyConfig.IMAGE_SIZE);
//        intent.putExtra("aspectY", MyConfig.IMAGE_SIZE / 2);
        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", MyConfig.IMAGE_SIZE);
//        intent.putExtra("outputY", MyConfig.IMAGE_SIZE / 2);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, requestCode);
    }

    private void startPhotoZoomFixedSize(Uri uri, int requestCode) {
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
        intent.putExtra("aspectX", MyConfig.IMAGE_SIZE);
        intent.putExtra("aspectY", MyConfig.IMAGE_SIZE * 330 / 640);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", MyConfig.IMAGE_SIZE);
        intent.putExtra("outputY", MyConfig.IMAGE_SIZE * 330 / 640);
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
     * 压缩图片片
     */
    private int CompressPicIndex = 0;

    private class CompressPicTask extends AsyncTask<String, Void, Integer> {
        String compressPath;

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String path = params[0];
                String savedir = XtomFileUtil.getTempFileDir(mContext);
                compressPath = XtomImageUtil.compressPictureWithSaveDir(path,
                        MyConfig.IMAGE_SIZE, MyConfig.IMAGE_SIZE,
                        MyConfig.IMAGE_QUALITY, savedir, mContext);
                return 0;
            } catch (Exception e) {
                return 1;
            }
        }

        @Override
        protected void onPreExecute() {
            if (CompressPicIndex <= 1)
                showProgressDialog("正在压缩图片");
        }

        @Override
        protected void onPostExecute(Integer result) {
            CompressPicIndex--;
            if (CompressPicIndex <= 0) {
                cancelProgressDialog();
            }
            switch (result) {
                case 0:
//                    images.add(compressPath);
//                    refreshAdapter();
                    addNewImage(compressPath);
                    break;
                case 1:
                    showTextDialog("图片压缩失败");
                    cancelProgressDialog();
                    break;
            }
        }
    }

    /**
     * 更新适配器的方法
     */
    protected abstract void refreshAdapter();

    protected abstract void addNewImage(String path);
    /*选择自拍照的相关方法END*/

    /**
     * 根据URI获取路径
     *
     * @param uri
     * @return
     */
    private String getRealFilePath(final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = mContext.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
