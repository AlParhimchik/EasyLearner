package com.example.sashok.easylearner.realm;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * Created by sashok on 23.9.17.
 */

public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        ///show version and add code
    }
}
