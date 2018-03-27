package com.ways.app.priceOptimize.entity;

import java.util.List;

public class SubModel {

	private String rank;
	private String subModelId;
	private String subModelName;
	private String subModelEname;
	private String subModelSales;
	private List<Version> versionList;
	private String speedUp;  //增速
	private String proportion;  //占比
	private String proportionChange;  //占比变化
	private String share;  //份额
	private String shareChange;  //份额变化
	private List<PriceRange> priceList;
	private String imagePath;
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getSubModelName() {
		return subModelName;
	}
	public void setSubModelName(String subModelName) {
		this.subModelName = subModelName;
	}
	public String getSubModelSales() {
		return subModelSales;
	}
	public void setSubModelSales(String subModelSales) {
		this.subModelSales = subModelSales;
	}
	public List<Version> getVersionList() {
		return versionList;
	}
	public void setVersionList(List<Version> versionList) {
		this.versionList = versionList;
	}
	public String getSpeedUp() {
		return speedUp;
	}
	public void setSpeedUp(String speedUp) {
		this.speedUp = speedUp;
	}
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	public String getProportionChange() {
		return proportionChange;
	}
	public void setProportionChange(String proportionChange) {
		this.proportionChange = proportionChange;
	}
	public String getShare() {
		return share;
	}
	public void setShare(String share) {
		this.share = share;
	}
	public String getShareChange() {
		return shareChange;
	}
	public void setShareChange(String shareChange) {
		this.shareChange = shareChange;
	}
	public String getSubModelId() {
		return subModelId;
	}
	public void setSubModelId(String subModelId) {
		this.subModelId = subModelId;
	}
	public List<PriceRange> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<PriceRange> priceList) {
		this.priceList = priceList;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getSubModelEname() {
		return subModelEname;
	}
	public void setSubModelEname(String subModelEname) {
		this.subModelEname = subModelEname;
	}
	
	
}
