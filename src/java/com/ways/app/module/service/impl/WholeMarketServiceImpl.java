package com.ways.app.module.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ways.app.common.utils.Constant;
import com.ways.app.common.utils.PriceUtil;
import com.ways.app.common.utils.SalesManfMarketNewExeportExeclUtil;
import com.ways.app.module.dao.IWholeMarketDao;
import com.ways.app.module.entity.AttributeChangeDataEntity;
import com.ways.app.module.entity.DimensionDataEntity;
import com.ways.app.module.entity.PriceSegmentOverview;
import com.ways.app.module.service.IWholeMarketService;
import com.ways.app.module.utils.PriceStringUtil;
@SuppressWarnings("unchecked")
@Service("wholeMarketService")
public class WholeMarketServiceImpl implements IWholeMarketService{

	@Autowired
	private IWholeMarketDao wholeDao;
	
	SalesManfMarketNewExeportExeclUtil styleUtil = new SalesManfMarketNewExeportExeclUtil();
	
	@Override
	public String getWholeMarketTotalData(Map<String, String> param) throws Exception{
		// TODO Auto-generated method stub
		String result = "";
		Integer maxPrice = Integer.parseInt(param.get("maxPrice"));
		Integer minPrice = Integer.parseInt(param.get("minPrice"));
		Integer segmentPrice =Integer.parseInt(param.get("segmentPrice"));
		String priceType = param.get("sources");
		String tabType = param.get("tabType");
		String leftSegmentSql = PriceStringUtil.getPriceSegment2(maxPrice, minPrice, segmentPrice);
		String priceSegmentSql = PriceStringUtil.getPriceSegment(maxPrice, minPrice, segmentPrice,priceType);
		param.put("leftSegmentSql", leftSegmentSql);
		param.put("priceSegmentSql", priceSegmentSql);
		List<PriceSegmentOverview> list = null;
		if("-1".equals(tabType)){//价格段概览
			list =  wholeDao.getWholeMarketTotalData(param);
			result = this.getWholeMarketData(list,param);
		}else {
			list =  wholeDao.getWholeMarketOriginalData(param);
			if("t-6".equals(tabType)){//型号
				result = this.getVersionData(list);
			}else{
				//result = this.getOriginalData(list,datatype,isShowChange,drill,tabType);
				result = this.getOriginalData(list,param);
			}
		}
		//result = this.getWholeMarketData(list);
		return result;
	}
	

	@Override
	public String getOriginalTotalData(Map<String, String> param)
			throws Exception {
		String result = "";
		Integer maxPrice = Integer.parseInt(param.get("maxPrice"));
		Integer minPrice = Integer.parseInt(param.get("minPrice"));
		Integer segmentPrice =Integer.parseInt(param.get("segmentPrice"));
		String priceType = param.get("sources");
		String dateYoY = param.get("dateYoY");
		String dateShow = param.get("dateShow");
		String drill = param.get("drill");
		String isShowChange = "1";//是否有对比时间，dateYoY、dateShow都为false的时候表示不显示对比数据
		if("false".equals(dateYoY) && "false".equals(dateShow)){
			isShowChange = "2";
		}
		String datatype = param.get("tabDataType");
		String leftSegmentSql = PriceStringUtil.getPriceSegment2(maxPrice, minPrice, segmentPrice);
		String priceSegmentSql = PriceStringUtil.getPriceSegment(maxPrice, minPrice, segmentPrice,priceType);
		param.put("leftSegmentSql", leftSegmentSql);
		param.put("priceSegmentSql", priceSegmentSql);
		List<PriceSegmentOverview> list =  wholeDao.getWholeMarketOriginalData(param);
		String tabType = param.get("tabType");
		if("t-6".equals(tabType)){//型号
			result = this.getVersionData(list);
		}else{
			result = this.getOriginalData(list,param);
		}
		return result;
	}
	
	/**
	 * 价格段概览数据组装
	 * @param list
	 * @return
	 */
	public String getWholeMarketData(List<PriceSegmentOverview> list,Map<String, String> param){
		String type = param.get("type");
		String name = "累计";
		if("1".equals(type)){
			name = "月均";
		}
		JSONObject result = new JSONObject();
		//标题行
		JSONArray titleArray = new JSONArray();
		JSONObject title = new JSONObject();
		title.put("title", "价格段");
		titleArray.add(title);
		title = new JSONObject();
		title.put("title", name+"销量");
		JSONObject percent = new JSONObject();
		percent.put("width", "300px");
		title.put("titleStyle", percent);
		titleArray.add(title);
		title = new JSONObject();
		title.put("title", "增速");
		titleArray.add(title);
		title = new JSONObject();
		title.put("title", "本期Mix");
		titleArray.add(title);
		title = new JSONObject();
		title.put("title", "同期Mix");
		titleArray.add(title);
		title = new JSONObject();
		title.put("title", "Mix变化");
		percent = new JSONObject();
		percent.put("width", "300px");
		title.put("titleStyle", percent);
		titleArray.add(title);
		
		//数据
		JSONArray dataArray = new JSONArray();
		JSONArray segmentData = null;
		JSONArray percentArr = null;
		JSONObject percentObj = null;
		Double maxSales = 0d;
		if(list!=null && list.size()>0){
			//计算累计销量区间最大值，作为柱状图基数
			for(int j=0;j<list.size();j++){
				Double sales = 0d;
				if(list.get(j).getAccBqSales()!=null && !list.get(j).getAccBqSales().equals("-")){
					sales = Double.parseDouble(list.get(j).getAccBqSales());
					if(sales > maxSales){
						maxSales = sales;
					}
				}
				if(list.get(j).getAccTqSales()!=null && !list.get(j).getAccTqSales().equals("-")){
					sales = Double.parseDouble(list.get(j).getAccTqSales());
					if(sales > maxSales){
						maxSales = sales;
					}
				}
			}
			
			for(int i=0;i<list.size();i++){
				segmentData = new JSONArray();
				segmentData.add(list.get(i).getSegment()!=null?list.get(i).getSegment():"-");
				percentArr = new JSONArray();
				percentObj = new JSONObject();
				percentObj.put("text", list.get(i).getAccBqSales()!=null?list.get(i).getAccBqSales():"-");
				if(list.get(i).getAccBqSales()!=null && !list.get(i).getAccBqSales().equals("-")){
					Double per = Double.parseDouble(list.get(i).getAccBqSales()) / maxSales;
					percentObj.put("percent",per);
				}else {
					percentObj.put("percent",0);
				}
				percentArr.add(percentObj);
				
				percentObj = new JSONObject();
				percentObj.put("text", list.get(i).getAccTqSales()!=null?list.get(i).getAccTqSales():"-");
				if(list.get(i).getAccTqSales()!=null && !list.get(i).getAccTqSales().equals("-")){
					Double per = Double.parseDouble(list.get(i).getAccTqSales()) / maxSales;
					percentObj.put("percent",per);
				}else {
					percentObj.put("percent",0);
				}
				//percentObj.put("percent","0.5");
				percentArr.add(percentObj);
				segmentData.add(percentArr);
				//segmentData.add(list.get(i).getAccBqSales()!=null?list.get(i).getAccBqSales():"-");
				//segmentData.add(list.get(i).getAccTqSales()!=null?list.get(i).getAccTqSales():"-");
				segmentData.add(!list.get(i).getGrowthRate().equals("-")?list.get(i).getGrowthRate()+"%":"-");
				segmentData.add(!list.get(i).getBqmix().equals("-")?list.get(i).getBqmix()+"%":"-");
				segmentData.add(!list.get(i).getTqmix().equals("-")?list.get(i).getTqmix()+"%":"-");
				segmentData.add(!list.get(i).getMixChange().equals("-")?list.get(i).getMixChange()+"%@percent":"-");	
				dataArray.add(segmentData);
				//Total行写入
				if(i==list.size()-1){
					segmentData = new JSONArray();
					segmentData.add("Total");
					segmentData.add(list.get(i).getTotalBqAccSales()!=null?list.get(i).getTotalBqAccSales():"-");
					//segmentData.add(list.get(i).getTotalTqAccSales()!=null?list.get(i).getTotalTqAccSales():"-");
					segmentData.add(!list.get(i).getTotalGrowthRate().equals("-")?list.get(i).getTotalGrowthRate()+"%":"-");
					segmentData.add("100%");
					segmentData.add("100%");
					segmentData.add("0.0%");	
					dataArray.add(segmentData);
				}
			}
		}
		
		result.put("columns", titleArray);
		result.put("data", dataArray);
		
		if(list != null && list.size() > 0){ 
			result.put("exportNoTotal", list.get(list.size()-1).getTotalTqAccSales()!=null?list.get(list.size()-1).getTotalTqAccSales():"-"); 
		} else{ 
			result.put("exportNoTotal", "-"); 
		}
		return result.toString();
	}
	
