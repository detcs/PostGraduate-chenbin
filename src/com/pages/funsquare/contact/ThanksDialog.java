package com.pages.funsquare.contact;

import com.app.ydd.R;

import android.app.Activity;
import android.app.Dialog;

public class ThanksDialog {
	private Dialog dialog;

	public ThanksDialog(Activity activity) {
		dialog = new Dialog(activity);
		dialog.setContentView(R.layout.contact_thanks_dialog);
	}

	public void show() {
		dialog.show();
	}
}
