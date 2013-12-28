package com.example.staggeredgridviewdemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.staggeredgridviewdemo.loader.ImageLoader;
import com.example.staggeredgridviewdemo.views.ScaleImageView;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.khoahuy.model.Item;
import com.khoahuy.model.Shop;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
public class ViewMenuActivity extends FacebookActivity {

	/**
	 * Images are taken by Romain Guy ! He's a great photographer as well as a
	 * great programmer. http://www.flickr.com/photos/romainguy
	 */

	private Button likeButton;
	private Button shareButton;
	private Button commentButton;
	private Button checkinButton;

	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;

	private String nfcid;
	private Shop shop;
	private Item[] arrayItem;

	@Override
	protected void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		// TODO Auto-generated method stub
		super.onSessionStateChange(session, state, exception);
		if (state.isOpened()) {
			likeButton.setVisibility(View.VISIBLE);
			shareButton.setVisibility(View.VISIBLE);
			commentButton.setVisibility(View.VISIBLE);
			checkinButton.setVisibility(View.VISIBLE);
		} else if (state.isClosed()) {
			likeButton.setVisibility(View.INVISIBLE);
			shareButton.setVisibility(View.INVISIBLE);
			commentButton.setVisibility(View.INVISIBLE);
			checkinButton.setVisibility(View.INVISIBLE);
		}
	}

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

		// Facebook control
		likeButton = (Button) this.findViewById(R.id.menu_like_button);
		shareButton = (Button) this.findViewById(R.id.menu_share_button);
		commentButton = (Button) this.findViewById(R.id.menu_comment_button);
		checkinButton = (Button) this.findViewById(R.id.menu_checkin_button);

		likeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				likePage();
			}
		});

		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				publishStory();
			}
		});

		checkinButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkInStorty();
			}
		});

		TextView titleTextView = (TextView) this.findViewById(R.id.title);
		TextView contactTextView = (TextView) this.findViewById(R.id.contact);
		ImageView shopImage = (ScaleImageView) this
				.findViewById(R.id.shop_image);

		int margin = getResources().getDimensionPixelSize(R.dimen.margin);

		gridView.setItemMargin(margin); // set the GridView margin

		gridView.setPadding(margin, 0, margin, 0); // have the margin on the
													// sides as well

		Intent callerIntent = getIntent();
		Bundle packageFromCaller = callerIntent.getBundleExtra("MyPackage");
		nfcid = packageFromCaller.getString("nfcid");
		boolean isPush = packageFromCaller.getBoolean("pushNotification");
		shop = getAndDisplayNFCITem();

		if (shop != null) {
			titleTextView.setText(shop.getTitle());
			contactTextView.setText("Địa chỉ: " + shop.getAddress()
					+ " - Phone: " + shop.getPhone());
			ImageLoader mLoader = new ImageLoader(this);
			mLoader.DisplayImage(shop.getImagePath(), shopImage);

			arrayItem = shop.getMenu().toArray(new Item[shop.getMenu().size()]);
			StaggeredAdapter adapter = new StaggeredAdapter(
					ViewMenuActivity.this, R.id.imageView1, arrayItem);
			gridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(StaggeredGridView parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Log.d("Huy", "Item clicked at: " + position + " - Title = "
							+ id);
					gotoDetailItem(position);
				}
			});
		}
		if (ParseFacebookUtils.getSession()!= null)
		{
			if (isPush && ParseFacebookUtils.getSession().isOpened()) {
				//pushNotification(shop.getTitle());
				pushNotification2();
			}
		}
		_getLocation();

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

	private void pushNotification2() {
		String fqlQuery = "SELECT uid,name,pic_square FROM user WHERE uid IN "
				+ "(SELECT uid2 FROM friend WHERE uid1 = me())";
		Bundle params = new Bundle();
		params.putString("q", fqlQuery);
		Session session = Session.getActiveSession();
		Request request = new Request(session, "/fql", params, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						Log.i(MainApplication.TAG,
								"Result: " + response.toString());

						try {
							GraphObject graphObject = response.getGraphObject();
							JSONObject jsonObject = graphObject
									.getInnerJSONObject();
							//Log.d("data", jsonObject.toString(0));
							List<String> friendsList = new ArrayList<String>();
							JSONArray array = jsonObject.getJSONArray("data");
							for (int i = 0; i < array.length(); i++) {
								JSONObject friend = array.getJSONObject(i);
								friendsList.add(friend.getString("uid"));
							}
							
							ParseQuery<ParseUser> friendQuery = ParseUser
									.getQuery();
							friendQuery.whereContainedIn("fbId", friendsList);
							pushNow(shop.getTitle(), friendQuery);
						} catch (JSONException e) {
							e.printStackTrace();
						} 
					}
				});
		Request.executeBatchAsync(request);
	}
	
	private void pushNow(String title, ParseQuery<ParseUser> friendQuery){
		try {
			ParsePush push = new ParsePush();
			
			ParseQuery<ParseInstallation> filterQuery = ParseInstallation.getQuery();
			filterQuery.whereMatchesQuery("user", friendQuery);
			//friendQuery.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
			filterQuery.whereEqualTo("deviceType", "android");
			push.setQuery(filterQuery);

			JSONObject data;
			String alertStr = "Peter đang có mặt tại " + title;
			data = new JSONObject(
					"{\"alert\": \" "
							+ alertStr
							+ "\", \"action\": \"com.example.staggeredgridviewdemo.UPDATE_STATUS\", \"nfcid\": \""
							+ nfcid + "\"}");
			// Create time interval
			long weekInterval = 60 * 60 * 24 * 7; // 1 week
			push.setExpirationTimeInterval(weekInterval);
			// push.setMessage("Peter đang có mặt tại " + shop.getTitle());
			push.setData(data);
			push.sendInBackground();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pushNotification(String title) {
		try {

			ParsePush push = new ParsePush();
			ParseQuery everyone = ParseInstallation.getQuery();
			everyone.whereNotEqualTo("objectId", ParseInstallation
					.getCurrentInstallation().getObjectId());
			everyone.whereEqualTo("deviceType", "android");
			push.setQuery(everyone);

			JSONObject data;
			String alertStr = "Peter đang có mặt tại " + title;
			data = new JSONObject(
					"{\"alert\": \" "
							+ alertStr
								+ "\", \"action\": \"com.example.staggeredgridviewdemo.UPDATE_STATUS\", \"nfcid\": \""
							+ nfcid + "\"}");
			// Create time interval
			long weekInterval = 60 * 60 * 24 * 7; // 1 week
			push.setExpirationTimeInterval(weekInterval);
			// push.setMessage("Peter đang có mặt tại " + shop.getTitle());
			push.setData(data);
			push.sendInBackground();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void gotoDetailItem(int position) {
		if (position < 0 || position >= arrayItem.length) {
			Toast.makeText(this, "Cannot load this item.", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		String itemID = arrayItem[position].getId();
		Intent myIntent = new Intent(this, ViewItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("itemid", itemID);
		myIntent.setAction(Intent.ACTION_VIEW);
		myIntent.putExtra("MyPackage", bundle);
		startActivity(myIntent);
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
			in = this.getResources().openRawResource(R.raw.shop4);
		} else if ("04753f52bc2b80".equals(nfcid)) {
			in = this.getResources().openRawResource(R.raw.shop2);
		} else if ("4ed859ab".equals(nfcid)) {
			in = this.getResources().openRawResource(R.raw.shop3);
		} else if ("6e293ba9".equals(nfcid)) {
			in = this.getResources().openRawResource(R.raw.shop4);
		} else if ("0492ed4abc2b80".equals(nfcid)) {
			in = this.getResources().openRawResource(R.raw.shop5);
		} else if ("045be74abc2b80".equals(nfcid)) {
			in = this.getResources().openRawResource(R.raw.shop6);
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

	// Facebook button:

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	private void likePage() {
		// String pageid = "141148276777";
		try {

			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("fb://profile/141148276777"));
			startActivity(intent);

		} catch (Exception e) {

			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.facebook.com/141148276777")));
		}
	}

	private void checkInStorty() {
		Session session = Session.getActiveSession();
		if (session != null) {
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			Bundle postParams = new Bundle();
			postParams.putString("restaurant",
					"http://samples.ogp.me/440002909390231");
			Request.Callback callback = new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					// TODO Auto-generated method stub
					if (response.getGraphObject() == null) {
						displayToast("Error when posting");
						Log.d("Huy", response.toString());
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
					FacebookRequestError error = response.getError();
					if (error != null) {
						displayToast(error.getErrorMessage());
					} else {
						displayToast(postId.toString());
					}
				}
			};

			Request request = new Request(session, "me/com_khoahuy:eat_a_meal",
					postParams, HttpMethod.POST, callback);
			// Log.d("Huy",request.getRestMethod());
			Log.d("Huy", request.getGraphPath());
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}
	}

	private void publishStory() {
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
			postParams.putString("name", shop.getTitle());
			postParams.putString("caption", shop.getAddress());
			postParams.putString("description", shop.getPhone());
			postParams.putString("link", shop.getImagePath());
			postParams.putString("picture", shop.getImagePath());

			Request.Callback callback = new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					// TODO Auto-generated method stub
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
					FacebookRequestError error = response.getError();
					if (error != null) {
						displayToast(error.getErrorMessage());
					} else {
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
