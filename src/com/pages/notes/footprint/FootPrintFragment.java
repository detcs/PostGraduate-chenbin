package com.pages.notes.footprint;

import com.app.ydd.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FootPrintFragment extends Fragment{

	 ListView noteList;
	 String date;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) 
	{
		Bundle bundle=getArguments();
		date=bundle.getString("footprint_date");
		View rootView = inflater.inflate(R.layout.fragment_footprint, container, false);
		noteList=(ListView)rootView.findViewById(R.id.footprint_list);
		noteList.setAdapter(new FootprintNoteListAdapter(getActivity(),date));
		return rootView;
	}
}
