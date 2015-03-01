package com.pages.funsquare.reserve;

import java.util.Timer;
import java.util.TimerTask;

import com.app.ydd.R;
import com.data.util.SysCall;
import com.view.util.AnimationUtil;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ReservedActivity extends Activity {
	private View backBu;
	private View setView;

	private FrameLayout frame;
	private Button button1;
	private View growBackView;
	private TextView perText;
	private TextView bottomHintText;
	private int square_size;

	// fonts set
	private TextView[] fonts;
	private static final int[] fontsId = { R.id.setView, R.id.textView21,
			R.id.textView22, R.id.hintText, R.id.bottomHintText };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserved);
		init(true);
	}

	// *************init*************
	private void init(boolean isReserved) {
		findViews();
		initView();
		startAnim();
		isNeedReserve(isReserved);
		setListener();
	}

	private void findViews() {
		bottomHintText = (TextView) findViewById(R.id.bottomHintText);
		growBackView = findViewById(R.id.growBackView);
		backBu = findViewById(R.id.backBu);
		setView = findViewById(R.id.setView);
		frame = (FrameLayout) findViewById(R.id.frame);
		button1 = (Button) findViewById(R.id.button1);
	}

	private void initView() {
		// fonts set
		Typeface face = Typeface.createFromAsset(getAssets(),
				"font/fangzhenglanting.ttf");
		fonts = new TextView[fontsId.length];
		for (int i = 0; i < fontsId.length; i++) {
			fonts[i] = (TextView) findViewById(fontsId[i]);
			fonts[i].setTypeface(face);
		}
		Typeface uface = Typeface.createFromAsset(getAssets(),
				"font/AvenirNextLTPro-UltLt.otf");
		perText = (TextView) findViewById(R.id.perText);
		perText.setTypeface(uface);
	}

	private void startAnim() {
		// growBackView, perText
		square_size = AnimationUtil.dip2px(this, 250);

		final float per = 0.6f;
		// Animation mShowAction = new ScaleAnimation(fromX, toX, fromY, toY,
		// pivotX, pivotY)
		Animation mShowAction = new ScaleAnimation(1.0f, 1.0f, 0.0f, per, 0.0f,
				square_size);
		mShowAction.setDuration(1500);
		mShowAction.setAnimationListener(new AnimationListener() {

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
				growBackView.setVisibility(View.VISIBLE);
				growBackView.measure(0, 0);
				ViewGroup.LayoutParams params = growBackView.getLayoutParams();
				params.height = (int) (square_size * per);
				Log.i("wsy", "" + params.height);
				growBackView.setLayoutParams(params);
			}
		});
		growBackView.startAnimation(mShowAction);

		ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
		animation.setDuration(1500);
		animation.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				perText.setText((int) ((Float) animation.getAnimatedValue()
						* per * 100)
						+ "%");
			}
		});
		// animation.setInterpolator(new CycleInterpolator(3));
		animation.start();

	}

	private void isNeedReserve(boolean isNeed) {
		if (isNeed) {
			// button1需要备份的状态
			bottomHintText.setText("您已备份20份笔记，还有80篇未备份。");
			button1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					bottomHintText.setText("正在为您备份，超压缩上传。一张图片只要几十k。");
					button1.setVisibility(View.INVISIBLE);
					button1.setClickable(false);
					button1.setEnabled(false);
				}
			});
		} else {
			// 完成备份的状态
			button1.setBackground(getResources().getDrawable(
					R.drawable.reserve_backup_over));
			button1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SysCall.clickBack();
				}
			});
		}
	}

	private void setListener() {
		backBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		setView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ReservedActivity.this,
						ReserveModeActivity.class);
				startActivity(intent);
			}
		});
	}
}
