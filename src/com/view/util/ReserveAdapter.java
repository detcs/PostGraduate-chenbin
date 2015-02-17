package com.view.util;

import com.app.ydd.R;
import com.data.model.Reserve;
import com.data.util.DataBuffer;
import com.data.util.ReserveUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReserveAdapter extends BaseAdapter implements AdapterFresh {
	private Context context;
	protected DataBuffer<Reserve> buffer;

	public ReserveAdapter(Context context) {
		this.context = context;
		init();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return buffer.getCount();
	}

	@Override
	public Reserve getItem(int position) {
		// TODO Auto-generated method stub
		return buffer.getDataItem(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Reserve vg = getItem(position);
		if (null == vg) {
			return LayoutInflater.from(context).inflate(
					R.layout.list_item_wait, parent, false);
		}
		ViewHolder holder;
		if (null == convertView || null == convertView.getTag()) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.person_reserve_item, parent, false);
			holder = new ViewHolder();
			holder.vg = vg;
			holder.image = (ImageView) convertView
					.findViewById(R.id.imageView1);
			holder.titleView = (TextView) convertView
					.findViewById(R.id.titleView);
			holder.sourceView = (TextView) convertView
					.findViewById(R.id.sourceView);
			holder.timeView = (TextView) convertView
					.findViewById(R.id.timeView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// holder.image.setImageBitmap(null);
		holder.titleView.setText(vg.getTitle_());
		holder.sourceView.setText(vg.getSource_());
		holder.timeView.setText(vg.getTime_());
		return convertView;
	}

	public static class ViewHolder {
		public Reserve vg;
		ImageView image;
		TextView titleView;
		TextView sourceView;
		TextView timeView;
	}

	// *********************init*********************
	private void init() {
		initVariable();
	}

	protected void initVariable() {
		// init buffer
		buffer = new DataBuffer<Reserve>(this, new ReserveUtil());
	}

	// AdapterFresh
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		buffer.destroy();
	}

	@Override
	public void fresh() {
		// TODO Auto-generated method stub
		buffer.reset();
	}

}
