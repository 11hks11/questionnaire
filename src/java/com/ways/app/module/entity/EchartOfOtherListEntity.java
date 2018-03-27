package com.ways.app.module.entity;

import java.io.Serializable;
import java.util.List;

public class EchartOfOtherListEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String dateKey;
	private String rn;
	private List<EchartOfOtherMapEntity> list;
	
	public String getDateKey() {
		return dateKey;
	}
	public void setDateKey(String dateKey) {
		this.dateKey = dateKey;
	}
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
	}
	public List<EchartOfOtherMapEntity> getList() {
		return list;
	}
	public void setList(List<EchartOfOtherMapEntity> list) {
		this.list = list;
	}
	
}
