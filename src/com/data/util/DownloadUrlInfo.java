package com.data.util;

public class DownloadUrlInfo
{
	String coverBgPic;
	String coverSong;
	String footPrintBgPic;
	String coverTwoBgPic;
	
	public DownloadUrlInfo(String coverBgPic, String coverSong,
			String footPrintBgPic, String coverTwoBgPic) {
		super();
		this.coverBgPic = coverBgPic;
		this.coverSong = coverSong;
		this.footPrintBgPic = footPrintBgPic;
		this.coverTwoBgPic = coverTwoBgPic;
	}
	public String getCoverBgPic() {
		return coverBgPic;
	}
	public void setCoverBgPic(String coverBgPic) {
		this.coverBgPic = coverBgPic;
	}
	public String getCoverSong() {
		return coverSong;
	}
	public void setCoverSong(String coverSong) {
		this.coverSong = coverSong;
	}
	public String getFootPrintBgPic() {
		return footPrintBgPic;
	}
	public void setFootPrintBgPic(String footPrintBgPic) {
		this.footPrintBgPic = footPrintBgPic;
	}
	public String getCoverTwoBgPic() {
		return coverTwoBgPic;
	}
	public void setCoverTwoBgPic(String coverTwoBgPic) {
		this.coverTwoBgPic = coverTwoBgPic;
	}
	
}
