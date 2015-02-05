package com.pages.notes.camera;

import com.app.ydd.R;
import com.data.util.SysCall;
import com.pages.notes.camera.MySurfaceView.MyCallBack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Button;
import android.widget.ImageView;

public class TakeFragment extends Fragment implements MyCallBack {
	// private static final String TAG = "TakeFragment";
	private static final int TAKEFRAGMENT = 100;
	private TakeJump jump;
	private Activity activity;
	private View rootView;
	private MySurfaceView surfaceView;

	private SensorManager sensorMgr;
	private float x, y, z;
	private Matrix matrix;
	private Bitmap bitmap;
	private Bitmap showBitmap;
	private int preState;
	private int bitmapWidth;
	private int bitmapHeight;

	private ImageView closeView;
	private ImageView pickView;
	private Button takeBu;

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
		initImage();
		initSensor();
		findViews(view);
		setListener();
	}

	private void initImage() {
		matrix = new Matrix();
		bitmap = (Bitmap) ((BitmapDrawable) getActivity().getResources()
				.getDrawable(R.drawable.play)).getBitmap();
		bitmapWidth = bitmap.getWidth();
		bitmapHeight = bitmap.getHeight();
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
		takeBu = (Button) view.findViewById(R.id.takeBu);
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
		float degrees = 0;
		if (x > 7) {
			state = 10;
			degrees = 90;
		} else if (x < -7) {
			degrees = -90;
			state = -10;
		}
		if (state == preState) {
			return;
		} else {
			preState = state;
		}
		matrix.reset();
		matrix.setRotate(degrees); // 设置旋转
		// if (showBitmap != null && !showBitmap.isRecycled()) {
		// showBitmap.recycle();
		// }
		showBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth,
				bitmapHeight, matrix, true);
		pickView.setImageBitmap(showBitmap);
		// ObjectAnimator anim1 = ObjectAnimator.ofFloat(pickView, "rotationX",
		// 90f, 0f);
		// // anim1.addUpdateListener(new AnimatorUpdateListener() {
		// // @Override
		// // public void onAnimationUpdate(ValueAnimator animation) {
		// // pickView.postInvalidate();
		// // pickView.invalidate();
		// // }
		// // });
		// ObjectAnimator anim2 = ObjectAnimator.ofFloat(takeBu, "rotationY",
		// 90f,
		// 0f);
		// ObjectAnimator anim3 = ObjectAnimator.ofFloat(closeView, "rotationZ",
		// 90f, 0f);
		// AnimatorSet animSet = new AnimatorSet();
		// animSet.setDuration(2000);
		// animSet.setInterpolator(new LinearInterpolator());
		// animSet.playTogether(anim1, anim2, anim3);
		// animSet.start();
	}

}
