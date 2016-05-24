package com.zhiren.wuljg.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import junit.framework.Test;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.zhiren.wuljg.BaseActivity;
import com.zhiren.wuljg.MainActivity;
import com.zhiren.wuljg.R;
import com.zhiren.wuljg.ZhiRen_Application;
import com.zhiren.wuljg.activity.ChildMenuActivity.MyLocationListener;
import com.zhiren.wuljg.adapter.MenuGridViewAdapter;
import com.zhiren.wuljg.entities.MenuList;
import com.zhiren.wuljg.entities.Result;
import com.zhiren.wuljg.entities.User.UserInfo;
import com.zhiren.wuljg.utils.ApiResult;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.ImageLoaderUtils;
import com.zhiren.wuljg.utils.NetUtil;
import com.zhiren.wuljg.utils.StringUtil;
import com.zhiren.wuljg.utils.TimeUtils;
import com.zhiren.wuljg.utils.ToastUtil;
import com.zhiren.wuljg.view.CircularImageView;
import com.zhiren.wuljg.view.DialogLoading;

/**
 * 菜单界面
 * 
 */
public class MenuActivity extends BaseActivity implements OnClickListener {

	public LocationClient mLocationClient = null;
	// 回调方法
	public BDLocationListener myListener = new MyLocationListener();

	private double latitude;
	private double longitude;

	private CircularImageView iv_userImage;
	private TextView tv_weather;
	private TextView tv_time;

	private TextView tv_name;
	private TextView tv_address; // 定位地点

	private DialogLoading loading;
	private MenuList menu;
	private boolean isFirst;
	private String time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu);

		findViewById(R.id.rl_1).setOnClickListener(this);
		findViewById(R.id.rl_2).setOnClickListener(this);
		findViewById(R.id.rl_3).setOnClickListener(this);
		findViewById(R.id.rl_4).setOnClickListener(this);

		iv_userImage = (CircularImageView) findViewById(R.id.xunshi)
				.findViewById(R.id.iv_userImage);
		tv_name = (TextView) findViewById(R.id.xunshi).findViewById(
				R.id.tv_name);

		tv_time = (TextView) findViewById(R.id.xunshi).findViewById(
				R.id.tv_time);

		tv_address = (TextView) findViewById(R.id.xunshi).findViewById(
				R.id.tv_address);
		findViewById(R.id.rl_4).setOnClickListener(this);

		init();
		loading = new DialogLoading(MenuActivity.this);

	}

	private void init() {
		isFirst = true;
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
			if (isFirst) {
				getDataFromNet(latitude, longitude);
				isFirst = false;
			}

		}

	}

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (loading.isShowing()) {
				loading.dismiss();
			}
			switch (msg.what) {
			case Constant.SUCCESS:
				menu = (MenuList) msg.obj;
				if (menu == null) {
					ToastUtil.ShowMessage(MenuActivity.this, "报错了");
				}
				time = TimeUtils.getRemainSecString(System
						.currentTimeMillis());
				time.split("&");
				tv_time.setText(time.split("&")[0]);
				tv_name.setText(menu.childmenu.username);
				tv_address.setText(menu.childmenu.addressname);
				ImageLoaderUtils.disPlayImage(Constant.BASE_PIC_URI
						+ menu.childmenu.userphoto, iv_userImage,
						ImageLoaderUtils.setImageOptiaons(R.drawable.top_02,
								false));

				break;

			case Constant.ERROR:
				ToastUtil.ShowMessage(MenuActivity.this, msg.obj + "");
				break;

			case Constant.EXCEPTION:
				ToastUtil.ShowMessage(MenuActivity.this, "异常");
				break;

			default:
				break;
			}
			return false;
		}
	});

	/**
	 * 联网获取数据
	 */
	private void getDataFromNet(final double latitude, final double longitude) {

		if (!NetUtil.isNetworkConnected(MenuActivity.this)) {
			ToastUtil.ShowMessage(MenuActivity.this,
					R.string.network_not_connected);
			return;
		}
		loading.show();
		new Thread() {
			public void run() {
				Message msg = handler.obtainMessage();

				try {
					TreeMap<String, Object> params = new TreeMap<String, Object>();
					params.put("latitude", latitude);
					params.put("longitude", longitude);

					Result result = ApiResult.getServerResult(
							MenuActivity.this, Constant.URL_MENU, params,
							MenuList.class);
					if (result.getStatus() == Constant.SUCCESS) {
						msg.what = Constant.SUCCESS;
						msg.obj = result.getObject();
					} else {
						msg.what = Constant.ERROR;
						if (!StringUtil.isEmpty(result.getMsg())) {
							msg.obj = result.getMsg();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = Constant.EXCEPTION;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			};
		}.start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_1:
			intentJump(MenuActivity.this, ChildMenuActivity.class);
			break;
		case R.id.rl_2:
			intentJump(MenuActivity.this, HuoCheJieXie_Activity.class);

			break;

		case R.id.rl_3:
			// intentJump(MenuActivity.this,
			// ChuanYunChu_FaCheAndPiDaiActivity.class);
			break;

		case R.id.rl_4:
			// intentJump(MenuActivity.this, ChuanYun_Activity.class);
			break;
		default:
			break;
		}

	}
	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();// 请求定位的位置
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		mLocationClient.stop();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocationClient.stop();
		super.onDestroy();
	}
}
