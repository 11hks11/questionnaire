package com.ways.app.priceOptimize.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ways.app.priceOptimize.dao.PreOptimizeDao;
import com.ways.app.priceOptimize.entity.DateValue;
import com.ways.app.priceOptimize.entity.ModelBase;
import com.ways.app.priceOptimize.entity.ModelComp;
import com.ways.app.priceOptimize.entity.SalesData;
import com.ways.framework.base.IBatisBaseDao;


@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository("PreOptimizeDao")
public class PreOptimizeDaoImpl extends IBatisBaseDao implements PreOptimizeDao{

	@Override
	public List<DateValue> getDate(HashMap map) {
		return (List<DateValue>) getSqlMapClientTemplate().queryForList(
				"preoptimize.getDate", map);
	}

	@Override
	public List<ModelBase> getBaseModel(HashMap map) {
		return (List<ModelBase>) getSqlMapClientTemplate().queryForList(
				"preoptimize.getBaseModel", map);
	}

	@Override
	public List<ModelComp> ModelComp(HashMap map) {
		return (List<ModelComp>) getSqlMapClientTemplate().queryForList(
				"preoptimize.getCompModel", map);
	}
	
	@Override
	public List<SalesData> getSalesDistribute(HashMap map) {
		return (List<SalesData>) getSqlMapClientTemplate().queryForList(
				"preoptimize.getSalesDistribute", map);
	}



}
