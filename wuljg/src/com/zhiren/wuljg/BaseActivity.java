package com.zhiren.wuljg;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


/**
 * Activity 基类
 */
public abstract class BaseActivity extends Activity {
	
	public ZhiRen_Application appContext;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext=(ZhiRen_Application) getApplicationContext();
	}
	//activity 切换动画
	public void intentJump(Activity context, Class<?> activity) {
		Intent intent = new Intent(context, activity);
		startActivity(intent);
//		context.overridePendingTransition(R.anim.tran_shownext_in, R.anim.tran_shownext_out);
	}
	public void intentJump(Activity context, Class<?> activity, Bundle bundle) {
		Intent intent = new Intent(context, activity);
		intent.putExtras(bundle);
		startActivity(intent);
//		context.overridePendingTransition(R.anim.tran_shownext_in, R.anim.tran_shownext_out);
	}
	public void intentJumpForResult(Activity context, Class<?> activity,int requestCode) {
		Intent intent = new Intent(context, activity);
		context.startActivityForResult(intent, requestCode);
//		context.overridePendingTransition(R.anim.tran_shownext_in, R.anim.tran_shownext_out);
	}
	public void intentJumpForResult(Activity context, Class<?> activity,Bundle bundle,int requestCode) {
		Intent intent = new Intent(context, activity);
		intent.putExtras(bundle);
		context.startActivityForResult(intent, requestCode);
//		context.overridePendingTransition(R.anim.tran_shownext_in, R.anim.tran_shownext_out);
	}
	public void finishActivity(Activity context) {
		context.finish();
//		context.overridePendingTransition(R.anim.tran_showlast_in, R.anim.tran_showlast_out);
	}
	/**
	 * 
	 * @param handler
	 * @param isCancel 是否有取消
	 * @param isBack   返回键是否可用
	 * @param isSend   是否发送所选的内容
	 * @param title    选择内容、、、
	 */
	
	/*
	public void showDialog(final Handler handler,final int msgWhat,boolean isCancel,boolean isBack,final String... title){
		final Message msg=handler.obtainMessage();
		msg.what=msgWhat;
		final Dialog dialog=new Dialog(this, R.style.customDialogStyle);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom, null);
		Button cancel=(Button) view.findViewById(R.id.dialog_cancel);
		Button top=(Button) view.findViewById(R.id.dialog_top);
		Button middle=(Button) view.findViewById(R.id.dialog_middle);
		Button bottom=(Button) view.findViewById(R.id.dialog_bottom);
		if(title==null||title.length==0){
		}else 
		if(title.length==2){
			middle.setVisibility(View.GONE);
			top.setText(title[0]);
			bottom.setText(title[1]);
		}else if(title.length>2){
			middle.setVisibility(View.VISIBLE);
			top.setText(title[0]);
			middle.setText(title[1]);
			bottom.setText(title[2]);
		}
		top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				msg.obj=((Button)v).getText().toString();
				handler.sendMessage(msg);
			}
		});
		middle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				msg.obj=((Button)v).getText().toString();
				handler.sendMessage(msg);
			}
		});
		bottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				msg.obj=((Button)v).getText().toString();
				handler.sendMessage(msg);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.show();
		
		dialog.getWindow().setWindowAnimations(R.style.commonAnimDialogStyle);
		dialog.getWindow().setLayout(appContext.getDisplayMetrics().widthPixels, LayoutParams.MATCH_PARENT);
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					return true;
				}
				return false;
			}
		});
	}
	
	
	
	public void showDatePickerDialog(final Handler handler,final int msgWhat,int ...initdate){
		final Dialog dialog=new Dialog(this, R.style.customDialogStyle);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_datepicker, null);
		DatePicker datePicker=(DatePicker) view.findViewById(R.id.datePicker);
		final TextView select=(TextView) view.findViewById(R.id.tv_select_time);
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		int year=1900,month=0,day=0;
		if(initdate==null||initdate.length!=3){
			Calendar c = Calendar.getInstance();
			year=c.get(Calendar.YEAR);
			month=c.get(Calendar.MONTH);
			day =c.get(Calendar.DAY_OF_MONTH);
			select.setText(year+"-"+month+"-"+day);
		}else{
			year=initdate[0];
			month=initdate[1]-1;
			day=initdate[2];
			select.setText(initdate[0]+"-"+initdate[1]+"-"+initdate[2]);
		}
		datePicker.init(year, month, day, new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				select.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
			}
		});
		view.findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.tv_sure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Message msg=handler.obtainMessage();
				msg.what=msgWhat;
				msg.obj=select.getText().toString().trim();
				handler.sendMessage(msg);
						
			}
		});
		
		dialog.show();
		dialog.getWindow().setWindowAnimations(R.style.commonAnimDialogStyle);
		dialog.getWindow().setLayout(appContext.getDisplayMetrics().widthPixels, LayoutParams.MATCH_PARENT);
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					return true;
				}
				return false;
			}
		});
	}
	*/
	
} 
