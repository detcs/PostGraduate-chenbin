package com.pages.funsquare.person;

import java.io.File;

import com.app.ydd.R;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.data.util.ComputeURL;
import com.data.util.ImageUtil;
import com.data.util.SysCall;
import com.squareup.picasso.Picasso;
import com.view.util.AnimationUtil;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonCenterFragment extends Fragment {
	private PersonJump jump;
	private View base;
	private FrameLayout frame;
	private TextView nameText, idText, textView6;// textView6:email
	private static final String TAG = "bump";
	private static final String ROOT = "ROOT";
	// private static final String[] TAGS = { "fragment_person_email_set",
	// "fragment_person_logout" };

	private View rootView;
	private TextView emailChangeView;
	private View infoView, pwChangeView, myreserve, checknew, aboutus,
			logoutBu, imageView1;// imageView1_back
	private ImageView imageView2;

	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		jump = (PersonJump) activity;
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == base) {
			base = inflater.inflate(R.layout.frame, container, false);
			frame = (FrameLayout) base.findViewById(R.id.FrameLayout1);
		}
		if (null == frame.findViewWithTag(ROOT)) {
			if (null == rootView) {
				rootView = inflater.inflate(R.layout.fragment_person_center,
						null);
				init(rootView);
			}
			rootView.setTag(ROOT);
			frame.addView(rootView);
		}
		// frame.findViewWithTag(tag)
		return base;
	}

	private void init(View view) {
		findViews(view);
		initView();
		setListener();
	}

	private void findViews(View view) {
		nameText = (TextView) view.findViewById(R.id.nameText);
		idText = (TextView) view.findViewById(R.id.idText);
		textView6 = (TextView) view.findViewById(R.id.textView6);

		imageView2 = (ImageView) view.findViewById(R.id.imageView2);
		imageView1 = view.findViewById(R.id.imageView1);
		infoView = view.findViewById(R.id.infoView);
		emailChangeView = (TextView) view.findViewById(R.id.emailChangeView);
		pwChangeView = view.findViewById(R.id.pwChangeView);
		myreserve = view.findViewById(R.id.myreserve);
		checknew = view.findViewById(R.id.checknew);
		aboutus = view.findViewById(R.id.aboutus);
		logoutBu = view.findViewById(R.id.logoutBu);
	}

	private void initView() {
		String headPath=FileDataHandler.APP_DIR_PATH+"/"+getResources().getString(R.string.head_img);
		File headFile=new File(headPath);
		if(headFile.exists())
		{
			ImageUtil.imageLoader.displayImage(ImageUtil.filePre+headPath,imageView2);
		}
		else
		Picasso.with(getActivity())
				.load(ComputeURL.getHeadImgURL(UserConfigs.getHeadImg()))
				.error(R.drawable.person_center_boyhead).into(imageView2);
		
		nameText.setText(UserConfigs.getAccount());
		idText.setText("研大大ID：" + UserConfigs.getId());
		if (null == UserConfigs.getEmail() || UserConfigs.getEmail().equals("")) {
			emailChangeView.setText("设置");
		} else {
			textView6.setText(UserConfigs.getEmail());
		}
	}

	private void setListener() {
		// back
		imageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		infoView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// to personal info set
				jump.infoSet();
			}
		});
		emailChangeView.setOnClickListener(new OnClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// change the email
				View email = frame.findViewWithTag(TAG);
				if (null == email) {
					email = LayoutInflater.from(getActivity()).inflate(
							R.layout.fragment_email_set, null);
					email.setTag(TAG);
					frame.addView(email);
					email.startAnimation(AnimationUtil.showAnimation());
					initEmailSet(email);
				}
			}
		});
		pwChangeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// pw reset
				jump.pwMonitor();
			}
		});
		myreserve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// jump to reserve
				jump.myReserve();
			}
		});
		checknew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// check new
				SysCall.error("check update");
				new CheckNewDialog(getActivity()).show();
				new IsNewDialog(getActivity()).show();
			}
		});
		aboutus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// about us
				SysCall.error("about us");
			}
		});
		logoutBu.setOnClickListener(new OnClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// log out
				View logout = frame.findViewWithTag(TAG);
				if (null == logout) {
					logout = LayoutInflater.from(getActivity()).inflate(
							R.layout.fragment_person_logout, null);
					logout.setTag(TAG);
					frame.addView(logout);
					logout.startAnimation(AnimationUtil.showAnimation());
					initLogout(logout);
				}
			}
		});
	}

	// ****************log out****************
	private View logoutView_l, quitView_l;

	private void initLogout(final View view) {
		logoutView_l = view.findViewById(R.id.logoutView);
		quitView_l = view.findViewById(R.id.quitView);
		quitView_l.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.startAnimation(AnimationUtil.hideAnimation());
				frame.removeView(view);
			}
		});
		logoutView_l.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.error("log out");
			}
		});
	}

	// ****************email set****************
	private TextView quitView_e;
	private TextView saveView_e;
	private EditText editView_e;

	private void initEmailSet(final View view) {
		quitView_e = (TextView) view.findViewById(R.id.quitView);
		saveView_e = (TextView) view.findViewById(R.id.saveView);
		editView_e = (EditText) view.findViewById(R.id.editView);
		SysCall.bumpSoftInput(editView_e, getActivity());
		quitView_e.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.startAnimation(AnimationUtil.hideAnimation());
				frame.removeView(view);
			}
		});
		saveView_e.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.error("email set");
			}
		});
	}
}
