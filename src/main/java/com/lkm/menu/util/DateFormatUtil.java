package com.lkm.menu.util;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期格式工具类
 * @author wuhda
 *
 */
public class DateFormatUtil {
	
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static Date getCurrentDate(){
		Calendar date = Calendar.getInstance();
		return date.getTime();
	}

	public static String getTimeAfterHours(int i)
	{
		Calendar date = Calendar.getInstance();

		date.set(Calendar.HOUR_OF_DAY,date.get(Calendar.HOUR_OF_DAY)+i);
		
		String format = "yyyy-MM-dd HH:mm:ss";
		
		SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
		String strdate = df.format(date.getTime());// new Date()为获取当前系统时间，也可使用当前时间戳
		return strdate;
	}
	/**
	 * 获取当前年
	 * @return
	 */
	public static String getCurrentYear(){
		Calendar date = Calendar.getInstance();
	    String year = String.valueOf(date.get(Calendar.YEAR));
	    return year;
	}
	
	/**
	 * 获取当前月
	 * @return
	 */
	public static String getCurrentMonth(){
		Calendar date = Calendar.getInstance();
	    String day = String.valueOf(date.get(Calendar.MONTH) + 1);
	    return day;
	}
	
	
	/**
	 * 获取当前天
	 * @return
	 */
	public static String getCurrentDay(){
		Calendar date = Calendar.getInstance();
	    String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
	    return day;
	}
	
	public static String[] getPreYearCperiod(String cyear, String cperiod){
		String[] strs = new String[2];
		GregorianCalendar date = new GregorianCalendar(Integer.valueOf(cyear), Integer.valueOf(cperiod), 0);
		date.add(Calendar.MONTH, -2);//获取上个月月份
		String year = String.valueOf(date.get(Calendar.YEAR));
		String month = String.valueOf(date.get(Calendar.MONTH) + 1);
		if(Integer.valueOf(month) <= 9){
			month = "0"+month;
		}
		strs[0] = year;
		strs[1] = month;
		return strs;
	}
	
