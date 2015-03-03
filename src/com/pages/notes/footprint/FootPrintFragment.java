package com.pages.notes.footprint;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
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
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.FootprintDisplayInfo;
import com.data.model.UserConfigs;
import com.data.util.DateUtil;
import com.data.util.GloableData;
import com.data.util.ImageUtil;
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
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) 
	{
		Bundle bundle=getArguments();
		date=bundle.getString("footprint_date");
		 SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		View rootView = inflater.inflate(R.layout.fragment_footprint, container, false);
		
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
		dayReviewInfo=(TextView) rootView.findViewById(R.id.day_review_info);
		dayReviewInfo.setTypeface(DataConstants.typeFZLT);
		int clockNum=UserConfigs.getClockDays();;
		int reviewNum= DataConstants.dbHelper.queryAllCoursesReviewedCountOnDate(getActivity(), db, date);
		int appendNum=DataConstants.dbHelper.queryAllCourseRecordsCountOnDate(getActivity(), db, date);
		dayReviewInfo.setText(getResources().getString(R.string.clocked)+clockNum+getResources().getString(R.string.day)+","
				+getResources().getString(R.string.reviewed)+reviewNum+getResources().getString(R.string.piece)+","
				+getResources().getString(R.string.appended)+appendNum+getResources().getString(R.string.piece));
		diary=(TextView) rootView.findViewById(R.id.day_diary);
		diary.setTypeface(DataConstants.typeFZLT);
		bgImg=(ImageView) rootView.findViewById(R.id.footprint_img);
		FootprintInfo fpInfo=DataConstants.dbHelper.queryFootPrintInfo(getActivity(), db, date);
		/*
		if(fpInfo!=null)
		{
			Log.e(DataConstants.TAG, "queryFootPrintInfo!=null");
			diary.setText(fpInfo.getDiary());
			ImageUtil.imageLoader.displayImage(ImageUtil.filePre+FileDataHandler.FOOTPRINT_PIC_DIR_PATH+"/"+fpInfo.getFootprintPicName(), bgImg);
		}
		else
		*/
		{
			Log.e(DataConstants.TAG, "queryFootPrintInfo==null");
			requestFootprintInfo(getFootprintURL(date));
		}
		appendNote=(TextView) rootView.findViewById(R.id.append_note);
		appendNote.setTypeface(DataConstants.typeFZLT);
		if(appendNum>0)
		{	
			appendNote.setText(getResources().getString(R.string.append_note)+"("+appendNum+")");
			noteList=(ListView)rootView.findViewById(R.id.footprint_list);
			noteList.setAdapter(new FootprintNoteListAdapter(getActivity(),date));
		}
		else
		{
			appendNote.setText(getResources().getString(R.string.no)+getResources().getString(R.string.append_note));
			noteList=(ListView)rootView.findViewById(R.id.footprint_list);
			noteList.setVisibility(View.INVISIBLE);
		}
		
		db.close();
		return rootView;
	}
	private void requestFootprintInfo(String url) {
		//final FootprintInfo fpInfo;
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.e(DataConstants.TAG, "footprint response=" + response);
						int errorCode;
						try {
							errorCode = response.getInt("errorCode");
							if(errorCode==0)
							{
								FootprintDisplayInfo fpInfo=parseFootprintDisplayInfo(response);
								Log.e(DataConstants.TAG, "diary=="+(diary==null)+" fpIno=="+(fpInfo==null));
								diary.setText(fpInfo.getDiary());
								int clockNum=0;
								String reviewNum=fpInfo.getReviewCount();
								String appendNum=fpInfo.getAddCount();
								dayReviewInfo.setText(getResources().getString(R.string.clocked)+clockNum+getResources().getString(R.string.day)+","
										+getResources().getString(R.string.reviewed)+reviewNum+getResources().getString(R.string.piece)+","
										+getResources().getString(R.string.appended)+appendNum+getResources().getString(R.string.piece));
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
}
