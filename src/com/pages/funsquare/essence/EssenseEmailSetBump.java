package com.pages.funsquare.essence;

import com.app.ydd.R;
import com.data.util.NetCall;
import com.data.util.SysCall;
import com.data.util.NetCall.InfoChangeCallback;
import com.view.util.AnimationUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EssenseEmailSetBump implements InfoChangeCallback {
	private static final String TAG = "EssenseEmailSetBump";
	private Activity activity;
	private View view;
	private FrameLayout frame;
	private ESBumpCallback callback;
	private TextView quitView_e;
	private TextView saveView_e;
	private EditText editView_e;

	public EssenseEmailSetBump(FrameLayout frame, Activity activity,
			Object tag, ESBumpCallback callback) {
		this.activity = activity;
		this.frame = frame;
		this.callback = callback;
		if (null == view) {
			view = LayoutInflater.from(activity).inflate(
					R.layout.fragment_email_set, frame, false);
			view.setTag(tag);
		}
	}

	public void show() {
		frame.addView(view);
		view.startAnimation(AnimationUtil.showAnimation());
		init();
		// SysCall.bumpSoftInput(editView_e, activity);
	}

	private void init() {
		quitView_e = (TextView) view.findViewById(R.id.quitView);
		saveView_e = (TextView) view.findViewById(R.id.saveView);
		editView_e = (EditText) view.findViewById(R.id.editView);
		quitView_e.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.hideSoftInput(view, activity);
				view.startAnimation(AnimationUtil.hideAnimation());
				frame.removeView(view);
			}
		});
		saveView_e.setOnClickListener(new OnClickListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// reset the email
				String email = editView_e.getText().toString();
				if (SysCall.isEmail(email)) {
					NetCall.changeInfo("", "", email, EssenseEmailSetBump.this);
				} else {
					Toast.makeText(activity, "邮箱格式错误", 500).show();
				}
			}
		});
	}

	public interface ESBumpCallback {
		// public void setFail();

		public void setSuccess();
	}

	// NetCall.InfoChangeCallback
	@SuppressLint("ShowToast")
	@Override
	public void changeSuccess() {
		// TODO Auto-generated method stub
		SysCall.error("email address local change");
		Toast.makeText(activity, "邮箱设置成功", 500).show();
		SysCall.hideSoftInput(view, activity);
		view.startAnimation(AnimationUtil.hideAnimation());
		frame.removeView(view);
		callback.setSuccess();
	}

	@SuppressLint("ShowToast")
	@Override
	public void changeFail() {
		// TODO Auto-generated method stub
		Toast.makeText(activity, "邮箱设置失败", 500).show();
	}
}
