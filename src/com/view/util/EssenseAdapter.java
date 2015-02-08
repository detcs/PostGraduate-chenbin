package com.view.util;

import com.app.ydd.R;
import com.data.model.Essense;
import com.data.util.DataBuffer;
import com.data.util.EssenseUtil;
import com.data.util.SysCall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * author:wsy
 * time:2015/1/29
 * last change:2015/1/29
 * 
 * */

public class EssenseAdapter extends BaseAdapter implements AdapterFresh {
	private Context context;
	protected DataBuffer<Essense> buffer;
	private int type;
	private static ListDownEssense callback;

	public EssenseAdapter(Context context, int dataType,
			ListDownEssense callbackin) {
		// TODO Auto-generated constructor stub
		this.context = context;
		type = dataType;
		callback = callbackin;
		init();
	}

	@Override
	public int getCount() {
		return buffer.getCount();
	}

	@Override
	public Essense getItem(int position) {
		return buffer.getDataItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Essense vg = getItem(position);
		if (null == vg) {
			return LayoutInflater.from(context).inflate(
					R.layout.list_item_wait, parent, false);
		}
		ViewHolder holder;
		if (null == convertView || null == convertView.getTag()) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.essense_list_item, parent, false);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.titleView);
			holder.author = (TextView) convertView
					.findViewById(R.id.authorView);
			holder.time = (TextView) convertView.findViewById(R.id.timeView);
			holder.typeImage = (ImageView) convertView
					.findViewById(R.id.typeImage);
			holder.downImage = (ImageView) convertView
					.findViewById(R.id.downImage);
			holder.shareImg = (ImageView) convertView
					.findViewById(R.id.shareImg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.author.setText(vg.getAuthor());
		holder.title.setText(vg.getTitle());
		holder.time.setText(vg.getTime());
		
		SysCall.error("wait for picture");
		// if (1 == vg.getHasDownload_()) {
		// holder.downImage.setImageBitmap(null);
		// holder.typeImage.setImageBitmap(null);
		// if (1 == vg.getNeedShare_()) {
		// holder.shareImg.setImageBitmap(null);
		// }
		// }

		holder.e = vg;
		holder.id = vg.getId();
		holder.init();
		return convertView;
	}

	public static class ViewHolder {
		ImageView typeImage;
		ImageView downImage;
		ImageView shareImg;
		TextView title;
		TextView author;
		TextView time;
		public Essense e;
		public String id;

		void init() {
			downImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					callback.down(e);
				}
			});
		}
	}

	// *********************init*********************
	private void init() {
		initVariable();
	}

	protected void initVariable() {
		// init buffer
		buffer = new DataBuffer<Essense>(this, new EssenseUtil(type));
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		buffer.destroy();
	}

	public interface ListDownEssense {
		public void down(Essense e);
	}
}
