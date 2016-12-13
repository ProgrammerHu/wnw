package com.hemaapp.wnw.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.activity.MyOrderActivity;
import com.hemaapp.wnw.activity.PayActivity;
import com.hemaapp.wnw.adapter.MyOrderListAdapter;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.dialog.MyTwoButtonDialog;
import com.hemaapp.wnw.model.BillListModel;
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
public class MyOrderFragment extends MyFragment {

    private int position;

    public MyOrderFragment() {
        super();
    }

    public static MyOrderFragment getInstance(int position) {
        MyOrderFragment fragment = new MyOrderFragment();
        fragment.position = position;
        return fragment;
    }

    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listView;
    private MyOrderListAdapter adapter;
    private MyOrderActivity activity;
    private List<BillListModel> listData = new ArrayList<>();
    private int page;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_listview);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//注册EventBus接收器
        page = 0;
        getNetWorker().billList(activity.getApplicationContext().getUser().getToken(),
                String.valueOf(position), String.valueOf(page));
        log_e("OnCreate" + position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
        log_e("onDestroy");
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
                getNetWorker().billList(activity.getApplicationContext().getUser().getToken(),
                        String.valueOf(position), String.valueOf(page));
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case BILL_OPERATE:
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
            case BILL_LIST:
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                MyArrayResult<BillListModel> billResult = (MyArrayResult<BillListModel>) hemaBaseResult;
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
            case BILL_OPERATE:
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
                                //非全部时，发送通知到全部，提醒更新
                                EventBus.getDefault().post(new EventBusModel(true, MyConfig.ORDER_BILL_CHANGE));
                                break;
                            case "2"://删除订单
                                activity.showMyOneButtonDialog("我的订单", "删除订单成功！");
                                listData.remove(index);
                                if (position != 0)
                                    EventBus.getDefault().post(new EventBusModel(true, MyConfig.ORDER_BILL_CHANGE));
                                break;
                            case "3"://确认收货
                                activity.showMyOneButtonDialog("我的订单", "确认收货成功！");
                                listData.get(index).setTradetype("3");
                                if (position == 0) {//如果是在全部订单列表，退出就可以了
                                    break;
                                }
                                listData.remove(index);
                                //非全部时，发送通知到全部，提醒更新
                                EventBus.getDefault().post(new EventBusModel(true, MyConfig.ORDER_BILL_CHANGE));
                                break;
                            case "4"://提醒发货
                                activity.showMyOneButtonDialog("我的订单", "已提醒卖家发货！");
                                break;
                        }
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
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
        activity = (MyOrderActivity) getActivity();
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listView = (XtomListView) findViewById(R.id.listView);
        adapter = new MyOrderListAdapter(getActivity(), listData, listView, this);
        adapter.setEmptyString("╭(╯^╰)╮暂时还没有相关订单");
        listView.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().billList(activity.getApplicationContext().getUser().getToken(), String.valueOf(position), String.valueOf(page));
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().billList(activity.getApplicationContext().getUser().getToken(), String.valueOf(position), String.valueOf(page));
            }
        });
    }


    /**
     * 订单操作接口
     *
     * @param keytype 关联类型	1取消订单2删除订单3确认收货4提醒发货
     * @param id      订单id
     */
    public void billOperate(String keytype, String id, int position) {
        switch (keytype) {
            case "1":
                checkOperate(keytype, id, "(>_<)确定要取消订单吗");
                break;
            case "2":
                checkOperate(keytype, id, "(>_<)确定要删除订单吗");
                break;
            case "3":
                checkReceipt(id, listData.get(position).getTotal());
                break;
            case "4":
                showProgressDialog("提醒发货");
                getNetWorker().billOperate(activity.getApplicationContext().getUser().getToken(), keytype, id);
                break;
        }
    }

    /**
     * 删除和取消订单时确认用户操作
     *
     * @param keytype
     * @param id
     * @param content
     */
    private void checkOperate(final String keytype, final String id, String content) {
        MyOneButtonDialog dialog = new MyOneButtonDialog(activity);
        dialog.setTitle("我的订单").setText(content).hideIcon().setButtonListener(new MyOneButtonDialog.OnButtonListener() {
            @Override
            public void onButtonClick(MyOneButtonDialog OneButtonDialog) {
                OneButtonDialog.cancel();
                getNetWorker().billOperate(activity.getApplicationContext().getUser().getToken(), keytype, id);
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
    public void gotoPay(BillListModel bill) {
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra("keyid", bill.getId());
        intent.putExtra("PayPrize", bill.getTotal_fee());
        intent.putExtra("keytype", "2");
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

    /**
     * 确认收货？
     */
    private void checkReceipt(final String id, String count) {
        MyTwoButtonDialog oneButtonDialog = new MyTwoButtonDialog(activity);
        oneButtonDialog.setTitle("确认收货提醒").setText("确认已收货？")
                .setLeftButtonText("否").setRightButtonText("是").setOnButtonClickListener(new MyTwoButtonDialog.OnButtonListener() {
            @Override
            public void onCancelClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
            }

            @Override
            public void onLeftClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
            }

            @Override
            public void onRightClick(MyTwoButtonDialog twoButtonDialog) {
                twoButtonDialog.cancel();
                showProgressDialog("确认收货");
                getNetWorker().billOperate(activity.getApplicationContext().getUser().getToken(), "3", id);
            }
        });
        oneButtonDialog.show();
    }
}
