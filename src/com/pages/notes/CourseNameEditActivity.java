package com.pages.notes;

import java.util.ArrayList;
import java.util.List;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.model.DataConstants.CourseClassName;
import com.pages.notes.CourseSettingFragment.Courses;
import com.pages.notes.NotesClassAdapter.ViewHolder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CourseNameEditActivity extends Activity{

	ImageView backImg;
	GridView gridView;
	TextView courseName;
	TextView complete;
	List<TextView> courseNameList;
	List<String> names;
	int defaultTextColor=Color.parseColor("#333333");
	int choosedTextColor=Color.parseColor("#ffffff");
	enum Courses{English,Math,Politics};
	CourseClassName currentCourseClass;
	CourseSettingInfo info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_coursename_edit);
		info=new CourseSettingInfo();
		gridView=(GridView)findViewById(R.id.grid_course_names);
		String course=getIntent().getStringExtra("course_edit_name");
		currentCourseClass=(CourseClassName)getIntent().getSerializableExtra("course_class_name");
		complete=(TextView) findViewById(R.id.save_edit);
		complete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(currentCourseClass==CourseClassName.English)
					info.storeEnglish();
				else if(currentCourseClass==CourseClassName.Math)
					info.storeMath();
			}
		});
		
		courseName=(TextView) findViewById(R.id.editing_course_name);
		courseName.setTypeface(DataConstants.typeFZLT);
		courseName.setText(course);
		names=new ArrayList<String>();
		courseNameList=new ArrayList<TextView>();
		if(course.contains(getResources().getString(R.string.english)))
		{
			names.add(getResources().getString(R.string.english1));
			names.add(getResources().getString(R.string.english2));
		}
		else if(course.contains(getResources().getString(R.string.math)))
		{
			names.add(getResources().getString(R.string.math1));
			names.add(getResources().getString(R.string.math2));
			names.add(getResources().getString(R.string.math3));
		}
		Log.e(DataConstants.TAG, "size "+names.size());
		gridView.setAdapter(new CourseNamesAdapter(names));
	}
	private void setButtonChooseState(TextView tv,boolean choose,CourseClassName course)
	{
		if(choose)
		{
			
			if(course==CourseClassName.English)
			{
				tv.setBackground(getResources().getDrawable(R.drawable.choose_english));
			
			}
			else if(course==CourseClassName.Math)
			{
				tv.setBackground(getResources().getDrawable(R.drawable.choose_math));
			}
			else if(course==CourseClassName.Politics)
			{
				tv.setBackground(getResources().getDrawable(R.drawable.choose_politic));
			}
			tv.setTextColor(choosedTextColor);
			
		}
		else
		{
			
			tv.setBackground(getResources().getDrawable(R.drawable.choose_no));
			tv.setTextColor(defaultTextColor);
			//tv.setText("default");
		}
	}
	class CourseNamesAdapter extends BaseAdapter
	{

		LayoutInflater mInflater;
		List<String > courseNames;
		public CourseNamesAdapter(List<String> list) {
			super();
			// TODO Auto-generated constructor stub
			mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			courseNames=list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return courseNames.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return courseNames.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null)
			{
				convertView= mInflater.inflate(R.layout.item_edit_coursename, null); 
				holder=new ViewHolder();
				holder.item=(TextView) convertView.findViewById(R.id.course_name);
				courseNameList.add(holder.item);
				convertView.setTag(holder);
			}
			else 
			{ 
			    holder = (ViewHolder) convertView.getTag(); 
			    
			} 
			holder.item.setText(names.get(arg0));
			holder.item.setTypeface(DataConstants.typeFZLT);
			holder.item.setGravity(Gravity.CENTER);
			setButtonChooseState(holder.item, false,currentCourseClass);
			holder.item.setOnClickListener(new CourseButtonClickListener(holder.item));
			return convertView;
		}
		
	}
	class CourseButtonClickListener implements OnClickListener
	{
		TextView tv;
		
		public CourseButtonClickListener(TextView tv) {
			super();
			this.tv = tv;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			TextView ctv;
			for(int i=0;i<courseNameList.size();i++)
			{
				ctv=courseNameList.get(i);
				if(tv==ctv)
				{
					setButtonChooseState(tv, true, currentCourseClass);
					if(currentCourseClass==CourseClassName.English)
					{
						info.setEnglish((i+1)+"");
					}
					else if(currentCourseClass==CourseClassName.Math)
					{
						info.setMath((i+1)+"");
					}
				}
				else
					setButtonChooseState(ctv, false, currentCourseClass);
			}
		}
		
	}
	static class ViewHolder
	{
		TextView item;
	}
}
