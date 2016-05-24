package com.zhiren.wuljg.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.zhiren.wuljg.R;

/**
 * 公共的加载dialog
 * @description
 */
public class DialogLoading extends Dialog {

	private TextView tv;
	private String content = "";
	private onDialogClickBackListener listener;

	public DialogLoading(Context context) {
		super(context, R.style.customDialogStyle);
	}

	public DialogLoading(Context context, String content) {
		super(context, R.style.customDialogStyle);
		this.content = content;
	}

	private DialogLoading(Context context, int theme) {
		super(context, theme);
	}

	public void setOnDialogClickBackListener(onDialogClickBackListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);

		tv = (TextView) this.findViewById(R.id.tv);
		if (content.equals("")) {
			tv.setText("请稍候...");
		
		} else {
			tv.setText(content);
		}
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			event.startTracking();
			if (listener != null) {
				listener.onDialogClickBack();
			}
			return true;
		}

		return false;
	}

	public interface onDialogClickBackListener {
		public void onDialogClickBack();
	}
}
