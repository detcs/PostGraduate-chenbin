package com.pages.notes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.app.ydd.R;
import com.data.model.DataConstants;
import com.data.util.SysCall;
import com.squareup.picasso.Picasso;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleNoteFragment extends Fragment{

	ImageView img;
	TextView remark;
	String path;
	String photoName;
	String tableName;
	@Override
	public View onCreateView(final LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View  rootView = inflater.inflate(R.layout.fragment_single_note, container, false);
		Bundle bundle=getArguments();
		path=bundle.getString("single_path");
		tableName=bundle.getString("single_tablename");
		photoName=path.split("|")[2];
		img=(ImageView)rootView.findViewById(R.id.single_img);
		Picasso.with(getActivity()).load(new File(path)).into(img);
		initTitleView();
		initRemarkEditView(rootView);
		return rootView;
	}
	private void initTitleView()
	{
		Button right=(Button)getActivity().findViewById(R.id.right_btn);
		right.setVisibility(View.INVISIBLE);
		Button left=(Button)getActivity().findViewById(R.id.left_btn);
		left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SysCall.clickBack();
			}
		});
	}
	private void initRemarkEditView(View rootView)
	{
		final TextView remarkContext=(TextView)rootView.findViewById(R.id.single_remark_content);
		remarkContext.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		remarkContext.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa一一一一一一一一一一一一一一一一一一一一一一一一一一一一一");
		remarkContext.setOnClickListener(new OnClickListener() {
			Boolean flag = true;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(flag){
			        flag = false;
			        remarkContext.setEllipsize(null); // 展开
			        remarkContext.setSingleLine(flag);
			        }else{
			        	Log.e(DataConstants.TAG,"remarkcontent "+flag);
			          flag = true;
			          remarkContext.setEllipsize(android.text.TextUtils.TruncateAt.END); // 收缩
			          remarkContext.setLines(2);
			    }
			}
		});
		final LinearLayout editRemarkLayout=(LinearLayout)rootView.findViewById(R.id.single_remark_edit_hideBar);
		final EditText editRemark = (EditText) rootView.findViewById(R.id.single_remark_edit);
		TextView cancelEdit=(TextView)rootView.findViewById(R.id.single_remark_edit_quitView);
		TextView saveEdit=(TextView)rootView.findViewById(R.id.single_remark_edit_saveView);
		ImageView editRemarkImg=(ImageView)rootView.findViewById(R.id.single_edit_remark_img);
		editRemarkImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editRemarkLayout.setVisibility(View.VISIBLE);
				SysCall.bumpSoftInput(editRemark, getActivity());
			}
		});
		cancelEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(editRemarkLayout.getVisibility()==View.VISIBLE)
				{
					editRemarkLayout.setVisibility(View.INVISIBLE);
					SysCall.hideSoftInput(editRemarkLayout, getActivity());
					editRemark.clearFocus();
					editRemark.setFocusable(false);
					editRemark.setFocusableInTouchMode(false);
				}
			}
		});
		saveEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editRemarkLayout.setVisibility(View.INVISIBLE);
				SysCall.hideSoftInput(editRemarkLayout, getActivity());
				editRemark.clearFocus();
				editRemark.setFocusable(false);
				editRemark.setFocusableInTouchMode(false);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				String date = sdf.format(calendar.getTime());
				SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				DataConstants.dbHelper.updateCourseRecordOnStringColByPhotoName(getActivity(), db, tableName,getResources().getString(R.string.dbcol_remark), editRemark.getText().toString(), photoName);
				//db.close();
			}
		});
	}
}
