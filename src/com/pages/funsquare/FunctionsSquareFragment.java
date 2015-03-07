package com.pages.funsquare;

import java.util.ArrayList;
import java.util.List;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.util.DisplayUtil;
import com.pages.viewpager.MainActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FunctionsSquareFragment extends Fragment{

	GridView funcGridView;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_funcs_gird, container, false);
		ImageView bg=(ImageView) rootView.findViewById(R.id.funcs_bg_img);
		BitmapFactory.Options opt=new BitmapFactory.Options();
		//opt.inJustDecodeBounds=true;
		opt.inSampleSize=10;
		//Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.today_background, opt);
		//Log.e(DataConstants.TAG,"null?"+(bitmap==null));
		//bitmap=DisplayUtil.doBlur(bitmap, 10, false);
		bg.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.today_background));
		initFunctionsSquareView(rootView);
		return rootView;
	}
	public void initFunctionsSquareView(View v) {

		GridView gv = (GridView) v.findViewById(R.id.funcs_grid);
		List<String> names = new ArrayList<String>();
		names.add(getResources().getString(R.string.essence));
		names.add(getResources().getString(R.string.square));
		names.add(getResources().getString(R.string.backup));
		names.add(getResources().getString(R.string.personal_center));
		names.add(getResources().getString(R.string.vip));
		names.add(getResources().getString(R.string.print));
		names.add(getResources().getString(R.string.contact_us));
		gv.setAdapter(new ButtonsGridViewAdapter(names, getActivity()));
	}
}
