package com.zhiren.wuljg.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.zhiren.wuljg.activity.ShowPic_Activity;
import com.zhiren.wuljg.view.DialogLoading;

public class GetPicUtil {
	/**
	 * 获取服务器图片的地址
	 */
	public static ArrayList<HashMap<String, Object>> getImgUri(
			final DialogLoading loading, final Handler handler,
			final String code, final String JSESSIONID, final String url,
			Context context) {
		final Message msg = handler.obtainMessage();
		final ArrayList<HashMap<String, Object>> imgSites = new ArrayList<>();
		loading.show();
		new Thread() {
			public void run() {
				PostMethod postMethod = new PostMethod(url);

				try {
					// FilePart：用来上传文件的类

					// 汉字的格式：new StringPart("status", URLEncoder.encode("汉字",
					// "utf-8"))
					Part[] parts = { new StringPart("code", code) };

					// 对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
					MultipartRequestEntity mre = new MultipartRequestEntity(
							parts, postMethod.getParams());

					postMethod.setRequestEntity(mre);
					if (null != JSESSIONID) {
						postMethod.setRequestHeader("Cookie", "JSESSIONID="
								+ JSESSIONID);
					}

					HttpClient client = new HttpClient();
					client.getHttpConnectionManager().getParams()
							.setConnectionTimeout(50000);// 设置连接时间
					int status = client.executeMethod(postMethod);
					if (status == HttpStatus.SC_OK) {
						if (status == 0) {
							return;
						}
						System.out
								.println(postMethod.getResponseBodyAsString());
						// 解析

						// 获取某个对象的JSON数组

						JSONArray myJsonArray;
						myJsonArray = new JSONArray(
								postMethod.getResponseBodyAsString());
						for (int i = 1; i < myJsonArray.length(); i++) {
							// 获取每一个JsonObject对象
							JSONObject myjObject = myJsonArray.getJSONObject(i);
							String url = myjObject.getString("src");
							String position = myjObject.getString("position");
							HashMap<String, Object> map = new HashMap<>();
							map.put("src", Constant.BASE_URL_PIC + url);
							map.put("position", position);
							imgSites.add(map);
							System.out.println(imgSites.get(i - 1));
						}
						//
						msg.what = Constant.SUCCESS;
						handler.handleMessage(msg);
					} else {
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 释放连接
					postMethod.releaseConnection();
				}

			};
		}.start();
		return imgSites;

	}
}
