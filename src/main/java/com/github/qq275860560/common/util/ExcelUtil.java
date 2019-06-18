package com.github.qq275860560.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.github.qq275860560.common.filter.ExceptionFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class ExcelUtil {
	 
	private ExcelUtil() {
	}

	// 如果最终希望转换为list或者数组，可先调用此方法，进行简单转换
	// String jsonArrayString = ExcelUtil.readFromExcel(properties, xlsFile);
	// ObjectMapper mapper = new ObjectMapper();
	// mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	// list<Student> list2 =
	// mapper.readValue(jsonArrayString,mapper.getTypeFactory().constructParametricType(List.class,
	// Student.class));

	//把excel表的数据转为json数组字符串,使用例子见单元测试
	// @param properties excel表中，每列的英文名称,比如new String[]{"a","b","c"}
	// @param xlsFile 带头部的excel表，每页的第一行是列名称，第二行开始才是真正的数据,比如execl表有1页4行，其实数据区才3行，返回的json数组是3个元素
	// @return 返回json数组     [{"a":"1","b":"2","c":"3"},{"a":"10","b":"20","c":"30"},{"a":"11","b":"22","c":"33"}]
	public static List<Map<String, String>> readFromExcelToList(String[] properties, File xlsFile) {
		String extension = FilenameUtils.getExtension(xlsFile.getName());
		Workbook workbook = null;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(xlsFile);
			if ("xls".equals(extension)) {
				workbook = new HSSFWorkbook(inputStream);
			} else if ("xlsx".equals(extension)) {
				workbook = new XSSFWorkbook(inputStream);
			} else {
				return null;
			}
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Iterator<Sheet> iterator = workbook.sheetIterator();
			while (iterator.hasNext()) {
				Sheet xssfSheet = iterator.next();
				int lastRowNum = xssfSheet.getLastRowNum();
				log.debug("当前页数据总行数lastRowNum=" + lastRowNum);// 每页真实行数还有第0行
				for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {// 第0行通常为标题行，不读取
					if (xssfSheet.getRow(rowIndex) == null)
						continue;
					Row xssfRow = xssfSheet.getRow(rowIndex);
					short lastCellNum = xssfRow.getLastCellNum();
					Map<String, String> map = new HashMap<String, String>();
					for (int cellNum = 0; cellNum < lastCellNum && cellNum < properties.length; cellNum++) {
						Cell xssfCell = xssfRow.getCell(cellNum);
						String value = getCellValue(xssfCell);
						map.put(properties[cellNum], value);
					}
					list.add(map);
				}
				break;// 只读取第一页，如果第二页也要读取，把该行去掉
			} // 每页

			return list;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (workbook != null)
				try {
					workbook.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
		}
		return null;
	}

	public static String readFromExcel(String[] properties, File xlsFile) {
		List<Map<String, String>> list = readFromExcelToList(properties, xlsFile);
		return JsonUtil.toJSONString(list);
	}

	private static String getCellValue(Cell cell) {
		if (cell == null)
			return null;
		String ret;
		switch (cell.getCellTypeEnum()) {
		case BLANK:
			ret = "";
			break;
		case BOOLEAN:
			ret = String.valueOf(cell.getBooleanCellValue());
			break;
		case ERROR:
			ret = null;
			break;
		case _NONE:
			ret = null;
			break;
		case FORMULA:
			Workbook wb = cell.getSheet().getWorkbook();
			CreationHelper crateHelper = wb.getCreationHelper();
			FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
			ret = getCellValue(evaluator.evaluateInCell(cell));
			break;
		case NUMERIC:
			ret = NumberToTextConverter.toText(cell.getNumericCellValue());
			break;
		case STRING:
			ret = cell.getRichStringCellValue().getString();
			break;
		default:
			ret = null;
		}

		return ret; // 有必要自行trim
	}

	public static String getValue(File xlsFile, String sheetName, int row, int col) {
		String extension = FilenameUtils.getExtension(xlsFile.getName());
		Workbook workbook = null;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(xlsFile);
			if ("xls".equals(extension)) {
				workbook = new HSSFWorkbook(inputStream);
			} else if ("xlsx".equals(extension)) {
				workbook = new XSSFWorkbook(inputStream);
			} else {
				return null;
			}

			Sheet xssfSheet = workbook.getSheet(sheetName);

			return getCellValue(xssfSheet.getRow(row).getCell(col));
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (workbook != null)
				try {
					workbook.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
		}
		return null;
	}

	public static int getRowCount(File xlsFile, String sheetName) {
		String extension = FilenameUtils.getExtension(xlsFile.getName());
		Workbook workbook = null;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(xlsFile);
			if ("xls".equals(extension)) {
				workbook = new HSSFWorkbook(inputStream);
			} else if ("xlsx".equals(extension)) {
				workbook = new XSSFWorkbook(inputStream);
			} else {
				return 0;
			}

			Sheet xssfSheet = workbook.getSheet(sheetName);
			return xssfSheet.getLastRowNum() + 1;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (workbook != null)
				try {
					workbook.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
		}
		return 0;
	}

	public static void writeToExcel(String[] headers, String[] properties, String jsonArrayString, File xlsFile) {
		List<Map<String, Object>> list = JsonUtil.parse(jsonArrayString, List.class);
		if (list.isEmpty())
			return;// 没有数据直接退出
		writeToExcel(headers, properties, list, xlsFile);
	}

	// 如果是list或者数组要调用此方法，可转换成json字符串再调用
	// ObjectMapper mapper = new ObjectMapper();
	// mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	// ExcelUtil.writeToExcel(headers,properties, mapper.writeValueAsString(list),
	// xlsFile);
	//
	// String[] headers = new
	// String[]{"银行账号","银行id","门店名称","银行名称","总金额","订单数量","所有总金额","订单来源列表","支付码列表","支付信息类型列表"};
	// String[] properties = new
	// String[]{"branchAcountNo","branchId","enterpriseName","branchName","sumAmount","orderCount","sumAllAmount","orderFromListStr","payWayCodeListStr","fundIntoTypeListStr"};
	// File file = new File(System.getProperty("java.io.tmpdir") + File.separator +
	// SessionGeneratorUtil.generateSession()+ ".xls");
	// ExcelUtil.writeToExcel(headers, properties, mapper.writeValueAsString(list),
	// file);
	// log.info(file.getAbsolutePath());

	//输出到文件 把json数组字符串换成excel文件 ,其中，每页的开头一行为中文名称(非数据),使用例子见单元测试,支持百万级别数据导入
	// @param headers 每列第一行名称，比如new String[]{"姓名","年龄","性别"}
	// @param properties 每列的属性，要跟json数组元素的属性名一致，比如new String[]{"name","age","sex"}
	// @param xlsFile 转换后的excel文件	
	//
	public static void writeToExcel(String[] headers, String[] properties, List<Map<String, Object>> list,
			File xlsFile) {
		SXSSFWorkbook workbook = null;// 缓存;
		OutputStream outputStream = null;
		try {

			workbook = generateSXSSWorkbook(headers, properties, list);
			outputStream = new FileOutputStream(xlsFile);
			workbook.write(outputStream);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (workbook != null)
				try {
					workbook.close();
					workbook.dispose();
				} catch (Exception e2) {
					log.error("", e2);
				}
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
		}
	}

	public static void writeOutputStream(String[] headers, String[] properties, List<Map<String, Object>> list,
			OutputStream outputStream) throws IOException {

		SXSSFWorkbook workbook = null;// 缓存;
		try {
			workbook = generateSXSSWorkbook(headers, properties, list);
			workbook.write(outputStream);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (workbook != null)
				try {
					workbook.close();
					workbook.dispose();
				} catch (Exception e2) {
					log.error("", e2);
				}
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
		}
	}

	public static void writeToHttpServletResponse(String[] headers, String[] properties, List<Map<String, Object>> list,
			HttpServletResponse response) throws IOException {
		SXSSFWorkbook workbook = null;// 缓存;
		try {
			workbook = generateSXSSWorkbook(headers, properties, list);
			;
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (workbook != null)
				try {
					workbook.close();
					workbook.dispose();
				} catch (Exception e2) {
					log.error("", e2);
				}
			if (response.getOutputStream() != null)
				try {
					response.getOutputStream().close();
				} catch (Exception e2) {
					log.error("", e2);
				}
		}
	}

	private static SXSSFWorkbook generateSXSSWorkbook(String[] headers, String[] properties,
			List<Map<String, Object>> list) {
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);// 缓存;
		workbook.setCompressTempFiles(true);
		SXSSFSheet sheet = null;
		int rowIndex = 1;// 记录每页数据行的行号，第0行写入列名headers[],第一行开始写入数据
		for (int count = 0, size = list.size(); count < size; count++) {
			if (count % 65535 == 0) {
				sheet = workbook.createSheet();
				SXSSFRow headerRow = sheet.createRow(0);
				for (int j = 0; j < headers.length; j++) {
					sheet.setColumnWidth(j, 20 * 256);// 设置列宽为20
					headerRow.createCell(j).setCellValue(headers[j]);// 每页第0行写入列名headers[]
				}
				rowIndex = 1;// 每页第1行开始写入数据
			}
			Map<String, Object> map = list.get(count);
			SXSSFRow dataRow = sheet.createRow(rowIndex);
			for (int i = 0; i < properties.length; i++) {
				SXSSFCell newCell = dataRow.createCell(i);
				Object o = map.get(properties[i]);
				String value = (o == null) ? "" : o.toString();
				newCell.setCellValue(value);
			}
			rowIndex++;
		}
		return workbook;
	}

}
