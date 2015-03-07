package com.pages.notes.timeline;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.ydd.R;
import com.data.model.CourseRecordAndPathInfo;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.data.model.DataConstants.PageName;
import com.data.util.DateUtil;
import com.data.util.DisplayUtil;
import com.data.util.GloableData;
import com.data.util.ImageUtil;
import com.data.util.NetWorkUtil;
import com.data.util.PhotoNamePathUtil;
import com.data.util.PhotoNameTableInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pages.notes.ExerciseActivity;
import com.pages.notes.ReviewFragment;
import com.pages.notes.SingleNoteFragment;
import com.pages.notes.footprint.FootprintNoteListAdapter;
import com.pages.notes.footprint.PhotoBrowseActivity;
import com.squareup.picasso.Picasso;

public class PhotoShowGridAdapter extends BaseAdapter
{

	//List<String> imgPaths;
	Context context;
	LayoutInflater mInflater;
	boolean chooseState=false;
	//List<CourseRecordInfo> courseInfos;
	List<CourseRecordInfo> photoInfos;
	String tableName;
	PageName fromPage;
	public PhotoShowGridAdapter(Context context,String tableName,List<CourseRecordInfo> photoInfos,boolean chooseState,PageName fromPage) {
		super();
		//this.imgPaths = imgPaths;
		this.context=context;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.chooseState=chooseState;
		this.tableName=tableName;
		this.photoInfos=photoInfos;
		if(photoInfos==null)
			this.photoInfos=new ArrayList<CourseRecordInfo>();
		this.fromPage=fromPage;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return photoInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return photoInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//Log.e(DataConstants.TAG,"photoshow getview "+position+" "+chooseState);
		GridViewHolder holder; 
	    if (convertView == null) { 
	        convertView = mInflater.inflate(R.layout.item_timeline_grid_item, null); 
	        holder = new GridViewHolder(); 
	        holder.img = (ImageView) convertView.findViewById(R.id.exercise_timeline_img); 
	        holder.chooseFlag=(ImageView) convertView.findViewById(R.id.choose_flag); 
	        holder.importanceFlag=(ImageView) convertView.findViewById(R.id.importance_flag); 
	        holder.loading=(ProgressBar) convertView.findViewById(R.id.loading);
	        SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
	 	    CourseRecordInfo cri=photoInfos.get(position);	 	    		
	        if(cri.getFlag()==1)
	        	holder.flag=1;
	        else
	        	holder.flag=0;
	        db.close();
	 	    convertView.setTag(holder); 
	    } else { 
	        holder = (GridViewHolder) convertView.getTag(); 
	    }
	    int width=(DataConstants.screenWidth-10)/4;	   
	    FrameLayout.LayoutParams param=new FrameLayout.LayoutParams(width,width);
	    holder.img.setLayoutParams(param);
	   BitmapFactory.Options opt=new BitmapFactory.Options();
	   //opt.outHeight=width;
	   //opt.outWidth=width;
	   opt.inSampleSize=8;
	    DisplayImageOptions opts=new DisplayImageOptions.Builder()
		.cacheInMemory(false)
		.cacheOnDisk(true)
		//.showImageOnLoading(R.drawable.note_thumb)
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.decodingOptions(opt)
		.build();
	    ImageUtil.imageLoader.displayImage(photoInfos.get(position).getPhotoPath(), holder.img,opts,new PhotoLoadingListener(holder.loading, context));
	   if(holder.flag==1)
	    	{
				holder.importanceFlag.setVisibility(View.VISIBLE);
				holder.importanceFlag.setImageDrawable(DisplayUtil.drawableTransfer(context, R.drawable.importance));
				//Picasso.with(context).load(R.drawable.importance).into(holder.importanceFlag); 
			}
	  
	   if(chooseState)
	    {
//		   if(holder.flag==1)
//	    	{
//				holder.chooseFlag.setVisibility(View.VISIBLE);
//				Picasso.with(context).load(R.drawable.importance).into(holder.chooseFlag); 
//			}
	    	holder.img.setEnabled(true);
	    	holder.img.setOnClickListener(new ChooseStateButtonClickListener(photoInfos.get(position), holder));
	    }
	    else
	    {   
//	    	if(holder.flag==1)
//	    	{
//				holder.chooseFlag.setVisibility(View.VISIBLE);
//				Picasso.with(context).load(R.drawable.importance).into(holder.chooseFlag); 
//			}
//	    	else
	    	{
	    		if(holder.chooseFlag.getVisibility()==View.VISIBLE)
	    			holder.chooseFlag.setVisibility(View.INVISIBLE);
	    	}
	    	holder.img.setOnClickListener(new CheckSingleNoteClickListener(photoInfos.get(position)));
	    }
		
	    return convertView; 
	}
	public void updateChooseState(boolean chooseState)
	{
		this.chooseState=chooseState;
		Log.e(DataConstants.TAG,"photoshow updatechoose "+chooseState);
		//ReviewChooseFragment.choosedPhotoPaths=new ArrayList<String>();
		notifyDataSetChanged();
		
	}
	class CheckSingleNoteClickListener implements OnClickListener
	{

