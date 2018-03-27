package com.ways.app.priceOptimize.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ways.app.priceOptimize.dao.PreOptimizeDao;
import com.ways.app.priceOptimize.entity.CompData;
import com.ways.app.priceOptimize.entity.DateValue;
import com.ways.app.priceOptimize.entity.ModelBase;
import com.ways.app.priceOptimize.entity.ModelComp;
import com.ways.app.priceOptimize.entity.ModelDB;
import com.ways.app.priceOptimize.entity.SalesData;
import com.ways.app.priceOptimize.service.PreOptimizeService;
import com.ways.app.priceOptimize.util.PriceStringUtil;
import com.ways.app.priceOptimize.util.SalesExportExcelUtils;



@SuppressWarnings({ "unchecked", "rawtypes" , "static-access", "unused"})
@Service
public class PreOptimizeServiceImpl implements PreOptimizeService{

	@Autowired
	private PreOptimizeDao preOptimizeDao;
	
	
	SalesExportExcelUtils styleUtil = new SalesExportExcelUtils();
	
	
	
	@Override
	public List<DateValue> getDate(HttpServletRequest request, HashMap map) {
		
		List<DateValue> list = preOptimizeDao.getDate(map);
		
		list.get(0).setSelect(list.get(0).getMax());
		
		return list;
	}




	@Override
	public List<ModelBase> getBaseModel(HttpServletRequest request, HashMap map) {
		
		String brand=request.getParameter("brand");
		String date=request.getParameter("time");
		
//		brand="14";
//		date="2017-8";
		
		String year = date.split("-")[0];
		String month = date.split("-")[1];
		String ym="";
		if(Integer.parseInt(month)<10){
			ym=year+"0"+month;
		}else{
			ym=year+month;
		}
		
		map.put("brand", brand);
		map.put("ym", ym);
		
		List<ModelBase> list = preOptimizeDao.getBaseModel(map);
		
		for(int i=0;i<list.size();i++){
			list.get(i).setUrl("http://web.thinktanksgmmd.com/sgm_picture/"+
						list.get(i).getUrl().replace("\\", "/"));
		}
		
		return list;
	}




	@Override
	public List<ModelComp> getCompModel(HttpServletRequest request, HashMap map) {
		
		String stageId=request.getParameter("stageId");
		String date=request.getParameter("time");
		String myModelId=request.getParameter("myModelId");
		
		
//		myModelId="3195";
//		date="2017-3";
//		stageId="4";
		
		String year = date.split("-")[0];
		String month = date.split("-")[1];
		String ym="";
		if(Integer.parseInt(month)<10){
			ym=year+"0"+month;
		}else{
			ym=year+month;
		}
		
		map.put("myModelId", myModelId);
		map.put("ym", ym);
		map.put("stageId", stageId);
		
		List<ModelComp> list = preOptimizeDao.ModelComp(map);
		
		return list;
	}




