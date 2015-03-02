package com.pages.notes.footprint;

public class FootprintInfo {

	String id;
	String coverPicName;
	//String coverSongPath;
	String coverSongFileName;
	String coverSongName;
	String coverSingerName;
	String footprintPicName;
	String coverNotePicName;
	String diary;
	String date;
	String encourage;
	String days;
	String daysLeft;
	String ifUpload;
	public FootprintInfo(String coverPicName, String coverSongFileName,
			String coverSongName, String coverSingerName,
			String footprintPicName, String coverNotePicName, String diary,
			String date, String encourage, String days, String daysLeft,
			String ifUpload) {
		super();
		this.coverPicName = coverPicName;
		this.coverSongFileName = coverSongFileName;
		this.coverSongName = coverSongName;
		this.coverSingerName = coverSingerName;
		this.footprintPicName = footprintPicName;
		this.coverNotePicName = coverNotePicName;
		this.diary = diary;
		this.date = date;
		this.encourage = encourage;
		this.days = days;
		this.daysLeft = daysLeft;
		this.ifUpload = ifUpload;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCoverPicName() {
		return coverPicName;
	}
	public void setCoverPicName(String coverPicName) {
		this.coverPicName = coverPicName;
	}
	public String getCoverSongFileName() {
		return coverSongFileName;
	}
	public void setCoverSongFileName(String coverSongFileName) {
		this.coverSongFileName = coverSongFileName;
	}
	public String getCoverSongName() {
		return coverSongName;
	}
	public void setCoverSongName(String coverSongName) {
		this.coverSongName = coverSongName;
	}
	public String getCoverSingerName() {
		return coverSingerName;
	}
	public void setCoverSingerName(String coverSingerName) {
		this.coverSingerName = coverSingerName;
	}
	public String getFootprintPicName() {
		return footprintPicName;
	}
	public void setFootprintPicName(String footprintPicName) {
		this.footprintPicName = footprintPicName;
	}
	
	public String getDiary() {
		return diary;
	}
	public void setDiary(String diary) {
		this.diary = diary;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getEncourage() {
		return encourage;
	}
	public void setEncourage(String encourage) {
		this.encourage = encourage;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getDaysLeft() {
		return daysLeft;
	}
	public void setDaysLeft(String daysLeft) {
		this.daysLeft = daysLeft;
	}
	public String getIfUpload() {
		return ifUpload;
	}
	public void setIfUpload(String ifUpload) {
		this.ifUpload = ifUpload;
	}
	
	public String getCoverNotePicName() {
		return coverNotePicName;
	}
	public void setCoverNotePicName(String coverNotePicName) {
		this.coverNotePicName = coverNotePicName;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String result="coverPicName" + coverPicName+
		"coverSongFileName" + coverSongFileName+
		"coverSongName" + coverSongName+
		"coverSingerName" + coverSingerName+
		"footprintPicName" + footprintPicName+
		"coverTwoPicName " + coverNotePicName+
		"diary " + diary+
		"date" + date+
		"encourage " + encourage+
		"days " + days+
		"daysLeft " + daysLeft+
		"ifUpload " + ifUpload;
		return result;
	}

	



	}
