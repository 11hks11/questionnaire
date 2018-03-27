package com.ways.app.module.entity;

import java.util.List;

public class ManfBrandOfSpellEntity {
	private String text;
	private List<ManfBrandEntity> list;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<ManfBrandEntity> getList() {
		return list;
	}
	public void setList(List<ManfBrandEntity> list) {
		this.list = list;
	}
	
}
