package com.pages.notes.timeline;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.ydd.R;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.data.model.DataConstants.PageName;
import com.data.util.GloableData;
import com.data.util.RequestAndParseUtil;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ExerciseTimeLineAdapter extends BaseAdapter {

	//List<String> paths;
	Context context;
	LayoutInflater mInflater;
	List<String> dates;
	String tableName;
	PhotoShowGridAdapter photoShowAdapter;
	boolean chooseState;
	List<CourseRecordInfo> records;
	//List<CourseRecordInfo> photoInfos;
	//HashMap<String, List<CourseRecordInfo>> dateAndPhotoInfosMap;
 	public ExerciseTimeLineAdapter(Context context,String tableName,List<String> dates) {
		super();
		//this.paths = paths;
		this.context=context;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.dates=dates;
		this.tableName=tableName;
		//this.dateAndPhotoInfosMap=dateAndPhotoInfosMap;
	}

 	public void setDates(List<String> dates)
 	{
 		this.dates=dates;
 	}
 	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dates.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dates.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//Log.e(DataConstants.TAG,"timeline getview "+position+" "+chooseState);
		ViewHolder holder;  
	    if (convertView == null) { 
	        convertView = mInflater.inflate(R.layout.item_exercise_timeline, null); 
	        
	        holder = new ViewHolder(); 
	        holder.grid=(GridView)convertView.findViewById(R.id.exercise_timeline_item_grid);
	        holder.day=(TextView)convertView.findViewById(R.id.exercise_timeline_date);
	        //holder.img = (ImageView) convertView.findViewById(R.id.exercise_timeline_img); 
	        convertView.setTag(holder); 
	    } else { 
	       
	        holder = (ViewHolder) convertView.getTag(); 
	    }
	    records=new ArrayList<CourseRecordInfo>();
		SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		List<String> photoNames=DataConstants.dbHelper.queryPhotoNamesAtDate(context, db, tableName, dates.get(position));
		Log.e(DataConstants.TAG, "size");
		if(photoNames.size()>0)
		{
			for(String photoName:photoNames)
			{
				CourseRecordInfo cri=DataConstants.dbHelper.queryCourseRecordByPhotoName(context, db, tableName, photoName);
				records.add(cri);
			}
			photoShowAdapter=new PhotoShowGridAdapter(context,tableName,records,chooseState,PageName.NoteReviewChoose);
			holder.grid.setAdapter(photoShowAdapter);
//			List<String> photoPaths=new ArrayList<String>();
//			String dirPath=FileDataHandler.APP_DIR_PATH+"/"+DataConstants.TABLE_DIR_MAP.get(tableName);
//			 for(String name:photoNames)
//				photoPaths.add(dirPath+"/"+name);
		}
		else
		{
			requestQuesInfo(RequestAndParseUtil.getQuesURL(dates.get(position)),holder.grid);
		}
		db.close();
		
		
	    
	    holder.day.setText(dates.get(position));
	    holder.day.setTypeface(DataConstants.typeFZLT);
	    if(position==0)
	    	holder.day.setTextColor(Color.RED);
	    return convertView; 
	}
	static class ViewHolder { 

		TextView day;
	    GridView grid;

	} 
	public void updateChooseState(boolean chooseState)
	{
		this.chooseState=chooseState;
		//Log.e(DataConstants.TAG,"timeline updatechoose");
		notifyDataSetChanged();
		//photoShowAdapter.updateChooseState(chooseState);
		//
	}
	private void requestQuesInfo(String url,final GridView gridView) {
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
								HashMap<String, List<CourseRecordInfo>> courseAndReocrdsMap=RequestAndParseUtil.parseQuesInfo(context,response);
								String course="";
								
								for(String courseName:UserConfigs.getCourseNames())
								{
									if(UserConfigs.getCourseNameAndTableMap().get(courseName).equals(tableName))
									{
										course=courseName;
										break;
									}
								}
								records=courseAndReocrdsMap.get(course);
								SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
								if(records!=null)
								{
									for(CourseRecordInfo record:records)
									{
										Log.e(DataConstants.TAG, "insert ques record:"+record.getDate());
										DataConstants.dbHelper.insertCourseRecord(context, db, tableName, record);
									}
								}
								db.close();
								photoShowAdapter=new PhotoShowGridAdapter(context,tableName,records,chooseState,PageName.NoteReviewChoose);
								gridView.setAdapter(photoShowAdapter);

								
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

}
