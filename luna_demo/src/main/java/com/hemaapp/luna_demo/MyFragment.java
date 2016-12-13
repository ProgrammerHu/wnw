package com.hemaapp.luna_demo;

import com.hemaapp.hm_FrameWork.HemaFragment;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.luna_demo.activity.MyActivity;

import xtom.frame.XtomActivityManager;
import xtom.frame.net.XtomNetWorker;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;

public abstract class MyFragment extends HemaFragment {

    @Override
    protected HemaNetWorker initNetWorker() {
        return new MyNetworker((MyActivity) getActivity());
    }

    @Override
    public MyNetworker getNetWorker() {
        return (MyNetworker) super.getNetWorker();
    }

    @Override
    public boolean onAutoLoginFailed(HemaNetWorker netWorker,
                                     HemaNetTask netTask, int failedType, HemaBaseResult baseResult) {
        switch (failedType) {
            case 0:// 服务器处理失败
                int error_code = baseResult.getError_code();
                switch (error_code) {
                    case 102:// 密码错误
                        XtomActivityManager.finishAll();
//				Intent it = new Intent(getActivity(), LoginActivity.class);
//				startActivity(it);
                        return true;
                    default:
                        break;
                }
            case XtomNetWorker.FAILED_HTTP:// 网络异常
            case XtomNetWorker.FAILED_DATAPARSE:// 数据异常
            case XtomNetWorker.FAILED_NONETWORK:// 无网络
                break;
        }
        return false;
    }

    // ------------------------下面填充项目自定义方法---------------------------


    /**
     * 数量小于20时显示
     */
    protected void showNone() {
        XtomToastUtil.showShortToast(getActivity(), "没有更多了数据了");
    }

    /**
     * 数量小于20时显示
     */
    protected void showNone(String content) {
        XtomToastUtil.showShortToast(getActivity(), content);
    }
}
