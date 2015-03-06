package com.data.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.ydd.R;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.UserConfigs;

public class RequestAndParseUtil {

	public static String getClockedDaysURL(String date) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MSignCountStatus");
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
		pair = new BasicNameValuePair("date",date);
		params.add(pair);
		//pair = new BasicNameValuePair("date", date);
		//params.add(pair);
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";

		}
		Log.e(DataConstants.TAG, "get Clocked url:" + resultURL);
		return resultURL;
	}
	public static String getPhotoedDatesURL(int type) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MGetDateList");
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
		pair = new BasicNameValuePair("type",type+"");
		params.add(pair);
		//pair = new BasicNameValuePair("date", date);
		//params.add(pair);
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";

		}
		Log.e(DataConstants.TAG, "getPhotoedDates url:" + resultURL);
		return resultURL;
	}
	public static List<String> parsePhotoedDatesInfo(JSONObject json)
	{
		List<String> dates=new ArrayList<String>();
		try {
			JSONObject data = json.getJSONObject("data");
			JSONArray list = data.getJSONArray("date_");
			String listStr=list.toString().replace("[", "");
			listStr=listStr.replace("]","");
			String[] datesList=listStr.split(",");
			Log.e(DataConstants.TAG, "len "+list.toString());
			String date;
			for(int i=0;i<datesList.length;i++)
			{
				date=datesList[i].replace("\"","");
				dates.add(date);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dates;
	}
	public static String getQuesURL(String date) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MQuesList");
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
		pair = new BasicNameValuePair("date", date);
		params.add(pair);
		//pair = new BasicNameValuePair("date", date);
		//params.add(pair);
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";

		}
		Log.e(DataConstants.TAG, "ques url:" + resultURL);
		return resultURL;
	}
	public static HashMap<String, List<CourseRecordInfo>> parseQuesInfo(Context context,JSONObject json)
	{
		HashMap<String, List<CourseRecordInfo>> courseAndRecordsMap=new HashMap<String, List<CourseRecordInfo>>();
		
		CourseRecordInfo cri = null;
		List<CourseRecordInfo> englishRecords=new ArrayList<CourseRecordInfo>();
		List<CourseRecordInfo> politicRecords=new ArrayList<CourseRecordInfo>();
		List<CourseRecordInfo> mathRecords=new ArrayList<CourseRecordInfo>();
		List<CourseRecordInfo> professOneRecords=new ArrayList<CourseRecordInfo>();
		List<CourseRecordInfo> professTwoRecords=new ArrayList<CourseRecordInfo>();
		 SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		try {
			JSONObject data = json.getJSONObject("data");
			JSONArray list = data.getJSONArray("list_");
			
			for(int i=0;i<list.length();i++)
			{
				JSONObject obj=list.getJSONObject(i);
				//String id=obj.getString("id_");
				int type=obj.getInt("type_");
				int isImportant=obj.getInt("isHighlight_");
				int isRecommender=obj.getInt("isRecommend_");
				String subject=obj.getString("subject_");
				String remark=obj.getString("remark_");
				String createTime=obj.getString("createTime_");
				String imgId=obj.getString("img_");
				
				String date=createTime.split(" ")[0];
				String time=createTime.split(" ")[1];
				time=date+"|"+time;
				String tableName=null;
				switch (type) {
				case 1:
					tableName=context.getResources().getString(R.string.db_english_table);
					cri=new CourseRecordInfo(imgId, DataConstants.DOWNLOAD_URL+imgId, tableName, "", remark, date, time, context.getResources().getString(R.string.state_unknow), context.getResources().getString(R.string.upload_yes), isImportant, 0, isRecommender, 1);
					englishRecords.add(cri);
					 DataConstants.dbHelper.insertCourseRecord(context, db, tableName, cri);
					break;
				case 2:
					tableName=context.getResources().getString(R.string.db_politics_table);
					cri=new CourseRecordInfo(imgId, DataConstants.DOWNLOAD_URL+imgId, tableName, "", remark, date, time, context.getResources().getString(R.string.state_unknow), context.getResources().getString(R.string.upload_yes), isImportant, 0, isRecommender, 1);
					politicRecords.add(cri);
					 DataConstants.dbHelper.insertCourseRecord(context, db, tableName, cri);
					break;
				case 3:
					tableName=context.getResources().getString(R.string.db_math_table);
					cri=new CourseRecordInfo(imgId, DataConstants.DOWNLOAD_URL+imgId, tableName, "", remark, date, time, context.getResources().getString(R.string.state_unknow), context.getResources().getString(R.string.upload_yes), isImportant, 0, isRecommender, 1);
					mathRecords.add(cri);
					 DataConstants.dbHelper.insertCourseRecord(context, db, tableName, cri);
					break;
				case 4:
					tableName=context.getResources().getString(R.string.db_profess1_table);
					cri=new CourseRecordInfo(imgId, DataConstants.DOWNLOAD_URL+imgId, tableName, "", remark, date, time, context.getResources().getString(R.string.state_unknow), context.getResources().getString(R.string.upload_yes), isImportant, 0, isRecommender, 1);
					professOneRecords.add(cri);
					 DataConstants.dbHelper.insertCourseRecord(context, db, tableName, cri);
					break;
				case 5:
					tableName=context.getResources().getString(R.string.db_profess2_table);
					cri=new CourseRecordInfo(imgId, DataConstants.DOWNLOAD_URL+imgId, tableName, "", remark, date, time, context.getResources().getString(R.string.state_unknow), context.getResources().getString(R.string.upload_yes), isImportant, 0, isRecommender, 1);
					professTwoRecords.add(cri);
					 DataConstants.dbHelper.insertCourseRecord(context, db, tableName, cri);
					break;
				}
				//Log.e(DataConstants.TAG, "parse:"+content+" "+days+" "+daysLeft+" "+footprintImgId+" "+diary+" "+reviewCount+" "+addCount);
								//infos.add(cri);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(DataConstants.TAG, "JSONException "+e);
		}
		db.close();
		if(englishRecords.size()>0)
			courseAndRecordsMap.put(UserConfigs.getCourseEnglishName(), englishRecords);
		if(politicRecords.size()>0)
			courseAndRecordsMap.put(UserConfigs.getCoursePoliticsName(), politicRecords);
		if(mathRecords.size()>0)
			courseAndRecordsMap.put(UserConfigs.getCourseMathName(), mathRecords);
		if(professOneRecords.size()>0)
			courseAndRecordsMap.put(UserConfigs.getCourseProfessOneName(), professOneRecords);
		if(professTwoRecords.size()>0)
			courseAndRecordsMap.put(UserConfigs.getCourseProfessTwoName(), professTwoRecords);
		return courseAndRecordsMap;
	}
	
}
