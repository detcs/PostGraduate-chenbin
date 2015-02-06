package com.pages.notes;

import com.app.ydd.R;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class TaskCompleteFragment extends Fragment{

	Button goClock;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_task_complete, container, false);
		goClock=(Button)rootView.findViewById(R.id.goto_clock_btn);
		goClock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jumpToClockFragment();
			}
		});
		return rootView;
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
