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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.khoahuy.model.Spotlight;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseAnalytics;

/**
 * An {@link Activity} which handles a broadcast of a new tag that the device
 * just discovered.
 */
public class HomeActivity extends AbstractActivity {

	private static String TAG = "Huy";
	private UiLifecycleHelper uiHelper;
	private GraphUser user;

	

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");

		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		LoginButton authButton = (LoginButton) this
				.findViewById(R.id.authButton);
		// authButton.set(this);
		authButton.setReadPermissions(Arrays
				.asList("user_likes", "user_status"));
		authButton
				.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
					@Override
					public void onUserInfoFetched(GraphUser user) {
						HomeActivity.this.user = user;
						if (user != null)
							displayToast("Facebook login: Welcome "
									+ user.getFirstName() + " "
									+ user.getLastName());
						// It's possible that we were waiting for this.user to
						// be populated in order to post a
						// status update.
					}
				});


		ParseAnalytics.trackAppOpened(getIntent());
		nfcid = "04753f52bc2b80";
		processNfcID(false);
	}

	private void displayToast(String str) {
		if (user != null) {
			Toast.makeText(this, str, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		/*
		 * Intent intent = getIntent(); if (intent != null &&
		 * Intent.EXTRA_UID.equals(intent.getAction())) { setIntent(null); }
		 */

	}

	@Override
	protected void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
		ListView listview = (ListView) findViewById(R.id.listview);
		Spotlight[] array = { Spotlight.getHotSpotlight(),
				Spotlight.getRecommendSpotlight(),
				Spotlight.getFriendSpotlight(), Spotlight.getCouponSpotlight() };
		listview.setAdapter(new SpotLightAdapter(this, array));
		Intent intent = this.getIntent();
		if (intent != null) {
			if (intent.hasExtra("com.parse.Channel")
					&& intent.hasExtra("com.parse.Data")) {
				try {
					String action = intent.getAction();
					String channel = intent.getExtras().getString(
							"com.parse.Channel");

					JSONObject json = new JSONObject(intent.getExtras()
							.getString("com.parse.Data"));
					nfcid = json.getString("nfcid");
					Log.d("Huy", "got action " + action + " on channel "
							+ channel + " with: nfcid = " + nfcid);
					// processNfcID(false);

					Intent myIntent = new Intent(HomeActivity.this,
							ViewMenuActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("nfcid", nfcid);
					bundle.putBoolean("pushNotification", false);

					myIntent.putExtra("MyPackage", bundle);
					startActivity(myIntent);
					this.setIntent(null);
					// finish();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void processNfcID(boolean isPush) {
		if (("").equals(nfcid) || nfcid == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					HomeActivity.this);
			builder.setTitle("Error");
			builder.setMessage("Dected your tag fail");
			builder.setPositiveButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					});
			builder.show();
		} else {
			// Todo : Process nfcid that just read
			Intent myIntent = new Intent(HomeActivity.this,
					ViewMenuActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("nfcid", nfcid);
			bundle.putBoolean("pushNotification", isPush);
			if (!isPush) {
				// myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
				// Intent.FLAG_ACTIVITY_CLEAR_TASK);
			}
			myIntent.putExtra("MyPackage", bundle);
			startActivity(myIntent);
			// finish();
		}
	}
}
