package com.zhiren.wuljg.activity;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.graphics.Picture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhiren.wuljg.R;
import com.zhiren.wuljg.R.id;
import com.zhiren.wuljg.ZhiRen_Application;
import com.zhiren.wuljg.utils.ChangeViewUtil;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.GetPicUtil;
import com.zhiren.wuljg.utils.GetUserInfo;
import com.zhiren.wuljg.utils.LogUtils;
import com.zhiren.wuljg.utils.TimeUtil;
import com.zhiren.wuljg.utils.ToastUtil;
import com.zhiren.wuljg.view.DialogLoading;

/**
 * 巡垛界面 2015-07-16
 * 
 * @author Ray
 *
 */
public class XunDuo_Activity extends Activity implements OnClickListener {

	// 巡垛的批次
	private String pic;
	private Button bt_complete;

	// 巡垛id,如果为0表示新建，如果不为0，则表示加载未完成的巡视任务
	private int id;

	private String XunDuoCode, HuoChangId, DuoWeiId, SystemDate;
	// 界面跳转Intent
	private Intent intent;

	private ImageView iv_back, iv_TakePic1, iv_TakePic2, iv_TakePic3,
			iv_TakePic4, iv_TakePic5;

	private ImageView iv_xunduo_arrow1, iv_xunduo_arrow2, iv_xunduo_arrow3,
			iv_xunduo_arrow4;

	private Dialog dialog;
	private EditText et_wendu, et_shidu;

	// 用于存放每个方位照片的list集合
	public static ArrayList<HashMap<String, Object>> imgSites;

	private Boolean[] HavePic = new Boolean[] { false, false, false, false,
			false };

	// 标识相机图标的状态
	private int camera_flag;

	private String JSESSIONID;

	private DialogLoading loading;

	Handler mhandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				loadDismiss();
				// 表示新创建的巡视任务
				ToastUtil.ShowMessage(XunDuo_Activity.this, "创建成功");
				break;
			case 2:
				loadDismiss();
				// 加载已有的未完成巡视任务
				getImgUri(ZhiRen_Application.getApplication().myXunDuo
						.getXunduo_code(), JSESSIONID, Constant.URL_GET_IMG_URI);
				break;
			case 3:
				loadDismiss();
				// 得到图片信息成功
				ToastUtil.ShowMessage(XunDuo_Activity.this, "加载成功");
				if (loading.isShowing()) {
					loading.dismiss();
				}
				imgSites = (ArrayList<HashMap<String, Object>>) msg.obj;
				setView(id);
				break;
			case 4:
				loadDismiss();
				// 得到图片信息失败
				ToastUtil.ShowMessage(XunDuo_Activity.this, "查询巡垛信息失败");
				break;
			case 5:
				ToastUtil.ShowMessage(XunDuo_Activity.this, "失败");

