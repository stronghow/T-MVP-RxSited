package com.sited;

import android.text.TextUtils;

import com.App;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by haozhong on 2017/6/27.
 */

public class JsEngine {
    private V8 engine = null;
    protected JsEngine(){  //在哪个线程创建 V8 就要在哪个线程运行 JS 代码
        App.getCurActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                engine = V8.createV8Runtime(null, RxSource.getContext().getApplicationInfo().dataDir);
            }
        });
    }

    /**
     * 预加载批函数
     * @param js
     * @return
     */

    public synchronized void loadJs(String js){
        App.getCurActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                engine.executeScript(js);
            }
        });
//        Observable.just(js)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(s -> engine.executeScript(s)
//                        , throwable -> LogUtils.showLog("JsEngine.loadJs", throwable.getMessage()));
    }


    /**
     * 调用函数
     * @param fun
     * @param args
     * @return
     */
    public Flowable<String> callJs(String fun, String... args){
        return Flowable.create(new FlowableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                String json;
                V8Array params = new V8Array(engine);
                for (String p : args) {
                    params.push(p);
                }
                json = engine.executeStringFunction(fun, params);
                if(TextUtils.isEmpty(json)) json = "[]";
                e.onNext(json);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(Schedulers.io());
    }
}
