package com.ways.app.module.entity;

import java.util.List;

public class ProvinceEntity {
	private boolean active = false;
	private boolean checked = false;
	private String text;
	private String id;
	private List<CityEntity> list;
	
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
		this.id = "p_" + id;
	}
	public List<CityEntity> getList() {
		return list;
	}
	public void setList(List<CityEntity> list) {
		this.list = list;
	}
	
}
