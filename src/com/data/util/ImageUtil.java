package com.data.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.app.ydd.R;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.LruCache;

public class ImageUtil {

	public static String filePre="file://";
	public static  ImageLoader imageLoader ; 
	//public static  ImageLoader imageLoaderWithoutCache ; 
	public static  DisplayImageOptions options;
	public static void init(Context context)
	{
		ActivityManager  _ActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

		ActivityManager.MemoryInfo minfo = new ActivityManager.MemoryInfo();
        _ActivityManager.getMemoryInfo(minfo);
		int maxSize=(int) (minfo.availMem/8);
		imageLoader = ImageLoader.getInstance(); 
		options=new DisplayImageOptions.Builder()
		.cacheInMemory(false)
		.cacheOnDisk(true)
		//.showImageOnLoading(R.drawable.note_thumb)
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .defaultDisplayImageOptions(options)
        //.threadPriority(Thread.NORM_PRIORITY - 2) 
        //.memoryCache(new LruMemoryCache(maxSize))
        .memoryCache(new WeakMemoryCache())
        .denyCacheImageMultipleSizesInMemory() 
        .build();
		imageLoader.init(config);
		ImageUtil.imageLoader.clearMemoryCache();
	}
}
