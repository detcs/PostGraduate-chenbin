package com.pages.notes;

import java.io.File;
import java.util.List;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.model.DatabaseHelper;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.data.util.DisplayUtil;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseSettingFragment extends Fragment {
	TextView english1;
	TextView english2;
	ImageView english1Bg;
	ImageView english2Bg;
	TextView math1;
	TextView math2;
	TextView math3;
	TextView mathNo;
	ImageView math1Bg;
	ImageView math2Bg;
	ImageView math3Bg;
	ImageView math4Bg;
	ImageView mathNoBg;
	TextView politics;
	LinearLayout politicBg;
	
	EditText professEdit1;
	EditText professEdit2;
	Button complete;
	CourseSettingInfo info;
	int defaultTextColor=Color.parseColor("#333333");
	int choosedTextColor=Color.parseColor("#ffffff");
	enum Courses{English,Math,Politics};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_course_set, container, false);
		info=new CourseSettingInfo();
		//LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(100,40);
		english1=(TextView)rootView.findViewById(R.id.english1);
	//	english1Bg=(ImageView) rootView.findViewById(R.id.english1_bg);
		//english1.setLayoutParams(param);
		english2=(TextView)rootView.findViewById(R.id.english2);
	//	english2Bg=(ImageView) rootView.findViewById(R.id.english2_bg);
		//english1.setTextColor(choosedTextColor);
		//english1.setBackgroundResource(R.drawable.choose_english);
		setButtonChooseState(english1Bg,english1, true,Courses.English);
		setButtonChooseState(english2Bg,english2, false,Courses.English);
		info.setEnglish(getResources().getString(R.string.english1));
		//english2.setTextColor(defaultTextColor);
		english1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.e(DataConstants.TAG,"click "+" "+R.id.english1);
				setButtonChooseState(english1Bg,english1, true,Courses.English);
				setButtonChooseState(english2Bg,english2, false,Courses.English);
				//english1.setTextColor()
				info.setEnglish(getResources().getString(R.string.english1));
			}
		});
		english2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setButtonChooseState(english1Bg,english1, false,Courses.English);
				setButtonChooseState(english2Bg,english2, true,Courses.English);
				info.setEnglish(getResources().getString(R.string.english2));
			}
		});
		
		
		math1=(TextView)rootView.findViewById(R.id.math1);
		math2=(TextView)rootView.findViewById(R.id.math2);
		math3=(TextView)rootView.findViewById(R.id.math3);
		mathNo=(TextView)rootView.findViewById(R.id.no_math);
//		math1Bg=(ImageView) rootView.findViewById(R.id.math1_bg);
//		math2Bg=(ImageView) rootView.findViewById(R.id.math2_bg);
//		math3Bg=(ImageView) rootView.findViewById(R.id.math3_bg);
//		mathNoBg=(ImageView) rootView.findViewById(R.id.math_no_bg);
		setButtonChooseState(math1Bg,math1, true,Courses.Math);
		setButtonChooseState(math2Bg,math2, false,Courses.Math);
		setButtonChooseState(math3Bg,math3, false,Courses.Math);
		setButtonChooseState(mathNoBg,mathNo, false,Courses.Math);
		info.setMath(getResources().getString(R.string.math1));
		math1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.e(DataConstants.TAG,"click "+" "+R.id.math1);
				setButtonChooseState(math1Bg,math1, true,Courses.Math);
				setButtonChooseState(math2Bg,math2, false,Courses.Math);
				setButtonChooseState(math3Bg,math3, false,Courses.Math);
				setButtonChooseState(mathNoBg,mathNo, false,Courses.Math);
				info.setMath(getResources().getString(R.string.math1));
			}
		});
		math2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setButtonChooseState(math1Bg,math1, false,Courses.Math);
				setButtonChooseState(math2Bg,math2, true,Courses.Math);
				setButtonChooseState(math3Bg,math3, false,Courses.Math);
				setButtonChooseState(mathNoBg,mathNo, false,Courses.Math);
				info.setMath(getResources().getString(R.string.math2));
			}
		});
		math3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setButtonChooseState(math1Bg,math1, false,Courses.Math);
				setButtonChooseState(math2Bg,math2,  false,Courses.Math);
				setButtonChooseState(math3Bg,math3,true,Courses.Math);
				setButtonChooseState(mathNoBg,mathNo, false,Courses.Math);
				info.setMath(getResources().getString(R.string.math3));
			}
		});
		mathNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setButtonChooseState(math1Bg,math1, false,Courses.Math);
				setButtonChooseState(math2Bg,math2,  false,Courses.Math);
				setButtonChooseState(math3Bg,math3,false,Courses.Math);
				setButtonChooseState(mathNoBg,mathNo, true,Courses.Math);
				info.setMath(null);
			}
		});
		politics=(TextView)rootView.findViewById(R.id.politics);
	//	politicBg=(LinearLayout)rootView.findViewById(R.id.politics_bg);
		setButtonChooseState(politicBg,politics, true,Courses.Politics);
		info.setPolitics(getResources().getString(R.string.politics));
