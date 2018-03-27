package com.ways.app.module.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import com.ways.app.common.utils.AppFrameworkUtil;
import com.ways.app.common.utils.EchartsUtil;
import com.ways.app.common.utils.OverallMarketExportUtil;
import com.ways.app.common.utils.OverallMarketNewExportUtil;
import com.ways.app.common.utils.SalesManfMarketExeportExeclUtil;
import com.ways.app.common.utils.SalesManfMarketNewExeportExeclUtil;
import com.ways.app.module.dao.IPriceSalesCommonDao;
import com.ways.app.module.dao.IPriceSalesOfTopDao;
import com.ways.app.module.entity.EchartOfOtherListEntity;
import com.ways.app.module.entity.EchartOfOtherMapEntity;
import com.ways.app.module.entity.ThoroughDataEntity;
import com.ways.app.module.service.IPriceSalesOfTopService;
@Service("priceSalesOfTopService")
public class PriceSalesOfTopServiceImpl implements IPriceSalesOfTopService {
	@Autowired
	private IPriceSalesOfTopDao priceSalesOfTopDao;
	
	OverallMarketExportUtil overallStyle = new OverallMarketExportUtil();
	SalesManfMarketExeportExeclUtil styleUtil = new SalesManfMarketExeportExeclUtil();
	
	OverallMarketNewExportUtil overallNewStyle = new OverallMarketNewExportUtil();
	SalesManfMarketNewExeportExeclUtil styleNewUtil = new SalesManfMarketNewExeportExeclUtil();
	
	@Override
	public String getTopEchartData(HttpServletRequest request,Map<String, Object> params) {
		String json = "";
		Map<String,Object> data = new HashMap<String, Object>();
		
		if(!"model".equals(params.get("objectType"))){//非车型
			List<EchartOfOtherListEntity> list = priceSalesOfTopDao.getTopEchartOfOtherData(params);
			request.getSession().setAttribute("priceSalesTopEchartDatas", list);		//保存数据，用于导出
			data.put("data", this.getBarChartOption(list, params));
		}else{//车型
			//柱状图，2个时间段
			//展示单个时间选（可以选择vs时间或者非vs时间展示），展示TP和MSRP
			if("bar".equals(params.get("chartType"))){
				List<EchartOfOtherListEntity> list = priceSalesOfTopDao.getTopBarEchartOfModelData(params);
				request.getSession().setAttribute("priceSalesTopEchartDatas", list);	//保存数据，用于导出
				data.put("data", this.getBarChartOption(list, params));
			}else{
				List<Map<String,String>> list = priceSalesOfTopDao.getTopPriceLevelEchartOfModelData(params);
				request.getSession().setAttribute("priceSalesTopEchartDatas", list);	//保存数据，用于导出
				Map<String,Object> option = new HashMap<String, Object>();
				if(list != null){
					List minPriceArr = new ArrayList();
					List msrpGapArr = new ArrayList();
					List names = new ArrayList();
					List tpGapArr = new ArrayList();
					List vmArr = new ArrayList();
					List yAxis = new ArrayList();
					
					int max = -100000;
					int min = 100000;
					for(int i = 0; i < list.size(); i++){
						Map<String,String> obj = list.get(i);
						
						minPriceArr.add(obj.get("MINMSRP").replace(",", ""));
						minPriceArr.add(obj.get("MINTP").replace(",", ""));
						
						msrpGapArr.add(obj.get("MSRPGAP").replace(",", ""));
						msrpGapArr.add("-");
						
						names.add("");
						names.add(obj.get("NAME"));
						
						tpGapArr.add("-");
						tpGapArr.add(obj.get("TPGAP").replace(",", ""));
						
						if("sum".equals(params.get("dataType"))){
							vmArr.add(obj.get("MAXSUMSALESMSRP").replace(",", ""));
							vmArr.add(obj.get("MAXSUMSALESTP").replace(",", ""));
						}else{
							vmArr.add(obj.get("MAXAVGSALESMSRP").replace(",", ""));
							vmArr.add(obj.get("MAXAVGSALESTP").replace(",", ""));
						}
						
						String minMsrp = "-".equals(obj.get("MINMSRP"))?"0":obj.get("MINMSRP").replace(",", "");
						String minTp = "-".equals(obj.get("MINTP"))?"0":obj.get("MINTP").replace(",", "");
						int value = Integer.parseInt(minMsrp);
						min = value < min && value != 0 ? value : min;
						value = Integer.parseInt(minTp);
						min = value < min && value != 0 ? value : min;
						
						String maxMsrp = "-".equals(obj.get("MAXMSRP"))?"0":obj.get("MAXMSRP").replace(",", "");
						String maxTp = "-".equals(obj.get("MAXTP"))?"0":obj.get("MAXTP").replace(",", "");
						value = Integer.parseInt(maxMsrp);
						max = value > max ? value : max;
						value = Integer.parseInt(maxTp);
						max = value > max ? value : max;
					}
					
					String[] maxAndMin = EchartsUtil.setLineYaxisMaxAndMin(max, min, true);
					
					option.put("minPriceArr", minPriceArr);
					option.put("msrpGapArr", msrpGapArr);
					option.put("names", names);
					option.put("tpGapArr", tpGapArr);
					option.put("vmArr", vmArr);
					option.put("yAxis", yAxis);
					option.put("yAxis", maxAndMin);
					option.put("chartType", "level");
				}
				data.put("data", option);
			}
		}
		json = AppFrameworkUtil.serializableJSONData(data);
		return json;
	}
	
