package com.data.model;

/*
 * 用于显示精华的资料列表：用于显示其中一项
 * 
 * */
public class Essense {
	private String author;
	private String title;
	private String time;
	private String id;
	private String url_;
	private String resid_;

	private int needShare_;// 是否需要分享才可以下载
	private int hasDownload_;// 有无附件下载
	private int isDownloaded_;// 是否下载过
	private int resType_;

	public Essense(String title, String author, String time, String id,
			int needShare_, int hasDownload_, int isDownloaded_, int resType_,
			String url_, String resid_) {
		this.title = title;
		this.time = time;
		this.author = author;
		this.id = id;
		this.needShare_ = needShare_;
		this.hasDownload_ = hasDownload_;
		this.isDownloaded_ = isDownloaded_;
		this.resType_ = resType_;
		this.url_ = url_;
		this.resid_ = resid_;
	}

	public String getResid_() {
		return resid_;
	}

	public String getUrl_() {
		return url_;
	}

	public int getNeedShare_() {
		return needShare_;
	}

	public int getHasDownload_() {
		return hasDownload_;
	}

	public int getIsDownloaded_() {
		return isDownloaded_;
	}

	public int getResType_() {
		return resType_;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	public String getAuthor() {
		// TODO Auto-generated method stub
		return author;
	}

	public String getTime() {
		// TODO Auto-generated method stub
		return time;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}
}
