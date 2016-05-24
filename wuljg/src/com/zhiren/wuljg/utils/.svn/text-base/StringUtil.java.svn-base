package com.zhiren.wuljg.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;

public class StringUtil {
	private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	private final static SimpleDateFormat dateFormater4 = new SimpleDateFormat("MM-dd", Locale.CHINA);

	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input)||"null".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}
	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String nowDateTime() {
		Date curDate = new Date(System.currentTimeMillis());
		return dateFormater.format(curDate);
	}
	/**
	 * emoji 表情过滤
	 * 
	 * @return
	 */
	public static InputFilter emojiFilter() {
		return new InputFilter() {

			Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				Matcher emojiMatcher = emoji.matcher(source);
				if (emojiMatcher.find()) {
					return "";
				}
				return null;
			}
		};
	}
	/**
	 * 转全角 
	 * @param input
	 * @return
	 */
	public static String toString(String input){
		input.replace(" ", "");
		char[] c = input.toCharArray();  
		   for (int i = 0; i< c.length; i++) {  
		       if (c[i] == 12288) {  
		         c[i] = (char) 32;  
		         continue;  
		       }if (c[i]> 65280&& c[i]< 65375)  
		          c[i] = (char) (c[i] - 65248);  
		       }  
		   return new String(c).trim();  
	}
}
