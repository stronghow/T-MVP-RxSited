package com;

import android.text.TextUtils;

import com.base.entity.DataArr;
import com.base.util.helper.RxSchedulers;
import com.dao.SourceApi;
import com.dao.engine.DdSource;
import com.model.Hots;
import com.model.LookModel;
import com.model.MainViewModel;
import com.model.PicModel;
import com.model.Sections;
import com.model.SourceModel;
import com.model.Tag;
import com.model.Tags;
import com.socks.library.KLog;
import com.ui.Section1.Section1Activity;

import org.noear.sited.SdSourceCallback;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;

import static com.C.QueryKey;

/**
 * Created by baixiaokang on 17/1/25.
 */

public class DbFactory {
    public static Observable getSource(HashMap<String, Object> param){
        return Observable.defer(() -> Realm.getDefaultInstance()
                .where(SourceModel.class)
                .findAll().asObservable());
    }

    public static Observable getHots(HashMap<String, Object> param){
        PublishSubject mSubject = PublishSubject.create();
        KLog.json("进入Observable");
        DdSource source = (DdSource) param.get(C.SOURCE);
        if(source == null){
            source = SourceApi.getByUrl((String)param.get(C.URL));
            if(source == null) {
                KLog.json("source == null");
                mSubject.onNext(null);
                mSubject.onCompleted();
            }
        }

        MainViewModel viewModel = new MainViewModel();
        source.getNodeViewModel(viewModel, source.home, (boolean) param.get(C.ISUPDATE), new SdSourceCallback() {
            @Override
            public void run(Integer code) {
                if(code == 1) {
                    DataArr<Hots> data = new DataArr<>();
                    data.results = viewModel.hotList;
                    KLog.json("tagList.size:"+viewModel.tagList.size());
                    //OkBus.getInstance().onEvent(EventTags.SHOW_TAG_LIST, viewModel.tagList);
                    mSubject.onNext(data);
                    mSubject.onCompleted();
                }
            }
        });

        return mSubject.compose(RxSchedulers.io_main());
    }

    public static Observable getTags(HashMap<String, Object> param){
        return Observable.defer(() -> Realm.getDefaultInstance()
                .where(Tags.class)
                .equalTo(QueryKey,(String)param.get(C.URL))
                .findAll().asObservable());
    }

    public static Observable getTag(HashMap<String, Object> param){
        KLog.json("getTag=" + ((Tags)param.get(C.MODEL)).url + param.get(C.PAGE));
        return Observable.defer(()->Realm.getDefaultInstance()
                .where(Tag.class)
                .equalTo(QueryKey,((Tags)param.get(C.MODEL)).url + param.get(C.PAGE))
                .findAll().asObservable());
    }

    public static Observable getUpdates(HashMap<String, Object> param){
        return Observable.create(new Observable.OnSubscribe<DataArr<Hots>>() {
            @Override
            public void call(Subscriber<? super DataArr<Hots>> subscriber) {
                KLog.json("进入Observable");
                DdSource source = (DdSource) param.get(C.SOURCE);
                if(source == null){
                    source = SourceApi.getByUrl((String)param.get(C.URL));
                    if(source == null) {
                        subscriber.onNext(null);
                        subscriber.onCompleted();
                    }
                }

                MainViewModel viewModel = new MainViewModel();
                source.getNodeViewModel(viewModel, source.home, (boolean) param.get(C.ISUPDATE), new SdSourceCallback() {
                    @Override
                    public void run(Integer code) {
                        if(code == 1){
                            DataArr<Hots> data = new DataArr<>();
                            data.results = viewModel.hotList;
                            subscriber.onNext(data);
                        }
                    }
                });
            }
        }).compose(RxSchedulers.io_main());
    }

    public static Observable getBook(HashMap<String, Object> param){
        Tag model = (Tag)param.get(C.MODEL);
        KLog.json("getBook=" + model.url);
        LookModel lookModel = Realm.getDefaultInstance()
                .where(LookModel.class)
                .equalTo(C.QueryKey,model.url)
                .findFirst();
        if(lookModel != null){
            C.oldIndex = lookModel.index;
        }
        return Observable.defer(() ->{
            RealmResults results = Realm.getDefaultInstance()
                    .where(Sections.class)
                    .equalTo(QueryKey,model.url)
                    .findAll();
            C.sSectionses = (ArrayList<Sections>) Realm.getDefaultInstance().copyFromRealm(results);
            //C.sSectionses = sectionses;
            KLog.json("C.sSectionses.size()="+C.sSectionses.size());
            return results.asObservable();
        });
    }

    public static Observable getSection(HashMap<String, Object> param){
        PublishSubject mSubject = PublishSubject.create();
        KLog.json("DbFactory::getSection");
        Sections model = (Sections) param.get(C.MODEL);
        int index = model.index;
        int page = (int)param.get(C.PAGE)-1;
        //sectionses = C.sSectionses;
        KLog.json("page=" + page + " index" + index);
        KLog.json("model=" + model.toString());
        if(index + page == C.sSectionses.size()) { //已经到最底部
            KLog.json("已经到最底部");
            return Observable_NULL();
        }
        model = C.sSectionses.get(index + page);
        while (TextUtils.isEmpty(model.url)){ //跳过分组标题
            page++;
            if(index + page == C.sSectionses.size()) { //已经到最底部
                KLog.json("已经到最底部");
                return Observable_NULL();
            }
            model = C.sSectionses.get(index + page);
            KLog.json(model.name + model.url + model.QueryKey);
        }

        C.newIndex = model.index;


        ((Section1Activity)App.getCurActivity()).mViewBinding.listItem.setHashMap(C.BOOKNAME,model.name);

        final String QueryKey = model.url;
        final Sections mmodel = model;

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(0);
                subscriber.onCompleted();
            }
        }).compose(RxSchedulers.io_main())
                .doOnCompleted(()->mSubject.onNext(Realm.getDefaultInstance()
                        .where(PicModel.class)
                        .equalTo(C.QueryKey,QueryKey)
                        .findAll()))
                .subscribe(integer -> {
                    //SiteDbApi.updateLastlook(C.sSectionses,mmodel)
                });

//        return Observable.defer(() -> Realm.getDefaultInstance()
//                .where(PicModel.class)
//                .equalTo(C.QueryKey,QueryKey)
//                .findAll().asObservable());

        return mSubject.compose(RxSchedulers.io_main());
    }

    private static Observable Observable_NULL(){
       return Observable.defer(() -> Realm.getDefaultInstance()
                .where(PicModel.class)
                .equalTo(C.QueryKey, "")
                .findAll().asObservable());
    }
}
