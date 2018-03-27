package com.ways.app.module.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

public interface IWholeMarketService {

	/**
	 * 获取整体市场价格段概览数据
	 */
	public String getWholeMarketTotalData(Map<String,String> param) throws Exception;
	/**
	 * 获取系别价格段概览数据
	 */
	public String getOriginalTotalData(Map<String,String> param) throws Exception;
	
	/**
	 * 获取各维度下tab页
	 */
	public String getTabInfo(Map<String,String> param) throws Exception;
	
	/**
	 * 获取各维度下tab页
	 */
	public Workbook exportBottomTableData(HttpServletRequest request,Map<String,Object> param) throws Exception;
	
}
