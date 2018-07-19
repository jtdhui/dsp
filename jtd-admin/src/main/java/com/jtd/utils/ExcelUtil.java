package com.jtd.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtd.web.service.admin.IAdminOperatorService;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>Excel工具类</p>
 */
public class ExcelUtil {
	
	private static Logger log = LoggerFactory.getLogger(IAdminOperatorService.class);
	
	// 私有化构造函数
	private ExcelUtil() {
	}

	public static final String ERROR_EXT_NAME = "错误的扩展名";
	
	public static final String FILE_EXTENSION_XLS = "xls";

    public static final String FILE_EXTENSION_XLSX = "xlsx";
    
    //表头单元格样式
    public static  CellStyle headStyle ;
    //字体
    public static  Font headFont;

	public static void main(String[] args) {
		List<SheetObject> sheets = new ArrayList<ExcelUtil.SheetObject>();
		SheetObject sheetObject = new SheetObject("测试");
		sheets.add(sheetObject);
	}

	/**
	 * @author mengweifeng
	 * @param filePath
	 * @param sheets
	 *            key=sheet页名称 value=需要写入的sheet页的内容(string数组的形式)，String[]第一行为标题
	 * @throws IOException
	 * @throws IOException
	 * @since 2012-11-28
	 */
	public static void create03ExcelFile(String filePath, Map<String, List<String[]>> sheets) throws IOException {
		File excelFile = new File(filePath);
		create03ExcelFile(excelFile, sheets);
	}

	/**
	 * @author mengweifeng
	 * @param excelFile
	 * @param sheets
	 *            key = sheet页名称 value =
	 *            需要写入的sheet页的内容(string数组的形式)，String[]第一行为标题
	 * @throws IOException
	 * @throws IOException
	 * @since 2012-11-28
	 */
	public static void create03ExcelFile(File excelFile, Map<String, List<String[]>> sheets) throws IOException {
		Workbook wb = new HSSFWorkbook();
		if (!excelFile.getAbsolutePath().endsWith(".xls")) {
			throw new IOException(ERROR_EXT_NAME);
		}
		createExcelFile(wb, excelFile, sheets);
	}

	/**
	 * 创建07格式的excel文件
	 * 
	 * @author mengweifeng
	 * @param filePath
	 * @param sheets
	 * @throws IOException
	 * @since 2013-5-8
	 */
	public static void create07ExcelFile(String filePath, Map<String, List<String[]>> sheets) throws IOException {
		File excelFile = new File(filePath);
		create07ExcelFile(excelFile, sheets);
	}

	/**
	 * 创建07格式的excel文件
	 * 
	 * @author mengweifeng
	 * @param excelFile
	 * @param sheets
	 * @throws IOException
	 * @since 2013-5-8
	 */
	public static void create07ExcelFile(File excelFile, Map<String, List<String[]>> sheets) throws IOException {
		Workbook wb = new XSSFWorkbook();
		if (!excelFile.getAbsolutePath().endsWith(".xlsx")) {
			throw new IOException(ERROR_EXT_NAME);
		}
		createExcelFile(wb, excelFile, sheets);
	}

	/**
	 * 创建带样式的2007版本excel文件
	 * 
	 * @param excelFile
	 * @param sheets
	 * @throws IOException
	 */
	public static void create07ExcelFileWithStyle(File excelFile, List<SheetObject> sheets) throws IOException {
		Workbook wb = new XSSFWorkbook();
		if (!excelFile.getAbsolutePath().endsWith(".xlsx")) {
			throw new IOException(ERROR_EXT_NAME);
		}
		createExcelFile(wb, excelFile, sheets);
	}

