package com.snow.payshare.listener;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2018/12/22
 * desc   :授权登录监听
 */

public interface ShareListener {
    void shareCancel();

    void shareError();

    /**
     * @param platform 平台
     */
    void shareSuccess(SHARE_MEDIA platform);
}
