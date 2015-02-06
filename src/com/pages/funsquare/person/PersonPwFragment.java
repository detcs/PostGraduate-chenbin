package com.pages.funsquare.person;

import com.app.ydd.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class PersonPwFragment extends Fragment {
	private View rootView;
	private View saveView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_person_pw_third,
					container, false);
		}
		init(rootView);
		return rootView;
	}

	private void init(View view) {
		findViews(view);
		setListener();
	}

	private void findViews(View view) {
		saveView = view.findViewById(R.id.saveView);
	}

	private void setListener() {
		saveView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new OldPwErrorDialog(getActivity()).show();
			}
		});
	}
}
