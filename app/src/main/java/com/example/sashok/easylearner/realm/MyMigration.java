package com.example.sashok.easylearner.realm;

import com.example.sashok.easylearner.model.Word;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by sashok on 23.9.17.
 */

public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        ///show version and add code
        RealmSchema schema = realm.getSchema();
        if (oldVersion==0){
            schema.get("Word").addField("transcription",String.class);
            oldVersion++;
        }
    }
}
