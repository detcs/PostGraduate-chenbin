package com.pages.viewpager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.NormalPostRequest;
import com.data.model.UserConfigs;
import com.data.util.GloableData;
import com.data.util.SysCall;
import com.pages.funsquare.ButtonsGridViewAdapter;
import com.pages.funsquare.FunctionsSquareFragment;
import com.pages.funsquare.square.SquareFragment;
import com.pages.login.LoginActivity;
import com.pages.notes.CourseSetting;
import com.pages.notes.ExerciseActivity;
import com.pages.notes.NoteFragment;
import com.pages.notes.NotesClassAdapter;
import com.pages.notes.camera.CameraActivity;
import com.pages.notes.footprint.DownloadTask;
import com.pages.notes.footprint.FootPrintActivity;
import com.pages.notes.footprint.FootprintInfo;
import com.pages.today.TodayFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private com.mobovip.views.DirectionalViewPager viewPager;
	final ArrayList<Fragment> fragList = new ArrayList<Fragment>();
	FragmentManager fm;
	TodayFragment todayFragment;
	NoteFragment noteFragment;
	FunctionsSquareFragment funtionsSquareFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// wsy 2015/2/3
		GloableData.init(getApplicationContext());

		LayoutInflater mInflater = getLayoutInflater();	
		todayFragment=new TodayFragment();
		noteFragment=new NoteFragment();
		funtionsSquareFragment=new FunctionsSquareFragment();
		fragList.add(todayFragment);
		fragList.add(noteFragment);
		fragList.add(funtionsSquareFragment);
		todayFragment.setActivity(this);
		viewPager = (com.mobovip.views.DirectionalViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragList));
		//viewPager.setAdapter(new MyPagerAdapter(listViews));
		viewPager.setOrientation(com.mobovip.views.DirectionalViewPager.VERTICAL);
		viewPager.setSaveEnabled(false);// else nullpoint when jump
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				// btn.setVisibility(position==listViews.size()-1?View.VISIBLE:View.GONE);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		//onCreate()里注册BroadcastReceiver
        //onDestroy()里解除注册
        //下面的"giuz.receiver.music"要在manifest.xml里注册　　
        IntentFilter filter = new IntentFilter(DataConstants.MUSIC_SERVICE);
        registerReceiver(updateUIReceiver, filter);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(updateUIReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	 public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	        

			public List<Fragment> mFraList;
			public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> mFraList) {
				super(fm);
				this.mFraList=mFraList;
				// TODO Auto-generated constructor stub
			}
			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mFraList.get(arg0);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mFraList.size();
			}

	       
	    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		Log.e(DataConstants.TAG, "resultCode"+resultCode+" requestcode"+requestCode+" ");
//		if(requestCode==DataConstants.REQUEST_CODE_CAMERA | requestCode==DataConstants.REQUEST_CODE_EXERCISE)
//		{
//			Log.e(DataConstants.TAG, resultCode+"");
//			noteFragment.updateNoteClassList();
//	
//		}
	}
	//定义一个BroadcastReceiver
    private BroadcastReceiver updateUIReceiver = new BroadcastReceiver() {
        //当service发出广播后，此方法就可以得到service传回来的值
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新界面。这里改变Button的值
            //得到intent返回来的值,0表示此时是播放，1表示暂停, 2是停止
            int backFlag = intent.getExtras().getInt("backFlag");
            switch(backFlag){
            case 0:
                //btnStartOrPause.setText("暂停");
            	Log.e(DataConstants.TAG, "暂停");
                break;
            case 1:
            case 2:
                //btnStartOrPause.setText("播放");
            	Log.e(DataConstants.TAG, "播放");
                break;
            }
        }
    };
    public void startMusicService(Intent intent)
    {
    	startService(intent);
    }
    public void stopMusicService(Intent intent)
    {
    	stopService(intent);
    }

}
