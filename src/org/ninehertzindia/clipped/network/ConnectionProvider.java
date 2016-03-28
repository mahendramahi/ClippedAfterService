package org.ninehertzindia.clipped.network;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.ninehertzindia.clipped.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager.LayoutParams;

public class ConnectionProvider {

	private ProgressDialog pDialog;
	private ResponseController mRevvNetworkListener;
	private APIResponse apiResponse = null;

	public void doHttpGetRequest(final Context context, String url, RequestParams params, Object listner,
			final boolean isShowLoading, final boolean cancalOrShowProgress) {

		String appHeader;
		mRevvNetworkListener = (ResponseController) listner;
		apiResponse = new APIResponse();
		// appHeader = CommonMethods.getAppTokenTypeFromPrefs(context) + " "
		// + CommonMethods.getAppTokenIDFromPrefs(context);

		System.out.println("hhhh url = " + url);
		Log.d("Response", "url "+url+":params:"+params);
		System.out.println("hhhhhh params = " + params);
		// System.out.println("pc app hedaer Token = " + appHeader);

		// creating the object for the library asynchttplibrary
		AsyncHttpClient client = new AsyncHttpClient();
		// client.addHeader("Authorization", appHeader);

		client.setTimeout(30000);
		client.setURLEncodingEnabled(true);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, String response) {

				System.out.println("hhhh response = " + response);
				Log.d("Response", ""+response);
				System.out.println("hhhhh response code = " + statusCode);

				if (isShowLoading) {
					dismissDialog();
				}
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(response);
				mRevvNetworkListener.OnComplete(apiResponse);

			}

