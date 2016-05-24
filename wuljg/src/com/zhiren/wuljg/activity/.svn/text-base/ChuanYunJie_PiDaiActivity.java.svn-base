package com.zhiren.wuljg.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zhiren.wuljg.BaseActivity;
import com.zhiren.wuljg.R;
import com.zhiren.wuljg.activity.ChildMenuActivity.MyLocationListener;
import com.zhiren.wuljg.entities.ChuanYunChuInfo;
import com.zhiren.wuljg.entities.MenuList;
import com.zhiren.wuljg.entities.Result;
import com.zhiren.wuljg.utils.ApiResult;
import com.zhiren.wuljg.utils.ChangeViewUtil;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.GetUserInfo;
import com.zhiren.wuljg.utils.LogUtils;
import com.zhiren.wuljg.utils.NetUtil;
import com.zhiren.wuljg.utils.StringUtil;
import com.zhiren.wuljg.utils.ToastUtil;
import com.zhiren.wuljg.view.DialogLoading;

/**
 * 船运接卸 -----皮带界面
 * 
 */
public class ChuanYunJie_PiDaiActivity extends BaseActivity implements
		OnClickListener {

	public static final int PIDAI_QIAN = 11;
	public static final int PIDAI_HOU = 12;
	public static final int FACHE = 13;
	private DialogLoading loading;
	private TextView common_top_title;
	private TextView tv_title;

	private ImageView iv_pic;
	private long jiglx;
	private String pidai_shuliang;

	private String isfrom;
	private String isfirst = "";
	private String camera_position;

	private EditText et_shuliang_pidai;

	private ImageView step_pre; // 灰色
	private ImageView step_pre_finish; // 蓝色
	private ImageView step_pre_on; // 红色

	private ImageView step_after_pre; // 灰色
	private ImageView step_after_finish; // 蓝色
	private ImageView step_after_on; // 红色

	private ImageView iv_next;
	private ImageView iv_pre; // 相机

	private ImageView iv_pic_pre; // 照片
	private ImageView iv_pic_next;

	private ChuanYunChuInfo chuanYunInfo;
	private MyReceiver myReceiver;

	private Dialog dialog;
	private Intent intent;
	private String code; // 接车

	private String JSESSIONID;

	private double latitude;
	private double longitude;

	public LocationClient mLocationClient = null;
	// 回调方法
	public BDLocationListener myListener = new MyLocationListener();
	private List<HashMap<String, Object>> imgSites;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_jiexie_pidai);

		chuanYunInfo = (ChuanYunChuInfo) getIntent().getExtras()
				.getSerializable("chuanYunInfo");
		jiglx = chuanYunInfo.jiglx;
		code = chuanYunInfo.code;
		// jiglx = 1;
		init();
		dingWei();

		common_top_title.setText("皮带");

		loading = new DialogLoading(ChuanYunJie_PiDaiActivity.this);
		JSESSIONID = GetUserInfo.getInfo();
		imgSites = new ArrayList<HashMap<String, Object>>();
		regist();
	}

	private void dingWei() {

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPS
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	public class MyLocationListener implements BDLocationListener {

		// 接受到了定位的位置
		@Override
		public void onReceiveLocation(BDLocation location) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();

		}

	}

	// 注册广播
	private void regist() {
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.zhiren.fachepidai");
		registerReceiver(myReceiver, filter);
	}

	private void init() {
		// 标题
		findViewById(R.id.jieche).findViewById(R.id.common_top_left)
				.setOnClickListener(this);
		common_top_title = (TextView) findViewById(R.id.jieche).findViewById(
				R.id.common_top_title);
		// 皮带
		iv_next = (ImageView) findViewById(R.id.iv_next);
		iv_next.setOnClickListener(this);

		iv_pre = (ImageView) findViewById(R.id.iv_pre);
		iv_pre.setOnClickListener(this);

		et_shuliang_pidai = (EditText) findViewById(R.id.et_shuliang_pidai);
		iv_pic_pre = (ImageView) findViewById(R.id.iv_pic_pre);
		iv_pic_next = (ImageView) findViewById(R.id.iv_pic_next);
		findViewById(R.id.iv_pic_next).setOnClickListener(this);
		findViewById(R.id.bt_pidai_ok).setOnClickListener(this);

		step_pre = (ImageView) findViewById(R.id.step_pre);
		step_pre_finish = (ImageView) findViewById(R.id.step_pre_finish);
		step_pre_on = (ImageView) findViewById(R.id.step_pre_on);

		step_after_pre = (ImageView) findViewById(R.id.step_after_pre);
		step_after_finish = (ImageView) findViewById(R.id.step_after_finish);
		step_after_on = (ImageView) findViewById(R.id.step_after_on);

		isfrom = "";

	}

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (loading.isShowing()) {
				loading.dismiss();
			}
			switch (msg.what) {
			case Constant.SUCCESS:

				break;

			case Constant.ERROR:
				ToastUtil.ShowMessage(ChuanYunJie_PiDaiActivity.this, msg.obj
						+ "");
				break;
			case 4:
				setData();
				break;

			case Constant.EXCEPTION:
				ToastUtil.ShowMessage(ChuanYunJie_PiDaiActivity.this, "异常");
				break;

			default:
				break;
			}
			return false;
		}
	});

	private void setData() {
		preGone();
		nextGone();
		if (imgSites.size() == 0) {
			// 第一次进来
			isfirst = "isfirst";
			step_pre_on.setVisibility(View.VISIBLE);
			step_after_pre.setVisibility(View.VISIBLE);

		} else if (imgSites.size() >= 1) {
			step_pre_finish.setVisibility(View.VISIBLE);
			step_after_on.setVisibility(View.VISIBLE);
			iv_next.setEnabled(true);
		}

		for (int i = 0; i < imgSites.size(); i++) {
			if ("336_1".equals(imgSites.get(i).get("position"))) {
				ChangeViewUtil.changeicon(iv_pre, 1);
			} else if ("336_2".equals(imgSites.get(i).get("position"))) {
				ChangeViewUtil.changeicon(iv_next, 1);
			}
		}

	}

	private void nextGone() {
		step_after_pre.setVisibility(View.GONE);
		step_after_finish.setVisibility(View.GONE);
		step_after_on.setVisibility(View.GONE);
	}

	private void preGone() {
		step_pre.setVisibility(View.GONE);
		step_pre_finish.setVisibility(View.GONE);
		step_pre_on.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		et_shuliang_pidai.setText("");
		getPicUrl();
		super.onResume();
	}

	/**
	 * 获得图片的url地址
	 */
	private void getPicUrl() {

		loading.show();
		new Thread() {
			public void run() {
				PostMethod postMethod = new PostMethod(Constant.URL_GET_IMG_URI);

				final Message msg = handler.obtainMessage();
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
							
							if ("336_1".equals(position)|"336_2".equals(position)) {
								map.put("src", Constant.BASE_URL_PIC + url);
								map.put("position", position);
								imgSites.add(map);
							}
						}
						//
						msg.what = 4;
					} else {
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 释放连接
					postMethod.releaseConnection();
				}
				handler.sendMessage(msg);

			};
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.common_top_left: // 返回按钮
			finish();
			break;

		case R.id.bt_pidai_ok:
			finish();
			break;

		case R.id.iv_next:
			pidaiHouTakePic();
			break;

		case R.id.iv_pre:
			pidaiQianTakePic();

			break;

		default:
			break;
		}
	}

	private void pidaiHouTakePic() {
		if (imgSites.size() == 0) {
			ToastUtil.ShowMessage(ChuanYunJie_PiDaiActivity.this, "作业前未拍照");
		} else {
			isfrom = "pidai_hou";
			camera_position = "336_2";
			dialog = new AlertDialog.Builder(ChuanYunJie_PiDaiActivity.this)
					.setTitle("拍照")
					.setMessage("点击\"拍照\"进入相机,点击\"查看\"浏览相片。")
					.setPositiveButton("拍照",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									intent = new Intent(
											ChuanYunJie_PiDaiActivity.this,
											TakePicAndUpload_Activity.class);

									intent.putExtra("camera_position",
											camera_position); // 314_1 //
																// 接车//
																// 315_1
									intent.putExtra("code", code);
									intent.putExtra("place", latitude + ","
											+ longitude);
									intent.putExtra("yw_type", "chuanyjx"); // 集港短倒
									intent.putExtra("business", "pid_zuoyhcdjc");

									intent.putExtra("is_fc", "1"); // 1 发车 2
																	// 接车
									intent.putExtra("zhuangcsj",
											System.currentTimeMillis() + "");
									intent.putExtra("tongd", "");
									intent.putExtra("tp_type", "jgddtp");
									intent.putExtra("jigtdid",
											chuanYunInfo.jigtdId);
									// 标记从哪来
									intent.putExtra("isfrom", isfrom);
									startActivity(intent);
								}
							})

					.setNeutralButton("查看",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									intent = new Intent(
											ChuanYunJie_PiDaiActivity.this,
											ShowPic_Activity.class);
									intent.putExtra("code", code);
									intent.putExtra("position", camera_position);
									// LogUtils.e("fasong=" + jiangpch);
									startActivity(intent);
								}
							}).create();
			dialog.show();
		}
	}

	private void pidaiQianTakePic() {
		isfrom = "pidai_qian";
		camera_position = "336_1";
		dialog = new AlertDialog.Builder(ChuanYunJie_PiDaiActivity.this)
				.setTitle("拍照").setMessage("点击\"拍照\"进入相机,点击\"查看\"浏览相片。")
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (isfirst.equals("isfirst")) {
							intent = new Intent(ChuanYunJie_PiDaiActivity.this,
									TakePicAndUpload_Activity.class);

							intent.putExtra("camera_position", camera_position);
							intent.putExtra("code", code);
							intent.putExtra("place", latitude + "," + longitude);
							intent.putExtra("yw_type", "chuanyjx"); // 集港短倒
							intent.putExtra("business", "pid_zuoyqcdjc");
							intent.putExtra("is_fc", "1"); // 1 发车 2 接车
							intent.putExtra("zhuangcsj",
									System.currentTimeMillis() + "");
							intent.putExtra("tongd", "");
							intent.putExtra("tp_type", "jgddtp");
							intent.putExtra("jigtdid", chuanYunInfo.jigtdId);
							// 标记从哪来
							intent.putExtra("isfrom", isfrom);
							startActivity(intent);
						}else if (isfirst.equals("")) {
							ToastUtil.ShowMessage(ChuanYunJie_PiDaiActivity.this, "已经拍过了");
						}
					}
				})

				.setNeutralButton("查看", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						intent = new Intent(ChuanYunJie_PiDaiActivity.this,
								ShowPic_Activity.class);
						intent.putExtra("code", code);
						intent.putExtra("position", camera_position);
						// LogUtils.e("fasong=" + jiangpch);
						startActivity(intent);

					}
				}).create();
		dialog.show();
	}

	class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Message msg = handler.obtainMessage();
			ToastUtil.ShowMessage(ChuanYunJie_PiDaiActivity.this, "上传成功,");

			if (isfrom.equals("fache")) {
				msg.what = FACHE;
			} else if (isfrom.equals("pidai_qian")) {
				msg.what = PIDAI_QIAN;
			} else if (isfrom.equals("pidai_hou")) {
				msg.what = PIDAI_HOU;
			}
			handler.sendMessage(msg);
			isfrom = "";
		}
	}

	@Override
	protected void onDestroy() {
		if (myReceiver != null) {
			unregisterReceiver(myReceiver);
		}
		super.onDestroy();
	}

}
