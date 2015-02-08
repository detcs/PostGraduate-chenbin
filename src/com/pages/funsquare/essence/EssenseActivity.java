package com.pages.funsquare.essence;

import java.util.ArrayList;

import com.app.ydd.R;
import com.data.model.EssenseDetail;
import com.data.util.NetCall;
import com.data.util.NetCall.PullEssenseDetail;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MotionEvent;

public class EssenseActivity extends Activity implements EssenseJump,
		PullEssenseDetail {
	// private static final String TAG = "EssenseActivity";
	// private static final String SHARETAG = "EssenseShareFragment";
	private boolean detail_is_first = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_essense);
		String essId = getIntent().getStringExtra("essenseId");
		if (essId != null && !essId.trim().equals("")) {
			detail_is_first = true;
			detail(essId);
		} else {
			init();
		}
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
		if (!detail_is_first)
			transaction.addToBackStack(null);
		transaction.commit();
	}

	// NetCall:PullEssenseDetail
	@Override
	public void pullEDFail() {
		// TODO Auto-generated method stub

	}

	// ********************dispatche the event to fragment
	private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
			10);

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		for (MyOnTouchListener listener : onTouchListeners) {
			listener.onTouch(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		onTouchListeners.add(myOnTouchListener);
	}

	public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		onTouchListeners.remove(myOnTouchListener);
	}

	public interface MyOnTouchListener {
		public boolean onTouch(MotionEvent ev);
	}
}
