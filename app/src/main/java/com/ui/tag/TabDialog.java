package com.ui.tag;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.C;
import com.DbFactory;
import com.EventTags;
import com.base.DataPresenter;
import com.base.adapter.AdapterPresenter;
import com.base.adapter.BaseViewHolder;
import com.base.adapter.TRecyclerView;
import com.base.adapter.TypeSelector;
import com.base.event.OkBus;
import com.model.Tags;
import com.ui.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by haozhong on 2017/7/19.
 */

public class TabDialog extends DialogFragment {
    private static TabDialog mDialog;
    private List<Tags> tagsList;
    private Map<String,Integer> map = new HashMap<>();
    public static TabDialog getInstance(String url){
        if(mDialog == null) mDialog = new TabDialog();
        Bundle bundle = new Bundle();
        bundle.putString(C.URL,url);
        mDialog.setArguments(bundle);
        return mDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        TypeSelector<Tags> tagsTypeSelector = (tags) -> !TextUtils.isEmpty(tags.group) ? R.layout.tab_dialog_item1 : R.layout.tab_dialog_item2;

        TRecyclerView<Tags> tRecyclerView = new TRecyclerView<>(getContext());
        tRecyclerView.setType_SpanCount(R.layout.tab_dialog_item1,1)
                     .setType_SpanCount(R.layout.tab_dialog_item2,3)
                     .setTypeSelector(tagsTypeSelector)
                     .setRefreshable(false);

        tRecyclerView.getCoreAdapter()
                     .setOnItemClickListener(new BaseViewHolder.ItemClickListener() {
                         @Override
                         public void onItemClick(View view, int postion) {
                                 Tags item = tagsList.get(postion);
                                 if (TextUtils.isEmpty(item.group)) {
                                     OkBus.getInstance().onEvent(EventTags.CURRENT_ITEM, map.get(item.url));
                                     dialog.dismiss();
                             }
                         }
                     });

        DataPresenter.<Tags>getInstance()
                     .setDbRepository(DbFactory::getTags)
                     .setParam(C.URL,mDialog.getArguments().getString(C.URL))
                     .fetch()
                     .map(tagses -> {
                         tagsList = new ArrayList<>();
                         for(int i=0;i<tagses.size();i++){
                             Tags item = tagses.get(i);
                             if(TextUtils.isEmpty(item.group)){
                                 tagsList.add(item);
                                 map.put(item.url,i);
                             }else{
                                 Tags tags = new Tags();
                                 tags.group = item.group;
                                 tagsList.add(tags);
                                 item.group = null;
                                 tagsList.add(item);
                                 map.put(item.url,i);
                             }
                         }
                         return tagsList;
                     }).subscribe(tagsList ->
                         tRecyclerView.getCoreAdapter()
                                      .setBeans(tagsList, AdapterPresenter.NO_MORE));
        dialog.setContentView(tRecyclerView);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        //注释掉 占满屏幕就不起作用了
        //window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundCard)));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    public  TabDialog  start(FragmentManager fm){
        show(fm,"");
        return this;
    }
}