	/**
	 * 数据组装
	 * @param list
	 * @return
	 */
	public String getOriginalData(List<PriceSegmentOverview> list,Map<String, String> param){
		String drill = param.get("drill");
		String priceType = param.get("sources");
		String dateYoY = param.get("dateYoY");
		String dateShow = param.get("dateShow");
		String isShowChange = "1";//是否有对比时间，dateYoY、dateShow都为false的时候表示不显示对比数据
		if("false".equals(dateYoY) && "false".equals(dateShow)){
			isShowChange = "2";//1表示不显示对比信息
		}
		String tabType = param.get("tabType");
		String type = param.get("tabDataType");
		String acctype = param.get("type");
		String name = "累计";
		if("1".equals(acctype)){
			name = "月均";
		}
		
		String title1="MIX";
		String title2="MIX变化";
		if("1".equals(type)){//type：mix、sales、shares
			title1 = "销量";
			title2 = "增速";
		}else if("3".equals(type)){
			title1 = "份额";
			title2 = "份额变化";
		}
		JSONObject result = new JSONObject();
		//标题行
		JSONArray titleArray = new JSONArray();
		JSONObject title = new JSONObject();
		title.put("title", "价格段");
		title.put("rowSpan", 2);
		titleArray.add(title);
		title = new JSONObject();
		title.put("title", name+"销量");
		title.put("rowSpan", 2);
		JSONObject percent = new JSONObject();
		percent.put("width", "300px");
		title.put("titleStyle", percent);
		titleArray.add(title);
		title = new JSONObject();
		title.put("title", "增速");
		title.put("rowSpan", 2);
		titleArray.add(title);
		title = new JSONObject();
		title.put("title", "MIX");
		title.put("rowSpan", 2);
		titleArray.add(title);
		title = new JSONObject();
		title.put("title", "MIX变化");
		title.put("rowSpan", 2);
		titleArray.add(title);
		
		//数据
		JSONArray dataArray = new JSONArray();
		JSONArray segmentData = null;//区间数据
		JSONArray segmentTotalData = null;//最后一行total总数据
		JSONArray children = null;
		JSONObject childType = null;
		PriceSegmentOverview overview = null;
		JSONArray percentArr = null;
		JSONObject percentObj = null;
		Double maxSales = 0d;
		if(list!=null && list.size()>0){
			//计算累计销量区间最大值，作为柱状图基数
			for(int j=0;j<list.size();j++){
				Double sales = 0d;
				if(list.get(j).getAccBqSales()!=null && !list.get(j).getAccBqSales().equals("-")){
					sales = Double.parseDouble(list.get(j).getAccBqSales());
					if(sales > maxSales){
						maxSales = sales;
					}
				}
				if(list.get(j).getAccTqSales()!=null && !list.get(j).getAccTqSales().equals("-")){
					sales = Double.parseDouble(list.get(j).getAccTqSales());
					if(sales > maxSales){
						maxSales = sales;
					}
				}
			}
			
			for(int i=0;i<list.size();i++){
				overview = list.get(i);
				DimensionDataEntity dde= null;
				if(overview!=null){
					//总览数据写入
					segmentData = new JSONArray();
					segmentData.add(overview.getSegment()!=null?overview.getSegment():"-");
					percentArr = new JSONArray();
					percentObj = new JSONObject();
					percentObj.put("text", list.get(i).getAccBqSales()!=null?list.get(i).getAccBqSales():"-");
					if(list.get(i).getAccBqSales()!=null && !list.get(i).getAccBqSales().equals("-")){
						Double per = Double.parseDouble(list.get(i).getAccBqSales()) / maxSales;
						percentObj.put("percent",per);
					}else {
						percentObj.put("percent",0);
					}
					percentArr.add(percentObj);
					
					percentObj = new JSONObject();
					percentObj.put("text", list.get(i).getAccTqSales()!=null?list.get(i).getAccTqSales():"-");
					if(list.get(i).getAccTqSales()!=null && !list.get(i).getAccTqSales().equals("-")){
						Double per = Double.parseDouble(list.get(i).getAccTqSales()) / maxSales;
						percentObj.put("percent",per);
					}else {
						percentObj.put("percent",0);
					}
					percentArr.add(percentObj);
					segmentData.add(percentArr);
					//segmentData.add(overview.getAccBqSales()!=null?overview.getAccBqSales():"-");
					//segmentData.add(overview.getAccTqSales()!=null?overview.getAccTqSales():"-");
					segmentData.add(!overview.getGrowthRate().equals("-")?overview.getGrowthRate()+"%":"-");
					segmentData.add(!overview.getBqmix().equals("-")?overview.getBqmix()+"%":"-");
					//segmentData.add(overview.getTqmix()!=null?overview.getTqmix():"-");
					segmentData.add(!overview.getMixChange().equals("-")?overview.getMixChange()+"%":"-");	
					//dataArray.add(segmentData);
					
					List<DimensionDataEntity> dimensionList = overview.getList();
					if(dimensionList !=null && dimensionList.size()>0){
						for(int j=0;j<dimensionList.size();j++){
							dde= dimensionList.get(j);
							if(dde!=null){
								if(i == 0){//插入title
									title = new JSONObject();
									title.put("title", dde.getAttributeName());
									if(!"true".equals(drill) && !"t-5".equals(tabType)){//钻取的时候不能再钻取
										title.put("click", dde.getAttributeId());
									}else {
										title.put("click", "");
									}
									//title.put("colSpan", 2);
									children = new JSONArray();
									childType = new JSONObject();
									childType.put("title", title1);
									children.add(childType);
									if("1".equals(isShowChange)){
										title.put("colSpan", 2);
										childType = new JSONObject();
										childType.put("title", title2);
										children.add(childType);
									}else {
										title.put("colSpan", 1);
									}
									title.put("children", children);
									titleArray.add(title);
									if(!"3".equals(type)){
										if(j==dimensionList.size()-1){//插入最后一列total
											title = new JSONObject();
											title.put("title", "Total");
											title.put("click", "");
											//title.put("colSpan", 2);
											children = new JSONArray();
											childType = new JSONObject();
											childType.put("title", title1);
											children.add(childType);
											if("1".equals(isShowChange)){
												title.put("colSpan", 2);
												childType = new JSONObject();
												childType.put("title", title2);
												children.add(childType);
											}else {
												title.put("colSpan", 1);
											}
											title.put("children", children);
											titleArray.add(title);
										}
									}
								}
								//维度数据
								List<AttributeChangeDataEntity> attributeList = dde.getList();
								if(attributeList != null && attributeList.size()>0){
									for(int z=0;z<attributeList.size();z++){
										if("2".equals(type)){
											segmentData.add(!attributeList.get(z).getMix().equals("-")?attributeList.get(z).getMix()+"%":"-");
											if("1".equals(isShowChange)){
												segmentData.add(!attributeList.get(z).getMixChange().equals("-")?attributeList.get(z).getMixChange()+"%":"-");
											}
										}else if("1".equals(type)){
											segmentData.add(attributeList.get(z).getSales());
											if("1".equals(isShowChange)){
												segmentData.add(!attributeList.get(z).getSalesChange().equals("-")?attributeList.get(z).getSalesChange()+"%":"-");
											}
										}else if("3".equals(type)){
											segmentData.add(!attributeList.get(z).getShares().equals("-")?attributeList.get(z).getShares()+"%":"-");
											if("1".equals(isShowChange)){
												segmentData.add(!attributeList.get(z).getSharesChange().equals("-")?attributeList.get(z).getSharesChange()+"%":"-");
											}
										}
										
										if(j==dimensionList.size()-1){//插入最后一列total区间数据
											if("2".equals(type)){
												segmentData.add(!attributeList.get(z).getAllMix().equals("-")?attributeList.get(z).getAllMix()+"%":"-");
												if("1".equals(isShowChange)){
													segmentData.add(!attributeList.get(z).getAllMixchange().equals("-")?attributeList.get(z).getAllMixchange()+"%":"-");
												}
											}else if("1".equals(type)){
												segmentData.add(attributeList.get(z).getAllSales());
												if("1".equals(isShowChange)){
													segmentData.add(!attributeList.get(z).getAllSalesChange().equals("-")?attributeList.get(z).getAllSalesChange()+"%":"-");
												}
											}else if("3".equals(type)){//最后一列份额不显示
												/*segmentData.add(attributeList.get(z).getAllShares());
												segmentData.add(attributeList.get(z).getAllSharesChange());*/
											}
											
										}
									}
									
								}
								
								/*//Total行数据写入
								if(i==list.size()-1){
									AttributeChangeDataEntity acd = dde.getList().get(0);
									segmentTotalData.add("100%");
									segmentTotalData.add("0.0%");
									//segmentTotalData.add(acd.getTotalMix());
									//segmentTotalData.add(acd.getTotalMixChange());
								}*/
							}
							
						}
						
					}
					dataArray.add(segmentData);
					//Total行写入
					if(i==list.size()-1){
						segmentTotalData = new JSONArray();
						segmentTotalData.add("Total");
						segmentTotalData.add(overview.getTotalBqAccSales()!=null?overview.getTotalBqAccSales():"-");
						//segmentTotalData.add(overview.getTotalTqAccSales()!=null?overview.getTotalTqAccSales():"-");
						segmentTotalData.add(!overview.getTotalGrowthRate().equals("-")?overview.getTotalGrowthRate()+"%":"-");
						segmentTotalData.add("100%");
						segmentTotalData.add("0.0%");	
						//dataArray.add(segmentData);
						
						if(dimensionList !=null && dimensionList.size()>0){
							for(int j=0;j<dimensionList.size();j++){
								dde = dimensionList.get(j);
								AttributeChangeDataEntity acd = dde.getList().get(0);
								
								if("2".equals(type)){
									segmentTotalData.add("100%");
									if("1".equals(isShowChange)){
										segmentTotalData.add("0.0%");
									}
								}else if("1".equals(type)){
									segmentTotalData.add(acd.getTotalSales());
									if("1".equals(isShowChange)){
										segmentTotalData.add(acd.getTotalSalesChange().equals("-")?"-":acd.getTotalSalesChange()+"%");
									}
								}else if("3".equals(type)){
									segmentTotalData.add(acd.getTotalShares().equals("-")?"-":acd.getTotalShares()+"%");
									if("1".equals(isShowChange)){
										segmentTotalData.add(acd.getTotalSharesChange().equals("-")?"-":acd.getTotalSharesChange()+"%");
									}
								}
								if(j == dimensionList.size()-1){
									//插入最后一列、最后一行的total，
									if("2".equals(type)){
										segmentTotalData.add("100%");
										if("1".equals(isShowChange)){
											segmentTotalData.add("0.0%");
										}
									}else if("1".equals(type)){//最后一列所选属性总销量的总销量和总增速
										segmentTotalData.add(acd.getAllTotalSales());
										if("1".equals(isShowChange)){
											segmentTotalData.add(acd.getAllTotalSalesChange().equals("-")?"-":acd.getAllTotalSalesChange()+"%");
										}
									}else if("3".equals(type)){//最后一列份额不显示
										
									}
								}
							}
							
						}
						dataArray.add(segmentTotalData);
					}
				}
				
				
			}
		}
		
		result.put("columns", titleArray);
		result.put("data", dataArray);
		
		if(list != null && list.size() > 0){ 
			result.put("exportNoTotal", list.get(list.size()-1).getTotalTqAccSales()!=null?list.get(list.size()-1).getTotalTqAccSales():"-"); 
		} else{ 
			result.put("exportNoTotal", "-"); 
		}
		
		return result.toString();
	}
	
