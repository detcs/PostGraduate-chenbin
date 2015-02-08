package com.pages.funsquare.essence;

import com.app.ydd.R;
import com.view.util.AnimationUtil;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class EssenseShareBump {
	private View view;
	private FrameLayout frame;
	private ShareBumpCallback callback;
	private ImageView imageView1_s;
	private ImageView imageView2_s;
	private Button button1_s;

	public EssenseShareBump(FrameLayout frame, Activity activity, Object tag,
			ShareBumpCallback callback) {
		this.frame = frame;
		this.callback = callback;
		if (null == view) {
			view = LayoutInflater.from(activity).inflate(
					R.layout.fragment_essense_share, frame, false);
			view.setTag(tag);
		}
	}

	public void show() {
		frame.addView(view);
		view.startAnimation(AnimationUtil.showAnimation());
		init();
	}

	private void init() {
		imageView1_s = (ImageView) view.findViewById(R.id.imageView1);
		imageView2_s = (ImageView) view.findViewById(R.id.imageView2);
		button1_s = (Button) view.findViewById(R.id.button1);
		imageView1_s.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 分享到朋友圈
			}
		});
		imageView2_s.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 分享到新浪微博
			}
		});
		button1_s.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.startAnimation(AnimationUtil.hideAnimation());
				frame.removeView(view);
			}
		});
	}

	public interface ShareBumpCallback {
		public void shareSuccess();

		public void shareFail();
	}
}
