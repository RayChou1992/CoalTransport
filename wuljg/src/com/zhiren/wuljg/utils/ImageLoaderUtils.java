package com.zhiren.wuljg.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

public class ImageLoaderUtils {
	private static ImageLoader imageLoader = ImageLoader.getInstance();

	private ImageLoaderUtils() {
	}

	public static int FADEINDISPLAY = 1;

	/**
	 * 本方法适用于displayImage方法的参数中
	 * 
	 * @param defaultPic
	 *            缺省图片id
	 * @param isCacheInDisk
	 *            是否需要在本地缓存
	 * @return 图片显示选项对象
	 */
	public static DisplayImageOptions setImageOptiaons(int defaultPic, boolean isCacheInDisk) {
		return setImageOptions(defaultPic, isCacheInDisk, true, 0, 0, false, 0);
	}

	/**
	 * 本方法适用于displayImage方法的参数中
	 * 
	 * @param defaultPic
	 *            缺省图片id
	 * @param isCacheInDisk
	 *            是否需要在本地缓存
	 * @param isEqualProportion
	 *            是否等比例变形(true不会产生拉伸，false严格按照后面的宽高要求变形)
	 * @param width
	 *            指定图片显示宽度
	 * @param height
	 *            指定图片显示高度
	 * @return
	 */
	public static DisplayImageOptions setImageOptions(int defaultPic, boolean isCacheInDisk, boolean isEqualProportion, float width, float height) {
		return setImageOptions(defaultPic, isCacheInDisk, isEqualProportion, width, height, false, 0);
	}

