package org.ninehertzindia.clipped.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * use for global static variables configuration
 * 
 * @author Manish Agrawal
 * 
 */
public class GlobalConfig {

	public static String userId = "";
	public static String showAd = "";
	public static String str_verify = "";
	public static String str_code = "";
	public static String str_number = "";
	public static String strName = "";
	public static String strEmail = "";
	public static String CountryZipCode = "";
	public static boolean AssignedStatus = true;

	public static void showToast(Activity _ac, String msg) {
		Toast.makeText(_ac, msg, Toast.LENGTH_SHORT).show();
	}

	/*
	 * Sets the font on all TextViews in the ViewGroup. Searches recursively for
	 * all inner ViewGroups as well. Just add a check for any other views you
	 * want to set as well (EditText, etc.)
	 */
	public static void setZrnicFont(ViewGroup group, Typeface font) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView)
				((TextView) v).setTypeface(font);
			else if (v instanceof ViewGroup)
				setZrnicFont((ViewGroup) v, font);
		}
	}

	/**
	 * This method is used for call another activity through
	 * 
	 * @param currentActivity
	 * @param class_activity
	 */
	public static void changeActivity(Activity currentActivity, Class<? extends Activity> class_activity) {

		Intent intent = new Intent(currentActivity, class_activity);
		currentActivity.startActivity(intent);
		currentActivity.finish();
		currentActivity.overridePendingTransition(0, 0);
	}

	/**
	 * This method is used for call another activity
	 * 
	 * @param currentActivity
	 * @param str_activity_name
	 */
	public static void changeActivityThroughPackage(Activity currentActivity, String str_activity_name) {

		try {
			String packageName = currentActivity.getApplicationContext().getPackageName();
			Intent intent = new Intent();
			intent.setComponent(new ComponentName(packageName, str_activity_name));
			currentActivity.startActivity(intent);
			currentActivity.overridePendingTransition(0, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}