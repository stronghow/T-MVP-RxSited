package com.model;

import com.app.annotation.apt.QueryKey;
import com.base.BaseBean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Sections extends RealmObject implements BaseBean {


	public String objectId;

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

	public int _code;

	//public DdNode _config;

	public int downTotal;
	public int downProgress;

	@QueryKey
	public String QueryKey;

	public boolean isLook;

	public int code(){
		if(_code==0 && url != null) {
			_code = url.hashCode();
		}

		return _code;
	}

	@Override
	public String getObjectId() {
		return objectId;
	}
}
