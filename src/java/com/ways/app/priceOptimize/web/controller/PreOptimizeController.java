package com.ways.app.priceOptimize.web.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ways.app.priceOptimize.entity.DateValue;
import com.ways.app.priceOptimize.entity.ModelBase;
import com.ways.app.priceOptimize.entity.ModelComp;
import com.ways.app.priceOptimize.entity.SalesData;
import com.ways.app.priceOptimize.service.PreOptimizeService;
import com.ways.waysdata.utils.ChartsUtil;


@SuppressWarnings({ "unchecked", "rawtypes" })
@Controller
@RequestMapping("/preOptimize")
public class PreOptimizeController {

	@Autowired
	private PreOptimizeService preOptimizeService;
	
	
	
	
	private HashMap getParamsMap(HttpServletRequest request) {

		HashMap map = new HashMap();

		return map;
	}
	
	/**
	 * 获取时间控件
	 * @param request
	 * @param response
	 * @author huangkaisheng
	 * @return
	 */
	@RequestMapping("/getDate.do")
	@ResponseBody
	public Object getDate(HttpServletRequest request, HttpServletResponse response) {
		
		
		HashMap map = getParamsMap(request);
		
		Map result = new HashMap();
		
		List<DateValue>  list= preOptimizeService.getDate(request, map);
		
		result.put("data", list);
		
		
		return result;


	}
	/**
	 * 获取本品车型
	 * @param request
	 * @param response
	 * @author huangkaisheng
	 * @return
	 */
	@RequestMapping("/getBaseModel.do")
	@ResponseBody
	public Object getBaseModel(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap map = getParamsMap(request);
		
		Map result = new HashMap();
		
		List<ModelBase>  list= preOptimizeService.getBaseModel(request, map);
		
		result.put("data", list);
		
		return result;
	}
	/**
	 * 获取竞品车型
	 * @param request
	 * @param response
	 * @author huangkaisheng
	 * @return
	 */
	@RequestMapping("/getCompModel.do")
	@ResponseBody
	public Object getCompModel(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap map = getParamsMap(request);
		
		Map result = new HashMap();
		
		List<ModelComp>  list= preOptimizeService.getCompModel(request, map);
		
		result.put("data", list);
		
		return result;
	}
	
	/**
	 * 获取销量价格段分布
	 * @param request
	 * @param response
	 * @author huangkaisheng
	 * @return
	 */
	@RequestMapping("/getSalesDistribute.do")
	@ResponseBody
	public Object getSalesDistribute(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap<String,String> map = getParamsMap(request);
		
		String json = "";
		json = preOptimizeService.getSalesDistribute(request, map);
		
		map.put("exportDatas", json);
		request.getSession().setAttribute("EXPORT_DATA", map);
		
		ChartsUtil.renderJSON(response,json);
		return null;
	}
	
	/**
	 * 下载表格
	 * @param request
	 * @param response
	 * @author huangkaisheng
	 */
	@RequestMapping("/exportTableData.do")
	public void exportTableData(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> params = null;
		
		params = (Map<String, Object>) request.getSession().getAttribute("EXPORT_DATA");
		
		
		Workbook wb = null;
		String excelName = null;
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			excelName = java.net.URLEncoder.encode("价格段销量分布", "UTF-8");
			wb = preOptimizeService.exportTableData(request, params);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+excelName+".xlsx" );  
			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
