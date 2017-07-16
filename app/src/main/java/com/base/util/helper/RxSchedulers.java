package com.base.util.helper;

import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by baixiaokang on 16/5/6.
 */
public class RxSchedulers {
    public static final FlowableTransformer<?,?> mio_mainTransformer
            = FlowableTransformer -> FlowableTransformer
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(io.reactivex.schedulers.Schedulers.io());


    public static final FlowableTransformer<?, ?> mio_ioTransformer
            = FlowableTransformer -> FlowableTransformer
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(io.reactivex.schedulers.Schedulers.io())
            .unsubscribeOn(io.reactivex.schedulers.Schedulers.io());

    @SuppressWarnings("unchecked")
    public static FlowableTransformer io_main() {
        return  mio_mainTransformer;
    }

    @SuppressWarnings("unchecked")
    public static FlowableTransformer io_io() {
        return  mio_ioTransformer;
    }
}
