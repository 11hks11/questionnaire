package com.ways.app.priceOptimize.entity;

import java.util.List;

public class MyVersion {

	private String myVersionId;
	private List<VsVersion> vsList;
	public String getMyVersionId() {
		return myVersionId;
	}
	public void setMyVersionId(String myVersionId) {
		this.myVersionId = myVersionId;
	}
	public List<VsVersion> getVsList() {
		return vsList;
	}
	public void setVsList(List<VsVersion> vsList) {
		this.vsList = vsList;
	}
	
}
