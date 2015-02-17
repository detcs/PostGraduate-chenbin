package com.data.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class SysCall {
	public static boolean isEmail(String email) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static void hideSoftInput(View rootView, Activity avtivity) {
		InputMethodManager imm = (InputMethodManager) avtivity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
	}

	public static void bumpSoftInput(EditText rootView, Activity avtivity) {
		rootView.setFocusable(true);
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		InputMethodManager imm = (InputMethodManager) avtivity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(rootView, 0);
	}

	public static void hint(Context context, String hint) {
		Toast.makeText(context, hint, Toast.LENGTH_SHORT).show();
	}

	public static void clickBack() {
		try {
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
		} catch (IOException e) {
			// Log.e("Exception when doBack", e.toString());
			e.printStackTrace();
		}
	}

	public static void error(String info) {
		try {
			throw new Exception(info + "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
