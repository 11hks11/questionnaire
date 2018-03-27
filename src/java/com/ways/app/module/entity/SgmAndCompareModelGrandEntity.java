package com.ways.app.module.entity;

import java.util.List;


public class SgmAndCompareModelGrandEntity {
	private String text;
	private List<FatherSgmModelEntity> list;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<FatherSgmModelEntity> getList() {
		return list;
	}
	public void setList(List<FatherSgmModelEntity> list) {
		this.list = list;
	}
	
}
