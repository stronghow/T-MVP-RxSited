package com.sited;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by haozhong on 2017/5/22.
 */
public class RxSource {
    private static Application context;
    private static HashMap<String,RxSource> rxSourceMap = new HashMap<>();
    public static final String defUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240";
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
    public RxNode updatese;
    public RxNode tags;
    public RxNode search;
    public RxNode tag;
    public RxNode book;
    public RxNode sections;
    public RxNodeSet section;
    public List<Lib> require;

    private JsEngine jsEngine;
    private RxJscript rxJscript;


    public static void init(Application app){
        context = app;
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
        //set(this);
    }

    //预加载 js
    public void PreLoadJS(){
        jsEngine = new JsEngine();
        RxJscript.load(jsEngine,code,require);
    }

    public String callJs(String func, String...args){
        return jsEngine.callJs(func, args).blockingFirst();
    }

    public String getUa() {
        return !TextUtils.isEmpty(ua) ? ua:defUA;
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
            KLog.json(cfg.buildUrl);
            return callJs(cfg.buildUrl, u1, page+"");
        }
        KLog.json(cfg.buildUrl);
        u1 = u1.replace("@page", page + "");
        KLog.json("getUrl = " + u1);
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
        KLog.json("rxParse-url", url);
        KLog.json("rxParse-html", html == null ? "null" : html);
        KLog.json("rxParse-parse",cfg.parse);
        if (TextUtils.isEmpty(cfg.parse) || "@null".equals(cfg.parse)) {
            if (TextUtils.isEmpty(html)) {
                html = "[]";
            }
            return Flowable.just(html);
        }
        return jsEngine.callJs(cfg.parse, url, html);
    }

    public Flowable<String> parseSearch(String url,String key){
        String ul = getUrl(search,url,key);
        return doGetNodeViewModel(search,ul);
    }

    public Flowable<String> parseTags(String url){
        if(tags == null) return Flowable.just("[]");
        else if(tags.staticData != null)
            return Flowable.just(tags.staticData); // .mergeWith(doGetNodeViewModel(tags,url))
        else return doGetNodeViewModel(tags,url);
    }

    public Flowable<String> parseTag(String url,int page){
        String ul = getUrl(tag,url,page);
        return doGetNodeViewModel(tag,ul);
    }

    public Flowable<String> parseBook(String url){
        String ul = getUrl(book,url);
        return doGetNodeViewModel(book,ul);
    }

    public Flowable<String> parseSections(String url){
        if(sections == null)
            return parseBook(url);
        String ul = getUrl(sections,url);
        return doGetNodeViewModel(sections,ul);
    }

    public Flowable<String> parseSection(String url){
        RxNode item = section.nodeMatch(url);
        String ul = getUrl(item,url);
        return doGetNodeViewModel(item,ul);
    }

    public Flowable<String> doGetNodeViewModel(final RxNode cfg, final String url) {
       return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
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
                        Thread.sleep(100);
                        e.onNext(HttpUtil.getHtml(cfg,u1));
                    }
                } else {
                    e.onNext(html);
                }
                e.onComplete();
            }
        },BackpressureStrategy.BUFFER).concatMap(html -> rxParse(cfg, url, html))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
                ", sections=" + sections +
                ", section=" + section +
                ", require='" + require + '\'' +
                '}';
    }
}
