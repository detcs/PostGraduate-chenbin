package com.data.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.data.model.DataConstants;
import com.data.model.FileDataHandler;
import com.data.model.UserConfigs;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadPicTask extends AsyncTask<String, Integer,String> {

	String path;
	Context context;
	ImageView img;
	
	public DownloadPicTask(String path, Context context, ImageView img) {
		super();
		this.path = path;
		this.context = context;
		this.img = img;
	}

	@Override
	protected String doInBackground(String... param) {
		// TODO Auto-generated method stub
		String urlStr=DataConstants.DOWNLOAD_URL+param;
		 OutputStream output=null;
		 InputStream input=null;
		 String fileName=null;
		 String filePath=null;
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
           // String nameInfo=conn.getHeaderField("Content-disposition");
            //fileName=nameInfo.split("=")[1];
            //downloadedNames[i]=fileName;
            //Log.e(DataConstants.TAG,"download filename:"+fileName);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
			String time = sdf.format(new Date());
			// i as index
			fileName = UserConfigs.getAccount() + "|" + time + ".jpg";
			//recInfos.get(i).setPicFileName(fileName);
			//DataConstants.dbHelper.insertTodayRecommenderInfoRecord(getApplicationContext(), db, recInfos.get(i));
			filePath=path+"/"+fileName;
			//Log.e(DataConstants.TAG, "todayrec "+path);
			//filePaths.add(path);
            File file=new File(filePath);
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
		
		return fileName;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Picasso.with(context).load(new File(path+"/"+result)).into(img);
	}
	

	
}
