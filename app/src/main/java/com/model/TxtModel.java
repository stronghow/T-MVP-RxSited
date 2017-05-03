package com.model;


/**
 * Created by yuety on 16/7/26.
 */
public class TxtModel extends ModelBase {
    public final String data;
    public final int type;

    public final String referer;

    public TxtModel(String referer,  String data, int type){
        this.referer  = referer;

        this.data     = data;
        this.type     = type;
    }

}
