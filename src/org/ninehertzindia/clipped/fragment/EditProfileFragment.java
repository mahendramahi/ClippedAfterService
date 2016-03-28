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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint({ "NewApi" })
public class EditProfileFragment extends Fragment implements ResponseController {
	private Activity activity;
	private CommonMethods commonMethods;
	private View view;
	private TextView middletxt;
	private ImageView imgback;
	private ImageView imgProfile;
	private RelativeLayout frameImg;
	private EditText et_name;
	private EditText et_email;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 = 200;
	private static Uri pickedImage;
	private static Bitmap bitmap;
	private int tasktype;

	public EditProfileFragment() {
	}

	public static EditProfileFragment newInstance() {
		EditProfileFragment fragment = new EditProfileFragment();
		return fragment;
	}

	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
		view = paramLayoutInflater.inflate(R.layout.edit_profile, paramViewGroup, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setBodyUI();
		return view;
	}

	private void setBodyUI() {
		activity = getActivity();
		et_name = (EditText) view.findViewById(R.id.et_name);
		et_email = (EditText) view.findViewById(R.id.et_email);
		et_name.setText(CommonMethods.getPreferences(activity, "name"));
		et_email.setText(CommonMethods.getPreferences(activity, "email"));
		imgProfile = (ImageView) view.findViewById(R.id.imgProfile);
		Picasso.with(activity).load(URLFactory.baseUrl + CommonMethods.getPreferences(activity, "image")).fit()
				.into(imgProfile);
		imgback = (ImageView) view.findViewById(R.id.imgback);
		imgback.setVisibility(View.VISIBLE);
		imgback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.onBackPressed();

			}
		});
		middletxt = (TextView) view.findViewById(R.id.middletxt);
		middletxt.setText("Edit Profile");
		view.findViewById(R.id.txtUplaod).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareProcess();

			}
		});
		commonMethods = new CommonMethods(activity, this);

		view.findViewById(R.id.txtSave).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkValidation();
			}
		});

		view.findViewById(R.id.txtCancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				activity.onBackPressed();

			}
		});

	}

	protected void checkValidation() {

		GlobalConfig.strName = et_name.getText().toString();
		GlobalConfig.strEmail = et_email.getText().toString();

		if (GlobalConfig.strName.equals("")) {
			GlobalConfig.showToast(getActivity(), "Please enter a Name.");
		} else if (!(validateName(GlobalConfig.strName))) {
			GlobalConfig.showToast(getActivity(), "Please enter valid Name.");
		} else if ((!isValidEmail(GlobalConfig.strEmail))) {
			GlobalConfig.showToast(getActivity(), "Please enter valid Email address.");
		} else if (commonMethods.getConnectivityStatus()) {
			try {

				if (bitmap != null) {
					File F = new File(getPath(pickedImage));
					tasktype = ApplicationConstants.TaskType.editProfile;
					commonMethods.EditProfileUser(
							commonMethods.getEditProfileRequestParmas(CommonMethods.getPreferences(activity, "userid"),
									GlobalConfig.strName, GlobalConfig.strEmail, F));
				} else {
					tasktype = ApplicationConstants.TaskType.editProfile;
					commonMethods.EditProfileUser(
							commonMethods.getEditProfileRequestParmas(CommonMethods.getPreferences(activity, "userid"),
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

	/*
	 * this method is used for name validation using pattern class in java
	 */
	public static boolean validateName(final String username) {
		// Pattern pattern = Pattern.compile("^[A-Z0-9_-]?[a-z0-9_-]{3,15}$");
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$");

		Matcher matcher = pattern.matcher(username);
		return matcher.matches();

	}// end validatenameName method

	// validating email id
	public static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}// end isValidEmail() method

	@SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		activity.startManagingCursor(cursor);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public void onResume() {
		super.onResume();
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
				/*
				 * Date d = new Date(); CharSequence s = DateFormat.format(
				 * "MM-dd-yy hh:mm:ss", d.getTime()); File file = new
				 * File(Environment.getExternalStorageDirectory(), "temp_"+s);
				 * file.mkdirs(); File image = new File(file,
				 * "temp_"+s.toString() + ".jpg");
				 * 
				 * Uri uriSavedImage = Uri.fromFile(image);
				 */

				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(Intent.EXTRA_STREAM, getOutputMediaFileUri());
				startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

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

	//
	//
	//
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

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	/*
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	@SuppressWarnings("deprecation")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// GlobalConfig.showToast(activity, "enter in fragment");
		if (resultCode != 0) {

			if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
				System.out.println("hhhhhhh "+data.getExtras());
				bitmap = (Bitmap) data.getExtras().get("data");
				pickedImage = getImageUri(getActivity(), bitmap);
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

				// edit_profile_pic.setImageBitmap(bitmap);
			} else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 && resultCode == Activity.RESULT_OK
					&& data != null) {

				// Here we need to check if the activity that was triggers was
				// the Image Gallery.
				// If it is the requestCode will match the LOAD_IMAGE_RESULTS
				// value.
				// If the resultCode is RESULT_OK and there is some data we know
				// that an image was picked.
				// if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE1
				// && resultCode == RESULT_OK && data != null) {
				// Let's read picked image data - its URI

				pickedImage = data.getData();

				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePathColumn, null, null, null);
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

		imgProfile.setImageURI(pickedImage);
		// File f = new File(pickedImage.toString());
		// Drawable d = Drawable.createFromPath(f.getAbsolutePath());
		// imgProfile.setBackground(d);
		imgProfile.setScaleType(ScaleType.FIT_XY);
		imgProfile.bringToFront();

		// Picasso.with(activity).load(pickedImage).fit().into(imgProfile);

		// frameImg.setBackgroundResource(R.drawable.frame);
		// frameImg.bringToFront();
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

	/***********************************************************/
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

						activity.onBackPressed();
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
