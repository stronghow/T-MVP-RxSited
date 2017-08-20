package com;

import android.text.TextUtils;

import com.base.event.OkBus;
import com.model.Hots;
import com.model.dtype1;
import com.model.Search;
import com.model.Sections;
import com.model.SourceModel;
import com.model.Tag;
import com.model.Tags;
import com.sited.RxSource;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.C.QueryKey;

/**
 * @apiNote SiteD 获取本地数据库工厂
 * Created by baixiaokang on 17/1/25.
 */
@SuppressWarnings("unchecked")
public class DbFactory {

    /**
     * @apiNote 获取源码本地数据
     * @param param
     * @return Observable
     */
    public static Flowable<List<SourceModel>> getSource(HashMap<String, Object> param){
//        return Flowable.create(new FlowableOnSubscribe<List>() {
//            @Override
//            public void subscribe(FlowableEmitter<List> e) throws Exception {
//                Realm realm = Realm.getDefaultInstance();
//                e.onNext(realm.copyFromRealm(
//                            realm.where(SourceModel.class)
//                                .findAll()));
//            }
//        }, BackpressureStrategy.BUFFER)
//                .subscribeOn(Schedulers.io());
        return Flowable.just(
                Realm.getDefaultInstance().copyFromRealm(
                Realm.getDefaultInstance()
                .where(SourceModel.class)
                .findAll()))
                .subscribeOn(Schedulers.io());
    }



