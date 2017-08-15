package com.base.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.base.util.helper.BlurTransformation;
import com.base.util.helper.GlideCircleTransform;
import com.base.util.helper.RxSchedulers;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ui.main.R;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by baixiaokang on 16/5/6.
 */
public class BindingUtils {

    @BindingAdapter({"imageUrl","refUrl"})
    public static void loadImg(ImageView v, String url,String refererUrl) {
//        Flowable.create(new FlowableOnSubscribe<DrawableRequestBuilder>() {
//            @Override
//            public void subscribe(FlowableEmitter<DrawableRequestBuilder> e) throws Exception {
//               //网络防盗链
//                LazyHeaders.Builder builder = new LazyHeaders.Builder();
//                if (refererUrl != null) {
//                    builder.addHeader("Referer", refererUrl);
//                }
//
//                e.onNext(Glide.with(v.getContext())
//                       .load(new GlideUrl(getFuckUrl(url),builder.build()))
//                       .diskCacheStrategy(DiskCacheStrategy.ALL)
//                       .error(R.drawable.a));
//            }
//        }, BackpressureStrategy.BUFFER)
//                .compose(RxSchedulers.io_main())
//                .subscribe(drawableRequestBuilder -> {
//                    v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
//
//                    drawableRequestBuilder.into(v);
//                });
        v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
        //网络防盗链
        LazyHeaders.Builder builder = new LazyHeaders.Builder();
        if (refererUrl != null) {
            builder.addHeader("Referer", refererUrl);
        }

        v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
        Glide.with(v.getContext())
                .load(new GlideUrl(getFuckUrl(url),builder.build()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.a)
                .into(v);
    }

    @BindingAdapter({"blurimageUrl","refUrl"})
    public static void blur_loadImg(ImageView v,  String url,String refererUrl) {
//        Flowable.create(new FlowableOnSubscribe<DrawableRequestBuilder>() {
//            @Override
//            public void subscribe(FlowableEmitter<DrawableRequestBuilder> e) throws Exception {
//                //网络防盗链
//                LazyHeaders.Builder builder = new LazyHeaders.Builder();
//                if (refererUrl != null) {
//                    builder.addHeader("Referer", refererUrl);
//                }
//                e.onNext(Glide.with(v.getContext())
//                        .load(new GlideUrl(getFuckUrl(url),builder.build()))
//                        .crossFade(1000)
//                        .bitmapTransform(new BlurTransformation(v.getContext(),15))
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .error(R.drawable.a));
//            }
//        }, BackpressureStrategy.BUFFER)
//                .compose(RxSchedulers.io_main())
//                .subscribe(drawableRequestBuilder ->{
//                    v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
//
//                    drawableRequestBuilder.into(v);
//                });
        v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);

