package com.view.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.data.model.DataConstants;
import com.data.util.DisplayUtil;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class DataViewUtil {
	private static final int TEXTSIZE = 12;
	private static final int YEARWIDTH = 130;

	public static void setPickerTextSize(DatePicker datePicker) {
		View dateView = null;
		Field[] fields = DatePicker.class.getDeclaredFields();
		Field[] fields2 = null;
		int i = 0;
		// 获取DatePicker中的属性
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getType().getSimpleName().equals("NumberPicker")) {
				try {
					dateView = (View) field.get(datePicker);
					if (2 == i) {
						dateView.measure(0, 0);
						dateView.getLayoutParams().width = YEARWIDTH;
					}
					i++;
					if (dateView != null) {
						fields2 = dateView.getClass().getDeclaredFields();
						for (Field field2 : fields2) {
							field2.setAccessible(true);
							if (field2.getType().getName()
									.equals(EditText.class.getName())) {
								try {
									EditText v_edit3 = (EditText) field2
											.get(dateView);
									if (v_edit3 != null) {
										v_edit3.setTextSize(TEXTSIZE);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void resizePikcer(FrameLayout tp) {
		List<NumberPicker> npList = findNumberPicker(tp);
		for (NumberPicker np : npList) {
			resizeNumberPicker(np);
		}
	}

	private static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;
		if (null != viewGroup) {
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker) {
					npList.add((NumberPicker) child);
				} else if (child instanceof LinearLayout) {
					List<NumberPicker> result = findNumberPicker((ViewGroup) child);
					if (result.size() > 0) {
						return result;
					}
				}
			}
		}
		return npList;
	}

	private static void resizeNumberPicker(NumberPicker np) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dipTopx(20, DataConstants.displayMetricsDensity),
				LayoutParams.WRAP_CONTENT);
		params.setMargins(3, 0, 3, 0);
		np.setLayoutParams(params);
	}
}
