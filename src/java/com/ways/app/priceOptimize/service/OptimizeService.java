package com.ways.app.priceOptimize.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ways.app.priceOptimize.entity.PriceRange;
import com.ways.app.priceOptimize.entity.SubModel;

public interface OptimizeService {

	/**
	 * 获取对比车型
	 * @param paramMap
	 * @return
	 * @author liuyuhuan
	 * @date 2018-2-5
	 */
	Map<String,Object> getComparisonModel(Map<String, String> paramMap, HttpServletRequest request);

	/**
	 * describe: 导出对标车型
	 * @authod liuyuhuan
	 * @date 2018-2-7 上午11:29:03
	 */
	void ExportComparisonModel(HttpServletResponse response, HttpServletRequest request);

	/**
	 * describe: 获取产品机会点分析数据
	 * @authod liuyuhuan
	 * @date 2018-2-8 下午5:46:28
	 */
	Map<String, Object> getAnalyzeData(Map<String, Object> paramMap,HttpServletRequest request);

	/**
	 * describe: 获取最终报告
	 * @param allVersionId 
	 * @authod liuyuhuan
	 * @date 2018-3-7 下午12:05:29
	 */
	Map<String,Object> getReportData(List<PriceRange> priceList, String[] params, 
			Set<String> allVersionId, String yqId ,String time, HttpServletRequest request);

	/**
	 * describe: 导出最终报告
	 * @authod liuyuhuan
	 * @date 2018-3-12 下午12:00:07
	 */
	void ExportReportData(HttpServletResponse response,
			HttpServletRequest request);

	/**
	 * describe: 导出产品分析数据
	 * @authod liuyuhuan
	 * @date 2018-3-13 上午9:15:00
	 */
	void ExportAnalyzeData(HttpServletResponse response,
			HttpServletRequest request);

	/**
	 * describe: 保存最终报告的请求参数
	 * @authod liuyuhuan
	 * @date 2018-3-13 下午8:10:46
	 */
	Map<String, Object> saveReportParams(Map<String, String> param);

	/**
	 * describe: 删除最终报告的请求参数
	 * @authod liuyuhuan
	 * @date 2018-3-14 下午12:01:05
	 */
	Map<String, Object> deleteReportParams(Map<String, String> param);

}
