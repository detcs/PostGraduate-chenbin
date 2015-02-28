package com.pages.notes.todayrec;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.model.UserConfigs;
import com.pages.notes.footprint.FootprintInfo;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TodayRecommenderActivity extends Activity{

	TextView titleBack;
	TextView titleCenter;
	TextView titleRight;

	ImageView recImg;
	TextView recSrc;
	TextView recIndex;
	TextView recContent;
	TextView collect;
	List<TodayRecommenderInfo> todayRecList;
	List<View> recViews;
	ViewPager viewPager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_rec);
		initTitleView();
		//recImg=(ImageView) findViewById(R.id.todayrec_img);
		recSrc=(TextView) findViewById(R.id.rec_src);
		recIndex=(TextView) findViewById(R.id.rec_index);
		recContent=(TextView) findViewById(R.id.rec_content);
		collect=(TextView) findViewById(R.id.collect_btn);
		recViews=new ArrayList<View>();
		ImageView img;
		View view;
		for (int i = 0; i <3;i++)
		{
			view=LayoutInflater.from(this).inflate(R.layout.view_today_rec, null);
			img=(ImageView) view.findViewById(R.id.rec_img);
			//Picasso.with(getApplicationContext()).load(R.drawable.note_thumb).into(img);
			img.setImageResource(R.drawable.note_thumb);
			recViews.add(view);
		}
		viewPager=(ViewPager) findViewById(R.id.today_rec_viewpager);
		viewPager.setAdapter(new PagerAdapter() {
			
			@Override
			public int getItemPosition(Object object) {

				return super.getItemPosition(object);
			}
			@Override
			public void startUpdate(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Parcelable saveState() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				// TODO Auto-generated method stub
				((ViewPager)container).addView(recViews.get(position));
				return recViews.get(position);
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return recViews.size();
			}
			
			@Override
			public void finishUpdate(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void destroyItem(View container, int position, Object arg2) {
				// TODO Auto-generated method stub
				((ViewPager)container).removeView(recViews.get(position));
			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				recIndex.setText((position+1)+"");
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		//requestTodayRecommenderInfo(getGetTodayRecommenderURL());
	}
	private void initTitleView()
	{
		titleBack=(TextView) findViewById(R.id.todayrec_back);
		titleBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		titleCenter=(TextView) findViewById(R.id.todayrec_title_center);
		titleRight=(TextView) findViewById(R.id.todayrec_title_right);
		
	}
	private void requestTodayRecommenderInfo(String url) {
		final FootprintInfo fpInfo;
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.e(DataConstants.TAG, "TodayRecommender response=" + response);
						todayRecList=parseTodayRecommender(response);
						recContent.setText(todayRecList.get(0).getRemark());
						Picasso.with(getApplicationContext()).load(DataConstants.DOWNLOAD_URL+todayRecList.get(0).getImgId()).into(recImg);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// tv_1.setText(arg0.toString());
						Log.i(DataConstants.TAG,
								"sorry,Error" + arg0.toString());
						// if (progressDialog.isShowing()
						// && progressDialog != null) {
						// progressDialog.dismiss();
						// }
					}
				});
		requestQueue.add(jsonObjectRequest);
		// return fpInfo;
	}

	private String getGetTodayRecommenderURL() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MQuesRecommend");
		params.add(pair);
		pair = new BasicNameValuePair("device", "android");
		params.add(pair);
		pair = new BasicNameValuePair("deviceid", "1");
		params.add(pair);
		pair = new BasicNameValuePair("appid", "nju");
		params.add(pair);
		pair = new BasicNameValuePair("userid", UserConfigs.getId());
		params.add(pair);
		pair = new BasicNameValuePair("verify", UserConfigs.getVerify());
		params.add(pair);
		//pair = new BasicNameValuePair("date", date);
		//params.add(pair);
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";

		}
		Log.e(DataConstants.TAG, "fpage:" + resultURL);
		return resultURL;
	}
	private List<TodayRecommenderInfo> parseTodayRecommender(JSONObject json)
	{
		List<TodayRecommenderInfo> list=new ArrayList<TodayRecommenderInfo>();
		TodayRecommenderInfo info;
		try {
			JSONObject data = json.getJSONObject("data");
			JSONArray recLists = data.getJSONArray("list_");
			for(int i=0;i<recLists.length();i++)
			{
				JSONObject obj=recLists.getJSONObject(i);
				String id=obj.getString("id_");
				String imgid=obj.getString("img_");
				String remark=obj.getString("remark_");
				String type=obj.getString("type_");
				String displayName=obj.getString("displayName_");
				String subject=obj.getString("subject_");
				String isAd=obj.getString("isAd_");
				boolean ifAd= isAd.equals("1")?true:false;
				info=new TodayRecommenderInfo(id, imgid, remark, subject, type, displayName, ifAd);
				list.add(info);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
