package com.jingchen.pulltorefresh;

/**
 * Created by yuety on 15/9/1.
 */
public interface IPullToRefreshView {
    void refreshFinish(int refreshResult);
    void loadmoreFinish(int refreshResult);
}
