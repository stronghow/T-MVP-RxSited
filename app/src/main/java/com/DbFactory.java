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

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.C.QueryKey;

/**
 * @apiNote SiteD 获取本地数据库工厂
 * Created by baixiaokang on 17/1/25.
 */

public class DbFactory {

    /**
     * @apiNote 获取源码本地数据
     * @param param
     * @return Observable
     */
    public static Observable getSource(HashMap<String, Object> param){
        return Observable.just(Realm.getDefaultInstance()
                .where(SourceModel.class)
                .findAll()).compose(RxSchedulers.io_main());
    }



    /**
     * @apiNote 获取hots节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Observable getHots(HashMap<String, Object> param){
        PublishSubject mSubject = PublishSubject.create();
        KLog.json("进入Observable");
        DdSource source = (DdSource) param.get(C.SOURCE);
        if(source == null){
            source = SourceApi.getByUrl((String)param.get(C.URL));
            if(source == null) {
                KLog.json("source == null");
                mSubject.onNext(null);
                mSubject.onComplete();
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
                    mSubject.onComplete();
                }
            }
        });

        return mSubject.compose(RxSchedulers.io_main());
    }

    /**
     * @apiNote 获取tags节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Observable<RealmResults<Tags>> getTags(HashMap<String, Object> param){
        return Observable.just(Realm.getDefaultInstance()
                .where(Tags.class)
                .equalTo(QueryKey,(String)param.get(C.URL))
                .findAll());
    }


    /**
     * @apiNote 获取tag节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Observable getTag(HashMap<String, Object> param){
        KLog.json("getTag=" + ((Tags)param.get(C.MODEL)).url + param.get(C.PAGE));
        return Observable.just(Realm.getDefaultInstance()
                        .where(Tag.class)
                        .equalTo(QueryKey,((Tags)param.get(C.MODEL)).url + param.get(C.PAGE))
                        .findAll()).compose(RxSchedulers.io_main());
    }

    /**
     * @apiNote 获取updates节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Observable getUpdates(HashMap<String, Object> param){
//        return Observable.create(new Observable.OnSubscribe<DataArr<Hots>>() {
//            @Override
//            public void call(Subscriber<? super DataArr<Hots>> subscriber) {
//                KLog.json("进入Observable");
//                DdSource source = (DdSource) param.get(C.SOURCE);
//                if(source == null){
//                    source = SourceApi.getByUrl((String)param.get(C.URL));
//                    if(source == null) {
//                        subscriber.onNext(null);
//                        subscriber.onCompleted();
//                    }
//                }
//
//                MainViewModel viewModel = new MainViewModel();
//                source.getNodeViewModel(viewModel, source.home, (boolean) param.get(C.ISUPDATE), new SdSourceCallback() {
//                    @Override
//                    public void run(Integer code) {
//                        if(code == 1){
//                            DataArr<Hots> data = new DataArr<>();
//                            data.results = viewModel.hotList;
//                            subscriber.onNext(data);
//                        }
//                    }
//                });
//            }
//        }).compose(RxSchedulers.io_main());
        return null;
    }

    /**
     * @apiNote 获取book节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
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
        RealmResults results = Realm.getDefaultInstance()
                .where(Sections.class)
                .equalTo(QueryKey,model.url)
                .findAll();
        // TODO: 2017/5/20 这里不能修改 容易崩溃
        C.sSectionses = (ArrayList<Sections>)Realm.getDefaultInstance().copyFromRealm(results);

       return Observable.just(results).compose(RxSchedulers.io_main());
    }

    /**
     * @apiNote 获取Section节点本地数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Observable getSection(HashMap<String, Object> param){
        KLog.json("DbFactory::getSection");
        Sections model = (Sections) param.get(C.MODEL);
        int index = model.index;
        int page = (int)param.get(C.PAGE)-1;
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

        return Observable.just(Realm.getDefaultInstance()
                .where(PicModel.class)
                .equalTo(C.QueryKey,QueryKey)
                .findAll()).compose(RxSchedulers.io_main());
    }

    /**
     * @apiNote 获取空本地数据的Observable
     * @param
     * @return Observable
     */
    private static Observable<RealmResults> Observable_NULL(){
        // TODO: 2017/5/20  无法解决显示正在加载问题
        //return null;
        return Observable.empty();
//        return Observable.just(Realm.getDefaultInstance().where(LookModel.class).equalTo(C.QueryKey,"").findAll())
//                         .compose(RxSchedulers.io_main());
    }
}
