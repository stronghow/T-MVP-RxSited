package com.ui.book1;

import android.text.TextUtils;
import android.view.View;
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


    private boolean isResume = false;
    
    private List<Sections> sectionsList;

    private List<Sections> reverse_sectionsList;
    
    private int index, page;
    private int oldIndex;
    private int newIndex;

    @Bus(EventTags.SECTION_PAGE)
    public void setPage(int page){
        this.page = page;
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
                            LookModel lookModel = Realm.getDefaultInstance()
                                    .where(LookModel.class)
                                    .equalTo(C.QueryKey,model.url)
                                    .findFirst();
                            if(lookModel != null){
                                oldIndex = lookModel.index;
                            }
                        }
                    }
                });
        mViewBinding.listItem.getCoreAdapter().setOnItemClickListener(new BaseViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                //ToastUtil.show(""+postion);
                //KLog.json("book::position = " + postion);
                List<Sections> sectionsList = (ArrayList<Sections>) mViewBinding.listItem.getCoreAdapter().getItemList();
                Sections item =  sectionsList.get(postion);
                index = item.index;
                RouterHelper.go(C.SECTION1,DataExtra.create()
                        .add(C.MODEL,item)
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
                NetFactory.reverse(sectionsList);
                mViewBinding.listItem.getCoreAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        KLog.json("onResume");
        super.onResume();
        if(isResume){  //newIndex != oldIndex
            Sections model = sectionsList.get(index + page);
            newIndex = model.index;
            sectionsList.get(newIndex).isLook = true;
            sectionsList.get(oldIndex).isLook = false;
            mViewBinding.listItem.getCoreAdapter().notifyItemChanged(oldIndex);
            mViewBinding.listItem.getCoreAdapter().notifyItemChanged(newIndex);
            SiteDbApi.updateLastlook(sectionsList.get(oldIndex), sectionsList.get(newIndex));
            oldIndex  = newIndex; //更新之后，原来新的位置变成了旧的
            mViewBinding.listItem.moveToposition(newIndex);
        }else{
            //监听recycleview渲染完毕
            mViewBinding.listItem.getViewTreeObserver().addOnGlobalLayoutListener(()->{
                if(!isResume && mViewBinding.listItem.hasFocus())
                    Flowable.timer(500, TimeUnit.MILLISECONDS)
                    .compose(RxSchedulers.io_main())
                    .subscribe(t ->{
                        mViewBinding.listItem.moveToposition(oldIndex);
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
