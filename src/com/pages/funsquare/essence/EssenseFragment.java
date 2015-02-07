package com.pages.funsquare.essence;

import com.app.ydd.R;
import com.data.model.Essense;
import com.data.model.EssenseDetail;
import com.data.util.GloableData;
import com.data.util.NetCall;
import com.data.util.NetCall.DownloadSource;
import com.data.util.SysCall;
import com.pages.funsquare.essence.EmailHintDialog.EmailHintDialogCallBack;
import com.pages.funsquare.essence.EssenseActivity.MyOnTouchListener;
import com.pages.funsquare.essence.EssenseEmailSetBump.ESBumpCallback;
import com.pages.funsquare.essence.EssenseShareBump.ShareBumpCallback;
import com.pages.funsquare.essence.ShareHintDialog.ShareHintCallBack;
import com.view.util.AnimationUtil;
import com.view.util.EssenseAdapter;
import com.view.util.EssenseAdapter.ListDownEssense;
import com.view.util.EssenseAdapter.ViewHolder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EssenseFragment extends Fragment implements ShareHintCallBack,
		EmailHintDialogCallBack, ListDownEssense, DownloadSource,
		ShareBumpCallback, ESBumpCallback, OnGestureListener {
	// private static final String TAG = "EssenseFragment";
	private static final String TAG = "bump";
	private View base;
	private FrameLayout frame;

	private FrameLayout content;
	private static final int[] typeArray = new int[] { GloableData.TYPE_NEW,
			GloableData.TYPE_MATERIAL, GloableData.TYPE_INFORMATION,
			GloableData.TYPE_EXERCISE };
	private TextView[] tabs;
	private int[] tabsId = { R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4 };
	private EssenseAdapter[] adapters;
	private ListView[] lists;
	private EssenseJump jump;
	private View rootView;
	private ImageView backImg;
	private ImageView queryImg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == base) {
			base = inflater.inflate(R.layout.frame, container, false);
		}
		frame = (FrameLayout) base.findViewById(R.id.FrameLayout1);
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_essense, container,
					false);
			initVariable(rootView);
			frame.addView(rootView);
		}
		((EssenseActivity) getActivity())
				.registerMyOnTouchListener(myOnTouchListener);
		return base;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof EssenseJump)) {
			throw new IllegalStateException("error");
		}
		jump = (EssenseJump) activity;
	}

	// *********init variable*********

	private void initVariable(View view) {
		tabs = new TextView[4];
		lists = new ListView[4];
		adapters = new EssenseAdapter[4];
		findViews(view);
		initContent();
		setListener();
	}

	private void findViews(View view) {
		for (int i = 0; i < 4; i++) {
			tabs[i] = (TextView) view.findViewById(tabsId[i]);
			lists[i] = (ListView) LayoutInflater.from(getActivity()).inflate(
					R.layout.fragment_essense_flip_listview, null);
			adapters[i] = new EssenseAdapter(getActivity(), typeArray[i], this);
			lists[i].setAdapter(adapters[i]);
		}
		backImg = (ImageView) view.findViewById(R.id.backImg);
		queryImg = (ImageView) view.findViewById(R.id.queryImg);
		content = (FrameLayout) view.findViewById(R.id.content);
	}

	private void initContent() {
		content.addView(lists[0]);
	}

	// tab状态记录
	private int preTab = 0;

	private void setListener() {
		backImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		queryImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jump.query();
			}
		});
		OnClickListener tabsListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int newTab = 0;
				for (int i = 0; i < 4; i++) {
					if (tabs[i].equals(v)) {
						newTab = i;
						Log.i(TAG, "new tab: " + i);
					}
				}
				flip(newTab);
			}
		};
		for (int i = 0; i < 4; i++) {
			tabs[i].setOnClickListener(tabsListener);
		}
		OnItemClickListener itemListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ViewHolder holder = (ViewHolder) view.getTag();
				String essid = holder.id;
				jump.detail(essid);
			}
		};
		for (int i = 0; i < 4; i++) {
			lists[i].setOnItemClickListener(itemListener);
		}
	}

	private void flip(int newTab) {
		if (newTab != preTab) {
			// newTab:for move animation; result:for result record
			int result = (newTab + 4) % 4;
			lists[preTab].startAnimation(AnimationUtil.getHideAnim(preTab,
					newTab));
			content.removeView(lists[preTab]);

			content.addView(lists[result]);
			lists[result].startAnimation(AnimationUtil.getShowAnim(preTab,
					newTab));
			preTab = result;
		}
	}

	private Essense ed;// 用户希望下载的那个

	// EssenseAdapter.ListDownEssense
	@Override
	public void down(Essense ed) {
		// TODO Auto-generated method stub
		// 如果没有资源文件，则不会执行到这里
		this.ed = ed;
		if (EssenseDetail.NEEDSHARE == ed.getNeedShare_()) {
			new ShareHintDialog(getActivity(), this).show();
			return;
		}
		if (!GloableData.hasSetEmail()) {
			// 检验邮箱是否已经设置
			new EmailHintDialog(getActivity(), this).show();
		} else {
			NetCall.download(ed.getId(), "314784088@qq.com", ed.getResid_(), ""
					+ ed.getNeedShare_(), this);
		}
	}

	// ShareHintDialog.ShareHintCallBack
	@Override
	public void ensureShare() {
		// TODO Auto-generated method stub
		if (null == frame.findViewWithTag(TAG)) {
			new EssenseShareBump(frame, getActivity(), TAG, this).show();
		}
	}

	// EmailHintDialog.EmailHintDialogCallBack

	@Override
	public void ensure() {
		// TODO Auto-generated method stub
		if (null == frame.findViewWithTag(TAG)) {
			new EssenseEmailSetBump(frame, getActivity(), TAG, this).show();
		}
	}

	// NetCall.DownloadSource
	@Override
	public void downloadSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadFail() {
		// TODO Auto-generated method stub

	}

	// EssenseShareBump.ShareBumpCallback
	@Override
	public void shareSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void shareFail() {
		// TODO Auto-generated method stub

	}

	// EssenseEmailSetBump.ESBumpCallback
	@Override
	public void setFail() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSuccess() {
		// TODO Auto-generated method stub

	}

	// **************gesture listener**************

	private MyOnTouchListener myOnTouchListener = new MyOnTouchListener() {
		@Override
		public boolean onTouch(MotionEvent ev) {
			boolean result = detector.onTouchEvent(ev);
			return result;
		}
	};
	private GestureDetector detector = new GestureDetector(getActivity(), this);

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	private static final int verticalMinDistance = 500;
	private static final int minVelocity = 0;

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {
			// left
			flip(preTab + 1);
		} else if (e2.getX() - e1.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {
			// right
			flip(preTab - 1);
		}
		return false;
	}
}