    /**
     * @apiNote 获取hots节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Flowable<RealmResults<Hots>> getHots(HashMap<String, Object> param){
        return null;
    }

    /**
     * @apiNote 获取tags节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Flowable<List<Tags>> getTags(HashMap<String, Object> param){
//        return Flowable.create(new FlowableOnSubscribe<List>() {
//            @Override
//            public void subscribe(FlowableEmitter<List> e) throws Exception {
//                Realm realm = Realm.getDefaultInstance();
//                e.onNext(realm.copyFromRealm(
//                        realm.where(Tags.class)
//                                .equalTo(C.QueryKey,(String)param.get(C.URL))
//                                .findAll()));
//            }
//        }, BackpressureStrategy.BUFFER)
//                .subscribeOn(Schedulers.io());
        return Flowable.just(
                Realm.getDefaultInstance().copyFromRealm(
                Realm.getDefaultInstance()
                .where(Tags.class)
                .equalTo(QueryKey,(String)param.get(C.URL))
                .findAll()))
                .subscribeOn(Schedulers.io());
    }


    /**
     * @apiNote 获取tag节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Flowable<List<Tag>> getTag(HashMap<String, Object> param){

//        return Flowable.create(new FlowableOnSubscribe<List>() {
//            @Override
//            public void subscribe(FlowableEmitter<List> e) throws Exception {
//                Realm realm = Realm.getDefaultInstance();
//                e.onNext(realm.copyFromRealm(
//                        realm.where(Tag.class)
//                                .equalTo(C.QueryKey,((Tags)param.get(C.MODEL)).url + param.get(C.PAGE))
//                                .findAll()));
//            }
//        }, BackpressureStrategy.BUFFER)
//                .subscribeOn(Schedulers.io());

//        KLog.json("getTag=" + ((Tags)param.get(C.MODEL)).url + param.get(C.PAGE));
        return Flowable.just(
                Realm.getDefaultInstance().copyFromRealm(
                        Realm.getDefaultInstance()
                                .where(Tag.class)
                                .equalTo(QueryKey,((Tags)param.get(C.MODEL)).url + param.get(C.PAGE))
                                .findAll()))
                .subscribeOn(Schedulers.io());
    }

    /**
     * @apiNote 获取Search节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Flowable<List<Search>> getSearch(HashMap<String, Object> param){
//        return Flowable.create(new FlowableOnSubscribe<List>() {
//            @Override
//            public void subscribe(FlowableEmitter<List> e) throws Exception {
//                Realm realm = Realm.getDefaultInstance();
//                e.onNext(realm.copyFromRealm(
//                        realm.where(Search.class)
//                                .equalTo(C.QueryKey,((String)param.get(C.URL)) + param.get(C.KEY))
//                                .findAll()));
//            }
//        }, BackpressureStrategy.BUFFER)
//                .subscribeOn(Schedulers.io());
        return Flowable.just(
                Realm.getDefaultInstance().copyFromRealm(
                        Realm.getDefaultInstance()
                                .where(Search.class)
                                .equalTo(QueryKey,((String)param.get(C.URL)) + param.get(C.KEY))
                                .findAll()))
                     .subscribeOn(Schedulers.io());
    }

    /**
     * @apiNote 获取updates节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Flowable getUpdates(HashMap<String, Object> param){
        return null;
    }

    /**
     * @apiNote 获取book节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Flowable<List<Sections>> getBook(HashMap<String, Object> param){
        Tag model = (Tag)param.get(C.MODEL);
//        return Flowable.create(new FlowableOnSubscribe<List>() {
//            @Override
//            public void subscribe(FlowableEmitter<List> e) throws Exception {
//                Realm realm = Realm.getDefaultInstance();
//                e.onNext(realm.copyFromRealm(
//                        realm.where(Sections.class)
//                                .equalTo(C.QueryKey,model.url)
//                                .findAll()));
//            }
//        }, BackpressureStrategy.BUFFER)
//                .subscribeOn(Schedulers.io());

       return Flowable.just(
               Realm.getDefaultInstance().copyFromRealm(
               Realm.getDefaultInstance()
               .where(Sections.class)
               .equalTo(QueryKey,model.url)
               .findAll()))
               .subscribeOn(Schedulers.io());
    }

    /**
     * @apiNote 获取Section节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static  Flowable<List> getSection(HashMap<String, Object> param){
        KLog.json("DbFactory::getSection");
        SitedManage.databind databind = (SitedManage.databind) param.get(C.DATABIND);
        Sections model = (Sections) param.get(C.MODEL);
        if(databind.isGoBook ) {
            final int index = (int) param.get(C.INDEX);
            List<Sections> sectionsList = (ArrayList<Sections>) param.get(C.SECTIONS);
            int page = (int) param.get(C.PAGE) - 1;
            KLog.json("page=" + page + " index=" + index);
            KLog.json("model=" + model.toString());
            if (index + page == sectionsList.size()) { //已经到最底部
                KLog.json("已经到最底部");
                return Observable_NULL();
            }
            model = sectionsList.get(index + page);
            while (TextUtils.isEmpty(model.url)) { //跳过分组标题
                page++;
                if (index + page == sectionsList.size()) { //已经到最底部
                    KLog.json("已经到最底部");
                    return Observable_NULL();
                }
                model = sectionsList.get(index + page);
                KLog.json(model.name + model.url + model.QueryKey);
            }
        }

        OkBus.getInstance().onEvent(EventTags.SET_BOOKNAME,model.name);
        //((Section1Activity)App.getCurActivity()).mViewBinding.listItem.setHashMap(C.BOOKNAME,model.name);

        final String QueryKey = model.url;
        return Flowable.create(new FlowableOnSubscribe<List>() {
            @Override
            public void subscribe(FlowableEmitter<List> e) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                e.onNext(realm.copyFromRealm(
                                realm.where(databind.model)
                                .equalTo(C.QueryKey,QueryKey)
                                .findAll()));
            }
        }, BackpressureStrategy.BUFFER)
        .subscribeOn(Schedulers.io());

//        return Flowable.just(
//                Realm.getDefaultInstance().copyFromRealm(
//                Realm.getDefaultInstance()
//                .where(dtype1.class)
//                .equalTo(C.QueryKey,QueryKey)
//                .findAll()))
//                .subscribeOn(Schedulers.io());
    }

    /**
     * @apiNote 获取空本地数据的Observable
     * @param
     * @return Observable
     */
    private static  Flowable<List> Observable_NULL(){
        return Flowable.just(new ArrayList<>(0));
    }
}
