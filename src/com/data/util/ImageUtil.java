package com.data.util;

import android.content.Context;

import com.app.ydd.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageUtil {

	public static String filePre="file://";
	public static  ImageLoader imageLoader ; 
	
	public static void init(Context context)
	{
		imageLoader = ImageLoader.getInstance(); 
		DisplayImageOptions options=new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.showImageOnLoading(R.drawable.note_thumb)
		.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .defaultDisplayImageOptions(options)
        .build();
		imageLoader.init(config);
	}
}
