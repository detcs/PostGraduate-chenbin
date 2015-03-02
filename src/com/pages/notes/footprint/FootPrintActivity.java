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
import com.data.util.DateUtil;
import com.data.util.DisplayUtil;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabWidget;
import android.widget.TextView;

public class FootPrintActivity extends FragmentActivity{

	private TabWidget mTabWidget;
	 ViewPager viewPager;
	 List<String> dates;
	 String tableName;
	 List<TextView> tabTvList;
	 HorizontalScrollView tabScrollView;
	 int tabWidth=DataConstants.screenWidth/3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_footprint);
		RelativeLayout titleLayout=(RelativeLayout) findViewById(R.id.footprint_title);
		titleLayout.setBackground(DisplayUtil.drawableTransfer(getApplicationContext(), R.drawable.register_title));
		//tableName=getIntent().getStringExtra("tableName");
		dates=new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String date = sdf.format(calendar.getTime());
		dates.add(date);
		Log.e(DataConstants.TAG, "startday "+UserConfigs.getStartDay());
		int gapDays=getDateGapDays(UserConfigs.getStartDay(), date);
		for(int i=0;i<gapDays;i++)
		{
			calendar.add(Calendar.DAY_OF_YEAR,-1);
			String tempDate=sdf.format(calendar.getTime());
			dates.add(tempDate);
		}
		for(String d:dates)
		{
			Log.e(DataConstants.TAG,d);
		}
		ImageView firstUseBg=(ImageView) findViewById(R.id.footprint_first_use_bg);
		if(dates.size()==0)
		{
			Log.e(DataConstants.TAG, "datessize==0");
			Picasso.with(getApplicationContext()).load(R.drawable.footprint_first_use).resize(400, 600).into(firstUseBg);
			firstUseBg.setVisibility(View.VISIBLE);
		}
		else
		{
			firstUseBg.setVisibility(View.INVISIBLE);
			mTabWidget=(TabWidget)findViewById(R.id.tabWidget);
			tabScrollView=(HorizontalScrollView) findViewById(R.id.tab_scrollview);
			viewPager=(ViewPager)findViewById(R.id.footprint_viewpager);
			SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
			
			List<Fragment> fraList=new ArrayList<Fragment>();
			View tabView;
			TextView tv=null;
			
			tabTvList=new ArrayList<TextView>();
			int gapDay=0;
			for(int i=0;i<dates.size();i++)
			{
				tabView=LayoutInflater.from(this).inflate(R.layout.view_footprint_tab, null);
				tv=(TextView) tabView.findViewById(R.id.tab_tv);
				tv.setWidth(tabWidth);
				tv.setTextColor(Color.parseColor("#333333"));
				tv.setTypeface(DataConstants.typeFZLT);
				//Log.e(DataConstants.TAG, dates.get(i)+" -- "+DateUtil.getAgoDateStringBefore(DateUtil.getTodayDateString(), 1));
				if(dates.get(i).equals(DateUtil.getAgoDateStringBefore(DateUtil.getTodayDateString(), 1)))
				{
					tv.setText(getResources().getString(R.string.yesyterday));
				}
				else
				{
					gapDay=DateUtil.getDateGapDays(UserConfigs.getStartDay(), dates.get(i));
					tv.setText(getResources().getString(R.string.footprint_my)
							+" "+gapDay+" "
							+getResources().getString(R.string.footprint_day));
				}
				tabTvList.add(tv);
				mTabWidget.addView(tabView);
				tv.setOnClickListener(new TabClickListener(i));
				FootPrintFragment fpFrag=new FootPrintFragment();
				Bundle b=new Bundle();
				b.putString("footprint_date", dates.get(i));
				fpFrag.setArguments(b);
				fraList.add(fpFrag);
			}
			viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fraList));
			viewPager.setOnPageChangeListener(mPageChangeListener);
			viewPager.setCurrentItem(0);
			mTabWidget.setCurrentTab(0);
		}
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
			setSelectedTabColor(index);
			
		}
		
	}
	private void setSelectedTabColor(final int index)
	{
		for(int i=0;i<tabTvList.size();i++)
		{
			TextView tv=tabTvList.get(i);
			if(i==index)
			{
				
				tv.setTextColor(Color.parseColor("#1f76dc"));
			}
			else
			{
				tv.setTextColor(Color.parseColor("#333333"));
			}
			
		}
		tabScrollView.post(new Runnable() {
			public void run() {
				if(index>0)
					tabScrollView.scrollTo(tabWidth*(index-1), tabScrollView.getScrollY());
			}
		});
	}
	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0)
        {
            mTabWidget.setCurrentTab(arg0);
			setSelectedTabColor(arg0);
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

