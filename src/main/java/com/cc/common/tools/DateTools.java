/**
 * 
 */
package com.cc.common.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cc.common.exception.LogicException;

/**
 * 时间处理工具类
 * @author Administrator
 *
 */
public class DateTools {

	/**
	 * format:yyyy-MM-dd HH:mm:ss
	 */
	public static String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * format:yyyyMMddHHmmss
	 */
	public static String DATEFORMAT2 = "yyyyMMddHHmmss";
	
	/**
	 * format:yyyy-MM-dd
	 */
	public static String DATEFORMAT3 = "yyyy-MM-dd";
	/**
	 * 格式化时间，返回日期字符串
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String getFormatDate(Date date, String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(date);
	}
	
	/**
	 * 当前时间
	 * @return
	 */
	public static Date now(){
		return new Date();
	}
	
	/**
	 * 获取两个日期之间的间隔时间
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getInterval(Date date1, Date date2){
		if (date1 == null || date2 == null) {
			throw new LogicException("E0001","日期不能为空");
		}
		return (date1.getTime()-date2.getTime());
	}
	
	/**
	 * 日期字符串转换成date
	 * @param dateString
	 * @return
	 */
	public static java.util.Date getDate(String dateString) {
		if ((dateString == null) || ("".equals(dateString))) {
			return null;
		}
		String format1_ = "\\d{4}-\\d{1,2}-\\d{1,2}\\s{1}\\d{1,2}:\\d{1,2}:\\d{1,2}";
		String format2_ = "\\d{14}";
		String format3_ = "\\d{4}-\\d{1,2}-\\d{1,2}";
		String format = null;
		if (dateString.matches(format1_)) {
			format = DATEFORMAT;
		}
		if (dateString.matches(format2_)) {
			format = DATEFORMAT2;
		}
		if (dateString.matches(format3_)) {
			format = DATEFORMAT3;
		}
		Date date = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat(format);
			date = df.parse(dateString);
		} catch (Exception e) {
			date = null;
		}
		return date;
	}
}
