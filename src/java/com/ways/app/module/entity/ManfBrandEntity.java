package com.ways.app.module.entity;

public class ManfBrandEntity {
	private boolean checked = false;
	private String id;
	private String text;
	private Integer orderNum;
	
	public boolean isChecked() {
		return checked;
	}
	public void setSchecked(boolean checked) {
		this.checked = true;
	}
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
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
}
