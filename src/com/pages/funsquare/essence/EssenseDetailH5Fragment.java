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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

public class EssenseDetailH5Fragment extends Fragment implements
		ReserveCallback, ShareBumpCallback {
	private static final String TAG = "EssenseDetailH5Fragment";
	private View base;
	private FrameLayout frame;
	private View rootView;
	private View backBu, reserveView, reserveText, shareView, shareText;
	private WebView webView1;
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
		setListener();
		setWebView();
	}

	private void findViews(View view) {
		backBu = view.findViewById(R.id.backBu);
		reserveView = view.findViewById(R.id.reserveView);
		reserveText = view.findViewById(R.id.reserveText);
		shareText = view.findViewById(R.id.shareText);
		shareView = view.findViewById(R.id.shareView);
		webView1 = (WebView) view.findViewById(R.id.webView1);
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
		reserveText.setOnClickListener(reserveListener);
		reserveView.setOnClickListener(reserveListener);
		OnClickListener shareListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rootView.clearFocus();
				new EssenseShareBump(frame, getActivity(), TAG,
						EssenseDetailH5Fragment.this).show();
			}
		};
		shareText.setOnClickListener(shareListener);
		shareView.setOnClickListener(shareListener);
	}

	private void setWebView() {
		webView1.loadUrl(ed.getUrl_());
		webView1.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	// NetCall.ReserveCallback
	@Override
	public void requestSuccess() {
		// TODO Auto-generated method stub
		ed.setIsCollected_((ed.getIsCollected_() + 1) % 2);
		SysCall.error("change the share image");
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
