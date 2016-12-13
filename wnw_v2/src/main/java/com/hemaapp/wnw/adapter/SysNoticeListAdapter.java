package com.hemaapp.wnw.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.MyGroupOrderDetailActivity;
import com.hemaapp.wnw.activity.MyOrderActivity;
import com.hemaapp.wnw.activity.MyOrderDetailActivity;
import com.hemaapp.wnw.activity.RefundDetailActivity;
import com.hemaapp.wnw.fragment.NoticeFragment;
import com.hemaapp.wnw.model.SystemNotice;

/**
 * 系统消息的列表
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月8日
 */
public class SysNoticeListAdapter extends MyAdapter {

    private List<SystemNotice> listData = new ArrayList<>();
    // private boolean DeleteState = false;
    private NoticeFragment fragment;

    public SysNoticeListAdapter(Context mContext, List<SystemNotice> listData,
                                NoticeFragment fragment) {
        super(mContext);
        this.listData = listData;
        this.fragment = fragment;
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
            setEmptyString("没有系统消息");
            return getEmptyView(parent);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listitem_sys_notice, null, false);
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
            txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            imageDelete = (ImageView) convertView.findViewById(R.id.imageDelete);
            imageLogo = (ImageView) convertView.findViewById(R.id.imageLogo);
            viewCircle = convertView.findViewById(R.id.viewCircle);
        }

        private TextView txtTitle, txtDatetime, txtContent;
        private ImageView imageDelete, imageLogo;
        private View layoutFather, viewCircle;
    }

    /**
     * 充实数据
     *
     * @param position
     * @param holder
     */
    private void setData(int position, ViewHolder holder) {
        final SystemNotice notice = listData.get(position);
        switch (notice.getKeytype()) {// 设置标题
            case "1"://系统通知
            case "6"://留言回复
                holder.txtTitle.setText("系统通知");
                holder.imageLogo.setImageResource(R.drawable.icon_system);
                break;
            case "2"://用户收到商家普通发货提醒
                holder.txtTitle.setText("发货提醒");
                holder.imageLogo.setImageResource(R.drawable.icon_send);
                break;
            case "5"://用户收到商家团购发货提醒
                holder.txtTitle.setText("发货提醒");
                holder.imageLogo.setImageResource(R.drawable.icon_send);
                break;
            case "3"://退款提醒
                holder.txtTitle.setText("退款提醒");
                holder.imageLogo.setImageResource(R.drawable.icon_back);
                break;
            case "4"://后台收到用户普通发货提醒
                holder.txtTitle.setText("提醒发货");
                holder.imageLogo.setImageResource(R.drawable.icon_send_action);
                break;
            case "7"://后台收到用户团购发货提醒
                holder.txtTitle.setText("提醒发货");
                holder.imageLogo.setImageResource(R.drawable.icon_send_action);
                break;
            case "9"://商家收到的退款提醒，需处理
                holder.txtTitle.setText("退款申请");
                holder.imageLogo.setImageResource(R.drawable.icon_refund_action);
                break;
            case "10"://商家收到普通订单确认收货的提醒
                holder.txtTitle.setText("收货提醒");
                holder.imageLogo.setImageResource(R.drawable.icon_receive);
                break;
            case "11"://商家收到团购订单确认收货的提醒
                holder.txtTitle.setText("收货提醒");
                holder.imageLogo.setImageResource(R.drawable.icon_receive);
                break;
            default:
                break;
        }
        holder.txtDatetime.setText(notice.getRegdate());
        holder.txtContent.setText(notice.getContent());
        int cirlceVisible = "2".equals(notice.getLooktype()) ? View.GONE
                : View.VISIBLE;
        holder.viewCircle.setVisibility(cirlceVisible);

        holder.layoutFather.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("1".equals(notice.getLooktype())) {
                    fragment.NoticeOperate("1", notice.getId());
                    notice.setLooktype("2");
                    notifyDataSetChanged();
                }
                Intent intent;
                MyFragmentActivity activity = (MyFragmentActivity) mContext;
                switch (notice.getKeytype()) {
                    case "2"://订单详情
                        intent = new Intent(mContext, MyOrderDetailActivity.class);
                        intent.putExtra("id", notice.getKeyid());
                        intent.putExtra("type", "1");
                        activity.startActivity(intent);
                        activity.changeAnim();
                        break;
                    case "3"://退款详情
                        intent = new Intent(mContext, RefundDetailActivity.class);
                        intent.putExtra("id", notice.getKeyid());
                        intent.putExtra("keytype", "1");
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        break;
                    case "4"://后台收到用户普通发货提醒
                    case "10"://商家收到普通订单确认收货的提醒
                        intent = new Intent(mContext, MyOrderDetailActivity.class);
                        intent.putExtra("id", notice.getKeyid());
                        intent.putExtra("type", "2");
                        activity.startActivity(intent);
                        activity.changeAnim();
                        break;
                    case "5"://团购订单详情
                        intent = new Intent(mContext, MyGroupOrderDetailActivity.class);
                        intent.putExtra("id", notice.getKeyid());
                        intent.putExtra("flag", "1");
                        activity.startActivity(intent);
                        activity.changeAnim();
                        break;
                    case "7"://后台收到用户团购发货提醒
                    case "11"://商家收到团购订单确认收货的提醒
                        intent = new Intent(mContext, MyGroupOrderDetailActivity.class);
                        intent.putExtra("id", notice.getKeyid());
                        intent.putExtra("flag", "2");
                        activity.startActivity(intent);
                        activity.changeAnim();
                        break;
                    case "9"://商家收到的退款提醒，需处理
                        intent = new Intent(mContext, RefundDetailActivity.class);
                        intent.putExtra("id", notice.getKeyid());
                        intent.putExtra("keytype", "2");
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                        break;
                }
            }
        });

        holder.imageDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                fragment.Delete(false, notice.getId());
            }
        });
    }

}
