package com.base.util.helper;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baixiaokang on 16/5/6.
 */
public class RxSchedulers {
    public static final Observable.Transformer<?, ?> mio_mainTransformer
            = observable -> observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());

    public static final Observable.Transformer<?, ?> mio_ioTransformer
            = observable -> observable
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io());

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> io_main() {
        return (Observable.Transformer<T, T>) mio_mainTransformer;
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> io_io() {
        return (Observable.Transformer<T, T>) mio_ioTransformer;
    }
}
