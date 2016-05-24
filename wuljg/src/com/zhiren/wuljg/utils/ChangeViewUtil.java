package com.zhiren.wuljg.utils;

import android.widget.ImageView;

import com.zhiren.wuljg.R;

public class ChangeViewUtil {
	/**
	 * 根据拍照状态来改变ImageVIew 的src
	 * 
	 * @param iv
	 * @param flag
	 */
	public static void changeicon(ImageView iv, int flag) {
		switch (flag) {
		case 0:
			// 未拍照的方位为黑色
			iv.setBackgroundResource(R.drawable.camera);
			break;
		case 1:
			// 已拍照的方位为蓝色
			iv.setBackgroundResource(R.drawable.camera1);
			break;
		case 2:
			// 待拍照的方位为红色
			iv.setBackgroundResource(R.drawable.camera2);
			break;
		case 3:
			iv.setBackgroundResource(R.drawable.camera3);
			break;

		default:
			break;
		}
	}
}
