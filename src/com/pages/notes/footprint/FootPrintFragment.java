package com.pages.notes.footprint;

import com.app.ydd.R;
import com.data.model.CourseRecordInfo;
import com.data.model.DataConstants;
import com.data.model.UserConfigs;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class FootPrintFragment extends Fragment{

	 ListView noteList;
	 String date;
	 TextView date_day;
	 TextView date_month_year;
	 TextView dayReviewInfo;
	 TextView diary;
	 TextView appendNote;
	 ScrollView scrollView;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) 
	{
		Bundle bundle=getArguments();
		date=bundle.getString("footprint_date");
		 SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		View rootView = inflater.inflate(R.layout.fragment_footprint, container, false);
		scrollView=(ScrollView) rootView.findViewById(R.id.scrollview);
		scrollView.post(new Runnable() {
		    
			   @Override
			   public void run() {
			    // TODO Auto-generated method stub
			    scrollView.fullScroll(ScrollView.FOCUS_UP);
			   }
			  });
		date_day=(TextView) rootView.findViewById(R.id.date_day);
		date_day.setTypeface(DataConstants.typeAvenir);
		date_day.setText(date.split("-")[2]);
		date_month_year=(TextView) rootView.findViewById(R.id.date_month_year);
		date_month_year.setTypeface(DataConstants.typeFZLT);
		dayReviewInfo=(TextView) rootView.findViewById(R.id.day_review_info);
		dayReviewInfo.setTypeface(DataConstants.typeFZLT);
		int clockNum=UserConfigs.getClockDays();;
		int reviewNum= DataConstants.dbHelper.queryAllCoursesReviewedCountOnDate(getActivity(), db, date);
		int appendNum=DataConstants.dbHelper.queryAllCourseRecordsCountOnDate(getActivity(), db, date);
		dayReviewInfo.setText(getResources().getString(R.string.clocked)+clockNum+getResources().getString(R.string.day)+","
				+getResources().getString(R.string.reviewed)+reviewNum+getResources().getString(R.string.piece)+","
				+getResources().getString(R.string.appended)+appendNum+getResources().getString(R.string.piece));
		diary=(TextView) rootView.findViewById(R.id.day_diary);
		diary.setTypeface(DataConstants.typeFZLT);
		FootprintInfo fpInfo=DataConstants.dbHelper.queryFootPrintInfo(getActivity(), db, date);
		if(fpInfo!=null)
			diary.setText(fpInfo.getDiary());
		appendNote=(TextView) rootView.findViewById(R.id.append_note);
		appendNote.setTypeface(DataConstants.typeFZLT);
		if(appendNum>0)
		{	
			appendNote.setText(getResources().getString(R.string.append_note)+"("+appendNum+")");
			noteList=(ListView)rootView.findViewById(R.id.footprint_list);
			noteList.setAdapter(new FootprintNoteListAdapter(getActivity(),date));
		}
		else
		{
			appendNote.setText(getResources().getString(R.string.no)+getResources().getString(R.string.append_note));
			noteList=(ListView)rootView.findViewById(R.id.footprint_list);
			noteList.setVisibility(View.INVISIBLE);
		}
		
		db.close();
		return rootView;
	}
}