        //网络防盗链
        LazyHeaders.Builder builder = new LazyHeaders.Builder();
        if (refererUrl != null) {
            builder.addHeader("Referer", refererUrl);
        }
        Glide.with(v.getContext())
                .load(new GlideUrl(getFuckUrl(url),builder.build()))
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(v.getContext(),15))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.a)
                .into(v);
    }

    @BindingAdapter({"fiximageUrl","refUrl"})
    public static void fix_loadImg(ImageView v, String url,String refererUrl) {
//        Flowable.create(new FlowableOnSubscribe<BitmapRequestBuilder>() {
//            @Override
//            public void subscribe(FlowableEmitter<BitmapRequestBuilder> e) throws Exception {
//                 //网络防盗链
//                LazyHeaders.Builder builder = new LazyHeaders.Builder();
//                if (refererUrl != null) {
//                    builder.addHeader("Referer", refererUrl);
//                }
//                e.onNext(Glide.with(v.getContext())
//                        .load(new GlideUrl(getFuckUrl(url),  builder.build()))
//                        .asBitmap()
//                        .placeholder(R.drawable.a)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .error(R.drawable.a));
//            }
//        }, BackpressureStrategy.BUFFER)
//                .compose(RxSchedulers.io_main())
//                .subscribe(drawableRequestBuilder ->
//                        drawableRequestBuilder.into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        int img_width = resource.getWidth();
//                        int img_height = resource.getHeight();
//                        int height = (int)(1.0 * img_height * ViewUtil.w_screen() / img_width);
//                        ViewGroup.LayoutParams para = v.getLayoutParams();
//                        para.height = height;
//                        para.width = ViewUtil.w_screen();
//                        v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
//                        v.setLayoutParams(para);
//                        v.setImageBitmap(resource);
//                    }
//                }));
        v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
        //网络防盗链
        LazyHeaders.Builder builder = new LazyHeaders.Builder();
        if (refererUrl != null) {
            builder.addHeader("Referer", refererUrl);
        }
        Glide.with(v.getContext())
                .load(new GlideUrl(getFuckUrl(url),  builder.build()))
                .asBitmap()
                .placeholder(R.drawable.a)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.a)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int img_width = resource.getWidth();
                        int img_height = resource.getHeight();
                        int height = (int)(1.0 * img_height * ViewUtil.w_screen() / img_width);
                        ViewGroup.LayoutParams para = v.getLayoutParams();
                        para.height = height;
                        para.width = ViewUtil.w_screen();
                        v.setLayoutParams(para);
                        v.setImageBitmap(resource);
                    }
                });
    }

    @BindingAdapter({"roundImageUrl","refUrl"})
    public static void loadRoundImg(ImageView v, String url,String refererUrl) {
//        Flowable.create(new FlowableOnSubscribe<DrawableRequestBuilder>() {
//            @Override
//            public void subscribe(FlowableEmitter<DrawableRequestBuilder> e) throws Exception {
//                v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
//                //网络防盗链
//                LazyHeaders.Builder builder = new LazyHeaders.Builder();
//                if (refererUrl != null) {
//                    builder.addHeader("Referer", refererUrl);
//                }
//
//                e.onNext(Glide.with(v.getContext())
//                        .load(new GlideUrl(getFuckUrl(url),builder.build()))
//                        .transform(new GlideCircleTransform(v.getContext()))
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .error(R.drawable.a));
//            }
//        }, BackpressureStrategy.BUFFER)
//                .compose(RxSchedulers.io_main())
//                .subscribe(drawableRequestBuilder -> {
//                    v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
//
//                    drawableRequestBuilder.into(v);
//                });
        v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
        //网络防盗链
        LazyHeaders.Builder builder = new LazyHeaders.Builder();
        if (refererUrl != null) {
            builder.addHeader("Referer", refererUrl);
        }

        Glide.with(v.getContext())
                .load(new GlideUrl(getFuckUrl(url),builder.build()))
                .transform(new GlideCircleTransform(v.getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.a)
                .into(v);
    }

    public static String getFuckUrl(String url) {
//        if (url != null && url.startsWith("http://ear.duomi.com/wp-content/themes/headlines/thumb.php?src=")) {
//            url = url.substring(url.indexOf("=") + 1, url.indexOf("jpg") > 0 ? url.indexOf("jpg") + 3 : url.indexOf("png") > 0 ? url.indexOf("png") + 3 : url.length());
//            url = url.replace("kxt.fm", "ear.duomi.com");
//        }
//        return url;
        if(TextUtils.isEmpty(url)){
            return "http://ddcat.noear.org/icon.png";
        }
        return url;
    }

    public static void loadRoundAndBgImg(ImageView v, String url, ImageView im_header) {
        v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
        Glide.with(v.getContext())
                .load(getFuckUrl(url))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(v.getContext()))
                .error(R.mipmap.ic_launcher)
                .into(v);
        im_header.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
        Glide.with(v.getContext()).load(getFuckUrl(url))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new BlurTransformation(v.getContext(), 100))
                .crossFade()
                .into(im_header);
    }

    public static String getUrlByIntent(Context mContext, Intent mdata) {
        Uri uri = mdata.getData();
        String scheme = uri.getScheme();
        String data = "";
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = mContext.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(
                            MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


}
