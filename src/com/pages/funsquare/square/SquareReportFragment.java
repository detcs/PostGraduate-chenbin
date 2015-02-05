package com.pages.funsquare.square;

import com.app.ydd.R;
import com.data.model.Post;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class SquareReportFragment extends Fragment {
	private View rootView;
	private SquareJump jump;
	private Post vg;

	private Button reportBu;
	private Button quitBu;

	public SquareReportFragment(Post vg) {
		this.vg = vg;
	}
	public SquareReportFragment() {
	
	}

	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		jump = (SquareJump) activity;
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_square_report,
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
		reportBu = (Button) view.findViewById(R.id.reportBu);
		quitBu = (Button) view.findViewById(R.id.quitBu);
	}

	private void setListener() {
		reportBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// report
			}
		});
		OnClickListener quitListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jump.removeReport();
			}
		};
		quitBu.setOnClickListener(quitListener);
		rootView.setOnClickListener(quitListener);
	}
}
