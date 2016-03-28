package org.ninehertzindia.clipped;

import org.ninehertzindia.clipped.fragment.RegistrationOtpFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	public MediaPlayer mp = null;

	@Override
	public void onReceive(Context c, Intent intent) {

		System.out.println("hhhh on rcivie call hua");

		Bundle bundle = intent.getExtras();

		Object[] messages = (Object[]) bundle.get("pdus");
		SmsMessage[] sms = new SmsMessage[messages.length];

		try {
			for (int n = 0; n < messages.length; n++) {
				sms[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			for (SmsMessage msg : sms) {
				// if(msg.getMessageBody().equals("SECUREDEVICE")){
				System.out.println("hhhhhhequal hua" + msg.getMessageBody().toString());
				String s = msg.getMessageBody().toString();

			//	if (s.length() > 45) {

					String temp_s = s.substring(0, 25);
					System.out.println("hhhhh temp:"+temp_s);
					if (temp_s.equalsIgnoreCase("Your confirmation code is")) {

						String lol = temp_s = s.substring(26, 30);
						System.out.println("hhhh main code:" + lol);
						
						try {
							RegistrationOtpFragment.txtOtp.setText(lol);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
			//	}

				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}