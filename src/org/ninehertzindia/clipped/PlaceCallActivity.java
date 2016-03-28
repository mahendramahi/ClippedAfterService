package org.ninehertzindia.clipped;

import org.ninehertzindia.clipped.util.CommonMethods;

import com.sinch.android.rtc.calling.Call;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceCallActivity extends Activity {

    private Button mCallButton;
    private EditText mCallName;
    private Call call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCallName = (EditText) findViewById(R.id.callName);
        mCallButton = (Button) findViewById(R.id.callButton);
        mCallButton.setEnabled(false);
        mCallButton.setOnClickListener(buttonClickListener);

        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(buttonClickListener);
        
        TextView userName = (TextView) findViewById(R.id.loggedInName);
        userName.setText(CommonMethods.getPreferences(getApplicationContext(), "name"));//getSinchServiceInterface().getUserName());
        mCallButton.setEnabled(true);
    }

//    @Override
//    protected void onServiceConnected() {
//        TextView userName = (TextView) findViewById(R.id.loggedInName);
//        //userName.setText(getSinchServiceInterface().getUserName());
//        mCallButton.setEnabled(true);
//    }

    @Override
    public void onDestroy() {
//        if (getSinchServiceInterface() != null) {
//            getSinchServiceInterface().stopClient();
//            
//        }
//    	call = null;
//    	ClippedService.mSinchClient.terminate();
        super.onDestroy();
    }

    private void stopButtonClicked() {
      //  if (getSinchServiceInterface() != null) {
          //  getSinchServiceInterface().stopClient();
    	
    	//call = null;
            ClippedService.mSinchClient.terminate();
           // mSinchClient.terminate();
            ClippedService.mSinchClient = null;
            
            this.stopService(new Intent(getApplicationContext(),ClippedService.class));
            
            
            this.startService(new Intent(getApplicationContext(),ClippedService.class));
            
            
            
        //}
        finish();
    }

    private void callButtonClicked() {
        String userName = mCallName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        
       call = ClippedService.mSinchClient.getCallClient().callUser(userName);
        //getSinchServiceInterface().callUser(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(ClippedService.CALL_ID, callId);
        startActivity(callScreen);
    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.callButton:
                    callButtonClicked();
                    break;

                case R.id.stopButton:
                    stopButtonClicked();
                    break;

            }
        }
    };
}
