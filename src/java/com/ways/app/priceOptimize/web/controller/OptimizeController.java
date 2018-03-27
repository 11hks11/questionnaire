package com.ways.app.priceOptimize.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ways.app.priceOptimize.entity.MyVersion;
import com.ways.app.priceOptimize.entity.PriceRange;
import com.ways.app.priceOptimize.entity.SubModel;
import com.ways.app.priceOptimize.service.OptimizeService;
import com.ways.auth.model.AuthConstants;
import com.ways.auth.model.MyUserDetails;

@Controller
@RequestMapping(value="/optimize")
public class OptimizeController {

	@Autowired
	private OptimizeService optimizeService;
	
	/**
	 * 获取对标车型
	 * @return
	 *  时间：time 
		最小价格：minPrice 
		最大价格：maxPrice 
		竞品车型id：vsModelIds 
		细分市场id：marketId
	 * @author liuyuhuan
	 * @date 2018-2-5
	 * http://10.11.2.190:8080/gmtt_priceSales/optimize/getComparisonModel.do?time=2017-9&minPrice=450000&maxPrice=500000&vsModelIds=445,446&myModelIds=445&marketId=80
	 */
	@RequestMapping(value="/getComparisonModel.do",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getComparisonModel(HttpServletRequest request){
		Map<String, String> paramMap = new HashMap<String, String>();
		String time = request.getParameter("time");
		String timeStr[] = time.split("-");
		if(timeStr[1].length()==1){
			paramMap.put("time", timeStr[0]+"0"+timeStr[1]);
		}else{
			paramMap.put("time", time.replace("-", ""));
		}
		String minPrice = request.getParameter("minPrice");
		paramMap.put("minPrice", minPrice);
		String maxPrice = request.getParameter("maxPrice");
		paramMap.put("maxPrice", maxPrice);
		String vsModelIds = request.getParameter("vsModelIds");
		paramMap.put("vsModelIds", vsModelIds);
		String marketId = request.getParameter("marketId");
		paramMap.put("marketId", marketId);
		String myModelIds = request.getParameter("myModelIds");
		paramMap.put("myModelIds", myModelIds);
		Map<String,Object> map = optimizeService.getComparisonModel(paramMap,request); 
		return map;
	}
	
	/**
	 * 
	 * describe: 导出对标车型
	 * @authod liuyuhuan
	 * @date 2018-2-7 上午11:27:33
	 */
	@RequestMapping(value="/ExportComparisonModel.do",method=RequestMethod.GET)
	public void ExportComparisonModel(HttpServletResponse response, HttpServletRequest request){
		optimizeService.ExportComparisonModel(response,request);
	}
	
	/**
	 *  本品ID：myModelId 
		竞品ID：vsModelId 
		时间：time
	 * describe: 获取产品机会点分析数据
	 * @authod liuyuhuan
	 * @date 2018-2-8 下午5:43:18
	 * http://10.11.2.190:8080/gmtt_priceSales/optimize/getAnalyzeData.do?myModelId=271863&vsModelId=274999&time=2017-4&priceTier=45万~50万&market=Luxury Car-4
	 */
	@RequestMapping(value="/getAnalyzeData.do",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getAnalyzeData(HttpServletRequest request){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("myModelId", request.getParameter("myModelId"));
		paramMap.put("vsModelId", request.getParameter("vsModelId"));
		paramMap.put("priceTier", request.getParameter("priceTier"));
		paramMap.put("market", request.getParameter("market"));
		String time = "";
		String timeArr[] = request.getParameter("time").split("-");
		if(timeArr[1].length()==1){
			time = timeArr[0]+"0"+timeArr[1];
		}else{
			time = timeArr[0]+timeArr[1];
		}
		paramMap.put("time", time);
		String year = time.substring(0, 4);
		paramMap.put("year", year);
		int quarter = Integer.parseInt(time.substring(5, 6));
		if(1<=quarter && quarter<=3){
			quarter=1;
		}else if(4<=quarter && quarter<=6){
			quarter=2;
		}else if(7<=quarter && quarter<=9){
			quarter=3;
		}else if(10<=quarter && quarter<=12){
			quarter=4;
		}
		paramMap.put("quarter", quarter);
		Map<String,Object> map = optimizeService.getAnalyzeData(paramMap,request);
		return map;
	}
	
	/**
	 * describe: 导出产品分析数据
	 * @authod liuyuhuan
	 * @date 2018-3-13 上午9:14:26
	 */
	@RequestMapping(value="/ExportAnalyzeData.do",method=RequestMethod.GET)
	public void ExportAnalyzeData(HttpServletResponse response, HttpServletRequest request){
		optimizeService.ExportAnalyzeData(response,request);
	}
	
	/**
	 * describe: 获取最终报告
	 * @authod liuyuhuan
	 * @date 2018-3-6 下午2:26:33
	 * http://10.11.2.190:8080/gmtt_priceSales/optimize/getReportData.do?time=2017-4&myvs=min=10,max=11,my=271864,vs=274999;min=10,max=11,my=271864,vs=275840;min=10,max=11,my=271866,vs=274999;min=10,max=11,my=271866,vs=275840;min=11,max=12,my=275789,vs=275839;min=11,max=12,my=275789,vs=275832;min=11,max=12,my=271863,vs=275839;min=11,max=12,my=271863,vs=275832;
	 */
	@RequestMapping(value="/getReportData.do",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getReportData(HttpServletRequest request){
		String myvs = request.getParameter("myvs");
		if(StringUtils.isNotBlank(myvs)){
			String params[] = myvs.split(";");
			if(params!=null && params.length>0){
				Set<String> priceSet = new HashSet<String>();
				Set<String> allVersionId = new HashSet<String>();
				List<PriceRange> pl = new ArrayList<PriceRange>();
				List<Integer> minList = new ArrayList<Integer>();
				//去重后获取价格区间和最小价格
				for(int i=0; i<params.length; i++){
					if(params[i]!=null && params[i].length()>0){
						String strArr[] = params[i].split(",");
						String min = "";
						String max = "";
						for(int s=0; s<strArr.length; s++){
							if(strArr[s].contains("max=")){
								max = strArr[s].replace("max=", "");
							}else if(strArr[s].contains("min=")){
								min = strArr[s].replace("min=", "");
							}else if(strArr[s].contains("my=")){
								allVersionId.add(strArr[s].replace("my=", ""));
							}else if(strArr[s].contains("vs=")){
								allVersionId.add(strArr[s].replace("vs=", ""));
							}
						}
						priceSet.add(min+"万"+"≤TP<"+max+"万");
					}
				}
				Iterator<String> i = priceSet.iterator();
				while(i.hasNext()){
					PriceRange p = new PriceRange();
					String priceRange = i.next();
					p.setPriceRange(priceRange);
					int min = Integer.parseInt(StringUtils.substringBefore(priceRange, "万"));
					p.setMin(min);
					pl.add(p);
					minList.add(min);
				}
				//根据最小价格做降序
				Collections.sort(minList);
				List<PriceRange> priceList = new ArrayList<PriceRange>();
				for(int k=minList.size()-1; k>-1; k--){
					int p = minList.get(k);
					for(int j=0; j<pl.size(); j++){
						if(p==pl.get(j).getMin()){
							priceList.add(pl.get(j));
						}
					}
				}
				//补全本品
				for(int d=0; d<priceList.size(); d++){
					Set<String> myVersion = new HashSet<String>();
					for(int t=0; t<params.length; t++){
						if(params[t]!=null && params[t].length()>0){
							String strArr[] = params[t].split(",");
							String min = "";
							for(int s=0; s<strArr.length; s++){
								if(strArr[s].contains("min=")){
									min = strArr[s].replace("min=", "");
								}
							}
							if(min.equals(String.valueOf(priceList.get(d).getMin()))){
								for(int s=0; s<strArr.length; s++){
									if(strArr[s].contains("my=")){
										myVersion.add(strArr[s].replace("my=", ""));
									}
								}
							}
						}
					}
					priceList.get(d).setMyVersion(myVersion);
				}
				String yqId = "";
				String q = "";
				String time = request.getParameter("time");
				String timeArr[] = time.split("-");
				int month = Integer.parseInt(timeArr[1]);
				if(0<month && month<=3){
					q="1";
				}else if(3<month && month<=6){
					q="2";
				}else if(6<month && month <=9){
					q="3";
				}else if(9<month && month <=12){
					q="4";
				}
				yqId = timeArr[0]+"0"+q;
				if(timeArr[1].length()==1){
					time = timeArr[0]+"0"+timeArr[1];
				}else{
					time = timeArr[0]+timeArr[1];
				}
				return optimizeService.getReportData(priceList,params,allVersionId,yqId,time,request);
			}
			
		}
		return null;
	}
	
	/**
	 * 
	 * describe: 导出最终报告
	 * @authod liuyuhuan
	 * @date 2018-3-12 上午11:27:33
	 */
	@RequestMapping(value="/ExportReportData.do",method=RequestMethod.GET)
	public void ExportReportData(HttpServletResponse response, HttpServletRequest request){
		optimizeService.ExportReportData(response,request);
	}
	
	/**
	 * describe: 保存最终报告的请求参数
	 * @authod liuyuhuan
	 * @date 2018-3-13 下午7:56:35
	 */
	@RequestMapping(value="/saveReportParams.do",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> saveReportParams(HttpServletRequest request){
		Map<String,Object> re = new HashMap<String, Object>();
		String subModelId = request.getParameter("subModelId");
		String time = request.getParameter("time");
		String myvs = request.getParameter("myvs");
		MyUserDetails userDetails = (MyUserDetails)request.getSession().getAttribute(AuthConstants.CURR_USER_DETAILS);
		String userId = userDetails.getUserId();
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(myvs) 
				|| StringUtils.isBlank(time) || StringUtils.isBlank(subModelId)){
			re.put("code", 0);
			re.put("str", "参数不能为空");
		}else{
			Map<String, String> param = new HashMap<String, String>();
			param.put("subModelId", subModelId);
			param.put("timeStr", time);
			param.put("myvs", myvs);
			param.put("userId", userId);
			re = optimizeService.saveReportParams(param);
		}
		return re;
	}
	
	/**
	 * describe: 删除最终报告的请求参数
	 * @authod liuyuhuan
	 * @date 2018-3-14 下午12:00:13
	 */
	@RequestMapping(value="/deleteReportParams.do",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> deleteReportParams(HttpServletRequest request){
		Map<String,Object> re = new HashMap<String, Object>();
		String subModelId = request.getParameter("subModelId");
		MyUserDetails userDetails = (MyUserDetails)request.getSession().getAttribute(AuthConstants.CURR_USER_DETAILS);
		String userId = userDetails.getUserId();
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(subModelId)){
			re.put("code", 0);
			re.put("str", "参数不能为空");
		}else{
			Map<String, String> param = new HashMap<String, String>();
			param.put("subModelId", subModelId);
			param.put("userId", userId);
			re = optimizeService.deleteReportParams(param);
		}
		return re;
	}
	
}
