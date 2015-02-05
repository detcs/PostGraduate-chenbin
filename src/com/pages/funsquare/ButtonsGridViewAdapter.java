package com.pages.funsquare;

import java.util.List;









import com.app.ydd.R;
import com.pages.funsquare.essence.EssenseActivity;
import com.pages.funsquare.reserve.ReservedActivity;
import com.pages.funsquare.square.SquareActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ButtonsGridViewAdapter extends BaseAdapter{

	List<String> names;
	Context context;
	LayoutInflater mInflater;
	public ButtonsGridViewAdapter(List<String> names,Context context) {
		super();
		this.names = names;
		this.context=context;
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
	        convertView = mInflater.inflate(R.layout.item_button_func, null); 
	        //Log.v("tag", "positon " + position + " convertView is null, " + "new: " + convertView); 
	        // Creates a ViewHolder and store references to the two children views 
	        // we want to bind data to. 
	        holder = new ViewHolder(); 
	        holder.button = (Button) convertView.findViewById(R.id.btn); 
	        convertView.setTag(holder); 
	    } else { 
	        // Get the ViewHolder back to get fast access to the TextView 
	        // and the ImageView. 
	        holder = (ViewHolder) convertView.getTag(); 
	    } 
	    // Bind the data efficiently with the holder. 
	    holder.button.setText(names.get(position));
	    if(names.get(position).equals(context.getResources().getString(R.string.essence)))
	    {
	    	holder.button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.setClass(context,EssenseActivity.class);
					context.startActivity(intent);
				}
			});
	    }
	    else if(names.get(position).equals(context.getResources().getString(R.string.square)))
	    {
	    	holder.button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.setClass(context,SquareActivity.class);
					context.startActivity(intent);
				}
			});
	    }
	    else if(names.get(position).equals(context.getResources().getString(R.string.backup)))
	    {
	    	holder.button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.setClass(context,ReservedActivity.class);
					context.startActivity(intent);
				}
			});
	    }
	    
	    return convertView; 
	}
	static class ViewHolder { 

	    Button button; 

	} 
}
