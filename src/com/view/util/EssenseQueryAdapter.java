package com.view.util;

import com.data.model.Essense;
import com.data.util.DataBuffer;
import com.data.util.GloableData;

import android.content.Context;

public class EssenseQueryAdapter extends EssenseAdapter {
	private EssenseQueryUtil util;

	public EssenseQueryAdapter(Context context, int dataType,
			ListDownEssense callbackin) {
		super(context, dataType, callbackin);
		// TODO Auto-generated constructor stub
	}

	protected void initVariable() {
		util = new EssenseQueryUtil(GloableData.TYPE_SEARCH);
		// init buffer
		buffer = new DataBuffer<Essense>(this, util);
	}

	public void search(String key) {
		util.search(key);
		buffer.reset();
	}
}
