package com.pages.notes.todayrec;

public class TodayRecommenderInfo {

	String id;
	String imgId;
	String remark;
	String subject;
	String type;
	String displayName;
	boolean isAd;
	String date;
	boolean ifCollected;
	String picFileName;
	public TodayRecommenderInfo(String id, String imgId, String remark,
			String subject, String type, String displayName, boolean isAd,String date,boolean ifCollected,String fileName) {
		super();
		this.id = id;
		this.imgId = imgId;
		this.remark = remark;
		this.subject = subject;
		this.type = type;
		this.displayName = displayName;
		this.isAd = isAd;
		this.date=date;
		this.ifCollected=ifCollected;
		this.picFileName=fileName;
	}
	
	public String getPicFileName() {
		return picFileName;
	}

	public void setPicFileName(String picFileName) {
		this.picFileName = picFileName;
	}

	public boolean isIfCollected() {
		return ifCollected;
	}

	public void setIfCollected(boolean ifCollected) {
		this.ifCollected = ifCollected;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImgId() {
		return imgId;
	}
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public boolean isAd() {
		return isAd;
	}
	public void setAd(boolean isAd) {
		this.isAd = isAd;
	}
	

}
