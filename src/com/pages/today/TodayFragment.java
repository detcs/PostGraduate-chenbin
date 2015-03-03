package com.pages.today;

import java.io.File;
import java.io.IOException;
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
import com.data.model.MusicService;
import com.data.model.UserConfigs;
import com.data.util.DateUtil;
import com.data.util.DisplayUtil;
import com.data.util.NetWorkUtil;
import com.pages.notes.footprint.DownloadTask;
import com.pages.notes.footprint.FootprintInfo;
import com.pages.viewpager.MainActivity;
import com.squareup.picasso.Picasso;
import com.view.util.RoundProgressBar;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.MediaStore.Audio;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class TodayFragment extends Fragment {
	boolean visible = true;
	MediaPlayer mp;
	View rootView;
	ImageView todayBgImg;
	//TextView musicName;
	//TextView singerName;
	//private RoundProgressBar mRoundProgressBar;
	int progress=0;
	Activity mActivity; 
	SQLiteDatabase db ;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_today, container, false);
		initTodayView(rootView);
		return rootView;
	}
	public void setActivity(Activity activity)
	{
		mActivity=activity;
	}
	public void initTodayView(View v) {

		todayBgImg=(ImageView) v.findViewById(R.id.today_bg_img);
		Picasso.with(getActivity()).load(R.drawable.today_background).resize(DataConstants.screenWidth/2, DataConstants.screenHeight/2).into(todayBgImg);
		TextView useDays=(TextView) v.findViewById(R.id.use_days);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		//calendar.roll(Calendar.DAY_OF_YEAR,1);//tomorrow
		String date = sdf.format(calendar.getTime());
		// Log.e(DataConstants.TAG,"date:"+date);
		int gapDays=DateUtil.getDateGapDays(UserConfigs.getStartDay(), date);
		if(gapDays<10)
			useDays.setText("0"+gapDays+"");
		else
			useDays.setText(gapDays+"");
		float useDaysSize=DisplayUtil.spTopx(180*DataConstants.dpiRate, DataConstants.displayMetricsScaledDensity);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),"font/AvenirNextLTPro-UltLt.otf");
		useDays.setTypeface(typeFace);
		useDays.setTextSize(useDaysSize);
		

		//mRoundProgressBar=(RoundProgressBar)v.findViewById(R.id.roundProgressBar);
		//mp = MediaPlayer.create(getActivity(), R.raw.song);
		//final ImageView play = (ImageView) v.findViewById(R.id.music_play);
		//play.setImageResource(R.drawable.music_play);
		//musicName = (TextView) v.findViewById(R.id.music_name);
		//musicName.setTypeface(DataConstants.typeFZLT);
		//singerName=(TextView) v.findViewById(R.id.singer_name);
		//singerName.setTypeface(DataConstants.typeFZLT);
		db= DataConstants.dbHelper.getReadableDatabase();
		FootprintInfo footPrintInfo=DataConstants.dbHelper.queryFootPrintInfo(getActivity(), db, date);
		if(footPrintInfo!=null)
		{
			Log.e(DataConstants.TAG, "footprint not null");
			setFirstpageView(footPrintInfo);
		}
		else
		{
			if(!NetWorkUtil.isConnected(getActivity()))
			{
				NetWorkUtil.showNoNetwork(getActivity());
			}
			else
			{
				requestFirstPageJasonInfo(getFirstPageURL(date),date,true);
				if(NetWorkUtil.isWifiConnected(getActivity()))
				{
					String laterDate=null;
					FootprintInfo laterFootPrintInfo=null;
//					for(int i=1;i<10;i++)
//					{
//						laterDate=DateUtil.getLaterDateStringAfter(date, i);
//						laterFootPrintInfo=DataConstants.dbHelper.queryFootPrintInfo(getActivity(), db, laterDate);
//						if(laterFootPrintInfo==null)
//						{
//							Log.e(DataConstants.TAG, "laterFootPrintInfo==null");
//							requestFirstPageJasonInfo(getFirstPageURL(laterDate),laterDate,false);
//						}
//					}
					
				}
			}
		}
		/*
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity, com.data.model.MusicService.class);
	            Bundle bundle2service = new Bundle();
	            String audioPath=FileDataHandler.COVER_SONG_DIR_PATH+"/201512823526.mp3";
	            //String audioPath=FileDataHandler.SD_PATH+"/kgmusic/download/1.mp3";
	            Log.e(DataConstants.TAG,"songpath "+audioPath);
	            String bc_receiver=DataConstants.MUSIC_SERVICE;
	            bundle2service.putString("audioPath", audioPath);//前面要定义AUDIO_PATH
	            //BC_RECEIVER也要在前面定义，并在manifest.xml里注册
	            bundle2service.putString("bc_receiver",bc_receiver);
	            intent.putExtras(bundle2service);
	            ((MainActivity)mActivity).startMusicService(intent);
	            /*
				if (mp.isPlaying()) {
					play.setImageResource(R.drawable.music_play);
					mp.pause();
				} else {
					play.setImageResource(R.drawable.music_pause);
					mp.start();
					new Thread(new Runnable() 
					{
						
						@Override
						public void run() 
						{
							while(progress <= 1000){
								progress += 1;
								
								System.out.println(progress);
								
								mRoundProgressBar.setProgress(progress);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
						}
						}).start();
					}
					
				}
				
			
		});
		 */
		// mp.start();
	}
	public void setFirstpageView(FootprintInfo info)
	{
		//musicName.setText(info.getCoverSongName());
		//singerName.setText(info.getCoverSingerName());
		String bgPath=FileDataHandler.COVER_PIC_DIR_PATH+"/"+info.getCoverPicName();
		Picasso.with(getActivity()).load(new File(bgPath)).resize(DataConstants.screenWidth/2, DataConstants.screenHeight/2).into(todayBgImg);
		TextView experienceTv = (TextView) rootView.findViewById(R.id.experience);
		TextView encourageTv = (TextView) rootView.findViewById(R.id.encourage);
		encourageTv.setText(info.getEncourage());
	}
	private void requestFirstPageJasonInfo(String url, final String date,final boolean updateUI) {
		final FootprintInfo fpInfo;
		RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						//Log.e(DataConstants.TAG, "response=" + response);
						FootprintInfo info=parseFirstPageInfo(response, date,updateUI);
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

	private FootprintInfo parseFirstPageInfo(JSONObject job, String date,boolean updateUI) {
		FootprintInfo fpInfo = null;
		try {
			JSONObject data = job.getJSONObject("data");
			JSONArray indexs = data.getJSONArray("index_");
			DownloadTask fileDownloadTask;
			DownloadTask songDownloadTask;
			
			Log.e(DataConstants.TAG, "len:" + indexs.length() + "");

			for (int i = 0; i < indexs.length(); i++) {
				JSONObject info = indexs.getJSONObject(i);
				JSONArray musics = info.getJSONArray("music_");
				JSONObject music = musics.getJSONObject(0);// info.getJSONObject("music_");
				//Log.e(DataConstants.TAG,"music "+music.toString());
				String songName = music.getString("title_");
				String songId = music.getString("file_");
				String coverImgId = info.getString("img_");
				String footprintImgId=info.getString("imgZj_");
				String coverTwoImgId=info.getString("imgGn_");
				String encourage = info.getString("content_");
				String days = info.getString("days_");
				String daysLeft = info.getString("daysLeft_");
				String singer=music.getString("singer_");
				// downloadHandler(DataConstants.DOWNLOAD_URL+songId,
				// FileDataHandler.COVER_SONG_DIR_PATH+"/"+songName+".mp3");
				// downloadHandler(DataConstants.DOWNLOAD_URL+imgId,
				// FileDataHandler.COVER_PIC_DIR_PATH+"/"+imgId+".jpg");
				fpInfo = new FootprintInfo("","songfile", songName,singer,"","covertwopic","diary", date,encourage, days, daysLeft,getResources().getString(R.string.upload_no));
				fileDownloadTask = new DownloadTask(getActivity(), date,fpInfo,TodayFragment.this,updateUI);
				String[] fileIds={coverImgId,songId,footprintImgId,coverTwoImgId};
				fileDownloadTask.execute(fileIds);
//				File songFile=new File(FileDataHandler.COVER_SONG_DIR_PATH+"/"+songId);
//				//if(!songFile.exists())
//				{
//					songDownloadTask = new DownloadTask(getActivity(),
//					 getResources().getString(R.string.dbcol_cover_song), date,fpInfo);
//					songDownloadTask.execute();
//				}
				//musicName.setText(songName);
				//mp.setDataSource(FileDataHandler.COVER_SONG_DIR_PATH+"/"+"201512823526.mp3");
				//mp.prepare();
				//Log.e(DataConstants.TAG,"songname "+ songName);
				//singerName.setText(singer);
				
				
				//DataConstants.dbHelper.insertFootprintInfoRecord(getActivity(), db, fpInfo);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fpInfo;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		db= DataConstants.dbHelper.getReadableDatabase();
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		db.close();
	}
	


}

