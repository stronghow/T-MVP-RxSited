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
    private String ua;
    
    public HttpMessage(RxNode config, String url){
        this.cfg = config;
        this.url = url;
        rebuild(null);
    }

    private void rebuild(final RxNode config) {
        if (config != null) {
            this.cfg = config;
        }
        encode = this.cfg.encode;
        method = this.cfg.method;
        ua = this.cfg.ua;


        headers = this.cfg.getHeader(url);
        if(headers==null){
            headers = new LinkedHashMap<>();
        }

        //headers.put(HttpHeaders.HEAD_KEY_CONTENT_TYPE,"text/html; charset="+encode);
        headers.put("User-Agent", ua);
        headers.put("Referer", url);

        rebuildUrl();
    }

    private void rebuildUrl() {
        if (url.startsWith("POST::")) {
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
        KLog.json(encode);
        return encode==null ? "UTF-8":encode;
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
        return url;
    }
}
