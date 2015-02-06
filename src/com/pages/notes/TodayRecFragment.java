package com.pages.notes;

import com.app.ydd.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TodayRecFragment extends Fragment{
	ImageView recImg;
	@Override
	public View onCreateView(final LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View  rootView = inflater.inflate(R.layout.fragment_today_rec, container, false);
		recImg=(ImageView)rootView.findViewById(R.id.rec_img);
		initTitleView();
		return rootView;
	}
	private void initTitleView()
	{
		
	}

}
