package com.pages.funsquare.contact;

import com.app.ydd.R;
import com.data.util.NetCall;
import com.data.util.NetCall.ContactCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends Activity implements ContactCallback {
	private EditText contentText, contactText;
	private View commitView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		contentText = (EditText) findViewById(R.id.contentText);
		contactText = (EditText) findViewById(R.id.contactText);
		commitView = findViewById(R.id.commitView);
		commitView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String content = contentText.getText().toString();
				String contact = contactText.getText().toString();
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
