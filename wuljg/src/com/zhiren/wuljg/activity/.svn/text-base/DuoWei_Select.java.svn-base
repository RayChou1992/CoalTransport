package com.zhiren.wuljg.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.zhiren.wuljg.BaseActivity;
import com.zhiren.wuljg.R;
import com.zhiren.wuljg.ZhiRen_Application;
import com.zhiren.wuljg.entities.DuoWeiInfo;
import com.zhiren.wuljg.entities.HuoChangLocation;
import com.zhiren.wuljg.entities.Result;
import com.zhiren.wuljg.entities.User.UserInfo;
import com.zhiren.wuljg.utils.ApiResult;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.DensityUtils;
import com.zhiren.wuljg.utils.NetUtil;
import com.zhiren.wuljg.utils.StringUtil;
import com.zhiren.wuljg.utils.ToastUtil;
import com.zhiren.wuljg.view.DialogLoading;

/**
 * 船运 -定位后 选垛位
 * 
 */
public class DuoWei_Select extends BaseActivity {

	public static final int RESULT_OK = 6;
	// 百度地图控件
	private MapView mMapView = null;
	// 百度地图对象
	private BaiduMap mBaiduMap;
	
	private boolean flag = false;

	public LocationClient mLocationClient = null;
	// Marker图标
	BitmapDescriptor mCurrentMarker;
	// 定位模式
	private LocationMode mCurrentMode;
	private BDLocation loc;

	private RelativeLayout mMarkerLy;
	private TextView tv_address;

	private StringBuffer sb;
	private DialogLoading loading;
	private String text = null;

	private InfoWindow infoWindow;
	private HuoChangLocation locInfo;
	// 垛位信息
	private DuoWeiInfo info;
	// 经纬度信息
	private Marker marker1;

	public MyLocationListener myListener = new MyLocationListener();

	private double huochang_id;
	private double duowei_id;
	private String huoz;
	private String pinm;
	private String shul;

