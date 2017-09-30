package com.example.sashok.easylearner.app;

import android.app.Application;

import com.example.sashok.easylearner.realm.MyMigration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sashok on 30.9.17.
 */

public class LearnerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(1)
                .migration(new MyMigration())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }
}
