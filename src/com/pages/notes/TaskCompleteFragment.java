package com.pages.notes;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.model.UserConfigs;
import com.data.util.DateUtil;
import com.data.util.DisplayUtil;
import com.data.util.SysCall;
import com.data.util.UploadInfoUtil;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskCompleteFragment extends Fragment{

	ImageView tick;
	TextView wonderful;
	TextView learnComplete;
	TextView insistLearning;
	TextView clockDays;
	TextView goClock;
	int type;
	String subject;
	String date;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		initTitleView();
		View rootView = inflater.inflate(R.layout.fragment_task_complete, container, false);
		int countClock=UserConfigs.getClockDays();
		Bundle bundle=getArguments();
		type=bundle.getInt("subject_type");
		subject=bundle.getString("subject");
		date=bundle.getString("clock_date");
		clockDays=(TextView) rootView.findViewById(R.id.insist_clock_days);
		clockDays.setText(countClock+getResources().getString(R.string.day));
		goClock=(TextView)rootView.findViewById(R.id.goto_clock_btn);
		goClock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goClock.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.clock_click));
				
				String todayDate=DateUtil.getTodayDateString();
				if(UserConfigs.getLatestClockedDate()==null || !UserConfigs.getLatestClockedDate().equals(todayDate))
				{
					
					UserConfigs.storeClockDay(UserConfigs.getClockDays()+1);
					UserConfigs.storeLatestClockedDate(todayDate);
					SQLiteDatabase db= DataConstants.dbHelper.getReadableDatabase();
					String clockedDays="";
					DataConstants.dbHelper.updateFootprintRecord(getActivity(), db, getResources().getString(R.string.dbcol_clocked_days), clockedDays, todayDate);
					db.close();
				}
					
				UploadInfoUtil.uploadClock(getActivity(), UploadInfoUtil.getUploadClockURL(type, subject, date));
				getActivity().finish();
				//Picasso.with(getActivity()).load(R.drawable.clock_click).into(goClock);
			}
		});
		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		initTitleView();
		super.onResume();
	}

	private void initTitleView()
	{
		TextView right=(TextView)getActivity().findViewById(R.id.right_btn);
		right.setVisibility(View.INVISIBLE);
		ImageView left=(ImageView)getActivity().findViewById(R.id.exercise_left_btn);
		Picasso.with(getActivity()).load(R.drawable.backhome).into(left);
		left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Log.e(DataConstants.TAG, "clock complete");
				//UserConfigs.storeClockDay();
				getActivity().finish();
			}
		});
		TextView center=(TextView)getActivity().findViewById(R.id.title_center);
		center.setText(getResources().getString(R.string.clock));
	}
	private void jumpToClockFragment()
	{
		Fragment fragment=new ClockFragment();
//		Bundle bundle = new Bundle();  
//        bundle.putString("type", "");  
//        fragment.setArguments(bundle);
		FragmentManager fm=getActivity().getFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();  
		trans.replace(R.id.exercise_frame, fragment);
		trans.commit();
	}
	
	
}

