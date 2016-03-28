 package org.ninehertzindia.clipped;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CallScreenActivity extends Activity {

	static final String TAG = CallScreenActivity.class.getSimpleName();

	private AudioPlayer mAudioPlayer;
	private Timer mTimer;
	private UpdateCallDurationTask mDurationTask;

	private String mCallId;
	private long mCallStart = 0;

	private TextView mCallDuration;
	private TextView mCallState;
	private TextView mCallerName;

	private String mPicture,name;
	public Call call;
	public KeyguardManager.KeyguardLock kl;
	private ImageView userImage;

	private class UpdateCallDurationTask extends TimerTask {

		@Override
		public void run() {
			CallScreenActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateCallDuration();
				}
			});
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		KeyguardManager km = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
		 kl = km.newKeyguardLock("MyKeyguardLock");
		kl.disableKeyguard();
		
		
		setContentView(R.layout.callscreen);
		mAudioPlayer = new AudioPlayer(this);
		mCallDuration = (TextView) findViewById(R.id.callDuration);
		mCallerName = (TextView) findViewById(R.id.remoteUser);
		mCallState = (TextView) findViewById(R.id.callState);
		userImage = (ImageView) findViewById(R.id.userImage);
		ImageView endCallButton = (ImageView) findViewById(R.id.hangupButton);

		endCallButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				endCall();
			}
		});
		mCallStart = System.currentTimeMillis();
		mCallId = getIntent().getStringExtra(ClippedService.CALL_ID);
		mPicture = getIntent().getStringExtra("image");
		name = getIntent().getStringExtra("localname");
		
		try {
			Picasso.with(this).load(mPicture).into(userImage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// call = ClippedService.mSinchClient.getCallClient().getCall(mCallId);
		// call.addCallListener(new SinchCallListener());
		// //mCallerName.setText(call.getRemoteUserId());
		// //mCallState.setText(call.getState().toString());

		Call call = ClippedService.mSinchClient.getCallClient().getCall(mCallId);
		if (call != null) {
			call.addCallListener(new SinchCallListener());
			mCallerName.setText(name);
			mCallState.setText(call.getState().toString());
		} else {
			Log.e(TAG, "Started with invalid callId, aborting.");
			finish();
		}

	}

	// @Override
	// public void onServiceConnected() {
	// // Call call = getSinchServiceInterface().getCall(mCallId);
	// // if (call != null) {
	// // call.addCallListener(new SinchCallListener());
	// // mCallerName.setText(call.getRemoteUserId());
	// // mCallState.setText(call.getState().toString());
	// // } else {
	// // Log.e(TAG, "Started with invalid callId, aborting.");
	// // finish();
	// // }
	// }

	@Override
	public void onPause() {
		super.onPause();
		mDurationTask.cancel();
		mTimer.cancel();
	}

	@Override
	public void onResume() {
		super.onResume();
		mTimer = new Timer();
		mDurationTask = new UpdateCallDurationTask();
		mTimer.schedule(mDurationTask, 0, 500);
	}

	@Override
	public void onBackPressed() {
		// User should exit activity by ending call, not by going back.
	}

	private void endCall() {
		
		mAudioPlayer.stopProgressTone();
		kl.reenableKeyguard();
		
		// ClippedService.call =
		// ClippedService.mSinchClient.getCallClient().getCall(mCallId);
		// ClippedService.call.addCallListener(new SinchCallListener());
		// if (ClippedService.call != null) {
		// System.out.println("hhhhhCall endcall up");
		//
		// ClippedService.call.hangup();
		// call = null;
		// }

		// finish();
		mAudioPlayer.stopProgressTone();
		Call call = ClippedService.mSinchClient.getCallClient().getCall(mCallId);
		if (call != null) {
			call.hangup();

			//this.stopService(new Intent(getApplicationContext(), ClippedService.class));

			//this.startService(new Intent(getApplicationContext(), ClippedService.class));

		}
		
		finish();

		// Call call = getSinchServiceInterface().getCall(mCallId);
		// if (call != null) {
		// call.hangup();
		// }
		// finish();
	}

	private String formatTimespan(long timespan) {
		long totalSeconds = timespan / 1000;
		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60;
		return String.format(Locale.US, "%02d:%02d", minutes, seconds);
	}

	private void updateCallDuration() {
		if (mCallStart > 0) {
			mCallDuration.setText(formatTimespan(System.currentTimeMillis() - mCallStart));
		}
	}

	private class SinchCallListener implements CallListener {

		@Override
		public void onCallEnded(Call call) {

			CallEndCause cause = call.getDetails().getEndCause();
			System.out.println("hhhhhhCall ended. Reason: " + cause.toString());
			mAudioPlayer.stopProgressTone();
			setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
			String endMsg = "Call ended: " + call.getDetails().toString();
			// Toast.makeText(CallScreenActivity.this, endMsg,
			// Toast.LENGTH_LONG).show();
			//ClippedService.mSinchClient.terminate();
			endCall();

		}

		@Override
		public void onCallEstablished(Call call) {
			System.out.println("hhhhhhhhhCall established");
			mAudioPlayer.stopProgressTone();
			mCallState.setText(call.getState().toString());
			setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
			mCallStart = System.currentTimeMillis();
			ClippedService.mSinchClient.isStarted();

		}

		@Override
		public void onCallProgressing(Call call) {
			System.out.println("hhhhhhhhhhCall progressing");
			mAudioPlayer.playProgressTone();
		}

		@Override
		public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
			// Send a push through your push provider here, e.g. GCM
		}
	}
}
