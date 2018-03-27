package com.ways.app.priceOptimize.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.priceOptimize.dao.OptimizeDao;
import com.ways.app.priceOptimize.entity.AttentionAndSatisfaction;
import com.ways.app.priceOptimize.entity.Configuration;
import com.ways.app.priceOptimize.entity.Mix;
import com.ways.app.priceOptimize.entity.SubModel;
import com.ways.app.priceOptimize.entity.Version;
import com.ways.framework.base.IBatisBaseDao;

@Repository("OptimizeDao")
public class OptimizeDaoImpl extends IBatisBaseDao implements OptimizeDao{

	@Override
	public List<SubModel> getComparisonModel(Map<String, String> paramMap) {
		try {
			return this.getSqlMapClient().queryForList("optimize.getComparisonModel",paramMap);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Mix> getMixData(Map<String, Object> paramMap) {
		try {
			return this.getSqlMapClient().queryForList("optimize.getMixData",paramMap);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Configuration> getConfigList(Map<String, Object> paramMap) {
		try {
			return this.getSqlMapClient().queryForList("optimize.getConfigList",paramMap);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AttentionAndSatisfaction> getAttentionAndSatisfaction(
			Map<String, Object> paramMap) {
		try {
			return this.getSqlMapClient().queryForList("optimize.getAttentionAndSatisfaction",paramMap);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Version> getDataByVersionIds(Map<String,String> paramsMap) {
		try {
			return this.getSqlMapClient().queryForList("optimize.getDataByVersionIds",paramsMap);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Version> getLoseByVersionIds(Map<String, String> paramsMap) {
		try {
			return this.getSqlMapClient().queryForList("optimize.getLoseByVersionIds",paramsMap);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Version> getSubModelByVersionId(String versionId) {
		try {
			return this.getSqlMapClient().queryForList("optimize.getSubModelByVersionId",versionId);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void saveReportParams(Map<String, String> param) throws SQLException {
			this.getSqlMapClient().insert("optimize.saveReportParams",param);
	}

	@Override
	public void deleteReportParams(Map<String, String> param) throws SQLException {
		this.getSqlMapClient().delete("optimize.deleteReportParams",param);
	}

}
