package com.ways.app.module.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.common.utils.AppFrameworkUtil;
import com.ways.app.common.utils.Constant;
import com.ways.app.module.dao.IPriceSalesCommonDao;
import com.ways.app.module.entity.AllAreaEntity;
import com.ways.app.module.entity.BaseTextEntity;
import com.ways.app.module.entity.Level0Entity;
import com.ways.app.module.entity.ManfBrandEntity;
import com.ways.app.module.entity.ManfBrandOfSpellEntity;
import com.ways.app.module.entity.ObjGrandEntity;
import com.ways.app.module.entity.ProvinceEntity;
import com.ways.app.module.entity.SgmAndCompareModelGrandEntity;
import com.ways.app.module.service.IPriceSalesCommonService;
@Service("priceSalesCommonService")
public class PriceSalesCommonServiceImpl implements IPriceSalesCommonService{
	@Autowired
	private IPriceSalesCommonDao priceSalesCommonDao;
	
	@Override
	public String getDate(HttpServletRequest request, Map<String, Object> params) {
		List<Map<String, String>> list = priceSalesCommonDao.getDate(null);
		List data = new ArrayList();
		String json = "[]";
		if(list != null && list.size() > 0){
			Map<String, Object> date1 = new HashMap<String, Object>();
			date1.put("max", list.get(0).get("MAXYM").replace("-0", "-"));
			date1.put("select", list.get(0).get("MAXYEAR") + "-1");
			date1.put("min", list.get(0).get("MINYM").replace("-0", "-"));
			data.add(date1);
			
			Map<String, Object> date2 = new HashMap<String, Object>();
			date2.put("max", list.get(0).get("MAXYM").replace("-0", "-"));
			date2.put("select", list.get(0).get("MAXYM").replace("-0", "-"));
			date2.put("min", list.get(0).get("MINYM").replace("-0", "-"));
			data.add(date2);
			
			Map<String, Object> date3 = new HashMap<String, Object>();
			date3.put("max", list.get(0).get("MAXYM").replace("-0", "-"));
			int year = Integer.parseInt(list.get(0).get("MAXYEAR")) - 1;
			date3.put("select", year + "-1");
			date3.put("min", list.get(0).get("MINYM").replace("-0", "-"));
			data.add(date3);
			
			Map<String, Object> date4 = new HashMap<String, Object>();
			date4.put("max", list.get(0).get("MAXYM").replace("-0", "-"));
			date4.put("select", year + "-" + list.get(0).get("MAXMONTH"));
			date4.put("min", list.get(0).get("MINYM").replace("-0", "-"));
			data.add(date4);
			json = AppFrameworkUtil.serializableJSONData(data);
		}
		return json;
	}

	@Override
	public String getSegment(HttpServletRequest request,Map<String, Object> params) {
		String tabType = params.get("tabType") == null ? "":params.get("tabType").toString();
		List<Level0Entity> list = priceSalesCommonDao.getSegment(params);
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List data = new ArrayList();
		
		//设置细分市场头
		Map<String,Object> headOfSon = new HashMap<String, Object>();
		headOfSon.put("id", "-99999");
		headOfSon.put("isGroup", true);
		headOfSon.put("level", 5);
		headOfSon.put("text", "子细分市场");
		
		Map<String,Object> head = new HashMap<String, Object>();
		head.put("id", "-9999");
		head.put("isGroup", true);
		head.put("level", 4);
		head.put("text", "细分市场");
		List headList = new ArrayList();
		headList.add(headOfSon);
		head.put("list", headList);
		
		Map<String,Object> headOfGroup = new HashMap<String, Object>();
		headOfGroup.put("id", "-999");
		headOfGroup.put("isGroup", true);
		headOfGroup.put("level", 3);
		headOfGroup.put("text", "细分市场分组");
		List headOfGroupList = new ArrayList();
		headOfGroupList.add(head);
		headOfGroup.put("list", headOfGroupList);
		
		Map<String,Object> classify = new HashMap<String, Object>();
		classify.put("id", "-99");
		classify.put("isGroup", true);
		classify.put("level", 2);
		classify.put("text", "分类");
		List classifyList = new ArrayList();
		classifyList.add(headOfGroup);
		classify.put("list", classifyList);
		
		Map<String,Object> all = new HashMap<String, Object>();
		all.put("id", "-9");
		all.put("isGroup", true);
		if(StringUtils.isBlank(tabType)){
			all.put("checked", true);
		}else{
			all.put("checked", false);
		}
		all.put("level", 1);
		all.put("text", "整体市场");
		List allList = new ArrayList();
		allList.add(classify);
		all.put("list", allList);
		
		data.add(all);
		
		//细分市场
		Map<String,Object> allData = new HashMap<String, Object>();
		allData.put("level", 1);
			allData.put("id", "-1");
		if(StringUtils.isBlank(tabType)){
			allData.put("checked", true);
		}else{
			allData.put("checked", false);
		}
		allData.put("text", "整体市场");
		allData.put("list", list);
		
		data.add(allData);
		
		resultMap.put("data", data);
		
		return AppFrameworkUtil.serializableJSONData(resultMap);
	}

