package com.ways.app.module.entity;

import java.util.List;

public class AreaEntity {
	private boolean active = false;
	private boolean checked = false;
	private String text;
	private String id;
	private List<ProvinceEntity> list;
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = "a_" + id;
	}
	public List<ProvinceEntity> getList() {
		return list;
	}
	public void setList(List<ProvinceEntity> list) {
		this.list = list;
	}
	
}
