package com.sited;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ui.main.R;

import java.util.List;

/**
 * Created by haozhong on 2017/6/27.
 */

public class RxJscript {

    private final List<Lib> require;
    private final String code;

    public RxJscript(@Nullable String code, List<Lib> require){
        this.code = code;
        this.require = require;
    }

    public boolean load(JsEngine js){
        js.loadJs(code); //fun
        if(require != null && require.size()>0) {  //lib
            for (Lib lib : require) {
                if (!loadLib(js, lib.lib))
                   js.loadJs(HttpUtil.getHtml(lib.url));
//                    RxHttp.get(lib.url)
//                            .subscribe(s -> js.loadJs(s));
            }
        }else{
            loadLib(js,"cheerio");
        }
        return true;
    }

    private boolean loadLib(JsEngine js,String lib){
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
                    js.loadJs(Utils.getFileString(R.raw.cheerio));
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
