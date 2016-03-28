package org.ninehertzindia.clipped.gcm;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import org.ninehertzindia.clipped.SplashActivity;

import com.google.android.gcm.GCMRegistrar;

/**
 * Register a new device for push notification
 * 
 * @author Lol
 * 
 */
public class RegisterDevice {

	// public static String name;
	// public static String email;
	Activity _activity;
	String regId;

	AsyncTask<Void, Void, Void> mRegisterTask;

	public RegisterDevice(Activity _activity) {
		// RegisterDevice.name = name;
		// RegisterDevice.email = email;
		this._activity = _activity;
	}

	public void registerDevice() {

		GCMRegistrar.checkDevice(_activity);
		GCMRegistrar.checkManifest(_activity);

		// Get GCM registration id
		regId = GCMRegistrar.getRegistrationId(_activity);
		System.out.println("111111" + regId);
		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(_activity, CommonUtilities.SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(_activity)) {
				System.out.println("Device already registered with GCM");
				SplashActivity.registrationId = regId;
			} else {
				final Context context = _activity;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						ServerUtilities.register(context, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}
}
