package com.ways.app.priceOptimize.entity;

import java.util.List;
import java.util.Set;


public class PriceRange {

	private int min;  //价格区间的最小价格
	private String priceRange;  //价格区间 
	private List<MyVersion> myList;
	private Set<String> myVersion;
	private List<Version> versionList;
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public String getPriceRange() {
		return priceRange;
	}
	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}
	public List<MyVersion> getMyList() {
		return myList;
	}
	public void setMyList(List<MyVersion> myList) {
		this.myList = myList;
	}
	public Set<String> getMyVersion() {
		return myVersion;
	}
	public void setMyVersion(Set<String> myVersion) {
		this.myVersion = myVersion;
	}
	public List<Version> getVersionList() {
		return versionList;
	}
	public void setVersionList(List<Version> versionList) {
		this.versionList = versionList;
	}
	
	
	
}
