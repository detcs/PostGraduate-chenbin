package com.view.util;

import com.app.ydd.R;
import com.data.model.DataConstants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/****
 * 
 * setFrame->layout->onlayout
 * 
 * @author zhangjia
 * 
 */

/*
 * 
 * 
 * screen_H, screen_W:
 * 
 * View.getTop;View.getLeft....;View.getHeight;View.getWidth
 */
public class DragImageView extends ImageView {
	private static final String TAG = "DragImageView";

	private Activity mActivity;

	private int screen_W, screen_H;// �ɼ���Ļ�Ŀ�߶�

	private int bitmap_W, bitmap_H;// ��ǰͼƬ���

	private int MAX_W, MAX_H, MIN_W, MIN_H;// ����ֵ

	// private int current_Top, current_Right, current_Bottom, current_Left;//
	// ��ǰͼƬ�����������

	private int start_Top = -1, start_Right = -1, start_Bottom = -1,
			start_Left = -1;// ��ʼ��Ĭ��λ��.

	private int start_x, start_y, current_x, current_y;// ����λ��

	private float beforeLenght, afterLenght;// ���������

	private float scale_temp;// ���ű���

	/**
	 * ģʽ NONE���� DRAG����ק. ZOOM:����
	 * 
	 * @author zhangjia
	 * 
	 */
	private enum MODE {
		NONE, DRAG, ZOOM
	};

	private MODE mode = MODE.NONE;// Ĭ��ģʽ

	private boolean isControl_V = false;// ��ֱ���:�Ƿ����������϶�

	private boolean isControl_H = false;// ˮƽ���

	private ScaleAnimation scaleAnimation;// ���Ŷ���

	private boolean isScaleAnim = false;// ���Ŷ���

	private MyAsyncTask myAsyncTask;// �첽����

	View parentlayout; 
	/** ���췽�� **/
	public DragImageView(Context context) {
		super(context);
	}

