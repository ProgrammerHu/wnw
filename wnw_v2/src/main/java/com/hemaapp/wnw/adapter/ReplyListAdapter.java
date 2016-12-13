package com.hemaapp.wnw.adapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import xtom.frame.view.XtomListView;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.fragment.NoticeFragment;
import com.hemaapp.wnw.model.ReplyModel;
import com.hemaapp.wnw.model.SystemNotice;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.wnw.result.MyArrayResult;

/**
 * 留言回复列表适配器
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月9日
 */
public class ReplyListAdapter extends MyAdapter {
    private List<ReplyModel> listData;
    // private boolean DeleteState = false;
    private NoticeFragment fragment;
    private XtomListView listView;

    public ReplyListAdapter(Context mContext, List<ReplyModel> listData,
                            NoticeFragment fragment, XtomListView listView) {
        super(mContext);
        this.listData = listData;
        this.fragment = fragment;
        this.listView = listView;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            setEmptyString("没有留言回复");
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listitem_message_return, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        }
        setData(position, holder);
        return convertView;
    }

    private class ViewHolder {
        public ViewHolder(View convertView) {
            layoutFather = convertView.findViewById(R.id.layoutFather);
            txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            txtDatetime = (TextView) convertView.findViewById(R.id.txtDatetime);
            txtQuestion = (TextView) convertView.findViewById(R.id.txtQuestion);
            txtReply = (TextView) convertView.findViewById(R.id.txtReply);
            imageDelete = (ImageView) convertView
                    .findViewById(R.id.imageDelete);
            imageLogo = (ImageView) convertView.findViewById(R.id.imageLogo);
            viewCircle = convertView.findViewById(R.id.viewCircle);
        }

        private ImageView imageLogo, imageDelete;
        private View layoutFather, viewCircle;
        private TextView txtTitle, txtDatetime, txtQuestion, txtReply;
    }

    /**
     * 充实数据
     *
     * @param position
     * @param holder
     */
    private void setData(final int position, ViewHolder holder) {
        final ReplyModel replyModel = listData.get(position);
        // int visible = DeleteState ? View.VISIBLE : View.INVISIBLE;
        // holder.imageCheck.setVisibility(visible);
        holder.txtDatetime.setText(replyModel.getRegdate());
        int cirlceVisible = "2".equals(replyModel.getLooktype()) ? View.GONE
                : View.VISIBLE;
        holder.viewCircle.setVisibility(cirlceVisible);
        if ("1".equals(replyModel.getFrom())) {//	1.别人给我的回复
            holder.txtTitle.setText(Html
                    .fromHtml("<font>来自 </font><font color=#6361a1>"
                            + replyModel.getNickname()
                            + "</font><font> 的留言回复:</font>"));
        } else {
            holder.txtTitle.setText(Html
                    .fromHtml("<font>来自 </font><font color=#6361a1>"
                            + replyModel.getNickname()
                            + "</font><font> 的留言:</font>"));
        }

        holder.txtQuestion.setText(replyModel.getTitle());
        if (isNull(replyModel.getContent())) {
            holder.txtReply.setVisibility(View.GONE);
        } else {
            holder.txtReply.setVisibility(View.VISIBLE);
            holder.txtReply.setText(Html
                    .fromHtml("<font color=#6361a1>回复:</font><font>" + replyModel.getContent() + "</font>"));
        }

        try {
            URL url = new URL(replyModel.getAvatar());
            ImageTask task = new ImageTask(holder.imageLogo, url, mContext,
                    R.drawable.icon_register_head);
            listView.addTask(position, 0, task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            holder.imageLogo.setImageResource(R.drawable.icon_register_head);
        }

        holder.layoutFather.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.NoticeOperate("2", replyModel.getId());
                replyModel.setLooktype("2");
                notifyDataSetChanged();
                if (replyModel.getFrom().equals("2") && isNull(replyModel.getContent())) {//2.需要回复的
                    fragment.gotoReplyContentActivity(position);
                }
            }
        });

        holder.imageDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                fragment.Delete(true, replyModel.getId());
            }
        });
    }

}
