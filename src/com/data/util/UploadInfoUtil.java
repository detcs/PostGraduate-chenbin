package com.data.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.ydd.R;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.DatabaseHelper;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;

public class UploadInfoUtil {

	
	public static void uploadDiary(Context context,String url)
	{
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
	
	public static void uploadQues(final Context context,final CourseRecordInfo cri,final String tableName,final String imgId)
	{
		//RequestQueue requestQueue = Volley.newRequestQueue(context);
		String url=getUploadQuesURL(context, cri, tableName,imgId);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.e(DataConstants.TAG, "upload ques response=" + response);
				int error=-1;
				try {
					error = response.getInt("errorCode");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(error==0)
				{
					DatabaseHelper dbHelper = DataConstants.dbHelper;
					SQLiteDatabase db = dbHelper.getReadableDatabase();
					DatabaseHelper.updateCourseRecordOnStringColByPhotoName(context, db, tableName, 
							context.getResources().getString(R.string.dbcol_ifupload), 
							context.getResources().getString(R.string.upload_yes),cri.getPhotoName());
					db.close();
				}
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
	
	public static String getUploadQuesURL(Context context,CourseRecordInfo cri,String tableName,String imgId) {
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
		pair = new BasicNameValuePair("id", cri.getDbId()+"");
		params.add(pair);
		pair = new BasicNameValuePair("remark",cri.getRemark());
		params.add(pair);
		
		String type=null,subject=null,picPath = null;
		if(tableName.equals(context.getResources().getString(R.string.db_english_table)))
		{
			type="1";
			subject=UserConfigs.getCourseEnglishName();
			picPath=FileDataHandler.APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_english);
		}
		else if(tableName.equals(context.getResources().getString(R.string.db_politics_table)))
		{
			type="2";
			subject=UserConfigs.getCoursePoliticsName();
			picPath=FileDataHandler.APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_politics);
		}
		else if(tableName.equals(context.getResources().getString(R.string.db_math_table)))
		{
			type="3";
			subject=UserConfigs.getCourseMathName();
			picPath=FileDataHandler.APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_math);
		}
		else if(tableName.equals(context.getResources().getString(R.string.db_profess1_table)))
		{
			type="4";
			subject=UserConfigs.getCourseProfessOneName();
			picPath=FileDataHandler.APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_profess1);
		}
		else if(tableName.equals(context.getResources().getString(R.string.db_profess2_table)))
		{
			type="5";
			subject=UserConfigs.getCourseProfessTwoName();
			picPath=FileDataHandler.APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_profess2);
		}
		pair = new BasicNameValuePair("img",imgId);
		params.add(pair);
		pair = new BasicNameValuePair("type",type);
		params.add(pair);
		String createDate=cri.getDate();
		String[] time=cri.getTime().split("\\|");
		String createTime=time[0]+" "+time[1];
		pair = new BasicNameValuePair("createTime",createTime);
		params.add(pair);
		pair = new BasicNameValuePair("subject",subject);
		params.add(pair);
		pair = new BasicNameValuePair("isHighlight",cri.getFlag()+"");
		params.add(pair);
		pair = new BasicNameValuePair("isRecommend",cri.getIfRecommender()+"");
		params.add(pair);
		int learned=cri.getMasterState().equals(context.getResources().getString(R.string.state_master))?1:0;
		pair = new BasicNameValuePair("hasLearned",learned+"");
		params.add(pair);
		pair = new BasicNameValuePair("reviewCount","1");
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
	public static void uploadQuesImg(final Context context,final CourseRecordInfo cri,final String tableName,String imgBase64)
	{
		//RequestQueue requestQueue = Volley.newRequestQueue(context);
		String url=getUploadImgURL(imgBase64);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.e(DataConstants.TAG, "upload img response=" + response);
				int error=-1;
				try {
					error = response.getInt("errorCode");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(error==0)
				{
					JSONObject obj;
					try {
						obj = response.getJSONObject("data");
						String imgId=obj.getString("msg_");
						uploadQues(context, cri, tableName, imgId);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
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
	public static void uploadImg(String base64, Context context,CourseRecordInfo cri, String tableName)
	{
		new UploadPictureTask(base64,context,cri,tableName).execute(DataConstants.SERVER_URL);
	}
	 public  static class UploadPictureTask extends AsyncTask<String , Integer, String>
	 {

		 String imgBase64;
		 Context context;
		 CourseRecordInfo cri;
		 String tableName;
		

		public UploadPictureTask(String imgBase64, Context context,
				CourseRecordInfo cri, String tableName) {
			super();
			this.imgBase64 = imgBase64;
			this.context = context;
			this.cri = cri;
			this.tableName = tableName;
		}

		@Override
		protected String doInBackground(String... url) {
			// TODO Auto-generated method stub
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			BasicNameValuePair pair = new BasicNameValuePair("methodno","MImgUpload");
			params.add(pair);
			//pair = new BasicNameValuePair("debug","MMarketType");
			//params.add(pair);
			pair = new BasicNameValuePair("deviceid","1");
			params.add(pair);
			pair = new BasicNameValuePair("appid","nju");
			params.add(pair);
			pair = new BasicNameValuePair("img",imgBase64);
			params.add(pair);
			String result = null;
			try {
				
				Log.e(DataConstants.TAG,"uploadTask begin");
				
				result = doGet(params, url[0]);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String res;
			JSONObject obj;
			try {
				String imgId=parseJson(result);
				uploadQues(context, cri, tableName, imgId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	 }
	 public  static String parseJson(String jsonResult) throws JSONException
		{
			
			JSONObject result= new JSONObject(jsonResult);
			int errorCode=result.getInt("errorCode");
			String imgId=null;
			if(errorCode==0)
			{
				JSONObject data=result.getJSONObject("data");
				String code=data.getString("code_");
				imgId=data.getString("msg_");//pic id
				
			}
			return imgId;
		}
	 public   static String doGet(List<NameValuePair> params,String url) throws ClientProtocolException, IOException
		{
			String result = null;
			String paramStr="";
			for(NameValuePair nvp:params)
			{
				paramStr+=nvp.getName()+"="+nvp.getValue()+"&";
			}
			//url+="?"+paramStr;
			Log.e(DataConstants.TAG,url);
			HttpPost httpPost=new HttpPost(url); 
			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response=new DefaultHttpClient().execute(httpPost);
			Log.e(DataConstants.TAG,"code:"+response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode()==200)
			{
				HttpEntity entity=response.getEntity(); 
				result=EntityUtils.toString(entity, HTTP.UTF_8);
			}
			//Log.i("com.market",result);
			return result;
			
		}
	public static String getUploadImgURL(String imgBase64) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MImgUpload");
		params.add(pair);
		pair = new BasicNameValuePair("device", "android");
		params.add(pair);
		pair = new BasicNameValuePair("deviceid", "1");
		params.add(pair);
		pair = new BasicNameValuePair("appid", "nju");
		params.add(pair);
		pair = new BasicNameValuePair("img",imgBase64);
		params.add(pair);
		//pair = new BasicNameValuePair("date", date);
		//params.add(pair);
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";

		}
		Log.e(DataConstants.TAG, "Uploadimg:" );
		return resultURL;
	}
	public static void uploadCourseNames(Context context,String url)
	{
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.e(DataConstants.TAG, "upload courses response=" + response);
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
	public static String getUploadCourseNamesURL(String english,String math,String profess1,String profess2) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MUpdateSubject");
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
		pair = new BasicNameValuePair("subjectMath", math);
		params.add(pair);
		pair = new BasicNameValuePair("subjectEng", english);
		params.add(pair);
		pair = new BasicNameValuePair("subjectMajor1", profess1);
		params.add(pair);
		pair = new BasicNameValuePair("subjectMajor2", profess2);
		params.add(pair);
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";

		}
		Log.e(DataConstants.TAG, "Upload course:" + resultURL);
		return resultURL;
	}
	
}
