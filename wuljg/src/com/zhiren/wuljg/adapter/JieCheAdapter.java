package com.zhiren.wuljg.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhiren.wuljg.R;
import com.zhiren.wuljg.activity.ChuanYunChu_FaCheAndPiDaiActivity;
import com.zhiren.wuljg.activity.ChuanYunChu_JieCheActivity;
import com.zhiren.wuljg.activity.TakePicAndUpload_Activity;
import com.zhiren.wuljg.entities.JieCheInfo.JCInfo;
import com.zhiren.wuljg.entities.MenuList;
import com.zhiren.wuljg.utils.Constant;
import com.zhiren.wuljg.utils.ImageLoaderUtils;
import com.zhiren.wuljg.utils.TimeUtil;
import com.zhiren.wuljg.utils.TimeUtils;
import com.zhiren.wuljg.utils.ToastUtil;

/**
 * 接车
 * 
 */
public class JieCheAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<JCInfo> list; // 原数据
	private Activity context;
	private Handler handler;

	public JieCheAdapter(Activity context, List<JCInfo> list, Handler handler) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView( final int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_jieche, null);
			holder = new ViewHolder();
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.iv_camera = (ImageView) convertView.findViewById(R.id.iv_camera);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final JCInfo jcInfo = list.get(position);
		 
		String time = TimeUtils.getRemainSecString(Long.parseLong(jcInfo.zhuangcsj));
		if (time.contains("&")) {
			String[] split = time.split("&");
			holder.tv_time.setText(split[0]+"\n"+split[1]);
		}
		ImageLoaderUtils.disPlayImage(Constant.BASE_URL_PIC+jcInfo.url, holder.iv_image, ImageLoaderUtils
				.setImageOptiaons(R.drawable.ic_launcher, false));
		holder.iv_camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = handler.obtainMessage();
				msg.what = ChuanYunChu_JieCheActivity.PAIZHAO;
				msg.obj = jcInfo ;
				handler.sendMessage(msg);
			}
		});
		return convertView;
	}

	
	public static class ViewHolder {
		TextView tv_time;
		ImageView iv_image;
		ImageView iv_camera;

	}
}
