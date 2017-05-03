package com.jingchen.pulltorefresh;

/**
 * Created by yuety on 15/9/1.
 */
public class VerticalPageloadStrings implements IStrings{
    public final static VerticalPageloadStrings instance = new VerticalPageloadStrings();

    @Override
    public String pull_to_refresh() {
        return "下拉加载上一话";
    }

    @Override
    public String release_to_refresh() {
        return "释放立即加载";
    }

    @Override
    public String refreshing() {
        return "正在加载...";
    }

    @Override
    public String refresh_succeed() {
        return "加载成功";
    }

    @Override
    public String refresh_fail() {
        return "加载失败";
    }

    @Override
    public String pullup_to_load() {
        return "上拉加载下一话";
    }

    @Override
    public String release_to_load() {
        return "释放立即加载";
    }

    @Override
    public String loading() {
        return "正在加载...";
    }

    @Override
    public String load_succeed() {
        return "加载成功";
    }

    @Override
    public String load_fail() {
        return "加载失败";
    }
}
