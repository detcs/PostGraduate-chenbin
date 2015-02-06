package com.pages.notes.camera;

import com.app.ydd.R;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TestFragment extends Fragment {
	private View rootView;
	private ImageView imageView1;
	private Bitmap b;

	public TestFragment(Bitmap bitmap) {
		// TODO Auto-generated constructor stub
		b = bitmap;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_test, container,
					false);
		}
		imageView1 = (ImageView) rootView.findViewById(R.id.imageView1);
		imageView1.setImageBitmap(b);
		return rootView;
	}
}
