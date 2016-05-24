package com.zhiren.wuljg.view;

import com.zhiren.wuljg.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class TakePicTipsDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Button bt_confirm, bt_cancel, bt_play;
	private TextView tv_title, tv_content;
	private String title, content, bt1, bt2, bt3;

	public TakePicTipsDialog(Context context, String title, String content,
			String bt1, String bt2, String bt3) {
		super(context);
		this.title = title;
		this.content = content;
		this.bt1 = bt1;
		this.bt2 = bt2;
		this.bt3 = bt3;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_takepictips);
		bt_confirm = (Button) findViewById(R.id.bt_dialog_confirm);
		bt_cancel = (Button) findViewById(R.id.bt_dialog_cancel);
		bt_play = (Button) findViewById(R.id.bt_dialog_play);

		tv_content = (TextView) findViewById(R.id.tv_dialog_content);
		tv_title = (TextView) findViewById(R.id.tv_dialog_title);
		tv_title.setText(title);
		tv_content.setText(content);
		bt_confirm.setText(bt1);
		bt_cancel.setText(bt2);
		bt_play.setText(bt3);

		bt_confirm.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_dialog_confirm:
			
			break;
		case R.id.bt_dialog_cancel:
			this.dismiss();
			break;
		case R.id.bt_dialog_play:

			break;

		default:
			break;
		}
	}
}
