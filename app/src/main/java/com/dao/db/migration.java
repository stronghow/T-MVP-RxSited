package com.dao.db;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by haozhong on 2017/5/2.
 */

public class migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
//        if(oldVersion==0){
//            oldVersion++;
//        }else if(oldVersion==1){
//            schema.get("LookModel")
//                    .addField("index",Integer.class);
//            oldVersion++;
//        }else if(oldVersion==2){
//            schema.get("LookModel")
//                    .addField("index",Integer.class);
//            oldVersion++;
//        }
    }
}
