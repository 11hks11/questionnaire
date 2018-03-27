package com.ways.app.priceOptimize.entity;

import java.util.List;

public class Version {
	private String versionId;
	private String versionName;
	private String versionSales;
	private String msrp;
	private String tp;
	private List<Config> allocation;
	private List<Lose> lose;
	private String loseStr;
	private String subModelEname;
	private String subModelImage;
	private List<VsVersion> vsVersionList;
	
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
	public String getVersionSales() {
		return versionSales;
	}
	public void setVersionSales(String versionSales) {
		this.versionSales = versionSales;
	}
	public String getMsrp() {
		return msrp;
	}
	public void setMsrp(String msrp) {
		this.msrp = msrp;
	}
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
	public List<Config> getAllocation() {
		return allocation;
	}
	public void setAllocation(List<Config> allocation) {
		this.allocation = allocation;
	}
	public String getSubModelImage() {
		return subModelImage;
	}
	public void setSubModelImage(String subModelImage) {
		this.subModelImage = subModelImage;
	}
	public List<VsVersion> getVsVersionList() {
		return vsVersionList;
	}
	public void setVsVersionList(List<VsVersion> vsVersionList) {
		this.vsVersionList = vsVersionList;
	}
	public String getLoseStr() {
		return loseStr;
	}
	public void setLoseStr(String loseStr) {
		this.loseStr = loseStr;
	}
	public List<Lose> getLose() {
		return lose;
	}
	public void setLose(List<Lose> lose) {
		this.lose = lose;
	}
	public String getSubModelEname() {
		return subModelEname;
	}
	public void setSubModelEname(String subModelEname) {
		this.subModelEname = subModelEname;
	}
	
	
}
