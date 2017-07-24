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
import com.base.adapter.AdapterPresenter;
import com.base.util.ToastUtil;
import com.model.SourceModel;
import com.sited.RxSource;
import com.sited.RxSourceApi;
import com.socks.library.KLog;
import com.utils.Base64Util;
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
            RxSource sd = RxSourceApi.getRxSource(cr.openInputStream(url));
            if (sd != null) {
                if(RxRouter.goTag(sd,sited)) {
                    ToastUtil.show("插件已成功安装");
                    return true;
                }else {
                    return false;
                }
            } else {
                ToastUtil.show("插件格式有问题");
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            KLog.file("RxSited",App.getAppContext().getExternalFilesDir(null),"RxSited_log.txt",ex.getMessage());
            ToastUtil.show("出错：" + ex.getMessage());
            return false;
        }
    }
}
