package com.sited;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.socks.library.KLog;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

/**
 * Created by haozhong on 2017/5/22.
 */
public class RxSource {
    private static Application context;
    private static OkHttpClient mHttpClient = new OkHttpClient();
    private static HashMap<String,RxSource> rxSourceMap = new HashMap<>();
    public static final String defUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240";
    public static final String defEncode = "UTF-8";
    public static final String CALL = "CALL::";

    public int engine;
    public String ua;
    public String title;
    public String url;
    public String expr;
    public String encode;
    public int dtype;

    public String code;

    public RxNode hots;
    public RxNode updates;
    public RxNode tags;
    public RxNode search;
    public RxNodeSet tag;
    public RxNodeSet book;
    public RxNodeSet section;
    public List<Lib> require;

    private JsEngine jsEngine;


    public static void init(Application app){
        context = app;
    }

    public static OkHttpClient getHttpClient() {
        return mHttpClient;
    }

    public static void put(String url,RxSource rxSource){
        rxSourceMap.put(url,rxSource);
    }

    public static RxSource get(String url){
        return rxSourceMap.get(url);
    }

    /** 获取全局上下文 */
    public static Context getContext() {
        Utils.checkNotNull(context, "please call RxSource.init() first in application!");
        return context;
    }

    public RxSource(){
        jsEngine = new JsEngine();
    }

    //预加载 js
    public void PreLoadJS(){
        RxJscript.load(jsEngine,code,require);
    }

    public String callJs(String func, String...args){
        return jsEngine.callJs(func, args).blockingFirst();
    }

    private String Json_Concat(String str1,String str2){
        return jsEngine.callJs("Json_Concat",str1,str2).blockingFirst();
    }

    //book section
    private String getUrl(RxNode cfg, String defUrl) {
        String u1 = TextUtils.isEmpty(cfg.url) ? defUrl:cfg.url;
        if (!TextUtils.isEmpty(cfg.buildUrl)) {
            return callJs(cfg.buildUrl, u1);
        }
        return u1;
    }

    //tag
    private String getUrl(RxNode cfg, String defUrl, int page){
        String u1 = TextUtils.isEmpty(cfg.url) ? defUrl:cfg.url;
        page += cfg.addPage;
        if (!TextUtils.isEmpty(cfg.buildUrl)) {
            return callJs(cfg.buildUrl, u1, page+"");
        }
        u1 = u1.replace("@page", page + "");
        return u1;
    }

    //search
    private String getUrl(RxNode cfg, String defUrl,String key) {
        String u1 = TextUtils.isEmpty(cfg.url) ? defUrl:cfg.url;
        u1 = u1.replace("@key", Utils.urlEncode(key,cfg.encode));
        return u1;
    }

    private Flowable<String> rxParseUrl(RxNode cfg, String url, String html) {
        return jsEngine.callJs(cfg.parseUrl, url, html);
    }

    private Flowable<String> rxParse(RxNode cfg,String url, String html) {
        if (TextUtils.isEmpty(cfg.parse) || "@null".equals(cfg.parse)) {
            if (TextUtils.isEmpty(html)) {
                html = "[]";
            }
            return Flowable.just(html);
        }
        return jsEngine.callJs(cfg.parse, url, html);
    }

    public Flowable<String> parseHots(String url){
        if(hots == null) return Flowable.just("[]");
        String ul = getUrl(hots,url);
        return doGetNodeViewModel(hots,ul);
    }

    public Flowable<String> parseUpdates(String url){
        if(updates == null) return Flowable.just("[]");
        String ul = getUrl(updates,url);
        return doGetNodeViewModel(updates,ul);
    }

    public Flowable<String> parseSearch(String url,String key){
        if(search == null) return Flowable.just("[]");
        String ul = getUrl(search,url,key);
        return doGetNodeViewModel(search,ul);
    }

    public Flowable<String> parseTags(String url){
        parseHots(url).subscribe();
        parseUpdates(url).subscribe();
        if(tags == null) return Flowable.just("[]");
        if(tags.canParse()) {
            String ul = getUrl(tags,url);
            return doGetNodeViewModel(tags, ul)
                    .map(s -> {
                        String json = Json_Concat(s, tags.staticData);
                        KLog.json("tags ==> " + json);
                        return json;
                    });
        } else{
            return Flowable.just(tags.staticData);
        }
    }

    public Flowable<String> parseTag(String url,int page){
        if(tag == null) return Flowable.just("[]");
        RxNode item = tag.nodeMatch(url);
        if(item == null) return Flowable.just("[]");
        String ul = getUrl(item,url,page);
        return doGetNodeViewModel(item,ul);
    }

    public Flowable<String> parseBook(String url){
        if(book == null) return Flowable.just("[]");
        RxNode item = book.nodeMatch(url);
        if(item == null) return Flowable.just("[]");
        String ul = getUrl(item,url);
        return doGetNodeViewModel(item,ul);
    }

    public Flowable<String> parseSection(String url){
        if(section == null) return Flowable.just("[]");
        RxNode item = section.nodeMatch(url);
        if(item == null) return Flowable.just("[]");
        String ul = getUrl(item,url);
        return doGetNodeViewModel(item,ul);
    }

    public RxNode getBook(String url){
        if(book == null) return null;
        RxNode item = book.nodeMatch(url);
        return item;
    }

    public RxNode getSection(String url){
        if(section == null) return null;
        RxNode item = section.nodeMatch(url);
        return item;
    }

    private Flowable<String> doGetNodeViewModel(@NonNull final RxNode cfg,@NonNull final String url) {
       return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                KLog.json("RxThread ->" + Thread.currentThread().getName());
                String html = HttpUtil.getHtml(cfg,url);
                if (!TextUtils.isEmpty(cfg.parseUrl)) {
                    String parseUrl = rxParseUrl(cfg, url, html).blockingFirst();
                    while (parseUrl.startsWith(CALL)) {
                        parseUrl = parseUrl.replace(CALL, "");
                        KLog.json("doGetNodeViewModel-isNextUrl", parseUrl);
                        String html2 = HttpUtil.getHtml(cfg,parseUrl);
                        parseUrl = rxParseUrl(cfg, url, html2).blockingFirst();
                        //parseUrl = getUrl(cfg, parseUrl);
                    }

                    String[] urls = parseUrl.split(";");
                    KLog.json(" urls.length = " + urls.length);
                    for (String u1 : urls) {
                        //Thread.sleep(200);
                        e.onNext(HttpUtil.getHtml(cfg, u1));
                    }
                } else {
                    e.onNext(html);
                }
                e.onComplete();
            }
        },BackpressureStrategy.BUFFER)
                .concatMap(html -> rxParse(cfg, url, html))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io());
    }

    @Override
    public String toString() {
        return "RxSource{" +
                "ua='" + ua + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", expr='" + expr + '\'' +
                ", encode='" + encode + '\'' +
                ", dtype=" + dtype +
                ", code='" + code + '\'' +
                ", tags=" + tags +
                ", search=" + search +
                ", tag=" + tag +
                ", book=" + book +
                ", section=" + section +
                ", require='" + require + '\'' +
                '}';
    }
}
