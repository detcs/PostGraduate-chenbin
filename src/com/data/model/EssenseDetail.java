package com.data.model;

public class EssenseDetail extends Essense {
	public static final int HASRESOURCE = 1;
	public static final int HASDOWNLOADED = 1;
	public static final int NEEDSHARE = 1;
	public static final int ISCOLLECTED = 1;

	private String resSize_;
	private String browseTimes_;
	private String downloadTimes_;
	private String content_;

	public EssenseDetail(String title, String author, String time, String id,
			int needShare_, int hasDownload_, int isDownloaded_, int resType_,
			String url_, String resid_, String resSize_, String browseTimes_,
			String downloadTimes_, int isCollected_, String content_) {
		super(title, author, time, id, needShare_, hasDownload_, isDownloaded_,
				resType_, url_, resid_, isCollected_);
		this.resSize_ = resSize_;
		this.browseTimes_ = browseTimes_;
		this.downloadTimes_ = downloadTimes_;
		this.content_ = content_;
	}

	public String getContent_() {
		return content_;
	}

	public String getResSize_() {
		return resSize_;
	}

	public String getBrowseTimes_() {
		return browseTimes_;
	}

	public String getDownloadTimes_() {
		return downloadTimes_;
	}
}
