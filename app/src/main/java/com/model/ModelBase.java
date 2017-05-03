package com.model;

import me.noear.exts.*;

public class ModelBase {
	
	public Act1<String> propertyChanged;

	public void notifyPropertyChanged(String propertyName)
	{
		if(propertyChanged!=null)
			propertyChanged.run(propertyName);
	}
}
