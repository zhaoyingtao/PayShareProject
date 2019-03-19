package com.snow.payshare.listener;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2018/12/22
 * desc   :授权登录监听
 */

public interface AuthorizeLoginListener {
    void authorizeCancel();

    /**
     * @param platform 平台
     * @param data 返回值
     */
    void authorizeSuccess(SHARE_MEDIA platform, Map<String, String> data);
}