	/**
	 * 本方法适用于displayImage方法的参数中
	 * 
	 * @param defaultPic
	 *            缺省图片id
	 * @param isCacheInDisk
	 *            是否需要在本地缓存
	 * @param isEqualProportion
	 *            是否等比例变形(true不会产生拉伸，false严格按照后面的宽高要求变形)
	 * @param width
	 *            指定图片显示宽度
	 * @param height
	 *            指定图片显示高度
	 * @param isRoundConner
	 *            是否显示为圆角图片
	 * @param roundPx
	 *            圆角半径
	 * @return 图片显示选项对象
	 */
	public static DisplayImageOptions setImageOptions(int defaultPic, boolean isCacheInDisk, final boolean isEqualProportion, final float width, final float height, final boolean isRoundConner, final float roundPx) {
		DisplayImageOptions option = null;
		Builder builder = null;
		BitmapProcessor postprocessor = null;

		if (width == 0 && height == 0) {// 不缩放图片时
			if (isRoundConner && roundPx > 0) {
				postprocessor = new BitmapProcessor() {

					@Override
					public Bitmap process(Bitmap bitmap) {
						// TODO Auto-generated method stub
						return GetRoundedCornerBitmap(bitmap, roundPx);
					}
				};
			}
		} else if (width == 0 || height == 0) {// 图片的宽高其一为0
			throw new RuntimeException("option中图片的宽高不能设置为0！");
		} else {// 图片被缩放时
			postprocessor = new BitmapProcessor() {
				@Override
				public Bitmap process(Bitmap bitmap) {
					float btwidth = bitmap.getWidth();
					float btheight = bitmap.getHeight();

					float scaleWidth = width / btwidth;
					float scaleHeight = height / btheight;
					Matrix matrix = new Matrix();
					if (!isEqualProportion) {// 如果是等比例缩放
						matrix.postScale(scaleWidth, scaleHeight);
					} else {// 如果不是等比例缩放
						matrix.postScale(Math.min(scaleWidth, scaleHeight), Math.min(scaleWidth, scaleHeight));
					}
					Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) btwidth, (int) btheight, matrix, true);
					if (isRoundConner) {// 如果要圆角图片
						if (roundPx <= 0) {
							throw new RuntimeException("圆角半径只能为正值！");
						} else {
							newBitmap = GetRoundedCornerBitmap(newBitmap, roundPx);
						}
					}
					return newBitmap;
				}
			};
		}
		BitmapProcessor bitmapprocessor = null;
		builder = new DisplayImageOptions.Builder();
		if (defaultPic == 0) {// 没有默认图，加载失败或者加载中时什么都不显示
			builder.cacheInMemory(true).cacheOnDisk(isCacheInDisk).postProcessor(bitmapprocessor).resetViewBeforeLoading(true)// 加载图片时重置ImageView
					.considerExifParams(false) // 是否关注jpeg的EXIF参数
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // 解码为bitmap时的尺寸类型，默认为每次压缩1/2直到小于目标尺寸
					.postProcessor(postprocessor)// 读取缓存后处理
					.build();
		} else {
			builder.showImageOnLoading(defaultPic).showImageForEmptyUri(defaultPic).showImageOnFail(defaultPic).cacheInMemory(true).cacheOnDisk(isCacheInDisk).postProcessor(bitmapprocessor).resetViewBeforeLoading(true)// 加载图片时重置ImageView
					.considerExifParams(false) // 是否关注jpeg的EXIF参数
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // 解码为bitmap时的尺寸类型，默认为每次压缩1/2直到小于目标尺寸
					.postProcessor(postprocessor)// 读取缓存后处理
					.build();
		}
		option = builder.build();
		return option;
	}

	/**
	 * 默认选项显示图片
	 * 
	 * @param uri
	 *            图片uri
	 * @param imageview
	 *            ImageView控件
	 */
	public static void disPlayImage(String uri, ImageView imageview) {
		disPlayImage(uri, imageview, null, null);
	}

	/**
	 * 含选项的显示图片
	 * 
	 * @param uri
	 *            图片uri
	 * @param imageview
	 *            ImageView控件
	 * @param option
	 *            图片显示选项，请调用ImageLoaderUtils.setImageOptions(...)进行设置
	 */
	public static void disPlayImage(String uri, ImageView imageview, DisplayImageOptions option) {
		disPlayImage(uri, imageview, option, null);
	}

	/**
	 * 含选项的显示图片
	 * 
	 * @param uri
	 *            图片uri
	 * @param imageview
	 *            ImageView控件
	 * @param option
	 *            图片显示选项，请调用ImageLoaderUtils.setImageOptions(...)进行设置
	 * @param ImageLoadingListenerType
	 *            图片监听器类型：ImageUtils.FADEINDISPLAY图片淡入效果
	 */
	public static void disPlayImage(String uri, ImageView imageview, DisplayImageOptions option, int ImageLoadingListenerType) {
		ImageLoadingListener listener = null;
		switch (ImageLoadingListenerType) {
		case 1:
			listener = new AnimateFirstDisplayListener();
			break;
		default:
			break;
		}
		disPlayImage(uri, imageview, option, listener);
	}

	/**
	 * 
	 * @param uri
	 *            图片uri
	 * @param imageview
	 *            ImageView控件
	 * @param option
	 *            图片显示选项，请调用ImageLoaderUtils.setImageOptions(...)进行设置
	 * @param listener
	 *            图片加载监听器
	 */
	public static void disPlayImage(String uri, ImageView imageview, DisplayImageOptions option, ImageLoadingListener listener) {

		imageLoader.displayImage(uri, imageview, option, listener);
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					displayedImages.add(imageUri);
				}
				FadeInBitmapDisplayer.animate(imageView, 1000);
			}
		}
	}

	private static Bitmap GetRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

	/**
	 * 通过url下载图片，可取消下载任务。方法中的线程睡眠是为了实现抛出异常取消下载任务。
	 * 
	 * @param imgUrl
	 * @return bitmap
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static Bitmap loadRmoteImageWithException(String imgUrl) throws InterruptedException, IOException {
		URL fileURL = null;
		Bitmap bitmap = null;
		fileURL = new URL(imgUrl);
		HttpURLConnection conn = (HttpURLConnection) fileURL.openConnection();
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		conn.setDoInput(true);
		Thread.sleep(1);
		conn.connect();
		InputStream is = conn.getInputStream();
		int length = (int) conn.getContentLength();
		if (length != -1) {
			byte[] imgData = new byte[length];
			byte[] buffer = new byte[512];
			int readLen = 0;
			int destPos = 0;
			while ((readLen = is.read(buffer)) > 0) {
				Thread.sleep(1);
				System.arraycopy(buffer, 0, imgData, destPos, readLen);
				destPos += readLen;
			}
			bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
		}
		return bitmap;
	}
	
	/**
	 * 通过url下载图片。
	 * @param imgUrl
	 * @return
	 */
	public static Bitmap loadRmoteImage(String imgUrl){
		Bitmap bmp = imageLoader.loadImageSync(imgUrl);
		return bmp;
	}
}