	@Override
	public String getSalesDistribute(HttpServletRequest request, HashMap map) {
		
		String myModelId=request.getParameter("myModelId");
		String vsModelId=request.getParameter("vsModelId");
		String date=request.getParameter("time");
		String myModelName=request.getParameter("myModelName");
		String vsModelName=request.getParameter("vsModelName");
		String priceSegment=request.getParameter("priceSegment");//价格段，三个一组，1-2(以3分段)
		String tip=request.getParameter("tip");//1增速 2占比 3占比变化 4份额 5份额变化
		
		
		
//		vsModelId="1043,3175,1963";
//		myModelId="2748";
//		date = "2017-9";
//		
//		myModelName="CT6";
//		vsModelName="XTS,XFL,ATS-L";
//		priceSegment = "26,40,2,40,50,5";
//		tip = "1,2,3";
		
		
		String[] compModelId=vsModelId.split(",");
		String[] compModelName=vsModelName.split(",");
		
		map.put("myModelId", myModelId);
		map.put("vsModelId", vsModelId);
		
		//时间
		String year = date.split("-")[0];
		String month = date.split("-")[1];
		String ym="";
		int lastYear=Integer.parseInt(year)-1;
		String lastYm="";
		if(Integer.parseInt(month)<10){
			ym=year+"0"+month;
			lastYm=lastYear+"0"+month;
		}else{
			ym=year+month;
			lastYm=lastYear+month;
		}
		map.put("ym", ym);
		map.put("lastYm", lastYm);
		
		
		
		
		
		//价格段
		int[] segment = new int[priceSegment.split(",").length];
		for(int i=0;i<priceSegment.split(",").length;i++){
			segment[i]=Integer.parseInt(priceSegment.split(",")[i]);
		}
		String leftSegmentSql = PriceStringUtil.getPriceSegment(segment);
		String priceSegmentSql = PriceStringUtil.getPriceSegment1(segment);
		map.put("leftSegmentSql", leftSegmentSql);
		map.put("priceSegmentSql", priceSegmentSql);
		//sql循环填充
		List<ModelDB> modelDB=new ArrayList<ModelDB>();
		for(int i=0;i<vsModelId.split(",").length;i++){
			ModelDB mm = new ModelDB();
			mm.setId(vsModelId.split(",")[i]);
			mm.setResult("result"+i);
			modelDB.add(0, mm);
		}
		String sql=getModelDBSql(modelDB);
		map.put("modelDB", sql);
		
		
		//价格段数据
		List<SalesData> list = null;
		list = preOptimizeDao.getSalesDistribute(map);
		
		//totalsql
		leftSegmentSql="select 'x' as fsort, 'total' segment from dual";
		priceSegmentSql="'x'";
		map.put("leftSegmentSql", leftSegmentSql);
		map.put("priceSegmentSql", priceSegmentSql);
		//总计
		List<SalesData> totallist = null;
		totallist = preOptimizeDao.getSalesDistribute(map);
		
		
		int num = list.get(0).getCompDateList().size(); //竞品数量
		int digit = tip.split(",").length;
		if(tip.equals("")){
			digit=0;
		}
		int digit0= digit;
		if(tip.indexOf("4")!=-1){
			digit0=digit0-1;
		}
		if(tip.indexOf("5")!=-1){
			digit0=digit0-1;
		}
		JSONObject result = new JSONObject();
		//标题行
		JSONArray titleArray = new JSONArray();
		JSONObject title = new JSONObject();
		JSONArray children = null;
		JSONObject childType = null;
		JSONObject titleStyle = null;
		JSONObject childTitleStyle = null;
		titleStyle=new JSONObject();
		title.put("title", "价格段");
		title.put("rowSpan", 2);
		titleStyle.put("width", 130);
		titleStyle.put("background", "#A6A6A6");
		title.put("titleStyle", titleStyle);
		titleArray.add(title);
		
		children = new JSONArray();
		title = new JSONObject();
		titleStyle=new JSONObject();
		title.put("title","行业");
		title.put("click", "");
		title.put("colSpan", digit0+1);
		titleStyle.put("background", "#A6A6A6");
		title.put("titleStyle", titleStyle);
		childType = new JSONObject();
		childTitleStyle=new JSONObject();
		childTitleStyle.put("width", 80);
		childTitleStyle.put("background", "#A6A6A6");
		childType.put("titleStyle", childTitleStyle);
		childType.put("title", "销量");
		children.add(childType);
		if(tip.indexOf("1")!=-1){
			childType = new JSONObject();
			childTitleStyle=new JSONObject();
			childTitleStyle.put("width", 80);
			childTitleStyle.put("background", "#A6A6A6");
			childType.put("titleStyle", childTitleStyle);
			childType.put("title", "增速");
			children.add(childType);
		}
		if(tip.indexOf("2")!=-1){
			childType = new JSONObject();
			childTitleStyle=new JSONObject();
			childTitleStyle.put("width", 80);
			childTitleStyle.put("background", "#A6A6A6");
			childType.put("titleStyle", childTitleStyle);
			childType.put("title", "占比");
			children.add(childType);
		}
		if(tip.indexOf("3")!=-1){
			childType = new JSONObject();
			childTitleStyle=new JSONObject();
			childTitleStyle.put("width", 80);
			childTitleStyle.put("background", "#A6A6A6");
			childType.put("titleStyle", childTitleStyle);
			childType.put("title", "占比变化");
			children.add(childType);
		}
		title.put("children", children);
		titleArray.add(title);
		
		children = new JSONArray();
		title = new JSONObject();
		titleStyle=new JSONObject();
		title.put("title",myModelName);
		title.put("click", "");
		title.put("colSpan", digit+1);
		titleStyle.put("background", "#538DD5");
		title.put("titleStyle", titleStyle);
		childType = new JSONObject();
		childTitleStyle=new JSONObject();
		childTitleStyle.put("width", 80);
		childTitleStyle.put("background", "#538DD5");
		childType.put("titleStyle", childTitleStyle);
		childType.put("title", "销量");
		children.add(childType);
		if(tip.indexOf("1")!=-1){
			childType = new JSONObject();
			childTitleStyle=new JSONObject();
			childTitleStyle.put("width", 80);
			childTitleStyle.put("background", "#538DD5");
			childType.put("titleStyle", childTitleStyle);
			childType.put("title", "增速");
			children.add(childType);
		}
		if(tip.indexOf("2")!=-1){
			childType = new JSONObject();
			childTitleStyle=new JSONObject();
			childTitleStyle.put("width", 80);
			childTitleStyle.put("background", "#538DD5");
			childType.put("titleStyle", childTitleStyle);
			childType.put("title", "占比");
			children.add(childType);
		}
		if(tip.indexOf("3")!=-1){
			childType = new JSONObject();
			childTitleStyle=new JSONObject();
			childTitleStyle.put("width", 80);
			childTitleStyle.put("background", "#538DD5");
			childType.put("titleStyle", childTitleStyle);
			childType.put("title", "占比变化");
			children.add(childType);
		}
		if(tip.indexOf("4")!=-1){
			childType = new JSONObject();
			childTitleStyle=new JSONObject();
			childTitleStyle.put("width", 80);
			childTitleStyle.put("background", "#538DD5");
			childType.put("titleStyle", childTitleStyle);
			childType.put("title", "份额");
			children.add(childType);
		}
		if(tip.indexOf("5")!=-1){
			childType = new JSONObject();
			childTitleStyle=new JSONObject();
			childTitleStyle.put("width", 80);
			childTitleStyle.put("background", "#538DD5");
			childType.put("titleStyle", childTitleStyle);
			childType.put("title", "份额变化");
			children.add(childType);
		}
		title.put("children", children);
		titleArray.add(title);
		
		for(int i=0;i<num;i++){
			
			children = new JSONArray();
			title = new JSONObject();
			titleStyle=new JSONObject();
			title.put("title",compModelName[i]);
			title.put("click", "");
			title.put("colSpan", digit+1);
			titleStyle.put("background", "#95B3D7");
			title.put("titleStyle", titleStyle);
			childType = new JSONObject();
			childTitleStyle=new JSONObject();
			childTitleStyle.put("width", 80);
			childTitleStyle.put("background", "#95B3D7");
			childType.put("titleStyle", childTitleStyle);
			childType.put("title", "销量");
			children.add(childType);
			if(tip.indexOf("1")!=-1){
				childType = new JSONObject();
				childTitleStyle=new JSONObject();
				childTitleStyle.put("width", 80);
				childTitleStyle.put("background", "#95B3D7");
				childType.put("titleStyle", childTitleStyle);
				childType.put("title", "增速");
				children.add(childType);
			}
			if(tip.indexOf("2")!=-1){
				childType = new JSONObject();
				childTitleStyle=new JSONObject();
				childTitleStyle.put("width", 80);
				childTitleStyle.put("background", "#95B3D7");
				childType.put("titleStyle", childTitleStyle);
				childType.put("title", "占比");
				children.add(childType);
			}
			if(tip.indexOf("3")!=-1){
				childType = new JSONObject();
				childTitleStyle=new JSONObject();
				childTitleStyle.put("width", 80);
				childTitleStyle.put("background", "#95B3D7");
				childType.put("titleStyle", childTitleStyle);
				childType.put("title", "占比变化");
				children.add(childType);
			}
			if(tip.indexOf("4")!=-1){
				childType = new JSONObject();
				childTitleStyle=new JSONObject();
				childTitleStyle.put("width", 80);
				childTitleStyle.put("background", "#95B3D7");
				childType.put("titleStyle", childTitleStyle);
				childType.put("title", "份额");
				children.add(childType);
			}
			if(tip.indexOf("5")!=-1){
				childType = new JSONObject();
				childTitleStyle=new JSONObject();
				childTitleStyle.put("width", 80);
				childTitleStyle.put("background", "#95B3D7");
				childType.put("titleStyle", childTitleStyle);
				childType.put("title", "份额变化");
				children.add(childType);
			}
			title.put("children", children);
			titleArray.add(title);
		}
		
		//数据
		JSONArray dataArray = new JSONArray();
		JSONArray segmentData = null;//区间数据
		JSONObject segmentNotCheckData = null;//首尾不选
		int comp=0;
		
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				SalesData datalist=list.get(i);
				segmentData=new JSONArray();
				segmentNotCheckData=new JSONObject();
				segmentNotCheckData.put("text", datalist.getSegment());
				if(i==0 || i==list.size()-1){
					if(datalist.getSegment().indexOf("<")!=-1 && datalist.getSegment().indexOf("≤")!=-1){
						segmentNotCheckData.put("checked",false);
					}else{
						segmentNotCheckData.put("checked",false);
						segmentNotCheckData.put("noCheck", true);
					}
				}else{
					if(datalist.getBaseBqSales().equals("-")){
						segmentNotCheckData.put("checked",false);
						segmentNotCheckData.put("noCheck", true);
					}else{
						segmentNotCheckData.put("checked",false);
					}
				}
				segmentData.add(segmentNotCheckData);
				segmentData.add(datalist.getBqSales());
				if(tip.indexOf("1")!=-1){
					segmentData.add(datalist.getGrowth());
				}
				if(tip.indexOf("2")!=-1){
					segmentData.add(datalist.getBqMix());
				}
				if(tip.indexOf("3")!=-1){
					segmentData.add(datalist.getMixChange());
				}
				
				segmentData.add(datalist.getBaseBqSales());
				if(tip.indexOf("1")!=-1){
					segmentData.add(datalist.getBaseGrowth());
				}
				if(tip.indexOf("2")!=-1){
					segmentData.add(datalist.getBaseBqMix());
				}
				if(tip.indexOf("3")!=-1){
					segmentData.add(datalist.getBaseMixChange());
				}
				if(tip.indexOf("4")!=-1){
					segmentData.add(datalist.getBaseModelShare());
				}
				if(tip.indexOf("5")!=-1){
					segmentData.add(datalist.getBaseShareChange());
				}
				comp=0;
				for(int k=0;k<compModelId.length;k++){
					for(int j=0;j<datalist.getCompDateList().size();j++){
						CompData dataComp=datalist.getCompDateList().get(j);
						if(dataComp.getId().equals(compModelId[k])){
							segmentData.add(dataComp.getSalesDataComp().get(0).getCompBqSales());
							if(!dataComp.getSalesDataComp().get(0).getCompBqSales().equals("-")){
								comp=1;
							}
							if(tip.indexOf("1")!=-1){
								segmentData.add(dataComp.getSalesDataComp().get(0).getCompGrowth());
							}
							if(tip.indexOf("2")!=-1){
								segmentData.add(dataComp.getSalesDataComp().get(0).getCompBqMix());
							}
							if(tip.indexOf("3")!=-1){
								segmentData.add(dataComp.getSalesDataComp().get(0).getCompMixChange());
							}
							if(tip.indexOf("4")!=-1){
								segmentData.add(dataComp.getSalesDataComp().get(0).getCompModelShare());
							}
							if(tip.indexOf("5")!=-1){
								segmentData.add(dataComp.getSalesDataComp().get(0).getCompShareChange());
							}
						}
					}
				}
				if(comp==0){
					segmentNotCheckData.put("noCheck", true);
				}
				dataArray.add(segmentData);
			}
		}
		//total行
		if(totallist!=null && totallist.size()>0){
			SalesData totaldata=totallist.get(0);
			segmentData=new JSONArray();
			segmentNotCheckData=new JSONObject();
			segmentNotCheckData.put("text",totaldata.getSegment());
			segmentNotCheckData.put("checked",false);
			segmentNotCheckData.put("noCheck", true);
			segmentData.add(segmentNotCheckData);
			segmentData.add(totaldata.getBqSales());
			if(tip.indexOf("1")!=-1){
				segmentData.add(totaldata.getGrowth());
			}
			if(tip.indexOf("2")!=-1){
				segmentData.add(totaldata.getBqMix());
			}
			if(tip.indexOf("3")!=-1){
				segmentData.add(totaldata.getMixChange());
			}
			
			segmentData.add(totaldata.getBaseBqSales());
			if(tip.indexOf("1")!=-1){
				segmentData.add(totaldata.getBaseGrowth());
			}
			if(tip.indexOf("2")!=-1){
				segmentData.add(totaldata.getBaseBqMix());
			}
			if(tip.indexOf("3")!=-1){
				segmentData.add(totaldata.getBaseMixChange());
			}
			if(tip.indexOf("4")!=-1){
				segmentData.add(totaldata.getBaseModelShare());
			}
			if(tip.indexOf("5")!=-1){
				segmentData.add(totaldata.getBaseShareChange());
			}
			
			for(int k=0;k<compModelId.length;k++){
				for(int j=0;j<totaldata.getCompDateList().size();j++){
					CompData dataComp=totaldata.getCompDateList().get(j);
					if(dataComp.getId().equals(compModelId[k])){
						segmentData.add(dataComp.getSalesDataComp().get(0).getCompBqSales());
						if(tip.indexOf("1")!=-1){
							segmentData.add(dataComp.getSalesDataComp().get(0).getCompGrowth());
						}
						if(tip.indexOf("2")!=-1){
							segmentData.add(dataComp.getSalesDataComp().get(0).getCompBqMix());
						}
						if(tip.indexOf("3")!=-1){
							segmentData.add(dataComp.getSalesDataComp().get(0).getCompMixChange());
						}
						if(tip.indexOf("4")!=-1){
							segmentData.add(dataComp.getSalesDataComp().get(0).getCompModelShare());
						}
						if(tip.indexOf("5")!=-1){
							segmentData.add(dataComp.getSalesDataComp().get(0).getCompShareChange());
						}
					}
				}
			}
			dataArray.add(segmentData);
		
		}
		
		result.put("columns", titleArray);
		result.put("data", dataArray);
		
		return result.toString();
	}


	



	
	@Override
	public Workbook exportTableData(HttpServletRequest request, Map<String, Object> params) {
		
		
		String tip=request.getParameter("tip");//1增速 2占比 3占比变化 4份额 5份额变化
		
		String marketName=request.getParameter("marketName");
		
//		tip = "1,2,3";
//		market="Inter Luxury";
		
		
		int digit=tip.split(",").length;//本竞品子标题数量
		if(tip.equals("")){
			digit=0;
		}
		int digit0= digit;//细分市场子标题数量
		int feed1=3;
		int feed2=5;//子标题四个字的换行显示
		if(tip.indexOf("1")==-1){
			feed1--;
			feed2--;
		}
		if(tip.indexOf("2")==-1){
			feed1--;
			feed2--;
		}
		if(tip.indexOf("3")==-1){
			feed1=-1;
			feed2--;
		}
		if(tip.indexOf("4")==-1){
			feed2--;
		}else{
			digit0=digit0-1;
		}
		if(tip.indexOf("5")==-1){
			feed2=-1;
		}else{
			digit0=digit0-1;
		}
		
		
		String json = null;
		
		if(params==null){
			json = this.getSalesDistribute(request, (HashMap) params);
		}else{
			json = (String) params.get("exportDatas");
		}
		
		
		Workbook wb =  new SXSSFWorkbook();
		
		JSONObject object = JSONObject.parseObject(json); 
		JSONArray columnsArray = object.getJSONArray("columns"); 
		JSONArray dataArray = object.getJSONArray("data");
		
//		CellStyle GradeheadStyle = styleUtil.getGradeHeadStyle(wb);
		CellStyle headStyleGray = styleUtil.getHeadExeclStyle2(wb);
		CellStyle headStyleBBlue = styleUtil.getHeadExeclStyle3(wb);
		CellStyle headStyleSBlue = styleUtil.getHeadExeclStyle(wb);
		CellStyle headChildStyleGray = styleUtil.getHeadChildExeclStyle2(wb);
		CellStyle headChildStyleBBlue = styleUtil.getHeadChildExeclStyle3(wb);
		CellStyle headChildStyleSBlue = styleUtil.getHeadChildExeclStyle(wb);
		CellStyle headChildFirstStyleGray = styleUtil.getHeadChildFirstExeclStyle2(wb);
		CellStyle headChildFirstStyleBBlue = styleUtil.getHeadChildFirstExeclStyle3(wb);
		CellStyle headChildFirstStyleSBlue = styleUtil.getHeadChildFirstExeclStyle(wb);
		Sheet sheet = wb.createSheet("data"); 
		sheet.setColumnWidth(0, 4000); 
		
		 Row row = null; 
		 Cell cell = null;
		 int rowIndex = 2; 
		
		//细分市场
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue("所属细分市场: "+marketName);
//		sheet.addMergedRegion(new CellRangeAddress(1,1,0,1));
//		cell.setCellStyle(GradeheadStyle);
		//标题
		row = sheet.createRow(rowIndex++);
		Row row1 = sheet.createRow(rowIndex++);
		for(int i=0;i<columnsArray.size();i++){
			JSONObject columnsObject = columnsArray.getJSONObject(i);
			
			if(i==0){
				cell = row.createCell(i); 
				cell.setCellValue(columnsObject.getString("title"));
				
				sheet.addMergedRegion(new CellRangeAddress(2,3,0,0));
				cell.setCellStyle(headStyleGray);
			}else if(i==1){
				JSONArray columnsArray1 = columnsObject.getJSONArray("children");
				cell = row.createCell(1); 
				cell.setCellValue(columnsObject.getString("title"));
				cell.setCellStyle(headStyleGray);
				for(int j=0;j<digit0;j++){
					cell = row.createCell(j+2);
					cell.setCellStyle(headStyleGray);
				}
				sheet.addMergedRegion(new CellRangeAddress(2,2,1,digit0+1));
				
				for(int j=0;j<columnsArray1.size();j++){
					JSONObject columnsObject1 = columnsArray1.getJSONObject(j); 
					int y=1;
					cell = row1.createCell(y+j); 
					if(j==0){
						cell.setCellStyle(headChildFirstStyleGray);
					}else{
						cell.setCellStyle(headChildStyleGray);
					}
					
					if(j==feed1){
						String t1=columnsObject1.getString("title").substring(0, 2);
						String t2=columnsObject1.getString("title").substring(2, 4);
						cell.setCellValue(new HSSFRichTextString(t1+"\n"+t2));
					}else{
						
						cell.setCellValue(columnsObject1.getString("title"));
					}
				}
			}else if(i==2){
				JSONArray columnsArray2 = columnsObject.getJSONArray("children");
				cell = row.createCell((digit0+2)); 
				cell.setCellValue(columnsObject.getString("title"));
				cell.setCellStyle(headStyleBBlue);
				for(int j=0;j<digit;j++){
					cell = row.createCell((digit0+2)+j+1); 
					cell.setCellStyle(headStyleBBlue);
				}
				sheet.addMergedRegion(new CellRangeAddress(2,2,(digit0+2),(digit0+2)+digit));
				for(int j=0;j<columnsArray2.size();j++){
					JSONObject columnsObject1 = columnsArray2.getJSONObject(j); 
					int y=digit0+2;
					cell = row1.createCell(y+j); 
					
					if(j==0){
						cell.setCellStyle(headChildFirstStyleBBlue);
					}else{
						cell.setCellStyle(headChildStyleBBlue);
					}
					if(j==feed1 ||j==feed2){
						String t1=columnsObject1.getString("title").substring(0, 2);
						String t2=columnsObject1.getString("title").substring(2, 4);
						cell.setCellValue(new HSSFRichTextString(t1+"\n"+t2));
					}else{
						
						cell.setCellValue(columnsObject1.getString("title"));
					}
				}
				
			}else{
				JSONArray columnsArray3 = columnsObject.getJSONArray("children");
				cell = row.createCell((i-2)*(digit+1)+(digit0+2)); 
				cell.setCellValue(columnsObject.getString("title"));
				cell.setCellStyle(headStyleSBlue);
				for(int j=0;j<digit;j++){
					cell = row.createCell((i-2)*(digit+1)+(digit0+2)+j+1); 
					cell.setCellStyle(headStyleSBlue);
				}
				sheet.addMergedRegion(new CellRangeAddress(2,2,(i-2)*(digit+1)+(digit0+2),(digit0+2)+(i-2)*(digit+1)+digit));
				for(int j=0;j<columnsArray3.size();j++){
					JSONObject columnsObject1 = columnsArray3.getJSONObject(j); 
					int y=(i-2)*(digit+1)+(digit0+2);
					cell = row1.createCell(y+j); 
					if(j==0){
						cell.setCellStyle(headChildFirstStyleSBlue);
					}else{
						cell.setCellStyle(headChildStyleSBlue);
					}
					
					if(j==feed1 ||j==feed2){
						String t1=columnsObject1.getString("title").substring(0, 2);
						String t2=columnsObject1.getString("title").substring(2, 4);
						cell.setCellValue(new HSSFRichTextString(t1+"\n"+t2));
					}else{
						
						cell.setCellValue(columnsObject1.getString("title"));
					}
				}
			}
		}
		
		CellStyle DataStyleRed = styleUtil.getDataRedStyle(wb);
		CellStyle DataStyleBlack = styleUtil.getDataBlackStyle(wb);
		CellStyle DataStyle = styleUtil.getDataStyle(wb);
		CellStyle totalDataStyle = styleUtil.getTotalDataStyle(wb);
		CellStyle totalDataStyleRed = styleUtil.getTotalDataRedStyle(wb);
		CellStyle totalDataStyleBlack = styleUtil.getTotalDataBlackStyle(wb);
		//数据
		for(int i=0;i<dataArray.size();i++){
			JSONArray dataObject = dataArray.getJSONArray(i);
			row = sheet.createRow(rowIndex++);
			if(i==dataArray.size()-1){
				
				for(int j=0;j<dataObject.size();j++){
					cell = row.createCell(j); 
					String data=null;
					if(j==0){
						JSONObject dataFirstObject =dataObject.getJSONObject(j);
						data=dataFirstObject.getString("text");
					}else{
						data=dataObject.getString(j);
					}
					if(data.indexOf("%")!=-1){
						double number=Double.parseDouble(data.replace("%", ""))/100;
						if(number<0){
							cell.setCellValue(number);
							cell.setCellStyle(totalDataStyleRed);
						}else{
							cell.setCellValue(number);
							cell.setCellStyle(totalDataStyleBlack);
						}
						
					}else{
						cell.setCellStyle(totalDataStyle);
						cell.setCellValue(data);
					}
					
				}

			}else{
				
				for(int j=0;j<dataObject.size();j++){
					cell = row.createCell(j); 
					String data=null;
					if(j==0){
						JSONObject dataFirstObject =dataObject.getJSONObject(j);
						data=dataFirstObject.getString("text");
					}else{
						data=dataObject.getString(j);
					}
					if(data.indexOf("%")!=-1){
						double number=Double.parseDouble(data.replace("%", ""))/100;
						if(number<0){
							cell.setCellValue(number);
							cell.setCellStyle(DataStyleRed);
						}else{
							cell.setCellValue(number);
							cell.setCellStyle(DataStyleBlack);
						}
						
					}else{
						cell.setCellStyle(DataStyle);
						cell.setCellValue(data);
					}
					
				}
			}
		}
		
		
		
		
		return wb;
	}
	
	
	private String getModelDBSql(List<ModelDB> modelDB) {
		
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<modelDB.size();i++){
			String id = modelDB.get(i).getId();
			String result = modelDB.get(i).getResult();
			String symbol = "";
			if(i!=modelDB.size()-1){
				symbol=" , ";
			}
					
			sb.append(result+" as"
					+"(select distinct mm.fsort,p.segment,"
					+ id +" as id,"
       +" to_char(round(mr.bqSales,0))as bqSales,"
       +" trim(to_char(round((nvl(mr.bqSales,0) - mr.tqSales) / mr.tqSales * 100,1),'99999990.9')) as growth,"
       +" trim(to_char(round(mm.bqMix*100,1),'99990.9')) as bqMix,"
       +" case "
       +" when mm.bqMix is null then trim(to_char(round((nvl(mm.bqMix,0) - mm.tqMix) * 100, 1),'99990.9'))"
       +" when mm.tqMix is null then trim(to_char(round((mm.bqMix - nvl(mm.tqMix,0)) * 100, 1),'99990.9'))"
       +" else trim(to_char(round((mm.bqMix - mm.tqMix) * 100, 1),'99990.9'))"
       +" end as mixChange,"
       
       +" to_char(round(dr.bqSales,0))as baseBqSales,"
       +" trim(to_char(round((nvl(dr.bqSales,0) - tr.tqSales) / tr.tqSales * 100,1),'99999990.9')) as baseGrowth,"
       +" trim(to_char(round(dm.bqMix*100,1),'99990.9')) as baseBqMix,"
       +" case "
       +" when dm.bqMix is null then trim(to_char(round((nvl(dm.bqMix,0) - dm.tqMix) * 100, 1),'99990.9'))"
       +" when dm.tqMix is null then trim(to_char(round((dm.bqMix - nvl(dm.tqMix,0)) * 100, 1),'99990.9'))"
       +" else trim(to_char(round((dm.bqMix - dm.tqMix) * 100, 1),'99990.9'))"
       +" end as baseMixChange,"
       +" trim(to_char(round(ms.bqShare*100,1),'99990.9')) as baseModelShare,"
       +" case "
       +" when ms.bqShare is null then trim(to_char(round((nvl(ms.bqShare,0) - ms.tqShare) * 100, 1),'99990.9'))"
       +" when ms.tqShare is null then trim(to_char(round((ms.bqShare - nvl(ms.tqShare,0)) * 100, 1),'99990.9'))"
       +" else trim(to_char(round((ms.bqShare - ms.tqShare) * 100, 1),'99990.9'))"
       +" end as baseShareChange,"
       
       +" to_char(round(dc.bqSales,0))as compBqSales,"
       +" trim(to_char(round((nvl(dc.bqSales,0) - tc.tqSales) / tc.tqSales * 100,1),'99999990.9')) as compGrowth,"
       +" trim(to_char(round(cc.bqMix*100,1),'99990.9')) as compBqMix,"
       +" case "
       +" when cc.bqMix is null then trim(to_char(round((nvl(cc.bqMix,0) - cc.tqMix) * 100, 1),'99990.9'))"
       +" when cc.tqMix is null then trim(to_char(round((cc.bqMix - nvl(cc.tqMix,0)) * 100, 1),'99990.9'))"
       +" else trim(to_char(round((cc.bqMix - cc.tqMix) * 100, 1),'99990.9'))"
       +" end as compMixChange,"
       +" trim(to_char(round(mc.bqShare*100,1),'99990.9')) as compModelShare,"
       +" case "
       +" when mc.bqShare is null then trim(to_char(round((nvl(mc.bqShare,0) - mc.tqShare) * 100, 1), '99990.9'))"
       +" when mc.tqShare is null then trim(to_char(round((mc.bqShare - nvl(mc.tqShare,0)) * 100, 1), '99990.9'))"
       +" else trim(to_char(round((mc.bqShare - mc.tqShare) * 100, 1), '99990.9'))"
       +" end as compShareChange "   
       
       +" from marketMix mm"
       +" left join marketRange mr on mm.fsort=mr.fsort"
       +" left join modelRange dr on dr.fsort=mr.fsort"
       +" left join tqModelRange tr on dr.fsort=tr.fsort"
       +" left join modelMix dm on dm.fsort=dr.fsort"
       +" left join pre p on mm.fsort=p.fsort"
       +" left join modelShare ms on ms.fsort=p.fsort"
       +" left join compModelRange dc on dc.fsort=p.fsort and dc.id="+id
       +" left join compTqModelRange tc on tc.fsort=p.fsort and tc.id="+id
       +" left join compModelMix cc on cc.fsort=p.fsort and cc.id="+id
       +" left join compModelShare mc on mc.fsort=p.fsort and mc.id="+id
       +" where mm.fsort is not null )"+symbol
					);
		}
		
		
		for(int i=0;i<modelDB.size();i++){
			String id = modelDB.get(i).getId();
			String result = modelDB.get(i).getResult();
			String symbol = "";
			if(i!=modelDB.size()-1){
				symbol=" union all ";
			}
			sb.append(
					"select distinct r.fsort,r.segment,r.id,"
					   +"nvl(r.bqSales,'-') as bqSales,"
					   +"nvl2(r.growth,r.growth||'%','-') as growth,"
				       +"nvl2(r.bqMix,r.bqMix||'%','-') as bqMix,"
				       +"nvl2(r.mixChange,r.mixChange||'%','-') as mixChange,"
				       
				       +"nvl(r.baseBqSales,'-') as baseBqSales,"
				       +"nvl2(r.baseGrowth,r.baseGrowth||'%','-') as baseGrowth,"
				       +"nvl2(r.baseBqMix,r.baseBqMix||'%','-') as baseBqMix,"
				       +"nvl2(r.baseMixChange,r.baseMixChange||'%','-') as baseMixChange,"
				       +"nvl2(r.baseModelShare,r.baseModelShare||'%','-') as baseModelShare,"
				       +"nvl2(r.baseShareChange,r.baseShareChange||'%','-') as baseShareChange,"
				         
				       +"nvl(r.compBqSales,'-') as compBqSales,"
				       +"nvl2(r.compGrowth,r.compGrowth||'%','-') as compGrowth,"
				       +"nvl2(r.compBqMix,r.compBqMix||'%','-') as compBqMix,"
				       +"nvl2(r.compMixChange,r.compMixChange||'%','-') as compMixChange,"
				       +"nvl2(r.compModelShare,r.compModelShare||'%','-') as compModelShare,"
				       +"nvl2(r.compShareChange,r.compShareChange||'%','-') as compShareChange" 
				  +" from "+result +" r" +symbol  
					
					);
			
		}
		
		return sb.toString();
	}
	
	
	
	
	
}
