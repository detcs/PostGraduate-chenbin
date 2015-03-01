package com.data.util;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.content.Context;

public class GloableData {
	// attention type-value:affects the EssenseFragment
	public static final int TYPE_MATERIAL = 0, TYPE_INFORMATION = 1,
			TYPE_EXERCISE = 2, TYPE_POST = 3, TYPE_COMMENT = 4,
			TYPE_INFORM = 5, TYPE_NEW = 6, TYPE_SEARCH = 100;
	public static final int BOY = 1, GIRL = 0;
	public static final int NET_ERROR = 111;
	public static final int ISNEW = 1;
	public static final String URL = "http://114.215.196.179:8080/gs";
	// public static final String URL = "http://192.168.0.115:8081/gs";
	public static final String ESSENSE_PATH = "/mobile?methodno=MEssenceList";
	public static final String IMAGE_PATH = "/download.do";
	public static final String POST_LIST_PATH = "/mobile?methodno=MPosts&debug=1";
	public static final String POST_DETAIL_PATH = "/mobile?methodno=MPostDetail&debug=1";
	public static final String ESSENSEDETAIL_PATH = "/mobile?methodno=MEssenceDetail";
	public static final String COMMENT_PATH = "/mobile?methodno=MComments&debug=1";
	public static final String UPLOAD_COMMENT_PATH = "/mobile?methodno=MCommentPublish&debug=1";
	public static final String PUBLISH_POST_PATH = "/mobile?methodno=MPostPublish&debug=1";
	public static final String INFORM_PATH = "/mobile?methodno=MCommentsToMe&debug=1";
	public static final String DOWNLOAD_PATH = "/mobile?methodno=MBaiheNewsList";
	public static final String RECOMMENDKEYS_PATH = "/mobile?methodno=MRecommendKeys";
	public static final String CHANGEPW_PATH = "/mobile?methodno=MPasswdChange";
	public static final String RESERVE_LIST_PATH = "/mobile?methodno=MMyEssences";
	public static final String CONTACT_US_PATH = "/mobile?methodno=MFeedback";
	public static final String RESERVE_PATH = "/mobile?methodno=MEssenceCollect";
	public static final String INFOCHANGE_PATH = "/mobile?methodno=MUpdateUserInfo";
	public static final String MSGCOUNT_PATH = "/mobile?methodno=MGetMsgCount";
	public static final String REPORT_PATH = "/mobile?methodno=MPostReport";
	public static final String UPLOAD_HEAD_PATH = "/mobile?methodno=MImgUpload";
	public static final String PRINT_PATH = "/mobile?methodno=MQuesPrint";
	public static int RESERVE_QUIT = 0, RESERVE_ENSURE = 1;

	public static final String UPLOAD_SUCCESS_MSG = "发布成功";
	public static RequestQueue requestQueue;
	private static String param;
	private static String userid = "6dfae24f-a77a-11e4-9812-ac853dac2305";
	// private static String userid;
	private static String verify = "1";

	// private static String verify;
	// private static String email;

	// public static String getEmail() {
	// if (null == email)
	// email = "";
	// return email;
	// }

	// public static boolean hasSetEmail() {
	// return false;
	// }

	// public static boolean setEmail(String email_in) {
	// if (email_in != null) {
	// if (!email_in.trim().equals("")) {
	// if (email_in.contains("@")) {
	// email = email_in;
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	public static void init(Context context) {
		// userid = UserConfigs.getId();
		// verify = UserConfigs.getVerify();

		// TelephonyManager tm = (TelephonyManager) context
		// .getSystemService(Context.TELEPHONY_SERVICE);
		// param = "&deviceid=" + tm.getDeviceId() + "&userid=" + userid
		// + "&verify=" + verify;
		param = "&deviceid=" + 1 + "&userid=" + userid + "&verify=" + verify;
	}

	public static void initRequestQueue(Context context) {
		requestQueue = Volley.newRequestQueue(context);
	}

	public static String getParam() {
		return param;
	}

}
