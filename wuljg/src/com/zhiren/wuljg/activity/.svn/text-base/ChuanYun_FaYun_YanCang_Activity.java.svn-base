package com.zhiren.wuljg.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiren.wuljg.BaseActivity;
import com.zhiren.wuljg.R;
import com.zhiren.wuljg.ZhiRen_Application;
import com.zhiren.wuljg.entities.ChuanCangInfo;
import com.zhiren.wuljg.entities.MenuList;
import com.zhiren.wuljg.entities.Result;
import com.zhiren.wuljg.utils.ApiResult;
import com.zhiren.wuljg.utils.ChangeViewUtil;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.GetUserInfo;
import com.zhiren.wuljg.utils.LogUtils;
import com.zhiren.wuljg.utils.StringUtil;
import com.zhiren.wuljg.utils.ToastUtil;
import com.zhiren.wuljg.utils.TuiChuDialogUtil;
import com.zhiren.wuljg.view.DialogLoading;

public class ChuanYun_FaYun_YanCang_Activity extends BaseActivity implements
		OnClickListener {

	// 页面控件
	private RelativeLayout relativeLayout_chuanyun_shuichidan;
	private TextView tv_title;
	private Button bt_kongcang, bt_shicang, bt_fengcang;
	private TextView tv_ship_name;
	private EditText et_chaunyun_shuichiliang;
	private ImageView iv_chuanyun_shuichidan;
	private ImageView iv_chuanyun_camera_shuichidan;
	private ImageView iv_chuanyun_camera1, iv_chuanyun_camera2,
			iv_chuanyun_camera3;
	private Button bt_finish;
	private ImageView iv_top_back;

	// 标识是进行到了那个步骤
	private int step = 1;
	private String yc_type = "1";// 1是发运验舱，2是接卸验舱

	// 相机的位置：一共11个相机机位
	private String camera_position;

	// 请求网络对话框
	private DialogLoading loading;

	// 验舱id
	private String yancang_id;
	// 验舱前后的水尺量
	private String shuicl_qian;
	private String shuicl_hou;
	private String code;

	private Intent intent;
	private ChuanCangInfo mCangInfo;

	private List<ChuanCangInfo> chuanCangInfo_list;

	// 监管合同批次号
	private String jiangpch;
	// 指令ID
	private String zhilid;
	private String chuanm;
	private String place;
	private String business;
	private List<HashMap<String, Object>> imgSites;

	private String JSESSIONID;
	private Dialog dialog;

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 查询初始化数据
				loadingDismiss();
				// mCangInfo=(ChuanCangInfo) msg.obj;
				if (msg.obj != null) {
					ToastUtil.ShowMessage(ChuanYun_FaYun_YanCang_Activity.this,
							"加载成功");

					chuanCangInfo_list = (List<ChuanCangInfo>) msg.obj;
				} else {
					ToastUtil.ShowMessage(ChuanYun_FaYun_YanCang_Activity.this,
							"请求失败");
				}

				if (chuanCangInfo_list != null
						&& chuanCangInfo_list.size() != 0) {
					// 由于这个只有一条数据，所以直接get(0)
					mCangInfo = chuanCangInfo_list.get(0);
					step = Integer.valueOf(mCangInfo.type);
					yancang_id = mCangInfo.Id;
					LogUtils.e("步骤=" + step);
					LogUtils.e("水尺量" + mCangInfo.beiz);
				}

				getPicUrl();

				break;

			case 2:
				// 更改验舱步骤
				loadingDismiss();
				ToastUtil.ShowMessage(ChuanYun_FaYun_YanCang_Activity.this,
						"提交成功");
				// relativeLayout_chuanyun_shuichidan.setVisibility(View.GONE);

				if (step == 3) {
					finish();
				} else {
					queryInfo(Constant.URL_GET_CHUANYUN_INFO);
				}

				break;
			case 3:
				// 没有输入水尺量
				loadingDismiss();
				ToastUtil.ShowMessage(ChuanYun_FaYun_YanCang_Activity.this,
						"请输入水尺量");
				et_chaunyun_shuichiliang.requestFocus();
				break;
			case 4:
				// 初始化布局
				setView();

				break;
			case Constant.ERROR:
				loadingDismiss();
				break;

			case Constant.EXCEPTION:
				loadingDismiss();
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chuanyun_fayun_yancang);
		InitView();
		loading = new DialogLoading(ChuanYun_FaYun_YanCang_Activity.this);

		intent = getIntent();
		if (intent != null) {
			zhilid = String.valueOf(intent.getExtras().getLong("zhilid"));
			jiangpch = intent.getExtras().getString("jiangpch");
			chuanm = intent.getExtras().getString("chuanm");
			place = intent.getExtras().getString("place");
			tv_ship_name.setText(chuanm);
			code = intent.getExtras().getString("code");
		}
		JSESSIONID = GetUserInfo.getInfo();
		imgSites = new ArrayList<HashMap<String, Object>>();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 查询信息
		queryInfo(Constant.URL_GET_CHUANYUN_INFO);
	}

	/**
	 * 查询服务器，得到初始化数据
	 */
	private void queryInfo(final String url) {
		loading.show();
		final Message msg = handler.obtainMessage();
		new Thread() {
			public void run() {

				try {
					TreeMap<String, Object> params = new TreeMap<String, Object>();
					params.put("jiangpch", jiangpch);
					params.put("yc_type", yc_type);

					Result result = ApiResult.getServerArrayResult(
							ChuanYun_FaYun_YanCang_Activity.this,
							Constant.URL_GET_CHUANYUN_INFO, params,
							ChuanCangInfo.class);
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

	/**
	 * 根据服务器返回的数据，初始化布局
	 */
	private void setView() {

		if (step == 1) {
			bt_kongcang.setBackgroundResource(R.drawable.step_background_pre);
			bt_shicang.setBackgroundResource(R.drawable.step_background);
			bt_fengcang.setBackgroundResource(R.drawable.step_background);
			relativeLayout_chuanyun_shuichidan.setVisibility(View.VISIBLE);
			if (mCangInfo != null && !"".equals(mCangInfo.beiz)) {

				shuicl_qian = mCangInfo.beiz;
			}
			LogUtils.e("qian=" + shuicl_qian);
			et_chaunyun_shuichiliang.setText(shuicl_qian);
			for (int i = 0; i < imgSites.size(); i++) {
				if ("311_1".equals(imgSites.get(i).get("position"))) {
					ChangeViewUtil.changeicon(iv_chuanyun_camera_shuichidan, 1);
				} else if ("311_2".equals(imgSites.get(i).get("position"))) {
					ChangeViewUtil.changeicon(iv_chuanyun_camera1, 1);

				} else if ("311_3".equals(imgSites.get(i).get("position"))) {
					ChangeViewUtil.changeicon(iv_chuanyun_camera2, 1);

				} else if ("311_4".equals(imgSites.get(i).get("position"))) {
					ChangeViewUtil.changeicon(iv_chuanyun_camera3, 1);

				}
			}
		} else if (step == 2) {
			bt_kongcang.setBackgroundResource(R.drawable.step_background);
			bt_shicang.setBackgroundResource(R.drawable.step_background_pre);
			bt_fengcang.setBackgroundResource(R.drawable.step_background);
			relativeLayout_chuanyun_shuichidan.setVisibility(View.GONE);

			for (int i = 0; i < imgSites.size(); i++) {
				if ("312_2".equals(imgSites.get(i).get("position"))) {
					ChangeViewUtil.changeicon(iv_chuanyun_camera1, 1);

				} else if ("312_3".equals(imgSites.get(i).get("position"))) {
					ChangeViewUtil.changeicon(iv_chuanyun_camera2, 1);

				} else if ("312_4".equals(imgSites.get(i).get("position"))) {
					ChangeViewUtil.changeicon(iv_chuanyun_camera3, 1);
				}
			}
		} else if (step == 3) {
			bt_kongcang.setBackgroundResource(R.drawable.step_background);
			bt_shicang.setBackgroundResource(R.drawable.step_background);
			bt_fengcang.setBackgroundResource(R.drawable.step_background_pre);
			relativeLayout_chuanyun_shuichidan.setVisibility(View.VISIBLE);
			shuicl_hou = mCangInfo.beiz;
			et_chaunyun_shuichiliang.setText(shuicl_hou);
			for (int i = 0; i < imgSites.size(); i++) {
				if ("313_1".equals(imgSites.get(i).get("position"))) {
					ChangeViewUtil.changeicon(iv_chuanyun_camera_shuichidan, 1);
				} else if ("313_2".equals(imgSites.get(i).get("position"))) {
					ChangeViewUtil.changeicon(iv_chuanyun_camera1, 1);

				} else if ("313_3".equals(imgSites.get(i).get("position"))) {
					ChangeViewUtil.changeicon(iv_chuanyun_camera2, 1);

				} else if ("313_4".equals(imgSites.get(i).get("position"))) {
					ChangeViewUtil.changeicon(iv_chuanyun_camera3, 1);
				}
			}
		}

	}

	/**
	 * 实例化布局控件
	 */
	private void InitView() {
		relativeLayout_chuanyun_shuichidan = (RelativeLayout) findViewById(R.id.relativeLayout_chuanyun_shuichidan);
		iv_top_back = (ImageView) findViewById(R.id.common_top_left);
		tv_title = (TextView) findViewById(R.id.common_top_title);
		tv_title.setText("验舱");
		tv_ship_name = (TextView) findViewById(R.id.tv_ship_name);
		bt_fengcang = (Button) findViewById(R.id.tv_chuanyun_yancang_step_fengcang);
		bt_kongcang = (Button) findViewById(R.id.tv_chuanyun_yancang_step_kongcang);
		bt_shicang = (Button) findViewById(R.id.tv_chuanyun_yancang_step_shicang);
		et_chaunyun_shuichiliang = (EditText) findViewById(R.id.et_chaunyun_shuichiliang);
		iv_chuanyun_shuichidan = (ImageView) findViewById(R.id.iv_chuanyun_shuichidan);
		iv_chuanyun_camera_shuichidan = (ImageView) findViewById(R.id.iv_chuanyun_camera_shuichidan);
		iv_chuanyun_camera1 = (ImageView) findViewById(R.id.iv_chuanyun_camera1);
		iv_chuanyun_camera2 = (ImageView) findViewById(R.id.iv_chuanyun_camera2);
		iv_chuanyun_camera3 = (ImageView) findViewById(R.id.iv_chuanyun_camera3);
		bt_finish = (Button) findViewById(R.id.bt_chuanyun_finish);

		iv_top_back.setOnClickListener(this);
		iv_chuanyun_camera_shuichidan.setOnClickListener(this);
		iv_chuanyun_camera1.setOnClickListener(this);
		iv_chuanyun_camera2.setOnClickListener(this);
		iv_chuanyun_camera3.setOnClickListener(this);
		bt_finish.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		intent = new Intent(ChuanYun_FaYun_YanCang_Activity.this,
				TakePicAndUpload_Activity.class);
		switch (v.getId()) {
		case R.id.common_top_left:
			new TuiChuDialogUtil(ChuanYun_FaYun_YanCang_Activity.this)
					.showDialog();
			break;
		case R.id.iv_chuanyun_camera_shuichidan:
			if (step == 1) {
				camera_position = "311_1";
				business = "kongc_shuicdpz";
			} else if (step == 3) {
				business = "fengc_shuicdpz";
				camera_position = "313_1";
			}
			dialog = new AlertDialog.Builder(
					ChuanYun_FaYun_YanCang_Activity.this)
					.setTitle("拍照")
					.setMessage("点击\"拍照\"进入相机,点击\"查看\"浏览相片。")
					.setPositiveButton("拍照",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									ChangeViewUtil.changeicon(
											iv_chuanyun_camera_shuichidan, 1);
									intent = new Intent(
											ChuanYun_FaYun_YanCang_Activity.this,
											TakePicAndUpload_Activity.class);

									intent.putExtra("camera_position",
											camera_position);
									// intent.putExtra("code", jiangpch);
									intent.putExtra("place", place);
									intent.putExtra("code", code);
									intent.putExtra("yw_type", "chuanyfy");
									intent.putExtra("business", business);
									startActivity(intent);
								}
							})

					.setNeutralButton("查看",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									intent = new Intent(
											ChuanYun_FaYun_YanCang_Activity.this,
											ShowPic_Activity.class);
									intent.putExtra("code", code);
									intent.putExtra("position", camera_position);
									startActivity(intent);

								}
							}).create();
			dialog.show();
			break;
		case R.id.iv_chuanyun_camera1:
			if (step == 1) {
				camera_position = "311_2";
				business = "kongc";
			} else if (step == 2) {
				camera_position = "312_2";
				business = "shic";
			} else if (step == 3) {
				camera_position = "313_2";
				business = "fengc";
			}
			dialog = new AlertDialog.Builder(
					ChuanYun_FaYun_YanCang_Activity.this)
					.setTitle("拍照")
					.setMessage("点击\"拍照\"进入相机,点击\"查看\"浏览相片。")
					.setPositiveButton("拍照",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									ChangeViewUtil.changeicon(
											iv_chuanyun_camera1, 1);
									intent = new Intent(
											ChuanYun_FaYun_YanCang_Activity.this,
											TakePicAndUpload_Activity.class);

									intent.putExtra("camera_position",
											camera_position);
									intent.putExtra("place", place);
									intent.putExtra("code", code);
									intent.putExtra("yw_type", "chuanyfy");
									intent.putExtra("business", business);
									startActivity(intent);
								}
							})

					.setNeutralButton("查看",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									intent = new Intent(
											ChuanYun_FaYun_YanCang_Activity.this,
											ShowPic_Activity.class);
									intent.putExtra("position", camera_position);
									intent.putExtra("code", code);
									LogUtils.e("fasong=" + jiangpch);
									startActivity(intent);

								}
							}).create();
			dialog.show();
			break;
		case R.id.iv_chuanyun_camera2:
			if (step == 1) {
				camera_position = "311_3";
				business = "kongc";
			} else if (step == 2) {
				camera_position = "312_3";
				business = "shic";
			} else if (step == 3) {
				camera_position = "313_3";
				business = "fengc";
			}
			dialog = new AlertDialog.Builder(
					ChuanYun_FaYun_YanCang_Activity.this)
					.setTitle("拍照")
					.setMessage("点击\"拍照\"进入相机,点击\"查看\"浏览相片。")
					.setPositiveButton("拍照",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									ChangeViewUtil.changeicon(
											iv_chuanyun_camera2, 1);
									intent = new Intent(
											ChuanYun_FaYun_YanCang_Activity.this,
											TakePicAndUpload_Activity.class);

									intent.putExtra("camera_position",
											camera_position);
									intent.putExtra("place", place);
									intent.putExtra("code", code);
									intent.putExtra("yw_type", "chuanyfy");
									intent.putExtra("business", business);
									startActivity(intent);
								}
							})

					.setNeutralButton("查看",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									intent = new Intent(
											ChuanYun_FaYun_YanCang_Activity.this,
											ShowPic_Activity.class);
									intent.putExtra("code", code);
									intent.putExtra("position", camera_position);
									startActivity(intent);

								}
							}).create();
			dialog.show();
			break;
		case R.id.iv_chuanyun_camera3:
			if (step == 1) {
				camera_position = "311_4";
				business = "kongc";
			} else if (step == 2) {
				camera_position = "312_4";
				business = "shic";
			} else if (step == 3) {
				camera_position = "313_4";
				business = "fengc";
			}
			dialog = new AlertDialog.Builder(
					ChuanYun_FaYun_YanCang_Activity.this)
					.setTitle("拍照")
					.setMessage("点击\"拍照\"进入相机,点击\"查看\"浏览相片。")
					.setPositiveButton("拍照",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									ChangeViewUtil.changeicon(
											iv_chuanyun_camera3, 1);
									intent = new Intent(
											ChuanYun_FaYun_YanCang_Activity.this,
											TakePicAndUpload_Activity.class);

									intent.putExtra("camera_position",
											camera_position);
									intent.putExtra("place", place);
									intent.putExtra("code", code);
									intent.putExtra("yw_type", "chuanyfy");
									intent.putExtra("business", business);
									startActivity(intent);
								}
							})

					.setNeutralButton("查看",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									intent = new Intent(
											ChuanYun_FaYun_YanCang_Activity.this,
											ShowPic_Activity.class);
									intent.putExtra("code", code);
									intent.putExtra("position", camera_position);
									startActivity(intent);

								}
							}).create();
			dialog.show();
			break;
		case R.id.bt_chuanyun_finish:
			// 提交更改当前操作步骤已经完成，即当前步骤加1
			LogUtils.e(step + "=step");
			changeYanCangStatus();
			break;
		default:
			break;
		}

	}

	private void loadingDismiss() {
		if (loading != null && loading.isShowing()) {
			loading.dismiss();
		}
	}

	/**
	 * 更改验舱的步骤
	 */

	private void changeYanCangStatus() {
		final Message msg = handler.obtainMessage();
		new Thread() {
			public void run() {
				try {
					TreeMap<String, Object> params = new TreeMap<String, Object>();

					switch (step) {
					case 1:
						// 页面更新
						shuicl_qian = et_chaunyun_shuichiliang.getText()
								.toString();

						if (shuicl_qian != null && !"".equals(shuicl_qian)) {
							params.put("shuicl", shuicl_qian);
							params.put("id", yancang_id);
							params.put("wuljgpch", jiangpch);
							params.put("type", 2);
							params.put("yc_type", yc_type);
							params.put("zy_type", "1");

						} else {
							msg.what = 3;
							handler.sendMessage(msg);
						}

						break;
					case 2:
						params.put("shuicl", shuicl_qian);
						params.put("id", yancang_id);
						params.put("wuljgpch", jiangpch);
						params.put("type", 3);
						params.put("yc_type", yc_type);
						params.put("zy_type", "1");

						break;
					case 3:
						shuicl_hou = et_chaunyun_shuichiliang.getText()
								.toString();
						if (shuicl_hou != null && !"".equals(shuicl_hou)) {

							params.put("shuicl", shuicl_hou);

						} else {
							msg.what = 3;
							handler.sendMessage(msg);
						}
						params.put("type", 3);
						params.put("wuljgpch", jiangpch);
						params.put("id", yancang_id);
						params.put("yc_type", yc_type);
						params.put("zy_type", "1");
						// handler.sendMessage(msg);
						// finish();
						break;
					default:
						break;
					}

					Result result = ApiResult.getServerResult(
							ChuanYun_FaYun_YanCang_Activity.this,
							Constant.URL_GET_SHUICL, params, MenuList.class);
					if (result.getStatus() == Constant.SUCCESS) {
						msg.what = 2;
						msg.obj = result.getObject();
					} else {
						msg.what = Constant.ERROR;
						if (!StringUtil.isEmpty(result.getMsg())) {
							msg.obj = result.getMsg();
						}
					}
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = Constant.EXCEPTION;
					msg.obj = e;
				}

			};

		}.start();

	}

	/**
	 * 获得图片的url地址
	 */

	private void getPicUrl() {
		new Thread() {
			public void run() {
				PostMethod postMethod = new PostMethod(Constant.URL_GET_IMG_URI);

				final Message msg = handler.obtainMessage();
				try {
					// FilePart：用来上传文件的类

					// 汉字的格式：new StringPart("status", URLEncoder.encode("汉字",
					// "utf-8"))
					Part[] parts = { new StringPart("code", jiangpch) };

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
							map.put("src", Constant.BASE_URL_PIC + url);
							map.put("position", position);
							imgSites.add(map);
							System.out.println(imgSites.get(i - 1));
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			new TuiChuDialogUtil(ChuanYun_FaYun_YanCang_Activity.this)
					.showDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
}
