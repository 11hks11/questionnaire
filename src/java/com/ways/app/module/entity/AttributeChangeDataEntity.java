package com.ways.app.module.entity;

import java.io.Serializable;

/**
 * 价格段分析各维度变化数据
 * @author caixu
 *
 */
public class AttributeChangeDataEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	private String sales;
	private String salesChange;
	private String mix;
	private String mixChange;
	private String shares;
	private String sharesChange;
	
	private String totalMix;//最后一行total(一定为100%)
	private String totalMixChange;//(一定为0%)
	private String totalSales;
	private String totalSalesChange;
	private String totalShares;
	private String totalSharesChange;
	
	private String allMix;//最后一列total
	private String allMixchange;
	private String allSales;
	private String allSalesChange;
	private String allShares;
	private String allSharesChange;
	
	private String allTotalSales;//所选系别总的销量（total行、total列交叉数据，mix固定为100%和0%，份额不需要显示）
	private String allTotalSalesChange;//所选系别总的销量变化
	
	private String versionId;//型号id
	private String versionName;//型号名称
	
	public String getSalesChange() {
		return salesChange;
	}
	public void setSalesChange(String salesChange) {
		this.salesChange = salesChange;
	}
	public String getMix() {
		return mix;
	}
	public void setMix(String mix) {
		this.mix = mix;
	}
	public String getMixChange() {
		return mixChange;
	}
	public void setMixChange(String mixChange) {
		this.mixChange = mixChange;
	}
	public String getShares() {
		return shares;
	}
	public void setShares(String shares) {
		this.shares = shares;
	}
	public String getSharesChange() {
		return sharesChange;
	}
	public void setSharesChange(String sharesChange) {
		this.sharesChange = sharesChange;
	}
	public String getTotalMix() {
		return totalMix;
	}
	public void setTotalMix(String totalMix) {
		this.totalMix = totalMix;
	}
	public String getTotalMixChange() {
		return totalMixChange;
	}
	public void setTotalMixChange(String totalMixChange) {
		this.totalMixChange = totalMixChange;
	}
	public String getTotalSalesChange() {
		return totalSalesChange;
	}
	public void setTotalSalesChange(String totalSalesChange) {
		this.totalSalesChange = totalSalesChange;
	}
	public String getTotalShares() {
		return totalShares;
	}
	public void setTotalShares(String totalShares) {
		this.totalShares = totalShares;
	}
	public String getTotalSharesChange() {
		return totalSharesChange;
	}
	public void setTotalSharesChange(String totalSharesChange) {
		this.totalSharesChange = totalSharesChange;
	}
	public String getAllMix() {
		return allMix;
	}
	public void setAllMix(String allMix) {
		this.allMix = allMix;
	}
	public String getAllMixchange() {
		return allMixchange;
	}
	public void setAllMixchange(String allMixchange) {
		this.allMixchange = allMixchange;
	}
	public String getAllSalesChange() {
		return allSalesChange;
	}
	public void setAllSalesChange(String allSalesChange) {
		this.allSalesChange = allSalesChange;
	}
	public String getAllShares() {
		return allShares;
	}
	public void setAllShares(String allShares) {
		this.allShares = allShares;
	}
	public String getAllSharesChange() {
		return allSharesChange;
	}
	public void setAllSharesChange(String allSharesChange) {
		this.allSharesChange = allSharesChange;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public String getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(String totalSales) {
		this.totalSales = totalSales;
	}
	public String getAllSales() {
		return allSales;
	}
	public void setAllSales(String allSales) {
		this.allSales = allSales;
	}
	public String getAllTotalSales() {
		return allTotalSales;
	}
	public void setAllTotalSales(String allTotalSales) {
		this.allTotalSales = allTotalSales;
	}
	public String getAllTotalSalesChange() {
		return allTotalSalesChange;
	}
	public void setAllTotalSalesChange(String allTotalSalesChange) {
		this.allTotalSalesChange = allTotalSalesChange;
	}
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
	
}
