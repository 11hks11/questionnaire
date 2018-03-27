package com.ways.app.module.dao;

import java.util.List;
import java.util.Map;

public interface IPriceSalesOfTopDao {
	/**
	 * 获取非车型echart图数据
	 * @param params
	 * @return
	 */
	public List getTopEchartOfOtherData(Map<String, Object> params);
	
	/**
	 * 获取车型echart图数据（柱状图）
	 * @param params
	 * @return
	 */
	public List getTopBarEchartOfModelData(Map<String, Object> params);
	
	/**
	 * 获取车型echart图数据（梯度）
	 * @param params
	 * @return
	 */
	public List getTopPriceLevelEchartOfModelData(Map<String, Object> params);
	
	/**
	 * 获取 非车型 钻取 非车型维度数据
	 * @param params
	 * @return
	 */
	public List getOtherThoroughOtherData(Map<String, Object> params);
	
	/**
	 * 获取 非车型 钻取 车型维度数据
	 * @param params
	 * @return
	 */
	public List getOtherThoroughModelData(Map<String, Object> params);
	
	/**
	 * 获取 非车型 钻取 城市层级
	 * @param params
	 * @return
	 */
	public List getOtherThoroughCityData(Map<String, Object> params);
	
	/**
	 * 获取 车型 钻取 车型维度数据
	 * @param params
	 * @return
	 */
	public List getModelThoroughData(Map<String, Object> params);
}
