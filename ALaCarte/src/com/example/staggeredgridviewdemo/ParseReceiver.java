package com.example.staggeredgridviewdemo;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ParseReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String action = intent.getAction();
			String channel = intent.getExtras().getString("com.parse.Channel");
			JSONObject json = new JSONObject(intent.getExtras().getString(
					"com.parse.Data"));
			String nfcid = json.getString("nfcid");
			Log.d("Huy", "got action " + action + " on channel " + channel
					+ " with: nfcid = " + nfcid);

			Intent intent2open = new Intent(context, HomeActivity.class);
			intent2open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent2open.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent2open.setAction(AbstractActivity.CUSTOM_ACTION);
			Bundle b = new Bundle();
			b.putString("nfcid", nfcid);
			// intent.putExtra("MyPackage", b);
			intent.putExtra("nfcid", nfcid);
			context.startActivity(intent2open);

		} catch (JSONException e) {
			Log.d("Huy", "JSONException: " + e.getMessage());
		}
	}


}
