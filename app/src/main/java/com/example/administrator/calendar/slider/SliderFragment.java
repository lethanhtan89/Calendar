package com.example.administrator.calendar.slider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.calendar.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SliderFragment extends CaldroidFragment {
	private static final int IMAGE_GALLERY = 10000;
	private static final int IMAGE_CAMERA = 10001;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final String BITAMP = "bitmap";
	private Uri uriCameraImage;
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
	TextView txtPicture, txtUpload, txtCompare;
	Bitmap bitmap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_image, container, false);
		previewCapturedImage(view);
		return view;
	}

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		return new SliderAdapter(getActivity(), month, year,
				getCaldroidData(), extraData);
	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	public static File getOutputMediaFile(int type) {
		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		}
		else {
			return null;
		}
		return mediaFile;
	}

	private void previewCapturedImage(View view) {
		try {
			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();
			// downsizing image as it throws OutOfMemory Exception for larger
			// images

			options.inSampleSize = 16;
			//options.inScreenDensity = 1;
			bitmap = BitmapFactory.decodeFile(uriCameraImage.getPath(), options);
			//Drawable drawable = Drawable.createFromPath(uriCameraImage.getPath());

			//imgLoad.setImageBitmap(bitmap);
//			Map<String, Object> extraData = this.getExtraData();
//			extraData.put(BITAMP, bitmap);
//			this.refreshView();
			ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
			imageView.setImageBitmap(bitmap);


		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

}
