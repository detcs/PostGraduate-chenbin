package com.pages.notes.camera;

import com.app.ydd.R;
import com.data.util.SysCall;
import com.pages.notes.camera.MySurfaceView.MyCallBack;
import com.view.util.AnimationUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

public class TakeFragment extends Fragment implements MyCallBack {
	// private static final String TAG = "TakeFragment";
	private static final int TAKEFRAGMENT = 100;
	private TakeJump jump;
	private Activity activity;
	private View rootView;
	private MySurfaceView surfaceView;

	private static final int DEGREE = 90;
	private SensorManager sensorMgr;
	private float x, y, z;
	private Matrix matrix;
	private int preState;

	private ImageView closeView;
	private ImageView pickView;
	private ImageView takeBu;
	private ImageView[] views;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_take, container,
					false);
		}
		init(rootView);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		if (!(activity instanceof TakeJump)) {
			throw new IllegalStateException("error");
		}
		jump = (TakeJump) activity;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TAKEFRAGMENT && resultCode == Activity.RESULT_OK
				&& data != null) {

			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = activity.getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			jump.show(BitmapFactory.decodeFile(picturePath));
		}
	}

	// ************interface call back************
	public interface TakeJump {
		public void show(Bitmap bitmap);
	}

	@Override
	public void afterTake(Bitmap bitmap) {
		// TODO Auto-generated method stub
		jump.show(bitmap);
	}

	// ************init************
	private void init(View view) {
		initSensor();
		findViews(view);
		setListener();
		initImage();
	}

	private void initImage() {
		matrix = new Matrix();
		views = new ImageView[] { closeView, takeBu, pickView };
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

	private void findViews(View view) {
		closeView = (ImageView) view.findViewById(R.id.closeView);
		pickView = (ImageView) view.findViewById(R.id.pickView);
		takeBu = (ImageView) view.findViewById(R.id.takeBu);
		surfaceView = (MySurfaceView) view.findViewById(R.id.mySurfaceView);
	}

	private void setListener() {
		closeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		pickView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				startActivityForResult(intent, TAKEFRAGMENT);
			}
		});
		takeBu.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// ����ʱ�Զ��Խ�
					surfaceView.autoFocus();
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// �ſ�������
					surfaceView.takePicture(TakeFragment.this);
				}
				return true;
			}
		});
	}

	// *************sensor util*************

	private void rotate(float x) {
		int state = 0;
		if (x > 7) {
			state = 1;
		} else if (x < -7) {
			state = -1;
		}
		if (state == preState) {
			return;
		} else {
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
}
