package com.snow.payshare.manager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.snow.payshare.listener.AuthorizeLoginListener;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.Map;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2018/12/22
 * desc   :授权登录
 */

public class AuthorizedLogin {
    private static AuthorizedLogin authorizedLogin;
    private AuthorizeLoginListener authorizeLoginListener;
    private Toast mToast;
    private Context mContext;
    private String cancelMsg = "取消了授权";
    private String failMsg = "授权失败：";


    public static AuthorizedLogin getInstance() {
        if (authorizedLogin == null) {
            authorizedLogin = new AuthorizedLogin();
        }
        return authorizedLogin;
    }

    /**
     * 清除授权登录
     */
    public void delAuthorized(Activity mActivity, SHARE_MEDIA shareMedia) {
        UMShareAPI umShareAPI = UMShareAPI.get(mActivity);
        umShareAPI.deleteOauth(mActivity, shareMedia, null);
    }

    /**
     * 使用新浪微博授权登录
     */
    public void useSinaLogin(Context mContext) {
        this.mContext = mContext;
        UMShareAPI umShareAPI = UMShareAPI.get(mContext);
        if (!umShareAPI.isInstall((Activity) mContext, SHARE_MEDIA.SINA)) {
            showToast("请先安装新浪微博");
        } else {
            umShareAPI.doOauthVerify((Activity) mContext, SHARE_MEDIA.SINA, authListener);
        }
    }

    /**
     * 使用QQ授权登录
     */
    public void userQQLogin(Context mContext) {
        this.mContext = mContext;
        UMShareAPI umShareAPI = UMShareAPI.get(mContext);
        //判断QQ安装有问题，只能使用自己的方法
//        if (!umShareAPI.isInstall((Activity) mContext, SHARE_MEDIA.QQ)) {
        if (!isInstallThisApp(mContext, "com.tencent.mobileqq")) {
            showToast("请先安装QQ");
        } else {
            umShareAPI.doOauthVerify((Activity) mContext, SHARE_MEDIA.QQ, authListener);
        }
    }

    /**
     * 使用微信授权登录
     */
    public void userWeiChatLogin(Context mContext) {
        this.mContext = mContext;
        UMShareAPI umShareAPI = UMShareAPI.get(mContext);
        umShareAPI.deleteOauth((Activity) mContext, SHARE_MEDIA.WEIXIN, null);
        if (!umShareAPI.isInstall((Activity) mContext, SHARE_MEDIA.WEIXIN)) {
            showToast("请先安装微信");
        } else {
            umShareAPI.doOauthVerify((Activity) mContext, SHARE_MEDIA.WEIXIN, authListener);
//            umShareAPI.getPlatformInfo((Activity) mContext, SHARE_MEDIA.WEIXIN, authListener);
        }
    }


    /**
     * 设置授权结果监听
     *
     * @param authorizeLoginListener
     */
    public void setAuthorizedLoginListener(AuthorizeLoginListener authorizeLoginListener) {
        this.authorizeLoginListener = authorizeLoginListener;
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (authorizeLoginListener != null) {
                authorizeLoginListener.authorizeSuccess(platform, data);
            }
//            switch (platform) {
//                case QQ:
//                    thirdPartyLogin("qq_openid", data.get("openid"));
//                    break;
//                case WEIXIN:
//                    thirdPartyLogin("wechat_unionid", data.get("unionid"));
//                    break;
//            }
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast(failMsg + t.getMessage());
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            if (authorizeLoginListener != null) {
                authorizeLoginListener.authorizeCancel();
            } else {
                showToast(cancelMsg);
            }
        }
    };

    /**
     * 设置toast文字提示，设置""显示默认值
     *
     * @param cancelMsg
     * @param failMsg
     */
    public void setAuthorizedLoginToastMessage(String cancelMsg, String failMsg) {
        if (!TextUtils.isEmpty(cancelMsg)) {
            this.cancelMsg = cancelMsg;
        }
        if (!TextUtils.isEmpty(failMsg)) {
            this.failMsg = failMsg;
        }
    }

    /**
     * 显示Toast
     *
     * @param mess
     */
    public void showToast(String mess) {
        if (mContext == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(mContext, null, Toast.LENGTH_LONG);
            mToast.setText(mess);
        } else {
            mToast.setText(mess);
        }
        mToast.show();
    }

    /**
     * 判断是否安装了某个应用
     *
     * @param context
     * @param pageName 包名
     * @return
     */
    public boolean isInstallThisApp(Context context, String pageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(pageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
