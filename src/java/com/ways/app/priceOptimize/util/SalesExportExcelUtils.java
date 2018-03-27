package com.ways.app.priceOptimize.util;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;

import com.ways.app.common.utils.Constant;

public class SalesExportExcelUtils {

	
	/**
	 * total表头
	 * @param wb
	 * @return
	 */
	public static CellStyle getGradeHeadStyle(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("微软雅黑");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_LEFT);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_THIN);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_THIN);//设置无边框
		contentStyle.setBorderRight(CellStyle.BORDER_THIN);
		
		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255,255,255)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return contentStyle;
	}
	/**
	 * 表头-浅蓝
	 * @param wb
	 * @return
	 */
	public static CellStyle getHeadExeclStyle(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("微软雅黑");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_THIN);//设置无边框
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(189, 219, 255)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return contentStyle;
	}
	
	/**
	 * 表头-灰色
	 * @param wb
	 * @return
	 */
	public static CellStyle getHeadExeclStyle2(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("微软雅黑");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_THIN);//设置无边框
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(206, 206, 206)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return contentStyle;
	}
	
	/**
	 * 表头-深蓝
	 * @param wb
	 * @return
	 */
	public static CellStyle getHeadExeclStyle3(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("微软雅黑");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_THIN);//设置无边框
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(113, 171, 243)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return contentStyle;
	}
	/**
	 * 表头 副标题-浅蓝
	 * @param wb
	 * @return
	 */
	public static CellStyle getHeadChildExeclStyle(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("微软雅黑");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_NONE);//设置无边框
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(189, 219, 255)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return contentStyle;
	}
	
	/**
	 * 表头-副标题灰色
	 * @param wb
	 * @return
	 */
	public static CellStyle getHeadChildExeclStyle2(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("微软雅黑");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_NONE);//设置无边框
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(206, 206, 206)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return contentStyle;
	}
	
	/**
	 * 表头-副标题深蓝
	 * @param wb
	 * @return
	 */
	public static CellStyle getHeadChildExeclStyle3(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("微软雅黑");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_NONE);//设置无边框
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(113, 171, 243)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return contentStyle;
	}
	/**
	 * 表头 副标题-浅蓝
	 * @param wb
	 * @return
	 */
	public static CellStyle getHeadChildFirstExeclStyle(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("微软雅黑");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_THIN);//设置无边框
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(189, 219, 255)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return contentStyle;
	}
	
	/**
	 * 表头-副标题灰色
	 * @param wb
	 * @return
	 */
	public static CellStyle getHeadChildFirstExeclStyle2(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("微软雅黑");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_THIN);//设置无边框
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(206, 206, 206)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return contentStyle;
	}
	
	/**
	 * 表头-副标题深蓝
	 * @param wb
	 * @return
	 */
	public static CellStyle getHeadChildFirstExeclStyle3(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("微软雅黑");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_THIN);//设置无边框
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(113, 171, 243)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return contentStyle;
	}
	/**
	 * 数据红色 百分比
	 * @param wb
	 * @return
	 */
	public CellStyle getDataRedStyle(Workbook wb) {
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		contentFont.setFontName("Arial");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.RED.index);
		//
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_NONE);
		contentStyle.setBorderLeft(CellStyle.BORDER_NONE);
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.YELLOW.index, (byte) 242, (byte) 242, (byte) 242); 
		//contentStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 255)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		
		//格式化数据
		XSSFDataFormat format = (XSSFDataFormat) wb.createDataFormat();
		contentStyle.setDataFormat(format.getFormat(Constant.PERFORMATTER));
		return contentStyle;
	}
	/**
	 * 数据百分比正常颜色
	 * @param wb
	 * @return
	 */
	public static CellStyle getDataBlackStyle(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("Arial");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_NONE);
		contentStyle.setBorderLeft(CellStyle.BORDER_NONE);
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
//		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
//		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
//		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 255)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		
		//格式化数据
		XSSFDataFormat format = (XSSFDataFormat) wb.createDataFormat();
		contentStyle.setDataFormat(format.getFormat(Constant.PERFORMATTER));
		
		return contentStyle;
		
	}
	/**
	 * 文本数据
	 * @param wb
	 * @return
	 */
	public static CellStyle getDataStyle(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("Arial");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_NONE);
		contentStyle.setBorderLeft(CellStyle.BORDER_NONE);
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
//		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
//		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
//		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 255)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		
		//格式化数据
		
		
		return contentStyle;
	}
	

	/**
	 * total文本
	 * @param wb
	 * @return
	 */
	public static CellStyle getTotalDataStyle(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("Arial");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_NONE);
		contentStyle.setBorderLeft(CellStyle.BORDER_NONE);
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
//		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
//		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
//		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(206, 206, 206)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return contentStyle;
	}
	/**
	 * total数据红色 百分比
	 * @param wb
	 * @return
	 */
	public CellStyle getTotalDataRedStyle(Workbook wb) {
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		contentFont.setFontName("Arial");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.RED.index);
		//
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_NONE);
		contentStyle.setBorderLeft(CellStyle.BORDER_NONE);
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.YELLOW.index, (byte) 242, (byte) 242, (byte) 242); 
		//contentStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(206, 206, 206)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		
		//格式化数据
		XSSFDataFormat format = (XSSFDataFormat) wb.createDataFormat();
		contentStyle.setDataFormat(format.getFormat(Constant.PERFORMATTER));
		return contentStyle;
	}
	/**
	 * total数据百分比正常颜色
	 * @param wb
	 * @return
	 */
	public static CellStyle getTotalDataBlackStyle(Workbook wb){
		XSSFCellStyle contentStyle=(XSSFCellStyle) wb.createCellStyle();
		Font contentFont= wb.createFont();
		
		contentFont.setFontName("Arial");
		contentFont.setFontHeightInPoints((short) 10);
		contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		contentFont.setCharSet(Font.DEFAULT_CHARSET);
		contentFont.setColor(IndexedColors.BLACK.index);
		//
		contentStyle.setWrapText(true);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		contentStyle.setFont(contentFont);
		contentStyle.setBorderTop(CellStyle.BORDER_NONE);
		contentStyle.setBorderBottom(CellStyle.BORDER_NONE);
		contentStyle.setBorderLeft(CellStyle.BORDER_NONE);
		contentStyle.setBorderRight(CellStyle.BORDER_NONE);
		
//		contentStyle.setLeftBorderColor(IndexedColors.WHITE.index);
//		contentStyle.setBottomBorderColor(IndexedColors.WHITE.index);
//		contentStyle.setTopBorderColor(IndexedColors.WHITE.index);
		//contentStyle.setRightBorderColor(IndexedColors.WHITE.index);
		//自定义颜色
		//HSSFPalette customPalette = ((HSSFWorkbook) wb).getCustomPalette();  
		//customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 201, (byte) 219, (byte) 255); 
		//contentStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		contentStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(206, 206, 206)));

		contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		
		//格式化数据
		XSSFDataFormat format = (XSSFDataFormat) wb.createDataFormat();
		contentStyle.setDataFormat(format.getFormat(Constant.PERFORMATTER));
		
		return contentStyle;
	}
	
	
}
