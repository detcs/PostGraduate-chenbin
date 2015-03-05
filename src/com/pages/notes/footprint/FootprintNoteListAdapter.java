package com.pages.notes.footprint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.app.ydd.R;
import com.data.model.CourseRecordAndPathInfo;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.data.model.DataConstants.PageName;
import com.data.util.PhotoNameTableInfo;
import com.pages.notes.timeline.PhotoShowGridAdapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FootprintNoteListAdapter extends BaseAdapter{

	LayoutInflater mInflater;
	String date;
	//List<CourseRecordInfo> courseInfos;
	HashMap<String, List<CourseRecordInfo>> courseAndPhotoInfoMap;
	//List<CourseRecordAndPathInfo> photoAndPathInfos;
	//List<String> courseNames;
	List<String> courseNames;
	List<String> tableNames;
	Context context;
	PhotoShowGridAdapter photoShowAdapter;
	public FootprintNoteListAdapter(Context context,String date,List<String> courseNames,List<String> tableNames,HashMap<String, List<CourseRecordInfo>> courseAndPhotoInfoMap) {
		super();
		this.context=context;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.date = date;
		this.tableNames=tableNames;
		this.courseNames=courseNames;
		this.courseAndPhotoInfoMap=courseAndPhotoInfoMap;
		//courseNames=UserConfigs.getCourseNames();
		//SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				
		HashMap<String, String> map=UserConfigs.getCourseNameAndTableMap();
//		tableNames=new ArrayList<String>();
//		for(String course:courseNames)
//		{
//			String table=map.get(course);
//			List<String> photoNames=DataConstants.dbHelper.queryPhotoNamesAtDate(context, db, table, date);
//			if(photoNames.size()>0)
//				tableNames.add(table);
//		}
//		for(int i=0;i<courseNames.size();i++)
//		{
//			tableNames.add(map.get(courseNames.get(i)));
//		}
		
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;  
	    if (convertView == null) { 
	        convertView = mInflater.inflate(R.layout.item_footprint_list, null); 
	        
	        holder = new ViewHolder(); 
	        holder.grid=(GridView)convertView.findViewById(R.id.footprint_grid);
	        holder.courseName=(TextView)convertView.findViewById(R.id.footprint_coursename);
	        //holder.img = (ImageView) convertView.findViewById(R.id.exercise_timeline_img); 
	        convertView.setTag(holder); 
	    } else { 
	       
	        holder = (ViewHolder) convertView.getTag(); 
	    }
	    holder.courseName.setText(courseNames.get(position));
		photoShowAdapter=new PhotoShowGridAdapter(context,tableNames.get(position),courseAndPhotoInfoMap.get(courseNames.get(position)),false,PageName.NoteFootprint);
	    holder.grid.setAdapter(photoShowAdapter);
	    return convertView;
	}
	static class ViewHolder { 

		TextView courseName;
	    GridView grid;

	} 
}