	/**
	 * 设置柱状图一些数据
	 * @param list
	 * @param params
	 * @return
	 */
	private Map<String,Object> getBarChartOption(List<EchartOfOtherListEntity> list,Map<String, Object> params){
		Map<String,Object> option = new HashMap<String, Object>();
		
		if(list != null){
			List<Object> series = new ArrayList<Object>();
			List<Object> legendData = new ArrayList<Object>();
			List<Object> xAxisData = new ArrayList<Object>();
			
			int max = -1000000;
			int min = 1000000;
			double maxPct = -1000000;
			double minPct = 1000000;
			
			for(int i = 0; i < list.size(); i++){
				Map<String,Object> seriesMap = new HashMap<String, Object>();
				seriesMap.put("name", list.get(i).getDateKey());
				seriesMap.put("type", "bar");
				List<Object> seriesData = new ArrayList<Object>();
				
				legendData.add(list.get(i).getDateKey());
				for(int j = 0; j < list.get(i).getList().size(); j++){
					if(i == 0){
						xAxisData.add(list.get(i).getList().get(j).getLevelName());
					}
					
					if("sales".equals(params.get("chartDataType"))){
						String str = (list.get(i).getList().get(j).getValue()).replace(",", "");
						int value = Integer.parseInt(str);
						max = value > max? value : max;
						min = value < min? value : min;
					}else{
						String str = (list.get(i).getList().get(j).getValue()).replace(",", "");
						double value = Double.parseDouble(str);
						maxPct = value > maxPct? value : maxPct;
						minPct = value < minPct? value : minPct;
					}
					
					seriesData.add(list.get(i).getList().get(j).getValue().replace(",", ""));
				}
				series.add(seriesData);
			}
			
			String[] maxAndMin = null;
			if("sales".equals(params.get("chartDataType"))){
				maxAndMin = EchartsUtil.setLineYaxisMaxAndMin(new Integer(max).floatValue(),
						new Integer(0).floatValue(), (String) params.get("chartDataType"));
			}else{
//				int v = new Double(maxPct).intValue() + 10;
//				maxAndMin = new String[]{"0",v + "", (v / 10) + ""};
				maxAndMin = EchartsUtil.setLineYaxisMaxAndMin(new Double(maxPct).floatValue(),
						new Double(0).floatValue(), (String) params.get("chartDataType"));
			}
			
			params.put("maxAndMin", maxAndMin);
			
			option.put("chartDataType", params.get("chartDataType"));
			option.put("series", series);
			option.put("legendData", legendData);
			option.put("xAxisData", xAxisData);
			option.put("maxAndMin", maxAndMin);
			option.put("data", list);
			option.put("sales", this.getSaveSales(list,params));
		}
		return option;
	}

