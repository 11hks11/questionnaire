package com.ways.app.module.web.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ways.app.common.utils.AppFrameworkUtil;
import com.ways.app.common.utils.Constant;
import com.ways.app.module.service.IPriceSalesCommonService;
import com.ways.app.module.service.IPriceSalesOfTopService;
import com.ways.app.module.service.IWholeMarketService;
import com.ways.app.module.utils.PriceSalesUtils;
import com.ways.auth.model.AuthConstants;
import com.ways.auth.model.MyUserDetails;
import com.ways.framework.utils.LocaleMessageUtil;
import com.ways.waysdata.log.service.ILogManager;
import com.ways.waysdata.utils.ChartsUtil;
/**
 * 价格销量
 * @author hujianrong
 *
 */
@SuppressWarnings({"unchecked","unused"})
@Controller
public class PriceSalesMainController {
	@Autowired
	private IPriceSalesOfTopService priceSalesOfTopService;
	
	@Autowired
	private IPriceSalesCommonService priceSalesCommonService;
	
	@Autowired
	private IWholeMarketService marketService;
	
	@Autowired
	private ILogManager logManager;
	
	/**
	 * 页面跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/priceSalesMain")
	public ModelAndView priceMain(HttpServletRequest request,HttpServletResponse response){
		Model model = new ExtendedModelMap();
		Map<String, String> map =new HashMap();
		MyUserDetails userDetails = (MyUserDetails)request.getSession().getAttribute(AuthConstants.CURR_USER_DETAILS);
		map.put("lang", LocaleMessageUtil.getCurrentDBLanguageSuffix());//zh,en
		map.put("unitId", AppFrameworkUtil.getUnitId(request));//机构ID
		map.put("roleId", userDetails.getDataRoleId());//角色ID
		try {
			logManager.saveModuleDetail(request, "402881996109534101612bfd5ec10005");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("/priceSales/priceSalesMain", model.asMap());
	}
	
	/**
	 * 获取时间
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getDate")
	public void getDate(HttpServletRequest request,HttpServletResponse response){
		String json = priceSalesCommonService.getDate(request, null);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 获取城市
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getCity")
	public void getCity(HttpServletRequest request,HttpServletResponse response){
		String json = priceSalesCommonService.getCity(request, null);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 获取细分市场
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getSegment")
	public void getSegment(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> param = new HashMap<String,Object>();
		String tabType = request.getParameter("tabType");//用于判断是tab页请求（默认选中所有一级细分市场）还是objectType 维度请求（默认选中整体市场）
		param.put("tabType", tabType);
		
		if(StringUtils.isNotBlank(tabType)){
			param.put("defaultGradeIds", Constant.PRICE_DEFAULT_GRADE_IDS);
		}
		String objectType = request.getParameter("objectType");//-1、整体市场；1、细分市场；2、厂商，3、品牌，4、厂商品牌，5、车型
		String valIds = request.getParameter("valIds");//上面维度的弹出框选择值
		String cityIds = request.getParameter("cityIds");//-1 全国
		String bodyType = request.getParameter("bodyType");//车身形式
		String poo = request.getParameter("poo");//产地属性
		String original = request.getParameter("original");//系别
		String date = request.getParameter("date");//时间数组，4个表示有对比月
		if(StringUtils.isNotBlank(cityIds)){
			if(cityIds.indexOf("a") != -1){
				param.put("cityType", "dist");
				param.put("cityId", cityIds.replace("a_", ""));
			}else if(cityIds.indexOf("p") != -1){
				param.put("cityType", "prov");
				param.put("cityId", cityIds.replace("p_", ""));
			}else if(cityIds.indexOf("c") != -1){
				param.put("cityType", "city");
				param.put("cityId", cityIds.replace("c_", ""));
			}else{
				param.put("cityType", "state");
			}
		}else{
			param.put("cityType", "state");
		}
		if(StringUtils.isNotBlank(date)){
			String[] dates = date.split(",");
			String startYm = dates[0];
			String[] startYmArr = startYm.split("-");
			String endYm = dates[1]; 
			String[] endYmArr = endYm.split("-");
			param.put("startYm", startYmArr[0]+(startYmArr[1].length()==1?"0"+startYmArr[1]:startYmArr[1]));
			param.put("endYm", endYmArr[0]+(endYmArr[1].length()==1?"0"+endYmArr[1]:endYmArr[1]));
		}
		param.put("bodyType", bodyType);
		param.put("pooAttribute", poo);
		param.put("original", original);
		if("1".equals(objectType)){
			param.put("subGradeIds", "-1".equals(valIds)?"":valIds);
		}
		String json = priceSalesCommonService.getSegment(request, param);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 获取厂商
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getManf")
	public void getManf(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> param = this.getParams(request);
//		String objectType = request.getParameter("objectType");//-1、整体市场；1、细分市场；2、厂商，3、品牌，4、厂商品牌，5、车型
//		if("-1".equals(objectType)){
//			param.put("manfIds", Constant.PRICE_WHOLEMARKET_DEFAULT_MANF);
//		}else if("1".equals(objectType)){
//			param.put("manfIds", Constant.PRICE_GRADE_DEFAULT_MANF);
//		}else{
//			param.put("manfIds", Constant.PRICE_DEFAULT_MANF);
//		}
		String json = priceSalesCommonService.getManf(request, param);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 获取品牌
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getBrand")
	public void getBrand(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> param = new HashMap<String,Object>();
		String objectType = request.getParameter("objectType");//-1、整体市场；1、细分市场；2、厂商，3、品牌，4、厂商品牌，5、车型
		String valIds = request.getParameter("valIds");//上面维度的弹出框选择值
		String cityIds = request.getParameter("cityIds");//-1 全国
		String bodyType = request.getParameter("bodyType");//车身形式
		String poo = request.getParameter("poo");//产地属性
		String original = request.getParameter("original");//系别
		String date = request.getParameter("date");//时间数组，4个表示有对比月
		if(StringUtils.isNotBlank(cityIds)){
			if(cityIds.indexOf("a") != -1){
				param.put("cityType", "dist");
				param.put("cityId", cityIds.replace("a_", ""));
			}else if(cityIds.indexOf("p") != -1){
				param.put("cityType", "prov");
				param.put("cityId", cityIds.replace("p_", ""));
			}else if(cityIds.indexOf("c") != -1){
				param.put("cityType", "city");
				param.put("cityId", cityIds.replace("c_", ""));
			}else{
				param.put("cityType", "state");
			}
		}else{
			param.put("cityType", "state");
		}
		if(StringUtils.isNotBlank(date)){
			String[] dates = date.split(",");
			String startYm = dates[0];
			String[] startYmArr = startYm.split("-");
			String endYm = dates[1]; 
			String[] endYmArr = endYm.split("-");
			param.put("startYm", startYmArr[0]+(startYmArr[1].length()==1?"0"+startYmArr[1]:startYmArr[1]));
			param.put("endYm", endYmArr[0]+(endYmArr[1].length()==1?"0"+endYmArr[1]:endYmArr[1]));
		}
		param.put("bodyType", bodyType);
		param.put("pooAttribute", poo);
		param.put("original", original);
		if("1".equals(objectType)){
			param.put("subGradeIds", "-1".equals(valIds)?"":valIds);
		}
		String json = priceSalesCommonService.getBrand(request, param);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 获取厂商品牌
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getManfBrand")
	public void getManfBrand(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> param = this.getParams(request);
//		String objectType = request.getParameter("objectType");//-1、整体市场；1、细分市场；2、厂商，3、品牌，4、厂商品牌，5、车型
//		if("-1".equals(objectType)){
//			param.put("manfBrandIds", Constant.PRICE_WHOLEMARKET_DEFAULT_MANFBRAND);
//		}else if("1".equals(objectType)){
//			param.put("manfBrandIds", Constant.PRICE_GRADE_DEFAULT_MANFBRAND);
//		}else{
//			param.put("manfBrandIds", Constant.PRICE_DEFAULT_MANFBRAND);
//		}
		String json = priceSalesCommonService.getManfBrand(request, param);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 获取车型
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getModel")
	public void getModel(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> param = new HashMap<String,Object>();
		String objectType = request.getParameter("objectType");//-1、整体市场；1、细分市场；2、厂商，3、品牌，4、厂商品牌，5、车型
		String valIds = request.getParameter("valIds");//上面维度的弹出框选择值
		String tabType = request.getParameter("tabType");
		String drill = request.getParameter("drill");
		String cityIds = request.getParameter("cityIds");//-1 全国
		String date = request.getParameter("date");//时间数组，4个表示有对比月
		if(StringUtils.isNotBlank(date)){
			String[] dates = date.split(",");
			String startYm = dates[0];
			String[] startYmArr = startYm.split("-");
			String endYm = dates[1]; 
			String[] endYmArr = endYm.split("-");
			param.put("startYm", startYmArr[0]+(startYmArr[1].length()==1?"0"+startYmArr[1]:startYmArr[1]));
			param.put("endYm", endYmArr[0]+(endYmArr[1].length()==1?"0"+endYmArr[1]:endYmArr[1]));
		}
		if(StringUtils.isNotBlank(cityIds)){
			if(cityIds.indexOf("a") != -1){
				param.put("cityType", "dist");
				param.put("cityId", cityIds.replace("a_", ""));
			}else if(cityIds.indexOf("p") != -1){
				param.put("cityType", "prov");
				param.put("cityId", cityIds.replace("p_", ""));
			}else if(cityIds.indexOf("c") != -1){
				param.put("cityType", "city");
				param.put("cityId", cityIds.replace("c_", ""));
			}else{
				param.put("cityType", "state");
			}
		}else{
			param.put("cityType", "state");
		}
		/*if("1".equals(objectType) && tabType != null){
			param.put("subModelIds", Constant.PRICE_GRADE_DEFAULT_SUBMODEL);
		}else if("2".equals(objectType) && tabType != null){
			param.put("subModelIds", Constant.PRICE_MANF_DEFAULT_SUBMODEL);
		}else if("5".equals(objectType) && tabType != null){
			param.put("subModelIds", Constant.PRICE_MODEL_DEFAULT_SUBMODEL);
		}else{
			param.put("subModelIds", "-1");
		}*/
		//tab页下面的车型选择
		if(StringUtils.isNotBlank(tabType)){
			String bodyType = request.getParameter("bodyType");//车身形式
			String poo = request.getParameter("poo");//产地属性
			String original = request.getParameter("original");//系别
			
			
			if("1".equals(objectType)){
				param.put("subGradeIds", "-1".equals(valIds)?"":valIds);
			}else if("2".equals(objectType)){
				param.put("manfIds", valIds);
			}else if("3".equals(objectType)){
				param.put("brandIds", valIds);
			}else if("4".equals(objectType)){
				param.put("manfBrandIds", valIds);
			}else if("5".equals(objectType)){
				param.put("subModelIds", valIds);
			}
			param.put("bodyType", bodyType);
			param.put("pooAttribute", poo);
			param.put("original", original);
			
			if("true".equals(drill)){
				String drillText = request.getParameter("drillText");
				if("1".equals(tabType)){
					param.put("original", drillText);
				}else if("t-7".equals(tabType)){
					param.put("carType", drillText);
				}else if("t-1".equals(tabType)){
					param.put("subGradeIds", drillText);
				}else if("t-3".equals(tabType)){
					param.put("brandIds", drillText);
				}else if("t-2".equals(tabType)){
					param.put("manfIds", drillText);
				}else if("t-4".equals(tabType)){
					param.put("manfBrandIds", drillText);
				}else if("3".equals(tabType)){
					param.put("pooAttribute", drillText);
				}else if("4".equals(tabType)){
					param.put("cityId", drillText);
				}else if("t-5".equals(tabType)){
					param.put("subModelIds", drillText);
				}
			}
			
		}else{
			param.put("subModelIds", "-1");
		}
		
