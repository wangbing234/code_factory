package com.doc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 把字符串转换成Date对象
	 * 
	 * @param dateStr
	 * @param pattern
	 *            日期模式，默认为DEFAULT_DATE_FORMAT
	 * @return Date对象
	 */
	public static Date parseDate(String dateStr) {
		if (-1 == dateStr.indexOf("-")) {
			return parseDate(dateStr, "yyyyMMdd");
		} else {
			return parseDate(dateStr, DEFAULT_DATE_FORMAT);
		}
	}

	public static Date parseDate(String dateStr, String pattern) {
		if (dateStr == null || "".equals(dateStr)) {
			return null;
		}
		if (pattern == null || "".equals(pattern)) {
			return null;
		}
		Date date;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 *            Date对象或者是String对象
	 * @param pattern
	 *            日期格式，默认为DEFAULT_DATE_FORMAT
	 * @return 日期字符串
	 */
	public static String formatDate(Date date) {
		return formatDate(date, DEFAULT_DATE_FORMAT);
	}

	public static String formatDatetime(Date time) {
		return formatDate(time, DEFAULT_DATETIME_FORMAT);
	}

	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		if (pattern == null || "".equals(pattern)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 日期+偏移量
	 * 
	 * @param date
	 *            Date对象
	 * @param offset
	 *            偏移量
	 * @return 日期
	 */
	public static Date offsetDate(Date date, int offset) {
		return offsetDate(date, offset, Calendar.DAY_OF_YEAR);
	}

	/**
	 * 日期+偏移量
	 * 
	 * @param date
	 *            Date对象
	 * @param offset
	 *            偏移量
	 * @return 日期
	 */
	public static Date offsetDate(Date date, int offset, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, offset);
		return calendar.getTime();
	}

	/**
	 * 日期间隔天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long betweenDays = Math.abs(time2 - time1) / (1000 * 3600 * 24) + 1;
		return (int) betweenDays;
	}

	/**
	 * 清除“秒”域
	 * 
	 * @param date
	 *            时间
	 */
	public static Date clearSecond(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 清除某时间域
	 * 
	 * @param date
	 *            时间
	 * @param field
	 *            域
	 */
	public static Date clearField(Date date, int field) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(field, 0);
		return cal.getTime();
	}

	public static void main(String[] args) {
		System.out.println(new Date().getTime());
		System.out.println(DateUtil.clearField(new Date(), Calendar.SECOND).getTime());
		System.out.println(DateUtil.clearSecond(new Date()).getTime());
		System.out.println(DateUtil.formatDate(new Date()));
		System.out.println(DateUtil.formatDate(DateUtil.clearSecond(new Date())));
		System.out.println(DateUtil.formatDate(DateUtil.offsetDate(new Date(), -5)));
		System.out.println(DateUtil.formatDate(DateUtil.offsetDate(DateUtil.parseDate("2015-12-28"), 5)));
	}

}
