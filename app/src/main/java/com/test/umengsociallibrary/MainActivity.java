package com.test.umengsociallibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

public class MainActivity extends AppCompatActivity {

    private final UMSocialService mController =
            UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
