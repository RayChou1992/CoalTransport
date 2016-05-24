package com.zhiren.wuljg.utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TimeUtils {
	
	/**
	 * 获取剩余时间的时、分、秒,
	 * 
	 * @param seconds
	 * @return
	 */
	
	public static String getRemainSecString(long second) {
		Long msecond =  second ;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd&HH:mm:ss");// 初始化Formatter的转换格式
		String remainStr = formatter.format(msecond);
		return remainStr;
	}
	
	/**
	 * 获得形如 19770608 格式的当前年月日
	 * 
	 * @return 当前年月日
	 */
	public static String getSimpleCurrentDate() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return sdf.format(new java.util.Date());
	}
}
