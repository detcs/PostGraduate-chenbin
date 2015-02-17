package com.pages.notes.footprint;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.app.ydd.R;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.ImportanceFlagOnClickListener;
import com.data.model.UserConfigs;
import com.data.model.DataConstants.PageName;
import com.data.util.PhotoNameTableInfo;
import com.data.util.SysCall;
import com.squareup.picasso.Picasso;
import com.view.util.BitmapUtil;
import com.view.util.DragImageView;
import com.view.util.TextContentShowUtil;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhotoBrowseActivity extends Activity{

	private List<View> contents;
	private List<DragImageView> images;
	private ViewPager viewPager;
	LinearLayout bottom;
	ImageView flurImg;
	ImageView importantImg;
	ImageView rotateImg;
	ImageView remarkImg;
	boolean importantFlag=false;
	boolean rotateFlag=false;
	boolean remarkFlag=false;
	int windowWidth=DataConstants.screenWidth;
	int windowHeight=DataConstants.screenHeight;
	int stateHeight=20;
	//ArrayList<String> paths;
	ArrayList<PhotoNameTableInfo> photoInfos;
	ArrayList<TextContentShowUtil> textUtils;
	int currentIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_browse);
		Bundle bundle=getIntent().getBundleExtra("photo_show_bundle");
		String date=bundle.getString("date");
		String tag=bundle.getString("jump_tag");
		SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		//Log.e(DataConstants.TAG, "tag "+tag);
		//paths=new ArrayList<String>();
		photoInfos=new ArrayList<PhotoNameTableInfo>();
		textUtils=new ArrayList<TextContentShowUtil>();
		PhotoNameTableInfo photoInfo=null;
		if(tag.equals(getResources().getString(R.string.jump_tag_footprint)))
		{
			HashMap<String, String> map=UserConfigs.getCourseNameAndTableMap();
			
			for(String course:map.keySet())
			{
				
				String table=map.get(course);
				List<String> result=DataConstants.dbHelper.queryPhotoNamesAtDate(getApplicationContext(), db, table, date);
				String dir=DataConstants.TABLE_DIR_MAP.get(table);
				String path;
				for(String name:result)
				{
					path=FileDataHandler.APP_DIR_PATH+"/"+dir+"/"+name;
					//paths.add(path);
					photoInfo=new PhotoNameTableInfo(name, table, path);
					photoInfos.add(photoInfo);
				}
			}
		}
		
		//Log.e(DataConstants.TAG, "path size "+paths.size());
			
		//paths=getIntent().getStringArrayListExtra(getResources().getString(R.string.jump_tag_footprint));
		contents = new ArrayList<View>();
		images = new ArrayList<DragImageView>();
		
		
		View content;
		DragImageView dragImageView;
		Bitmap bmp=null;
		BitmapFactory.Options opt=new BitmapFactory.Options();
		opt.inSampleSize=4;
		//Bitmap bmp = BitmapUtil.ReadBitmapById(this, R.drawable.today_background,windowWidth, windowHeight);
		for (int i = 0; i < photoInfos.size(); i++) {
			content = LayoutInflater.from(this).inflate(R.layout.content_layout, null);
			initRemarkEditView(content);
			CourseRecordInfo cri= DataConstants.dbHelper.queryCourseRecordByPhotoName(getApplicationContext(), db, photoInfos.get(i).getTableName(), photoInfos.get(i).getPhotoName());
			String text=cri.getRemark();
			bottom=(LinearLayout) content.findViewById(R.id.browse_bottom);
			
			TextContentShowUtil tcs=new TextContentShowUtil(getApplicationContext(),
					text);
			bottom.addView(tcs.getTextContentView());
			textUtils.add(tcs);
			bmp=BitmapFactory.decodeFile(photoInfos.get(i).getPath(), opt);
			flurImg=(ImageView) content.findViewById(R.id.blur_img);
			String blurPath=FileDataHandler.photoPathToBlurPath(photoInfos.get(i).getPath());
			Picasso.with(getApplicationContext()).load(new File(blurPath)).into(flurImg);
			
			importantImg=(ImageView) content.findViewById(R.id.important_img);
			
			
			
			if(cri.getFlag()==1)
				Picasso.with(getApplicationContext()).load(R.drawable.notespec_importance_click).into(importantImg);
			else
				Picasso.with(getApplicationContext()).load(R.drawable.notespec_importance).into(importantImg);
			boolean currentFlag=cri.getFlag()==1?true:false;
			importantImg.setOnClickListener(new ImportanceFlagOnClickListener(photoInfos.get(i).getPhotoName(),photoInfos.get(i).getTableName(), this,importantImg,currentFlag,PageName.NoteSpec));
			
			rotateImg=(ImageView) content.findViewById(R.id.rotate_img);
			Picasso.with(getApplicationContext()).load(R.drawable.notespec_rotate).into(rotateImg);
			
			remarkImg=(ImageView) content.findViewById(R.id.remark_img);
			Picasso.with(getApplicationContext()).load(R.drawable.notespec_remark).into(remarkImg);
//			remarkImg.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					if(remarkFlag)
//					{
//						Picasso.with(getApplicationContext()).load(R.drawable.notespec_remark).into(remarkImg);
//						remarkFlag=!remarkFlag;
//					}
//					else
//					{
//						Picasso.with(getApplicationContext()).load(R.drawable.notespec_remark_click).into(remarkImg);
//						remarkFlag=!remarkFlag;
//						
//					}
//				}
//			});
			dragImageView = (DragImageView) content.findViewById(R.id.div_main);
			dragImageView.setImageBitmap(bmp);
			dragImageView.setmActivity(this);// ע��Activity.
			dragImageView.setLayout(content);
			dragImageView.setBumpHeight(300);
			dragImageView.setScreen_H(windowHeight-stateHeight);
			dragImageView.setScreen_W(windowWidth);
			images.add(dragImageView);
			contents.add(content);
		}
		db.close();
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
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currentIndex=arg0;
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
		
	}
	private void initRemarkEditView(View rootView)
	{
		//View rootView = LayoutInflater.from(this).inflate(R.layout.content_layout, null);

		final LinearLayout editRemarkLayout=(LinearLayout)rootView.findViewById(R.id.browse_remark_edit_hideBar);
		final EditText editRemark = (EditText) rootView.findViewById(R.id.browse_remark_edit);
		TextView cancelEdit=(TextView)rootView.findViewById(R.id.browse_remark_edit_quitView);
		TextView saveEdit=(TextView)rootView.findViewById(R.id.browse_remark_edit_saveView);
		
		ImageView editRemarkImg=(ImageView)rootView.findViewById(R.id.remark_img);
		editRemarkImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editRemarkLayout.setVisibility(View.VISIBLE);
				SysCall.bumpSoftInput(editRemark,PhotoBrowseActivity.this);
			}
		});
		cancelEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(editRemarkLayout.getVisibility()==View.VISIBLE)
				{
					editRemarkLayout.setVisibility(View.INVISIBLE);
					SysCall.hideSoftInput(editRemarkLayout, PhotoBrowseActivity.this);
					editRemark.clearFocus();
					editRemark.setFocusable(false);
					editRemark.setFocusableInTouchMode(false);
				}
			}
		});
		saveEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editRemarkLayout.setVisibility(View.INVISIBLE);
				SysCall.hideSoftInput(editRemarkLayout, PhotoBrowseActivity.this);
				editRemark.clearFocus();
				editRemark.setFocusable(false);
				editRemark.setFocusableInTouchMode(false);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				String date = sdf.format(calendar.getTime());
				SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				DataConstants.dbHelper.updateCourseRecordOnStringColByPhotoName(PhotoBrowseActivity.this, db, photoInfos.get(currentIndex).getTableName(),getResources().getString(R.string.dbcol_remark), editRemark.getText().toString(), photoInfos.get(currentIndex).getPhotoName());
				textUtils.get(currentIndex).setTextContent(editRemark.getText().toString());
				//remarkContext.setText(editRemark.getText().toString());
				db.close();
			}
		});
	}
}
	
	
	

