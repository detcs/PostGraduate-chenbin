package com.pages.funsquare.essence;

import com.app.ydd.R;
import com.data.model.EssenseDetail;
import com.data.util.GloableData;
import com.data.util.NetCall;
import com.data.util.NetCall.ReserveCallback;
import com.data.util.SysCall;
import com.pages.funsquare.essence.EssenseShareBump.ShareBumpCallback;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EssenseDetailH5Fragment extends Fragment implements
		ReserveCallback, ShareBumpCallback {
	private static final String TAG = "EssenseDetailH5Fragment";
	private View base;
	private FrameLayout frame;
	private View rootView;
	private View backBu, shareView;
	private ImageView backupView;
	private TextView titleText, timeText, contentText, moreEssView;
	private EssenseDetail ed;

	public EssenseDetailH5Fragment(EssenseDetail ed) {
		this.ed = ed;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == base) {
			base = inflater.inflate(R.layout.frame, container, false);
		}
		frame = (FrameLayout) base.findViewById(R.id.FrameLayout1);
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_essense_detail_h5,
					container, false);
			frame.addView(rootView);
			init(rootView);
		}
		return base;
	}

	private void init(View view) {
		findViews(view);
		setContent();
		setListener();
	}

	private void findViews(View view) {
		backBu = view.findViewById(R.id.backBu);
		backupView = (ImageView) view.findViewById(R.id.backupView);
		shareView = view.findViewById(R.id.shareView);
		titleText = (TextView) view.findViewById(R.id.titleText);
		timeText = (TextView) view.findViewById(R.id.timeText);
		contentText = (TextView) view.findViewById(R.id.contentText);
		moreEssView = (TextView) view.findViewById(R.id.moreEssView);

		Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
				"font/fangzhenglanting.ttf");

		moreEssView.setTypeface(face);
		titleText.setTypeface(face);
		timeText.setTypeface(face);
		contentText.setTypeface(face);
	}

	private void setContent() {
		titleText.setText(ed.getTitle());
		timeText.setText(ed.getTime());
		contentText.setText(ed.getContent_());
		if (EssenseDetail.ISCOLLECTED == ed.getIsCollected_()) {
			backupView.setImageDrawable(getActivity().getResources()
					.getDrawable(R.drawable.essense_backup_over));
		}
	}

	private void setListener() {
		backBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		OnClickListener reserveListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String id = ed.getId();
				int type;
				if (GloableData.RESERVE_ENSURE == ed.getIsCollected_()) {
					type = GloableData.RESERVE_QUIT;
				} else {
					type = GloableData.RESERVE_ENSURE;
				}
				NetCall.reserve(id, type, EssenseDetailH5Fragment.this);
			}
		};
		backupView.setOnClickListener(reserveListener);
		OnClickListener shareListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rootView.clearFocus();
				new EssenseShareBump(frame, getActivity(), TAG,
						EssenseDetailH5Fragment.this).show();
			}
		};
		shareView.setOnClickListener(shareListener);
	}

	// NetCall.ReserveCallback
	@Override
	public void requestSuccess() {
		// TODO Auto-generated method stub
		if ((ed.getIsCollected_() + 1) % 2 == GloableData.RESERVE_ENSURE) {
			backupView.setImageDrawable(getActivity().getResources()
					.getDrawable(R.drawable.essense_backup_over));
		} else {
			backupView.setImageDrawable(getActivity().getResources()
					.getDrawable(R.drawable.essense_backup));
		}
		ed.setIsCollected_((ed.getIsCollected_() + 1) % 2);
	}

	@SuppressLint("ShowToast")
	@Override
	public void requestFail() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "操作失败", 500);
	}

	// EssenseShareBump.ShareBumpCallback
	@SuppressLint("ShowToast")
	@Override
	public void shareSuccess() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "分享成功", 500);
	}

	@SuppressLint("ShowToast")
	@Override
	public void shareFail() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "分享失败，请重新分享", 500);
	}
}
