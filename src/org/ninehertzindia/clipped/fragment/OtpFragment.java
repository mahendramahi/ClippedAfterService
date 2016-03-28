package org.ninehertzindia.clipped.fragment;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.ninehertzindia.clipped.ClippedService;
import org.ninehertzindia.clipped.CommonFragmentActivity;
import org.ninehertzindia.clipped.FillArraylistAsyncTask;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.SplashActivity;
import org.ninehertzindia.clipped.database.DatabaseHandler;
import org.ninehertzindia.clipped.network.APIResponse;
import org.ninehertzindia.clipped.network.ResponseController;
import org.ninehertzindia.clipped.util.ApplicationConstants;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.GlobalConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
public class OtpFragment extends Fragment implements OnClickListener, ResponseController {
	EditText et_otp;
	private Activity activity;
	private TextView txtResend;
	private int mPageNumber;
	ViewGroup rootView;
	private CommonMethods common;
	protected int TaskType;
	private ImageView imgback;
	private EditText txtOtp;
	private TextView txt_next;
	private TextView middletxt;
	private DatabaseHandler db;

	public OtpFragment() {
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static OtpFragment newInstance() {
		OtpFragment fragment = new OtpFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = (ViewGroup) inflater.inflate(R.layout.otp, container, false);

		Log.d("2", "create");
		setBodyUI();

		return rootView;
	}

	private void setBodyUI() {
		activity = getActivity();

		db = new DatabaseHandler(activity);

		common = new CommonMethods(getActivity(), this);
		txtOtp = (EditText) rootView.findViewById(R.id.txtOtp);
		imgback = (ImageView) rootView.findViewById(R.id.imgback);
		imgback.setVisibility(View.VISIBLE);
		middletxt = (TextView) rootView.findViewById(R.id.middletxt);
		middletxt.setText("OTP");
		imgback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.onBackPressed();
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
					Log.d("2", "Next Secreen 3");
					common.verifyOtp(
							common.getVerifyOtpRequestParmas(str_otp, CommonMethods.getPreferences(activity, "userid"),
									GlobalConfig.str_number, "", SplashActivity.device_id + ""
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

						CommonMethods.SetPreferences(activity, "mobile", GlobalConfig.str_number);

						new Thread(new Runnable() {

							@Override
							public void run() {

								try {
									System.out.println("hhhhhh thread start");
									activity.stopService(new Intent(activity, ClippedService.class));
									System.out.println("hhhhhh Service Stop");

									db.delete();
									System.out.println("hhhhhh Data tables Delete");
									Thread.sleep(100);
									System.out.println("hhhhhh thread jag gai");

									db = new DatabaseHandler(activity);
									System.out.println("hhhhhh data base ban gya");
									if (db.getContactVOsCount() == 0) {

										System.out.println("hhhhhh table count xzero aay contact ka");

										new FillArraylistAsyncTask(activity).execute();

									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// db.del

							}
						}).start();

						JSONObject detail = rootJsonObjObject.getJSONObject("detail");
						GlobalConfig.userId = detail.getString("userid");

						if (!(detail.getString("name").equals(""))) {
							CommonMethods.SetPreferences(activity, "mobile", detail.getString("mobile"));
							CommonMethods.SetPreferences(activity, "name", detail.getString("name"));
							CommonMethods.SetPreferences(activity, "email", detail.getString("email"));
							CommonMethods.SetPreferences(activity, "image", detail.getString("image"));
							CommonMethods.SetPreferences(activity, "thumb_image", detail.getString("thumb_image"));
						}

						Intent intent = new Intent(activity, CommonFragmentActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
						getActivity().finish();
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
