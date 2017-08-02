package com;

import android.text.TextUtils;

import com.base.DataFactory;
import com.base.util.helper.RxSchedulers;
import com.dao.db.SiteDbApi;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.model.BookModel;
import com.model.PicModel;
import com.model.Sections;
import com.model.Tag;
import com.model.Tags;
import com.sited.RxSource;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

import static com.base.DataFactory.JsonToList;

/**
 * Created by haozhong on 2017/7/4.
 */

public class NetFactory {
    /**
     * @apiNote 获取Tags节点网络数据
     * @param param
     * @return Observable
     */
    public static Flowable<List<Tags>> getTags(HashMap<String, Object> param){
        String url = (String)param.get(C.URL);
        RxSource rxSource = (RxSource)param.get(C.SOURCE);
        return rxSource.parseTags(url)
                    .map(s -> JsonToList(s,Tags.class))
                    .doOnNext(tagses -> {
                        for (Tags tags : tagses) tags.QueryKey = url;
                        SiteDbApi.insertOrUpdate(tagses);
                    });

    }

    public static Flowable<List<Tag>> getTag(HashMap<String, Object> param){
        Tags _tags = (Tags) param.get(C.MODEL);
        int page = (int)param.get(C.PAGE);
        RxSource rxSource = (RxSource)param.get(C.SOURCE);
        return rxSource.parseTag(_tags.url,page)
                .map(s -> JsonToList(s,Tag.class))
                .doOnNext(tags -> {
                    final String key = _tags.url + page;
                    for(Tag tag : tags) tag.QueryKey = key;
                    SiteDbApi.insertOrUpdate(tags);
                });

    }

    public static Flowable<List<Tag>> getSearch(HashMap<String, Object> param){
        String url = (String) param.get(C.URL);
        String key = (String)param.get(C.KEY);
        RxSource rxSource = (RxSource)param.get(C.SOURCE);
        final String QKey = url + key;
        return rxSource.parseSearch(url,key)
                .map(s -> JsonToList(s,Tag.class));
//                .doOnNext(tags -> {
//                    for(Tag tag : tags) tag.QueryKey = QKey;
//                    SiteDbApi.insertOrUpdate(tags);
//                });

    }

    public static Flowable<List<Sections>> getBook(HashMap<String, Object> param){
        Tag tag = (Tag)param.get(C.MODEL);
        C.oldIndex = 0;
            //sited::获取book节点的数据
        RxSource rxSource = (RxSource)param.get(C.SOURCE);

       return Flowable.timer(300, TimeUnit.MILLISECONDS)
                .flatMap(aLong -> rxSource.parseSections(tag.url)
                        .map(s ->{
                            C.sSectionses = DataFactory.JsonToBean(s,BookModel.class).sections;
                            getBookBy(C.sSectionses,tag); //维持正序
                            return C.sSectionses;
                        }).doOnNext(SiteDbApi::insertOrUpdate));
    }


    public static Flowable<List<PicModel>> getSection(HashMap<String, Object> param){
        Sections sections = (Sections) param.get(C.MODEL);
        final RxSource rxSource = (RxSource)param.get(C.SOURCE);
        final int index = sections.index;
        int page = (int)param.get(C.PAGE) -1;
        if(index + page == C.sSectionses.size()) { //已经到最底
            KLog.json("已经到最底部");
            return Observable_NULL();
        }

        sections = C.sSectionses.get(index + page);
        while (TextUtils.isEmpty(sections.url)){ //跳过分组标题
            page++;
            if(index + page == C.sSectionses.size()) { //已经到最底部
                KLog.json("已经到最底部");
                return Observable_NULL();
            }
            sections = C.sSectionses.get(index + page);
        }

        final String key = sections.url;
        return rxSource.parseSection(sections.url)
                .map(s -> {
                    KLog.json(s);
                    List<PicModel> picModels = new ArrayList<>();
                    JsonElement element = new JsonParser().parse(s);
                    //JsonArray jsonArray = element.getAsJsonArray(); //TextUtils.isEmpty(jsonArray.get(0).getAsJsonObject().get("url").getAsString())
                    if(element.isJsonArray())
                        for (JsonElement el : element.getAsJsonArray()) {
                            PicModel picModel = new PicModel();
                            picModel.url = el.getAsString();
                            picModel.QueryKey = key;
                            picModels.add(picModel);
                        }
                    else picModels = DataFactory.JsonToList(s,PicModel.class);
                    return picModels;
                }).doOnNext(SiteDbApi::insertOrUpdate);

    }

    /**
     * @apiNote 根据Sections的中间两项的name属性比较来调整Sections使其维持正序
     * @param
     * @return void
     */
    private static void getBookBy(List<Sections> sectionses,Tag model){
        if (sectionses.size() > 1) { //大于一条数据
            int cent = (sectionses.size()-1) / 2;
            // 大于0 反序 调换List
            if (sectionses.get(cent).name.compareTo(sectionses.get(cent + 1).name) > 0) {
                Collections.reverse(sectionses);
            }

            for(int i=0; i< sectionses.size();i++){
                C.sSectionses.get(i).index = i;
                C.sSectionses.get(i).QueryKey = model.url;
            }
        }else if(sectionses.size()==1){ //只有一条数据不用改
            sectionses.get(0).QueryKey = model.url;
            sectionses.get(0).index = 0;
        }
    }

    public static void reverse(List<Sections> sectionses){
        Collections.reverse(sectionses);
        for(int i=0; i< sectionses.size();i++){
            C.sSectionses.get(i).index = i;
        }
    }

    private static Flowable Observable_NULL(){
        return Flowable.just(new ArrayList(0)).compose(RxSchedulers.io_main());
    }
}
