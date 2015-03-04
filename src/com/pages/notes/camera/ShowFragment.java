package com.pages.notes.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.app.ydd.R;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.DatabaseHelper;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.data.util.DisplayUtil;
import com.data.util.NetWorkUtil;
import com.data.util.SysCall;
import com.data.util.UploadInfoUtil;
import com.data.util.UploadInfoUtil.UploadPictureTask;
import com.view.util.VerticalTextView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * last change:
 * 
 * authro:wsy
 * time:2/5
 * 
 * */
public class ShowFragment extends Fragment {
	private static final String TAG = "ShowFragment";
	private View rootView;
	private Bitmap bitmap;
	private ShowJump jump;
	private List<String> items;
	private ImageView imageView1;// 要显示的图片
	private String finalRemarks = "";
	private int selectIndex = -1;

	private LinearLayout hideBar;// hide
	private RelativeLayout hideChoose;
	private TextView quitView;
	private TextView saveView;
	private EditText remarksView;// is empty before edit

	// 所有要旋转的内容
	private boolean canRotate = true;
	private FrameLayout chooseBar;
	private TextView[] radios;
	private VerticalTextView[] radiosx;

	private ImageView againView;
	private ImageView editView;// click you can edit

	private ImageView saveBu;
	private ImageView[] views;

	private static final int[] layouts = new int[] {
			R.layout.camera_choose_left, R.layout.camera_choose_bar,
			R.layout.camera_choose_right };
	private static final int[] radiosId = new int[] { R.id.radioButton1,
			R.id.radioButton2, R.id.radioButton3, R.id.radioButton4 };

	// sensor
	private static final int DEGREE = 90;
	private SensorManager sensorMgr;
	private float x, y, z;
	private Matrix matrix;
	private int preState;

