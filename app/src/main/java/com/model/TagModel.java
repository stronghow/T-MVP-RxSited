package com.model;

//Ŀ¼
public class TagModel extends ModelBase {

	public TagModel(String name, String url, int type) {
		this.name = name;
		this.url = url;
		this.type = type;
	}
	
	public String name;
	public String url;
	public int type;// 0分类；1填空; 10分组；11分组填空

}
