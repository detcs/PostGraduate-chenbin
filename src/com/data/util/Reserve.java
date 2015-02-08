package com.data.util;

public class Reserve {
	private String id_;
	private String title_;// 标题
	private String source_;// 来源
	private String content_;
	private int hasDownload_;// 有无附件下载
	private String resid_;// 附件资源的id
	private int isDownloaded_;// 是否下载过
	private String time_;
	private int type_; // 类别
	private int resType_;

	public Reserve(String id, String title, String source, String content,
			int hasDownload, String resid, int isDownloaded, String time,
			int type, int resType) {
		this.id_ = id;
		this.title_ = title;// 标题
		this.source_ = source;// 来源
		this.content_ = content;
		this.hasDownload_ = hasDownload;// 有无附件下载
		this.resid_ = resid;// 附件资源的id
		this.isDownloaded_ = isDownloaded;// 是否下载过
		this.time_ = time;
		this.type_ = type; // 类别
		this.resType_ = resType;
	}

	public String getId_() {
		return id_;
	}

	public String getTitle_() {
		return title_;
	}

	public String getSource_() {
		return source_;
	}

	public String getContent_() {
		return content_;
	}

	public int getHasDownload_() {
		return hasDownload_;
	}

	public String getResid_() {
		return resid_;
	}

	public int getIsDownloaded_() {
		return isDownloaded_;
	}

	public String getTime_() {
		return time_;
	}

	public int getType_() {
		return type_;
	}

	public int getResType_() {
		return resType_;
	}

}
