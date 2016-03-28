package org.ninehertzindia.clipped.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ninehertzindia.clipped.network.ConnectionProvider;

import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class CommonMethods {

	private static Activity context;
	Object listener;

	public CommonMethods(Activity mActivity, Object listener) {
		this.context = mActivity;
		this.listener = listener;
	}

	/********************* set shared preferences **************************/
	public static void SetPreferences(Context con, String key, String value) {
		// save the data
		SharedPreferences preferences = con.getSharedPreferences("prefs_login", 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/****************** get shared preferences *******************/
	public static String getPreferences(Context con, String key) {
		SharedPreferences sharedPreferences = con.getSharedPreferences("prefs_login", 0);
		String value = sharedPreferences.getString(key, "");
		return value;

	}

	/**********************
	 * set shared preferences in int
	 *********************************/
	public static void SetPreferencesInteger(Context con, String key, int value) {
		// save the data
		SharedPreferences preferences = con.getSharedPreferences("prefs_login", 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/*********************
	 * get shared preferences in int
	 ***********************/
	public static int getPreferencesInteger(Context con, String key) {
		SharedPreferences sharedPreferences = con.getSharedPreferences("prefs_login", 0);
		int value = sharedPreferences.getInt(key, 0);
		return value;

	}

	// /**
	// * method used for getting the request parameters for the Registration web
	// * service with the given parameter
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete Registration json request parameter
	// */
	//
	// public String getRegistrationRequestParmas(String country_code,
	// String mobile_number) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(
	// ApplicationConstants.RegistrationAPIKeys.COUNTRY_CODE_KEY,
	// country_code);
	// rootJsonObj.put(
	// ApplicationConstants.RegistrationAPIKeys.MOBILE_NUMBER_KEY,
	// mobile_number);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for chnage mobile number
	// * web service with the given parameter
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete Registration json request parameter
	// */
	// public String getChangeMobileRequestParmas(String userid, String old_num,
	// String new_num) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj
	// .put(ApplicationConstants.ChangeMobileAPIKeys.USERID_KEY,
	// userid);
	// rootJsonObj
	// .put(ApplicationConstants.ChangeMobileAPIKeys.OLD_MOBILE_NUMBER_KEY,
	// old_num);
	// rootJsonObj
	// .put(ApplicationConstants.ChangeMobileAPIKeys.NEW_MOBILE_NUMBER_KEY,
	// new_num);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for chnage password web
	// * service with the given parameter
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete Registration json request parameter
	// */
	// public String getChangePasswordRequestParmas(String userid,
	// String old_password, String new_password) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(
	// ApplicationConstants.ChangePasswordAPIKeys.USERID_KEY,
	// userid);
	// rootJsonObj
	// .put(ApplicationConstants.ChangePasswordAPIKeys.OLD_PASSWORD_KEY,
	// old_password);
	// rootJsonObj
	// .put(ApplicationConstants.ChangePasswordAPIKeys.NEW_PASSWORD_KEY,
	// new_password);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for the Registration web
	// * service with the given parameter
	// *
	// * @param registrationId
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete Registration json request parameter
	// */
	// public String getSetPasswordRequestParmas(String code, String number,
	// String password, String registrationId) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(
	// ApplicationConstants.SetPasswordAPIKeys.COUNTRY_CODE_KEY,
	// code);
	// rootJsonObj.put(
	// ApplicationConstants.SetPasswordAPIKeys.MOBILE_NUMBER_KEY,
	// number);
	// rootJsonObj.put(
	// ApplicationConstants.SetPasswordAPIKeys.PASSWORD_KEY,
	// password);
	// rootJsonObj.put(
	// ApplicationConstants.SetPasswordAPIKeys.DEVICE_TOKEN_KEY,
	// registrationId);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for the Registration web
	// * service with the given parameter
	// *
	// * @param registrationId
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete Registration json request parameter
	// */
	// public String getSetLoginRequestParmas(String userid, String password,
	// String registrationId) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(ApplicationConstants.SetLoginAPIKeys.USERID_KEY,
	// userid);
	// rootJsonObj.put(
	// ApplicationConstants.SetPasswordAPIKeys.PASSWORD_KEY,
	// password);
	// rootJsonObj.put(
	// ApplicationConstants.SetPasswordAPIKeys.DEVICE_TOKEN_KEY,
	// registrationId);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for the Registration web
	// * service with the given parameter
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete Registration json request parameter
	// */
	// public String getSettingRequestParmas(String userid) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(ApplicationConstants.SetLoginAPIKeys.USERID_KEY,
	// userid);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for the Registration web
	// * service with the given parameter
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete Registration json request parameter
	// */
	// public String getLogoutRequestParmas(String userid) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(ApplicationConstants.SetLogoutAPIKeys.USERID_KEY,
	// userid);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for the Registration web
	// * service with the given parameter
	// *
	// * @param str_time
	// * @param str_msg
	// * @param str_request_id
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete Registration json request parameter
	// */
	// public String setUpdateAirtimeParmas(String userid, String
	// str_request_id,
	// String str_msg, String str_time) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(
	// ApplicationConstants.SetUpdateAirtimeAPIKeys.USERID_KEY,
	// userid);
	// rootJsonObj
	// .put(ApplicationConstants.SetUpdateAirtimeAPIKeys.REQUEST_ID_KEY,
	// str_request_id);
	// rootJsonObj.put(
	// ApplicationConstants.SetUpdateAirtimeAPIKeys.MESSAGE_KEY,
	// str_msg);
	// rootJsonObj.put(
	// ApplicationConstants.SetUpdateAirtimeAPIKeys.TIME_KEY,
	// str_time);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for the airtime request
	// * web service with the given parameter
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete Registration json request parameter
	// */
	// public String getAirtimeRequestParmas(String userid, String airtime_type)
	// {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(ApplicationConstants.GetAirtimeAPIKeys.USERID_KEY,
	// userid);
	// rootJsonObj.put(
	// ApplicationConstants.GetAirtimeAPIKeys.AIRTIME_TYPE_KEY,
	// airtime_type);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for the airtime request
	// * web service with the given parameter
	// *
	// * @param status
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete Registration json request parameter
	// */
	// public String setAirtimeStatusParmas(String userid, String request_id,
	// String status) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(
	// ApplicationConstants.SetAirtimeStatusAPIKeys.USERID_KEY,
	// userid);
	// rootJsonObj
	// .put(ApplicationConstants.SetAirtimeStatusAPIKeys.REQUEST_ID_KEY,
	// request_id);
	// rootJsonObj.put(
	// ApplicationConstants.SetAirtimeStatusAPIKeys.STATUS_KEY,
	// status);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for the payment no ads
	// * version web service with the given parameter
	// *
	// * @param userid
	// * @param payment_id
	// * @param amount
	// *
	// * @return The complete Registration json request parameter
	// */
	// public String getPaymentForNoAdsRequestParmas(String userid,
	// String payment_id, String amount) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(
	// ApplicationConstants.SetPaymentProofAPIKeys.USERID_KEY,
	// userid);
	// rootJsonObj.put(
	// ApplicationConstants.SetPaymentProofAPIKeys.PAYMENT_ID_KEY,
	// payment_id);
	// rootJsonObj.put(
	// ApplicationConstants.SetPaymentProofAPIKeys.AMOUNT_KEY,
	// amount);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for the set setting web
	// * service with the given parameter
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete set setting json request parameter
	// */
	// public String setSettingRequestParmas(String userid,
	// String notification_status) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(ApplicationConstants.SetSettingAPIKeys.USERID_KEY,
	// userid);
	// rootJsonObj
	// .put(ApplicationConstants.SetSettingAPIKeys.NOTIFICATION_STATUS_KEY,
	// notification_status);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for the No ads version
	// app
	// * web service with the given parameter
	// *
	// * @param userid
	// * @param code
	// *
	// * @return The complete set setting json request parameter
	// */
	// public String setNoAdsVersionRequestParmas(String userid,
	// String show_ad_status) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(ApplicationConstants.ShowAdAPIKeys.USERID_KEY,
	// userid);
	// rootJsonObj.put(
	// ApplicationConstants.ShowAdAPIKeys.SHOW_AD_STATUS_KEY,
	// show_ad_status);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	// /**
	// * method used for getting the request parameters for the Registration web
	// * service with the given parameter
	// *
	// * @param country_code
	// * @param mobile_number
	// *
	// * @return The complete Registration json request parameter
	// */
	// public String getVerifyOtpRequestParmas(String Otp, String country_code,
	// String mobile_number) {
	//
	// JSONArray rootJsonArr = null;
	// try {
	// rootJsonArr = new JSONArray();
	// JSONObject rootJsonObj = new JSONObject();
	// rootJsonObj.put(ApplicationConstants.VerifyOtpAPIKeys.OTP_KEY, Otp);
	// rootJsonObj.put(
	// ApplicationConstants.VerifyOtpAPIKeys.COUNTRY_CODE_KEY,
	// country_code);
	// rootJsonObj.put(
	// ApplicationConstants.VerifyOtpAPIKeys.MOBILE_NUMBER_KEY,
	// mobile_number);
	// rootJsonArr.put(rootJsonObj);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return rootJsonArr.toString();
	// }
	//
	//
	//
	// /**
	// * method used for Registration user with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void registrationUser(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	// al_signature_strings
	// .add(ApplicationConstants.RegistrationAPIKeys.API_TYPE);
	// al_signature_strings
	// .add(ApplicationConstants.RegistrationAPIKeys.COUNTRY_CODE_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.RegistrationAPIKeys.MOBILE_NUMBER_KEY);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.getSignUpUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, true);
	// }
	//
	// /**
	// * method used for change mobile number user with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void changeMobile(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	// al_signature_strings
	// .add(ApplicationConstants.ChangeMobileAPIKeys.API_TYPE);
	// al_signature_strings
	// .add(ApplicationConstants.ChangeMobileAPIKeys.USERID_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.ChangeMobileAPIKeys.OLD_MOBILE_NUMBER_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.ChangeMobileAPIKeys.NEW_MOBILE_NUMBER_KEY);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.changeMobileNumUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, true);
	// }
	//
	// /**
	// * method used for change mobile number user with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void changePassword(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	// al_signature_strings
	// .add(ApplicationConstants.ChangePasswordAPIKeys.API_TYPE);
	// al_signature_strings
	// .add(ApplicationConstants.ChangePasswordAPIKeys.USERID_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.ChangePasswordAPIKeys.OLD_PASSWORD_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.ChangePasswordAPIKeys.NEW_PASSWORD_KEY);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.changePasswordUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, true);
	// }
	//
	// /**
	// * method used for set Password with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void setPassword(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetPasswordAPIKeys.COUNTRY_CODE_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.SetPasswordAPIKeys.MOBILE_NUMBER_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.SetPasswordAPIKeys.PASSWORD_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.SetPasswordAPIKeys.API_TYPE);
	// al_signature_strings
	// .add(ApplicationConstants.SetPasswordAPIKeys.DEVICE_TOKEN_KEY);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.setPasswordUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, true);
	// }
	//
	// /**
	// * method used for Login with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void Login(String requestParams) throws
	// UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetLoginAPIKeys.USERID_KEY);
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetLoginAPIKeys.PASSWORD_KEY);
	// al_signature_strings.add(ApplicationConstants.SetLoginAPIKeys.API_TYPE);
	// al_signature_strings
	// .add(ApplicationConstants.SetLoginAPIKeys.DEVICE_TOKEN_KEY);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.LoginUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, true);
	// }
	//
	// /**
	// * method used for Login with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void GetSetting(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings
	// .add(ApplicationConstants.GetSettingAPIKeys.USERID_KEY);
	//
	// al_signature_strings
	// .add(ApplicationConstants.GetSettingAPIKeys.API_TYPE);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.SettingUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, true);
	// }
	//
	// /**
	// * method used for logout with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void GetLogout(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetLogoutAPIKeys.USERID_KEY);
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetLogoutAPIKeys.API_TYPE);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.LogoutUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, true);
	// }
	//
	// /**
	// * method used for update airtime with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void SetUpdateAirtime(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetUpdateAirtimeAPIKeys.API_TYPE);
	// al_signature_strings
	// .add(ApplicationConstants.SetUpdateAirtimeAPIKeys.USERID_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.SetUpdateAirtimeAPIKeys.MESSAGE_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.SetUpdateAirtimeAPIKeys.REQUEST_ID_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.SetUpdateAirtimeAPIKeys.TIME_KEY);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(
	// context,
	// URLFactory.SetUpdateAirtimeUrl() + "&signature="
	// + str_signature + "&data="
	// + URLEncoder.encode(requestParams, "UTF-8"), null,
	// listener, true);
	// }
	//
	// /**
	// * method used for get airtime with the given parameter
	// *
	// * @param requestParams
	// * @param b
	// * @throws UnsupportedEncodingException
	// */
	// public void GetAirtimeRequst(String requestParams, boolean b)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings
	// .add(ApplicationConstants.GetAirtimeAPIKeys.USERID_KEY);
	//
	// al_signature_strings
	// .add(ApplicationConstants.GetAirtimeAPIKeys.API_TYPE);
	//
	// al_signature_strings
	// .add(ApplicationConstants.GetAirtimeAPIKeys.AIRTIME_TYPE_KEY);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.GetAirtimeUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, b);
	// }
	//
	// /**
	// * method used for get airtime with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void SetAirtimeStatus(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetAirtimeStatusAPIKeys.USERID_KEY);
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetAirtimeStatusAPIKeys.API_TYPE);
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetAirtimeStatusAPIKeys.STATUS_KEY);
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetAirtimeStatusAPIKeys.REQUEST_ID_KEY);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(
	// context,
	// URLFactory.SetAirtimeStatusUrl() + "&signature="
	// + str_signature + "&data="
	// + URLEncoder.encode(requestParams, "UTF-8"), null,
	// listener, true);
	// }
	//
	// /**
	// * method used for payment proof with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void GetPaymentProof(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetPaymentProofAPIKeys.USERID_KEY);
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetPaymentProofAPIKeys.API_TYPE);
	// al_signature_strings
	// .add(ApplicationConstants.SetPaymentProofAPIKeys.AMOUNT_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.SetPaymentProofAPIKeys.PAYMENT_ID_KEY);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.PaymentProofUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, true);
	// }
	//
	// /**
	// * method used for Login with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void GetPayPalCharge() throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings
	// .add(ApplicationConstants.GetPayPalChargeAPIKeys.API_TYPE);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.GetPaypalUrl() + "&signature=" + str_signature + "",
	// null, listener, true);
	// }
	//
	// /**
	// * method used for Login with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void GetCountryCodes() throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings.add(ApplicationConstants.GetCountryCodes.API_TYPE);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.GetCountryCodeUrl() + "&signature=" + str_signature
	// + "", null, listener, true);
	// }
	//
	// /**
	// * method used for set setting with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void SetSetting(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetSettingAPIKeys.USERID_KEY);
	//
	// al_signature_strings
	// .add(ApplicationConstants.SetSettingAPIKeys.API_TYPE);
	// al_signature_strings
	// .add(ApplicationConstants.SetSettingAPIKeys.NOTIFICATION_STATUS_KEY);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.SaveSettingUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, true);
	// }
	//
	// /**
	// * method used for no ads version with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void NoAdsversion(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings.add(ApplicationConstants.ShowAdAPIKeys.USERID_KEY);
	//
	// al_signature_strings.add(ApplicationConstants.ShowAdAPIKeys.API_TYPE);
	// al_signature_strings
	// .add(ApplicationConstants.ShowAdAPIKeys.SHOW_AD_STATUS_KEY);
	//
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.ShowAdsVersionUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, true);
	// }
	//
	// /**
	// * method used for Verify Otp with the given parameter
	// *
	// * @param requestParams
	// * @throws UnsupportedEncodingException
	// */
	// public void verifyOtp(String requestParams)
	// throws UnsupportedEncodingException {
	//
	// // create the arraylist for getting the signature
	// ArrayList<String> al_signature_strings = new ArrayList<String>();
	//
	// al_signature_strings
	// .add(ApplicationConstants.VerifyOtpAPIKeys.COUNTRY_CODE_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.VerifyOtpAPIKeys.MOBILE_NUMBER_KEY);
	// al_signature_strings.add(ApplicationConstants.VerifyOtpAPIKeys.OTP_KEY);
	// al_signature_strings
	// .add(ApplicationConstants.VerifyOtpAPIKeys.API_TYPE);
	// // calling a method for creating the signature for country list API
	// String str_signature = SignatureCommonMenthods
	// .getSignatureForAPI(al_signature_strings);
	//
	// BlupeyConnectionProvider blupeyConnectionProvider = new
	// BlupeyConnectionProvider();
	// blupeyConnectionProvider.doHttpGetRequest(context,
	// URLFactory.getVerifyOtpUrl() + "&signature=" + str_signature
	// + "&data=" + URLEncoder.encode(requestParams, "UTF-8"),
	// null, listener, true);
	// }

	/**
	 * This method is used for get the connectivity status
	 * 
	 * @return
	 */
	public boolean getConnectivityStatus() {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null)
			if (info.isConnected()) {
				return true;
			} else {
				return false;
			}
		else
			return false;
	}

	public static boolean getMahiConnectivityStatus() {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null)
			if (info.isConnected()) {
				return true;
			} else {
				return false;
			}
		else
			return false;
	}

	/**
	 * this method is used for getting error message from APIs
	 * 
	 * @param errorResponse
	 * @return errorMsg
	 */
	public String getErrorMessageFromAPI(String errorResponse) {
		String errorMsg = "";

		try {
			JSONObject rootJsonObj = new JSONObject(errorResponse);
			errorMsg = rootJsonObj.getString("message");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return errorMsg;
	}

	// public void runAdmob(AdView mAdView, AdView mAdView1) {
	//
	// AdRequest adRequest = new AdRequest.Builder().build();
	// mAdView.loadAd(adRequest);
	// mAdView1.loadAd(adRequest);
	// }

	/**
	 * Method is used for get string from input strem
	 * 
	 * @param is
	 * @return
	 */

	public String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	/**
	 * 
	 * method for check number is exist or not in contact list
	 * 
	 * @param name2
	 */

	public String contactExists(Context context, String number) {
		// / number is the phone number
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		String[] mPhoneNumberProjection = { PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
		Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
		try {
			if (cur.moveToFirst()) {
				String name = cur.getString(cur.getColumnIndex(PhoneLookup.DISPLAY_NAME));

				return name;
			}
		} finally {
			if (cur != null)
				cur.close();
		}
		return number;
	}

	public static void showSuspendDialog(final Activity activity) {
		new AlertDialog.Builder(activity).setMessage("You are suspended by admin.").setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
						activity.getSharedPreferences("prefs_login", 0).edit().putString("user_id", "").commit();
						activity.finish();
					}
				}).show();
	}

	public void showMessageDialog(Activity paramActivity, String paramString) {
		new AlertDialog.Builder(paramActivity).setMessage(paramString).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
						paramAnonymousDialogInterface.dismiss();
					}
				}).show();
	}

	public void hideKeyboard() {
		View view = context.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	//
	public RequestParams setRingtoneRequestParamas(String userid, String otheruserid, File file, String file_type,
			String text) {

		RequestParams requestParams = new RequestParams();

		requestParams.put("userid", userid);
		requestParams.put("otheruserid", otheruserid);
		try {
			requestParams.put("file", file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		requestParams.put("file_type", file_type);
		requestParams.put("text", text);

		return requestParams;
	}

	public void setPingtone(RequestParams requestParams) throws UnsupportedEncodingException {
		ArrayList<String> al_signature_strings = new ArrayList<String>();
		al_signature_strings.add("setrington");
		al_signature_strings.add("userid");
		al_signature_strings.add("otheruserid");
		al_signature_strings.add("file_type");
		al_signature_strings.add("text");

		String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);
		System.out.println("hhhhhSinature" + str_signature);
		ConnectionProvider connectionProvider = new ConnectionProvider();
		connectionProvider.doHttpPostRequestWithOutHeader(context,
				URLFactory.setRingToneUrl() + "&signature=" + str_signature, requestParams, listener, true, true);

	}

	public RequestParams getRegistrationRequestParmas(String countryZipCode, String str_number) {
		RequestParams requestParams = new RequestParams();

		requestParams.put("country_code", countryZipCode);
		requestParams.put("mobile", str_number);
		return requestParams;

	}

	public void registrationUser(RequestParams requestParams) throws UnsupportedEncodingException {
		ArrayList<String> al_signature_strings = new ArrayList<String>();
		al_signature_strings.add("signup");
		al_signature_strings.add("country_code");
		al_signature_strings.add("mobile");
		String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);
		ConnectionProvider connectionProvider = new ConnectionProvider();
		connectionProvider.doHttpPostRequestWithOutHeader(context,
				URLFactory.getSignUpUrl() + "&signature=" + str_signature, requestParams, listener, true, true);

	}

	public String getAssignedSettingRequestParmas(String userid, String status) {

		JSONArray rootJsonArr = null;
		try {
			rootJsonArr = new JSONArray();

			JSONObject rootJsonObj = new JSONObject();

			rootJsonObj.put("userid", userid);

			rootJsonObj.put("setton", status);

			rootJsonArr.put(rootJsonObj);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rootJsonArr.toString();
	}

	public void AssignedSetting(String settingRequestParmas) throws UnsupportedEncodingException {
		ArrayList<String> al_signature_strings = new ArrayList<String>();
		al_signature_strings.add("setton");

		al_signature_strings.add("userid");

		// al_signature_strings.add("setton");

		String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);
		Log.d("Response", "" + str_signature);
		ConnectionProvider connectionProvider = new ConnectionProvider();
		connectionProvider.doHttpGetRequest(context, URLFactory.changeAssignedSettingUrl() + "&signature="
				+ str_signature + "&data=" + URLEncoder.encode(settingRequestParmas, "UTF-8"), null, listener, false,
				false);
	}

	public String getPaymentRequestParmas(String userid, String transationId, String expire_time) {
		JSONArray rootJsonArr = null;
		try {
			rootJsonArr = new JSONArray();

			JSONObject rootJsonObj = new JSONObject();

			rootJsonObj.put("userid", userid);
			rootJsonObj.put("transation_id", transationId);
			rootJsonObj.put("expire_time", expire_time);

			rootJsonArr.put(rootJsonObj);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rootJsonArr.toString();
	}

	public void PaymentSetting(String settingRequestParmas) throws UnsupportedEncodingException {
		ArrayList<String> al_signature_strings = new ArrayList<String>();
		al_signature_strings.add("planpayment");
		// PLANPAYMENT

		al_signature_strings.add("userid");
		al_signature_strings.add("transation_id");
		al_signature_strings.add("expire_time");

		String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);
		Log.d("Response", "" + str_signature);
		ConnectionProvider connectionProvider = new ConnectionProvider();
		connectionProvider.doHttpGetRequest(context, URLFactory.PaymentUrl() + "&signature=" + str_signature + "&data="
				+ URLEncoder.encode(settingRequestParmas, "UTF-8"), null, listener, true, false);
	}

	public String getCheckSettoneRequestParmas(String userid, String touserid) {
		JSONArray rootJsonArr = null;
		try {
			rootJsonArr = new JSONArray();

			JSONObject rootJsonObj = new JSONObject();

			rootJsonObj.put("userid", userid);
			rootJsonObj.put("touserid", touserid);

			rootJsonArr.put(rootJsonObj);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rootJsonArr.toString();
	}

	public void CheckSettone(String settingRequestParmas) throws UnsupportedEncodingException {
		ArrayList<String> al_signature_strings = new ArrayList<String>();
		// CHECKSETTON

		al_signature_strings.add("checksetton");
		// PLANPAYMENT
		al_signature_strings.add("userid");
		al_signature_strings.add("touserid");

		String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);
		Log.d("hhhhh Response", "" + str_signature);
		ConnectionProvider connectionProvider = new ConnectionProvider();
		connectionProvider.doHttpGetRequest(context, URLFactory.CheckSettone() + "&signature=" + str_signature
				+ "&data=" + URLEncoder.encode(settingRequestParmas, "UTF-8"), null, listener, false, false);
	}

	public String getVerifyOtpRequestParmas(String str_otp, String userid, String str_number, String device_id,
			String uniqe_id) {
		JSONArray rootJsonArr = null;
		try {
			rootJsonArr = new JSONArray();
			JSONObject rootJsonObj = new JSONObject();
			rootJsonObj.put("otp", str_otp);
			rootJsonObj.put("userid", userid);
			rootJsonObj.put("mobile", str_number);
			rootJsonObj.put("device_id", device_id);
			rootJsonObj.put("uniqe_id", uniqe_id);
			rootJsonArr.put(rootJsonObj);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rootJsonArr.toString();
	}

	public void verifyOtp(String verifyOtpRequestParmas) throws UnsupportedEncodingException {
		ArrayList<String> al_signature_strings = new ArrayList<String>();
		al_signature_strings.add("otpverified");
		al_signature_strings.add("otp");
		al_signature_strings.add("userid");
		al_signature_strings.add("mobile");
		al_signature_strings.add("device_id");
		al_signature_strings.add("uniqe_id");
		String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);
		ConnectionProvider connectionProvider = new ConnectionProvider();
		connectionProvider.doHttpGetRequest(context, URLFactory.getVerifyOtpUrl() + "&signature=" + str_signature
				+ "&data=" + URLEncoder.encode(verifyOtpRequestParmas, "UTF-8"), null, listener, true, false);
	}

	public RequestParams getEditProfileRequestParmas(String userId, String strName, String strEmail, File f)
			throws FileNotFoundException {
		RequestParams requestParams = new RequestParams();

		requestParams.put("userid", userId);
		requestParams.put("name", strName);
		requestParams.put("email", strEmail);
		requestParams.put("image", f);

		return requestParams;
	}

	public void EditProfileUser(RequestParams editProfileRequestParmas) {
		ArrayList<String> al_signature_strings = new ArrayList<String>();
		al_signature_strings.add("editprofile");
		al_signature_strings.add("userid");
		al_signature_strings.add("name");
		al_signature_strings.add("email");
		String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);
		ConnectionProvider connectionProvider = new ConnectionProvider();
		connectionProvider.doHttpPostRequestWithOutHeader(context,
				URLFactory.getEditProfileUrl() + "&signature=" + str_signature, editProfileRequestParmas, listener,
				true, true);

	}

	public String getChangeMobileNumberRequestParmas(String countryZipCode, String str_number, String preferences,
			String strOldNum) {
		JSONArray rootJsonArr = null;
		try {
			rootJsonArr = new JSONArray();
			JSONObject rootJsonObj = new JSONObject();
			rootJsonObj.put("country_code", countryZipCode);
			rootJsonObj.put("userid", preferences);
			rootJsonObj.put("mobile", str_number);
			rootJsonObj.put("oldmobile", strOldNum);
			rootJsonArr.put(rootJsonObj);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rootJsonArr.toString();
	}

	public void changeMobile(String changeMobileNumberRequestParmas) throws UnsupportedEncodingException {
		ArrayList<String> al_signature_strings = new ArrayList<String>();
		al_signature_strings.add("editmobilenumber");
		al_signature_strings.add("country_code");
		al_signature_strings.add("userid");
		al_signature_strings.add("mobile");
		al_signature_strings.add("oldmobile");

		String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);
		ConnectionProvider connectionProvider = new ConnectionProvider();
		connectionProvider
				.doHttpGetRequest(context,
						URLFactory.changeMobileUrl() + "&signature=" + str_signature + "&data="
								+ URLEncoder.encode(changeMobileNumberRequestParmas, "UTF-8"),
						null, listener, true, false);

	}

	public String deactivate(String preferences) {
		JSONArray rootJsonArr = null;
		try {
			rootJsonArr = new JSONArray();
			JSONObject rootJsonObj = new JSONObject();
			rootJsonObj.put("userid", preferences);
			rootJsonArr.put(rootJsonObj);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rootJsonArr.toString();
	}

	public void setDeactivate(String deactivate) throws UnsupportedEncodingException {
		ArrayList<String> al_signature_strings = new ArrayList<String>();
		al_signature_strings.add("userid");
		al_signature_strings.add("deleteuser");

		String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);
		ConnectionProvider connectionProvider = new ConnectionProvider();
		connectionProvider.doHttpGetRequest(context, URLFactory.deactivateUrl() + "&signature=" + str_signature
				+ "&data=" + URLEncoder.encode(deactivate, "UTF-8"), null, listener, true, false);

	}
}
