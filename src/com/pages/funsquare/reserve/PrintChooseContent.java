package com.pages.funsquare.reserve;

import com.app.ydd.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class PrintChooseContent {
	private View view;
	private View[] subs;
	private static final int[] subsId = { R.id.sub1, R.id.sub2, R.id.sub3,
			R.id.sub4 };
	private DatePicker fromdata, todata;

	private int chooseSub;
	private int f_y, f_m, f_d, t_y, t_m, t_d;

	public PrintChooseContent(Activity activity) {
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
		for (int i = 0; i < 4; i++) {
			subs[i] = view.findViewById(subsId[i]);
		}
		fromdata = (DatePicker) view.findViewById(R.id.fromdata);
		todata = (DatePicker) view.findViewById(R.id.todata);
	}

	private void initSet() {
		// set subs
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
					} else {
						// change other subs background
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
