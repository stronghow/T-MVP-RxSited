package com;

import com.model.Sections;

import java.util.ArrayList;

/**
 * Created by baixiaokang on 16/4/23.
 */
public class C {
    //==================API============//
    public static final String X_LC_Id = "i7j2k7bm26g7csk7uuegxlvfyw79gkk4p200geei8jmaevmx";
    public static final String X_LC_Key = "n6elpebcs84yjeaj5ht7x0eii9z83iea8bec9szerejj7zy3";
    public static final String BASE_URL = "https://leancloud.cn:443/1.1/";

    public static final String ADMIN_ID = "53d9076ce4b0ef69707fc78c";
    public static final String ADMIN_FACE = "https://avatars0.githubusercontent.com/u/7598555?v=3&s=460";
    //==================Base============//
    public static final int PAGE_COUNT = 10;
    public static final int FLAG_MULTI_VH = 0x000001;
    public static final String OPEN_TYPE = "公开";

    //==================intent============//
    public static final String TRANSLATE_VIEW = "share_img";
    public static final String TYPE = "type";
    public static final String HEAD_DATA = "data";
    public static final String VH_CLASS = "vh";
    public static final int IMAGE_REQUEST_CODE = 100;


    //==================API============//
    public static final String _CREATED_AT = "-createdAt";
    public static final String INCLUDE = "include";
    public static final String CREATER = "creater";
    public static final String UID = "uId";
    public static final String PAGE = "page";

    //==================Router============//
    public static final String OBJECT_ID = "objectId";


    //==================SITED============//
    public static final String WEB = "web";
    public static final String MAIN = "main";
    public static final String TAG = "tag";
    public static final String BOOK1 = "book1";
    public static final String SECTION1 = "section1";

    public static final String TAG_TYPE = "tag_type";

    public static final String SOURCE = "source";

    public static final String MODEL = "model";

    public static final String URL = "url";

    public static final String REFURL = "refurl";

    public static final String ISUPDATE = "isUpdate";

    public static final int NO_MORE = -1;

    public static final String BEGIN = "begin";

    public static final String SETIONS = "setions";

    public static final String GET_FAVICON = "http://g.soz.im/{url}/cdn.ico?defaulticon=wp";

    public static final String FAVICONBYGG = "http://www.google.com/s2/favicons?domain=";

    public static final String QueryKey = "QueryKey";

    public static final String LOAD_CACHE_ONLY = "load_cache_only"; // 不使用网络，只读取本地缓存数据
    public static final String LOAD_DEFAULT = "load_default"; //（默认）根据cache-control决定是否从网络上取数据。
    public static final String LOAD_NO_CACHE = "load_no_cache";  //不使用缓存，只从网络获取数据.
    public static final String LOAD_CACHE_ELSE_NETWORK = "load+_cache_else_network"; //只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

    public static ArrayList<Sections> sSectionses = new ArrayList<>();

    public static int oldIndex;

    public static int newIndex;

    public static final String BATTERY = "Battery";

    public static final String BOOKNAME = "bookname";
}
