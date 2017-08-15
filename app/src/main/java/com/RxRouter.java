package com;

import android.text.TextUtils;

import com.apt.TRouter;
import com.base.entity.DataExtra;
import com.base.util.ToastUtil;
import com.dao.db.SiteDbApi;
import com.model.SourceModel;
import com.sited.HttpUtil;
import com.sited.RxSource;
import com.sited.RxSourceApi;
import com.socks.library.KLog;

import java.util.HashMap;

/**
 * Created by haozhong on 2017/4/12.
 */

public class RxRouter {

    public static void openWeb(String Url){
        TRouter.go(C.WEB, DataExtra.getExtra(C.URL, Url));
    }

    public static boolean goTag(RxSource sd,String sited){
        if(sd.tags == null) {
            ToastUtil.show("对不起,暂时不支持无Tags节点的插件:)");
            return false;
        }
        SourceModel sourceModel = new SourceModel();
        sourceModel.title = sd.title;
        sourceModel.url = sd.url;
        sourceModel.sited = sited;
        SiteDbApi.insertOrUpdate(sourceModel);
        HashMap<String,Object> map = new HashMap<>();
        map.put(C.URL,null);
        map.put(C.SOURCE,sd);
        TRouter.go(C.TAG,map);
        return true;
    }

    public static void navByUri(String uri) {
        if (TextUtils.isEmpty(uri))
            return;

        String webUrl = uri.replace("sited://","http://");

        if(webUrl.indexOf(".sited")>0){
            KLog.json("navByUri", ".sited");
            String Html = HttpUtil.getHtml(webUrl);
            RxSource sd = RxSourceApi.getRxSource(Html);
            if(sd == null) return;
            if(sd.tags == null) {
                ToastUtil.show("对不起,暂时不支持无Tags节点的插件:)");
                return;
            }
            SourceModel model = new SourceModel();
            model.expr = sd.expr;
            model.sited = Html;
            model.title = sd.title;
            model.url = sd.url;
            SiteDbApi.insertOrUpdate(model);
            HashMap<String,Object> map = new HashMap<>();
            map.put(C.URL,sd.url);
            map.put(C.SOURCE,sd);
            TRouter.go(C.TAG,map);
        }
    }
}
