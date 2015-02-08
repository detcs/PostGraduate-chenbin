package com.data.util;

import java.util.List;

import org.json.JSONObject;

public class ReserveUtil implements NetUtil<Reserve> {

	@Override
	public String getURL(int page, int limit) {
		// TODO Auto-generated method stub
		return ComputeURL.getReserveListURL();
	}

	@Override
	public List<Reserve> parseToVG(JSONObject response) throws Exception {
		// TODO Auto-generated method stub
		return Json_util.transToReserve(response);
	}

}
