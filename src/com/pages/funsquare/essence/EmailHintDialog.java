package com.pages.funsquare.essence;

import com.app.ydd.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EmailHintDialog {
	private Dialog dialog;
	private Button quitBu;
	private Button ensureBu;
	private EmailHintDialogCallBack callback;

	public EmailHintDialog(Activity activity,
			final EmailHintDialogCallBack callback) {
		// TODO Auto-generated constructor stub
		this.callback = callback;
		dialog = new Dialog(activity);
		dialog.setContentView(R.layout.essense_email_hint_dialog);
		quitBu = (Button) dialog.findViewById(R.id.quitBu);
		ensureBu = (Button) dialog.findViewById(R.id.ensureBu);
		setListener();
	}

	private void setListener() {
		quitBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callback.quit();
			}
		});
		ensureBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callback.ensure();
			}
		});
	}

	public void show() {
		// TODO Auto-generated method stub
		dialog.show();
	}

	public interface EmailHintDialogCallBack {
		public void quit();

		public void ensure();
	}

}