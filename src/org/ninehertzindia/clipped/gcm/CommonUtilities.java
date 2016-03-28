package org.ninehertzindia.clipped.gcm;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

	// static final String SERVER_URL =
	// "http://10.0.2.2/gcm_server_php/register.php";
	// public static final String SERVER_URL =
	// "http://demo2server.com/sites/mobile/and-push.php";
	public static String SENDER_ID = "664115267120";//775626488318";
	//664115267120

	public static final String TAG = "Clipped";
	public static final String DISPLAY_MESSAGE_ACTION = "org.ninehertzindia.clipped.gcm.DISPLAY_MESSAGE";
	//public static final String EXTRA_MESSAGE = "message";
	public static final String EXTRA_MOBILE = "mobile";
	public static final String EXTRA_NAME = "name";
	public static final String EXTRA_FileType = "filetype";
	public static final String EXTRA_Data = "data";
	public static final String EXTRA_Message = "message";

	/**
	 * Notifies UI to display a message.
	 */
	public static void displayMessage(Context context, String filetype,
			String mobile,String name,String data2) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_NAME, name);
		intent.putExtra(EXTRA_MOBILE, mobile);
		intent.putExtra(EXTRA_FileType, filetype);
		intent.putExtra(EXTRA_Data, data2);
		context.sendBroadcast(intent);
	}
}