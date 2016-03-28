package org.ninehertzindia.clipped;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;
import org.ninehertzindia.clipped.footerfragment.ContactFragment;
import org.ninehertzindia.clipped.footerfragment.HistoryFragment;
import org.ninehertzindia.clipped.footerfragment.ProfileFragment;
import org.ninehertzindia.clipped.footerfragment.SettingFragment;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.SelctedLineareLayout;
import org.ninehertzindia.clipped.util.URLFactory;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class CommonFragmentActivity extends DemoActivity implements OnClickListener {

	public Activity activity;
	private LinearLayout ll_history, ll_contact, ll_profile, ll_setting;

	private SelctedLineareLayout root;
	private FragmentManager fragmentManager;
	private CommonMethods commonMethods;

	private int backStatus = 0;

	private String popupname;
	private String popupdata;
	private String popupfiletype;
	private String popupmobile;
	private String popupstatus;
	private String popupdataname;
	// Ads
	private InterstitialAd mInterstitialAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_fragment);

		System.out.println("hhh-----" + isMyServiceRunning(ClippedService.class));
		System.out.println("hhh-----" + CommonMethods.getPreferences(getApplicationContext(), "mobile"));
		SetBodyUI();
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@SuppressLint("NewApi")
	private void SetBodyUI() {

		activity = this;

		mInterstitialAd = new InterstitialAd(activity);

		mInterstitialAd.setAdUnitId(getResources().getString(R.string.AdmobKey));

		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		AdRequest adRequest = new AdRequest.Builder()

				// Add a test device to show Test Ads
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("" + tm.getDeviceId()).build();
		mInterstitialAd.loadAd(adRequest);

		System.out.println("hhhhhhhhhhhhh request");

		mInterstitialAd.setAdListener(new AdListener() {
			public void onAdLoaded() {
				// Call displayInterstitial() function
				System.out.println("hhhhhhhhhhhhh Display");
				displayInterstitial();
			}
		});

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				System.out.println("username" + CommonMethods.getPreferences(activity, "name"));
				// if (!getSinchServiceInterface().isStarted()) {
				// getSinchServiceInterface().startClient(CommonMethods.getPreferences(activity,
				// "name"));
				// // showSpinner();
				// }

			}
		}, 3000);
		fragmentManager = getSupportFragmentManager();
		root = (SelctedLineareLayout) findViewById(R.id.root);

		root.setselected(0);
		fragmentManager.beginTransaction().replace(R.id.container, HistoryFragment.newInstance()).commit();

		commonMethods = new CommonMethods(activity, this);
		commonMethods.hideKeyboard();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		ll_history = (LinearLayout) findViewById(R.id.ll_history);
		ll_contact = (LinearLayout) findViewById(R.id.ll_contact);
		ll_profile = (LinearLayout) findViewById(R.id.ll_profile);
		ll_setting = (LinearLayout) findViewById(R.id.ll_setting);
		ll_history.setOnClickListener(this);
		ll_contact.setOnClickListener(this);
		ll_profile.setOnClickListener(this);
		ll_setting.setOnClickListener(this);

		fragmentManager.addOnBackStackChangedListener(getListener());

		//
		startService(new Intent(activity, DownloadDataService.class));
		//

		if (!isMyServiceRunning(ClippedService.class)) {

			System.out.println("hh---Service ko Fir se start Kiya");

			getApplicationContext().startService(new Intent(activity.getApplicationContext(), ClippedService.class));

		}

		try {

			popupstatus = getIntent().getStringExtra("status");
			popupdata = getIntent().getStringExtra("data");
			popupfiletype = getIntent().getStringExtra("filetype");
			popupname = getIntent().getStringExtra("name");
			popupmobile = getIntent().getStringExtra("mobile");
			popupdataname = getIntent().getStringExtra("dataname");

			System.out.println("hhhhhhhhhh Commom status:" + popupstatus + "data:" + popupdata + "filetype:"
					+ popupfiletype + "name:" + popupname + "mobile:" + popupmobile);

		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			if (popupstatus.equalsIgnoreCase("push")) {

				System.out.println("hhhhhhhh push call");
				ShowDialogForAssignedFile();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/////////////////////////////////////////////////

	private FragmentManager.OnBackStackChangedListener getListener() {
		FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
			public void onBackStackChanged() {

				Fragment fragment = fragmentManager.findFragmentById(R.id.container);

				if (fragment != null) {
					commonMethods.hideKeyboard();
					fragment.onResume();
				} else {
					finish();
				}
			}
		};

		return result;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.ll_history:
			backStatus = 0;
			if (root.getselected() != 0) {
				root.setselected(0);
				fragmentManager.beginTransaction().replace(R.id.container, HistoryFragment.newInstance()).commit();
			}
			break;

		case R.id.ll_contact:

			backStatus = 0;
			if (root.getselected() != 2) {
				root.setselected(2);
				fragmentManager.beginTransaction().replace(R.id.container, ContactFragment.newInstance()).commit();
			}
			break;

		case R.id.ll_profile:
			backStatus = 0;
			if (root.getselected() != 4) {
				root.setselected(4);
				fragmentManager.beginTransaction().replace(R.id.container, ProfileFragment.newInstance()).commit();
			}
			break;

		case R.id.ll_setting:
			backStatus = 0;
			if (root.getselected() != 6) {
				root.setselected(6);
				fragmentManager.beginTransaction().replace(R.id.container, SettingFragment.newInstance()).commit();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		int count = fragmentManager.getBackStackEntryCount();
		commonMethods.hideKeyboard();
		if (count == 0) {
			if (backStatus == 0) {
				backStatus++;
				Toast.makeText(activity, "Press again to close app.", Toast.LENGTH_SHORT).show();
			} else {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // super.onActivityResult(requestCode, resultCode, data);
	// GlobalConfig.showToast(activity, "enter in activity");
	// Fragment fragment = fragmentManager.findFragmentById(R.id.container);
	//
	// if (fragment instanceof EditProfileFragment) {
	// fragment.onActivityResult(requestCode, resultCode, data);
	// }
	// // else if (fragment instanceof SinglePostFragment) {
	// // fragment.onActivityResult(requestCode, resultCode, data);
	// // }
	// }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Request for Ads

		if (CommonMethods.getPreferences(activity, "userid").equalsIgnoreCase("")) {
			Intent i = new Intent(activity, SplashActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			startActivity(i);
			this.finish();

		}

	} // onResume

	// DisplayAds
	public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		if (mInterstitialAd.isLoaded()) {

			// Terms For Is user Premium or not
			if (commonMethods.getPreferences(activity, "paymentstatus").toString().equalsIgnoreCase("")
					|| commonMethods.getPreferences(activity, "paymentstatus").toString().equalsIgnoreCase("free")) {
				mInterstitialAd.show();
			} else {

			}

		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

		try {

			popupstatus = intent.getStringExtra("status");
			popupdata = intent.getStringExtra("data");
			popupfiletype = intent.getStringExtra("filetype");
			popupname = intent.getStringExtra("name");
			popupmobile = intent.getStringExtra("mobile");
			popupdataname = intent.getStringExtra("dataname");

			System.out.println("hhhhhhhhhh Commom status:" + popupstatus + "data:" + popupdata + "filetype:"
					+ popupfiletype + "name:" + popupname + "mobile:" + popupmobile);

		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			if (popupstatus.equalsIgnoreCase("push")) {

				System.out.println("hhhhhhhh push call");
				ShowDialogForAssignedFile();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	void ShowDialogForAssignedFile() {

		System.out.println("hhhhhhhhhhhhhhhhh Show dialog");

		final Dialog dialog = new Dialog(activity);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.assigneddatapopup);
		dialog.getWindow().setGravity(Gravity.CENTER);

		final TextView tvok = (TextView) dialog.findViewById(R.id.txt_OK);

		tvok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// DownloadDataService.AssignedDataDownload(activity);

				System.out.println("hhhhhhhhhhhhhhhhh Show dialog Click");
				Intent downloadService = new Intent(activity, DownloadDataService.class);

				downloadService.putExtra("data", popupdata);
				downloadService.putExtra("filetype", popupfiletype);
				downloadService.putExtra("name", popupname);
				downloadService.putExtra("mobile", popupmobile);
				downloadService.putExtra("dataname", popupdataname);

				startService(downloadService);

				popupstatus = "";

				// DownloadFromUrl(URLFactory.baseUrl+popupdata, "gana.mp3");

				dialog.dismiss();

			}
		});

		dialog.show();

	}

	public void DownloadFromUrl(String imageURL, String fileName) { // this is
																	// the
																	// downloader
																	// method

		int count;
		try {
			File root = android.os.Environment.getExternalStorageDirectory();

			File dir = new File(root.getAbsolutePath() + "/Clipped/");
			if (dir.exists() == false) {
				dir.mkdirs();
			}

			System.out.println("hhhhhhh url" + popupdata);
			URL urls = new URL(URLFactory.baseUrl + popupdata);
			URLConnection connection = urls.openConnection();
			connection.connect();
			// this will be useful so that you can show a tipical 0-100%
			// progress bar
			int lenghtOfFile = connection.getContentLength();

			InputStream input = new BufferedInputStream(urls.openStream());
			OutputStream output = new FileOutputStream(dir + "/lol.mp3");

			byte data[] = new byte[1024];

			long total = 0;

			while ((count = input.read(data)) != -1) {
				total += count;
				// publishing the progress....
				// publishProgress((int) (total * 100 / lenghtOfFile));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
			System.out.println("hhhhhh" + e);
		}
		/*
		 * try { URL url = new
		 * URL(URLFactory.baseUrl+"uploads/rington/39/1454931351.ogg"); //you
		 * can write here any link File file = new File(fileName);
		 * 
		 * long startTime = System.currentTimeMillis(); System.out.println(
		 * "hhhhhdownload begining"); System.out.println("hhhhh download url:" +
		 * url); System.out.println("hhhhh downloaded file name:" + fileName);
		 * Open a connection to that URL. URLConnection ucon =
		 * url.openConnection();
		 * 
		 * 
		 * Define InputStreams to read from the URLConnection.
		 * 
		 * InputStream is = ucon.getInputStream(); BufferedInputStream bis = new
		 * BufferedInputStream(is);
		 * 
		 * 
		 * Read bytes to the Buffer until there is nothing more to read(-1).
		 * 
		 * ByteArrayBuffer baf = new ByteArrayBuffer(50); int current = 0; while
		 * ((current = bis.read()) != -1) { baf.append((byte) current); }
		 * 
		 * Convert the Bytes read to a String. FileOutputStream fos = new
		 * FileOutputStream(file); fos.write(baf.toByteArray()); fos.close();
		 * System.out.println("hhhhh download ready in" +
		 * ((System.currentTimeMillis() - startTime) / 1000) + " sec");
		 * 
		 * } catch (Exception e) { System.out.println(
		 * "hhhhh ImageManager Error: " + e); }
		 */

	}

}
