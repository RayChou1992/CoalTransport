package com.zhiren.wuljg.utils;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.zhiren.wuljg.entities.ChuanCangInfo;
import com.zhiren.wuljg.entities.JieCheInfo;
import com.zhiren.wuljg.entities.Result;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class ApiResult {

	public static Result getServerResult(Context context, String url,
			TreeMap<String, Object> appParams, Class t) throws Exception {
		Result result = new Result();
		url = NetUtil.MakeUrl(context, url, appParams);
		Log.d("zhiren", "request==" + url);
		String params = NetUtil.MakeParams(context, appParams);
		Log.d("zhiren", "params==" + params);
		String response = NetUtil.http_post_server(context, url, params);
		Log.d("zhiren", "response==" + response);
		if (!StringUtil.isEmpty(response)) {
			try {
				JSONObject json = new JSONObject(response);
				// JSONObject sysjson = json.getJSONObject("rspsys");
				if (json.getInt("status") == 1) {
					result.setStatus(1);
					if (json.has("rspsys")
							&& !StringUtil.isEmpty(json.getString("rspsys"))) {
						if (t != null) {
							JSONObject appjson = json.getJSONObject("rspsys");
							result.setObject(new Gson().fromJson(
									appjson.toString(), t));
						}
					}
				} else {
					result.setStatus(0);
					result.setMsg(json.getString("msg"));
					// result.setErrorcode(sysjson.getString("errorcode"));
				}
			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Result getServerArrayResult1(Context context, String url,
			TreeMap<String, Object> appParams, Class t) throws Exception {
		Result result = new Result();
		url = NetUtil.MakeUrl(context, url, appParams);
		Log.d("zhiren", "request==" + url);
		String params = NetUtil.MakeParams(context, appParams);
		Log.d("zhiren", "params==" + params);
		String response = NetUtil.http_post_server(context, url, params);
		Log.d("zhiren", "response==" + response);
		if (!StringUtil.isEmpty(response)) {
			try {
				JSONObject json = new JSONObject(response);
				if (json.getInt("status") == 1) {
					result.setStatus(1);
					if (json.has("rspsys")
							&& !StringUtil.isEmpty(json.getString("rspsys"))) {

						 GsonBuilder builder = new GsonBuilder();
						 Gson gson = builder.create();
						result.setObject(gson.fromJson(json.toString(), t));
					}
				} else {
					result.setStatus(0);
					result.setMsg(json.getString("msg"));
					// result.setErrorcode(sysjson.getString("errorcode"));
				}

			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return result;
	}

	public static Result getServerArrayResult(Context context, String url,
			TreeMap<String, Object> appParams, Class class_name)
			throws Exception {
		Result result = new Result();
		url = NetUtil.MakeUrl(context, url, appParams);
		Log.d("zhiren", "request==" + url);
		String params = NetUtil.MakeParams(context, appParams);
		Log.d("zhiren", "params==" + params);
		String response = NetUtil.http_post_server(context, url, params);
		Log.d("zhiren", "response==" + response);
		if (!StringUtil.isEmpty(response)) {
			try {
				JSONObject json = new JSONObject(response);
				if (json.getInt("status") == 1) {
					result.setStatus(1);
					if (json.has("rspsys")
							&& !StringUtil.isEmpty(json.getString("rspsys"))) {
						String jo = json.getString("rspsys");

						LogUtils.e(jo);
						// String js =
						// "[{\"weiz\":\"孟家岗\",\"position\":\"1\",\"chuanczt\":"
						// +
						// " \"1\",\"chuanh\": \"123\",\"jiangpch\": \"123\",\"Id\":"
						// + " 1,\"jianghtbh\": \"123\","
						// +
						// "\"code\": \"/wuljg/upload/huocxs/20150723/229.0_244.0_11.jpg\""
						// +
						// ",\"type\": \"1\",\"shij\": \"15:50\",\"bianjsj\": \"2015-7-26\","
						// + "\"beiz\": \"100\",\"bianjr\": \"Ray\"}]";
						// LogUtils.e(js);
						Gson gson = new Gson();
						if (class_name != null) {

							List<ChuanCangInfo> ps = gson.fromJson(jo,
									new TypeToken<List<ChuanCangInfo>>() {
									}.getType());
							result.setObject(ps);
						}
					}

				} else {
					result.setStatus(0);
					result.setMsg(json.getString("msg"));
					// result.setErrorcode(sysjson.getString("errorcode"));
				}

			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return result;
	}

	public static Result getServerResult(Context context, String url,
			TreeMap<String, Object> appParams, Class t, String cacheName)
			throws Exception {
		Result result = new Result();
		url = NetUtil.MakeUrl(context, url, appParams);
		Log.d("zhiren", "request==" + url);
		String params = NetUtil.MakeParams(context, appParams);
		Log.d("zhiren", "params==" + params);
		String response = NetUtil.http_post_server(context, url, params);
		Log.d("zhiren", "response==" + response);
		if (!StringUtil.isEmpty(response)) {
			try {
				JSONObject json = new JSONObject(response);
				// JSONObject sysjson = json.getJSONObject("rspsys");
				if (json.getInt("status") == 1) {
					result.setStatus(1);
					if (json.has("rspsys")
							&& !StringUtil.isEmpty(json.getString("rspsys"))) {
						if (t != null) {
							JSONObject appjson = json.getJSONObject("rspsys");
							if (!StringUtil.isEmpty(cacheName)) {
								FileUtils.writeFile(appjson.toString()
										.getBytes(), FileUtils.getDownloadDir()
										+ cacheName, false);
							}
							result.setObject(new Gson().fromJson(
									appjson.toString(), t));
						}
					}
				} else {
					result.setStatus(0);
					result.setMsg(json.getString("msg"));
					// result.setErrorcode(sysjson.getString("errorcode"));
				}
			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return result;
	}

	public static Result getCacheResult(String cacheName, Class t) {
		cacheName = FileUtils.getDownloadDir() + cacheName;
		Result result = new Result();
		File f = new File(cacheName);
		if (!f.exists()) {
			result.setStatus(0);
		} else {
			result.setStatus(1);
			result.setObject(new Gson().fromJson(FileUtils.readFile(cacheName),
					t));
		}
		return result;

	}

	/**
	 * 单个文件上传
	 * 
	 * @param url
	 *            上传URL
	 * @param filepath
	 *            文件路径
	 * @return
	 * @throws Exception
	 */
	public static Result uplaodFile(Context context, String url,
			TreeMap<String, Object> appParams, String filepath)
			throws Exception {
		Result result = new Result();
		url = NetUtil.MakeUrl(context, url, appParams);
		if (NetUtil.http_post_server_upload(url, filepath)) {
			result.setStatus(1);
		} else {
			result.setStatus(0);
		}
		return result;
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 *            下载URL
	 * @param destFileName
	 *            目标文件名称
	 * @return
	 * @throws AppException
	 */
	/*
	 * public static Result downloadFile(String url, String destFileName,
	 * Context context, Handler handler) throws AppException { Result result =
	 * new Result(); if (NetUtil.Http_post_server_download(url, destFileName,
	 * context, handler)) { result.setSuccess(1); } else { result.setSuccess(0);
	 * } return result; }
	 */
}
