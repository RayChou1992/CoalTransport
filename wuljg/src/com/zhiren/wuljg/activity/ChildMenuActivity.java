package com.zhiren.wuljg.activity;

import java.util.TreeMap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zhiren.wuljg.BaseActivity;
import com.zhiren.wuljg.R;
import com.zhiren.wuljg.adapter.MenuGridViewAdapter;
import com.zhiren.wuljg.adapter.ChuanYunJie_JieCheAdapter;
import com.zhiren.wuljg.entities.ChuanYunChuInfo;
import com.zhiren.wuljg.entities.MenuList;
import com.zhiren.wuljg.entities.Result;
import com.zhiren.wuljg.utils.ApiResult;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.ImageLoaderUtils;
import com.zhiren.wuljg.utils.LogUtils;
import com.zhiren.wuljg.utils.NetUtil;
import com.zhiren.wuljg.utils.StringUtil;
import com.zhiren.wuljg.utils.TimeUtils;
import com.zhiren.wuljg.utils.ToastUtil;
import com.zhiren.wuljg.view.CircularImageView;
import com.zhiren.wuljg.view.DialogLoading;

/**
 * 子菜单界面
 * 
 */
public class ChildMenuActivity extends BaseActivity implements OnClickListener {

	public static final int RESULT_OK = 6;
	public static final int RESULT_JIE_OK = 7;
	private GridView gv_menu;
	private CircularImageView iv_userImage;
	private TextView tv_name;
	private TextView tv_time;
	private TextView tv_address; // 定位地点

	private boolean isFirst;
	private DialogLoading loading;

	private MenuGridViewAdapter adapter;
	private MenuList menu;

	private double latitude;
	private double longitude;
	private String time;
	public LocationClient mLocationClient = null;
	// 回调方法
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_childmenu);

		dingWei();

		init();
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

	private void init() {

		isFirst = true;

		gv_menu = (GridView) findViewById(R.id.gv_menu);
		iv_userImage = (CircularImageView) findViewById(R.id.xunshi)
				.findViewById(R.id.iv_userImage);
		tv_name = (TextView) findViewById(R.id.xunshi).findViewById(
				R.id.tv_name);
		tv_time = (TextView) findViewById(R.id.xunshi).findViewById(
				R.id.tv_time);
		tv_address = (TextView) findViewById(R.id.xunshi).findViewById(
				R.id.tv_address);

		loading = new DialogLoading(ChildMenuActivity.this);

		gv_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					// ToastUtil.ShowMessage(ChildMenuActivity.this, "第一个");
					Bundle bundle = new Bundle();
					bundle.putString("isfrom", "xunshi");
					bundle.putString("username", menu.childmenu.username);
					bundle.putDouble("latitude", latitude);
					bundle.putDouble("longitude", longitude);
					intentJump(ChildMenuActivity.this, XunShiActivity.class,
							bundle);
					break;

				case 1:
					// ToastUtil.ShowMessage(ChildMenuActivity.this, "第二个");
					getAddress_chu(latitude, longitude);
					break;

				case 2:
					getAddress_jie(latitude, longitude);
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 船运接卸 获取当前地址是 码头还是 垛位
	 */
	private void getAddress_jie(final double latitude, final double longitude) {
		if (!NetUtil.isNetworkConnected(ChildMenuActivity.this)) {
			ToastUtil.ShowMessage(ChildMenuActivity.this,
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
					params.put("type", "2");// 1 船运发运 /2船运接卸

					Result result = ApiResult.getServerResult(
							ChildMenuActivity.this, Constant.URL_CHUANYUN_DATA,
							params, ChuanYunChuInfo.class);
					if (result.getStatus() == Constant.SUCCESS) {
						msg.what = RESULT_JIE_OK;
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

	/**
	 * 船运出库 获取当前地址是 码头还是 垛位
	 */
	private void getAddress_chu(final double latitude, final double longitude) {
		if (!NetUtil.isNetworkConnected(ChildMenuActivity.this)) {
			ToastUtil.ShowMessage(ChildMenuActivity.this,
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
					params.put("type", "1");// 1 船运发运 /2船运接卸

					Result result = ApiResult.getServerResult(
							ChildMenuActivity.this, Constant.URL_CHUANYUN_DATA,
							params, ChuanYunChuInfo.class);
					if (result.getStatus() == Constant.SUCCESS) {
						msg.what = RESULT_OK;
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

			// Toast.makeText(getApplicationContext(),
			// latitude + "..." + longitude, 0).show();

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
					ToastUtil.ShowMessage(ChildMenuActivity.this, "报错了");
				}
				time = TimeUtils.getRemainSecString(System
						.currentTimeMillis());
				time.split("&");
				tv_time.setText(time.split("&")[0]);
				tv_name.setText(menu.childmenu.username);
				tv_address.setText(menu.childmenu.addressname);
				ImageLoaderUtils.disPlayImage(menu.childmenu.userphoto,
						iv_userImage, ImageLoaderUtils.setImageOptiaons(
								R.drawable.top_02, false));
				adapter = new MenuGridViewAdapter(ChildMenuActivity.this,
						menu.childmenu.menulist, handler);
				gv_menu.setAdapter(adapter);
				break;

			case RESULT_OK:
				ChuanYunChuInfo chuanYunInfo = (ChuanYunChuInfo) msg.obj;
				if (chuanYunInfo != null) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("chuanYunInfo", chuanYunInfo);
					// 1 皮带 2 汽车
					if (chuanYunInfo.duowId > 0) {

						bundle.putString("address", menu.childmenu.addressname);

						intentJump(ChildMenuActivity.this,
								ChuanYunChu_FaCheAndPiDaiActivity.class, bundle);

						// intentJump(ChildMenuActivity.this,
						// DuoWei_Select.class, bundle );

					} else if (chuanYunInfo.matId > 0) {
						bundle.putString("type", "1");
						bundle.putString("yc_type", "1");
						intentJump(ChildMenuActivity.this,
								ChuanYun_Activity.class, bundle);
					}
				}
				break;

			case RESULT_JIE_OK:
				// TODO
				ChuanYunChuInfo chuanYunInfo1 = (ChuanYunChuInfo) msg.obj;
				if (chuanYunInfo1 != null) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("chuanYunInfo", chuanYunInfo1);
					// 1 皮带 2 汽车
					if (chuanYunInfo1.duowId > 0) {
						// 1 皮带 2 汽车
						if (chuanYunInfo1.jiglx == 1) {
							intentJump(ChildMenuActivity.this,
									ChuanYunJie_PiDaiActivity.class, bundle);
						} else if (chuanYunInfo1.jiglx == 2) {
							intentJump(ChildMenuActivity.this,
									ChuanYunJie_JieCheActivity.class, bundle);
						}

					} else if (chuanYunInfo1.matId > 0) {
						bundle.putString("address", menu.childmenu.addressname);
						bundle.putString("type", "2");
						bundle.putString("yc_type", "2");
						intentJump(ChildMenuActivity.this,
								ChuanYun_Activity.class, bundle);
					}
				}

				break;

			case Constant.ERROR:
				ToastUtil.ShowMessage(ChildMenuActivity.this, msg.obj + "");
				break;

			case Constant.EXCEPTION:
				ToastUtil.ShowMessage(ChildMenuActivity.this, "异常");
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

		if (!NetUtil.isNetworkConnected(ChildMenuActivity.this)) {
			ToastUtil.ShowMessage(ChildMenuActivity.this,
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
							ChildMenuActivity.this, Constant.URL_MENU, params,
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
