package org.ninehertzindia.clipped.fragment;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ninehertzindia.clipped.CommonFragmentActivity;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.network.APIResponse;
import org.ninehertzindia.clipped.network.ResponseController;
import org.ninehertzindia.clipped.util.CommonMethods;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi" })
public class MembershipBasicFragment extends Fragment implements ResponseController {
	public static String POSITION_KEY;
	private Activity activity;
	private TextView MakePaymentButton;
	private TextView mobileno, paymentStatus;
	private CommonMethods commonMethods;
	private View view;
	private static final String TAG = "paymentExample";
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

	// note that these credentials will differ between live & sandbox
	
	// environments.
	private static final String CONFIG_CLIENT_ID = "AcfnQz_kiXYL7endkCaRNtVjWDngqq3L7YuNFIYEoadouzkgY3CwjwKkud2tv9ldM02SShaLg9ouCnUk";
	//"AVkE0u2pNzYI6z_80Ezy3pZXCLWyG4qY_nhukkALiPBdSK8DM6oI1xCA-MP15SmITJy0uT9Nc-20gJNj";
	private static final int REQUEST_CODE_PAYMENT = 1;
	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	private static final int REQUEST_CODE_PROFILE_SHARING = 3;

	private static PayPalConfiguration config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("BluPey").merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

	public MembershipBasicFragment() {

	}

	public static MembershipBasicFragment newInstance(Bundle args) {

		MembershipBasicFragment fragment = new MembershipBasicFragment();

		return fragment;

	}

	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
		view = paramLayoutInflater.inflate(R.layout.membership_basic_plan, paramViewGroup, false);

		setBodyUI();
		Intent intent = new Intent(activity, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		activity.startService(intent);

		MakePaymentButton = (TextView) view.findViewById(R.id.textView2);
		mobileno = (TextView) view.findViewById(R.id.tttmobile);
		paymentStatus = (TextView) view.findViewById(R.id.statuspayment);

		mobileno.setText(commonMethods.getPreferences(activity, "mobile").toString());

		if (commonMethods.getPreferences(activity, "paymentstatus").toString().equalsIgnoreCase("")
				|| commonMethods.getPreferences(activity, "paymentstatus").toString().equalsIgnoreCase("free")) {

			paymentStatus.setText("Free");
			// MakePaymentButton.isClickable();

		} else {

			MakePaymentButton.setVisibility(View.GONE);
			paymentStatus.setText("Premium");

		}

		MakePaymentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

				Intent intent = new Intent(getActivity(), PaymentActivity.class);

				intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

				intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

				startActivityForResult(intent, REQUEST_CODE_PAYMENT);

			}
		});

		return view;
	}

	private void setBodyUI() {
		activity = getActivity();
		commonMethods = new CommonMethods(activity, this);

	}

	private PayPalPayment getThingToBuy(String paymentIntent) {
		return new PayPalPayment(new BigDecimal("2"), "USD", "Clipped Payment", paymentIntent);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public static Fragment create(int position) {
		return null;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PAYMENT) {

			System.out.println("hhhhhhh LOL");
			if (resultCode == getActivity().RESULT_OK) {

				System.out.println("hhhhhhh LOL1");

				PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {

					try {
						System.out.println("Responseeee" + confirm);
						// Log.i("paymentExample",
						// confirm.toJSONObject().toString());

						JSONObject jsonObj = new JSONObject(confirm.toJSONObject().toString());

						System.out.println("hhhhhh jobj" + jsonObj);

						final String paymentId = jsonObj.getJSONObject("response").getString("id");
						System.out.println("hhhhhh payment" + paymentId);
						System.out.println("payment id:-==" + paymentId);

						// Toast.makeText(activity, "Payment Succeed \n Payment
						// ID-" + paymentId, Toast.LENGTH_LONG).show();

						try {

							commonMethods.PaymentSetting(commonMethods.getPaymentRequestParmas(
									commonMethods.getPreferences(activity, "userid").toString(), paymentId, "12"));

						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} catch (JSONException e) {
						// Log.e("paymentExample", "an extremely unlikely
						// failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				System.out.println("hhhhhhhhhh Cencel PAYMENt");
				// Log.i("paymentExample", "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				System.out.println("hhhhhhhhhh Invalid");
				// Log.i("paymentExample", "An invalid Payment was submitted.
				// Please see the docs.");
			}

		}

	}

	@Override
	public void OnComplete(APIResponse apiResponse) {

		System.out.println("hhh" + apiResponse.getResponse().toString());
		System.out.println("hhh" + apiResponse.getCode());
		if (!(apiResponse.getResponse() == null)) {
			try {

				System.out.println("hhh1 " + apiResponse.getResponse().toString());
				JSONObject rootJsonObjObject = new JSONObject(apiResponse.getResponse());
				String str_message = rootJsonObjObject.getString("msg");
				String str_status = rootJsonObjObject.getString("status");
				System.out.println("hhh status " + str_status);

				if (str_status.equalsIgnoreCase("true")) {

					JSONArray data = rootJsonObjObject.getJSONArray("data");
					System.out.println("hhh  data " + data);

					JSONObject temp_data = data.getJSONObject(0) ;
					
					if (temp_data.getString("status").equalsIgnoreCase("1")) {
						System.out.println("hhh  status inner " + temp_data.getString("status"));

						commonMethods.SetPreferences(activity, "paymentstatus", "active");

						MakePaymentButton.setVisibility(View.GONE);
						Toast.makeText(activity, "Successfully..", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(activity, CommonFragmentActivity.class));
						activity.finish();

					}
				} else {
					Toast.makeText(activity, "" + str_message, Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				System.out.println("hhhh exception"+e);
			}
		}

	}

}
