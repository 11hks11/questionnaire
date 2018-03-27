package com.ways.app.module.entity;

import java.util.List;

public class ObjGrandEntity {
	private String text;
	private List<ObjFatherEntity> list;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<ObjFatherEntity> getList() {
		return list;
	}
	public void setList(List<ObjFatherEntity> list) {
		this.list = list;
	}
	
}
