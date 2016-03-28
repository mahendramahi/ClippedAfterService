package org.ninehertzindia.clipped;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.ninehertzindia.clipped.database.DatabaseHandler;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.ContactData;
import org.ninehertzindia.clipped.util.ContactVO;
import org.ninehertzindia.clipped.util.HistoryVO;

import com.google.android.gms.internal.el;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

@SuppressLint("Wakelock")
public class IncomingCallScreenActivity extends Activity {

	static final String TAG = IncomingCallScreenActivity.class.getSimpleName();
	private String mCallId;
	private AudioPlayer mAudioPlayer;
	private WakeLock wakeLock;
	private Call call;
	public static ContactVO contactVO;
	public static HistoryVO historyVO;
	public static ContactData contactData;
	private ImageView incomingprofileImage;
	private TextView text;
	private VideoView videoplayback;
	private int b;
	public DatabaseHandler db = new DatabaseHandler(this);
	public KeyguardManager.KeyguardLock kl;
	TextView remoteUser;
	MediaPlayer mediaPlayer;
	public DateFormat timeFormate, dateFormate;
	Date date1, date2;
	private String CallTime, CallDate;
	int i = 0;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		KeyguardManager km = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
		kl = km.newKeyguardLock("MyKeyguardLock");
		kl.disableKeyguard();

		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(
				PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
				"MyWakeLock");
		wakeLock.acquire();

		setContentView(R.layout.incoming);

		timeFormate = new SimpleDateFormat("dd/MM/yyyy");
		dateFormate = new SimpleDateFormat("hh:mm aa");

		date1 = new Date();
		date2 = new Date();

		CallTime = "" + timeFormate.format(date1);
		CallDate = "" + dateFormate.format(date2);

		System.out.println("hhhhhhhhhhhhh Time :" + CallTime + ":Date:" + CallDate);

		System.out.println("hhhhhh  " + db.getContactVOsCount());
		System.out.println("hhhhhhdata  " + db.getContactDatasCount());

		ImageView answer = (ImageView) findViewById(R.id.answerButton);
		answer.setOnClickListener(mClickListener);
		ImageView decline = (ImageView) findViewById(R.id.declineButton);

		incomingprofileImage = (ImageView) findViewById(R.id.incommingimageprofile);
		text = (TextView) findViewById(R.id.text);
		text.setVisibility(View.GONE);
		videoplayback = (VideoView) findViewById(R.id.videoplayback);
		videoplayback.setVisibility(View.GONE);

		decline.setOnClickListener(mClickListener);

		mCallId = getIntent().getStringExtra(ClippedService.CALL_ID);

		///

