package com.zhiren.wuljg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zhiren.wuljg.entities.User.UserInfo;
import com.zhiren.wuljg.entities.XunDuo;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.StringUtil;

public class ZhiRen_Application extends Application {

	// 用于标识某个相机方位，拍摄的张数
	public static HashMap<Object, Object> cishu;

	public static String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	private static ZhiRen_Application mInstance;

	// 全局的巡垛对象
	public static XunDuo myXunDuo = new XunDuo();

	/**
	 * 暴漏上下文
	 * 
	 * @return
	 */
	public static ZhiRen_Application getApplication() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		SDKInitializer.initialize(getApplicationContext());
		cishu = new HashMap<Object, Object>();
		cishu.put(1, 1);
		cishu.put(2, 1);
		cishu.put(3, 1);
		cishu.put(4, 1);
		cishu.put(5, 1);
		mInstance = this;
		initImageLoader();
	}

	public String getPhoneNum() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String number = tm.getLine1Number();
		return number;
	}

	public PackageInfo getPackageInfo() {
		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageInfo;
	}

	public String getDeviceID() {
		String android_id = android.provider.Settings.Secure.getString(
				getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		String tmSerial = getTelephonyManager().getSimSerialNumber();
		String device_id = getTelephonyManager().getDeviceId();
		if (tmSerial == null) {
			tmSerial = "000000000000";
		}
		UUID uuid = new UUID(android_id.hashCode(),
				((long) device_id.hashCode() << 32) | tmSerial.hashCode());
		return uuid.toString();
	}

	@SuppressLint("ServiceCast")
	public TelephonyManager getTelephonyManager() {
		TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm;
	}

	public String getMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 清除用户信息
	 */
	public void cleanLoginInfo() {
		Editor userData = getSharedPreferences("user", 0).edit();
		userData.clear();
		userData.commit();
	}

	/**
	 * 保存用户信息
	 */
	public void saveLoginInfo(UserInfo user) {
		Editor editor = getSharedPreferences("user", 0).edit();
		editor.putString("id", user.id);
		editor.putString("name", user.name);
		editor.putString("session", user.session);
		editor.commit();
	}

	public UserInfo getUserInfo() {
		UserInfo user_Info = new UserInfo();
		SharedPreferences userInfo = getSharedPreferences("user", 0);
		user_Info.id = userInfo.getString("id", "0");
		user_Info.name = userInfo.getString("name", "");
		user_Info.session = userInfo.getString("session", "");
		return user_Info;
	}

	/** 是否登录 **/
	public boolean isLogin() {
		UserInfo user = new UserInfo();
		SharedPreferences userData = getSharedPreferences("user", 0);
		user.id = userData.getString("userid", "0");
		user.session = (userData.getString("session", ""));
		if (user.id.equals("0") && !StringUtil.isEmpty(user.session)) {
			return true;
		}
		return false;
	}

	/** 初始化ImaageLoader **/
	public void initImageLoader() {
		Boolean isHaveSD = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
		File ImageCacheDir;
		if (isHaveSD) {
			ImageCacheDir = new File(getPicPath("ImageLoaderCach"));
			ImageCacheDir.mkdir();
		} else {
			ImageCacheDir = getApplicationContext().getCacheDir();
		}
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
		// .memoryCacheExtraOptions(480, 800) // default = 内存缓存最大图片宽度，默认屏幕宽度
				.diskCacheExtraOptions(480, 800, null)// 重构或压缩下载后的图片存到硬盘选项，参数为下载图片的最大宽高和序列化到硬盘的处理程序。使用了此选项会降低效率。
				// .taskExecutor(null)//自定义任务处理器，无需设置
				// .taskExecutorForCachedImages(null)//自定义任务处理器，无需设置
				.threadPoolSize(5) // 图片展示线程池大小
				// .threadPriority(Thread.NORM_PRIORITY - 1) // default
				.tasksProcessingOrder(QueueProcessingType.FIFO) // 加载展示图片任务队列顺序，默认先进先出
				.denyCacheImageMultipleSizesInMemory()// 拒绝同一图片不同尺寸存入内存，一张图片只能存一份到内存，后来的尺寸会覆盖内存中先前尺寸版本
				// .memoryCache(new LruMemoryCache(2 * 1024 * 1024))//内存缓存大小为2m
				.memoryCacheSizePercentage(13) // 内存缓存大小为每个应用可用内存的制定百分比，默认使用lru
				.diskCache(new UnlimitedDiscCache(ImageCacheDir)) // 不限制本地缓存空间大小，使用lru，缓存速度快
				.diskCacheSize(50 * 1024 * 1024)// 本地缓存大小50m
				.diskCacheFileCount(100)// 缓存100个文件
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // 本地缓存文件命名法，哈希值命名
				.imageDownloader(
						new BaseImageDownloader(getApplicationContext())) // 默认图片下载器，HttpURLConnection
				.imageDecoder(new BaseImageDecoder(true)) // 默认解码器，联网获取流解码为bitmap
				// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// //Simple为最简陋，一般不用，而是在界面中重写DisplayImageOptions
				// .writeDebugLogs()// 打印debug日志
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 拼装图片缓存绝对路径
	 */
	public String getPicPath(String path) {
		String absolutePath = null;
		absolutePath = Constant.BASE_PIC_URI + path + "/";
		return absolutePath;
	}

}