	private Marker mMarkerA;
	private Marker mMarkerD;
	List<Points> infos = new ArrayList<>();

	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);
	// BitmapDescriptor bdB = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_markb);
	// BitmapDescriptor bdC = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_markc);

	// BitmapDescriptor bdD = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_gcoding);

	private double duoweilatitude;
	private double duoweilongitude;

	private double latitude;
	private double longitude;
	
	private long duoweiid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_select_duowei);
		init();

		latitude = getIntent().getExtras().getDouble("latitude");
		longitude = getIntent().getExtras().getDouble("longitude");
		
		duoweilatitude = getIntent().getExtras().getDouble("duoweilatitude");
		duoweilongitude = getIntent().getExtras().getDouble("duoweilongitude");
		
		duoweiid = getIntent().getExtras().getLong("duoweiid");
		
		infos.add(new Points(duoweilatitude, duoweilongitude));
		 getDataFromNet(latitude, longitude);
		initData();
	}

	private void init() {
		loading = new DialogLoading(DuoWei_Select.this);
		tv_address = (TextView) findViewById(R.id.xunshi).findViewById(
				R.id.tv_address);

		mCurrentMode = LocationMode.NORMAL;// 设置定位模式为普通
		mCurrentMarker = BitmapDescriptorFactory// 构建mark图标
				.fromResource(R.drawable.icon_gcoding);

		mLocationClient = new LocationClient(this); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		mMapView = (MapView) findViewById(R.id.bmapview);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);

		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 卫星地图
		// mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

		LocationClientOption option = new LocationClientOption();

		option.setOpenGps(true);
		// Hight_Accuracy高精度、Battery_Saving低功耗、Device_Sensors仅设备(GPS)
		// option.setLocationMode(LocationMode.Hight_Accuracy);
		// gcj02默认国测局加密经纬度;bd9ll:百度加密经纬度；bd09：百度加密抹托卡坐标
		option.setCoorType("bd09ll");
		option.setProdName("zhiren");
		option.setOpenGps(true);
		// 定位时间间隔
		option.setScanSpan(5000);
		// 是否反解析地址
		option.setIsNeedAddress(true);
		option.setAddrType("all");
		option.setNeedDeviceDirect(true);

		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	private void initData() {
		mMarkerLy = (RelativeLayout) findViewById(R.id.rl_pop);

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				marker1 = marker;
				flag = false;
				text = "";
				if (marker == mMarkerA) {
					getDetailInfo(duoweiid);
				} else if (marker == mMarkerD) {
					text = sb.toString();
					shouWindow(marker, text);
				}
				return false;
			}
		});

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				if (infoWindow == null) {
					return;
				}
				mBaiduMap.hideInfoWindow();
			}
		});
	}

	private void shouWindow(Marker marker, String text) {

		TextView tv = new TextView(DuoWei_Select.this);
		tv.setBackgroundResource(R.drawable.location_tips);
		tv.setPadding(30, 20, 30, 50);
		tv.setText(text);
		tv.setTextSize(DensityUtils.dp2px(DuoWei_Select.this, 10));
		tv.setTextColor(Color.parseColor("#ffffff"));

		final LatLng latLng = marker.getPosition();
		Point p = mBaiduMap.getProjection().toScreenLocation(latLng);
		p.y -= 40;
		LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(tv);

		infoWindow = new InfoWindow(bitmap, ll, -10,
				new OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick() {
						ToastUtil.ShowMessage(DuoWei_Select.this, "pidai == fache");
//							intentJump(DuoWei_Select.this,
//									XunDuo_Activity.class, bundle);
						finish();
					}
				});

		mBaiduMap.showInfoWindow(infoWindow);
		mMarkerLy.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 */
	public String getData() {
		if (flag) {
			StringBuffer sb1 = new StringBuffer(256);

			sb1.append("垛位id: ");
			sb1.append(info.tongdid);

			sb1.append("\n垛位名称: ");
			sb1.append(info.tongdmc);

			sb1.append("\n货主名称 : ");
			sb1.append(info.quanc);

			sb1.append("\n品 种: ");
			sb1.append(info.mingc);

			text = sb1.toString();

			return text;
		}

		return "再来一次";
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!mLocationClient.isStarted())
			mLocationClient.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocationClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();

		// 回收 bitmap 资源
		bdA.recycle();
		// bdB.recycle();
		// bdC.recycle();
		// bdD.recycle();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || mMapView == null) {
				return;
			}
			sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			// sb.append("\nerror code : ");
			// sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}

		}

	}

	/**
	 * 添加覆盖物
	 * 
	 * @param infos
	 */
	private void addOverlays(List<Points> infos) {
		// add marker overlay
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;

		// --------------------------------------------------------
		LatLng llA = new LatLng(infos.get(0).latitude, infos.get(0).longitude);
		// LatLng llB = new LatLng(39.942821, 116.369199);
		// LatLng llC = new LatLng(39.939723, 116.425541);
		//LatLng llD = new LatLng(latitude, longitude);

		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

		// OverlayOptions ooB = new MarkerOptions().position(llB).icon(bdB)
		// .zIndex(5);
		// mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));

		// OverlayOptions ooC = new MarkerOptions().position(llC).icon(bdC)
		// .perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);
		// mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));

		// OverlayOptions ooD = new MarkerOptions().position(llD).icon(bdD)
		// .zIndex(5);
		// mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));

		LatLng point = new LatLng(latitude, longitude);
		float f = mBaiduMap.getMaxZoomLevel();// 19.0
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, f);
		mBaiduMap.animateMapStatus(u);
		overlay(point, mCurrentMarker, mBaiduMap);
		// MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(on);
		mBaiduMap.setMapStatus(u);
	}

	/**
	 * 
	 * @param longitude
	 * @param latitude
	 * @param bitmap
	 * @param baiduMap
	 */
	private void overlay(LatLng point, BitmapDescriptor bitmap,
			BaiduMap baiduMap) {
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap);
		// mMarkerD = (Marker) (mBaiduMap.addOverlay(ooC));

		mMarkerD = (Marker) baiduMap.addOverlay(option);
	}

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			loadingDismiss();
			switch (msg.what) {
			case Constant.SUCCESS:
				addOverlays(infos);
				break;

			case RESULT_OK:
				
				info = (DuoWeiInfo) msg.obj;
				if (info != null) {
					flag = true;
					text = getData();
					shouWindow(marker1, text);
				}
				break;

			case Constant.ERROR:
				ToastUtil.ShowMessage(DuoWei_Select.this, msg.obj + "");
				break;

			case Constant.EXCEPTION:
				ToastUtil.ShowMessage(DuoWei_Select.this, "异常");
				break;

			default:
				break;
			}
			return false;
		}

	});

	/**
	 * 关闭加载圈圈
	 */
	private void loadingDismiss() {
		if (loading.isShowing()) {
			loading.dismiss();
		}
	}

	/**
	 * 首次定位成功后 请求服务器
	 */
	private void getDataFromNet(final double latitude, final double longitude) {
		if (!NetUtil.isNetworkConnected(DuoWei_Select.this)) {
			ToastUtil.ShowMessage(DuoWei_Select.this,
					R.string.network_not_connected);
			return;
		}
		loading.show();
		new Thread() {
			public void run() {
				Message msg = handler.obtainMessage();
				UserInfo userInfo = ZhiRen_Application.getApplication()
						.getUserInfo();
				try {
					TreeMap<String, Object> params = new TreeMap<String, Object>();
					params.put("latitude", latitude);
					params.put("longitude", longitude);

					Result result = ApiResult.getServerResult(
							DuoWei_Select.this, Constant.URL_LOCATION, params,
							HuoChangLocation.class);
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

	// 0 未抵押 1 抵押

	/**
	 * 根据垛位id 查找垛位详细信息
	 */
	private void getDetailInfo(final long id) {
		if (!NetUtil.isNetworkConnected(DuoWei_Select.this)) {
			ToastUtil.ShowMessage(DuoWei_Select.this,
					R.string.network_not_connected);
			return;
		}
		loading.show();
		new Thread() {
			public void run() {
				Message msg = handler.obtainMessage();
				UserInfo userInfo = ZhiRen_Application.getApplication()
						.getUserInfo();
				try {
					TreeMap<String, Object> params = new TreeMap<String, Object>();
					params.put("duoWeiId", id);

					Result result = ApiResult.getServerResult(
							DuoWei_Select.this, Constant.URL_DUOWEI_DATA,
							params, DuoWeiInfo.class);
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

	class Points{
		private double latitude ;
		private double longitude ;
		
		
		public Points(double latitude, double longitude) {
			super();
			this.latitude = latitude;
			this.longitude = longitude;
		}
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
	}
}
