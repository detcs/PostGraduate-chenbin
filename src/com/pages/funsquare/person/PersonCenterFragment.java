package com.pages.funsquare.person;

import com.app.ydd.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PersonCenterFragment extends Fragment {
	private View rootView;
	private View infoView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_person_center,
					container, false);
		}
		return rootView;
	}
}
