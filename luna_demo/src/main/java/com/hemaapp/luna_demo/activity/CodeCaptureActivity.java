package com.hemaapp.luna_demo.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hemaapp.luna_demo.R;
import com.hemaapp.luna_demo.zxing.camera.CameraManager;
import com.hemaapp.luna_demo.zxing.decoding.CaptureActivityHandler;
import com.hemaapp.luna_demo.zxing.decoding.InactivityTimer;
import com.hemaapp.luna_demo.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by HuHu on 2016/4/17.
 */
public class CodeCaptureActivity extends Activity implements SurfaceHolder.Callback {
    private ViewfinderView viewFinderView;
    private CaptureActivityHandler handler;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        viewFinderView = (ViewfinderView) findViewById(R.id.viewFinderView);
        CameraManager.init(getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
        }

    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }


    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        String resultString = result.getText();
        if (resultString.equals("")) {
            Log.e("sqCode", "扫描失败");
        } else {
            Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show();
            ReStartCode();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            Log.e("initCamera: ", e.getMessage());
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    public ViewfinderView getViewfinderView() {
        return viewFinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewFinderView.drawViewfinder();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 重新开始扫描
     */
    private void ReStartCode() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
        onResume();
    }
}
