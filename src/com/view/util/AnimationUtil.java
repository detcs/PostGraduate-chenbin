package com.view.util;

import com.app.ydd.R;
import com.data.util.SysCall;

import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {
	private static final String TAG = "AnimationUtil";

	public static Animation getShowAnim(int prePosition, int newPosition) {
		if (prePosition < newPosition) {
			return showFromLeft();
		} else if (prePosition > newPosition) {
			return showFromRight();
		} else {
			SysCall.error(TAG);
			return null;
		}
	}

	public static Animation getHideAnim(int prePosition, int newPosition) {
		if (prePosition < newPosition) {
			return hideToRight();
		} else if (prePosition > newPosition) {
			return hideToLeft();
		} else {
			SysCall.error(TAG);
			return null;
		}
	}

	public static Animation showFromLeft() {
		Animation mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		return mShowAction;
	}

	public static Animation showFromRight() {
		Animation mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		return mShowAction;
	}

	public static Animation hideToRight() {
		Animation mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		return mShowAction;
	}

	public static Animation hideToLeft() {
		Animation mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		return mShowAction;
	}

	public static Animation showAnimation() {
		Animation mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		return mShowAction;
	}

	public static Animation hideAnimation() {
		Animation mHiddenAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f);
		mHiddenAction.setDuration(500);
		return mHiddenAction;
	}

	public static Animation rotateAnimation(Context context, int degree) {
		Animation rotateAction = null;
		switch (degree) {
		case 90:
			rotateAction = AnimationUtils.loadAnimation(context,
					R.anim.anim_rotate_90);
			break;
		case -90:
			rotateAction = AnimationUtils.loadAnimation(context,
					R.anim.anim_rotate_m90);
			break;
		default:
			Log.i(TAG, "" + degree);
			SysCall.error("invalid degree");
			break;
		}
		return rotateAction;
	}
}
