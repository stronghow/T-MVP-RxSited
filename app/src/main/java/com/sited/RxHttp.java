package com.sited;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okrx2.adapter.ObservableResponse;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by haozhong on 2017/6/27.
 */

public class RxHttp {

//    public static Observable<String> get(HttpMsg msg){
//        return OkGo.<String>get(msg.getUrl())
//                .headers(getHttpHeaders(msg.getHeaders()))
//                .converter(new StringConvert())
//                .adapt(new ObservableResponse<>())
//                .map(stringResponse -> new String(stringResponse.body().getBytes(),msg.getEncode()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//
//        //   .generateRequest(RequestBody.create(MediaType.parse("text/html; charset="+msg.getEncode()),"json"))
//    }
//
//    public static Observable<String> post(HttpMsg msg){
//        return OkGo.<String>post(msg.getUrl())
//                .headers(getHttpHeaders(msg.getHeaders()))
//                .params(msg.getParams())
//                .converter(new StringConvert())
//                .adapt(new ObservableResponse<>())
//                .map(stringResponse -> new String(stringResponse.body().getBytes(),msg.getEncode()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public static Observable<String> call(HttpMsg msg){
//        if(msg.getMethod().equals("get")) return get(msg);
//        else return post(msg);
//    }
//
//    public static Observable<String> get(String url,HttpHeaders httpHeaders){
//        return OkGo.<String>get(url)
//                .headers(httpHeaders)
//                .converter(new StringConvert())
//                .adapt(new ObservableResponse<>())
//                .map(stringResponse -> stringResponse.body())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public static Observable<String> get(String url){
//        return OkGo.<String>get(url)
//                .converter(new StringConvert())
//                .adapt(new ObservableResponse<>())
//                .map(stringResponse -> stringResponse.body())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public static Observable<String> post(String url, HttpHeaders httpHeaders, Map<String,String> param){
//        return OkGo.<String>post(url)
//                .headers(httpHeaders)
//                .params(param)
//                .converter(new StringConvert())
//                .adapt(new ObservableResponse<>())
//                .map(stringResponse -> stringResponse.body())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    private static HttpHeaders getHttpHeaders(LinkedHashMap<String,String> map){
//        HttpHeaders headers = new HttpHeaders();
//        headers.headersMap = map;
//        return headers;
//    }
}
