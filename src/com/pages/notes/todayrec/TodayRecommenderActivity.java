package com.pages.notes.todayrec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.data.util.DateUtil;
import com.data.util.DisplayUtil;
import com.data.util.DownloadUrlInfo;
import com.data.util.GloableData;
import com.data.util.ImageUtil;
import com.data.util.NetWorkUtil;
import com.data.util.UploadInfoUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pages.notes.footprint.FootprintInfo;
import com.squareup.picasso.Picasso;
import com.view.util.AnimationUtil;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TodayRecommenderActivity extends Activity{

	TextView titleBack;
	TextView titleCenter;
	TextView titleRight;

	ImageView recImg;
	TextView recSrc;
	TextView recIndex;
	TextView recNum;
	TextView recContent;
	TextView collect;
	List<TodayRecommenderInfo> todayRecList;
	List<View> recViews;
	ViewPager viewPager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_rec);
		initTitleView();
		initBottomView();
		//recImg=(ImageView) findViewById(R.id.todayrec_img);
		recSrc=(TextView) findViewById(R.id.rec_src);
		recIndex=(TextView) findViewById(R.id.rec_index);
		recNum=(TextView) findViewById(R.id.rec_num);
		recContent=(TextView) findViewById(R.id.rec_content);
		collect=(TextView) findViewById(R.id.collect_btn);
		
		//recViews=new ArrayList<View>();
		ImageView img;
		View view;
		viewPager=(ViewPager) findViewById(R.id.today_rec_viewpager);
		
		SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		List<TodayRecommenderInfo> recInfos=DataConstants.dbHelper.queryTodayRecommenderInfoOnDate(getApplicationContext(), db, DateUtil.getTodayDateString());
		db.close();
		if(recInfos==null || recInfos.size()==0)
		{
			Log.e(DataConstants.TAG, "---net quest today rec");
			requestTodayRecommenderInfo(getGetTodayRecommenderURL());
		}
		else
		{
			Log.e(DataConstants.TAG, "---db exist  today rec");
			todayRecList=recInfos;
			setPageViewAdapter(todayRecList);
		}
		
		//requestTodayRecommenderInfo(getGetTodayRecommenderURL());
	}
	private void initBottomView() {
		// TODO Auto-generated method stub
		ImageView gradualBg=(ImageView) findViewById(R.id.gradual_text_bg_img);
		Picasso.with(getApplicationContext()).load(R.drawable.gradual_text_bg).into(gradualBg);
	}
	private void initTitleView()
	{
		RelativeLayout title=(RelativeLayout) findViewById(R.id.today_rec_title);
		title.setBackground(DisplayUtil.drawableTransfer(getApplicationContext(), R.drawable.gradual_title_bg));
		titleBack=(TextView) findViewById(R.id.todayrec_back);
		titleBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		titleCenter=(TextView) findViewById(R.id.todayrec_title_center);
		titleRight=(TextView) findViewById(R.id.todayrec_title_right);
		
	}
	private boolean hasDownloadTodayRecPic()
	{
		File dir=new File(FileDataHandler.TODAY_REC_PIC_DIR_PATH);
		String[] filelist = dir.list();
		String todayDate=DateUtil.getTodayDateString();
		for(int i=0;i<filelist.length;i++)
		{
			String fileDate=filelist[i].split("|")[2];
			if(fileDate.equals(todayDate))
				return true;
		}
		return false;
	}

	private void requestTodayRecommenderInfo(String url) {
		//final FootprintInfo fpInfo;
		//RequestQueue requestQueue = Volley.newRequestQueue(this);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.e(DataConstants.TAG, "TodayRecommender response=" + response);
						todayRecList=parseTodayRecommender(response);
						List<String> imgIds=new ArrayList<String>();
						
						{
							setPageViewAdapter(todayRecList);
						}
						recContent.setText(todayRecList.get(0).getRemark());
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

	private String getGetTodayRecommenderURL() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MQuesRecommend");
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
		//pair = new BasicNameValuePair("date", date);
		//params.add(pair);
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";

		}
		Log.e(DataConstants.TAG, "fpage:" + resultURL);
		return resultURL;
	}
	private List<TodayRecommenderInfo> parseTodayRecommender(JSONObject json)
	{
		List<TodayRecommenderInfo> list=new ArrayList<TodayRecommenderInfo>();
		TodayRecommenderInfo info;
		try {
			JSONObject data = json.getJSONObject("data");
			JSONArray recLists = data.getJSONArray("list_");
			for(int i=0;i<recLists.length();i++)
			{
				JSONObject obj=recLists.getJSONObject(i);
				String id=obj.getString("id_");
				String imgid=obj.getString("img_");
				String remark=obj.getString("remark_");
				String type=obj.getString("type_");
				String displayName=obj.getString("displayName_");
				String subject=obj.getString("subject_");
				String isAd=obj.getString("isAd_");
				boolean ifAd= isAd.equals("1")?true:false;
				info=new TodayRecommenderInfo(id, imgid, remark, subject, type, displayName, ifAd,DateUtil.getTodayDateString(),false,"");
				list.add(info);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	private void setPageViewAdapter(List<TodayRecommenderInfo> infos)
	{
		recNum.setText("/"+infos.size()+"");
		recViews=new ArrayList<View>();
		for(TodayRecommenderInfo info:infos)
		{
			View view=LayoutInflater.from(TodayRecommenderActivity.this).inflate(R.layout.view_today_rec, null);
			ImageView img=(ImageView) view.findViewById(R.id.rec_img);
			String path=FileDataHandler.TODAY_REC_PIC_DIR_PATH+"/"+info.getPicFileName();
			Log.e(DataConstants.TAG, "info.getPicFileName():"+info.getPicFileName());
			//Picasso.with(TodayRecommenderActivity.this).load(new File(path)).into(img);
			//img.setImageResource(R.drawable.note_thumb);
			recViews.add(view);
		}
		viewPager.setAdapter(new PagerAdapter() {
			
			@Override
			public int getItemPosition(Object object) {

				return super.getItemPosition(object);
			}
			@Override
			public void startUpdate(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Parcelable saveState() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				// TODO Auto-generated method stub
				Log.e(DataConstants.TAG, "instantiateItem "+position);
				((ViewPager)container).addView(recViews.get(position));
				final TodayRecommenderInfo recInfo=todayRecList.get(position);
			 	//final int index=position;
				View view=recViews.get(position);
				ImageView img=(ImageView) view.findViewById(R.id.rec_img);
				final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading);
				Log.e(DataConstants.TAG, "recInfo.getImgId() =="+recInfo.getImgId());
				if(recInfo.getPicFileName().equals(""))
				{
					Log.e(DataConstants.TAG, "getpicname ==");
					String uri=DataConstants.DOWNLOAD_URL+recInfo.getImgId();
					ImageUtil.imageLoader.displayImage(uri, img, new SimpleImageLoadingListener()
					{

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub
							super.onLoadingCancelled(imageUri, view);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							//spinner.clearAnimation();
							spinner.setVisibility(View.INVISIBLE); 
							Log.e(DataConstants.TAG, "loading complete");
							 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
							String time = sdf.format(new Date());
								// i as index
							String fileName = UserConfigs.getAccount() + "|" + time + "|"+recInfo.getId()+".jpg";
							//todayRecList.get(index).setPicFileName(fileName);
							recInfo.setPicFileName(fileName);
							String	filePath=FileDataHandler.TODAY_REC_PIC_DIR_PATH+"/"+fileName;

							try {
								FileDataHandler.saveBitmap(loadedImage, filePath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							SQLiteDatabase db=DataConstants.dbHelper.getReadableDatabase();
							DataConstants.dbHelper.insertTodayRecommenderInfoRecord(getApplicationContext(), db, recInfo);
							db.close();
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							super.onLoadingFailed(imageUri, view, failReason);
						}

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							super.onLoadingStarted(imageUri, view);
							Log.e(DataConstants.TAG, "loading start");
							spinner.setVisibility(View.VISIBLE); 
							//spinner.startAnimation(AnimationUtil.loadingAnimation(getApplicationContext()));
						}
						
					});
					//DownloadPicTask picTask=new DownloadPicTask(img,recInfo);
					//picTask.execute(recInfo.getImgId());
				}
				else
				{
					String path=FileDataHandler.TODAY_REC_PIC_DIR_PATH+"/"+recInfo.getPicFileName();
					ImageUtil.imageLoader.displayImage(ImageUtil.filePre+path, img);
					//View view=LayoutInflater.from(TodayRecommenderActivity.this).inflate(R.layout.view_today_rec, null);
					//ImageView img=(ImageView) view.findViewById(R.id.rec_img);
					//Picasso.with(TodayRecommenderActivity.this).load(new File(path)).into(img);
				}
				return recViews.get(position);
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return recViews.size();
			}
			
			@Override
			public void finishUpdate(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void destroyItem(View container, int position, Object arg2) {
				// TODO Auto-generated method stub
				((ViewPager)container).removeView(recViews.get(position));
			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				TodayRecommenderInfo recInfo=todayRecList.get(position);
				recIndex.setText((position+1)+"");
				recContent.setText(recInfo.getRemark());
//				View view=recViews.get(position);
//				ImageView img=(ImageView) view.findViewById(R.id.rec_img);
//				if(recInfo.getPicFileName().equals(""))
//				{
//					DownloadPicTask picTask=new DownloadPicTask(img,recInfo);
//					picTask.execute(recInfo.getImgId());
//				}
//				else
//				{
//					String path=FileDataHandler.TODAY_REC_PIC_DIR_PATH+"/"+recInfo.getPicFileName();
//					//View view=LayoutInflater.from(TodayRecommenderActivity.this).inflate(R.layout.view_today_rec, null);
//					//ImageView img=(ImageView) view.findViewById(R.id.rec_img);
//					Picasso.with(TodayRecommenderActivity.this).load(new File(path)).into(img);
//				}

				final int  index=position;
				boolean collected=recInfo.isIfCollected();
				if(!collected)
				{
					collect.setBackground(DisplayUtil.drawableTransfer(getApplicationContext(), R.drawable.collect_note));
					collect.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							todayRecList.get(index).setIfCollected(true);
							collect.setBackground(DisplayUtil.drawableTransfer(getApplicationContext(), R.drawable.collect_cancel));
							SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
							DataConstants.dbHelper.updateTodayRecommenderRecord(getApplicationContext(), db, getResources().getString(R.string.dbcol_rec_if_collect), "1", todayRecList.get(index).getId());
							
							String fileName=todayRecList.get(index).getPicFileName();
							String srcPath=FileDataHandler.TODAY_REC_PIC_DIR_PATH+"/"+fileName;
							String subject=todayRecList.get(index).getSubject();
							String targetPath=FileDataHandler.APP_DIR_PATH+"/";
							String tableName = null;
							if(subject.contains(getResources().getString(R.string.english)))
							{
								targetPath+=getResources().getString(R.string.dir_english);
								tableName=getResources().getString(R.string.db_english_table);
							}
							else if(subject.equals(getResources().getString(R.string.politics)))
							{
								targetPath+=getResources().getString(R.string.dir_politics);
								tableName=getResources().getString(R.string.db_politics_table);
							}
							if(subject.contains(getResources().getString(R.string.math)))
							{
								targetPath+=getResources().getString(R.string.dir_math);
								tableName=getResources().getString(R.string.db_math_table);
							}
							if(tableName!=null)
							{
								targetPath+="/"+fileName;
								Log.e(DataConstants.TAG, "subject"+subject+": "+srcPath+" to "+targetPath);
								FileDataHandler.copyFile(srcPath, targetPath);
								String timeInfo=fileName.split("\\|")[2];
								String time=fileName.split("\\|")[1]+"|"+timeInfo;
								Log.e(DataConstants.TAG,"insert time "+time);
								CourseRecordInfo cri=new CourseRecordInfo(fileName,ImageUtil.filePre+targetPath,tableName, "", todayRecList.get(index).getRemark(), DateUtil.getTodayDateString(), time, getResources().getString(R.string.state_unknow), getResources().getString(R.string.upload_no), 0, 0, 1,0);
								DataConstants.dbHelper.insertCourseRecord(getApplicationContext(), db, tableName, cri);
								if(NetWorkUtil.isWifiConnected(TodayRecommenderActivity.this))
								{
									CourseRecordInfo criWithID=DataConstants.dbHelper.queryCourseRecordByPhotoName(TodayRecommenderActivity.this, db, tableName, fileName);	
									//Log.e(DataConstants.TAG, "to64 "+photoPath);
									String photoPath=targetPath;
									String imgBase64=FileDataHandler.getBase64ImageStr(photoPath);
									UploadInfoUtil.uploadImg(imgBase64,TodayRecommenderActivity.this, criWithID, tableName);
								}
								db.close();
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.collect_success), Toast.LENGTH_LONG).show();
							}
						}
					});
				}
				else
				{
					
					collect.setBackground(DisplayUtil.drawableTransfer(getApplicationContext(), R.drawable.collect_cancel));
					collect.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							todayRecList.get(index).setIfCollected(false);
							collect.setBackground(DisplayUtil.drawableTransfer(getApplicationContext(), R.drawable.collect_note));
						}
					});
					
					
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}


