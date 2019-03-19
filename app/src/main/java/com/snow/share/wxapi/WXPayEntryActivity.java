package com.snow.share.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.snow.share.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
/**
 * Created by zyt on 2018/9/17.
 * 用于支付回调的类
 */

public class WXPayEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    private IWXAPI iwxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //修改此处的微信注册ID
        iwxapi = WXAPIFactory.createWXAPI(this, "WEI_XIN_APP_ID", true);
        iwxapi.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {//支付
            if (baseResp.errCode == 0) {
                //TODO 支付成功发送通知
//                EventBus.getInstance().post("");
                ToastUtils.getInstance().showToast("支付成功");
            } else if (baseResp.errCode == -1) {
                ToastUtils.getInstance().showToast("用户取消了支付");
            } else {
                ToastUtils.getInstance().showToast("支付失败");
            }
            finish();
        }
    }
}
