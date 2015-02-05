package com.pages.notes;

import com.app.ydd.R;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SingleNoteFragment extends Fragment{

	ImageView img;
	
	@Override
	public View onCreateView(final LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View  rootView = inflater.inflate(R.layout.fragment_single_note, container, false);
		return rootView;
	}
}
