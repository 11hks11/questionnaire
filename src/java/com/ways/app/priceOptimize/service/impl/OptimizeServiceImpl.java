package com.ways.app.priceOptimize.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.solr.common.util.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ways.app.priceOptimize.dao.OptimizeDao;
import com.ways.app.priceOptimize.entity.AttentionAndSatisfaction;
import com.ways.app.priceOptimize.entity.Configuration;
import com.ways.app.priceOptimize.entity.Lose;
import com.ways.app.priceOptimize.entity.Mix;
import com.ways.app.priceOptimize.entity.PriceRange;
import com.ways.app.priceOptimize.entity.SubModel;
import com.ways.app.priceOptimize.entity.Version;
import com.ways.app.priceOptimize.entity.VsVersion;
import com.ways.app.priceOptimize.service.OptimizeService;
import com.ways.app.priceOptimize.util.Constants;
import com.ways.app.priceOptimize.util.ExcelUtil;

@Service("OptimizeService")
public class OptimizeServiceImpl implements OptimizeService{

	@Autowired
	private OptimizeDao optimizeDao;

	@Override
	public Map<String, Object> getComparisonModel(Map<String, String> paramMap, HttpServletRequest request) {
		List<SubModel> list = optimizeDao.getComparisonModel(paramMap);
		String myModelIds = paramMap.get("myModelIds");
		Map<String, Object> map = new HashMap<String, Object>();
		if(list!=null && list.size()>0){
			request.getSession().setAttribute("comparisonModel", list);
			//添加表头数据
			List<Map<String,Object>> columns = new ArrayList<Map<String,Object>>();
			    String comparisonModelExcelTitle[] = Constants.comparisonModelExcelTitle;
			    String comparisonModelExcelTitle_key[] = Constants.comparisonModelExcelTitle_key;
			    for(int i=0; i<comparisonModelExcelTitle.length; i++){
			    	Map<String,Object> m = new HashMap<String, Object>();
			    	m.put("key", comparisonModelExcelTitle_key[i]);
			    	m.put("title", comparisonModelExcelTitle[i]);
			    	columns.add(m);
			    }
				
			map.put("columns", columns);
			List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
			int size = list.size();
			SubModel mySubModel = null;
			for(int i=0; i<size; i++){
				if(myModelIds.equals(list.get(i).getSubModelId())){
					mySubModel = list.get(i);
				}else{
					Map<String,Object> m = new HashMap<String, Object>();
					m.put("rank", list.get(i).getRank());
					m.put("subModelName", list.get(i).getSubModelName());
					m.put("subModelSales", list.get(i).getSubModelSales());
					List<Version> vList = list.get(i).getVersionList();
					List<Map<String,Object>> versionName_ = new ArrayList<Map<String,Object>>();
					List<String> versionSales_ = new ArrayList<String>();
					if(vList!=null && vList.size()>0){
						for(int j=0; j<vList.size(); j++){
							Map<String,Object> vm = new HashMap<String, Object>();
							vm.put("versionName", vList.get(j).getVersionName());
							vm.put("versionId", vList.get(j).getVersionId());
							vm.put("checked", false);
							versionName_.add(vm);
							versionSales_.add(vList.get(j).getVersionSales());
						}
					}else{
						versionName_.add(new HashMap<String, Object>());
						versionSales_.add("");
					}
					m.put("versionName", versionName_);
					m.put("versionSales", versionSales_);
					m.put("speedUp", list.get(i).getSpeedUp());
					m.put("proportion", list.get(i).getProportion());
					m.put("proportionChange", list.get(i).getProportionChange());
					m.put("share", list.get(i).getShare());
					m.put("shareChange", list.get(i).getShareChange());
					data.add(m);
				}
			}
			//本品放在最后一条的位置
			if(mySubModel!=null){
				Map<String,Object> m = new HashMap<String, Object>();
				m.put("rank", mySubModel.getRank());
				m.put("subModelName", mySubModel.getSubModelName());
				m.put("subModelSales", mySubModel.getSubModelSales());
				List<Version> vList = mySubModel.getVersionList();
				List<Map<String,Object>> versionName_ = new ArrayList<Map<String,Object>>();
				List<String> versionSales_ = new ArrayList<String>();
				if(vList!=null && vList.size()>0){
					for(int j=0; j<vList.size(); j++){
						Map<String,Object> vm = new HashMap<String, Object>();
						vm.put("versionName", vList.get(j).getVersionName());
						vm.put("versionId", vList.get(j).getVersionId());
						vm.put("checked", false);
						versionName_.add(vm);
						versionSales_.add(vList.get(j).getVersionSales());
					}
				}else{
					versionName_.add(new HashMap<String, Object>());
					versionSales_.add("");
				}
				m.put("versionName", versionName_);
				m.put("versionSales", versionSales_);
				m.put("speedUp", mySubModel.getSpeedUp());
				m.put("proportion", mySubModel.getProportion());
				m.put("proportionChange", mySubModel.getProportionChange());
				m.put("share", mySubModel.getShare());
				m.put("shareChange", mySubModel.getShareChange());
				data.add(m);
			}
			map.put("data", data);
			
		}
		return map;
	}

