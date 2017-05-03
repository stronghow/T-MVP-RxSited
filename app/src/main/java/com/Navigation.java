//package com;
//
//import android.app.Activity;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Intent;
//import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//
//
//import com.apt.TRouter;
//import com.base.entity.DataExtra;
//import com.dao.AddinClientApi;
//import com.dao.SourceApi;
//import com.dao.db.SiteDbApi;
//import com.dao.engine.DdNode;
//import com.dao.engine.DdSource;
//import com.model.BookModel;
//import com.model.Sections;
//import com.model.TagModel;
//import com.ui.main.R;
//
//
//
///**
// * Created by yuety on 14/10/26.
// * 导航控制(所有页面跳转统一由此控制)，与ios相同的接口设计
// */
//public class Navigation {
//
//    public static void showAddinNavigation(Activity from) {
//        showOutBrowser(from, Config.SITED_WEB_CENTER());
//    }
//
//    public static void showWebBySite(FragmentBase from, String webUrl) {
//
//        if (webUrl.indexOf("http") < 0)
//            webUrl = "http://" + webUrl;
//
//        DdSource sd = SourceApi.getByUrl(webUrl);
//
//        if (sd != null) {
//            DdNode cfg = sd.book(webUrl);
//            if(cfg.isMatch(webUrl))
//                Navigation.showSiteBook(from,webUrl,cfg.dtype());
//            else
//                Navigation.showSiteMain(from, webUrl, sd.title);
//        } else {
//            WebFragment_ frm = new WebFragment_();
//            frm.isBook=false;
//            frm.webUrl = webUrl;
//            doAnimTran(from, frm, true);
//        }
//    }
//
//    public static void showWebOnly(FragmentBase from, String webUrl) {
//        WebFragment_ frm = new WebFragment_();
//        frm.isWebOnly = true;
//        frm.webUrl = webUrl;
//        doAnimTran(from, frm, true);
//    }
//
//    public static void showWebByBook(FragmentBase from, BookModel book) {
//
//        DdSource sd = SourceApi.getByUrl(book.url);
//
//        if (sd != null) {
//            Navigation.showSiteBook(from, book, sd.book(book.url).dtype());
//        } else {
//            WebFragment_ frm = new WebFragment_();
//            frm.isBook = true;
//            frm.webUrl = book.url;
//            doAnimTran(from, frm, true);
//        }
//    }
//
//    public static void showWebOnly(AppCompatActivity from, String webUrl) {
//
//        if (webUrl.indexOf("http") < 0)
//            webUrl = "http://" + webUrl;
//
//        WebFragment_ frm = new WebFragment_();
//        frm.isWebOnly = true;
//        frm.webUrl = webUrl;
//        doReplaceTran(from, frm);
//    }
//
//    public static void showWebAddinLogin(AppCompatActivity from, DdSource source, String webUrl) {
//
//        if (webUrl == null)
//            return;
//
//        WebNavigationActivity.webUrl = webUrl;
//        WebNavigationActivity.isForAddinLogin=true;
//        WebNavigationActivity.source = source;
//
//        Intent intent = new Intent(from, WebNavigationActivity.class);
//        from.startActivity(intent);
//    }
//
//    public static void doShowWebAddinLogin(AppCompatActivity from, String webUrl) {
//
//        if (webUrl.indexOf("http") < 0)
//            webUrl = "http://" + webUrl;
//
//        AddinWebLoginFragment_ frm = new AddinWebLoginFragment_();
//        frm.webUrl = webUrl;
//        doReplaceTran(from, frm);
//    }
//
//    public static void showHome(AppCompatActivity from) {
//
//        showWebOnly(from, Config.SITED_WEB_CENTER());
//    }
//
//    //外部浏览器
//    public static void showOutBrowser(Activity from, String url) {
//        Intent intent= new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri content_url = Uri.parse(url);
//        intent.setData(content_url);
//        from.startActivity(intent);
//    }
//
//    public static void showOutUri(Activity from, String url) {
//        Log.v("Navigation",url);
//
//        Intent intent= new Intent();
//        Uri content_url = Uri.parse(url);
//        intent.setData(content_url);
//        try {
//            from.startActivity(intent);
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }
//    //----------------
//
//
//    private static FragmentBase getBookFragment(AppCompatActivity from, BookModel model, int dtype){
//        if(dtype<4) {
//            BookFragment_ frm = new BookFragment_();
//            frm.bookUrl = model.url;
//            frm.bookLogo = model.logo;
//            frm.bookTitle = model.name;
//            return frm;
//        }
//
//        if(dtype==4) {
//            Book4Fragment_ frm = new Book4Fragment_();
//            frm.bookUrl = model.url;
//            frm.bookLogo = model.logo;
//            frm.bookTitle = model.name;
//
//            return frm;
//        }
//
//        if(dtype==5) {
//            Book5Fragment_ frm = new Book5Fragment_();
//            frm.bookUrl = model.url;
//            frm.bookLogo = model.logo;
//            frm.bookTitle = model.name;
//
//            return frm;
//        }
//
//        if(dtype==6) {
//            Book6Fragment_ frm = new Book6Fragment_();
//            frm.bookUrl = model.url;
//            frm.bookLogo = model.logo;
//            frm.bookTitle = model.name;
//
//            return frm;
//        }
//
//        if(dtype==7){
//            Book7NavigationActivity.model = model;
//
//            Intent intent = new Intent(from, Book7NavigationActivity.class);
//            from.startActivity(intent);
//        }
//
//        return null;
//    }
//
//    public static void showSiteBook(AppCompatActivity from,BookModel model, int dtype) {
//        FragmentBase frm = getBookFragment(from, model, dtype);
//        if (frm != null) {
//            doReplaceTran(from, frm);
//        }
//    }
//
//    public static void showSiteBook(FragmentBase from, BookModel model, int dtype) {
//        FragmentBase frm = getBookFragment(from.activity, model, dtype);
//        if (frm != null) {
//            doAnimTran(from, frm, true);
//        }
//    }
//
//    public static void showSiteBook(FragmentBase from,String url, int dtype) {
//        Log.v("ddcat","showSiteBook");
//        BookModel model = new BookModel();
//        model.url = url;
//
//        FragmentBase frm = getBookFragment(from.activity, model, dtype);
//        if (frm != null) {
//            doAnimTran(from, frm, true);
//        }
//    }
//
//    public static void showSiteMain(AppCompatActivity from, String key, String title)
//    {
//        MainFragment_ frm = new MainFragment_();
//        frm.sourceKey = key;
//
//        doReplaceTran(from, frm);
//    }
//
//    public static void showSiteMain(FragmentBase from, String key, String title)
//    {
//        MainFragment_ frm = new MainFragment_();
//        frm.sourceKey = key;
//
//        doAnimTran(from, frm, true);
//    }
//
//
//    public static void showSiteSearch(FragmentBase from, DdSource source, String key) {
//        SearchFragment_ frm = new SearchFragment_();
//        frm.source = source;
//        frm.key    = key;
//
//        doAnimTran(from, frm, true);
//    }
//
//    public static void showSiteTag(FragmentBase from, DdSource source, TagModel tag)
//    {
//        TagFragment_ frm  = new TagFragment_();
//        frm.source = source;
//        frm.model  = tag;
//
//        doAnimTran(from,frm,true);
//
//
//    }
//
//    public static void showSiteSectionNavigation(Activity from, DdSource source, BookViewModel bookViewModel, Sections sec)
//    {
//        if(TextUtils.isEmpty(sec.url))
//            return;
//
//        DbApi.setBookLastLook(bookViewModel,sec.url,-1);
//        bookViewModel.setLastLook(sec.url);
//
//
//        DdNode cfg = source.section(sec.url);
//
//        if (cfg.isWebrun()) { //如果只能web打开，则打开浏览器
//            String webUrl = source.buildWeb(cfg, sec.url);
//
//            showOutBrowser(from, webUrl);
//            return;
//        }
//
//        int dtype = cfg.dtype();
//
//        //----------
//
//        if(dtype==1) { //漫画
//            Section1NavigationActivity.util = new Section1NavigationViewModel(source, bookViewModel, sec);
//
//            Intent intent = new Intent(from, Section1NavigationActivity.class);
//            from.startActivity(intent);
//            return;
//        }
//
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
//    }
//
//    public static void showSiteOptions(FragmentBase from, Section1NavigationViewModel util)
//    {
//        Options1Fragment_ frm  = new Options1Fragment_();
//        frm.util = util;
//
//        doAnimTranRoot(from, frm, true);
//    }
//
//
//    public static void navByUri(AppCompatActivity from, String uri) {
//        if (TextUtils.isEmpty(uri))
//            return;
//
//        String webUrl = uri.replace("sited://","http://");
//
//
//
//        if(webUrl.indexOf(".sited")>0){
//            Log.d("navByUri", ".sited");
//            AddinClientApi.downAddin(webUrl, true, (code, sd) -> {
//                if (code == 1) {
//                    Log.d("navByUri", "code == 1");
//                    TRouter.go(C.MAIN,new DataExtra(C.URL,sd.url));
//                    Navigation.showSiteMain(from, sd.url, sd.title);
//                    //Setting.setIsLocalSite(true);
//                }
//            });
//        }else {
//            DdSource sd = SourceApi.getByUrl(webUrl);
//
//            if (sd != null) {
//                DdNode cfg = sd.book(webUrl);
//
//                if (cfg.isMatch(webUrl)) {
//                    BookModel book = new BookModel();
//                    book.url = webUrl;
//                    Navigation.showSiteBook(from, book, cfg.dtype());
//                } else
//                    Navigation.showSiteMain(from, webUrl, sd.title);
//            } else {
//                TRouter.go(C.MAIN,new DataExtra(C.));
//
////                WebFragment_ frm = new WebFragment_();
////                frm.isBook = false;
////                frm.webUrl = webUrl;
////                doReplaceTran(from, frm);
//            }
//        }
//    }
//
//    public static void navByUri(FragmentBase from,String uri) {
//        Log.v("ddcat","navByUri");
//        if (TextUtils.isEmpty(uri))
//            return;
//
//        String webUrl = uri.replace("sited://","http://");
//
//        if(webUrl.indexOf(".sited")>0){
//            AddinClientApi.downAddin(webUrl, true, (code, sd) -> {
//                if (code == 1) {
//                    //SiteDbApi.addSubscibeByKey(sd.url_md5, sd.url, sd.title);
//
//                    Navigation.showSiteMain(from, sd.url, sd.title);
//                    //Setting.setIsLocalSite(true);
//                }
//            });
//        }else {
//            DdSource sd = SourceApi.getByUrl(webUrl);
//
//            if (sd != null) {
//                DdNode cfg = sd.book(webUrl);
//
//                if (cfg.isMatch(webUrl)) {
//                    BookModel book = new BookModel();
//                    book.url = webUrl;
//                    Navigation.showSiteBook(from, book,cfg.dtype());
//                } else
//                    Navigation.showSiteMain(from, webUrl, sd.title);
//            } else {
//                Navigation.showWebBySite(from, webUrl);
//            }
//        }
//    }
//
//    public static void navByCode(FragmentBase from, int tileID) {
//
//        switch (tileID) {
//
//            case 995:
//                Navigation.showWebBySite(from,Config.SITED_WEB_CENTER());
//                break;
//
//            case 999:
//                Navigation.showAddinNavigation(from.activity);
//                break;
//        }
//    }
//
//    //
//    //-------------
//    //
//    private static void doAnimTran(FragmentBase from, FragmentBase toFragment, boolean isCanBack) {
//        FragmentManager fm = from.getActivity().getFragmentManager();
//
//        if (fm == null)
//            return;
//
//        FragmentTransaction ft = fm.beginTransaction();
//
//        if (Config.isPhone()) {
//            ft.setCustomAnimations(R.animator.fragment_enter, R.animator.fragment_exit, R.animator.fragment_pop_enter, R.animator.fragment_pop_exit);
//        }
//
//        if(from.isLeftMenu()==false) {
//            ft.hide(from);
//        }
//
////        if(Config.isPhone())
//            ft.add(R.id.frame_layout, toFragment);
////        else
////            ft.add(R.id.frame2_layout, toFragment);
//
//        if(isCanBack) {ft.addToBackStack(null);}
//
//        ft.commit();
//    }
//
//    private static void doAnimTranRoot(FragmentBase from, FragmentBase toFragment, boolean isCanBack) {
//        FragmentManager fm = from.getActivity().getFragmentManager();
//
//        if (fm == null)
//            return;
//
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.setCustomAnimations(R.animator.fragment_enter, R.animator.fragment_exit, R.animator.fragment_pop_enter, R.animator.fragment_pop_exit);
//        ft.add(R.id.frame_layout, toFragment);
//
//        if (isCanBack) {
//            ft.addToBackStack(null);
//        }
//
//        ft.commit();
//    }
//
//    private static void doReplaceTran(AppCompatActivity from, FragmentBase toFragment)
//    {
//        FragmentManager fm = from.getFragmentManager();
//
//        if(fm==null)
//            return;
//
//        FragmentTransaction ft = fm.beginTransaction();
//
//        ft.replace(R.id.frame_layout, toFragment);
//        ft.show(toFragment);
//        ft.commit();
//    }
//
//
//
//    //-------------------
//    public static boolean isNumeric(String str) {
//        for (int i = str.length(); --i >= 0; ) {
//            if (!Character.isDigit(str.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//}
