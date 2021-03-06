package com.model;

import com.app.annotation.apt.QueryKey;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;


public class Sections extends RealmObject{
	public int index;
	public int orgIndex;

	@PrimaryKey
	public String url;
	public String name;
	//public boolean isGroup;//是否为分组

	public String bookName;

	public String bookUrl;

	//pic total
	public int total;

	public int downTotal;
	public int downProgress;

	@Index
	@QueryKey
	public String QueryKey;

	public boolean isLook;
}
