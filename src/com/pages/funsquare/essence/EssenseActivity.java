package com.pages.funsquare.essence;

import com.app.ydd.R;
import com.data.model.EssenseDetail;
import com.data.util.NetCall;
import com.data.util.NetCall.PullEssenseDetail;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class EssenseActivity extends Activity implements EssenseJump,
		PullEssenseDetail {
	// private static final String TAG = "EssenseActivity";
	private static final String SHARETAG = "EssenseShareFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_essense);
		init();
	}

	// EssenseJump
	@Override
	public void detail(String id) {
		// TODO Auto-generated method stub
		NetCall.getEssenseDetail(id, this);
	}

	@Override
	public void query() {
		// TODO Auto-generated method stub
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		EssenseQueryFragment fragment = new EssenseQueryFragment();
		transaction.replace(R.id.FrameLayout1, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void addShare(EssenseDetail ed) {
		// TODO Auto-generated method stub
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		EssenseShareFragment fragment = new EssenseShareFragment();
		transaction.setCustomAnimations(R.anim.essense_bottom_in, 0);
		transaction.add(fragment, SHARETAG);
		transaction.commit();
	}

	@Override
	public void removeShare() {
		// TODO Auto-generated method stub
		FragmentManager manager = getFragmentManager();
		EssenseShareFragment fragment = (EssenseShareFragment) manager
				.findFragmentByTag(SHARETAG);
		if (null == fragment)
			return;
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.setCustomAnimations(0, R.anim.essense_bottom_out);
		transaction.remove(fragment);
		transaction.commit();
	}

	// *************init*************
	private void init() {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		EssenseFragment essenseFragment = new EssenseFragment();
		transaction.replace(R.id.FrameLayout1, essenseFragment);
		transaction.commit();
	}

	// NetCall:PullEssenseDetail
	@Override
	public void pullEDSuccess(EssenseDetail ed) {
		// TODO Auto-generated method stub
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		EssenseDetailFragment detailFragment = new EssenseDetailFragment(ed);
		transaction.replace(R.id.FrameLayout1, detailFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// NetCall:PullEssenseDetail
	@Override
	public void pullEDFail() {
		// TODO Auto-generated method stub

	}

}