		Call call = ClippedService.mSinchClient.getCallClient().getCall(mCallId);
		if (call != null) {
			call.addCallListener(new SinchCallListener());
			remoteUser = (TextView) findViewById(R.id.remoteUser);
			try {
				System.out.println("hhh put value" + call.getRemoteUserId().toString());

				contactVO = db.getContactVO(call.getRemoteUserId().toString());
				System.out.println("incoming name" + contactVO.getContactName());
				System.out.println("incoming name" + contactVO.get_id());
				System.out.println("incoming name" + contactVO.getMobile());
				System.out.println("incoming url" + contactVO.getContactDataUrl());
				System.out.println("incoming type" + contactVO.getContactDataType());
				System.out.println("incoming name" + db.getContactVO(call.getRemoteUserId().toString()));
				System.out.println("incoming But Histdetail" + db.getHistoryVO(call.getRemoteUserId().toString()));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				contactData = db.getContactData(call.getRemoteUserId().toString());
				System.out.println("incomingData name" + contactData.getContactName());
				System.out.println("incomingData name" + contactData.get_id());
				System.out.println("incomingData name" + contactData.getMobile());
				System.out.println("incomingData url" + contactData.getContactDataUrl());
				System.out.println("incomingData type" + contactData.getContactDataType());
				System.out.println("incomingData name" + db.getContactVO(call.getRemoteUserId().toString()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {

				if (contactVO == null) {

					HistoryVO historyVO = new HistoryVO(2, "" + call.getRemoteUserId(),
							"" + call.getRemoteUserId().toString(), "", CallDate, CallTime, "incoming call");
					db.addHistoryVO(historyVO);
				}

				else {
					HistoryVO historyVO = new HistoryVO(2, "" + contactVO.getContactName(),
							"" + call.getRemoteUserId().toString(), "" + contactVO.getContactImageUri(), CallDate,
							CallTime, "incoming call");
					db.addHistoryVO(historyVO);

				}
				System.out.println("hhhhhhhhhh entry" + contactVO);
				System.out.println(
						"hhhhhhhhhh entry" + db.getContactVO(call.getRemoteUserId().toString()).getContactName());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				if (contactVO != null) {

					remoteUser.setText("" + contactVO.getContactName());
				} else {

					remoteUser.setText("" + call.getRemoteUserId().toString());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Log.e(TAG, "Started with invalid callId, aborting");
			finish();
		}
		b = db.getContactDataStatus(call.getRemoteUserId().toString());

		System.out.println("hhhhhhhhhhh Value of b :" + b);
		if (b == 0) {
			System.out.println("hhhhhhhhhhh Normal Value of b is Zero");

			if (CommonMethods.getPreferences(IncomingCallScreenActivity.this, "mycliptype").equalsIgnoreCase("1")) {

				mediaPlayer = new MediaPlayer();

				try {
					System.out.println("hhhhhh url set to media :" + CommonMethods.getPreferences(this, "myclipurl"));
					mediaPlayer
							.setDataSource(CommonMethods.getPreferences(IncomingCallScreenActivity.this, "myclipurl"));

					mediaPlayer.prepare();
					mediaPlayer.setLooping(true);
					mediaPlayer.start();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (CommonMethods.getPreferences(this, "mycliptype").equalsIgnoreCase("2")) {

				System.out.println("hhhhhhhhhhh Video File Check");
				incomingprofileImage.setVisibility(View.GONE);

				videoplayback.setVisibility(View.VISIBLE);

				String uriPath = CommonMethods.getPreferences(this, "myclipurl");// contactData.getContactDataUrl();
				Uri uri = Uri.parse(uriPath);
				videoplayback.setVideoURI(uri);
				videoplayback.setOnPreparedListener(new OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {

						mp.setLooping(true);

					}
				});

				videoplayback.getKeepScreenOn();
				// videoplayback.set
				videoplayback.requestFocus();

				videoplayback.start();

			} else {
				mAudioPlayer = new AudioPlayer(this);
				mAudioPlayer.playRingtone();
			}

		} else {
			System.out.println("hhhhhhhhhhh Not Normal ");

			System.out.println("hhhhhhhhhhh File Check" + contactData.getContactDataType());

			if (contactData.getContactDataType() == 1) {
				System.out.println("hhhhhhhhhhh Audio File Check");

				// Picasso.with(getApplicationContext()).load(contactVO.getContactImageUri()).into(incomingprofileImage);

				File file = new File(contactData.getContactDataUrl());
				if (file.exists()) {
					mediaPlayer = new MediaPlayer();

					try {
						System.out.println("hhhhhh url set to media :" + contactData.getContactDataUrl());
						mediaPlayer.setDataSource(contactData.getContactDataUrl());
						mediaPlayer.prepare();
						mediaPlayer.setLooping(true);
						mediaPlayer.start();
					} catch

					(Exception e) {
						
						mAudioPlayer = new AudioPlayer(this);
						mAudioPlayer.playRingtone();
						//e.printStackTrace();
					}
				} else {
					mAudioPlayer = new AudioPlayer(this);
					mAudioPlayer.playRingtone();
				}

			}
			if (contactData.getContactDataType() == 2) {
				System.out.println("hhhhhhhhhhh Video File Check");
				incomingprofileImage.setVisibility(View.GONE);

				File file = new File(contactData.getContactDataUrl());
				if (file.exists()) {
					videoplayback.setVisibility(View.VISIBLE);

					try {
						String uriPath = contactData.getContactDataUrl();
						Uri uri = Uri.parse(uriPath);
						videoplayback.setVideoURI(uri);
						videoplayback.setOnPreparedListener(new OnPreparedListener() {

							@Override
							public void onPrepared(MediaPlayer mp) {

								mp.setLooping(true);

							}
						});

						videoplayback.getKeepScreenOn();
						// videoplayback.set
						videoplayback.requestFocus();

						videoplayback.start();
					} catch (Exception e) {
						mAudioPlayer = new AudioPlayer(this);
						mAudioPlayer.playRingtone();
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					mAudioPlayer = new AudioPlayer(this);
					mAudioPlayer.playRingtone();

				}

			}

			if (contactData.getContactDataType() == 3) {
				System.out.println("hhhh data type 3 hai ");

				mAudioPlayer = new AudioPlayer(this);
				mAudioPlayer.playRingtone();

				incomingprofileImage.setVisibility(View.GONE);
				text.setVisibility(View.VISIBLE);
				text.setText(contactData.getContactDataUrl().toString());

			}

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		declineClicked();
		super.onBackPressed();
		IncomingCallScreenActivity.this.finish();
	}

	@SuppressLint("NewApi")
	private void answerClicked() {

		if (b == 0) {
			try {
				if (CommonMethods.getPreferences(this, "mycliptype").equalsIgnoreCase("1")) {

					try {
						if (mediaPlayer != null) {

							mediaPlayer.stop();
							mediaPlayer.release();
							mediaPlayer = null;
						}
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}

				} else if (CommonMethods.getPreferences(this, "mycliptype").equalsIgnoreCase("2")) {

					videoplayback.stopPlayback();

				} else {

					mAudioPlayer.stopRingtone();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			if (contactData.getContactDataType() == 1) {
				File file = new File(contactData.getContactDataUrl());
				if (file.exists()) {
					try {
						if (mediaPlayer != null) {
							mediaPlayer.stop();
							mediaPlayer.release();
							mediaPlayer = null;
						}
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						mAudioPlayer.stopRingtone();
						e.printStackTrace();
					}
				} else {
					mAudioPlayer.stopRingtone();
				}

			}
			if (contactData.getContactDataType() == 2) {
				File file = new File(contactData.getContactDataUrl());
				if (file.exists()) {
					videoplayback.stopPlayback();
				} else {
					mAudioPlayer.stopRingtone();
				}
			}

			if (contactData.getContactDataType() == 3) {
				try {
					mAudioPlayer.stopRingtone();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		/*
		 * if (contactVO.getContactDataType() == 0) {
		 * 
		 * try { mAudioPlayer.stopRingtone(); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * } if (contactData.getContactDataType() == 1) { try { if (mediaPlayer
		 * != null) {
		 * 
		 * mediaPlayer.stop(); mediaPlayer.release(); mediaPlayer = null; } }
		 * catch (IllegalStateException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * } if (contactVO.getContactDataType() == 2) {
		 * 
		 * } if (contactVO.getContactDataType() == 3) { try {
		 * mAudioPlayer.stopRingtone(); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 */

		call = ClippedService.mSinchClient.getCallClient().getCall(mCallId);
		call.addCallListener(new SinchCallListener());
		if (call != null) {
			call.answer();
			Intent intent = new Intent(this, CallScreenActivity.class);
			intent.putExtra(ClippedService.CALL_ID, mCallId);
			try {
				intent.putExtra("localname", contactVO.getContactName());
				intent.putExtra("image", contactVO.getContactImageUri());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			startActivity(intent);
		} else {
			finish();
		}
	}

	private void declineClicked() {
		if (b == 0) {
			try {
				if (CommonMethods.getPreferences(this, "mycliptype").equalsIgnoreCase("1")) {

					try {
						if (mediaPlayer != null) {

							mediaPlayer.stop();
							mediaPlayer.release();
							mediaPlayer = null;
						}
					} catch (IllegalStateException e) {

						// TODO Auto-generated catch block

						e.printStackTrace();
					}

				} else if (CommonMethods.getPreferences(this, "mycliptype").equalsIgnoreCase("2")) {

					videoplayback.stopPlayback();

				} else {
					mAudioPlayer.stopRingtone();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			if (contactData.getContactDataType() == 1) {
				File file = new File(contactData.getContactDataUrl());
				if (file.exists()) {
					try {
						if (mediaPlayer != null) {
							mediaPlayer.stop();
							mediaPlayer.release();
							mediaPlayer = null;
						}
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					mAudioPlayer.stopRingtone();
				}

			}
			if (contactData.getContactDataType() == 2) {
				File file = new File(contactData.getContactDataUrl());
				if (file.exists()) {
					videoplayback.stopPlayback();
				} else {
					mAudioPlayer.stopRingtone();
				}
			}

			if (contactData.getContactDataType() == 3) {
				try {
					mAudioPlayer.stopRingtone();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		Call call = ClippedService.mSinchClient.getCallClient().getCall(mCallId);
		if (call != null) {
			call.hangup();
		}

		kl.reenableKeyguard();

		IncomingCallScreenActivity.this.finish();

	}

	private class SinchCallListener implements CallListener {

		@Override
		public void onCallEnded(Call call) {
			CallEndCause cause = call.getDetails().getEndCause();
			Log.d(TAG, "Call ended, cause: " + cause.toString());

			if (b == 0) {
				try {
					if (CommonMethods.getPreferences(IncomingCallScreenActivity.this, "mycliptype")
							.equalsIgnoreCase("1")) {

						try {
							if (mediaPlayer != null) {

								mediaPlayer.stop();
								mediaPlayer.release();
								mediaPlayer = null;
							}
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else if (CommonMethods.getPreferences(IncomingCallScreenActivity.this, "mycliptype")
							.equalsIgnoreCase("2")) {

						videoplayback.stopPlayback();

					} else {
						mAudioPlayer.stopRingtone();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				if (contactData.getContactDataType() == 1) {
					File file = new File(contactData.getContactDataUrl());
					if (file.exists()) {
						try {
							if (mediaPlayer != null) {
								mediaPlayer.stop();
								mediaPlayer.release();
								mediaPlayer = null;
							}
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						mAudioPlayer.stopRingtone();
					}

				}
				if (contactData.getContactDataType() == 2) {
					File file = new File(contactData.getContactDataUrl());
					if (file.exists()) {
						videoplayback.stopPlayback();
					} else {
						mAudioPlayer.stopRingtone();
					}
				}

				if (contactData.getContactDataType() == 3) {
					try {
						mAudioPlayer.stopRingtone();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

			IncomingCallScreenActivity.this.finish();
		}

		@Override
		public void onCallEstablished(Call call) {

			// ClippedService.mSinchClient.isStarted();
			System.out.println("hhhhhhhhhhhCall established");
		}

		@Override
		public void onCallProgressing(Call call) {
			System.out.println("hhhhhhhhhCall progressing");
		}

		@Override
		public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
			// Send a push through your push provider here, e.g. GCM
		}
	}

	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.answerButton:
				System.out.println("hhhh ans Call");
				answerClicked();
				break;
			case R.id.declineButton:
				System.out.println("hhhh decline Call");
				declineClicked();
				break;
			}
		}
	};

}
