package com.ui.Section1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.C;
import com.EventTags;
import com.app.annotation.apt.Extra;
import com.app.annotation.apt.Router;
import com.base.BaseActivity;
import com.base.entity.DataExtra;
import com.base.event.OkBus;
import com.model.Sections;
import com.sited.RxSource;
import com.socks.library.KLog;
import com.ui.main.R;
import com.ui.main.databinding.ActivitySitedSectionBinding;

import java.util.List;

/**
 * Created by haozhong on 2017/4/4.
 */


@Router(C.SECTION1)
public class Section1Activity extends BaseActivity<Section1Presenter,ActivitySitedSectionBinding> implements Section1Contract.View {
    @Extra(C.MODEL)
    public Sections model;

    @Extra(C.SOURCE)
    public RxSource rxSource;

    @Extra(C.SECTIONS)
    public List<Sections> sectionsList;

    private BatteryReceiver batteryReceiver;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sited_section;
    }

    @Override
    public void before_content() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏
    }

    @Override
    public void initView() {
        mViewBinding.listItem.getTextView().setVisibility(View.VISIBLE);
        mViewBinding.listItem.setHashMap(C.BOOKNAME,model.name);
        mPresenter.initAdapterPresenter(mViewBinding.listItem.getPresenter(), DataExtra.create()
                .add(C.MODEL,model)
                .add(C.SOURCE,rxSource)
                .add(C.SECTIONS,sectionsList)
                .build());
    }

    @Override
    public void initEven() {
        //注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //创建广播接受者对象
        batteryReceiver = new BatteryReceiver();

        //注册receiver
        registerReceiver(batteryReceiver, intentFilter);
    }


    protected void onDestroy() {
        super.onDestroy();
        //更新Sections
        OkBus.getInstance().onEvent(EventTags.SECTION_PAGE,mViewBinding.listItem.getPresenter().getBegin()-1);
            unregisterReceiver(batteryReceiver);
        if(mViewBinding.listItem.getPresenter()!=null)
            mViewBinding.listItem.getPresenter().unsubscribe();
    }

    /**
     * 广播接受者
     */
    class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            //判断它是否是为电量变化的Broadcast Action
            if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //电量的总刻度
                int scale = intent.getIntExtra("scale", 100);
                //把它转成百分比
                mViewBinding.listItem.setHashMap(C.BATTERY,((level*100)/scale)+"%");
            }
        }

    }
}
