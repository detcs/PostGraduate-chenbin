package com.pages.funsquare.person;

import java.io.File;

import com.app.ydd.R;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.data.util.BitmapUtil;
import com.data.util.ComputeURL;
import com.data.util.ImageUtil;
import com.data.util.NetCall;
import com.data.util.SysCall;
import com.data.util.NetCall.InfoChangeCallback;
import com.data.util.NetCall.UploadHeadCallback;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.squareup.picasso.Picasso;
import com.view.util.AnimationUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonInfoFragment extends Fragment implements InfoChangeCallback,
		UploadHeadCallback {
	private static final int PICK = 100;
	private static final int TAKE = 101;
	private static final int CROP = 102;
	// private Uri photoUri;
	private View base;
	private FrameLayout frame;
	private static final String TAG = "bump";
	private TextView[] fonts;
	private static final int[] fontsId = { R.id.saveView, R.id.textView1,
			R.id.textView3, R.id.nickNameText, R.id.textView5 };

	private TextView nickNameText;
	private View rootView;
	private View backView;
	private View saveView;
	private ImageView imageView1;// head img
	private ImageView sexchooseImg;
	private int showSex = 1;
	private String headimg;
	private String headImgPath;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == base) {
			base = inflater.inflate(R.layout.frame, container, false);
			frame = (FrameLayout) base.findViewById(R.id.FrameLayout1);
		}
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_person_info,
					container, false);
			frame.addView(rootView);
			init(rootView);
		}
		return base;
	}

	private void init(View view) {
		headImgPath=FileDataHandler.APP_DIR_PATH+"/"+getActivity().getResources().getString(R.string.head_img);
		backView = view.findViewById(R.id.backView);
		saveView = view.findViewById(R.id.saveView);
		imageView1 = (ImageView) view.findViewById(R.id.imageView1);
		String headPath=FileDataHandler.APP_DIR_PATH+"/"+getResources().getString(R.string.head_img);
		File headFile=new File(headPath);
		if(headFile.exists())
		{
			ImageUtil.imageLoader.displayImage(ImageUtil.filePre+headPath,imageView1);
		}
		else
		Picasso.with(getActivity())
				.load(ComputeURL.getHeadImgURL(UserConfigs.getHeadImg()))
				.error(R.drawable.person_center_boyhead).into(imageView1);
		initShow(view);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
		saveView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveChange();
			}
		});
		imageView1.setOnLongClickListener(new OnLongClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				View pickView = frame.findViewWithTag(TAG);
				if (null == pickView) {
					pickView = LayoutInflater.from(getActivity()).inflate(
							R.layout.fragment_person_headset, null);
					pickView.setTag(TAG);
					initHeadSet(pickView);
					frame.addView(pickView);
					pickView.startAnimation(AnimationUtil.showAnimation());
				}
				return true;
			}
		});
		sexchooseImg.setOnClickListener(new OnClickListener() {// 只改变显示的，本地的网络的均未改动
					private final int[] ids = {
							R.drawable.person_center_sexchoose_girl,
							R.drawable.person_center_sexchoose_boy };

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showSex = (showSex + 1) % 2;
						sexchooseImg.setImageDrawable(getActivity()
								.getResources().getDrawable(ids[showSex]));
					}
				});
	}

	private void initShow(View view) {
		// fonts set
		Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
				"font/fangzhenglanting.ttf");
		fonts = new TextView[fontsId.length];
		for (int i = 0; i < fontsId.length; i++) {
			fonts[i] = (TextView) view.findViewById(fontsId[i]);
			fonts[i].setTypeface(face);
		}
		// nick name set
		nickNameText = (TextView) view.findViewById(R.id.nickNameText);
		nickNameText.setText(UserConfigs.getNickName());
		// head set
		Picasso.with(getActivity())
				.load(ComputeURL.getHeadImgURL(UserConfigs.getHeadImg()))
				.error(R.drawable.square_boy);
		headimg = UserConfigs.getHeadImg();
		// sex set
		sexchooseImg = (ImageView) view.findViewById(R.id.sexchooseImg);
		if (UserConfigs.GIRL == UserConfigs.getSex()) {
			showSex = UserConfigs.GIRL;
			sexchooseImg.setImageDrawable(getResources().getDrawable(
					R.drawable.person_center_sexchoose_girl));
		}
	}

	// ******************init head set******************
	private View takeView, album, quitView;

	private void initHeadSet(final View view) {
		takeView = view.findViewById(R.id.button1);
		album = view.findViewById(R.id.button2);
		quitView = view.findViewById(R.id.button3);
		takeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.startAnimation(AnimationUtil.hideAnimation());
				frame.removeView(view);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
//						Environment.getExternalStorageDirectory() + "/",
//						"temp.jpg")));
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(headImgPath)));
				intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, true);
				startActivityForResult(intent, TAKE);
			}
		});
		album.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				view.startAnimation(AnimationUtil.hideAnimation());
				frame.removeView(view);
				// TODO Auto-generated method stub
				// Intent intent = new Intent(
				// Intent.ACTION_PICK,
				// android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				// startActivityForResult(intent, PICK);
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
				intent.setType("image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 300);
				intent.putExtra("outputY", 300);
				intent.putExtra("scale", true);
				intent.putExtra("return-data", true);
				intent.putExtra("noFaceDetection", true);
				startActivityForResult(intent, PICK);
			}
		});
		quitView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.startAnimation(AnimationUtil.hideAnimation());
				frame.removeView(view);
			}
		});
	}

	// pick / take
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case Activity.RESULT_CANCELED:
			Toast.makeText(getActivity(), "发生了一个错误", 500).show();
			break;
		case Activity.RESULT_OK:
			switch (requestCode) {
			case TAKE: {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap bitmap = BitmapFactory
						.decodeFile(headImgPath, options);
				BitmapUtil.saveBitmap(headImgPath, bitmap);
				bitmap.recycle();
				
				Intent intent = new Intent();
				intent.setAction("com.android.camera.action.CROP");
				intent.setDataAndType(Uri.fromFile(new File(FileDataHandler.APP_DIR_PATH + "/", getActivity().getResources().getString(R.string.head_img))),
						"image/*");// mUri是已经选择的图片Uri
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);// 裁剪框比例
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 100);// 输出图片大小
				intent.putExtra("outputY", 100);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, CROP);
			}
				break;
			case PICK:// pick and crop
				// break;
			case CROP:// crop after take
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Log.i("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");
				// change head
				imageView1.setImageBitmap(bitmap);
				//FileDataHandler.saveBitmap(bitmap, path);
				NetCall.uploadHead2(BitmapUtil.bitmapToString(bitmap),
						PersonInfoFragment.this);
				break;
			default:
				SysCall.error("unknown request");
				break;
			}
			break;
		default:
			break;
		}
	}

	private void saveChange() {
		String email = "";
		String nickname = "";
		NetCall.changeInfo(nickname, headimg, email, showSex,
				PersonInfoFragment.this);
	}

	// NetCall.InfoChangeCallback
	@Override
	public void changeSuccess() {
		// TODO Auto-generated method stub
		// change the head img
		UserConfigs.storeHeadImg(headimg);
		UserConfigs.storeSex(showSex);
		Toast.makeText(getActivity(), "保存成功", 500).show();
	}

	@Override
	public void changeFail() {
		// TODO Auto-generated method stub
		// 修改失败
		Toast.makeText(getActivity(), "修改失败", 500).show();
	}

	// UploadHeadCallback
	@Override
	public void headsuccess(String headImg) {
		// TODO Auto-generated method stub
		this.headimg = headImg;
		//UserConfigs.
	}

	@Override
	public void headfail() {
		// TODO Auto-generated method stub

	}
}
