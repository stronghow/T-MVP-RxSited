package com.ui.book1;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.C;
import com.EventTags;
import com.NetFactory;
import com.app.annotation.apt.Extra;
import com.app.annotation.apt.Router;
import com.app.annotation.apt.SceneTransition;
import com.app.annotation.javassist.Bus;
import com.app.annotation.javassist.BusRegister;
import com.base.BaseActivity;
import com.base.adapter.AdapterPresenter;
import com.base.adapter.BaseViewHolder;
import com.base.entity.DataExtra;
import com.base.event.Event;
import com.base.util.BindingUtils;
import com.base.util.ToastUtil;
import com.base.util.helper.RouterHelper;
import com.base.util.helper.RxSchedulers;
import com.dao.db.SiteDbApi;
import com.model.LookModel;
import com.model.Sections;
import com.model.Tag;
import com.sited.RxSource;
import com.socks.library.KLog;
import com.ui.main.R;
import com.ui.main.databinding.ActivitySitedBookBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.realm.Realm;

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

    private List<Sections> sectionsList;
    
    private int index, page;
    private int oldIndex;
    private int newIndex;

    @Bus(EventTags.SECTION_PAGE)
    public void setPage(int page){
        this.page = page;
        update();
    }

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
        mViewBinding.listItem.getPresenter()
                .setDataFromListener(new AdapterPresenter.DataFromListener() {
                    @Override
                    public void Call(Boolean isFromNet) {
                        if(isFromNet) oldIndex = 0;
                        else {
                            LookModel lookModel = SiteDbApi.getLastLook(model.url);
                            if(lookModel != null){
                                oldIndex = lookModel.index;
                            }
                        }
                    }
                });
        mViewBinding.listItem.getCoreAdapter().setOnItemClickListener(new BaseViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                if(sectionsList == null) {
                     sectionsList = mViewBinding.listItem.getCoreAdapter().getItemList();
                }
                Sections item =  sectionsList.get(postion);
                //index = item.index;
                index = postion;
                RouterHelper.go(C.SECTION1,DataExtra.create()
                        .add(C.MODEL,item)
                        .add(C.INDEX,index)
                        .add(C.SOURCE,rxSource)
                        .add(C.SECTIONS,sectionsList)
                        .build());
            }
        });
        mViewBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldIndex = sectionsList.size() - oldIndex -1;
                newIndex = sectionsList.size() - newIndex -1;
                reverse(sectionsList);
                mViewBinding.listItem.getCoreAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void initEven() {
        mViewBinding.listItem.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(mViewBinding.listItem.hasFocus()){
                    mViewBinding.listItem.moveToposition(oldIndex);
                    mViewBinding.listItem.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    Flowable.timer(500, TimeUnit.MILLISECONDS)
//                            .compose(RxSchedulers.io_main())
//                            .subscribe(t ->{
//                                mViewBinding.listItem.moveToposition(oldIndex);
//                                mViewBinding.listItem.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                            });
                }
            }
        });
    }

    private void update(){
//        Sections model = sectionsList.get(index + page);
//        newIndex = model.index;
        newIndex = index + page;
        Sections oldSec = sectionsList.get(oldIndex);
        Sections newSec = sectionsList.get(newIndex);
        oldSec.isLook = false;
        newSec.isLook = true;
        mViewBinding.listItem.getCoreAdapter().notifyItemChanged(oldIndex);
        mViewBinding.listItem.getCoreAdapter().notifyItemChanged(newIndex);
        SiteDbApi.updateLastLook(oldSec, newSec, newIndex);
        oldIndex = newIndex; //更新之后，原来新的位置变成了旧的

        mViewBinding.listItem.moveToposition(newIndex);
    }

    private void reverse(List<Sections> sectionsList){
        Collections.reverse(sectionsList);
//        for(int i=0; i< sectionsList.size();i++){
//            sectionsList.get(i).index = i;
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SiteDbApi.insertOrUpdate(sectionsList);
        if(mViewBinding.listItem.getPresenter()!=null)
            mViewBinding.listItem.getPresenter().unsubscribe();
    }
}