		String json = priceSalesCommonService.getModel(request, param);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 获取 筛选条件
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getCondition")
	public void getCondition(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> params = this.getParams(request);
		String json = priceSalesCommonService.getCondition(request, params);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 设置echart图头部信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getEchartHeadInfo")
	public void getEchartHeadInfo(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> params = this.getParams(request);
		String json = priceSalesCommonService.getEchartHeadInfo(request, params);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 获取echart图数据 （ppt中的3）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getTopEchartData")
	public void getTopEchartData(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> params = this.getParams(request);
		String json = priceSalesOfTopService.getTopEchartData(request, params);
		request.getSession().setAttribute("priceSalesTopEchartParams", params);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 获取表格钻取的头部
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getThoroughHead")
	public void getThoroughHead(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> params = this.getParams(request);
		String json = priceSalesCommonService.getThoroughHead(request,params);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 获取表格钻取的内容
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/getEchartThoroughTableData")
	public void getEchartThoroughTableData(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> params = this.getParams(request);
		String json = priceSalesOfTopService.getEchartThoroughTableData(request,params);
		request.getSession().setAttribute("priceSalesThoroughEchartParams", params);
		ChartsUtil.renderJSON(response, json);
	}
	
	/**
	 * 图表下载
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/exportEchartData")
	public void exportEchartData(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> params = (Map<String, Object>) request.getSession().getAttribute("priceSalesTopEchartParams");
		Workbook wb = null;
		String excelName = null;
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			excelName = java.net.URLEncoder.encode("价格段销量分析", "UTF-8");
			wb = priceSalesOfTopService.exportEchartData(request, params);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+excelName+".xlsx" );  
			wb.write(out);
		} catch (IOException e) {
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
	
	/**
	 * 下载 表格钻取内容
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/exportThoroughTableData")
	public void exportThoroughTableData(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> params = (Map<String, Object>) request.getSession().getAttribute("priceSalesThoroughEchartParams");
		Workbook wb = null;
		String excelName = null;
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			excelName = java.net.URLEncoder.encode("价格段销量分析", "UTF-8");
			wb = priceSalesOfTopService.exportThoroughTableData(request, params);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+excelName+".xlsx" );  
			wb.write(out);
		} catch (IOException e) {
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
	
	/**
	 * 下载底部表格内容
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/priceSales/exportBottomTableData")
	public void exportBottomTableData(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> params = null;
		String exportDrill = request.getParameter("exportDrill");
		if(exportDrill != null && "true".equals(exportDrill)){
			params = (Map<String, Object>) request.getSession().getAttribute("DRILL_EXPROTBOTTOMTABLEDATA");
		}else{
			params = (Map<String, Object>) request.getSession().getAttribute("EXPROTBOTTOMTABLEDATA");
		}
		Workbook wb = null;
		String excelName = null;
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			excelName = java.net.URLEncoder.encode("价格段销量分析", "UTF-8");
			wb = marketService.exportBottomTableData(request, params);
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
	
	/**
	 * 整体市场价格段概览
	 */
	@RequestMapping("/priceSales/getWholeMarketTotalData")
	public Object getWholeMarketTotalData(HttpServletRequest request,HttpServletResponse response){
		Map<String,String> map=getPageParams(request);
		String configJson = "";
		try {
			configJson = marketService.getWholeMarketTotalData(map);
			map.put("exportDatas", configJson);
			if(request.getParameter("drill") != null && "true".equals(request.getParameter("drill"))){
				request.getSession().setAttribute("DRILL_EXPROTBOTTOMTABLEDATA", map);
			}else{
				request.getSession().setAttribute("EXPROTBOTTOMTABLEDATA", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String valIdsSize = map.get("valIdsSize");
		String drillValIdsSize = map.get("drillValIdsSize");
		if((valIdsSize != null && Integer.parseInt(valIdsSize) > 10) 
				|| (drillValIdsSize != null && Integer.parseInt(drillValIdsSize) > 10)){//超过10个查询对象，进行下载
			ChartsUtil.renderJSON(response,"{\"error\":{\"type\":\"overLimit\"}}");
		}else{
			ChartsUtil.renderJSON(response,configJson);
		}
		
		return null;  
	}
	
	/**
	 * 获取各维度下tab页
	 */
	@RequestMapping("/priceSales/getTabInfo")
	public Object getTabInfo(HttpServletRequest request,HttpServletResponse response){
		Map<String,String> map=getPageParams(request);
		String configJson = "";
		try {
			configJson = marketService.getTabInfo(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ChartsUtil.renderJSON(response,configJson);
		return null;  
	}
	
	/**
     * 获取页面参数
     * @param request
     * @return
     */

	public Map<String, String> getPageParams(HttpServletRequest request)
	{
    	MyUserDetails userDetails = (MyUserDetails)request.getSession().getAttribute(AuthConstants.CURR_USER_DETAILS);
		Map<String, String> map = new HashMap<String, String>();
		/*String sources = "1";//1、tp,2、msrp
		String type = "2";//1、月均，2、累计
		String objectType = "2";//-1、整体市场；1、细分市场；2、厂商，3、品牌，4、厂商品牌，5、车型
		String valIds = "98";//上面维度的弹出框选择值
		String cityIds = "";//null 全国,c_194
		String date = "2017-1,2017-11,,";//时间数组，4个表示有对比月
		String dateYoY = "true";//是否对比去年同期 true:对比同期，false：不对比
		String dateShow = "false";//有无对比时间 true：有对比，false：无对比
		String bodyType = "1,82";//车身形式
		String poo = "";//产地属性
		String original = "4,5,6,7";//系别
		String priceMin = "20";
		String priceMax = "100";
		String segment = "10";
		String tabType = "t-2";//1、价格段概览;2、系别;3、汽车类型;4、细分市场;5、品牌;6、厂商;7、厂商品牌;8、产地属性;9、城市层级;10、车型;11、型号
		String tabDataType = "2";//1销量,2 MIX,3 份额
		String tabLc = "1";//1 豪华车，2 非豪华车
		String tabTime = "2017-1~2017-11";//车型下面时间参数
		String tabVersion = "";//年款车型
		String tabIds = "11";//各tab页下面的弹出框选择条件
*/
		String sources = request.getParameter("sources");//1、tp,2、msrp
		String type = request.getParameter("type");//1、月均，2、累计
		String objectType = request.getParameter("objectType");//-1、整体市场；1、细分市场；2、厂商，3、品牌，4、厂商品牌，5、车型
		String valIds = request.getParameter("valIds");//上面维度的弹出框选择值
		String cityIds = request.getParameter("cityIds");//-1 全国
		String date = request.getParameter("date");//时间数组，4个表示有对比月
		String dateYoY = request.getParameter("dateYoY");//是否对比去年同期 true:对比同期，false：不对比
		String dateShow = request.getParameter("dateShow");//有无对比时间 true：有对比，false：无对比
		String bodyType = request.getParameter("bodyType");//车身形式
		String poo = request.getParameter("poo");//产地属性
		String original = request.getParameter("original");//系别
		String priceMin = request.getParameter("priceMin");
		String priceMax = request.getParameter("priceMax");
		String segment = request.getParameter("segment");
		String customSegment = request.getParameter("customSegment");//自定义价格段
		String tabType = request.getParameter("tabType");//-1、价格段概览;1、系别;t-7、汽车类型;t-1、细分市场;t-3、品牌;t-2、厂商;t-4、厂商品牌;3、产地属性;4、城市层级;t-5、车型;t-6、型号
		String tabDataType = request.getParameter("tabDataType");//1销量,2 MIX,3 份额
		String tabLc = request.getParameter("tabLc");//1 豪华车，2 非豪华车
		String tabTime = request.getParameter("tabTime");//车型下面时间参数
		String tabVersion = request.getParameter("tabVersion");//年款车型
		String tabIds = request.getParameter("tabIds");//各tab页下面的弹出框选择条件
		
		String drillText = request.getParameter("drillText");//钻取id
		String drill = request.getParameter("drill");//是否钻取请求。true：钻取
		String drillTabIds = request.getParameter("drillTabIds");
		String drillTabDataType = request.getParameter("drillTabDataType");
		String drillTabTime = request.getParameter("drillTabTime");
		String drillTabType = request.getParameter("drillTabType");
		String drillTabVersion = request.getParameter("drillTabVersion");
		
		if(tabIds != null){
			map.put("valIdsSize", tabIds.split(",").length+"");	//记录参数数量
		}
		if(drillTabIds != null){
			map.put("drillValIdsSize", drillTabIds.split(",").length+"");	//记录参数数量
		}
		
		String tabType_1 = "";//钻取的时候根据这个来设置钻取id
		if("true".equals(drill)){//钻取的时候，这些参数改成钻取参数
			tabType_1 = tabType;
			tabIds = drillTabIds;
			tabDataType = drillTabDataType;
			tabTime = drillTabTime;
			tabType = drillTabType;
			tabVersion = drillTabVersion;
		}
		
		if(StringUtils.isNotBlank(date)){
			String[] dates = date.split(",");
			String startYm = dates[0];
			String[] startYmArr = startYm.split("-");
			String endYm = dates[1]; 
			String[] endYmArr = endYm.split("-");
			map.put("startYm", startYmArr[0]+(startYmArr[1].length()==1?"0"+startYmArr[1]:startYmArr[1]));
			map.put("endYm", endYmArr[0]+(endYmArr[1].length()==1?"0"+endYmArr[1]:endYmArr[1]));
			String startLastYm_1 = "";
			String endLastYm_1 = "";
			if("true".equals(dateShow)){
				startLastYm_1 = dates[2];
				endLastYm_1 = dates[3];
				startYmArr = dates[2].split("-");
				endYmArr = dates[3].split("-"); 
				map.put("startLastYm", startYmArr[0]+(startYmArr[1].length()==1?"0"+startYmArr[1]:startYmArr[1]));
				map.put("endLastYm", endYmArr[0]+(endYmArr[1].length()==1?"0"+endYmArr[1]:endYmArr[1]));
			//}else if("true".equals(dateYoY)){//对比去年同期，月份做了补0操作
			}else{//不管是否对比去年，数据都查询出去年的，组装的时候区分是否显示去年数据
				startLastYm_1 = Integer.parseInt(startYm.substring(0, 4))-1 +"-"+startYm.substring(5);
				endLastYm_1 = Integer.parseInt(endYm.substring(0, 4))-1 +"-"+endYm.substring(5);
				String startLastYm = Integer.parseInt(startYm.substring(0, 4))-1 +(startYm.substring(5).length()==1?"0"+startYm.substring(5):startYm.substring(5));//截取前四位年份再减一
				String endLastYm = Integer.parseInt(endYm.substring(0, 4))-1 +(endYm.substring(5).length()==1?"0"+endYm.substring(5):endYm.substring(5));
				map.put("startLastYm", startLastYm);
				map.put("endLastYm", endLastYm);
				
				//将date同期日期补全,月份没有做补0操作
				date = dates[0]+","+dates[1]+","+(Integer.parseInt(startYm.substring(0, 4))-1 +startYm.substring(4))+","+(Integer.parseInt(endYm.substring(0, 4))-1 +endYm.substring(4));
			}
			
			//算出本期月份差，供计算月均数据
			Integer monthNum = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Calendar c1 = Calendar.getInstance();
	        Calendar c2 = Calendar.getInstance();
	        try {
				c1.setTime(sdf.parse(startYm));
				c2.setTime(sdf.parse(endYm));
				monthNum = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH) + 1;
				map.put("monthNum", monthNum+"");//本期月份差
				
				c1.setTime(sdf.parse(startLastYm_1));
				c2.setTime(sdf.parse(endLastYm_1));
				Integer avgMonthNum = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH) + 1;
				map.put("avgMonthNum", avgMonthNum+"");//本期月份差
				/*if("true".equals(dateShow)){//同期月份差（可能和本期月份差不一样）
				}*/
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        
		}
		if(StringUtils.isNotBlank(cityIds)){
			if(cityIds.indexOf("a") != -1){
				map.put("cityType", "dist");
				map.put("cityId", cityIds.replace("a_", ""));
			}else if(cityIds.indexOf("p") != -1){
				map.put("cityType", "prov");
				map.put("cityId", cityIds.replace("p_", ""));
			}else if(cityIds.indexOf("c") != -1){
				map.put("cityType", "city");
				map.put("cityId", cityIds.replace("c_", ""));
			}else{
				map.put("cityType", "state");
			}
		}else{
			map.put("cityType", "state");
		}
		if(StringUtils.isNotBlank(tabTime)){
			String[] bt = tabTime.split("~");
			String[] bt_1 = bt[0].split("-");
			String[] bt_2 = bt[1].split("-");
			map.put("startYm", bt_1[0]+(bt_1[1].length()==1?"0"+bt_1[1]:bt_1[1]));
			map.put("endYm", bt_2[0]+(bt_2[1].length()==1?"0"+bt_2[1]:bt_2[1]));
		}
		//不同的维度设置不同的条件id
		/*if("-1".equals(valIds)){//为-1的时候为整体，设置为空
			valIds = "";
		}*/
		if("1".equals(objectType)){
			map.put("subGradeIds", "-1".equals(valIds)?"":valIds);
		}else if("2".equals(objectType)){
			map.put("manfIds", valIds);
			map.put("manfIds_sort", ","+valIds+",");
		}else if("3".equals(objectType)){
			map.put("brandIds", valIds);
			map.put("brandIds_sort", ","+valIds+",");
		}else if("4".equals(objectType)){
			map.put("manfBrandIds", valIds);
			map.put("manfBrandIds_sort", ","+valIds+",");
		}else if("5".equals(objectType)){
			map.put("subModelIds", valIds);
			map.put("subModelIds_sort", ","+valIds+",");
		}
		
		//设置价格段概览数据查询条件(区分左右两边数据)
		if("1".equals(objectType)){
			map.put("subGradeIds_all", "-1".equals(valIds)?"":valIds);
		}else if("2".equals(objectType)){
			map.put("manfIds_all", valIds);
		}else if("3".equals(objectType)){
			map.put("brandIds_all", valIds);
		}else if("4".equals(objectType)){
			map.put("manfBrandIds_all", valIds);
		}else if("5".equals(objectType)){
			map.put("subModelIds_all", valIds);
		}
		
		//不同tab页下面不同的条件(tabIds不为空时进行设置，如果为空，会覆盖全局的查询条件)
		if(StringUtils.isNotBlank(tabIds)){
			if("t-1".equals(tabType)){
				map.put("subGradeIds", tabIds);
			}else if("t-3".equals(tabType)){
				map.put("brandIds", tabIds);
				map.put("brandIds_sort", ","+tabIds.trim()+",");
			}else if("t-2".equals(tabType)){
				map.put("manfIds", tabIds);
				map.put("manfIds_sort", ","+tabIds.trim()+",");
			}else if("t-4".equals(tabType)){
				map.put("manfBrandIds", tabIds);
				map.put("manfBrandIds_sort", ","+tabIds.trim()+",");
			}else if("t-5".equals(tabType)){
				map.put("subModelIds", tabIds);
				map.put("subModelIds_sort", ","+tabIds.trim()+",");
			}else if("t-6".equals(tabType)){
				map.put("subModelIds", tabIds);
				map.put("subModelIds_sort", ","+tabIds.trim()+",");
			}/*else if("3".equals(tabType)){//产地属性tab页的时候，默认查询全部产地：1,2,3
				map.put("pooAttributeIds", "1,2,3");
			}*/
		}
		if("3".equals(tabType)){//产地属性tab页的时候，默认查询全部产地：1,2,3（与上面区分开，产地属性直接设置全属性）
			map.put("pooAttributeIds", "1,2,3");
		}
		/*if("true".equals(drill)){//钻取的时候subModelIds 直接设置为空，按所有条件根据销量排序取前5
			map.put("subModelIds", "");
		}*/
		map.put("drillText", drillText);
		map.put("drill", drill);
		map.put("sources", sources);
		map.put("type", type);
		map.put("tabDataType", tabDataType);
		map.put("objectType", objectType);
		map.put("valIds", valIds);
		map.put("cityIds", cityIds);
		map.put("date", date);
		map.put("dateYoY", dateYoY);
		map.put("dateShow", dateShow);
		map.put("bodyType", bodyType);
		map.put("pooAttribute", poo != null && poo.indexOf("3") != -1 ? poo+",2" : poo);//"合资"合并到 "国产" 里
		map.put("original", original);
		map.put("minPrice", priceMin);
		map.put("maxPrice", priceMax);
		map.put("segmentPrice", StringUtils.isNotBlank(segment) ? segment : customSegment);
		map.put("tabType", tabType);
		map.put("tabLc", tabLc);
		map.put("tabTime", tabTime);
		map.put("modelYear", tabVersion);
		map.put("tabIds", tabIds);
		//钻取的时候将钻取id设置成对应的属性id
		if("true".equals(drill)){
			if("1".equals(tabType_1)){
				map.put("original", drillText);
			}else if("t-7".equals(tabType_1)){
				map.put("carType", drillText);
			}else if("t-1".equals(tabType_1)){
				map.put("subGradeIds", drillText);
			}else if("t-3".equals(tabType_1)){
				map.put("brandIds", drillText);
			}else if("t-2".equals(tabType_1)){
				map.put("manfIds", drillText);
			}else if("t-4".equals(tabType_1)){
				map.put("manfBrandIds", drillText);
			}else if("3".equals(tabType_1)){
				map.put("pooAttribute", drillText);
			}else if("4".equals(tabType_1)){
				map.put("cityId", drillText);
			}else if("t-5".equals(tabType_1)){
				map.put("subModelIds", drillText);
			}
		}
		/*if("true".equals(drill) && "t-5".equals(drillTabType)){//钻取的时候subModelIds 直接设置为空，按所有条件根据销量排序取前5
			map.put("subModelIds", "");
		}*/
		return map;
	}
	
	
	/**
	 * 获取参数
	 * @return
	 */
	private Map<String,Object> getParams(HttpServletRequest request){
		MyUserDetails userDetails = (MyUserDetails) request.getSession().getAttribute(AuthConstants.CURR_USER_DETAILS);
		Map<String, Object> params = new HashMap<String, Object>();
		
		String priceType = request.getParameter("sources");	//价格类型
		if("1".equals(priceType)){
			params.put("priceType", "tp");
		}else{
			params.put("priceType", "msrp");
		}
		
		String dataType = request.getParameter("type");//1:月均	2:累计
		if("1".equals(dataType)){
			params.put("dataType", "avg");
		}else{
			params.put("dataType", "sum");
		}
		
		String chartDataType = request.getParameter("chartDataType");
		if("1".equals(chartDataType)){
			params.put("chartDataType", "sales");
		}else{
			params.put("chartDataType", "mix");
		}
		
		//车型维度，区分不同的图
		String chartType = request.getParameter("chartType");
		if("1".equals(chartType)){
			params.put("chartType", "level");
		}else{
			params.put("chartType", "bar");
		}
		
		String cityId = request.getParameter("cityIds");
		if(cityId != null){
			if(cityId.indexOf("a") != -1){
				params.put("cityType", "dist");
				params.put("cityId", cityId.replace("a_", ""));
			}else if(cityId.indexOf("p") != -1){
				params.put("cityType", "prov");
				params.put("cityId", cityId.replace("p_", ""));
			}else if(cityId.indexOf("c") != -1){
				params.put("cityType", "city");
				params.put("cityId", cityId.replace("c_", ""));
			}else{
				params.put("cityType", "state");
			}
		}else{
			params.put("cityType", "state");
		}
		
		//变量:同一个变量名，不同值
		String objectType = request.getParameter("objectType");		//选择的对象类型
		if("1".equals(objectType)){
			params.put("objectType", "segment");
			if(request.getParameter("valIds") == null || request.getParameter("valIds").indexOf("-1") != -1
					|| request.getParameter("valIds").indexOf("-9") != -1){
				params.put("isAllSegment", "t");
			}else{
				params.put("isAllSegment", "f");
			}
		}else if("2".equals(objectType)){
			params.put("objectType", "manf");
		}else if("3".equals(objectType)){
			params.put("objectType", "brand");
		}else if("4".equals(objectType)){
			params.put("objectType", "manfBrand");
		}else{
			params.put("objectType", "model");
			String value = request.getParameter("valIds");
			String order = " order by decode(id ";
			String[] models = value.split(",");
			for(int i = 0; i < models.length; i++){
				order = order + "," + models[i] + "," + (i+1) ;
				if(i == models.length - 1){
					order = order + ")  ";
				}
			}
			params.put("modelOrder", order);
		}
		params.put("value", request.getParameter("valIds"));
		
		params.put("bodyType", request.getParameter("bodyType"));	//车身形式
		String poo = request.getParameter("poo");
		if(poo != null && poo.indexOf("3") != -1){
			poo += ",2";
		}
		params.put("portType", poo);	//产地属性
		params.put("originType", request.getParameter("original"));	//系别
		
		if(request.getParameter("priceMin") != null){
			int startPrice = Integer.parseInt(request.getParameter("priceMax"));
			int endPrice = Integer.parseInt(request.getParameter("priceMin"));
			int splitPrice = 0;
			if(request.getParameter("segment") == null || request.getParameter("segment").isEmpty()
					|| request.getParameter("segment") == "-1"){
				splitPrice = Integer.parseInt(request.getParameter("customSegment"));
			}else{
				splitPrice = Integer.parseInt(request.getParameter("segment"));
			}
			
			params.put("startPrice", startPrice);	//价格段区间——开始
			params.put("endPrice", endPrice);	//价格段区间——结束
			params.put("splitPrice", splitPrice);	//价格分档
			
			//价格段补数模板sql
			params.put("priceLevelTable", PriceSalesUtils.getPriceLevelTableSql(startPrice, endPrice, splitPrice, priceType));
			//价格段case sql
			params.put("priceLevelCase", PriceSalesUtils.getPriceLevelCaseSql(startPrice, endPrice, splitPrice, priceType));
		}
		
		//车型维度 梯度图特殊处理
		params.put("dateRange", request.getParameter("chartDate"));
		
		String compareYoy = request.getParameter("compareYoy");		//对比去年同期
		if(compareYoy != null && "1".equals(compareYoy)){
			params.put("compareYoy", "t");
		}else{
			params.put("compareYoy", "f");
		}
		
		String startDate = "";
		String endDate = "";
		String[] date = null;
		if(request.getParameter("date") != null){
			date = (request.getParameter("date")).split(",");
			startDate = date[0];
			endDate = date[1];
			if(startDate != null && endDate != null){
				String[] start = startDate.split("-");
				String[] end = endDate.split("-");
				params.put("startDate", start[0] + (start[1].length()==1?"0"+start[1]:start[1]));
				params.put("endDate", end[0] + (end[1].length()==1?"0"+end[1]:end[1]));
				params.put("startDateKey", startDate + "~" + endDate);
			}
		}
		
		String dateShow = request.getParameter("dateShow");		//是否自定义对比时间
		String dateYoY = request.getParameter("dateYoY");		//是否对比去年同期
		if("true".equals(dateShow) || "true".equals(dateYoY)){		//有对比时间
			params.put("isVsTime", "t");
			
			//有对比时间，对比自定义时间 (直接拿)
			if("true".equals(dateShow)){
				startDate = date[2];
				endDate = date[3];
				if(startDate != null && endDate != null){
					String[] start = startDate.split("-");
					String[] end = endDate.split("-");
					params.put("endStartDate", start[0] + (start[1].length()==1?"0"+start[1]:start[1]));
					params.put("endEndDate", end[0] + (end[1].length()==1?"0"+end[1]:end[1]));
					params.put("endDateKey", startDate + "~" + endDate);
					
					//获取前一年时间（用于点击钻取第去年时，没有前年时间）
					params.put("grandStartDate", (Integer.parseInt(start[0])-1) + (start[1].length()==1?"0"+start[1]:start[1]));
					params.put("grandEndDate", (Integer.parseInt(end[0])-1) + (end[1].length()==1?"0"+end[1]:end[1]));
					params.put("grandDateKey", ((Integer.parseInt(start[0])-1) + "-" + start[1]) + "~" + 
							((Integer.parseInt(end[0])-1) + "-" + end[1]));
				}
			}else{
				//有时间对比，对比去年同期(自己算)
				String[] start = startDate.split("-");
				start[0] = (Integer.parseInt(start[0]) - 1) + "";
				String[] end = endDate.split("-");
				end[0] = (Integer.parseInt(end[0]) - 1) + "";
				params.put("endStartDate", start[0] + (start[1].length()==1?"0"+start[1]:start[1]));
				params.put("endEndDate", end[0] + (end[1].length()==1?"0"+end[1]:end[1]));
				params.put("endDateKey", (start[0] + "-" + start[1]) + "~" + (end[0] + "-" + end[1]));
				
				//获取前一年时间（用于点击钻取第去年时，没有前年时间）
				start[0] = (Integer.parseInt(start[0]) - 1) + "";
				end[0] = (Integer.parseInt(end[0]) - 1) + "";
				params.put("grandStartDate", start[0] + (start[1].length()==1?"0"+start[1]:start[1]));
				params.put("grandEndDate", end[0] + (end[1].length()==1?"0"+end[1]:end[1]));
				params.put("grandDateKey", (start[0] + "-" + start[1]) + "~" + (end[0] + "-" + end[1]));
			}
		}else{
			params.put("isVsTime", "f");
			
			if(!startDate.isEmpty()){
				//获取前一年时间（用于点击钻取第去年时，没有前年时间）
				String[] start = startDate.split("-");
				start[0] = (Integer.parseInt(start[0]) - 1) + "";
				String[] end = endDate.split("-");
				end[0] = (Integer.parseInt(end[0]) - 1) + "";
				params.put("grandStartDate", start[0] + (start[1].length()==1?"0"+start[1]:start[1]));
				params.put("grandEndDate", end[0] + (end[1].length()==1?"0"+end[1]:end[1]));
				params.put("grandDateKey", (start[0] + "-" + start[1]) + "~" + (end[0] + "-" + end[1]));
			}
		}
		
		params.put("seriesName", request.getParameter("seriesName"));
		String priceName = request.getParameter("name");
		if(priceName != null){
			priceName = priceName.replace("&lt;", "<");
			params.put("priceName", priceName);
		}
		
		if(request.getParameter("echartTablePreNum") != null){
			//获取钻取数据的开始排序
//			params.put("thoroughNo", Integer.parseInt(request.getParameter("thoroughNo"))+1);
			
			//替换时间，设置销量
			String seriesIndex = request.getParameter("seriesIndex");
			String salesData = request.getParameter("salesData");
			if(salesData != null && seriesIndex != null){
				String[] sales = salesData.split(",");
				if("true".equals(dateShow) || "true".equals(dateYoY)){		//有对比时间
					params.put("isVsTime", "t");
					if("0".equals(seriesIndex)){
						//钻取今年，双时间段下不变
						//设置销量
						params.put("allShow", sales[0]);
						if(sales.length >= 2){
							params.put("vsAllShow", sales[1]);
						}
					}else{
						//去年为开始，去年变今年，前年变去年
						params.put("allShow", sales[1]);
						if(sales.length == 2){
							params.put("vsAllShow", sales[2]);
						}
						//设置时间
						params.put("startDate", params.get("endStartDate"));
						params.put("endDate", params.get("endEndDate"));
						params.put("endStartDate", params.get("grandStartDate"));
						params.put("endEndDate", params.get("grandEndDate"));
					}
				}else{
					//单时间段
					//设置销量
					params.put("isVsTime", "t");
					params.put("allShow", sales[0]);
					if(sales.length == 2){
						params.put("vsAllShow", sales[1]);
					}else{
						params.put("isVsTime", "f");
					}
					//设置时间
					params.put("endStartDate", params.get("grandStartDate"));
					params.put("endEndDate", params.get("grandEndDate"));
				}
			}
			
			
			
			//变量:同一个变量名，不同值
			String thoroughObjectType = request.getParameter("echartTablePreNum");		//选择钻取对象类型
			if("segment".equals(thoroughObjectType)){
				params.put("thoroughObjectType", "segment");
				params.put("tableHeadName", "细分市场");
			}else if("manf".equals(thoroughObjectType)){
				params.put("thoroughObjectType", "manf");
				params.put("tableHeadName", "厂商");
			}else if("brand".equals(thoroughObjectType)){
				params.put("thoroughObjectType", "brand");
				params.put("tableHeadName", "品牌");
			}else if("manfBrand".equals(thoroughObjectType)){
				params.put("thoroughObjectType", "manfBrand");
				params.put("tableHeadName", "厂商品牌");
			}else if("orig".equals(thoroughObjectType)){
				params.put("thoroughObjectType", "orig");
				params.put("tableHeadName", "系别");
			}else if("carType".equals(thoroughObjectType)){
				params.put("thoroughObjectType", "carType");
				params.put("tableHeadName", "汽车类型");
			}else if("poo".equals(thoroughObjectType)){
				params.put("thoroughObjectType", "pooAttr");
				params.put("tableHeadName", "产地属性");
			}else if("city".equals(thoroughObjectType)){
				params.put("thoroughObjectType", "city");
				params.put("tableHeadName", "城市层级");
			}else if("model".equals(thoroughObjectType)){
				params.put("thoroughObjectType", "model");
				params.put("tableHeadName", "车型");
			}
		}
		
		//将（MSRP≥44万）或者（39万≤MSRP<44万） 解析成    （ and x.msrp >=440000 ） 或 （ and 390000<= x.msrp and  x.msrp <440000 ）
		String priceRange = priceName;
		if(priceRange != null){
			params.put("sourcePriceRange", priceRange);		//用于导出
			String thoroughPrice = priceRange.indexOf("TP")!=-1?"tp":"msrp";
			String price = priceRange.indexOf("TP")!=-1?"TP":"MSRP";
			params.put("thoroughPrice", thoroughPrice);
			
			priceRange = priceRange.replace("万", "");
			//正则添加0000有bug
//			Pattern pattern = Pattern.compile("\\d+");
//			Matcher matcher = pattern.matcher(priceRange);
//			
//			List<String> list = new ArrayList();
//			if(matcher.find()){
//				String str = matcher.group(0);
//				priceRange = priceRange.replace(str, str+"0000");
//			}
			
			char[] ss = priceRange.toCharArray();
			String n1 = "";
			String n2 = "";
			String n = "";
			boolean flag = true;
			for(char c:ss){
				if(Character.isDigit(c)){
					if(flag){
						n1 += c;
					}else{
						n2 += c;
					}
				}else{
					flag = false;
					n += c;
				}
			}
			n1 = n1.isEmpty()?"":n1+"0000";
			n2 = n2.isEmpty()?"":n2+"0000";
			priceRange = n1 + n + n2;
			
			if(priceRange.indexOf("MSRP") != -1){
				if(priceRange.split("MSRP").length == 1){
					priceRange = " and " + priceRange.replace("MSRP", "x."+thoroughPrice);
				}else{
					String[] s = priceRange.split("MSRP");
					if(s[0] != null && s[0].length() > 0){
						priceRange = " and " + s[0] + " x.msrp and  x.msrp " + s[1] + " ";
					}else{
						priceRange = " and x.msrp " + s[1] + " ";
					}
				}
			}else{
				if(priceRange.split("TP").length == 1){
					priceRange = " and " + priceRange.replace("TP", "x."+thoroughPrice);
				}else{
					String[] s = priceRange.split("TP");
					if(s[0] != null && s[0].length() > 0){
						priceRange = " and " + s[0] + " x.tp and  x.tp " + s[1] + " ";
					}else{
						priceRange = " and x.tp " + s[1] + " ";
					}
					
				}
			}
			
			priceRange = priceRange.replace("≤", "<=");
			priceRange = priceRange.replace("≥", ">=");
			
			params.put("priceRange", priceRange);
		}
		
		
		params.put("unitId", AppFrameworkUtil.getUnitId(request));//机构ID
		params.put("roleId", userDetails.getDataRoleId());//角色ID
		
		return params;
	}
	
}
