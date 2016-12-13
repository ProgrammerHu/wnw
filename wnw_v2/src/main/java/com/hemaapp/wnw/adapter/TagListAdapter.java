package com.hemaapp.wnw.adapter;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.MyConfig;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.LoginActivity;
import com.hemaapp.wnw.activity.PostCommentActivity;
import com.hemaapp.wnw.activity.PostDetailActivity;
import com.hemaapp.wnw.activity.SellerDetailActivity;
import com.hemaapp.wnw.activity.TagDetailActivtiy;
import com.hemaapp.wnw.model.NoteList;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import xtom.frame.view.XtomListView;

/**
 * 主页Fragment适配器 Created by Hufanglin on 2016/2/22.
 */
public class TagListAdapter extends MyAdapter implements View.OnClickListener {
    private XtomListView listView;
    private MyActivity activity;
    private List<NoteList> listData;

    // private List<Image>
    public TagListAdapter(MyActivity activity, XtomListView listView,
                          List<NoteList> listData) {
        super(activity);
        this.listView = listView;
        this.activity = activity;
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
            return getEmptyView(parent);
        }
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listitem_mainfragment, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(holder, position);
        setListener(holder, position);
        return convertView;
    }

    private class ViewHolder {

        public ViewHolder(View convertView) {
            imageBackground = (ImageView) convertView
                    .findViewById(R.id.imageBackground);
            imageBackground.getLayoutParams().height = MyUtil.getScreenWidth(mContext) * 330 / 640;
            imageHead = (ImageView) convertView.findViewById(R.id.imageHead);
            imageComment = (ImageView) convertView
                    .findViewById(R.id.imageComment);
            imagePraise = (ImageView) convertView
                    .findViewById(R.id.imagePraise);
            txtAction = (TextView) convertView.findViewById(R.id.txtAction);
            txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtTimeDiff = (TextView) convertView.findViewById(R.id.txtTimeDiff);
            txtComment = (TextView) convertView.findViewById(R.id.txtComment);
            txtPraise = (TextView) convertView.findViewById(R.id.txtPraise);
        }

        private ImageView imageBackground, imageHead, imageComment,
                imagePraise;
        private TextView txtAction, txtContent, txtName, txtTimeDiff,
                txtComment, txtPraise;
    }

    private void setData(ViewHolder holder, int position) {
        NoteList note = listData.get(position);
        holder.txtAction.setText(note.getTitle());
        holder.txtTimeDiff.setText(note.getTimeDiff());
        holder.txtName.setText(note.getNickname());
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
        int index = 0;
//        try {
//            URL urlImgurlbig = new URL(listData.get(position).getImgurlbig());
//            ImageTask imageTask = new ImageTask(holder.imageBackground,
//                    urlImgurlbig, mContext, R.drawable.image_main_temp);
//            listView.addTask(position, index, imageTask);
//            index++;
//        } catch (MalformedURLException e) {
//            holder.imageBackground.setImageResource(R.drawable.image_main_temp);
//            e.printStackTrace();
//        }
//
//        try {
//            URL urlAvatar = new URL(listData.get(position).getAvatar());
//            ImageTask avatarTask = new ImageTask(holder.imageHead, urlAvatar,
//                    mContext, R.drawable.icon_register_head);
//            listView.addTask(position, index, avatarTask);
//        } catch (MalformedURLException e) {
//            holder.imageHead.setImageResource(R.drawable.icon_register_head);
//            e.printStackTrace();
//        }

        Glide.with(MyApplication.getInstance()).load(listData.get(position).getImgurlbig()).placeholder(R.drawable.image_main_temp).into(holder.imageBackground);
        Glide.with(MyApplication.getInstance()).load(listData.get(position).getAvatar()).placeholder(R.drawable.icon_register_head).into(holder.imageHead);


    }

    /**
     * 绑定事件
     *
     * @param holder
     * @param position
     */
    private void setListener(ViewHolder holder, final int position) {

        holder.imageHead.setTag(R.id.TAG, position);
        holder.imageHead.setOnClickListener(this);
        holder.imageBackground.setTag(R.id.TAG, position);
        holder.imageBackground.setOnClickListener(this);
        holder.imageComment.setTag(R.id.TAG, position);
        holder.imageComment.setOnClickListener(this);
        holder.imagePraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteList note = listData.get(position);
                if ("0".equals(note.getFlag())) {
                    if (changePraise(note.getId(), "1")) {
                        note.setFlag("1");
                        int count = Integer.valueOf(listData.get(position)
                                .getPraise_count()) + 1;
                        note.setPraise_count(String.valueOf(count));
                    }
                } else {
                    if (changePraise(note.getId(), "2")) {
                        note.setFlag("0");
                        int count = Integer.valueOf(listData.get(position)
                                .getPraise_count()) - 1;
                        note.setPraise_count(String.valueOf(count));
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.TAG);
        Intent intent;
        switch (v.getId()) {
            case R.id.imageHead:
                intent = new Intent(mContext, SellerDetailActivity.class);
                intent.putExtra("client_id", listData.get(position).getClient_id());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.imageBackground:
                intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.imageComment:
                intent = new Intent(mContext, PostCommentActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                intent.putExtra("client_id", listData.get(position).getClient_id());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
        }
    }

    /**
     * 执行点赞
     *
     * @param keyid
     * @param flag  1.点赞2.取消点赞
     */
    public boolean changePraise(String keyid, String flag) {
        if (MyUtil.IsLogin(activity)) {
            activity.getNetWorker().praiseOperate(
                    activity.getApplicationContext().getUser().getToken(), "2", flag,
                    keyid);
            return true;
        } else {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.putExtra("ActivityType", MyConfig.LOGIN);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
            return false;
        }
    }

}
