package com.view.util;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {
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
}
