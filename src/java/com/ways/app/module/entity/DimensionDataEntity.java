package com.ways.app.module.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 价格段分析各维度数据
 * @author caixu
 *
 */
public class DimensionDataEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	private String attributeId;
	private String attributeName;
	private List<AttributeChangeDataEntity> list;
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public List<AttributeChangeDataEntity> getList() {
		return list;
	}
	public void setList(List<AttributeChangeDataEntity> list) {
		this.list = list;
	}
	
}
