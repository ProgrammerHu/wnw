package com.hemaapp.wnw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.AddressListAdapter;
import com.hemaapp.wnw.model.Address;
import com.hemaapp.wnw.result.MyArrayResult;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.view.XtomListView;

/**
 * 收货地址列表 Created by Hufanglin on 2016/3/7.
 */
public class AddressListActivity extends MyActivity implements
        View.OnClickListener {
    public static final int EDIT = 1;
    private ImageView imageQuitActivity;
    private TextView txtTitle;
    private XtomListView listView;
    private Button btnConfirm;
    private AddressListAdapter adapter;
    private ArrayList<Address> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address_list);
        super.onCreate(savedInstanceState);
        getNetWorker()
                .addressList(getApplicationContext().getUser().getToken());
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation infomation = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (infomation) {
            case ADDRESS_LIST:
                showProgressDialog(R.string.loading);
                break;
            case REMOVE_ROOT:
                showProgressDialog("删除中");
                break;
            case ADDRESS_OPERATE:
                showProgressDialog(R.string.committing);
                break;
        }

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult hemaBaseResult) {
        MyHttpInformation infomation = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (infomation) {
            case ADDRESS_LIST:
                cancelProgressDialog();
                MyArrayResult<Address> result = (MyArrayResult<Address>) hemaBaseResult;
                listData.clear();
                listData.addAll(result.getObjects());
                adapter.notifyDataSetChanged();
                if (listData.size() == 1
                        && listData.get(0).getDefaultflag().equals("0")) {
                    adapter.addressOperate(listData.get(0).getId());
                }
                break;
            case REMOVE_ROOT:
                if (!afterRemoveRoot(hemaNetTask.getParams().get("keyid"))) {
                    getNetWorker().addressList(
                            getApplicationContext().getUser().getToken());
                }
                showMyOneButtonDialog(getResources().getString(R.string.address_controller), "删除成功");
                break;
            case ADDRESS_OPERATE:
                cancelProgressDialog();
                getNetWorker().addressList(
                        getApplicationContext().getUser().getToken());

                showMyOneButtonDialog(getResources().getString(R.string.address_controller), hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        showMyOneButtonDialog(
                getResources().getString(R.string.address_controller),
                hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.address_controller);
        listView = (XtomListView) findViewById(R.id.listView);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        adapter = new AddressListAdapter(mContext, listData, false);
        listView.setAdapter(adapter);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        btnConfirm.setOnClickListener(this);
        imageQuitActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                Intent data = new Intent();
                data.putExtra("ListAddress", listData);
                setResult(RESULT_OK, data);
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.btnConfirm:
                Intent intent = new Intent(this, AddressEditActivity.class);
                startActivityForResult(intent, EDIT);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        Intent data = new Intent();
        data.putExtra("ListAddress", listData);
        setResult(RESULT_OK, data);
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    /**
     * 转到
     *
     * @param address
     */
    public void gotoAddressEdit(Address address) {
        Intent intent = new Intent(this, AddressEditActivity.class);
        intent.putExtra("Address", address);
        startActivityForResult(intent, EDIT);
        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
    }


    /**
     * 删除的后续处理
     *
     * @param reomveId
     * @return true，没有默认地址了，false，还有默认地址
     */
    private boolean afterRemoveRoot(String reomveId) {
        boolean hasDefault = false;
        List<Address> tempListData = new ArrayList<>();

        for (int i = 0; i < listData.size(); i++) {
            if (!reomveId.equals(listData.get(i).getId())
                    && "1".equals(listData.get(i).getDefaultflag())) {// 验证没有删除并且是默认地址的行
                hasDefault = true;
            }
            if (!reomveId.equals(listData.get(i).getId())) {
                tempListData.add(listData.get(i));
            }
        }
        if (!hasDefault && tempListData.size() > 0) {// 没有默认地址了
            adapter.addressOperate(tempListData.get(0).getId());
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT) {
            getNetWorker().addressList(
                    getApplicationContext().getUser().getToken());
        }
    }

}
