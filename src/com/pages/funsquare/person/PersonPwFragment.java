package com.pages.funsquare.person;

import com.app.ydd.R;
import com.data.util.NetCall;
import com.data.util.SysCall;
import com.data.util.NetCall.PwChangeCallback;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class PersonPwFragment extends Fragment implements PwChangeCallback {
	private View rootView;
	private View saveView, backView;
	private EditText textView1, textView3, textView4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_person_pw, container,
					false);
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
		backView = view.findViewById(R.id.backView);
	}

	private void setListener() {
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		saveView.setOnClickListener(new OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String oldpw = textView1.getText().toString();
				String pw = textView3.getText().toString();
				String ensure = textView4.getText().toString();
				if (pw.equals(ensure)) {
					NetCall.changePw(oldpw, pw, PersonPwFragment.this);
				} else {
					Toast.makeText(getActivity(), "两次输入密码不同请重新输入", 500);
				}
				//
			}
		});
	}

	// NetCall.PwChangeCallback
	@SuppressLint("ShowToast")
	@Override
	public void changeSuccess() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "修改成功", 500);
	}

	@Override
	public void changeFail(int error) {
		// TODO Auto-generated method stub
		if (PwChangeCallback.NETERROR == error) {
			Toast.makeText(getActivity(), "网络连接失败", 500);
		} else {
			new OldPwErrorDialog(getActivity()).show();
		}
	}
}
