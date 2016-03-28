package org.ninehertzindia.clipped.fragment;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.ninehertzindia.clipped.ClippedService;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.RegistrationSlideActivity;
import org.ninehertzindia.clipped.network.APIResponse;
import org.ninehertzindia.clipped.network.ResponseController;
import org.ninehertzindia.clipped.util.ApplicationConstants;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.GlobalConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint({ "NewApi" })
public class ChangeMobileFragment extends Fragment implements ResponseController {
	private Activity activity;
	private View view;
	private TextView middletxt;
	private ImageView imgback;
	private EditText etOldNumber;
	private EditText etNewNumber;
	private CommonMethods common;

	public ChangeMobileFragment() {
	}

	public static ChangeMobileFragment newInstance() {
		ChangeMobileFragment fragment = new ChangeMobileFragment();
		return fragment;
	}

	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
		view = paramLayoutInflater.inflate(R.layout.change_mobile, paramViewGroup, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setBodyUI();
		return view;
	}

	private void setBodyUI() {
		activity = getActivity();
		GetCountryZipCode();
		common = new CommonMethods(activity, this);
		imgback = (ImageView) view.findViewById(R.id.imgback);
		imgback.setVisibility(View.VISIBLE);
		etOldNumber = (EditText) view.findViewById(R.id.etOldNumber);
		etNewNumber = (EditText) view.findViewById(R.id.etNewNumber);
		imgback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.onBackPressed();

			}
		});

		view.findViewById(R.id.txt_Submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkValidation();

			}
		});

		middletxt = (TextView) view.findViewById(R.id.middletxt);
		middletxt.setText("Change Number");
	}

	protected void checkValidation() {
		String strOldNum = etOldNumber.getText().toString();
		GlobalConfig.str_number = etNewNumber.getText().toString();

		if (strOldNum.equals("")) {
			GlobalConfig.showToast(activity, "Please enter old number.");
		} else if (GlobalConfig.str_number.equals("")) {
			GlobalConfig.showToast(activity, "Please enter new number.");
		} else if (GlobalConfig.str_number.equals(strOldNum)) {

			GlobalConfig.showToast(activity, "Please enter different new number.");
		}

		else if (common.getConnectivityStatus()) {
			try {
				Log.d("1", "API");
				common.changeMobile(common.getChangeMobileNumberRequestParmas(GlobalConfig.CountryZipCode,
						GlobalConfig.str_number, CommonMethods.getPreferences(activity, "userid"), strOldNum));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			GlobalConfig.showToast(getActivity(), getResources().getString(R.string.internet_error_message));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void OnComplete(APIResponse apiResponse) {
		System.out.println("Response of change mobile number" + apiResponse.getResponse());
		String str_message = "";
		if (!(apiResponse.getResponse() == null)) {
			try {
				JSONObject rootJsonObjObject = new JSONObject(apiResponse.getResponse());
				str_message = rootJsonObjObject.getString("msg");
				String str_status = rootJsonObjObject.getString("status");

				if (Boolean.parseBoolean(str_status)) {
					
					
					
					/*activity.stopService(new Intent(activity, ClippedService.class));*/
					
					
					

					// JSONObject data =
					// rootJsonObjObject.getJSONObject("data");
					
					//CommonMethods.SetPreferences(activity, "mobile", GlobalConfig.str_number);

					// CommonMethods.SetPreferences(activity, "userid",
					// data.getString("userid"));
					// CommonMethods.SetPreferences(activity, "mobile",
					// data.getString("mobile"));
					// CommonMethods.SetPreferences(activity, "name",
					// data.getString("name"));
					// CommonMethods.SetPreferences(activity, "email",
					// data.getString("email"));
					// CommonMethods.SetPreferences(activity, "image",
					// data.getString("image"));
					// CommonMethods.SetPreferences(activity, "image",
					// data.getString("thumb_image"));

					// go to otp screen
					getFragmentManager().beginTransaction().add(R.id.container, OtpFragment.newInstance())
							.addToBackStack(null).commit();
				} else {
					GlobalConfig.showToast(getActivity(), str_message);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			GlobalConfig.showToast(getActivity(), str_message);
		}
	}

	public String GetCountryZipCode() {

		TelephonyManager manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		// getNetworkCountryIso
		String CountryID = manager.getSimCountryIso().toUpperCase();
		String[] items = this.getResources().getStringArray(R.array.CountryCodes);
		for (int i = 0; i < items.length; i++) {
			String[] g = items[i].split(",");
			if (g[1].trim().equals(CountryID.trim())) {
				GlobalConfig.CountryZipCode = g[0];
				String CountryName = g[1];
				break;
			}
		}
		return GlobalConfig.CountryZipCode;
	}

}
