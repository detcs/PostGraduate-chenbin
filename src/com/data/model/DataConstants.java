package com.data.model;

import java.util.HashMap;

public class DataConstants {

	public final static String TAG="flip";
	public final static String USER_OPTIONS="userOptions";
	public final static HashMap<String, String> TABLE_DIR_MAP=new HashMap<String ,String>();
	//public final static String DEVICE_ID=Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);;
	//public  static String SD_PATH=null;
	
	public static DatabaseHelper dbHelper ;
	public static int screenWidth;
	public static int screenHeight;
	public static int RESULTCODE_COURSE_SETTING=1;
	//sina weibo
	public final static String WEIBO_APP_KEY="77238273";
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";//"http://s1.smartjiangsu.com/index.html";// 应用的回调页 
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";// 应用申请的高级权限\
	//server
	public static final String SERVER_URL="http://114.215.196.179:8080/gs/mobile";
	public static final String DOWNLOAD_URL="http://114.215.196.179:8080/gs/download.do?id=";
	
}
