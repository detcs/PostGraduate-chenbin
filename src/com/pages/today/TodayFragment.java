package com.pages.today;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.data.util.DisplayUtil;
import com.pages.notes.footprint.DownloadTask;
import com.pages.notes.footprint.FootprintInfo;
import com.pages.viewpager.MainActivity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


public class TodayFragment extends Fragment {
	boolean visible = true;
	MediaPlayer mp;
	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_today, container, false);
		initTodayView(rootView);
		return rootView;
	}
	public void initTodayView(View v) {

		TextView useDays=(TextView) v.findViewById(R.id.use_days);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DAY_OF_YEAR,1);//tomorrow
		String date = sdf.format(calendar.getTime());
		// Log.e(DataConstants.TAG,"date:"+date);
		int gapDays=getDateGapDays(UserConfigs.getStartDay(), date);
		if(gapDays<10)
			useDays.setText("0"+gapDays+"");
		else
			useDays.setText(gapDays+"");
		float useDaysSize=DisplayUtil.spTopx(180*DataConstants.dpiRate, DataConstants.displayMetricsScaledDensity);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),"font/AvenirNextLTPro-UltLt.otf");
		useDays.setTypeface(typeFace);
		useDays.setTextSize(useDaysSize);
		// requestFirstPageJasonInfo(getFirstPageURL(date),date);

		mp = MediaPlayer.create(getActivity(), R.raw.song);
		final ImageView play = (ImageView) v.findViewById(R.id.music_play);
		play.setImageResource(R.drawable.play);
		TextView musicName = (TextView) v.findViewById(R.id.music_name);
		musicName.setText("可惜没如果");
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mp.isPlaying()) {
					play.setImageResource(R.drawable.play);
					mp.pause();
				} else {
					play.setImageResource(R.drawable.pause);
					mp.start();
				}
			}
		});

		// mp.start();
	}
	private void requestFirstPageJasonInfo(String url, final String date) {
		final FootprintInfo fpInfo;
		RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.e(DataConstants.TAG, "response=" + response);
						parseFirstPageInfo(response, date);
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
		requestQueue.add(jsonObjectRequest);
		// return fpInfo;
	}

	private String getFirstPageURL(String date) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MIndex");
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
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";

		}
		Log.e(DataConstants.TAG, "fpage:" + resultURL);
		return resultURL;
	}

	private FootprintInfo parseFirstPageInfo(JSONObject job, String date) {
		FootprintInfo fpInfo = null;
		try {
			JSONObject data = job.getJSONObject("data");
			JSONArray indexs = data.getJSONArray("index_");
			DownloadTask fileDownloadTask;
			DownloadTask songDownloadTask;
			SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
			Log.e(DataConstants.TAG, "len:" + indexs.length() + "");

			for (int i = 0; i < indexs.length(); i++) {
				JSONObject info = indexs.getJSONObject(i);
				JSONArray musics = info.getJSONArray("music_");
				JSONObject music = musics.getJSONObject(0);// info.getJSONObject("music_");
				String songName = music.getString("title_");
				String songId = music.getString("file_");
				String imgId = info.getString("img_");
				String encourage = info.getString("content_");
				String days = info.getString("days_");
				String daysLeft = info.getString("daysLeft_");
				// downloadHandler(DataConstants.DOWNLOAD_URL+songId,
				// FileDataHandler.COVER_SONG_DIR_PATH+"/"+songName+".mp3");
				// downloadHandler(DataConstants.DOWNLOAD_URL+imgId,
				// FileDataHandler.COVER_PIC_DIR_PATH+"/"+imgId+".jpg");
				fileDownloadTask = new DownloadTask(getActivity(),
				FileDataHandler.COVER_PIC_DIR_PATH, getResources().getString(R.string.dbcol_cover_pic), imgId,date);
				fileDownloadTask.execute();
				songDownloadTask = new DownloadTask(getActivity(),
				FileDataHandler.COVER_SONG_DIR_PATH, getResources().getString(R.string.dbcol_cover_song), songId,date);
				songDownloadTask.execute();
				fpInfo = new FootprintInfo("", songName, "", "", date,encourage, days, daysLeft,getResources().getString(R.string.upload_no));
				TextView experienceTv = (TextView) rootView.findViewById(R.id.experience);
				TextView encourageTv = (TextView) rootView.findViewById(R.id.encourage);
				encourageTv.setText(encourage);
				DataConstants.dbHelper.insertFootprintInfoRecord(getActivity(), db, fpInfo);
			}
			db.close();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fpInfo;
	}
	public int getDateGapDays (String beginDate, String endDate)
	{       
			int gapDays=0;
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	        try { 
	                Date date = sdf.parse(endDate);// 通过日期格式的parse()方法将字符串转换成日期              
	                Date dateBegin = sdf.parse(beginDate);
	                long betweenTime = date.getTime() - dateBegin.getTime(); 
	                gapDays = (int)(betweenTime  / 1000 / 60 / 60 / 24); 
	             } catch(Exception e)
	             {
	              }
	        return (int)gapDays; 
	}


}

