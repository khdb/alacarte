package com.example.staggeredgridviewdemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.khoahuy.model.Item;
import com.khoahuy.model.Shop;
import com.origamilabs.library.views.StaggeredGridView;

/**
 * 
 * This will not work so great since the heights of the imageViews are
 * calculated on the iamgeLoader callback ruining the offsets. To fix this try
 * to get the (intrinsic) image width and height and set the views height
 * manually. I will look into a fix once I find extra time.
 * 
 * @author Maurycy Wojtowicz
 * 
 */
public class ViewMenuActivity extends Activity {

	/**
	 * Images are taken by Romain Guy ! He's a great photographer as well as a
	 * great programmer. http://www.flickr.com/photos/romainguy
	 */

	private String nfcid;

	/**
	 * This will not work so great since the heights of the imageViews are
	 * calculated on the iamgeLoader callback ruining the offsets. To fix this
	 * try to get the (intrinsic) image width and height and set the views
	 * height manually. I will look into a fix once I find extra time.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_menu);

		StaggeredGridView gridView = (StaggeredGridView) this
				.findViewById(R.id.staggeredGridView1);

		TextView titleTextView = (TextView) this.findViewById(R.id.title);
		TextView contactTextView = (TextView) this.findViewById(R.id.contact);

		int margin = getResources().getDimensionPixelSize(R.dimen.margin);

		gridView.setItemMargin(margin); // set the GridView margin

		gridView.setPadding(margin, 0, margin, 0); // have the margin on the
													// sides as well

		Intent callerIntent = getIntent();
		Bundle packageFromCaller = callerIntent.getBundleExtra("MyPackage");
		nfcid = packageFromCaller.getString("nfcid");
		Shop shop = getAndDisplayNFCITem();

		if (shop != null) {
			titleTextView.setText(shop.getTitle());
			contactTextView.setText(shop.getAddress() + " - Phone: "
					+ shop.getPhone());

			Item[] array = shop.getMenu().toArray(
					new Item[shop.getMenu().size()]);
			StaggeredAdapter adapter = new StaggeredAdapter(
					ViewMenuActivity.this, R.id.imageView1, array);

			gridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

		_getLocation();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void displayLocation(Location location) {
		Toast.makeText(
				this,
				"Location: Latitude" + location.getLatitude()
						+ " - Longitude: " + location.getLongitude(),
				Toast.LENGTH_SHORT).show();
	}

	private void _getLocation() {
		// Get the location manager
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String bestProvider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(bestProvider);
		LocationListener loc_listener = new LocationListener() {

			public void onLocationChanged(Location l) {
			}

			public void onProviderEnabled(String p) {
			}

			public void onProviderDisabled(String p) {
			}

			public void onStatusChanged(String p, int status, Bundle extras) {
			}
		};
		locationManager
				.requestLocationUpdates(bestProvider, 0, 0, loc_listener);
		location = locationManager.getLastKnownLocation(bestProvider);
		double lat;
		double lon;
		try {
			lat = location.getLatitude();
			lon = location.getLongitude();
		} catch (NullPointerException e) {
			lat = -1.0;
			lon = -1.0;
		}
		Toast.makeText(this,
				"Location: Latitude" + lat + " - Longitude: " + lon,
				Toast.LENGTH_LONG).show();
	}

	private Shop getAndDisplayNFCITem() {
		InputStream in;
		nfcid = nfcid.toLowerCase();
		if ("946e33dd".equals(nfcid)) {
			in = this.getResources().openRawResource(R.raw.shop1);
		} else if ("04753f52bc2b80".equals(nfcid)) {
			in = this.getResources().openRawResource(R.raw.shop2);
		} else if ("4ed859ab".equals(nfcid)) {
			in = this.getResources().openRawResource(R.raw.shop3);
		} else if ("6e293ba9".equals(nfcid)) {
			in = this.getResources().openRawResource(R.raw.shop4);
		} else {
			Toast.makeText(this, "This location not support.",
					Toast.LENGTH_SHORT).show();
			return null;
		}

		String jsonString = readTextFile(in);
		Shop shop = Shop.parseFromJson(jsonString);
		return shop;

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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
