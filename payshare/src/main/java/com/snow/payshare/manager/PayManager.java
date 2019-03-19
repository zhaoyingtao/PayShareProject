package com.snow.payshare.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.snow.payshare.listener.PaymentListener;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zyt on 2018/7/25.
 * 支付有关的处理
 */

public class PayManager {
    private static PayManager payManager;
    private PaymentListener paymentListener;
    public Context mContext;
    private String cancelMsg = "用户取消了支付";
    private String failMsg = "支付失败";
    private String successMsg = "支付成功了";
    public static PayManager init(Context context) {
        if (payManager == null) {
            payManager = new PayManager();
        }
        return payManager;
    }

    /**
     * 设置支付结果监听（阿里支付，微信支付结果不在这里监听）===不设置这个回调，则只toast支付结果
     *
     * @param paymentListener
     */
    public void setPaymentListener(PaymentListener paymentListener) {
        this.paymentListener = paymentListener;
    }

    /**
     * 支付宝去支付
     */
    public void goToAliPay(Context context, String aliRequest) {
        if (TextUtils.isEmpty(aliRequest)) {
            return;
        }
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                mContext = context;
                PayTask aliPay = new PayTask((Activity) context);
                Map<String, String> result = aliPay.payV2(aliRequest, true);
                Message msg = new Message();
                msg.what = 001;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝支付的回调
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //            { when=-10s197ms what=1 obj={resultStatus=6001, result=, memo=用户取消} target=com.changdao.ttsing.presenter.mine.MyAccountPresent$5 planTime=1513064114814 dispatchTime=0 finishTime=0 }
            Map<String, String> result = (Map<String, String>) msg.obj;
            String resultStatus = result.get("resultStatus");
            if ("9000".equals(resultStatus)) {
                if (paymentListener != null) {
                    paymentListener.paySuccess();
                } else {
                    showToast(successMsg);
                }
            } else if ("6001".equals(resultStatus)) {
                if (paymentListener != null) {
                    paymentListener.payCancel();
                } else {
                    showToast(cancelMsg);
                }
            } else {
                if (paymentListener != null) {
                    paymentListener.payError();
                } else {
                    showToast(failMsg);
                }
            }
//            else if ("6002".equals(resultStatus)) {
////                ToastUtils.getInstance().showToast("网络连接出错");
//            }
            return false;
        }
    });

    /**
     * 微信去支付
     */
    public void goToWeChatPay(Context context, JSONObject pay_data) {
        if (pay_data == null) {
            return;
        }
        mContext = context;
        try {
            IWXAPI msgApi = WXAPIFactory.createWXAPI(mContext, null);
            PayReq request = new PayReq();
            request.appId = pay_data.optString("appid");
            request.partnerId = pay_data.optString("partnerid");
            request.prepayId = pay_data.optString("prepayid");
            request.packageValue = pay_data.optString("package");//"Sign=WXPay"
            request.nonceStr = pay_data.optString("noncestr");
            request.timeStamp = pay_data.optString("timestamp");
            request.sign = pay_data.optString("sign");
            msgApi.sendReq(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 设置toast文字提示，设置""显示默认值
     *
     * @param cancelMsg
     * @param failMsg
     */
    public void setToastMessage(String successMsg,String cancelMsg, String failMsg) {
        if (!TextUtils.isEmpty(successMsg)) {
            this.successMsg = successMsg;
        }
        if (!TextUtils.isEmpty(cancelMsg)) {
            this.cancelMsg = cancelMsg;
        }
        if (!TextUtils.isEmpty(failMsg)) {
            this.failMsg = failMsg;
        }
    }
    void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }
}
