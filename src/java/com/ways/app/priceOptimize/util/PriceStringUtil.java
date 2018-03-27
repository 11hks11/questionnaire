package com.ways.app.priceOptimize.util;

public class PriceStringUtil {
//	/**
//	 * 根据价格段数据生成区间查询sql
//	 * @param maxPrice
//	 * @param minPrice
//	 * @param segmentPrice
//	 * @return
//	 */
//	public static String getPriceSegment(int maxPrice,int minPrice,int segmentPrice){
//		StringBuffer sb = new StringBuffer();
//		String type = "tp";
//		
//		sb.append("case when m."+type+" >= "+maxPrice*10000+" then 1");
//		//区间数
//		if((maxPrice - minPrice)%segmentPrice == 0){
//			int segmentnum = (maxPrice - minPrice)/segmentPrice;
//			int num = 0;
//			for(int i=0;i<segmentnum;i++){
//				num = i+2;
//				sb.append(" when m."+type+" < "+(maxPrice-segmentPrice*i)*10000+" and m."+type+" >= "+(maxPrice-segmentPrice*(i+1))*10000+" then "+(i+2));
//			}
//			sb.append(" when m."+type+" < "+minPrice*10000+" then "+(num+1)+" else null end ");
//		}else{
//			int segmentnum = (maxPrice - minPrice)/segmentPrice;
//			int num = 0;
//			for(int i=0;i<segmentnum;i++){
//				num = i+2;
//				sb.append(" when m."+type+" < "+(maxPrice-segmentPrice*i)*10000+" and m."+type+" >= "+(maxPrice-segmentPrice*(i+1))*10000+" then "+(i+2));
//			}
//			sb.append(" when m."+type+" < "+(maxPrice-segmentPrice*segmentnum)*10000+" and m."+type+" >= "+(minPrice*10000)+" then "+(num+1));
//			sb.append(" when m."+type+" < "+minPrice*10000+" then "+(num+2)+" else null end ");
//		}
//		return sb.toString();
//	}
//	/**
//	 * 根据价格段数据生成区间查询sql
//	 * @param maxPrice
//	 * @param minPrice
//	 * @param segmentPrice
//	 * @return
//	 */
//	public static String getPriceSegment2(int maxPrice,int minPrice,int segmentPrice){
//		StringBuffer sb = new StringBuffer();
//		sb.append("select 1 as fsort, 'TP≥"+maxPrice+"万' segment from dual");
//		//区间数
//		int num = 0;
//		if((maxPrice - minPrice)%segmentPrice == 0){
//			int segmentnum = (maxPrice - minPrice)/segmentPrice;
//			for(int i=0;i<segmentnum;i++){
//				num = i+2;
//				sb.append(" union all select "+(i+2)+" as fsort, '"+(maxPrice-segmentPrice*(i+1))+"万≤TP<"+(maxPrice-segmentPrice*i)+"万' segment from dual");
//			}
//			sb.append(" union all select "+(++num)+" as fsort, 'TP≤"+minPrice+"万' segment from dual");
//		}else{
//			int segmentnum = (maxPrice - minPrice)/segmentPrice;
//			for(int i=0;i<segmentnum;i++){
//				num = i+2;
//				sb.append(" union all select "+(i+2)+" as fsort, '"+(maxPrice-segmentPrice*(i+1))+"万≤TP<"+(maxPrice-segmentPrice*i)+"万' segment from dual");
//			}
//			sb.append(" union all select "+(++num)+" as fsort, '"+(maxPrice-segmentPrice*segmentnum)+"万≤TP<"+minPrice+"万' segment from dual");
//			sb.append(" union all select "+(++num)+" as fsort, 'TP≤"+minPrice+"万' segment from dual");
//		}
//		return sb.toString();
//	}
	/**
	 * 生成价格段区间(多价格段)
	 * @param segment
	 * @return
	 */
	public static String getPriceSegment(int[] segment) {
		
		int size =segment.length/3;
		int max = segment[segment.length-2];
		int min = segment[0];
		StringBuffer sb = new StringBuffer();
		sb.append("select 1 as fsort, 'TP≥"+max+"万' segment from dual");
		//区间数
		int num = 1;
		for(int j=0;j<size;j++){
			int maxPrice = segment[(size-j)*3-2];
			int minPrice = segment[(size-j)*3-3];
			int segmentPrice = segment[(size-j)*3-1];
			if((maxPrice - minPrice)%segmentPrice == 0){
				int segmentnum = (maxPrice - minPrice)/segmentPrice;
				for(int i=0;i<segmentnum;i++){
					num++;
					sb.append(" union all select "+(num)+" as fsort, '"+(maxPrice-segmentPrice*(i+1))+"万≤TP<"+(maxPrice-segmentPrice*i)+"万' segment from dual");
				}
				if(j==size-1){
					sb.append(" union all select "+(++num)+" as fsort, 'TP<"+min+"万' segment from dual");
				}
			}else{
				int segmentnum = (maxPrice - minPrice)/segmentPrice;
				for(int i=0;i<segmentnum;i++){
					num++;
					sb.append(" union all select "+(num)+" as fsort, '"+(maxPrice-segmentPrice*(i+1))+"万≤TP<"+(maxPrice-segmentPrice*i)+"万' segment from dual");
				}
				sb.append(" union all select "+(++num)+" as fsort, '"+minPrice+"万≤TP<"+(maxPrice-segmentPrice*segmentnum)+"万' segment from dual");
				if(j==size-1){
					sb.append(" union all select "+(++num)+" as fsort, 'TP<"+min+"万' segment from dual");
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 根据价格段数据生成区间查询sql(多价格段)
	 * @param segment
	 * @return
	 */
	public static String getPriceSegment1(int[] segment){
		StringBuffer sb = new StringBuffer();
		String type = "tp";
		
		int size =segment.length/3;
		int max = segment[segment.length-2];
		int min = segment[0];
		
		sb.append("case when m."+type+" >= "+max*10000+" then 1");
		//区间数
		int num = 1;
		for(int j=0;j<size;j++){
			int maxPrice = segment[(size-j)*3-2];
			int minPrice = segment[(size-j)*3-3];
			int segmentPrice = segment[(size-j)*3-1];
			if((maxPrice - minPrice)%segmentPrice == 0){
				int segmentnum = (maxPrice - minPrice)/segmentPrice;
				for(int i=0;i<segmentnum;i++){
					num++;
					sb.append(" when m."+type+" < "+(maxPrice-segmentPrice*i)*10000+" and m."+type+" >= "+(maxPrice-segmentPrice*(i+1))*10000+" then "+num);
				}
				if(j==size-1){
					sb.append(" when m."+type+" < "+min*10000+" then "+(++num)+" else null end ");
				}
			}else{
				int segmentnum = (maxPrice - minPrice)/segmentPrice;
				for(int i=0;i<segmentnum;i++){
					num++;
					sb.append(" when m."+type+" < "+(maxPrice-segmentPrice*i)*10000+" and m."+type+" >= "+(maxPrice-segmentPrice*(i+1))*10000+" then "+num);
				}
				sb.append(" when m."+type+" < "+(maxPrice-segmentPrice*segmentnum)*10000+" and m."+type+" >= "+(minPrice*10000)+" then "+(++num));
				if(j==size-1){
					sb.append(" when m."+type+" < "+min*10000+" then "+(++num)+" else null end ");
				}
			}
		}
			
		return sb.toString();
	}
	
}
