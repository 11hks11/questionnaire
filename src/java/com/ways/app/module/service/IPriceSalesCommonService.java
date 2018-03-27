package com.ways.app.module.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface IPriceSalesCommonService {
	/**
	 * 获取时间
	 * @param params
	 * @return
	 */
	public String getDate(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 获取钻取的头部数据
	 * @param params
	 * @return
	 */
	public String getThoroughHead(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 获取城市
	 * @param params
	 * @return
	 */
	public String getCity(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 获取细分市场
	 * @param params
	 * @return
	 */
	public String getSegment(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 获取厂商
	 * @param params
	 * @return
	 */
	public String getManf(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 获取品牌
	 * @param params
	 * @return
	 */
	public String getBrand(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 获取厂商品牌
	 * @param params
	 * @return
	 */
	public String getManfBrand(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 获取车型
	 * @param params
	 * @return
	 */
	public String getModel(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 获取筛选条件
	 * @param params
	 * @return
	 */
	public String getCondition(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 设置echart图头部信息
	 * @param params
	 * @return
	 */
	public String getEchartHeadInfo(HttpServletRequest request,Map<String, Object> params);
	
}
