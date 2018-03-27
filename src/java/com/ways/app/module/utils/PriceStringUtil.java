package com.ways.app.module.utils;

public class PriceStringUtil {
	/**
	 * 根据价格段数据生成区间查询sql
	 * @param maxPrice
	 * @param minPrice
	 * @param segmentPrice
	 * @return
	 */
	public static String getPriceSegment(int maxPrice,int minPrice,int segmentPrice,String priceType){
		StringBuffer sb = new StringBuffer();
		String type = "tp";
		if("2".equals(priceType)){
			type = "msrp";
		}
		sb.append("case when t."+type+" >= "+maxPrice*10000+" then 1");
		//区间数
		if((maxPrice - minPrice)%segmentPrice == 0){
			int segmentnum = (maxPrice - minPrice)/segmentPrice;
			int num = 0;
			for(int i=0;i<segmentnum;i++){
				num = i+2;
				sb.append(" when t."+type+" < "+(maxPrice-segmentPrice*i)*10000+" and t."+type+" >= "+(maxPrice-segmentPrice*(i+1))*10000+" then "+(i+2));
			}
			sb.append(" when t."+type+" < "+minPrice*10000+" then "+(num+1)+" else null end ");
		}else{
			int segmentnum = (maxPrice - minPrice)/segmentPrice;
			int num = 0;
			for(int i=0;i<segmentnum;i++){
				num = i+2;
				sb.append(" when t."+type+" < "+(maxPrice-segmentPrice*i)*10000+" and t."+type+" >= "+(maxPrice-segmentPrice*(i+1))*10000+" then "+(i+2));
			}
			sb.append(" when t."+type+" < "+(maxPrice-segmentPrice*segmentnum)*10000+" and t."+type+" >= "+(minPrice*10000)+" then "+(num+1));
			sb.append(" when t."+type+" < "+minPrice*10000+" then "+(num+2)+" else null end ");
		}
		return sb.toString();
	}
	/**
	 * 根据价格段数据生成区间查询sql
	 * @param maxPrice
	 * @param minPrice
	 * @param segmentPrice
	 * @return
	 */
	public static String getPriceSegment2(int maxPrice,int minPrice,int segmentPrice){
		StringBuffer sb = new StringBuffer();
		sb.append("select 1 as fsort, '"+maxPrice+"万以上' segment from dual");
		//区间数
		if((maxPrice - minPrice)%segmentPrice == 0){
			int segmentnum = (maxPrice - minPrice)/segmentPrice;
			int num = 0;
			for(int i=0;i<segmentnum;i++){
				num = i+2;
				sb.append(" union all select "+(i+2)+" as fsort, '"+(maxPrice-segmentPrice*(i+1))+"-"+(maxPrice-segmentPrice*i)+"万' segment from dual");
			}
			sb.append(" union all select "+(num+1)+" as fsort, '"+minPrice+"万以下' segment from dual");
		}else{
			int segmentnum = (maxPrice - minPrice)/segmentPrice;
			int num = 0;
			for(int i=0;i<segmentnum;i++){
				num = i+2;
				sb.append(" union all select "+(i+2)+" as fsort, '"+(maxPrice-segmentPrice*(i+1))+"-"+(maxPrice-segmentPrice*i)+"万' segment from dual");
			}
			sb.append(" union all select "+(num+1)+" as fsort, '"+(maxPrice-segmentPrice*segmentnum)+"-"+minPrice+"万' segment from dual");
			sb.append(" union all select "+(num+2)+" as fsort, '"+minPrice+"万以下' segment from dual");
		}
		return sb.toString();
	}
}