	public void setBitmap(Bitmap bitmap) {
		// TODO Auto-generated constructor stub
		this.bitmap = bitmap;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_show, container,
					false);
		}
		init(rootView);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof ShowJump)) {
			throw new IllegalStateException("error");
		}
		jump = (ShowJump) activity;
	}

	// ****************init****************
	private void init(View view) {
		initSensor();
		findVeiws(view);
		imageView1.setImageBitmap(bitmap);
		initItem(1);
		setListener();
		initImages();
	}

	private void initImages() {
		matrix = new Matrix();
		views = new ImageView[] { againView, editView, saveBu };
		preState = 0;
	}

	private void initSensor() {
		sensorMgr = (SensorManager) getActivity().getSystemService(
				Context.SENSOR_SERVICE);
		Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		SensorEventListener lsn = new SensorEventListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onSensorChanged(SensorEvent e) {
				// TODO Auto-generated method stub
				x = e.values[SensorManager.DATA_X];
				y = e.values[SensorManager.DATA_Y];
				z = e.values[SensorManager.DATA_Z];
				rotate(x);
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
			}
		};
		// 注册listener，第三个参数是检测的精确度
		sensorMgr.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_UI);
	}

	private void findVeiws(View view) {
		imageView1 = (ImageView) view.findViewById(R.id.imageView1);

		againView = (ImageView) view.findViewById(R.id.againView);
		editView = (ImageView) view.findViewById(R.id.editView);

		hideBar = (LinearLayout) view.findViewById(R.id.hideBar);
		hideChoose = (RelativeLayout) view.findViewById(R.id.hideChoose);
		quitView = (TextView) view.findViewById(R.id.quitView);
		saveView = (TextView) view.findViewById(R.id.saveView);
		remarksView = (EditText) view.findViewById(R.id.remarksView);
		// 设置字体
		Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
				"font/fangzhenglanting.ttf");
		remarksView.setTypeface(face);
		chooseBar = (FrameLayout) view.findViewById(R.id.chooseBar);

		saveBu = (ImageView) view.findViewById(R.id.saveBu);
	}

	@SuppressLint("ResourceAsColor")
	private void initItem(int chooseId) {// 这里的参数是指选择哪一种倾斜模式
		// init show subjects
		if (null == items) {
			items = new ArrayList<String>();
			List<String> cont = new ArrayList<String>();
			cont = UserConfigs.getCourseNames();
			String item;
			for (int i = 0; i < 4; i++) {
				if (i < cont.size()) {
					item = cont.get(i);
				} else {
					item = "王";
				}
				if (null == item || item.trim().equals("")) {
					item = "王";
				}
				items.add(item.charAt(0) + "");
			}
		}
		View choose = LayoutInflater.from(getActivity()).inflate(
				layouts[chooseId], null);// layouts can only be used here
		if (1 == chooseId) {
			radios = new TextView[4];
			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for (int i = 0; i < 4; i++) {
						if (radios[i] == v) {
							selectIndex = i;
							Log.i(TAG, "choose: " + selectIndex);
							radios[i].setTextColor(R.color.camera_choosen_word);
							radios[i].setBackground(getActivity()
									.getResources().getDrawable(
											R.drawable.camera_item_chosen));
						} else {
							radios[i].setTextColor(R.color.camera_choose_word);
							radios[i].setBackground(getActivity()
									.getResources().getDrawable(
											R.drawable.camera_item));
						}
					}
				}
			};
			for (int i = 0; i < 4; i++) {
				radios[i] = (TextView) choose.findViewById(radiosId[i]);
				// radios[i].setTypeface(face);
				radios[i].setText(items.get(i));
				radios[i].setOnClickListener(listener);
				if (i == selectIndex) {
					radios[i].setTextColor(R.color.camera_choosen_word);
					radios[i].setBackground(getActivity().getResources()
							.getDrawable(R.drawable.camera_item_chosen));
				} else {
					radios[i].setTextColor(R.color.camera_choose_word);
					radios[i].setBackground(getActivity().getResources()
							.getDrawable(R.drawable.camera_item));
				}
			}
		} else {
			radiosx = new VerticalTextView[4];
			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for (int i = 0; i < 4; i++) {
						if (radiosx[i] == v) {
							selectIndex = i;
							radiosx[i]
									.setTextColor(R.color.camera_choosen_word);
							radiosx[i].setBackground(getActivity()
									.getResources().getDrawable(
											R.drawable.camera_item_chosen));
						} else {
							radiosx[i].setTextColor(R.color.camera_choose_word);
							radiosx[i].setBackground(getActivity()
									.getResources().getDrawable(
											R.drawable.camera_item));
						}
					}
				}
			};
			for (int i = 0; i < 4; i++) {
				radiosx[i] = (VerticalTextView) choose
						.findViewById(radiosId[i]);
				// radiosx[i].setTypeface(face);
				radiosx[i].setText(items.get(i));
				radiosx[i].setOnClickListener(listener);
				if (i == selectIndex) {
					radiosx[i].setTextColor(R.color.camera_choosen_word);
					radiosx[i].setBackground(getActivity().getResources()
							.getDrawable(R.drawable.camera_item_chosen));
				} else {
					radiosx[i].setTextColor(R.color.camera_choose_word);
				}
				radiosx[i].setTextSize(getActivity().getResources()
						.getDimension(R.dimen.camera_item_text));
				radiosx[i].setBackground(getActivity().getResources()
						.getDrawable(R.drawable.camera_item));
			}
		}

		chooseBar.removeAllViews();
		chooseBar.addView(choose);
	}

	private void setListener() {
		againView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jump.take();
			}
		});

		editView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				canRotate = false;
				String remarks = remarksView.getText().toString();
				if (remarks.trim().equals("")) {
					showRemark();
				} else {
					// change remarks
					canEdit();
				}
			}
		});

		quitView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				canRotate = true;
				hideRemark();
			}
		});
		saveView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				canRotate = true;
				String remarks = remarksView.getText().toString().trim();
				// no mater whether has remarks: do as has remarks
				finalRemarks = remarks;
				// editView.setText("修改备注");
				hideChoose.setVisibility(View.INVISIBLE);
				// remarksView.clearFocus();这样还是可以获得焦点的
				cannotEdit();
			}
		});

		saveBu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// wsy 2/5
				SysCall.hint(getActivity(), "已保存");
				save(selectIndex);
				jump.take();
			}
		});
	}

	// ****************interface call back****************
	public interface ShowJump {
		public void take();
	}

	// *************sensor util*************

	private void rotate(float x) {
		if (!canRotate) {
			return;
		}
		int state = 0;
		if (x > 7) {// left
			state = 1;
		} else if (x < -7) {// right
			state = -1;
		}
		if (state == preState) {
			return;
		} else {
			// "rotate" choose items
			initItem(state + 1);
			// rotate images
			// Animation a = AnimationUtil.rotateAnimation(getActivity(),
			// (state - preState) * DEGREE);
			// a.setFillAfter(true);
			// pickView.startAnimation(a);
			matrix.reset();
			matrix.setRotate((state - preState) * DEGREE); // 设置旋转
			// 注意要正方形的，否则宽度大于原本的就会有问题
			Bitmap contantBm;
			Bitmap showBm;
			for (int i = 0; i < 3; i++) {
				views[i].setDrawingCacheEnabled(true);
				contantBm = views[i].getDrawingCache();
				showBm = Bitmap.createBitmap(contantBm, 0, 0,
						contantBm.getHeight(), contantBm.getWidth(), matrix,
						true);
				views[i].setImageBitmap(showBm);
				views[i].setDrawingCacheEnabled(false);
			}
			preState = state;
		}
	}

	// ****************view util****************
	private void canEdit() {
		hideChoose.setVisibility(View.VISIBLE);
		SysCall.bumpSoftInput(remarksView, getActivity());
	}

	@SuppressLint("ResourceAsColor")
	private void cannotEdit() {
		SysCall.hideSoftInput(rootView, getActivity());
		remarksView.clearFocus();
		remarksView.setFocusable(false);
		remarksView.setFocusableInTouchMode(false);
		// remarksView.setBackgroundColor(R.color.camera_float_bar);
	}

	private void showRemark() {
		hideBar.setVisibility(View.VISIBLE);
		SysCall.bumpSoftInput(remarksView, getActivity());
	}

	private void hideRemark() {
		remarksView.setText(finalRemarks);
		if (finalRemarks.equals("")) {
			hideBar.setVisibility(View.INVISIBLE);
		} else {
			hideChoose.setVisibility(View.INVISIBLE);
		}
		cannotEdit();
	}
	// *******************save util*******************

	public String saveMyBitmap(Bitmap bitmap,String path) throws IOException {
		File f = new File(path);
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//String base64=FileDataHandler.getBase64ImageStr(path);
		return null;
	}

	@SuppressLint("SimpleDateFormat")
	private void save(int index) {
		if (!FileDataHandler.sdCardExist())// no sd card
		{

		} else {
			String storePath = FileDataHandler.APP_DIR_PATH + "/";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
			String time = sdf.format(new Date());
			// test date
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(new Date());
			// test date
			String photoName = UserConfigs.getAccount() + "|" + time + ".jpg";
			try {

				String tableName = null;
				if (index == 0) {
					tableName = getResources().getString(
							R.string.db_english_table);
					storePath += getResources().getString(R.string.dir_english);
				} else if (index == 1) {
					tableName = getResources().getString(
							R.string.db_politics_table);
					storePath += getResources()
							.getString(R.string.dir_politics);
				} else if (index == 2) {
					if (UserConfigs.getCourseMathName() != null) {
						tableName = getResources().getString(
								R.string.db_math_table);
						storePath += getResources()
								.getString(R.string.dir_math);
					} else {
						tableName = getResources().getString(
								R.string.db_profess1_table);
						storePath += getResources().getString(
								R.string.dir_profess1);
					}
				} else if (index == 3) {
					if (UserConfigs.getCourseProfessOneName() == null) {
						tableName = getResources().getString(
								R.string.db_profess1_table);
						storePath += getResources().getString(
								R.string.dir_profess1);
					} else {
						tableName = getResources().getString(
								R.string.db_profess2_table);
						storePath += getResources().getString(
								R.string.dir_profess2);
					}
				}
				Log.e(DataConstants.TAG, "save " + storePath);
				saveMyBitmap(bitmap,storePath + "/" + photoName);
				BitmapFactory.Options opt=new BitmapFactory.Options();
				opt.inSampleSize=4;
				Bitmap blurBitmap=BitmapFactory.decodeFile(storePath + "/" + photoName, opt);
				blurBitmap=DisplayUtil.doBlur(blurBitmap, 10, false);
				saveMyBitmap(blurBitmap,storePath + "/" + DataConstants.blured+photoName);
				savePhotoRecordToDataBase(tableName, photoName, "photobase64",
						finalRemarks, date, time, 0,storePath + "/" + photoName);
				blurBitmap.recycle();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void savePhotoRecordToDataBase(String tableName, String photoName,
			String photobase64, String remark, String date, String time,
			int flag,String photoPath) {
		DatabaseHelper dbHelper = DataConstants.dbHelper;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		CourseRecordInfo cri = new CourseRecordInfo(photoName, photobase64,
				remark, date, time, getResources().getString(
						R.string.state_unknow), getResources().getString(
						R.string.upload_no), flag, 0, 0,0);
		dbHelper.insertCourseRecord(getActivity(), db, tableName, cri);
		//DatabaseHelper.queryShowRecords(db, tableName);
		if(NetWorkUtil.isWifiConnected(getActivity()))
		{
			CourseRecordInfo criWithID=dbHelper.queryCourseRecordByPhotoName(getActivity(), db, tableName, photoName);	
			//Log.e(DataConstants.TAG, "to64 "+photoPath);
			String imgBase64=FileDataHandler.getBase64ImageStr(photoPath);
			UploadInfoUtil.uploadImg(imgBase64,getActivity(), criWithID, tableName);
			//new UploadInfoUtil.UploadPictureTask(imgBase64).execute(DataConstants.SERVER_URL);
			//UploadInfoUtil.uploadQuesImg(getActivity(), criWithID, tableName, imgBase64);
		}
		db.close();
	}
}
