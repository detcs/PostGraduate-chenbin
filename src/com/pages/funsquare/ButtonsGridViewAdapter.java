package com.pages.funsquare;

import java.util.List;

import com.app.ydd.R;
import com.data.util.DisplayUtil;
import com.pages.funsquare.contact.ContactActivity;
import com.pages.funsquare.essence.EssenseActivity;
import com.pages.funsquare.person.PersonActivity;
import com.pages.funsquare.print.PrintActivity;
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
import android.widget.ImageView;
import android.widget.TextView;

public class ButtonsGridViewAdapter extends BaseAdapter {

	List<String> names;
	Context context;
	LayoutInflater mInflater;

	public ButtonsGridViewAdapter(List<String> names, Context context) {
		super();
		this.names = names;
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		// When convertView is not null, we can reuse it directly, there is no
		// need
		// to reinflate it. We only inflate a new View when the convertView
		// supplied
		// by ListView is null.
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_button_func, null);
			// Log.v("tag", "positon " + position + " convertView is null, " +
			// "new: " + convertView);
			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			// holder.button = (Button) convertView.findViewById(R.id.btn);
			holder.img = (ImageView) convertView
					.findViewById(R.id.function_icon);
			holder.tv = (TextView) convertView.findViewById(R.id.function_name);
			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}
		// Bind the data efficiently with the holder.
		holder.tv.setText(names.get(position));
		if (names.get(position).equals(
				context.getResources().getString(R.string.essence))) {
			holder.img.setBackground(DisplayUtil.drawableTransfer(context, R.drawable.function_essence));
		} else if (names.get(position).equals(
				context.getResources().getString(R.string.square))) {
			holder.img.setBackground(DisplayUtil.drawableTransfer(context, R.drawable.function_square));
		} else if (names.get(position).equals(
				context.getResources().getString(R.string.backup))) {
			holder.img.setBackground(DisplayUtil.drawableTransfer(context, R.drawable.function_backup));
		} else if (names.get(position).equals(
				context.getResources().getString(R.string.personal_center))) {
			holder.img.setBackground(DisplayUtil.drawableTransfer(context, R.drawable.function_person_center));
		} else if (names.get(position).equals(
				context.getResources().getString(R.string.vip))) {
			holder.img.setBackground(DisplayUtil.drawableTransfer(context, R.drawable.function_vip));
		} else if (names.get(position).equals(
				context.getResources().getString(R.string.print))) {
			holder.img.setBackground(DisplayUtil.drawableTransfer(context, R.drawable.function_print));
		} else if (names.get(position).equals(
				context.getResources().getString(R.string.contact_us))) {
			holder.img.setBackground(DisplayUtil.drawableTransfer(context, R.drawable.function_contact));
		}

		if (names.get(position).equals(
				context.getResources().getString(R.string.essence))) {
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, EssenseActivity.class);
					context.startActivity(intent);
				}
			});
		} else if (names.get(position).equals(
				context.getResources().getString(R.string.square))) {
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, SquareActivity.class);
					context.startActivity(intent);
				}
			});
		} else if (names.get(position).equals(
				context.getResources().getString(R.string.backup))) {
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, ReservedActivity.class);
					context.startActivity(intent);
				}
			});
		} else if (names.get(position).equals(
				context.getResources().getString(R.string.personal_center))) {
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, PersonActivity.class);
					context.startActivity(intent);
				}
			});
		} else if (names.get(position).equals(
				context.getResources().getString(R.string.print))) {
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, PrintActivity.class);
					context.startActivity(intent);
				}
			});
		} else if (names.get(position).equals(
				context.getResources().getString(R.string.contact_us))) {
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, ContactActivity.class);
					context.startActivity(intent);
				}
			});
		}

		return convertView;
	}

	static class ViewHolder {

		ImageView img;
		TextView tv;

	}
}
