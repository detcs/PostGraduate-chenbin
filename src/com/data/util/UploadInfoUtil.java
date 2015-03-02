package com.data.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data.model.DataConstants;
import com.data.model.UserConfigs;
import com.pages.notes.todayrec.TodayRecommenderActivity.TodatRecDownloadTask;

public class UploadInfoUtil {

	public static void uploadDiary(Context context,String url)
	{
		//RequestQueue requestQueue = Volley.newRequestQueue(context);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.e(DataConstants.TAG, "uploadDiary response=" + response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// tv_1.setText(arg0.toString());
				Log.i(DataConstants.TAG,
						"sorry,Error" + arg0.toString());
			
			}
		});
		GloableData.requestQueue.add(jsonObjectRequest);
	}
	
	public static String getUploadDiaryURL(String id,String content,String date) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MUploadDiary");
		params.add(pair);
		pair = new BasicNameValuePair("device", "android");
		params.add(pair);
		pair = new BasicNameValuePair("deviceid", "1");
		params.add(pair);
		pair = new BasicNameValuePair("appid", "nju");
		params.add(pair);
		pair = new BasicNameValuePair("userid", UserConfigs.getId());
		params.add(pair);
		pair = new BasicNameValuePair("verify", UserConfigs.getVerify());
		params.add(pair);
		pair = new BasicNameValuePair("id", id);
		params.add(pair);
		pair = new BasicNameValuePair("content",content);
		params.add(pair);
		pair = new BasicNameValuePair("date",date);
		params.add(pair);
		//pair = new BasicNameValuePair("date", date);
		//params.add(pair);
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";

		}
		Log.e(DataConstants.TAG, "UploadDiary:" + resultURL);
		return resultURL;
	}
	
	public static void uploadQues(Context context,String url)
	{
		//RequestQueue requestQueue = Volley.newRequestQueue(context);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.e(DataConstants.TAG, "upload ques response=" + response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// tv_1.setText(arg0.toString());
				Log.i(DataConstants.TAG,
						"sorry,Error" + arg0.toString());
			
			}
		});
		GloableData.requestQueue.add(jsonObjectRequest);
	}
	public static String getUploadQuesURL(String id,String remark,String img,String type,String createDate,String subject,int ifImportant,int ifRecommender) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MUploadQues");
		params.add(pair);
		pair = new BasicNameValuePair("device", "android");
		params.add(pair);
		pair = new BasicNameValuePair("deviceid", "1");
		params.add(pair);
		pair = new BasicNameValuePair("appid", "nju");
		params.add(pair);
		pair = new BasicNameValuePair("userid", UserConfigs.getId());
		params.add(pair);
		pair = new BasicNameValuePair("verify", UserConfigs.getVerify());
		params.add(pair);
		pair = new BasicNameValuePair("id", id);
		params.add(pair);
		pair = new BasicNameValuePair("remark",remark);
		params.add(pair);
		pair = new BasicNameValuePair("img",img);
		params.add(pair);
		pair = new BasicNameValuePair("type",type);
		params.add(pair);
		pair = new BasicNameValuePair("createTime",createDate);
		params.add(pair);
		pair = new BasicNameValuePair("subject",subject);
		params.add(pair);
		pair = new BasicNameValuePair("isHighlight",ifImportant+"");
		params.add(pair);
		pair = new BasicNameValuePair("isRecommend",ifRecommender+"");
		params.add(pair);
		//pair = new BasicNameValuePair("date", date);
		//params.add(pair);
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";
		}
		Log.e(DataConstants.TAG, "Upload ques:" + resultURL);
		return resultURL;
	}

}
