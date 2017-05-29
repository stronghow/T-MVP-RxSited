package com.dao;

import android.text.TextUtils;

import com.App;
import com.dao.db.SiteDbApi;
import com.dao.engine.DdSource;
import com.model.SourceModel;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by noear on 15/10/8.
 *
 */
public class SourceApi {

    static List<DdSource> _sourceList;

    public static void tryInit() {
        if (_sourceList == null) {
            _sourceList = new ArrayList<>();
        }
    }

    public static int getCount(){
        tryInit();

        return _sourceList.size();
    }

    public static DdSource loadSource(String sited, String cookies, boolean isUpdate) {

        //KLog.json(sited);
        DdSource sd = parseSource(sited);

        if (sd != null) {
            KLog.json("sd != null");
            if (cookies != null) {
                    sd.setCookies(cookies);
            }

            addSource(sd, cookies, true);
        }else {
            KLog.json("sd == null");
        }

        return sd;
    }

    public  static void addSource(DdSource sd, String cookies, boolean isUpdate) {
        if (isUpdate) {
            for (int i = 0, len = _sourceList.size(); i < len; i++) {
                DdSource s1 = _sourceList.get(i);
                if (s1.isMatch(sd.url)) {
                    if (cookies == null) {
                        sd.setCookies(s1.cookies());
                    }
                    _sourceList.remove(i);
                    break;
                }
            }
        }

        _sourceList.add(sd);
    }

    public static DdSource parseSource(String sited){
        try {
            DdSource sd = new DdSource(App.getCurrent(), sited);
            return sd;
        }catch (Exception ex) {
            KLog.json(ex.getMessage());
            ex.printStackTrace();
//            if (Setting.isDeveloperModel())
//                HintUtil.show("SourceApi.error::" + ex.getMessage());
//            else
//                HintUtil.show("此插件暂不支持你的手机;(");

            return null;
        }
    }



    public static DdSource getByUrl(String url) {
        tryInit();

        for (DdSource sd : _sourceList) {
            if (sd.isMatch(url))
                return sd;
        }

        SourceModel m = SiteDbApi.getSourceByUrl(url);
        if(m != null) {
            if (TextUtils.isEmpty(m.sited) == false) {
                return loadSource(m.sited, m.cookies, false);
            }
        }

        return null;
    }
}
