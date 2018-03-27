package com.ways.app.priceOptimize.dao;

import java.util.HashMap;
import java.util.List;

import com.ways.app.priceOptimize.entity.DateValue;
import com.ways.app.priceOptimize.entity.ModelBase;
import com.ways.app.priceOptimize.entity.ModelComp;
import com.ways.app.priceOptimize.entity.SalesData;

public interface PreOptimizeDao {

	List<DateValue> getDate(HashMap map);

	List<ModelBase> getBaseModel(HashMap map);

	List<ModelComp> ModelComp(HashMap map);

	List<SalesData> getSalesDistribute(HashMap map);

}