//		politics.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Log.e(DataConstants.TAG,"click "+" "+R.id.english1);
//				
//				info.setPolitics("politics");
//			}
//		});
		
		professEdit1=(EditText)rootView.findViewById(R.id.input_profess_course1);
		professEdit2=(EditText)rootView.findViewById(R.id.input_profess_course2);
		complete=(Button)rootView.findViewById(R.id.complete_choose);
		complete.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.register_complete_normal));
		complete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				complete.setBackground(DisplayUtil.drawableTransfer(getActivity(), R.drawable.register_complete_click));
				info.setProfess1(professEdit1.getText().toString());
				info.storeToConfig();
				makeCourseFileDir(info);
				UserConfigs.storeIsFirstTakePhoto("no");
				getActivity().setResult(DataConstants.RESULTCODE_COURSE_SETTING);
				getActivity().finish();
			}
		});
		return rootView;
	}
	private void setButtonChooseState(View v,TextView tv,boolean choose,Courses course)
	{
		if(choose)
		{
			
			if(course==Courses.English)
				tv.setBackground(getResources().getDrawable(R.drawable.choose_english));
			else if(course==Courses.Math)
				tv.setBackground(getResources().getDrawable(R.drawable.choose_math));
			else if(course==Courses.Politics)
				tv.setBackground(getResources().getDrawable(R.drawable.choose_politic));
			tv.setTextColor(choosedTextColor);
			
		}
		else
		{
			
			tv.setBackground(getResources().getDrawable(R.drawable.choose_no));
			tv.setTextColor(defaultTextColor);
			//tv.setText("default");
		}
	}
	private void makeCourseFileDir(CourseSettingInfo info)
	{
		if(FileDataHandler.sdCardExist())      //如果SD卡存在
		{                               
			DatabaseHelper dbHelper = new DatabaseHelper(getActivity());//这段代码放到Activity类中才用this
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			
			String dirPath=FileDataHandler.APP_DIR_PATH;
			File dir=new File(dirPath+"/"+getResources().getString(R.string.dir_english));
			
			if(!dir.exists())
			{
				dir.mkdir();
				
			}
			dir=new File(dirPath+"/"+getResources().getString(R.string.dir_politics));
			if(!dir.exists())
			{
				dir.mkdir();
				
			}
			dir=new File(dirPath+"/"+getResources().getString(R.string.dir_profess1));
			if(!dir.exists())
			{
				dir.mkdir();
				
			}
			dbHelper.createCourseTable(getActivity(),db, getResources().getString(R.string.db_profess1_table));
			if(info.getMath()!=null)
			{
				dir=new File(dirPath+"/"+getResources().getString(R.string.dir_math));
				if(!dir.exists())
				{
					dir.mkdir();
					
				}
				dbHelper.createCourseTable(getActivity(),db, getResources().getString(R.string.db_math_table));
			}
			if(info.getProfess2()!=null)
			{
				dir=new File(dirPath+"/"+getResources().getString(R.string.dir_profess2));
				if(!dir.exists())
				{
					dir.mkdir();
					
				}
				dbHelper.createCourseTable(getActivity(),db, getResources().getString(R.string.db_profess2_table));
			}
			dbHelper.createCourseTable(getActivity(),db, getResources().getString(R.string.db_english_table));
			dbHelper.createCourseTable(getActivity(),db, getResources().getString(R.string.db_politics_table));
			db.close();
		}   
	}
}
