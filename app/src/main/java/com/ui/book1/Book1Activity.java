package com.ui.book1;

import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.C;
import com.app.annotation.apt.Extra;
import com.app.annotation.apt.Router;
import com.app.annotation.apt.SceneTransition;
import com.base.BaseActivity;
import com.base.util.BindingUtils;
import com.model.Sections;
import com.model.Tag;
import com.socks.library.KLog;
import com.ui.main.R;
import com.ui.main.databinding.ActivitySitedBookBinding;

import java.util.HashMap;

/**
 * Created by haozhong on 2017/4/4.
 */

@Router(C.BOOK1)
public class Book1Activity extends BaseActivity<Book1Presenter,ActivitySitedBookBinding> implements Book1Contract.View{
    @Extra(C.MODEL)
    public Tag model;

    @SceneTransition(C.TRANSLATE_VIEW)
    public ImageView image;


    boolean isResume = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sited_book;
    }

    @Override
    public void initView() {
        KLog.json("Book1::initView");
        BindingUtils.loadImg(mViewBinding.image, model.logo,model.url);
        BindingUtils.blur_loadImg(mViewBinding.blurimage,model.logo,model.url);
        setTitle(model.name);
        HashMap map = new HashMap();
        map.put(C.MODEL,model);
        mPresenter.initAdapterPresenter(mViewBinding.listItem.getPresenter(),map);
    }

    @Override
    protected void onResume() {
        KLog.json("onResume");
        super.onResume();
        if(isResume && C.oldIndex != C.newIndex){
            ((Sections)mViewBinding.listItem.getCoreAdapter().getItemList().get(C.newIndex)).isLook = true;
            ((Sections)mViewBinding.listItem.getCoreAdapter().getItemList().get(C.oldIndex)).isLook = false;
            mViewBinding.listItem.getCoreAdapter().notifyItemChanged(C.oldIndex);
            mViewBinding.listItem.getCoreAdapter().notifyItemChanged(C.newIndex);
            C.oldIndex  = C.newIndex;
            mViewBinding.listItem.moveToposition(C.newIndex);
        }else{
            ViewTreeObserver observer = mViewBinding.listItem.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(()->{
                if(!isResume && mViewBinding.listItem.hasFocus())
                    new Handler().postDelayed(()->{
                        mViewBinding.listItem.moveToposition(C.oldIndex);
                        isResume = true;
                    },300);
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mViewBinding.listItem.getPresenter()!=null)
            mViewBinding.listItem.getPresenter().unsubscribe();

    }
}
