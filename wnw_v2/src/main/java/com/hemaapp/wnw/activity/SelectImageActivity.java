package com.hemaapp.wnw.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import java.util.ArrayList;

import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;

/**
 * 需要选择图片的界面
 * Created by Hufanglin on 2016/3/18.
 */
public abstract class SelectImageActivity extends MyActivity {
    protected final int MAX_COUNT = 4;//最大图片数量

    protected final int REQUEST_CODE_PICK_IMAGE = 1;//相册获取
    protected final int REQUEST_CODE_CAPTURE_CAMEIA = 2;//相机获取
//    protected final int EDIT_IMAGE = 3;//编辑照片

    protected MyBottomThreeButtonDialog selectImageDialog;
    protected ArrayList<String> images = new ArrayList<String>();
    protected HemaImageWay imageWay;
    protected String imagePathCamera;//相机拍照路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null || isNull(savedInstanceState.getString("imagePathCamera"))) {
            imageWay = new HemaImageWay(mContext, REQUEST_CODE_PICK_IMAGE, REQUEST_CODE_CAPTURE_CAMEIA) {
                public void album() {
                    Intent intent = new Intent(mContext, AlbumActivity.class);
                    intent.putExtra("limitCount", MAX_COUNT - images.size());
                    if (mContext != null)
                        mContext.startActivityForResult(intent, albumRequestCode);

                }
            };
        } else {
            imagePathCamera = savedInstanceState.getString("imagePathCamera");
            imageWay = new HemaImageWay(mContext, REQUEST_CODE_PICK_IMAGE, REQUEST_CODE_CAPTURE_CAMEIA) {
                public void album() {
                    Intent intent = new Intent(mContext, AlbumActivity.class);
                    intent.putExtra("limitCount", MAX_COUNT - images.size());
                    if (mContext != null)
                        mContext.startActivityForResult(intent, albumRequestCode);

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
        }
    }

    /**
     * 调用相机的后续处理
     */
    private void camera() {
        String imagepath = imageWay.getCameraImage();
        if (!isNull(imagepath)) {
            imagePathCamera = imagepath;
            new CompressPicTask().execute(imagePathCamera);
        }
    }

    /**
     * 通过相册获取图片的后续处理
     *
     * @param data
     */
    private void album(Intent data) {
        if (data == null) {
            return;
        }
        ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra("images");
        if (images == null || images.size() == 0) {
            return;
        }
        CompressPicIndex = 0;
        for (String path : images) {
            new CompressPicTask().execute(path);
            CompressPicIndex++;
        }
//        Uri selectedImageUri = data.getData();
//        new CompressPicTask().execute(getRealFilePath(selectedImageUri));
        // 获取图片路径
        /*String[] proj = {MediaStore.Images.Media.DATA};
        final CursorLoader loader = new CursorLoader(mContext);
        loader.setUri(selectedImageUri);
        loader.setProjection(proj);
        loader.registerListener(0, new Loader.OnLoadCompleteListener<Cursor>() {

            @Override
            public void onLoadComplete(Loader<Cursor> arg0, Cursor cursor) {
                int columnIndex = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String imagepath = cursor.getString(columnIndex);
//                editImage(imagepath, 3);
//                showTextDialog(imagepath);
                new CompressPicTask().execute(imagepath);
                loader.stopLoading();
                cursor.close();
            }
        });
        loader.startLoading();*/
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
                    images.add(compressPath);
                    refreshAdapter();
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
