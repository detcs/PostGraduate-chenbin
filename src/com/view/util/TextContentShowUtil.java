package com.view.util;

import com.app.ydd.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class TextContentShowUtil {

	Context context;
	String content;
	TextView contentTv;
	public TextContentShowUtil(Context context,String content) {
		super();
		this.context = context;
		//initView();
	}

	private View getTextContentView(Context context,String content)
	{
		LayoutInflater mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=mInflater.inflate(R.layout.view_text_content, null); 
		contentTv=(TextView) view.findViewById(R.id.text_remark_content);
		contentTv.setText(content);
		return view;
	}
}