	@Override
	public String getEchartThoroughTableData(HttpServletRequest request,Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<ThoroughDataEntity> list = null;
		if(!"model".equals(params.get("objectType"))){//非车型
			if("city".equals(params.get("thoroughObjectType"))){
				list = priceSalesOfTopDao.getOtherThoroughCityData(params);
			}else if("model".equals(params.get("thoroughObjectType"))){//非车型钻取车型
				list = priceSalesOfTopDao.getOtherThoroughModelData(params);
			}else{
				list = priceSalesOfTopDao.getOtherThoroughOtherData(params);
			}
		}else{
			list = priceSalesOfTopDao.getModelThoroughData(params);
		}
		
		String width = "";
		if("model".equals(params.get("thoroughObjectType")) || "model".equals(params.get("objectType"))){
			width = "12.5%";
		}else{
			width = "16.6%";
		}
		
		request.getSession().setAttribute("echartThoroughTableData", list);
		
		Map<String,Object> titleStyle = new HashMap<String, Object>();
		titleStyle.put("width", width);
		
		List<Object> columns = new ArrayList<Object>();
		Map<String,Object> columnsData = new HashMap<String, Object>();
		columnsData.put("title", "排名");
		columnsData.put("key", "week1");
		columnsData.put("titleStyle", titleStyle);
		columns.add(columnsData);
		
		columnsData = new HashMap<String, Object>();
		columnsData.put("title", params.get("tableHeadName"));
		columnsData.put("key", "week2");
		columnsData.put("titleStyle", titleStyle);
		columns.add(columnsData);
		
		columnsData = new HashMap<String, Object>();
		columnsData.put("title", "累计销量");
		columnsData.put("key", "week3");
		columnsData.put("titleStyle", titleStyle);
		columns.add(columnsData);
		
		columnsData = new HashMap<String, Object>();
		columnsData.put("title", "增速");
		columnsData.put("key", "week4");
		columnsData.put("titleStyle", titleStyle);
		columns.add(columnsData);
		
		columnsData = new HashMap<String, Object>();
		columnsData.put("title", "MIX");
		columnsData.put("key", "week5");
		columnsData.put("titleStyle", titleStyle);
		columns.add(columnsData);
		
		columnsData = new HashMap<String, Object>();
		columnsData.put("title", "MIX变化");
		columnsData.put("key", "week6");
		columnsData.put("titleStyle", titleStyle);
		columns.add(columnsData);
		
		if("model".equals(params.get("thoroughObjectType")) || "model".equals(params.get("objectType"))){
			columnsData = new HashMap<String, Object>();
			columnsData.put("title", "TP");
			columnsData.put("key", "week7");
			columnsData.put("titleStyle", titleStyle);
			columns.add(columnsData);
			
			columnsData = new HashMap<String, Object>();
			columnsData.put("title", "MSRP");
			columnsData.put("key", "week8");
			columnsData.put("titleStyle", titleStyle);
			columns.add(columnsData);
		}
		
		resultMap.put("columns", columns);
		
		List<Object> data = new ArrayList<Object>();
		for(ThoroughDataEntity e:list){
			Map<String,Object> obj = new HashMap<String, Object>();
			obj.put("week1", e.getSumRanks());
			obj.put("week2", e.getName());
			obj.put("week3", e.getSales());
			obj.put("week4", "-".equals(e.getZs())?"-":e.getZs()+"%");
			obj.put("week5", "-".equals(e.getMix())?"-":e.getMix()+"%");
			obj.put("week6", "-".equals(e.getMixChanges())?"-":e.getMixChanges()+"%");
			if("model".equals(params.get("thoroughObjectType")) || "model".equals(params.get("objectType"))){
				obj.put("week7", e.getTp());
				obj.put("week8", e.getMsrp());
			}
			data.add(obj);
		}
		resultMap.put("data", data);
		
		String json = AppFrameworkUtil.serializableJSONData(resultMap);
		
		return json;
	}

	@Override
	public Workbook exportEchartData(HttpServletRequest request,Map<String, Object> params) {
		Workbook wb = null;
		if("model".equals(params.get("objectType")) && params.get("chartType") != null && "level".equals(params.get("chartType"))){
			wb = this.exportModelLevel(request, params);
		}else{
			wb = this.exportBarCharts(request, params);
		}
		
		return wb;
	}

