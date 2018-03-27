package com.ways.app.priceOptimize.entity;

import java.util.List;

public class Mix {

	private String versionId;
	private String versionName;
	private String tp;
	private String msrp;
	private List<Lose> loseList;
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
	public String getMsrp() {
		return msrp;
	}
	public void setMsrp(String msrp) {
		this.msrp = msrp;
	}
	public List<Lose> getLoseList() {
		return loseList;
	}
	public void setLoseList(List<Lose> loseList) {
		this.loseList = loseList;
	}
	
	
	
}
