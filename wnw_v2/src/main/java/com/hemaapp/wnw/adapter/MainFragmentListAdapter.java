package com.hemaapp.wnw.adapter;

import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.PostCommentActivity;
import com.hemaapp.wnw.activity.PostDetailActivity;
import com.hemaapp.wnw.activity.SellerDetailActivity;
import com.hemaapp.wnw.fragment.MainFragment;
import com.hemaapp.wnw.model.NoteList;
import com.hemaapp.wnw.nettask.ImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import xtom.frame.view.XtomListView;

/**
 * 主页Fragment适配器 Created by Hufanglin on 2016/2/22.
 */
public class MainFragmentListAdapter extends MyAdapter implements
        View.OnClickListener {
    private XtomListView listView;
    private MyFragmentActivity fragmentActivity;
    private List<NoteList> listData;
    private MainFragment fragment;
    private int height;

    public MainFragmentListAdapter(MyFragmentActivity fragmentActivity,
                                   XtomListView listView, List<NoteList> listData,
                                   MainFragment fragment) {
        super(fragmentActivity);
        this.listView = listView;
        this.fragmentActivity = fragmentActivity;
        this.listData = listData;
        this.fragment = fragment;
        height = MyUtil.getScreenWidth(mContext) * 330 / 640;
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
            imageBackground.getLayoutParams().height = height;
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
            fatherBackground = convertView.findViewById(R.id.fatherBackground);
            fatherInfo = convertView.findViewById(R.id.fatherInfo);
        }

        private ImageView imageBackground, imageHead, imageComment,
                imagePraise;
        private TextView txtAction, txtContent, txtName, txtTimeDiff,
                txtComment, txtPraise;
        private View fatherBackground, fatherInfo;
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
//                    urlImgurlbig, mContext, holder.fatherBackground, R.drawable.image_main_temp);
//            listView.addTask(position, index, imageTask);
//            Log.e("hehe", "position:" + position + ", index:" + index + ",urlImgurlbig:" + urlImgurlbig);
//            index++;
//        } catch (MalformedURLException e) {
//            holder.imageBackground.setImageResource(R.drawable.image_main_temp);
//            e.printStackTrace();
//        }
        Glide.with(MyApplication.getInstance()).load(listData.get(position).getImgurlbig()).placeholder(R.drawable.image_main_temp).into(holder.imageBackground);
        Glide.with(MyApplication.getInstance()).load(listData.get(position).getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.icon_register_head).into(MyUtil.getCircleImage(mContext, holder.imageHead));

//        try {
//            URL urlAvatar = new URL(listData.get(position).getAvatar());
//            ImageTask avatarTask = new ImageTask(holder.imageHead, urlAvatar,
//                    mContext, holder.fatherInfo, R.drawable.icon_register_head);
//            listView.addTask(position, index, avatarTask);
//            Log.e("hehe", "position:" + position + ", index:" + index + ",urlImgurlbig:" + urlAvatar);
//        } catch (MalformedURLException e) {
//            holder.imageHead.setImageResource(R.drawable.icon_register_head);
//            e.printStackTrace();
//        }

    }

    /**
     * 绑定事件
     *
     * @param holder
     * @param position
     */
    private void setListener(final ViewHolder holder, final int position) {

        holder.imageHead.setTag(R.id.TAG, position);
        holder.imageHead.setOnClickListener(this);
        holder.imageBackground.setTag(R.id.TAG, position);
        holder.imageBackground.setOnClickListener(this);
        holder.imageComment.setTag(R.id.TAG, position);
        holder.imageComment.setOnClickListener(this);
        holder.imagePraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDetail(position);
            }
        });
        holder.txtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDetail(position);
            }
        });
    }

    private void postDetail(final int position) {
        NoteList note = listData.get(position);
        if ("0".equals(note.getFlag())) {
            if (fragment.changePraise(note.getId(), "1")) {
                note.setFlag("1");
                int count = Integer.valueOf(listData.get(position)
                        .getPraise_count()) + 1;
                note.setPraise_count(String.valueOf(count));
            }
        } else {
            if (fragment.changePraise(note.getId(), "2")) {
                note.setFlag("0");
                int count = Integer.valueOf(listData.get(position)
                        .getPraise_count()) - 1;
                note.setPraise_count(String.valueOf(count));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.TAG);
        Intent intent;
        switch (v.getId()) {
            case R.id.imageHead:
                intent = new Intent(mContext, SellerDetailActivity.class);
                intent.putExtra("client_id", listData.get(position).getClient_id());
                fragmentActivity.startActivity(intent);
                fragmentActivity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.imageBackground:
                intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                fragmentActivity.startActivity(intent);
                fragmentActivity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
            case R.id.imageComment:
                intent = new Intent(mContext, PostCommentActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                intent.putExtra("client_id", listData.get(position).getClient_id());
                fragmentActivity.startActivity(intent);
                fragmentActivity.overridePendingTransition(R.anim.right_in,
                        R.anim.my_left_out);
                break;
        }
    }

}
