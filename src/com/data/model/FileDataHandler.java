package com.data.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.app.ydd.R;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class FileDataHandler {
	public static String SD_PATH=null;
	public static String APP_DIR_PATH=null;
	public static String COVER_SONG_DIR_PATH=null;
	public static String COVER_PIC_DIR_PATH=null;
	public static String FOOTPRINT_PIC_DIR_PATH=null;

	public static void init(Context context)
	{
		if(sdCardExist())
		{
			SD_PATH=Environment.getExternalStorageDirectory().toString();//获取跟目录 
			APP_DIR_PATH=SD_PATH+"/"+context.getResources().getString(R.string.dir_app);
			COVER_PIC_DIR_PATH=APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_cover_pic);
			COVER_SONG_DIR_PATH=APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_cover_song);
			FOOTPRINT_PIC_DIR_PATH=APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_footprint_pic);
		}
	}
	public static boolean sdCardExist()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
	}
	
	public static void transferFile(String srcFilePath,String targetFilePath)
	{
		//Log.e(DataConstants.TAG,"transfer:"+srcFilePath+" "+targetFilePath);
		File srcFile=new File(srcFilePath);
		File targetFile=new File(targetFilePath);
//		try {
//			targetFile.createNewFile();
//			String content=read(srcFilePath);
//			save(targetFilePath, content);
//			
//			//srcFile.delete();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			Log.e(DataConstants.TAG,e.toString());
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			Log.e(DataConstants.TAG,e.toString());
//			e.printStackTrace();
//		}
		try {

			FileInputStream fosfrom = new FileInputStream(srcFile);

			FileOutputStream fosto = new FileOutputStream(targetFile);

			byte bt[] = new byte[1024];

			int c;

			while ((c = fosfrom.read(bt)) > 0) {

			fosto.write(bt, 0, c); //将内容写到新文件当中

			}

			fosfrom.close();

			fosto.close();
			srcFile.delete();
			} catch (Exception ex) {

			//Log.e("readfile", ex.getMessage());

		}

	
		
	}
	public static String read(String fileName) throws Exception {

		Log.e(DataConstants.TAG,"read:"+fileName);
		FileInputStream fileInputStream = new FileInputStream(fileName);

		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];

		int len = 0;

		while ((len = fileInputStream.read(buffer)) > 0) {

		byteArray.write(buffer, 0, len);

		};
		Log.e(DataConstants.TAG,"byteArray len"+byteArray.size());
		return byteArray.toString();

	}
	public static void save(String fileName, String fileContent) throws Exception {

		
		FileOutputStream fileOutputStream = new FileOutputStream(fileName);

		fileOutputStream.write(fileContent.getBytes());
		fileOutputStream.flush();
		fileOutputStream.close();

	}
	public static String photoPathToBlurPath(String srcPath)
	{
		String blurPath="";
		String []info=srcPath.split("\\/");
		String photoName=info[info.length-1];
		for(int i=0;i<info.length-1;i++)
			blurPath+=info[i]+"/";
		blurPath+=DataConstants.blured+photoName;
		return blurPath;
	}
	class SaveTask extends AsyncTask<String, Integer, String>
	{
		String fileName;
		String fileContent;
		String srcFilePath;
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				save(fileName, fileContent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			File f=new File(srcFilePath);
			f.delete();
		}
		
		
	}
}
