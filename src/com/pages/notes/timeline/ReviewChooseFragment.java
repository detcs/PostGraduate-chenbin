package com.pages.notes.timeline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.app.ydd.R;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.pages.notes.ReviewFragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReviewChooseFragment extends Fragment{

	ListView exerciseTimeLine;
	RelativeLayout layout;
	LinearLayout reviewChooseLayout;
	LinearLayout transferDelLayout;
	LinearLayout courseTransferChooseLayout;
	Button courseTransferChooseCancelBtn;
	Button courseTransferChooseConfirmBtn;
	TextView transferPhoto;
	TextView deletePhoto;
	Button reviewReverse;
	Button reviewEbbin;
	String tableName;
	Button choose;
	ExerciseTimeLineAdapter timeLineAdapter;
	boolean chooseState=false;
	static List<String> choosedPhotoPaths;
	@Override
	public View onCreateView(final LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View  rootView = inflater.inflate(R.layout.fragment_reviewchoose, container, false);
		exerciseTimeLine=(ListView)rootView.findViewById(R.id.exercise_timeline_list);
		//paths=new ArrayList<String>();
		
		tableName=getArguments().getString("course_table_name");//getResources().getString(R.string.db_english_table);
		//choosePaths(DataConstants.SD_PATH+"/"+DataConstants.PHOTO_DIR_PATH+"/"+tableName);
		SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		TreeSet<String> dateSet=DataConstants.dbHelper.queryDates(getActivity(), db, tableName);
		final List<String> dates=new ArrayList<String>();
		for(String date:dateSet)
			dates.add(date);
		db.close();
		layout=(RelativeLayout)rootView.findViewById(R.id.reviewchoose_layout);
		reviewChooseLayout=(LinearLayout)rootView.findViewById(R.id.review_choose);	
		timeLineAdapter=new ExerciseTimeLineAdapter(getActivity(),tableName,dates);
		exerciseTimeLine.setAdapter(timeLineAdapter);
		reviewReverse=(Button)rootView.findViewById(R.id.reverse_review);
		reviewReverse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jumpToReview(dates.get(0));
			}
		});
		courseTransferChooseLayout=(LinearLayout)rootView.findViewById(R.id.course_transfer_choose);
		Button course1=(Button)rootView.findViewById(R.id.couser1);
		course1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.e(DataConstants.TAG,"onclickkkk");
				courseTransferChooseLayout.startAnimation(hideAnimation());
				courseTransferChooseLayout.setVisibility(View.INVISIBLE);
			}
		});
		courseTransferChooseCancelBtn=(Button)rootView.findViewById(R.id.couser_transfer_choose_cancel);
		courseTransferChooseCancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.e(DataConstants.TAG,"onclickkkk");
				courseTransferChooseLayout.startAnimation(hideAnimation());
				courseTransferChooseLayout.setVisibility(View.INVISIBLE);
			}
		});
		courseTransferChooseConfirmBtn=(Button)rootView.findViewById(R.id.couser_transfer_choose_confirm);
		transferDelLayout=(LinearLayout)rootView.findViewById(R.id.transfer_delete);
		transferPhoto=(TextView)rootView.findViewById(R.id.transfer_photo);
		deletePhoto=(TextView)rootView.findViewById(R.id.delete_photo);
		transferPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				String photoname=null;
				CourseRecordInfo cri=null;
				String targetTable;
				courseTransferChooseLayout.startAnimation(showAnimation());
				courseTransferChooseLayout.setVisibility(View.VISIBLE);
				/*
				for(String path:choosedPhotoPaths)
				{
					String[] info=path.split("/");
					int len=info.length;
					photoname=info[len-1];
					cri=DataConstants.dbHelper.queryCourseRecordByPhotoName(getActivity(), db, tableName, photoname);
					DataConstants.dbHelper.deleteCourseRecordByPhotoName(getActivity(), db, tableName, photoname);
					targetTable=getResources().getString(R.string.db_profess1_table);
					DataConstants.dbHelper.insertCourseRecord(getActivity(), db, targetTable, cri);
					FileDataHandler.transferFile(path,FileDataHandler.APP_DIR_PATH+"/"+DataConstants.TABLE_DIR_MAP.get(targetTable)+"/"+photoname);
				}
				*/
			}
		});
		deletePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				String photoname=null;
				for(String path:choosedPhotoPaths)
				{
					String[] info=path.split("/");
					int len=info.length;
					photoname=info[len-1];
					//Log.e(DataConstants.TAG,"del "+photoname);
					DataConstants.dbHelper.updateCourseRecordOnIntColByPhotoName(getActivity(), db, tableName, photoname, getResources().getString(R.string.dbcol_photo_delete), 1);
				}
				db.close();
				timeLineAdapter.notifyDataSetChanged();
			}
		});
		choose=(Button)getActivity().findViewById(R.id.right_btn);
		choose.setText(getResources().getString(R.string.choose));
		choose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(choose.getText().toString().equals(getResources().getString(R.string.choose)))
				{
					chooseState=true;
					choosedPhotoPaths=new ArrayList<String>();
					timeLineAdapter.updateChooseState(chooseState);
					choose.setText(getResources().getString(R.string.cancel));
					transferDelLayout.setVisibility(View.VISIBLE);
					reviewChooseLayout.setVisibility(View.INVISIBLE);
				}
				else if(choose.getText().toString().equals(getResources().getString(R.string.cancel)))
				{
					chooseState=false;
					timeLineAdapter.updateChooseState(chooseState);
					choose.setText(getResources().getString(R.string.choose));
					transferDelLayout.setVisibility(View.INVISIBLE);
					reviewChooseLayout.setVisibility(View.VISIBLE);
				}
			}
		});
		return rootView;
	}
	
	
	public void jumpToReview(String date)
	{
		Fragment fragment=new ReviewFragment();
		Bundle bundle = new Bundle();  
        bundle.putString("type", "");
        bundle.putString("date", date);
        bundle.putString("course_table_name", tableName);
        fragment.setArguments(bundle);
		FragmentManager fm=getActivity().getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();  
		trans.replace(R.id.exercise_frame, fragment);
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

}
