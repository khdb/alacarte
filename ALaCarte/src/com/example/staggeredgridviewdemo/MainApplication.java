package com.example.staggeredgridviewdemo;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.PushService;

import android.app.Application;

public class MainApplication extends Application {
	
	public static final String TAG = "Huy";

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
		PushService.setDefaultPushCallback(this, HomeActivity.class);
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		ParseInstallation.getCurrentInstallation().saveInBackground();

	}
}
