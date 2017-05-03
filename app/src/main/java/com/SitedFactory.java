package com;

import android.text.TextUtils;

import com.base.entity.DataArr;
import com.base.util.helper.RxSchedulers;
import com.dao.SourceApi;
import com.dao.db.SiteDbApi;
import com.dao.engine.DdNode;
import com.dao.engine.DdSource;
import com.model.BookViewModel;
import com.model.Hots;
import com.model.MainViewModel;
import com.model.PicModel;
import com.model.Section1ViewModel;
import com.model.Sections;
import com.model.Tag;
import com.model.TagViewModel;
import com.model.Tags;
import com.model.Updates;
import com.socks.library.KLog;

import org.noear.sited.SdSourceCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by haozhong on 2017/4/12.
 */

public class SitedFactory {
    private static DataArr mData = new DataArr();

    public static Observable getHots(HashMap<String, Object> param){
      PublishSubject  mSubject = PublishSubject.create();
        KLog.json("进入Observable");
        DdSource source = (DdSource) param.get(C.SOURCE);
        if(source == null){
            source = SourceApi.getByUrl((String)param.get(C.URL));
            if(source == null) {
                KLog.json("source == null");
                return  Observable_NULL();
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

    public static Observable getUpdates(HashMap<String, Object> param){
        PublishSubject  mSubject = PublishSubject.create();
        KLog.json("进入Observable");
        DdSource source = (DdSource) param.get(C.SOURCE);
        if(source == null){
            source = SourceApi.getByUrl((String)param.get(C.URL));
            if(source == null) {
                KLog.json("source == null");
                return  Observable_NULL();
            }
        }

        MainViewModel viewModel = new MainViewModel();
        source.getNodeViewModel(viewModel, source.home, (boolean) param.get(C.ISUPDATE), new SdSourceCallback() {
            @Override
            public void run(Integer code) {
                if(code == 1) {
                    DataArr<Updates> data = new DataArr<>();
                    data.results = viewModel.updateList;
                    //OkBus.getInstance().onEvent(EventTags.SHOW_TAG_LIST, viewModel.tagList);
                    mSubject.onNext(data);
                    mSubject.onCompleted();
                }
            }
        });

        return mSubject.compose(RxSchedulers.io_main());
    }

    public static Observable getTags(HashMap<String, Object> param){
        PublishSubject mSubject = PublishSubject.create();
        DdSource source = (DdSource) param.get(C.SOURCE);
        String type = (String)param.get(C.URL);
        if(source == null){
            source = SourceApi.getByUrl(type);
            if(source == null) {
                mSubject.onCompleted();
                return Observable_NULL();
            }
        }

        MainViewModel viewModel = new MainViewModel();
        source.getNodeViewModel(viewModel, source.home, (boolean) param.get(C.ISUPDATE), new SdSourceCallback() {
            @Override
            public void run(Integer code) {
                if(code == 1){
                    //OkBus.getInstance().onEvent(EventTags.SHOW_TAG_LIST, viewModel.tagList);
                    mSubject.onNext(viewModel.tagList);
                    mSubject.onCompleted();
                    Observable.from(viewModel.tagList).map(tags -> {tags.QueryKey = type;return tags;}).toList()
                            .compose(RxSchedulers.io_io())
                            .subscribe(t -> SiteDbApi.insertOrUpdate(t));

//                    for(Tags tags : viewModel.tagList){
//                        tags.QueryKey = type;
//                    }
//                    SiteDbApi.insertOrUpdate(viewModel.tagList);
                }
            }
        });
        return mSubject.compose(RxSchedulers.io_main());
    }

    public static Observable getTag(HashMap<String, Object> param){
        PublishSubject  mSubject = PublishSubject.create();
        DdSource source = (DdSource) param.get(C.SOURCE);
        Tags model = (Tags) param.get(C.MODEL);
        TagViewModel viewModel = new TagViewModel();
        DdNode tagConfig = source.tag(model.url);

        KLog.json("page=" + (int)param.get(C.PAGE));

        source.getNodeViewModel(viewModel, false, (int)param.get(C.PAGE),model.url,tagConfig, new SdSourceCallback() {
            @Override
            public void run(Integer code) {
                if(code == 1){
                    DataArr<Tag> data = new DataArr<>();
                    KLog.json("size=" + viewModel.resultList.size());
                    data.results = viewModel.resultList;
                    mSubject.onNext(data);
                    mSubject.onCompleted();
                    Observable.from(viewModel.resultList).map(tag -> {tag.QueryKey = model.url + param.get(C.PAGE);return tag;}).toList()
                            .compose(RxSchedulers.io_io())
                            .subscribe(tags -> SiteDbApi.insertOrUpdate(tags));
//                    for(Tag tag : viewModel.resultList){
//                        tag.QueryKey = model.url + param.get(C.PAGE);
//                    }
//                    KLog.json("getTag=" + model.url + param.get(C.PAGE));
//                    SiteDbApi.insertOrUpdate(viewModel.resultList);
                }
            }
        });

        return mSubject.compose(RxSchedulers.io_main());
    }

    public static Observable getBook(HashMap<String, Object> param){
        PublishSubject  mSubject = PublishSubject.create();
        KLog.json("进入Observable");
        Tag model = (Tag) param.get(C.MODEL);
        DdSource source = SourceApi.getByUrl(model.url);
        C.oldIndex = 0;
        if(source == null){
            mSubject.onNext(new DataArr<>());
            mSubject.onCompleted();
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

    public static Observable getSection(HashMap<String, Object> param){
        PublishSubject  mSubject = PublishSubject.create();
        KLog.json("进入Observable");
        Sections model = (Sections) param.get(C.MODEL);
        DdSource source = SourceApi.getByUrl(model.bookUrl);

        int index = model.index;
        int page = (int)param.get(C.PAGE) -1;
//        KLog.json("page=" + page + " index" + index);
//
//        //sectionses = C.sSectionses;
//
        if(index + page == C.sSectionses.size() || source == null) { //已经到最底部或者source为空
            KLog.json("已经到最底部或者source为空");
            mSubject.onCompleted();
            return Observable_NULL();
        }

        model = C.sSectionses.get(index + page);
        while (TextUtils.isEmpty(model.url)){ //跳过分组标题
            page++;
            if(index + page == C.sSectionses.size()) { //已经到最底部
                KLog.json("已经到最底部或者source为空");
                mSubject.onCompleted();
                return Observable_NULL();
            }
            model = C.sSectionses.get(index + page);
            KLog.json(model.name + model.url);
        }


        //设置book::sections记录
        //记录数据位置
        //SiteDbApi.updateLastlook(C.sSectionses,model);

        //((Section1Activity)App.getCurActivity()).mViewBinding.listItem.setHashMap(C.BOOKNAME,model.name);

        //sited::获取section节点的数据
        Section1ViewModel viewModel = new Section1ViewModel();
        viewModel.currentSection = model;
        source.getNodeViewModel(viewModel, true, model.url, source.section(model.url), (code) -> {
            if (code == 1) {
                if (viewModel.total() == 0) {
                    mSubject.onNext(mData);
                }
                else {
                    KLog.json("SitedFactory::getSection()");
                    DataArr<PicModel> data = new DataArr<>();
                    data.results = (ArrayList<PicModel>) viewModel.items;
                    mData.results = (ArrayList<PicModel>) viewModel.items;
                    mSubject.onNext(data);
                    mSubject.onCompleted();
                    Observable.from(viewModel.items).map(picModel -> {picModel.QueryKey = picModel.sections.url; return picModel;}).toList()
                            .compose(RxSchedulers.io_io())
                            .subscribe(t -> SiteDbApi.insertOrUpdate(t));
                }
            }
        });

        return mSubject.compose(RxSchedulers.io_main());
    }

    private static void getBookBy(Subject mSubject,BookViewModel viewModel,Tag model){
        if (viewModel.sectionList().size() > 1) { //大于一条数据
            int cent = (viewModel.sectionList().size()-1) / 2;
            if (viewModel.sectionList().get(cent).name.compareTo(viewModel.sectionList().get(cent + 1).name) > 0) {
                int size = viewModel.sectionList().size();

                Observable
                        .from(viewModel.sectionList())
                        .map(sections -> {sections.QueryKey = model.url;sections.index = size - sections.index - 1;return sections;})
                        .toList()
                        .map(s -> {Collections.reverse(s);return s;})
                        .compose(RxSchedulers.io_io())
                        .subscribe(s ->{
                            DataArr<Sections> data = new DataArr<>();
                            data.results = (ArrayList<Sections>) s;

                            C.sSectionses = (ArrayList<Sections>) s;
                            KLog.json("getBook=" + model.url);
                            //C.sSectionses = sectionses;
                            SiteDbApi.insertOrUpdate(s);
                            mSubject.onNext(data);
                            mSubject.onCompleted();
                        });

            }
        }else if(viewModel.sectionList().size()==1){ //只有一条数据不用改
            viewModel.sectionList().get(0).QueryKey = model.url;
            DataArr<Sections> data = new DataArr<>();
            data.results = (ArrayList<Sections>) viewModel.sectionList();
            C.sSectionses = (ArrayList<Sections>) viewModel.sectionList();
            KLog.json("getBook=" + model.url);
            //C.sSectionses = sectionses;
            SiteDbApi.insertOrUpdate(viewModel.sectionList());
            mSubject.onNext(data);
            mSubject.onCompleted();
        }
    }

    private static Observable Observable_NULL(){
        return Observable.create(new Observable.OnSubscribe<DataArr>() {
            @Override
            public void call(Subscriber<? super DataArr> subscriber) {
                DataArr<PicModel> data = new DataArr<>();
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        });
    }
}