		CourseRecordInfo cri;
		
		
		public CheckSingleNoteClickListener(CourseRecordInfo cri) {
			super();
			this.cri = cri;
		}

	

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			jumpToReview(cri);
		}
		
	}
	class ChooseStateButtonClickListener implements OnClickListener
	{

		CourseRecordInfo record;
		//String path;
		GridViewHolder holder;
		public ChooseStateButtonClickListener(CourseRecordInfo cri,GridViewHolder holder) {
			super();
			this.record=cri;
			this.holder=holder;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(holder.chooseFlag.getVisibility()==View.INVISIBLE)
			{
				holder.chooseFlag.setVisibility(View.VISIBLE);
				ReviewChooseFragment.choosedRecords.add(record);
				//Log.e(DataConstants.TAG, "choose add "+ReviewChooseFragment.choosedPhotoPaths.size());
				
			}
			else
			{
				
				{
					holder.chooseFlag.setVisibility(View.INVISIBLE);
					ReviewChooseFragment.choosedRecords.remove(record);
				}
					
				
				//Log.e(DataConstants.TAG, "choose remove "+ReviewChooseFragment.choosedPhotoPaths.size());
				
			}
		}
		
	}
	public void jumpToReview(CourseRecordInfo cri)
	{
		//String[] info=path.split("/");
		String date=cri.getDate();
//		List<CourseRecordInfo> courseInfos=new ArrayList<CourseRecordInfo>();
//		//List<PhotoNameTableInfo> photoInfos=new ArrayList<PhotoNameTableInfo>();
//		//textUtils=new ArrayList<TextContentShowUtil>();
//		//PhotoNameTableInfo photoInfo=null;
//		CourseRecordInfo courseInfo=null;
//		SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
//		if(fromPage==PageName.NoteFootprint)
//		{
//			HashMap<String, String> map=UserConfigs.getCourseNameAndTableMap();
//			
//			for(String course:map.keySet())
//			{
//				
//				String table=map.get(course);
//				List<String> result=DataConstants.dbHelper.queryPhotoNamesAtDate(context, db, table, date);
//				String dir=DataConstants.TABLE_DIR_MAP.get(table);
//				String path;
//				for(String name:result)
//				{
//					path=FileDataHandler.APP_DIR_PATH+"/"+dir+"/"+name;
//					//paths.add(path);
//					photoInfo=new PhotoNameTableInfo(name, table, path);
//					photoInfos.add(photoInfo);
//				}
//			}
//		}
//		else if(fromPage==PageName.NoteReviewChoose)// three days current course
//		{
//			String agoDate="";
//			for(int i=0;i<3;i++)
//			{
//				agoDate=DateUtil.getAgoDateStringBefore(date, i);
//				List<String> result=DataConstants.dbHelper.queryPhotoNamesAtDate(context, db, tableName, agoDate);
//				String dir=DataConstants.TABLE_DIR_MAP.get(tableName);
//				String path;
//				if(result.size()>0)
//				{
//					for(String name:result)
//					{
//						courseInfo=DataConstants.dbHelper.queryCourseRecordByPhotoName(context, db, tableName, name);
//						courseInfos.add(courseInfo);
//					}
//				}
//				else
//				{
//					//net work request
//					if(NetWorkUtil.isConnected(context))
//					{
//						requestQuesInfo(getQuesURL(date));
//					}
//				}
//			}
//		}

//		db.close();
		//Log.e(DataConstants.TAG, "jump "+info[info.length-1]+" "+date);
		Intent intent=new Intent();
		intent.setClass(context, PhotoBrowseActivity.class);
		Bundle bundle=new Bundle();
		//bundle.putString("jump_tag", context.getResources().getString(R.string.jump_tag_footprint));
		bundle.putSerializable("from_page", fromPage);
		bundle.putString("date",date);
		intent.putExtra("photo_show_bundle", bundle);
		//bundle.putString("table_name",tableName);
		intent.putExtra("courseInfos", (Serializable)photoInfos);
		//intent.putParcelableArrayListExtra(name, value)
		context.startActivity(intent);
		//for(CourseRecordInfo )
		//ImageUtil.imageLoader.clearMemoryCache();
	}
