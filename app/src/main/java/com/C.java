package com;

import com.model.Sections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baixiaokang on 16/4/23.
 */
public class C {

    //==================intent============//
    public static final String TRANSLATE_VIEW = "share_img";


    //==================API============//
    public static final String PAGE = "page";
    public static final String KEY = "KEY";

    //==================Router============//
    public static final String OBJECT_ID = "objectId";


    //==================SITED============//
    public static final String WEB = "web";
    public static final String MAIN = "main";
    public static final String SEARCH = "search";
    public static final String TAG = "tag";
    public static final String BOOK1 = "book1";
    public static final String SECTION1 = "section1";

    public static final String SOURCE = "source";

    public static final String MODEL = "model";

    public static final String DATABIND = "databind";

    public static final String URL = "url";

    public static final String INDEX = "index";

    public static final String REFURL = "refurl";

    public static final String SECTIONS = "sections";

    public static final int NO_MORE = -1;

    public static final String GET_FAVICON = "http://g.soz.im/{url}/cdn.ico?defaulticon=wp";

    public static final String FAVICONBYGG = "http://www.google.com/s2/favicons?domain=";

    public static final String QueryKey = "QueryKey";

    public static final String LOAD_CACHE_ONLY = "load_cache_only"; // 不使用网络，只读取本地缓存数据
    public static final String LOAD_DEFAULT = "load_default"; //（默认）根据cache-control决定是否从网络上取数据。
    public static final String LOAD_NO_CACHE = "load_no_cache";  //不使用缓存，只从网络获取数据.
    public static final String LOAD_CACHE_ELSE_NETWORK = "load+_cache_else_network"; //只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

    public static final String BATTERY = "Battery"; //获取低昂量

    public static final String BOOKNAME = "bookname";
}
