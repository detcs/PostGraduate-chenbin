package com.pages.funsquare.print;

import com.app.ydd.R;
import com.data.util.GloableData;
import com.data.util.SysCall;
import com.pages.funsquare.essence.EmailHintDialog;
import com.pages.funsquare.essence.EssenseEmailSetBump;
import com.pages.funsquare.essence.EmailHintDialog.EmailHintDialogCallBack;
import com.pages.funsquare.essence.EssenseEmailSetBump.ESBumpCallback;
import com.pages.funsquare.reserve.PrintChooseContent;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class PrintActivity extends Activity implements EmailHintDialogCallBack,
		ESBumpCallback {
	private static final String TAG = "PrintActivity";
	private FrameLayout frame;

	private View rootView;
	private View backBu;
	private Button button1;
	private boolean needReserve;
	private FrameLayout contentFrame;
	private PrintChooseContent pcc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame);
		frame = (FrameLayout) findViewById(R.id.FrameLayout1);
		rootView = LayoutInflater.from(this).inflate(R.layout.activity_print,
				frame, false);
		frame.addView(rootView);
		init(rootView);
	}

	private void init(View view) {
		// set needReserve
		needReserve = false;
		findViews(view);
		initSet();
		setListener();
	}

	private void findViews(View view) {
		backBu = view.findViewById(R.id.backBu);
		button1 = (Button) view.findViewById(R.id.button1);
		contentFrame = (FrameLayout) view.findViewById(R.id.frame);
	}

	private void initSet() {
		// choose frame content
		View content;
		if (needReserve) {
			// not reserve
			content = LayoutInflater.from(this).inflate(
					R.layout.print_reserve_hint, null);
		} else {
			// has reserve
			pcc = new PrintChooseContent(this);
			content = pcc.getView();
		}
		contentFrame.addView(content);
		// set button text
	}

	private void setListener() {
		backBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (needReserve) {
					backup();
				} else {
					print();
				}
			}
		});
	}

	private void backup() {
		// upload exercises
	}

	private void print() {
		if (!GloableData.hasSetEmail()) {
			// 检验邮箱是否已经设置
			new EmailHintDialog(this, this).show();
			return;
		}
		// print net interface
	}

	// EmailHintDialog.EmailHintDialogCallBack

	@Override
	public void ensure() {
		// TODO Auto-generated method stub
		if (null == frame.findViewWithTag(TAG)) {
			new EssenseEmailSetBump(frame, this, TAG, this).show();
		}
	}

	// EssenseEmailSetBump.ESBumpCallback
	@Override
	public void setSuccess() {
		// TODO Auto-generated method stub
		button1.setText("发送打印文件至" + GloableData.getEmail());
	}
}
