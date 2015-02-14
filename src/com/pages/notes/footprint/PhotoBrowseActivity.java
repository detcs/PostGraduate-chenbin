package com.pages.notes.footprint;

import java.util.ArrayList;
import java.util.List;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.view.util.BitmapUtil;
import com.view.util.DragImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PhotoBrowseActivity extends Activity{

	private List<View> contents;
	private List<DragImageView> images;
	private ViewPager viewPager;
	int windowWidth=DataConstants.screenWidth;
	int windowHeight=DataConstants.screenHeight;
	int stateHeight=20;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_browse);
		contents = new ArrayList<View>();
		images = new ArrayList<DragImageView>();
		View content;
		DragImageView dragImageView;
		
		Bitmap bmp = BitmapUtil.ReadBitmapById(this, R.drawable.today_background,windowWidth, windowHeight);
		for (int i = 0; i < 3; i++) {
			content = LayoutInflater.from(this).inflate(R.layout.content_layout, null);
			dragImageView = (DragImageView) content.findViewById(R.id.div_main);
			dragImageView.setImageBitmap(bmp);
			dragImageView.setmActivity(this);// ע��Activity.
			dragImageView.setBumpHeight(300);
			dragImageView.setScreen_H(windowHeight-stateHeight);
			dragImageView.setScreen_W(windowWidth);
			images.add(dragImageView);
			contents.add(content);
		}
		viewPager=(ViewPager) findViewById(R.id.browse_viewpager);
		viewPager.setAdapter(new PagerAdapter() {


			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return contents.size();
			}

			@Override
			public void destroyItem(View container, int position,
					Object object) {
				((ViewPager)container).removeView(contents.get(position));
			}

			@Override
			public int getItemPosition(Object object) {

				return super.getItemPosition(object);
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(contents.get(position));
				return contents.get(position);
			}

			@Override
			public void finishUpdate(View arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Parcelable saveState() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void startUpdate(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	


}
