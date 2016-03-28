package org.ninehertzindia.clipped.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.RegistrationSlideActivity;
import org.ninehertzindia.clipped.network.APIResponse;
import org.ninehertzindia.clipped.network.ResponseController;
import org.ninehertzindia.clipped.util.ApplicationConstants;
import org.ninehertzindia.clipped.util.CircleTransform;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.GlobalConfig;
import org.ninehertzindia.clipped.util.URLFactory;

import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class RegistrationUserProfileFragment extends Fragment implements ResponseController {
	public static final String ARG_PAGE = "page";
	private int mPageNumber;
	Activity activity;
	private ImageView imgback;
	private TextView txt_upload;
	private ImageView imgUserPic;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 = 200;
	private static Uri pickedImage;
	private static Bitmap bitmap;
	private CommonMethods commonMethods;
	private int tasktype;
	private TextView middletxt;

	public static RegistrationUserProfileFragment create(int pageNumber) {
		RegistrationUserProfileFragment fragment = new RegistrationUserProfileFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public RegistrationUserProfileFragment() {
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.registration_profile, container, false);
		activity = getActivity();
		commonMethods = new CommonMethods(activity, this);
		rootView.findViewById(R.id.txt_next).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				checkValidation();

			}
		});
		middletxt = (TextView) rootView.findViewById(R.id.middletxt);
		middletxt.setText("Registration");
		imgback = (ImageView) rootView.findViewById(R.id.imgback);
		imgback.setVisibility(View.VISIBLE);
		imgback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RegistrationSlideActivity.previousScreen(1);

			}
		});
		imgUserPic = (ImageView) rootView.findViewById(R.id.imgUserPic);
		if (!CommonMethods.getPreferences(activity, "name").equals("")) {
			Picasso.with(activity).load(URLFactory.baseUrl + CommonMethods.getPreferences(activity, "image"))
					.transform(new CircleTransform()).into(imgUserPic);
		}
		txt_upload = (TextView) rootView.findViewById(R.id.txt_upload);

		txt_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareProcess();

			}
		});

		return rootView;
	}

	protected void checkValidation() {
		if (commonMethods.getConnectivityStatus()) {
			try {

				if (bitmap != null) {
					File F = new File(getPath(pickedImage));
					tasktype = ApplicationConstants.TaskType.editProfile;
					commonMethods.EditProfileUser(commonMethods.getEditProfileRequestParmas(GlobalConfig.userId,
							GlobalConfig.strName, GlobalConfig.strEmail, F));
				} else {
					tasktype = ApplicationConstants.TaskType.editProfile;
					commonMethods.EditProfileUser(commonMethods.getEditProfileRequestParmas(GlobalConfig.userId,
							GlobalConfig.strName, GlobalConfig.strEmail, null));
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			GlobalConfig.showToast(activity, getResources().getString(R.string.internet_error_message));
		}

	}

	@SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		activity.startManagingCursor(cursor);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
		return mPageNumber;
	}

	private void shareProcess() {
		final Dialog dialogMapMain = new Dialog(activity);
		dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable());
		dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogMapMain.setContentView(R.layout.popup_uploadpic);
		dialogMapMain.getWindow().setGravity(Gravity.BOTTOM);

		dialogMapMain.setCancelable(true);
		dialogMapMain.setCanceledOnTouchOutside(true);
		dialogMapMain.show();

		LinearLayout btn_takepic = (LinearLayout) dialogMapMain.findViewById(R.id.llTakePic);

		btn_takepic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMapMain.dismiss();
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(Intent.EXTRA_STREAM, getOutputMediaFileUri());
				startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
				// Intent cameraIntent = new
				// Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				// startActivityForResult(cameraIntent,
				// CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
			}
		});

		LinearLayout btn_picgallery = (LinearLayout) dialogMapMain.findViewById(R.id.llPicGallery);

		btn_picgallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMapMain.dismiss();

				Intent i = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE1);

			}
		});

	}
	/**
	 * Creating file uri to store image/video
	 */
	public static Uri getOutputMediaFileUri() {
		return Uri.fromFile(getOutputMediaFile());
	}

	/**
	 * returning image / video
	 */
	private static File getOutputMediaFile() {

		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"YOUR DIRECTORY NAME");

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("image upload", "Oops! Failed create " + "YOUR DIRECTORY NAME" + " directory");
				return null;
			}
		}

		// TODO change naming
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		return mediaFile;
	}
	//
	//

	public static void fixMediaDir() {
		File sdcard = Environment.getExternalStorageDirectory();
		if (sdcard == null) {
			return;
		}
		File dcim = new File(sdcard, "DCIM");
		if (dcim == null) {
			return;
		}
		File camera = new File(dcim, "Camera");
		if (camera.exists()) {
			return;
		}
		camera.mkdir();
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		/*
		 * ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		 * inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		 * 
		 * String path =
		 * Images.Media.insertImage(inContext.getContentResolver(), inImage,
		 * "Title", null); return Uri.parse(path);
		 */
		//
		System.out.println("hhhhhhhhhhhBitMAP: " + inImage);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		System.out.println("hhhhhhhhhhhinimage " + inImage);
		String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		System.out.println("hhhhhhhhPATH: " + path);
		return Uri.parse(path);
	}

	public static Bitmap rotateImage(Bitmap source, float angle) {
		Bitmap retVal;

		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

		return retVal;
	}

	/*
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != 0) {

			if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
				
				
				// get bundle

				bitmap = (Bitmap) data.getExtras().get("data"); 
				/*
				 * Bundle extras = activity.getIntent().getExtras();
				 * System.out.println("hhhhhhhhhhhh extra :" + extras); // get
				 * bitmap bitmap = (Bitmap) extras.get("data");
				 */
				System.out.println("hhhhhhhhhhhh extra :" + bitmap);

				// bitmap = (Bitmap) data.getExtras().get("data");
				pickedImage = getImageUri(activity, bitmap);
				System.out.println("hhhhhhh "+bitmap);
				System.out.println("hhhhhhh "+pickedImage);

				Bitmap sourceBitmap = bitmap;

				ExifInterface exif = null;
				try {
					exif = new ExifInterface(pickedImage.getPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
				System.out.println("in camera rotate back and ori" + orientation);
				if (orientation == 6) {
					Matrix matrix = new Matrix();
					matrix.postRotate(90);
					bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(),
							matrix, true);
				} else if (orientation == 8) {
					Matrix matrix = new Matrix();
					matrix.postRotate(270);
					bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(),
							matrix, true);
				} else {
					bitmap = sourceBitmap;
				}
				pickedImage = null;
				pickedImage = getImageUri(getActivity(), bitmap);
			} else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 && resultCode == Activity.RESULT_OK
					&& data != null) {

				pickedImage = data.getData();

				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = activity.getContentResolver().query(pickedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				BitmapFactory.Options options = new BitmapFactory.Options();

				// downsizing image as it throws OutOfMemory Exception for
				// larger
				// images
				options.inSampleSize = 8;

				bitmap = BitmapFactory.decodeFile(picturePath, options);

				Bitmap sourceBitmap = bitmap;

				ExifInterface exif = null;
				try {
					exif = new ExifInterface(picturePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
				System.out.println("in camera rotate back and ori" + orientation);
				if (orientation == 6) {
					Matrix matrix = new Matrix();
					matrix.postRotate(90);
					bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(),
							matrix, true);
				} else if (orientation == 8) {
					Matrix matrix = new Matrix();
					matrix.postRotate(270);
					bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(),
							matrix, true);
				} else {
					bitmap = sourceBitmap;
				}

			} else if (requestCode == 0000) {
				Intent intenttt = new Intent();
				bitmap = (Bitmap) intenttt.getParcelableExtra("bitmap_round");

			}

		}
		Picasso.with(activity).load(pickedImage).transform(new CircleTransform()).into(imgUserPic);

	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(input);
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void OnComplete(APIResponse apiResponse) {

		switch (tasktype) {
		case ApplicationConstants.TaskType.editProfile:
			System.out.println("Response of upload details" + apiResponse.getResponse());
			String str_message = "";
			if (!(apiResponse.getResponse() == null)) {
				try {
					JSONObject rootJsonObjObject = new JSONObject(apiResponse.getResponse());
					str_message = rootJsonObjObject.getString("msg");
					String str_status = rootJsonObjObject.getString("status");

					if (Boolean.parseBoolean(str_status)) {

						JSONObject data = rootJsonObjObject.getJSONObject("data");

						CommonMethods.SetPreferences(activity, "userid", data.getString("userid"));
						CommonMethods.SetPreferences(activity, "mobile", data.getString("mobile"));
						CommonMethods.SetPreferences(activity, "name", data.getString("name"));
						CommonMethods.SetPreferences(activity, "email", data.getString("email"));
						CommonMethods.SetPreferences(activity, "image", data.getString("image"));
						CommonMethods.SetPreferences(activity, "image", data.getString("thumb_image"));

						// go to otp screen
						RegistrationSlideActivity.nextScreen(1);

					} else {
						GlobalConfig.showToast(getActivity(), str_message);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				GlobalConfig.showToast(getActivity(), str_message);
			}
			break;

		default:
			break;
		}
	}

	/***********************************************************/

}
