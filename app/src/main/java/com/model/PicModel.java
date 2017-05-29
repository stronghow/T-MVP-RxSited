package com.model;


import com.app.annotation.apt.QueryKey;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//ͼƬ
public class PicModel  extends RealmObject{

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
}

