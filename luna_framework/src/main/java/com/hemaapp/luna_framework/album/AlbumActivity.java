package com.hemaapp.luna_framework.album;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaActivity;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.album.ImageFloder;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_framework.R;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * 自定义相册界面
 * Created by HuHu on 2016/4/4.
 */
public class AlbumActivity extends HemaActivity implements NavigationView.OnNavigationItemSelectedListener {
    private GridView gridView;
    private AlbumStaggerAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MenuItem tempItem;
    private ImageView imageView, imageDone;
    private TextView txtTitle;
//    private Toolbar toolBar;
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<>();
    /**
     * 扫描拿到所有的图片文件夹
     */
    private ArrayList<ImageFloder> mImageFloders = new ArrayList<>();
    private int totalCount = 0;
    private int limitCount = 0;
    /**
     * 所有的图片
     */
    private List<String> mImgs = new ArrayList<>();

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            cancelProgressDialog();
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
//            initListDirPopupWindw();

            Menu menu = navigationView.getMenu();
            menu.clear();
            int index = 0;
            int maxPosition = index;
            int maxCount = 0;
            for (ImageFloder imageFloder : mImageFloders) {
                MenuItem item = menu.add(0, index, index,
                        imageFloder.getName() + "(" + imageFloder.getCount() + ")");
                if (imageFloder.getCount() > maxCount) {//寻找图片数量最多的相册予以显示
                    maxCount = imageFloder.getCount();
                    maxPosition = index;
                }
                index++;
            }
            onNavigationItemSelected(menu.getItem(maxPosition));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_album);
        super.onCreate(savedInstanceState);
        getImages();
    }

    @Override
    protected void findView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        gridView = (GridView) findViewById(R.id.gridView);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageDone = (ImageView) findViewById(R.id.imageDone);

    }

    @Override
    protected void getExras() {
        limitCount = mIntent.getIntExtra("limitCount", 0);
    }

    @Override
    protected void setListener() {

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        imageDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (adapter != null) {
                    intent.putExtra("images", adapter.mSelectedImage);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressDialog("正在扫描图片");
        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = AlbumActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    Log.e("TAG", path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    String[] strings = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    });
                    if (strings != null) {
                        int picSize = strings.length;
                        totalCount += picSize;

                        imageFloder.setCount(picSize);
                        mImageFloders.add(imageFloder);

                        if (picSize > mPicsSize) {
                            mPicsSize = picSize;
                            mImgDir = parentFile;
                        }
                    }
                }
                mCursor.close();

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();
    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), "一张图片没扫描到",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        List<File> files = Arrays.asList(mImgDir.listFiles(filefiter));

        Collections.sort(files, new FileComparator());
        mImgs.clear();
        for (File file : files) {
            mImgs.add(file.getPath());
        }
        setAdapter();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//点击列表的操作
        if (tempItem != null) {
            tempItem.setChecked(false);
        }
        item.setChecked(true);
        drawerLayout.closeDrawers();
        int index = item.getItemId();
        String title = mImageFloders.get(index).getName().replace("/", "");//设置toolbar的标题
        txtTitle.setText(title);
        changeData(index);

        tempItem = item;

        return false;
    }

    /**
     * 更换相册数据
     *
     * @param position
     */
    private void changeData(int position) {
        ImageFloder floder = mImageFloders.get(position);
        mImgDir = new File(floder.getDir());
        List<File> files = Arrays.asList(mImgDir.listFiles(filefiter));
        mImgs.clear();
        for (File file : files) {
            mImgs.add(file.getPath());
        }
        setAdapter();
    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new AlbumStaggerAdapter(AlbumActivity.this, mImgs,
                    R.layout.recycleritem_image, mImgDir.getAbsolutePath(), limitCount);
            gridView.setAdapter(adapter);
        } else {
            adapter.setDatas(mImgs);
            adapter.setDirPath(mImgDir.getAbsolutePath());
            adapter.notifyDataSetChanged();

        }
    }


    private class FileComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            if (lhs.lastModified() < rhs.lastModified()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private FileFilter filefiter = new FileFilter() {

        @Override
        public boolean accept(File f) {
            String tmp = f.getName().toLowerCase();
            if (tmp.endsWith(".png") || tmp.endsWith(".jpg")
                    || tmp.endsWith(".jpeg")) {
                return true;
            }
            return false;
        }

    };

    @Override
    protected HemaNetWorker initNetWorker() {
        return null;
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
    public boolean onAutoLoginFailed(HemaNetWorker hemaNetWorker, HemaNetTask hemaNetTask, int i, HemaBaseResult hemaBaseResult) {
        return false;
    }
}
