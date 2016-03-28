package org.ninehertzindia.clipped.util;

/**
 * this class is used for defining all global constants
 * 
 * @author Pramit Chaturvedi
 * 
 */
public class ApplicationConstants {

	public static final String BP_SIGNATURE_SUFFIX = "clipped!@s5";
	public static final String USER_ID = "userid";

	/**
	 * This interface is used for handling constants of web services
	 * 
	 * @author Pramit Chaturvedi
	 * 
	 */
	public interface TaskType {
		public static final int VALIDATE_OTP_TASK = 1001;
		public static final int RESEND_CODE_TASK = 1002;
		public static final int GETPAYPALCHARGE = 1007;
		public static final int SETPAYPALCHARGE = 1008;
		public static final int REGISTRSTION = 1014;
		public static final int editProfile = 1015;

	}

}