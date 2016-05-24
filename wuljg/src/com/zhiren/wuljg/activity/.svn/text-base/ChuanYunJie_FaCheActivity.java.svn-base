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
 * 船运接  ---   发车
 * 
 */
public class ChuanYunJie_FaCheActivity extends BaseActivity implements
		OnClickListener {

	public static final int PIDAI_QIAN = 11;
	public static final int PIDAI_HOU = 12;
	public static final int FACHE = 13;
	private DialogLoading loading;
	private TextView common_top_title;
	private TextView tv_title;
	// 发车
	private EditText et_shuliang_che;
	private EditText et_chehao;
	private ImageView iv_pic;
	private long jiglx;
	private String che_shuliang;
	private String chehao;
	private String pidai_shuliang;
	// 上一次拍的车号
	private String lastChehao = "" ;

	private String isfrom;
	private String camera_position;

	private ImageView iv_che; // 车相机


	private ChuanYunChuInfo chuanYunInfo;
	private MyReceiver myReceiver;

	private Dialog dialog;
	private Intent intent;
	private String code; // 接车
	private String address;
	
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
		setContentView(R.layout.activity_jiexie_fache);

		chuanYunInfo = (ChuanYunChuInfo) getIntent().getExtras().getSerializable(
				"chuanYunInfo");
		jiglx = chuanYunInfo.jiglx;
		code = chuanYunInfo.code; 
		address = (String) getIntent().getExtras().get("address");
	//	jiglx = 1;
		init();
		dingWei();

		common_top_title.setText("发车");
		
		loading = new DialogLoading(ChuanYunJie_FaCheActivity.this);
		JSESSIONID = GetUserInfo.getInfo();
		imgSites = new ArrayList<HashMap<String, Object>>();
		tv_title.setText(address);
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
		common_top_title = (TextView) findViewById(R.id.jieche)
				.findViewById(R.id.common_top_title);
		
		// 发车
		et_shuliang_che = (EditText) findViewById(R.id.et_shuliang_che);
		et_chehao = (EditText) findViewById(R.id.et_chehao);
		iv_che = (ImageView) findViewById(R.id.iv_che);
		tv_title = (TextView)findViewById(R.id.tv_title);
		iv_che.setOnClickListener(this);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		iv_pic.setOnClickListener(this);
		findViewById(R.id.bt_che_ok)
				.setOnClickListener(this);
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
				ToastUtil.ShowMessage(ChuanYunJie_FaCheActivity.this, msg.obj + "");
				break;

			case Constant.EXCEPTION:
				ToastUtil.ShowMessage(ChuanYunJie_FaCheActivity.this, "异常");
				break;

			default:
				break;
			}
			return false;
		}
	});
	
	@Override
	protected void onResume() {
			et_shuliang_che.setText("");
			et_chehao.setText("");
		
		super.onResume();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_che: // 相机
			facheTakePic();
			break;

		case R.id.common_top_left: // 返回按钮
			finish();
			break;

		case R.id.bt_che_ok: // 发车完成
			finish();
			break;
			
		default:
			break;
		}
	}

	/**
	 * 发车拍照
	 */
	private void facheTakePic() {
		chehao = et_chehao.getText().toString().trim();
		che_shuliang = et_shuliang_che.getText().toString().trim();
		if ( lastChehao.equals(chehao) & !TextUtils.isEmpty(lastChehao) ) {
				ToastUtil.ShowMessage(ChuanYunJie_FaCheActivity.this, "车号" + chehao
						+ "你已经拍过了");
			et_shuliang_che.setText("");
			et_chehao.setText("");
			
		} else {
			lastChehao = chehao;
			camera_position = "334_1";
			isfrom = "fache";
			dialog = new AlertDialog.Builder(ChuanYunJie_FaCheActivity.this)
					.setTitle("拍照")
					.setMessage("点击\"拍照\"进入相机,点击\"查看\"浏览相片。")
					.setPositiveButton("拍照",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {

									intent = new Intent(
											ChuanYunJie_FaCheActivity.this,
											TakePicAndUpload_Activity.class);
									intent.putExtra("camera_position",
											camera_position); // 314_1 发车 //
																// 接车
																// 315_1 //
																// 316_1 皮带
									intent.putExtra("code", code);
									intent.putExtra("place", latitude + ","
											+ longitude);
									intent.putExtra("yw_type", "chuanyjx"); // 集港短倒
									intent.putExtra("business", "qicdycph"); 
									intent.putExtra("is_fc", "1"); // 1 发车 2// 接车
									intent.putExtra("zhuangcsj",
											System.currentTimeMillis() + "");
									intent.putExtra("tongd", "");
									intent.putExtra("tp_type", "jgddtp");
									intent.putExtra("cheh", chehao);
									intent.putExtra("shul", che_shuliang);
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
											ChuanYunJie_FaCheActivity.this,
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

	class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Message msg = handler.obtainMessage();
			ToastUtil.ShowMessage(ChuanYunJie_FaCheActivity.this, "上传成功,");

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
