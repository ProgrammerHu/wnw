package com.hemaapp.luna_framework.view.Transformer;

import android.view.View;

/**
 * 下层Fragment不动，上层左侧出现
 * Created by HuHu on 2016/3/30.
 */
public class StackTransformer extends ABaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);
    }

}