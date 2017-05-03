package com.model;

/**
 * Created by yuety on 15/10/23.
 */
public class ViewSetModel {
    public int view_model;      //翻页模式
    public int view_direction;  //阅读方向
    public int view_scale;      //图片缩放
    public int view_orientation;//屏幕缩放

    public ViewSetModel() {
        view_model       = -1;
        view_direction   = -1;
        view_scale       = -1;
        view_orientation = -1;
    }
}