	@Override
	public String getManf(HttpServletRequest request, Map<String, Object> params) {
		Map<String,Object> map = new HashMap<String, Object>();
		List data = new ArrayList();
		List<ManfBrandOfSpellEntity> allManf = priceSalesCommonDao.getAllManf(params);
		String id = "";
		if(allManf != null){
			for(ManfBrandOfSpellEntity fa:allManf){
				if(fa.getList() != null){
					for(ManfBrandEntity e:fa.getList()){
						if(e.isChecked()){
							id = id + "," + e.getId();
						}
					}
				}
			}
		}
		if(id.length() > 1){
			id = id.substring(1,id.length());
			params.put("objIds", id);
			List<ManfBrandOfSpellEntity> mainManf = priceSalesCommonDao.getMainManf(params);
			
			data.add(mainManf);
			data.add(allManf);
			map.put("data", data);
		}
		return AppFrameworkUtil.serializableJSONData(map);
	}

	@Override
	public String getBrand(HttpServletRequest request,Map<String, Object> params) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String,String>> id = priceSalesCommonDao.getDefaultBrandId(params);
		if(id != null && id.size() > 0){
			String ids = "";
			for(int i = 0; i < id.size(); i++){
				ids += id.get(i).get("ID");
				if(i != id.size() - 1){
					ids += ",";
				}
			}
			params.put("brandIds", ids);
			params.put("brandIds_sort", ","+ids+",");
		}
		
		List data = new ArrayList();
		List<ManfBrandOfSpellEntity> allBrand = priceSalesCommonDao.getAllBrand(params);
		List<ManfBrandOfSpellEntity> mainBrand = priceSalesCommonDao.getMainBrand(params);
		
		data.add(mainBrand);
		data.add(allBrand);
		map.put("data", data);
		