				break;
			default:
				break;
			}
			return false;
		}

	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xunduo);

		InitView();
		// 从上一个界面获取到货场id，垛位id

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {

			// ZhiRen_Application.myXunDuo.setHuochang_id("249");
			// ZhiRen_Application.myXunDuo.setDuowei_id("249");
			ZhiRen_Application.myXunDuo.setHuochang_id(bundle
					.getDouble("huochang_id") + "");
			ZhiRen_Application.myXunDuo.setDuowei_id(bundle
					.getDouble("duowei_id") + "");
			ZhiRen_Application.myXunDuo.setHuoz(bundle.getString("huoz"));
			ZhiRen_Application.myXunDuo.setPinm(bundle.getString("pinm"));
			ZhiRen_Application.myXunDuo.setShul(String.valueOf(bundle
					.getDouble("shul")));
			ZhiRen_Application.myXunDuo.setTime(TimeUtil.getDate());
		}

		LogUtils.e(ZhiRen_Application.myXunDuo.getDuowei_id()
				+ ZhiRen_Application.myXunDuo.getHuochang_id());
		JSESSIONID = GetUserInfo.getInfo();

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 得到已有巡视任务或者创建新的巡视任务
		queryXunDuo(Constant.URL_CREATE_OR_GET_XUNDUO);

	}

	private void loadDismiss() {
		if (loading.isShowing()) {
			loading.dismiss();
		}
	}

	private void queryXunDuo(final String url) {

		final Message msg = mhandler.obtainMessage();
		loading.show();
		new Thread() {
			public void run() {
				PostMethod postMethod = new PostMethod(url);
				try {

					// 汉字的格式：new StringPart("status", URLEncoder.encode("汉字",
					// "utf-8"))
					Part[] parts = {
							new StringPart("id_huochang",
									ZhiRen_Application.myXunDuo
											.getHuochang_id()),
							new StringPart("id_duowei",
									ZhiRen_Application.myXunDuo.getDuowei_id()) };

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
						System.out
								.println(postMethod.getResponseBodyAsString());
						// 获取某个对象的JSON数组

						JSONArray myJsonArray;
						myJsonArray = new JSONArray(
								postMethod.getResponseBodyAsString());

						// 获取第一组数据：状态
						// 获取每一个JsonObject对象
						JSONObject myjObject1 = myJsonArray.getJSONObject(0);

						// 获取第二组数据：参数
						JSONObject myjObject2 = myJsonArray.getJSONObject(1);
						// 批次

						int id = myjObject2.getInt("id");
						LogUtils.e("id=" + id);
						if (id == 0) {
							// 新创建的巡视任务
							ZhiRen_Application.myXunDuo.setPic(myjObject2
									.getString("pic"));

							ZhiRen_Application.myXunDuo.setXunduo_id(String
									.valueOf(myjObject2.getInt("id")));
							pic = myjObject2.getString("pic");

							// 巡垛id=货场id+垛位id+时间+批次
							ZhiRen_Application.myXunDuo
									.setXunduo_code(ZhiRen_Application.myXunDuo
											.getHuochang_id()
											+ "_"
											+ ZhiRen_Application.myXunDuo
													.getDuowei_id()
											+ "_"
											+ ZhiRen_Application.myXunDuo
													.getTime()
											+ "_"
											+ ZhiRen_Application.myXunDuo
													.getPic());

							LogUtils.e("新建巡视code="
									+ ZhiRen_Application.myXunDuo
											.getXunduo_code());
							msg.what = 1;

						} else {
							// 加载未完成的巡视任务

							ZhiRen_Application.myXunDuo.setPic(myjObject2
									.getString("pic"));

							ZhiRen_Application.myXunDuo.setXunduo_id(String
									.valueOf(myjObject2.getInt("id")));
							ZhiRen_Application.myXunDuo
									.setXunduo_code(myjObject2
											.getString("code"));
							LogUtils.e("加载巡视code="
									+ ZhiRen_Application.myXunDuo
											.getXunduo_code());

							msg.what = 2;

						}
					} else {
						LogUtils.e("failed");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 释放连接
					postMethod.releaseConnection();
				}

				mhandler.sendMessage(msg);
			}
		}.start();
	}

	private void setView(int id) {

		for (int i = 0; i < imgSites.size(); i++) {
			if ("1".equals(imgSites.get(i).get("position"))) {
				LogUtils.e("position");
				HavePic[0] = true;
				ZhiRen_Application.cishu.put(1,
						(Integer) ZhiRen_Application.cishu.get(1) + 1);
				if (!HavePic[1]) {

					ChangeViewUtil.changeicon(iv_TakePic2, 2);
				}
				ChangeViewUtil.changeicon(iv_TakePic1, 1);
			}
			if ("2".equals(imgSites.get(i).get("position"))) {
				LogUtils.e("position");
				HavePic[0] = true;
				ZhiRen_Application.cishu.put(1,
						(Integer) ZhiRen_Application.cishu.get(2) + 1);
				if (!HavePic[1]) {

					ChangeViewUtil.changeicon(iv_TakePic3, 2);
				}
				ChangeViewUtil.changeicon(iv_TakePic2, 1);
			}
			if ("3".equals(imgSites.get(i).get("position"))) {
				ZhiRen_Application.cishu.put(1,
						(Integer) ZhiRen_Application.cishu.get(3) + 1);
				LogUtils.e("position");
				HavePic[0] = true;
				if (!HavePic[2]) {

					ChangeViewUtil.changeicon(iv_TakePic4, 2);
				}
				ChangeViewUtil.changeicon(iv_TakePic3, 1);
			}
			if ("4".equals(imgSites.get(i).get("position"))) {
				ZhiRen_Application.cishu.put(1,
						(Integer) ZhiRen_Application.cishu.get(4) + 1);
				LogUtils.e("position");
				HavePic[0] = true;
				if (!HavePic[3]) {

					ChangeViewUtil.changeicon(iv_TakePic5, 2);
				}
				ChangeViewUtil.changeicon(iv_TakePic4, 1);
			}
			if ("5".equals(imgSites.get(i).get("position"))) {
				ZhiRen_Application.cishu.put(1,
						(Integer) ZhiRen_Application.cishu.get(5) + 1);
				LogUtils.e("position");
				HavePic[0] = true;
				ChangeViewUtil.changeicon(iv_TakePic5, 1);
			}
		}

	}

	/**
	 * 获取服务器图片的地址
	 */
	public void getImgUri(final String code, final String JSESSIONID,
			final String url) {

		final Message msg = mhandler.obtainMessage();
		final ArrayList<HashMap<String, Object>> imgSites = new ArrayList<>();

		// loading.show();
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
							.setConnectionTimeout(5000);// 设置连接时间
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
							LogUtils.e(imgSites.get(i - 1).toString());
						}

						msg.what = 3;
						msg.obj = imgSites;

					} else {
						msg.what = 4;
						msg.obj = null;

						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = Constant.ERROR;
				} finally {
					// 释放连接
					postMethod.releaseConnection();
				}
				mhandler.sendMessage(msg);
			};
		}.start();

	}

	/**
	 * 初始化布局
	 */
	private void InitView() {
		loading = new DialogLoading(XunDuo_Activity.this);
		iv_back = (ImageView) findViewById(R.id.iv_title_back);
		iv_back.setOnClickListener(this);
		bt_complete = (Button) findViewById(R.id.bt_xunduo_complete);
		et_shidu = (EditText) findViewById(R.id.et_xunduo_shidu);
		et_wendu = (EditText) findViewById(R.id.et_xunduo_wendu);
		iv_TakePic1 = (ImageView) findViewById(R.id.iv_xunduo_camera1);
		iv_TakePic2 = (ImageView) findViewById(R.id.iv_xunduo_camera2);
		iv_TakePic3 = (ImageView) findViewById(R.id.iv_xunduo_camera3);
		iv_TakePic4 = (ImageView) findViewById(R.id.iv_xunduo_camera4);
		iv_TakePic5 = (ImageView) findViewById(R.id.iv_xunduo_camera5);

		bt_complete.setOnClickListener(this);
		iv_TakePic1.setOnClickListener(this);
		iv_TakePic2.setOnClickListener(this);
		iv_TakePic3.setOnClickListener(this);
		iv_TakePic4.setOnClickListener(this);
		iv_TakePic5.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_xunduo_complete:
			finishXunDuo(Constant.URL_FINISH_XUNDUO);
			break;

		case R.id.iv_title_back:
			finish();
			break;

		case R.id.iv_xunduo_camera1:
			takePhoto(1);

			break;
		case R.id.iv_xunduo_camera2:
			takePhoto(2);
			break;
		case R.id.iv_xunduo_camera3:
			takePhoto(3);
			break;
		case R.id.iv_xunduo_camera4:
			takePhoto(4);
			break;
		case R.id.iv_xunduo_camera5:
			takePhoto(5);
			break;
		default:
			break;
		}
	}

	/**
	 * 完成巡垛
	 */
	private void finishXunDuo(final String url) {
		final Message msg = mhandler.obtainMessage();
		new Thread() {
			public void run() {
				PostMethod postMethod = new PostMethod(url);
				Part[] parts = {
						new StringPart("status", "1"),
						new StringPart("xunduo_id",
								ZhiRen_Application.myXunDuo.getXunduo_id()) };
				MultipartRequestEntity mre = new MultipartRequestEntity(parts,
						postMethod.getParams());

				postMethod.setRequestEntity(mre);
				if (null != JSESSIONID) {
					postMethod.setRequestHeader("Cookie", "JSESSIONID="
							+ JSESSIONID);
					LogUtils.e(JSESSIONID);
				}

				HttpClient client = new HttpClient();
				client.getHttpConnectionManager().getParams()
						.setConnectionTimeout(50000);// 设置连接时间
				int status;
				try {
					status = client.executeMethod(postMethod);
					if (status == HttpStatus.SC_OK) {
						msg.what = 5;
						finish();
					}
				} catch (IOException e) {
					msg.what = 6;
					e.printStackTrace();
				}
			};
		}.start();
		mhandler.sendMessage(msg);
	}

	private void setInfo() {

		ZhiRen_Application.myXunDuo.setShidu(et_shidu.getText().toString());
		ZhiRen_Application.myXunDuo.setWendu(et_wendu.getText().toString());
	}

	private void takePhoto(final int flag) {
		if (flag != 1 && !HavePic[flag - 2]) {
			Toast.makeText(XunDuo_Activity.this, "请按照图示顺序进行巡垛拍照", 0).show();
		} else {
			if (!HavePic[flag - 1]) {
				dialog = new AlertDialog.Builder(XunDuo_Activity.this)
						.setTitle("拍照")
						.setMessage(
								"点击\"拍照\"进入相机,点击\"取消\"跳过此方位，点击\"查看\"浏览历史相片。")
						.setPositiveButton("拍照",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										intent = new Intent(
												XunDuo_Activity.this,
												XunDuo_TakePic_Activity.class);
										intent.putExtra("flag", flag);
										intent.putExtra("code",
												ZhiRen_Application.myXunDuo
														.getXunduo_code());
										intent.putExtra("id",
												ZhiRen_Application.myXunDuo
														.getXunduo_id());
										intent.putExtra("pic",
												ZhiRen_Application.myXunDuo
														.getPic());
										intent.putExtra("huocang_id",
												ZhiRen_Application.myXunDuo
														.getHuochang_id());
										intent.putExtra("duowei_id",
												ZhiRen_Application.myXunDuo
														.getDuowei_id());
										setInfo();
										startActivityForResult(intent, 1);

									}
								})
						.setNegativeButton("跳过",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										if (flag == 1) {
											Toast.makeText(
													XunDuo_Activity.this,
													"带标牌的方位不可跳过", 0).show();

										} else if (flag == 2) {
											ChangeViewUtil.changeicon(
													iv_TakePic2, 0);
											ChangeViewUtil.changeicon(
													iv_TakePic3, 2);
											HavePic[flag - 1] = true;
											dialog.dismiss();
										} else if (flag == 3) {
											ChangeViewUtil.changeicon(
													iv_TakePic3, 0);
											ChangeViewUtil.changeicon(
													iv_TakePic4, 2);
											HavePic[flag - 1] = true;
											dialog.dismiss();
										} else if (flag == 4) {
											ChangeViewUtil.changeicon(
													iv_TakePic4, 0);
											ChangeViewUtil.changeicon(
													iv_TakePic5, 2);
											HavePic[flag - 1] = true;
											dialog.dismiss();
										}
									}
								})
						.setNeutralButton("查看",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										intent = new Intent(
												XunDuo_Activity.this,
												ShowPic_Activity.class);
										intent.putExtra("code",
												ZhiRen_Application.myXunDuo
														.getXunduo_code());
										intent.putExtras(new Bundle());
										intent.putExtra("position", flag);

										startActivity(intent);

									}
								}).create();
			} else {
				dialog = new AlertDialog.Builder(XunDuo_Activity.this)
						.setTitle("补充拍照")
						.setMessage("是否需要补充拍照")
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										intent = new Intent(
												XunDuo_Activity.this,
												XunDuo_TakePic_Activity.class);
										intent.putExtra("flag", flag);
										intent.putExtra("pic",
												ZhiRen_Application.myXunDuo
														.getPic());
										intent.putExtra("id",
												ZhiRen_Application.myXunDuo
														.getXunduo_id());
										intent.putExtra("code",
												ZhiRen_Application.myXunDuo
														.getXunduo_code());
										intent.putExtra("huocang_id",
												ZhiRen_Application.myXunDuo
														.getHuochang_id());
										intent.putExtra("duowei_id",
												ZhiRen_Application.myXunDuo
														.getDuowei_id());
										setInfo();
										startActivityForResult(intent, 1);

									}
								})
						.setNegativeButton("否",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										dialog.dismiss();
									}
								})
						.setNeutralButton("查看",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										intent = new Intent(
												XunDuo_Activity.this,
												ShowPic_Activity.class);
										intent.putExtra("code",
												ZhiRen_Application.myXunDuo
														.getXunduo_code());
										intent.putExtra("position",
												String.valueOf(flag));
										LogUtils.e(flag + "=flag");

										startActivity(intent);
									}
								}).create();
			}
			dialog.show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {

			int flag = data.getIntExtra("flag", 0);
			camera_flag = data.getIntExtra("camera_flag", 0);
			HavePic[flag - 1] = data.getBooleanExtra("havePic", false);
			switch (flag) {
			case 1:
				if (HavePic[flag - 1]) {
					ChangeViewUtil.changeicon(iv_TakePic1, 1);
					ChangeViewUtil.changeicon(iv_TakePic2, 2);

					// iv_xunduo_arrow1.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				if (HavePic[flag - 1]) {
					ChangeViewUtil.changeicon(iv_TakePic2, 1);
					ChangeViewUtil.changeicon(iv_TakePic3, 2);
					// iv_xunduo_arrow2.setVisibility(View.VISIBLE);
					// iv_xunduo_arrow1.setVisibility(View.GONE);
				}
				break;
			case 3:
				if (HavePic[flag - 1]) {
					ChangeViewUtil.changeicon(iv_TakePic3, 1);
					ChangeViewUtil.changeicon(iv_TakePic4, 2);
					// iv_xunduo_arrow3.setVisibility(View.VISIBLE);
					// iv_xunduo_arrow2.setVisibility(View.GONE);
				}
				break;
			case 4:
				if (HavePic[flag - 1]) {
					ChangeViewUtil.changeicon(iv_TakePic4, 1);
					ChangeViewUtil.changeicon(iv_TakePic5, 2);
					// iv_xunduo_arrow4.setVisibility(View.VISIBLE);
					// iv_xunduo_arrow3.setVisibility(View.GONE);
				}
				break;
			case 5:
				if (HavePic[flag - 1]) {
					ChangeViewUtil.changeicon(iv_TakePic5, 1);
					// iv_xunduo_arrow4.setVisibility(View.GONE);
				}
				break;

			default:
				break;
			}
		}
	}
}