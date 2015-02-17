package com.data.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.app.ydd.R;
import com.data.model.DataConstants.PageName;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImportanceFlagOnClickListener implements OnClickListener{

	String photoName;
	String tableName;
	Context context;
	boolean importantFlag;
	ImageView importantImg;
	PageName page;
	


	public ImportanceFlagOnClickListener(String photoName, String tableName,
			Context context,ImageView img,boolean currentFlag,PageName page) {
		super();
		this.photoName = photoName;
		this.tableName = tableName;
		this.context = context;
		this.importantFlag =currentFlag;
		this.importantImg=img;
		this.page=page;
	}




	@Override
	public void onClick(View arg0) 
	{
		// TODO Auto-generated method stub
			if(importantFlag)
			{
				if(page==PageName.NoteSpec)
					Picasso.with(context).load(R.drawable.notespec_importance).into(importantImg);
				else if(page==PageName.NoteReview)
					Picasso.with(context).load(R.drawable.review_importance).into(importantImg);
					
				importantFlag=!importantFlag;
				SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				DataConstants.dbHelper.updateCourseRecordOnIntColByPhotoName(context, db, tableName, photoName,context.getResources().getString(R.string.dbcol_flag), 0);
				db.close();
			}
			else
			{
				if(page==PageName.NoteSpec)
					Picasso.with(context).load(R.drawable.notespec_importance_click).into(importantImg);
				else if(page==PageName.NoteReview)
					Picasso.with(context).load(R.drawable.review_importance_click).into(importantImg);
				importantFlag=!importantFlag;
				SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
				DataConstants.dbHelper.updateCourseRecordOnIntColByPhotoName(context, db, tableName, photoName, context.getResources().getString(R.string.dbcol_flag), 1);
				db.close();
			}
	}

}
