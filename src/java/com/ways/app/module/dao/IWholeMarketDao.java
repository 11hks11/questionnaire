package com.ways.app.module.dao;

import java.util.List;
import java.util.Map;

import com.ways.app.module.entity.PriceSegmentOverview;

public interface IWholeMarketDao {

	/**
	 * 获取整体市场价格段概览
	 */
	public List<PriceSegmentOverview> getWholeMarketTotalData(Map<String,String> param);
	
	/**
	 * 获取整体市场系别数据
	 */
	public List<PriceSegmentOverview> getWholeMarketOriginalData(Map<String,String> param);
	
	/**
	 * 根据默认子车型获取年款数据
	 * @param param
	 * @return
	 */
	public List getYearModel(Map<String,String> param);
}
