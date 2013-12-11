package com.example.staggeredgridviewdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.staggeredgridviewdemo.loader.ImageLoader;
import com.example.staggeredgridviewdemo.views.ScaleImageView;
import com.khoahuy.model.Item;

public class StaggeredAdapter extends ArrayAdapter<Item> {

	private ImageLoader mLoader;

	public StaggeredAdapter(Context context, int textViewResourceId,
			Item[] objects) {
		super(context, textViewResourceId, objects);
		mLoader = new ImageLoader(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			convertView = layoutInflator.inflate(R.layout.row_staggered_demo,
					null);
			holder = new ViewHolder();
			holder.nameTextView = (TextView) convertView.findViewById(R.id.name);
			holder.imageView = (ScaleImageView) convertView .findViewById(R.id.imageView1);
			holder.priceTextView = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();
		holder.nameTextView.setText(getItem(position).getName());
		holder.priceTextView.setText(getItem(position).getPrice() + " VNĐ");
		mLoader.DisplayImage(getItem(position).getImagePath(), holder.imageView);
		return convertView;
	}

	static class ViewHolder {
		TextView nameTextView;
		TextView priceTextView;
		ScaleImageView imageView;
	}
}
