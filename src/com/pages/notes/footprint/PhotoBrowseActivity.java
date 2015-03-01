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
import com.data.util.DateUtil;
import com.data.util.PhotoNamePathUtil;
import com.data.util.PhotoNameTableInfo;
import com.data.util.SysCall;
import com.squareup.picasso.Picasso;
import com.view.util.AnimationUtil;
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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhotoBrowseActivity extends Activity{

	private List<View> contents;
	private List<DragImageView> images;
	private ViewPager viewPager;
	PagerAdapter pageAdapter;
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
	//ArrayList<TextContentShowUtil> textUtils;
	//TextContentShowUtil textUtil;
	int currentIndex=0;
	SQLiteDatabase db=null;
	
	//text
	TextView remarkContent; 
	//edit 
	LinearLayout editRemarkLayout;
	EditText editRemark;
	TextView cancelEdit;
	TextView saveEdit;
	ImageView editRemarkImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_browse);
		Bundle bundle=getIntent().getBundleExtra("photo_show_bundle");
		String date=bundle.getString("date");
		String tag=bundle.getString("jump_tag");
		PageName fromPage=(PageName) bundle.get("from_page");
		String tableName=bundle.getString("table_name");
		db = DataConstants.dbHelper.getReadableDatabase();
		initTitleView();
		initBottomView();
		initRemarkEditView();
		photoInfos=new ArrayList<PhotoNameTableInfo>();
		//textUtils=new ArrayList<TextContentShowUtil>();
		PhotoNameTableInfo photoInfo=null;
		//if(tag.equals(getResources().getString(R.string.jump_tag_footprint)))// current date all course
		if(fromPage==PageName.NoteFootprint)
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
		else if(fromPage==PageName.NoteReviewChoose)// three days current course
		{
			String agoDate="";
			for(int i=0;i<3;i++)
			{
				agoDate=DateUtil.getAgoDateStringBefore(date, i);
				List<String> result=DataConstants.dbHelper.queryPhotoNamesAtDate(getApplicationContext(), db, tableName, agoDate);
				String dir=DataConstants.TABLE_DIR_MAP.get(tableName);
				String path;
				for(String name:result)
				{
					path=FileDataHandler.APP_DIR_PATH+"/"+dir+"/"+name;
					//paths.add(path);
					photoInfo=new PhotoNameTableInfo(name, tableName, path);
					photoInfos.add(photoInfo);
				}
			}
		}
		
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

			dragImageView = (DragImageView) content.findViewById(R.id.div_main);
			bmp=BitmapFactory.decodeFile(photoInfos.get(i).getPath(), opt);
			dragImageView.setImageBitmap(bmp);
			dragImageView.setmActivity(this);// ע��Activity.
			dragImageView.setLayout(content);
			dragImageView.setBumpHeight(300);
			dragImageView.setScreen_H(windowHeight-stateHeight);
			dragImageView.setScreen_W(windowWidth);
			images.add(dragImageView);
			contents.add(content);
		}
		
		viewPager=(ViewPager) findViewById(R.id.browse_viewpager);
		pageAdapter=new PagerAdapter() {


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
		};
		viewPager.setAdapter(pageAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currentIndex=arg0;
				
				String blurPath=FileDataHandler.photoPathToBlurPath(photoInfos.get(currentIndex).getPath());
				File blurFile=new File(blurPath);
				if(blurFile.exists())
					Picasso.with(getApplicationContext()).load(blurFile).into(flurImg);
				CourseRecordInfo cri= DataConstants.dbHelper.queryCourseRecordByPhotoName(getApplicationContext(), db, photoInfos.get(currentIndex).getTableName(), photoInfos.get(currentIndex).getPhotoName());
				String text=cri.getRemark();
				remarkContent.setText(text);
				//textUtil.setTextContent(text, true);
				
				if(cri.getFlag()==1)
					Picasso.with(getApplicationContext()).load(R.drawable.notespec_importance_click).into(importantImg);
				else
					Picasso.with(getApplicationContext()).load(R.drawable.notespec_importance).into(importantImg);
				boolean currentFlag=cri.getFlag()==1?true:false;
				importantImg.setOnClickListener(new ImportanceFlagOnClickListener(photoInfos.get(currentIndex).getPhotoName(),photoInfos.get(currentIndex).getTableName(), PhotoBrowseActivity.this,importantImg,currentFlag,PageName.NoteSpec));
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
	private void initTitleView()
	{
		TextView back=(TextView) findViewById(R.id.browse_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		final LinearLayout deleteLayout=(LinearLayout) findViewById(R.id.browse_bottom_delete);
		TextView rubbish=(TextView) findViewById(R.id.browse_delete);
		rubbish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				deleteLayout.setVisibility(View.VISIBLE);
				deleteLayout.startAnimation(AnimationUtil.showUpAnimation());
			}
		});
		TextView delete=(TextView)findViewById(R.id.browse_delete_bg);
		TextView deleteCancel=(TextView)findViewById(R.id.browse_delete_cancel_bg);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String photoname=photoInfos.get(currentIndex).getPhotoName();
				DataConstants.dbHelper.updateCourseRecordOnIntColByPhotoName(PhotoBrowseActivity.this, db,photoInfos.get(currentIndex).getTableName(), photoname, getResources().getString(R.string.dbcol_photo_delete), 1);
				contents.remove(currentIndex);
				pageAdapter.notifyDataSetChanged();
				viewPager.setCurrentItem(currentIndex);
				deleteLayout.startAnimation(AnimationUtil.hideDownAnimation());
				deleteLayout.setVisibility(View.INVISIBLE);
				//bgLayout.setAlpha(0);
			}
		});
		deleteCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				deleteLayout.startAnimation(AnimationUtil.hideDownAnimation());
				deleteLayout.setVisibility(View.INVISIBLE);
				//bglayout.setAlpha(255);
			}
		});
		
	}
	private void initBottomView()
	{
		ImageView textBg=(ImageView) findViewById(R.id.gradual_text_bg);
		Picasso.with(getApplicationContext()).load(R.drawable.gradual_text_bg).into(textBg);
		
		//bottom=(LinearLayout) findViewById(R.id.bottom_text_layout);
		
		//TextContentShowUtil tcs=new TextContentShowUtil(getApplicationContext(),"");
		//textUtil=new TextContentShowUtil(PhotoBrowseActivity.this,"");
		//FrameLayout.LayoutParams frameParam=new  FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		//View frame=textUtil.getTextContentView();
		//frame.setLayoutParams(param);
		//textUtil.getTextContentView();
		//textUtil.getTextView().setTextSize(12);
		//bottom.addView(frame);
		//textUtils.add(tcs);
		//bmp=BitmapFactory.decodeFile(photoInfos.get(i).getPath(), opt);
		flurImg=(ImageView)findViewById(R.id.blur_img);
		importantImg=(ImageView)findViewById(R.id.important_img);
		Picasso.with(getApplicationContext()).load(R.drawable.notespec_importance).into(importantImg);
		rotateImg=(ImageView)findViewById(R.id.rotate_img);
		Picasso.with(getApplicationContext()).load(R.drawable.notespec_rotate).into(rotateImg);
		remarkImg=(ImageView)findViewById(R.id.remark_img);
		Picasso.with(getApplicationContext()).load(R.drawable.notespec_remark).into(remarkImg);
	}
	private void initRemarkEditView()
	{
		remarkContent=(TextView)findViewById(R.id.text_remark_content);
		TextView extendBtn=(TextView)findViewById(R.id.extends_img);
		remarkContent.setTypeface(DataConstants.typeFZLT);
		//CourseRecordInfo cri= DataConstants.dbHelper.queryCourseRecordByPhotoName(this, db, tableName, photoNames.get(index));
		//String text=cri.getRemark();
		//remarkContent.setText(text);
		remarkContent.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		//remarkContext.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa一一一一一一一一一一一一一一一一一一一一一一一一一一一一一");
		extendBtn.setOnClickListener(new OnClickListener() {
			Boolean flag = true;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(flag){
			        flag = false;
			        remarkContent.setEllipsize(null); // 展开
			        remarkContent.setSingleLine(flag);
			        remarkContent.setLines(4);
			        }else{
			        	Log.e(DataConstants.TAG,"remarkcontent "+flag);
			          flag = true;
			          remarkContent.setEllipsize(android.text.TextUtils.TruncateAt.END); // 收缩
			          remarkContent.setLines(2);
			    }
			}
		});
		//View rootView = LayoutInflater.from(this).inflate(R.layout.content_layout, null);

		editRemarkLayout=(LinearLayout)findViewById(R.id.browse_remark_edit_hideBar);
		editRemark = (EditText)findViewById(R.id.browse_remark_edit);
		cancelEdit=(TextView)findViewById(R.id.browse_remark_edit_quitView);
		saveEdit=(TextView)findViewById(R.id.browse_remark_edit_saveView);
		
		editRemarkImg=(ImageView)findViewById(R.id.remark_img);
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
				//SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				DataConstants.dbHelper.updateCourseRecordOnStringColByPhotoName(PhotoBrowseActivity.this, db, photoInfos.get(currentIndex).getTableName(),getResources().getString(R.string.dbcol_remark), editRemark.getText().toString(), photoInfos.get(currentIndex).getPhotoName());
				//textUtils.get(currentIndex)
				//textUtil.setTextContent(editRemark.getText().toString(),true);
				remarkContent.setText(editRemark.getText().toString());
				//db.close();
			}
		});
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		db = DataConstants.dbHelper.getReadableDatabase();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		db.close();
	}
	
}
	
	
	

