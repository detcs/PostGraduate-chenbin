package com.pages.funsquare.square;

import com.app.ydd.R;
import com.data.util.NetCall;
import com.data.util.NetCall.ReportCallback;
import com.view.util.AnimationUtil;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;

public class SquarePostReportBump implements ReportCallback {
	private View view;
	private FrameLayout frame;
	private View reportBu, quitBu;
	private String pid;
	private Activity activity;

	public SquarePostReportBump(FrameLayout frame, Activity activity,
			Object tag, String pid) {
		this.frame = frame;
		this.pid = pid;
		this.activity = activity;
		if (null == view) {
			view = LayoutInflater.from(activity).inflate(
					R.layout.fragment_square_report, frame, false);
			view.setTag(tag);
		}
	}

	public void show() {
		frame.addView(view);
		view.startAnimation(AnimationUtil.showAnimation());
		init();
	}

	private void init() {
		findViews();
		setListener();
	}

	private void findViews() {
		reportBu = view.findViewById(R.id.reportBu);
		quitBu = view.findViewById(R.id.quitBu);
	}

	private void setListener() {
		reportBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NetCall.report(pid, SquarePostReportBump.this);
			}
		});
		quitBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.startAnimation(AnimationUtil.hideAnimation());
				frame.removeView(view);
			}
		});
	}

	// NetCall.ReportCallback
	@Override
	public void reportSuccess() {
		// TODO Auto-generated method stub
		Toast.makeText(activity, "举报成功", 500).show();
		view.startAnimation(AnimationUtil.hideAnimation());
		frame.removeView(view);
	}

	@Override
	public void reportFail() {
		// TODO Auto-generated method stub
		Toast.makeText(activity, "举报失败", 500).show();
	}
}