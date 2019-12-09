package com.snow.payshare;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;


/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2018/12/22
 * desc   : 支付、分享、第三方登录初始化
 */

public class PayShareApplication {
    private static PayShareApplication payShare;

    public static PayShareApplication getInstance() {
        if (payShare == null) {
            payShare = new PayShareApplication();
        }
        return payShare;
    }

    /**
     * 设置debug模式，可以打印出具体的错误信息
     * @param isDebug
     * @return
     */
    public PayShareApplication setIsDebug(boolean isDebug) {
        if (isDebug) {
            UMConfigure.setLogEnabled(true);
            UMConfigure.setEncryptEnabled(false);
        }
        return this;
    }

    /**
     * umeng平台申请的应用appkey
     *
     * @param context
     * @param umengAppkey
     * @return
     */
    public PayShareApplication initPayShare(Context context, String umengAppkey) {
        UMConfigure.init(context, umengAppkey
                , null, UMConfigure.DEVICE_TYPE_PHONE, null);
        return payShare;
    }

    /**
     * 微信平台申请的appId,将应用注册到微信中
     *
     * @param context
     * @param appId
     * @return
     */
    public PayShareApplication registerAppToWeChat(Context context, String appId) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, appId, true);
        wxapi.registerApp(appId);
        return this;
    }

    /**
     * 设置微信平台
     *
     * @param appId
     * @param appsecret
     * @return
     */
    public PayShareApplication setWeixin(String appId, String appsecret) {
        PlatformConfig.setWeixin(appId, appsecret);
        return payShare;
    }

    /**
     * 设置QQ平台
     *
     * @param appId
     * @param appKey
     * @return
     */
    public PayShareApplication setQQZone(String appId, String appKey) {
        PlatformConfig.setQQZone(appId, appKey);
        return payShare;
    }

    /**
     * 设置新浪微博平台
     *
     * @param appkey
     * @param appsecret
     * @return
     */
    public PayShareApplication setSinaWeibo(String appkey, String appsecret) {
        PlatformConfig.setSinaWeibo(appkey, appsecret, "http://sns.whalecloud.com");
        return payShare;
    }
}
