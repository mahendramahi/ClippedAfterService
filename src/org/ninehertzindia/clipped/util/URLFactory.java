package org.ninehertzindia.clipped.util;

/**
 * class is used for managing URLs for each and every service
 * 
 * @author Pramit Chaturvedi
 * 
 */
public class URLFactory {

	//public static String baseUrl = "http://demo2server.com/sites/mobile/clipped/";
	
// Client Base Url	
	//public static String baseUrl = "http://iclipped.net/";

	//public static String baseurllocal = "http://projectmanager/clipped/";

	// public static String baseUrl = "http://192.168.1.151/clipped/";

	public static String setRingToneUrl() {

		return baseUrl + "services/ws-user.php?type=SETRINGTON";

	}

	public static String CheckSettone() {

		return baseUrl + "services/ws-setting.php?type=CHECKSETTON";

	}

	public static String getSignUpUrl() {

		return baseUrl + "services/ws-user.php?type=SIGNUP";

	}

	public static String getEditProfileUrl() {

		return baseUrl + "services/ws-user.php?type=EDITPROFILE";

	}

	public static String PaymentUrl() {

		return baseUrl + "services/ws-payment.php?type=PLANPAYMENT";

	}

	public static String getVerifyOtpUrl() {
		return baseUrl + "services/ws-user.php?type=OTPVERIFIED";
	}

	public static String changeMobileUrl() {
		return baseUrl + "services/ws-user.php?type=EDITMOBILENUMBER";
	}

	public static String deactivateUrl() {
		return baseUrl + "services/ws-user.php?type=DELETEUSER";
	}

	public static String getContactUrl() {
		return baseUrl + "services/ws-user.php?type=CONTACTSEARCH";
	}

	public static String PaymentProofUrl() {
		return baseUrl + "ws-user.php?type=ADDPAYMENT";
	}

	public static String GetPaypalUrl() {
		return baseUrl + "ws-user.php?type=GETCHARGE";
	}

	public static String changeMobileNumUrl() {
		return baseUrl + "ws-user.php?type=CHANGEMOBILE";
	}

	public static String changeAssignedSettingUrl() {
		return baseUrl + "services/ws-setting.php?type=SETTON";
	}
}
