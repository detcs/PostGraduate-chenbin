package com.data.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetWorkUtil {

	public static boolean isConnected(Context context)
	{
		if(isNetworkConnected(context) || isWifiConnected(context))
			return true;
		return false;
	}
	public static boolean isNetworkConnected(Context context) { 
		if (context != null) 
		{ 
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
			.getSystemService(Context.CONNECTIVITY_SERVICE); 
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
			if (mNetworkInfo != null) 
			{ 
				return mNetworkInfo.isAvailable(); 
			} 
		} 
			return false; 
		}
	
	public static boolean isWifiConnected(Context context) { 
		if (context != null) { 
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
			.getSystemService(Context.CONNECTIVITY_SERVICE); 
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager 
			.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
			if (mWiFiNetworkInfo != null) 
			{ 
				return mWiFiNetworkInfo.isAvailable(); 
			} 
		} 
		return false; 
		}
	public static void showNoNetwork(Context context)
	{
		Toast.makeText(context, "no network", Toast.LENGTH_LONG).show();
	}
	
}
