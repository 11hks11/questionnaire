package com.ways.app.module.entity;

import java.util.ArrayList;
import java.util.List;

public class CityEntity {
	private boolean active = false;
	private boolean checked = false;
	private String text;
	private String id;
	private List list = new ArrayList();
	
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
		this.id = "c_" + id;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	
}
