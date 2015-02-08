package com.pages.funsquare.square;

import com.app.ydd.R;
import com.view.util.AnimationUtil;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class SquarePostShareBump {
	private View view;
	private FrameLayout frame;
	private PostShareCallBack callback;
	private View friendsView, weixinView, xinnaView, qqView, quitView;

	public SquarePostShareBump(FrameLayout frame, Activity activity,
			Object tag, PostShareCallBack callback) {
		this.frame = frame;
		this.callback = callback;
		if (null == view) {
			view = LayoutInflater.from(activity).inflate(
					R.layout.fragment_square_share, frame, false);
			view.setTag(tag);
		}
	}

	public void show() {
		frame.addView(view);
		view.startAnimation(AnimationUtil.showAnimation());
		init();
	}

	public interface PostShareCallBack {
		public void shareSuccess();

		public void shareFail();
	}

	private void init() {
		findViews();
		setListener();
	}

	private void findViews() {
		friendsView = view.findViewById(R.id.friendsView);
		weixinView = view.findViewById(R.id.weixinView);
		xinnaView = view.findViewById(R.id.xinnaView);
		qqView = view.findViewById(R.id.qqView);
		quitView = view.findViewById(R.id.quitView);
	}

	private void setListener() {
		// ...
		friendsView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		quitView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.startAnimation(AnimationUtil.hideAnimation());
				frame.removeView(view);
			}
		});
	}
}
