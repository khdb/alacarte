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




import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

/**
 * An {@link Activity} which handles a broadcast of a new tag that the device
 * just discovered.
 */
public class HomeActivity extends AbstractActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ListView listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(new SpotLightAdapter(this, new String[] { "data1",
                "data2" }));	
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	};

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null && Intent.EXTRA_UID.equals(intent.getAction())) {
			setIntent(null);
		}

	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void processNfcID() {
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
			Intent myIntent = new Intent(this, ViewMenuActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("nfcid", nfcid);
			myIntent.setAction(Intent.ACTION_VIEW);
			myIntent.putExtra("MyPackage", bundle);
			startActivity(myIntent);
		}
	}
}
