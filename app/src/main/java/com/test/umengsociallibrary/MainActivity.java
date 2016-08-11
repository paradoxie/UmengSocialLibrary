package com.test.umengsociallibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;

public class MainActivity extends Activity {

    private Button btn_1, btn_2;
    private final UMSocialService mSocialService = UMServiceFactory.getUMSocialService("com.umeng.share");
    //友盟分享Controller
    private final UMSocialService mController =
            UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                自带分享();
            }
        });
        准备分享();
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                自定义分享(MainActivity.this, mController, SHARE_MEDIA.QQ, new QQShareContent());
            }
        });
    }

    private void 自带分享() {
        //        mSocialService.setShareContent(getString(R.string.sms_spread_text));
        //        mSocialService.setShareMedia(new UMImage(MainActivity.this, R.mipmap.ic_launcher));
        //        mSocialService.getConfig().removePlatform(SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
        //
        //        // 添加微信平台
        //        UMWXHandler wxHandler = new UMWXHandler(MainActivity.this, "appId", "appSecret");
        //        wxHandler.addToSocialSDK();
        //        wxHandler.setTitle(getString(R.string.app_name));
        //        wxHandler.setTargetUrl(getString(R.string.sms_spread_url));
        //        // 添加微信朋友圈
        //        UMWXHandler wxCircleHandler = new UMWXHandler(MainActivity.this, "appId", "appSecret");
        //        wxCircleHandler.setToCircle(true);
        //        wxCircleHandler.setTitle(getString(R.string.app_name));
        //        wxCircleHandler.setTargetUrl(getString(R.string.sms_spread_url));
        //        wxCircleHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(MainActivity.this, "APP ID",
                "APP kEY");
        qqSsoHandler.setTitle(getString(R.string.app_name));
        qqSsoHandler.setTargetUrl(getString(R.string.sms_spread_url));
        qqSsoHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(MainActivity.this, "APP ID",
                "APP kEY");
        qZoneSsoHandler.setTargetUrl(getString(R.string.sms_spread_url));
        qZoneSsoHandler.addToSocialSDK();

        mSocialService.openShare(MainActivity.this, false);

    }

    private void 准备分享() {

        //        // 添加微信平台
        //        UMWXHandler wxHandler = new UMWXHandler(this, Common.APP_ID, Common.APP_SECRET);
        //        wxHandler.addToSocialSDK();
        //        // 支持微信朋友圈
        //        UMWXHandler wxCircleHandler = new UMWXHandler(this, Common.APP_ID, Common.APP_SECRET);
        //        wxCircleHandler.setToCircle(true);
        //        wxCircleHandler.addToSocialSDK();
        //QQ分享
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1105529778",
                "fZ1fXiLaOyNmvRe4");
        qqSsoHandler.addToSocialSDK();
        //QQ空间分享
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "APP_ID",
                "APP kEY");
        qZoneSsoHandler.addToSocialSDK();
        //        //新浪微博
        //        SinaSsoHandler sinaSsoHandler = new SinaSsoHandler(this);
        //        sinaSsoHandler.addToSocialSDK();
    }

    private void 自定义分享(Context context, final UMSocialService mController, SHARE_MEDIA var, BaseShareContent content) {
        //        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字
        content.setShareContent("唉~！可惜了.下载地址：www.xxxxx.com");
        //设置title
        content.setTitle("屠龙宝刀，点击就送！");
        //设置分享内容跳转URL
        content.setTargetUrl("www.baidu.com");
        //设置分享图片
        UMImage img = new UMImage(context, R.mipmap.ic_launcher);

        content.setShareImage(img);
        mController.setShareMedia(content);
        setShareBtn(context, mController, var);
    }

    /**
     * 设置自定义监听
     *
     * @param context     上下文
     * @param mController 分享服务
     * @param var         平开类型
     */
    private static void setShareBtn(final Context context, final UMSocialService mController, SHARE_MEDIA var) {

        mController.postShare(context, var,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA arg0, int eCode,
                                           SocializeEntity arg2) {
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


}
