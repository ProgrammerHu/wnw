package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.luna_framework.view.MyWebView;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.model.Image;

import java.util.ArrayList;

/**
 * WebView
 * Created by Hufanglin on 2016/2/20.
 */
public class WebviewActivity extends MyActivity {

    private String Baidu = "https://www.baidu.com/";
    private MyWebView webview;
    private ImageView imageQuitActivity;
    private TextView txtTitle;

    private String Title;
    private String URL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_webview);
        super.onCreate(savedInstanceState);
//        webview.getSettings().setJavaScriptEnabled(true);
        if (isNull(URL))
            webview.loadUrl(Baidu);
        else
            webview.loadUrl(URL);
        // 添加js交互接口类，并起别名 imagelistner
//        webview.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
//        webview.setWebViewClient(new MyWebViewClient());
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
    protected void findView() {
        webview = (MyWebView) findViewById(R.id.webview);
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(Title);

    }

    @Override
    protected void getExras() {
        Title = mIntent.getStringExtra("Title");
        URL = mIntent.getStringExtra("URL");

    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish(R.anim.my_left_in, R.anim.right_out);
            }
        });
        webview.setOnMultiImageClickListener(new MyWebView.OnMultiImageClickListener() {
            @Override
            public void clickMultiImage(String position, String images) {
                String[] imagesArray = images.split(";");
                ArrayList<Image> imagesList = new ArrayList<>();
                int i = 0;
                for (String image : imagesArray) {
                    imagesList.add(new Image("", "", "", "", image, image, String.valueOf(i++)));
                }
                try {
                    int index = Integer.parseInt(position);
                    Intent intent = new Intent(mContext, MyShowLargePicActivity.class);
                    intent.putExtra("position", index);
                    intent.putExtra("images", imagesList);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                } catch (Exception e) {
                    Intent intent = new Intent(mContext, MyShowLargePicActivity.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("images", imagesList);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                }

            }
        });
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    @Override
    protected void onResume() {
        webview.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        webview.onPause();
        super.onPause();
    }

    private ShowLargeImageView mView;// 展示大图

    private void showLargeView(String img) {

        mView = new ShowLargeImageView(mContext,
                findViewById(R.id.father));
        mView.setImageURL(img);
        mView.show();
    }
}
