package com.data.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.app.ydd.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class FileDataHandler {
	public static String SD_PATH=null;
	public static String APP_DIR_PATH=null;
	public static String COVER_SONG_DIR_PATH=null;
	public static String COVER_PIC_DIR_PATH=null;
	public static String FOOTPRINT_PIC_DIR_PATH=null;
	public static String COVER_NOTE_PIC_DIR_PATH=null;
	public static String TODAY_REC_PIC_DIR_PATH=null;
	public static void init(Context context)
	{
		if(sdCardExist())
		{
			SD_PATH=Environment.getExternalStorageDirectory().toString();//获取跟目录 
			APP_DIR_PATH=SD_PATH+"/"+context.getResources().getString(R.string.dir_app);
			COVER_PIC_DIR_PATH=APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_cover_pic);
			COVER_SONG_DIR_PATH=APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_cover_song);
			FOOTPRINT_PIC_DIR_PATH=APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_footprint_pic);
			COVER_NOTE_PIC_DIR_PATH=APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_cover_note_pic);
			TODAY_REC_PIC_DIR_PATH=APP_DIR_PATH+"/"+context.getResources().getString(R.string.dir_today_rec_pic);
		}
	}
	public static boolean sdCardExist()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
	}
	public static void copyFile(String srcFilePath,String targetFilePath)
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
			//srcFile.delete();
			} catch (Exception ex) {

			//Log.e("readfile", ex.getMessage());

		}

	
		
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
	public static void saveBitmap(Bitmap bitmap,String path) throws IOException {
		File f = new File(path);
		if(f.exists())
			f.delete();
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(DataConstants.TAG,e+"");
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(DataConstants.TAG,e+"");
		}
		Log.e(DataConstants.TAG,"save over");
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
	public static String getBase64ImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
		/*
		byte[] data = null;  
		Log.i("com.market","begin base64");
		// 读取图片字节数组  
		try {  
		InputStream in = new FileInputStream(imgFilePath);  
		data = new byte[in.available()];  
		in.read(data);  
		in.close();  
		} catch (IOException e) {  
		e.printStackTrace();  
		}  
		  
		// 对字节数组Base64编码  
		String res = Base64.encodeToString(data, Base64.DEFAULT); 
		Log.i("com.market","base len:"+res);
		return res;// 返回Base64编码过的字节数组字符串  
		*/
		  File  file = new File(imgFilePath);
		  byte[] buffer=null;
	        FileInputStream inputFile;
			try {
				inputFile = new FileInputStream(file);
				  buffer = new byte[(int)file.length()];
			        inputFile.read(buffer);
			        inputFile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
//	        return new android.util.Base64;
			Log.i(DataConstants.TAG,"base len:"+android.util.Base64.encodeToString(buffer, Base64.DEFAULT).length());
	        return android.util.Base64.encodeToString(buffer, Base64.DEFAULT);
		}  
}
