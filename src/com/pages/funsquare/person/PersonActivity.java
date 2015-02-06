package com.pages.funsquare.person;

import com.app.ydd.R;
import com.data.util.SysCall;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class PersonActivity extends Activity implements PersonJump {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
		init();
	}

	private void init() {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		PersonCenterFragment fragment = new PersonCenterFragment();
		transaction.add(R.id.FrameLayout1, fragment);
		transaction.commit();
	}

	// PersonJump

	@Override
	public void infoSet() {
		// TODO Auto-generated method stub
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		PersonInfoFragment fragment = new PersonInfoFragment();
		transaction.replace(R.id.FrameLayout1, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void pwMonitor() {
		// TODO Auto-generated method stub
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		SysCall.error("judge update");
		// PersonPwFragment fragment = new PersonPwFragment();
		PersonPwThirdFragment fragment = new PersonPwThirdFragment();
		transaction.replace(R.id.FrameLayout1, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void myReserve() {
		// TODO Auto-generated method stub
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		PersonMyreserveFragment fragment = new PersonMyreserveFragment();
		transaction.replace(R.id.FrameLayout1, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
