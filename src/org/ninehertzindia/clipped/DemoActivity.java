package org.ninehertzindia.clipped;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;

public class DemoActivity extends FragmentActivity implements ServiceConnection {
	private SinchService.SinchServiceInterface mSinchServiceInterface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getApplicationContext().bindService(new Intent(this, SinchService.class), this, BIND_AUTO_CREATE);
	}

	@Override
	public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
		if (SinchService.class.getName().equals(componentName.getClassName())) {
			System.out.println("-------step1");
			mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
			onServiceConnected();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName componentName) {
		if (SinchService.class.getName().equals(componentName.getClassName())) {
			System.out.println("-------step2");
			mSinchServiceInterface = null;
			onServiceDisconnected();
		}
	}

	protected void onServiceConnected() {
		// for subclasses
	}

	protected void onServiceDisconnected() {
		// for subclasses

	}

	public SinchService.SinchServiceInterface getSinchServiceInterface() {
		System.out.println("-------step3");
		return mSinchServiceInterface;
	}

}
