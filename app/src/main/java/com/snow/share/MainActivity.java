package com.snow.share;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.snow.payshare.listener.AuthorizeLoginListener;
import com.snow.payshare.manager.AuthorizedLogin;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;



public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        findViewById(R.id.share_Wechat).setOnClickListener(view -> {
            AuthorizedLogin.getInstance().delAuthorized(this, SHARE_MEDIA.WEIXIN);
            AuthorizedLogin.getInstance().userWeiChatLogin(this);
            ToastUtils.getInstance().showToast("分享");
        });
        findViewById(R.id.share_QQ).setOnClickListener(view -> {
            AuthorizedLogin.getInstance().delAuthorized(this, SHARE_MEDIA.QQ);
            AuthorizedLogin.getInstance().userQQLogin(this);
        });
        findViewById(R.id.share_sina).setOnClickListener(view -> {
            AuthorizedLogin.getInstance().delAuthorized(this, SHARE_MEDIA.SINA);
            AuthorizedLogin.getInstance().useSinaLogin(this);
        });
    }

    private void initView() {
        AuthorizedLogin.getInstance().setAuthorizedLoginListener(new AuthorizeLoginListener() {
            @Override
            public void authorizeCancel() {

            }

            @Override
            public void authorizeSuccess(SHARE_MEDIA platform, Map<String, String> data) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
