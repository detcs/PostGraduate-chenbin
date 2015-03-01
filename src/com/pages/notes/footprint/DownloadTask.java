package com.pages.notes.footprint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.util.DownloadUrlInfo;
import com.pages.today.TodayFragment;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

public class DownloadTask extends AsyncTask<String, Integer, DownloadUrlInfo>{
	Context context;
	//String dirPath;
	//String updateCol;
	//String id;
	String date;
	FootprintInfo fpInfo;
	Fragment requestFragment;
	boolean updateUI=false;
	public DownloadTask(Context context,String date,FootprintInfo info,Fragment fragment,boolean updateUI) {
		super();
		this.context = context;
		//this.dirPath = dirPath;
		//this.updateCol=updateCol;
		//this.id=id;
		fpInfo=info;
		requestFragment=fragment;
		this.updateUI=updateUI;
	}
	/* @Desciption: 利用Http协议下载文件并存储到SDCard 
    1.创建一个URL对象 
    2.通过URL对象,创建一个HttpURLConnection对象 
    3.得到InputStream 
    4.从InputStream当中读取数据 
    存到SDCard 
    1.取得SDCard路径 
    2.利用读取大文件的IO读法，读取文件 
	 */
	@Override
								// param[0]cover_bgpic param[1]cover_song param[2] footprint_bgpic
	protected DownloadUrlInfo doInBackground(String... param) {
		// TODO Auto-generated method stub
		DownloadUrlInfo urlInfo=null;
//		String coverBgPic=null;
//		String coverSong=null;
//		String footprintBgPic=null;
		String downloadedNames[]=new String[4];
		String downloadedDirPaths[]={FileDataHandler.COVER_PIC_DIR_PATH,FileDataHandler.COVER_SONG_DIR_PATH,FileDataHandler.FOOTPRINT_PIC_DIR_PATH,FileDataHandler.COVER_NOTE_PIC_DIR_PATH};
		String urlStr=null;//DataConstants.DOWNLOAD_URL+id;  
		//Log.e(DataConstants.TAG,"cover_bgpic "+urlStr);
		for(int i=0;i<param.length;i++)
		{
			urlStr=DataConstants.DOWNLOAD_URL+param[i];
			 OutputStream output=null;
			 InputStream input=null;
			 String fileName=null;
			try {  
	            /* 
	             * 通过URL取得HttpURLConnection 
	             * 要网络连接成功，需在AndroidMainfest.xml中进行权限配置 
	             * <uses-permission android:name="android.permission.INTERNET" /> 
	             */  
	            URL url=new URL(urlStr);  
	            HttpURLConnection conn=(HttpURLConnection)url.openConnection(); 
	            conn.setRequestProperty("Accept-Encoding", "identity"); 
	            //取得inputStream，并进行读取  
	            input=conn.getInputStream(); 
	            String nameInfo=conn.getHeaderField("Content-disposition");
	            fileName=nameInfo.split("=")[1];
	            downloadedNames[i]=fileName;
	            //Log.e(DataConstants.TAG,"download filename:"+fileName);
	            File file=new File(downloadedDirPaths[i]+"/"+fileName);
	            file.createNewFile();
	            output=new FileOutputStream(file);
	            byte[] buffer=new byte[4*1024];  
	//            while(input.read(buffer)!=-1){  
	//                output.write(buffer);  
	//            }  
	            int temp;
	            while ((temp = input.read(buffer)) != -1) 
	            {
	                output.write(buffer, 0, temp);
	            }
	            	output.flush();  
		        } catch (MalformedURLException e) {  
		            e.printStackTrace();  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }  finally{  
	            try {  
	            	if(output!=null)
	                output.close(); 
	            	if(input!=null)
	                input.close();
	               // System.out.println("success");  
	            } catch (IOException e) {  
	                //System.out.println("fail");  
	                e.printStackTrace();  
	            }  
	        }
    }  
		urlInfo=new DownloadUrlInfo(downloadedNames[0], downloadedNames[1], downloadedNames[2], downloadedNames[3]);
		return urlInfo;
	}
	@Override
	protected void onPostExecute(DownloadUrlInfo result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		// Log.e(DataConstants.TAG,"onpost:");
		 fpInfo.setCoverPicName(result.getCoverBgPic());
		 fpInfo.setCoverSongFileName(result.getCoverSong());
		 fpInfo.setFootprintPicName(result.getFootPrintBgPic());
		 fpInfo.setCoverNotePicName(result.getCoverTwoBgPic());
		 SQLiteDatabase db = DataConstants.dbHelper.getReadableDatabase();
		 DataConstants.dbHelper.insertFootprintInfoRecord(context, db, fpInfo);
		 db.close();
		 if(updateUI)
			 ((TodayFragment)requestFragment).setFirstpageView(fpInfo);
		//DataConstants.dbHelper.updateFootprintRecord(context, db, updateCol, result, date);
	}
	
	
	
	

}
