package com.pages.notes;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.app.ydd.R;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.DataConstants.PageName;
import com.data.model.FileDataHandler;
import com.data.model.ImportanceFlagOnClickListener;
import com.data.util.DisplayUtil;
import com.data.util.SysCall;
import com.pages.viewpager.MainActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReviewFragment extends Fragment{

	View rootView;
	TextView leftBtn;
	TextView rightBtn;
	TextView titleCenter;
	ImageView reviewImg;
	ProgressBar progressBar;
	List<String> reviewImgPaths;
	List<String> photoNames;
	
	SQLiteDatabase db;
	String tableName;
	int index=0;
	int totalNum=0;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_review, container, false);
		leftBtn=(TextView)rootView.findViewById(R.id.master);
		rightBtn=(TextView)rootView.findViewById(R.id.unmaster);
		leftBtn.setBackground(DisplayUtil.drawableTransfer(getActivity(),R.drawable.master));
		rightBtn.setBackground(DisplayUtil.drawableTransfer(getActivity(),R.drawable.nomaster));
		reviewImg=(ImageView)rootView.findViewById(R.id.review_img);
		progressBar=(ProgressBar) rootView.findViewById(R.id.progress_horizontal);
		Bundle bundle=getArguments();
		String type=bundle.getString("type");
		if(type.equals(getResources().getString(R.string.today_rec)))
		{
			leftBtn.setText(getResources().getString(R.string.get));
			rightBtn.setText(getResources().getString(R.string.collect));
		}
		else
		{
			initTitleView();
			String date=bundle.getString("date");
			tableName=getArguments().getString("course_table_name");
			db = DataConstants.dbHelper.getReadableDatabase();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(sdf.parse(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//把当前时间赋给日历
			String tempDate = sdf.format(calendar.getTime());
			ArrayList<String> targetDates=new ArrayList<String>();
			targetDates.add(tempDate);
			for(int i=0;i<2;i++)
			{
				calendar.roll(Calendar.DAY_OF_YEAR,-1);
				tempDate=sdf.format(calendar.getTime());
				targetDates.add(tempDate);
			}
			photoNames=new ArrayList<String>();
			for(String targetDate:targetDates)
			{
				List<String> dates=DataConstants.dbHelper.queryPhotoNamesAtDate(getActivity(), db, tableName, targetDate);
				photoNames.addAll(dates);
			}
			List<String> photoPaths=new ArrayList<String>();
			String dirPath=FileDataHandler.APP_DIR_PATH+"/"+DataConstants.TABLE_DIR_MAP.get(tableName);
			reviewImgPaths=new ArrayList<String>();
			for(String name:photoNames)
				reviewImgPaths.add(dirPath+"/"+name);
			totalNum=reviewImgPaths.size();
			progressBar.setMax(totalNum);
			progressBar.setProgress(1);
			titleCenter.setText(getResources().getString(R.string.reverse_review)+" ("+(index+1)+"/"+totalNum+" )");
			Picasso.with(getActivity()).load(new File(reviewImgPaths.get(0))).into(reviewImg);
			leftBtn.setOnClickListener(new CourseMasterListener(photoNames.get(index), tableName, getResources().getString(R.string.state_master)));
			rightBtn.setOnClickListener(new CourseMasterListener(photoNames.get(index), tableName, getResources().getString(R.string.state_unmaster)));
			
			initNoteEditView();
			
		}
		return rootView;
	}
	private void jumpToCompleteFragment()
	{
		Fragment fragment=new TaskCompleteFragment();
//		Bundle bundle = new Bundle();  
//        bundle.putString("type", "");  
//        fragment.setArguments(bundle);
		FragmentManager fm=getActivity().getFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();  
		trans.replace(R.id.exercise_frame, fragment);
		trans.commit();
	}
	class CourseMasterListener implements OnClickListener
	{

		String photoName;
		String tableName;
		String masterState;
		public CourseMasterListener(String photoName, String tableName,
				String masterState) {
			super();
			this.photoName = photoName;
			this.tableName = tableName;
			this.masterState = masterState;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		
				//rootView.startAnimation(animation);
				DataConstants.dbHelper.updateCourseRecordOnStringColByPhotoName(getActivity(), db, tableName,getResources().getString(R.string.dbcol_master_state), masterState, photoNames.get(index));
				index++;
				titleCenter.setText(getResources().getString(R.string.reverse_review)+" ("+(index+1)+"/"+totalNum+" )");
				progressBar.setProgress(progressBar.getProgress()+1);
				if(index==reviewImgPaths.size())
				{
					jumpToCompleteFragment();
				}
				else
				{
					Picasso.with(getActivity()).load(new File(reviewImgPaths.get(index))).into(reviewImg);
					initNoteEditView();
				}
				
			}

		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		db = DataConstants.dbHelper.getReadableDatabase();
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		db.close();
	}
	private void initTitleView()
	{
		RelativeLayout title=(RelativeLayout) getActivity().findViewById(R.id.title);
		title.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.gradual_title_bg));
		TextView right=(TextView)getActivity().findViewById(R.id.right_btn);
		right.setVisibility(View.INVISIBLE);
		ImageView left=(ImageView) getActivity().findViewById(R.id.exercise_left_btn);
		left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		titleCenter=(TextView)getActivity().findViewById(R.id.title_center);
		titleCenter.setText(getResources().getString(R.string.reverse_review));
	}
	private void initNoteEditView()
	{
		ImageView flagImg=(ImageView)rootView.findViewById(R.id.review_flag_img);
		Picasso.with(getActivity()).load(R.drawable.review_importance).into(flagImg);
		CourseRecordInfo cri= DataConstants.dbHelper.queryCourseRecordByPhotoName(getActivity(), db, tableName, photoNames.get(index));
		
		if(cri.getFlag()==1)
			Picasso.with(getActivity()).load(R.drawable.review_importance_click).into(flagImg);
		else
			Picasso.with(getActivity()).load(R.drawable.review_importance).into(flagImg);
		boolean currentFlag=cri.getFlag()==1?true:false;
		flagImg.setOnClickListener(new ImportanceFlagOnClickListener(photoNames.get(index),tableName,getActivity(),flagImg,currentFlag,PageName.NoteReview));
		
		ImageView rotateImg=(ImageView)rootView.findViewById(R.id.review_rotate_img);
		Picasso.with(getActivity()).load(R.drawable.review_rotate).into(rotateImg);
		rotateImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.e(DataConstants.TAG, "rotate");
				
				
				Bitmap bitmap=BitmapFactory.decodeFile(reviewImgPaths.get(index));
				Matrix matrix = new Matrix(); 
				matrix.setRotate(90);
		          //旋转图像，并生成新的Bitmap对像  
		        bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		        try {
		        	Log.e(DataConstants.TAG,"save bitmap");
					FileDataHandler.saveBitmap(bitmap, reviewImgPaths.get(index));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        bitmap.recycle();
		        bitmap=null;
		        Target target=new Target() {
					
					@Override
					public void onPrepareLoad(Drawable arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onBitmapLoaded(Bitmap arg0, LoadedFrom arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onBitmapFailed(Drawable arg0) {
						// TODO Auto-generated method stub
						
					}
				};
				
				//Picasso.with(getActivity()).load(new File(reviewImgPaths.get(index))).skipMemoryCache().into(target);
		        Picasso.with(getActivity()).load(new File(reviewImgPaths.get(index))).skipMemoryCache().into(reviewImg);
			}
		});
		
		ImageView remarkImg=(ImageView)rootView.findViewById(R.id.edit_remark_img);
		Picasso.with(getActivity()).load(R.drawable.review_remark).into(remarkImg);
		
		initRemarkEditView();
	}
	private void initRemarkEditView()
	{
		final TextView remarkContext=(TextView)rootView.findViewById(R.id.remark_content);
		TextView extendBtn=(TextView)rootView.findViewById(R.id.extends_btn);
		remarkContext.setTypeface(DataConstants.typeFZLT);
		CourseRecordInfo cri= DataConstants.dbHelper.queryCourseRecordByPhotoName(getActivity(), db, tableName, photoNames.get(index));
		String text=cri.getRemark();
		remarkContext.setText(text);
		remarkContext.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		//remarkContext.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa一一一一一一一一一一一一一一一一一一一一一一一一一一一一一");
		extendBtn.setOnClickListener(new OnClickListener() {
			Boolean flag = true;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(flag){
			        flag = false;
			        remarkContext.setEllipsize(null); // 展开
			        remarkContext.setSingleLine(flag);
			        remarkContext.setLines(4);
			        }else{
			        	Log.e(DataConstants.TAG,"remarkcontent "+flag);
			          flag = true;
			          remarkContext.setEllipsize(android.text.TextUtils.TruncateAt.END); // 收缩
			          remarkContext.setLines(2);
			    }
			}
		});
		final LinearLayout editRemarkLayout=(LinearLayout)rootView.findViewById(R.id.remark_edit_hideBar);
		final EditText editRemark = (EditText) rootView.findViewById(R.id.remark_edit);
		TextView cancelEdit=(TextView)rootView.findViewById(R.id.remark_edit_quitView);
		TextView saveEdit=(TextView)rootView.findViewById(R.id.remark_edit_saveView);
		
		ImageView editRemarkImg=(ImageView)rootView.findViewById(R.id.edit_remark_img);
		editRemarkImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editRemarkLayout.setVisibility(View.VISIBLE);
				SysCall.bumpSoftInput(editRemark, getActivity());
			}
		});
		cancelEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(editRemarkLayout.getVisibility()==View.VISIBLE)
				{
					editRemarkLayout.setVisibility(View.INVISIBLE);
					SysCall.hideSoftInput(editRemarkLayout, getActivity());
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
				SysCall.hideSoftInput(editRemarkLayout, getActivity());
				editRemark.clearFocus();
				editRemark.setFocusable(false);
				editRemark.setFocusableInTouchMode(false);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				String date = sdf.format(calendar.getTime());
				//SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				DataConstants.dbHelper.updateCourseRecordOnStringColByPhotoName(getActivity(), db, tableName,getResources().getString(R.string.dbcol_remark), editRemark.getText().toString(), photoNames.get(index));
				remarkContext.setText(editRemark.getText().toString());
				//db.close();
			}
		});
	}
}
