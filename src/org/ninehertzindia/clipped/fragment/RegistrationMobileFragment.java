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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint({ "NewApi", "DefaultLocale" })
public class RegistrationMobileFragment extends Fragment implements ResponseController {
	public static final String ARG_PAGE = "page";
	private int mPageNumber;
	private EditText et_number;
	private ViewGroup rootView;
	private String items[];
	private String CountryID = "";
	private CommonMethods common;
	protected int TaskType;
	private EditText txtCountrCode;
	private String CountryName;
	private TextView middletxt;

	public static RegistrationMobileFragment create(int pageNumber) {
		RegistrationMobileFragment fragment = new RegistrationMobileFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public RegistrationMobileFragment() {
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		GlobalConfig.strEmail = "";
		GlobalConfig.strName = "";
		GlobalConfig.str_number = "";
		
		
		try {
			CommonMethods.SetPreferences(getActivity(), "name", "");
			CommonMethods.SetPreferences(getActivity(), "email", "");
			CommonMethods.SetPreferences(getActivity(), "mobile", "");
			CommonMethods.SetPreferences(getActivity(), "paymentstatus", "free");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rootView = (ViewGroup) inflater.inflate(R.layout.registration_mobile, container, false);
		Log.d("1", "oncreate");
		setBodyUI();
		return rootView;
	}

	private void setBodyUI() {
		
		try {
			getActivity().stopService(new Intent(getActivity(), ClippedService.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		common = new CommonMethods(getActivity(), this);
		middletxt = (TextView) rootView.findViewById(R.id.middletxt);
		middletxt.setText("Registration");
		txtCountrCode = (EditText) rootView.findViewById(R.id.txtCountrCode);
		et_number = (EditText) rootView.findViewById(R.id.et_number);
		GetCountryZipCode();

		txtCountrCode.setText(GlobalConfig.CountryZipCode);

		rootView.findViewById(R.id.txt_next).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("1", "click");
				checkValidation();
			}
		});
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(getActivity().INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}
	}

	private void checkValidation() {
		GlobalConfig.str_number = et_number.getText().toString();
		GlobalConfig.CountryZipCode = txtCountrCode.getText().toString();

		if (txtCountrCode.getText().toString().equals("")) {
			GlobalConfig.showToast(getActivity(), "Please enter country code.");
		} else if (GlobalConfig.str_number.equals("")) {
			GlobalConfig.showToast(getActivity(), "Please enter 10 digit mobile number.");
		} else if (GlobalConfig.str_number.length() < 10) {
			GlobalConfig.showToast(getActivity(), "Please enter valid mobile number.");
		} else if (common.getConnectivityStatus()) {
			TaskType = ApplicationConstants.TaskType.REGISTRSTION;
			try {
				Log.d("1", "API");
				GlobalConfig.str_code = txtCountrCode.getText().toString();

				common.registrationUser(common.getRegistrationRequestParmas(txtCountrCode.getText().toString(),
						GlobalConfig.str_number));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			GlobalConfig.showToast(getActivity(), getResources().getString(R.string.internet_error_message));
		}

	}

	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
		return mPageNumber;
	}

	public String GetCountryZipCode() {

		TelephonyManager manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		// getNetworkCountryIso
		CountryID = manager.getSimCountryIso().toUpperCase();
		items = this.getResources().getStringArray(R.array.CountryCodes);
		for (int i = 0; i < items.length; i++) {
			String[] g = items[i].split(",");
			if (g[1].trim().equals(CountryID.trim())) {
				GlobalConfig.CountryZipCode = g[0];
				CountryName = g[1];
				break;
			}
		}
		return GlobalConfig.CountryZipCode;
	}

	/**
	 * This is overridden method of the Response of the Registration API
	 */

	@Override
	public void OnComplete(APIResponse apiResponse) {

		switch (TaskType) {
		case ApplicationConstants.TaskType.REGISTRSTION:
			System.out.println("Responce REGISTRSTION" + apiResponse.getResponse());
			String str_message = "";
			if (!(apiResponse.getResponse() == null)) {
				try {
					JSONObject rootJsonObjObject = new JSONObject(apiResponse.getResponse());
					str_message = rootJsonObjObject.getString("msg");
					String str_status = rootJsonObjObject.getString("status");
					GlobalConfig.userId = rootJsonObjObject.getString("userid");

					if (Boolean.parseBoolean(str_status)) {

						// go to otp screen
						Log.d("1", "NEXT SCREEN");
						RegistrationSlideActivity.nextScreen(1);

					} else {

						GlobalConfig.showToast(getActivity(), str_message);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				GlobalConfig.showToast(getActivity(), str_message);
			}
			break;

		default:
			break;
		}

	}

}