		return AppFrameworkUtil.serializableJSONData(map);
	}

	@Override
	public String getManfBrand(HttpServletRequest request,Map<String, Object> params) {
		Map<String,Object> map = new HashMap<String, Object>();
		List data = new ArrayList();
		List<ManfBrandOfSpellEntity> allManfBrand = priceSalesCommonDao.getAllManfBrand(params);
		String id = "";
		if(allManfBrand != null){
			for(ManfBrandOfSpellEntity fa:allManfBrand){
				if(fa.getList() != null){
					for(ManfBrandEntity e:fa.getList()){
						if(e.isChecked()){
							id = id + "," + e.getId();
						}
					}
				}
			}
		}
		if(id.length() > 1){
			id = id.substring(1,id.length());
			params.put("objIds", id);
			List<ManfBrandOfSpellEntity> mainManfBrand = priceSalesCommonDao.getMainManfBrand(params);
			
			data.add(mainManfBrand);
			data.add(allManfBrand);
			map.put("data", data);
		}
		return AppFrameworkUtil.serializableJSONData(map);
	}
	
	@Override
	public String getModel(HttpServletRequest request,Map<String, Object> params) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<Object> data = new ArrayList<Object>();
		
		List<SgmAndCompareModelGrandEntity> main = null;
		List<ObjGrandEntity> segment = null;
		List<ObjGrandEntity> brand = null;
		try{
			List<Map<String,String>> id = priceSalesCommonDao.getMaxSalesModelId(params);
			if(id != null && id.size() > 0){
				String ids = "";
				for(int i = 0; i < id.size(); i++){
					ids += id.get(i).get("ID");
					if(i != id.size() - 1){
						ids += ",";
					}
				}
				params.put("subModelIds", ids);
				params.put("subModelIds_sort", ","+ids+",");
			}else{
				params.put("subModelIds", Constant.PRICE_GRADE_DEFAULT_SUBMODEL);
				params.put("subModelIds_sort", ","+Constant.PRICE_GRADE_DEFAULT_SUBMODEL+",");
			}
		
			main = priceSalesCommonDao.getSgmAndCompareModel(params);
			segment = priceSalesCommonDao.getSegmentModel(params);
			brand = priceSalesCommonDao.getBrandModel(params);
			data.add(main);
			data.add(segment);
			data.add(brand);
			map.put("data", data);
		}catch(Exception e){
			e.printStackTrace();
		}
		return AppFrameworkUtil.serializableJSONData(map);
	}

	@Override
	public String getCondition(HttpServletRequest request,Map<String, Object> params) {
		List<Map<String,Object>> maxAndMin = priceSalesCommonDao.getMaxAndMin(params);
		
		Map<String,Object> price = new HashMap<String, Object>();
		List<Map<String,Object>> segment = new ArrayList<Map<String,Object>>();
		
		boolean isSetDefault = false;
		
		if(maxAndMin != null && maxAndMin.size() > 0){
			if(Integer.parseInt(maxAndMin.get(0).get("MAXVAL") + "") > 50 
					&& Integer.parseInt(maxAndMin.get(0).get("MINVAL") + "") < 5){
				price.put("max", 50);
				price.put("min", 5);
				isSetDefault = true;
			}else{
				price.put("max", maxAndMin.get(0).get("MAXVAL"));
				price.put("min", maxAndMin.get(0).get("MINVAL"));
			}
			
			for(int i = 0; i < maxAndMin.size(); i++){
				Map<String,Object> obj = new HashMap<String, Object>();
				obj.put("value", maxAndMin.get(i).get("INTERV") + "");
				obj.put("text", maxAndMin.get(i).get("INTERV") + "万");
				obj.put("selected", false);
				if(maxAndMin.size() == 3 && i == 1){
					obj.put("selected", true);
				}
				if(maxAndMin.size() != 3 && i == 0){
					obj.put("selected", true);
				}
				
				/* 默认改成固定值 start */
				if(maxAndMin.size() == 3 && isSetDefault){
					if(i == 0){
						obj.put("value", "2");
						obj.put("text", "2万");
					}else if(i == 1){
						obj.put("value", "5");
						obj.put("text", "5万");
					}else if(i == 2){
						obj.put("value", "10");
						obj.put("text", "10万");
					}
				}
				/* 默认改成固定值 end */
				
				segment.add(obj);
				if(i == (maxAndMin.size() - 1)){
					obj = new HashMap<String, Object>();
					obj.put("value", "-1");
					obj.put("text", "自定义");
					obj.put("selected", false);
					segment.add(obj);
				}
			}
			
		}
		
		Map<String,Object> data = new HashMap<String, Object>();
		
		if(params != null && "segment".equals(params.get("objectType"))){
			List<BaseTextEntity> orig = priceSalesCommonDao.getOriginal(params);
			data.put("original", orig);
		}
		
		if(params != null && !"model".equals(params.get("objectType"))){
			List<BaseTextEntity> bodyType = priceSalesCommonDao.getBodyType(params);
			List<BaseTextEntity> pooAttr = priceSalesCommonDao.getPooAttribute(params);
			data.put("bodyType", bodyType);
			data.put("poo", pooAttr);
		}
		data.put("price", price);
		data.put("segment", segment);
		
		return AppFrameworkUtil.serializableJSONData(data);
	}

	@Override
	public String getCity(HttpServletRequest request, Map<String, Object> params) {
		List<AllAreaEntity> area = priceSalesCommonDao.getAreaCityData();
		List<ProvinceEntity> pros = priceSalesCommonDao.getProvinceData();
		Map<String,Object> pro = new HashMap<String, Object>();
		pro.put("active", false);
		pro.put("checked", false);
		pro.put("id", 0);
		pro.put("text", "省份");
		pro.put("title", "省份");
		pro.put("list", pros);
		
		List data = new ArrayList();
		
		if(area != null && area.size() > 0){
			area.get(0).setActive(true);
			for(int i = 0; i < area.size(); i++){
				data.add(area.get(i));
			}
			data.add(pro);
		}
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("data", data);
		
		return AppFrameworkUtil.serializableJSONData(resultMap);
	}

	@Override
	public String getEchartHeadInfo(HttpServletRequest request,Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String,Object> data = new HashMap<String, Object>();
		String objectType = (String) params.get("objectType");
		if(objectType != null){
			if("segment".equals(objectType)){
				data.put("objectType", "1");
			}else if("manf".equals(objectType)){
				data.put("objectType", "2");
			}else if("brand".equals(objectType)){
				data.put("objectType", "3");
			}else if("manfBrand".equals(objectType)){
				data.put("objectType", "4");
			}else{
				data.put("objectType", "5");
			}
		}
		
		List<Object> dateOptions = new ArrayList<Object>();
		if("t".equals(params.get("isVsTime"))){
			Map<String,Object> dateOptionsDatas = new HashMap<String, Object>();
			dateOptionsDatas.put("text", params.get("startDateKey"));
			dateOptionsDatas.put("value", params.get("startDateKey"));
			dateOptionsDatas.put("selected", true);
			dateOptionsDatas.put("checked", true);
			dateOptions.add(dateOptionsDatas);
			
			dateOptionsDatas = new HashMap<String, Object>();
			dateOptionsDatas.put("text", params.get("endDateKey"));
			dateOptionsDatas.put("value", params.get("endDateKey"));
			dateOptionsDatas.put("selected", false);
			dateOptionsDatas.put("checked", false);
			dateOptions.add(dateOptionsDatas);
		}else{
			Map<String,Object> dateOptionsDatas = new HashMap<String, Object>();
			dateOptionsDatas.put("text", params.get("startDateKey"));
			dateOptionsDatas.put("value", params.get("startDateKey"));
			dateOptionsDatas.put("selected", true);
			dateOptionsDatas.put("checked", true);
			dateOptions.add(dateOptionsDatas);
		}
		data.put("dateOptions", dateOptions);
		resultMap.put("data", data);
		return AppFrameworkUtil.serializableJSONData(resultMap);
	}

	@Override
	public String getThoroughHead(HttpServletRequest request,Map<String, Object> params) {
		Map<String, Object> obj = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("text", params.get("seriesName") + " " + params.get("priceName") + " 排名销量");
		
		if("segment".equals(params.get("objectType"))){
			List<Object> titleOptions = new ArrayList<Object>();
			
			Map<String, Object> segment = new HashMap<String, Object>();
			segment.put("text", "细分市场");
			segment.put("id", "segment");
			segment.put("value", "segment");
			segment.put("checked", true);
			titleOptions.add(segment);
			
			Map<String, Object> carType = new HashMap<String, Object>();
			carType.put("text", "汽车类型");
			carType.put("id", "carType");
			carType.put("value", "carType");
			carType.put("checked", false);
			titleOptions.add(carType);
			
			Map<String, Object> brand = new HashMap<String, Object>();
			brand.put("text", "品牌");
			brand.put("id", "brand");
			brand.put("value", "brand");
			brand.put("checked", false);
			titleOptions.add(brand);
			
			Map<String, Object> manf = new HashMap<String, Object>();
			manf.put("text", "厂商");
			manf.put("id", "manf");
			manf.put("value", "manf");
			manf.put("checked", false);
			titleOptions.add(manf);
			
			Map<String, Object> manfBrand = new HashMap<String, Object>();
			manfBrand.put("text", "厂商品牌");
			manfBrand.put("id", "manfBrand");
			manfBrand.put("value", "manfBrand");
			manfBrand.put("checked", false);
			titleOptions.add(manfBrand);
			
			Map<String, Object> orig = new HashMap<String, Object>();
			orig.put("text", "系别");
			orig.put("id", "orig");
			orig.put("value", "orig");
			orig.put("checked", false);
			titleOptions.add(orig);
			
			Map<String, Object> poo = new HashMap<String, Object>();
			poo.put("text", "产地属性");
			poo.put("id", "poo");
			poo.put("value", "poo");
			poo.put("checked", false);
			titleOptions.add(poo);
			
			Map<String, Object> city = new HashMap<String, Object>();
			city.put("text", "城市层级");
			city.put("id", "city");
			city.put("value", "city");
			city.put("checked", false);
			titleOptions.add(city);
			
			data.put("titleOptions", titleOptions);
			
		}else if("manf".equals(params.get("objectType"))){
			List<Object> titleOptions = new ArrayList<Object>();
			
			Map<String, Object> segment = new HashMap<String, Object>();
			segment.put("text", "细分市场");
			segment.put("id", "segment");
			segment.put("value", "segment");
			segment.put("checked", false);
			titleOptions.add(segment);
			
			Map<String, Object> carType = new HashMap<String, Object>();
			carType.put("text", "汽车类型");
			carType.put("id", "carType");
			carType.put("value", "carType");
			carType.put("checked", false);
			titleOptions.add(carType);
			
			Map<String, Object> manf = new HashMap<String, Object>();
			manf.put("text", "厂商");
			manf.put("id", "manf");
			manf.put("value", "manf");
			manf.put("checked", true);
			titleOptions.add(manf);
			
			Map<String, Object> poo = new HashMap<String, Object>();
			poo.put("text", "产地属性");
			poo.put("id", "poo");
			poo.put("value", "poo");
			poo.put("checked", false);
			titleOptions.add(poo);
			
			Map<String, Object> city = new HashMap<String, Object>();
			city.put("text", "城市层级");
			city.put("id", "city");
			city.put("value", "city");
			city.put("checked", false);
			titleOptions.add(city);
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("text", "车型");
			model.put("id", "model");
			model.put("value", "model");
			model.put("checked", false);
			titleOptions.add(model);
			
			data.put("titleOptions", titleOptions);
		}else if("brand".equals(params.get("objectType"))){
			List<Object> titleOptions = new ArrayList<Object>();
			
			Map<String, Object> segment = new HashMap<String, Object>();
			segment.put("text", "细分市场");
			segment.put("id", "segment");
			segment.put("value", "segment");
			segment.put("checked", false);
			titleOptions.add(segment);
			
			Map<String, Object> carType = new HashMap<String, Object>();
			carType.put("text", "汽车类型");
			carType.put("id", "carType");
			carType.put("value", "carType");
			carType.put("checked", false);
			titleOptions.add(carType);
			
			Map<String, Object> brand = new HashMap<String, Object>();
			brand.put("text", "品牌");
			brand.put("id", "brand");
			brand.put("value", "brand");
			brand.put("checked", true);
			titleOptions.add(brand);
			
			Map<String, Object> poo = new HashMap<String, Object>();
			poo.put("text", "产地属性");
			poo.put("id", "poo");
			poo.put("value", "poo");
			poo.put("checked", false);
			titleOptions.add(poo);
			
			Map<String, Object> city = new HashMap<String, Object>();
			city.put("text", "城市层级");
			city.put("id", "city");
			city.put("value", "city");
			city.put("checked", false);
			titleOptions.add(city);
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("text", "车型");
			model.put("id", "model");
			model.put("value", "model");
			model.put("checked", false);
			titleOptions.add(model);
			
			data.put("titleOptions", titleOptions);
		}else if("manfBrand".equals(params.get("objectType"))){
			List<Object> titleOptions = new ArrayList<Object>();
			
			Map<String, Object> segment = new HashMap<String, Object>();
			segment.put("text", "细分市场");
			segment.put("id", "segment");
			segment.put("value", "segment");
			segment.put("checked", false);
			titleOptions.add(segment);
			
			Map<String, Object> carType = new HashMap<String, Object>();
			carType.put("text", "汽车类型");
			carType.put("id", "carType");
			carType.put("value", "carType");
			carType.put("checked", false);
			titleOptions.add(carType);
			
			Map<String, Object> manfBrand = new HashMap<String, Object>();
			manfBrand.put("text", "厂商品牌");
			manfBrand.put("id", "manfBrand");
			manfBrand.put("value", "manfBrand");
			manfBrand.put("checked", true);
			titleOptions.add(manfBrand);
			
			Map<String, Object> poo = new HashMap<String, Object>();
			poo.put("text", "产地属性");
			poo.put("id", "poo");
			poo.put("value", "poo");
			poo.put("checked", false);
			titleOptions.add(poo);
			
			Map<String, Object> city = new HashMap<String, Object>();
			city.put("text", "城市层级");
			city.put("id", "city");
			city.put("value", "city");
			city.put("checked", false);
			titleOptions.add(city);
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("text", "车型");
			model.put("id", "model");
			model.put("value", "model");
			model.put("checked", false);
			titleOptions.add(model);
			
			data.put("titleOptions", titleOptions);
		}else{
			data.put("titleOptions", "no");
		}
		
		obj.put("data", data);
		
		return AppFrameworkUtil.serializableJSONData(obj);
	}
	
}
