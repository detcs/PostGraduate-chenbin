package com.pages.funsquare.essence;

import com.app.ydd.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class EssenseShareFragment extends Fragment {
	private View rootView;
	private EssenseJump jump;

	private ImageView imageView1;
	private ImageView imageView2;
	private Button button1;

	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		jump = (EssenseJump) activity;
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_essense_share,
					container, false);
		}
		init(rootView);
		return rootView;
	}

	private void init(View view) {
		findViews(view);
		setListener();
	}

	private void findViews(View view) {
		imageView1 = (ImageView) view.findViewById(R.id.imageView1);
		imageView2 = (ImageView) view.findViewById(R.id.imageView2);
		button1 = (Button) view.findViewById(R.id.button1);
	}

	private void setListener() {
		imageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 分享到朋友圈
			}
		});
		imageView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 分享到新浪微博
			}
		});
		OnClickListener outListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// quit
				jump.removeShare();
			}
		};
		button1.setOnClickListener(outListener);
		rootView.setOnClickListener(outListener);
	}
}
