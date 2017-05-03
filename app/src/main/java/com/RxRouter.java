package com;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.apt.TRouter;
import com.base.entity.DataExtra;
import com.dao.AddinClientApi;
import com.dao.SourceApi;
import com.dao.engine.DdNode;
import com.dao.engine.DdSource;
import com.model.BookViewModel;
import com.model.Sections;
import com.model.Tag;
import com.socks.library.KLog;

import java.util.HashMap;

/**
 * Created by haozhong on 2017/4/12.
 */

public class RxRouter {

    public static void openWeb(String Url){
        TRouter.go(C.WEB, DataExtra.getExtra(C.URL, Url));
    }

    public static void navByUri(Activity from, String uri) {
        if (TextUtils.isEmpty(uri))
            return;

        String webUrl = uri.replace("sited://","http://");



        if(webUrl.indexOf(".sited")>0){
            Log.d("navByUri", ".sited");
            AddinClientApi.downAddin(webUrl, true, (code, sd) -> {
                if (code == 1) {
                    Log.d("navByUri", "code == 1");
                    HashMap map = new HashMap();
                    map.put(C.URL,sd.url);
                    map.put(C.SOURCE,sd);
                    KLog.json(sd.hots.title + sd.updates.title + sd.tags.title);
                    KLog.json(sd.intro);
                    //TRouter.go(C.MAIN,map);
                    TRouter.go(C.TAG,map);
                    //Navigation.showSiteMain(from, sd.url, sd.title);
                    //Setting.setIsLocalSite(true);
                }
            });
        }else {
            DdSource sd = SourceApi.getByUrl(webUrl);

            if (sd != null) {
                DdNode cfg = sd.book(webUrl);

//                if (cfg.isMatch(webUrl)) {
//                    BookModel book = new BookModel();
//                    book.url = webUrl;
//                    Navigation.showSiteBook(from, book, cfg.dtype());
//                } else
//                    Navigation.showSiteMain(from, webUrl, sd.title);
            } else {
//                TRouter.go(C.MAIN,new DataExtra(C.));

//                WebFragment_ frm = new WebFragment_();
//                frm.isBook = false;
//                frm.webUrl = webUrl;
//                doReplaceTran(from, frm);
            }
        }
    }

    public static void navByUri(String url){
        navByUri(null,url);
    }

    public static void showSiteTag(Activity from, DdSource source, Tag tag){
        HashMap map = new HashMap();
        map.put(C.MODEL,tag);
        map.put(C.SOURCE,source);
        TRouter.go(C.TAG,map);
   }

    public static void showSiteSectionNavigation(Activity from, DdSource source, BookViewModel bookViewModel, Sections sec)
    {
        if(TextUtils.isEmpty(sec.url))
            return;

        //DbApi.setBookLastLook(bookViewModel,sec.url,-1);
        bookViewModel.setLastLook(sec.url);


        DdNode cfg = source.section(sec.url);

//        if (cfg.isWebrun()) { //如果只能web打开，则打开浏览器
//            String webUrl = source.buildWeb(cfg, sec.url);
//
//            showOutBrowser(from, webUrl);
//            return;
//        }

        int dtype = cfg.dtype();

        //----------

        if(dtype==1) { //漫画

            return;
        }

//        if(dtype==2) { //轻小说
//            Section2NavigationActivity.util = new Section2NavigationViewModel(source, bookViewModel, sec);
//
//            Intent intent = new Intent(from, Section2NavigationActivity.class);
//            from.startActivity(intent);
//            return;
//        }
//
//        if(dtype==3){ //动画
//            Section3NavigationActivity.util = new Section3NavigationViewModel();
//            Section3NavigationActivity.util.source = source;
//            Section3NavigationActivity.util.model  = sec;
//            Section3NavigationActivity.util.bookViewModel = bookViewModel;
//
//            Intent intent = new Intent(from, Section3NavigationActivity.class);
//            from.startActivity(intent);
//            return;
//        }
    }
}
