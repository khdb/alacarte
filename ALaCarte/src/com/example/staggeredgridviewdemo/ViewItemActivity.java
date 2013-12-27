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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.staggeredgridviewdemo.loader.ImageLoader;
import com.example.staggeredgridviewdemo.views.ScaleImageView;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.khoahuy.model.Item;
import com.khoahuy.model.Shop;
import com.khoahuy.model.Spotlight;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
public class ViewItemActivity extends FacebookActivity {

	private Button likeButton;
	private Button shareButton;
	private Button commentButton;

	private Item item;

	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_item);
		InputStream in = this.getResources().openRawResource(R.raw.item1);
		String jsonString = readTextFile(in);
		item = Item.parseFromJson(jsonString);

		ImageLoader mLoader = new ImageLoader(this);
		ImageView mainImageView = (ScaleImageView) this
				.findViewById(R.id.item_main_image);
		TextView nameTextView = (TextView) this.findViewById(R.id.item_name);
		TextView priceTextView = (TextView) this.findViewById(R.id.item_price);

		likeButton = (Button) this.findViewById(R.id.item_like_button);
		shareButton = (Button) this.findViewById(R.id.item_share_button);
		commentButton = (Button) this.findViewById(R.id.item_comment_button);

		likeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				likeItem();
			}
		});

		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareItem();
			}
		});

		nameTextView.setText(item.getName());
		priceTextView.setText(item.getPrice() + " VNĐ");
		mLoader.DisplayImage(item.getImagePath(), mainImageView);

		LinearLayout myGallery = (LinearLayout) findViewById(R.id.mygallery);

		List<String> urls = item.getOtherImage();
		for (String url : urls) {
			myGallery.addView(insertPhoto(url, mLoader));
		}
		
		if (savedInstanceState != null) {
			pendingPublishReauthorization = savedInstanceState.getBoolean(
					PENDING_PUBLISH_KEY, false);
		}

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
		super.onSaveInstanceState(outState);

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

	private void likeItem() {
		Session session = Session.getActiveSession();

		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			Request.Callback callback = new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					// TODO Auto-generated method stub

					FacebookRequestError error = response.getError();
					if (error != null) {
						displayToast(error.getErrorMessage());
					} else {
						if (response.getGraphObject() == null) {
							displayToast("Error when posting");
							return;
						}
						JSONObject graphResponse = response.getGraphObject()
								.getInnerJSONObject();
						String postId = null;
						try {
							postId = graphResponse.getString("id");
						} catch (JSONException e) {
							Log.i("Huy", "JSON error " + e.getMessage());
						}
						displayToast(postId.toString());
					}
				}
			};

			Bundle postParams = new Bundle();
			postParams.putString("object", item.getImagePath());

			Request request = new Request(session, "me/og.likes", postParams,
					HttpMethod.POST, callback);
			Log.d("Lavoro", "request" + request.toString());

			// Request request = new Request(session, postId+"/comments",
			// postParams,
			// HttpMethod.POST, callback);
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}
	}

	private void shareItem() {
		Session session = Session.getActiveSession();
		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			Bundle postParams = new Bundle();
			postParams.putString("name", item.getName());
			postParams.putString("caption", item.getName());
			postParams.putString("description", item.getDescription());
			postParams.putString("link", item.getImagePath());
			postParams.putString("picture", item.getImagePath());

			Request.Callback callback = new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					// TODO Auto-generated method stub

					FacebookRequestError error = response.getError();
					if (error != null) {
						displayToast(error.getErrorMessage());
					} else {
						if (response.getGraphObject() == null) {
							displayToast("Error when posting");
							return;
						}
						JSONObject graphResponse = response.getGraphObject()
								.getInnerJSONObject();
						String postId = null;
						try {
							postId = graphResponse.getString("id");
						} catch (JSONException e) {
							Log.i("Huy", "JSON error " + e.getMessage());
						}
						displayToast(postId.toString());
					}
				}
			};

			Request request = new Request(session, "me/feed", postParams,
					HttpMethod.POST, callback);
			// Log.d("Huy",request.getRestMethod());
			Log.d("Huy", request.getGraphPath());
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}
	}

}
