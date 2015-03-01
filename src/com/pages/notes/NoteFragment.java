package com.pages.notes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.data.util.DateUtil;
import com.data.util.DisplayUtil;
import com.data.util.SysCall;
import com.pages.funsquare.ButtonsGridViewAdapter;
import com.pages.notes.camera.CameraActivity;
import com.pages.notes.footprint.FootPrintActivity;
import com.pages.notes.footprint.FootprintInfo;
import com.pages.notes.todayrec.TodayRecommenderActivity;
import com.pages.viewpager.MainActivity;
import com.squareup.picasso.Picasso;
import com.view.util.AutoScrollTextView;
import com.view.util.AutoScrollView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NoteFragment  extends Fragment{
	GridView funcGridView;
	ListView courseNamelist;
	NotesClassAdapter noteClassAdapter;
	List<String> courseTableNames;
	List<String> names;
	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_notes, container, false);
		initFootprint();
		initNoteView(rootView);
		return rootView;
	}
	public void initNoteView(View v) {
		// final boolean
		// isFirstUse=UserConfigs.getIsFirstTakePhoto()==null?true:false;
		//RelativeLayout headLayout=(RelativeLayout) v.findViewById(R.id.head_layout);
		ImageView bg=(ImageView) v.findViewById(R.id.note_bg_img);
		SQLiteDatabase db= DataConstants.dbHelper.getReadableDatabase();
		FootprintInfo fpInfo=DataConstants.dbHelper.queryFootPrintInfo(getActivity(), db, DateUtil.getTodayDateString());
		db.close();
		if(fpInfo!=null)
		{
			String bgFilePath=FileDataHandler.COVER_NOTE_PIC_DIR_PATH+"/"+fpInfo.getCoverNotePicName();
			Picasso.with(getActivity()).load(new File(bgFilePath)).resize(200, 200).into(bg);
		}
		else
		{
			Picasso.with(getActivity()).load(R.drawable.note_bg).resize(200, 200).into(bg);
		}
		ImageView headImg=(ImageView) v.findViewById(R.id.head_img);
		Picasso.with(getActivity()).load(R.drawable.note_default_male).resize(100, 100).into(headImg);
		
		initCountInfo(v);
		
		final LinearLayout editDiaryLayout=(LinearLayout)v.findViewById(R.id.diary_hideBar);
		final EditText editDiary = (EditText) v.findViewById(R.id.diary_remarksView);
		TextView cancelEdit=(TextView)v.findViewById(R.id.diary_quitView);
		TextView saveEdit=(TextView)v.findViewById(R.id.diary_saveView);
		final TextView diary = (TextView) v.findViewById(R.id.diary);
		diary.setTypeface(DataConstants.typeFZLT);
		//SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String date = sdf.format(calendar.getTime());
		//FootprintInfo fpInfo=DataConstants.dbHelper.queryFootPrintInfo(getActivity(), db, date);
		
		if(fpInfo!=null)
			diary.setText(fpInfo.getDiary());
		cancelEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(editDiaryLayout.getVisibility()==View.VISIBLE)
				{
					editDiaryLayout.setVisibility(View.INVISIBLE);
					SysCall.hideSoftInput(editDiaryLayout, getActivity());
					editDiary.clearFocus();
					editDiary.setFocusable(false);
					editDiary.setFocusableInTouchMode(false);
				}
			}
		});
		saveEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editDiaryLayout.setVisibility(View.INVISIBLE);
				SysCall.hideSoftInput(editDiaryLayout, getActivity());
				editDiary.clearFocus();
				editDiary.setFocusable(false);
				editDiary.setFocusableInTouchMode(false);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				String date = sdf.format(calendar.getTime());
				SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				DataConstants.dbHelper.updateFootprintRecord(getActivity(), db, getResources().getString(R.string.dbcol_diary), editDiary.getText().toString(), date);
				db.close();
				diary.setText(editDiary.getText().toString());
			}
		});
		ImageView todayRec = (ImageView) v.findViewById(R.id.today_rec);
		todayRec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				
				boolean isFirstUse = UserConfigs.getIsFirstTakePhoto() == null ? true
						: false;
				if (isFirstUse) {
					intent.setClass(getActivity(), ExerciseActivity.class);
					intent.putExtra("tag",getResources().getString(R.string.first_use));
					startActivityForResult(intent, 0);
				}
			   else {
				   intent.setClass(getActivity(), TodayRecommenderActivity.class);
					intent.putExtra("tag",getResources().getString(R.string.today_rec));
					startActivity(intent);
				}
				
			}
		});
		ImageView editImg=(ImageView) v.findViewById(R.id.edit_diary);
		Picasso.with(getActivity()).load(R.drawable.diary_edit).resize(30, 30).into(editImg);
		editImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editDiaryLayout.setVisibility(View.VISIBLE);
				SysCall.bumpSoftInput(editDiary, getActivity());
			}
		});
		Button takePhoto = (Button) v.findViewById(R.id.take_photo);
		takePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("filp", "" + arg0.getId() + " button 1");
				Intent intent = new Intent();
				intent.setClass(getActivity(), CameraActivity.class);
				getActivity().startActivityForResult(intent,DataConstants.REQUEST_CODE_CAMERA);

			}
		});
		// GridView gv=(GridView)v.findViewById(R.id.notes_grid);
		courseNamelist = (ListView) v.findViewById(R.id.course_list);
		final List<String> courseTableNames=new ArrayList<String>();
		final List<String> names = new ArrayList<String>();
		names.add(getResources().getString(R.string.english)+UserConfigs.getCourseEnglishName());
		courseTableNames.add(getResources().getString(R.string.db_english_table));
		names.add(getResources().getString(R.string.politics));
		courseTableNames.add(getResources().getString(R.string.db_politics_table));
		if(UserConfigs.getCourseMathName()!=null)
		{
			names.add(getResources().getString(R.string.math)+UserConfigs.getCourseMathName());
			courseTableNames.add(getResources().getString(R.string.db_math_table));
		}
		names.add(UserConfigs.getCourseProfessOneName());
		courseTableNames.add(getResources().getString(R.string.db_profess1_table));
		if(UserConfigs.getCourseProfessTwoName()!=null)
		{
			names.add(UserConfigs.getCourseProfessTwoName());
			courseTableNames.add(getResources().getString(R.string.db_profess2_table));
		}
		noteClassAdapter=new NotesClassAdapter(names,courseTableNames,getActivity());
		courseNamelist.setAdapter(noteClassAdapter);
		courseNamelist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), ExerciseActivity.class);
				boolean isFirstUse = UserConfigs.getIsFirstTakePhoto() == null ? true
						: false;
				if (isFirstUse) {
					intent.putExtra("tag",getResources().getString(R.string.first_use));
					startActivityForResult(intent, 0);
				} else {
					intent.putExtra("course_table_name", courseTableNames.get(position));
					intent.putExtra("tag",getResources().getString(R.string.note_class));
					getActivity().startActivityForResult(intent, DataConstants.REQUEST_CODE_EXERCISE);
				}
				
			}
		});
		ImageView myFootPrint = (ImageView) v.findViewById(R.id.my_footprint);
		myFootPrint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();

				boolean isFirstUse = UserConfigs.getIsFirstTakePhoto() == null ? true
						: false;
				if (isFirstUse) {
					intent.putExtra("tag",
							getResources().getString(R.string.first_use));
					intent.setClass(getActivity(), ExerciseActivity.class);
				} else {
					intent.setClass(getActivity(), FootPrintActivity.class);
				}
				startActivity(intent);
			}
		});
		//TextView count_note=(TextView)v.findViewById(R.id.count_note);
		
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initCountInfo(rootView);
		updateNoteClassList();
	}
	private void initFootprint()
	{
//		SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar calendar = Calendar.getInstance();
//		String date = sdf.format(calendar.getTime());
//		FootprintInfo fpInfo=DataConstants.dbHelper.queryFootPrintInfo(getActivity(), db, date);
//		if(fpInfo==null)
//		{
//			fpInfo=new FootprintInfo("", "", "singer","", "diary", date, "encourage", days, daysLeft,getResources().getString(R.string.upload_no));
//			DataConstants.dbHelper.insertFootprintInfoRecord(getActivity(), db, fpInfo);
//		}
//		db.close();
	}
	private void initCountInfo(View v)
	{
		float countSize=DisplayUtil.spTopx(60*DataConstants.dpiRate, DataConstants.displayMetricsScaledDensity);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),"font/fangzhenglanting.ttf");
		TextView countClockTv=(TextView) v.findViewById(R.id.count_clock);
		int countClock=UserConfigs.getClockDays();
		countClockTv.setText(""+countClock);
		countClockTv.setTypeface(typeFace);
		countClockTv.setTextSize(countSize);
		
		TextView countNoteTv=(TextView) v.findViewById(R.id.count_note);
		
		SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		int countNote=DataConstants.dbHelper.queryAllCourseRecordsCount(getActivity(), db);
		//countSize=DisplayUtil.spTopx(24*DataConstants.dpiRate, DataConstants.displayMetricsScaledDensity);
		countNoteTv.setTypeface(typeFace);
		countNoteTv.setTextSize(countSize);
		countNoteTv.setText(countNote+"");
		//countNoteTv.init(getActivity().getWindowManager());
		//countNoteTv.startScroll();
		
		TextView countTodayAdd=(TextView)v.findViewById(R.id.today_add);
		
		int count=DataConstants.dbHelper.queryAllCourseRecordsCount(getActivity(), db);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		int todayAdd=DataConstants.dbHelper.queryAllCourseRecordsCountOnDate(getActivity(), db, date);
		db.close();
		countTodayAdd.setTypeface(typeFace);
		countTodayAdd.setTextSize(countSize);
		countTodayAdd.setText(todayAdd+"");
		//countTodayAdd.init(getActivity().getWindowManager());
		//countTodayAdd.startScroll();
		//AutoScrollView todayAddScrollView=(AutoScrollView) v.findViewById(R.id.today_add_scrollview);
		//todayAddScrollView.setScrolled(true);
	}
	public void updateNoteClassList()
	{
		getTableAndCourseNames();
		noteClassAdapter.setNames(names);
		noteClassAdapter.setCourseTableNames(courseTableNames);
		noteClassAdapter.notifyDataSetChanged();
	}
	private void getTableAndCourseNames()
	{
		courseTableNames=new ArrayList<String>();
		names = new ArrayList<String>();
		names.add(getResources().getString(R.string.english)+UserConfigs.getCourseEnglishName());
		courseTableNames.add(getResources().getString(R.string.db_english_table));
		names.add(getResources().getString(R.string.politics));
		courseTableNames.add(getResources().getString(R.string.db_politics_table));
		if(UserConfigs.getCourseMathName()!=null)
		{
			names.add(getResources().getString(R.string.math)+UserConfigs.getCourseMathName());
			courseTableNames.add(getResources().getString(R.string.db_math_table));
		}
		names.add(UserConfigs.getCourseProfessOneName());
		courseTableNames.add(getResources().getString(R.string.db_profess1_table));
		if(UserConfigs.getCourseProfessTwoName()!=null)
		{
			names.add(UserConfigs.getCourseProfessTwoName());
			courseTableNames.add(getResources().getString(R.string.db_profess2_table));
		}
	}
	

}
