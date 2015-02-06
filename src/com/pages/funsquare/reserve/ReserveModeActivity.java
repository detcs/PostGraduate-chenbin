package com.pages.funsquare.reserve;

import com.app.ydd.R;
import com.data.util.SysCall;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ReserveModeActivity extends Activity {
	private RelativeLayout[] modes;
	private ImageView[] imageViews;
	private ImageView backView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserve_mode);
		init();
	}

	private void init() {
		findViews();
		initViews();
		setListener();
	}

	private void findViews() {
		backView = (ImageView) findViewById(R.id.backView);
		modes = new RelativeLayout[2];
		imageViews = new ImageView[2];
		modes[0] = (RelativeLayout) findViewById(R.id.mode1);
		modes[1] = (RelativeLayout) findViewById(R.id.mode2);
		imageViews[0] = (ImageView) findViewById(R.id.imageView1);
		imageViews[1] = (ImageView) findViewById(R.id.ImageView01);
	}

	private void initViews() {
		// check saved mode
		SysCall.error("check saved mode");
		imageViews[0].setVisibility(View.VISIBLE);
		imageViews[1].setVisibility(View.INVISIBLE);
	}

	private void setListener() {
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.error("check saved mode");
				int choose = 0;
				for (int i = 0; i < 2; i++) {
					if (v == modes[i]) {
						imageViews[i].setVisibility(View.VISIBLE);
						choose = i;
					} else {
						imageViews[i].setVisibility(View.INVISIBLE);
					}
				}
				// save chosen mode
			}
		};
		for (int i = 0; i < 2; i++) {
			modes[i].setOnClickListener(listener);
		}
	}
}
