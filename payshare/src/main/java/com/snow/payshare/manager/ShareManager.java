package com.snow.payshare.manager;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.snow.payshare.ShareBean;
import com.snow.payshare.listener.ShareListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

/**
 * Created by zyt on 2018/9/12.
 * 分享的操作管理器 === 注意设置分享类型只选择一种数据设置
 */

public class ShareManager {
    private static ShareManager shareManager;

    private UMWeb umWeb;
    private static Activity mActivity;
    private ShareBean shareBean;
    private UMImage umImage;
    // 0 朋友圈
    public static final int SHARE_WEIXIN_CIRCLE = 0;
    // 1 微信
    public static final int SHARE_WEIXIN = 1;
    //2 QQ好友
    public static final int SHARE_QQ = 2;
    //3 QQ空间
    public static final int SHARE_QZONE = 3;
    //4 微博
    public static final int SHARE_SINA = 4;
    //5 复制链接
    public static final int SHARE_CLIPBOARD_URL = 5;
    private ShareListener shareBListener;
    private String cancelMsg = "取消分享";
    private String failMsg = "分享失败";
    private String successMsg = "分享成功";

    public static ShareManager init(Activity activity) {
        mActivity = activity;
        if (shareManager == null) {
            synchronized (ShareManager.class) {
                if (shareManager == null) {
                    shareManager = new ShareManager();
                }
            }
        }
        return shareManager;
    }

    /**
     * 分享链接==最常用
     *
     * @param shareBean
     */
    public void setShareData(ShareBean shareBean) {
        this.shareBean = shareBean;
        if (shareBean == null) {
            shareBean = new ShareBean();
        }
        if (shareBean.getUrl() == null) {
            return;
        }
        umWeb = new UMWeb(shareBean.getUrl());
        if (!TextUtils.isEmpty(shareBean.getTitle()))
            umWeb.setTitle(shareBean.getTitle());
        if (!TextUtils.isEmpty(shareBean.getDescribe()))
            umWeb.setDescription(shareBean.getDescribe());
        if (!TextUtils.isEmpty(shareBean.getCover()))
            umWeb.setThumb(new UMImage(mActivity, shareBean.getCover()));
    }

    /**
     * 纯图片
     *
     * @param imgUrl ==网络图
     */
    public void setShareData(String imgUrl) {
        umImage = new UMImage(mActivity, imgUrl);//网络图片
        //设置缩略图
        UMImage thumb = new UMImage(mActivity, imgUrl);
        umImage.setThumb(thumb);
        umImage.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//        umImage.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
    }

    /**
     * 纯图片
     *
     * @param bitmap bitmap格式
     */
    public void setShareData(Bitmap bitmap) {
        umImage = new UMImage(mActivity, bitmap);//网络图片
        //设置缩略图
        UMImage thumb = new UMImage(mActivity, bitmap);
        umImage.setThumb(thumb);
        umImage.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
    }

    /**
     * 分享的具体行为
     *
     * @param position ShareManager.SHARE_WEIXIN_CIRCLE 朋友圈
     *                 ShareManager.SHARE_WEIXIN 微信
     *                 ShareManager.SHARE_QQ  QQ好友
     *                 ShareManager.SHARE_QZONE  QQ空间
     *                 ShareManager.SHARE_SINA  微博
     *                 ShareManager.SHARE_CLIPBOARD_URL  复制链接
     */
    public void shareAction(int position) {
        UMShareAPI umShareAPI = UMShareAPI.get(mActivity);
        switch (position) {
            case SHARE_WEIXIN_CIRCLE://朋友圈
                if (!umShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN)) {//判断是否安装了微信
                    showToast("请先安装微信");
                    return;
                }
                setShareAction(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case SHARE_WEIXIN://微信
                if (!umShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN)) {//判断是否安装了微信
                    showToast("请先安装微信");
                    return;
                }
                setShareAction(SHARE_MEDIA.WEIXIN);
                break;
            case SHARE_QQ://QQ好友
                if (!isInstallThisApp(mActivity, "com.tencent.mobileqq")) {//判断是否安装了QQ
                    showToast("请先安装QQ");
                    return;
                }
                setShareAction(SHARE_MEDIA.QQ);
                break;
            case SHARE_QZONE://QQ空间
                if (!isInstallThisApp(mActivity, "com.tencent.mobileqq")) {//判断是否安装了QQ
                    showToast("请先安装QQ");
                    return;
                }
                setShareAction(SHARE_MEDIA.QZONE);
                break;
            case SHARE_SINA://微博
                setShareAction(SHARE_MEDIA.SINA);
                break;
            case SHARE_CLIPBOARD_URL://复制链接
                if (shareBean != null) {
                    ClipboardManager clipboardManager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(shareBean.getUrl());
                    showToast("已复制链接");
                }
                break;
        }
    }

    /**
     * 设置分享行为
     *
     * @param platform 平台
     */
    private void setShareAction(SHARE_MEDIA platform) {
        if (umWeb != null) {
            new ShareAction(mActivity)
                    .setPlatform(platform)
                    .withMedia(umWeb)
                    .setCallback(shareListener)
                    .share();
        }
        if (umImage != null) {
            new ShareAction(mActivity)
                    .setPlatform(platform)
                    .withMedia(umImage)
                    .setCallback(shareListener)
                    .share();
        }

    }

    /**
     * 设置分享结果监听
     *
     * @param shareBListener
     */
    public void setShareCallBackListener(ShareListener shareBListener) {
        this.shareBListener = shareBListener;
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (shareBListener != null) {
                shareBListener.shareSuccess(platform);
            } else {
                showToast(successMsg);
            }
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (shareBListener != null) {
                shareBListener.shareError();
            } else {
                showToast(failMsg);
            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (shareBListener != null) {
                shareBListener.shareCancel();
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
    public void setToastMessage(String successMsg, String cancelMsg, String failMsg) {
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
        Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
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