	/**
	 * 获取年月（2017.10）这种格式
	 * @return
	 */
	public static String getYearMonthStr(String value){
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
		} catch (ParseException e) {
			
		}
		String str = new SimpleDateFormat("yyyy.MM").format(date);
		return str;
	}
	
	/**
	 * 获取制定日期的天
	 * @return
	 */
	public static int getDay(String specifiedDay){
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			
		}
		c.setTime(date);
	   	int day = c.get(Calendar.DAY_OF_MONTH);
	    return day;
	}
	
	  /**
	   * 获得指定日期的前一天 
	   * @param specifiedDay
	   * @return
	   */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);
		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}
	 
	public static String getDateTime(String format){
		if(format == null || format.length() == 0){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		return date;
	}

	/**
	 * <p>系统当前时间</p>
	 * <p>格式：yyyy-MM-dd HH:mm:ss</p>
	 * @return 系统当前时间
	 */
	public static String getDateTime(){
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		return date;
	}
	
	
	public static String getFormatDate(long currentTimeMillis , String format){
		if(format == null || format.length() == 0){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
		String date = df.format(currentTimeMillis);// new Date()为获取当前系统时间，也可使用当前时间戳
		return date;
	}
	
	public static String getFormatDate(long currentTimeMillis){
		return DateFormatUtil.getFormatDate(currentTimeMillis, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date getDateTime(String datestr, String format){
		Date date=null;
		if(format == null || format.length() == 0){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);//设置日期格式
		try {
			date=sdf.parse(datestr);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		return date;
	}
	/** format 默认 yyyy-MM-dd
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateStr(Date date, String format){
		if(format == null){
			format = DEFAULT_DATE_FORMAT;
		}
		SimpleDateFormat text_format = new SimpleDateFormat(format);
		return text_format.format(date);
	}
	public static Date getDate(String datestr, String format){
		Date date=null;
		if(format == null || format.length() == 0){
			format = DEFAULT_DATE_FORMAT;// "yyyy-MM-dd" "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);//设置日期格式
		try {
			date=sdf.parse(datestr);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		return date;
	}
	public static String getPreWeekDateFormatStr(String format){
		if(format == null || format.length() == 0){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		Calendar c = Calendar.getInstance();
	    //过去七天
	    c.setTime(new Date());
	    c.add(Calendar.DATE, - 7);
	    Date d = c.getTime();
	    SimpleDateFormat df = new SimpleDateFormat(format);
	    String date = df.format(d);
		return date;
	}
	
	public static String getPreDayDateFormatStr(String format, int day){
		if(format == null || format.length() == 0){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		Calendar c = Calendar.getInstance();
	    //过去day天
	    c.setTime(new Date());
	    c.add(Calendar.DATE, - day);
	    Date d = c.getTime();
	    SimpleDateFormat df = new SimpleDateFormat(format);
	    String date = df.format(d);
		return date;
	}

	//获取指定日期的前N天的日期 #5706 liuyi 20200225
	public static String getPreDayDateByDate(String strDate, int N){
		String preDate = "";
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		c.setTime(date);
		int day1 = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day1 - N);
		preDate = sdf.format(c.getTime());
		return preDate;
	}

	public static String getDateFormatStr(String format){
		
		if(format == null || format.length() == 0){
			format = "yyyy-MM-dd";
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		String date = df.format(new Date());
		return date;
	}
	
	public static Integer comparedate(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * <p>时间1 > 时间2 return 1</p>
	 * <p>时间1 < 时间2 return -1</p>
	 * <p>否则 return 0</p>
	 * @param date1 时间1
	 * @param date2 时间2
	 * @param format 时间格式
	 * @return
	 */
	public static Integer comparedate(String date1, String date2, String format) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * @function 日期格式的字符串转日期
	 * @param str {@link String} 日期格式的字符串
	 * @return {@link Date}
	 * @author MaxSoft
	 */
	public static Date strToDate(String str){
		Assert.notNull(str);
		DateFormat parse = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		try {
			return parse.parse(str);
		} catch (ParseException e) {
			
		}
		return null;
	}
	
	public static Date parse(String strDate) throws ParseException {  
	       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	       return sdf.parse(strDate);  
	  }  
	 
	//由出生日期获得年龄  
    public static int getAge(String birthDay) throws ParseException {  
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {  
            throw new IllegalArgumentException(  
                    "The birthDay is before Now.It's unbelievable!");  
        }  
        int yearNow = cal.get(Calendar.YEAR);  
        int monthNow = cal.get(Calendar.MONTH);  
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);  
        cal.setTime(parse(birthDay));
        int yearBirth = cal.get(Calendar.YEAR);  
        int monthBirth = cal.get(Calendar.MONTH);  
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;  
        if (monthNow <= monthBirth) {  
            if (monthNow == monthBirth) {  
                if (dayOfMonthNow < dayOfMonthBirth) age--;  
            }else{  
                age--;  
            }  
        }  
        return age;  
    }
    
	/**
	 * @function 日期格式的字符串转时间格式字符串
	 * @param str {@link String} 日期格式的字符串
	 * @return {@link Date}
	 * @author wyj
	 */
	public static String strToDateTime(String str){
		Assert.notNull(str);
		if(null == str||"".equals(str)){
			return null;
		}
		try {
			SimpleDateFormat sdfbf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			Date datebf = sdfbf.parse(str);
			SimpleDateFormat sdfaf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
			return  sdfaf.format(datebf);
		} catch (Exception e) {
			
		}
		return null;
	}
	
	/**
	 * 
	*描述: 判断日期是否早于当月
	*@param effectdate
	*@return
	*@throws ParseException boolean
	 */
	public static boolean isBeforeCurMonth(String effectdate) throws ParseException {
		if(StringUtils.isEmpty(effectdate)){
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse(effectdate));
		int effectYear = cal.get(Calendar.YEAR);
		int effectMonth = cal.get(Calendar.MONTH);
		Calendar date = Calendar.getInstance();
		int sysYear = date.get(Calendar.YEAR);
		int sysMonth = date.get(Calendar.MONTH);
 		if(effectYear<sysYear||(effectYear==sysYear && effectMonth<sysMonth)){
 			return true;
 		}
		return false;
	}
	
	
	/**
	 * 获得该月最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return lastDayOfMonth;
	}
	
	public static String dateToString(Date date, String format){
		if(format == null){
			format = DEFAULT_DATETIME_FORMAT;
		}
		SimpleDateFormat text_format = new SimpleDateFormat(format);
		return text_format.format(date);
	}

	public static void main(String[] args) {
		
	}
	

    /**计算时间差值 
     * @param start   startdate.geTime()
     * @param end end.getTime()
     * @return   day + "天" + hour + "时" + min + "分" + s + "秒" 
     */
    public static String TimeDifference(long start, long end) {
        long between = end - start;
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                - min * 60 * 1000 - s * 1000);
//        String timeDifference = day + "天" + hour + "时" + min + "分" + s + "秒" + ms
//                + "毫秒";
        String timeDifference = day + "天" + hour + "时" + min + "分" + s + "秒" ;
        return timeDifference;
    }

	/**
	 * 改变日期天数（默认格式 YY-MM-DD）
	 * @param date 需要改变的日期
	 * @param amount 增减的天数
	 * @return
	 */
	public static String changeDate(String date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(strToDate(date));
		c.add(Calendar.DAY_OF_MONTH, amount);
		return dateToString(c.getTime(), DEFAULT_DATE_FORMAT);
	}

	/**
	 * 获取当天0点Date对象
	 * @param date
	 * @return
	 */
	public static Date getStartOfDay(Date date) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
		LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}


	/**
	 * 获取当天特定时间Date对象
	 * @param date
	 * @param time
	 * @return
	 */
	public static Date getDefOfDay(Date date, String time) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
		LocalDateTime startOfDay = localDateTime.with(LocalTime.parse(time));
		return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}

}
