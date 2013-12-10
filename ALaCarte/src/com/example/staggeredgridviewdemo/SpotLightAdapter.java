package com.example.staggeredgridviewdemo;

import java.io.InputStream;

import com.example.staggeredgridviewdemo.loader.ImageLoader;
import com.khoahuy.model.Item;
import com.khoahuy.model.Spotlight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
		Spotlight sl = data[position];
		Item item1 = sl.getArray()[0];
		Item item2 = sl.getArray()[1];
		Item item3 = sl.getArray()[2];
		
		mLoader.DisplayImage(item1.getImagePath(), img1);
		mLoader.DisplayImage(item2.getImagePath(), img2);
		mLoader.DisplayImage(item3.getImagePath(), img3);
		
		//new DownloadImageTask(img1).execute(item1.getImagePath());
		//new DownloadImageTask(img2).execute(item2.getImagePath());
		//new DownloadImageTask(img3).execute(item3.getImagePath());
		text.setText(data[position].getTitle());
		// img1.setImageDrawable(this.ge.getDrawable(R.drawable.myimage));
		return vi;
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