package com.ways.app.module.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

public interface IPriceSalesOfTopService {
	/**
	 * 获取echart图数据
	 * @param request
	 * @param params
	 * @return
	 */
	public String getTopEchartData(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 获取钻取数据
	 * @param request
	 * @param params
	 * @return
	 */
	public String getEchartThoroughTableData(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 图表下载
	 * @param request
	 * @param params
	 * @return
	 */
	public Workbook exportEchartData(HttpServletRequest request,Map<String, Object> params);
	
	/**
	 * 下载 表格钻取内容
	 * @param request
	 * @param params
	 * @return
	 */
	public Workbook exportThoroughTableData(HttpServletRequest request,Map<String, Object> params);
}
