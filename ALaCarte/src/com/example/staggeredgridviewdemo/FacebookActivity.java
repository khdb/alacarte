package com.example.staggeredgridviewdemo;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public abstract class FacebookActivity extends Activity {

	protected UiLifecycleHelper uiHelper;

	protected void onSessionStateChange(Session session, SessionState state,
			Exception exception) {

	}

	protected void displayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	protected Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
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
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

}
