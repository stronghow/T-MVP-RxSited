package com.dao.db;


import com.C;
import com.model.LookModel;
import com.model.Sections;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;


/**
 * Created by yuety on 15/8/21.
 */
public class SiteDbApi {

    public static void insertOrUpdate(Collection<? extends RealmModel> objects){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(objects);
        realm.commitTransaction();
    }

    public static void insertOrUpdate(RealmModel objects){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(objects);
        realm.commitTransaction();
    }

    public static LookModel getLastLook(String QueryKey){
        return Realm.getDefaultInstance()
                .where(LookModel.class)
                .equalTo(C.QueryKey,QueryKey)
                .findFirst();
    }

    private static void setLastLook(String QueryKey,String url,int index){
        LookModel model = new LookModel();
        model.QueryKey = QueryKey;
        model.url = url;
        model.index = index;
        insertOrUpdate(model);
    }

    public static void updateLastLook(Sections oldmodel,Sections newmodel,int index){
        //设置book::sections记录
        //记录数据位置
        setLastLook(newmodel.QueryKey,newmodel.url,index);
        oldmodel.isLook = false;
        newmodel.isLook = true;
        List<Sections> sectionses = new ArrayList<>();
        sectionses.add(oldmodel);
        sectionses.add(newmodel);
        insertOrUpdate(sectionses);
    }
}