			@SuppressWarnings("deprecation")
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
				super.onFailure(statusCode, headers, error, content);
				if (isShowLoading) {
					dismissDialog();
				}
				System.out.println(
						"pc onFailure : " + error.getMessage() + " : " + content + " status code : " + statusCode);
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(content);
				mRevvNetworkListener.OnComplete(apiResponse);

			}

			@Override
			public void onStart() {
				super.onStart();
				if (isShowLoading) {
					showDialog(context, cancalOrShowProgress);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (isShowLoading) {
					dismissDialog();
				}
			}

		});
	}

	/**
	 * method is used for show dialog when web service is used
	 * 
	 * @param context
	 */
	private void showDialog(Context context, boolean b) {

		// pDialog = ProgressDialog.show(context, null, "");
		// pDialog.setContentView(R.layout.view_progress);
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Loading..");
		pDialog.getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}

	/**
	 * method is used for dismissing the progress dialog
	 */
	private void dismissDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	/**
	 * method is used for send post request by using asynchttpclientlibrary
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param listner
	 */
	public void doHttpPostRequest(final Context context, String url, String params, Object listner,
			final boolean isShowLoading, final boolean cancalOrShowProgress) {

		String appHeader;
		StringEntity entity = null;

		mRevvNetworkListener = (ResponseController) listner;
		apiResponse = new APIResponse();
		// appHeader = CommonMethods.getAppTokenTypeFromPrefs(context) + " "
		// + CommonMethods.getAppTokenIDFromPrefs(context);

		System.out.println("pc url : " + url);
		System.out.println("pc params : " + params);
		// System.out.println("pc app hedaer Token : " + appHeader);

		// creating the object for the library asynchttplibrary
		AsyncHttpClient client = new AsyncHttpClient();
		// client.addHeader("Authorization", appHeader);

		if (params != null) {
			try {
				entity = new StringEntity(params);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		// client.setTimeout(30000);
		client.post(context, url, entity, "application/json", new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, String response) {

				System.out.println("pc response = " + response);
				System.out.println("pc response code = " + statusCode);

				dismissDialog();
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(response);
				mRevvNetworkListener.OnComplete(apiResponse);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
				super.onFailure(statusCode, headers, error, content);

				System.out.println(
						"pc onFailure : " + error.getMessage() + " : " + content + " status code : " + statusCode);
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(content);
				mRevvNetworkListener.OnComplete(apiResponse);

			}

			@Override
			public void onStart() {
				super.onStart();
				if (isShowLoading) {
					showDialog(context, cancalOrShowProgress);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (isShowLoading) {
					dismissDialog();
				}
			}
		});
	}

	/**
	 * method is used for send post request by using asynchttpclientlibrary
	 * without header
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param listner
	 */
	public void doHttpPostRequestWithOutHeader(final Context context, String url, RequestParams params, Object listner,
			final boolean isShowLoading, final boolean cancalOrShowProgress) {

		// String appHeader;

		mRevvNetworkListener = (ResponseController) listner;
		apiResponse = new APIResponse();
		// appHeader = CommonMethods.getAppTokenTypeFromPrefs(context) + " "
		// + CommonMethods.getAppTokenIDFromPrefs(context);

		System.out.println("hhhhh url : " + url);
		System.out.println("hhhh params : " + params);
		// System.out.println("pc app hedaer Token : " + appHeader);

		// creating the object for the library asynchttplibrary
		AsyncHttpClient client = new AsyncHttpClient();
		// client.addHeader("Authorization", appHeader);
		client.setTimeout(30000);
		client.post(context, url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, String response) {

				System.out.println("hhh response = " + response);
				System.out.println("hhh response code = " + statusCode);
				if (isShowLoading) {
					dismissDialog();
				}
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(response);
				mRevvNetworkListener.OnComplete(apiResponse);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
				super.onFailure(statusCode, headers, error, content);

				System.out.println(
						"pc onFailure : " + error.getMessage() + " : " + content + " status code : " + statusCode);
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(content);
				mRevvNetworkListener.OnComplete(apiResponse);

			}

			@Override
			public void onStart() {
				super.onStart();
				if (isShowLoading) {
					showDialog(context, cancalOrShowProgress);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (isShowLoading) {
					dismissDialog();
				}
			}

		});
	}

	/**
	 * method is used for send post request by using asynchttpclientlibrary
	 * without header
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param listner
	 */
	public void doHttpPostRequestWith(final Context context, String url, String params, Object listner,
			final boolean cancalOrShowProgress) {

		// String appHeader;
		StringEntity entity = null;

		mRevvNetworkListener = (ResponseController) listner;
		apiResponse = new APIResponse();
		// appHeader = CommonMethods.getAppTokenTypeFromPrefs(context) + " "
		// + CommonMethods.getAppTokenIDFromPrefs(context);

		System.out.println("pc url : " + url);
		System.out.println("pc params : " + params);
		// System.out.println("pc app hedaer Token : " + appHeader);

		// creating the object for the library asynchttplibrary
		AsyncHttpClient client = new AsyncHttpClient();
		// client.addHeader("Authorization", appHeader);

		if (params != null) {
			try {
				entity = new StringEntity(params);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		client.post(context, url, entity, "application/json", new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, String response) {

				System.out.println("pc response = " + response);
				System.out.println("pc response code = " + statusCode);

				dismissDialog();
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(response);
				mRevvNetworkListener.OnComplete(apiResponse);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
				super.onFailure(statusCode, headers, error, content);

				System.out.println(
						"pc onFailure : " + error.getMessage() + " : " + content + " status code : " + statusCode);
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(content);
				mRevvNetworkListener.OnComplete(apiResponse);

			}

			@Override
			public void onStart() {
				super.onStart();
				showDialog(context, cancalOrShowProgress);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				dismissDialog();
			}

		});
	}

	/**
	 * method is used for send put request by using asynchttpclientlibrary
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param listner
	 */
	public void doHttpPutRequest(final Context context, String url, String params, Object listner,
			final boolean isShowLoading, final boolean cancalOrShowProgress) {

		String appHeader;
		StringEntity entity = null;

		mRevvNetworkListener = (ResponseController) listner;
		apiResponse = new APIResponse();
		// appHeader = CommonMethods.getAppTokenTypeFromPrefs(context) + " "
		// + CommonMethods.getAppTokenIDFromPrefs(context);

		System.out.println("pc url : " + url);
		System.out.println("pc params : " + params);
		// System.out.println("pc app hedaer Token : " + appHeader);

		// creating the object for the library asynchttplibrary
		AsyncHttpClient client = new AsyncHttpClient();
		// client.addHeader("Authorization", appHeader);

		if (params != null) {
			try {
				entity = new StringEntity(params);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		client.put(context, url, entity, "application/json", new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, String response) {
				System.out.println("pc response = " + response);
				System.out.println("pc response code = " + statusCode);

				dismissDialog();
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(response);
				mRevvNetworkListener.OnComplete(apiResponse);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
				super.onFailure(statusCode, headers, error, content);

				System.out.println(
						"pc onFailure : " + error.getMessage() + " : " + content + " status code : " + statusCode);
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(content);
				mRevvNetworkListener.OnComplete(apiResponse);

			}

			@Override
			public void onStart() {
				super.onStart();
				if (isShowLoading) {
					showDialog(context, cancalOrShowProgress);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (isShowLoading) {
					dismissDialog();
				}
			}
		});
	}

	/**
	 * method is used for send delete request by using asynchttpclientlibrary
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param listner
	 */
	public void doHttpDeleteRequest(final Context context, String url, String params, Object listner,
			final boolean isShowLoading, final boolean cancalOrShowProgress) {

		String appHeader;
		// StringEntity entity = null;

		mRevvNetworkListener = (ResponseController) listner;
		apiResponse = new APIResponse();
		// appHeader = CommonMethods.getAppTokenTypeFromPrefs(context) + " "
		// + CommonMethods.getAppTokenIDFromPrefs(context);

		System.out.println("pc url : " + url);
		System.out.println("pc params : " + params);
		// System.out.println("pc app hedaer Token : " + appHeader);

		// creating the object for the library asynchttplibrary
		AsyncHttpClient client = new AsyncHttpClient();
		// client.addHeader("Authorization", appHeader);

		// if (params != null) {
		// try {
		// entity = new StringEntity(params);
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// }

		client.delete(context, url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, String response) {
				System.out.println("pc response = " + response);
				System.out.println("pc response code = " + statusCode);

				dismissDialog();
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(response);
				mRevvNetworkListener.OnComplete(apiResponse);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
				super.onFailure(statusCode, headers, error, content);

				System.out.println(
						"pc onFailure : " + error.getMessage() + " : " + content + " status code : " + statusCode);
				apiResponse.setCode(statusCode);
				apiResponse.setResponse(content);
				mRevvNetworkListener.OnComplete(apiResponse);

			}

			@Override
			public void onStart() {
				super.onStart();
				if (isShowLoading) {
					showDialog(context, cancalOrShowProgress);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (isShowLoading) {
					dismissDialog();
				}
			}
		});

	}
}
