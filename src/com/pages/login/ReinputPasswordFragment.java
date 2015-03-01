package com.pages.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.model.UserConfigs;
import com.data.util.DisplayUtil;
import com.data.util.GloableData;
import com.pages.viewpager.MainActivity;
import com.view.util.FragmentActionListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ReinputPasswordFragment extends Fragment{

	private FragmentActionListener mListener = null;
	Button completeResetPwd;
	EditText inputNewPwd;
	EditText reinputPwd;
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
		View rootView=inflater.inflate(R.layout.fragment_reinput_password, null);
		inputNewPwd=(EditText)rootView.findViewById(R.id.input_new_passward);
		reinputPwd=(EditText)rootView.findViewById(R.id.reinput_passward);
		completeResetPwd=(Button)rootView.findViewById(R.id.complete_reset_pwd);
		completeResetPwd.setBackground(DisplayUtil.drawableTransfer(getActivity(),R.drawable.register_complete_normal));
		completeResetPwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				completeResetPwd.setBackground(DisplayUtil.drawableTransfer(getActivity(),R.drawable.register_complete_click));
				completeResetPwd.setEnabled(false);
				requestPasswordChange(getPasswordChangeURL("", inputNewPwd.getText().toString(), ""));
			}
		});
		return rootView;
	}
	private void requestPasswordChange(String url) {
		//RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.e(DataConstants.TAG, "PasswordChange response=" + response);
						//parseFirstPageInfo(response, date);
						String errorCode;
						try {
							errorCode = response.getString("errorCode");
							if(errorCode.equals("0"))
							{
								Intent intent=new Intent();
								intent.setClass(getActivity(), MainActivity.class);
								getActivity().startActivity(intent);
								getActivity().finish();
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
	}
	private String getPasswordChangeURL(String nickname,String password,String sex) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair pair = new BasicNameValuePair("methodno", "MPasswdChange");
		params.add(pair);
		pair = new BasicNameValuePair("device", "android");
		params.add(pair);
		pair = new BasicNameValuePair("deviceid", "1");
		params.add(pair);
		pair = new BasicNameValuePair("appid", "nju");
		params.add(pair);
		pair = new BasicNameValuePair("nickname", nickname);
		params.add(pair);
		pair = new BasicNameValuePair("password", password);
		params.add(pair);
		pair = new BasicNameValuePair("sex",sex);
		params.add(pair);
		pair = new BasicNameValuePair("verify",UserConfigs.getVerify());
		params.add(pair);
		pair = new BasicNameValuePair("userid",UserConfigs.getId());
		params.add(pair);
		String resultURL = DataConstants.SERVER_URL + "?";
		for (NameValuePair nvp : params) {
			resultURL += nvp.getName() + "=" + nvp.getValue() + "&";
		}          
		Log.e(DataConstants.TAG, "fpage:" + resultURL);
		savePassword(password);
		return resultURL;
	}
	private void savePassword(String password) {
		// TODO Auto-generated method stub
		UserConfigs.storePassword(password);
	}
}
