package com.data.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String getTodayDateString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String date = sdf.format(calendar.getTime());
		return date;
	}
	public static String getAgoDateStringBefore(String date,int n)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calendar.add(Calendar.DAY_OF_YEAR, -n);
		String d = sdf.format(calendar.getTime());
		return d;
	}
	public static String getLaterDateStringAfter(String date,int n)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calendar.add(Calendar.DAY_OF_YEAR, n);
		String d = sdf.format(calendar.getTime());
		return d;
	}
	public static int getDateGapDays (String beginDate, String endDate)
	{       
			int gapDays=0;
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	        try { 
	                Date date = sdf.parse(endDate);// 通过日期格式的parse()方法将字符串转换成日期              
	                Date dateBegin = sdf.parse(beginDate);
	                long betweenTime = date.getTime() - dateBegin.getTime(); 
	                gapDays = (int)(betweenTime  / 1000 / 60 / 60 / 24); 
	             } catch(Exception e)
	             {
	              }
	        return (int)gapDays; 
	}

}
