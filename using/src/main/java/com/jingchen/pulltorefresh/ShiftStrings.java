package com.jingchen.pulltorefresh;

/**
 * Created by yuety on 15/9/1.
 */
public class ShiftStrings implements IStrings{

    public final static ShiftStrings instance = new ShiftStrings();

    public String target = "";

    @Override
    public String pull_to_refresh() {
        return "下拉切换"+target;
    }

    @Override
    public String release_to_refresh() {
        return "释放立即切换";
    }

    @Override
    public String refreshing() {
        return "正在切换...";
    }

    @Override
    public String refresh_succeed() {
        return "切换成功";
    }

    @Override
    public String refresh_fail() {
        return "切换失败";
    }

    @Override
    public String pullup_to_load() {
        return "";
    }

    @Override
    public String release_to_load() {
        return "";
    }

    @Override
    public String loading() {
        return "";
    }

    @Override
    public String load_succeed() {
        return "";
    }

    @Override
    public String load_fail() {
        return "";
    }
}
