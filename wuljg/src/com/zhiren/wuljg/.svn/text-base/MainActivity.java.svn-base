package com.zhiren.wuljg;

import java.util.TreeMap;

import com.zhiren.wuljg.activity.MenuActivity;
import com.zhiren.wuljg.entities.Result;
import com.zhiren.wuljg.entities.User;
import com.zhiren.wuljg.utils.ApiResult;
import com.zhiren.wuljg.utils.AppException;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.StringUtil;
import com.zhiren.wuljg.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnClickListener {

	private EditText et_username_login;
	private EditText et_password_login;
	private String userName;
	private String passWord;

	private Button bt_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		inti();
		initData();
		et_username_login = (EditText) findViewById(R.id.et_username_login);
		et_password_login = (EditText) findViewById(R.id.et_password_login);
		bt_login = (Button) findViewById(R.id.bt_login);
		bt_login.setOnClickListener(this);

	}

	private void inti() {

		et_username_login = (EditText) findViewById(R.id.et_username_login);
		et_password_login = (EditText) findViewById(R.id.et_password_login);

	}

	private void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login:
			login();
			break;

		default:
			break;
		}
	}

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.SUCCESS:
				ToastUtil.ShowMessage(MainActivity.this, "登陆成功");
				// 登录成功
				User user = (User) msg.obj;
				ZhiRen_Application.getApplication()
						.saveLoginInfo(user.userinfo);

				Intent intent = new Intent(MainActivity.this,
						MenuActivity.class);
				startActivity(intent);
				break;

			case Constant.ERROR:
				ToastUtil.ShowMessage(MainActivity.this, msg.obj + "");
				break;

			case Constant.EXCEPTION:
				ToastUtil.ShowMessage(MainActivity.this, "异常");
				break;

			default:
				break;
			}
			return false;
		}
	});

	/**
	 * 登录 操作
	 */
	private void login() {
		getData();
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
			Toast.makeText(MainActivity.this, "请输入正确的用户名和密码", 0).show();
		}

		new Thread() {
			public void run() {
				Message msg = handler.obtainMessage();
				try {
					TreeMap<String, Object> params = new TreeMap<String, Object>();
					params.put("username", userName);
					params.put("password", passWord);
					Result result = ApiResult.getServerResult(
							MainActivity.this, Constant.URL_LOGIN, params,
							User.class);

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
	 * 获取用户输入的用户名和密码
	 */
	private void getData() {
		userName = et_username_login.getText().toString().trim();
		passWord = et_password_login.getText().toString().trim();

	}

}
