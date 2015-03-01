package com.pages.funsquare.print;

import java.util.List;

import com.app.ydd.R;
import com.data.model.UserConfigs;
import com.view.util.DataViewUtil;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

public class PrintChooseContent {
	private View view;
	private Activity activity;
	private TextView[] fonts;
	private static final int[] fontsId = { R.id.textView1, R.id.textView6,
			R.id.textView7, R.id.textView8 };
	private TextView[] subs;
	private static final int[] subsId = { R.id.sub1, R.id.sub2, R.id.sub3,
			R.id.sub4 };
	private DatePicker fromdata, todata;

	private int chooseSub;
	private int f_y, f_m, f_d, t_y, t_m, t_d;

	public PrintChooseContent(Activity activity) {
		this.activity = activity;
		if (null == view) {
			view = LayoutInflater.from(activity).inflate(
					R.layout.print_options, null);
			init(view);
		}
	}

	public View getView() {
		return view;
	}

	private void init(View view) {
		chooseSub = -1;
		findViews(view);
		initSet();
		setListener();
	}

	private void findViews(View view) {
		subs = new TextView[4];
		for (int i = 0; i < 4; i++) {
			subs[i] = (TextView) view.findViewById(subsId[i]);
		}
		Typeface face = Typeface.createFromAsset(activity.getAssets(),
				"font/fangzhenglanting.ttf");
		fonts = new TextView[4];
		for (int i = 0; i < 4; i++) {
			fonts[i] = (TextView) view.findViewById(fontsId[i]);
			fonts[i].setTypeface(face);
		}

		fromdata = (DatePicker) view.findViewById(R.id.fromdata);
		todata = (DatePicker) view.findViewById(R.id.todata);
		DataViewUtil.resizePikcer(fromdata);
		DataViewUtil.resizePikcer(todata);
		DataViewUtil.setPickerTextSize(fromdata);
		DataViewUtil.setPickerTextSize(todata);
	}

	private void initSet() {
		// set subs
		List<String> subStrs = UserConfigs.getCourseNames();
		for (int i = 0; i < 4; i++) {
			// subs[i].setText(subStrs.get(i).charAt(0)+"");
			subs[i].setText("T");
		}
		// set times
	}

	private void setListener() {
		OnClickListener subListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < 4; i++) {
					if (v == subs[i]) {
						// change sub background
						chooseSub = i;
						subs[i].setTextColor(0xff7abb74);
						subs[i].setBackground(activity.getResources()
								.getDrawable(R.drawable.print_sub_down));
					} else {
						// change other subs background
						subs[i].setTextColor(0xffffffff);
						subs[i].setBackground(activity.getResources()
								.getDrawable(R.drawable.print_sub));
					}
				}
			}
		};
		for (int i = 0; i < 4; i++) {
			subs[i].setOnClickListener(subListener);
		}
		OnDateChangedListener datalist = new OnDateChangedListener() {

			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				if (view == fromdata) {
					f_y = year;
					f_m = monthOfYear;
					f_d = dayOfMonth;
				} else {
					t_y = year;
					t_m = monthOfYear;
					t_d = dayOfMonth;
				}
			}
		};
		fromdata.init(f_y, f_m, f_d, datalist);
		todata.init(t_y, t_m, t_d, datalist);
	}

	public int getF_y() {
		return f_y;
	}

	public int getF_m() {
		return f_m;
	}

	public int getF_d() {
		return f_d;
	}

	public int getT_y() {
		return t_y;
	}

	public int getT_m() {
		return t_m;
	}

	public int getT_d() {
		return t_d;
	}

	public int getChooseSub() {
		return chooseSub;
	}
}
