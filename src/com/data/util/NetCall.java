package com.data.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data.model.EssenseDetail;
import com.data.model.Post;

public class NetCall {
	private static final String TAG = "NetCall";

	private static RequestQueue requestQueue = GloableData.requestQueue;

	// 广场举报贴子
	public static void report(String pid, final ReportCallback callback) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, ComputeURL.getReportURL(pid), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int errorCode = response.getInt("errorCode");
							if (0 == errorCode) {
								JSONObject data = (JSONObject) response
										.get("data");
								int code_ = data.getInt("code_");
								if (1 == code_) {
									callback.reportSuccess();
									return;
								}
							}
							callback.reportFail();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						callback.reportFail();
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	public interface ReportCallback {
		public void reportSuccess();

		public void reportFail();
	}

	// 获取新消息数目
	public static void getMsgCount(final MsgCountCallback callback) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, ComputeURL.getMsgCountURL(), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int errorCode = response.getInt("errorCode");
							if (0 == errorCode) {
								JSONObject data = (JSONObject) response
										.get("data");
								int essence_ = data.getInt("essence_");
								int square_ = data.getInt("square_");
								callback.getSuccess(essence_, square_);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	public interface MsgCountCallback {
		public void getSuccess(int essence_, int square_);
	}

	// 修改个人信息： 昵称，头像，邮箱
	public static void changeInfo(String nickname, String headimg,
			String email, final InfoChangeCallback callback) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, ComputeURL.getInfoChangeURL(nickname,
						headimg, email), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int errorCode = response.getInt("errorCode");
							if (0 == errorCode) {
								JSONObject data = (JSONObject) response
										.get("data");
								int code_ = data.getInt("code_");
								if (1 == code_) {
									callback.changeSuccess();
									return;
								}
							}
							callback.changeFail();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						callback.changeFail();
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	public interface InfoChangeCallback {
		public void changeSuccess();

		public void changeFail();
	}

	// 精华收藏
	public static void reserve(String id, int type,
			final ReserveCallback callback) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, ComputeURL.getReserveURL(id, type), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int errorCode = response.getInt("errorCode");
							if (0 == errorCode) {
								callback.requestSuccess();
							} else {
								callback.requestFail();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						callback.requestFail();
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	public interface ReserveCallback {
		public void requestSuccess();

		public void requestFail();
	}

	// 联系我们
	public static void contactUs(String content, String contact,
			final ContactCallback callback) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET,
				ComputeURL.getContactUsURL(content, contact), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int errorCode = response.getInt("errorCode");
							if (0 == errorCode) {
								callback.contactSuccess();
							} else {
								callback.contactFail();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						callback.contactFail();
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	public interface ContactCallback {
		public void contactSuccess();

		public void contactFail();
	}

	// 修改密码
	public static void changePw(String oldpw, String pw,
			final PwChangeCallback callback) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, ComputeURL.getPwChangeURL(oldpw, pw), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int errorCode = response.getInt("errorCode");
							if (0 == errorCode) {
								JSONObject data = (JSONObject) response
										.get("data");
								int resultcode = data.getInt("code_");
								if (1 == resultcode) {
									callback.changeSuccess();
								} else {
									callback.changeFail(PwChangeCallback.OLDPWERROR);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						callback.changeFail(PwChangeCallback.NETERROR);
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	public interface PwChangeCallback {
		public static final int NETERROR = 0, OLDPWERROR = 1;

		public void changeSuccess();

		public void changeFail(int error);
	}

	// 获取广场搜索提示
	public static void askRecommendKeys(String key,
			final RecommendKeysCallback callback) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, ComputeURL.getRecommendKeyURL(key), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int errorCode = response.getInt("errorCode");
							if (0 == errorCode) {
								JSONObject data = (JSONObject) response
										.get("data");
								JSONArray array = (JSONArray) data.get("key_");
								List<String> keys = new ArrayList<String>();
								for (int i = 0; i < array.length(); i++) {
									keys.add(array.get(i).toString());
								}
								callback.success(keys);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	public interface RecommendKeysCallback {
		public void success(List<String> keys);
	}

	// 精华下载
	public static void download(String id, String email, String resourceId,
			String isShared, final DownloadSource ds) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, ComputeURL.getDownloadURL(id, email,
						resourceId, isShared), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int errorCode = response.getInt("errorCode");
							if (0 == errorCode) {
								JSONObject data = (JSONObject) response
										.get("data");
								int code_ = data.getInt("code_");
								if (1 == code_) {
									// success
									ds.downloadSuccess();
									return;
								}
							}
							ds.downloadFail();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						ds.downloadFail();
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	// 获取精华详细信息
	public static void getEssenseDetail(String postId,
			final PullEssenseDetail pd) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, ComputeURL.getEssenseDetailURL(postId),
				null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int errorCode = response.getInt("errorCode");
							if (errorCode != 0) {
								return;
							} else {
								// success
								pd.pullEDSuccess(Json_util
										.getEssenseDetail(response));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Log.i("error", "wsy");
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	// 获取广场帖子详细信息
	public static void getPost(final String postId, final PullViewGenerator up) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, ComputeURL.getPostDetailURL(postId), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// json trans
						try {
							Json_util.checkErrorCode(response);
							Post vg = Json_util.transToOnePost(response);
							up.pullVGSuccess(vg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Log.i(TAG, ComputeURL.getPostDetailURL(postId));
							up.pullVGFail();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						up.pullVGFail();
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	// 广场发帖
	public static void post(String title, String content,
			final UploadData callback) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET,
				ComputeURL.getPublishPostURL(title, content), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						String msg = Json_util.getUploadReturnMsg(response);
						if (msg.equals(GloableData.UPLOAD_SUCCESS_MSG)) {
							callback.updateSuccess();
						} else {
							callback.updatetFail();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						callback.updatetFail();
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	// 发表评论
	public static void comment(String postId, String content, String userId,
			final UploadData callback) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, ComputeURL.getCommentURL(postId, content,
						userId), null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						String msg = Json_util.getUploadReturnMsg(response);
						if (msg.equals(GloableData.UPLOAD_SUCCESS_MSG)) {
							callback.updateSuccess();
						} else {
							callback.updatetFail();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						callback.updatetFail();
					}
				});
		requestQueue.add(jsonObjectRequest);
	}

	public interface PullEssenseDetail {
		public void pullEDSuccess(EssenseDetail ed);

		public void pullEDFail();
	}

	public interface PullViewGenerator {
		// request one post detail
		public void pullVGSuccess(Post vg);

		public void pullVGFail();
	}

	public interface UploadData {
		public void updateSuccess();

		public void updatetFail();
	}

	public interface DownloadSource {
		public void downloadSuccess();

		public void downloadFail();
	}
}
