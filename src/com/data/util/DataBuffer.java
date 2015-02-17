package com.data.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;
import android.util.LruCache;
import android.widget.BaseAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class DataBuffer<D> {
	private static final String TAG = "DataBuffer";
	private final RequestQueue requestQueue;
	private final BaseAdapter adapter;
	private static final int ITEMPERSCREEN = 10;// 控制屏幕的“长度”
	private static final int TRIGGER = 1;
	private static final int TIMES = 3;

	// 刷新databuffer需要初始化的数据
	private int items_number;
	private int coefficient;// for pre-data
	private LruCache<Integer, Object> cache;

	private NetUtil<D> nu;

	public DataBuffer(BaseAdapter adapter, NetUtil<D> nu) {
		if (null == nu) {
			items_number = 0;
		} else {
			items_number = TRIGGER;
			this.nu = nu;
		}
		requestQueue = GloableData.requestQueue;
		this.adapter = adapter;

		cache = new LruCache<Integer, Object>(TIMES * ITEMPERSCREEN);
		coefficient = 0;
	}

	public void destroy() {
		requestQueue.cancelAll(nu);
		cache = null;
	}

	public void reset() {
		destroy();
		cache = new LruCache<Integer, Object>(TIMES * ITEMPERSCREEN);
		coefficient = 0;
		items_number = TRIGGER;
		adapter.notifyDataSetChanged();
	}

	public int getCount() {
		return items_number;
	}

	@SuppressWarnings("unchecked")
	public D getDataItem(int index) {
		Object aim = cache.get(index);
		if (null == aim) {
			// send request:
			sendRequest(index);
			return null;
		}
		return (D) aim;
	}

	// *********************util*********************

	private void sendRequest(final int index) {
		final int page = index / ITEMPERSCREEN + 1;
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, nu.getURL(page, ITEMPERSCREEN), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						List<D> list = new ArrayList<D>();
						try {
							list = nu.parseToVG(response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							// 如果获取数据失败，直接返回。
							Log.i(TAG, "登陆验证失败");
							Log.i(TAG, nu.getURL(page, ITEMPERSCREEN));
						}
						freshDataSet(index, list);
						// notify the adapter
						adapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Log.i(TAG,
								"net request error: "
										+ nu.getURL(page, ITEMPERSCREEN));
					}
				});
		jsonObjectRequest.setTag(nu);
		requestQueue.add(jsonObjectRequest);
	}

	private void freshDataSet(final int index, List<D> list) {
		// put into the cache
		final int page = index / ITEMPERSCREEN + 1;
		int base = (page - 1) * ITEMPERSCREEN;
		for (int i = 0; i < list.size(); i++) {
			cache.put(i + base, list.get(i));
		}
		// check the length whether needs grow
		if (index / ITEMPERSCREEN == coefficient) {
			if (list.size() < ITEMPERSCREEN) {
				items_number = coefficient * ITEMPERSCREEN + list.size();
			} else {
				coefficient++;
				items_number = coefficient * ITEMPERSCREEN + TRIGGER;
			}
		}
	}
}
