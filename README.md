# PayShareProject
通过简单配置实现umeng的微信、QQ、新浪微博的授权登录和分享功能，外加支付宝和微信支付功能


# PayShareProject
[ ![Download](https://api.bintray.com/packages/zhaoyingtao/maven/payshare/images/download.svg) ](https://bintray.com/zhaoyingtao/maven/payshare/_latestVersion)

关于授权登录和分享这块集成相对配置比较多，需要注意点！

集成方式参考如下：  

一、在项目中直接引入  
将下面的 x.y.z 更改为上面显示的版本号   
`
compile 'com.bintray.library:payshare:x.y.z'
`   

二、项目中的配置

2.1 在application中初始化各个平台的信息  
``` 
 PayShareApplication.getInstance()
                .setIsDebug(true)
                .initPayShare(this, "xxxx")
                .registerAppToWeChat(this, "xxxx")
                .setWeixin("xxxx", "xxxx")
                .setQQZone("xxxx", "xxxx")
                .setSinaWeibo("xxxx", "xxxx");
``` 
2.2 在项目的build.gradle中添加如下代码（在最外层添加，不要添加到任何标签中） 
``` 
ext {
    payshareLibs = projectDir.getPath() + "/payshare/libs"
}
``` 
2.3 在AndroidManifest.xml中配置(注意修改QQ分享配置的appid为自己申请的)
```
权限，根据自己项目去除重复的权限，有提示缺少其他权限，添加即可
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
  <uses-permission android:name="android.permission.READ_PHONE_STATE" />
  
  <application>
  <!-- 微信分享 -->
        <activity
            android:name=".wxapi.WXEntryActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@android:style/Theme.Translucent" />
        <activity
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@android:style/Theme.Translucent" />
        <!-- QQ分享 -->
        <activity
            android:name="com.tencent.tauth.AuthActivity"
            android:launchMode="singleTask"
            android:noHistory="true">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <!-- 修改此处的appid，只替换数字即可 -->
                <data android:scheme="tencent1106674812" />
            </intent-filter>
        </activity>
        <activity
            android:name="com.tencent.connect.common.AssistActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

        <!--新浪： -->
        <activity
            android:name="com.umeng.socialize.media.WBShareCallBackActivity"
            android:configChanges="keyboardHidden|orientation"
            android:exported="false"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

        <activity
            android:name="com.sina.weibo.sdk.share.WbShareTransActivity"
            android:launchMode="singleTask"
            android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen">
            <intent-filter>
                <action android:name="com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>
    </application>
  
```
2.4 将自己的打包签名文件放置在app模块中（和src文件夹同级别），在build.gradle中配置
```
android {
//修改下面信息umeng分享（签名文件如果不加，不配置，debug模式不能授权和分享）
 signingConfigs {
        debug {
            storeFile file('签名文件名带后缀')//如：'payshare.keystore'
            storePassword "密码"
            keyAlias "别名"
            keyPassword "密码"
        }
    }
 }
 
 repositories {
        flatDir {
        //因为依赖库中使用了使用了libs中的aar和jar文件，所以需要指明要虚调用的libs路径，路径在2.2中已经配置好了
            dirs 'libs', rootProject.ext.payshareLibs
        }
    }
```
2.5 将项目中的wxapi包整个复制到自己项目包名根路径下，不能放置在其他路径下（这是微信相关必须配置的）
```
在调用分享的Activity中添加如下代码，不添加与QQ相关分享授权无法监听回调
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
  }
 ```
三、授权、分享、支付相关调用   
授权 
```
  //调用微信授权
  AuthorizedLogin.getInstance().userWeiChatLogin(this);
  //调用QQ授权
  AuthorizedLogin.getInstance().userQQLogin(this);
  //调用微博授权
  AuthorizedLogin.getInstance().useSinaLogin(this);
  //设置授权结果回调监听，AuthorizeLoginListener 剩余的自己处理吧
  AuthorizedLogin.getInstance().setAuthorizedLoginListener(this);
```
分享  
```
//设置分享数据，这里支持分享链接、纯图片（网络图和bitmap格式）
ShareManager.init(mActivity).setShareData(imgUrl);
//设置分享行为，参数传对应的行为
ShareManager.init(mActivity).shareAction(SHARE_WEIXIN);
//分享结果监听，微信6.7.2以后分享返回结果统一返回都为成功，
ShareManager.init(mActivity).setShareCallBackListener(new ShareListener());
 
```
支付
```
//微信支付
PayManager.init(this).goToWeChatPay(this, weChatPayDataObj);

注意weChatPayDataObj格式为如下json数据，字段不要修改，否则无法获取到
      {
        "appid":"xxxxxxxx",
        "noncestr":"xxxxxxxx",
        "package":"Sign=WXPay",
        "partnerid":"xxxxxxxx",
        "prepayid":"xxxxxxxx",
        "sign":"xxxxxxxx",
        "timestamp":"xxxxxxxx"
    }

//支付宝支付，支付宝的相对简单直接是一长串字符串，这里就不示例了
PayManager.init(this).goToAliPay(this, payDataObj);


阿里的支付结果监听
PayManager.init(this).setPaymentListener();

微信的支付监听在项目配置的2.5中的WXPayEntryActivity类中处理
```

好了，该说的基本都说了，应该没问题了，有问题就在Issues提出来吧
