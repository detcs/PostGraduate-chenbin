package com.pages.funsquare.essence;

import com.app.ydd.R;
import com.view.util.AnimationUtil;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class EssenseShareBump {
	private View view;
	private FrameLayout frame;
	private Activity activity;
	private ShareBumpCallback callback;
	private ImageView imageView1_s;
	private ImageView imageView2_s;
	private View button1_s;
	private Object tag;
	private View RelativeLayout1;

	public EssenseShareBump(FrameLayout frame, Activity activity, Object tag,
			ShareBumpCallback callback) {
		this.frame = frame;
		this.callback = callback;
		this.tag = tag;
		this.activity = activity;
		if (null == view) {
			view = LayoutInflater.from(activity).inflate(
					R.layout.fragment_essense_share, frame, false);
			view.setTag(tag);
		}
	}

	public void show() {
		if (null == frame.findViewWithTag(tag)) {
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
	}

	private void init() {
		RelativeLayout1 = view.findViewById(R.id.RelativeLayout1);
		imageView1_s = (ImageView) view.findViewById(R.id.imageView1);
		imageView2_s = (ImageView) view.findViewById(R.id.imageView2);
		button1_s = view.findViewById(R.id.button1);
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
