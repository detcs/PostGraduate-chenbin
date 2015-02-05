package com.pages.funsquare.contact;

import com.app.ydd.R;
import com.app.ydd.R.id;
import com.app.ydd.R.layout;
import com.app.ydd.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ContactActivity extends Activity {
	private EditText contentView;
	private EditText contactView;
	private Button commitBu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		contentView = (EditText) findViewById(R.id.contentView);
		contactView = (EditText) findViewById(R.id.contactView);
		commitBu = (Button) findViewById(R.id.commitBu);
		commitBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String content = contentView.getText().toString().trim();
				String contact = contactView.getText().toString().trim();
				// 提交
				new ThanksDialog(ContactActivity.this).show();
			}
		});
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	*/
}
