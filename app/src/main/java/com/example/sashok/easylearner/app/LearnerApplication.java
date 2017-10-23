package com.example.sashok.easylearner.app;

import android.app.Application;
import android.content.Context;

import com.example.sashok.easylearner.realm.MyMigration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sashok on 30.9.17.
 */

public class LearnerApplication extends Application {

    private static LearnerApplication instance;

    public static LearnerApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(3)
                .migration(new MyMigration())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }
    public static Context getContext() {
        return instance.getApplicationContext();
        // or return instance.getApplicationContext();
    }
}
