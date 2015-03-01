package com.data.util;

import java.util.List;

import org.json.JSONObject;

import com.data.model.Reserve;

public class ReserveUtil implements NetUtil<Reserve> {

	@Override
	public String getURL(int page, int limit) {
		// TODO Auto-generated method stub
		return ComputeURL.getReserveListURL(page, limit);
	}

	@Override
	public List<Reserve> parseToVG(JSONObject response) throws Exception {
		// TODO Auto-generated method stub
		return Json_util.transToReserve(response);
	}

}
