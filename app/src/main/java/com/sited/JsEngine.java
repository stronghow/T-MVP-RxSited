package com.sited;

import android.text.TextUtils;

import com.App;
import com.base.util.LogUtils;
import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Locker;
import com.eclipsesource.v8.V8Object;
import com.socks.library.KLog;

import java.util.concurrent.Executor;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by haozhong on 2017/6/27.
 */

public class JsEngine {
    private V8 engine = null;
    private V8Array params = null;
    private final Scheduler scheduler = Schedulers.single();

    protected JsEngine(){  //在哪个线程创建 V8 就要在哪个线程运行 JS 代码
            Flowable.just(true)
                    .subscribeOn(scheduler)
                    .subscribe(aBoolean -> {
                                engine = V8.createV8Runtime(null, RxSource.getContext().getApplicationInfo().dataDir);
                                JavaVoidCallback callback = new JavaVoidCallback() {
                                    public void invoke(final V8Object receiver, final V8Array parameters) {
                                        if (parameters.length() > 0) {
                                            Object arg1 = parameters.get(0);
                                            KLog.json(arg1.toString());
                                            if (arg1 instanceof Releasable) {
                                                ((Releasable) arg1).release();
                                            }
                                        }
                                    }
                                };
                                engine.registerJavaMethod(callback, "print");
                    }
                    );
    }

    /**
     * 预加载批函数
     * @param js
     * @return
     */

    public void loadJs(String js){
        Flowable.just(js)
                .subscribeOn(scheduler)
                .subscribe(s -> {
                    KLog.json(s);
                    engine.executeScript(s);
                });
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
                params = new V8Array(engine);
                for (String p : args) {
                    params.push(p);
                }
                json = engine.executeStringFunction(fun, params);
                if(TextUtils.isEmpty(json)) json = "[]";
                e.onNext(json);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
        .subscribeOn(scheduler);
    }

    public Flowable<String> callJs(String script){
        return Flowable.create(new FlowableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext(engine.executeStringScript(script));
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(scheduler);
    }
}
