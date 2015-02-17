package com.pages.funsquare.essence;

import com.app.ydd.R;
import com.data.model.EssenseDetail;
import com.data.util.NetCall.DownloadSource;
import com.data.util.GloableData;
import com.data.util.NetCall;
import com.data.util.NetCall.ReserveCallback;
import com.data.util.SysCall;
import com.pages.funsquare.essence.EmailHintDialog.EmailHintDialogCallBack;
import com.pages.funsquare.essence.EssenseEmailSetBump.ESBumpCallback;
import com.pages.funsquare.essence.EssenseShareBump.ShareBumpCallback;
import com.pages.funsquare.essence.ShareHintDialog.ShareHintCallBack;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EssenseDetailFragment extends Fragment implements DownloadSource,
		EmailHintDialogCallBack, ShareHintCallBack, ShareBumpCallback,
		ESBumpCallback, ReserveCallback {
	private static final String TAG = "EssenseDetailFragment";
	private View base;
	private FrameLayout frame;

	private View rootView;
	private EssenseDetail ed;

	private ImageView backBu;
	private TextView titleView;
	private TextView genderView;
	private TextView sizeView;
	private TextView visitView;
	private TextView downView;
	private TextView authorView;
	private ImageView reserveView;
	private ImageView shareView;
	private Button downBu;

	public EssenseDetailFragment(EssenseDetail ed) {
		this.ed = ed;
	}

	public EssenseDetailFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == base) {
			base = inflater.inflate(R.layout.frame, container, false);
		}
		frame = (FrameLayout) base.findViewById(R.id.FrameLayout1);
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_essense_detail,
					container, false);
		}
		init(rootView);
		frame.addView(rootView);
		return base;
	}

	private void init(View view) {
		findViews(view);
		initViews();
		setListener();
		initDownBu();
	}

	private void findViews(View view) {
		backBu = (ImageView) view.findViewById(R.id.backBu);
		titleView = (TextView) view.findViewById(R.id.titleView);
		genderView = (TextView) view.findViewById(R.id.genderView);
		sizeView = (TextView) view.findViewById(R.id.sizeView);
		visitView = (TextView) view.findViewById(R.id.visitView);
		downView = (TextView) view.findViewById(R.id.downView);
		authorView = (TextView) view.findViewById(R.id.authorView);
		reserveView = (ImageView) view.findViewById(R.id.reserveView);
		shareView = (ImageView) view.findViewById(R.id.shareView);
	}

	private void initViews() {
		// SysCall.error("init views");
		if (GloableData.RESERVE_ENSURE == ed.getIsCollected_()) {
			shareView.setImageBitmap(null);
		}
		titleView.setText("政治大神最新考研。。。");
		genderView.setText("视屏");
		sizeView.setText("1.26M");
		visitView.setText("765次");
		downView.setText("586次");
		authorView.setText("王小丫");
	}

	private void setListener() {
		backBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		reserveView.setOnClickListener(new OnClickListener() {

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
				NetCall.reserve(id, type, EssenseDetailFragment.this);
			}
		});
		shareView.setOnClickListener(new OnClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null == frame.findViewWithTag(TAG)) {
					rootView.clearFocus();
					new EssenseShareBump(frame, getActivity(), TAG,
							EssenseDetailFragment.this).show();
				}
			}
		});
	}

	private void initDownBu() {
		if (EssenseDetail.HASRESOURCE == ed.getHasDownload_()) {

			if (GloableData.hasSetEmail()) {
				downBu.setText("下载至" + GloableData.getEmail());
			}
			downBu = (Button) rootView.findViewById(R.id.downBu);
			downBu.setVisibility(View.VISIBLE);
			downBu.setClickable(true);
			downBu.setEnabled(true);
			downBu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					download();
				}
			});
		}
	}

	// ***********************download***********************
	private void download() {
		// 如果没有资源文件，则不会执行到这里
		if (!GloableData.hasSetEmail()) {
			// 检验邮箱是否已经设置
			new EmailHintDialog(getActivity(), this).show();
			return;
		}
		if (EssenseDetail.NEEDSHARE == ed.getNeedShare_()) {
			new ShareHintDialog(getActivity(), this).show();
			return;
		}
		NetCall.download(ed.getId(), "314784088@qq.com", ed.getResid_(), ""
				+ ed.getNeedShare_(), this);
	}

	// EmailHintDialogCallBack

	@Override
	public void ensure() {
		// TODO Auto-generated method stub
		if (null == frame.findViewWithTag(TAG)) {
			new EssenseEmailSetBump(frame, getActivity(), TAG, this).show();
		}
	}

	// ShareHintCallBack
	@Override
	public void ensureShare() {
		// TODO Auto-generated method stub
		if (null == frame.findViewWithTag(TAG)) {
			new EssenseShareBump(frame, getActivity(), TAG, this).show();
		}
	}

	// NetCall.DownloadSource
	@SuppressLint("ShowToast")
	@Override
	public void downloadSuccess() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "已发送至您的邮箱", 500);
	}

	@SuppressLint("ShowToast")
	@Override
	public void downloadFail() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "下载失败", 500);
	}

	// EssenseShareBump.ShareBumpCallback
	@SuppressLint("ShowToast")
	@Override
	public void shareSuccess() {
		// TODO Auto-generated method stub
		// 分享
		Toast.makeText(getActivity(), "分享成功", 500);
		download();
	}

	@SuppressLint("ShowToast")
	@Override
	public void shareFail() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "分享失败，请重新分享", 500);
	}

	@SuppressLint("ShowToast")
	@Override
	public void setSuccess() {
		// TODO Auto-generated method stub
		// Toast.makeText(getActivity(), "邮箱设置成功", 500);
		download();
	}

	// NetCall.ReserveCallback
	@Override
	public void requestSuccess() {
		// TODO Auto-generated method stub
		ed.setIsCollected_((ed.getIsCollected_() + 1) % 2);
		SysCall.error("change the share image");
		// shareView.setImageBitmap(null);
	}

	@SuppressLint("ShowToast")
	@Override
	public void requestFail() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "操作失败", 500);
	}
}
