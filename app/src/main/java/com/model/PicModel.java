package com.model;


import com.app.annotation.apt.QueryKey;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

//ͼƬ
public class PicModel  extends RealmObject{

	@PrimaryKey
	public  String url;

	@Index
	@QueryKey
	public String QueryKey;
}

