package com.pages.funsquare.essence;

import com.app.ydd.R;
import com.data.model.Essense;
import com.data.model.EssenseDetail;
import com.data.util.GloableData;
import com.data.util.NetCall;
import com.data.util.NetCall.DownloadSource;
import com.data.util.SysCall;
import com.pages.funsquare.essence.EmailHintDialog.EmailHintDialogCallBack;
import com.pages.funsquare.essence.EssenseEmailSetBump.ESBumpCallback;
import com.pages.funsquare.essence.EssenseShareBump.ShareBumpCallback;
import com.pages.funsquare.essence.ShareHintDialog.ShareHintCallBack;
import com.view.util.EssenseAdapter;
import com.view.util.EssenseAdapter.ListDownEssense;
import com.view.util.EssenseAdapter.ViewHolder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TextView;

public class EssenseFragment extends Fragment implements ShareHintCallBack,
		EmailHintDialogCallBack, ListDownEssense, DownloadSource,
		ShareBumpCallback, ESBumpCallback {
	// private static final String TAG = "EssenseFragment";
	private static final String TAG = "bump";
	private View base;
	private FrameLayout frame;

	private static final String[] TABS = new String[] { "最新", "资讯", "资料", "真题" };
	private static final int[] typeArray = new int[] { GloableData.TYPE_NEW,
			GloableData.TYPE_MATERIAL, GloableData.TYPE_INFORMATION,
			GloableData.TYPE_EXERCISE };
	private ListView[] listViews;
	private EssenseAdapter[] adapters;
	private int[] listViewsId;

	private EssenseJump jump;
	private View rootView;
	private ImageView backImg;
	private ImageView queryImg;
	private TabHost tabhost;
	private boolean isInit = true;

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
		}
		initVariable(rootView);
		frame.addView(rootView);
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

	@Override
	public void onStop() {
		for (int i = 0; i < 3; i++) {
			adapters[i].destroy();
		}
		super.onStop();
	}

	// *********init variable*********

	private void initVariable(View view) {
		listViewsId = new int[] { R.id.listView1, R.id.listView2,
				R.id.listView3, R.id.listView4 };
		listViews = new ListView[4];
		findViews(view);
		if (isInit) {
			initTabHost();
			initListViews();
		}
		isInit = false;
		setListener();
	}

	private void findViews(View view) {
		backImg = (ImageView) view.findViewById(R.id.backImg);
		queryImg = (ImageView) view.findViewById(R.id.queryImg);
		tabhost = (TabHost) view.findViewById(R.id.tabhost);
		for (int i = 0; i < 4; i++) {
			listViews[i] = (ListView) view.findViewById(listViewsId[i]);
		}
	}

	private void initListViews() {
		Context context = getActivity().getBaseContext();
		adapters = new EssenseAdapter[4];
		for (int i = 0; i < 4; i++) {
			adapters[i] = new EssenseAdapter(context, typeArray[i], this);
			listViews[i].setAdapter(adapters[i]);
		}
	}

	@SuppressWarnings("deprecation")
	private void initTabHost() {
		// Call setup() before adding tabs if loading TabHost using
		// findViewById(). However: You do not need to call setup() after
		// getTabHost() in TabActivity.
		tabhost.setup();
		int[] tabsId = { R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4 };
		for (int i = 0; i < 4; i++) {
			tabhost.addTab(tabhost.newTabSpec(TABS[i]).setIndicator(TABS[i])
					.setContent(tabsId[i]));
		}
		for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
			View view = tabhost.getTabWidget().getChildAt(i);
			// // view.setBackgroundColor(R.color.essense_tabhost_background);
			TextView textView = (TextView) view
					.findViewById(android.R.id.title);
			// textView.setTextSize(20);
			// textView.setBackgroundColor(R.color.essense_tabhost_background);
			// textView.setTextColor(R.color.essense_tabhost_word);
			textView.setGravity(Gravity.CENTER);
			textView.getLayoutParams().height = LayoutParams.FILL_PARENT;
			textView.getLayoutParams().width = LayoutParams.FILL_PARENT;
			// ImageView image = (ImageView)
			// view.findViewById(android.R.id.icon);
			// // image.setBackgroundColor(R.color.essense_tabhost_background);
			// image.setBackgroundResource(R.drawable.default_head);// userful
		}
	}

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
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				jump(view);
			}
		};
		for (int i = 0; i < 4; i++) {
			listViews[i].setOnItemClickListener(listener);
		}
	}

	private void jump(View view) {
		ViewHolder holder = (ViewHolder) view.getTag();
		String id = holder.id;
		jump.detail(id);
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
}
