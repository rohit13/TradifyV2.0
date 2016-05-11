package com.hello_world.ronak.tradify;

import android.app.Application;

import com.firebase.client.Firebase;
import com.batch.android.Batch;
import com.batch.android.Config;
/**
 * Created by ronak_000 on 4/15/2016.
 */
public class ProductsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        // other setup code
        Batch.Push.setGCMSenderId("324126125500");
        //Batch.Push.setManualDisplay(true);
        Batch.setConfig(new Config("AIzaSyBCfqM-iAXLF8oKi34krJwgAe1PLJv7jNg"));
    }

}
