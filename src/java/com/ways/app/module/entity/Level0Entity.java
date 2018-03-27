package com.ways.app.module.entity;

import java.util.List;

public class Level0Entity {
	private int id;
	private int level = 2;
	private String text;
	private List<Level1Entity> list;
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
	public List<Level1Entity> getList() {
		return list;
	}
	public void setList(List<Level1Entity> list) {
		this.list = list;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
