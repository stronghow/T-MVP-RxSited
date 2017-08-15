package com.ui.book1;

import android.view.View;
import android.widget.ImageView;

import com.C;
import com.NetFactory;
import com.app.annotation.apt.Extra;
import com.app.annotation.apt.Router;
import com.app.annotation.apt.SceneTransition;
import com.base.BaseActivity;
import com.base.adapter.BaseViewHolder;
import com.base.entity.DataExtra;
import com.base.util.BindingUtils;
import com.base.util.ToastUtil;
import com.base.util.helper.RouterHelper;
import com.base.util.helper.RxSchedulers;
import com.dao.db.SiteDbApi;
import com.model.Sections;
import com.model.Tag;
import com.sited.RxSource;
import com.socks.library.KLog;
import com.ui.main.R;
import com.ui.main.databinding.ActivitySitedBookBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

/**
 * Created by haozhong on 2017/4/4.
 */

@Router(C.BOOK1)
public class Book1Activity extends BaseActivity<Book1Presenter,ActivitySitedBookBinding> implements Book1Contract.View{
    @Extra(C.MODEL)
    public Tag model;

    @Extra(C.SOURCE)
    public RxSource rxSource;

    @SceneTransition(C.TRANSLATE_VIEW)
    public ImageView image;


    private boolean isResume = false;
    
    private List<Sections> sectionsList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sited_book;
    }

    @Override
    public void initView() {
        BindingUtils.loadImg(mViewBinding.image, model.logo,model.url);
        BindingUtils.blur_loadImg(mViewBinding.blurimage,model.logo,model.url);
        setTitle(model.name);
        mPresenter.initAdapterPresenter(mViewBinding.listItem.getPresenter(),
                                    DataExtra.create()
                                    .add(C.MODEL,model)
                                    .add(C.SOURCE,rxSource)
                                    .build());
        sectionsList =  mViewBinding.listItem.getCoreAdapter().getItemList();
        mViewBinding.listItem.getCoreAdapter().setOnItemClickListener(new BaseViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                //ToastUtil.show(""+postion);
                //KLog.json("book::position = " + postion);
                List<Sections> sectionsList = (List<Sections>) mViewBinding.listItem.getCoreAdapter().getItemList();
                Sections item =  sectionsList.get(postion);
                RouterHelper.go(C.SECTION1,DataExtra.create()
                        .add(C.MODEL,item)
                        .add(C.SOURCE,rxSource)
                        .build());
            }
        });
        mViewBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                C.isReverse = !C.isReverse;
                C.oldIndex = sectionsList.size() - C.oldIndex -1;
                C.newIndex = sectionsList.size() - C.newIndex -1;
                NetFactory.reverse(sectionsList);
                C.sSectionses = sectionsList;
                mViewBinding.listItem.getCoreAdapter().notifyDataSetChanged(sectionsList);
            }
        });
    }

    @Override
    protected void onResume() {
        KLog.json("onResume");
        super.onResume();
        if(isResume){  //C.newIndex != C.oldIndex
            ((Sections)mViewBinding.listItem.getCoreAdapter().getItemList().get(C.newIndex)).isLook = true;
            ((Sections)mViewBinding.listItem.getCoreAdapter().getItemList().get(C.oldIndex)).isLook = false;
            mViewBinding.listItem.getCoreAdapter().notifyItemChanged(C.oldIndex);
            mViewBinding.listItem.getCoreAdapter().notifyItemChanged(C.newIndex);
            SiteDbApi.updateLastlook(sectionsList.get(C.oldIndex), sectionsList.get(C.newIndex));
            C.oldIndex  = C.newIndex; //更新之后，原来新的位置变成了旧的
            mViewBinding.listItem.moveToposition(C.newIndex);
        }else{
            //监听recycleview渲染完毕
            mViewBinding.listItem.getViewTreeObserver().addOnGlobalLayoutListener(()->{
                if(!isResume && mViewBinding.listItem.hasFocus())
                    Flowable.timer(500, TimeUnit.MILLISECONDS)
                    .compose(RxSchedulers.io_main())
                    .subscribe(t ->{
                        mViewBinding.listItem.moveToposition(C.oldIndex);
                        isResume = true;
                    });

            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SiteDbApi.insertOrUpdate(sectionsList);
        if(mViewBinding.listItem.getPresenter()!=null)
            mViewBinding.listItem.getPresenter().unsubscribe();
    }
}
