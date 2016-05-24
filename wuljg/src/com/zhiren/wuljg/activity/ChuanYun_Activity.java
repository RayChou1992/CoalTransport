package com.zhiren.wuljg.activity;

import com.zhiren.wuljg.BaseActivity;
import com.zhiren.wuljg.R;
import com.zhiren.wuljg.adapter.ChuanYunJie_JieCheAdapter;
import com.zhiren.wuljg.entities.ChuanYunChuInfo;
import com.zhiren.wuljg.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ChuanYun_Activity extends BaseActivity implements OnClickListener {

	// 页面控件
	private TextView tv_ship_name, tv_fayungang, tv_mudigang, tv_huozhu,
			tv_pinming, tv_shuling;
	private Button bt_yancang, bt_jigang;
	private ChuanYunChuInfo info;
	private Bundle bundle;
	private String type;
	private String yc_type;
	public static String code;
	private String address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chuanyun);
		InitView();
		setView();

	}

	/**
	 * 为布局设置数据
	 */
	private void setView() {
		Intent intent = getIntent();
		if (intent != null) {
			info = (ChuanYunChuInfo) intent
					.getSerializableExtra("chuanYunInfo");
			if (info != null) {
				address = intent.getExtras().getString("address");
				tv_ship_name.setText(info.chuanm);
				tv_fayungang.setText(info.fayg);
				tv_mudigang.setText(info.mudg);
				tv_huozhu.setText(info.huoz);
				tv_pinming.setText(info.pingm);
				tv_shuling.setText(info.shul + "吨");
				code = info.code;
				type = intent.getExtras().getString("type");
				yc_type = intent.getExtras().getString("type");

				if ("1".equals(type)) {
					bt_jigang.setText("集港");
				} else if ("2".equals(type)) {
					bt_jigang.setText("发车");
				}
			}

		}
	}

	private void InitView() {
		tv_ship_name = (TextView) findViewById(R.id.tv_chuanyun_shipName);
		tv_fayungang = (TextView) findViewById(R.id.tv_chuanyun_fayungang);
		tv_mudigang = (TextView) findViewById(R.id.tv_chuanyun_mudigang);
		tv_huozhu = (TextView) findViewById(R.id.tv_chuanyun_huozhu);
		tv_pinming = (TextView) findViewById(R.id.tv_chuanyun_pinm);
		tv_shuling = (TextView) findViewById(R.id.tv_chuanyun_shuling);
		bt_jigang = (Button) findViewById(R.id.bt_chuanyun_jigang);
		bt_yancang = (Button) findViewById(R.id.bt_chuanyun_yancang);

		bt_yancang.setOnClickListener(this);
		bt_jigang.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.bt_chuanyun_jigang:

			if ("1".equals(type)) {

				// 进入集港操作
				bundle = new Bundle();
				bundle.putLong("jigtdid", info.jigtdId);
				bundle.putString("code", code);
				intentJump(ChuanYun_Activity.this,
						ChuanYunChu_JieCheActivity.class, bundle);
			} else if ("2".equals(type)) {
				bundle = new Bundle();
				bundle.putSerializable("chuanYunInfo", info);
				bundle.putString("address", address);
				intentJump(ChuanYun_Activity.this,
						ChuanYunJie_FaCheActivity.class, bundle);
			}

			break;
		case R.id.bt_chuanyun_yancang:

			// 进入验舱操作

			bundle = new Bundle();
			if (info != null) {
				bundle.putLong("zhilid", info.zhilId);
				bundle.putString("jiangpch", info.jiangpch);
				bundle.putString("chuanm", info.chuanm);
				bundle.putString("place", info.lot + "," + info.lat);
				bundle.putString("code", code);

				if ("1".equals(yc_type)) {
					// 发运
					intentJump(ChuanYun_Activity.this,
							ChuanYun_FaYun_YanCang_Activity.class, bundle);
				} else if ("2".equals(yc_type)) {
					intentJump(ChuanYun_Activity.this,
							ChuanYun_JieXie_YanCang_Activity.class, bundle);

				}
			} else {
				ToastUtil.ShowMessage(ChuanYun_Activity.this, "暂无数据");
			}

			break;
		default:
			break;
		}
	}
}
