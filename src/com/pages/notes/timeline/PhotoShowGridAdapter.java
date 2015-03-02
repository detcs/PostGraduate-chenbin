package com.pages.notes.timeline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.ydd.R;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.DataConstants.PageName;
import com.data.util.DisplayUtil;
import com.data.util.PhotoNamePathUtil;
import com.pages.notes.ExerciseActivity;
import com.pages.notes.ReviewFragment;
import com.pages.notes.SingleNoteFragment;
import com.pages.notes.footprint.PhotoBrowseActivity;
import com.squareup.picasso.Picasso;

public class PhotoShowGridAdapter extends BaseAdapter
{

	List<String> imgPaths;
	Context context;
	LayoutInflater mInflater;
	boolean chooseState=false;
	String tableName;
	PageName fromPage;
	public PhotoShowGridAdapter(Context context,List<String> imgPaths,boolean chooseState,String tableName,PageName fromPage) {
		super();
		this.imgPaths = imgPaths;
		this.context=context;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.chooseState=chooseState;
		this.tableName=tableName;
		this.fromPage=fromPage;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgPaths.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return imgPaths.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//Log.e(DataConstants.TAG,"photoshow getview "+position+" "+chooseState);
		GridViewHolder holder; 
	    if (convertView == null) { 
	        convertView = mInflater.inflate(R.layout.item_timeline_grid_item, null); 
	        holder = new GridViewHolder(); 
	        holder.img = (ImageView) convertView.findViewById(R.id.exercise_timeline_img); 
	        holder.chooseFlag=(ImageView) convertView.findViewById(R.id.choose_flag); 
	        holder.importanceFlag=(ImageView) convertView.findViewById(R.id.importance_flag); 
	        SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
	 	    CourseRecordInfo cri= DataConstants.dbHelper.queryCourseRecordByPhotoName(context, db, tableName, PhotoNamePathUtil.pathToPhotoName(imgPaths.get(position)));
	        if(cri.getFlag()==1)
	        	holder.flag=1;
	        else
	        	holder.flag=0;
	        db.close();
	 	    convertView.setTag(holder); 
	    } else { 
	        holder = (GridViewHolder) convertView.getTag(); 
	    }
	    // Bind the data efficiently with the holder.
	    //Log.e(DataConstants.TAG,"pos:"+position+" path:"+ imgPaths.get(position));
	    Log.e(DataConstants.TAG,"convertView.getWidth"+convertView.getWidth());
	    int width=(DataConstants.screenWidth-10)/4;
	   // Log.e(DataConstants.TAG,"chooseState:"+chooseState);
//	    if(chooseState)
//	    	holder.chooseFlag.setVisibility(View.VISIBLE);
//	    else
//	    	holder.chooseFlag.setVisibility(View.INVISIBLE);
	    //Picasso.with(context).load(new File(imgPaths.get(position))).centerInside().resize(width,width).into(holder.img);
	    FrameLayout.LayoutParams param=new FrameLayout.LayoutParams(width,width);
	   // holder.img.setLayoutParams(param);
	   Picasso.with(context).load(new File(imgPaths.get(position))).resize(width,width).into(holder.img);
	   if(holder.flag==1)
	    	{
				holder.importanceFlag.setVisibility(View.VISIBLE);
				Picasso.with(context).load(R.drawable.importance).into(holder.importanceFlag); 
			}
	  
	   if(chooseState)
	    {
//		   if(holder.flag==1)
//	    	{
//				holder.chooseFlag.setVisibility(View.VISIBLE);
//				Picasso.with(context).load(R.drawable.importance).into(holder.chooseFlag); 
//			}
	    	holder.img.setEnabled(true);
	    	holder.img.setOnClickListener(new ChooseStateButtonClickListener(imgPaths.get(position), holder));
	    }
	    else
	    {   
//	    	if(holder.flag==1)
//	    	{
//				holder.chooseFlag.setVisibility(View.VISIBLE);
//				Picasso.with(context).load(R.drawable.importance).into(holder.chooseFlag); 
//			}
//	    	else
	    	{
	    		if(holder.chooseFlag.getVisibility()==View.VISIBLE)
	    			holder.chooseFlag.setVisibility(View.INVISIBLE);
	    	}
	    	holder.img.setOnClickListener(new CheckSingleNoteClickListener(imgPaths.get(position)));
	    }
		
	    return convertView; 
	}
	public void updateChooseState(boolean chooseState)
	{
		this.chooseState=chooseState;
		Log.e(DataConstants.TAG,"photoshow updatechoose "+chooseState);
		//ReviewChooseFragment.choosedPhotoPaths=new ArrayList<String>();
		notifyDataSetChanged();
		
	}
	class CheckSingleNoteClickListener implements OnClickListener
	{

		String path;
		
		public CheckSingleNoteClickListener(String path) {
			super();
			this.path = path;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			jumpToReview(path);
		}
		
	}
	class ChooseStateButtonClickListener implements OnClickListener
	{

		String path;
		GridViewHolder holder;
		public ChooseStateButtonClickListener(String path,GridViewHolder holder) {
			super();
			this.path = path;
			this.holder=holder;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(holder.chooseFlag.getVisibility()==View.INVISIBLE)
			{
				holder.chooseFlag.setVisibility(View.VISIBLE);
				ReviewChooseFragment.choosedPhotoPaths.add(path);
				Log.e(DataConstants.TAG, "choose add "+ReviewChooseFragment.choosedPhotoPaths.size());
				
			}
			else
			{
				
				{
					holder.chooseFlag.setVisibility(View.INVISIBLE);
					ReviewChooseFragment.choosedPhotoPaths.remove(path);
				}
					
				
				Log.e(DataConstants.TAG, "choose remove "+ReviewChooseFragment.choosedPhotoPaths.size());
				
			}
		}
		
	}
	public void jumpToReview(String path)
	{
		/*
		Fragment fragment=new SingleNoteFragment();
		Bundle bundle = new Bundle();  
        bundle.putString("type", "");
        bundle.putString("single_path", path);
        bundle.putString("single_tablename",tableName);
       // Log.e(DataConstants.TAG,"send singlepath:"+path);
       // bundle.putString("course_table_name", tableName);
        fragment.setArguments(bundle);
		FragmentManager fm=((ExerciseActivity)context).getFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();  
		trans.replace(R.id.exercise_frame, fragment);
		trans.addToBackStack(null);
		trans.commit();
		*/

		String[] info=path.split("/");
		String date=info[info.length-1].split("\\|")[1];
		Log.e(DataConstants.TAG, "jump "+info[info.length-1]+" "+date);
		Intent intent=new Intent();
		intent.setClass(context, PhotoBrowseActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString("jump_tag", context.getResources().getString(R.string.jump_tag_footprint));
		bundle.putSerializable("from_page", fromPage);
		bundle.putString("date",date);
		bundle.putString("table_name",tableName);
		intent.putExtra("photo_show_bundle", bundle);
		//intent.putStringArrayListExtra(, (ArrayList<String>)imgPaths);
		context.startActivity(intent);
	
	}
}

class GridViewHolder { 

    ImageView img; 
    ImageView chooseFlag;
    ImageView importanceFlag;
    int flag;
} 
