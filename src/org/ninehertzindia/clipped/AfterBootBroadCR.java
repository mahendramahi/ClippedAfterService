package org.ninehertzindia.clipped;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AfterBootBroadCR extends BroadcastReceiver{

	
	@Override
	public void onReceive(final Context context, Intent intent) {
		
		
		nr();
		 if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			 
			 System.out.println("hh===onboot complete");
		        Intent serviceLauncher = new Intent(context, ClippedService.class);
		        context.startService(serviceLauncher);
		        Log.v("TEST", "Service loaded at start");
		 
		 }
		 
	    }
public void nr(){
	Log.d("TAG", "receive cal hua");
}
}
