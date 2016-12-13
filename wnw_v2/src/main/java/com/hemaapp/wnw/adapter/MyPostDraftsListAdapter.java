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
import com.hemaapp.wnw.activity.PostDetailActivity;
import com.hemaapp.wnw.activity.business.SendPostActivity;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.fragment.MyPostListFragment;
import com.hemaapp.wnw.model.NoteList;

import java.util.ArrayList;

/**
 * 我的帖子草稿箱列表适配器
 * Created by HuHu on 2016-09-01.
 */
public class MyPostDraftsListAdapter extends MyAdapter {
    private ArrayList<NoteList> listData = new ArrayList<>();
    private MyFragment fragment;

    public MyPostDraftsListAdapter(Context mContext, MyFragment fragment, ArrayList<NoteList> listData) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_my_post_send_drafts, null, false);
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
        Glide.with(MyApplication.getInstance()).load(note.getImgurlbig()).placeholder(R.drawable.image_main_temp).into(holder.imageBackground);
        holder.txtDate.setText(note.getUpdateDiff());
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {//删除
            @Override
            public void onClick(View v) {
                MyTwoButtonDialog dialog = new MyTwoButtonDialog(mContext);//要显示框框确认，这里就真的删了
                dialog.setText("确定要删除吗？").setTitle("我的帖子").setLeftButtonText("确定").setRightButtonText("取消");
                dialog.setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
                    @Override
                    public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                    }

                    @Override
                    public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                        MyPostListFragment postListFragment = (MyPostListFragment) fragment;
                        postListFragment.noteOperate("5", position);
                    }

                    @Override
                    public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                        twoButtonDialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        holder.txtEdit.setOnClickListener(new View.OnClickListener() {//编辑帖子
            @Override
            public void onClick(View v) {
                MyFragmentActivity activity = (MyFragmentActivity) mContext;
                Intent intent = new Intent(activity, SendPostActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                activity.startActivity(intent);
                activity.changeAnim();
            }
        });
        holder.txtPublic.setOnClickListener(new View.OnClickListener() {//发布帖子
            @Override
            public void onClick(View v) {
                MyPostListFragment postListFragment = (MyPostListFragment) fragment;
                postListFragment.noteOperate("6", position);
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
    }

    private class ViewHolder {
        private TextView txtAction, txtContent, txtDelete, txtEdit, txtPublic, txtDate;
        private ImageView imageBackground;

        public ViewHolder(View convertView) {
            txtAction = (TextView) convertView.findViewById(R.id.txtAction);
            txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
            txtEdit = (TextView) convertView.findViewById(R.id.txtEdit);
            txtPublic = (TextView) convertView.findViewById(R.id.txtPublic);
            txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            imageBackground = (ImageView) convertView.findViewById(R.id.imageBackground);
        }
    }
}
