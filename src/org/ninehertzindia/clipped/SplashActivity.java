package org.ninehertzindia.clipped;

import org.ninehertzindia.clipped.database.DatabaseHandler;
import org.ninehertzindia.clipped.gcm.RegisterDevice;
import org.ninehertzindia.clipped.util.CommonMethods;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
@SuppressWarnings("deprecation")

public class SplashActivity extends Activity {

	private Activity activity;
	private String userID;
	public static String device_id;
	public static String Manufetures_id;
	public static String HardWare_Serial;
	public static String registrationId;
	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		activity = this;
		device_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		
		HardWare_Serial = android.os.Build.SERIAL;
		Manufetures_id = android.os.Build.MANUFACTURER;
		
		System.out.println("hhhhhhhhhh"+HardWare_Serial+"deviceid"+device_id+"Lol"+Manufetures_id);
		
		db = new DatabaseHandler(activity);
		
		RegisterDevice regDevice = new RegisterDevice(activity);
		regDevice.registerDevice();

		System.out.println("hhhhhhhhhhhhDevice id" + registrationId);
		
		
		userID = CommonMethods.getPreferences(activity, "userid");
		if (db.getContactVOsCount() == 0) {
			new FillArraylistAsyncTask(this).execute();
		}
		/*else {
			if (CommonMethods.getPreferencesInteger(activity, "countVO") != db.getContactVOsCount()) {
				
			}
			CommonMethods.SetPreferencesInteger(activity,"countVO", db.getContactVOsCount());
		}*/
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				
				System.out.println("username" + CommonMethods.getPreferences(activity, "name"));
				// if (!getSinchServiceInterface().isStarted()) {
				// getSinchServiceInterface().startClient(CommonMethods.getPreferences(activity,
				// "name"));
				// // showSpinner();
				// }
				if (userID.equalsIgnoreCase("") || userID == null) {
					Intent intent = new Intent(SplashActivity.this, RegistrationSlideActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				} else {
					CommonMethods.SetPreferences(activity, "flash", "0");
					Intent intent = new Intent(SplashActivity.this, CommonFragmentActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}

			}
		}, 2000);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
