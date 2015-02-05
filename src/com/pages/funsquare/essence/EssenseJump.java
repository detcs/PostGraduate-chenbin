package com.pages.funsquare.essence;

import com.data.model.EssenseDetail;

public interface EssenseJump {
	// public void essense();

	public void detail(String id);

	public void query();

	public void addShare(EssenseDetail ed);
	
	public void removeShare();
}