	public void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
		//parentlayout= LayoutInflater.from(mActivity).inflate(R.layout.content_layout, null);
	}

	/** �ɼ���Ļ��� **/
	public void setScreen_W(int screen_W) {
		this.screen_W = screen_W;
	}

	/** �ɼ���Ļ�߶� **/
	public void setScreen_H(int screen_H) {
		this.screen_H = screen_H;
	}

	public DragImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/***
	 * ������ʾͼƬ
	 */
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		/** ��ȡͼƬ��� **/
		bitmap_W = bm.getWidth();
		bitmap_H = bm.getHeight();

		MAX_W = bitmap_W * 3;
		MAX_H = bitmap_H * 3;

		MIN_W = bitmap_W / 2;
		MIN_H = bitmap_H / 2;

	}

	/** ��layout���ã�����Ϊview��ÿһ����Ů���ô�С��λ�ã����ҵ���ÿ����Ů��layout���� **/
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (start_Top == -1) {
			start_Top = top;
			start_Left = left;
			start_Bottom = bottom;
			start_Right = right;
		}

	}

	private long lastClick;

	/***
	 * touch �¼�
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/** ���?�㡢��㴥�� **/
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			if (this.getWidth() > screen_W) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			Log.i(TAG, "ACTION_DOWN");
			if (System.currentTimeMillis() - lastClick < 300) {
				// double click
				doubleClick();
				break;
			}
			//else
				
			lastClick = System.currentTimeMillis();
			onTouchDown(event);
			break;
		// ��㴥��
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.i(TAG, "ACTION_POINTER_DOWN");
			onPointerDown(event);
			break;
		case MotionEvent.ACTION_MOVE:
			if (this.getWidth() > screen_W) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			Log.i(TAG, "ACTION_MOVE");
			onTouchMove(event);
			break;
		case MotionEvent.ACTION_UP:
			Log.i(TAG, "ACTION_UP");
			clickImage();
			onTouchUp(event);
			mode = MODE.NONE;
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.i(TAG, "ACTION_CANCEL");
			onTouchUp(event);
			mode = MODE.NONE;
			break;
		// ����ɿ�
		case MotionEvent.ACTION_POINTER_UP:
			Log.i(TAG, "ACTION_POINTER_UP");
			mode = MODE.NONE;
			/** ִ�����Ż�ԭ **/
			if (isScaleAnim) {
				doScaleAnim();
			}
			break;
		}
		return true;
	}

	/** ���� **/
	void onTouchDown(MotionEvent event) {
		// ��ֻ��һֻ��ָ���ʱ�����ģʽΪ�϶�ģʽ�����Ҽ�¼����ָ�������Ļ��λ�á��͡���ָ����ڵ�ǰwidget��λ�á�
		mode = MODE.DRAG;

		current_x = (int) event.getRawX();// �������Ļ�����
		current_y = (int) event.getRawY();

		// start_x:�����imageView�ľ���
		start_x = (int) event.getX();// �����view�ľ���
		// start_y��ʵҲ�������imageview�ľ���;getTop�������ĸ��ؼ��ľ���
		start_y = current_y - this.getTop();

	}

	/** ������ָ ֻ�ܷŴ���С **/
	void onPointerDown(MotionEvent event) {
		if (event.getPointerCount() == 2) {
			// ������ֻ��ָ�����Ļʱ�����ģʽΪzoom�����Ҽ�¼��ʼ��ֻ��ָ��ľ���
			mode = MODE.ZOOM;
			beforeLenght = getDistance(event);// ��ȡ����ľ���
		}
	}

	void onTouchUp(MotionEvent event) {
		if (mode == MODE.DRAG) {
			int left = 0, top = 0, right = 0, bottom = 0;
			/** ������Ҫ�����жϴ��?��ֹ��dragʱ��Խ�� **/
			/** ��ȡ��Ӧ��l��t,r ,b **/
			// ͨ�����ַ�ʽ��õ���߾���ұ߾�(iamgeView�븸�ؼ���ľ���)����֤��һ�㣺��ָ��iamgeView�����λ��ʼ�ղ���
			left = current_x - start_x;
			// right = current_x + this.getWidth() - start_x;
			right = left + this.getWidth();
			top = current_y - start_y;
			// bottom = current_y - start_y + this.getHeight();
			bottom = top + this.getHeight();
			/** ˮƽ�����ж� **/
			if (isControl_H) {
				// ���iamgeView�ڸ��ؼ����ұߣ������̽�imageview�ƶ�����iamgeview��ߺ͸��ؼ��غϵ�λ��
				if (left >= 0) {
					left = 0;
					right = this.getWidth();
				}
				if (right <= screen_W) {
					left = screen_W - this.getWidth();
					right = screen_W;
				}
			} else {
				left = this.getLeft();
				right = this.getRight();
			}
			/** ��ֱ�ж� **/
			if (this.getHeight() >= screen_H) {
				if (top >= 0) {
					top = 0;
					bottom = top + this.getHeight();
				}
				if (bottom <= screen_H) {
					top = screen_H - this.getHeight();
					bottom = screen_H;
				}
			} else {
				top = this.getTop();
				bottom = this.getBottom();
			}
			// �����˺����������ļ���ſ��ܻ�仯������ͼƬ�����б仯��
			if (isControl_H || isControl_V)
				this.setPosition(left, top, right, bottom);
		}
	}

	/** �ƶ��Ĵ��� **/
	void onTouchMove(MotionEvent event) {
		/** �����϶� **/
		if (mode == MODE.DRAG) {
			int left = 0, top = 0, right = 0, bottom = 0;
			/** ������Ҫ�����жϴ��?��ֹ��dragʱ��Խ�� **/
			/** ��ȡ��Ӧ��l��t,r ,b **/
			// ͨ�����ַ�ʽ��õ���߾���ұ߾�(iamgeView�븸�ؼ���ľ���)����֤��һ�㣺��ָ��iamgeView�����λ��ʼ�ղ���
			left = current_x - start_x;
			// right = current_x + this.getWidth() - start_x;
			right = left + this.getWidth();
			top = current_y - start_y;
			// bottom = current_y - start_y + this.getHeight();
			bottom = top + this.getHeight();
			/** ˮƽ�����ж� **/
			if (isControl_H) {
				// ���iamgeView�ڸ��ؼ����ұߣ������̽�imageview�ƶ�����iamgeview��ߺ͸��ؼ��غϵ�λ��
				if (left >= 0) {
					left = 0;
					right = this.getWidth();
				}
				if (right <= screen_W) {
					left = screen_W - this.getWidth();
					right = screen_W;
				}
			} else {
				left = this.getLeft();
				right = this.getRight();
			}
			/** ��ֱ�ж� **/
			if (this.getHeight() > screen_H) {
				if (top >= bumpHeight) {
					top = bumpHeight;
					bottom = top + this.getHeight();
				}
				if (bottom <= screen_H - bumpHeight) {
					top = screen_H - this.getHeight() - bumpHeight;
					bottom = screen_H - bumpHeight;
				}
			} else {
				top = this.getTop();
				bottom = this.getBottom();
			}
			// �����˺����������ļ���ſ��ܻ�仯������ͼƬ�����б仯��
			if (isControl_H || isControl_V)
				this.setPosition(left, top, right, bottom);

			// ʵʱ������ָ�������Ļ��λ��
			current_x = (int) event.getRawX();
			current_y = (int) event.getRawY();

		}
		/** �������� **/
		else if (mode == MODE.ZOOM) {
			afterLenght = getDistance(event);// ��ȡ����ľ���
			float gapLenght = afterLenght - beforeLenght;// �仯�ĳ���
			if (Math.abs(gapLenght) > 5f) {
				scale_temp = afterLenght / beforeLenght;// ������ŵı���
				this.setScale(scale_temp);
				beforeLenght = afterLenght;
			}
		}
	}

	/** ��ȡ����ľ��� **/
	float getDistance(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);

		return FloatMath.sqrt(x * x + y * y);
	}

	/** ʵ�ִ����϶� **/
	private void setPosition(int left, int top, int right, int bottom) {
		// view:Ϊһ��view������ʾ��λ�ã��������������Ů���������Χ֮��
		this.layout(left, top, right, bottom);
	}

	// wsy:�ױ߻����ϱ��������뿪���ؼ���Ӧ�ߵľ���
	private int bumpHeight = 300;

	public void setBumpHeight(int i) {
		this.bumpHeight = i;
	}
	private void clickImage()
	{
		//Log.e(DataConstants.TAG, "click img");
		LinearLayout top=(LinearLayout) parentlayout.findViewById(R.id.browse_top);
		RelativeLayout bottom=(RelativeLayout) parentlayout.findViewById(R.id.browse_bottom);
		if(top.getVisibility()==View.INVISIBLE)
			top.setVisibility(View.VISIBLE);
		else if(top.getVisibility()==View.VISIBLE)
			top.setVisibility(View.INVISIBLE);
		if(bottom.getVisibility()==View.INVISIBLE)
			bottom.setVisibility(View.VISIBLE);
		else if(bottom.getVisibility()==View.VISIBLE)
			bottom.setVisibility(View.INVISIBLE);
	}
	private void doubleClick() {
		if (this.getWidth() == bitmap_W) {
			// �Ŵ�����
			int current_Top, current_Right, current_Bottom, current_Left;// Ŀ��λ��
			int disX = (int) (this.getWidth() * Math.abs(1 - 3)) / 4;
			int disY = (int) (this.getHeight() * Math.abs(1 - 3)) / 4;
			current_Left = start_Left - disX;
			current_Top = start_Top - disY;
			current_Right = start_Right + disX;
			current_Bottom = start_Bottom + disY;
			// wsy 2/9
			if (current_Left < 0 || current_Right > screen_W) {
				isControl_H = true;
			} else {
				isControl_H = false;
			}
			if (this.getHeight() > screen_H) {
				isControl_V = true;
			} else {
				isControl_V = false;
			}
			this.setFrame(current_Left, current_Top, current_Right,
					current_Bottom);
			Log.i(TAG + "double", current_Left + " " + current_Top + " "
					+ current_Right + " " + current_Bottom);
		} else {
			// ��Ϊԭ���ĳߴ�
			this.setFrame(start_Left, start_Top, start_Right, start_Bottom);
			Log.i(TAG + "origin", start_Left + " " + start_Top + " "
					+ start_Right + " " + start_Bottom);
			isControl_H = false;
			isControl_V = false;
		}
	}

	/** �������� **/
	@SuppressLint("FloatMath")
	void setScale(float scale) {
		int current_Top, current_Right, current_Bottom, current_Left;// ��ǰͼƬ�����������
		int disX = (int) (this.getWidth() * Math.abs(1 - scale)) / 4;// ��ȡ����ˮƽ����
		int disY = (int) (this.getHeight() * Math.abs(1 - scale)) / 4;// ��ȡ���Ŵ�ֱ����

		// �Ŵ�
		if (scale > 1 && this.getWidth() <= MAX_W) {
			// getLeft():����������ĸ��ؼ�����ߵľ���
			current_Left = this.getLeft() - disX;
			current_Top = this.getTop() - disY;
			current_Right = this.getRight() + disX;
			current_Bottom = this.getBottom() + disY;
			// ���������ǶԳƵģ�����ֻ���϶����ܻ�ʹ��ͼƬ������
			this.setFrame(current_Left, current_Top, current_Right,
					current_Bottom);
			Log.i(TAG + "bigger", current_Left + " " + current_Top + " "
					+ current_Right + " " + current_Bottom);
			// wsy:2/10
			if (current_Left < 0 || current_Right > screen_W) {
				isControl_H = true;
			} else {
				isControl_H = false;
			}
			if (current_Top < 0 || current_Bottom > screen_H) {
				isControl_V = true;
			} else {
				isControl_V = false;
			}
		}
		// ��С
		else if (scale < 1 && this.getWidth() >= MIN_W) {
			current_Left = this.getLeft() + disX;
			current_Top = this.getTop() + disY;
			current_Right = this.getRight() - disX;
			current_Bottom = this.getBottom() - disY;
			/***
			 * ������Ҫ�������Ŵ���
			 */
			// �ϱ�Խ��
			if (isControl_V && current_Top > 0) {
				current_Top = 0;
				current_Bottom = this.getBottom() - 2 * disY;
				if (current_Bottom < screen_H) {
					current_Bottom = screen_H;
					isControl_V = false;// �رմ�ֱ����
				}
			}
			// �±�Խ��
			if (isControl_V && current_Bottom < screen_H) {
				current_Bottom = screen_H;
				current_Top = this.getTop() + 2 * disY;
				if (current_Top > 0) {
					current_Top = 0;
					isControl_V = false;// �رմ�ֱ����
				}
			}

			// ���Խ��
			if (isControl_H && current_Left >= 0) {
				current_Left = 0;
				current_Right = this.getRight() - 2 * disX;
				if (current_Right <= screen_W) {
					current_Right = screen_W;
					isControl_H = false;// �ر�
				}
			}
			// �ұ�Խ��
			if (isControl_H && current_Right <= screen_W) {
				current_Right = screen_W;
				current_Left = this.getLeft() + 2 * disX;
				if (current_Left >= 0) {
					current_Left = 0;
					isControl_H = false;// �ر�
				}
			}

			if (isControl_H || isControl_V) {
				this.setFrame(current_Left, current_Top, current_Right,
						current_Bottom);
			} else {
				this.setFrame(current_Left, current_Top, current_Right,
						current_Bottom);
				isScaleAnim = true;// �������Ŷ���
			}
			Log.i(TAG + "scale", current_Left + " " + current_Top + " "
					+ current_Right + " " + current_Bottom);
		}

	}

	/***
	 * ���Ŷ�������
	 */
	public void doScaleAnim() {
		myAsyncTask = new MyAsyncTask(screen_W, this.getWidth(),
				this.getHeight());
		myAsyncTask.setLTRB(this.getLeft(), this.getTop(), this.getRight(),
				this.getBottom());
		myAsyncTask.execute();
		isScaleAnim = false;// �رն���
	}

	/***
	 * ������������
	 */
	class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
		private int screen_W, current_Width, current_Height;

		private int left, top, right, bottom;

		private float scale_WH;// ��ߵı���

		/** ��ǰ��λ������ **/
		public void setLTRB(int left, int top, int right, int bottom) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}

		private float STEP = 8f;// ����

		private float step_H, step_V;// ˮƽ��������ֱ����

		public MyAsyncTask(int screen_W, int current_Width, int current_Height) {
			super();
			this.screen_W = screen_W;
			this.current_Width = current_Width;
			this.current_Height = current_Height;
			scale_WH = (float) current_Height / current_Width;
			step_H = STEP;
			step_V = scale_WH * STEP;
		}

		@Override
		protected Void doInBackground(Void... params) {

			while (current_Width <= screen_W) {

				left -= step_H;
				top -= step_V;
				right += step_H;
				bottom += step_V;

				current_Width += 2 * step_H;

				left = Math.max(left, start_Left);
				top = Math.max(top, start_Top);
				right = Math.min(right, start_Right);
				bottom = Math.min(bottom, start_Bottom);
				Log.e("jj", "top=" + top + ",bottom=" + bottom + ",left="
						+ left + ",right=" + right);
				onProgressUpdate(new Integer[] { left, top, right, bottom });
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(final Integer... values) {
			super.onProgressUpdate(values);
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					setFrame(values[0], values[1], values[2], values[3]);
				}
			});

		}

	}

	public void setLayout(View content) {
		// TODO Auto-generated method stub
		parentlayout=content;
	}

}
