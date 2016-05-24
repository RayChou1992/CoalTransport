package com.zhiren.wuljg.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class TuiChuDialogUtil {
	public static Context context;

	public TuiChuDialogUtil(Context context) {
		super();
		this.context = context;
	}

	public void showDialog() {
		Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("确认退出吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				Activity activity = (Activity) context;
				activity.finish();

			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();

			}
		});
		
		builder.create().show();
	}
}