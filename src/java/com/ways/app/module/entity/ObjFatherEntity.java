package com.ways.app.module.entity;

import java.util.List;

public class ObjFatherEntity {
	private String text;
	private boolean checked = false;
	private List<SonModelEntity> list;
	
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
	public List<SonModelEntity> getList() {
		return list;
	}
	public void setList(List<SonModelEntity> list) {
		this.list = list;
	}
	
}
