package com.ways.app.module.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.module.dao.IPriceSalesOfTopDao;
import com.ways.framework.base.IBatisBaseDao;
@Repository("priceSalesOfTopDao")
public class PriceSalesOfTopDaoImpl extends IBatisBaseDao implements IPriceSalesOfTopDao {

	@Override
	public List getTopEchartOfOtherData(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesOfTop.getTopEchartOfOtherData",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getTopBarEchartOfModelData(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesOfTop.getTopBarEchartOfModelData",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getTopPriceLevelEchartOfModelData(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesOfTop.getTopPriceLevelEchartOfModelData",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getOtherThoroughOtherData(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesOfTop.getOtherThoroughOtherData",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getOtherThoroughModelData(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesOfTop.getOtherThoroughModelData",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List getOtherThoroughCityData(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesOfTop.getOtherThoroughCityData",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getModelThoroughData(Map<String, Object> params) {
		try {
			return this.getSqlMapClientTemplate().queryForList("priceSalesOfTop.getModelThoroughData",params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
