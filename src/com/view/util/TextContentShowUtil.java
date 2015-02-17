package com.view.util;

import com.app.ydd.R;
import com.data.model.DataConstants;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TextContentShowUtil {

	Context context;
	String content;
	TextView contentTv;
	TextView extendImg;
	//Typeface typeFZLT;
	public TextContentShowUtil(Context context,String content) {
		super();
		this.context = context;
		//initView();
		this.content=content;
		//typeFZLT=Typeface.createFromAsset(context.getAssets(), "font/");
	}

	public void setTextContent(String content)
	{
		this.content=content;
		contentTv.setText(content);
	}
	public View getTextContentView()
	{
		LayoutInflater mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=mInflater.inflate(R.layout.view_text_content, null); 
		contentTv=(TextView) view.findViewById(R.id.text_remark_content);
		contentTv.setText(content);
		contentTv.setTypeface(DataConstants.typeFZLT);
		contentTv.setTextSize(12);
		extendImg=(TextView) view.findViewById(R.id.extends_img);
		extendImg.setOnClickListener(new OnClickListener() {
			Boolean flag = true;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(flag){
			        flag = false;
			        contentTv.setEllipsize(null); // 展开
			        contentTv.setSingleLine(flag);
			        }else{
			        	Log.e(DataConstants.TAG,"remarkcontent "+flag);
			          flag = true;
			          contentTv.setEllipsize(android.text.TextUtils.TruncateAt.END); // 收缩
			          contentTv.setLines(2);
			    }
			}
		});
		return view;
	}
}
