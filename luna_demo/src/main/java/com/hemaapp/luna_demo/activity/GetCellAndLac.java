package com.hemaapp.luna_demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaHttpInfomation;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_demo.R;

/**
 * Created by HuHu on 2016-10-08.
 */
public class GetCellAndLac extends MyActivity {
    private Button btn1, btn2;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cell);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        textView = (TextView) findViewById(R.id.textView);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                CellLocation cel = tel.getCellLocation();
                int nPhoneType = tel.getPhoneType();
                //移动联通 GsmCellLocation
                if (cel instanceof GsmCellLocation) {
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) cel;
                    int nGSMCID = gsmCellLocation.getCid();
                    if (nGSMCID > 0) {
                        if (nGSMCID != 65535) {
                            textView.setText("基站号:" + nGSMCID + "\n小区号:" + gsmCellLocation.getLac());
                        }
                    }
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                CellLocation cel = tel.getCellLocation();
                int nPhoneType = tel.getPhoneType();
                //电信   CdmaCellLocation
                if (nPhoneType == 2 && cel instanceof CdmaCellLocation) {
                    CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cel;
                    textView.setText("SystemId:" + cdmaCellLocation.getSystemId() +
                            "\nNetworkId:" + cdmaCellLocation.getNetworkId() +
                            "\nBaseStationId:" + cdmaCellLocation.getBaseStationId());
                }
            }
        });
    }

    @Override
    public void onError(HemaHttpInfomation hemaHttpInfomation) {

    }

    @Override
    public void onSuccess(HemaBaseResult hemaBaseResult, HemaHttpInfomation hemaHttpInfomation) {

    }
}