	/**
	 * 型号数据组装
	 */
	public String getVersionData(List<PriceSegmentOverview> list){

		JSONObject result = new JSONObject();
		//标题行
		JSONArray titleArray = new JSONArray();
		JSONObject title = new JSONObject();
		title.put("title", "价格段");
		title.put("rowSpan", 2);
		titleArray.add(title);
		
		//数据
		JSONArray dataArray = new JSONArray();
		JSONArray segmentData = null;//区间数据
		JSONArray segmentTotalData = null;//最后一行total总数据
		JSONArray children = null;
		JSONObject childType = null;
		PriceSegmentOverview overview = null;
		
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				overview = list.get(i);
				
				DimensionDataEntity dde= null;
				if(overview!=null){
					//总览数据写入
					segmentData = new JSONArray();
					segmentData.add(overview.getSegment()!=null?overview.getSegment():"-");
					
					List<DimensionDataEntity> dimensionList = overview.getList();
					
					if(dimensionList !=null && dimensionList.size()>0){
						for(int j=0;j<dimensionList.size();j++){
							dde= dimensionList.get(j);
							if(dde!=null){
								if(i == 0){//插入title
									title = new JSONObject();
									title.put("title", dde.getAttributeName());
									title.put("click", "");
									title.put("colSpan", 3);
									children = new JSONArray();
									
									childType = new JSONObject();
									childType.put("title", "销量");
									children.add(childType);
									
									childType = new JSONObject();
									childType.put("title", "MIX");
									children.add(childType);
									
									childType = new JSONObject();
									childType.put("title", "型号");
									children.add(childType);
									
									/*children.add("销量");
									children.add("MIX");
									children.add("型号");*/
									title.put("children", children);
									titleArray.add(title);
								}
								
								/*JSONArray salesArr = new JSONArray();
								JSONArray mixArr = new JSONArray();
								JSONArray versionArr = new JSONArray();
								//维度数据
								List<AttributeChangeDataEntity> attributeList = dde.getList();
								if(attributeList != null && attributeList.size()>0){
									for(int z=0;z<attributeList.size();z++){
										salesArr.add(attributeList.get(z).getSales());
										mixArr.add(attributeList.get(z).getMix().equals("-")?"-":(attributeList.get(z).getMix()+"%"));
										versionArr.add(attributeList.get(z).getVersionName());
									}
								}*/
								
								String salesArr = "";
								String mixArr = "";
								String versionArr = "";
								//维度数据
								List<AttributeChangeDataEntity> attributeList = dde.getList();
								if(attributeList != null && attributeList.size()>0){
									for(int z=0;z<attributeList.size();z++){
										salesArr += "@"+attributeList.get(z).getSales();
										mixArr += "@"+(attributeList.get(z).getMix().equals("-")?"-":(attributeList.get(z).getMix()+"%"));
										versionArr += "@"+attributeList.get(z).getVersionName();
									}
								}
								segmentData.add(salesArr!=""?salesArr.substring(1):"-");
								segmentData.add(mixArr!=""?mixArr.substring(1):"-");
								segmentData.add(versionArr!=""?versionArr.substring(1):"-");
							}
							
						}
						
					}
					dataArray.add(segmentData);
				}
				
				
			}
		}
		
		result.put("columns", titleArray);
		result.put("data", dataArray);
		return result.toString();
	}


	@Override
	public String getTabInfo(Map<String, String> param) throws Exception {
		String objectType = param.get("objectType");
		String drill = param.get("drill");
		String valIds = param.get("valIds");
		String cityIds = param.get("cityIds");//为-1，不显示城市层级
		JSONArray data = new JSONArray();
		JSONObject result = null;
		String[] tabInfo = null;
		String[] tabId = null;
		if(!"true".equals(drill)){
			if("2".equals(objectType) && valIds.indexOf(",")!=-1){//valIds.indexOf(",")!=-1 表示不止一个厂商、品牌，此时只显示价格段概览tab页
				tabInfo = new String[]{"价格段概览"};
				tabId = new String[]{"t-2"};
			}else if("3".equals(objectType) && valIds.indexOf(",")!=-1){
				tabInfo = new String[]{"价格段概览"};
				tabId = new String[]{"t-3"};
			}else if("4".equals(objectType) && valIds.indexOf(",")!=-1){
				tabInfo = new String[]{"价格段概览"};
				tabId = new String[]{"t-4"};
			}else if(!"-1".equals(cityIds)){
				if("1".equals(objectType) && "-1".equals(valIds)){//整体市场
					tabInfo = Constant.PRICE_WHOLEMARKET_TAB_INFO_1;
					tabId = Constant.PRICE_WHOLEMARKET_TAB_ID_1;
				}else if("1".equals(objectType) && !"-1".equals(valIds)){//细分市场
					tabInfo = Constant.PRICE_SUBGRADE_TAB_INFO_1;
					tabId = Constant.PRICE_SUBGRADE_TAB_ID_1;
				}else if("2".equals(objectType)){//厂商
					tabInfo = Constant.PRICE_MANF_TAB_INFO_1;
					tabId = Constant.PRICE_MANF_TAB_ID_1;
				}else if("3".equals(objectType)){//品牌
					tabInfo = Constant.PRICE_BRAND_TAB_INFO_1;
					tabId = Constant.PRICE_BRAND_TAB_ID_1;
				}else if("4".equals(objectType)){//厂商品牌
					tabInfo = Constant.PRICE_MANFBRAND_TAB_INFO_1;
					tabId = Constant.PRICE_MANFBRAND_TAB_ID_1;
				}
			}else{
				if("1".equals(objectType) && "-1".equals(valIds)){//整体市场
					tabInfo = Constant.PRICE_WHOLEMARKET_TAB_INFO;
					tabId = Constant.PRICE_WHOLEMARKET_TAB_ID;
				}else if("1".equals(objectType) && !"-1".equals(valIds)){//细分市场
					tabInfo = Constant.PRICE_SUBGRADE_TAB_INFO;
					tabId = Constant.PRICE_SUBGRADE_TAB_ID;
				}else if("2".equals(objectType)){//厂商
					tabInfo = Constant.PRICE_MANF_TAB_INFO;
					tabId = Constant.PRICE_MANF_TAB_ID;
				}else if("3".equals(objectType)){//品牌
					tabInfo = Constant.PRICE_BRAND_TAB_INFO;
					tabId = Constant.PRICE_BRAND_TAB_ID;
				}else if("4".equals(objectType)){//厂商品牌
					tabInfo = Constant.PRICE_MANFBRAND_TAB_INFO;
					tabId = Constant.PRICE_MANFBRAND_TAB_ID;
				}/*else if("5".equals(objectType)){//车型
					tabInfo = Constant.PRICE_SUBMODEL_TAB_INFO;
					tabId = Constant.PRICE_SUBMODEL_TAB_ID;
				}*/
			}
			if(!"5".equals(objectType)){
				for(int i=0;i<tabInfo.length;i++){
					if("汽车类型".equals(tabInfo[i])){//屏蔽 汽车类型
						continue;
					}
					result = new JSONObject();
					result.put("text", tabInfo[i]);
					result.put("id", tabId[i]);
					if(i==0){
						result.put("active", "true");
					}
					data.add(result);
				}
			}
		}
		
		if("5".equals(objectType) || "true".equals(drill)){//车型,格式不同，另外处理
			tabInfo = Constant.PRICE_SUBMODEL_TAB_INFO;
			tabId = Constant.PRICE_SUBMODEL_TAB_ID;
			result = new JSONObject();
			result.put("text", tabInfo[0]);
			result.put("id", tabId[0]);
			result.put("value", tabId[0]);
			data.add(result);
			
			result = new JSONObject();
			result.put("text", tabInfo[1]);
			result.put("id", tabId[1]);
			result.put("value", tabId[1]);
			
			//JSONObject timeOptions = new JSONObject();
			JSONObject timeInfo = new JSONObject();
			JSONArray time = new JSONArray();
			String date = param.get("date");
			String[] dateArr = date.split(",");
			timeInfo.put("text", dateArr[0]+"~"+dateArr[1]);
			timeInfo.put("value", dateArr[0]+"~"+dateArr[1]);
			timeInfo.put("checked", true);
			time.add(timeInfo);
			//有对比时间的情况,date数据已做补全
			if("true".equals(param.get("dateYoY")) || "true".equals(param.get("dateShow"))){
				timeInfo = new JSONObject();
				timeInfo.put("text", dateArr[2]+"~"+dateArr[3]);
				timeInfo.put("value", dateArr[2]+"~"+dateArr[3]);
				time.add(timeInfo);
			}
			result.put("timeOptions", time);
			
			//年款数据
			param.put("subModelIds", Constant.PRICE_MODEL_DEFAULT_SUBMODEL);
			List list = wholeDao.getYearModel(param);
			//timeOptions = new JSONObject();
			
			time = new JSONArray();
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					timeInfo = new JSONObject();
					Map map = (Map) list.get(i);
					Object val = map.get("MODELYEAR");
					timeInfo.put("text", val.toString()+"款");
					timeInfo.put("value", val.toString());
					time.add(timeInfo);
				}
				result.put("versionOptions", time);
			}
			data.add(result);
		}
		JSONObject obj = new JSONObject();
		obj.put("data", data);
		return obj.toString();
	}


	@Override
	public Workbook exportBottomTableData(HttpServletRequest request,Map<String, Object> param) throws Exception {
		
		String tabType = param.get("tabType").toString();//-1为 价格段概览，1为系别， 
		String json = (String) param.get("exportDatas");
		Workbook wb =  new SXSSFWorkbook();
		if("-1".equals(tabType)){//价格段概览
			exportWholeMarketDataExcel(wb, param, request, json); 
		} else if("t-6".equals(tabType)){ //型号
			exportVersionData(wb, request, param);
		} else{//其他维度
			exportWholeMarketDataExcel2(wb, param, request, json);
		}
		return wb;
	}
	
	/** 
	 * 导出价格段概览 
	 * @param wb 
	 * @param paramsMap 
	 * @param request 
	 * @param json 
	 */ 
	@SuppressWarnings("static-access") 
	private void exportWholeMarketDataExcel(Workbook wb, Map<String, Object> paramsMap, HttpServletRequest request, String json) { 
		JSONObject object = JSONObject.parseObject(json);
		JSONArray columnsArray = object.getJSONArray("columns"); 
		JSONArray dataArray = object.getJSONArray("data"); 
		
		CellStyle evenTextStyle =styleUtil.getExeclLastRowEvenContentCellStyle1(wb);//偶数 （文本） 
		CellStyle evenLastTextStyle =styleUtil.getExeclLastRowEvenContentCellStyle2(wb);//最后一行偶数 （文本）带下划线 
		CellStyle oddTextStyle =styleUtil.getExeclOddContentLastCellStyleChart1(wb);//奇数 （文本） 
		CellStyle oddLastTextStyle =styleUtil.getExeclOddContentLastCellStyleChart2(wb);//最后一行奇数 （文本） 带下划线 
		
		CellStyle oddThousandsStyle =styleUtil.getExeclOddContentLastCellStyleChartS1(wb);//普通-奇数千分位格式化 
		CellStyle oddLastThousandsStyle =styleUtil.getExeclOddContentLastCellStyleChartS2(wb);//最后一行奇数千分位格式化 带千分位 
		CellStyle evenThousandsStyle =styleUtil.getExeclEvenContentLastCellStyleS1(wb); //普通-偶数千分位格式化 
		CellStyle evenLastThousandsStyle =styleUtil.getExeclEvenContentLastCellStyleS2(wb); //最后一行偶数千分位格式化 带下划线 
		
		//CellStyle oddNumberStyle =overallStyle.getExeclOddNumberContentLastCellStyleChartS(wb);//普通-奇数 纯数字 -最后一行 
		//CellStyle evenNumberStyle =overallStyle.getExeclEvenNumberContentLastCellStyleS(wb);//普通-偶数 纯数字 -最后一行 
		CellStyle evenPctStyle = styleUtil.getExeclEvenContentChart_Percent1(wb);	//百分比 偶数 
		CellStyle evenLastPctStyle = styleUtil.getExeclEvenContentChart_Percent2(wb);	//最后一行百分比 偶数带下划线 
		CellStyle oddPctStyle = styleUtil.getExeclOddContentChart_Percent1(wb);	// 百分比 奇数 
		CellStyle oddLastPctStyle = styleUtil.getExeclOddContentChart_Percent2(wb);	// 最后一行百分比 奇数 带下划线 
		
		CellStyle oddRedPctStyle = styleUtil.getExeclOddContentRedChartLastCellStyle_Percent(wb);//百分比 奇数 红色 
		CellStyle oddLastRedPctStyle = styleUtil.getExeclOddLastContentRedChartLastCellStyle_Percent(wb);//最后一行百分比 奇数 红色 
		CellStyle evenRedPctStyle = styleUtil.getExeclEvenContentRedCellStyle_Percent(wb);//百分比 偶数 红色 
		CellStyle evenLastRedPctStyle = styleUtil.getExeclEvenLastContentRedCellStyle_Percent(wb);//最后一行百分比 偶数 红色 
		
		CellStyle tableHeadStyle = styleUtil.getExeclRowHeadContentCellStyle1(wb);//表头 
		CellStyle tableGrayHeadStyle = styleUtil.getExeclRowHeadContentCellStyle2(wb);//表头灰色 
		CellStyle tableWhiteHeadStyle = styleUtil.getExeclRowHeadContentCellStyle3(wb);//表头白色 
		//CellStyle cellStyle = overallStyle.getExeclEvenContentCellStyleChart(wb); 
		
		Sheet sheet = wb.createSheet("data"); 
		sheet.setColumnWidth(0, 2000); 
		sheet.setColumnWidth(1, 5000); 
		sheet.setColumnWidth(2, 6000); 
		sheet.setColumnWidth(3, 6000); 
		sheet.setColumnWidth(4, 3000); 
		sheet.setColumnWidth(5, 3000); 
		sheet.setColumnWidth(6, 3000); 
		sheet.setColumnWidth(7, 3000); 
		
		Row row = null; 
		Cell cell = null; 
		int rowIndex = 0; 
		int cellIndex = 1; 
		//第一行标题 
		row = sheet.createRow(rowIndex++); 
		row.setHeight((short) 500); 
		cell = row.createCell(cellIndex); 
		cell.setCellValue("销售比例-价格段销量分析"); 
		cell.setCellStyle(tableGrayHeadStyle); 
		//合并单元格,四个参数分别为，行，行，列，列 
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,7)); 
		//for(){} 
		
		//第二行标题 
		row = sheet.createRow(rowIndex++); 
		row.setHeight((short) 400); 
		cell = row.createCell(cellIndex); 
		cell.setCellValue("时间：" + paramsMap.get("startYm").toString() + "~" + paramsMap.get("endYm").toString()); 
		cell.setCellStyle(tableWhiteHeadStyle); 
		//合并单元格,四个参数分别为，行，行，列，列 
		 sheet.addMergedRegion(new CellRangeAddress(1,1,1,7)); 
		 
		//第三行标题 
		row = sheet.createRow(rowIndex++); 
		row.setHeight((short) 400); 
		for(int i = 0; i < columnsArray.size(); i++){ 
			cell = row.createCell(cellIndex++); 
			JSONObject columnsObject = columnsArray.getJSONObject(i); 
			if(i == 1){ 
				cell.setCellValue(paramsMap.get("startLastYm").toString() + "~" + paramsMap.get("endLastYm").toString() + " " + columnsObject.getString("title")); 
				cell.setCellStyle(tableHeadStyle); 
				 
				cell = row.createCell(cellIndex++); 
				cell.setCellValue(paramsMap.get("startYm").toString() + "~" + paramsMap.get("endYm").toString() + " " + columnsObject.getString("title")); 
				cell.setCellStyle(tableHeadStyle); 
			} else{ 
				cell.setCellValue(columnsObject.getString("title")); 
				cell.setCellStyle(tableHeadStyle); 
			} 
			 
		} 
		 
		//数据 
		for(int i = 0; i < dataArray.size(); i++){ 
			row = sheet.createRow(rowIndex++); 
			row.setHeight((short) 400); 
			JSONArray priceScaleArray = dataArray.getJSONArray(i); 
			cellIndex = 1; 
			for(int k = 0; k < priceScaleArray.size(); k++){ 
				//最后一条的total 
				if(i == dataArray.size() - 1){ 
					if(k != 1){ 
						cell = row.createCell(cellIndex++); 
						if(k == 0){ //第一条total子弹是文本样式 
							cell.setCellValue(priceScaleArray.getString(k)); 
							if(rowIndex % 2 == 0){ 
								cell.setCellStyle(evenLastTextStyle); 
							} else{ 
								cell.setCellStyle(oddLastTextStyle); 
							} 
						} else{//其他加千分位 
							if(!"-".equals(priceScaleArray.getString(k))){ 
								String value = priceScaleArray.getString(k).replace("%", "").replace("@percent", ""); 
								double number = Double.parseDouble(value)/100; 
								cell.setCellValue(number); 
								if(number < 0){ 
									if(rowIndex % 2 == 0){ 
										cell.setCellStyle(evenLastRedPctStyle); 
									} else{ 
										cell.setCellStyle(oddLastRedPctStyle); 
									} 
								} else{ 
									if(rowIndex % 2 == 0){ 
										cell.setCellStyle(evenLastPctStyle); 
									} else{ 
										cell.setCellStyle(oddLastPctStyle); 
									} 
								} 
							} else{ 
								cell.setCellValue(priceScaleArray.getString(k)); 
								if(rowIndex % 2 == 0){ 
									cell.setCellStyle(evenLastTextStyle); 
								} else{ 
									cell.setCellStyle(oddLastTextStyle); 
								} 
							} 
						} 
						 
							 
					} else{ 
						cell = row.createCell(cellIndex++); 
						if("-".equals(object.getString("exportNoTotal"))){ 
							cell.setCellValue(object.getString("exportNoTotal")); 
						} else{ 
							cell.setCellValue(Double.parseDouble(object.getString("exportNoTotal"))); 
						} 
						 
						 
						if(rowIndex % 2 == 0){ 
							if(!"-".equals(object.getString("exportNoTotal"))){ 
								cell.setCellStyle(evenLastThousandsStyle); 
							} else{ 
								cell.setCellStyle(evenLastTextStyle); 
							} 
						} else{ 
							if(!"-".equals(object.getString("exportNoTotal"))){ 
								cell.setCellStyle(oddLastThousandsStyle); 
							} else{ 
								cell.setCellStyle(oddLastTextStyle); 
							} 
						} 
						 
						cell = row.createCell(cellIndex++); 
						if("-".equals(priceScaleArray.getString(k))){ 
							cell.setCellValue(priceScaleArray.getString(k)); 
						} else{ 
							cell.setCellValue(Double.parseDouble(priceScaleArray.getString(k))); 
						} 
						 
						if(rowIndex % 2 == 0){ 
							if(!"-".equals(priceScaleArray.getString(k))){ 
								cell.setCellStyle(evenLastThousandsStyle); 
							} else{ 
								cell.setCellStyle(evenLastTextStyle); 
							} 
						} else{ 
							if(!"-".equals(priceScaleArray.getString(k))){ 
								cell.setCellStyle(oddLastThousandsStyle); 
							} else{ 
								cell.setCellStyle(oddLastTextStyle); 
							} 
						} 
						 
					} 
				} else{ 
					if(k != 1){ 
						cell = row.createCell(cellIndex++); 
						if(k == 0){//第一条字段是文本样式 
							cell.setCellValue(priceScaleArray.getString(k)); 
							if(rowIndex % 2 == 0){ 
								cell.setCellStyle(evenTextStyle); 
							} else{ 
								cell.setCellStyle(oddTextStyle); 
							} 
						} else{//其他加千分位 
							if(!"-".equals(priceScaleArray.getString(k))){ 
								String value = priceScaleArray.getString(k).replace("%", "").replace("@percent", ""); 
								double number = Double.parseDouble(value)/100; 
								cell.setCellValue(number); 
								if(number < 0){ 
									if(rowIndex % 2 == 0){ 
										cell.setCellStyle(evenRedPctStyle); 
									} else{ 
										cell.setCellStyle(oddRedPctStyle); 
									} 
								} else{ 
									if(rowIndex % 2 == 0){ 
										cell.setCellStyle(evenPctStyle); 
									} else{ 
										cell.setCellStyle(oddPctStyle); 
									} 
								} 
							} else{ 
								cell.setCellValue(priceScaleArray.getString(k)); 
								if(rowIndex % 2 == 0){ 
									cell.setCellStyle(evenTextStyle); 
								} else{ 
									cell.setCellStyle(oddTextStyle); 
								} 
							} 
						} 
					} else{ 
						JSONArray priceScaleArray1 = priceScaleArray.getJSONArray(k); 
						for(int l = priceScaleArray1.size() - 1; l >= 0; l--){ 
							JSONObject priceScaleObject = priceScaleArray1.getJSONObject(l); 
							cell = row.createCell(cellIndex++); 
							if("-".equals(priceScaleObject.getString("text"))){ 
								cell.setCellValue(priceScaleObject.getString("text")); 
							} else{ 
								cell.setCellValue(Double.parseDouble(priceScaleObject.getString("text"))); 
							} 
							 
							if(rowIndex % 2 == 0){ 
								if(!"-".equals(priceScaleArray.getString(k))){ 
									cell.setCellStyle(evenThousandsStyle); 
								} else{ 
									cell.setCellStyle(evenTextStyle); 
								} 
							} else{ 
								if(!"-".equals(priceScaleArray.getString(k))){ 
									cell.setCellStyle(oddThousandsStyle); 
								} else{ 
									cell.setCellStyle(oddTextStyle); 
								} 
							} 
						} 
					} 
				} 
			} 
		} 
		sheet.setDisplayGridlines(false); 
	}
	
	/** 
	 * 导出第二种格式的数据 
	 * @param wb 
	 * @param paramsMap 
	 * @param request 
	 * @param json 
	 */ 
	private void exportWholeMarketDataExcel2(Workbook wb,  Map<String, Object> paramsMap, HttpServletRequest request, 
		  String json) { 
	   //String tabDataType = paramsMap.get("tabDataType").toString();//1为销量，2为mix，3为份额 
	   JSONObject object = JSONObject.parseObject(json); 
	   JSONArray columnsArray = object.getJSONArray("columns"); 
	   JSONArray dataArray = object.getJSONArray("data"); 
		
	   CellStyle evenTextStyle =styleUtil.getExeclLastRowEvenContentCellStyle1(wb);//偶数 （文本） 
	   CellStyle evenLastTextStyle =styleUtil.getExeclLastRowEvenContentCellStyle2(wb);//最后一行偶数 （文本）带下划线 
	   CellStyle oddTextStyle =styleUtil.getExeclOddContentLastCellStyleChart1(wb);//奇数 （文本） 
	   CellStyle oddLastTextStyle =styleUtil.getExeclOddContentLastCellStyleChart2(wb);//最后一行奇数 （文本） 带下划线 
		
	   CellStyle oddThousandsStyle =styleUtil.getExeclOddContentLastCellStyleChartS1(wb);//普通-奇数千分位格式化 
	   CellStyle oddLastThousandsStyle =styleUtil.getExeclOddContentLastCellStyleChartS2(wb);//最后一行奇数千分位格式化 带千分位 
	   CellStyle evenThousandsStyle =styleUtil.getExeclEvenContentLastCellStyleS1(wb); //普通-偶数千分位格式化 
	   CellStyle evenLastThousandsStyle =styleUtil.getExeclEvenContentLastCellStyleS2(wb); //最后一行偶数千分位格式化 带下划线 
		
	   //CellStyle oddNumberStyle =overallStyle.getExeclOddNumberContentLastCellStyleChartS(wb);//普通-奇数 纯数字 -最后一行 
	   //CellStyle evenNumberStyle =overallStyle.getExeclEvenNumberContentLastCellStyleS(wb);//普通-偶数 纯数字 -最后一行 
	   CellStyle evenPctStyle = styleUtil.getExeclEvenContentChart_Percent1(wb);   //百分比 偶数 
	   CellStyle evenLastPctStyle = styleUtil.getExeclEvenContentChart_Percent2(wb);   //最后一行百分比 偶数带下划线 
	   CellStyle oddPctStyle = styleUtil.getExeclOddContentChart_Percent1(wb);   // 百分比 奇数 
	   CellStyle oddLastPctStyle = styleUtil.getExeclOddContentChart_Percent2(wb);   // 最后一行百分比 奇数 带下划线 
		
	   CellStyle oddRedPctStyle = styleUtil.getExeclOddContentRedChartLastCellStyle_Percent(wb);//百分比 奇数 红色 
	   CellStyle oddLastRedPctStyle = styleUtil.getExeclOddLastContentRedChartLastCellStyle_Percent(wb);//最后一行百分比 奇数 红色 
	   CellStyle evenRedPctStyle = styleUtil.getExeclEvenContentRedCellStyle_Percent(wb);//百分比 偶数 红色 
	   CellStyle evenLastRedPctStyle = styleUtil.getExeclEvenLastContentRedCellStyle_Percent(wb);//最后一行百分比 偶数 红色 
		
	   CellStyle tableHeadStyle = styleUtil.getExeclRowHeadContentCellStyle1(wb);//表头 
	   CellStyle tableGrayHeadStyle = styleUtil.getExeclRowHeadContentCellStyle2(wb);//表头灰色 
	   CellStyle tableWhiteHeadStyle = styleUtil.getExeclRowHeadContentCellStyle3(wb);//表头白色 
	   //CellStyle cellStyle = overallStyle.getExeclEvenContentCellStyleChart(wb); 
		
	   Sheet sheet = wb.createSheet("data"); 
	   sheet.setColumnWidth(0, 2000); 
	   sheet.setColumnWidth(1, 5000); 
	   sheet.setColumnWidth(2, 6000); 
	   sheet.setColumnWidth(3, 6000); 
	   sheet.setColumnWidth(4, 3000); 
	   sheet.setColumnWidth(5, 3000); 
	   sheet.setColumnWidth(6, 3000); 
	   sheet.setColumnWidth(7, 3000); 
		
	   Row row = null; 
	   Cell cell = null; 
	   int rowIndex = 0; 
	   int cellIndex = 1; 
	   //第一行标题 
	   row = sheet.createRow(rowIndex++); 
	   row.setHeight((short) 500); 
	   cell = row.createCell(cellIndex); 
	   cell.setCellValue("销售比例-价格段销量分析"); 
	   cell.setCellStyle(tableGrayHeadStyle); 
	   //合并单元格,四个参数分别为，行，行，列，列 
	   sheet.addMergedRegion(new CellRangeAddress(0,0,1, 6 + (columnsArray.size()-5)*2)); 
	   //for(){} 
		
	   //第二行标题 
	   row = sheet.createRow(rowIndex++); 
	   row.setHeight((short) 400); 
	   cell = row.createCell(cellIndex); 
		cell.setCellValue("时间：" + paramsMap.get("startYm").toString() + "~" + paramsMap.get("endYm").toString()); 
		cell.setCellStyle(tableWhiteHeadStyle); 
		//合并单元格,四个参数分别为，行，行，列，列 
		 sheet.addMergedRegion(new CellRangeAddress(1,1,1, 6 + (columnsArray.size()-5)*2)); 
		 
		//第三行标题 
		row = sheet.createRow(rowIndex++); 
		row.setHeight((short) 400); 
		for(int i = 0; i < columnsArray.size(); i++){ 
			JSONObject columnsObject = columnsArray.getJSONObject(i); 
			if(i == 1){ 
				cell = row.createCell(cellIndex++); 
				cell.setCellValue(paramsMap.get("startLastYm").toString() + "~" + paramsMap.get("endLastYm").toString() + " " + columnsObject.getString("title")); 
				cell.setCellStyle(tableHeadStyle); 
				 
				cell = row.createCell(cellIndex++); 
				cell.setCellValue(paramsMap.get("startYm").toString() + "~" + paramsMap.get("endYm").toString() + " " + columnsObject.getString("title")); 
				cell.setCellStyle(tableHeadStyle); 
			} else if(i >= 5){//出现合并的列 
				JSONArray columnsArray1 = columnsObject.getJSONArray("children"); 
				for(int k = 0; k < columnsArray1.size(); k++){ 
					cell = row.createCell(cellIndex++); 
					cell.setCellValue(columnsObject.getString("title")); 
					cell.setCellStyle(tableHeadStyle); 
				} 
				//合并单元格,四个参数分别为，行，行，列，列 
				  sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex-columnsArray1.size(),cellIndex-1)); 
			}else{ 
				cell = row.createCell(cellIndex++); 
				cell.setCellValue(columnsObject.getString("title")); 
				cell.setCellStyle(tableHeadStyle); 
			} 
			 
		} 
		 
		//第四行标题 (要合并的第二行) 
		cellIndex = 1; 
		row = sheet.createRow(rowIndex++); 
		row.setHeight((short) 400); 
		for(int i = 0; i < columnsArray.size(); i++){ 
			JSONObject columnsObject = columnsArray.getJSONObject(i); 
			if(i == 1){ 
				cell = row.createCell(cellIndex++); 
				cell.setCellValue(paramsMap.get("startLastYm").toString() + "~" + paramsMap.get("endLastYm").toString() + " " + columnsObject.getString("title")); 
				cell.setCellStyle(tableHeadStyle); 
				//合并单元格,四个参数分别为，行，行，列，列 
				  sheet.addMergedRegion(new CellRangeAddress(2,3,cellIndex-1,cellIndex-1)); 
				 
				cell = row.createCell(cellIndex++); 
				cell.setCellValue(paramsMap.get("startYm").toString() + "~" + paramsMap.get("endYm").toString() + " " + columnsObject.getString("title")); 
				cell.setCellStyle(tableHeadStyle); 
				//合并单元格,四个参数分别为，行，行，列，列 
				  sheet.addMergedRegion(new CellRangeAddress(2,3,cellIndex-1,cellIndex-1)); 
			} else if(i >= 5){//出现合并的列 
				JSONArray columnsArray1 = columnsObject.getJSONArray("children"); 
				for(int k = 0; k < columnsArray1.size(); k++){ 
					JSONObject columnsObject1 = columnsArray1.getJSONObject(k); 
					cell = row.createCell(cellIndex++); 
					cell.setCellValue(columnsObject1.getString("title")); 
					cell.setCellStyle(tableHeadStyle); 
				} 
			}else{ 
				cell = row.createCell(cellIndex++); 
				cell.setCellValue(columnsObject.getString("title")); 
				cell.setCellStyle(tableHeadStyle); 
				//合并单元格,四个参数分别为，行，行，列，列 
				  sheet.addMergedRegion(new CellRangeAddress(2,3,cellIndex-1,cellIndex-1)); 
			} 
			 
		} 
		 
	  //数据 
		for(int i = 0; i < dataArray.size(); i++){ 
			row = sheet.createRow(rowIndex++); 
			row.setHeight((short) 400); 
			JSONArray priceScaleArray = dataArray.getJSONArray(i); 
			cellIndex = 1; 
			for(int k = 0; k < priceScaleArray.size(); k++){ 
				//最后一条的total 
				if(i == dataArray.size() - 1){ 
					if(k != 1){ 
						cell = row.createCell(cellIndex++); 
						if(k == 0){ //第一条total子弹是文本样式 
						   cell.setCellValue(priceScaleArray.getString(k)); 
						   if(rowIndex % 2 == 0){ 
						      cell.setCellStyle(evenLastTextStyle); 
						   } else{ 
						      cell.setCellStyle(oddLastTextStyle); 
						   } 
						} else{//其他加千分位 
						   if(!"-".equals(priceScaleArray.getString(k))){ 
						      String value = priceScaleArray.getString(k).replace("@percent", ""); 
						      double number = 0; 
						      if(value.indexOf("%") > -1){ 
						         number = Double.parseDouble(value.replace("%", ""))/100; 
						      } else{ 
						         number = Double.parseDouble(value); 
						      } 
						      cell.setCellValue(number); 
						      if(number < 0){ 
						         if(rowIndex % 2 == 0){ 
						            if(value.indexOf("%") > -1){ 
						               cell.setCellStyle(evenLastRedPctStyle); 
						            } else{ 
						               cell.setCellStyle(evenLastThousandsStyle); 
						            } 
						         } else{ 
						            if(value.indexOf("%") > -1){ 
						               cell.setCellStyle(oddLastRedPctStyle); 
						            } else{ 
						               cell.setCellStyle(oddLastThousandsStyle); 
						            } 
						         } 
						      } else{ 
						         if(rowIndex % 2 == 0){ 
						            if(value.indexOf("%") > -1){ 
						               cell.setCellStyle(evenLastPctStyle); 
						            } else{ 
						               cell.setCellStyle(evenLastThousandsStyle); 
						            } 
						         } else{ 
						            if(value.indexOf("%") > -1){ 
						               cell.setCellStyle(oddLastPctStyle); 
						            } else{ 
						               cell.setCellStyle(oddLastThousandsStyle); 
						            } 
						         } 
						      } 
						   } else{ 
						      cell.setCellValue(priceScaleArray.getString(k)); 
						      if(rowIndex % 2 == 0){ 
						         cell.setCellStyle(evenLastTextStyle); 
						      } else{ 
						         cell.setCellStyle(oddLastTextStyle); 
						      } 
						   } 
						} 
						 
						    
					} else{ 
						cell = row.createCell(cellIndex++); 
						if("-".equals(object.getString("exportNoTotal"))){ 
						   cell.setCellValue(object.getString("exportNoTotal")); 
						} else{ 
						   cell.setCellValue(Double.parseDouble(object.getString("exportNoTotal"))); 
						} 
						 
						 
						if(rowIndex % 2 == 0){ 
						   if(!"-".equals(object.getString("exportNoTotal"))){ 
						      cell.setCellStyle(evenLastThousandsStyle); 
						   } else{ 
						      cell.setCellStyle(evenLastTextStyle); 
						   } 
						} else{ 
						   if(!"-".equals(object.getString("exportNoTotal"))){ 
						      cell.setCellStyle(oddLastThousandsStyle); 
						   } else{ 
						      cell.setCellStyle(oddLastTextStyle); 
						   } 
						} 
						 
						cell = row.createCell(cellIndex++); 
						if("-".equals(priceScaleArray.getString(k))){ 
						   cell.setCellValue(priceScaleArray.getString(k)); 
						} else{ 
						   cell.setCellValue(Double.parseDouble(priceScaleArray.getString(k))); 
						} 
						 
						if(rowIndex % 2 == 0){ 
						   if(!"-".equals(priceScaleArray.getString(k))){ 
						      cell.setCellStyle(evenLastThousandsStyle); 
						   } else{ 
						      cell.setCellStyle(evenLastTextStyle); 
						   } 
						} else{ 
						   if(!"-".equals(priceScaleArray.getString(k))){ 
						      cell.setCellStyle(oddLastThousandsStyle); 
						   } else{ 
						      cell.setCellStyle(oddLastTextStyle); 
						   } 
						} 
						 
					} 
				} else{ 
					if(k != 1){ 
						cell = row.createCell(cellIndex++); 
						if(k == 0){//第一条字段是文本样式 
						   cell.setCellValue(priceScaleArray.getString(k)); 
						   if(rowIndex % 2 == 0){ 
						      cell.setCellStyle(evenTextStyle); 
						   } else{ 
						      cell.setCellStyle(oddTextStyle); 
						   } 
						} else{//其他加千分位 
						   if(!"-".equals(priceScaleArray.getString(k))){ 
						      String value = priceScaleArray.getString(k).replace("@percent", ""); 
						      double number = 0; 
						      if(value.indexOf("%") > -1){ 
						         number = Double.parseDouble(value.replace("%", ""))/100; 
						      } else{ 
						         number = Double.parseDouble(value); 
						      } 
						             
						      cell.setCellValue(number); 
						      if(number < 0){ 
						         if(rowIndex % 2 == 0){ 
						            if(value.indexOf("%") > -1){ 
						               cell.setCellStyle(evenRedPctStyle); 
						            } else{ 
						               cell.setCellStyle(evenThousandsStyle); 
						            } 
						         } else{ 
						            if(value.indexOf("%") > -1){ 
						               cell.setCellStyle(oddRedPctStyle); 
						            } else{ 
						               cell.setCellStyle(oddThousandsStyle); 
						            } 
						         } 
						      } else{ 
						         if(rowIndex % 2 == 0){ 
						            if(value.indexOf("%") > -1){ 
						               cell.setCellStyle(evenPctStyle); 
						            } else{ 
						               cell.setCellStyle(evenThousandsStyle); 
						            } 
						         } else{ 
						            if(value.indexOf("%") > -1){ 
						               cell.setCellStyle(oddPctStyle); 
						            } else{ 
						               cell.setCellStyle(oddThousandsStyle); 
						            } 
						         } 
						      } 
						   } else{ 
						      cell.setCellValue(priceScaleArray.getString(k)); 
						      if(rowIndex % 2 == 0){ 
						         cell.setCellStyle(evenTextStyle); 
						      } else{ 
						         cell.setCellStyle(oddTextStyle); 
						      } 
						   } 
						} 
					} else{ 
						JSONArray priceScaleArray1 = priceScaleArray.getJSONArray(k); 
						for(int l = priceScaleArray1.size() - 1; l >= 0; l--){ 
						   JSONObject priceScaleObject = priceScaleArray1.getJSONObject(l); 
						   cell = row.createCell(cellIndex++); 
						   if("-".equals(priceScaleObject.getString("text"))){ 
						      cell.setCellValue(priceScaleObject.getString("text")); 
						   } else{ 
						      cell.setCellValue(Double.parseDouble(priceScaleObject.getString("text"))); 
						   } 
						    
						   if(rowIndex % 2 == 0){ 
						      if(!"-".equals(priceScaleArray.getString(k))){ 
						         cell.setCellStyle(evenThousandsStyle); 
						      } else{ 
						         cell.setCellStyle(evenTextStyle); 
						      } 
						   } else{ 
						      if(!"-".equals(priceScaleArray.getString(k))){ 
						         cell.setCellStyle(oddThousandsStyle); 
						      } else{ 
						         cell.setCellStyle(oddTextStyle); 
						      } 
						   } 
						} 
					} 
				} 
			} 
		} 
		sheet.setDisplayGridlines(false); 
	}
	
	/**
	 * 钻取的型号导出
	 * @param request
	 * @param params
	 * @return
	 */
	public Workbook exportVersionData(Workbook wb,HttpServletRequest request,Map<String, Object> params) {
		CellStyle evenTextStyle =styleUtil.getExeclLastRowEvenContentCellStyle1(wb);//偶数 （文本） 
		CellStyle evenLastTextStyle =styleUtil.getExeclLastRowEvenContentCellStyle2(wb);//最后一行偶数 （文本）带下划线 
		CellStyle oddTextStyle =styleUtil.getExeclOddContentLastCellStyleChart1(wb);//奇数 （文本） 
		CellStyle oddLastTextStyle =styleUtil.getExeclOddContentLastCellStyleChart2(wb);//最后一行奇数 （文本） 带下划线 
		
		CellStyle oddThousandsStyle =styleUtil.getExeclOddContentLastCellStyleChartS1(wb);//普通-奇数千分位格式化 
		CellStyle oddLastThousandsStyle =styleUtil.getExeclOddContentLastCellStyleChartS2(wb);//最后一行奇数千分位格式化 带千分位 
		CellStyle evenThousandsStyle =styleUtil.getExeclEvenContentLastCellStyleS1(wb); //普通-偶数千分位格式化 
		CellStyle evenLastThousandsStyle =styleUtil.getExeclEvenContentLastCellStyleS2(wb); //最后一行偶数千分位格式化 带下划线 
		
		//CellStyle oddNumberStyle =overallStyle.getExeclOddNumberContentLastCellStyleChartS(wb);//普通-奇数 纯数字 -最后一行 
		//CellStyle evenNumberStyle =overallStyle.getExeclEvenNumberContentLastCellStyleS(wb);//普通-偶数 纯数字 -最后一行 
		CellStyle evenPctStyle = styleUtil.getExeclEvenContentChart_Percent1(wb);   //百分比 偶数 
		CellStyle evenLastPctStyle = styleUtil.getExeclEvenContentChart_Percent2(wb);   //最后一行百分比 偶数带下划线 
		CellStyle oddPctStyle = styleUtil.getExeclOddContentChart_Percent1(wb);   // 百分比 奇数 
		CellStyle oddLastPctStyle = styleUtil.getExeclOddContentChart_Percent2(wb);   // 最后一行百分比 奇数 带下划线 
		
		CellStyle tableHeadStyle = styleUtil.getExeclRowHeadContentCellStyle1(wb);//表头 
		CellStyle tableGrayHeadStyle = styleUtil.getExeclRowHeadContentCellStyle2(wb);//表头灰色 
		CellStyle tableWhiteHeadStyle = styleUtil.getExeclRowHeadContentCellStyle3(wb);//表头白色
		
		Sheet sheet = wb.createSheet("价格段销量分析");
		sheet.setDefaultRowHeight((short)450);
		Row row = null;
		Cell cell = null;
		
		int rowNumber = 0;
		
		JSONObject obj = JSONObject.parseObject((String) params.get("exportDatas"));
		JSONArray columns = obj != null?obj.getJSONArray("columns"):null;
		JSONArray data = obj != null?obj.getJSONArray("data"):null;
		
		if(columns != null && data != null){
			//暂不知参数，先不写头部提醒信息
			
			
			//遍历表头
			row = sheet.createRow(rowNumber++);
			cell = row.createCell(0);
			this.setValueAndStyle(cell, "价格段", tableHeadStyle);
			sheet.addMergedRegion(new CellRangeAddress(rowNumber-1, rowNumber, 0, 0));
			int modelSize = 0;
			if(columns.size() > 1){
				modelSize = columns.size() - 1;
				int cellNumber = 1;
				Row row2 = sheet.createRow(rowNumber++);
				for(int i = 1; i < columns.size(); i++){
					//遍历车型名称
					cell = row.createCell(cellNumber);
					this.setValueAndStyle(cell, columns.getJSONObject(i).getString("title"), tableHeadStyle);
					sheet.addMergedRegion(new CellRangeAddress(rowNumber-2, rowNumber-2, cellNumber, cellNumber+2));
					
					//遍历数据名称  (销量,MIX,型号)
					cell = row2.createCell(cellNumber++);
					this.setValueAndStyle(cell, "销量", tableHeadStyle);
					cell = row2.createCell(cellNumber++);
					this.setValueAndStyle(cell, "MIX", tableHeadStyle);
					cell = row2.createCell(cellNumber++);
					this.setValueAndStyle(cell, "型号", tableHeadStyle);
				}
			}
			
			//遍历表格内容
			if(data.size() > 0){
				for(int i = 0; i < data.size(); i++){
					JSONArray datas = data.getJSONArray(i);
					int versionSize = 1;
					if(datas.size() > 1){
						for(int j = 1; j < datas.size(); j++){
							if(datas.getString(j).split("@").length > versionSize){
								versionSize = datas.getString(j).split("@").length;
							}
						}
					}
					CellStyle textStyle = null;
					CellStyle numberStyle = null;
					CellStyle mixStyle = null;
					//重新定义样式
					if(i%2 == 0 && i != data.size()-1){//奇数
						textStyle = oddTextStyle;
						numberStyle = oddThousandsStyle;
						mixStyle = oddPctStyle;
					}else if(i%2 != 0 && i != data.size()-1){//偶数
						textStyle = evenTextStyle;
						numberStyle = evenThousandsStyle;
						mixStyle = evenPctStyle;
					}else if(i%2 == 0 && i == data.size()-1){//奇数 最后一行
						textStyle = oddLastTextStyle;
						numberStyle = oddLastThousandsStyle;
						mixStyle = oddLastPctStyle;
					}else{//偶数 最后一行
						textStyle = evenLastTextStyle;
						numberStyle = evenLastThousandsStyle;
						mixStyle = evenLastPctStyle;
					}
					
					//写入价格段
					row = sheet.createRow(rowNumber++);
					cell = row.createCell(0);
					this.setValueAndStyle(cell, datas.getString(0), textStyle);
					sheet.addMergedRegion(new CellRangeAddress(rowNumber-1, rowNumber-2+versionSize, 0, 0));
					if(i == data.size()-1){//最后一行补样式
						row = sheet.createRow(rowNumber-2+versionSize);
						cell = row.createCell(0);
						cell.setCellStyle(textStyle);
					}
					
					//遍历方式，横向根据车型，再竖向根据改车型的型号
					//写入数据
					int cellNumber = 1;
					int index = 1;	//记录data里的每个数值的下表
					for(int j = 0; j < modelSize; j++){	//遍历车型
						int versionRowNumber = rowNumber - 1;
						int thisVersionSize = datas.getString(index).split("@").length;
						String[] salesDatas = datas.getString(index++).split("@");
						String[] mixDatas = datas.getString(index++).split("@");
						String[] versionDatas = datas.getString(index++).split("@");
						for(int k = 0; k < versionSize; k++){//型号
							//重新定义样式
							if(i%2 == 0){//奇数
								textStyle = oddTextStyle;
								numberStyle = oddThousandsStyle;
								mixStyle = oddPctStyle;
							}else if(i%2 != 0){//偶数
								textStyle = evenTextStyle;
								numberStyle = evenThousandsStyle;
								mixStyle = evenPctStyle;
							}
							if(i%2 == 0 && i == data.size()-1 && k == (versionSize - 1)){//奇数 最后一行
								textStyle = oddLastTextStyle;
								numberStyle = oddLastThousandsStyle;
								mixStyle = oddLastPctStyle;
							}else if(i%2 != 0 && i == data.size()-1 && k == (versionSize - 1)){//偶数 最后一行
								textStyle = evenLastTextStyle;
								numberStyle = evenLastThousandsStyle;
								mixStyle = evenLastPctStyle;
							}
							if(k < thisVersionSize){
								int versionCellNumber = cellNumber;
								row = this.getRow(sheet, versionRowNumber++);
								
								cell = row.createCell(versionCellNumber++);//销量
								if("-".equals(salesDatas[k])){
									this.setValueAndStyle(cell, "-", textStyle);
								}else{
									this.setValueAndStyle(cell, Double.parseDouble(salesDatas[k]), numberStyle);
								}
								
								cell = row.createCell(versionCellNumber++);//MIX
								if("-".equals(mixDatas[k])){
									this.setValueAndStyle(cell, "-", textStyle);
								}else{
									mixDatas[k] = mixDatas[k].replace("%", "");
									Double d = Double.parseDouble(mixDatas[k]) / 100.0;
									this.setValueAndStyle(cell, d, mixStyle);
								}
								
								cell = row.createCell(versionCellNumber++);//型号
								this.setValueAndStyle(cell, versionDatas[k], textStyle);
								sheet.setColumnWidth((versionCellNumber-1), (short)(13100)); //设置宽
							}else{
								int versionCellNumber = cellNumber;
								row = this.getRow(sheet, versionRowNumber++);
								cell = row.createCell(versionCellNumber++);//补样式
								this.setValueAndStyle(cell, "", textStyle);
								cell = row.createCell(versionCellNumber++);//补样式
								this.setValueAndStyle(cell, "", textStyle);
								cell = row.createCell(versionCellNumber++);//补样式
								this.setValueAndStyle(cell, "", textStyle);
								sheet.setColumnWidth((versionCellNumber-1), (short)(13100)); //设置宽
							}
						}
						cellNumber += 3;
					}
					rowNumber += (versionSize - 1);
				}
				
			}
		}
		sheet.setDisplayGridlines(false);
		return wb;
	}
	
	private Row getRow(Sheet sheet,int number){
		Row row = sheet.getRow(number);
		if(row == null){
			row = sheet.createRow(number);
		}
		return row;
	}
	
	/**
	 * 多个销量的情况下，添加千分位样式 (12345@9999 变 12,345\n9,999)
	 * @param str
	 * @return
	 */
	private String addThousandsStyle(String str){
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(true);//设置千分位
		
		String s = "";
		String[] datas = str.split("@");
		for(int i = 0; i < datas.length; i++){
			if(!"-".equals(datas[i])){
				datas[i] = nf.format(Integer.parseInt(datas[i]));
			}
			if(i != datas.length - 1){
				s = s + datas[i] + "\n";
			}else{
				s = s + datas[i];
			}
		}
		return s;
	}
	
	private void setValueAndStyle(Cell cell, Double value,CellStyle style){
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	private void setValueAndStyle(Cell cell, String value,CellStyle style){
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
}
