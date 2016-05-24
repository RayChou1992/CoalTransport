package com.zhiren.wuljg.utils;

import android.os.Environment;

/**
 * 常量
 */
public class Constant {

	/** 网络访问状态标识 **/
	public final static int ERROR = 0;
	public final static int SUCCESS = 1;
	public final static int SUCCESS_CODE = 2;
	public final static int EXCEPTION = -1;

	public final static String APPKEY = "AndroidAppKey";
	public final static String API_VERSION = "1.0";// 接口版本
	public final static String VERSION_NAME = "v1.0";// app版本名称
	// 测试环境
	public final static String SECURITYCODE = "CE718A2D-0A92-2369-7C57-46CCEBB02989";// 测试md5密码
	// public final static String BASE_URL = "http://10.115.25.36:8888/wuljg";//
	// 老吕测试
	// public final static String BASE_URL_PIC = "http://10.115.25.36:8888";//
	// 老吕测试
//	public final static String BASE_URL = "http://10.115.25.120/wuljg";// 测试
//	public final static String BASE_URL_PIC = "http://10.115.25.120";// 测试
//	public final static String BASE_URL = "http://112.124.70.50/wuljg-test";// 外网测试
//	public final static String BASE_URL_PIC = "http://112.124.70.50/";// 外网测试
	 public final static String BASE_URL = "http://10.115.25.13:8080/wuljg";// 晓波测试
	 public final static String BASE_URL_PIC = "http://10.115.25.13:8080/";// 晓波测试
	// public final static String BASE_URL =	// "http://10.115.25.218:8080/wuljg";//
	// public final static String BASE_URL_PIC = "http://10.115.25.218:8080/";//

	/** 登录 **/
	public final static String URL_LOGIN = BASE_URL + "/app/login";

	/** 菜单 **/
	public final static String URL_MENU = BASE_URL + "/app/xuns/getMenu";
	// public final static String URL_MENU = BASE_URL + "/web/app/duoweibyjw";

	/** 获取定位信息 **/
	public final static String URL_LOCATION = BASE_URL
			+ "/app/xuns/getLocationData";

	/** 获取垛位信息 **/
	public final static String URL_DUOWEI_DATA = BASE_URL
			+ "/app/xuns/getDuoWeiData";

	/** 上传图片及信息 **/
	public final static String URL_UPLOAD = BASE_URL + "/app/xuns/upload";

	/** 上传短倒 拍照 图片 **/
	public final static String URL_UPLOADTPXX = BASE_URL + "/app/uploadTpxx";

	/** 获取当前巡视批次或者创建新的巡视批次 **/
	public final static String URL_CREATE_OR_GET_XUNDUO = BASE_URL
			+ "/app/xuns/getBathNumber";

	/** 获取图片地址 **/
	public final static String URL_GET_IMG_URI = BASE_URL
			+ "/app/xuns/getPicture";
	public final static String URL_FINISH_XUNDUO = BASE_URL
			+ "/app/xuns/alterXunduo";

	/*** 图片根路径 sd卡/JKD/ */
	public final static String BASE_PIC_URI = Environment
			.getExternalStorageDirectory() + "/zhiren/";

	/************************** 船运接口 ***************************/

	/** 查询船运信息 **/
	public final static String URL_GET_CHUANYUN_INFO = BASE_URL
			+ "/app/chuanyfy/getchuancqk";
	public final static String URL_GET_CHUANYUN_STATUS = BASE_URL + "/app/";

	/** 获取船运信息 **/
	public final static String URL_CHUANYUN_DATA = BASE_URL
			+ "/app/chuanyfy/getChuanyfyLocationData";

	/** 接车 -- 获取发车拍的照片 **/
	public final static String URL_JIECHE_DATA = BASE_URL
			+ "/app/getDDtupByTongdId";

	public final static String URL_GET_SHUICL = BASE_URL
			+ "/app/chuanyfy/getshuicl";
	
	/**  上传  皮带  数量  **/
	public final static String URL_UPDATA_SHULIANG = BASE_URL
			+ "/app/chuanyfy/saveBangdxx";

}
