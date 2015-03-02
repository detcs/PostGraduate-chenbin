package com.pages.notes;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

import com.app.ydd.R;
import com.data.util.SysCall;

public class DialogChangeCourseName {

	private Dialog dialog;
	private View quitView, saveView;

	public DialogChangeCourseName(Activity activity) {
		dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.check_new_dialog);
		quitView = dialog.findViewById(R.id.quitView);
		saveView = dialog.findViewById(R.id.saveView);
		quitView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		saveView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.error("update");
				dialog.dismiss();
			}
		});
	}

	public void show() {
		dialog.show();
	}
}
