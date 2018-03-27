package com.ways.app.module.utils;

public class PriceSalesUtils {
	/**
	 * 获取拼接SQL，格式如下
	 *  select 0 levelValue, 'TP≥30万' levelName from dual
		union all
		select 1 levelValue, '23万≤TP<30万' levelName from dual
		union all
		select 2 levelValue, '16万≤TP<23万' levelName from dual
		union all
		select 3 levelValue, '9万≤TP<16万' levelName from dual
		union all
		select 4 levelValue, '2万≤TP<9万' levelName from dual
		union all
		select 5 levelValue, 'TP<2万' levelName from dual
	 * @param start 最大
	 * @param end 最小
	 * @param split
	 * @param priceType
	 * @return
	 */
	public static String getPriceLevelTableSql(int start,int end,int split,String priceType){
		int count = (start-end)%split==0?(start-end)/split:(start-end)/split+1;
		if("1".equals(priceType)){
			priceType = "TP";
		}else{
			priceType = "MSRP";
		}
		
		StringBuffer priceLevelTable = new StringBuffer();
		
		for(int i = 0; i < count + 2; i++){
			if(i == 0){
				priceLevelTable.append(" select "+i+" levelValue, '"+priceType+"≥"+start+"万' levelName  from dual ");
			}else if(i == count + 1){
				priceLevelTable.append(" union all ");
				priceLevelTable.append(" select "+i+" levelValue, '"+priceType+"<"+start+"万' levelName  from dual ");
			}else{
				int ends = start - split;
				if(ends < end){
					ends = end;
				}
				priceLevelTable.append(" union all ");
				priceLevelTable.append(" select "+i+" levelValue, '"+ends+"万≤"+priceType+"<"+start+"万' levelName  from dual ");
			}
			if(i != 0){
				if(start-split < end){
					start = end;
				}else{
					start -= split;
				}
			}
		}
		return priceLevelTable.toString();
	}
	
	/**
	 * 获取价格段case when sql语句，格式如下
	 * case
         when x.TP >= 300000 then 0
         when x.TP >= 230000 and x.TP < 300000 then 1
         when x.TP >= 160000 and x.TP < 230000 then 2
         when x.TP >= 90000 and x.TP < 160000 then 3
         when x.TP >= 20000 and x.TP < 90000 then 4
         when x.TP < 20000 then 5
       end
	 * @param start 最大
	 * @param end 最小
	 * @param split
	 * @param priceType
	 * @return
	 */
	public static String getPriceLevelCaseSql(int start,int end,int split,String priceType){
		int count = (start-end)%split==0?(start-end)/split:(start-end)/split+1;
		if("1".equals(priceType)){
			priceType = "TP";
		}else{
			priceType = "MSRP";
		}
		
		StringBuffer priceLevelCase = new StringBuffer();
		priceLevelCase.append(" case ");
		
		for(int i = 0; i < count + 2; i++){
			if(i == 0){
				priceLevelCase.append(" when x."+priceType+" >= "+(start*10000)+" then " + i);
			}else if(i == count + 1){
				priceLevelCase.append(" when x."+priceType+" < "+(end*10000)+" then " + i +" end ");
			}else{
				int ends = start - split;
				if(ends < end){
					ends = end;
				}
				priceLevelCase.append(" when x."+priceType+" >= "+(ends*10000)+" and x."+priceType+" < "+(start*10000)+" then " + i);
			}
			if(i != 0){
				if(start-split < end){
					start = end;
				}else{
					start -= split;
				}
			}
		}
		return priceLevelCase.toString();
	}
}
