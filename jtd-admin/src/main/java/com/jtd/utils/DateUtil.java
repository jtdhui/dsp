package com.jtd.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>日期工具类</p>
 */
public class DateUtil {
	
	private static final Log log = LogFactory.getLog(DateUtil.class);
	
	private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd");
		}
	};
    /**
     * 获取时分秒时间字符串
     * @param date
     * @return
     */
	public static String getHhMmSs(Date date){
		if(null == date){
			date = new Date();
		}
	  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	  String hms = sdf.format(date);
	  return hms;
    }

	/**
	 * 将日期转为int
	 * 
	 * @param date
	 * @return
	 */
	public static int getDateInt(Date date) {
		String dateStr = getYYYYMMdd(date);
		return Integer.parseInt(dateStr);
	}

	/**
	 * 获取两个时间相差的天数
	 * 
	 * @param dateStr1
	 * @param dateStr2
	 * @return
	 */
	public static int getDiffDays(String dateStr1, String dateStr2) {
		Date date1 = getDate(dateStr1);
		Date date2 = getDate(dateStr2);
		int diffDays = getDiffDays(date1, date2);
		return diffDays;
	}

	/**
	 * 获取两个时间相差的天数 比较第二个日期比第一个日期大多少天
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDiffDays(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new RuntimeException("输入参数为null");
		}
		Long day = null;
		day = (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);

		return Integer.parseInt(day.toString());
	}

	/**
	 * 时间字符串转为事件对象
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getDate(String dateStr) {
		SimpleDateFormat sdf = threadLocal.get();
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			log.warn("解析时间字符串[" + dateStr + "]失败");
		}
		return date;
	}

	/**
	 * 时间对象转为时间字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getYYYYMMdd(Date date) {
		SimpleDateFormat sdf = threadLocal.get();
		return sdf.format(date);
	}
	
	/**
	 * 时间对象转为时间字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateStr(Date date,String partten) {
		SimpleDateFormat sdf = new SimpleDateFormat(partten);
		return sdf.format(date);
	}

	/**
	 * 将date增加或减少一定天数
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNextDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}

	/**
	 * 获取日期所在月份的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int maxDays = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DAY_OF_MONTH, maxDays);
		return calendar.getTime();
	}

	/**
	 * 获取日期所在月份的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 获取日期所在周的周一
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return calendar.getTime();
	}

	/**
	 * 获取日期所在周的周日
	 * 
	 * @param date
	 * @return
	 */
	public static Date getSunday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return calendar.getTime();
	}

	/**
	 * 获取是周几 <br>
	 * 周一的值是2，周二的值是3，以此类推，周日的值是1 <br>
	 * 日期类一周开始的日期是周日
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 是否是周一
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isMonday(Date date) {
		int dayOfWeek = getDayOfWeek(date);
		return dayOfWeek == Calendar.MONDAY;
	}

	/**
	 * 是否是周日
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isSunday(Date date) {
		int dayOfWeek = getDayOfWeek(date);
		return dayOfWeek == Calendar.SUNDAY;
	}

	/**
	 * 获取是本月的第几天
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取是本年的第几天
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取上个月的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getPreviousMonthFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 获取上个月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getPreviousMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month - 1);
		int maxDays = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DAY_OF_MONTH, maxDays);
		return calendar.getTime();
	}

	/**
	 * 获取日期所在月份 <br>
	 * 月份从0开始， 0表示一月，11表示十二月
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH);
	}

	/**
	 * 判断是否是某个月的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isMonthFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return day == 1;
	}

	/**
	 * 判断是否是某个月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int maxDays = calendar.getActualMaximum(Calendar.DATE);
		return day == maxDays;
	}

	/**
	 * 是否是今天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Calendar today = Calendar.getInstance();
		return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
				&& today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * 获取到今天00:00的时间
	 * 
	 * @return
	 */
	public static Date getTodayZeroTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	/**
	 * 得到时
	 * @return
	 */
    public static int getHourByDay(){
    	Calendar calendar = Calendar.getInstance();
    	return calendar.get(Calendar.HOUR_OF_DAY);
    }
 
	public static int getWeekDayInt(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int index = calendar.get(Calendar.DAY_OF_WEEK);
		index = index - 1;
		if (index == 0) {
			index = 7;
		}
		return index;
	}

	public static int getMonthDayInt(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int index = calendar.get(Calendar.DAY_OF_MONTH);
		return index;
	}

	public static Date getYestoday() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, -1);
		return now.getTime();
	}

	public static String getYestodayStr() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, -1);
		SimpleDateFormat sdf = threadLocal.get();
		return sdf.format(now.getTime());
	}
	
	public static void main(String[] a){
		System.out.println(getDateStr(new Date(),"yyyy-MM-dd"));
	}
}
