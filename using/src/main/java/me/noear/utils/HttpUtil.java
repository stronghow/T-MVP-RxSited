package me.noear.utils;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import me.noear.exts.Act2;

/**
 * Created by yuety on 14-6-16.
 * HTTP 工具
 * 与IOS 保持一至，有利于代码迁移
 */
public class HttpUtil {

    public static void get(String url, final Act2<Integer,String> callback)
    {
        get(url,null,callback);
    }

    public static void get(String url, Map<String,String> params, final Act2<Integer,String> callback){
        get(null,url,params,callback);
    }

    /*发起get请求，并返回String结果*/
    public static void get(Map<String,String> header,String url, Map<String,String> params, final Act2<Integer,String> callback)
    {
        RequestParams getData = null;
        if(params==null)
            getData = new RequestParams();
        else
            getData = new RequestParams(params);

        AsyncHttpClient client = new AsyncHttpClient();

        if(header!=null && header.size()>0){
            for (String key :header.keySet()){
                client.addHeader(key,header.get(key));
            }
        }

        client.get(null, url, getData, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String s, Throwable throwable) {
                callback.run(-2,null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String s) {
                if (s == null)
                    callback.run(-1, s);
                else
                    callback.run(1, s);
            }
        });
    }

    /*发起post请求，并返回String结果*/
    public static void post(String url,Map<String,String> params, final Act2<Integer,String> callback)
    {
        RequestParams postData = new RequestParams(params);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(null, url, postData, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String s, Throwable throwable) {
                callback.run(-2,null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String s) {
                if (s == null)
                    callback.run(-1, s);
                else
                    callback.run(1, s);
            }
        });
    }



    public static void upload(String url,File file, final Act2<Integer,String> callback)  throws IOException
    {
        RequestParams postData = new RequestParams();
        postData.put("file",file);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(null, url, postData, new TextHttpResponseHandler(){

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String s, Throwable throwable) {
                callback.run(-2,null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String s) {
                if (s == null)
                    callback.run(-1, s);
                else
                    callback.run(1, s);
            }
        });
    }
}


