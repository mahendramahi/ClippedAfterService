package org.ninehertzindia.clipped.util;

public class InternalExternalFileUtils {
	

	public static boolean isSDCARD()
	{
		
		boolean isSDPresent =  android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		
		return isSDPresent; 
		
	}
	
	
	
}
