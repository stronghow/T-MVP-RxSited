package me.noear.exts;

import java.text.SimpleDateFormat;
import java.util.Date;

public  class TimeExt {
	// for list
	//

	
	// date time
	//
	public static String formatDate(Date date, String format)
	{
		SimpleDateFormat fm = new SimpleDateFormat(format);
		
		return fm.format(date);
	}

}
