package com.pages.funsquare.person;

import com.app.ydd.R;
import com.data.util.SysCall;
import com.view.util.AnimationUtil;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class PersonInfoFragment extends Fragment {
	private View base;
	private FrameLayout frame;
	private static final String TAG = "bump";

	private View rootView;
	private View backView;
	private View saveView;
	private View imageView1;// head img

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == base) {
			base = inflater.inflate(R.layout.frame, container, false);
			frame = (FrameLayout) base.findViewById(R.id.FrameLayout1);
		}
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_person_info,
					container, false);
		}
		frame.addView(rootView);
		init(rootView);
		return base;
	}

	private void init(View view) {
		backView = view.findViewById(R.id.backView);
		saveView = view.findViewById(R.id.saveView);
		imageView1 = view.findViewById(R.id.imageView1);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		saveView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.error("save info");
			}
		});
		imageView1.setOnLongClickListener(new OnLongClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				View pickView = frame.findViewWithTag(TAG);
				if (null == pickView) {
					pickView = LayoutInflater.from(getActivity()).inflate(
							R.layout.fragment_person_headset, null);
					pickView.setTag(TAG);
					initHeadSet(pickView);
					frame.addView(pickView);
					pickView.startAnimation(AnimationUtil.showAnimation());
				}
				return true;
			}
		});
	}

	// ******************init head set******************
	private View takeView, album, quitView;

	private void initHeadSet(final View view) {
		takeView = view.findViewById(R.id.button1);
		album = view.findViewById(R.id.button2);
		quitView = view.findViewById(R.id.button3);
		takeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.error("take photo");
			}
		});
		album.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.error("pick from album");
			}
		});
		quitView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.startAnimation(AnimationUtil.hideAnimation());
				frame.removeView(view);
			}
		});
	}
}
