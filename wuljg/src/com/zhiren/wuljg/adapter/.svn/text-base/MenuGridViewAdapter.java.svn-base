package com.zhiren.wuljg.adapter;

import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiren.wuljg.R;
import com.zhiren.wuljg.entities.MenuList;
import com.zhiren.wuljg.utils.ImageLoaderUtils;

/**
 * 子菜单 适配器
 *
 */
public class MenuGridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<String> list; // 原数据
	private Activity context;
	private Handler handler;

	public MenuGridViewAdapter(Activity context, List<String> list,
			Handler handler) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_menu_gridview, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.iv_image = (ImageView) convertView
					.findViewById(R.id.iv_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String name = list.get(position);
		
		holder.tv_name.setText(name);
		ImageLoaderUtils.disPlayImage("", holder.iv_image,ImageLoaderUtils.setImageOptiaons(R.drawable.ic_launcher, false));
 		return convertView;
	}

	public static class ViewHolder {
		TextView tv_name;
		ImageView iv_image;

	}
}
