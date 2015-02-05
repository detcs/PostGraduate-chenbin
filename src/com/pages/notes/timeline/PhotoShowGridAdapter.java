package com.pages.notes.timeline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.squareup.picasso.Picasso;

public class PhotoShowGridAdapter extends BaseAdapter
{

	List<String> imgPaths;
	Context context;
	LayoutInflater mInflater;
	boolean chooseState=false;
	//List<String> choosedPhotoPaths;
	public PhotoShowGridAdapter(Context context,List<String> imgPaths,boolean chooseState) {
		super();
		this.imgPaths = imgPaths;
		this.context=context;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.chooseState=chooseState;
		//choosedPhotoPaths=new ArrayList<String>();
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
		//Log.e(DataConstants.TAG,"photoshow getview"+chooseState);
		GridViewHolder holder; 
	    if (convertView == null) { 
	        convertView = mInflater.inflate(R.layout.item_timeline_grid_item, null); 
	        holder = new GridViewHolder(); 
	        holder.img = (ImageView) convertView.findViewById(R.id.exercise_timeline_img); 
	        holder.chooseFlag=(ImageView) convertView.findViewById(R.id.choose_flag); 
	        convertView.setTag(holder); 
	    } else { 
	        holder = (GridViewHolder) convertView.getTag(); 
	    }
	    // Bind the data efficiently with the holder.
	    //Log.e(DataConstants.TAG,"pos:"+position+" path:"+ imgPaths.get(position));
	    int width=(DataConstants.screenWidth-10)/4;
	   // Log.e(DataConstants.TAG,"chooseState:"+chooseState);
//	    if(chooseState)
//	    	holder.chooseFlag.setVisibility(View.VISIBLE);
//	    else
//	    	holder.chooseFlag.setVisibility(View.INVISIBLE);
	    Picasso.with(context).load(new File(imgPaths.get(position))).centerInside().resize(width,width).into(holder.img);
	    if(chooseState)
	    {
	    	holder.img.setEnabled(true);
	    	holder.img.setOnClickListener(new ChooseStateButtonClickListener(imgPaths.get(position), holder));
	    }
	    else
	    {   
	    	if(holder.chooseFlag.getVisibility()==View.VISIBLE)
			holder.chooseFlag.setVisibility(View.INVISIBLE);
	    	holder.img.setEnabled(false);
	    }
	    return convertView; 
	}
	public void updateChooseState(boolean chooseState)
	{
		this.chooseState=chooseState;
		//Log.e(DataConstants.TAG,"photoshow updatechoose "+chooseState);
		//ReviewChooseFragment.choosedPhotoPaths=new ArrayList<String>();
		notifyDataSetChanged();
		
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
				holder.chooseFlag.setVisibility(View.INVISIBLE);
				ReviewChooseFragment.choosedPhotoPaths.remove(path);
				Log.e(DataConstants.TAG, "choose remove "+ReviewChooseFragment.choosedPhotoPaths.size());
			}
		}
		
	}
	
}

class GridViewHolder { 

    ImageView img; 
    ImageView chooseFlag; 
} 
