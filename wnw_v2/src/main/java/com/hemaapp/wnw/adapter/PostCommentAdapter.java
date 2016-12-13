package com.hemaapp.wnw.adapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.media.Image;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.PostCommentActivity;
import com.hemaapp.wnw.model.ReplyCommentModel;
import com.hemaapp.wnw.nettask.ImageTask;

import xtom.frame.view.XtomListView;

/**
 * 帖子回复适配器
 * Created by Hufanglin on 2016/2/26.
 */
public class PostCommentAdapter extends MyAdapter {

    private XtomListView listView;
    private List<ReplyCommentModel> listData;

    public PostCommentAdapter(Context mContext, XtomListView listView,
                              List<ReplyCommentModel> listData) {
        super(mContext);
        this.listView = listView;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        int count = listData == null ? 0 : listData.size();
        return count == 0 ? 1 : count;
    }

    @Override
    public boolean isEmpty() {
        int count = listData == null ? 0 : listData.size();
        return count == 0;
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
        if (isEmpty()) {
            setEmptyString("快来抢沙发啦");
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listitem_post_comment, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        public ViewHolder(View convertView) {
            imageHead = (ImageView) convertView.findViewById(R.id.imageHead);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtTimeDiff = (TextView) convertView.findViewById(R.id.txtTimeDiff);
            txtCotent = (TextView) convertView.findViewById(R.id.txtCotent);
            txtReply = (TextView) convertView.findViewById(R.id.txtReply);
            layoutReply = convertView.findViewById(R.id.layoutReply);
            imageDelete = (ImageView) convertView.findViewById(R.id.imageDelete);
        }

        private ImageView imageHead, imageDelete;
        private View layoutReply;
        private TextView txtName, txtTimeDiff, txtCotent, txtReply;
    }

    /**
     * 充实数据
     *
     * @param holder
     * @param position
     */
    private void setData(ViewHolder holder, final int position) {
        ReplyCommentModel reply = listData.get(position);
        holder.txtName.setText(reply.getNickname());
        holder.txtTimeDiff.setText(reply.getTimeDiff());
        holder.txtCotent.setText(reply.getContent());
        int visible = isNull(reply.getReply()) ? View.GONE : View.VISIBLE;
        holder.layoutReply.setVisibility(visible);
        MyActivity activity = (MyActivity) mContext;
        if (MyUtil.IsLogin(activity)) {
            int deleteVisible = reply.getClient_id().equals(MyApplication.getInstance().getUser().getId()) ? View.VISIBLE : View.GONE;
            holder.imageDelete.setVisibility(deleteVisible);
        } else {
            holder.imageDelete.setVisibility(View.GONE);
        }

        try {
            URL url = new URL(reply.getAvatar());
            ImageTask task = new ImageTask(holder.imageHead, url, mContext, R.drawable.icon_register_head);
            listView.addTask(position, 0, task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            holder.imageHead.setImageResource(R.drawable.icon_register_head);
        }

        holder.txtReply.setText(getClickableSpan(reply));
        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCommentActivity activity = (PostCommentActivity) mContext;
                activity.deleteReply(position);
            }
        });

    }

    private SpannableString getClickableSpan(ReplyCommentModel model) {
        String header = model.getReply_name() + " 回复 " + model.getNickname() + ":";
        String content = header + model.getReply();
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimaryLight)), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)), model.getReply_name().length() + 1, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimaryLight)), model.getReply_name().length() + 4, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black_light)), header.length(), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
