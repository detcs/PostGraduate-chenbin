package com.pages.notes;

import java.util.List;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.model.UserConfigs;
import com.pages.viewpager.MainActivity;
import com.squareup.picasso.Picasso;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class NotesClassAdapter extends BaseAdapter{
	List<String> names;
	List<String> courseTableNames;
	Context context;
	LayoutInflater mInflater;
	//private TextView editItem; //用于执行删除的button
	public NotesClassAdapter(List<String> names, List<String> courseTableNames,Context context) {
		super();
		this.names = names;
		this.courseTableNames=courseTableNames;
		this.context = context;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder; 
	    // When convertView is not null, we can reuse it directly, there is no need 
	    // to reinflate it. We only inflate a new View when the convertView supplied 
	    // by ListView is null. 
	    if (convertView == null) { 
	        convertView = mInflater.inflate(R.layout.item_course_name, null); 
	        // Creates a ViewHolder and store references to the two children views 
	        // we want to bind data to. 
	        holder = new ViewHolder(); 
	       // holder.button = (Button) convertView.findViewById(R.id.btn); 
	        holder.img=(ImageView)convertView.findViewById(R.id.course_name_img);
	        holder.courseName=(TextView)convertView.findViewById(R.id.course_name);
	        holder.courseInfo=(TextView)convertView.findViewById(R.id.course_note_info);
	        holder.editNameLayout=(RelativeLayout)convertView.findViewById(R.id.item_right);
	        convertView.setTag(holder); 
	    } else { 
	       // Get the ViewHolder back to get fast access to the TextView 
	        // and the ImageView. 
	        holder = (ViewHolder) convertView.getTag(); 
	    } 
	    final String name=names.get(position);
	    holder.courseName.setText(name);
	    LinearLayout.LayoutParams lp2 = new LayoutParams(100, LayoutParams.MATCH_PARENT);
        holder.editNameLayout.setLayoutParams(lp2);
        holder.editNameLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.e(DataConstants.TAG,"click edit");
				jumpToCourseNameEditActivity(name);
			}
		});
	    SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		// if(!DataConstants.dbHelper.tableIsExist(db,
		// getResources().getString(R.string.db_footprint_table)))
		int count=DataConstants.dbHelper.queryCourseRecordsCount(db,courseTableNames.get(position));
		db.close();
	    holder.courseInfo.setText(count+context.getResources().getString(R.string.piece));
	    if(courseTableNames.get(position).equals(context.getResources().getString(R.string.db_english_table)))
	    {
	    	Picasso.with(context).load(R.drawable.course_english).into(holder.img);
	    }
	    else if(courseTableNames.get(position).equals(context.getResources().getString(R.string.db_politics_table)))
	    {
	    	Picasso.with(context).load(R.drawable.course_politic).into(holder.img);
	    }
	    else if(courseTableNames.get(position).equals(context.getResources().getString(R.string.db_math_table)))
	    {
	    	Picasso.with(context).load(R.drawable.course_math).into(holder.img);
	    }
	    else if(courseTableNames.get(position).equals(context.getResources().getString(R.string.db_profess1_table)))
	    {
	    	Picasso.with(context).load(R.drawable.course_profess1).into(holder.img);
	    }
	    else if(courseTableNames.get(position).equals(context.getResources().getString(R.string.db_profess2_table)))
	    {
	    	Picasso.with(context).load(R.drawable.course_profess2).into(holder.img);
	    }
	    convertView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				Log.e(DataConstants.TAG, "list flip on touch");
				return false;
			}
		});
	    // Bind the data efficiently with the holder. 
//	    holder.button.setText(names.get(position)); 
//	    final int pos=position;
//	    holder.button.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent();
//				intent.setClass(context, ExerciseActivity.class);
//				boolean isFirstUse=UserConfigs.getIsFirstTakePhoto()==null?true:false;
//				if(isFirstUse)
//				{
//					intent.putExtra("tag", context.getResources().getString(R.string.first_use));
//				}
//				else
//				{
//					intent.putExtra("course_name",names.get(pos));
//					intent.putExtra("tag", context.getResources().getString(R.string.note_class));
//				}
//				context.startActivity(intent);
//			}
//		});
	    return convertView; 
	}
	public void refresh()
	{
		Log.e(DataConstants.TAG,"refresh");
		notifyDataSetChanged();
	}
	static class ViewHolder { 

	    Button button; 
	    ImageView img;
	    TextView courseName;
	    TextView courseInfo;
	    TextView editItem;
	    
	    RelativeLayout editNameLayout;

	} 
	private void jumpToCourseNameEditActivity(String course)
	{
//		Fragment fragment=new CourseNameEditFragment();
//		Bundle bundle = new Bundle();  
//        bundle.putString("course_edit_name", course);  
//        fragment.setArguments(bundle);
//		FragmentManager fm=((MainActivity)context).getFragmentManager();
//		FragmentTransaction trans = fm.beginTransaction();  
//		trans.replace(R.id.main_frame, fragment);
//		trans.addToBackStack(null);
//		trans.commit();
		Intent intent =new Intent();
		intent.setClass(context, CourseNameEditActivity.class);
		intent.putExtra("course_edit_name", course);
		context.startActivity(intent);
	}

}
