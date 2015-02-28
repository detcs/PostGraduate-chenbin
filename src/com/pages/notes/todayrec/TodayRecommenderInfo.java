package com.pages.notes.todayrec;

public class TodayRecommenderInfo {

	String id;
	String imgId;
	String remark;
	String subject;
	String type;
	String displayName;
	boolean isAd;
	public TodayRecommenderInfo(String id, String imgId, String remark,
			String subject, String type, String displayName, boolean isAd) {
		super();
		this.id = id;
		this.imgId = imgId;
		this.remark = remark;
		this.subject = subject;
		this.type = type;
		this.displayName = displayName;
		this.isAd = isAd;
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
