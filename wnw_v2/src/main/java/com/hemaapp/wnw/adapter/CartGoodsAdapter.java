package com.hemaapp.wnw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.MyCartActivity;
import com.hemaapp.wnw.model.CartListModel;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.wnw.view.DelSlideExpandableListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 购物车列表的适配器
 * Created by Hufanglin on 2016/3/16.
 */
public class CartGoodsAdapter extends BaseExpandableListAdapter {

    private TextView emptyTextView;
    private String emptyString = "购物车空空如也";
    private Context mContext;
    private ArrayList<CartListModel> listData = new ArrayList<>();
    private MyCartActivity activity;
    private DelSlideExpandableListView listView;

    public CartGoodsAdapter(Context mContext, ArrayList<CartListModel> listData,
                            DelSlideExpandableListView listView) {
        this.mContext = mContext;
        this.listData = listData;
        this.listView = listView;
        activity = (MyCartActivity) mContext;
    }

    @Override
    public int getGroupCount() {
        int count = listData == null ? 0 : listData.size();
        return count == 0 ? 1 : count;
    }

    @Override
    public boolean isEmpty() {
        int count = listData == null ? 0 : listData.size();
        return count == 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (isEmpty()) {
            return 0;
        }
        return listData.get(groupPosition).getChildItems() == null
                || listData.get(groupPosition).getChildItems().size() == 0 ? 0 : listData
                .get(groupPosition).getChildItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listData == null ? null : listData.get(groupPosition);

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listData == null ? null : listData.get(groupPosition).getChildItems()
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return listData == null ? 0 : groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return listData == null ? 0 : childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            return getEmptyView(parent);
        }
//        convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_cart, null, false);
//        ViewHolder holder = new ViewHolder(convertView);
////        convertView.setVisibility(View.GONE);
//        setData(holder, groupPosition);
        convertView = new View(mContext);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChildren holder;
        convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_cart_goods, null, false);
        holder = new ViewHolderChildren(convertView);
        setChildrenData(holder, groupPosition, childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public View getEmptyView(ViewGroup parent) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.listitem_empty_my, (ViewGroup) null);
        this.emptyTextView = (TextView) view.findViewById(R.id.textview);
        this.emptyTextView.setText(this.emptyString);
        int width = parent.getWidth();
        int height = parent.getHeight();
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(width, height);
        view.setLayoutParams(params);
        return view;
    }

    private class ViewHolder {
        public ViewHolder(View convertView) {
            txtNickname = (TextView) convertView.findViewById(R.id.txtNickname);
            imageCheck = (ImageView) convertView.findViewById(R.id.imageCheck);
            layoutFather = convertView.findViewById(R.id.layoutFather);
//            layoutFather.setVisibility(View.GONE);
        }

        private TextView txtNickname;
        private ImageView imageCheck;
        private View layoutFather;
    }

    /**
     * 设置商家级别的数据
     *
     * @param holder
     * @param position
     */
    private void setData(ViewHolder holder, int position) {
        CartListModel cart = listData.get(position);
        int checkResource = cart.isSelected() ? R.drawable.icon_checkbox_checked : R.drawable.icon_checkbox;
        holder.imageCheck.setImageResource(checkResource);//设置标记位
        holder.txtNickname.setText(cart.getMerchant_name());
    }

    /**
     * 设置商品级别的数据
     *
     * @param holder
     * @param groupPosition
     * @param childPosition
     */
    private void setChildrenData(ViewHolderChildren holder, final int groupPosition, final int childPosition) {
        final CartListModel.ChildItemsEntity childItemsEntity = listData.get(groupPosition).
                getChildItems().get(childPosition);
        int checkResource = childItemsEntity.isSelected() ? R.drawable.icon_checkbox_checked : R.drawable.icon_checkbox;
        holder.imageCheck.setImageResource(checkResource);//设置标记位
        holder.txtName.setText(childItemsEntity.getName());
        holder.txtSize.setText(childItemsEntity.getRule());
        holder.txtPrize.setText(childItemsEntity.getPrice());
        holder.txtCount.setText(childItemsEntity.getBuycount());
        try {
            URL url = new URL(childItemsEntity.getImgurl());
            ImageTask task = new ImageTask(holder.imageView, url, mContext, R.drawable.logo_default_square);
            activity.imageWorker.loadImage(task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.logo_default_square);
        }
        final int BuyCount = Integer.valueOf(childItemsEntity.getBuycount());
        holder.imageMore.setOnClickListener(new View.OnClickListener() {//数量+1
            @Override
            public void onClick(View v) {
                activity.cartSaveOperate("3", childItemsEntity.getId(), String.valueOf(BuyCount + 1));
            }
        });
        holder.imageLess.setOnClickListener(new View.OnClickListener() {//数量-1
            @Override
            public void onClick(View v) {
                if (BuyCount <= 1) {
                    return;
                }
                activity.cartSaveOperate("3", childItemsEntity.getId(), String.valueOf(BuyCount - 1));

            }
        });
        holder.imageCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartListModel.ChildItemsEntity child = listData.get(groupPosition).getChildItems().get(childPosition);
                changeChildrenState(!child.isSelected(), groupPosition, childPosition);
            }
        });
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//单个删除
                activity.cartSaveOperate("1", childItemsEntity.getId(), "0");
            }
        });
    }


    private class ViewHolderChildren {
        public ViewHolderChildren(View convertView) {
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtSize = (TextView) convertView.findViewById(R.id.txtSize);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtCount = (TextView) convertView.findViewById(R.id.txtCount);
            txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
            imageCheck = (ImageView) convertView.findViewById(R.id.imageCheck);
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageLess = (ImageView) convertView.findViewById(R.id.imageLess);
            imageMore = (ImageView) convertView.findViewById(R.id.imageMore);
        }

        private TextView txtName, txtSize, txtPrize, txtCount, txtDelete;
        private ImageView imageCheck, imageView, imageLess, imageMore;
    }

    public void setEmptyString(String emptyString) {
        if (this.emptyTextView != null) {
            this.emptyTextView.setText(emptyString);
        }
        this.emptyString = emptyString;
    }

    /**
     * 改变全部的状态
     *
     * @param isSelect
     */
    public void changeAllState(boolean isSelect) {
        for (CartListModel cart : listData) {
            cart.setIsSelected(isSelect);
            cart.setAllChildrenIsSelect(isSelect);
        }
        notifyDataSetChanged();
    }

    /**
     * 批量修改商家级别的状态
     *
     * @param isSelect
     * @param groupPosition
     */
    public void changeGroupState(boolean isSelect, int groupPosition) {
        CartListModel cart = listData.get(groupPosition);
        cart.setIsSelected(isSelect);
        cart.setAllChildrenIsSelect(isSelect);
        notifyDataSetChanged();
        checkIsSelectAll();
    }

    /**
     * 修改商品级别的状态
     *
     * @param isSelect
     * @param groupPosition
     * @param childPostion
     */
    public void changeChildrenState(boolean isSelect, int groupPosition, int childPostion) {
        CartListModel.ChildItemsEntity childItemsEntity =
                listData.get(groupPosition).getChildItems().get(childPostion);
        childItemsEntity.setIsSelected(isSelect);
        notifyDataSetChanged();//修改完了直接刷新界面就行了
        checkGroupSelectAll(groupPosition);
        checkIsSelectAll();
    }

    /**
     * 验证是否全选
     */
    private void checkIsSelectAll() {
        boolean isSelectAll = true;//默认已经被全选了
        for (CartListModel cart : listData) {
            if (!cart.isSelected()) {//如果子元素中有一个没有被选中，则未全选
                isSelectAll = false;
                break;//即可跳出循环了
            }
        }
        activity.changeSelectAllState(isSelectAll);
    }

    /**
     * 验证商家级别是否已被选择
     */
    private void checkGroupSelectAll(int groupPosition) {
        CartListModel cart = listData.get(groupPosition);
        boolean isSelectAll = true;//默认已经被全选了
        for (CartListModel.ChildItemsEntity childItemsEntity : cart.getChildItems()) {
            if (!childItemsEntity.isSelected()) {//如果子元素中有一个没有被选中，则未全选
                isSelectAll = false;
                break;//即可跳出循环了
            }
        }
        cart.setIsSelected(isSelectAll);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {//每次更新数据集时都要遍历一次数据集合，计算总价格。并更新到Activity
        super.notifyDataSetChanged();
        DecimalFormat df = new DecimalFormat("0.00");
        activity.setTotalFee(df.format(getTotalFee()));
    }

    /**
     * 获取已经选择的总价款
     *
     * @return
     */
    public float getTotalFee() {
        float totlFee = 0.00f;
        for (int groupPosition = 0; groupPosition < listData.size(); groupPosition++) {//遍历商家列表
            CartListModel cart = listData.get(groupPosition);
            for (int childPosition = 0; childPosition < cart.getChildItems().size(); childPosition++) {//遍历商品列表
                CartListModel.ChildItemsEntity child = cart.getChildItems().get(childPosition);
                if (child.isSelected()) {
                    float price = Float.valueOf(child.getPrice());
                    float buyCount = Float.valueOf(child.getBuycount());
                    totlFee += (price * buyCount);
                }
            }
        }
        return totlFee;
    }

    /**
     * 获取已经选择的购物车列表
     *
     * @return
     */
    public ArrayList<CartListModel> getSelectList() {
        ArrayList<CartListModel> tempList = new ArrayList<>();
        for (int groupPosition = 0; groupPosition < listData.size(); groupPosition++) {//遍历商家列表
            CartListModel cart = listData.get(groupPosition);//彻底复制至关重要，千万不可以引用，引用完了就没完没了了
            ArrayList<CartListModel.ChildItemsEntity> newChildList = new ArrayList<>();
            for (int childPosition = 0; childPosition < cart.getChildItems().size(); childPosition++) {//遍历商品列表
                CartListModel.ChildItemsEntity child = cart.getChildItems().get(childPosition);
                if (child.isSelected()) {
                    CartListModel.ChildItemsEntity childNew = new CartListModel.ChildItemsEntity(child.getId(),
                            child.getName(), child.getRule_id(), child.getRule(), child.getBuycount(), child.getStatetype(),
                            child.getImgurl(), child.getImgurlbig(), child.getPrice(), child.getSaleflag(), child.getExpressfee());
                    childNew.setIsSelected(true);
                    newChildList.add(childNew);
                }
            }
            if (newChildList.size() != 0) {
                CartListModel newCart = new CartListModel(cart.getMerchant_id(),
                        cart.getMerchant_name(), cart.getPromote_price(), newChildList);
                newCart.setIsSelected(true);
                tempList.add(newCart);
            }
        }
        return tempList;
    }
}
