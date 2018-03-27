package com.ways.app.module.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ways.app.module.dao.IWholeMarketDao;
import com.ways.app.module.entity.PriceSegmentOverview;
import com.ways.framework.base.IBatisBaseDao;
@Repository("wholeMarketDao")
public class WholeMarketDaoImpl extends IBatisBaseDao implements IWholeMarketDao{

	@Override
	public List<PriceSegmentOverview> getWholeMarketTotalData(Map<String, String> param) {
		String tabType = param.get("tabType");//tab页
		String type = param.get("type");//月均、累计
		String xmlName = "priceSalesAnalysis";
		if("1".equals(type)){
			xmlName = "avgPriceSalesAnalysis";
		}
		return this.getSqlMapClientTemplate().queryForList(""+xmlName+".getSalesAnalysis", param);
	}

	@Override
	public List<PriceSegmentOverview> getWholeMarketOriginalData(Map<String, String> param) {
		List<PriceSegmentOverview> list = null;
		String tabType = param.get("tabType");//tab页
		String attributeSql = "";
		String type = param.get("type");//月均、累计
		String xmlName = "priceSalesAnalysis";
		if("1".equals(type)){
			xmlName = "avgPriceSalesAnalysis";
		}
		if("1".equals(tabType)){
			attributeSql = "l.original_id,l.original_name,";
			param.put("attributeSql", attributeSql);
			list = this.getSqlMapClientTemplate().queryForList(""+xmlName+".getOriginalData", param);
		}else if("t-7".equals(tabType)){
			attributeSql = "l.car_type_id,l.car_type_name,";
			param.put("attributeSql", attributeSql);
			list = this.getSqlMapClientTemplate().queryForList(""+xmlName+".getCarTypeData", param);
		}else if("t-1".equals(tabType)){
			//attributeSql = "l.sub_grade_id,l.sub_grade_name,";
			param.put("attributeSql", attributeSql);
			if("2".equals(type)){
				list = this.getSqlMapClientTemplate().queryForList("priceSalesAnalysis_grade.getGradeData", param);
			}else {
				list = this.getSqlMapClientTemplate().queryForList("priceSalesAnalysis_grade.getGradeAvgData", param);
			}
		}else if("t-3".equals(tabType)){
			attributeSql = "l.brand_id,l.brand_name,";
			param.put("attributeSql", attributeSql);
			list = this.getSqlMapClientTemplate().queryForList(""+xmlName+".getBrandData", param);
		}else if("t-2".equals(tabType)){
			attributeSql = "l.manf_id,l.manf_name,";
			param.put("attributeSql", attributeSql);
			list = this.getSqlMapClientTemplate().queryForList(""+xmlName+".getManfData", param);
		}else if("t-4".equals(tabType)){
			attributeSql = "l.manf_brand_id,l.manf_brand_name,";
			param.put("attributeSql", attributeSql);
			list = this.getSqlMapClientTemplate().queryForList(""+xmlName+".getManfBrandData", param);
		}else if("3".equals(tabType)){//产地属性
			attributeSql = "l.poo_attribute_id,l.poo_attribute_name,";
			param.put("attributeSql", attributeSql);
			list = this.getSqlMapClientTemplate().queryForList(""+xmlName+".getPooData", param);
		}else if("4".equals(tabType)){
			attributeSql = "l.citygrade_id,l.citygrade_name,";
			param.put("attributeSql", attributeSql);
			list = this.getSqlMapClientTemplate().queryForList(""+xmlName+".getCityGradeData", param);
		}else if("t-5".equals(tabType)){//产地属性 钻取 车型/型号
			attributeSql = "l.sub_model_id,l.sub_model_name,";
			param.put("attributeSql", attributeSql);
			list = this.getSqlMapClientTemplate().queryForList(""+xmlName+".getModelData", param);
		}else if("t-6".equals(tabType)){
			list = this.getSqlMapClientTemplate().queryForList(""+xmlName+".getVersionModelData", param);
		}
		return list;
	}

	@Override
	public List getYearModel(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("priceSalesAnalysis.getYearModel", param);
	}

}
