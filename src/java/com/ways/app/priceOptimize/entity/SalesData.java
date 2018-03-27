package com.ways.app.priceOptimize.entity;

import java.util.List;

public class SalesData {
	
	
	private String segment;
	private String bqSales;
	private String growth;
	private String bqMix;
	private String mixChange;
	private String baseBqSales;
	private String baseGrowth;
	private String baseBqMix;
	private String baseMixChange;
	private String baseModelShare;
	private String baseShareChange;
	private List<CompData> compDateList;
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getBqSales() {
		return bqSales;
	}
	public void setBqSales(String bqSales) {
		this.bqSales = bqSales;
	}
	public String getGrowth() {
		return growth;
	}
	public void setGrowth(String growth) {
		this.growth = growth;
	}
	public String getBqMix() {
		return bqMix;
	}
	public void setBqMix(String bqMix) {
		this.bqMix = bqMix;
	}
	public String getMixChange() {
		return mixChange;
	}
	public void setMixChange(String mixChange) {
		this.mixChange = mixChange;
	}
	public String getBaseBqSales() {
		return baseBqSales;
	}
	public void setBaseBqSales(String baseBqSales) {
		this.baseBqSales = baseBqSales;
	}
	public String getBaseGrowth() {
		return baseGrowth;
	}
	public void setBaseGrowth(String baseGrowth) {
		this.baseGrowth = baseGrowth;
	}
	public String getBaseBqMix() {
		return baseBqMix;
	}
	public void setBaseBqMix(String baseBqMix) {
		this.baseBqMix = baseBqMix;
	}
	public String getBaseMixChange() {
		return baseMixChange;
	}
	public void setBaseMixChange(String baseMixChange) {
		this.baseMixChange = baseMixChange;
	}
	public String getBaseModelShare() {
		return baseModelShare;
	}
	public void setBaseModelShare(String baseModelShare) {
		this.baseModelShare = baseModelShare;
	}
	public String getBaseShareChange() {
		return baseShareChange;
	}
	public void setBaseShareChange(String baseShareChange) {
		this.baseShareChange = baseShareChange;
	}
	public List<CompData> getCompDateList() {
		return compDateList;
	}
	public void setCompDateList(List<CompData> compDateList) {
		this.compDateList = compDateList;
	}
	
	
	
	
	
	
	
	
	
	
}
