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
import com.data.util.DisplayUtil;
import com.data.util.ImageUtil;
import com.data.util.PhotoNamePathUtil;
import com.data.util.PhotoNameTableInfo;
import com.data.util.SysCall;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
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
	//ArrayList<PhotoNameTableInfo> photoInfos;
	List<CourseRecordInfo> courseInfos;
	//ArrayList<TextContentShowUtil> textUtils;
	//TextContentShowUtil textUtil;
	int currentIndex=0;
	SQLiteDatabase db=null;
	DragImageView dragImageView;
	View content;
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
//		String date=bundle.getString("date");
//		String tag=bundle.getString("jump_tag");
		PageName fromPage=(PageName) bundle.get("from_page");
//		String tableName=bundle.getString("table_name");
		db = DataConstants.dbHelper.getReadableDatabase();
		
		courseInfos=(List<CourseRecordInfo>) getIntent().getSerializableExtra("courseInfos");
		PhotoNameTableInfo photoInfo=null;
			contents = new ArrayList<View>();
			images = new ArrayList<DragImageView>();
			Bitmap bmp=null;
			BitmapFactory.Options opt=new BitmapFactory.Options();
			opt.inSampleSize=4;
			//Bitmap bmp = BitmapUtil.ReadBitmapById(this, R.drawable.today_background,windowWidth, windowHeight);
			for (int i = 0; i < courseInfos.size(); i++) {
				content = LayoutInflater.from(this).inflate(R.layout.content_layout, null);
				dragImageView = (DragImageView) content.findViewById(R.id.div_main);
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
					View view=contents.get(position);
					final DragImageView img=(DragImageView) view.findViewById(R.id.div_main);
					BitmapFactory.Options opt=new BitmapFactory.Options();
					opt.inSampleSize=8;
					DisplayImageOptions opts=new DisplayImageOptions.Builder()
					.cacheInMemory(false)
					.cacheOnDisk(true)
					//.showImageOnLoading(R.drawable.note_thumb)
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.decodingOptions(opt)
					.build();
					ImageUtil.imageLoader.displayImage(courseInfos.get(position).getPhotoPath(), img,opts, new SimpleImageLoadingListener()
					{

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub
							super.onLoadingCancelled(imageUri, view);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							img.setmActivity(PhotoBrowseActivity.this);// ע��Activity.
							img.setLayout(content);
							img.setBumpHeight(300);
							img.setScreen_H(windowHeight-stateHeight);
							img.setScreen_W(windowWidth);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							super.onLoadingFailed(imageUri, view, failReason);
							Log.e(DataConstants.TAG, "fail");
						}

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							super.onLoadingStarted(imageUri, view);
							Log.e(DataConstants.TAG, "start");
						}
						
					});
					/*
					ImageUtil.imageLoader.loadImage(courseInfos.get(position).getPhotoPath(),opts,new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
							// TODO Auto-generated method stub
							//bmp=BitmapFactory.decodeFile(courseInfos.get(i).getPhotoPath(), opt);
							img.setImageBitmap(bitmap);
							img.setmActivity(PhotoBrowseActivity.this);// ע��Activity.
							img.setLayout(content);
							img.setBumpHeight(300);
							img.setScreen_H(windowHeight-stateHeight);
							img.setScreen_W(windowWidth);
							
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
							
						}
					});
					*/
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
					
					String blurPath=FileDataHandler.photoPathToBlurPath(courseInfos.get(currentIndex).getPhotoPath());
					File blurFile=new File(blurPath);
					if(blurFile.exists())
						Picasso.with(getApplicationContext()).load(blurFile).into(flurImg);
					CourseRecordInfo cri= DataConstants.dbHelper.queryCourseRecordByPhotoName(getApplicationContext(), db, courseInfos.get(currentIndex).getTableName(), courseInfos.get(currentIndex).getPhotoName());
					String text=cri.getRemark();
					remarkContent.setText(text);
					//textUtil.setTextContent(text, true);
					
					if(cri.getFlag()==1)
						importantImg.setBackground(DisplayUtil.drawableTransfer(PhotoBrowseActivity.this, R.drawable.notespec_importance_click));
						//Picasso.with(getApplicationContext()).load(R.drawable.notespec_importance_click).into(importantImg);
					else
						importantImg.setBackground(DisplayUtil.drawableTransfer(PhotoBrowseActivity.this, R.drawable.notespec_importance));
						//Picasso.with(getApplicationContext()).load(R.drawable.notespec_importance).into(importantImg);
					boolean currentFlag=cri.getFlag()==1?true:false;
					importantImg.setOnClickListener(new ImportanceFlagOnClickListener(courseInfos.get(currentIndex).getPhotoName(),courseInfos.get(currentIndex).getTableName(), PhotoBrowseActivity.this,importantImg,currentFlag,PageName.NoteSpec));
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
			initTitleView();
			initBottomView();
			initRemarkEditView();
			viewPager.setCurrentItem(0);
	}
	private void initTitleView()
	{
		RelativeLayout browseTop=(RelativeLayout) findViewById(R.id.browse_top);
		//ImageUtil.imageLoader.
		//browseTop.setBackgroundResource(R.drawable.gradual_title_bg);
		browseTop.setBackground(DisplayUtil.drawableTransfer(getApplicationContext(), R.drawable.gradual_title_bg));
		TextView back=(TextView) findViewById(R.id.browse_back);
		back.setOnClickListener(new OnClickListener() 
		{
			
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
		//delete.setBackgroundResource(R.drawable.delete_long_btn);
		delete.setBackground(DisplayUtil.drawableTransfer(PhotoBrowseActivity.this, R.drawable.delete_long_btn));
		TextView deleteCancel=(TextView)findViewById(R.id.browse_delete_cancel_bg);
		//deleteCancel.setBackgroundResource(R.drawable.delete_long_btn);
		deleteCancel.setBackground(DisplayUtil.drawableTransfer(PhotoBrowseActivity.this, R.drawable.delete_long_btn));
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String photoname=courseInfos.get(currentIndex).getPhotoName();
				DataConstants.dbHelper.updateCourseRecordOnIntColByPhotoName(PhotoBrowseActivity.this, db,courseInfos.get(currentIndex).getTableName(), photoname, getResources().getString(R.string.dbcol_photo_delete), 1);
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
		FrameLayout bottomFrame=(FrameLayout) findViewById(R.id.browse_bottom_frame);
		//bottomFrame.setBackgroundResource(R.drawable.gradual_text_bg);
		bottomFrame.setBackground(DisplayUtil.drawableTransfer(PhotoBrowseActivity.this, R.drawable.gradual_text_bg));
		//ImageView textBg=(ImageView) findViewById(R.id.gradual_text_bg);
		//Picasso.with(getApplicationContext()).load(R.drawable.gradual_text_bg).into(textBg);
		
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
		importantImg.setBackground(DisplayUtil.drawableTransfer(PhotoBrowseActivity.this, R.drawable.notespec_importance));
		//Picasso.with(getApplicationContext()).load(R.drawable.notespec_importance).into(importantImg);
		rotateImg=(ImageView)findViewById(R.id.rotate_img);
		rotateImg.setBackground(DisplayUtil.drawableTransfer(PhotoBrowseActivity.this, R.drawable.notespec_rotate));
		//Picasso.with(getApplicationContext()).load(R.drawable.notespec_rotate).into(rotateImg);
		remarkImg=(ImageView)findViewById(R.id.remark_img);
		remarkImg.setBackground(DisplayUtil.drawableTransfer(PhotoBrowseActivity.this, R.drawable.notespec_remark));
		//Picasso.with(getApplicationContext()).load(R.drawable.notespec_remark).into(remarkImg);
	}
	private void initRemarkEditView()
	{
		remarkContent=(TextView)findViewById(R.id.text_remark_content);
		TextView extendBtn=(TextView)findViewById(R.id.extends_img);
		extendBtn.setBackground(DisplayUtil.drawableTransfer(PhotoBrowseActivity.this, R.drawable.look_all));
		remarkContent.setTypeface(DataConstants.typeFZLT);
		String tableName=courseInfos.get(currentIndex).getTableName();
		String photoName=courseInfos.get(currentIndex).getPhotoName();
		CourseRecordInfo cri= DataConstants.dbHelper.queryCourseRecordByPhotoName(this, db, tableName, photoName);
		String text=cri.getRemark();
		remarkContent.setText(text);
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
				editRemark.setText(remarkContent.getText().toString());
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
				DataConstants.dbHelper.updateCourseRecordOnStringColByPhotoName(PhotoBrowseActivity.this, db, courseInfos.get(currentIndex).getTableName(),getResources().getString(R.string.dbcol_remark), editRemark.getText().toString(), courseInfos.get(currentIndex).getPhotoName());
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
		initBottomView();initRemarkEditView();initTitleView();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		db.close();
		releaseTitleBackground();
		releaseBottomBackground();
	}
	private void releaseRemarkBackground()
	{
		TextView extendBtn=(TextView)findViewById(R.id.extends_img);
		extendBtn.setBackground(null);
	}
	private void releaseTitleBackground()
	{
		RelativeLayout browseTop=(RelativeLayout) findViewById(R.id.browse_top);
		//ImageUtil.imageLoader.
		//browseTop.setBackgroundResource(R.drawable.gradual_title_bg);
		browseTop.setBackground(null);
		TextView delete=(TextView)findViewById(R.id.browse_delete_bg);
		//delete.setBackgroundResource(R.drawable.delete_long_btn);
		delete.setBackground(null);
		TextView deleteCancel=(TextView)findViewById(R.id.browse_delete_cancel_bg);
		//deleteCancel.setBackgroundResource(R.drawable.delete_long_btn);
		deleteCancel.setBackground(null);
	}
	private void releaseBottomBackground()
	{
		FrameLayout bottomFrame=(FrameLayout) findViewById(R.id.browse_bottom_frame);
		bottomFrame.setBackground(null);
		importantImg.setBackground(null);
		rotateImg.setBackground(null);
		remarkImg.setBackground(null);
	}
	
}
	
	
	

