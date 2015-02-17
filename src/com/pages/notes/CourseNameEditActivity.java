package com.pages.notes;

import java.util.ArrayList;
import java.util.List;

import com.app.ydd.R;
import com.pages.notes.NotesClassAdapter.ViewHolder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CourseNameEditActivity extends Activity{

	ImageView backImg;
	GridView gridView;
	List<String> names;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_coursename_edit);
		gridView=(GridView)findViewById(R.id.grid_course_names);
		String course=getIntent().getStringExtra("course_edit_name");
		names=new ArrayList<String>();
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
		gridView.setAdapter(new CourseNamesAdapter());
	}
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		View rootView=inflater.inflate(R.layout.fragment_coursename_edit, container, false);
//		gridView=(GridView) rootView.findViewById(R.id.grid_course_names);
//		String course=getArguments().getString("course_edit_name");
//		names=new ArrayList<String>();
//		if(course.contains(getResources().getString(R.string.english)))
//		{
//			names.add(getResources().getString(R.string.english1));
//			names.add(getResources().getString(R.string.english2));
//		}
//		else if(course.contains(getResources().getString(R.string.math)))
//		{
//			names.add(getResources().getString(R.string.math1));
//			names.add(getResources().getString(R.string.math2));
//			names.add(getResources().getString(R.string.math3));
//		}
//		gridView.setAdapter(new CourseNamesAdapter());
//		return rootView;
//	}
	class CourseNamesAdapter extends BaseAdapter
	{

		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return names.get(arg0);
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
				convertView=new View(CourseNameEditActivity.this);
				holder=new ViewHolder();
				holder.item=new TextView(CourseNameEditActivity.this);
				convertView.setTag(holder);
			}
			else 
			{ 
			    holder = (ViewHolder) convertView.getTag(); 
			    
			} 
			holder.item.setText(names.get(arg0));
			holder.item.setGravity(Gravity.CENTER);
			return convertView;
		}
		
	}
	static class ViewHolder
	{
		TextView item;
	}
}
