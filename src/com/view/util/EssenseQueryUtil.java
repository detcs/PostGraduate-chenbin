package com.view.util;

import com.data.util.ComputeURL;
import com.data.util.EssenseUtil;
import com.data.util.GloableData;

public class EssenseQueryUtil extends EssenseUtil {
	private String key;

	public void search(String key) {
		this.key = key;
	}

	public EssenseQueryUtil(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getURL(int page, int limit) {
		// TODO Auto-generated method stub
		return ComputeURL.getEssenseListURL(page, limit,
				GloableData.TYPE_SEARCH) + "&key=" + key;
	}
}
