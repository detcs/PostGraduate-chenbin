package com.pages.notes.footprint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.ydd.R;
import com.data.model.CourseRecordAndPathInfo;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.FootprintDisplayInfo;
import com.data.model.UserConfigs;
import com.data.util.DateUtil;
import com.data.util.GloableData;
import com.data.util.ImageUtil;
import com.data.util.PhotoNameTableInfo;
import com.data.util.RequestAndParseUtil;
import com.pages.notes.todayrec.TodayRecommenderInfo;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class FootPrintFragment extends Fragment{

	ImageView bgImg;
	 ListView noteList;
	 String date;
	 TextView date_day;
	 TextView date_month_year;
	 TextView dayReviewInfo;
	 TextView diary;
	 TextView appendNote;
	 ScrollView scrollView;
	 List<PhotoNameTableInfo> photoInfos;
	private TextView clockDays;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) 
	{
		Bundle bundle=getArguments();
		date=bundle.getString("footprint_date");
		 SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		View rootView = inflater.inflate(R.layout.fragment_footprint, container, false);
		noteList=(ListView)rootView.findViewById(R.id.footprint_list);
		scrollView=(ScrollView) rootView.findViewById(R.id.scrollview);
		scrollView.post(new Runnable() {
		    
			   @Override
			   public void run() {
			    // TODO Auto-generated method stub
			    scrollView.fullScroll(ScrollView.FOCUS_UP);
			   }
			  });
		date_day=(TextView) rootView.findViewById(R.id.date_day);
		date_day.setTypeface(DataConstants.typeAvenir);
		date_day.setText(date.split("-")[2]);
		date_month_year=(TextView) rootView.findViewById(R.id.date_month_year);
		date_month_year.setTypeface(DataConstants.typeFZLT);
		clockDays=(TextView) rootView.findViewById(R.id.clocked_days);
		clockDays.setTypeface(DataConstants.typeFZLT);
		int clockNum=UserConfigs.getClockDays();
		clockDays.setText(getResources().getString(R.string.clocked)+clockNum+getResources().getString(R.string.day)+",");
		dayReviewInfo=(TextView) rootView.findViewById(R.id.day_review_info);
		dayReviewInfo.setTypeface(DataConstants.typeFZLT);
		
		int reviewNum= DataConstants.dbHelper.queryAllCoursesReviewedCountOnDate(getActivity(), db, date);
		int appendNum=DataConstants.dbHelper.queryAllCourseRecordsCountOnDate(getActivity(), db, date);
		dayReviewInfo.setText(
				getResources().getString(R.string.reviewed)+reviewNum+getResources().getString(R.string.piece)+","
				+getResources().getString(R.string.appended)+appendNum+getResources().getString(R.string.piece));
		diary=(TextView) rootView.findViewById(R.id.day_diary);
		diary.setTypeface(DataConstants.typeFZLT);
		bgImg=(ImageView) rootView.findViewById(R.id.footprint_img);
		appendNote=(TextView) rootView.findViewById(R.id.append_note);
		appendNote.setTypeface(DataConstants.typeFZLT);
		photoInfos=new ArrayList<PhotoNameTableInfo>();
		FootprintInfo fpInfo=DataConstants.dbHelper.queryFootPrintInfo(getActivity(), db, date);
		
		if(fpInfo!=null)
		{
			Log.e(DataConstants.TAG, "queryFootPrintInfo!=null");
			diary.setText(fpInfo.getDiary());
			ImageUtil.imageLoader.displayImage(ImageUtil.filePre+FileDataHandler.FOOTPRINT_PIC_DIR_PATH+"/"+fpInfo.getFootprintPicName(), bgImg);
			//SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
			List<String> usedCourseNames=new ArrayList<String>();
			List<String> courseNames=UserConfigs.getCourseNames();
			HashMap<String, String> map=UserConfigs.getCourseNameAndTableMap();
			List<String> tableNames=new ArrayList<String>();
			HashMap<String, List<CourseRecordInfo>> courseAndPhotoInfoMap=new HashMap<String, List<CourseRecordInfo>>();
			for(String course:courseNames)
			{
				String table=map.get(course);
				List<String> photoNames=DataConstants.dbHelper.queryPhotoNamesAtDate(getActivity(), db, table, date);
				if(photoNames.size()>0)
				{
					usedCourseNames.add(course);
					tableNames.add(table);
					List<CourseRecordInfo> infos=new ArrayList<CourseRecordInfo>();
					for(String photoName:photoNames)
					{
						CourseRecordInfo cri=DataConstants.dbHelper.queryCourseRecordByPhotoName(getActivity(), db, table, photoName);
						infos.add(cri);
					}
					courseAndPhotoInfoMap.put(course, infos);
				}
				
			}
			
			if(usedCourseNames.size()>0)
			{
				noteList.setAdapter(new FootprintNoteListAdapter(getActivity(),date,usedCourseNames,tableNames,courseAndPhotoInfoMap));
				appendNote.setText(getResources().getString(R.string.append_note)+"("+appendNum+")");
			}
			else
			{
				appendNote.setText(getResources().getString(R.string.no)+getResources().getString(R.string.append_note));
				noteList.setVisibility(View.INVISIBLE);
			}
			db.close();
		}
		
		else
		{
			Log.e(DataConstants.TAG, "queryFootPrintInfo==null at "+date);
			requestFootprintInfo(getFootprintURL(date),date);
			requestQuesInfo(RequestAndParseUtil.getQuesURL(date));
		}
		/*
		if(appendNum>0)
		{	
			appendNote.setText(getResources().getString(R.string.append_note)+"("+appendNum+")");
			
		}
		else
		{
			appendNote.setText(getResources().getString(R.string.no)+getResources().getString(R.string.append_note));
			noteList=(ListView)rootView.findViewById(R.id.footprint_list);
			noteList.setVisibility(View.INVISIBLE);
		}
		*/
		
		return rootView;
	}
	
	private void requestQuesInfo(String url) {
		//final FootprintInfo fpInfo;
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.e(DataConstants.TAG, "ques response=" + response);
						int errorCode;
						try {
							errorCode = response.getInt("errorCode");
							if(errorCode==0)
							{
								HashMap<String, List<CourseRecordInfo>> courseAndReocrdsMap=RequestAndParseUtil.parseQuesInfo(getActivity(),response);
								List<String> usedCourseNames=new ArrayList<String>();
								List<String> courseNames=UserConfigs.getCourseNames();
								HashMap<String, String> map=UserConfigs.getCourseNameAndTableMap();
								List<String> tableNames=new ArrayList<String>();
								for(String course:courseNames)
								{
									
									for(String key:courseAndReocrdsMap.keySet())
									{
										Log.e(DataConstants.TAG, "map *"+key+"*"+course+"*"+key.equals(course));
										if(key.equals(course))
										{
											usedCourseNames.add(course);
											tableNames.add(map.get(course));
										}
									}
								}
								Log.e(DataConstants.TAG,"usedCourseNames.size()"+usedCourseNames.size());
								if(usedCourseNames.size()>0)
								{
									//noteList.setAdapter(new FootprintNoteListAdapter(getActivity(),date,usedCourseNames,tableNames,courseAndReocrdsMap));
									int appendNum=0;
									for(String course:courseAndReocrdsMap.keySet())
									{

										int add=courseAndReocrdsMap.get(course).size();
										
										appendNum+=add;
									}
									Log.e(DataConstants.TAG,"appendNum "+appendNum);
									appendNote.setText(getResources().getString(R.string.append_note)+"("+appendNum+")");
									noteList.setVisibility(View.VISIBLE);
									noteList.setAdapter(new FootprintNoteListAdapter(getActivity(),date,usedCourseNames,tableNames,courseAndReocrdsMap));
								}
								else
								{
									appendNote.setText(getResources().getString(R.string.no)+getResources().getString(R.string.append_note));
									noteList.setVisibility(View.INVISIBLE);
								}
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//Picasso.with(getApplicationContext()).load(DataConstants.DOWNLOAD_URL+todayRecList.get(0).getImgId()).into(recImg);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// tv_1.setText(arg0.toString());
						Log.i(DataConstants.TAG,
								"sorry,Error" + arg0.toString());
						// if (progressDialog.isShowing()
						// && progressDialog != null) {
						// progressDialog.dismiss();
						// }
					}
				});
		GloableData.requestQueue.add(jsonObjectRequest);
		// return fpInfo;
	}
	
	private void requestFootprintInfo(String url,final String date) {
		//final FootprintInfo fpInfo;
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.e(DataConstants.TAG, "footprint response=" );
						int errorCode;
						try {
							errorCode = response.getInt("errorCode");
							if(errorCode==0)
							{
								FootprintDisplayInfo fpInfo=parseFootprintDisplayInfo(response);
								//Log.e(DataConstants.TAG, "diary=="+(diary==null)+" fpIno=="+(fpInfo==null));
								diary.setText(fpInfo.getDiary());
								int clockNum=0;
								String reviewNum=fpInfo.getReviewCount();
								String appendNum=fpInfo.getAddCount();
								dayReviewInfo.setText(getResources().getString(R.string.reviewed)+reviewNum+getResources().getString(R.string.piece)+","
										+getResources().getString(R.string.appended)+appendNum+getResources().getString(R.string.piece));
								
								//insert footprint db
								 SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
								 FootprintInfo info=new FootprintInfo("", "","","", fpInfo.getFootprintImgId(), "", fpInfo.getDiary(), date, "", fpInfo.getDays(),fpInfo.getDaysLeft(),getResources().getString(R.string.upload_yes),"");
								 DataConstants.dbHelper.insertFootprintInfoRecord(getActivity(), db, info);
								 db.close();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//Picasso.with(getApplicationContext()).load(DataConstants.DOWNLOAD_URL+todayRecList.get(0).getImgId()).into(recImg);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// tv_1.setText(arg0.toString());
						Log.i(DataConstants.TAG,
								"sorry,Error" + arg0.toString());
						// if (progressDialog.isShowing()
						// && progressDialog != null) {
						// progressDialog.dismiss();
						// }
					}
				});
		GloableData.requestQueue.add(jsonObjectRequest);
		// return fpInfo;
	}
	private String getFootprintURL(String date) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MFootprint");
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
		Log.e(DataConstants.TAG, "footprint url:" + resultURL);
		return resultURL;
	}
	private FootprintDisplayInfo parseFootprintDisplayInfo(JSONObject json)
	{
		FootprintDisplayInfo fpInfo = null;
		
		try {
			JSONObject data = json.getJSONObject("data");
			JSONArray fpList = data.getJSONArray("index_");
			
			for(int i=0;i<fpList.length();i++)
			{
				JSONObject obj=fpList.getJSONObject(i);
				//String id=obj.getString("id_");
				String footprintImgId=obj.getString("imgZj_");
				String days=obj.getString("days_");
				String daysLeft=obj.getString("daysLeft_");
				String diary=obj.getString("diary_");
				String content=obj.getString("content_");
				String reviewCount=obj.getString("reviewCount_");
				String addCount=obj.getString("addCount_");
				Log.e(DataConstants.TAG, "parse:"+content+" "+days+" "+daysLeft+" "+footprintImgId+" "+diary+" "+reviewCount+" "+addCount);
				fpInfo=new FootprintDisplayInfo(content, days, daysLeft, footprintImgId, diary, reviewCount, addCount);		
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(DataConstants.TAG, "JSONException "+e);
		}
		return fpInfo;
	}
	private String getClockedDaysURL(String date) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MFootprint");
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
		Log.e(DataConstants.TAG, "footprint url:" + resultURL);
		return resultURL;
	}
	
	
}
