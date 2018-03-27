package com.ways.app.priceOptimize.util;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class ExcelUtil {

	/**
	 * describe: 内容:水平居中+垂直居中, 字体：白色+加粗， 自定义蓝色背景色
	 * @authod liuyuhuan
	 * @date 2018-2-7 下午2:20:55
	 */
	public static CellStyle getTitleOne(Workbook wb,int point,short pontColor, boolean addBackground){
		XSSFCellStyle s=(XSSFCellStyle) wb.createCellStyle();
		//内容居中
		s.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		s.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		//设置字体
		Font font = wb.createFont();
		font.setFontName("黑体");    
		font.setFontHeightInPoints((short) point);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(pontColor);
		s.setFont(font);
		
		if(addBackground){
			//自定义背景色
			s.setFillForegroundColor(new XSSFColor(new java.awt.Color(83, 141, 213)));
			s.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		}
		return s;
	}
	
	public static CellStyle getPointOne(Workbook wb,String fontName,int point, boolean isBold){
		CellStyle s=wb.createCellStyle();
		//内容居中
		s.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		s.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		//设置字体
		Font font = wb.createFont();
		font.setFontName(fontName);    
		font.setFontHeightInPoints((short) point);
		if(isBold){
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		s.setFont(font);
		return s;
	}
	
	/**
	 * describe: 内容:水平居中+垂直居中
	 * @authod liuyuhuan
	 * @date 2018-2-7 下午2:22:44
	 */
	public static CellStyle getCellStyleOne(Workbook wb){
		XSSFCellStyle s=(XSSFCellStyle) wb.createCellStyle();
		//内容居中
		s.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		s.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		s.setDataFormat(wb.createDataFormat().getFormat("###0.0"));
		return s;
	}
	
	public static CellStyle getCellStyleTwo(Workbook wb,int r, int g, int b){
		XSSFCellStyle s=(XSSFCellStyle) wb.createCellStyle();
		//内容居中
		s.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		s.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		s.setFillForegroundColor(new XSSFColor(new java.awt.Color(r, g, b)));
		s.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return s;
	}
	
}
