package com.sited;

import com.socks.library.KLog;

import java.util.LinkedHashMap;

/**
 * Created by haozhong on 2017/6/29.
 */

public class HttpMessage implements HttpMsg{
    private  LinkedHashMap<String, String> headers;
    private  LinkedHashMap<String, String> params;
    private String url;
    private RxNode cfg;

    //可由cfg实始化
    private String encode;
    private String method;
    
    public HttpMessage(RxNode config, String url){
        this.cfg = config;
        this.url = url;
        build();
    }

    private void build() {
        encode = cfg.encode;
        method = cfg.method;

        rebuildUrl();

        headers = cfg.getHeader(url);
    }

    private void rebuildUrl() {
        if (url.startsWith("POST::")||method.equals("post")) {
            url = url.replace("POST::", "");
            method = "post";
            params = cfg.getParam(url);
        }else if(url.startsWith("GET::")){
            url = url.replace("GET::", "");
            method = "get";
        }else{
            method = "get";
        }
    }

    @Override
    public String getEncode() {
        return encode;
    }

    @Override
    public LinkedHashMap<String, String> getParams() {
        return params;
    }

    @Override
    public  LinkedHashMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getUrl() {
        KLog.json("url ==> " + url);
        return url;
    }
}
