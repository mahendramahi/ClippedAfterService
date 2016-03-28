package org.ninehertzindia.clipped.fragment;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.RegistrationSlideActivity;
import org.ninehertzindia.clipped.SplashActivity;
import org.ninehertzindia.clipped.database.DatabaseHandler;
import org.ninehertzindia.clipped.network.APIResponse;
import org.ninehertzindia.clipped.network.ResponseController;
import org.ninehertzindia.clipped.util.ApplicationConstants;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.GlobalConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class RegistrationOtpFragment extends Fragment implements OnClickListener, ResponseController {
	public static final String ARG_PAGE = "page";
	EditText et_otp;
	private Activity activity;
	private TextView txtResend;
	private int mPageNumber;
	ViewGroup rootView;
	private CommonMethods common;
	protected int TaskType;
	private ImageView imgback;
	public static EditText txtOtp;
	private TextView txt_next;
	private TextView middletxt;
	private DatabaseHandler db;

	public static RegistrationOtpFragment create(int pageNumber) {

		RegistrationOtpFragment fragment = new RegistrationOtpFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		return fragment;

	}

	public RegistrationOtpFragment() {

	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = (ViewGroup) inflater.inflate(R.layout.registration_otp, container, false);

		Log.d("2", "create");
		setBodyUI();

		return rootView;
	}

	private void setBodyUI() {
		activity = getActivity();
		db = new DatabaseHandler(activity);
		common = new CommonMethods(getActivity(), this);
		txtOtp = (EditText) rootView.findViewById(R.id.txtOtp);
		middletxt = (TextView) rootView.findViewById(R.id.middletxt);
		middletxt.setText("Registration");
		imgback = (ImageView) rootView.findViewById(R.id.imgback);
		imgback.setVisibility(View.VISIBLE);

		imgback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RegistrationSlideActivity.previousScreen(1);

			}
		});

		txt_next = (TextView) rootView.findViewById(R.id.txt_next_otp);
		txtResend = (TextView) rootView.findViewById(R.id.txtResend);
		txt_next.setOnClickListener(this);
		txtResend.setOnClickListener(this);

	}

	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
		return mPageNumber;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_next_otp:
			String str_otp = txtOtp.getText().toString();
			if (str_otp.equalsIgnoreCase("")) {
				GlobalConfig.showToast(getActivity(), "Please enter otp");
			} else if (common.getConnectivityStatus()) {
				TaskType = ApplicationConstants.TaskType.VALIDATE_OTP_TASK;
				try {
					System.out.println("hhhhhhhhhhNext Secreen 3" + SplashActivity.registrationId);
					common.verifyOtp(common.getVerifyOtpRequestParmas(str_otp, "", GlobalConfig.str_number,
							SplashActivity.registrationId, SplashActivity.device_id + ""
									+ SplashActivity.HardWare_Serial + "" + SplashActivity.Manufetures_id));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

				GlobalConfig.showToast(getActivity(), getResources().getString(R.string.internet_error_message));

			}

			break;

		case R.id.txtResend:

			if (common.getConnectivityStatus()) {
				TaskType = ApplicationConstants.TaskType.RESEND_CODE_TASK;
				System.out.println("hh test" + GlobalConfig.str_code + ":no:" + GlobalConfig.str_number);
				try {
					common.registrationUser(
							common.getRegistrationRequestParmas(GlobalConfig.str_code, GlobalConfig.str_number));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				GlobalConfig.showToast(getActivity(), getResources().getString(R.string.internet_error_message));
			}

			break;

		default:
			break;
		}

	}

	@Override
	public void OnComplete(APIResponse apiResponse) {

		switch (TaskType) {
		case ApplicationConstants.TaskType.VALIDATE_OTP_TASK:
			System.out.println("Responce otp" + apiResponse.getResponse());
			if (!(apiResponse.getResponse() == null)) {
				try {
					JSONObject rootJsonObjObject = new JSONObject(apiResponse.getResponse());
					String str_message = rootJsonObjObject.getString("msg");
					String str_status = rootJsonObjObject.getString("status");

					if (Boolean.parseBoolean(str_status)) {
						
						try {
							db.deleteHistoryTable();
							
						} catch (Exception e) {
							// TODO: handle exception
						}
						

						JSONObject detail = rootJsonObjObject.getJSONObject("detail");
						GlobalConfig.userId = detail.getString("userid");
						if (!(detail.getString("name").equals(""))) {
							CommonMethods.SetPreferences(activity, "mobile", detail.getString("mobile"));
							CommonMethods.SetPreferences(activity, "name", detail.getString("name"));
							CommonMethods.SetPreferences(activity, "email", detail.getString("email"));
							CommonMethods.SetPreferences(activity, "image", detail.getString("image"));
							CommonMethods.SetPreferences(activity, "setton", detail.getString("set_rington"));
							Log.d("Responce", "setton status" + detail.getString("set_rington"));
							CommonMethods.SetPreferences(activity, "thumb_image", detail.getString("thumb_image"));
						}
						RegistrationSlideActivity.nextScreen(1);
						JSONObject currentPlan = rootJsonObjObject.getJSONObject("currentplan");
						if (!(currentPlan.getString("userid").equals(""))) {
							CommonMethods.SetPreferences(activity, "transation_id", detail.getString("mobile"));
							if (!currentPlan.getString("userid").equalsIgnoreCase("1")) {

								CommonMethods.SetPreferences(activity, "paymentstatus", "active");
							} else {

								CommonMethods.SetPreferences(activity, "paymentstatus", "free");
							}
						}

					} else {
						GlobalConfig.showToast(getActivity(), str_message);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				GlobalConfig.showToast(getActivity(), "Internet is too slow.");
			}
			break;
		case ApplicationConstants.TaskType.RESEND_CODE_TASK:

			if (!(apiResponse.getResponse() == null)) {
				try {
					JSONObject rootJsonObjObject = new JSONObject(apiResponse.getResponse());
					String str_message = rootJsonObjObject.getString("message");
					String str_status = rootJsonObjObject.getString("status");

					if (Boolean.parseBoolean(str_status)) {
						GlobalConfig.showToast(getActivity(), "Otp resend successfully.");
					} else {
						GlobalConfig.showToast(getActivity(), str_message);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				GlobalConfig.showToast(getActivity(), "Internet is too slow.");
			}

			break;

		default:
			break;
		}

	}
}
