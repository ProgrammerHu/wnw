package com.hemaapp.wnw.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.adapter.BuyTagAdapter;
import com.hemaapp.wnw.model.CartListModel;
import com.hemaapp.wnw.model.GoodsDetailModel;
import com.hemaapp.wnw.model.GoodsDetailModel.AtrrItemsEntity;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.wnw.view.FlowLayout.TagFlowLayout;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xtom.frame.XtomObject;

/**
 * 选择商品规格的dialog Created by Hufanglin on 2016/2/25.
 */
public class GoodsSelectDialog extends XtomObject implements
        View.OnClickListener {
    private Context context;
    private GoodsDetailActivity activity;
    private Dialog mDialog;
    private ShowLargeImageView mView;// 展示大图

    private GoodsDetailModel goodsDetailModel;
    private int BuyCount = 1;

    private ImageView imageLess, imageMore, imageView, imageClose;
    private TextView txtCount, txtGoodsName, txtPrize, txtLeftCount;
    private ScrollView scrollView;
    private LinearLayout layoutTag;
    private TagFlowLayout flowLayout;
    private Button btnConfirm;
    private boolean isBuyNow = true;// 标记是否是立即购买

    private int PrivateLeftCount = 1;// 记录每种参数的库存数量

    public GoodsSelectDialog(Context context, GoodsDetailModel goodsDetailModel) {
        this.context = context;
        this.activity = (GoodsDetailActivity) context;
        this.isBuyNow = true;
        this.goodsDetailModel = goodsDetailModel;
        mDialog = new Dialog(context, R.style.custom_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_goods_select, null);
        setData(view);
        setListener();
        setTag();

        mDialog.setCancelable(true);
        mDialog.setContentView(view);
        WindowManager.LayoutParams localLayoutParams = mDialog.getWindow()
                .getAttributes();
        localLayoutParams.gravity = Gravity.BOTTOM;
        localLayoutParams.width = MyUtil.getScreenWidth(context);
        mDialog.onWindowAttributesChanged(localLayoutParams);
    }

    public void show() {
        mDialog.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        mDialog.getWindow().setAttributes(lp);
    }

    public void cancel() {
        mDialog.cancel();
    }

    private void setData(View view) {
        imageLess = (ImageView) view.findViewById(R.id.imageLess);
        imageMore = (ImageView) view.findViewById(R.id.imageMore);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageClose = (ImageView) view.findViewById(R.id.imageClose);
        txtCount = (TextView) view.findViewById(R.id.txtCount);
        txtCount.setText(String.valueOf(BuyCount));
        txtGoodsName = (TextView) view.findViewById(R.id.txtGoodsName);
        txtGoodsName.setText(goodsDetailModel.getName());
        txtPrize = (TextView) view.findViewById(R.id.txtPrize);
        txtPrize.setText("￥" + (goodsDetailModel.getPriceItems().size() > 0 ? goodsDetailModel.getPriceItems().get(0).getPrice() : goodsDetailModel.getPrice()));
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        layoutTag = (LinearLayout) view.findViewById(R.id.layoutTag);
        flowLayout = (TagFlowLayout) view.findViewById(R.id.flowLayout);

        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        txtLeftCount = (TextView) view.findViewById(R.id.txtLeftCount);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) scrollView.getLayoutParams();
        params.height = (int) (MyUtil.getScreenHeight(context) * 0.3);// 固定死ScrollView的高度
        PrivateLeftCount = Integer.parseInt(goodsDetailModel.getLeftcount());
        if (goodsDetailModel.getPriceItems().size() > 0) {
            txtLeftCount.setText("库存" + goodsDetailModel.getPriceItems().get(0).getLeftcount());
        } else {
            txtLeftCount.setText("库存" + goodsDetailModel.getLeftcount());
        }

    }

    /**
     * 绑定事件
     */
    private void setListener() {
        imageLess.setOnClickListener(this);
        imageMore.setOnClickListener(this);
        imageClose.setOnClickListener(this);
        imageView.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageClose:
                cancel();
                break;
            case R.id.imageLess:
                if (BuyCount > 1) {
                    txtCount.setText(String.valueOf(--BuyCount));
                }
                break;
            case R.id.imageMore:
                if (PrivateLeftCount <= 0) {
                    activity.showMyOneButtonDialog("商品详情", "库存不足");
                    break;
                } else if (BuyCount + 1 > PrivateLeftCount) {
                    activity.showMyOneButtonDialog("商品详情", "库存不足");
                    break;
                }
                txtCount.setText(String.valueOf(++BuyCount));
                break;
            case R.id.btnConfirm:
                clickConfirm();
                break;
        }
    }

    /**
     * 点击确定
     */
    private void clickConfirm() {//1、确定是否所有选项都已经选择，2、获取参数id及其加个，3、选择是加入购物车还是立即购买

        /*验证所有选项均已选择*/
        for (GoodsDetailModel.AtrrItemsEntity attrs : goodsDetailModel.getAtrrItems()) {//遍历每种参数
            String selectedParams = "";
            for (GoodsDetailModel.AttrChildItemsEntity attrChild : attrs.getAtrr()) {//遍历每个参数
                if (attrChild.getIs_selected()) {//寻找已经选中的标签并返回
                    if (isNull(selectedParams)) {
                        selectedParams = attrChild.getAttr_id();
                    }
                    break;
                }
            }
            if (isNull(selectedParams)) {
                activity.showMyOneButtonDialog("商品详情", "请选择" + attrs.getName());
                return;
            }
        }
        /*获取参数id*/
        String[] selectedParams = getSelectedParams();
        String rule_id = "";
        for (GoodsDetailModel.PriceItemsEntity priceItemsEntity : goodsDetailModel.getPriceItems()) {
            if (priceItemsEntity.getMix().equals(selectedParams[0]) ||
                    priceItemsEntity.getMix_two().equals(selectedParams[0])) {
                rule_id = priceItemsEntity.getRule_id();
                break;
            }
        }
        if (isNull(rule_id)) {
            activity.showMyOneButtonDialog("商品详情", "库存不足");
            return;
        }

        if (PrivateLeftCount <= 0) {
            activity.showMyOneButtonDialog("商品详情", "库存不足");
            return;
        }
        if (Integer.valueOf(txtCount.getText().toString().trim()) > PrivateLeftCount) {
            activity.showMyOneButtonDialog("商品详情", "库存不足");
            return;
        }
        try {
            if (!isNull(goodsDetailModel.getLimit_count()) && Integer.valueOf(goodsDetailModel.getLimit_count()) < Integer.valueOf(txtCount.getText().toString().trim())) {
                activity.showMyOneButtonDialog("商品详情", "限购" + goodsDetailModel.getLimit_count() + "件");
                return;
            }
        } catch (Exception e) {

        }
        switch (goodsDetailModel.getKeytype()) {//1.正常3.限时4.预售
            case "3"://验证time_end是否结束

                break;
            case "4"://验证
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date now = sdf.parse(goodsDetailModel.getCurr_date());
                    Date sendDate = sdf.parse(goodsDetailModel.getTime());
                    if (sendDate.getTime() < now.getTime()) {
                        activity.showTextDialog("商品已过期");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }


        /*选择是否是立即购买*/
        if (isBuyNow) {
            activity.gotoCheckOrder();
            cancel();
        } else {//添加到购物车
            activity.cartAdd(txtCount.getText().toString(), rule_id);
            cancel();
        }

    }

    /**
     * 设置是否立即购买
     *
     * @param isBuyNow true:立即购买，false:加入购物车
     */
    public void setIsBuyNow(boolean isBuyNow) {
        this.isBuyNow = isBuyNow;
    }

    /**
     * 是否自己购买
     *
     * @return
     */
    public boolean getIsBuyNow() {
        return isBuyNow;
    }

    /**
     * 初始化标签
     */
    private void setTag() {
        for (AtrrItemsEntity attr : goodsDetailModel.getAtrrItems()) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.scrollitem_tag, layoutTag, false);
            BuyTagAdapter adapter = new BuyTagAdapter(attr.getAtrr(), context, GoodsSelectDialog.this);
            TagFlowLayout flowLayout = (TagFlowLayout) view
                    .findViewById(R.id.flowLayout);
            flowLayout.setAdapter(adapter);
            TextView txtTagTitle = (TextView) view.findViewById(R.id.txtTagTitle);
            txtTagTitle.setText(attr.getName());
            layoutTag.addView(view);
        }
    }

    /**
     * 设置右上角的图片
     */
    public void setImageUrl(String imgurl, final String imgurlbig) {
        try {
            URL url = new URL(imgurl);
            ImageTask task = new ImageTask(imageView, url, activity,
                    R.drawable.logo_default_square);
            activity.imageWorker.loadImage(task);
            imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!isNull(imgurlbig)) {
                        cancel();
                        mView = new ShowLargeImageView(activity, activity
                                .findViewById(R.id.father));
                        mView.show();
                        mView.setImageURL(imgurlbig);
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
            imageView.setImageResource(R.drawable.logo_default_square);
        }
    }

    /**
     * 获取选择的参数
     *
     * @return string[0]参数编号，string[1]参数内容
     */
    private String[] getSelectedParams() {
        String[] selectedParams = {"", ""};
        for (GoodsDetailModel.AtrrItemsEntity attrs : goodsDetailModel.getAtrrItems()) {//遍历每种参数
            for (GoodsDetailModel.AttrChildItemsEntity attrChild : attrs.getAtrr()) {//遍历每个参数
                if (attrChild.getIs_selected()) {//寻找已经选中的标签并返回
                    if (isNull(selectedParams[0])) {
                        selectedParams[0] += attrChild.getAttr_id();
                        selectedParams[1] += attrChild.getName();
                    } else {
                        selectedParams[0] += "," + attrChild.getAttr_id();
                        selectedParams[1] += "/" + attrChild.getName();
                    }
                    break;
                }
            }
        }
        return selectedParams;
    }

    /**
     * 获取选择的商品
     *
     * @return
     */
    public CartListModel.ChildItemsEntity getCartGoodsModel() {
        String[] selectedParams = getSelectedParams();
        String rule_id = "";
        String rule = "";
        String price = "";
        String sale_flag = "1";
        String buy_count = txtCount.getText().toString();
        for (GoodsDetailModel.PriceItemsEntity priceItemsEntity : goodsDetailModel.getPriceItems()) {
            if (priceItemsEntity.getMix().equals(selectedParams[0]) ||
                    priceItemsEntity.getMix_two().equals(selectedParams[0])) {
                rule_id = priceItemsEntity.getRule_id();
                rule = selectedParams[1];
                price = priceItemsEntity.getPrice();
                break;
            }
        }
        CartListModel.ChildItemsEntity cartGoods = new CartListModel.ChildItemsEntity(goodsDetailModel.getId(),
                goodsDetailModel.getName(), rule_id, rule, buy_count, "1", goodsDetailModel.getImgurl(), goodsDetailModel.getImgurlbig(),
                price, sale_flag, goodsDetailModel.getExpressfee());

        return cartGoods;
    }

    /**
     * 修改dialog顶部的价格，由标签的适配器调用
     */
    public void changePrice() {
        String[] selectedParams = getSelectedParams();
        for (GoodsDetailModel.PriceItemsEntity priceItemsEntity : goodsDetailModel.getPriceItems()) {
            if (priceItemsEntity.getMix().equals(selectedParams[0]) ||
                    priceItemsEntity.getMix_two().equals(selectedParams[0])) {
                txtPrize.setText("￥" + priceItemsEntity.getPrice());
                txtLeftCount.setText("库存" + priceItemsEntity.getLeftcount());
                PrivateLeftCount = Integer.parseInt(priceItemsEntity.getLeftcount());
                break;
            }
        }
    }


}
