package com.sited;
import android.text.TextUtils;

import java.util.LinkedHashMap;

/**
 * Created by haozhong on 2017/5/20.
 */
public class RxNode implements IRxNode {

    private RxSource rxSource;

    public String nodename;
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
    public String staticData = null;
    @Override
    public int nodeType() {
        return 0;
    }

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

    //ext prop (for post)

    @Override
    public RxNode nodeMatch(String url) {
        return null;
    }

    @Override
    public String nodeName() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public void build(RxSource rxSource){
        this.rxSource  = rxSource;
        this.title    = attrs.getString("title");//可能为null
        this.url = attrs.getString("url");
        this.encode = attrs.getString("encode");
        if(TextUtils.isEmpty(this.encode)) this.encode = rxSource.encode;

        this.ua = attrs.getString("ua");
        if(TextUtils.isEmpty(this.ua)) this.ua = rxSource.getUa();

        this.args = attrs.getString("args");
        this.header = attrs.getString("header");
        this.dtype = attrs.getInt("dtype");
        if(this.dtype == 0) this.dtype = rxSource.dtype;

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
    }

    public LinkedHashMap<String, String> getHeader(String url) {
        if(!TextUtils.isEmpty(header)) return getMap(header);
        else if(!TextUtils.isEmpty(buildHeader)) return getMap(rxSource.callJs(buildHeader,url));
        else return null;
    }

//    public LinkedHashMap<String,String> getHeader(final String data) {
//        return getMap(data);
//    }

    public LinkedHashMap<String, String> getParam(String url) {
        if(!TextUtils.isEmpty(args)) return getMap(args);
        else if(!TextUtils.isEmpty(buildArgs)) return getMap(rxSource.callJs(buildArgs,url));
        else return null;
    }

//    public LinkedHashMap<String,String> getParam(final String data) {
//        return getMap(data);
//    }

    private LinkedHashMap<String,String> getMap(final String data){
        if (!TextUtils.isEmpty(data)) {
            if (rxSource.engine < 34) {
                return StringtoMap(data, ";", "=");
            } else {
                return StringtoMap(data, "\\$\\$", ":");//new
            }
        }
        return null;
    }

    private LinkedHashMap<String,String> StringtoMap(final String str, final String sp1, final String sp2) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (String kv : str.split(sp1)) {
            String[] item = kv.split(sp2);
            if (item.length == 2)
                map.put(item[0].trim(), item[1].trim());
        }
        return map;
    }

    public RxSource getRxSource() {
        return rxSource;
    }
}
