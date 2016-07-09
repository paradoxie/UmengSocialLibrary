# UmengSocialLibrary
## 友盟的分享，每次都单独配置，有点麻烦，整个库

Android的分享功能友盟用的蛮多的，一般情况下**自带的分享面板**就满足需求了，但是也有一些情况下需要在自己的项目里点击按钮直接跳转分享，所以自带的就不太给力了。这里记录一下项目里**自定义分享功能**，官方文档里也说明了需要怎么处理，但是对于刚接触的人来说可能就不是那么明了了。这里记录一下下，看看文档，然后开搞！  

     注：由于腾讯开放平台增加包名、签名校验，因此使用QQ、Qzone的分享、授权功能的开发者
        必须使用自己在腾讯开放平台申请获取的QQ APPID，否则将无法正常使用。


## 一、配置  **由于我把整个分享部分弄成依赖库了，所以除开QQ微信必须的东西，只需要依赖就行了**
>* 1.注册下载SDK，准备QQ微信的ID啊什么的。
>* 2.配置SDK。

   在项目的build.gradle加入代码

        allprojects {
        		repositories {
        			...
        			maven { url "https://jitpack.io" }
        		}
        	}

   在module的build.gradle加入

        	dependencies {
        	        compile 'com.github.xzq35110:UmengSocialLibrary:0.2'
        	}
>* 3.配置manifest.xml的权限和必要的activity

### 这样就能调用到友盟的方法了！

## 二、友盟集成的分享

添加友盟分享Controller成员变量

    private final UMSocialService mSocialService =
    UMServiceFactory.getUMSocialService("com.umeng.share");

在唤起分享的点击事件中加入：

    mSocialService.setShareContent("分享内容");
    mSocialService.setShareMedia(new UMImage(SettingActivity.this, 图标));
    mSocialService.getConfig().removePlatform(SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);

// 添加微信平台

    UMWXHandler wxHandler = new UMWXHandler(SettingActivity.this, appId, appSecret);
    wxHandler.addToSocialSDK();
    wxHandler.setTitle(getString(R.string.app_name));
    wxHandler.setTargetUrl(getString(R.string.sms_spread_url));

// 添加微信朋友圈

    UMWXHandler wxCircleHandler = new UMWXHandler(SettingActivity.this, appId, appSecret);
    wxCircleHandler.setToCircle(true);
    wxCircleHandler.addToSocialSDK();
    wxCircleHandler.setTitle(getString(R.string.app_name));
        wxCircleHandler.setTargetUrl(getString(R.string.sms_spread_url));
//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY 

    UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(SettingActivity.this, "APP ID", "APP KEY");
    qqSsoHandler.addToSocialSDK();
    qqSsoHandler.setTitle(getString(R.string.app_name));
    qqSsoHandler.setTargetUrl(getString(R.string.sms_spread_url));

    //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.

    QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(SettingActivity.this, "APP ID","APP KEY");
    qZoneSsoHandler.addToSocialSDK();
    qZoneSsoHandler.setTargetUrl(getString(R.string.sms_spread_url)); 

最后

     mSocialService.openShare(SettingActivity.this, false);

## 三、自定义分享面板

首先看[官方文档](http://dev.umeng.com/social/android/android-update?spm=0.0.0.0.m3OtYE#6")的做法

    new ShareAction(this)
    .setPlatform(SHARE_MEDIA.SINA)
    .setCallback(umShareListener)
    .withText("hello umeng video")
    .withTargetUrl("http://www.baidu.com")
    .withMedia(image).share();

传入了umShareListener,需要重写回调接口:
    
    new UMShareListener() { 
        @Override 
        public void onResult(SHARE_MEDIA platform) {  
               Toast.makeText(ShareActivity.this,platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
         } 

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) { 
               Toast.makeText(ShareActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHOR T).show(); 
        } 
        @Override 
        public void onCancel(SHARE_MEDIA platform) { 
                Toast.makeText(ShareActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show(); 
        } 
      };

讲真，第一次看有点蒙逼。。。


>*假装有条分割线。。。。。。。。


#开始搞自己的：


首先，自己的面板也就是没有面板。。。也就是随便来个按钮都能唤起分享，所以需要来个方法响应点击事件。在此之前，需要将支持的平台初始化加入到SocialSDK,初始化控件之前：

    private void prepareShare(){
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, Common.APP_ID, Common.APP_SECRET);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, Common.APP_ID, Common.APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
        //支持QQ
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "APP ID","APP KEY");
        qqSsoHandler.addToSocialSDK();
        //QQ空间分享
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "APP ID","APP KEY");
        qZoneSsoHandler.addToSocialSDK();
        //新浪微博不用这么干
        //SinaSsoHandler sinaSsoHandler = new SinaSsoHandler(this);
        //sinaSsoHandler.addToSocialSDK();
	}

类成员变量mController：
    
        private final UMSocialService mController =             
        UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);


然后仿造官方的开始封装自定义分享方法，回调监听,抽象出了很多参数，这样在点击事件中可以直接传值。

        /** 
        * 友盟社会化分享自定义 *
        * @param context     上下文
        * @param mController 分享服务 
        * @param var         平台类型 
        * @param content     平台 
        */
        public static void shareToFriend(Context context, final UMSocialService mController, SHARE_MEDIA var, BaseShareContent content) {    
        //        WeiXinShareContent weixinContent = new WeiXinShareContent();    
                //设置分享文字    
                content.setShareContent("内容");    
                //设置title    
                content.setTitle("标题");    
                //设置分享内容跳转URL
                content.setTargetUrl("WWW.BAIDU.COM" );    
                //设置分享图片    
                UMImage img = new UMImage(context, R.drawable.ic_launcher);    
                content.setShareImage(img);
                mController.setShareMedia(content);    
                setShareBtn(context, mController, var);
        }

        /** 
        * 设置自定义监听 * 
        * @param context     上下文 
        * @param mController 分享服务 
        * @param var         平开类型 
        */
        private static void setShareBtn(final Context context, final UMSocialService mController, SHARE_MEDIA var) {  
        mController.postShare(context, var, new SocializeListeners.SnsPostListener() {
                @Override                
                public void onStart() {                
                  }               
             @Override                
                public void onComplete(SHARE_MEDIA arg0, int eCode,SocializeEntity arg2) {
                    if (eCode == 200) {
                    } else {
                        String eMsg = "";
                        if (eCode == -101) {
                            eMsg = "没有授权";
                        }
                    } 
               }
            });
}

比如上面的**SHARE_MEDIA**参数，即可传入SHARE_MEDIA.WEIXIN，微博QQ类推。
而最后的参数为**BaseShareContent**类型，实际传参是new出平台对象就可以了，如：new QZoneShareContent()作为参数传入方法。

#### 封装后的点击事件

        //新浪微博
        shareToFriend(this, mController, SHARE_MEDIA.SINA, new SinaShareContent());
        //QQ空间
        shareToFriend(this, mController, SHARE_MEDIA.QZONE, new QZoneShareContent());
        //QQ
        shareToFriend(this, mController, SHARE_MEDIA.QQ, new QQShareContent());
        //微信
        shareToFriend(this, mController, SHARE_MEDIA.WEIXIN, new WeiXinShareContent());
        //微信朋友圈
        shareToFriend(this, mController, SHARE_MEDIA.WEIXIN_CIRCLE, new CircleShareContent());


## 到这里就OK啦(*^__^*)
## 更加直观系统的使用参看项目内Demo
