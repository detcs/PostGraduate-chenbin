package com.pages.funsquare.square;

import com.app.ydd.R;
import com.view.util.AnimationUtil;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class SquarePostReportBump {
	private View view;
	private FrameLayout frame;

	public SquarePostReportBump(FrameLayout frame, Activity activity, Object tag) {
		this.frame = frame;
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
	}
}
