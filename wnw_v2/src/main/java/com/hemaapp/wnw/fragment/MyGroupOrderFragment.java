package com.hemaapp.wnw.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.MyOrderActivity;
import com.hemaapp.wnw.activity.PayActivity;
import com.hemaapp.wnw.adapter.MyGroupOrderListAdapter;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.SendExpressDialog;
import com.hemaapp.wnw.model.GroupBillListModel;
import com.hemaapp.wnw.model.eventbus.EventBusModel;
import com.hemaapp.wnw.model.eventbus.EventBusPay;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 我的订单列表界面
 * Created by Hufanglin on 2016/3/12.
 */
public class MyGroupOrderFragment extends MyFragment {

    private int position;
    private boolean IsMerchant;//是否是商家

    public MyGroupOrderFragment() {
        super();
    }

    public static MyGroupOrderFragment getInstance(int position, boolean IsMerchant) {
        MyGroupOrderFragment fragment = new MyGroupOrderFragment();
        fragment.position = position;
        fragment.IsMerchant = IsMerchant;
        return fragment;
    }

    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private MyGroupOrderListAdapter adapter;
    private MyFragmentActivity activity;
    private List<GroupBillListModel> listData = new ArrayList<>();
    private int page;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_listview);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//注册EventBus接收器
        page = 0;
        postList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    public void onEventMainThread(EventBusPay event) {// 响应EventBus的广播
        if (event.getType().equals("Pay") && event.getState()) {
            changeOrderState(event.getId());
        }
    }

    public void onEventMainThread(EventBusModel event) {//订单状态改变时，刷新界面数据
        if (!event.getState()) {
            return;
        }
        String type = event.getType();
        switch (type) {
            case MyConfig.ORDER_REPLY:
            case MyConfig.ORDER_REFUND:
            case MyConfig.ORDER_BILL_CHANGE:
                page = 0;
                postList();
                break;
        }

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_BILL_OPERATE:
            case MERCHANT_GROUP_OPERATE:
                showProgressDialog(R.string.committing);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        cancelProgressDialog();
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GROUP_BILL_LIST:
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                MyArrayResult<GroupBillListModel> billResult = (MyArrayResult<GroupBillListModel>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                    adapter.showProgress = false;
                }
                page++;
                listData.addAll(billResult.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.setLoadmoreable(billResult.getObjects().size() >=
                        activity.getApplicationContext().getSysInitInfo().getSys_pagesize());
                break;
            case GROUP_BILL_OPERATE: {
                String keytype = hemaNetTask.getParams().get("keytype");
                String id = hemaNetTask.getParams().get("id");
                for (int index = 0; index < listData.size(); index++) {
                    if (id.equals(listData.get(index).getId())) {
                        switch (keytype) {
                            case "1"://取消订单
                                activity.showMyOneButtonDialog("我的订单", "取消订单成功！");
                                listData.get(index).setTradetype("5");
                                if (position == 0) {//如果是在全部订单列表，退出就可以了
                                    break;
                                }
                                listData.remove(index);
                                break;
                            case "2"://删除订单
                                activity.showMyOneButtonDialog("我的订单", "删除订单成功！");
                                listData.remove(index);
                                break;
                            case "3"://确认收货
                                activity.showMyOneButtonDialog("我的订单", "确认收货成功！");
                                listData.get(index).setTradetype("3");
                                if (position == 0) {//如果是在全部订单列表，退出就可以了
                                    break;
                                }
                                listData.remove(index);
                                break;
                            case "4"://提醒发货
                                activity.showMyOneButtonDialog("我的订单", "已提醒卖家发货！");
                                break;
                        }
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
            break;
            case MERCHANT_GROUP_OPERATE: {
                String keytype = hemaNetTask.getParams().get("keytype");
                String id = hemaNetTask.getParams().get("id");
                for (int index = 0; index < listData.size(); index++) {
                    if (id.equals(listData.get(index).getId())) {
                        switch (keytype) {
                            case "1"://发货
                                activity.showMyOneButtonDialog("团购订单", "发货成功！");
                                listData.get(index).setTradetype("2");
                                if (position == 0) {//如果是在全部订单列表，退出就可以了
                                    break;
                                }
                                listData.remove(index);
                                break;
                            case "2"://删除订单
                                activity.showMyOneButtonDialog("团购订单", "删除订单成功！");
                                listData.remove(index);
                                break;

                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult
            hemaBaseResult) {
        if (page == 0) {
            adapter.showProgress = false;
            adapter.notifyDataSetChanged();
        }
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
        activity.showMyOneButtonDialog("我的订单", hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        if (page == 0) {
            adapter.showProgress = false;
            adapter.notifyDataSetChanged();
        }
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void findView() {
        activity = (MyFragmentActivity) getActivity();
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new MyGroupOrderListAdapter(getActivity(), listData, listView, this, IsMerchant);
        adapter.setEmptyString("╭(╯^╰)╮暂时还没有相关订单");
        listView.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                postList();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                postList();
            }
        });
    }


    /**
     * 订单操作接口
     *
     * @param keytype 关联类型	1取消订单2删除订单3确认收货4提醒发货
     * @param id      订单id
     */

    public void groupBillOperate(String keytype, String id) {
        switch (keytype) {
//            case "1":
//                checkOperate(keytype, id, "(>_<)确定要取消订单吗");
//                break;
            case "2":
                checkOperate(keytype, id, "(>_<)确定要删除订单吗");
                break;
            case "3":
                showProgressDialog("确认收货");
                getNetWorker().groupBillOperate(activity.getApplicationContext().getUser().getToken(), keytype, id);
                break;
            case "4":
                showProgressDialog("提醒发货");
                getNetWorker().groupBillOperate(activity.getApplicationContext().getUser().getToken(), keytype, id);
                break;
        }
    }

    /**
     * 商家团购订单操作接口
     *
     * @param keytype 1发货2删除订单
     * @param id
     */
    public void merchantGroupOperate(final String keytype, final String id) {
        switch (keytype) {
            case "1":
                SendExpressDialog dialog = new SendExpressDialog(activity);
                dialog.setOnButtonListener(new SendExpressDialog.OnButtonListener() {
                    @Override
                    public void clickConfirm(SendExpressDialog OneButtonDialog, String shipping_name, String shipping_num) {
                        if (isNull(shipping_name)) {
                            showTextDialog("请输入快递名称");
                            return;
                        }
                        if (isNull(shipping_num)) {
                            showTextDialog("请输入快递单号");
                            return;
                        }
                        OneButtonDialog.cancel();
                        getNetWorker().merchantGroupOperate(MyApplication.getInstance().getUser().getToken(), keytype, id, shipping_name, shipping_num);
                    }

                    @Override
                    public void clickCancel(SendExpressDialog OneButtonDialog) {
                        OneButtonDialog.cancel();
                    }
                });
                dialog.show();
                break;
            case "2":
                MyOneButtonDialog deleteDialog = new MyOneButtonDialog(activity);
                deleteDialog.setText("确定要删除订单吗？").setTitle("团购订单").hideIcon()
                        .setButtonListener(new MyOneButtonDialog.OnButtonListener() {
                            @Override
                            public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                                OneButtonDialog.cancel();
                                getNetWorker().merchantBillOperate(MyApplication.getInstance().getUser().getToken(), "2", id, "", "");
                            }

                            @Override
                            public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                                OneButtonDialog.cancel();
                            }
                        });
                deleteDialog.show();
                break;
        }

    }

    /**
     * 删除订单时确认用户操作
     *
     * @param keytype
     * @param id
     * @param content
     */
    private void checkOperate(final String keytype, final String id, String content) {
        MyOneButtonDialog dialog = new MyOneButtonDialog(activity);
        dialog.setTitle("团购订单").setText(content).hideIcon().setButtonListener(new MyOneButtonDialog.OnButtonListener() {
            @Override
            public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                OneButtonDialog.cancel();
                getNetWorker().groupBillOperate(activity.getApplicationContext().getUser().getToken(), keytype, id);
            }

            @Override
            public void onCancelClick(MyOneButtonDialog OneButtonDialog) {
                OneButtonDialog.cancel();
            }
        });

        dialog.show();
    }

    /**
     * 去支付界面
     *
     * @param bill
     */
    public void gotoPay(GroupBillListModel bill) {
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra("keyid", bill.getId());
        intent.putExtra("PayPrize", bill.getTotal_fee());
        intent.putExtra("keytype", "3");
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
    }

    /**
     * 支付成功之后，要修改订单状态
     *
     * @param id
     */
    private void changeOrderState(String id) {
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).getId().equals(id)) {//找到了相同id的
                listData.get(i).setTradetype("1");
                if (position != 0) {//不是全部订单，删除就可以了
                    listData.remove(i);
                }
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void postList() {
        if (IsMerchant) {//是商家
            getNetWorker().groupBillList(activity.getApplicationContext().getUser().getToken(), String.valueOf(position), "2", String.valueOf(page));
        } else {//普通用户
            getNetWorker().groupBillList(activity.getApplicationContext().getUser().getToken(), String.valueOf(position), "1", String.valueOf(page));
        }
    }
}
