package com.zhiren.wuljg.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class TimeUtil {

	/**
	 * 获取系统当前时间
	 */
	public static  String getDate() {

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy_MM_dd_HH_mm_ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		return time;
	}
}
