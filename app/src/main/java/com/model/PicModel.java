package com.model;


import com.app.annotation.apt.QueryKey;
import com.base.BaseBean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//ͼƬ
public class PicModel  extends RealmObject implements BaseBean {


	public String objectId;

	@PrimaryKey
	public  String url;

	public  int secIndex;

	public  Sections sections;

	@QueryKey
	public String QueryKey;

	//public DdSource source;

	public  int cacheID;

	public int orgWidth;
	public int orgHeight;

	public int tmpWidth;
	public int tmpHeight;

	@Override
	public String getObjectId() {
		return this.objectId;
	}

}

