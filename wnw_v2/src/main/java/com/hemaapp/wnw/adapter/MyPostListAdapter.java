package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.PostCommentActivity;
import com.hemaapp.wnw.activity.PostDetailActivity;
import com.hemaapp.wnw.activity.ReplyActivity;
import com.hemaapp.wnw.activity.business.SendPostActivity;
import com.hemaapp.wnw.fragment.MyPostListFragment;
import com.hemaapp.wnw.model.NoteList;

import java.util.ArrayList;

/**
 * 我的帖子列表适配器
 * Created by HuHu on 2016-08-16.
 */
public class MyPostListAdapter extends MyAdapter {
    private ArrayList<NoteList> listData = new ArrayList<>();
    private MyFragment fragment;

    public MyPostListAdapter(Context mContext, MyFragment fragment, ArrayList<NoteList> listData) {
        super(mContext);
        this.fragment = fragment;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size() == 0 ? 1 : listData.size();
    }

    @Override
    public boolean isEmpty() {
        return listData.size() == 0;
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
        if (isEmpty() && showProgress) {
            return showProgessView(parent);
        } else if (isEmpty()) {
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_my_post_send, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(position, holder);
        return convertView;
    }

    private void setData(final int position, ViewHolder holder) {
        final NoteList note = listData.get(position);
        holder.txtAction.setText(note.getTitle());
        if ("1".equals(note.getGood())) {//	1.普通
            holder.txtContent.setText(note.getContent());
        } else {//2.精华
            holder.txtContent.setText(Html.fromHtml("<font color=#e9161c>[精华]</font><font>"
                    + note.getContent()
                    + "</font>"));
        }
        holder.txtWatchCount.setText(note.getVisitcount() + "浏览");
        holder.txtReplyCount.setText(note.getReplycount());
        holder.txtGoodCount.setText(note.getFlag() + "赞");

        Glide.with(MyApplication.getInstance()).load(note.getImgurlbig()).placeholder(R.drawable.image_main_temp).into(holder.imageBackground);
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPostListFragment postListFragment = (MyPostListFragment) fragment;
                postListFragment.showBottomThreeButtonDialog(position);
            }
        });
        holder.txtDate.setText(note.getUpdateDiff());
        holder.txtRefresh.setOnClickListener(new View.OnClickListener() {//1.更新帖子
            @Override
            public void onClick(View v) {
                MyPostListFragment postListFragment = (MyPostListFragment) fragment;
                postListFragment.noteOperate("1", position);
            }
        });
        holder.txtEdit.setOnClickListener(new View.OnClickListener() {//编辑
            @Override
            public void onClick(View v) {
                MyActivity activity = (MyActivity) mContext;
                MyPostListFragment postListFragment = (MyPostListFragment) fragment;
                postListFragment.noteOperate("2", position);
                Intent intent = new Intent(activity, SendPostActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                activity.startActivity(intent);
                activity.changeAnim();
            }
        });
        holder.txtAddGoods.setOnClickListener(new View.OnClickListener() {//3.添加商品
            @Override
            public void onClick(View v) {
                if (addGoodsListener != null) {
                    addGoodsListener.addGoods(position);
                }
            }
        });
        holder.imageBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivity activity = (MyActivity) mContext;
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("id", note.getId());
                activity.startActivity(intent);
                activity.changeAnim();
            }
        });
        holder.imageReplyCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivity activity = (MyActivity) mContext;
                Intent intent = new Intent(mContext, PostCommentActivity.class);
                intent.putExtra("id", note.getId());
                intent.putExtra("client_id", note.getClient_id());
                activity.startActivity(intent);
                activity.changeAnim();
            }
        });
    }

    private class ViewHolder {
        private TextView txtDelete, txtEdit, txtAction, txtContent, txtRefresh, txtAddGoods,
                txtDate, txtWatchCount, txtGoodCount, txtReplyCount;
        private ImageView imageBackground, imageReplyCount;

        public ViewHolder(View convertView) {
            txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
            txtEdit = (TextView) convertView.findViewById(R.id.txtEdit);
            txtAction = (TextView) convertView.findViewById(R.id.txtAction);
            txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            txtRefresh = (TextView) convertView.findViewById(R.id.txtRefresh);
//            txtRecommend = (TextView) convertView.findViewById(R.id.txtRecommend);
            txtAddGoods = (TextView) convertView.findViewById(R.id.txtAddGoods);
            txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            txtWatchCount = (TextView) convertView.findViewById(R.id.txtWatchCount);
            txtGoodCount = (TextView) convertView.findViewById(R.id.txtGoodCount);
            txtReplyCount = (TextView) convertView.findViewById(R.id.txtReplyCount);
            imageBackground = (ImageView) convertView.findViewById(R.id.imageBackground);
            imageReplyCount = (ImageView) convertView.findViewById(R.id.imageReplyCount);
        }
    }

    public void setAddGoodsListener(AddGoodsListener addGoodsListener) {
        this.addGoodsListener = addGoodsListener;
    }

    private AddGoodsListener addGoodsListener;

    public interface AddGoodsListener {
        void addGoods(int position);
    }
}
