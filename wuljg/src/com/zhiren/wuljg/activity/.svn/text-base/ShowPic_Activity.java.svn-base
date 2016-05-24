package com.zhiren.wuljg.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.zhiren.wuljg.R;
import com.zhiren.wuljg.ZhiRen_Application;
import com.zhiren.wuljg.adapter.MyGridViewAdapter;
import com.zhiren.wuljg.entities.MenuList;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.GetPicUtil;
import com.zhiren.wuljg.utils.GetUserInfo;
import com.zhiren.wuljg.utils.ImageLoaderUtils;
import com.zhiren.wuljg.utils.LogUtils;
import com.zhiren.wuljg.utils.ToastUtil;
import com.zhiren.wuljg.view.DialogLoading;

/**
 * 显示图片界面 2015-07-16
 * 
 * @author Ray
 *
 */
public class ShowPic_Activity extends Activity implements OnItemClickListener {

	// 巡视任务code
	private String code;
	private String position;
	private GridView gv_showpic;
	private Button bt_takepic;
	private Intent intent;
	private Bitmap myBitmap;

	private ArrayList<HashMap<String, Object>> imgSites;
	private ArrayList<HashMap<String, Object>> imgSitesList;
	private MyGridViewAdapter adapter;
	private String JSESSIONID;
	private DialogLoading loading;

	private Handler handler = new Handler(new Handler.Callback() {

		@SuppressWarnings("unchecked")
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.SUCCESS:
				loadingDismiss();

				imgSitesList = new ArrayList<HashMap<String, Object>>();
				imgSites = (ArrayList<HashMap<String, Object>>) msg.obj;
				if (imgSites == null || imgSites.size() == 0) {
					ToastUtil.ShowMessage(ShowPic_Activity.this, "暂无图片");
				} else {

					for (int i = 0; i < imgSites.size(); i++) {
						
						//根据position过滤图片
						if (imgSites.get(i).get("position").equals(position)) {
							imgSitesList.add(imgSites.get(i));
							if (imgSitesList.size() != 0) {							
								adapter = new MyGridViewAdapter(position, imgSitesList,
										ShowPic_Activity.this);
								gv_showpic.setAdapter(adapter);
							}
						} else {
							
						}
					}

					

				}

				break;

			default:
				break;
			}

			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showpic);
		gv_showpic = (GridView) findViewById(R.id.showPicGridView);
		gv_showpic.setOnItemClickListener(this);
		loading = new DialogLoading(ShowPic_Activity.this);
		// imgSites.add(Constant.BASE_URL + "/upload/huocxs/20150721/1.jpg");
		// imgSites.add("http://images.sohu.com/uiue/sohu_logo/beijing2008/2008sohu.gif");

		JSESSIONID = GetUserInfo.getInfo();
		intent = getIntent();
		if (intent != null) {

			code = intent.getStringExtra("code");
			position = intent.getStringExtra("position");

			LogUtils.e("3position=" + position);
		}
		LogUtils.e("1position=" + position);
		
		getImgUri(code, JSESSIONID, Constant.URL_GET_IMG_URI,
				ShowPic_Activity.this);

		// adapter = new MyGridViewAdapter(flag, imgSites,
		// ShowPic_Activity.this);
		// gv_showpic.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	/**
	 * 关闭加载圈圈
	 */
	private void loadingDismiss() {
		if (loading.isShowing()) {
			loading.dismiss();
		}
	}

	/**
	 * 获取服务器图片的地址
	 */
	public void getImgUri(final String code, final String JSESSIONID,
			final String url, Context context) {

		final Message msg = handler.obtainMessage();
		final ArrayList<HashMap<String, Object>> imgSites = new ArrayList<>();

		loading.show();
		new Thread() {
			public void run() {
				PostMethod postMethod = new PostMethod(url);

				try {
					// FilePart：用来上传文件的类

					// 汉字的格式：new StringPart("status", URLEncoder.encode("汉字",
					// "utf-8"))
					Part[] parts = { new StringPart("code", code) };

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
							.setConnectionTimeout(5000);// 设置连接时间
					int status = client.executeMethod(postMethod);
					LogUtils.e(status + "=status");
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
						imgSites.clear();
						for (int i = 1; i < myJsonArray.length(); i++) {
							// 获取每一个JsonObject对象
							JSONObject myjObject = myJsonArray.getJSONObject(i);
							String url = myjObject.getString("src");
							String position = myjObject.getString("position");
							HashMap<String, Object> map = new HashMap<>();
							map.put("src", Constant.BASE_URL_PIC + url);
							map.put("position", position);
							imgSites.add(map);
							LogUtils.e(imgSites.get(i - 1).toString() + "-----"
									+ imgSites.size());
						}

						msg.what = Constant.SUCCESS;
						msg.obj = imgSites;

					} else {
						msg.what = Constant.ERROR;
						msg.obj = null;

						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = Constant.ERROR;
				} finally {
					// 释放连接
					postMethod.releaseConnection();
				}

				handler.sendMessage(msg);
			};
		}.start();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

}