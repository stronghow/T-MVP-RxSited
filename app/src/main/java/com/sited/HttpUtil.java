package com.sited;

import android.accounts.NetworkErrorException;

import com.App;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by haozhong on 2017/7/10.
 */

public class HttpUtil {

    public static String getHtml(final RxNode cfg, final String url)  {
        try {
            return loadHtml(new HttpMessage(cfg, url)).blockingFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Observable<String> RxgetHtml(final RxNode cfg, final String url)  {
            return loadHtml(new HttpMessage(cfg, url));
    }


    public static String getHtml(String url){
        try {
            return loadHtml(OnlygetUrl(url)).blockingFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static Observable<String> loadHtml(final HttpMsg msg) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> s) throws Exception {
                Request.Builder builder = new Request.Builder().url(msg.getUrl());

                for(Map.Entry<String, String> entry: msg.getHeaders().entrySet()){
                    builder.addHeader(entry.getKey(), entry.getValue());
                }

                if ("post".equals(msg.getMethod())) {
                    builder.post(getFormBody(msg.getParams()));
                } else {
                    builder.get();
                }
                Request request = builder.build();
                RxSource.getHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            KLog.json("HttpUtil-RequestHeader", request.headers().toString());
                            KLog.json("HttpUtil-ResponseHeader", response.headers().toString());
                            s.onNext(new String(response.body().bytes(), msg.getEncode()));
                            s.onComplete();
                            response.close();
                        }else{
                            s.onNext("[]");
                            s.onComplete();
                            response.close();
                        }
                    }
                });
//                String html = getResponseBody(App.getHttpClient(), builder.build(), msg.getEncode());
//                s.onNext(html);
//
//                s.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    private static FormBody getFormBody(LinkedHashMap<String,String> map){
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,String>  entry : map.entrySet())
            builder.add(entry.getKey(),entry.getValue());
        return builder.build();
    }

    private static HttpMsg OnlygetUrl(String url){
        return new HttpMsg() {
            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public String getMethod() {
                return "get";
            }

            @Override
            public String getEncode() {
                return "utf-8";
            }

            @Override
            public LinkedHashMap<String, String> getHeaders() {
                return new LinkedHashMap<>();
            }

            @Override
            public LinkedHashMap<String, String> getParams() {
                return new LinkedHashMap<>();
            }
        };
    }

    @SuppressWarnings("all")
    private static String getResponseBody(OkHttpClient client, Request request, String encode) {
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                KLog.json("HttpUtil-RequestHeader", request.headers().toString());
                KLog.json("HttpUtil-ResponseHeader", response.headers().toString());
                return new String(response.body().bytes(), encode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return "[]";
    }

    public static String getResponseBody(OkHttpClient client, Request request) throws NetworkErrorException {
        return getResponseBody(client, request, "UTF-8");
    }
}