	@Override
	public Workbook exportThoroughTableData(HttpServletRequest request,Map<String, Object> params) {
		Workbook wb = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/"); 
//			wb = new HSSFWorkbook(new FileInputStream(new File(path+"exceTemplate/priceSales/test.xls")));
			wb = new SXSSFWorkbook(new XSSFWorkbook(new FileInputStream(new File(path+"exceTemplate/priceSales/test.xlsx"))));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		CellStyle evenTextStyle =styleNewUtil.getExeclLastRowEvenContentCellStyle(wb);//最后一行-偶数 （文本）
		CellStyle oddTextStyle =overallNewStyle.getExeclOddContentLastCellStyleChart(wb);//最后一行-奇数 （文本）
		CellStyle oddThousandsStyle =overallNewStyle.getExeclOddContentLastCellStyleChartS(wb);//普通-奇数千分位格式化-最后一行
		CellStyle evenThousandsStyle =overallNewStyle.getExeclEvenContentLastCellStyleS(wb); //普通-偶数千分位格式化-最后一行
		CellStyle oddNumberStyle =overallNewStyle.getExeclOddNumberContentLastCellStyleChartS(wb);//普通-奇数 纯数字 -最后一行
		CellStyle evenNumberStyle =overallNewStyle.getExeclEvenNumberContentLastCellStyleS(wb);//普通-偶数 纯数字 -最后一行
		CellStyle oddPctStyle = overallNewStyle.getExeclOddContentChartLastCellStyle_Percent(wb);	//最后一行 百分比 奇数 无背色
		CellStyle evenPctStyle = overallNewStyle.getExeclLastRowEvenContentCellStyle_Percent(wb);	//最后一行 百分比 偶数 有背色
		CellStyle oddRedPctStyle = overallNewStyle.getExeclOddContentRedChartLastCellStyle_Percent(wb);	//最后一行 百分比 奇数 无背色
		CellStyle evenRedPctStyle = overallNewStyle.getExeclLastRowEvenContentRedCellStyle_Percent(wb);	//最后一行 百分比 偶数 有背色
		CellStyle tableHeadStyle = styleNewUtil.getExeclHeadContentCellStyle(wb);//表头
		CellStyle cellStyle = overallNewStyle.getExeclEvenContentCellStyleChart(wb);
		
		Sheet sheet = wb.getSheetAt(0);
		sheet = sheet == null?wb.createSheet("价格段销量分析"):sheet;
		Row row = null;
		Cell cell = null;
		
		List<ThoroughDataEntity> list = (List<ThoroughDataEntity>) request.getSession().getAttribute("echartThoroughTableData");
		
		if(list != null && list.size() > 0){
			row = this.getRow(sheet, 0);
			cell = this.getCell(row, 0);
			this.setValueAndStyle(cell, (String) params.get("seriesName"), cellStyle);
//			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, list.size()));
			
			row = this.getRow(sheet, 1);
			cell = this.getCell(row, 0);
			this.setValueAndStyle(cell, (String) params.get("priceName"), cellStyle);
//			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, list.size()));
			
			String[] head = null;
			if("model".equals(params.get("thoroughObjectType")) || "model".equals(params.get("objectType"))){
				head = new String[]{"排名",(String) params.get("tableHeadName"),"累计销量","增速","MIX","MIX变化","TP","MSRP"};
			}else{
				head = new String[]{"排名",(String) params.get("tableHeadName"),"累计销量","增速","MIX","MIX变化"};
			}
			
			row = this.getRow(sheet, 3);
			for(int i = 0; i < head.length; i++){
				cell = this.getCell(row, i);
				this.setValueAndStyle(cell, head[i], tableHeadStyle);
			}
			
			int rowNum = 4;
			for(int i = 0; i < list.size(); i++){
				int cellNum = 0;
				row = this.getRow(sheet, rowNum++);
				ThoroughDataEntity obj = list.get(i);
				
				cell = this.getCell(row, cellNum++);
				this.setValueAndStyle(cell, (double) Integer.parseInt(obj.getSumRanks()), i%2==0?oddNumberStyle:evenNumberStyle);
				
				cell = this.getCell(row, cellNum++);
				this.setValueAndStyle(cell, obj.getName(), i%2==0?oddTextStyle:evenTextStyle);
				
				cell = this.getCell(row, cellNum++);
				String value = obj.getSales().replace(",", "");
				if("-".equals(value)){
					this.setValueAndStyle(cell, "-", i%2==0?oddTextStyle:evenTextStyle);
				}else{
					this.setValueAndStyle(cell, (double) Integer.parseInt(value), i%2==0?oddThousandsStyle:evenThousandsStyle);
				}
				
				cell = this.getCell(row, cellNum++);
				value = obj.getZs().replace(",", "");
				if("-".equals(value)){
					this.setValueAndStyle(cell, "-", i%2==0?oddTextStyle:evenTextStyle);
				}else{
					double pct = Double.parseDouble(value) / 100;
					if(pct < 0){ //red
						this.setValueAndStyle(cell, (double) pct, i%2==0?oddRedPctStyle:evenRedPctStyle);
					}else{
						this.setValueAndStyle(cell, (double) pct, i%2==0?oddPctStyle:evenPctStyle);
					}
				}
				
				cell = this.getCell(row, cellNum++);
				value = obj.getMix().replace(",", "");
				if("-".equals(value)){
					this.setValueAndStyle(cell, "-", i%2==0?oddTextStyle:evenTextStyle);
				}else{
					double pct = Double.parseDouble(value) / 100;
					if(pct < 0){ //red
						this.setValueAndStyle(cell, (double) pct, i%2==0?oddRedPctStyle:evenRedPctStyle);
					}else{
						this.setValueAndStyle(cell, (double) pct, i%2==0?oddPctStyle:evenPctStyle);
					}
				}
				
				cell = this.getCell(row, cellNum++);
				value = obj.getMixChanges().replace(",", "");
				if("-".equals(value)){
					this.setValueAndStyle(cell, "-", i%2==0?oddTextStyle:evenTextStyle);
				}else{
					double pct = Double.parseDouble(value) / 100;
					if(pct < 0){ //red
						this.setValueAndStyle(cell, (double) pct, i%2==0?oddRedPctStyle:evenRedPctStyle);
					}else{
						this.setValueAndStyle(cell, (double) pct, i%2==0?oddPctStyle:evenPctStyle);
					}
				}
				
				if("model".equals(params.get("thoroughObjectType")) || "model".equals(params.get("objectType"))){
					cell = this.getCell(row, cellNum++);
					value = obj.getTp().replace(",", "");
					if("-".equals(value)){
						this.setValueAndStyle(cell, "-", i%2==0?oddTextStyle:evenTextStyle);
					}else{
						this.setValueAndStyle(cell, (double) Integer.parseInt(value), i%2==0?oddThousandsStyle:evenThousandsStyle);
					}
					
					cell = this.getCell(row, cellNum++);
					value = obj.getMsrp().replace(",", "");
					if("-".equals(value)){
						this.setValueAndStyle(cell, "-", i%2==0?oddTextStyle:evenTextStyle);
					}else{
						this.setValueAndStyle(cell, (double) Integer.parseInt(value), i%2==0?oddThousandsStyle:evenThousandsStyle);
					}
				}
				
			}
		}
		sheet.setDisplayGridlines(false);
		
		return wb;
	}
	
