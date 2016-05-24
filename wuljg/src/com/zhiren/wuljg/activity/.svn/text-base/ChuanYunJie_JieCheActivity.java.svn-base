package com.zhiren.wuljg.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zhiren.wuljg.BaseActivity;
import com.zhiren.wuljg.R;
import com.zhiren.wuljg.adapter.ChuanYunJie_JieCheAdapter;
import com.zhiren.wuljg.entities.ChuanYunChuInfo;
import com.zhiren.wuljg.entities.JieCheInfo;
import com.zhiren.wuljg.entities.Result;
import com.zhiren.wuljg.entities.JieCheInfo.JCInfo;
import com.zhiren.wuljg.utils.ApiResult;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.NetUtil;
import com.zhiren.wuljg.utils.StringUtil;
import com.zhiren.wuljg.utils.ToastUtil;
import com.zhiren.wuljg.view.DialogLoading;
import com.zhiren.wuljg.view.XListView;
import com.zhiren.wuljg.view.XListView.IXListViewListener;

/**
 * 接车界面
 * 
 */
public class ChuanYunJie_JieCheActivity extends BaseActivity implements
		OnClickListener, IXListViewListener {

	public static final int PAIZHAO = 99;
	public static final int JIE_SUCCESS = 91;
	private TextView common_top_title;
	private XListView xlv_menu;
	private RelativeLayout rl_rl;
	private DialogLoading loading;
	private long jigtdid;
	private JieCheInfo jiecheinfos;
	private List<JCInfo> lists = new ArrayList<>();;

	private String isfrom;
	private MyJieReceiver myReceiver;
	private JieCheInfo infos;

	private double latitude;
	private double longitude;
	private String code;
	public LocationClient mLocationClient = null;
	// 回调方法
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chu_jieche);

		chuanYunInfo = (ChuanYunChuInfo) getIntent().getExtras().getSerializable(
				"chuanYunInfo");
		
		jigtdid = chuanYunInfo.jigtdId;
		code = chuanYunInfo.code;
		
		init();
		regist();
		dingWei();
		getDataFromNet(jigtdid);
		
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
		myReceiver = new MyJieReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.zhiren.jiejieche");
		registerReceiver(myReceiver, filter);
	}

	private void init() {
		loading = new DialogLoading(ChuanYunJie_JieCheActivity.this);
		xlv_menu = (XListView) findViewById(R.id.xlv_menu);
		// xlv_menu.setPullLoadEnable(true);
		xlv_menu.setPullRefreshEnable(true);
		xlv_menu.setXListViewListener(this);
		xlv_menu.setVisibility(View.GONE);
		findViewById(R.id.jieche).findViewById(R.id.common_top_left)
				.setOnClickListener(this);
		rl_rl = (RelativeLayout) findViewById(R.id.rl_rl);
		rl_rl.setVisibility(View.GONE);
		common_top_title = (TextView) findViewById(R.id.jieche).findViewById(
				R.id.common_top_title);
		common_top_title.setText("接车");
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
				xlv_menu.setPullRefreshEnable(true);
				lists.clear();
				infos = (JieCheInfo) msg.obj;
				if (infos != null && infos.rspsys != null
						&& infos.rspsys.length > 0) {
					xlv_menu.setVisibility(View.VISIBLE);
					rl_rl.setVisibility(View.GONE);
					lists.addAll(Arrays.asList(infos.rspsys));
					ChuanYunJie_JieCheAdapter adapter = new ChuanYunJie_JieCheAdapter(
							ChuanYunJie_JieCheActivity.this, lists, handler);
					if (adapter != null) {
						xlv_menu.setAdapter(adapter);
					}
				} else {
					xlv_menu.setVisibility(View.GONE);
					rl_rl.setVisibility(View.VISIBLE);
					xlv_menu.setPullRefreshEnable(false);
				}
				break;

			case Constant.ERROR:
				ToastUtil.ShowMessage(ChuanYunJie_JieCheActivity.this, msg.obj
						+ "");
				break;

			case Constant.EXCEPTION:
				ToastUtil.ShowMessage(ChuanYunJie_JieCheActivity.this, "异常");
				break;

			case JIE_SUCCESS:
				getDataFromNet(jigtdid);
				ToastUtil.ShowMessage(ChuanYunJie_JieCheActivity.this, "接车成功");
				break;

			case PAIZHAO:
				JCInfo jcInfo = (JCInfo) msg.obj;
				isfrom = "jiejieche";
				Intent intent2 = new Intent(ChuanYunJie_JieCheActivity.this,
						TakePicAndUpload_Activity.class);

				intent2.putExtra("camera_position", "335_1"); // 314_1 // 接车//
																// 315_1
				intent2.putExtra("code", code);
				intent2.putExtra("place", latitude + "," + longitude);
				intent2.putExtra("yw_type", "chuanyjx"); // 集港短倒
				intent2.putExtra("business", "qicdycph"); // 集港短倒
				intent2.putExtra("is_fc", "2"); // 1 发车 2 接车
				intent2.putExtra("zhuangcsj", jcInfo.zhuangcsj);
				intent2.putExtra("tongd", "");
				intent2.putExtra("tp_type", "jgddtp");
				intent2.putExtra("jigtdid", jcInfo.jigtdid);
				intent2.putExtra("isfrom", isfrom);
				startActivity(intent2);
				break;

			default:
				break;
			}
			return false;
		}
	});
	private ChuanYunChuInfo chuanYunInfo;

	/**
	 * 联网获取数据
	 */
	private void getDataFromNet(final long jgtdId) {

		if (!NetUtil.isNetworkConnected(ChuanYunJie_JieCheActivity.this)) {
			ToastUtil.ShowMessage(ChuanYunJie_JieCheActivity.this,
					R.string.network_not_connected);
			return;
		}
		loading.show();
		new Thread() {
			public void run() {
				Message msg = handler.obtainMessage();
				try {
					TreeMap<String, Object> params = new TreeMap<String, Object>();
					params.put("jigtdid", jgtdId);

					Result result = ApiResult.getServerArrayResult1(
							ChuanYunJie_JieCheActivity.this,
							Constant.URL_JIECHE_DATA, params, JieCheInfo.class);
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
		case R.id.common_top_left:
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public void onRefresh() {
		onLoad();
	}

	@Override
	public void onLoadMore() {
		onLoad();
	}

	private void onLoad() {
		ToastUtil.ShowMessage(ChuanYunJie_JieCheActivity.this, "刷新了");
		getDataFromNet(jigtdid);
		xlv_menu.stopRefresh();
		// xlv_menu.stopLoadMore();
		xlv_menu.setRefreshTime("刚刚");
	}

	class MyJieReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ToastUtil.ShowMessage(ChuanYunJie_JieCheActivity.this, "成功");
			isfrom = "";
			Message msg = handler.obtainMessage();
			msg.what = JIE_SUCCESS;
			handler.sendMessage(msg);
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
