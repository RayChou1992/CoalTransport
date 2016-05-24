package com.zhiren.wuljg.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiren.wuljg.BaseActivity;
import com.zhiren.wuljg.R;
import com.zhiren.wuljg.utils.ChangeViewUtil;
import com.zhiren.wuljg.utils.TuiChuDialogUtil;

public class Huoche_YanChe_Activity extends BaseActivity implements
		OnClickListener {

	/* 布局控件--始 */
	private TextView tv_title;
	private ImageView iv_top_back;
	private Button bt_StepOne, bt_StepTwo, bt_StepThree;
	private EditText et_shouchehao, et_weichehao, et_jieshu, et_zhongliang;
	private LinearLayout ll_stepone, ll_steptwo, ll_stepthree;
	private ImageView iv_camera_dayundan, iv_camera_xiaoyundan;
	private ImageView iv_camera_chexiang_top, iv_camera_chexiang_chehao;
	private ImageView iv_camera_chexiangleft, iv_camera_chexiangright;
	private Button bt_finish;

	/* 布局控件--完 */

	private Dialog dialog;
	private Intent intent;
	// 经度
	private String lot;
	// 纬度
	private String lat;
	// 业务code
	private String code;
	// 操作类型
	private String business;

	// 标识进行到那个步骤
	private int step = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huoche_jiexie_yancang);

		initView();
		getInfo();

	}

	// 获取初始化数据
	private void getInfo() {
		step = 1;
		code="houchejixiepz";
		business="123";
	}

	/**
	 * 初始化布局
	 * 
	 */

	
	private void initView() {
		tv_title = (TextView) findViewById(R.id.common_top_title);

		tv_title.setText("验车");
		iv_top_back = (ImageView) findViewById(R.id.common_top_left);
		bt_StepOne = (Button) findViewById(R.id.tv_huoche_yancang_stepone);
		bt_StepTwo = (Button) findViewById(R.id.tv_huoche_yancang_steptwo);
		bt_StepThree = (Button) findViewById(R.id.tv_huoche_yancang_stepthree);
		et_shouchehao = (EditText) findViewById(R.id.et_huocjx_shouchehao);
		et_weichehao = (EditText) findViewById(R.id.et_huocjx_weichehao);
		et_jieshu = (EditText) findViewById(R.id.et_huocjx_jieshu);
		et_zhongliang = (EditText) findViewById(R.id.et_huocjx_zhongliang);
		ll_stepone = (LinearLayout) findViewById(R.id.ll_huoche_yanche_stepone);
		ll_steptwo = (LinearLayout) findViewById(R.id.ll_huoche_yanche_steptwo);
		ll_stepthree = (LinearLayout) findViewById(R.id.ll_huoche_yanche_stepthree);
		iv_camera_dayundan = (ImageView) findViewById(R.id.iv_huoche_dayundan);
		iv_camera_xiaoyundan = (ImageView) findViewById(R.id.iv_huoche_xiaoyundan);
		iv_camera_chexiang_top = (ImageView) findViewById(R.id.iv_huoche_chexiang_top);
		iv_camera_chexiang_chehao = (ImageView) findViewById(R.id.iv_huoche_chexiang_chehao);
		iv_camera_chexiangleft = (ImageView) findViewById(R.id.iv_huoche_chexiang_left);
		iv_camera_chexiangright = (ImageView) findViewById(R.id.iv_huoche_chexiang_right);
		bt_finish = (Button) findViewById(R.id.bt_huoche_finish);

		bt_finish.setOnClickListener(this);
		iv_camera_dayundan.setOnClickListener(this);
		iv_camera_xiaoyundan.setOnClickListener(this);
		iv_camera_chexiang_top.setOnClickListener(this);
		iv_camera_chexiang_chehao.setOnClickListener(this);
		iv_camera_chexiangleft.setOnClickListener(this);
		iv_camera_chexiangright.setOnClickListener(this);
		iv_top_back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_top_left:
			new TuiChuDialogUtil(this).showDialog();
			break;
		case R.id.iv_huoche_dayundan:
			takePicDialog(iv_camera_dayundan, "431_1", lot + "," + lat, code,
					business);
			break;
		case R.id.iv_huoche_xiaoyundan:
			takePicDialog(iv_camera_xiaoyundan, "431_2", lot + "," + lat, code,
					business);
			break;
		case R.id.iv_huoche_chexiang_top:
			takePicDialog(iv_camera_chexiang_top, "432_1", lot + "," + lat,
					code, business);
			break;
		case R.id.iv_huoche_chexiang_chehao:
			takePicDialog(iv_camera_chexiang_chehao, "432_2", lot + "," + lat,
					code, business);
			break;
		case R.id.iv_huoche_chexiang_left:
			takePicDialog(iv_camera_chexiangleft, "433_1", lot + "," + lat,
					code, business);
			break;
		case R.id.iv_huoche_chexiang_right:
			takePicDialog(iv_camera_chexiangright, "433_2", lot + "," + lat,
					code, business);
			break;
		case R.id.bt_huoche_finish:
			if (step == 1) {
				step = 2;
				ChangeView(1);
			} else if (step == 2) {
				step=3;
				ChangeView(2);
			} else if (step == 3) {
				finish();
			}

			break;

		default:
			break;
		}
	}

	private void ChangeView(int flag) {
		switch (flag) {
		case 1:
			ll_stepone.setVisibility(View.GONE);
			ll_steptwo.setVisibility(View.VISIBLE);
			ll_stepthree.setVisibility(View.GONE);
			bt_StepOne.setBackgroundResource(R.drawable.step_background);
			bt_StepTwo.setBackgroundResource(R.drawable.step_background_pre);
			bt_StepThree.setBackgroundResource(R.drawable.step_background);

			break;
		case 2:
			ll_stepone.setVisibility(View.GONE);
			ll_steptwo.setVisibility(View.GONE);
			ll_stepthree.setVisibility(View.VISIBLE);
			bt_StepOne.setBackgroundResource(R.drawable.step_background);
			bt_StepTwo.setBackgroundResource(R.drawable.step_background);
			bt_StepThree.setBackgroundResource(R.drawable.step_background_pre);

			break;

		default:
			break;
		}
	}

	/**
	 * 弹出dialog，选择是拍照还是查看
	 * 
	 * @param iv
	 *            ImageView
	 * @param camera_position
	 *            机位
	 * @param place
	 *            地点（经纬度）
	 * @param code
	 *            编号
	 * @param business
	 *            操作类型
	 */
	private void takePicDialog(final ImageView iv,
			final String camera_position, final String place,
			final String code, final String business) {
		dialog = new AlertDialog.Builder(Huoche_YanChe_Activity.this)
				.setTitle("拍照").setMessage("点击\"拍照\"进入相机,点击\"查看\"浏览相片。")
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						ChangeViewUtil.changeicon(iv, 1);
						intent = new Intent(Huoche_YanChe_Activity.this,
								TakePicAndUpload_Activity.class);

						intent.putExtra("camera_position", camera_position);
						intent.putExtra("place", place);
						intent.putExtra("code", code);
						intent.putExtra("yw_type", "huocjx");
						intent.putExtra("business", business);
						startActivity(intent);
					}
				})

				.setNeutralButton("查看", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						intent = new Intent(Huoche_YanChe_Activity.this,
								ShowPic_Activity.class);
						intent.putExtra("code", code);
						intent.putExtra("position", camera_position);
						startActivity(intent);

					}
				}).create();
		dialog.show();
	}
}
