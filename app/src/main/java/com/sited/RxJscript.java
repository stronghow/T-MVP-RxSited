package com.sited;

import android.text.TextUtils;

import com.ui.main.R;

import java.util.List;

/**
 * Created by haozhong on 2017/6/27.
 */

public class RxJscript {
    private static final String cheerio = Utils.getFileString(R.raw.cheerio);

    public static boolean load(JsEngine js, String code, List<Lib> require){
        js.loadJs(code); //fun
        if(require != null && require.size()>0) {  //lib
            for (Lib lib : require) {
                if (!loadLib(js, lib.lib))
                   js.loadJs(HttpUtil.getHtml(lib.url));
            }
        }else{
            loadLib(js,"cheerio");
        }
        return true;
    }

    private static boolean loadLib(JsEngine js,String lib){
        if(TextUtils.isEmpty(lib)) return false;
        try {
            switch (lib) {
                case "md5":
                    js.loadJs(Utils.getFileString(R.raw.md5));
                    break;
                case "sha1":
                    js.loadJs(Utils.getFileString(R.raw.sha1));
                    break;
                case "base64":
                    js.loadJs(Utils.getFileString(R.raw.base64));
                    break;
                case "cheerio":
                    if(cheerio == null)
                        js.loadJs(Utils.getFileString(R.raw.cheerio));
                    else js.loadJs(cheerio);
                    break;
                default:
                    return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
