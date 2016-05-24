package com.zhiren.wuljg.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhiren.wuljg.ZhiRen_Application;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class NetUtil {

	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	public static final String CHARSET_NAME = "UTF-8";

	/**
	 * 
	 * 判断是否联网。
	 * 
	 * @return boolean
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();

		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtil.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase(Locale.ENGLISH).equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/**
	 * 获取入参参数
	 * 
	 * @param context
	 * @param appParams
	 * @return
	 */
	public static String getRequestParams(Context context,
			TreeMap<String, Object> appParams) {
		ZhiRen_Application appContext = (ZhiRen_Application) context
				.getApplicationContext();

		String mobilePhone= ZhiRen_Application.getApplication().getPhoneNum();
		
		TreeMap<String, Object> sysParams = new TreeMap<String, Object>();
		sysParams.put("appkey", Constant.APPKEY);
		sysParams.put("reqtime", System.currentTimeMillis() / 1000 + "");// 时间戳
		sysParams.put("apiversion", Constant.API_VERSION);// 接口版本
		// sysParams.put("versiontype", "Android"); //设备类型
		if (!StringUtil.isEmpty(appContext.getPackageInfo().versionName)) {
			sysParams
					.put("appversion", appContext.getPackageInfo().versionName);// app
																				// version
																				// name
		} else {
			sysParams.put("appversion", Constant.VERSION_NAME);
		}
		sysParams.put("refid", "an");
		/*
		 * if(!StringUtil.isEmpty(appContext.getApplicationInfo().metaData.getString
		 * ("dd"))){ sysParams.put("refid",
		 * appContext.getApplicationInfo().metaData.getString("dd")); //渠道名称
		 * }else{ sysParams.put("refid", "mengyou88"); }
		 */
		sysParams.put("devicename", android.os.Build.MANUFACTURER
				+ android.os.Build.MODEL);
		sysParams.put("deviceid", appContext.getDeviceID()); // 设备id
		sysParams.put("devicemac", appContext.getMacAddress()); // clientip
		sysParams.put("longitude", "");
		sysParams.put("latitude", "");
		sysParams.put("mobielphone", mobilePhone);
		
		JSONObject requesstObject = new JSONObject();
		JSONObject sysObject = new JSONObject();
		JSONObject appObject = new JSONObject();
		try {
			for (String name : sysParams.keySet()) {
				sysObject.put(name, URLEncoder.encode(sysParams.get(name)
						.toString(), CHARSET_NAME));
				// sysObject.put(name, sysParams.get(name).toString());
				// URLEncoder.encode(sysParams.get(name).toString(),CHARSET_NAME);
			}
			for (String name : appParams.keySet()) {
				appObject.put(name, appParams.get(name));
			}
			requesstObject.put("SysData", sysObject);
			requesstObject.put("AppData", appObject);
		} catch (JSONException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return requesstObject.toString();
	}

	/**
	 * 生成服务端请求url
	 * 
	 * @param context
	 * @param url
	 *            服务端url
	 * @param params
	 *            入参参数
	 * @return
	 * @throws Exception 
	 */
	public static String MakeUrl(Context context, String url,
			TreeMap<String, Object> params) throws Exception {
		StringBuilder sb = new StringBuilder();
		url = sb.append(url).append("?")// append("?sign=")
				.append(URLEncoder.encode((getRequestParams(context, params).toString()), "utf-8"))
				.toString();
		
		return url ;
	}

	public static String MakeParams(Context context,
			TreeMap<String, Object> appParams) {
		return getRequestParams(context, appParams);
	}

	/**
	 * 请求服务端 post方法
	 * 
	 * @param context
	 * @param url
	 *            添加签名的url
	 * @param appParams
	 *            请求入参json数据格式
	 * @return 返回值String
	 * @throws AppException
	 */

	public static String http_post_server(Context context, String url,
			String requestParams) throws AppException {
		String responseBody = "";
		CloseableHttpClient httpClient = HttpClients.custom()
				.useSystemProperties().build();
		try {
			HttpPostHC4 httpPostHC4 = new HttpPostHC4(url);
			httpPostHC4.addHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8");
			httpPostHC4.addHeader("Accept-Language",
					"zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
			StringEntity se = new StringEntity(requestParams);
			se.setContentType("text/json");
			httpPostHC4.setEntity(se);
			CloseableHttpResponse response = httpClient.execute(httpPostHC4);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw AppException.http(statusCode);
			}
			HttpEntity entity = response.getEntity();
			responseBody = EntityUtils.toString(entity, CHARSET_NAME);
			return responseBody;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw AppException.http(e);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw AppException.http(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw AppException.io(e);
		} catch (AppException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw AppException.io(e);
			}
		}
		return responseBody;
	}

	/**
	 * 上传文件
	 * 
	 * @param context
	 * @param url
	 * @param filepath
	 *            文件路径
	 * @return
	 * @throws AppException
	 */
	public static boolean http_post_server_upload(String url, String filepath)
			throws AppException {
		// CloseableHttpClientt httpClient =
		// HttpClients.custom().useSystemProperties().build();
		// HttpClient
		// httpClient=HttpClients.custom().useSystemProperties().build();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		// httpPost.addHeader(HTTP.CONTENT_TYPE, "application/octet-stream");
		FileEntity fileEntityHC4 = new FileEntity(new File(filepath),
				"binary/octet-stream");
		fileEntityHC4.setContentEncoding("binary/octet-stream");
		httpPost.setEntity(fileEntityHC4);
		try {
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			Log.d("====",
					" upload path="
							+ EntityUtils.toString(response.getEntity(),
									CHARSET_NAME));
			if (statusCode == HttpStatus.SC_OK) {
				return true;
			} else {
				throw AppException.http(statusCode);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw AppException.http(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw AppException.io(e);
		}
	}

	/*
	*//**
	 * 下载文件
	 * 
	 * @param url
	 * @param destFileName
	 * @return
	 * @throws AppException
	 */
	/*
	 * public static boolean Http_post_server_download(String url, String
	 * destFileName , Context context ,Handler handler ) throws AppException {
	 * destFileName = FileUtils.getDownloadDir() + destFileName;
	 * CloseableHttpClient httpclient = HttpClients.custom()
	 * .useSystemProperties().build(); InputStream in = null; // HttpPost
	 * httpPost = new HttpPost(url); HttpGetHC4 httpPost=new HttpGetHC4(url);
	 * try { CloseableHttpResponse response = httpclient.execute(httpPost); int
	 * statusCode = response.getStatusLine().getStatusCode(); if (statusCode ==
	 * HttpStatus.SC_OK) { HttpEntity entity = response.getEntity(); in =
	 * entity.getContent(); // 文件的总长 long lenght = entity.getContentLength(); if
	 * (FileUtils.writeFile(in, destFileName, true,lenght,context ,handler)) {
	 * return true; } else { return false; } } else { return false; } } catch
	 * (ClientProtocolException e) { e.printStackTrace(); Log.d("debug_mengyou",
	 * "ClientProtocolException==" + e.toString()); throw AppException.http(e);
	 * } catch (IOException e) { e.printStackTrace(); Log.d("debug_mengyou",
	 * "IOException==" + e.toString()); throw AppException.io(e); } finally {
	 * try { httpclient.close(); } catch (IOException e) { e.printStackTrace();
	 * throw AppException.io(e); } } }
	 */
}
