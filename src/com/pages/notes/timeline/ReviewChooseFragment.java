package com.pages.notes.timeline;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.ydd.R;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.DatabaseHelper;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.data.util.DisplayUtil;
import com.data.util.GloableData;
import com.data.util.PhotoNamePathUtil;
import com.data.util.RequestAndParseUtil;
import com.data.util.UploadInfoUtil;
import com.pages.notes.ReviewFragment;
import com.pages.notes.footprint.FootprintNoteListAdapter;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReviewChooseFragment extends Fragment{

	LinearLayout bgLayout;
	ListView exerciseTimeLine;
	RelativeLayout layout;
	LinearLayout reviewChooseLayout;
	LinearLayout transferDelLayout;
	LinearLayout courseTransferChooseLayout;
	TextView courseTransferChooseCancelBtn;
	TextView courseTransferChooseConfirmBtn;
	int cancelConfirmColor=Color.parseColor("#429dd7");
	int courseChoosedColor=Color.parseColor("#ffffff");
	int courseNormalColor=Color.parseColor("#999999");
	Typeface typefaceFZLT;
	TextView transferPhoto;
	TextView deletePhoto;
	Button reviewReverse;
	Button reviewEbbin;
	String tableName;
	TextView choose;
	TextView titleCenter;
	ExerciseTimeLineAdapter timeLineAdapter;
	boolean chooseState=false;
	static List<CourseRecordInfo> choosedRecords;
	String targetTransferCourse;
	TextView delete;
	TextView deleteCancel;
	@Override
	public View onCreateView(final LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View  rootView = inflater.inflate(R.layout.fragment_reviewchoose, container, false);
		exerciseTimeLine=(ListView)rootView.findViewById(R.id.exercise_timeline_list);
		tableName=getArguments().getString("course_table_name");
		SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		TreeSet<String> dateSet=DataConstants.dbHelper.queryDates(getActivity(), db, tableName);
		final List<String> dbDates=new ArrayList<String>();
		HashMap<String, List<CourseRecordInfo>> dateAndPhotoInfosMap=new HashMap<String, List<CourseRecordInfo>>();
		Log.e(DataConstants.TAG, "dateSet:"+dateSet);
		for(String date:dateSet)
		{
			dbDates.add(date);
			List<String> photoNames=DataConstants.dbHelper.queryPhotoNamesAtDate(getActivity(), db, tableName, date);
			if(photoNames.size()>0)
			{
				List<CourseRecordInfo> infos=new ArrayList<CourseRecordInfo>();
				for(String photoName:photoNames)
				{
					CourseRecordInfo cri=DataConstants.dbHelper.queryCourseRecordByPhotoName(getActivity(), db, tableName, photoName);
					infos.add(cri);
				}
				dateAndPhotoInfosMap.put(date, infos);
			}
		}
		db.close();
		
		int type=UserConfigs.getTableAndCourseTypeMap().get(tableName);
		
		requestPhotoedDates(RequestAndParseUtil.getPhotoedDatesURL(type),dbDates);
		
		bgLayout=(LinearLayout) rootView.findViewById(R.id.reviewchoose_bg);
		layout=(RelativeLayout)rootView.findViewById(R.id.reviewchoose_layout);
		reviewChooseLayout=(LinearLayout)rootView.findViewById(R.id.review_choose);			
		reviewEbbin=(Button)rootView.findViewById(R.id.ebbinghaus_review);
		reviewEbbin.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.review_btn_bg));
		reviewReverse=(Button)rootView.findViewById(R.id.reverse_review);
		reviewReverse.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.review_btn_bg));
		reviewReverse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jumpToReview(dbDates.get(0));
			}
		});
		initTitleView();
		initTransferDelView(rootView);
		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		initTitleView();
		resumeBackground();
		super.onResume();
	}

	
	
	
	private void initTitleView()
	{
		//typefaceFZLT= Typeface.createFromAsset (getActivity().getAssets(),"font/fangzhenglanting.ttf");
		titleCenter=(TextView)getActivity().findViewById(R.id.title_center);
		if(tableName.equals(getResources().getString(R.string.db_english_table)))
		{
			titleCenter.setText(getResources().getString(R.string.english_catalog));	
		}
		else if(tableName.equals(getResources().getString(R.string.db_politics_table)))
		{
			titleCenter.setText(getResources().getString(R.string.politic_catalog));
		}
		else if(tableName.equals(getResources().getString(R.string.db_math_table)))
		{
			titleCenter.setText(getResources().getString(R.string.math_catalog));
		}
		else if(tableName.equals(getResources().getString(R.string.db_profess1_table)) 
				||tableName.equals(getResources().getString(R.string.db_profess2_table)) )
		{
			titleCenter.setText(getResources().getString(R.string.profess_catalog));
		}
		choose=(TextView)getActivity().findViewById(R.id.right_btn);
		//choose.setText(getResources().getString(R.id.ch))
		choose.setTypeface(DataConstants.typeFZLT);
		//choose.setBackground(background)
		choose.setText(getResources().getString(R.string.choose));
		choose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(choose.getText().toString().equals(getResources().getString(R.string.choose)))
				{
					chooseState=true;
					choosedRecords=new ArrayList<CourseRecordInfo>();
					timeLineAdapter.updateChooseState(chooseState);
					choose.setText(getResources().getString(R.string.cancel));
					transferDelLayout.setVisibility(View.VISIBLE);
					reviewChooseLayout.setVisibility(View.INVISIBLE);
					//layout.setBackgroundResource(Color.parseColor("#000000"));
					bgLayout.setAlpha((float) 0.5);
				}
				else if(choose.getText().toString().equals(getResources().getString(R.string.cancel)))
				{
					chooseState=false;
					timeLineAdapter.updateChooseState(chooseState);
					choose.setText(getResources().getString(R.string.choose));
					transferDelLayout.setVisibility(View.INVISIBLE);
					reviewChooseLayout.setVisibility(View.VISIBLE);
					//layout.setBackgroundResource(Color.parseColor("#000000"));
					bgLayout.setAlpha(0);
				}
			}
		});
	}
	private void setCourseTransferButtonListner(final TextView[] courseButtons)
	{
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < 3; i++) {
					// radios[i].setClickable(false);
					// radios[i].setEnabled(false);
					
					if (courseButtons[i] == v) {
						targetTransferCourse=courseButtons[i].getText().toString();
						courseButtons[i].setTextColor(0);
						courseButtons[i].setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.transferd_course));
						courseButtons[i].setTextColor(courseChoosedColor);
					}
					else
					{
						courseButtons[i].setTextColor(courseNormalColor);
						courseButtons[i].setBackground(DisplayUtil.drawableTransfer(getActivity(),R.drawable.untransfered_course));
					}
				}
			}
		};
		for (int i = 0; i < 3; i++) {
			courseButtons[i].setOnClickListener(listener);
		}
	}
	private void initTransferDelView(View rootView)
	{
		courseTransferChooseLayout=(LinearLayout)rootView.findViewById(R.id.course_transfer_choose);
		TextView[] courseButtons = new TextView[3];
		courseButtons[0]=(TextView)rootView.findViewById(R.id.course1);
		courseButtons[1]=(TextView)rootView.findViewById(R.id.course2); 
		courseButtons[2]=(TextView)rootView.findViewById(R.id.course3);
		courseButtons[0].setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.untransfered_course));
		courseButtons[1].setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.untransfered_course));
		courseButtons[2].setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.untransfered_course));
		int index=0,targetIndex=0;
		final HashMap<String, String> courseNameTableMap=UserConfigs.getCourseNameAndTableMap();
		for(String name:courseNameTableMap.keySet())
		{
			if(courseNameTableMap.get(name).equals(tableName)==false)
			{
				courseButtons[index].setText(name);
				index++;
			}
		}
		for(int i=0;i<courseButtons.length;i++)
		{
			courseButtons[i].setTypeface(typefaceFZLT);
			courseButtons[i].setTextColor(courseNormalColor);
		}
		setCourseTransferButtonListner(courseButtons);
		
		courseTransferChooseCancelBtn=(TextView)rootView.findViewById(R.id.couser_transfer_choose_cancel);
		courseTransferChooseCancelBtn.setTextColor(cancelConfirmColor);
		courseTransferChooseCancelBtn.setTypeface(typefaceFZLT);
		courseTransferChooseCancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.e(DataConstants.TAG,"onclickkkk");
				courseTransferChooseLayout.startAnimation(hideAnimation());
				courseTransferChooseLayout.setVisibility(View.INVISIBLE);
				
			}
		});
		courseTransferChooseConfirmBtn=(TextView)rootView.findViewById(R.id.couser_transfer_choose_confirm);
		courseTransferChooseConfirmBtn.setTextColor(cancelConfirmColor);
		courseTransferChooseConfirmBtn.setTypeface(typefaceFZLT);
		courseTransferChooseConfirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				String photoname=null;
				CourseRecordInfo cri=null;
				String targetTable=courseNameTableMap.get(targetTransferCourse);
				for(CourseRecordInfo choosedRecord:choosedRecords)
				{
					
					photoname=choosedRecord.getPhotoName();
					cri=DataConstants.dbHelper.queryCourseRecordByPhotoName(getActivity(), db, tableName, photoname);
					Log.e(DataConstants.TAG,cri.toString());
					DataConstants.dbHelper.deleteCourseRecordByPhotoName(getActivity(), db, tableName, photoname);
					cri.setTableName(targetTable);
					DataConstants.dbHelper.insertCourseRecord(getActivity(), db, targetTable, cri);
					//FileDataHandler.transferFile(path,FileDataHandler.APP_DIR_PATH+"/"+DataConstants.TABLE_DIR_MAP.get(targetTable)+"/"+photoname);
					//UploadInfoUtil.upl
					String url=UploadInfoUtil.getUploadQuesURL(getActivity(), cri, tableName,cri.getPhotoName());
					UploadInfoUtil.uploadQues(url,getActivity(), cri, targetTable, cri.getPhotoName());
				}
				TreeSet<String> dateSet=DataConstants.dbHelper.queryDates(getActivity(), db, tableName);
				final List<String> dates=new ArrayList<String>();
				for(String date:dateSet)
					dates.add(date);
				db.close();
				courseTransferChooseLayout.startAnimation(hideAnimation());
				courseTransferChooseLayout.setVisibility(View.INVISIBLE);
				chooseState=false;
				timeLineAdapter.setDates(dates);
				timeLineAdapter.updateChooseState(chooseState);
				choose.setText(getResources().getString(R.string.choose));
				transferDelLayout.setVisibility(View.INVISIBLE);
				reviewChooseLayout.setVisibility(View.VISIBLE);
				
			}
		});
		
		transferDelLayout=(LinearLayout)rootView.findViewById(R.id.transfer_delete);
		transferPhoto=(TextView)rootView.findViewById(R.id.transfer_photo);
		deletePhoto=(TextView)rootView.findViewById(R.id.delete_photo);
		transferPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				String targetTable;
				courseTransferChooseLayout.startAnimation(showAnimation());
				courseTransferChooseLayout.setVisibility(View.VISIBLE);
			}
		});
		final LinearLayout deleteLayout=(LinearLayout) rootView.findViewById(R.id.bottom_delete);
		delete=(TextView) rootView.findViewById(R.id.delete_bg);
		delete.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.delete_long_btn));
		deleteCancel=(TextView) rootView.findViewById(R.id.delete_cancel_bg);
		deleteCancel.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.delete_long_btn));
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				String photoname=null;
				for(CourseRecordInfo cr:choosedRecords)
				{
//					String[] info=path.split("/");
//					int len=info.length;
//					photoname=info[len-1];
					photoname=cr.getPhotoName();//PhotoNamePathUtil.pathToPhotoName(path);
					DataConstants.dbHelper.updateCourseRecordOnIntColByPhotoName(getActivity(), db, tableName, photoname, getResources().getString(R.string.dbcol_photo_delete), 1);
				}
				db.close();
				timeLineAdapter.notifyDataSetChanged();
				deleteLayout.startAnimation(hideAnimation());
				deleteLayout.setVisibility(View.INVISIBLE);
				bgLayout.setAlpha(0);
			}
		});
		deleteCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				deleteLayout.startAnimation(hideAnimation());
				deleteLayout.setVisibility(View.INVISIBLE);
				//bglayout.setAlpha(255);
			}
		});
		deletePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				deleteLayout.setVisibility(View.VISIBLE);
				deleteLayout.startAnimation(showAnimation());
			}
		});
	}
	
	public void jumpToReview(String date)
	{
		Fragment fragment=new ReviewFragment();
		Bundle bundle = new Bundle();  
        bundle.putString("type", "");
        bundle.putString("date", date);
        bundle.putString("course_table_name", tableName);
        fragment.setArguments(bundle);
		FragmentManager fm=getActivity().getFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();  
		trans.replace(R.id.exercise_frame, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}
	private Animation showAnimation()
	{
		Animation mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f,     
	            Animation.RELATIVE_TO_SELF, 0.0f, 
	            Animation.RELATIVE_TO_SELF, 1.0f, 
	            Animation.RELATIVE_TO_SELF, 0.0f);     
	    mShowAction.setDuration(500);
	    return mShowAction;
	}
	private Animation hideAnimation()
	{
		Animation mHiddenAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, 
                Animation.RELATIVE_TO_SELF, 0.0f,     
                Animation.RELATIVE_TO_SELF, 0.0f, 
                Animation.RELATIVE_TO_SELF,1.0f);    
		mHiddenAction.setDuration(500);
	    return mHiddenAction;
	}
	private void requestPhotoedDates(String url,final List<String> dbDates)
	{
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.e(DataConstants.TAG, "requestPhotoedDates response=" + response);
				int error=-1;
				try {
					error = response.getInt("errorCode");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(error==0)
				{
					List<String> dates=RequestAndParseUtil.parsePhotoedDatesInfo(response);
					//Log.e(DataConstants.TAG, "dates size"+dates.size());
					for(String dbDate:dbDates)
					{
						if(!dates.contains(dbDate))
							dates.add(dbDate);
					}
					Collections.reverse(dates);
					timeLineAdapter=new ExerciseTimeLineAdapter(getActivity(),tableName,dates);
					exerciseTimeLine.setAdapter(timeLineAdapter);
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
	private void resumeBackground()
	{
		reviewEbbin.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.review_btn_bg));
		reviewReverse.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.review_btn_bg));
		delete.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.delete_long_btn));
		deleteCancel.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.delete_long_btn));
	}
	private void releaseBackground()
	{
		reviewEbbin.setBackground(null);
		reviewReverse.setBackground(null);
		delete.setBackground(null);
		deleteCancel.setBackground(null);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		releaseBackground();
	}
	
}

