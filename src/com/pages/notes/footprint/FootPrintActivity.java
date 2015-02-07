package com.pages.notes.footprint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.model.UserConfigs;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TabWidget;
import android.widget.TextView;

public class FootPrintActivity extends FragmentActivity{

	private TabWidget mTabWidget;
	 ViewPager viewPager;
	 List<String> dates;
	 String tableName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_footprint);
		//tableName=getIntent().getStringExtra("tableName");
		dates=new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String date = sdf.format(calendar.getTime());
		dates.add(date);
		int gapDays=getDateGapDays(UserConfigs.getStartDay(), date);
		for(int i=1;i<gapDays;i++)
		{
			calendar.roll(Calendar.DAY_OF_YEAR,-1);
			String tempDate=sdf.format(calendar.getTime());
			dates.add(tempDate);
		}
		for(String d:dates)
		{
			Log.e(DataConstants.TAG,d);
		}
		mTabWidget=(TabWidget)findViewById(R.id.tabWidget);
		viewPager=(ViewPager)findViewById(R.id.footprint_viewpager);
		SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		
		List<Fragment> fraList=new ArrayList<Fragment>();
		TextView tv=null;
		for(int i=0;i<dates.size();i++)
		{
			tv=new TextView(this);
			tv.setWidth(DataConstants.screenWidth/3);
			tv.setText(dates.get(i));
			mTabWidget.addView(tv);
			tv.setOnClickListener(new TabClickListener(i));
			fraList.add(new FootPrintFragment());
		}
		viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fraList));
		viewPager.setOnPageChangeListener(mPageChangeListener);
		viewPager.setCurrentItem(3);
		mTabWidget.setCurrentTab(3);
	}
	class TabClickListener implements OnClickListener
	{

		int index;
		
		public TabClickListener(int index) {
			super();
			this.index = index;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			viewPager.setCurrentItem(index);
		}
		
	}
	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0)
        {
            mTabWidget.setCurrentTab(arg0);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {

        }

        @Override
        public void onPageScrollStateChanged(int arg0)
        {

        }
    };
	public class MyViewPagerAdapter extends FragmentPagerAdapter{
		
		List<Fragment> fragmentList;
		public MyViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
			super(fm);
			fragmentList=list;
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
	}
	public int getDateGapDays (String beginDate, String endDate)
	{       
			int gapDays=0;
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	        try { 
	                Date date = sdf.parse(endDate);// 通过日期格式的parse()方法将字符串转换成日期              
	                Date dateBegin = sdf.parse(beginDate);
	                long betweenTime = date.getTime() - dateBegin.getTime(); 
	                gapDays = (int)(betweenTime  / 1000 / 60 / 60 / 24); 
	             } catch(Exception e)
	             {
	              }
	        return (int)gapDays; 
	}

		
}
