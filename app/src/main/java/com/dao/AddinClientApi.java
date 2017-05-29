package com.dao;

import android.util.Log;

import com.base.util.ToastUtil;
import com.dao.db.SiteDbApi;
import com.dao.engine.DdSource;
import com.model.SourceModel;
import com.socks.library.KLog;

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
                    SiteDbApi.insertOrUpdate(sourceModel);

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
}
