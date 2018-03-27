package com.ways.app.module.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.module.entity.Level0Entity;

public interface IPriceSalesCommonDao {
	/**
	 * 获取时间
	 * @param params
	 * @return
	 */
	public List getDate(Map<String, Object> params);
	
	/**
	 * 获取细分市场
	 * @param params
	 * @return
	 */
	public List<Level0Entity> getSegment(Map<String, Object> params);
	
	/**
	 * 获取主要厂商
	 * @param params
	 * @return
	 */
	public List getMainManf(Map<String, Object> params);
	
	/**
	 * 获取全部厂商
	 * @param params
	 * @return
	 */
	public List getAllManf(Map<String, Object> params);
	
	/**
	 * 获取全部品牌
	 * @param params
	 * @return
	 */
	public List getAllBrand(Map<String, Object> params);
	
	/**
	 * 获取主要品牌
	 * @param params
	 * @return
	 */
	public List getMainBrand(Map<String, Object> params);
	
	/**
	 * 获取全部厂商品牌
	 * @param params
	 * @return
	 */
	public List getAllManfBrand(Map<String, Object> params);
	
	/**
	 * 获取主要厂商品牌
	 * @param params
	 * @return
	 */
	public List getMainManfBrand(Map<String, Object> params);
	
	/**
	 * 获取主要厂商品牌
	 * @param params
	 * @return
	 */
	public List getSgmAndCompareModel(Map<String, Object> params);
	
	/**
	 * 获取 细分市场 车型
	 * @param params
	 * @return
	 */
	public List getSegmentModel(Map<String, Object> params);
	
	/**
	 * 获取品牌 车型
	 * @param params
	 * @return
	 */
	public List getBrandModel(Map<String, Object> params);
	
	/**
	 * 获取 车身形式
	 * @param params
	 * @return
	 */
	public List getBodyType(Map<String, Object> params);
	
	/**
	 * 获取 系别
	 * @param params
	 * @return
	 */
	public List getOriginal(Map<String, Object> params);
	
	/**
	 * 获取 产地属性
	 * @param params
	 * @return
	 */
	public List getPooAttribute(Map<String, Object> params);
	
	/**
	 * 获取 最大最小值
	 * @param params
	 * @return
	 */
	public List getMaxAndMin(Map<String, Object> params);
	
	/**
	 * 获取 大区
	 * @param params
	 * @return
	 */
	public List getAreaCityData();
	
	/**
	 * 获取 省份
	 * @param params
	 * @return
	 */
	public List getProvinceData();
	
	/**
	 * 获取10辆销量最大的车型ID
	 * @param params
	 * @return
	 */
	public List getMaxSalesModelId(Map<String, Object> params);
	
	/**
	 * 获取5个默认品牌
	 * @param params
	 * @return
	 */
	public List getDefaultBrandId(Map<String, Object> params);
}
