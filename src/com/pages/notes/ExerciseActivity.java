package com.pages.notes;



import com.app.ydd.R;
import com.data.model.DataConstants;
import com.pages.notes.timeline.ReviewChooseFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

public class ExerciseActivity extends Activity{

	Fragment fragment;
	FrameLayout frame;
	FragmentManager fm;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);
		Intent intent=getIntent();
		String tag=intent.getStringExtra("tag");
		if(tag.equals(getResources().getString(R.string.first_use)))
		{
			frame=(FrameLayout)findViewById(R.id.exercise_frame);
			fragment=new CourseSettingFragment();
//			Bundle bundle = new Bundle();  
//	        bundle.putString("type", getResources().getString(R.string.today_rec));  
//	        fragment.setArguments(bundle);
			fm=getFragmentManager();
			FragmentTransaction trans = fm.beginTransaction();  
			trans.replace(R.id.exercise_frame, fragment);
			trans.commit();
		}
		else if(tag.equals(getResources().getString(R.string.today_rec)))
		{
			frame=(FrameLayout)findViewById(R.id.exercise_frame);
			fragment=new ReviewFragment();
			Bundle bundle = new Bundle();  
	        bundle.putString("type", getResources().getString(R.string.today_rec));  
	        fragment.setArguments(bundle);
			fm=getFragmentManager();
			FragmentTransaction trans = fm.beginTransaction();  
			trans.replace(R.id.exercise_frame, fragment);
			trans.commit();
			
		}
		else if(tag.equals(getResources().getString(R.string.note_class)))
		{
			frame=(FrameLayout)findViewById(R.id.exercise_frame);
			fragment=new ReviewChooseFragment();
			
			String tableName=getIntent().getStringExtra("course_table_name");
			Bundle bundle = new Bundle();  
	        bundle.putString("type", "");  
	        bundle.putString("course_table_name", tableName);  
	        fragment.setArguments(bundle);
			fm=getFragmentManager();
			FragmentTransaction trans = fm.beginTransaction();  
			trans.replace(R.id.exercise_frame, fragment,getResources().getString(R.string.reviewchoose_fra_tag));
			//trans.addToBackStack(null);
			trans.commit();
		}
		
		//frame.
	}
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		
////		setResult(DataConstants.RESULTCODE_COURSE_SETTING);
////		finish();
////		super.onBackPressed();
//	}
	
	
	
}
