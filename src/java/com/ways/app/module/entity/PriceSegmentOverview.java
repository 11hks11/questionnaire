package com.ways.app.module.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 价格段分析总览数据
 * @author caixu
 *
 */
public class PriceSegmentOverview implements Serializable{

	private static final long serialVersionUID = 1L;

	private String segment;
	private String accBqSales;
	private String accTqSales;
	private String growthRate;
	private String bqmix;
	private String tqmix;
	private String mixChange;
	private String totalBqAccSales;
	private String totalTqAccSales;
	private String totalGrowthRate;
	private List<DimensionDataEntity> list = new ArrayList<DimensionDataEntity>();
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getGrowthRate() {
		return growthRate;
	}
	public void setGrowthRate(String growthRate) {
		this.growthRate = growthRate;
	}
	public String getMixChange() {
		return mixChange;
	}
	public void setMixChange(String mixChange) {
		this.mixChange = mixChange;
	}
	public List<DimensionDataEntity> getList() {
		return list;
	}
	public void setList(List<DimensionDataEntity> list) {
		this.list = list;
	}
	
	public String getTotalGrowthRate() {
		return totalGrowthRate;
	}
	public void setTotalGrowthRate(String totalGrowthRate) {
		this.totalGrowthRate = totalGrowthRate;
	}
	public String getAccBqSales() {
		return accBqSales;
	}
	public void setAccBqSales(String accBqSales) {
		this.accBqSales = accBqSales;
	}
	public String getAccTqSales() {
		return accTqSales;
	}
	public void setAccTqSales(String accTqSales) {
		this.accTqSales = accTqSales;
	}
	public String getTotalBqAccSales() {
		return totalBqAccSales;
	}
	public void setTotalBqAccSales(String totalBqAccSales) {
		this.totalBqAccSales = totalBqAccSales;
	}
	public String getTotalTqAccSales() {
		return totalTqAccSales;
	}
	public void setTotalTqAccSales(String totalTqAccSales) {
		this.totalTqAccSales = totalTqAccSales;
	}
	public String getBqmix() {
		return bqmix;
	}
	public void setBqmix(String bqmix) {
		this.bqmix = bqmix;
	}
	public String getTqmix() {
		return tqmix;
	}
	public void setTqmix(String tqmix) {
		this.tqmix = tqmix;
	}
	
}
