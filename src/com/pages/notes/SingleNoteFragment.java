package com.pages.notes;

import java.io.File;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleNoteFragment extends Fragment{

	ImageView img;
	TextView remark;
	String path;
	String photoName;
	
	@Override
	public View onCreateView(final LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View  rootView = inflater.inflate(R.layout.fragment_single_note, container, false);
		Bundle bundle=getArguments();
		path=bundle.getString("single_path");
		photoName=path.split("|")[2];
		img=(ImageView)rootView.findViewById(R.id.single_img);
		Picasso.with(getActivity()).load(new File(path)).into(img);
		return rootView;
	}
}
