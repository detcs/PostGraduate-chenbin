package com.pages.notes;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.model.UserConfigs;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ClockFragment extends Fragment {

	TextView clockDays;
	Button clock;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_clock, container, false);
		clockDays=(TextView)rootView.findViewById(R.id.clock_days);
		clockDays.setText(getResources().getString(R.string.clock)+"1"+getResources().getString(R.string.day));
		clock=(Button)rootView.findViewById(R.id.clock);
		clock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//UserConfigs.storeClockDay();
				FragmentManager fm=getActivity().getFragmentManager();
//				Fragment f=fm.findFragmentByTag(getResources().getString(R.string.reviewchoose_fra_tag));
//				Log.e(DataConstants.TAG,"f null "+(f==null));
//				fm.popBackStackImmediate(getResources().getString(R.string.reviewchoose_fra_tag),fm.POP_BACK_STACK_INCLUSIVE);
//				FragmentTransaction trans = fm.beginTransaction();  
				getActivity().finish();
			}
		});
		return rootView;
	}
}

