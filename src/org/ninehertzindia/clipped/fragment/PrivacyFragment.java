package org.ninehertzindia.clipped.fragment;

import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.util.CommonMethods;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi" })
public class PrivacyFragment extends Fragment {
	private Activity activity;
	private CommonMethods commonMethods;
	private View view;
	private TextView middletxt;
	private ImageView imgback;
	private WebView webview_privacy;

	public PrivacyFragment() {
	}

	public static PrivacyFragment newInstance() {
		PrivacyFragment fragment = new PrivacyFragment();
		return fragment;
	}

	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
		view = paramLayoutInflater.inflate(R.layout.privacyfragment, paramViewGroup, false);
		setBodyUI();
		return view;
	}

	private void setBodyUI() {
		activity = getActivity();
		imgback = (ImageView) view.findViewById(R.id.imgback);
		webview_privacy = (WebView) view.findViewById(R.id.webview_privacy);
		imgback.setVisibility(View.VISIBLE);

		if (CommonMethods.getMahiConnectivityStatus()) {
			//webview_privacy.setVisibility(View.VISIBLE);
			startWebView("http://demo2server.com/sites/mobile/clipped/privacy.php");
		} else {
			webview_privacy.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(), "No INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
		}

		imgback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.onBackPressed();
			}
		});
		middletxt = (TextView) view.findViewById(R.id.middletxt);
		middletxt.setText("Privacy & Policy");
		commonMethods = new CommonMethods(activity, this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void startWebView(String url) {

		// Create new webview Client to show progress dialog
		// When opening a url or click on link

		webview_privacy.setWebViewClient(new WebViewClient() {
			ProgressDialog progressDialog;

			// If you will not use this method url links are opeen in new brower
			// not in webview
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// Show loader on url load
			public void onLoadResource(WebView view, String url) {
				if (progressDialog == null) {
					// in standard case YourActivity.this
					progressDialog = new ProgressDialog(getActivity());
					progressDialog.setMessage("Loading...");
					progressDialog.show();
				}
			}

			public void onPageFinished(WebView view, String url) {
				try {
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
						progressDialog = null;
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		});

		// Javascript inabled on webview
		webview_privacy.getSettings().setJavaScriptEnabled(true);
		webview_privacy.loadUrl(url);
	}
}
