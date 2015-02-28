package com.view.util;

import com.app.ydd.R;
import com.data.model.DataConstants;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TextContentShowUtil {

	//Context context;
	String content;
	TextView contentTv;
	TextView extendImg;
	Activity mActivity;
	//Typeface typeFZLT;
	public TextContentShowUtil(Activity activity,String content) {
		super();
		//this.context = context;
		this.mActivity=activity;
		//initView();
		this.content=content;
		//typeFZLT=Typeface.createFromAsset(context.getAssets(), "font/");
	}

	public void setTextContent(String content,boolean contract)
	{
		this.content=content;
		String tempContent = null;
		if(contract)
		{
			if(content.length()>54)
				tempContent=content.substring(0, 35)+"...";
			else
				tempContent=content;
		}
		else
		{
			tempContent=content;
		}
		contentTv.setText(tempContent);
	}
	public void truncateText()
	{
		
	}
	public TextView getTextView()
	{
		return contentTv;
	}
	public View getTextContentView()
	{
		LayoutInflater mInflater=(LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View view=mInflater.inflate(R.layout.view_text_content, null); 
		contentTv=(TextView) mActivity.findViewById(R.id.text_remark_content);
		contentTv.setText(content);
		contentTv.setTypeface(DataConstants.typeFZLT);
		contentTv.setTextSize(12);
		contentTv.setMovementMethod(ScrollingMovementMethod.getInstance());
		extendImg=(TextView) mActivity.findViewById(R.id.extends_img);
		extendImg.setOnClickListener(new OnClickListener() {
			Boolean flag = true;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(flag){
			        flag = false;
			        contentTv.setEllipsize(null); // 展开
			        contentTv.setSingleLine(flag);
			        contentTv.setLines(4);
			        setTextContent(content, false);
			        //contentTv.setPadding(0, 0, 0, 0);
			        }else{
			        	Log.e(DataConstants.TAG,"remarkcontent "+flag);
			          flag = true;
			          contentTv.setEllipsize(android.text.TextUtils.TruncateAt.END); // 收缩
			          contentTv.setLines(2);
			          setTextContent(content, true);
			    }
			}
		});
		return null;
	}
}
