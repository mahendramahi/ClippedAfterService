package org.ninehertzindia.clipped;

import org.ninehertzindia.clipped.util.CommonMethods;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ClippedService extends Service {

	public static SinchClient mSinchClient;

	/*private static final String APP_KEY = "aba8e928-1ab8-4a62-ac40-5c5764e25842";
	private static final String APP_SECRET = "m9nGwpf2iEezwngFtnXHbg==";
	private static final String ENVIRONMENT = "sandbox.sinch.com";*/
	//"3b675323-1fc0-43d7-ae5a-4780527ce403";
	//"sj+vQ1NCtUiYjJgExQM+LA==";
//	
//	*** Client Sixto Sinch Key
//	
	private static final String APP_KEY = "9811d3ab-b0a6-4daa-81c9-6fd7fd0f03e1";
	private static final String APP_SECRET = "nTTu0BiCp0+DZmpiCtrkdQ==";
	private static final String ENVIRONMENT = "sandbox.sinch.com";

	public static final String CALL_ID = "CALL_ID";
	// public static Call call;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		System.out.println("hhhhhClipped onstart");
		try {
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("hhhhhhonstart Commn");

		System.out.println("----------serice start ");
		//if (mSinchClient == null) {
			// mUserId = userName;
			System.out.println("hhhhhhhhh serice start ");
			try {
				System.out.println(
						"hhhhhhhhh serice start with " + CommonMethods.getPreferences(getApplicationContext(), "mobile"));
				mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext())
						.userId(CommonMethods.getPreferences(getApplicationContext(), "mobile")).applicationKey(APP_KEY)
						.applicationSecret(APP_SECRET).environmentHost(ENVIRONMENT).build();

				mSinchClient.setSupportCalling(true);
				mSinchClient.startListeningOnActiveConnection();
				mSinchClient.start();

				mSinchClient.getCallClient().addCallClientListener(new ClippedCallClientListener());

				mSinchClient.addSinchClientListener(new ClippedClientListener());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// call.addCallListener(new );

	//	}

		return super.onStartCommand(intent, flags, startId);
	}

	private class ClippedCallClientListener implements CallClientListener {

		@Override
		public void onIncomingCall(CallClient callClient, Call call) {

			System.out.println("hhhhhhClippedIncoming call");
			Intent intent = new Intent(ClippedService.this, IncomingCallScreenActivity.class);
			intent.putExtra(CALL_ID, call.getCallId());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ClippedService.this.startActivity(intent);

		}

	}

	public class ClippedClientListener implements SinchClientListener {

		@Override
		public void onClientFailed(SinchClient arg0, SinchError arg1) {
			// TODO Auto-generated method stub
			System.out.println("hhhhhhhhhonClient Failed");

		}

		@Override
		public void onClientStarted(SinchClient arg0) {
			System.out.println("hhhhhhhonClient Started");

		}

		@Override
		public void onClientStopped(SinchClient arg0) {
			System.out.println("hhhhhhhhonClient Stopped");

		}

		@Override
		public void onLogMessage(int level, String area, String message) {
			switch (level) {
			case Log.DEBUG:
				Log.d(area, message);
				break;
			case Log.ERROR:
				Log.e(area, message);
				break;
			case Log.INFO:
				Log.i(area, message);
				break;
			case Log.VERBOSE:
				Log.v(area, message);
				break;
			case Log.WARN:
				Log.w(area, message);
				break;
			}
		}

		@Override
		public void onRegistrationCredentialsRequired(SinchClient arg0, ClientRegistration arg1) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onDestroy() {
		
		

		if (mSinchClient != null && mSinchClient.isStarted()) {
			try {
				mSinchClient.terminate();
				mSinchClient = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("hhhhhh Service terminate Hui");
		}
		super.onDestroy();
		System.out.println("hhhhhh Service terminate Hui111");

	}

}
