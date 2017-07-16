package com.model;

import com.base.entity.ItemTitle;
import com.sited.RxSource;

/**
 * Created by haozhong on 2017/7/13.
 */

public class Tab implements ItemTitle{
    public String title;
    public String url;
    public RxSource rxSource;

    public Tab(String title,String url,RxSource rxSource){
        this.title = title;
        this.url = url;
        this.rxSource = rxSource;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
    }
}
