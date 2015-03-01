package com.pages.funsquare.contact;

import com.app.ydd.R;
import com.data.util.NetCall;
import com.data.util.NetCall.ContactCallback;
import com.data.util.SysCall;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends Activity implements ContactCallback {
	private EditText contentText, contactText;
	private View backView;
	private Button commitView;
	private TextView[] texts;
	private static final int[] ids = { R.id.textView1, R.id.textView2,
			R.id.textView4, R.id.textView6, R.id.textView7 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		backView = findViewById(R.id.backView);

		Typeface face = Typeface.createFromAsset(getAssets(),
				"font/fangzhenglanting.ttf");
		contentText = (EditText) findViewById(R.id.contentText);
		contactText = (EditText) findViewById(R.id.contactText);
		commitView = (Button) findViewById(R.id.commitView);
		contentText.setTypeface(face);
		contactText.setTypeface(face);
		commitView.setTypeface(face);
		texts = new TextView[ids.length];
		for (int i = 0; i < ids.length; i++) {
			texts[i] = (TextView) findViewById(ids[i]);
			texts[i].setTypeface(face);
		}

		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		commitView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					commitView.setBackground(getResources().getDrawable(
							R.drawable.contact_commit_down));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					commitView.setBackground(getResources().getDrawable(
							R.drawable.contact_commit));
				}
				return false;
			}
		});
		commitView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String content = contentText.getText().toString();
				String contact = contactText.getText().toString();
				if (content.trim().equals("")) {
					Toast.makeText(ContactActivity.this, "请输入反馈内容", 500).show();
					return;
				}
				NetCall.contactUs(content, contact, ContactActivity.this);
			}
		});
	}

	@Override
	public void contactSuccess() {
		// TODO Auto-generated method stub
		new ContactThanksDialog(this).show();
	}

	@SuppressLint("ShowToast")
	@Override
	public void contactFail() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "提交失败", 500);
	}
}
