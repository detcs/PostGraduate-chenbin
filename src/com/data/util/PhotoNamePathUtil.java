package com.data.util;

public class PhotoNamePathUtil {


	public static String pathToPhotoName(String path) 
	{
		String name="";
		String[] info=path.split("/");
		int len=info.length;
		name=info[len-1];
		return name;

	}

}
