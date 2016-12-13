package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.wnw.R;
import com.hemaapp.wnw.dialog.GoodsSelectDialog;
import com.hemaapp.wnw.model.GoodsDetailModel;
import com.hemaapp.wnw.view.FlowLayout.FlowLayout;
import com.hemaapp.wnw.view.FlowLayout.TagAdapter;

import java.util.List;

/**
 * 选择商品参数的适配器
 * Created by Hufanglin on 2016/2/25.
 */
public class BuyTagAdapter extends TagAdapter<GoodsDetailModel.AttrChildItemsEntity> {
    private Context mContext;
    private GoodsSelectDialog dialog;

    public BuyTagAdapter(List<GoodsDetailModel.AttrChildItemsEntity> datas, Context mContext, GoodsSelectDialog dialog) {
        super(datas);
        datas.get(0).setIs_selected(true);
        this.mContext = mContext;
        this.dialog = dialog;
    }

    @Override
    public View getView(FlowLayout parent, int position, final GoodsDetailModel.AttrChildItemsEntity attrChildItemsEntity) {
        final TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_textview, parent, false);
        textView.setText(getItem(position).getName());
        int textColor = attrChildItemsEntity.getIs_selected() ? R.color.white : R.color.black_light;
        if (attrChildItemsEntity.getIs_selected()) {//已选中
            textView.setTextColor(mContext.getResources().getColor(R.color.white));
            textView.setBackgroundColor(mContext.getResources().getColor(R.color.main_purple));
        } else {
            textView.setTextColor(mContext.getResources().getColor(R.color.black_light));
            textView.setBackgroundResource(R.drawable.bg_white_border);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//加个选择状态标记一下就好了
                if (attrChildItemsEntity.getIs_selected()) {
                    attrChildItemsEntity.setIs_selected(false);
                    notifyDataChanged();//通知适配器变化
                    return;
                }
                for (GoodsDetailModel.AttrChildItemsEntity attr : mTagDatas) {
                    attr.setIs_selected(false);
                }
                attrChildItemsEntity.setIs_selected(true);
                notifyDataChanged();//通知适配器变化
                dialog.changePrice();//检索价格
            }
        });
        return textView;
    }
}
