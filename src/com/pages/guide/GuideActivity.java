package com.pages.guide;

import com.app.ydd.R;
import com.data.model.UserConfigs;
import com.data.util.DisplayUtil;
import com.data.util.ImageUtil;
import com.pages.login.LoginActivity;
import com.pages.viewpager.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity{

	RelativeLayout layout;
	ImageView play;
	ImageView begin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		layout=(RelativeLayout) findViewById(R.id.welcome_layout);
		//layout.setBackgroundResource(R.drawable.welcome_bg);
		layout.setBackground(DisplayUtil.drawableTransfer(GuideActivity.this, R.drawable.welcome_bg));
		boolean isFirstUse = UserConfigs.getIsFirstTakePhoto() == null ? true
				: false;
		if(isFirstUse)
		{
			
			play=(ImageView) findViewById(R.id.play_video);
			play.setImageDrawable(DisplayUtil.drawableTransfer(GuideActivity.this, R.drawable.guide_play));
			begin=(ImageView) findViewById(R.id.begin);
			begin.setImageDrawable(DisplayUtil.drawableTransfer(GuideActivity.this, R.drawable.guide_begin));
			begin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					jumpActivity();
				}
			});
		}
		else
		{
			jumpActivity();
		}
		
	}
	private void jumpActivity()
	{
		Intent intent=new Intent();
		intent.setClass(GuideActivity.this, LoginActivity.class);
		startActivity(intent);
		layout.setBackground(null);
		finish();
	}

	
}
