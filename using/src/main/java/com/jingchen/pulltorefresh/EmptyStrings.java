package com.jingchen.pulltorefresh;

/**
 * Created by yuety on 15/9/1.
 */
public class EmptyStrings implements IStrings{

    public final static EmptyStrings instance = new EmptyStrings();

    @Override
    public String pull_to_refresh() {
        return "";
    }

    @Override
    public String release_to_refresh() {
        return "";
    }

    @Override
    public String refreshing() {
        return "";
    }

    @Override
    public String refresh_succeed() {
        return "";
    }

    @Override
    public String refresh_fail() {
        return "";
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
