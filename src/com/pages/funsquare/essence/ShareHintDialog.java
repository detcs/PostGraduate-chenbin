package com.pages.funsquare.essence;

import com.app.ydd.R;

import android.app.Activity;
import android.app.Dialog;

public class ShareHintDialog {
	private Dialog dialog;

	public ShareHintDialog(Activity activity) {
		// TODO Auto-generated constructor stub
		dialog = new Dialog(activity);
		dialog.setContentView(R.layout.essense_share_hint_dialog);
	}

	public void show() {
		// TODO Auto-generated method stub
		dialog.show();
	}

}
