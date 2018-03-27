package com.ways.app.module.entity;

import java.util.List;

public class Level1Entity {
	private int id;
	private int level = 3;
	private String text;
	private List<Level2Entity> list;
	private boolean checked = false;
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<Level2Entity> getList() {
		return list;
	}
	public void setList(List<Level2Entity> list) {
		this.list = list;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