	@Override
	public void ExportComparisonModel(HttpServletResponse response, HttpServletRequest request) {
		ServletOutputStream out = null;
		try{
			out = response.getOutputStream();  
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("对标车型");
			//Sheet sheet = wb.createSheet("对标车型");
			//设置列的宽度
			sheet.setColumnWidth(0, 3840);
			sheet.setColumnWidth(1, 5376);
			sheet.setColumnWidth(2, 3840);
			sheet.setColumnWidth(3, 10752);
			sheet.setColumnWidth(4, 3840);
			sheet.setColumnWidth(5, 3840);
			sheet.setColumnWidth(6, 3840);
			sheet.setColumnWidth(7, 3840);
			sheet.setColumnWidth(8, 3840);
			sheet.setColumnWidth(9, 3840);
			Row row = sheet.createRow(0);
			//设置行高
			row.setHeightInPoints(30);
			//写标题
			String[] title = Constants.comparisonModelExcelTitle;
			CellStyle titleStyle = ExcelUtil.getTitleOne(wb,11,HSSFColor.BLACK.index,true);
			Cell cell;
			for(int i=0; i<title.length; i++){
				cell = row.createCell(i);
				cell.setCellValue(title[i]);
				cell.setCellStyle(titleStyle);
			}
			//数据
			List<SubModel> list = (List<SubModel>) request.getSession().getAttribute("comparisonModel");
			if(list!=null && list.size()>0){
				int rowNum = 1;
				int size = list.size();
				int firstRow = 1;
				int lastRow = 1;
				CellStyle cellStyle = ExcelUtil.getCellStyleOne(wb);
				for(int i=0; i<size; i++){
					row = sheet.createRow(rowNum);
					//设置行高
					row.setHeightInPoints(15);
					firstRow = rowNum;
					lastRow = rowNum;
					cell = row.createCell(0);
					cell.setCellValue(list.get(i).getRank());
					cell.setCellStyle(cellStyle);
					cell = row.createCell(1);
					cell.setCellValue(list.get(i).getSubModelName());
					cell.setCellStyle(cellStyle);
					cell = row.createCell(2);
					cell.setCellValue(list.get(i).getSubModelSales());
					cell.setCellStyle(cellStyle);
					cell = row.createCell(5);
					cell.setCellValue(list.get(i).getSpeedUp());
					cell.setCellStyle(cellStyle);
					cell = row.createCell(6);
					cell.setCellValue(list.get(i).getProportion());
					cell.setCellStyle(cellStyle);
					cell = row.createCell(7);
					cell.setCellValue(list.get(i).getProportionChange());
					cell.setCellStyle(cellStyle);
					cell = row.createCell(8);
					cell.setCellValue(list.get(i).getShare());
					cell.setCellStyle(cellStyle);
					cell = row.createCell(9);
					cell.setCellValue(list.get(i).getShareChange());
					cell.setCellStyle(cellStyle);
					List<Version> vl = list.get(i).getVersionList();
					if(vl!=null && vl.size()>0){
						int vlSize = vl.size();
						for(int k=0; k<vlSize; k++){
							cell = row.createCell(3);
							cell.setCellValue(vl.get(k).getVersionName());
							cell.setCellStyle(cellStyle);
							cell = row.createCell(4);
							cell.setCellValue(vl.get(k).getVersionSales());
							cell.setCellStyle(cellStyle);
							rowNum++;
							row = sheet.createRow(rowNum);
							//设置行高
							row.setHeightInPoints(15);
						}
						lastRow = rowNum-1;
					}
					CellRangeAddress range1 = new CellRangeAddress(firstRow, lastRow, 0, 0);
					sheet.addMergedRegion(range1);
					CellRangeAddress range2 = new CellRangeAddress(firstRow, lastRow, 1, 1);
					sheet.addMergedRegion(range2);
					CellRangeAddress range3 = new CellRangeAddress(firstRow, lastRow, 2, 2);
					sheet.addMergedRegion(range3);
					CellRangeAddress range4 = new CellRangeAddress(firstRow, lastRow, 5, 5);
					sheet.addMergedRegion(range4);
					CellRangeAddress range5 = new CellRangeAddress(firstRow, lastRow, 6, 6);
					sheet.addMergedRegion(range5);
					CellRangeAddress range6 = new CellRangeAddress(firstRow, lastRow, 7, 7);
					sheet.addMergedRegion(range6);
					CellRangeAddress range7 = new CellRangeAddress(firstRow, lastRow, 8, 8);
					sheet.addMergedRegion(range7);
					CellRangeAddress range8 = new CellRangeAddress(firstRow, lastRow, 9, 9);
					sheet.addMergedRegion(range8);
				}
			}
			
			String excelName = java.net.URLEncoder.encode("对标车型", "UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+excelName+".xls" );  
			wb.write(out);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Map<String, Object> getAnalyzeData(Map<String, Object> paramMap, HttpServletRequest request) {
		//获取MSRP+TP+配置缺失抱怨
		List<Mix> mixList = optimizeDao.getMixData(paramMap);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> mixAndLose = new ArrayList<Map<String,Object>>();
		if(mixList!=null && mixList.size()>0){
			for(int i=0; i<mixList.size(); i++){
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("versionName", mixList.get(i).getVersionName());
				m.put("msrp", mixList.get(i).getMsrp());
				m.put("tp", mixList.get(i).getTp());
				m.put("versionId", mixList.get(i).getVersionId());
				if(paramMap.get("myModelId").equals(mixList.get(i).getVersionId())){
					m.put("isMy", "y");
				}else{
					m.put("isMy", "n");
				}
				List<Lose> l = mixList.get(i).getLoseList();
				if(l!=null && l.size()>0){
					m.put("N", l.size());
					List<Map<String, Object>> lose = new ArrayList<Map<String,Object>>();
					for(int k=0; k<l.size(); k++){
						Map<String, Object> mLose = new HashMap<String, Object>();
						mLose.put("text", l.get(k).getConfigName());
						mLose.put("value", l.get(k).getTalkingVale());
						lose.add(mLose);
					}
					m.put("lose", lose);
					
				}
				mixAndLose.add(m);
			}
		}
		map.put("mixAndLose", mixAndLose);
		
		//获取优势配置
		Set<String> mySafety = new HashSet<String>();
		Set<String> myControl = new HashSet<String>();
		Set<String> myControllability = new HashSet<String>();
		Set<String> myPower = new HashSet<String>();
		Set<String> myPrice = new HashSet<String>();
		Set<String> mySpace = new HashSet<String>();
		Set<String> myDecorate = new HashSet<String>();
		Set<String> myComfort = new HashSet<String>();
		Set<String> myFacade = new HashSet<String>();
		Set<String> myRecreation = new HashSet<String>();
		Set<String> vsSafety = new HashSet<String>();
		Set<String> vsControl = new HashSet<String>();
		Set<String> vsControllability = new HashSet<String>();
		Set<String> vsPower = new HashSet<String>();
		Set<String> vsPrice = new HashSet<String>();
		Set<String> vsSpace = new HashSet<String>();
		Set<String> vsDecorate = new HashSet<String>();
		Set<String> vsComfort = new HashSet<String>();
		Set<String> vsFacade = new HashSet<String>();
		Set<String> vsRecreation = new HashSet<String>();
		List<Configuration> configList = optimizeDao.getConfigList(paramMap);
		if(configList!=null && configList.size()>0){
			int size = configList.size();
			String myModelId = (String) paramMap.get("myModelId");
			a:for(int i=0; i<size; i++){
				Configuration c1 = configList.get(i);
				for(int k=0; k<size; k++){
					if(configList.get(k).getConfigId().equals(c1.getConfigId()) && configList.get(k).getVersionId()!=c1.getVersionId()){
						Configuration c2 = configList.get(k);
						//安全性
						if("1".equals(c1.getConfigTypeId())){
							String re = isAdvantageous(c1.getDataType(), c1.getSortNum(), c2.getSortNum(), c1.getConfigId(),c1.getVersionId(),c2.getVersionId(),myModelId);
							if("c1".equals(re)){
								mySafety.add(c1.getConfigName());
							}else if("c2".equals(re)){
								vsSafety.add(c2.getConfigName());
							}
						}
						//操控
						else if("2".equals(c1.getConfigTypeId())){
							String re = isAdvantageous(c1.getDataType(), c1.getSortNum(), c2.getSortNum(), c1.getConfigId(),c1.getVersionId(),c2.getVersionId(),myModelId);
							if("c1".equals(re)){
								myControl.add(c1.getConfigName());
							}else if("c2".equals(re)){
								vsControl.add(c2.getConfigName());
							}
						}
						//操控性
						else if("3".equals(c1.getConfigTypeId())){
							String re = isAdvantageous(c1.getDataType(), c1.getSortNum(), c2.getSortNum(), c1.getConfigId(),c1.getVersionId(),c2.getVersionId(),myModelId);
							if("c1".equals(re)){
								myControllability.add(c1.getConfigName());
							}else if("c2".equals(re)){
								vsControllability.add(c2.getConfigName());
							}
						}
						//动力
						else if("4".equals(c1.getConfigTypeId())){
							String re = isAdvantageous(c1.getDataType(), c1.getSortNum(), c2.getSortNum(), c1.getConfigId(),c1.getVersionId(),c2.getVersionId(),myModelId);
							if("c1".equals(re)){
								myPower.add(c1.getConfigName());
							}else if("c2".equals(re)){
								vsPower.add(c2.getConfigName());
							}
						}
						//价格因素
						else if("5".equals(c1.getConfigTypeId())){
							String re = isAdvantageous(c1.getDataType(), c1.getSortNum(), c2.getSortNum(), c1.getConfigId(),c1.getVersionId(),c2.getVersionId(),myModelId);
							if("c1".equals(re)){
								myPrice.add(c1.getConfigName());
							}else if("c2".equals(re)){
								vsPrice.add(c2.getConfigName());
							}
						}
						//空间
						else if("6".equals(c1.getConfigTypeId())){
							String re = isAdvantageous(c1.getDataType(), c1.getSortNum(), c2.getSortNum(), c1.getConfigId(),c1.getVersionId(),c2.getVersionId(),myModelId);
							if("c1".equals(re)){
								mySpace.add(c1.getConfigName());
							}else if("c2".equals(re)){
								vsSpace.add(c2.getConfigName());
							}
						}
						//内饰
						else if("7".equals(c1.getConfigTypeId())){
							String re = isAdvantageous(c1.getDataType(), c1.getSortNum(), c2.getSortNum(), c1.getConfigId(),c1.getVersionId(),c2.getVersionId(),myModelId);
							if("c1".equals(re)){
								myDecorate.add(c1.getConfigName());
							}else if("c2".equals(re)){
								vsDecorate.add(c2.getConfigName());
							}
						}
						//舒适型
						else if("8".equals(c1.getConfigTypeId())){
							String re = isAdvantageous(c1.getDataType(), c1.getSortNum(), c2.getSortNum(), c1.getConfigId(),c1.getVersionId(),c2.getVersionId(),myModelId);
							if("c1".equals(re)){
								myComfort.add(c1.getConfigName());
							}else if("c2".equals(re)){
								vsComfort.add(c2.getConfigName());
							}
						}
						//外观
						else if("9".equals(c1.getConfigTypeId())){
							String re = isAdvantageous(c1.getDataType(), c1.getSortNum(), c2.getSortNum(), c1.getConfigId(),c1.getVersionId(),c2.getVersionId(),myModelId);
							if("c1".equals(re)){
								myFacade.add(c1.getConfigName());
							}else if("c2".equals(re)){
								vsFacade.add(c2.getConfigName());
							}
						}
						//信息娱乐性
						else if("10".equals(c1.getConfigTypeId())){
							String re = isAdvantageous(c1.getDataType(), c1.getSortNum(), c2.getSortNum(), c1.getConfigId(),c1.getVersionId(),c2.getVersionId(),myModelId);
							if("c1".equals(re)){
								myRecreation.add(c1.getConfigName());
							}else if("c2".equals(re)){
								vsRecreation.add(c2.getConfigName());
							}
						}
						continue a;
					}
					
				}
				//补全，该配置只有一辆车存在
				//安全性
				if("1".equals(c1.getConfigTypeId())){
					if("B".equals(c1.getDataType()) || "T".equals(c1.getDataType()) || "X".equals(c1.getDataType())){
						if(c1.getVersionId().equals(myModelId)){
							mySafety.add(c1.getConfigName());
						}else{
							vsSafety.add(c1.getConfigName());
						}
					}
				}
				//操控
				else if("2".equals(c1.getConfigTypeId())){
					if("B".equals(c1.getDataType()) || "T".equals(c1.getDataType()) || "X".equals(c1.getDataType())){
						if(c1.getVersionId().equals(myModelId)){
							myControl.add(c1.getConfigName());
						}else{
							vsControl.add(c1.getConfigName());
						}
					}
				}
				//操控性
				else if("3".equals(c1.getConfigTypeId())){
					if("B".equals(c1.getDataType()) || "T".equals(c1.getDataType()) || "X".equals(c1.getDataType())){
						if(c1.getVersionId().equals(myModelId)){
							myControllability.add(c1.getConfigName());
						}else{
							vsControllability.add(c1.getConfigName());
						}
					}
				}
				//动力
				else if("4".equals(c1.getConfigTypeId())){
					if("B".equals(c1.getDataType()) || "T".equals(c1.getDataType()) || "X".equals(c1.getDataType())){
						if(c1.getVersionId().equals(myModelId)){
							myPower.add(c1.getConfigName());
						}else{
							vsPower.add(c1.getConfigName());
						}
					}
				}
				//价格因素
				else if("5".equals(c1.getConfigTypeId())){
					if("B".equals(c1.getDataType()) || "T".equals(c1.getDataType()) || "X".equals(c1.getDataType())){
						if(c1.getVersionId().equals(myModelId)){
							myPrice.add(c1.getConfigName());
						}else{
							vsPrice.add(c1.getConfigName());
						}
					}
				}
				//空间
				else if("6".equals(c1.getConfigTypeId())){
					if("B".equals(c1.getDataType()) || "T".equals(c1.getDataType()) || "X".equals(c1.getDataType())){
						if(c1.getVersionId().equals(myModelId)){
							mySpace.add(c1.getConfigName());
						}else{
							vsSpace.add(c1.getConfigName());
						}
					}
				}
				//内饰
				else if("7".equals(c1.getConfigTypeId())){
					if("B".equals(c1.getDataType()) || "T".equals(c1.getDataType()) || "X".equals(c1.getDataType())){
						if(c1.getVersionId().equals(myModelId)){
							myDecorate.add(c1.getConfigName());
						}else{
							vsDecorate.add(c1.getConfigName());
						}
					}
				}
				//舒适型
				else if("8".equals(c1.getConfigTypeId())){
					if("B".equals(c1.getDataType()) || "T".equals(c1.getDataType()) || "X".equals(c1.getDataType())){
						if(c1.getVersionId().equals(myModelId)){
							myComfort.add(c1.getConfigName());
						}else{
							vsComfort.add(c1.getConfigName());
						}
					}
				}
				//外观
				else if("9".equals(c1.getConfigTypeId())){
					if("B".equals(c1.getDataType()) || "T".equals(c1.getDataType()) || "X".equals(c1.getDataType())){
						if(c1.getVersionId().equals(myModelId)){
							myFacade.add(c1.getConfigName());
						}else{
							vsFacade.add(c1.getConfigName());
						}
					}
				}
				//信息娱乐性
				else if("10".equals(c1.getConfigTypeId())){
					if("B".equals(c1.getDataType()) || "T".equals(c1.getDataType()) || "X".equals(c1.getDataType())){
						if(c1.getVersionId().equals(myModelId)){
							myRecreation.add(c1.getConfigName());
						}else{
							vsRecreation.add(c1.getConfigName());
						}
					}
				}
			}
		}
		//获取关注度，满意度
		String time = (String) paramMap.get("time");
		if(StringUtils.isNotBlank(time)){
			String quarter = "";
			Integer i = Integer.parseInt(time.substring(4,6));
			if(0<i && i<4){
				quarter = "01";
			}else if(3<i && i<7){
				quarter = "02";
			}else if(6<i && i<10){
				quarter = "03";
			}else if(9<i && i<13){
				quarter = "04";
			}
			paramMap.put("yq_id", time.substring(0, 4)+quarter);
		}
		List<AttentionAndSatisfaction> asList = optimizeDao.getAttentionAndSatisfaction(paramMap);
		
		//拼接返回的json数据
		List<Map<String,Object>> columns = new ArrayList<Map<String,Object>>();
		Map<String,Object> c1 = new HashMap<String, Object>();
		c1.put("title", "优势配置");
		c1.put("key", "myAdvantage");
		columns.add(c1);
		Map<String,Object> c2 = new HashMap<String, Object>();
		c2.put("title", "满意度");
		c2.put("key", "mySatisfaction");
		columns.add(c2);
		Map<String,Object> c3 = new HashMap<String, Object>();
		c3.put("title", "关注度");
		c3.put("key", "myAttention");
		columns.add(c3);
		Map<String,Object> c4 = new HashMap<String, Object>();
		c4.put("title", "满意度");
		c4.put("key", "vsSatisfaction");
		columns.add(c4);
		Map<String,Object> c5 = new HashMap<String, Object>();
		c5.put("title", "优势配置");
		c5.put("key", "vsAdvantage");
		columns.add(c5);
		map.put("columns", columns);
		
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		if(asList!=null && asList.size()>0){
			for(int i=0; i<asList.size(); i++){
				String configTypeId = asList.get(i).getConfigTypeId();
				Map<String,Object> d = new HashMap<String, Object>();
				if("1".equals(configTypeId)){
					addStr(mySafety);
					addStr(vsSafety);
					d.put("myAdvantage", mySafety);
					d.put("vsAdvantage", vsSafety);
				}else if("2".equals(configTypeId)){
					addStr(myControl);
					addStr(vsControl);
					d.put("myAdvantage", myControl);
					d.put("vsAdvantage", vsControl);
				}else if("3".equals(configTypeId)){
					addStr(myControllability);
					addStr(vsControllability);
					d.put("myAdvantage", myControllability);
					d.put("vsAdvantage", vsControllability);
				}else if("4".equals(configTypeId)){
					addStr(myPower);
					addStr(vsPower);
					d.put("myAdvantage", myPower);
					d.put("vsAdvantage", vsPower);
				}else if("5".equals(configTypeId)){
					addStr(myPrice);
					addStr(vsPrice);
					d.put("myAdvantage", myPrice);
					d.put("vsAdvantage", vsPrice);
				}else if("6".equals(configTypeId)){
					addStr(mySpace);
					addStr(vsSpace);
					d.put("myAdvantage", mySpace);
					d.put("vsAdvantage", vsSpace);
				}else if("7".equals(configTypeId)){
					addStr(myDecorate);
					addStr(vsDecorate);
					d.put("myAdvantage", myDecorate);
					d.put("vsAdvantage", vsDecorate);
				}else if("8".equals(configTypeId)){
					addStr(myComfort);
					addStr(vsComfort);
					d.put("myAdvantage", myComfort);
					d.put("vsAdvantage", vsComfort);
				}else if("9".equals(configTypeId)){
					addStr(myFacade);
					addStr(vsFacade);
					d.put("myAdvantage", myFacade);
					d.put("vsAdvantage", vsFacade);
				}else if("10".equals(configTypeId)){
					addStr(myRecreation);
					addStr(vsRecreation);
					d.put("myAdvantage", myRecreation);
					d.put("vsAdvantage", vsRecreation);
				}
				d.put("mySatisfaction", asList.get(i).getMySatisfactionVale());
				d.put("vsSatisfaction", asList.get(i).getVsSatisfactionVale());
				List<String> l = new ArrayList<String>();
				l.add(asList.get(i).getConfigTypeName());
				l.add(asList.get(i).getMyAttentionVale());
				d.put("myAttention", l);
				data.add(d);
			}
			
		}
	    
		map.put("data", data);
		map.put("market", paramMap.get("market"));
		map.put("priceTier", paramMap.get("priceTier"));
		int myListSize = mySafety.size()+myControl.size()+myControllability.size()+myPower.size()+myPrice.size()+mySpace.size()
				+myDecorate.size()+myComfort.size()+myFacade.size()+myRecreation.size();
		int vsListSize = vsSafety.size()+vsControl.size()+vsControllability.size()+vsPower.size()+vsPrice.size()+vsSpace.size()
				+vsDecorate.size()+vsComfort.size()+vsFacade.size()+vsRecreation.size();
		map.put("myListSize", myListSize);
		map.put("vsListSize", vsListSize);
		request.getSession().setAttribute("analyzeData", map);
		return map;
	}
	
	public void addStr(Set<String> set){
		if(set.size()==0){
			set.add("");
		}
	}
	
	/**
	 * describe: 判断优劣势配置
	 * @authod liuyuhuan
	 * @date 2018-3-2 上午11:38:52
	 */
	public String isAdvantageous(String dataType,Integer sortNum1,Integer sortNum2,
			String configId,String versionId1,String versionId2,String myModelId){
		if("B".equals(dataType)){
			if(sortNum1 == null && sortNum2 != null){
				if(versionId1.equals(myModelId)){
					return "c2";
				}else{
					return "c1";
				}
			}else if(sortNum1!= null && sortNum2 == null){
				if(versionId1.equals(myModelId)){
					return "c1";
				}else{
					return "c2";
				}
			}
		}else if("T".equals(dataType) || "X".equals(dataType)){
			if(sortNum1 == null && sortNum2 != null){
				if(versionId1.equals(myModelId)){
					return "c2";
				}else{
					return "c1";
				}
			}else if(sortNum1!= null && sortNum2 == null){
				if(versionId1.equals(myModelId)){
					return "c1";
				}else{
					return "c2";
				}
			}else if(sortNum1 != null && sortNum2 != null){
				if(sortNum1 > sortNum2){
					if(versionId1.equals(myModelId)){
						return "c1";
					}else{
						return "c2";
					}
				}else if(sortNum1 < sortNum2){
					if(versionId1.equals(myModelId)){
						return "c2";
					}else{
						return "c1";
					}
				}
			}
		}else if("N".equals(dataType) || "I".equals(dataType)){
			if("11".equals(configId) || "17".equals(configId) 
					|| "20".equals(configId) || "21".equals(configId) 
					|| "22".equals(configId) || "23".equals(configId)
					|| "24".equals(configId)){
				if(sortNum1 != null && sortNum2 != null){
					if(sortNum1 > sortNum2){
						if(versionId1.equals(myModelId)){
							return "c2";
						}else{
							return "c1";
						}
					}
				}
			}else{
				if(sortNum1 != null && sortNum2 != null){
					if(sortNum1 > sortNum2){
						if(versionId1.equals(myModelId)){
							return "c1";
						}else{
							return "c2";
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public Map<String,Object> getReportData(List<PriceRange> priceList, String[] params, Set<String> allVersionId, 
			String yqId, String time, HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		Iterator<String> iterator = allVersionId.iterator();
		while(iterator.hasNext()){
			sb.append(iterator.next()+",");
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("versionIds", sb.substring(0, sb.length()-1));
		paramsMap.put("yqId", yqId);
		paramsMap.put("time", time);
		//获取型号信息，msrp，tp，配置满意度
		List<Version> infoList = optimizeDao.getDataByVersionIds(paramsMap);
		//获取缺失抱怨
		List<Version> loseList = optimizeDao.getLoseByVersionIds(paramsMap);
		SubModel sub = new SubModel();
		//通过本品型号获取车型图片地址，名称
		Set<String> myVersion = priceList.get(0).getMyVersion();
		Iterator<String> iterator2 = myVersion.iterator();
		String myVersionId = "";
		while(iterator2.hasNext()){
			myVersionId = iterator2.next();
			break;
		}
		Version v1 = getVersionById(infoList,myVersionId);
		if(v1 != null){
			sub.setImagePath(v1.getSubModelImage());
			sub.setSubModelEname(v1.getSubModelEname());
		}else{
			List<Version> vList = optimizeDao.getSubModelByVersionId(myVersionId);
			if(vList!=null && vList.size()>0){
				sub.setImagePath(vList.get(0).getSubModelImage());
				sub.setSubModelEname(vList.get(0).getSubModelEname());
			}
		}
		//根据价格区间补全信息
		for(int i=0; i<priceList.size(); i++){
			List<Version> vList = new ArrayList<Version>();
			Iterator<String> iterator3 = priceList.get(i).getMyVersion().iterator();
			while(iterator3.hasNext()){
				Version v = getVersionById(infoList,iterator3.next());
				if(v!=null){
					//拼接本竞品缺失抱怨
					v.setLoseStr(getLoseStr(loseList,v.getVersionId()));
					//拼接竞品数据
					List<String> vsIds = getVsByMy(params,v.getVersionId());
					List<VsVersion> vsList = new ArrayList<VsVersion>();
					for(int t=0; t<vsIds.size(); t++){
						Version ver = getVersionById(infoList,vsIds.get(t));
						VsVersion vs = new VsVersion();
						vs.setVsVersionName(ver.getVersionName());
						vs.setLoseStr(getLoseStr(loseList,ver.getVersionId()));
						Map<String,String> m = getConfig(infoList,v.getVersionId(),ver.getVersionId());
						vs.setAdvantageousStr(m.get("advantageousStr"));
						vs.setDisadvantagedStr(m.get("disadvantagedStr"));
						vsList.add(vs);
					}
					v.setVsVersionList(vsList);
					vList.add(v);
				}
			}
			//对本竞品进行排序
			if(vList.size()>1){
				Collections.sort(vList,new Comparator<Version>(){
					@Override
					public int compare(Version o1, Version o2) {
						return o2.getTp().compareTo(o1.getTp());
					}
				});
			}
			priceList.get(i).setVersionList(vList);
		}
		sub.setPriceList(priceList);
		Map<String, Object> subMap = new HashMap<String, Object>();
		subMap.put("sub", sub);
		
		//拼接返回的json数据
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> columns = new ArrayList<Map<String,Object>>();
		Map<String,Object> c1 = new HashMap<String, Object>();
		c1.put("title", "本品车型");
		c1.put("key", "mySubModel");
		columns.add(c1);
		Map<String,Object> c2 = new HashMap<String, Object>();
		c2.put("title", "价格段");
		c2.put("key", "price");
		columns.add(c2);
		Map<String,Object> c3 = new HashMap<String, Object>();
		c3.put("title", "本品型号");
		c3.put("key", "myVersion");
		columns.add(c3);
		Map<String,Object> c4 = new HashMap<String, Object>();
		c4.put("title", "MSRP");
		c4.put("key", "msrp");
		columns.add(c4);
		Map<String,Object> c5 = new HashMap<String, Object>();
		c5.put("title", "TP");
		c5.put("key", "tp");
		columns.add(c5);
		Map<String,Object> c6 = new HashMap<String, Object>();
		c6.put("title", "竞品圈型号");
		c6.put("key", "vsVersion");
		columns.add(c6);
		Map<String,Object> c7 = new HashMap<String, Object>();
		c7.put("title", "优势配置");
		c7.put("key", "advantageAllocation");
		columns.add(c7);
		Map<String,Object> c8 = new HashMap<String, Object>();
		c8.put("title", "劣势配置");
		c8.put("key", "disadvantagedAllocation");
		columns.add(c8);
		Map<String,Object> c9 = new HashMap<String, Object>();
		c9.put("title", "本品缺失抱怨");
		c9.put("key", "myLose");
		columns.add(c9);
		Map<String,Object> c10 = new HashMap<String, Object>();
		c10.put("title", "竞品缺失抱怨");
		c10.put("key", "vsLose");
		columns.add(c10);
		map.put("columns", columns);
		
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		if(sub.getPriceList()!=null && sub.getPriceList().size()>0){
			Map<String,Integer> numMap = new HashMap<String, Integer>();
			int vsNum = 0;
			for(int i=0; i<sub.getPriceList().size(); i++){
				if(sub.getPriceList().get(i).getVersionList()!=null && sub.getPriceList().get(i).getVersionList().size()>0){
					List<Version> ml = sub.getPriceList().get(i).getVersionList();
					int mlSize = ml.size();
					int priceNum = 0;
					for(int j=0; j<mlSize; j++){
						if(ml.get(j).getVsVersionList()!=null && ml.get(j).getVsVersionList().size()>0){
							List<VsVersion> vl = ml.get(j).getVsVersionList();
							int vlSize = vl.size();
							int myNum = 0;
							for(int k=0; k<vlSize; k++){
								myNum++;
								priceNum++;
								vsNum++;
							}
							numMap.put(sub.getPriceList().get(i).getPriceRange()+ml.get(j).getVersionName(), myNum);
						}
					}
					numMap.put(sub.getPriceList().get(i).getPriceRange(), priceNum);
				}
			}
			numMap.put("subModel", vsNum);
			subMap.put("numMap", numMap);
			List<PriceRange> pl = sub.getPriceList();
			int plSize = pl.size();
			int subModelSign = 0;
			for(int i=0; i<plSize; i++){
				int priceSign = 0;
				if(pl.get(i).getVersionList()!=null && pl.get(i).getVersionList().size()>0){
					List<Version> ml = pl.get(i).getVersionList();
					int mlSize = ml.size();
					for(int j=0; j<mlSize; j++){
						int mlSign = 0;
						if(ml.get(j).getVsVersionList()!=null && ml.get(j).getVsVersionList().size()>0){
							List<VsVersion> vl = ml.get(j).getVsVersionList();
							int vlSize = vl.size();
							for(int k=0; k<vlSize; k++){
								Map<String,Object> m = new HashMap<String, Object>();
								if(subModelSign==0){
									Map<String,Object> mm = new HashMap<String, Object>();
									mm.put("rowSpan", vsNum);
									mm.put("data", sub.getSubModelEname());
									m.put("mySubModel", mm);
									subModelSign++;
								}
								if(priceSign==0){
									Map<String,Object> mm = new HashMap<String, Object>();
									mm.put("rowSpan", numMap.get(pl.get(i).getPriceRange()));
									mm.put("data", pl.get(i).getPriceRange());
									m.put("price", mm);
									priceSign++;
								}
								if(mlSign==0){
									int rowSpan = numMap.get(pl.get(i).getPriceRange()+pl.get(i).getVersionList().get(j).getVersionName());
									Map<String,Object> versionNameMap = new HashMap<String, Object>();
									versionNameMap.put("rowSpan", rowSpan);
									versionNameMap.put("data", pl.get(i).getVersionList().get(j).getVersionName());
									m.put("myVersion", versionNameMap);
									Map<String,Object> versionMsrpMap = new HashMap<String, Object>();
									versionMsrpMap.put("rowSpan", rowSpan);
									versionMsrpMap.put("data", pl.get(i).getVersionList().get(j).getMsrp());
									m.put("msrp", versionMsrpMap);
									Map<String,Object> versionTPMap = new HashMap<String, Object>();
									versionTPMap.put("rowSpan", rowSpan);
									versionTPMap.put("data", pl.get(i).getVersionList().get(j).getTp());
									m.put("tp", versionTPMap);
									Map<String,Object> versionLoseMap = new HashMap<String, Object>();
									versionLoseMap.put("rowSpan", rowSpan);
									versionLoseMap.put("data", pl.get(i).getVersionList().get(j).getLoseStr());
									m.put("myLose", versionLoseMap);
									mlSign++;
								}
								m.put("vsVersion", vl.get(k).getVsVersionName());
								m.put("advantageAllocation", vl.get(k).getAdvantageousStr());
								m.put("disadvantagedAllocation", vl.get(k).getDisadvantagedStr());
								m.put("vsLose", vl.get(k).getLoseStr());
								data.add(m);
							}
						}
					}
				}
			}
		}
		
		map.put("data", data);		
		request.getSession().setAttribute("reportData", subMap);
		return map;
	}
	
	/**
	 * describe: 根据id从list中获取实例
	 * @authod liuyuhuan
	 * @date 2018-3-7 下午4:28:09
	 */
	public Version getVersionById(List<Version> list, String id) {
		Version v = null;
		for(int i=0; i<list.size(); i++){
			if(id.equals(list.get(i).getVersionId())){
				v = list.get(i);
				break;
			}
		}
		return v;
	}
	
	/**
	 * describe: 根据本品id从参数中获取竞品id
	 * @authod liuyuhuan
	 * @date 2018-3-7 下午5:39:21
	 */
	public List<String> getVsByMy(String[] params, String id){
		List<String> list = new ArrayList<String>();
		if(StringUtils.isNotBlank(id)){
			for(int i=0; i<params.length; i++){
				String str[] = params[i].split(",");
				a:for(int t=0; t<str.length; t++){
					if(id.equals(str[t].replace("my=", ""))){
						for(int y=0; y<str.length; y++){
							if(str[y].contains("vs=")){
								list.add(str[y].replace("vs=", ""));
								break a;
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * describe: 根据id获取缺失抱怨的字符串
	 * @authod liuyuhuan
	 * @date 2018-3-7 下午5:49:03
	 */
	public String getLoseStr(List<Version> loseList,String id){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<loseList.size(); i++){
			if(id.equals(loseList.get(i).getVersionId())){
				for(int t=0; t<loseList.get(i).getLose().size(); t++){
					sb.append(loseList.get(i).getLose().get(t).getConfigName()+"、");
				}
			}
		}
		if(sb.length()>0){
			return sb.substring(0, sb.length()-1);
		}
		return sb.toString();
	}
	
	/**
	 * describe: 根据本竞品id和竞品id获取优势配置和劣势配置
	 * @authod liuyuhuan
	 * @date 2018-3-7 下午6:07:31
	 */
	public Map<String,String> getConfig(List<Version> list,String myId, String vsId){
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer advantageousStr = new StringBuffer();
		StringBuffer disadvantagedStr = new StringBuffer();
		Version my = getVersionById(list,myId);
		Version vs = getVersionById(list,vsId);
		if(my.getAllocation()!=null && my.getAllocation().size()>0 && vs.getAllocation()!=null && vs.getAllocation().size()>0){
			for(int i=0; i<my.getAllocation().size(); i++){
				String configTypeName = my.getAllocation().get(i).getConfigTypeName();
				double value = my.getAllocation().get(i).getSatisfactionValue();
				for(int y=0; y<vs.getAllocation().size(); y++){
					if(configTypeName.equals(vs.getAllocation().get(y).getConfigTypeName())){
						if(value > vs.getAllocation().get(y).getSatisfactionValue()){
							advantageousStr.append(configTypeName+"、");
						}else if(value < vs.getAllocation().get(y).getSatisfactionValue()){
							disadvantagedStr.append(configTypeName+"、");
						}
						break;
					}
				}
			}
			if(advantageousStr.length()>0){
				map.put("advantageousStr", advantageousStr.substring(0, advantageousStr.length()-1));
			}
			if(disadvantagedStr.length()>0){
				map.put("disadvantagedStr", disadvantagedStr.substring(0, disadvantagedStr.length()-1));
			}
		}
		return map;
	}

	@Override
	public void ExportReportData(HttpServletResponse response,
			HttpServletRequest request) {
		ServletOutputStream out = null;
		try{
			out = response.getOutputStream();  
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("最终报告");
			//设置列的宽度
			sheet.setColumnWidth(0, 3840);
			sheet.setColumnWidth(1, 5376);
			sheet.setColumnWidth(2, 10752);
			sheet.setColumnWidth(3, 3840);
			sheet.setColumnWidth(4, 3840);
			sheet.setColumnWidth(5, 10752);
			sheet.setColumnWidth(6, 10752);
			sheet.setColumnWidth(7, 10752);
			sheet.setColumnWidth(8, 10752);
			sheet.setColumnWidth(9, 10752);
			Row row = sheet.createRow(0);
			//设置行高
			row.setHeightInPoints(30);
			//写标题
			String[] title = Constants.reportDataExcelTitle;
			CellStyle titleStyle = ExcelUtil.getTitleOne(wb,11,HSSFColor.BLACK.index,true);
			Cell cell;
			for(int i=0; i<title.length; i++){
				cell = row.createCell(i);
				cell.setCellValue(title[i]);
				cell.setCellStyle(titleStyle);
			}
			//数据
			Map<String, Object> subMap = (Map<String, Object>) request.getSession().getAttribute("reportData");
			Map<String,Integer> numMap = (Map<String, Integer>) subMap.get("numMap");
			SubModel sub = (SubModel) subMap.get("sub");
			if(numMap!=null && sub!=null){
				List<PriceRange> pl = sub.getPriceList();
				int plSize = pl.size();
				int subModelSign = 0;
				int rowNum = 1;
				CellStyle cellStyle = ExcelUtil.getCellStyleOne(wb);
				for(int i=0; i<plSize; i++){
					int priceSign = 0;
					if(pl.get(i).getVersionList()!=null && pl.get(i).getVersionList().size()>0){
						List<Version> ml = pl.get(i).getVersionList();
						int mlSize = ml.size();
						for(int j=0; j<mlSize; j++){
							int mlSign = 0;
							if(ml.get(j).getVsVersionList()!=null && ml.get(j).getVsVersionList().size()>0){
								List<VsVersion> vl = ml.get(j).getVsVersionList();
								int vlSize = vl.size();
								for(int k=0; k<vlSize; k++){
									row = sheet.createRow(rowNum);
									if(subModelSign==0){
										/*String imagePath = "http://web.thinktanksgmmd.com/sgm_picture/"+sub.getImagePath().replace("\\", "/");
										URL url = new URL(imagePath.trim());
					                    //打开链接  
					                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
					                    //设置请求方式为"GET"  
					                    conn.setRequestMethod("GET");  
					                    //超时响应时间为5秒  
					                    conn.setConnectTimeout(5 * 1000);  
					                    //通过输入流获取图片数据  
					                    InputStream inStream = conn.getInputStream();  
					                    //得到图片的二进制数据，以二进制封装得到数据，具有通用性  
					                    byte[] data = readInputStream(inStream);
					                    //anchor主要用于设置图片的属性  
					                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 250,(short) 0, rowNum, (short) 0, rowNum+numMap.get("subModel")-1);     
					                    //Sets the anchor type （图片在单元格的位置）
					                    //0 = Move and size with Cells, 2 = Move but don't size with cells, 3 = Don't move or size with cells.
					                    anchor.setAnchorType(0);    
					                    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();   
					                    patriarch.createPicture(anchor, wb.addPicture(data, HSSFWorkbook.PICTURE_TYPE_JPEG));*/
										/*ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
										System.out.println(imagePath);
										BufferedImage bufferImg = ImageIO.read(new File("http://h.hiphotos.baidu.com/image/pic/item/e824b899a9014c0899ee068a067b02087af4f4cc.jpg"));
										ImageIO.write(bufferImg, "jpg", byteArrayOut);
										int width = bufferImg.getWidth();//原始宽度
										int height = bufferImg.getHeight();//原始高度
										// 一个12号字体的宽度为13,前面已设置了列的宽度为30*256，故这里的等比例高度计算如下
										height = (int) Math.round((height * (30 * 13) * 1.0 / width));
										// excel单元格高度是以点单位，1点=2像素; POI中Height的单位是1/20个点，故设置单元的等比例高度如下
										row.setHeight((short) (height / 2 * 20));
										// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
										HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
										// anchor主要用于设置图片的属性
										HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) j, i + 1, (short) j, i + 1);
										anchor.setAnchorType(3);
										// 插入图片
										patriarch.createPicture(anchor,wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));*/
										cell = row.createCell(0);
										cell.setCellValue(sub.getSubModelEname());
										cell.setCellStyle(cellStyle);
										CellRangeAddress range1 = new CellRangeAddress(rowNum, rowNum+numMap.get("subModel")-1, 0, 0);
										sheet.addMergedRegion(range1);
										subModelSign++;
									}
									if(priceSign==0){
										cell = row.createCell(1);
										cell.setCellValue(pl.get(i).getPriceRange());
										cell.setCellStyle(cellStyle);
										CellRangeAddress range1 = new CellRangeAddress(rowNum, rowNum+numMap.get(pl.get(i).getPriceRange())-1, 1, 1);
										sheet.addMergedRegion(range1);
										priceSign++;
									}
									if(mlSign==0){
										cell = row.createCell(2);
										cell.setCellValue(ml.get(j).getVersionName());
										cell.setCellStyle(cellStyle);
										cell = row.createCell(3);
										cell.setCellValue(ml.get(j).getMsrp());
										cell.setCellStyle(cellStyle);
										cell = row.createCell(4);
										cell.setCellValue(ml.get(j).getTp());
										cell.setCellStyle(cellStyle);
										cell = row.createCell(8);
										cell.setCellValue(ml.get(j).getLoseStr());
										cell.setCellStyle(cellStyle);
										CellRangeAddress range1 = new CellRangeAddress(rowNum, rowNum+numMap.get(pl.get(i).getPriceRange()+ml.get(j).getVersionName())-1, 2, 2);
										sheet.addMergedRegion(range1);
										CellRangeAddress range2 = new CellRangeAddress(rowNum, rowNum+numMap.get(pl.get(i).getPriceRange()+ml.get(j).getVersionName())-1, 3, 3);
										sheet.addMergedRegion(range2);
										CellRangeAddress range3 = new CellRangeAddress(rowNum, rowNum+numMap.get(pl.get(i).getPriceRange()+ml.get(j).getVersionName())-1, 4, 4);
										sheet.addMergedRegion(range3);
										CellRangeAddress range4 = new CellRangeAddress(rowNum, rowNum+numMap.get(pl.get(i).getPriceRange()+ml.get(j).getVersionName())-1, 8, 8);
										sheet.addMergedRegion(range4);
										mlSign++;
									}
									cell = row.createCell(5);
									cell.setCellValue(vl.get(k).getVsVersionName());
									cell.setCellStyle(cellStyle);
									cell = row.createCell(6);
									cell.setCellValue(vl.get(k).getAdvantageousStr());
									cell.setCellStyle(cellStyle);
									cell = row.createCell(7);
									cell.setCellValue(vl.get(k).getDisadvantagedStr());
									cell.setCellStyle(cellStyle);
									cell = row.createCell(9);
									cell.setCellValue(vl.get(k).getLoseStr());
									cell.setCellStyle(cellStyle);
									rowNum++;
								}
							}
						}
					}
				}
			}
			String excelName = java.net.URLEncoder.encode("最终报告", "UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+excelName+".xls" );  
			wb.write(out);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static byte[] readInputStream(InputStream inStream) throws Exception{  
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	    //创建一个Buffer字符串  
	    byte[] buffer = new byte[1024];  
	    //每次读取的字符串长度，如果为-1，代表全部读取完毕  
	    int len = 0;  
	    //使用一个输入流从buffer里把数据读取出来  
	    while( (len=inStream.read(buffer)) != -1 ){  
	        //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
	        outStream.write(buffer, 0, len);  
	    }  
	    //关闭输入流  
	    inStream.close();  
	    //把outStream里的数据写入内存  
	    return outStream.toByteArray();  
	}

	@Override
	public void ExportAnalyzeData(HttpServletResponse response,
			HttpServletRequest request) {
		ServletOutputStream out = null;
		try{
			out = response.getOutputStream();  
			Workbook wb = new SXSSFWorkbook();
			Sheet sheet = wb.createSheet("产品机会点分析");
			Row row = sheet.createRow(0);
			row.setHeightInPoints(30);
			Cell cell;
			CellStyle cellStyle = ExcelUtil.getCellStyleOne(wb);
			CellStyle cellStyle1 = ExcelUtil.getCellStyleTwo(wb,210,222,239);
			CellStyle cellStyle2 = ExcelUtil.getCellStyleTwo(wb,234,239,247);
			CellStyle cellStyle3 = ExcelUtil.getPointOne(wb, "黑体", 11, true);
			
			CellStyle cellTitleStyle = ExcelUtil.getTitleOne(wb,10,HSSFColor.WHITE.index,true);
			CellStyle cellTitleStyleTwo = ExcelUtil.getTitleOne(wb,11,HSSFColor.BLACK.index,false);
			Map<String, Object> map = (Map<String, Object>) request.getSession().getAttribute("analyzeData");
			if(map!=null){
				//写标题
				String title = "价格段："+map.get("priceTier")+"  所属细分市场："+map.get("market");
				cell = row.createCell(0);
				cell.setCellValue(title);
				cell.setCellStyle(cellTitleStyleTwo);
				CellRangeAddress range1 = new CellRangeAddress(0, 0, 0, 6);
				sheet.addMergedRegion(range1);
				sheet.setColumnWidth(0, 10752);
				sheet.setColumnWidth(1, 5600);
				sheet.setColumnWidth(2, 5600);
				sheet.setColumnWidth(3, 5376);
				sheet.setColumnWidth(4, 5400);
				sheet.setColumnWidth(5, 5400);
				sheet.setColumnWidth(6, 10752);
				
				List<Map<String, Object>> mixAndLose = (List<Map<String, Object>>) map.get("mixAndLose");
				if(mixAndLose!=null && mixAndLose.size()>0){
					Map<String, Object> my = null;
					Map<String, Object> vs = null;
					if(mixAndLose!=null && mixAndLose.size()>0){
						for(int i=0; i<mixAndLose.size(); i++){
							if("y".equals(mixAndLose.get(i).get("isMy"))){
								my = mixAndLose.get(i);
							}else{
								vs = mixAndLose.get(i);
							}
						}
					}
					
					//缺失抱怨标题+MSRP+TP
					row = sheet.createRow(1);
					row.setHeightInPoints(20);
					cell = row.createCell(0);
					cell.setCellValue(my.get("versionName").toString());
					cell.setCellStyle(cellStyle3);
					cell = row.createCell(1);
					cell.setCellValue(my.get("versionName").toString());
					CellRangeAddress range2 = new CellRangeAddress(1, 1, 1, 2);
					sheet.addMergedRegion(range2);
					cell.setCellStyle(cellTitleStyle);
					cell = row.createCell(3);
					cell.setCellStyle(cellTitleStyle);
					cell = row.createCell(4);
					cell.setCellValue(vs.get("versionName").toString());
					CellRangeAddress range3 = new CellRangeAddress(1, 1, 4, 5);
					cell.setCellStyle(cellTitleStyle);
					sheet.addMergedRegion(range3);
					cell = row.createCell(6);
					cell.setCellValue(vs.get("versionName").toString());
					cell.setCellStyle(cellStyle3);
					
					row = sheet.createRow(2);
					cell = row.createCell(0);
					cell.setCellValue("配置缺失抱怨");
					cell.setCellStyle(cellStyle3);
					cell = row.createCell(1);
					cell.setCellValue("MSRP");
					cell.setCellStyle(cellStyle1);
					cell = row.createCell(2);
					cell.setCellValue(my.get("msrp").toString());
					cell.setCellStyle(cellStyle1);
					cell = row.createCell(3);
					cell.setCellStyle(cellStyle1);
					cell = row.createCell(4);
					cell.setCellValue("MSRP");
					cell.setCellStyle(cellStyle1);
					cell = row.createCell(5);
					cell.setCellValue(vs.get("msrp").toString());
					cell.setCellStyle(cellStyle1);
					cell = row.createCell(6);
					cell.setCellValue("配置缺失抱怨");
					cell.setCellStyle(cellStyle3);
					
					row = sheet.createRow(3);
					cell = row.createCell(1);
					cell.setCellValue("TP");
					cell.setCellStyle(cellStyle2);
					cell = row.createCell(2);
					cell.setCellValue(my.get("tp").toString());
					cell.setCellStyle(cellStyle2);
					cell = row.createCell(3);
					cell.setCellStyle(cellStyle2);
					cell = row.createCell(4);
					cell.setCellValue("TP");
					cell.setCellStyle(cellStyle2);
					cell = row.createCell(5);
					cell.setCellValue(vs.get("tp").toString());
					cell.setCellStyle(cellStyle2);
					
					row = sheet.createRow(4);
					row.setHeightInPoints(20);
					cell = row.createCell(0);
					cell.setCellValue("N="+my.get("N"));
					cell.setCellStyle(cellStyle);
					cell = row.createCell(1);
					cell.setCellValue("优势配置");
					cell.setCellStyle(cellTitleStyle);
					cell = row.createCell(2);
					cell.setCellValue("满意度");
					cell.setCellStyle(cellTitleStyle);
					cell = row.createCell(3);
					cell.setCellValue("关注度");
					cell.setCellStyle(cellTitleStyle);
					cell = row.createCell(4);
					cell.setCellValue("满意度");
					cell.setCellStyle(cellTitleStyle);
					cell = row.createCell(5);
					cell.setCellValue("优势配置");
					cell.setCellStyle(cellTitleStyle);
					cell = row.createCell(6);
					cell.setCellValue("N="+vs.get("N"));
					cell.setCellStyle(cellStyle);
					
					List<Map<String,Object>> data = (List<Map<String, Object>>) map.get("data");
					//展示缺失抱怨内容+优势配置+关注度+满意度
					//判断以谁为主做循环
					int myNum = Integer.parseInt(my.get("N").toString());
					int vsNum = Integer.parseInt(vs.get("N").toString());
					int myListSize = Integer.parseInt(map.get("myListSize").toString());
					int vsListSize = Integer.parseInt(map.get("vsListSize").toString());
					int rowNum = 5;
					int signNum = 0;
					List<Map<String,String>> myLose = (List<Map<String, String>>) my.get("lose");
					int myLoseSign = 0;
					int myLoseSize=myLose.size();
					List<Map<String,String>> vsLose = (List<Map<String, String>>) vs.get("lose");
					int vsLoseSign = 0;
					int vsLoseSize = vsLose.size();
					for(int i=0; i<data.size(); i++){
						Map<String,Object> m = data.get(i);
						Set<String> mySet = (Set<String>) m.get("myAdvantage");
						List<String> myList = new ArrayList<String>(mySet);
						int myListNum = myList.size();
						Set<String> vsSet = (Set<String>) m.get("vsAdvantage");
						List<String> vsList = new ArrayList<String>(vsSet);
						int vsListNum = vsList.size();
						int num = 0;
						if(myListNum > vsListNum){
							num = myListNum;
						}else if(myListNum < vsListNum){
							num = vsListNum;
						}else if(myListNum == vsListNum){
							num = myListNum;
						}
						for(int q=0; q<num; q++){
							row = sheet.createRow(rowNum);
							//关注度、满意度
							if(q==0){
								cell = row.createCell(2);
								if("-".equals(m.get("mySatisfaction").toString())){
									cell.setCellValue(m.get("mySatisfaction").toString());
								}else{
									cell.setCellValue(Double.parseDouble(m.get("mySatisfaction").toString()));
								}
								cell.setCellStyle(cellStyle);
								List<String> l = (List<String>) m.get("myAttention");
								if(l!=null && l.size()>0){
									cell = row.createCell(3);
									cell.setCellValue(l.get(0)+"("+l.get(1)+")");
									cell.setCellStyle(cellStyle);
								}
								cell = row.createCell(4);
								if("-".equals(m.get("mySatisfaction").toString())){
									cell.setCellValue(m.get("mySatisfaction").toString());
								}else{
									cell.setCellValue(Double.parseDouble(m.get("mySatisfaction").toString()));
								}
								cell.setCellStyle(cellStyle);
							}
							//配置缺失抱怨内容
							if(signNum%2==0){
								if((myNum*2-1)>i){
									if(myLoseSign<myLoseSize){
										cell = row.createCell(0);
										cell.setCellValue(myLose.get(myLoseSign).get("text")+" "+myLose.get(myLoseSign).get("value"));
										cell.setCellStyle(cellStyle);
										myLoseSign++;
									}
								}
								if((vsNum*2-1)>i){
									if(vsLoseSign<vsLoseSize){
										cell = row.createCell(6);
										cell.setCellValue(vsLose.get(vsLoseSign).get("text")+" "+vsLose.get(vsLoseSign).get("value"));
										cell.setCellStyle(cellStyle);
										vsLoseSign++;
									}
								}
							}
							//优势配置
							if(myListNum > q){
								cell = row.createCell(1);
								cell.setCellValue(myList.get(q));
								cell.setCellStyle(cellStyle);
							}
							if(vsListNum > q){
								cell = row.createCell(5);
								cell.setCellValue(vsList.get(q));
								cell.setCellStyle(cellStyle);
							}
							signNum++;
							rowNum++;
						}
						
					}
					//缺失抱怨的两倍数量比优势配置数量多时
					if(myNum*2>myListSize || myNum*2>vsListSize || vsNum*2>myListSize || vsNum*2>vsListSize){
						int maxNum=0;
						if(myNum>vsNum){
							maxNum = myNum*2;
						}else{
							maxNum = vsNum*2;
						}
						int maxListSize=0;
						if(myListSize>vsListSize){
							maxListSize = myListSize;
						}else{
							maxListSize = vsListSize;
						}
						for(int i=0; i<maxNum-maxListSize; i++){
							if(signNum%2==0){
								row = sheet.createRow(rowNum);
								if(myLoseSign<myLoseSize){
									cell = row.createCell(0);
									cell.setCellValue(myLose.get(myLoseSign).get("text")+" "+myLose.get(myLoseSign).get("value"));
									cell.setCellStyle(cellStyle);
									myLoseSign++;
								}
								if(vsLoseSign<vsLoseSize){
									cell = row.createCell(6);
									cell.setCellValue(vsLose.get(vsLoseSign).get("text")+" "+vsLose.get(vsLoseSign).get("value"));
									cell.setCellStyle(cellStyle);
									vsLoseSign++;
								}
							}
							rowNum++;
							signNum++;
						}
						
					}
					
				}
				sheet.setDisplayGridlines(false); 
				}
			String excelName = java.net.URLEncoder.encode("产品机会点分析", "UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+excelName+".xlsx" );
			wb.write(out);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Map<String, Object> saveReportParams(Map<String, String> param) {
		Map<String, Object> re = new HashMap<String, Object>();
		try{
			optimizeDao.deleteReportParams(param);
			optimizeDao.saveReportParams(param);
			re.put("code", 1);
			re.put("str", "保存成功");
		}catch(Exception e){
			e.printStackTrace();
			re.put("code", 0);
			re.put("str", "保存失败");
		}
		return re;
	}

	@Override
	public Map<String, Object> deleteReportParams(Map<String, String> param) {
		Map<String, Object> re = new HashMap<String, Object>();
		try{
			optimizeDao.deleteReportParams(param);
			re.put("code", 1);
			re.put("str", "保存成功");
		}catch(Exception e){
			e.printStackTrace();
			re.put("code", 0);
			re.put("str", "保存失败");
		}
		return re;
	}
	
}
