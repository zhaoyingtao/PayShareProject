package com.snow.payshare;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2018/12/22
 * desc   :分享的实体类
 */

public class ShareBean implements Parcelable {
    private String title;
    private String describe;//分享描述
    private String cover;//分享图片地址
    private String url;//分享链接

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.describe);
        dest.writeString(this.cover);
        dest.writeString(this.url);
    }

    public ShareBean() {
    }

    protected ShareBean(Parcel in) {
        this.title = in.readString();
        this.describe = in.readString();
        this.cover = in.readString();
        this.url = in.readString();
    }

    public static final Creator<ShareBean> CREATOR = new Creator<ShareBean>() {
        @Override
        public ShareBean createFromParcel(Parcel source) {
            return new ShareBean(source);
        }

        @Override
        public ShareBean[] newArray(int size) {
            return new ShareBean[size];
        }
    };
}
