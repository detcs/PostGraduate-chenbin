package com.pages.funsquare.person;

import com.app.ydd.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;

public class OldPwErrorDialog {
	private Dialog dialog;
	private View ensureView;

	public OldPwErrorDialog(Activity activity) {
		dialog = new Dialog(activity);
		dialog.setContentView(R.layout.old_pw_error_dialog);
		ensureView = dialog.findViewById(R.id.button1);
		ensureView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

	public void show() {
		dialog.show();
	}
}
