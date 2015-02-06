package com.pages.notes.camera;

import com.app.ydd.R;
import com.pages.notes.camera.ShowFragment.ShowJump;
import com.pages.notes.camera.TakeFragment.TakeJump;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;

/*
 * last change:
 * wsy 2-6
 * 
 * */
//github.com/detcs/PostGraduate-chenbin.git

public class CameraActivity extends Activity implements TakeJump, ShowJump {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		toTake();
	}

	// *************switch fragment*************
	private void toTake() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		TakeFragment takeFragment = new TakeFragment();
		fragmentTransaction.replace(R.id.FrameLayout1, takeFragment);
		fragmentTransaction.commit();
	}

	private void toShow(Bitmap bitmap) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		ShowFragment showFragment = new ShowFragment();
		showFragment.setBitmap(bitmap);
		// TestFragment showFragment=new TestFragment(bitmap);
		fragmentTransaction.replace(R.id.FrameLayout1, showFragment);
		fragmentTransaction.commit();
	}

	// @Override
	// public void onBackPressed() {
	// // TODO Auto-generated method stub
	// Log.e(DataConstants.TAG, "camera on back");
	// setResult(DataConstants.RESULTCODE_COURSE_SETTING);
	// finish();
	// //super.onBackPressed();
	// }

	// **************interface jump**************
	@Override
	public void show(Bitmap bitmap) {
		// TODO Auto-generated method stub
		toShow(bitmap);
	}

	@Override
	public void take() {
		// TODO Auto-generated method stub
		toTake();
	}
}
