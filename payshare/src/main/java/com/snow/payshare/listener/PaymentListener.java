package com.snow.payshare.listener;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2018/12/22
 * desc   :支付宝支付监听===微信支付回调不在这里监听
 */

public interface PaymentListener {
    void payCancel();

    void payError();

    void paySuccess();
}
