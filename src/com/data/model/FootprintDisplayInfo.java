package com.data.model;

public class FootprintDisplayInfo {

	String content;
	//String imgId;
	String days;
	String daysLeft;
	String footprintImgId;
	String diary;
	String reviewCount;
	String addCount;
	public FootprintDisplayInfo(String content, String days, String daysLeft,
			String footprintImgId, String diary, String reviewCount,
			String addCount) {
		super();
		this.content = content;
		this.days = days;
		this.daysLeft = daysLeft;
		this.footprintImgId = footprintImgId;
		this.diary = diary;
		this.reviewCount = reviewCount;
		this.addCount = addCount;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getFootprintImgId() {
		return footprintImgId;
	}
	public void setFootprintImgId(String footprintImgId) {
		this.footprintImgId = footprintImgId;
	}
	public String getDiary() {
		return diary;
	}
	public void setDiary(String diary) {
		this.diary = diary;
	}
	public String getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(String reviewCount) {
		this.reviewCount = reviewCount;
	}
	public String getAddCount() {
		return addCount;
	}
	public void setAddCount(String addCount) {
		this.addCount = addCount;
	}
	
}
