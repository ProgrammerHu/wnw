package com.hemaapp.luna_demo.activity;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.view.HemaWebView;
import com.hemaapp.luna_demo.R;

/**
 * Created by HuHu on 2016/4/12.
 */
public class WebviewActivity extends Activity {
    private HemaWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (HemaWebView) findViewById(R.id.webView);
        webView.loadUrl(getIntent().getStringExtra("content"));
        webView.addJavascriptInterface(new JsInteration(), "control");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                testMethod(webView);
            }
        });
    }

    private void testMethod(WebView webView) {
        String call = "javascript:sayHello()";

        call = "javascript:alertMessage(\"" + "content" + "\")";

        call = "javascript:toastMessage(\"" + "content" + "\")";

        call = "javascript:sumToJava(1,2)";
        webView.loadUrl(call);

    }

    public class JsInteration {

        @JavascriptInterface
        public void toastMessage(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void onSumResult(int result) {
            Log.i("onSumResult", "onSumResult result=" + result);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
