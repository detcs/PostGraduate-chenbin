package com.data.util;

import java.io.Serializable;

public class PhotoNameTableInfo implements Serializable{

	String photoName;
	String tableName;
	String path;
	public PhotoNameTableInfo(String photoName, String tableName, String path) {
		super();
		this.photoName = photoName;
		this.tableName = tableName;
		this.path = path;
	}
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
