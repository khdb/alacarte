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
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.khoahuy.model.Spotlight;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * An {@link Activity} which handles a broadcast of a new tag that the device
 * just discovered.
 */
public class HomeActivity extends AbstractActivity {

	private static String TAG = "Huy";
	private UiLifecycleHelper uiHelper;

	private Button loginButton;
	private Button logoutButton;
	private Dialog progressDialog;

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
			// loginButton.setVisibility(Button.INVISIBLE);
			// logoutButton.setVisibility(Button.VISIBLE);

		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
			// loginButton.setVisibility(Button.VISIBLE);
			// logoutButton.setVisibility(Button.INVISIBLE);
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
		ParseAnalytics.trackAppOpened(getIntent());
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		loginButton = (Button) findViewById(R.id.loginButton);
		logoutButton = (Button) findViewById(R.id.logoutButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});

		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLogoutButtonClicked();
			}
		});

		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user info activity
			loginButton.setVisibility(Button.INVISIBLE);
			logoutButton.setVisibility(Button.VISIBLE);
			getFacebookIdInBackground();
		}
		// nfcid = "04753f52bc2b80";
		// processNfcID(false);
	}

	private void onLogoutButtonClicked() {
		Log.d("Huy", "Logout clicked");
		// Log the user out
		ParseFacebookUtils.getSession().closeAndClearTokenInformation();
		ParseUser.logOut();
		loginButton.setVisibility(Button.VISIBLE);
		logoutButton.setVisibility(Button.INVISIBLE);
		// Go to the login view
		// startLoginActivity();
	}

	private void onLoginButtonClicked() {
		HomeActivity.this.progressDialog = ProgressDialog.show(
				HomeActivity.this, "", "Logging in...", true);
		List<String> permissions = Arrays.asList("basic_info", "user_about_me",
				"user_relationships", "user_birthday", "user_location");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				HomeActivity.this.progressDialog.dismiss();
				if (user == null) {
					Log.d(MainApplication.TAG,
							"Uh oh. The user cancelled the Facebook login.");
					return;
				} else if (user.isNew()) {
					Log.d(MainApplication.TAG,
							"User signed up and logged in through Facebook!");
					// showUserDetailsActivity();
				} else {
					Log.d(MainApplication.TAG,
							"User logged in through Facebook!");
					// showUserDetailsActivity();
				}
				loginButton.setVisibility(Button.INVISIBLE);
				logoutButton.setVisibility(Button.VISIBLE);
				getFacebookIdInBackground();
			}
		});
	}

	private void getFacebookIdInBackground() {

		Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						// TODO Auto-generated method stub
						if (user != null) {
							ParseUser.getCurrentUser()
									.put("fbId", user.getId());
							ParseUser.getCurrentUser().saveInBackground();
							ParseInstallation inst = ParseInstallation.getCurrentInstallation();
							inst.put("user", ParseUser.getCurrentUser());
							inst.saveInBackground();
							Log.d("Huy", "Save fbID to PARSE: " + user.getId());
						}

					}
				}).executeAsync();

	}


	private void displayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
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
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
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
