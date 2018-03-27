package com.ways.app.module.entity;

import java.util.List;

public class AllAreaEntity {
	private boolean active = false;
	private boolean checked = false;
	private String text;
	private String title;
	private String id;
	private List<AreaEntity> list;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<AreaEntity> getList() {
		return list;
	}
	public void setList(List<AreaEntity> list) {
		this.list = list;
	}
	
}