	public static void createExcelFile(Workbook wb, File excelFile, Map<String, List<String[]>> sheets) throws IOException {
		for (Entry<String, List<String[]>> entry : sheets.entrySet()) {
			String sheetName = entry.getKey();
			Sheet sheet = wb.createSheet(sheetName);
			List<String[]> sheetValues = entry.getValue();
			if (sheetValues == null) {
				continue;
			}
			for (int i = 0; i < sheetValues.size(); i++) {
				String[] values = sheetValues.get(i);
				Row row = sheet.createRow(i);
				for (int j = 0; j < values.length; j++) {
					Cell cell = row.createCell(j);
					String value = values[j];
					if (value == null) {
						value = "";
					}
					try {
						Double numberValue = Double.parseDouble(value);
						cell.setCellValue(numberValue);
					} catch (Exception e) {
						cell.setCellValue(values[j]);
					}
				}
			}
		}
		OutputStream os = new FileOutputStream(excelFile);
		wb.write(os);
	}

	public static void createExcelFile(Workbook wb, File excelFile, List<SheetObject> sheets) throws IOException {
		Map<DspCellStyleType, CellStyle> cellStyleMap = createCellStyleMap(wb);
		for (SheetObject sheetObject : sheets) {
			String sheetName = sheetObject.getSheetName();
			Sheet sheet = wb.createSheet(sheetName);
			int startRowNumber = 0;
			RowObject titleRow = sheetObject.getTitles();
			if (titleRow != null) {
				Row row = sheet.createRow(0);
				fillCellData(cellStyleMap, row, titleRow);
				startRowNumber++;
			}
			List<RowObject> dataRowObjects = sheetObject.getDatas();
			for (int i = startRowNumber; i < dataRowObjects.size() + startRowNumber; i++) {
				Row row = sheet.createRow(i);
				RowObject dataRowObject = dataRowObjects.get(i);
				fillCellData(cellStyleMap, row, dataRowObject);
			}
		}
		OutputStream os = new FileOutputStream(excelFile);
		wb.write(os);
	}

