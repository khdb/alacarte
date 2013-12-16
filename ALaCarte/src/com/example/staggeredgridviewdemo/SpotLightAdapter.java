package com.example.staggeredgridviewdemo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.staggeredgridviewdemo.loader.ImageLoader;
import com.khoahuy.model.Item;
import com.khoahuy.model.Spotlight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpotLightAdapter extends BaseAdapter {

	Context context;
	Spotlight[] data;
	ImageLoader mLoader;

	private static LayoutInflater inflater = null;

	public SpotLightAdapter(Context context, Spotlight[] data) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data = data;
		mLoader = new ImageLoader(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		if (vi == null)
			vi = inflater.inflate(R.layout.row_spotlight, null);
		TextView text = (TextView) vi.findViewById(R.id.spot_title);
		ImageView img1 = (ImageView) vi.findViewById(R.id.spotlight_image1);
		ImageView img2 = (ImageView) vi.findViewById(R.id.spotlight_image2);
		ImageView img3 = (ImageView) vi.findViewById(R.id.spotlight_image3);

		TextView text1 = (TextView) vi.findViewById(R.id.spotlight_name1);
		TextView text2 = (TextView) vi.findViewById(R.id.spotlight_name2);
		TextView text3 = (TextView) vi.findViewById(R.id.spotlight_name3);
		Spotlight sl = data[position];
		Item item1 = sl.getArray()[0];
		Item item2 = sl.getArray()[1];
		Item item3 = sl.getArray()[2];

		mLoader.DisplayImage(item1.getImagePath(), img1);
		mLoader.DisplayImage(item2.getImagePath(), img2);
		mLoader.DisplayImage(item3.getImagePath(), img3);

		// new DownloadImageTask(img1).execute(item1.getImagePath());
		// new DownloadImageTask(img2).execute(item2.getImagePath());
		// new DownloadImageTask(img3).execute(item3.getImagePath());
		text.setText(sl.getTitle());
		text1.setText(sl.getArray()[0].getName());
		text2.setText(sl.getArray()[1].getName());
		text3.setText(sl.getArray()[2].getName());

		if (sl.getType() == Spotlight.TYPE.FRIEND && position == 2) {
			Log.d("Huy", "Type = " + sl.getType());

			LinearLayout linearLayout1 = (LinearLayout) vi
					.findViewById(R.id.item_linear1);
			loadFriendAvatar(linearLayout1);

		}
		// img1.setImageDrawable(this.ge.getDrawable(R.drawable.myimage));
		return vi;
	}

	public void loadFriendAvatar(LinearLayout linearLayout) {
		LinearLayout avatarLayout = new LinearLayout(context);

		avatarLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		avatarLayout.setOrientation(LinearLayout.HORIZONTAL);
		String[] urls = randomAvatar();
		for (String url : urls) {
			Log.d("Huy", "URL  = " + url);
			avatarLayout.addView(insertPhoto(url, mLoader));
		}
		linearLayout.addView(avatarLayout);
	}

	public int random(int begin, int end) {
		Random rand = new Random();
		return rand.nextInt(end) + begin;

	}

	public String[] randomAvatar() {
		List<String> sampleAvatar = new ArrayList<String>();
		sampleAvatar
				.add("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-prn2/c44.44.552.552/s200x200/1069420_520276641377080_1415996119_n.jpg");
		sampleAvatar
				.add("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-frc3/c0.1.176.176/4635_111415132183_2524040_a.jpg");
		sampleAvatar
				.add("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-prn2/c1.0.350.350/s200x200/1476278_601384163260240_138246984_n.jpg");
		sampleAvatar
				.add("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash2/c0.0.860.860/s200x200/1379637_699218990105637_1205373647_n.jpg");
		sampleAvatar
				.add("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash2/c1.0.632.632/s200x200/1185591_586615224737091_1834362218_n.jpg");
		String[] urls = new String[random(1, 3)];
		for (int i = 0; i < urls.length; i++) {
			int index = (new Random()).nextInt(sampleAvatar.size());
			urls[i] = sampleAvatar.get(index);
			sampleAvatar.remove(index);
		}
		return urls;
	}

	public View insertPhoto(String url, ImageLoader mLoader) {

		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new LayoutParams(75, 75));
		layout.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(context);
		imageView.setLayoutParams(new LayoutParams(70, 70));
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		// imageView.setImageBitmap(bm);
		mLoader.DisplayImage(url, imageView);
		layout.addView(imageView);
		return layout;
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Huy", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
}