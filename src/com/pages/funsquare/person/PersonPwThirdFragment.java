package com.pages.funsquare.person;

import com.app.ydd.R;
import com.data.util.SysCall;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class PersonPwThirdFragment extends Fragment {
	private View rootView;

	private View backView, saveView;
	private EditText editText1, editText2;

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
		setListener(view);
	}

	private void findViews(View view) {
		backView = view.findViewById(R.id.backView);
		saveView = view.findViewById(R.id.saveView);
		editText1 = (EditText) view.findViewById(R.id.editText1);
		editText2 = (EditText) view.findViewById(R.id.editText2);
	}

	private void setListener(View view) {
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		saveView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}
}
