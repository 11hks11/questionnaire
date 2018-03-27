package com.ways.app.module.entity;

public class SonModelEntity {
	private String id;
	private String text;
	private boolean sgm;
	private boolean checked;
	private boolean importChecked;
	private Integer orderNum;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isImportChecked() {
		return importChecked;
	}
	public void setImportChecked(boolean importChecked) {
		this.importChecked = importChecked;
	}
	public boolean isSgm() {
		return sgm;
	}
	public void setSgm(boolean sgm) {
		this.sgm = sgm;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
}