	/**
	 * 导出车型维度梯度图
	 * @param request
	 * @param params
	 * @return
	 */
	private Workbook exportModelLevel(HttpServletRequest request,Map<String, Object> params){
		Workbook wb = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/"); 
//			wb = new HSSFWorkbook(new FileInputStream(new File(path+"exceTemplate/priceSales/modelRatio_priceGradient.xls")));
//			wb = new HSSFWorkbook(new FileInputStream(new File(path+"exceTemplate/priceSales/test.xls")));
			wb = new SXSSFWorkbook(new XSSFWorkbook(new FileInputStream(new File(path+"exceTemplate/priceSales/test.xlsx"))));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		CellStyle cellStyleLastOdd_s =overallNewStyle.getExeclOddContentLastCellStyleChartS(wb);//普通-奇数千分位格式化-最后一行
		CellStyle cellStyleLastOdd =overallNewStyle.getExeclOddContentLastCellStyleChart(wb);//最后一行-奇数 （文本）
		CellStyle cellStyleLastEven_s =overallNewStyle.getExeclEvenContentLastCellStyleS(wb); //普通-偶数千分位格式化-最后一行
		CellStyle cellStyleLastEven =styleNewUtil.getExeclLastRowEvenContentCellStyle(wb);//最后一行-偶数 （文本）
		CellStyle cellStyleHead =styleNewUtil.getExeclHeadContentCellStyle(wb);//表头
		CellStyle cellStyleMainTitle =styleNewUtil.getExeclMainTitleContentCellStyle(wb);//主标题样式
		CellStyle cellStyleSubTitle =styleNewUtil.getExeclSubTitleContentCellStyle(wb);//副标题样式
		CellStyle cellStyle=overallNewStyle.getExeclEvenContentCellStyleChart(wb);
		CellStyle evenNumberStyle = overallNewStyle.getExeclEvenContentCellStyleS(wb);		//普通千分位 有色
		CellStyle oddNumberStyle = overallNewStyle.getExeclOddContentCellStyleChartS(wb);	//普通千分位 无色
		CellStyle oddTextStyle = overallNewStyle.getExeclOddContentCellStyleChart(wb);		//无色 文本
		CellStyle evenTextStyle = overallNewStyle.getExeclEvenContentCellStyle(wb);			//有色 文本
		
//		Sheet sheet = wb.getSheet("厂商价格段分析.");
//		Sheet sheet = wb.getSheet("价格段销量分析");
		Sheet sheet = wb.getSheet("价格段销量分析");
		Row row = this.getRow(sheet, 1);
		Cell cell = this.getCell(row, 1);
		
		String dateKey = "";
		if(((String) params.get("startDateKey")).equals(params.get("dateRange"))){
			dateKey = (String) params.get("startDateKey");
		}else{
			dateKey = (String) params.get("endDateKey");
		}
		
		this.setValueAndStyle(cell, dateKey, cellStyle);
		
		List<Map<String,String>> list = (List<Map<String, String>>) request.getSession().getAttribute("priceSalesTopEchartDatas");
		
		row = this.getRow(sheet, 2);
		row.setHeight((short) 360);
		cell = this.getCell(row, 1);
		this.setValueAndStyle(cell, "价格段分析", cellStyleSubTitle);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, (list.size() * 2)+1));
		
		//设置表头
		row = this.getRow(sheet, 3);
		cell = this.getCell(row, 1);
		this.setValueAndStyle(cell, "", cellStyleHead);
		int cellNum = 2;
		for(int i = 0; i < list.size(); i++){
			cell = this.getCell(row, cellNum++);
			this.setValueAndStyle(cell, "", cellStyleHead);
			cell = this.getCell(row, cellNum++);
			this.setValueAndStyle(cell, list.get(i).get("NAME"), cellStyleHead);
		}
		
		row = this.getRow(sheet, 4);
		cell = this.getCell(row, 1);
		this.setValueAndStyle(cell, "Max(MSRP/TP)", oddTextStyle);
		cellNum = 2;
		for(int i = 0; i < list.size(); i++){
			cell = this.getCell(row, cellNum++);
			String value = list.get(i).get("MAXMSRP");
			if(!"-".equals(value)){
				this.setValueAndStyle(cell, (double)Integer.parseInt(value.replace(",", "")), oddNumberStyle);
			}else{
				this.setValueAndStyle(cell, "-", oddTextStyle);
			}
			cell = this.getCell(row, cellNum++);
			value = list.get(i).get("MAXTP");
			if(!"-".equals(value)){
				this.setValueAndStyle(cell, (double)Integer.parseInt(value.replace(",", "")), oddNumberStyle);
			}else{
				this.setValueAndStyle(cell, "-", oddTextStyle);
			}
		}
		
		row = this.getRow(sheet, 5);
		cell = this.getCell(row, 1);
		this.setValueAndStyle(cell, "Min(MSRP/TP)", evenTextStyle);
		cellNum = 2;
		for(int i = 0; i < list.size(); i++){
			cell = this.getCell(row, cellNum++);
			String value = list.get(i).get("MINMSRP");
			if(!"-".equals(value)){
				this.setValueAndStyle(cell, (double)Integer.parseInt(value.replace(",", "")), evenNumberStyle);
			}else{
				this.setValueAndStyle(cell, "-", evenTextStyle);
			}
			cell = this.getCell(row, cellNum++);
			value = list.get(i).get("MINTP");
			if(!"-".equals(value)){
				this.setValueAndStyle(cell, (double)Integer.parseInt(value.replace(",", "")), evenNumberStyle);
			}else{
				this.setValueAndStyle(cell, "-", evenTextStyle);
			}
		}
		
		row = this.getRow(sheet, 6);
		cell = this.getCell(row, 1);
		this.setValueAndStyle(cell, "VM(MSRP/TP)", oddTextStyle);
		cellNum = 2;
		for(int i = 0; i < list.size(); i++){
			cell = this.getCell(row, cellNum++);
			String value = "";
			if("avg".equals(params.get("dataType"))){
				value = list.get(i).get("MAXAVGSALESMSRP");
			}else{
				value = list.get(i).get("MAXSUMSALESMSRP");
			}
			if(!"-".equals(value)){
				this.setValueAndStyle(cell, (double)Integer.parseInt(value.replace(",", "")), oddNumberStyle);
			}else{
				this.setValueAndStyle(cell, "-", oddTextStyle);
			}
			cell = this.getCell(row, cellNum++);
			if("avg".equals(params.get("dataType"))){
				value = list.get(i).get("MAXAVGSALESTP");
			}else{
				value = list.get(i).get("MAXSUMSALESTP");
			}
			if(!"-".equals(value)){
				this.setValueAndStyle(cell, (double)Integer.parseInt(value.replace(",", "")), oddNumberStyle);
			}else{
				this.setValueAndStyle(cell, "-", oddTextStyle);
			}
		}
		
		row = this.getRow(sheet, 7);
		cell = this.getCell(row, 1);
		this.setValueAndStyle(cell, "柱长(MSRP/TP)", cellStyleLastEven);
		cellNum = 2;
		for(int i = 0; i < list.size(); i++){
			cell = this.getCell(row, cellNum++);
			String value = list.get(i).get("MSRPGAP");
			if(!"-".equals(value)){
				this.setValueAndStyle(cell, (double)Integer.parseInt(value.replace(",", "")), cellStyleLastEven_s);
			}else{
				this.setValueAndStyle(cell, "-", cellStyleLastEven);
			}
			cell = this.getCell(row, cellNum++);
			value = list.get(i).get("TPGAP");
			if(!"-".equals(value)){
				this.setValueAndStyle(cell, (double)Integer.parseInt(value.replace(",", "")), cellStyleLastEven_s);
			}else{
				this.setValueAndStyle(cell, "-", cellStyleLastEven);
			}
		}
		sheet.setDisplayGridlines(false);
		return wb;
	}
	
	/**
	 * 导出柱状图
	 * @param request
	 * @param params
	 * @return
	 */
	private Workbook exportBarCharts(HttpServletRequest request,Map<String, Object> params){
		Workbook wb = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/"); 
//			wb = new HSSFWorkbook(new FileInputStream(new File(path+"exceTemplate/priceSales/sales_bar.xls")));//百分比
//			wb = new HSSFWorkbook(new FileInputStream(new File(path+"exceTemplate/priceSales/test.xls")));//百分比
			wb = new SXSSFWorkbook(new XSSFWorkbook(new FileInputStream(new File(path+"exceTemplate/priceSales/test.xlsx"))));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		CellStyle cellStyleLastOdd_s =overallNewStyle.getExeclOddContentLastCellStyleChartS(wb);//普通-奇数千分位格式化-最后一行
		CellStyle cellStyleLastOdd =overallNewStyle.getExeclOddContentLastCellStyleChart(wb);//最后一行-奇数 （文本）
		CellStyle cellStyleLastEven_s =overallNewStyle.getExeclEvenContentLastCellStyleS(wb); //普通-偶数千分位格式化-最后一行
		CellStyle cellStyleLastEven =styleNewUtil.getExeclLastRowEvenContentCellStyle(wb);//最后一行-偶数 （文本）
		CellStyle cellStyleHead =styleNewUtil.getExeclHeadContentCellStyle(wb);//表头
		CellStyle cellStyleMainTitle =styleNewUtil.getExeclMainTitleContentCellStyle(wb);//主标题样式
		CellStyle cellStyleSubTitle =styleNewUtil.getExeclSubTitleContentCellStyle(wb);//副标题样式
		CellStyle cellStyle=overallNewStyle.getExeclEvenContentCellStyleChart(wb);
		CellStyle evenNumberStyle = overallNewStyle.getExeclEvenContentCellStyleS(wb);		//普通千分位 有色
		CellStyle oddNumberStyle = overallNewStyle.getExeclOddContentCellStyleChartS(wb);	//普通千分位 无色
		CellStyle oddTextStyle = overallNewStyle.getExeclOddContentCellStyleChart(wb);		//无色 文本
		CellStyle evenTextStyle = overallNewStyle.getExeclEvenContentCellStyle(wb);			//有色 文本
		
		CellStyle oddLastPct = overallNewStyle.getExeclOddContentChartLastCellStyle_Percent(wb);	//最后一行 百分比 奇数 无色
		CellStyle evenLastPct = overallNewStyle.getExeclLastRowEvenContentCellStyle_Percent(wb);	//最后一行 百分比 偶数 有色
		
//		Sheet sheet = wb.getSheet("销量分析-上海大众.");
//		Sheet sheet = wb.getSheet("价格段销量分析");
		Sheet sheet = wb.getSheet("价格段销量分析");
		Row row = this.getRow(sheet, 2);
		Cell cell = this.getCell(row, 1);
		
		String dateKey = (String) params.get("startDateKey");
		this.setValueAndStyle(cell, dateKey, cellStyle);
		
		List<EchartOfOtherListEntity> list = (List<EchartOfOtherListEntity>) request.getSession().getAttribute("priceSalesTopEchartDatas");
		
		if(list != null && list.size() > 0 && list.get(0).getList() != null && list.get(0).getList().size() > 0){
			row = this.getRow(sheet, 3);
			row.setHeight((short) 360);
			cell = this.getCell(row, 1);
			this.setValueAndStyle(cell, "价格段分析", cellStyleSubTitle);
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, (list.get(0).getList().size())+1));
			
			//设置表头
			row = this.getRow(sheet, 4);
			cell = this.getCell(row, 1);
			this.setValueAndStyle(cell, "价格段", cellStyleHead);
			int cellNum = 2;
			for(int i = 0; i < list.get(0).getList().size(); i++){
				cell = this.getCell(row, cellNum++);
				this.setValueAndStyle(cell, list.get(0).getList().get(i).getLevelName(), cellStyleHead);
			}
			
			int rowNum = 5;
			for(int i = 0; i < list.size(); i++){
				EchartOfOtherListEntity entity = list.get(i);
				row = this.getRow(sheet, rowNum++);
				cellNum = 1;
				cell = this.getCell(row, cellNum++);
				this.setValueAndStyle(cell, entity.getDateKey(), i==0?cellStyleLastOdd:cellStyleLastEven);
				for(int j = 0; j < entity.getList().size(); j++){
					EchartOfOtherMapEntity e = entity.getList().get(j);
					CellStyle text = i==0?cellStyleLastOdd:cellStyleLastEven;
					cell = this.getCell(row, cellNum++);
					String value = e.getValue();
					if("sales".equals(params.get("chartDataType"))){
						CellStyle number = i==0?cellStyleLastOdd_s:cellStyleLastEven_s;
						if("-".equals(value)){
							this.setValueAndStyle(cell, "", text);
						}else{
							this.setValueAndStyle(cell, (double) Integer.parseInt(value.replace(",", "")), number);
						}
					}else{
						CellStyle number = i==0?oddLastPct:evenLastPct;
						if("-".equals(value)){
							this.setValueAndStyle(cell, "", text);
						}else{
							double d = (double) Double.parseDouble(value.replace(",", ""));
							this.setValueAndStyle(cell, (double)d/100.0, number);
						}
					}
				}
			}
			
			//设置Y轴 51
