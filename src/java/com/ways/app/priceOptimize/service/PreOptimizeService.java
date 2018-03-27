package com.ways.app.priceOptimize.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;

import com.ways.app.priceOptimize.entity.DateValue;
import com.ways.app.priceOptimize.entity.ModelBase;
import com.ways.app.priceOptimize.entity.ModelComp;
import com.ways.app.priceOptimize.entity.SalesData;

public interface PreOptimizeService {

	List<DateValue> getDate(HttpServletRequest request, HashMap map);

	List<ModelBase> getBaseModel(HttpServletRequest request, HashMap map);

	List<ModelComp> getCompModel(HttpServletRequest request, HashMap map);

	String getSalesDistribute(HttpServletRequest request, HashMap<String,String> map);

	Workbook exportTableData(HttpServletRequest request, Map<String, Object> params);

}
