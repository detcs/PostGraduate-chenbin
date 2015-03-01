package com.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.app.ydd.R;
import com.pages.notes.footprint.FootprintInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
 
    private static final String DB_NAME = "ydd.db"; //数据库名称
    private static final int version = 1; //数据库版本
     
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String sql = "create table user(username varchar(20) not null , password varchar(60) not null );";          
//        db.execSQL(sql);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
 
    }
   
    public static void createCourseTable( Context context,SQLiteDatabase db,String courseName)
    {
    	String sql = "create table if not exists "+courseName+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
    			+context.getResources().getString(R.string.dbcol_photo_name)+" TEXT not null , "
    			+context.getResources().getString(R.string.dbcol_photo_base64)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_remark)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_flag)+" INTEGER,"
    			+context.getResources().getString(R.string.dbcol_if_recommender)+" INTEGER,"
    			+context.getResources().getString(R.string.dbcol_photo_delete)+" INTEGER,"
    			+context.getResources().getString(R.string.dbcol_date)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_master_state)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_ifupload)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_time)+" TEXT not null );";          
        Log.e(DataConstants.TAG, "sql:"+sql);
    	db.execSQL(sql);
    }
    public static void createFootprintTable( Context context,SQLiteDatabase db)
    {
    	String sql = "create table if not exists "+context.getResources().getString(R.string.db_footprint_table)
    			+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
    			+context.getResources().getString(R.string.dbcol_cover_pic)+" TEXT not null , "
    			+context.getResources().getString(R.string.dbcol_cover_song_file)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_cover_song_name)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_cover_singer)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_footprint_pic)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_cover_two_pic)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_diary)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_encourage)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_days)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_daysleft)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_ifupload)+" TEXT not null,"
    			+context.getResources().getString(R.string.dbcol_date)+" TEXT not null );";          
        Log.e(DataConstants.TAG, "sql:"+sql);
    	db.execSQL(sql);
    }
    public static FootprintInfo queryFootPrintInfo(Context context,SQLiteDatabase db,String date)
    {
    	FootprintInfo fpInfo=null;
    	Cursor result=db.rawQuery("SELECT * FROM "+context.getResources().getString(R.string.db_footprint_table)+" where "+context.getResources().getString(R.string.dbcol_date)+"=' "+date+"'",null); 
	    result.moveToFirst(); 
	    while (!result.isAfterLast()) { 
	         
	        String coverPicPath=result.getString(1); 
	        String coverSongFile=result.getString(2);
	        String coverSongName=result.getString(3);
	        String coverSinger=result.getString(4);
	        String footprintPic=result.getString(5);
	        String coverTwoPic=result.getString(6);
	        String diary=result.getString(7);
	        String encourage=result.getString(8);
	        String days=result.getString(9);
	        String daysleft=result.getString(10);
	        fpInfo=new FootprintInfo(coverPicPath,coverSongFile,coverSongName,coverSinger,footprintPic,coverTwoPic,diary,date,encourage,days,daysleft,context.getResources().getString(R.string.upload_no)); 
	       // Log.e(DataConstants.TAG,"db:query "+id+","+name);
	        result.moveToNext(); 
	      } 
	      result.close();
	      return fpInfo;
    }
    
    public static void insertFootprintInfoRecord(Context context,SQLiteDatabase db,FootprintInfo fpInfo)
    {
    	ContentValues cv=new ContentValues();
    	cv.put(context.getResources().getString(R.string.dbcol_cover_pic), fpInfo.getCoverPicName());
    	cv.put(context.getResources().getString(R.string.dbcol_footprint_pic), fpInfo.getFootprintPicName());
    	cv.put(context.getResources().getString(R.string.dbcol_cover_song_file), fpInfo.getCoverSongFileName());
    	cv.put(context.getResources().getString(R.string.dbcol_cover_song_name), fpInfo.getCoverSongName());
    	cv.put(context.getResources().getString(R.string.dbcol_cover_singer), fpInfo.getCoverSongName());
    	cv.put(context.getResources().getString(R.string.dbcol_cover_two_pic), fpInfo.getCoverTwoPicName());
    	cv.put(context.getResources().getString(R.string.dbcol_encourage), fpInfo.getEncourage());
    	cv.put(context.getResources().getString(R.string.dbcol_date), fpInfo.getDate());
    	cv.put(context.getResources().getString(R.string.dbcol_diary), fpInfo.getDiary());
    	cv.put(context.getResources().getString(R.string.dbcol_days), fpInfo.getDays());
    	cv.put(context.getResources().getString(R.string.dbcol_daysleft), fpInfo.getDaysLeft());
    	cv.put(context.getResources().getString(R.string.dbcol_ifupload), fpInfo.getIfUpload());
    	long rowid=db.insert(context.getResources().getString(R.string.db_footprint_table), null, cv);
    	Log.e(DataConstants.TAG,"insert footprint rowid:"+rowid);
    }
    public static void updateFootprintRecord(Context context,SQLiteDatabase db,String updateCol,String updateValue,String date)
    {
    	Log.e(DataConstants.TAG, "updateFootprint "+updateCol+"="+updateValue+" at "+date);
    	ContentValues cv=new ContentValues();
       	cv.put(updateCol, updateValue);
    	String whereClause =context.getResources().getString(R.string.dbcol_date)+ "=?";//修改条件
    	String[] whereArgs = { date };//修改条件的参数
    	db.update(context.getResources().getString(R.string.db_footprint_table),cv,whereClause,whereArgs);//执行修改
    }

    public static void insertCourseRecord(Context context,SQLiteDatabase db,String tableName,CourseRecordInfo cri)
    {
    	ContentValues cv=new ContentValues();
    	cv.put(context.getResources().getString(R.string.dbcol_photo_name), cri.getPhotoName());
    	cv.put(context.getResources().getString(R.string.dbcol_photo_base64), cri.getPhotobase64());
    	cv.put(context.getResources().getString(R.string.dbcol_remark), cri.getRemark());
    	cv.put(context.getResources().getString(R.string.dbcol_date), cri.getDate());
    	cv.put(context.getResources().getString(R.string.dbcol_time), cri.getTime());
    	cv.put(context.getResources().getString(R.string.dbcol_master_state), cri.getMasterState());
    	cv.put(context.getResources().getString(R.string.dbcol_flag), cri.getFlag());
    	cv.put(context.getResources().getString(R.string.dbcol_ifupload), cri.getIfUpload());
    	cv.put(context.getResources().getString(R.string.dbcol_photo_delete), cri.getIfDeleted());
    	cv.put(context.getResources().getString(R.string.dbcol_if_recommender), cri.getIfRecommender());
    	long rowid=db.insert(tableName, null, cv);
    	Log.e(DataConstants.TAG,cri.getDate()+" "+"insertCourseRecord "+tableName+":"+cri.toString()+" rowid:"+rowid);
    }
    public static void updateCourseRecord(Context context,SQLiteDatabase db,String tableName,String updateCol,String updateValue,String date)
    {
    	ContentValues cv=new ContentValues();
       	cv.put(updateCol, updateValue);
    	String whereClause =context.getResources().getString(R.string.dbcol_date)+ "=?";//修改条件
    	String[] whereArgs = { date };//修改条件的参数
    	db.update(tableName,cv,whereClause,whereArgs);//执行修改
    }
    public static int queryUnbackUpSubjectCounts(Context context,SQLiteDatabase db,String tableName)
    {
    	int count=0;
    	Cursor result=db.rawQuery("SELECT count(*) FROM "+tableName+" where "+context.getResources().getString(R.string.dbcol_ifupload)+"= 0",null); 
	    result.moveToFirst(); 
	    while (!result.isAfterLast()) {
	    	 count=result.getInt(0); 
	    	result.moveToNext();
	    }
	    return count;
    }
    public static CourseRecordInfo queryUnbackUpSubject(Context context,SQLiteDatabase db,String tableName)
    {
    	CourseRecordInfo cri=null;
    	Cursor result=db.rawQuery("SELECT * FROM "+tableName+" where "+context.getResources().getString(R.string.dbcol_ifupload)+"= 0 order by _id desc",null); 
	    result.moveToFirst(); 
	    while (!result.isAfterLast()) { 
	         
	        String photoName=result.getString(1); 
	        String photobase64=result.getString(2);
	        String remark=result.getString(3);
	        int  flag=result.getInt(4);
	        int  ifDeleted=result.getInt(6);
	        int  ifRecommender=result.getInt(5);
	        String date=result.getString(7);
	        String time=result.getString(10);
	        String masterState=result.getString(8);
	        String  ifUpload=result.getString(9);
	        
	       cri=new CourseRecordInfo(photoName, photobase64, remark, date, time, masterState, ifUpload, flag, ifDeleted, ifRecommender);
	       break; 
	      } 
	      result.close();
    	return cri;
    }
    public static CourseRecordInfo queryCourseRecordByPhotoName(Context context,SQLiteDatabase db,String tableName,String photoName)
    {
    	CourseRecordInfo cri=null;
    	Cursor result=db.rawQuery("SELECT * FROM "+tableName+" where "+context.getResources().getString(R.string.dbcol_photo_name)+"='"+photoName+"'",null); 
	    result.moveToFirst(); 
	    while (!result.isAfterLast()) { 
	         
	        String id=result.getString(0); 
	        String photoname=result.getString(1); 
	        String photobase64=result.getString(2); 
	        String remark=result.getString(3);
	        int  flag=result.getInt(4);
	        int  ifDeleted=result.getInt(6);
	        int  ifRecommender=result.getInt(5);
	        String date=result.getString(7);
	        String time=result.getString(10);
	        String masterState=result.getString(8);
	        String  ifUpload=result.getString(9);
	        
	        cri=new CourseRecordInfo(photoname, photobase64, remark, date, time, masterState, ifUpload, flag, ifDeleted,ifRecommender);
	        Log.e(DataConstants.TAG,"db:queryCourseRecordByPhotoName "+cri);
	        result.moveToNext(); 
	      } 
	      result.close();
	     return cri;
    }
    public static void deleteCourseRecordByPhotoName(Context context,SQLiteDatabase db,String tableName,String photoName)
    {
    	String whereClause =context.getResources().getString(R.string.dbcol_photo_name)+ "=?";//修改条件
    	String[] whereArgs = { photoName };//修改条件的参数
    	db.delete(tableName, whereClause, whereArgs);
    }
    public static void updateCourseRecordOnStringColByPhotoName(Context context,SQLiteDatabase db,String tableName,String updateCol,String updateValue,String photoName)
    {
    	Log.e(DataConstants.TAG,"updateCourseRecord "+updateCol+" "+updateValue+" where photoname="+photoName);
    	ContentValues cv=new ContentValues();
       	cv.put(updateCol, updateValue);
    	String whereClause =context.getResources().getString(R.string.dbcol_photo_name)+ "=?";//修改条件
    	String[] whereArgs = { photoName };//修改条件的参数
    	db.update(tableName,cv,whereClause,whereArgs);//执行修改
    }
    public static void updateCourseRecordOnIntColByPhotoName(Context context,SQLiteDatabase db,String tableName,String photoName,String updateCol,int updateValue)
    {
    	
    	ContentValues cv=new ContentValues();
       	cv.put(updateCol, updateValue);
    	String whereClause =context.getResources().getString(R.string.dbcol_photo_name)+ "=?";//修改条件
    	String[] whereArgs = {photoName};//修改条件的参数
    	int res=db.update(tableName,cv,whereClause,whereArgs);//执行修改
    	Log.e(DataConstants.TAG,"updateCourseRecordOnIntCol "+updateCol+" "+updateValue+" where photoname="+photoName+" res ");
    }
    public static int queryAllCourseRecordsCount(Context context,SQLiteDatabase db)
    {
    	int count=0;
    	count+=queryCourseRecordsCount(db, context.getResources().getString(R.string.db_english_table));
    	count+=queryCourseRecordsCount(db, context.getResources().getString(R.string.db_politics_table));
    	count+=queryCourseRecordsCount(db, context.getResources().getString(R.string.db_profess1_table));
    	if(UserConfigs.getCourseMathName()!=null)
    		count+=queryCourseRecordsCount(db, context.getResources().getString(R.string.db_math_table));
    	if(UserConfigs.getCourseProfessTwoName()!=null)
    		count+=queryCourseRecordsCount(db, context.getResources().getString(R.string.db_profess2_table));
	      return count;
    }
    public static int queryCourseRecordsCount(SQLiteDatabase db,String tableName)
    {
    	
    	boolean tableExist=isTableExists(db,tableName);
    	//Log.e(DataConstants.TAG,"tableExist " +tableExist);
    	if(tableExist==false)
    		return 0;
    	
    	Cursor result=db.rawQuery("SELECT count(*) FROM "+tableName+" where dbPhotoDel = 0",null); 
	    result.moveToFirst(); 
	    int count=0;
	    while (!result.isAfterLast()) { 
	         
	        count=result.getInt(0); 
	       // String name=result.getString(1); 
	       // Log.e(DataConstants.TAG,"db:query count:"+tableName+":"+count);
	        result.moveToNext(); 
	      } 
	      result.close();
	      return count;
    }
    public static int queryAllCoursesReviewedCountOnDate(Context context,SQLiteDatabase db,String date)
    {
    	int count=0;
    	count+=queryCourseReviewedCount(context,db, context.getResources().getString(R.string.db_english_table),date);
    	count+=queryCourseReviewedCount(context,db, context.getResources().getString(R.string.db_politics_table),date);
    	count+=queryCourseReviewedCount(context,db, context.getResources().getString(R.string.db_profess1_table),date);
    	if(UserConfigs.getCourseMathName()!=null)
    		count+=queryCourseReviewedCount(context,db, context.getResources().getString(R.string.db_math_table),date);
    	if(UserConfigs.getCourseProfessTwoName()!=null)
    		count+=queryCourseReviewedCount(context,db, context.getResources().getString(R.string.db_profess2_table),date);
	      return count;
    }
    public static int queryCourseReviewedCount(Context context,SQLiteDatabase db,String tableName,String date)
    {
    	
    	boolean tableExist=isTableExists(db,tableName);
    	//Log.e(DataConstants.TAG,"tableExist " +tableExist);
    	if(tableExist==false)
    		return 0;
    	
    	Cursor result=db.rawQuery("SELECT count(*) FROM "+tableName+" where dbPhotoDel = 0 and "+
    	context.getResources().getString(R.string.dbcol_master_state)+" != '"+context.getResources().getString(R.string.state_unknow)
    	+"' and "+
   		context.getResources().getString(R.string.dbcol_date)+"= '"+date+"'",null); 
	    result.moveToFirst(); 
	    int count=0;
	    while (!result.isAfterLast()) { 
	         
	        count=result.getInt(0); 
	       // String name=result.getString(1); 
	       // Log.e(DataConstants.TAG,"db:query count:"+tableName+":"+count);
	        result.moveToNext(); 
	      } 
	      result.close();
	      return count;
    }
    public static int queryCourseRecordsCountOnDate(Context context,SQLiteDatabase db,String tableName,String date)
    {
    	
    	boolean tableExist=isTableExists(db,tableName);
    	//Log.e(DataConstants.TAG,"tableExist " +tableExist);
    	if(tableExist==false)
    		return 0;
    	
    	Cursor result=db.rawQuery("SELECT count(*) FROM "+tableName+" where dbPhotoDel = 0 and "+context.getResources().getString(R.string.dbcol_date)+"='"+date+"'",null); 
	    result.moveToFirst(); 
	    int count=0;
	    while (!result.isAfterLast()) { 
	         
	        count=result.getInt(0); 
	       // String name=result.getString(1); 
	       // Log.e(DataConstants.TAG,"db:query count:"+tableName+":"+count);
	        result.moveToNext(); 
	      } 
	      result.close();
	      return count;
    }
    public static int queryAllCourseRecordsCountOnDate(Context context,SQLiteDatabase db,String date)
    {
    	
    	int count=0;
    	count+=queryCourseRecordsCountOnDate(context,db, context.getResources().getString(R.string.db_english_table),date);
    	count+=queryCourseRecordsCountOnDate(context,db, context.getResources().getString(R.string.db_politics_table),date);
    	count+=queryCourseRecordsCountOnDate(context,db, context.getResources().getString(R.string.db_profess1_table),date);
    	if(UserConfigs.getCourseMathName()!=null)
    		count+=queryCourseRecordsCountOnDate(context,db, context.getResources().getString(R.string.db_math_table),date);
    	if(UserConfigs.getCourseProfessTwoName()!=null)
    		count+=queryCourseRecordsCountOnDate(context,db, context.getResources().getString(R.string.db_profess2_table),date);
	      return count;
    }
    public static void dropTable(SQLiteDatabase db,String tableName){
        try
        {
            String sql="drop table "+tableName;                
            db.execSQL(sql);            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Log.e(DataConstants.TAG,"had deleted table: user->");  
    }
    public static void queryShowRecords(SQLiteDatabase db,String tableName)
    {
    	Cursor result=db.rawQuery("SELECT * FROM "+tableName,null); 
	    result.moveToFirst(); 
	    while (!result.isAfterLast()) { 
	         
	        String id=result.getString(0); 
	        String name=result.getString(1); 
	        Log.e(DataConstants.TAG,"db:query "+id+","+name);
	        result.moveToNext(); 
	      } 
	      result.close();
    }
    public static TreeSet<String> queryDates(Context context,SQLiteDatabase db,String tableName)
    {
    	TreeSet<String> dates=new TreeSet<String>();
    	Cursor result=db.rawQuery("SELECT "+context.getResources().getString(R.string.dbcol_date)+" FROM "+tableName,null); 
	    result.moveToFirst(); 
	    while (!result.isAfterLast()) { 
	         
	        String date=result.getString(0); 
	        dates.add(date); 
	        Log.e(DataConstants.TAG,"db:queryDates "+tableName+","+date);
	        result.moveToNext(); 
	      } 
	      result.close();
	      return dates;
    }
    public static List<String> queryPhotoNamesAtDate(Context context,SQLiteDatabase db,String tableName,String date)
    {
    	List<String> names=new ArrayList<String>();
    	Cursor result=db.rawQuery("SELECT "+context.getResources().getString(R.string.dbcol_photo_name)+" FROM "+tableName+" where "+context.getResources().getString(R.string.dbcol_date)+" = '"+date+"' and "+context.getResources().getString(R.string.dbcol_photo_delete)+" = 0",null); 
	    result.moveToFirst(); 
	    while (!result.isAfterLast()) { 
	         
	        String name=result.getString(0); 
	        names.add(name); 
	        Log.e(DataConstants.TAG,"db:query photo name:"+name);
	        result.moveToNext(); 
	      } 
	      result.close();
	      return names;
    }
    // SearchRecordsTable
    public static void createSearchRecordsTable( Context context,SQLiteDatabase db)
    {
    	String sql = "create table if not exists "+context.getResources().getString(R.string.db_search_records_table)
    			+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
    			+context.getResources().getString(R.string.dbcol_search_word)+" TEXT not null , "
    			+context.getResources().getString(R.string.dbcol_date)+" TEXT not null );";          
        Log.e(DataConstants.TAG, "sql:"+sql);
    	db.execSQL(sql);
    }
    public static void insertSearchRecord(Context context,SQLiteDatabase db,String word,String date)
    {
    	ContentValues cv=new ContentValues();
    	cv.put(context.getResources().getString(R.string.dbcol_search_word),word);
    	cv.put(context.getResources().getString(R.string.dbcol_date),date);
    	db.insert(context.getResources().getString(R.string.db_search_records_table), null, cv);
    }
    public static List<String> querySearchRecord(Context context,SQLiteDatabase db)
    {
    	List<String> names=new ArrayList<String>();
    	Cursor result=db.rawQuery("SELECT "+context.getResources().getString(R.string.dbcol_search_word)+" FROM "+context.getResources().getString(R.string.db_search_records_table),null); 
	    result.moveToFirst(); 
	    while (!result.isAfterLast()) { 
	         
	        String name=result.getString(0); 
	        names.add(name); 
	        Log.e(DataConstants.TAG,"db:query SearchRecord:"+name);
	        result.moveToNext(); 
	      } 
	      result.close();
	      return names;
    }
    public static void deleteAllSearchRecord(Context context,SQLiteDatabase db)
    {
    	//String whereClause =context.getResources().getString(R.string.dbcol_photo_name)+ "=?";//修改条件
    	//String[] whereArgs = { photoName };//修改条件的参数
    	db.delete(context.getResources().getString(R.string.db_search_records_table), null, null);
    }
    public static boolean isTableExists(SQLiteDatabase mDatabase,String tableName) {  
//         {  
//            if(mDatabase == null || !mDatabase.isOpen()) {  
//                mDatabase = getReadableDatabase();  
//            }  
//      
//            if(!mDatabase.isReadOnly()) {  
//                mDatabase.close();  
//                mDatabase = getReadableDatabase();  
//            }  
//        }  
      
        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);  
        if(cursor!=null) {  
            if(cursor.getCount()>0) {  
                                cursor.close();  
                return true;  
            }  
                        cursor.close();  
        }  
        return false;  
    }  
   
}
