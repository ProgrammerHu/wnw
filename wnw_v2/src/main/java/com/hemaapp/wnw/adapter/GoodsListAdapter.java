package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.hm_FrameWork.album.ViewHolder;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.activity.PostCommentActivity;
import com.hemaapp.wnw.activity.PostDetailActivity;
import com.hemaapp.wnw.activity.SellerDetailActivity;
import com.hemaapp.wnw.model.GoodsListModel;
import com.hemaapp.wnw.model.NoteList;
import com.hemaapp.wnw.nettask.ImageTask;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import xtom.frame.view.XtomListView;

/**
 * 显示在店铺首页的适配器 Created by Hufanglin on 2016/2/24.
 */
public class GoodsListAdapter extends MyAdapter implements View.OnClickListener {

    private XtomListView listView;
    private SellerDetailActivity activity;

    private List<GoodsListModel> goodsList = new ArrayList<>();// 左侧商品列表
    private List<NoteList> noteList = new ArrayList<NoteList>();// 右侧帖子列表

    public GoodsListAdapter(Context mContext, XtomListView listView,
                            List<GoodsListModel> goodsList, List<NoteList> noteList) {
        super(mContext);
        activity = (SellerDetailActivity) mContext;
        this.listView = listView;
        this.goodsList = goodsList;
        this.noteList = noteList;
    }

    @Override
    public int getCount() {

        return noteList == null ? 0 : noteList.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderRight holder = null;
        if (convertView == null
                || convertView.getTag(R.id.TAG_VIEWHOLDER) == null
                || convertView.getTag(R.id.TAG_VIEWHOLDER) instanceof ViewHolderLeft) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listitem_blog_seller, null, false);
            holder = new ViewHolderRight(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolderRight) convertView
                    .getTag(R.id.TAG_VIEWHOLDER);
        }

        setData(holder, position);
        setListenerRight(holder, position);

        return convertView;

    }

	/* 左侧列表开始 */

    private class ViewHolderLeft {
        public ViewHolderLeft(View convertView) {
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            txtOldPrize = (TextView) convertView.findViewById(R.id.txtOldPrize);
            txtOldPrize.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txtOldPrize.getPaint().setAntiAlias(true);
            layoutFather = convertView.findViewById(R.id.layoutFather);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtHasSell = (TextView) convertView.findViewById(R.id.txtHasSell);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtBuy = (TextView) convertView.findViewById(R.id.txtBuy);
        }

        private ImageView imageView;
        private TextView txtName, txtHasSell, txtPrize, txtOldPrize, txtBuy;
        private View layoutFather;
    }

    private void setData(ViewHolderLeft holder, int position) {
        GoodsListModel goods = goodsList.get(position);
        holder.txtPrize.setText("￥" + goods.getPrice());
        holder.txtOldPrize.setText("￥" + goods.getOldprice());
        holder.txtHasSell.setText("已售" + goods.getPaycount());
        holder.txtName.setText(goods.getName());
        try {
            URL url = new URL(goods.getImgurl());
            ImageTask imageTask = new ImageTask(holder.imageView, url,
                    mContext, R.drawable.logo_default_square);
            listView.addTask(position, 0, imageTask);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.logo_default_square);
        }
    }

    private void setListenerLeft(ViewHolderLeft holder, int position) {
        holder.layoutFather.setTag(R.id.TAG, position);
        holder.layoutFather.setOnClickListener(this);
        holder.txtBuy.setTag(R.id.TAG, position);
        holder.txtBuy.setOnClickListener(this);
    }

	/* 左侧列表结束 */

    /* 右侧列表开始 */
    private class ViewHolderRight {

        public ViewHolderRight(View convertView) {
            imageBackground = (ImageView) convertView
                    .findViewById(R.id.imageBackground);
            imageBackground.getLayoutParams().height = MyUtil.getScreenWidth(mContext) * 330 / 640;
            imageComment = (ImageView) convertView
                    .findViewById(R.id.imageComment);
            imagePraise = (ImageView) convertView
                    .findViewById(R.id.imagePraise);
            txtAction = (TextView) convertView.findViewById(R.id.txtAction);
            txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            txtTimeDiff = (TextView) convertView.findViewById(R.id.txtTimeDiff);
            txtComment = (TextView) convertView.findViewById(R.id.txtComment);
            txtPraise = (TextView) convertView.findViewById(R.id.txtPraise);
        }

        private ImageView imageBackground, imageComment, imagePraise;
        private TextView txtAction, txtContent, txtTimeDiff, txtComment,
                txtPraise;
    }

    private void setData(ViewHolderRight holder, int position) {
        NoteList note = noteList.get(position);
        holder.txtAction.setText(note.getTitle());
        holder.txtTimeDiff.setText(note.getTimeDiff());
        if ("1".equals(note.getGood())) {//	1.普通
            holder.txtContent.setText(note.getContent());
        } else {//2.精华
            holder.txtContent.setText(Html.fromHtml("<font color=#e9161c>[精华]</font><font>"
                    + note.getContent()
                    + "</font>"));
        }
        holder.txtComment.setText(note.getReplycount());
        holder.txtPraise.setText(note.getPraise_count());
        int PraiseResource = "0".equals(note.getFlag()) ? R.drawable.icon_praise
                : R.drawable.icon_praise_cancel;
        holder.imagePraise.setImageResource(PraiseResource);
        Glide.with(MyApplication.getInstance()).load(note.getImgurlbig()).placeholder(R.drawable.image_main_temp).into(holder.imageBackground);

    }

    private void setListenerRight(ViewHolderRight holder, int position) {
        holder.imageBackground.setTag(R.id.TAG, position);
        holder.imageBackground.setOnClickListener(this);
        holder.imageComment.setTag(R.id.TAG, position);
        holder.imageComment.setOnClickListener(this);
        holder.imagePraise.setTag(R.id.TAG, position);
        holder.imagePraise.setOnClickListener(this);
    }

	/* 右侧列表结束 */

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.TAG);
        Intent intent;
        switch (v.getId()) {
            case R.id.layoutFather:
                intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("goods_id", goodsList.get(position).getId());
                intent.putExtra("ShowDialog", false);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.txtBuy:
                intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("goods_id", goodsList.get(position).getId());
                intent.putExtra("ShowDialog", true);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.imageHead:
                intent = new Intent(mContext, SellerDetailActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.imagePraise://点击点赞操作
                NoteList note = noteList.get(position);
                if ("0".equals(note.getFlag())) {
                    if (activity.changePraise(note.getId(), "1")) {
                        note.setFlag("1");
                        int count = Integer.valueOf(noteList.get(position)
                                .getPraise_count()) + 1;
                        note.setPraise_count(String.valueOf(count));
                    }
                } else {
                    if (activity.changePraise(note.getId(), "2")) {
                        note.setFlag("0");
                        int count = Integer.valueOf(noteList.get(position)
                                .getPraise_count()) - 1;
                        note.setPraise_count(String.valueOf(count));
                    }
                }
                notifyDataSetChanged();
                break;
            case R.id.imageComment://点击去评论列表
                intent = new Intent(mContext, PostCommentActivity.class);
                intent.putExtra("id", noteList.get(position).getId());
                intent.putExtra("client_id", noteList.get(position).getClient_id());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.imageBackground://点击去帖子详情
                intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("id", noteList.get(position).getId());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
        }
    }

}
