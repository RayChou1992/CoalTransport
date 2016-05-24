package com.zhiren.wuljg.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.baidu.a.a.a.b;
import com.zhiren.wuljg.R;
import com.zhiren.wuljg.ZhiRen_Application;
import com.zhiren.wuljg.entities.XunDuo;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.GetUserInfo;
import com.zhiren.wuljg.utils.TakePicUtil;
import com.zhiren.wuljg.utils.TimeUtil;
import com.zhiren.wuljg.utils.ToastUtil;

/**
 * 2015-07-16 自定义相机，拍照
 * 
 * @author Ray
 * 
 */
public class TakePicAndUpload_Activity extends Activity implements Callback,
		OnClickListener, AutoFocusCallback {

	private boolean isClicked;
	private SurfaceView takePicSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Camera myCamera;
	private Button bt_take, bt_save, bt_delete;
	private LinearLayout linearLayout;
	private EditText et_yichangxinxi;

	// 要添加的水印的文字信息
	private String userName;
	private String date;

	private String phoneImei;
	// 谁开启的 activity fache
	private String isfrom;

	private Intent intent;

	private String is_fc;
	private String zhuangcsj;
	private long jigtdid;
	private String tongd;
	private String jgddtp;

	/**
	 * 需要上传的数值
	 */

	private File file;
	// 业务类型
	private String yw_type;
	private String business;
	private String JSESSIONID = GetUserInfo.getInfo();
	// 相机机位
	private String position;
	// 任务编号
	private String code;
	// 位置
	private String place;
	// 异常信息
	private String yichang;

	private String jiangpch;
	// 创建jpeg图片回调数据对象
	PictureCallback jpeg = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				bt_take.setVisibility(View.GONE);
				linearLayout.setVisibility(View.VISIBLE);
				et_yichangxinxi.setVisibility(View.VISIBLE);
				// 获得图片
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

				int width = bm.getWidth();
				int height = bm.getHeight();
				Bitmap icon = Bitmap.createBitmap(width, height,
						Bitmap.Config.ARGB_8888);

				Canvas canvas = new Canvas(icon);

				Paint photoPaint = new Paint();// 建立画笔

				photoPaint.setDither(true); // 获取更清晰的图像采样
				photoPaint.setFilterBitmap(true);// 过滤一些
				Rect src = new Rect(0, 0, bm.getWidth(), bm.getHeight());// 创建一个指定的新矩形的坐标

				Rect dst = new Rect(0, 0, bm.getWidth(), bm.getHeight());// 创建一个指定的新矩形的坐标

				canvas.drawBitmap(bm, src, dst, photoPaint);// 将photo 缩放或则扩大到
															// dst使用的填充区photoPaint
				Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
						| Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
				textPaint.setTextSize(100.0f);// 字体大小
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
				textPaint.setColor(Color.RED);// 采用的颜色
				canvas.drawText(date, 100, 106, textPaint);// 绘制上去字，开始位置x,y
				canvas.drawText(userName, 100, 212, textPaint);// 绘制上去字，开始位置x,y
				canvas.drawText("IMEI:" + phoneImei, 100, 318, textPaint);// 绘制上去字，开始位置x,y
				canvas.save(Canvas.ALL_SAVE_FLAG);
				canvas.restore();

				File fileFolder = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/wuljg/");
				String fileName = code + "_" + date + "_" + position + ".jpg";
				if (!fileFolder.exists()) {
					fileFolder.mkdir();
				}
				file = new File(fileFolder, fileName);
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(file));
				icon.compress(Bitmap.CompressFormat.JPEG, 30, bos);// 将图片压缩到流中
				bos.flush();// 输出
				bos.close();// 关闭

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题

		setContentView(R.layout.activity_takepic);
		intiView();
		getInfo();

	}

	/**
	 * 获取基本信息
	 */
	private void getInfo() {
		// 获取手机的IMEI号
		TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		phoneImei = TelephonyMgr.getDeviceId();
		intent = getIntent();
		userName = ZhiRen_Application.getApplication().getUserInfo().name;
		date = TimeUtil.getDate();

		if (intent != null) {

			position = intent.getStringExtra("camera_position");
			// code = intent.getStringExtra("code");
			code = intent.getStringExtra("code");
			place = intent.getStringExtra("place");
			yichang = intent.getStringExtra("yichang");
			jgddtp = intent.getStringExtra("tp_type");
			yw_type = intent.getStringExtra("yw_type");
			business = intent.getStringExtra("business");
			is_fc = intent.getStringExtra("is_fc");
			zhuangcsj = intent.getStringExtra("zhuangcsj");
			jigtdid = intent.getLongExtra("jigtdid", 0);
			tongd = intent.getStringExtra("tongd");

			isfrom = intent.getStringExtra("isfrom");

		}

	}

	private void intiView() {
		linearLayout = (LinearLayout) findViewById(R.id.takePic_ll3);
		takePicSurfaceView = (SurfaceView) findViewById(R.id.takePicSurfaceView);
		// 获得相机预览空间句柄
		mSurfaceHolder = takePicSurfaceView.getHolder();
		// 添加句柄回调事件
		mSurfaceHolder.addCallback(this);
		// 设置类型
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		takePicSurfaceView.setOnClickListener(this);
		et_yichangxinxi = (EditText) findViewById(R.id.et_takePic_yichang);
		bt_delete = (Button) findViewById(R.id.bt_takePic_delete);
		bt_save = (Button) findViewById(R.id.bt_takePic_save);
		bt_take = (Button) findViewById(R.id.bt_takePic_take);
		bt_delete.setOnClickListener(this);
		bt_save.setOnClickListener(this);
		bt_take.setOnClickListener(this);

	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// 设置参数并开始预览
		Camera.Parameters params = myCamera.getParameters();
		params.setPictureFormat(PixelFormat.JPEG);
		params.set("rotation", 90);
		myCamera.setParameters(params);
		myCamera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 开启相机
		if (myCamera == null) {
			myCamera = Camera.open();
			try {
				myCamera.setPreviewDisplay(holder);
				myCamera.setDisplayOrientation(90);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		myCamera.stopPreview();
		myCamera.release();
		myCamera = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.takePicSurfaceView:

			if (!isClicked) {
				myCamera.autoFocus(this);// 自动对焦
			}

			break;
		case R.id.bt_takePic_take:
			isClicked = true;
			myCamera.autoFocus(this);// 自动对焦
			myCamera.takePicture(null, null, jpeg);
			break;
		case R.id.bt_takePic_delete:
			isClicked = false;

			// 消除预览
			myCamera.stopPreview();
			myCamera.startPreview();
			et_yichangxinxi.setVisibility(View.GONE);
			linearLayout.setVisibility(View.GONE);
			bt_take.setVisibility(View.VISIBLE);
			break;
		case R.id.bt_takePic_save:

			// 上传到服务器，并退出相机
			yichang = et_yichangxinxi.getText().toString().trim();

			if (isfrom == null || "".equals(isfrom)) {
				TakePicUtil.uploadFile(JSESSIONID, file,
						Constant.URL_UPLOADTPXX, yw_type, business, position,
						code, yichang, place);
			} else {
				TakePicUtil.uploadFile(JSESSIONID, file,
						Constant.URL_UPLOADTPXX, yw_type, position, code,
						yichang, place, isfrom, jgddtp, is_fc, zhuangcsj,
						jigtdid, tongd, TakePicAndUpload_Activity.this);
			}

			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		if (success) {
			// 设置参数,并拍照
			Camera.Parameters params = myCamera.getParameters();
			params.setPictureFormat(PixelFormat.JPEG);
			params.set("rotation", 90);
			myCamera.setParameters(params);

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			setResult(1, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
