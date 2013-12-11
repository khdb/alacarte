/*
 * Copyright (C) 2010 The Android Open Source Project
 * Copyright (C) 2011 Adam Nybäck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.staggeredgridviewdemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.example.staggeredgridviewdemo.loader.ImageLoader;
import com.example.staggeredgridviewdemo.views.ScaleImageView;
import com.khoahuy.model.Item;
import com.khoahuy.model.Shop;
import com.khoahuy.model.Spotlight;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * An {@link Activity} which handles a broadcast of a new tag that the device
 * just discovered.
 */
public class ViewItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_item);
		InputStream in = this.getResources().openRawResource(R.raw.item1);
		String jsonString = readTextFile(in);
		Item item = Item.parseFromJson(jsonString);

		ImageLoader mLoader = new ImageLoader(this);
		ImageView mainImageView = (ScaleImageView) this
				.findViewById(R.id.item_main_image);
		TextView nameTextView = (TextView) this.findViewById(R.id.item_name);
		TextView priceTextView = (TextView) this.findViewById(R.id.item_price);

		nameTextView.setText(item.getName());
		priceTextView.setText(item.getPrice() + " VNĐ");
		mLoader.DisplayImage(item.getImagePath(), mainImageView);

		LinearLayout myGallery = (LinearLayout) findViewById(R.id.mygallery);


		List<String> urls = item.getOtherImage();
		for (String url : urls) {
			myGallery.addView(insertPhoto(url, mLoader));
		}

	}

	public View insertPhoto(String url, ImageLoader mLoader) {

		LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setLayoutParams(new LayoutParams(250, 250));
		layout.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setLayoutParams(new LayoutParams(220, 220));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// imageView.setImageBitmap(bm);
		mLoader.DisplayImage(url, imageView);
		layout.addView(imageView);
		return layout;
	}

	public String readTextFile(InputStream inputStream) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		byte buf[] = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toString();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

}
