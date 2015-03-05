package com.data.model;

import java.io.Serializable;

public class CourseRecordInfo implements Serializable{
	private int dbId;
	private String photoName;
	private String photobase64;
	private String remark;
	private String date;
	private String time;
	private String masterState;
	private String ifUpload;
	private int flag;
	private int ifDeleted;
	private int ifRecommender;
	private int ifRemote;//if from remote,photoName=imgId
	private String photoPath;//if from remote,path=url;else path= dir+photoName
	private String tableName;
	public CourseRecordInfo(String photoName,String path, String tableName,String photobase64,
			String remark, String date, String time, String masterState,
			String ifUpload, int flag, int ifDeleted, int ifRecommender,int ifRemote) {
		super();
		this.photoName = photoName;
		this.photobase64 = photobase64;
		this.remark = remark;
		this.date = date;
		this.time = time;
		this.masterState = masterState;
		this.ifUpload = ifUpload;
		this.flag = flag;
		this.ifDeleted = ifDeleted;
		this.ifRecommender = ifRecommender;
		this.ifRemote=ifRemote;
		this.photoPath=path;
		this.tableName=tableName;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public int getIfRemote() {
		return ifRemote;
	}

	public void setIfRemote(int ifRemote) {
		this.ifRemote = ifRemote;
	}

	public int getDbId() {
		return dbId;
	}

	public void setDbId(int dbId) {
		this.dbId = dbId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.photoName+","+this.photobase64+","+this.remark+","+this.date+","+
		this.time+","+this.masterState+","+this.ifUpload+","+this.flag+","+this.ifDeleted+","+ifRecommender;
	}

	public int getIfRecommender() {
		return ifRecommender;
	}

	public void setIfRecommender(int ifRecommender) {
		this.ifRecommender = ifRecommender;
	}

	public int getIfDeleted() {
		return ifDeleted;
	}

	public void setIfDeleted(int ifDeleted) {
		this.ifDeleted = ifDeleted;
	}

	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	public String getPhotobase64() {
		return photobase64;
	}
	public void setPhotobase64(String photobase64) {
		this.photobase64 = photobase64;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMasterState() {
		return masterState;
	}
	public void setMasterState(String masterState) {
		this.masterState = masterState;
	}
	public String getIfUpload() {
		return ifUpload;
	}
	public void setIfUpload(String ifUplaod) {
		this.ifUpload = ifUplaod;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	

}
