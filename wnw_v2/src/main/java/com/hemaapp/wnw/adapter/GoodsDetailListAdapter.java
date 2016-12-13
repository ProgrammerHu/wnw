package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hemaapp.luna_framework.view.MyWebView;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.activity.MyShowLargePicActivity;
import com.hemaapp.wnw.model.Image;
import com.hemaapp.wnw.model.ReplyCommentModel;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * 商品详情列表适配器
 * Created by Hufanglin on 2016/2/24.
 */
public class GoodsDetailListAdapter extends MyAdapter {

    private XtomListView listView;
    private MyFragmentActivity activity;
    private String id;// 商品id

    private boolean IsLeft = true;
    private View LeftView;

    private String totalCount;//评论总数
    private float level;//商品星级
    private ArrayList<ReplyCommentModel> replyList = new ArrayList<>();

    /**
     * 设置评论数
     *
     * @param totalCount
     */
    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 设置星星数
     *
     * @param level
     */
    public void setLevel(float level) {
        this.level = level;
    }

    public GoodsDetailListAdapter(Context mContext, XtomListView listView,
                                  String id, ArrayList<ReplyCommentModel> replyList) {
        super(mContext);
        this.activity = (MyFragmentActivity) mContext;
        this.listView = listView;
        this.id = id;
        this.replyList = replyList;
        IsLeft = true;
    }

    @Override
    public int getCount() {
        if (IsLeft)
            return 1;
        else {
            int count = replyList == null ? 0 : replyList.size();
            return count + 1;
        }
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
        if (position == 0) {
            if (IsLeft) {// 左侧显示WebView
                // if (LeftView == null)
                {
                    LeftView = LayoutInflater.from(mContext).inflate(
                            R.layout.webview_only, null, false);
                    MyWebView webView = (MyWebView) LeftView
                            .findViewById(R.id.webView);
                    String path = activity.getApplicationContext()
                            .getSysInitInfo().getSys_plugins()
                            + "share/writing.php?id=" + id;
                    webView.loadUrl(path);
                    webView.setOnMultiImageClickListener(new MyWebView.OnMultiImageClickListener() {
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
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                            } catch (Exception e) {
                                Intent intent = new Intent(mContext, MyShowLargePicActivity.class);
                                intent.putExtra("position", 0);
                                intent.putExtra("images", imagesList);
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                            }

                        }
                    });
                }
                return LeftView;
            } else {// 右侧第一行显示标题
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.listitem_comment_top, null, false);
                ViewHolderTop holder = new ViewHolderTop(convertView);
                holder.txtCommentCount.setText(totalCount);
                holder.ratingBar.setRating(level);
            }
        } else {
            ViewHolderRight holder;
            if (convertView == null
                    || convertView.getTag(R.id.TAG_VIEWHOLDER) == null
                    || !(convertView.getTag(R.id.TAG_VIEWHOLDER) instanceof ViewHolderRight)) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.listitem_comment, null, false);
                holder = new ViewHolderRight(convertView);
                convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
            } else {
                holder = (ViewHolderRight) convertView
                        .getTag(R.id.TAG_VIEWHOLDER);
            }

            setData(holder, position);
            setListener(holder, position);

        }
        return convertView;
    }

    private class ViewHolderTop {
        public ViewHolderTop(View convertView) {
            txtCommentCount = (TextView) convertView
                    .findViewById(R.id.txtCommentCount);
            ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        }

        private TextView txtCommentCount;
        private RatingBar ratingBar;

    }

    private class ViewHolderRight {
        public ViewHolderRight(View convertView) {
            imageHead = (ImageView) convertView.findViewById(R.id.imageHead);
            imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
            imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
            imageView3 = (ImageView) convertView.findViewById(R.id.imageView3);
            imageView4 = (ImageView) convertView.findViewById(R.id.imageView4);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            imageViews.add(imageView1);
            imageViews.add(imageView2);
            imageViews.add(imageView3);
            imageViews.add(imageView4);
        }

        private ImageView imageHead, imageView1, imageView2, imageView3,
                imageView4;
        private TextView txtName, txtDate, txtContent;
        private RatingBar ratingBar;
        private ArrayList<ImageView> imageViews = new ArrayList<>();
    }

    private void setData(ViewHolderRight holder, final int position) {//设置数据，首行不会执行
        final int realPosition = position - 1;
        if (realPosition < 0 || realPosition >= replyList.size()) {//越界直接返回
            return;
        }
        final ReplyCommentModel reply = replyList.get(realPosition);
        holder.txtName.setText(reply.getNickname());
        holder.txtDate.setText(reply.getTimeDiff());
        holder.txtContent.setText(reply.getContent());
        try {
            float rating = Float.parseFloat(reply.getReplytype());
            holder.ratingBar.setRating(rating);
        } catch (Exception e) {
            holder.ratingBar.setRating(0);
        }

        /*加载图片*/
        int orderby = 0;
        try {
            URL headUrl = new URL(reply.getAvatar());
            ImageTask task = new ImageTask(holder.imageHead, headUrl, mContext, R.drawable.icon_register_head);
            listView.addTask(position, orderby, task);
            orderby++;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            holder.imageHead.setImageResource(R.drawable.icon_register_head);
        }
        int childImgsCount = reply.getImgItems().size();
        for (int i = 0; i < holder.imageViews.size(); i++) {
            if (i < childImgsCount) {//有图片，需要加载图片
                holder.imageViews.get(i).setVisibility(View.VISIBLE);

                try {
                    URL url = new URL(reply.getImgItems().get(i).getImgurl());
                    ImageTask task = new ImageTask(holder.imageViews.get(i), url, mContext, R.drawable.logo_default_square);
                    listView.addTask(position, orderby, task);
                    orderby++;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    holder.imageViews.get(i).setImageResource(R.drawable.logo_default_square);
                }
                final int index = i;
                holder.imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//点击图片看大图
                        Intent sIt = new Intent(mContext, MyShowLargePicActivity.class);
                        sIt.putExtra("position", index);
                        sIt.putExtra("images", reply.getImgItems());
                        activity.startActivity(sIt);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                    }
                });
            } else {//没有图片，隐藏ImageView即可
                holder.imageViews.get(i).setVisibility(View.GONE);
            }
        }
        /*加载图片结束*/
    }

    private void setListener(ViewHolderRight holder, int position) {

    }

    /**
     * 切换到左侧WebView
     */
    public void changeToLeft() {
        IsLeft = true;
        notifyDataSetChanged();
    }

    /**
     * 切换到右侧列表
     */
    public void changeToRight() {
        IsLeft = false;
        notifyDataSetChanged();
    }

}
