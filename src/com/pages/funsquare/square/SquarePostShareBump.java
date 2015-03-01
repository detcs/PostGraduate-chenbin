package com.pages.funsquare.square;

import com.app.ydd.R;
import com.view.util.AnimationUtil;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SquarePostShareBump {
	private View view;
	private FrameLayout frame;
	private PostShareCallBack callback;
	private Activity activity;
	private View friendsView, weixinView, xinnaView, qqView;
	private TextView quitView;
	private View RelativeLayout1;

	public SquarePostShareBump(FrameLayout frame, Activity activity,
			Object tag, PostShareCallBack callback) {
		this.frame = frame;
		this.activity = activity;
		this.callback = callback;
		if (null == view) {
			view = LayoutInflater.from(activity).inflate(
					R.layout.fragment_square_share, frame, false);
			view.setTag(tag);
		}
	}

	public void show() {
		frame.addView(view);
		init();
		Animation anim = AnimationUtil.showAnimation();
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				RelativeLayout1.setBackgroundColor(activity.getResources()
						.getColor(R.color.default_float_back));
				RelativeLayout1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						RelativeLayout1.setBackgroundColor(0xffffff);
						view.startAnimation(AnimationUtil.hideAnimation());
						frame.removeView(view);
					}
				});
			}
		});
		view.startAnimation(anim);

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
		RelativeLayout1 = view.findViewById(R.id.RelativeLayout1);
		friendsView = view.findViewById(R.id.friendsView);
		weixinView = view.findViewById(R.id.weixinView);
		xinnaView = view.findViewById(R.id.xinnaView);
		qqView = view.findViewById(R.id.qqView);
		quitView = (TextView) view.findViewById(R.id.quitView);
		Typeface face = Typeface.createFromAsset(activity.getAssets(),
				"font/fangzhenglanting.ttf");
		quitView.setTypeface(face);
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
