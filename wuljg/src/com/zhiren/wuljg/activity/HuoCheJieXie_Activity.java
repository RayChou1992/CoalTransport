package com.zhiren.wuljg.activity;

import com.baidu.navi.location.l;
import com.zhiren.wuljg.BaseActivity;
import com.zhiren.wuljg.R;
import com.zhiren.wuljg.utils.TuiChuDialogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HuoCheJieXie_Activity extends BaseActivity implements OnClickListener {
	// 页面控件
	private TextView tv_train_name, tv_shifazhan, tv_mudizhan, tv_huozhu,
			tv_pinming, tv_shuling;
	private Button bt_yanche, bt_fache, bt_pidai;
	private TextView tv_title;
	private ImageView iv_top_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huoche);

		initView();

	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.common_top_title);
		iv_top_back = (ImageView) findViewById(R.id.common_top_left);
		tv_title.setText("火车接卸");
		tv_train_name = (TextView) findViewById(R.id.tv_huoche_shipName);
		tv_shifazhan = (TextView) findViewById(R.id.tv_huoche_fayungang);
		tv_mudizhan = (TextView) findViewById(R.id.tv_huoche_mudigang);
		tv_huozhu = (TextView) findViewById(R.id.tv_huoche_huozhu);
		tv_pinming = (TextView) findViewById(R.id.tv_huoche_pinm);
		tv_shuling = (TextView) findViewById(R.id.tv_huoche_shuling);
		bt_fache = (Button) findViewById(R.id.bt_huoche_fache);
		bt_pidai = (Button) findViewById(R.id.bt_huoche_pidai);
		bt_yanche = (Button) findViewById(R.id.bt_huoche_yanche);

		bt_yanche.setOnClickListener(this);
		bt_fache.setOnClickListener(this);
		bt_pidai.setOnClickListener(this);

		iv_top_back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_top_left:
			new TuiChuDialogUtil(HuoCheJieXie_Activity.this).showDialog();
			break;
		case R.id.bt_huoche_fache:			
			break;
		case R.id.bt_huoche_pidai:
			break;
		case R.id.bt_huoche_yanche:
			intentJump(this, Huoche_YanChe_Activity.class);
			break;

		default:
			break;
		}
	}
}
