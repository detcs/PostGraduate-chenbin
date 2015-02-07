package com.pages.notes.footprint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.pages.notes.timeline.PhotoShowGridAdapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class FootprintNoteListAdapter extends BaseAdapter{

	LayoutInflater mInflater;
	String date;
	List<String> courseNames;
	List<String> tableNames;
	Context context;
	PhotoShowGridAdapter photoShowAdapter;
	public FootprintNoteListAdapter(Context context,String date) {
		super();
		this.context=context;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.date = date;
		courseNames=UserConfigs.getCourseNames();
		HashMap<String, String> map=UserConfigs.getCourseNameAndTableMap();
		tableNames=new ArrayList<String>();
		for(int i=0;i<courseNames.size();i++)
		{
			tableNames.add(map.get(courseNames.get(i)));
		}
		
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
	    SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		List<String> photoNames=DataConstants.dbHelper.queryPhotoNamesAtDate(context, db, tableNames.get(position), date);
		List<String> photoPaths=new ArrayList<String>();
		String dirPath=FileDataHandler.APP_DIR_PATH+"/"+DataConstants.TABLE_DIR_MAP.get(tableNames.get(position));
		 for(String name:photoNames)
			photoPaths.add(dirPath+"/"+name);
		db.close();
		photoShowAdapter=new PhotoShowGridAdapter(context,photoPaths,false,tableNames.get(position));
	    holder.grid.setAdapter(photoShowAdapter);
	    return convertView;
	}
	static class ViewHolder { 

		TextView courseName;
	    GridView grid;

	} 
}
