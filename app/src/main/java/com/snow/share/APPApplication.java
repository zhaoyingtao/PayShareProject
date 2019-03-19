package com.snow.share;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.snow.payshare.PayShareApplication;


/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2018/12/22
 * desc   :
 */

public class APPApplication extends MultiDexApplication {
    private static APPApplication appApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        appApplication = this;
        init();
        initShare();
    }
    public static APPApplication getApplicationCotext(){
        return appApplication;
    }

    private void init() {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * umeng相关、分享
     */
    private void initShare() {
        PayShareApplication.getInstance()
                .setIsDebug(true)
                .initPayShare(this, "xxxx")
                .registerAppToWeChat(this, "xxxx")
                .setWeixin("xxxx", "xxxx")
                .setQQZone("xxxx", "xxxx")
                .setSinaWeibo("xxxx", "xxxx");
    }
}
