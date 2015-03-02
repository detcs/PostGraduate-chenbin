package com.pages.notes;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.app.ydd.R;
import com.data.util.SysCall;

public class DialogChangeCourseName {

	private Dialog dialog;
	private View quitView, saveView;
	private TextView tv1;
	private TextView tv2;

	public DialogChangeCourseName(Activity activity) {
		dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.check_new_dialog);
		tv1=(TextView) dialog.findViewById(R.id.textView1);
		tv2=(TextView) dialog.findViewById(R.id.textView2);
		tv1.setText(activity.getResources().getString(R.string.ensure_change_course_name));
		tv2.setText(activity.getResources().getString(R.string.note_still_exsit));
		
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
