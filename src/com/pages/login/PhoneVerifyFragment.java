package com.pages.login;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
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
import com.data.util.DisplayUtil;
import com.data.util.GloableData;
import com.pages.notes.footprint.FootprintInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.view.util.FragmentActionListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PhoneVerifyFragment extends Fragment {

	private FragmentActionListener mListener = null;
	EditText phone;
	TextView obtainVerifyNum; 
	EditText verifyNum;
	TextView nextStep;
	private TimeCount time;
	@Override
	public void onAttach(Activity activity) {
		try {
			mListener = (FragmentActionListener) activity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onAttach(activity);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView=inflater.inflate(R.layout.fragment_phone_verify, null);
		phone=(EditText)rootView.findViewById(R.id.phone);
		verifyNum=(EditText)rootView.findViewById(R.id.verify_num);
		time = new TimeCount(60000, 1000);//构造CountDownTimer对象
		obtainVerifyNum=(TextView)rootView.findViewById(R.id.get_verify_num);
		obtainVerifyNum.setTextColor(Color.parseColor("#333333"));
		//obtainVerifyNum.setBackgroundResource();
		obtainVerifyNum.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.obtain_verifynum_init));
		obtainVerifyNum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				requestVerifyNum(getObtainVeifyNumURL(phone.getText().toString()));
				time.start();
//				GetPhoneVerifyNum gpv=new GetPhoneVerifyNum();
//				 gpv.execute(DataConstants.SERVER_URL);
				obtainVerifyNum.setTextColor(Color.parseColor("#ffffff"));
				obtainVerifyNum.setBackgroundResource(R.drawable.obtain_verifynum_normal);
			}
		});
		nextStep=(TextView)rootView.findViewById(R.id.next_step);
		nextStep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				if(verifyNum.getText().toString().equals(arg0))
//				mListener.switchToNext();
				nextStep.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.register_complete_click));
				requestIsVerifyNumRight(getIsVeifyNumRightURL(phone.getText().toString(), verifyNum.getText().toString()));
			}
		});
		return rootView;
	}
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
			obtainVerifyNum.setText("重新验证");
			obtainVerifyNum.setClickable(true);
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
			obtainVerifyNum.setClickable(false);
			obtainVerifyNum.setText(millisUntilFinished /1000+getResources().getString(R.string.resend_verify_num));
		}
		}
	private void requestVerifyNum(String url) {
		//RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.e(DataConstants.TAG, "requestVerifyNum response=" + response);
						//parseFirstPageInfo(response, date);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// tv_1.setText(arg0.toString());
						Log.i(DataConstants.TAG,
								"sorry,Error" + arg0.toString());
					}
				});
		GloableData.requestQueue.add(jsonObjectRequest);
	}
	private String getObtainVeifyNumURL(String phone) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MGetMobileVerify");
		params.add(pair);
		pair = new BasicNameValuePair("device", "android");
		params.add(pair);
		pair = new BasicNameValuePair("deviceid", "1");
		params.add(pair);
		pair = new BasicNameValuePair("appid", "nju");
		params.add(pair);
		pair = new BasicNameValuePair("phone", phone);
		params.add(pair);
		
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";

		}
		Log.e(DataConstants.TAG, "getObtainVeifyNum:" + resultURL);
		return resultURL;
	}
	private boolean requestIsVerifyNumRight(String url) {
		//RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.e(DataConstants.TAG, "IsVerifyNumRight response=" + response);
						//parseFirstPageInfo(response, date);
						try {
							String errorCode=response.getString("errorCode");
							if(errorCode.equals("0"))
							{
								mListener.switchToNext();
								JSONObject data=response.getJSONObject("data");
								String verify=data.getString("verify_");
								String userid=data.getString("id_");
								String account=data.getString("account_");
								UserConfigs.storeVerify(verify);
								UserConfigs.storeId(userid);
								UserConfigs.storeAccount(account);
								UserConfigs.storeLoginWay(getResources().getString(R.string.user_phone));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// tv_1.setText(arg0.toString());
						Log.i(DataConstants.TAG,
								"sorry,Error" + arg0.toString());
					}
				});
		GloableData.requestQueue.add(jsonObjectRequest);
		return false;
	}
	private String getIsVeifyNumRightURL(String phone,String code) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MRegister");
		params.add(pair);
		pair = new BasicNameValuePair("device", "android");
		params.add(pair);
		pair = new BasicNameValuePair("deviceid", "1");
		params.add(pair);
//		pair = new BasicNameValuePair("appid", "nju");
//		params.add(pair);
		pair = new BasicNameValuePair("phone", phone);
		params.add(pair);
		pair = new BasicNameValuePair("code", code);
		params.add(pair);
		
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";

		}
		Log.e(DataConstants.TAG, "getIsVeifyNumRight:" + resultURL);
		return resultURL;
	}
	public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();

    }
//	//获取手机验证码
//		class GetPhoneVerifyNum extends AsyncTask<String, Integer, String>
//		{
//
//			@Override
//			protected String doInBackground(String... url) {
//				// TODO Auto-generated method stub
//				List<NameValuePair> params=new ArrayList<NameValuePair>();
//				BasicNameValuePair pair = new BasicNameValuePair("methodno","MGetMobileVerify");
//				params.add(pair);
//				pair = new BasicNameValuePair("deviceid","1");
//				params.add(pair);
//				pair = new BasicNameValuePair("appid","nju");
//				params.add(pair);
//				pair = new BasicNameValuePair("phone",phone.getText().toString());
//				params.add(pair);
//				
//				String result = null;
//				try {
//					
//					//Log.i("com.market","PublishTask begin");
//					//Log.i("com.market","PublishTask imgurl "+itemInfo.getImgUrl());
//					result = doPost(params, url[0]);
//				
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return result;
//			}
//			
//		}
//		public static String doPost(List<NameValuePair> params,String url) throws ClientProtocolException, IOException
//		{
//			String result = null;
//			String paramStr="";
//			for(NameValuePair nvp:params)
//			{
//				paramStr+=nvp.getName()+"="+nvp.getValue()+"&";
//			}
//			//url+="?"+paramStr;
//			//Log.i(DataConstants.TAG,url+paramStr);
//			HttpPost httpPost=new HttpPost(url); 
//			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
//			HttpResponse response=new DefaultHttpClient().execute(httpPost);
//			//Log.i(DataConstants.TAG,"code:"+response.getStatusLine().getStatusCode());
//			if(response.getStatusLine().getStatusCode()==200)
//			{
//				HttpEntity entity=response.getEntity(); 
//				result=EntityUtils.toString(entity, HTTP.UTF_8);
//			}
//			//Log.i("com.market",result);
//			return result;
//			
//		}
}

