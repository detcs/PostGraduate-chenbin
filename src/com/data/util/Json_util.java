package com.data.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
//import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.data.model.Comment;
import com.data.model.Essense;
import com.data.model.EssenseDetail;
import com.data.model.Inform;
import com.data.model.Post;
import com.data.model.Reserve;

public class Json_util {

	public static void checkErrorCode(JSONObject json) throws Exception {
		int errorcode = -1;
		try {
			errorcode = json.getInt("errorCode");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (errorcode != 0) {
			throw new Exception("error code is not 0");
		}
	}

	// upload comment
	public static String getUploadReturnMsg(JSONObject json) {
		String reStr;
		try {
			JSONObject data = (JSONObject) json.get("data");
			reStr = data.getString("msg_");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reStr = "error";
		}
		return reStr;
	}

	public static EssenseDetail getEssenseDetail(JSONObject json) {
		EssenseDetail ed = null;
		String author = "";
		String title = "";
		String time = "";
		String id = "";
		int needShare_;// 是否需要分享才可以下载
		int hasDownload_;// 有无附件下载
		int isDownloaded_;// 是否下载过
		int resType_;
		String url_ = "";
		String resid_ = "";
		String resSize_ = "";
		String browseTimes_ = "";
		String downloadTimes_ = "";
		int isCollected_;
		String content_ = "";
		JSONObject item;
		try {
			item = (JSONObject) json.get("data");
			author = item.getString("source_");
			title = item.getString("title_");
			time = item.getString("time_");
			id = item.getString("id_");
			url_ = item.getString("url_");
			needShare_ = item.getInt("needShare_");
			hasDownload_ = item.getInt("hasDownload_");
			isDownloaded_ = item.getInt("isDownloaded_");
			resType_ = item.getInt("resType_");
			resid_ = item.getString("resid_");
			resSize_ = item.getString("resSize_");
			browseTimes_ = item.getString("browseTimes_");
			downloadTimes_ = item.getString("downloadTimes_");
			isCollected_ = item.getInt("isCollected_");
			content_ = item.getString("content_");
			ed = new EssenseDetail(title, author, time, id, needShare_,
					hasDownload_, isDownloaded_, resType_, url_, resid_,
					resSize_, browseTimes_, downloadTimes_, isCollected_,
					content_);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ed;
	}

	public static String getEssenseContentURL(JSONObject json) {
		String url_ = "http://www.baidu.com";
		try {
			JSONObject data = (JSONObject) json.get("data");
			url_ = data.getString("url_");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url_;
	}

	public static Post transToOnePost(JSONObject json) {
		Post vg = null;
		try {
			JSONObject data = (JSONObject) json.get("data");

			String title = data.getString("title_");
			String time = data.getString("time_");
			String id = data.getString("id_");
			String author = data.getString("nickname_");
			String headimg = data.getString("headimg_");
			String content = data.getString("content_");
			int comments = data.getInt("commentCount_");
			int sex = data.getInt("sex_");

			vg = new Post(title, author, time, id, headimg, content, comments,
					sex);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vg;
	}

	// reserve parse
	public static List<Reserve> transToReserve(JSONObject json) {
		List<Reserve> reList = new ArrayList<Reserve>();
		try {
			JSONObject data = (JSONObject) json.get("data");
			JSONArray array = (JSONArray) data.get("essence_");
			String id_;
			String title_;// 标题
			String source_;// 来源
			String content_;
			int hasDownload_;// 有无附件下载
			String resid_;// 附件资源的id
			int isDownloaded_;// 是否下载过
			String time_;
			int type_; // 类别
			int resType_;
			JSONObject item;
			Reserve vg = null;
			for (int i = 0; i < array.length(); i++) {
				item = array.getJSONObject(i);
				id_ = item.getString("id_");
				title_ = item.getString("title_");
				source_ = item.getString("source_");
				content_ = item.getString("content_");
				hasDownload_ = item.getInt("hasDownload_");
				resid_ = item.getString("resid_");
				isDownloaded_ = item.getInt("isDownloaded_");
				time_ = item.getString("time_");
				type_ = item.getInt("type_");
				resType_ = item.getInt("resType_");
				vg = new Reserve(id_, title_, source_, content_, hasDownload_,
						resid_, isDownloaded_, time_, type_, resType_);
				reList.add(vg);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reList;
	}

	public static List<Inform> transToInform(JSONObject json) {
		List<Inform> reList = new ArrayList<Inform>();
		try {
			JSONObject data = (JSONObject) json.get("data");
			JSONArray array = (JSONArray) data.get("comments_");
			JSONObject item;
			Inform vg = null;
			String title;
			String postId;
			String comment;
			int tag;
			for (int i = 0; i < array.length(); i++) {
				item = array.getJSONObject(i);
				title = item.getString("title_");
				postId = item.getString("pid_");
				comment = item.getString("content_");
				tag = item.getInt("isNew_");
				vg = new Inform(title, postId, comment, tag);
				reList.add(vg);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reList;
	}

	public static List<Comment> transToComment(JSONObject json) {
		List<Comment> reList = new ArrayList<Comment>();
		try {
			JSONObject data = (JSONObject) json.get("data");
			JSONArray array = (JSONArray) data.get("comments_");
			JSONObject item;
			Comment vg = null;
			String id;
			String nickName;
			String content;
			String headimg;
			String time;
			String userId;
			int sex;
			for (int i = 0; i < array.length(); i++) {
				item = array.getJSONObject(i);
				id = item.getString("id_");
				nickName = item.getString("nickname_");
				content = item.getString("content_");
				headimg = item.getString("headimg_");
				time = item.getString("time_");
				userId = item.getString("userid_");
				sex = item.getInt("sex_");
				// Comment(String id, String nickName, String headimg, String
				// time,String content)
				vg = new Comment(id, nickName, headimg, time, content, userId,
						sex);
				reList.add(vg);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reList;
	}

	public static List<Post> transToPost(JSONObject json) {
		List<Post> reList = new ArrayList<Post>();
		try {
			JSONObject data = (JSONObject) json.get("data");
			JSONArray array = (JSONArray) data.get("list_");
			JSONObject item;
			Post vg = null;
			String author = "";
			String title = "";
			String time = "";
			String id = "";
			String headimg = "";
			String content = "";
			int comments = 0;
			int sex = 0;
			for (int i = 0; i < array.length(); i++) {
				item = array.getJSONObject(i);
				title = item.getString("title_");
				time = item.getString("time_");
				id = item.getString("id_");
				author = item.getString("nickname_");
				headimg = item.getString("headimg_");
				content = item.getString("content_");
				comments = item.getInt("commentCount_");
				sex = item.getInt("sex_");
				vg = new Post(title, author, time, id, headimg, content,
						comments, sex);
				reList.add(vg);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reList;
	}

	public static List<Essense> transToEssense(JSONObject json) {
		List<Essense> reList = new ArrayList<Essense>();
		try {
			JSONObject data = (JSONObject) json.get("data");
			JSONArray array = (JSONArray) data.get("essence_");
			JSONObject item;
			Essense vg = null;
			String author = "";
			String title = "";
			String time = "";
			String id = "";
			int needShare_;// 是否需要分享才可以下载
			int hasDownload_;// 有无附件下载
			int isDownloaded_;// 是否下载过
			int resType_;
			String url_ = "";
			String resid_ = "";
			int isCollected_;
			for (int i = 0; i < array.length(); i++) {
				item = array.getJSONObject(i);
				author = item.getString("source_");
				title = item.getString("title_");
				time = item.getString("time_");
				id = item.getString("id_");
				url_ = item.getString("url_");
				needShare_ = item.getInt("needShare_");
				hasDownload_ = item.getInt("hasDownload_");
				isDownloaded_ = item.getInt("isDownloaded_");
				resType_ = item.getInt("resType_");
				resid_ = item.getString("resid_");
				isCollected_ = item.getInt("isCollected_");
				vg = new Essense(title, author, time, id, needShare_,
						hasDownload_, isDownloaded_, resType_, url_, resid_,
						isCollected_);
				reList.add(vg);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reList;
	}
}