//
//	private void requestQuesInfo(String url) {
//		//final FootprintInfo fpInfo;
//		
//		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
//				new Response.Listener<JSONObject>() {
//					@Override
//					public void onResponse(JSONObject response) {
//						Log.e(DataConstants.TAG, "ques response=" + response);
//						int errorCode;
//						try {
//							errorCode = response.getInt("errorCode");
//							if(errorCode==0)
//							{
//								HashMap<String, List<CourseRecordInfo>> courseAndReocrdsMap=parseQuesInfo(response);
//								List<String> usedCourseNames=new ArrayList<String>();
//								List<String> courseNames=UserConfigs.getCourseNames();
//								HashMap<String, String> map=UserConfigs.getCourseNameAndTableMap();
//								List<String> tableNames=new ArrayList<String>();
//								
//							}
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						
//						//Picasso.with(getApplicationContext()).load(DataConstants.DOWNLOAD_URL+todayRecList.get(0).getImgId()).into(recImg);
//					}
//				}, new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError arg0) {
//						// tv_1.setText(arg0.toString());
//						Log.i(DataConstants.TAG,
//								"sorry,Error" + arg0.toString());
//						// if (progressDialog.isShowing()
//						// && progressDialog != null) {
//						// progressDialog.dismiss();
//						// }
//					}
//				});
//		GloableData.requestQueue.add(jsonObjectRequest);
//		// return fpInfo;
//	}
//	private String getQuesURL(String date) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MQuesList");
//		params.add(pair);
//		pair = new BasicNameValuePair("device", "android");
//		params.add(pair);
//		pair = new BasicNameValuePair("deviceid", "1");
//		params.add(pair);
//		pair = new BasicNameValuePair("appid", "nju");
//		params.add(pair);
//		pair = new BasicNameValuePair("userid", UserConfigs.getId());
//		params.add(pair);
//		pair = new BasicNameValuePair("verify", UserConfigs.getVerify());
//		params.add(pair);
//		pair = new BasicNameValuePair("date", date);
//		params.add(pair);
//		//pair = new BasicNameValuePair("date", date);
//		//params.add(pair);
//		String resultURL = DataConstants.SERVER_URL + "?";
//		for (NameValuePair nvp : params) {
//			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";
//
//		}
//		Log.e(DataConstants.TAG, "ques url:" + resultURL);
//		return resultURL;
//	}
//	private HashMap<String, List<CourseRecordInfo>> parseQuesInfo(JSONObject json)
//	{
//		HashMap<String, List<CourseRecordInfo>> courseAndRecordsMap=new HashMap<String, List<CourseRecordInfo>>();
//		
//		CourseRecordInfo cri = null;
//		List<CourseRecordInfo> englishRecords=new ArrayList<CourseRecordInfo>();
//		List<CourseRecordInfo> politicRecords=new ArrayList<CourseRecordInfo>();
//		List<CourseRecordInfo> mathRecords=new ArrayList<CourseRecordInfo>();
//		List<CourseRecordInfo> professOneRecords=new ArrayList<CourseRecordInfo>();
//		List<CourseRecordInfo> professTwoRecords=new ArrayList<CourseRecordInfo>();
//		 SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
//		try {
//			JSONObject data = json.getJSONObject("data");
//			JSONArray list = data.getJSONArray("list_");
//			
//			for(int i=0;i<list.length();i++)
//			{
//				JSONObject obj=list.getJSONObject(i);
//				//String id=obj.getString("id_");
//				int type=obj.getInt("type_");
//				int isImportant=obj.getInt("isHighlight_");
//				int isRecommender=obj.getInt("isRecommend_");
//				String subject=obj.getString("subject_");
//				String remark=obj.getString("remark_");
//				String createTime=obj.getString("createTime_");
//				String imgId=obj.getString("img_");
//				
//				String date=createTime.split(" ")[0];
//				String time=createTime.split(" ")[1];
//				time=date+"|"+time;
//				String tableName=null;
//				switch (type) {
//				case 1:
//					tableName=context.getResources().getString(R.string.db_english_table);
//					cri=new CourseRecordInfo(imgId, DataConstants.DOWNLOAD_URL+imgId, tableName, "", remark, date, time, context.getResources().getString(R.string.state_unknow), context.getResources().getString(R.string.upload_yes), isImportant, 0, isRecommender, 1);
//					englishRecords.add(cri);
//					 DataConstants.dbHelper.insertCourseRecord(context. db, tableName, cri);
//					break;
//				case 2:
//					tableName=context.getResources().getString(R.string.db_politics_table);
//					cri=new CourseRecordInfo(imgId, DataConstants.DOWNLOAD_URL+imgId, tableName, "", remark, date, time, context.getResources().getString(R.string.state_unknow), context.getResources().getString(R.string.upload_yes), isImportant, 0, isRecommender, 1);
//					politicRecords.add(cri);
//					 DataConstants.dbHelper.insertCourseRecord(context, db, tableName, cri);
//					break;
//				case 3:
//					tableName=context.getResources().getString(R.string.db_math_table);
//					cri=new CourseRecordInfo(imgId, DataConstants.DOWNLOAD_URL+imgId, tableName, "", remark, date, time, context.getResources().getString(R.string.state_unknow), context.getResources().getString(R.string.upload_yes), isImportant, 0, isRecommender, 1);
//					mathRecords.add(cri);
//					 DataConstants.dbHelper.insertCourseRecord(context, db, tableName, cri);
//					break;
//				case 4:
//					tableName=context.getResources().getString(R.string.db_profess1_table);
//					cri=new CourseRecordInfo(imgId, DataConstants.DOWNLOAD_URL+imgId, tableName, "", remark, date, time, context.getResources().getString(R.string.state_unknow), context.getResources().getString(R.string.upload_yes), isImportant, 0, isRecommender, 1);
//					professOneRecords.add(cri);
//					 DataConstants.dbHelper.insertCourseRecord(context, db, tableName, cri);
//					break;
//				case 5:
//					tableName=context.getResources().getString(R.string.db_profess2_table);
//					cri=new CourseRecordInfo(imgId, DataConstants.DOWNLOAD_URL+imgId, tableName, "", remark, date, time, context.getResources().getString(R.string.state_unknow), context.getResources().getString(R.string.upload_yes), isImportant, 0, isRecommender, 1);
//					professTwoRecords.add(cri);
//					 DataConstants.dbHelper.insertCourseRecord(context, db, tableName, cri);
//					break;
//				}
//				//Log.e(DataConstants.TAG, "parse:"+content+" "+days+" "+daysLeft+" "+footprintImgId+" "+diary+" "+reviewCount+" "+addCount);
//								//infos.add(cri);
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.e(DataConstants.TAG, "JSONException "+e);
//		}
//		db.close();
//		if(englishRecords.size()>0)
//			courseAndRecordsMap.put(UserConfigs.getCourseEnglishName(), englishRecords);
//		if(politicRecords.size()>0)
//			courseAndRecordsMap.put(UserConfigs.getCoursePoliticsName(), politicRecords);
//		if(mathRecords.size()>0)
//			courseAndRecordsMap.put(UserConfigs.getCourseMathName(), mathRecords);
//		if(professOneRecords.size()>0)
//			courseAndRecordsMap.put(UserConfigs.getCourseProfessOneName(), professOneRecords);
//		if(professTwoRecords.size()>0)
//			courseAndRecordsMap.put(UserConfigs.getCourseProfessTwoName(), professTwoRecords);
//		return courseAndRecordsMap;
//	}
}
	class PhotoLoadingListener extends SimpleImageLoadingListener
	{

		ProgressBar bar;
		Context context;
		

		public PhotoLoadingListener(ProgressBar bar, Context context) {
			super();
			this.bar = bar;
			this.context = context;
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub
			super.onLoadingCancelled(imageUri, view);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// TODO Auto-generated method stub
			super.onLoadingComplete(imageUri, view, loadedImage);
			bar.setVisibility(View.INVISIBLE);
			view.setEnabled(true);
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
			bar.setVisibility(View.VISIBLE);
			view.setEnabled(false);
		}
		
	}
class GridViewHolder { 

    ImageView img; 
    ImageView chooseFlag;
    ImageView importanceFlag;
    ProgressBar loading;
    int flag;
} 
