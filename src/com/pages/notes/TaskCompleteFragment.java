package com.pages.notes;

import com.app.ydd.R;
import com.data.model.UserConfigs;
import com.data.util.SysCall;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
	ImageView goClock;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		initTitleView();
		View rootView = inflater.inflate(R.layout.fragment_task_complete, container, false);
		int countClock=UserConfigs.getClockDays();
		clockDays=(TextView) rootView.findViewById(R.id.insist_clock_days);
		clockDays.setText(countClock+getResources().getString(R.string.day));
		goClock=(ImageView)rootView.findViewById(R.id.goto_clock_btn);
		Picasso.with(getActivity()).load(R.drawable.clock).into(goClock);
		goClock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Picasso.with(getActivity()).load(R.drawable.clock_click).into(goClock);
			}
		});
		return rootView;
	}
	private void initTitleView()
	{
		Button right=(Button)getActivity().findViewById(R.id.right_btn);
		right.setVisibility(View.INVISIBLE);
		ImageView left=(ImageView)getActivity().findViewById(R.id.exercise_left_btn);
		Picasso.with(getActivity()).load(R.drawable.backhome).into(left);
		left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				UserConfigs.storeClockDay();
				getActivity().finish();
			}
		});
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

