package com.zhiren.wuljg.utils;

import java.io.File;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.view.Surface;

import com.zhiren.wuljg.ZhiRen_Application;
import com.zhiren.wuljg.entities.XunDuo;

@SuppressLint("NewApi")
public class TakePicUtil {
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 预览界面，画面的旋转
	 * 
	 * @param activity
	 * @param cameraId
	 * @param camera
	 */
	@SuppressLint("NewApi")
	public static void setCameraDisplayOrientation(Activity activity,
			int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	/**
	 * 巡垛上传图片
	 * 
	 * @param JSESSIONID
	 *            用户id
	 * @param context
	 *            上下文对象
	 * @param file
	 *            文件
	 * @param url
	 *            服务器地址
	 */
	public static void uploadFile(final String JSESSIONID,
			final Context context, final File file, final XunDuo xunDuo,
			final String url, final int flag) {

		if (!file.exists()) {
			return;
		}
		new Thread() {
			public void run() {
				PostMethod postMethod = new PostMethod(url);

				try {
					// FilePart：用来上传文件的类
					FilePart fp = new FilePart("filedata", file);

					// 汉字的格式：new StringPart("status", URLEncoder.encode("汉字",
					// "utf-8"))
					Part[] parts = {
							new StringPart("user", xunDuo.getUser()),
							new StringPart("place", URLEncoder.encode(
									xunDuo.getPlace(), "UTF-8")),
							new StringPart("account", xunDuo.getAccount()),
							new StringPart("position", xunDuo.getFangwei()),
							new StringPart("yichang", URLEncoder.encode(
									xunDuo.getYichangxinxi(), "UTF-8")),
							new StringPart("tianqi", URLEncoder.encode(
									xunDuo.getTianqi(), "UTF-8")),
							new StringPart("wendu", xunDuo.getWendu()),
							// new StringPart("time", xunDuo.getTime()),
							new StringPart("shidu", xunDuo.getShidu()),
							// new StringPart("pic_seq", xunDuo.getPic_seq()),
							new StringPart("huocdm", xunDuo.getHuochang_id()),
							new StringPart("xunduo_id", xunDuo.getXunduo_id()),
							new StringPart("cangcdd", xunDuo.getDuowei_id()),
							new StringPart("pic", xunDuo.getPic()),
							new StringPart("type", "xuns"),
							new StringPart("business", "xuns_pz"),
							new StringPart("code", xunDuo.getXunduo_code()), fp };

					// 对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
					MultipartRequestEntity mre = new MultipartRequestEntity(
							parts, postMethod.getParams());

					postMethod.setRequestEntity(mre);
					if (null != JSESSIONID) {
						postMethod.setRequestHeader("Cookie", "JSESSIONID="
								+ JSESSIONID);
						LogUtils.e(JSESSIONID);
					}

					HttpClient client = new HttpClient();
					client.getHttpConnectionManager().getParams()
							.setConnectionTimeout(50000);// 设置连接时间
					int status = client.executeMethod(postMethod);
					if (status == HttpStatus.SC_OK) {
						System.out.println("上传照片之后的返回数据："
								+ postMethod.getResponseBodyAsString());

						JSONObject jsonObject = new JSONObject(
								postMethod.getResponseBodyAsString());

						if (!"0".equals(jsonObject.getString("status"))) {

							String id = jsonObject.getString("id");
							LogUtils.e(id);
							ZhiRen_Application.cishu.put(flag,
									(Integer) ZhiRen_Application.cishu
											.get(flag) + 1);
							ZhiRen_Application.myXunDuo.setXunduo_id(id);
							ZhiRen_Application.myXunDuo.setPic(jsonObject
									.getString("pic"));
							ZhiRen_Application.myXunDuo.setCode(jsonObject
									.getString("code"));
						}

					} else {
						System.out.println("fail");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 释放连接
					postMethod.releaseConnection();
				}
			}
		}.start();
		;

	}

	/**
	 * 上传图片公用方法
	 * @param JSESSIONID 用户
	 * @param file 图片文件
	 * @param url 请求的url地址
	 * @param yw_type 业务类型
	 * @param business 操作类型
	 * @param position 相机机位
	 * @param code 编号
	 * @param yichang 异常信息
	 * @param place 地理位置
	 */
	public static void uploadFile(final String JSESSIONID, final File file,
			final String url, final String yw_type, final String business,final String position,
			final String code, final String yichang, final String place) {

		if (!file.exists()) {
			return;
		}
		new Thread() {
			public void run() {
				PostMethod postMethod = new PostMethod(url);

				LogUtils.e("图片上传请求接口路径：" + url);
				try {
					// FilePart：用来上传文件的类
					FilePart fp = new FilePart("filedata", file);
					Part[] parts = { new StringPart("yw_type", yw_type),
							new StringPart("position", position),
							new StringPart("code", code),
							new StringPart("yichang", yichang),
							new StringPart("place", place),
							new StringPart("business", business), fp };

					// 对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
					MultipartRequestEntity mre = new MultipartRequestEntity(
							parts, postMethod.getParams());
					postMethod.setRequestEntity(mre);
					if (null != JSESSIONID) {
						postMethod.setRequestHeader("Cookie", "JSESSIONID="
								+ JSESSIONID);
						LogUtils.e(JSESSIONID);
					}

					HttpClient client = new HttpClient();
					client.getHttpConnectionManager().getParams()
							.setConnectionTimeout(50000);// 设置连接时间

					int status = client.executeMethod(postMethod);
					if (status == HttpStatus.SC_OK) {
						System.out.println("上传照片之后的返回数据："
								+ postMethod.getResponseBodyAsString());
						JSONObject jsonObject = new JSONObject(
								postMethod.getResponseBodyAsString());
						String msg = jsonObject.getString("msg");

					}

					// JSONObject jsonObject = new JSONObject(
					// postMethod.getResponseBodyAsString());

					// } else {
					// System.out.println("fail");
					// }
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 释放连接
					postMethod.releaseConnection();
				}
			}
		}.start();
		;

	}

	/**
	 * 上传图片
	 * 
	 * @param JSESSIONID
	 *            用户session
	 * @param file
	 *            图片文件
	 * @param url
	 *            url地址
	 * @param ye_type
	 *            业务类型 fachepz
	 * @param position
	 *            相机方位 314_1
	 * @param code
	 *            图片编码 314_1
	 * @param yichang
	 *            图片异常信息 ""
	 * @param place
	 *            位置
	 */

	public static void uploadFile(final String JSESSIONID, final File file,
			final String url, final String yw_type, final String position,
			final String code, final String yichang, final String place,
			final String isfrom, final String jgddtp, final String is_fc,
			final String zhuangcsj, final long jigtdid, final String tongd,final Context context ) {
		if (!file.exists()) {
			return;
		}
		new Thread() {
			private Part[] parts;
			public void run() {
				PostMethod postMethod = new PostMethod(url);
				try {
					// FilePart：用来上传文件的类
					FilePart fp = new FilePart("filedata", file);
					if (isfrom.equals("pidai_hou")||isfrom.equals("pidai_qian")) {
						parts = new Part[] {
								new StringPart("yw_type", yw_type),
								new StringPart("position", position),
								new StringPart("code", code),
								new StringPart("yichang", yichang),
								new StringPart("place", place),
								new StringPart("zhuangcsj", zhuangcsj + ""),
								new StringPart("tp_type", "pidai"), fp };
					} else {
						parts = new Part[] {
								new StringPart("yw_type", yw_type),
								new StringPart("position", position),
								new StringPart("code", code),
								new StringPart("yichang", yichang),
								new StringPart("place", place),
								new StringPart("tp_type", "jgddtp"),
								new StringPart("is_fc", is_fc + ""),
								new StringPart("zhuangcsj", zhuangcsj + ""),
								new StringPart("jigtdid", jigtdid + ""),
								new StringPart("tongd", tongd + ""), fp };
					}
					// 对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
					MultipartRequestEntity mre = new MultipartRequestEntity(
							parts, postMethod.getParams());
					postMethod.setRequestEntity(mre);
					if (null != JSESSIONID) {
						postMethod.setRequestHeader("Cookie", "JSESSIONID="
								+ JSESSIONID);
						LogUtils.e(JSESSIONID);
					}

					HttpClient client = new HttpClient();
					client.getHttpConnectionManager().getParams()
							.setConnectionTimeout(50000);// 设置连接时间
					System.out.println("上传照片请求数据："
							+ client.getHttpConnectionManager().getParams()
									.toString());
					int status = client.executeMethod(postMethod);

					if (status == HttpStatus.SC_OK) {
						System.out.println("上传照片之后的返回数据："
								+ postMethod.getResponseBodyAsString());

						JSONObject jsonObject = new JSONObject(
								postMethod.getResponseBodyAsString());
						// System.out.println(jsonObject.getInt("status"));
						// 成功后
						if (isfrom.equals("jieche")) {
							Intent i = new Intent("com.zhiren.chujieche");
							context.sendBroadcast(i);
						}else if (isfrom.equals("jiejieche")) {
							Intent i = new Intent("com.zhiren.jiejieche");
							context.sendBroadcast(i);
						}else {
							Intent intent = new Intent("com.zhiren.fachepidai");
							context.sendBroadcast(intent);
						}
						
					} else {
						System.out.println("fail");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 释放连接
					postMethod.releaseConnection();
				}
			}
		}.start();
		;

	}
}
