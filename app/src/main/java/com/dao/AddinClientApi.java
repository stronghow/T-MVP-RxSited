package com.dao;

import android.util.Log;

import com.base.util.ToastUtil;
import com.dao.db.SiteDbApi;
import com.dao.engine.DdSource;
import com.model.SourceModel;
import com.socks.library.KLog;

import me.noear.exts.Act1;
import me.noear.exts.Act2;
import me.noear.utils.HttpUtil;

/**
 * Created by noear on 15/10/8.
 *
 * 下载前叫：插件
 * 下载后叫：源（下载后，插件成为一个数据源）
 */
public class AddinClientApi {

    //下载插件
    public static void downAddin(String src, boolean isSubscribe, Act2<Integer, DdSource> callback) {
        Log.v("downSource", src);

        Log.v("AddinClientApi.down", src);
        HttpUtil.get(src, (code, text) -> {
            if (code == 1) {
                KLog.json(text);
                DdSource sd = SourceApi.loadSource(text, null, true);
                if (sd != null) {
                    //添加到数据库

                    SourceModel sourceModel = new SourceModel();
                    sourceModel.title = sd.title;
                    sourceModel.url = sd.url;
                    sourceModel.sited = text;
                    KLog.json(text);
                    //sourceModel.source = sd;
                    SiteDbApi.insertOrUpdate(sourceModel);
                    //SiteDbApi.addSource(sd, sd.sited, isSubscribe);
                    sd.sited = null;
                    KLog.json(""+code);
                    callback.run(code, sd);
                } else {
                    KLog.json(""+"-3");
                    callback.run(-3, null);
                }
            } else {
                ToastUtil.show("请检查网络：（");
                callback.run(code, null);
            }
        });
    }

    //检查
    public static void tryCheck(String sds, String url, Act1<SourceModel> callback) {
//        if (TextUtils.isEmpty(sds) || sds.indexOf(".noear.org") > 0) {
//            sds = Config.SITED_DEF_API + "chk/";
//        }
//
//        sds = sds + url;
//
//        Log.v("AddinClientApi.chk", sds);
//
//
//        Map<String, String> postData = new HashMap<String, String>();
//        postData.put("u_id", Session.userID + "");
//        postData.put("u_vip", Session.isVip + "");
//        postData.put("u_level", Session.level + "");
//        postData.put("appid", Config.WEBAPI_CID + "");
//        postData.put("appver", Config.getBuilder() + "");
//
//        HttpUtil.post(sds, postData, (code, text) -> {
//
//            if (code == 1) {
//                ONode data = ONode.tryLoad(text);
//                if (data.isObject()) {
//                    SourceModel m = new SourceModel();
//                    m.url = url;
//                    m.loadByJson(data);
//
//                    callback.run(m);
//                    return;
//                }
//            }
//
//            callback.run(null);
//        });
    }
}
