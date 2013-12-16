package com.example.staggeredgridviewdemo;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import android.app.Application;
import android.util.Log;

public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("Huy", "This is running ??");
		Parse.initialize(this, "zy96IOxBgOiGHeSKiCvWfzNOWpisixel8aH4Q7xH",
				"EbLR5dNpSAkY0YcZx618XZwk2J4Xk3vbVEN023rp");
		PushService.setDefaultPushCallback(this, HomeActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		Log.d("Huy", "This is running 2??");

	}
}
