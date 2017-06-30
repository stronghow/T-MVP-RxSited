package com.ui.main;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.App;
import com.C;
import com.DbFactory;
import com.RxRouter;
import com.app.annotation.apt.InstanceFactory;
import com.apt.TRouter;
import com.base.adapter.AdapterPresenter;
import com.base.util.ToastUtil;
import com.dao.SourceApi;
import com.dao.db.SiteDbApi;
import com.dao.engine.DdSource;
import com.model.SourceModel;
import com.socks.library.KLog;
import com.utils.Base64Util;
import com.utils.CallUtil;
import com.utils.FileUtil;

import java.util.HashMap;

/**
 * Created by haozhong on 2017/4/5.
 */

@InstanceFactory
public class MainPresenter extends MainContract.Presenter {
    @Override
    public void initAdapterPresenter(AdapterPresenter<SourceModel> mAdapterPresenter, HashMap map) {
        mAdapterPresenter.setDbRepository(DbFactory::getSource)
                .setBegin(C.NO_MORE)
                .fetch();
    }

    @Override
    public boolean forIntent(Intent intent, boolean isNewIntent) {
        KLog.json("forIntent");
        if(intent!=null&&intent.getIntExtra("cmd", 0)<=0
                &&intent.getAction().equals(Intent.ACTION_VIEW)
                &&intent.getData().getScheme()!=null
                &&intent.getData().getScheme().equals("sited")) //网络的启动参数
        {
            if(intent.getData().getHost().equals("data"))
                RxRouter.navByUri(Base64Util.decode(intent.getData().getQuery()));
            RxRouter.navByUri(Base64Util.decode(intent.getData().getHost()));
            return true;
        }

        if(intent!=null&&intent.getIntExtra("cmd", 0)<=0
                &&intent.getAction().equals(Intent.ACTION_VIEW)
                &&intent.getData().getScheme()!=null
                &&intent.getData().getScheme().equals("file")) //本地文件启动参数
        {
            if(intent.getData().toString().indexOf(".sited")<0) {
                ToastUtil.show("不是有效插件");
                return false;
            }else{
               return parsebyfile(intent.getData());
            }
        }

        if(intent!=null&&intent.getIntExtra("cmd", 0)<=0
                &&!TextUtils.isEmpty(intent.getStringExtra("sited"))) //网络的启动参数
        {
            RxRouter.navByUri(intent.getStringExtra("sited"));
            return true;
        }
        return false;
    }

    private boolean parsebyfile(Uri url){
        try {
            ContentResolver cr = App.getContext().getContentResolver();
            String sited = FileUtil.toString(cr.openInputStream(url));

            DdSource sd = SourceApi.loadSource(sited, null, true);
            if (sd != null) {
                SourceModel sourceModel = new SourceModel();
                sourceModel.title = sd.title;
                sourceModel.url = sd.url;
                sourceModel.sited = sited;
                sourceModel.cookies = sd.cookies();
                SiteDbApi.insertOrUpdate(sourceModel);
                sd.sited = null;

                ToastUtil.show("插件已成功安装");

                CallUtil.asynCall(() -> {
                    HashMap map = new HashMap();
                    map.put(C.URL,null);
                    map.put(C.SOURCE,sd);
                    TRouter.go(C.TAG,map);
                });
                return true;
            } else {
                ToastUtil.show("插件格式有问题");
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            ToastUtil.show("出错：" + ex.getMessage());
            return false;
        }
    }
}