	 /**
	  *  excelExport(根据maps导出excel)
	  * @param maps
	  * @param list
	  * @param type
	  * @return
	  */
    public static Workbook  excelExport(Map<String, String> maps, List<Map<String,Object>> list,String type) {
        Workbook wb = null;
        
        try {
           
            if (type.equals(FILE_EXTENSION_XLS)) {
                wb = new HSSFWorkbook();
               
            }
            if (type.equals(FILE_EXTENSION_XLSX)) {
                wb = new XSSFWorkbook();
                
            }
            //创建样式
            headStyle = wb.createCellStyle();
            headFont=wb.createFont();
            
            setHeadStyle() ;
            
            CreationHelper createHelper = wb.getCreationHelper();
            Sheet sheet = wb.createSheet("sheet1");
            Set<String> sets = maps.keySet();
            Row row = sheet.createRow(0);
            int i = 0;
            // 定义表头
            for (Iterator<String> it = sets.iterator(); it.hasNext();) {
                String key = it.next();
                Cell cell = row.createCell(i++);
                cell.setCellValue(createHelper.createRichTextString(maps.get(key)));
                
                cell.setCellStyle(headStyle);
           
            }
            // 填充表单内容
            
           
            int count = 1;
            for (int j = 0; j < list.size(); j++) {
                Map<String,Object> map = list.get(j);
               
                int index = 0;
                Row row1 = sheet.createRow(j + 1);
                for (Iterator<String> it = sets.iterator(); it.hasNext();) {
                    String key = it.next();
                    
                    // 调用原对象的getXXX()方法
                    Object value = map.get(key);
                    Cell cell = row1.createCell(index++);
                    cell.setCellValue(value==null ?"":value.toString());
                }
            }
            //列宽自适应
            for(int a=0;a<i;a++){
                sheet.autoSizeColumn((short)a);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return wb;
        } 
        return wb;
    }
    
    public static void  setHeadStyle() {
    	 
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headStyle.setWrapText(true);
        headFont.setFontName("黑体");
        headFont.setFontHeightInPoints((short) 13);//设置字体大小
        
        headStyle.setFont(headFont);
    }
    
	/**
	 * 填充单元格数据
	 * 
	 * @param cellStyleMap
	 * @param row
	 * @param dataRowObject
	 */
	private static void fillCellData(Map<DspCellStyleType, CellStyle> cellStyleMap, Row row, RowObject dataRowObject) {
		List<CellObject> cellObjects = dataRowObject.getCells();
		for (int j = 0; j < cellObjects.size(); j++) {
			Cell cell = row.createCell(j);
			CellObject cellObject = cellObjects.get(j);
			DspCellStyleType dspCellStyleType = cellObject.getDspCellStyleType();
			CellStyle cellStyle = cellStyleMap.get(dspCellStyleType);
			cell.setCellStyle(cellStyle);
			Object valueObject = cellObject.getValue();
			String value;
			if (valueObject == null) {
				value = "-";
				cell.setCellValue(value);
				continue;
			}
			value = valueObject.toString();
			switch (dspCellStyleType) {
			case BLUETITLE:
			case YELLOWTITLE:
			case STRING:
				// 字符串类
				cell.setCellValue(value);
				break;
			case INTEGER:
			case DOUBLE:
			case PERCENT:
			case MONEY:
				// 数字类
				Double doubleValue = Double.valueOf(value);
				cell.setCellValue(doubleValue);
				break;
			case DATE:
				// 日期时间类
				// TODO 时间的格式暂不清楚，所以暂时不写
				cell.setCellValue(value);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 生成单元格样式集合
	 * 
	 * @param wb
	 * @return
	 */
	private static Map<DspCellStyleType, CellStyle> createCellStyleMap(Workbook wb) {
		Map<DspCellStyleType, CellStyle> cellStyleMap = new HashMap<DspCellStyleType, CellStyle>();
		XSSFDataFormat df = (XSSFDataFormat) wb.createDataFormat();
		// 蓝色底标题样式
		CellStyle blueTitleCellStyle = getCellStyle(wb);
		blueTitleCellStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
		blueTitleCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font whiteTitleFont = wb.createFont();
		whiteTitleFont.setColor(HSSFColor.WHITE.index);
		whiteTitleFont.setFontName("微软雅黑");
		blueTitleCellStyle.setFont(whiteTitleFont);
		whiteTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStyleMap.put(DspCellStyleType.BLUETITLE, blueTitleCellStyle);

		// 蓝色底标题样式
		CellStyle yellowTitleCellStyle = getCellStyle(wb);
		yellowTitleCellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		yellowTitleCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font blackTitleFont = wb.createFont();
		blackTitleFont.setColor(HSSFColor.BLACK.index);
		blackTitleFont.setFontName("微软雅黑");
		yellowTitleCellStyle.setFont(blackTitleFont);
		blackTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStyleMap.put(DspCellStyleType.YELLOWTITLE, yellowTitleCellStyle);

		// 日期格式
		CellStyle statDateCellStyle = getCellStyle(wb);
		statDateCellStyle.setDataFormat(df.getFormat("yyyy-mm-dd"));
		cellStyleMap.put(DspCellStyleType.DATE, statDateCellStyle);
		// 普通字符串
		CellStyle stringCellStyle = getCellStyle(wb);
		cellStyleMap.put(DspCellStyleType.STRING, stringCellStyle);
		// 整数
		CellStyle integerCellStyle = getCellStyle(wb);
		integerCellStyle.setDataFormat(df.getFormat("#,##0"));
		cellStyleMap.put(DspCellStyleType.INTEGER, integerCellStyle);
		// 小数
		CellStyle doubleCellStyle = getCellStyle(wb);
		doubleCellStyle.setDataFormat(df.getFormat("#,##0.00"));
		cellStyleMap.put(DspCellStyleType.DOUBLE, doubleCellStyle);
		// 百分数
		CellStyle percentCellStyle = getCellStyle(wb);
		percentCellStyle.setDataFormat(df.getFormat("0.00%"));
		cellStyleMap.put(DspCellStyleType.PERCENT, percentCellStyle);
		// 金额
		CellStyle moneyCellStyle = getCellStyle(wb);
		moneyCellStyle.setDataFormat(df.getFormat("¥#,##0.00"));
		cellStyleMap.put(DspCellStyleType.MONEY, moneyCellStyle);
		return cellStyleMap;
	}

	/**
	 * 获取单元格基本样式
	 * 
	 * @author mengweifeng
	 * @return
	 * @since 2013-6-18
	 */
	private static CellStyle getCellStyle(Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		Font font = wb.createFont();
		font.setFontName("微软雅黑");
		cellStyle.setFont(font);
		return cellStyle;
	}

	/**
	 * 读取exce文件
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static Workbook getWorkbook(File file) throws Exception {
		Workbook wb = null;
		InputStream is = new FileInputStream(file);
		String fileName = file.getName();
		String extendName = FileUtil.getExtendName(fileName);
		if ("xls".equals(extendName)) {
			// 2003格式
			wb = new HSSFWorkbook(is);
		} else if ("xlsx".equals(extendName)) {
			// 2007格式
			wb = new XSSFWorkbook(is);
		} else {
			// 不是excel文件
			throw new Exception("不是EXCLE文件");
		}
		return wb;
	}

	/**
	 * 获取单元格的内容
	 * 
	 * @author mengweifeng
	 * @param cell
	 * @return
	 * @since 2013-2-20
	 */
	public static String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		String value = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:

			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				if (date != null) {
					value = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
				}
			} else {
				value = new DecimalFormat("0").format(cell.getNumericCellValue());
			}
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			// 导入时如果为公式生成的数据则无值
			if (!cell.getStringCellValue().equals("")) {
				value = cell.getStringCellValue();
			} else {
				value = cell.getNumericCellValue() + "";
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = "";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			value = (cell.getBooleanCellValue() == true ? "Y" : "N");
			break;
		default:
			value = "";
		}
		value = rightTrim(value);
		return value;
	}

	/**
	 * 
	 * 去掉字符串右边的空格
	 * 
	 * @param str
	 *            要处理的字符串
	 * 
	 * @return 处理后的字符串
	 */

	private static String rightTrim(String str) {
		if (str == null) {
			return "";
		}
		str = str.trim().replaceAll("\r", "").replaceAll("\r\n", "").replaceAll("\n", "");
		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) != 0x20 && str.charAt(i) != (char) 160) {
				break;
			}
			length--;
		}
		return str.substring(0, length);
	}

	static class SheetObject {
		private String sheetName;
		private RowObject titles;
		private List<RowObject> datas;

		public String getSheetName() {
			return sheetName;
		}

		public SheetObject(String sheetName) {
			super();
			this.sheetName = sheetName;
		}

		public void setSheetName(String sheetName) {
			this.sheetName = sheetName;
		}

		public RowObject getTitles() {
			return titles;
		}

		public void setTitles(RowObject titles) {
			this.titles = titles;
		}

		public List<RowObject> getDatas() {
			return datas;
		}

		public void setDatas(List<RowObject> datas) {
			this.datas = datas;
		}

	}

	static class RowObject {
		List<CellObject> cells;

		public List<CellObject> getCells() {
			return cells;
		}

		public void setCells(List<CellObject> cells) {
			this.cells = cells;
		}
	}

	static class CellObject {
		private Object value;
		private DspCellStyleType dspCellStyleType;

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public DspCellStyleType getDspCellStyleType() {
			return dspCellStyleType;
		}

		public void setDspCellStyleType(DspCellStyleType dspCellStyleType) {
			this.dspCellStyleType = dspCellStyleType;
		}

	}
	
	public static void downloadExcel(String excelCode, List<Map<String, Object>> dataList, HttpServletResponse response) {

		if (StringUtils.isEmpty(excelCode)) {
			return;
		}
		
		File file = FileUtil.getFileInClassPath("/report.json");
		
		String fileContent = FileUtil.readAsString(file);
		
		JSONObject reportConfig = null ;
		try {
			reportConfig = JSONObject.parseObject(fileContent);
		} catch (Exception e) {
			log.error("downloadExcel发生错误，report.json解析失败",e);
		}
		
		if(reportConfig == null){
			log.error("downloadExcel发生错误，reportConfig为空");
			return ;
		}
		
		/**
		 * --------------------------------------------------------------------
		 * --------------------------------------------------------------------
		 */

		// excel最终的行数据
		List<String[]> excelRowList = new ArrayList<String[]>();

		JSONObject config = reportConfig.getJSONObject(excelCode);
		String fileName = config.getString("fileName");
		JSONArray firstRowColumeJsonArray = config
				.getJSONArray("firstRowColume");
		JSONArray otherRowColumeJsonArray = config
				.getJSONArray("otherRowColume");

		// 生成excel第一行
		String[] firstRowCols = new String[firstRowColumeJsonArray.size()];
		firstRowColumeJsonArray.toArray(firstRowCols);

		// excelRowList添加第一行
		excelRowList.add(firstRowCols);

		/**
		 * excelRowList添加其他行
		 */
		if (dataList != null) {
			for (Map<String, Object> dataMap : dataList) {

				String[] otherRowCols = new String[firstRowColumeJsonArray
						.size()];

				// 按照firstRowCols的顺序，同步取otherRowColume中的值，再用该值做key从map中取到数据
				for (int i = 0; i < firstRowCols.length; i++) {

					String dataKey = otherRowColumeJsonArray.getString(i);
					otherRowCols[i] = dataMap.get(dataKey) != null ? dataMap
							.get(dataKey).toString() : "";

				}

				excelRowList.add(otherRowCols);
			}
		}
		/**
		 * excelRowList添加其他行 END
		 */

		/**
		 * excelRowList准备完成后，开始生成excel
		 */
		Map<String, List<String[]>> sheets = new HashMap<String, List<String[]>>();
		sheets.put(fileName, excelRowList);

		File excelFile = null;
		try {
			String baseDir = System.getProperty("java.io.tmpdir");
			excelFile = new File(baseDir + "/" + System.currentTimeMillis()
					+ ".xlsx");
			ExcelUtil.create07ExcelFile(excelFile, sheets);
			FileUtil.downloadFile(response, fileName + ".csv", excelFile);
		} catch (IOException e) {
			// log.error(e.getMessage(), e);
		} finally {
			if (excelFile != null && excelFile.exists()) {
				try {
					excelFile.delete();
				} catch (Exception e) {
					log.error("downloadExcel生成报表发生错误,excelCode={}", excelCode);
				}
			}
		}

		/**
		 * excelRowList准备完成后，开始生成excel END
		 */
	}

    public static void downloadExcel(String fileName, JSONArray headerColumeJsonArray,JSONArray dataRowColumeJsonArray, List<Map<String, Object>> dataList, HttpServletResponse response) {

        // excel最终的行数据
        List<String[]> excelRowList = new ArrayList<String[]>();

        // 生成excel第一行
        String[] firstRowCols = new String[headerColumeJsonArray.size()];
        headerColumeJsonArray.toArray(firstRowCols);

        // excelRowList添加第一行
        excelRowList.add(firstRowCols);

        /**
         * excelRowList添加其他行
         */
        if (dataList != null) {
            for (Map<String, Object> dataMap : dataList) {
                String[] dataRowCols = new String[headerColumeJsonArray.size()];
                for (int i = 0; i < firstRowCols.length; i++) {
                    String dataKey = dataRowColumeJsonArray.getString(i);
                    dataRowCols[i] = dataMap.get(dataKey) != null ? dataMap.get(dataKey).toString() : "";
                }
                excelRowList.add(dataRowCols);
            }
        }

        /**
         * excelRowList准备完成后，开始生成excel
         */
        Map<String, List<String[]>> sheets = new HashMap<String, List<String[]>>();
        sheets.put(fileName, excelRowList);

        File excelFile = null;
        try {
            String baseDir = System.getProperty("java.io.tmpdir");
            excelFile = new File(baseDir + "/" + System.currentTimeMillis() + ".xlsx");
            ExcelUtil.create07ExcelFile(excelFile, sheets);
            FileUtil.downloadFile(response, fileName + ".csv", excelFile);
        } catch (IOException e) {
            // log.error(e.getMessage(), e);
        } finally {
            if (excelFile != null && excelFile.exists()) {
                try {
                    excelFile.delete();
                } catch (Exception e) {
                    log.error("downloadExcel生成报表发生错误,excelCode={}", fileName);
                }
            }
        }
    }
}

enum DspCellStyleType {
	BLUETITLE, YELLOWTITLE, DATE, STRING, INTEGER, DOUBLE, PERCENT, MONEY
}
