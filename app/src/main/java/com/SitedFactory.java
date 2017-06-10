package com;

import android.text.TextUtils;

import com.base.util.helper.RxSchedulers;
import com.dao.SourceApi;
import com.dao.db.SiteDbApi;
import com.dao.engine.DdNode;
import com.dao.engine.DdSource;
import com.model.BookViewModel;
import com.model.MainViewModel;
import com.model.PicModel;
import com.model.Section1ViewModel;
import com.model.Sections;
import com.model.Tag;
import com.model.TagViewModel;
import com.model.Tags;
import com.socks.library.KLog;

import org.noear.sited.SdSourceCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


/**
 *  @apiNote SiteD 网络请求和数据处理工厂
 * Created by haozhong on 2017/4/12.
 */

public class SitedFactory {
    private static List<PicModel> mData = new ArrayList();

    /**
     * @apiNote 获取hots节点网络数据
     * @param param
     * @return Observable
     */
    public static Observable getHots(HashMap<String, Object> param){

        return null;
    }

    /**
     * @apiNote 获取Updates节点网络数据
     * @param param
     * @return Observable
     */
    public static Observable getUpdates(HashMap<String, Object> param){
        return  null;
    }

    /**
     * @apiNote 获取Tags节点网络数据
     * @param param
     * @return Observable
     */
    public static Observable<List<Tags>> getTags(HashMap<String, Object> param){

        PublishSubject mSubject = PublishSubject.create();
        DdSource source = (DdSource) param.get(C.SOURCE);
        String type = (String)param.get(C.URL);
        if(source == null){
            source = SourceApi.getByUrl(type);
            if(source == null) {
                mSubject.onComplete();
                return Observable_NULL();
            }
        }

        MainViewModel viewModel = new MainViewModel();
        source.getNodeViewModel(viewModel, source.home, (boolean) param.get(C.ISUPDATE), new SdSourceCallback() {
            @Override
            public void run(Integer code) {
                if(code == 1){
                    Tags search = new Tags();
                    search.title = "搜索";
                    search.url = type+"search";
                    search.isSearch = true;
                    viewModel.tagList.add(search);
                    mSubject.onNext(viewModel.tagList);
                    mSubject.onComplete();
                    Observable.fromIterable(viewModel.tagList)
                            .map(tags -> {tags.QueryKey = type;return tags;}).toList()
                            .subscribe(t -> SiteDbApi.insertOrUpdate(t));
                }
            }
        });
        return mSubject.compose(RxSchedulers.io_main());
    }


    /**
     * @apiNote 获取Tag节点网络数据
     * @param param
     * @return Observable
     */
    public static Observable<List<Tag>> getTag(HashMap<String, Object> param){

        PublishSubject  mSubject = PublishSubject.create();
        DdSource source = (DdSource) param.get(C.SOURCE);
        Tags model = (Tags) param.get(C.MODEL);
        TagViewModel viewModel = new TagViewModel();
        DdNode Config;

        //tag节点 和 search节点 的 model 是一样的
        if(model.isSearch)
            Config  = source.search;
        else
            Config = source.tag(model.url);

        KLog.json("page=" + (int)param.get(C.PAGE));


        source.getNodeViewModel(viewModel, false,model.isSearch,(int)param.get(C.PAGE),model.url,Config, new SdSourceCallback() {
            @Override
            public void run(Integer code) {
                if(code == 1){
                    mSubject.onNext(viewModel.resultList);
                    mSubject.onComplete();
                    Observable.fromIterable(viewModel.resultList).map(tag -> {tag.QueryKey = model.url + param.get(C.PAGE);return tag;}).toList()
                            .subscribe(tags -> SiteDbApi.insertOrUpdate(tags));
                }
            }
        });

        return mSubject.compose(RxSchedulers.io_main());
    }

