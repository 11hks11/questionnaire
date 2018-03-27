package com.ways.app.priceOptimize.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ways.app.priceOptimize.entity.AttentionAndSatisfaction;
import com.ways.app.priceOptimize.entity.Configuration;
import com.ways.app.priceOptimize.entity.Mix;
import com.ways.app.priceOptimize.entity.SubModel;
import com.ways.app.priceOptimize.entity.Version;

public interface OptimizeDao {

	/**
	 * 获取对比车型
	 * @param paramMap
	 * @return
	 * @author liuyuhuan
	 * @date 2018-2-5
	 */
	List<SubModel> getComparisonModel(Map<String, String> paramMap);

	/**
	 * describe: 获取mix值和缺失抱怨数据
	 * @authod liuyuhuan
	 * @date 2018-2-9 上午11:42:12
	 */
	List<Mix> getMixData(Map<String, Object> paramMap);

	/**
	 * describe: 获取优势配置
	 * @authod liuyuhuan
	 * @date 2018-3-2 上午9:44:28
	 */
	List<Configuration> getConfigList(Map<String, Object> paramMap);

	/**
	 * describe: 获取本品的关注度，满意度 和 竞品的满意度
	 * @authod liuyuhuan
	 * @date 2018-3-5 下午12:27:20
	 */
	List<AttentionAndSatisfaction> getAttentionAndSatisfaction(
			Map<String, Object> paramMap);

	/**
	 * describe: 根据型号id获取型号信息
	 * @authod liuyuhuan
	 * @date 2018-3-7 下午12:17:46
	 */
	List<Version> getDataByVersionIds(Map<String, String> paramsMap);

	/**
	 * describe: 根据型号id获取缺失抱怨
	 * @authod liuyuhuan
	 * @date 2018-3-7 下午3:52:33
	 */
	List<Version> getLoseByVersionIds(Map<String, String> paramsMap);

	/**
	 * describe: 根据型号id获取车型信息
	 * @authod liuyuhuan
	 * @date 2018-3-8 下午12:31:46
	 */
	List<Version> getSubModelByVersionId(String myVersionId);

	/**
	 * describe: 保存最终报告的请求参数
	 * @throws SQLException 
	 * @authod liuyuhuan
	 * @date 2018-3-13 下午8:16:49
	 */
	void saveReportParams(Map<String, String> param) throws SQLException;

	/**
	 * describe: 删除最终报告的请求参数
	 * @throws SQLException 
	 * @authod liuyuhuan
	 * @date 2018-3-14 上午9:02:12
	 */
	void deleteReportParams(Map<String, String> param) throws SQLException;

}