//	public class DownloadPicTask extends AsyncTask<String, Integer,String> {
//
////		String path;
////		Context context;
//		ImageView img;
//		TodayRecommenderInfo recInfo;
//		public DownloadPicTask(ImageView img, TodayRecommenderInfo recInfo) {
//			super();
//			this.img = img;
//			this.recInfo = recInfo;
//		}
//
//		@Override
//		protected String doInBackground(String... param) {
//			// TODO Auto-generated method stub
//			String urlStr=DataConstants.DOWNLOAD_URL+param[0];
//			Log.e(DataConstants.TAG, urlStr);
//			 OutputStream output=null;
//			 InputStream input=null;
//			 String fileName=null;
//			 String filePath=null;
//			try {  
//	            /* 
//	             * 通过URL取得HttpURLConnection 
//	             * 要网络连接成功，需在AndroidMainfest.xml中进行权限配置 
//	             * <uses-permission android:name="android.permission.INTERNET" /> 
//	             */  
//	            URL url=new URL(urlStr);  
//	            HttpURLConnection conn=(HttpURLConnection)url.openConnection(); 
//	            conn.setRequestProperty("Accept-Encoding", "identity"); 
//	            //取得inputStream，并进行读取  
//	            input=conn.getInputStream(); 
//	            //downloadedNames[i]=fileName;
//	            //Log.e(DataConstants.TAG,"download filename:"+fileName);
//	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
//				String time = sdf.format(new Date());
//				// i as index
//				fileName = UserConfigs.getAccount() + "|" + time + ".jpg";
//				recInfo.setPicFileName(fileName);
//				
//				filePath=FileDataHandler.TODAY_REC_PIC_DIR_PATH+"/"+fileName;
//				//Log.e(DataConstants.TAG, "todayrec "+path);
//				//filePaths.add(path);
//	            File file=new File(filePath);
//	            file.createNewFile();
//	            output=new FileOutputStream(file);
//	            byte[] buffer=new byte[4*1024];  
////	            while(input.read(buffer)!=-1){  
////	                output.write(buffer);  
////	            }  
//	            int temp;
//	            while ((temp = input.read(buffer)) != -1) 
//	            {
//	                output.write(buffer, 0, temp);
//	            }
//	            	output.flush();  
//		        } catch (MalformedURLException e) {  
//		            e.printStackTrace();  
//		        } catch (IOException e) {  
//		            e.printStackTrace();  
//		        }  finally{  
//	            try {  
//	            	if(output!=null)
//	                output.close(); 
//	            	if(input!=null)
//	                input.close();
//	               // System.out.println("success");  
//	            } catch (IOException e) {  
//	                //System.out.println("fail");  
//	                e.printStackTrace();  
//	            }  
//	        }
//			SQLiteDatabase db=DataConstants.dbHelper.getReadableDatabase();
//			DataConstants.dbHelper.insertTodayRecommenderInfoRecord(getApplicationContext(), db, recInfo);
//			db.close();
//			return fileName;
//		}
//
//		@Override
//		protected void onPostExecute(String result) 
//		{
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//
//			String path=FileDataHandler.TODAY_REC_PIC_DIR_PATH+"/"+result;
//			Log.e(DataConstants.TAG, "post img:"+path);
//			//View view=LayoutInflater.from(TodayRecommenderActivity.this).inflate(R.layout.view_today_rec, null);
//			//ImageView img=(ImageView) view.findViewById(R.id.rec_img);
//			Picasso.with(TodayRecommenderActivity.this).load(new File(path)).skipMemoryCache().into(img);
//		}
//	}
	
}