//			String[] maxAndMin = (String[]) params.get("maxAndMin");
//			if(maxAndMin != null && maxAndMin.length == 2){
//				if("mix".equals(params.get("chartDataType"))){
//					row = this.getRow(sheet, 0);//坐标最小值
//					cell = this.getCell(row, 51);
//					this.setValueAndStyle(cell, ((new Integer(maxAndMin[1])).doubleValue() / 100)+"", null);
//					
//					row = this.getRow(sheet, 1);//主要刻度
//					cell = this.getCell(row, 51);
//					this.setValueAndStyle(cell, 
//							((Integer.parseInt(maxAndMin[0]) - Integer.parseInt(maxAndMin[1])) / (double)5 / (double)100)+"",
//							null);
//					
//					row = this.getRow(sheet, 2);//坐标最大值
//					cell = this.getCell(row, 51);
//					this.setValueAndStyle(cell, ((new Integer(maxAndMin[0])).doubleValue() / 100)+"", null);
//				}else{
//					row = this.getRow(sheet, 0);//坐标最小值
//					cell = this.getCell(row, 51);
//					this.setValueAndStyle(cell, maxAndMin[1], null);
//					
//					row = this.getRow(sheet, 1);//主要刻度
//					cell = this.getCell(row, 51);
//					this.setValueAndStyle(cell, ((Integer.parseInt(maxAndMin[0]) - Integer.parseInt(maxAndMin[1])) / 5)+"", null);
//					
//					row = this.getRow(sheet, 2);//坐标最大值
//					cell = this.getCell(row, 51);
//					this.setValueAndStyle(cell, maxAndMin[0], null);
//				}
//				row = this.getRow(sheet, 3);//是否为百分比
//				cell = this.getCell(row, 51);
//				this.setValueAndStyle(cell, "mix".equals(params.get("chartDataType"))?"1":"0", null);
//			}
		
			sheet.setDisplayGridlines(false);
		}
		return wb;
	}
	
	private Row getRow(Sheet sheet, int rowNum){
		Row row = sheet.getRow(rowNum);
		if(row == null){
			row = sheet.createRow(rowNum);
		}
		return row;
	}
	
	private Cell getCell(Row row, int cellNum){
		Cell cell = row.getCell(cellNum);
		if(cell == null){
			cell = row.createCell(cellNum);
		}
		return cell;
	}
	
	private void setValueAndStyle(Cell cell, String value,CellStyle style){
		cell.setCellValue(value);
		if(style != null){
			cell.setCellStyle(style);
		}
	}
	
	private void setValueAndStyle(Cell cell, Double value,CellStyle style){
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	/**
	 * 保存销量，用于钻取
	 * @param list
	 * @return
	 */
	private List getSaveSales(List list,Map<String, Object> params){
		
//		//获取前一年销量（用于点击钻取第去年时，没有前年销量）
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.putAll(params);
		params2.put("isVsTime", "f");
		params2.put("startDate", params2.get("grandStartDate"));
		params2.put("endDate", params2.get("grandEndDate"));
		params2.put("startDateKey", params2.get("grandDateKey"));
		System.out.println("-------获取前年销量，用于钻取去年-------");
		List<EchartOfOtherListEntity> list2 = priceSalesOfTopDao.getTopEchartOfOtherData(params2);
		
		List datas = new ArrayList();
		if(list != null && list.size() > 0){
			int size = 0;
			if(((EchartOfOtherListEntity)list.get(0)).getList() != null
					&& ((EchartOfOtherListEntity)list.get(0)).getList().size() >0){
				size = ((EchartOfOtherListEntity)list.get(0)).getList().size();
			}
			for(int i = 0; i < size; i++){
				List data = new ArrayList();
				for(int j = 0; j < list.size(); j++){
					data.add(((EchartOfOtherListEntity)list.get(j)).getList().get(i).getSales().replace(",", ""));
				}
				if(list2 != null && list2.get(0) != null && list2.get(0).getList() != null
						&& i <= list2.get(0).getList().size()-1){
					data.add(((EchartOfOtherListEntity)list2.get(0)).getList().get(i).getSales().replace(",", ""));
				}
				datas.add(data);
			}
		}
		return datas;
	}

}

