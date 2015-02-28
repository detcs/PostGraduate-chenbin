package com.pages.notes.todayrec;

import com.app.ydd.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TodayRecommenderFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) 
	{
		View rootView= inflater.inflate(R.layout.fragment_today_rec, container, false);
		return rootView;
	}
}
