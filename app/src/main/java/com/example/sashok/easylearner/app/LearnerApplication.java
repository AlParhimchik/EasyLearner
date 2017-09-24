package com.example.sashok.easylearner.app;

import android.app.Application;

import com.example.sashok.easylearner.realm.MyMigration;
import com.example.sashok.easylearner.realm.RealmController;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sashok on 23.9.17.
 */

public class LearnerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .migration(new MyMigration())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }

}

