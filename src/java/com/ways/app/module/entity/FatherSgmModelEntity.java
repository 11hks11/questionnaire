package com.ways.app.module.entity;

import java.util.List;


public class FatherSgmModelEntity {
	private String id;
	private String text;
	private boolean checked;
	private boolean sgm;
	private boolean importChecked;
	private List<SonModelEntity> list;
	
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
	public List<SonModelEntity> getList() {
		return list;
	}
	public void setList(List<SonModelEntity> list) {
		this.list = list;
	}
	public boolean isSgm() {
		return sgm;
	}
	public void setSgm(boolean sgm) {
		this.sgm = sgm;
	}
	
}
