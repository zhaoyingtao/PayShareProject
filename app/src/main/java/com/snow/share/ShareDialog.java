package com.snow.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snow.payshare.ShareBean;
import com.snow.payshare.manager.ShareManager;


import static com.snow.payshare.manager.ShareManager.SHARE_CLIPBOARD_URL;
import static com.snow.payshare.manager.ShareManager.SHARE_WEIXIN;
import static com.snow.payshare.manager.ShareManager.SHARE_WEIXIN_CIRCLE;


/**
 * Created by zhaozhaoyingtao on 2017/12/5.
 * 分享
 * <p>
 * 需要将分享的数据封装到ShareBean中
 */

public class ShareDialog extends Dialog {

    LinearLayout llWx;
    LinearLayout llWxCircle;
    LinearLayout llCopy;
    TextView tvCancel;


    private Activity mActivity;


    public ShareDialog(@NonNull Context context) {
        super(context);
        mActivity = (Activity) context;
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        getWindow().setAttributes(p);
    }

    private void initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_share, null);
        llWx = view.findViewById(R.id.llWx);
        llWxCircle = view.findViewById(R.id.llWxCircle);
        llCopy = view.findViewById(R.id.llCopy);
        tvCancel = view.findViewById(R.id.tvCancel);
        setContentView(view);
        setCanceledOnTouchOutside(true);


        llWx.setOnClickListener(v -> {
            ShareManager.init(mActivity).shareAction(SHARE_WEIXIN);
            dismiss();
        });
        llWxCircle.setOnClickListener(v -> {
            ShareManager.init(mActivity).shareAction(SHARE_WEIXIN_CIRCLE);
            dismiss();
        });
        llCopy.setOnClickListener(v -> {
            ShareManager.init(mActivity).shareAction(SHARE_CLIPBOARD_URL);
            dismiss();
        });
    }

    /**
     * 网络图片
     *
     * @param imgUrl
     */
    public void setShareData(String imgUrl) {
        if (mActivity != null) {
            ShareManager.init(mActivity).setShareData(imgUrl);
        }
    }

    /**
     * 图片bitmap
     *
     * @param bitmap
     */
    public void setShareData(Bitmap bitmap) {
        if (mActivity != null) {
            ShareManager.init(mActivity).setShareData(bitmap);
        }
    }

    /**
     * 分享链接
     *
     * @param shareBean
     */
    public void setShareData(ShareBean shareBean) {
        if (mActivity != null) {
            ShareManager.init(mActivity).setShareData(shareBean);
        }
    }
}
