package org.ninehertzindia.clipped;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.entity.FileEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.ninehertzindia.clipped.gcm.CommonUtilities;
import org.ninehertzindia.clipped.gcm.ServerUtilities;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.GlobalConfig;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.gson.JsonObject;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	public static int count = 0;
	public static int number = 0;
	public static SharedPreferences shared_noti_count;
	public static SharedPreferences.Editor editor;
	String filetype;
	String mobile;
	String name;
	String data2;
	String dataname;

	// public CommonMethods commonmethods = new CommonMethods(this, this);

	public GCMIntentService() {
		super(CommonUtilities.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		System.out.println("registration id in onRegistered : " + registrationId);
		SplashActivity.registrationId = registrationId;
		CommonUtilities.displayMessage(context, "Your device registred with GCM", null, null, null);
		ServerUtilities.register(context, registrationId);
	}

	/**
	 * Method called on device un registred
	 */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		CommonUtilities.displayMessage(context, getString(R.string.gcm_unregistered), null, null, null);
		ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 */
	@Override
	protected void onMessage(Context context, Intent intent) {

		System.out.println("hhhhhhhhhhhhhhh GCM Received message");

		/*
		 * String mobile =
		 * intent.getExtras().getString(CommonUtilities.EXTRA_MOBILE); String
		 * name = intent.getExtras().getString(CommonUtilities.EXTRA_NAME);
		 * String filetype =
		 * intent.getExtras().getString(CommonUtilities.EXTRA_FileType);
		 */

		String data = intent.getExtras().getString(CommonUtilities.EXTRA_Data);
		//
Log.d("hhhhhhhhhhhhh GCM", ""+data);
		if (intent.getExtras().getString(CommonUtilities.EXTRA_Message).toString().equalsIgnoreCase("logout")) {

			String message = null;
			try {

				GlobalConfig.strName = "";
				GlobalConfig.strEmail = "";
				
				stopService(new Intent(context, ClippedService.class));
				
				CommonMethods.SetPreferences(context, "name", "");
				CommonMethods.SetPreferences(context, "email", "");
				CommonMethods.SetPreferences(context, "mobile", "");
				CommonMethods.SetPreferences(context, "paymentstatus", "free");
				

			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				message = intent.getExtras().getString(CommonUtilities.EXTRA_Message);
				Toast.makeText(context, "Clipped Logout", Toast.LENGTH_SHORT).show();
				CommonMethods.SetPreferences(context, "userid", "");// getPreferences(activity,
																	// "userid")
				startActivity(new Intent(context, SplashActivity.class));
				System.exit(0);
				System.out.println("hhhhhhhhh Clipped GCM" + message.toString());

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		if (intent.getExtras().getString(CommonUtilities.EXTRA_Message).toString().equalsIgnoreCase("currentplan")) {

			Log.d("hhhhhhhhhhhhh GCM", "Current lane");
		//	String message = null;
			try {

				CommonMethods.SetPreferences(context, "paymentstatus", "free");
				Toast.makeText(getBaseContext(), "Your Payment Expire", Toast.LENGTH_SHORT).show();

			} catch (Exception e) {
				// TODO: handle exception
			}

		} 
		else {
			try {
				System.out.println("hhhhhhhhh Clipped GCM" + data.toString());
				JSONObject jsonObject = new JSONObject(data);
				filetype = jsonObject.getString("filetype");
				mobile = jsonObject.getString("mobile");
				name = jsonObject.getString("name");
				data2 = jsonObject.getString("data");
				dataname = jsonObject.getString("fileName");

				System.out.println("hhhhhhhhhhhh " + mobile + "nn" + name + "ff" + filetype + "data" + data2 + ":name:"
						+ dataname);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// String name = contactExists(context, mobile);
			// System.out.println("hhhhhhhmessage is : " + name + message);

			// notifies user
			/*
			 * if (CommonMethods.getPreferences(context,
			 * "userid").equalsIgnoreCase("") ||
			 * CommonMethods.getPreferences(context, "userid") == null) {
			 * 
			 * } else {
			 */
			if (CommonMethods.getPreferences(context, "setton").equalsIgnoreCase("")
					|| CommonMethods.getPreferences(context, "setton").equalsIgnoreCase("1")) {
				CommonUtilities.displayMessage(context, filetype, mobile, name, data2);
				generateNotification(context, filetype, mobile, name, data2, dataname);
			} else {

			}
		}
		// }

	}

	private String contactExists(Context context, String mobile) {
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(mobile));
		String[] mPhoneNumberProjection = { PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
		Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
		try {
			if (cur.moveToFirst()) {
				String name = cur.getString(cur.getColumnIndex(PhoneLookup.DISPLAY_NAME));

				return name;
			}
		} finally {
			if (cur != null)
				cur.close();
		}
		return mobile;
	}

	/**
	 * Method called on receiving a deleted message
	 */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		CommonUtilities.displayMessage(context, message, null, null, null);
		// notifies user
		// generateNotification(context, filetype, mobile, name, data2,
		// dataname);
	}

	/**
	 * Method called on Error
	 */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		CommonUtilities.displayMessage(context, getString(R.string.gcm_error, errorId), null, null, null);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		CommonUtilities.displayMessage(context, getString(R.string.gcm_recoverable_error, errorId), null, null, null);
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 * 
	 * @param name
	 */
	@SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String filetype, String mobile, String name, String data,
			String dataname) {

		String temp_file_type = "";
		if (filetype.equalsIgnoreCase("1")) {
			temp_file_type = "Audio";
		}
		if (filetype.equalsIgnoreCase("2")) {
			temp_file_type = "Video";
		}
		if (filetype.equalsIgnoreCase("3")) {
			temp_file_type = "Text";
		}
		/*
		 * Intent intent = new Intent(context, CommonFragmentActivity.class);
		 * PendingIntent pIntent = PendingIntent.getActivity(context,
		 * (int)System.currentTimeMillis(), intent, 0);
		 * 
		 * Notification myNotification = new Notification.Builder(context)
		 * .setContentTitle("Title") .setContentText("Some text...."
		 * +temp_file_type) .setSmallIcon(R.drawable.ic_launcher)
		 * .setContentIntent(pIntent) .setAutoCancel(true).build();
		 * 
		 * 
		 * NotificationManager notificationManager = (NotificationManager)
		 * context.getSystemService(NOTIFICATION_SERVICE);
		 * 
		 * notificationManager.notify(0, myNotification);
		 */

		/// New Notitification

		String str_notification = "Assigned" + " " + temp_file_type;
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, str_notification, when);

		String title = "" + name + "-" + mobile;// context.getString("");//R.string.app_name);

		Intent notificationIntent = new Intent(context, CommonFragmentActivity.class);

		notificationIntent.putExtra("pushMsg", str_notification);
		notificationIntent.putExtra("mobile", mobile);
		notificationIntent.putExtra("filetype", filetype);
		notificationIntent.putExtra("name", name);

		notificationIntent.putExtra("status", "push");
		notificationIntent.putExtra("data", data);
		notificationIntent.putExtra("dataname", dataname);

		notificationIntent.putExtra("fragment", "frag");

		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context,
				CommonMethods.getPreferencesInteger(context, "notififation"), notificationIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, title, str_notification, intent);
		// PendingIntent pendingNotificationIntent =
		// PendingIntent.getActivity(context.getApplicationContext(), 0,
		// notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// notification.sound = Uri.parse("android.resource://" +
		// context.getPackageName() + "your_sound_file_name.mp3");

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		if (CommonMethods.getPreferencesInteger(context, "notififation") == 0) {

			notificationManager.notify(1, notification);
			CommonMethods.SetPreferencesInteger(context, "notififation", 1);
		} else {
			notificationManager.notify(CommonMethods.getPreferencesInteger(context, "notififation") + 1, notification);
			CommonMethods.SetPreferencesInteger(context, "notififation",
					CommonMethods.getPreferencesInteger(context, "notififation") + 1);

		}

	}

}
