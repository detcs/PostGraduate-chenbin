package com.data.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ComputeURL {
	private static final String TAG = "ComputeURL";

	// 广场举报帖子
	public static String getReportURL(String pid) {
		String re = GloableData.URL + GloableData.REPORT_PATH
				+ GloableData.getParam() + "&pid=" + str(pid);
		return re;
	}

	public static String getMsgCountURL() {
		String re = GloableData.URL + GloableData.MSGCOUNT_PATH
				+ GloableData.getParam();
		return re;
	}

	public static String getInfoChangeURL(String nickname, String headimg,
			String email) {
		String re = GloableData.URL + GloableData.INFOCHANGE_PATH
				+ GloableData.getParam() + "&nickname=" + str(nickname)
				+ "&headimg=" + str(headimg) + "&email=" + str(email);
		return re;
	}

	public static String getReserveURL(String id, int type) {
		String re = GloableData.URL + GloableData.RESERVE_PATH
				+ GloableData.getParam() + "&id=" + str(id) + "&type=" + type;
		return re;
	}

	public static String getContactUsURL(String content, String contact) {
		String re = GloableData.URL + GloableData.CONTACT_US_PATH
				+ GloableData.getParam() + "&content=" + str(content)
				+ "&contact=" + str(contact);
		return re;
	}

	public static String getReserveListURL() {
		String re = GloableData.URL + GloableData.RESERVE_LIST_PATH
				+ GloableData.getParam();
		return re;
	}

	public static String getPwChangeURL(String oldpw, String pw) {
		String re = GloableData.URL + GloableData.CHANGEPW_PATH
				+ GloableData.getParam() + "&password=" + stringToMD5(str(pw))
				+ "&passwordOld=" + stringToMD5(str(oldpw));
		return re;
	}

	public static String getRecommendKeyURL(String key) {
		String re = GloableData.URL + GloableData.RECOMMENDKEYS_PATH
				+ GloableData.getParam() + "&key=" + str(key);
		return re;
	}

	// ************IMAGE************
	private static final int HEAD_WIDTH = 100;
	private static final int HEAD_HEIGHT = 100;
	private static final int SMALL_HEAD_HEIGHT = 50;
	private static final int SMALL_HEAD_WIDTH = 50;
	private static final String ImgUrlHead = GloableData.URL
			+ GloableData.IMAGE_PATH + "?id=";

	public static String getHeadImgURL(String id) {
		return ImgUrlHead + str(id) + "&w=" + HEAD_WIDTH + "&h=" + HEAD_HEIGHT;
	}

	public static String getSmallHeadImgURL(String id) {
		return ImgUrlHead + str(id) + "&w=" + SMALL_HEAD_WIDTH + "&h="
				+ SMALL_HEAD_HEIGHT;
	}

	// ***************************************************************
	public static String getPostDetailURL(String postId) {
		String re = GloableData.URL + GloableData.POST_DETAIL_PATH
				+ GloableData.getParam() + "&postid=" + str(postId);
		return re;
	}

	public static String getPublishPostURL(String title, String content) {
		String re = GloableData.URL + GloableData.PUBLISH_POST_PATH
				+ GloableData.getParam() + "&title=" + str(title) + "&content="
				+ str(content);
		return re;
	}

	public static String getCommentURL(String postid, String content,
			String replyid) {
		String re = GloableData.URL + GloableData.UPLOAD_COMMENT_PATH
				+ GloableData.getParam() + "&postid=" + str(postid)
				+ "&content=" + str(content) + "&replyid=" + str(replyid);
		return re;
	}

	public static String getDownloadURL(String id, String email,
			String resourceId, String isShared) {
		String reStr = GloableData.URL + GloableData.DOWNLOAD_PATH
				+ GloableData.getParam() + "&id=" + str(id) + "&email="
				+ str(email) + "&resid=" + str(resourceId) + "&isShared="
				+ str(isShared);
		return reStr;
	}

	public static String getEssenseDetailURL(String id) {
		String reStr = GloableData.URL + GloableData.ESSENSEDETAIL_PATH
				+ GloableData.getParam() + "&id=" + str(id);
		return reStr;
	}

	public static String getEssenseListURL(int page, int limit, int type) {
		if (GloableData.TYPE_NEW == type) {
			type = 3;
		}
		String reStr = GloableData.URL + GloableData.ESSENSE_PATH
				+ GloableData.getParam() + "&page=" + page + "&limit=" + limit
				+ "&type=" + type;
		return reStr;
	}

	public static String getPostListURL(int page, int limit) {
		String reStr = GloableData.URL + GloableData.POST_LIST_PATH
				+ GloableData.getParam() + "&page=" + page + "&limit=" + limit
				+ "&type=" + GloableData.TYPE_POST;
		return reStr;
	}

	public static String getCommentListURL(int page, int limit, String id) {
		String reStr = GloableData.URL + GloableData.COMMENT_PATH
				+ GloableData.getParam() + "&page=" + page + "&limit=" + limit
				+ "&postid=" + str(id);
		return reStr;
	}

	public static String getInformListURL(int page, int limit) {
		String reStr = GloableData.URL + GloableData.INFORM_PATH
				+ GloableData.getParam() + "&page=" + page + "&limit=" + limit;
		return reStr;
	}

	// ***************str utils***************

	private static String str(String in) {
		if (null == in.trim()) {
			return "";
		} else {
			return in.replace("\n", "");
		}
	}

	private static String stringToMD5(String string) {
		byte[] hash;

		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}

		return hex.toString();
	}
}
