package com.data.model;

public class CourseRecordAndPathInfo {

	CourseRecordInfo cri;
	String path;//if remote: http://+cri.getPhotoName  else  dirPath+cri.getPohtoName
	public CourseRecordAndPathInfo(CourseRecordInfo cri, String path) {
		super();
		this.cri = cri;
		this.path = path;
	}
	public CourseRecordInfo getCourseRecordInfo() {
		return cri;
	}
	public void setCourseRecordInfo(CourseRecordInfo cri) {
		this.cri = cri;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
