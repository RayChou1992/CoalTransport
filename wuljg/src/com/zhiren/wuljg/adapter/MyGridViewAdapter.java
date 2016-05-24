package com.zhiren.wuljg.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhiren.wuljg.R;
import com.zhiren.wuljg.utils.ImageLoaderUtils;
import com.zhiren.wuljg.utils.LogUtils;
import com.zhiren.wuljg.view.DialogLoading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MyGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private String flag;
	private final LayoutInflater mInflater;
	private ArrayList<HashMap<String, Object>> imgSrcList;

	public MyGridViewAdapter(String position,
			ArrayList<HashMap<String, Object>> imgSrcList, Context context) {
		super();
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.imgSrcList = imgSrcList;

		this.flag = position;
	}

	@Override
	public int getCount() {
		return imgSrcList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ImageItemHolder holder = null;
		if (view == null) {
			view = mInflater.inflate(R.layout.item_showpic_gridview, null);
			holder = new ImageItemHolder();
			view.setTag(holder);
		} else {
			holder = (ImageItemHolder) view.getTag();
		}
		holder.imgItem = (ImageView) view
				.findViewById(R.id.iv_item_showpic_gridview);

		if (flag != null) {
			// for (int i = 0; i < imgSrcList.size(); i++) {

			// if (flag.equals(imgSrcList.get(i).get("position"))) {
			// LogUtils.e(imgSrcList.size()+"////"+"i="+i+",flag="+flag+",position="+imgSrcList.get(i).get("position"));
			//

			// ImageLoaderUtils.disPlayImage(
			// imgSrcList.get(i).get("src").toString(),
			// holder.imgItem);
			ImageLoaderUtils
					.disPlayImage(imgSrcList.get(position).get("src")
							.toString(), holder.imgItem, ImageLoaderUtils
							.setImageOptiaons(R.drawable.top_02, false));

			LogUtils.e(imgSrcList.get(position).get("position").toString());

		}

		// }

		return view;
	}

	static class ImageItemHolder {
		private ImageView imgItem;

	}

}
