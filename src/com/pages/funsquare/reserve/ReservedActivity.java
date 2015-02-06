package com.pages.funsquare.reserve;

import com.app.ydd.R;
import com.data.util.SysCall;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReservedActivity extends Activity {
	private Button backBu;
	private TextView setView;

	private Button button1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserved);
		init(true);
	}

	// *************init*************
	private void init(boolean isReserved) {
		findViews();
		isNeedReserve(isReserved);
		setListener();
	}

	private void findViews() {
		backBu = (Button) findViewById(R.id.backBu);
		setView = (TextView) findViewById(R.id.setView);
		button1 = (Button) findViewById(R.id.button1);
	}

	private void isNeedReserve(boolean isNeed) {
		if (isNeed) {
			// button1
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