    /**
     * @apiNote 获取book节点网络数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Observable<List<Sections>> getBook(HashMap<String, Object> param){
        PublishSubject  mSubject = PublishSubject.create();
        KLog.json("进入Observable");
        Tag model = (Tag) param.get(C.MODEL);
        DdSource source = SourceApi.getByUrl(model.url);
        C.oldIndex = 0;
        if(source == null){
            mSubject.onNext(new ArrayList<Sections>());
            mSubject.onComplete();
        }else {
            //sited::获取book节点的数据
            Observable.timer(500,TimeUnit.MILLISECONDS)
                    .compose(RxSchedulers.io_main())
                    .subscribe((t) ->{
                        BookViewModel viewModel = new BookViewModel(source, model.url);
                        source.getNodeViewModel(viewModel, false, model.url, source.book(model.url), (code) -> {
                            if (code == 1) {
                                //维持正序
                                getBookBy(mSubject,viewModel,model);
                            }
                        });
                    });
        }


        return mSubject.compose(RxSchedulers.io_main());
    }

    /**
     * @apiNote 获取Section节点网络数据
     * @param param HashMap<String, Object> param
     * @return Observable
     */
    public static Observable<List<PicModel>> getSection(HashMap<String, Object> param){
        PublishSubject mSubject = PublishSubject.create();
        KLog.json("进入Observable");
        Sections model = (Sections) param.get(C.MODEL);
        DdSource source = SourceApi.getByUrl(model.bookUrl);

        int index = model.index;
        int page = (int)param.get(C.PAGE) -1;
        if(index + page == C.sSectionses.size() || source == null) { //已经到最底部或者source为空
            KLog.json("已经到最底部或者source为空");
            mSubject.onComplete();
            return Observable_NULL();
        }

        model = C.sSectionses.get(index + page);
        while (TextUtils.isEmpty(model.url)){ //跳过分组标题
            page++;
            if(index + page == C.sSectionses.size()) { //已经到最底部
                KLog.json("已经到最底部或者source为空");
                mSubject.onComplete();
                return Observable_NULL();
            }
            model = C.sSectionses.get(index + page);
            KLog.json(model.name + model.url);
        }

        Section1ViewModel viewModel = new Section1ViewModel();
        viewModel.currentSection = model;
        source.getNodeViewModel(viewModel, true, model.url, source.section(model.url), (code) -> {
            if (code == 1) {
                if (viewModel.total() == 0) {
                    mSubject.onNext(mData);
                    mSubject.onComplete();
                }
                else {
                    KLog.json("SitedFactory::getSection()");
                    mData =  viewModel.items;
                    mSubject.onNext(viewModel.items);
                    mSubject.onComplete();
                    Observable.fromIterable(viewModel.items).map(picModel -> {picModel.QueryKey = picModel.sections.url; return picModel;})
                            .toList()
                            .subscribeOn(Schedulers.io())
                            .subscribe(t -> SiteDbApi.insertOrUpdate(t));
                }
            }
        });
        return mSubject.compose(RxSchedulers.io_main());
    }


    /**
     * @apiNote 根据Sections的中间两项的name属性比较来调整Sections使其维持正序
     * @param
     * @return void
     */
    private static void getBookBy(PublishSubject Subscriber, BookViewModel viewModel, Tag model){
        if (viewModel.sectionList().size() > 1) { //大于一条数据
            int cent = (viewModel.sectionList().size()-1) / 2;
            if (viewModel.sectionList().get(cent).name.compareTo(viewModel.sectionList().get(cent + 1).name) > 0) {
                int size = viewModel.sectionList().size();

                Observable
                        .fromIterable(viewModel.sectionList())
                        .map(sections -> {sections.QueryKey = model.url;sections.index = size - sections.index - 1;return sections;})
                        .toList()
                        .map(s -> {Collections.reverse(s);return s;})
                        .subscribe(s ->{
                            C.sSectionses = s;
                            KLog.json("getBook=" + model.url);
                            SiteDbApi.insertOrUpdate(s);
                            Subscriber.onNext(s);
                            Subscriber.onComplete();
                        });

            }
        }else if(viewModel.sectionList().size()==1){ //只有一条数据不用改
            viewModel.sectionList().get(0).QueryKey = model.url;
            C.sSectionses =  viewModel.sectionList();
            KLog.json("getBook=" + model.url);
            SiteDbApi.insertOrUpdate(viewModel.sectionList());
            Subscriber.onNext(viewModel.sectionList());
            Subscriber.onComplete();
        }
    }


    /**
     * @apiNote 返回空数据的网络Observable
     * @param
     * @return Observable
     */
    private static Observable Observable_NULL(){
        return Observable.just(new ArrayList<>(0)).compose(RxSchedulers.io_main());
    }
}
