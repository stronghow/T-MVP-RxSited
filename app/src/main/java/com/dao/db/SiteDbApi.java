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

    public static String getLastLook(String QueryKey){
        RealmResults results = Realm.getDefaultInstance().where(LookModel.class).equalTo(C.QueryKey,QueryKey).findAll();
        if(results.size() != 0){
            return ((LookModel)results.get(0)).url;
        }
        return null;
    }

    public static void setLastLook(String QueryKey,String url,int index){
        LookModel model = new LookModel();
        model.QueryKey = QueryKey;
        model.url = url;
        model.index = index;
        insertOrUpdate(model);
    }

    public static void updateLastlook(Sections oldmodel,Sections newmodel){
        //设置book::sections记录
        //记录数据位置
        SiteDbApi.setLastLook(newmodel.QueryKey,newmodel.url,newmodel.index);
        KLog.json("oldmodel = " + oldmodel.name + "newmodel = " + newmodel.name);
        oldmodel.isLook = false;
        newmodel.isLook = true;
        List<Sections> sectionses = new ArrayList<>();
        sectionses.add(oldmodel);
        sectionses.add(newmodel);
        SiteDbApi.insertOrUpdate(sectionses);
    }

    public static void updateLastlook(List<Sections> sectionses,Sections model){
        //设置book::sections记录
        //记录数据位置

        SiteDbApi.setLastLook(model.QueryKey,model.url,model.index);

        Observable.fromIterable(sectionses)
                .filter(sections -> sections.isLook||(sections.url != null && sections.index == model.index))
                .map(sections -> {sections.isLook = !sections.isLook; return sections;})
                .subscribe((data)->{
                    SiteDbApi.insertOrUpdate(data);
                });
    }
}
