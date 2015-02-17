package com.pages.funsquare.essence;

import com.app.ydd.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShareHintDialog {
	private Dialog dialog;
	private Button quitBu;
	private Button shareBu;

	public ShareHintDialog(Activity activity, final ShareHintCallBack callback) {
		// TODO Auto-generated constructor stub
		dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.essense_share_hint_dialog);
		quitBu = (Button) dialog.findViewById(R.id.quitBu);
		shareBu = (Button) dialog.findViewById(R.id.shareBu);
		quitBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		shareBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				callback.ensureShare();
			}
		});
	}

	public interface ShareHintCallBack {
		public void ensureShare();
	}

	public void show() {
		// TODO Auto-generated method stub
		dialog.show();
	}

}
