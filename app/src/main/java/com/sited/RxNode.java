package com.sited;
import android.text.TextUtils;

import java.util.LinkedHashMap;

/**
 * Created by haozhong on 2017/5/20.
 */
public class RxNode implements IRxNode {

    private RxSource rxSource;

    public String type;
    public String title;
    public String expr;
    public int dtype;
    public String method;
    public String args;
    public String header;
    public String encode;
    public String url;
    public String ua;

    //add prop for search or tag
    protected String addCookie; //需要添加的关键字
    protected String addKey; //需要添加的关键字
    protected int    addPage;//需要添加的页数值


    public RxAttributeList attrs = new RxAttributeList();
    public String staticData = "[]";

    //build
    protected String buildArgs;
    protected String buildUrl;
    protected String buildRef;
    protected String buildHeader;
    protected String buildWeb;
    protected String buildCookie;

    //parse
    protected String parse; //解析函数
    protected String parseUrl; //解析出真正在请求的Url
    boolean isParse = false;

    @Override
    public int nodeType() {
        return 1;
    }


    @Override
    public RxNode nodeMatch(String url) {
        return this;
    }

    @Override
    public String nodeName() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public RxNode(RxSource rxSource){
        this.rxSource = rxSource;
    }

    public void build(){
        this.title    = attrs.getString("title");//可能为null
        this.url = attrs.getString("url");
        this.encode = attrs.getString("encode",rxSource.encode);
//        if(TextUtils.isEmpty(this.encode)){
//            this.encode = rxSource.encode;
//            //attrs.set("encode",this.encode);
//        }

        this.ua = attrs.getString("ua",rxSource.ua);
//        if(TextUtils.isEmpty(this.ua)){
//            this.ua = rxSource.ua;
//            //attrs.set("ua",this.ua);
//        }

        this.args = attrs.getString("args");
        this.header = attrs.getString("header");
        this.dtype = attrs.getInt("dtype",rxSource.dtype);
//        if(this.dtype == 0){
//            this.dtype = rxSource.dtype;
//            //attrs.set("dtype",""+this.dtype);
//        }

        this.method = attrs.getString("method","get");
        this.addKey = attrs.getString("addKey");
        this.addPage = attrs.getInt("addPage",0);
        this.addCookie = attrs.getString("addCookie");

        this.buildUrl = attrs.getString("buildUrl");
        this.buildArgs = attrs.getString("buildArgs");
        this.buildRef = attrs.getString("buildRef");
        this.buildHeader = attrs.getString("buildHeader");
        this.buildWeb = attrs.getString("buildWeb");
        this.parseUrl = attrs.getString("parseUrl");
        this.parse    = attrs.getString("parse");

        isParse = !(TextUtils.isEmpty(parseUrl) && TextUtils.isEmpty(parse));
    }

    public RxSource getRxSource() {
        return rxSource;
    }

    public LinkedHashMap<String, String> getHeader(String url) {
        LinkedHashMap<String, String> headerMap = new LinkedHashMap<>();
        if(!TextUtils.isEmpty(header)) headerMap.putAll(RxNodeHelp.getMap(header,rxSource.engine));
        if(!TextUtils.isEmpty(buildHeader)) headerMap.putAll(RxNodeHelp.getMap(rxSource.callJs(buildHeader,url),rxSource.engine));

        if(!TextUtils.isEmpty(buildRef)) headerMap.put("Referer", rxSource.callJs(buildRef,url));
        else headerMap.put("Referer",url);

        headerMap.put("User-Agent", ua);

        return headerMap;
    }

    public LinkedHashMap<String, String> getParam(String url) {
        LinkedHashMap<String, String> headerMap = new LinkedHashMap<>();
        if(!TextUtils.isEmpty(args)) headerMap.putAll(RxNodeHelp.getMap(args,rxSource.engine));
        if(!TextUtils.isEmpty(buildArgs))  headerMap.putAll(RxNodeHelp.getMap(rxSource.callJs(buildArgs,url),rxSource.engine));
        return headerMap;
    }

    public boolean canParse(){
        return isParse;
    }
}
