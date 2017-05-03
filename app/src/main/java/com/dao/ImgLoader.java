//package com.dao;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.dao.engine.DdNode;
//import com.model.PicModel;
//import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
//import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
//import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
//import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//
//import org.noear.sited.SdNode;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import me.noear.exts.Act1;
//import me.noear.exts.Act2;
//import me.noear.exts.Act3;
//
///**
// * Created by yuety on 15/7/22.
// * 图片加载器 (此接口与ios相同, 便于代码迁移)
// */
//public class ImgLoader {
//    public static class ImageLoaderEx extends ImageLoader
//    {
//        public ImageLoaderEx(){
//            super();
//        }
//    }
//
//    private static ImageLoaderEx logoLoader  = new ImageLoaderEx();
//    private static ImageLoaderEx picsLoader = new ImageLoaderEx();
//
//    public static  Bitmap getCache(String key) {
//        File file =  picsLoader.getDiskCache().get(key);
//        if (file == null || file.isFile()==false)
//            return null;
//
//        try {
//            Bitmap image = BitmapFactory.decodeStream(new FileInputStream(file));
//
//            return image;
//        }catch (Exception ex){
//            ex.printStackTrace();
//            return null;
//        }
//    }
//
//    public static void setCache(String key, Bitmap image) {
//        if (image == null) {
//             picsLoader.getDiskCache().remove(key);
//        }
//        else {
//            try {
//                picsLoader.getDiskCache().save(key, image);
//            }catch (Exception ex){
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    public static void init(Context context)
//    {
//        File _root = null;
//        if (_root == null) {
//            _root = context.getExternalFilesDir(null); //内置ＳＤ卡
//        }
//
//        if (_root == null) {
//            _root = context.getFilesDir();
//        }
//
//
//        initLogo(context, _root);
//        initImage(context,_root);
//    }
//
//    static void initLogo(Context context, File root) {
//        File cacheDir = new File(root, "comics_logo");
//
//        DisplayImageOptions displayImageOptions = new DisplayImageOptions
//                .Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration
//                .Builder(context)
//                .threadPoolSize(5)//线程池内加载的数量
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new UsingFreqLimitedMemoryCache(10 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
//                .tasksProcessingOrder(QueueProcessingType.FIFO)
//                .diskCache(new UnlimitedDiskCache(cacheDir, null,new Md5FileNameGenerator()))//自定义缓存路径//100M
//                .defaultDisplayImageOptions(displayImageOptions)
//                .imageDownloader(new ImgLoader.HttpClientLogoDownloader(context, 10 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//                .build();//开始构建
//
//        logoLoader.init(config);
//    }
//
//    static ImageLoaderConfiguration picsConfig;
//    static void initImage(Context context, File root){
//        File cacheDir = new File(root,"comics_pics");
//
//        DisplayImageOptions displayImageOptions = new DisplayImageOptions
//                .Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
//
//        picsConfig = new ImageLoaderConfiguration
//                .Builder(context)
//                .threadPoolSize(5)//线程池内加载的数量
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new UsingFreqLimitedMemoryCache(10 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
//                .tasksProcessingOrder(QueueProcessingType.FIFO)
//                .diskCache(new UnlimitedDiskCache(cacheDir, null,new Md5FileNameGenerator()))//自定义缓存路径//1000M*10
//                .defaultDisplayImageOptions(displayImageOptions)
//                .imageDownloader(new ImgLoader.HttpClientImageDownloader(context, 10 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//                .build();//开始构建
//
//        picsLoader.init(picsConfig);
//
//    }
//
//
//    public static void displayImage(ImageView imageView, String uri,String referer)
//    {
//        if(uri!=null && uri.length()>7) {
//            DisplayImageOptions options = new DisplayImageOptions
//                    .Builder()
//                    .cacheInMemory(true)
//                    .cacheOnDisk(true)
//                    .extraForDownloader(referer).build();
//
//            logoLoader.displayImage(uri, imageView,options);
//        }
//    }
//
//    public static void downloadImage(String uri,String referer,Act1<Bitmap> callback)
//    {
//        if(uri!=null && uri.length()>7) {
//            DisplayImageOptions options = new DisplayImageOptions
//                    .Builder()
//                    .cacheInMemory(true)
//                    .cacheOnDisk(true)
//                    .extraForDownloader(referer).build();
//
//            logoLoader.loadImage(uri, new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String s, View view) {
//
//                }
//
//                @Override
//                public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//                }
//
//                @Override
//                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                    callback.run(bitmap);
//                }
//
//                @Override
//                public void onLoadingCancelled(String s, View view) {
//
//                }
//            });
//        }
//    }
//
//
//    public static void displaySectionImage(ImageView imageView, DdNode objConfig, PicModel pic, Act3<View,Bitmap,PicModel> callback) {
//        SectionImageContext cx = new SectionImageContext();
//        cx.sectionUrl = pic.sections.url;
//        cx.refererUrl = objConfig.referer(pic.sections.url);
//        cx.config = objConfig;
//
//        DisplayImageOptions options = new DisplayImageOptions
//                .Builder()
//                .cacheInMemory(false)
//                .cacheOnDisk(true)
//                .extraForDownloader(cx).build();
//
//        picsLoader.loadImage(pic.url, options, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//                Log.v("displaySectionImage","onLoadingStarted");
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//                Log.v("displaySectionImage","onLoadingFailed");
//                imageView.setImageBitmap(null);
//                if (callback != null)
//                    callback.run(imageView, null, pic);
//            }
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                Log.v("displaySectionImage","onLoadingComplete");
//                imageView.setImageBitmap(bitmap);
//                if (callback != null) {
//                    callback.run(imageView, bitmap, pic);
//                }
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//                Log.v("displaySectionImage","onLoadingCancelled");
//                if (callback != null)
//                    callback.run(imageView, null, pic);
//            }
//        });
//    }
//
//
//
//    //根据情况，开入新的接口
//
//    public static void downloadSectionImage(DdNode objConfig, PicModel pic, Act3<Integer,Bitmap,PicModel> callback) {
//        SectionImageContext cx = new SectionImageContext();
//        cx.sectionUrl = pic.sections.url;
//        cx.refererUrl = objConfig.referer(pic.sections.url);
//        cx.config = objConfig;
//
//        DisplayImageOptions options = new DisplayImageOptions
//                .Builder()
//                .cacheInMemory(false)
//                .cacheOnDisk(true)
//                .extraForDownloader(cx)
//                .build();
//
//
//        picsLoader.loadImage(pic.url, options, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//                Log.v("downloadSectionImage","onLoadingStarted");
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//                Log.v("downloadSectionImage","onLoadingFailed");
//                if (callback != null)
//                    callback.run(-2, null, pic);
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                Log.v("downloadSectionImage","onLoadingComplete");
//                if (callback != null) {
//                    callback.run(1, bitmap, pic);
//                }
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//                Log.v("downloadSectionImage","onLoadingCancelled");
//                if (callback != null)
//                    callback.run(-1, null, pic);
//            }
//        });
//
//    }
//
//    public static void downloadSectionImage(DdNode objConfig, String sectionUrl, String picUrl, Act2<Integer,Bitmap> callback) {
//        SectionImageContext cx = new SectionImageContext();
//        cx.sectionUrl = sectionUrl;
//        cx.refererUrl = objConfig.referer(sectionUrl);
//        cx.config = objConfig;
//
//        DisplayImageOptions options = new DisplayImageOptions
//                .Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .extraForDownloader(cx)
//                .build();
//
//
//        picsLoader.loadImage(picUrl, options, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//                if (callback != null)
//                    callback.run(-2, null);
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                if (callback != null) {
//                    callback.run(1, bitmap);
//                }
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//
//                if (callback != null)
//                    callback.run(-1, null);
//            }
//        });
//    }
//
//    //-------------
//
//    public static void clearPics(){
//        logoLoader.getDiskCache().clear();
//        picsLoader.getDiskCache().clear();
//    }
//
//    public static void removePic(String url){
//        picsLoader.getDiskCache().remove(url);
//
//    }
//
//    public static void clearLogo(){
//        logoLoader.getDiskCache().clear();
//    }
//
//
//    public static class SectionImageContext
//    {
//        public String refererUrl;
//        public String sectionUrl;
//        public SdNode config;
//    }
//
//    public static class HttpClientImageDownloader extends BaseImageDownloader {
//
//        public HttpClientImageDownloader(Context context, int connectTimeout, int readTimeout) {
//            super(context, connectTimeout, readTimeout);
//        }
//
//        @Override
//        protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
//            SectionImageContext cx = null;
//            if (extra != null) {
//                cx = (SectionImageContext) extra;
//            }
//
//            String encodedUrl = null;
//            if (cx == null)
//                encodedUrl = Uri.encode(url, "@#&=*+-_.,:!?()/~\'%");
//            else {
//                if (TextUtils.isEmpty(cx.config.args)) //如果没有tag标识
//                    encodedUrl = Uri.encode(url, "@#&=*+-_.,:!?()/~\'%");
//                else
//                    encodedUrl = url;
//            }
//
//
//            HttpURLConnection conn = (HttpURLConnection) (new URL(encodedUrl)).openConnection();
//            conn.setConnectTimeout(this.connectTimeout);
//            conn.setReadTimeout(this.readTimeout);
//
//            if (extra != null) {
//                conn.setRequestProperty("User-Agent", cx.config.ua());
//
//                if (cx.config.isInCookie()) {
//                    String cookies = cx.config.cookies(cx.sectionUrl);
//                    if (cookies != null) {
//                        conn.setRequestProperty("Cookie", cookies);
//                    }
//                }
//
//                if (cx.config.isInReferer()) {
//                    conn.setRequestProperty("Referer", cx.refererUrl);
//                }
//
//                if (TextUtils.isEmpty(cx.config.header) == false) {
//                    for (String kv : cx.config.header.split(";")) {
//                        String[] kv2 = kv.split("=");
//                        if (kv2.length == 2) {
//                            conn.setRequestProperty(kv2[0], kv2[1]);
//                        }
//                    }
//                }
//
//            }
//
//            return conn;
//        }
//    }
//
//    public static class HttpClientLogoDownloader extends BaseImageDownloader {
//
//        public HttpClientLogoDownloader(Context context, int connectTimeout, int readTimeout) {
//            super(context, connectTimeout, readTimeout);
//        }
//
//        @Override
//        protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
//
//            String encodedUrl =  Uri.encode(url, "@#&=*+-_.,:!?()/~\'%");
//
//            HttpURLConnection conn = (HttpURLConnection) (new URL(encodedUrl)).openConnection();
//            conn.setConnectTimeout(this.connectTimeout);
//            conn.setReadTimeout(this.readTimeout);
//
//            if (extra != null) {
//                conn.setRequestProperty("Referer", (String) extra);
//            }
//
//            return conn;
//        }
//    }
//}
