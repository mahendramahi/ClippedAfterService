package org.ninehertzindia.clipped.footerfragment;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;
import org.ninehertzindia.clipped.ClippedService;
import org.ninehertzindia.clipped.CommonFragmentActivity;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.RegistrationSlideActivity;
import org.ninehertzindia.clipped.fileutiles.FileUtils;
import org.ninehertzindia.clipped.fragment.AboutFragment;
import org.ninehertzindia.clipped.fragment.ChangeMobileFragment;
import org.ninehertzindia.clipped.fragment.MembershipFragment;
import org.ninehertzindia.clipped.fragment.PrivacyFragment;
import org.ninehertzindia.clipped.network.APIResponse;
import org.ninehertzindia.clipped.network.ResponseController;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.GlobalConfig;

import com.google.android.gms.internal.el;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

@SuppressLint({ "NewApi", "InflateParams", "DefaultLocale" })
public class SettingFragment extends Fragment implements OnClickListener, ResponseController {

	private View view;
	private Activity activity;
	private CommonMethods commonMethods;
	private TextView middletxt;
	private RelativeLayout rlAboutApp;
	private RelativeLayout rlPlan;
	private RelativeLayout rlDeactivate;
	private RelativeLayout rlChangePassword;
	private RelativeLayout rlPrivacy;
	private RelativeLayout rlMyClip;
	private ToggleButton settonToggle;
	private int Taskype = 0;

	private int Audio_File_Fetch = 2323;
	private int Video_File_Fetch = 2424;
	private String audio_path = "";
	private String video_path = "";
	private String strMsg;
	private String strStatus;

	public static SettingFragment newInstance() {
		SettingFragment fragment = new SettingFragment();
		return fragment;
	}

	public SettingFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.setting_fragment, container, false);
		setBodyUI();

		return view;
	}

	@SuppressLint("SetJavaScriptEnabled")

	private void setBodyUI() {

		activity = getActivity();
		middletxt = (TextView) view.findViewById(R.id.middletxt);
		middletxt.setText("Settings");
		rlAboutApp = (RelativeLayout) view.findViewById(R.id.rlAboutApp);
		rlPlan = (RelativeLayout) view.findViewById(R.id.rlPlan);
		rlDeactivate = (RelativeLayout) view.findViewById(R.id.rlDeactivate);
		rlPrivacy = (RelativeLayout) view.findViewById(R.id.rlPrivacy);
		rlMyClip = (RelativeLayout) view.findViewById(R.id.rlMyClip);
		rlChangePassword = (RelativeLayout) view.findViewById(R.id.rlChangePassword);
		settonToggle = (ToggleButton) view.findViewById(R.id.settonstatus);
		if (commonMethods.getPreferences(activity, "setton").equalsIgnoreCase("")
				|| commonMethods.getPreferences(activity, "setton").equalsIgnoreCase("1")) {

			settonToggle.setChecked(true);

		} else {

			settonToggle.setChecked(false);

		}

		settonToggle.setOnClickListener(this);
		rlAboutApp.setOnClickListener(this);
		rlPlan.setOnClickListener(this);
		rlDeactivate.setOnClickListener(this);
		rlPrivacy.setOnClickListener(this);
		rlMyClip.setOnClickListener(this);
		rlChangePassword.setOnClickListener(this);
		commonMethods = new CommonMethods(activity, this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlAboutApp:
			getFragmentManager().beginTransaction().add(R.id.container, AboutFragment.newInstance())
					.addToBackStack(null).commit();
			break;
		case R.id.rlPlan:
			getFragmentManager().beginTransaction().add(R.id.container, MembershipFragment.newInstance())
					.addToBackStack(null).commit();
			break;
		case R.id.rlDeactivate:
			showDialog();

			break;
		case R.id.rlPrivacy:
			getFragmentManager().beginTransaction().add(R.id.container, PrivacyFragment.newInstance())
					.addToBackStack(null).commit();
			break;
		case R.id.rlChangePassword:
			getFragmentManager().beginTransaction().add(R.id.container, ChangeMobileFragment.newInstance())
					.addToBackStack(null).commit();
			break;

		case R.id.rlMyClip:

			openDialogWithMoreApp();

			break;

		case R.id.settonstatus:
			String settonstatus = "";
			if (CommonMethods.getPreferences(activity, "setton").equalsIgnoreCase("")
					|| CommonMethods.getPreferences(activity, "setton").equalsIgnoreCase("1")) {
				settonstatus = "0";
			} else {
				settonstatus = "1";
			}

			if (commonMethods.getConnectivityStatus()) {

				String temp_userid = CommonMethods.getPreferences(activity, "userid");

				try {
					Taskype = 22;
					commonMethods
							.AssignedSetting(commonMethods.getAssignedSettingRequestParmas(temp_userid, settonstatus));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				commonMethods.showMessageDialog(activity, getString(R.string.internet_error_message));

			}

			break;

		default:
			break;
		}

	}

	private void showDialog() {
		new AlertDialog.Builder(getActivity()).setMessage("Are you sure delete your account?").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						if (commonMethods.getConnectivityStatus()) {
							try {
								Taskype = 21;
								commonMethods.setDeactivate(
										commonMethods.deactivate(CommonMethods.getPreferences(activity, "userid")));

							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						dialog.dismiss();
					}
				}).setNegativeButton("No", null).show();

	}

	@Override
	public void OnComplete(APIResponse apiResponse) {

		switch (Taskype) {
		case 21:
			JSONObject rootJsonObjObject;
			try {
				rootJsonObjObject = new JSONObject(apiResponse.getResponse());
				strMsg = rootJsonObjObject.getString("msg");
				strStatus = rootJsonObjObject.getString("status");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (strStatus.equals("true")) {

				activity.stopService(new Intent(activity, ClippedService.class));

				Intent intent = new Intent(getActivity(), RegistrationSlideActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				getActivity().finish();
			} else {

				GlobalConfig.showToast(activity, strMsg);

			}
			break;

		case 22:
			JSONObject rootJsonObjObject1 = null;
			try {
				rootJsonObjObject1 = new JSONObject(apiResponse.getResponse());
				strMsg = rootJsonObjObject1.getString("msg");
				strStatus = rootJsonObjObject1.getString("status");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.d("Response", "" + apiResponse.getResponse() + "code" + apiResponse.getCode());

			if (strStatus.equals("true")) {

				try {
					CommonMethods.SetPreferences(activity, "setton", "" + rootJsonObjObject1.getString("setton"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if (CommonMethods.getPreferences(activity, "setton").equalsIgnoreCase("")
					|| CommonMethods.getPreferences(activity, "setton").equalsIgnoreCase("1")) {

				settonToggle.setChecked(true);

			} else {

				settonToggle.setChecked(false);

			}
			break;

		default:
			break;
		}
	}

	private void openDialogWithMoreApp() {

		Log.d("LL", "click 1");
		final Dialog dialogMapMain = new Dialog(activity);
		dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		// dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogMapMain.setContentView(R.layout.dialog_more_option);
		dialogMapMain.getWindow().setGravity(Gravity.CENTER);
		dialogMapMain.setCanceledOnTouchOutside(false);

		LinearLayout ll_AssigneAudio = (LinearLayout) dialogMapMain.findViewById(R.id.ll_AssignAudio);

		LinearLayout ll_AssigneVideo = (LinearLayout) dialogMapMain.findViewById(R.id.ll_AssigneVideo);

		LinearLayout ll_AssigneCancel = (LinearLayout) dialogMapMain.findViewById(R.id.ll_Cancel);

		ll_AssigneCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialogMapMain.dismiss();
				GlobalConfig.showToast(activity, "Cancel");
			}
		});

		ll_AssigneAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMapMain.dismiss();
				// Use the GET_CONTENT intent from the utility class
				Intent target = FileUtils.createGetContentIntentAudio();
				// Create the chooser Intent
				Intent intent = Intent.createChooser(target, "ChooseAudio");
				try {
					startActivityForResult(intent, Audio_File_Fetch);
				} catch (ActivityNotFoundException e) {
					// The reason for the existence of aFileChooser
				}

			}
		});

		ll_AssigneVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogMapMain.dismiss();
				Intent target = FileUtils.createGetContentIntentVideo();
				// Create the chooser Intent
				Intent intent = Intent.createChooser(target, "ChooseVideo");
				try {
					startActivityForResult(intent, Video_File_Fetch);
				} catch (ActivityNotFoundException e) {
				}

			}
		});

		dialogMapMain.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			if (requestCode == Audio_File_Fetch) {

				final Uri uri = data.getData();

				audio_path = FileUtils.getPath(activity, uri);

				File f = new File(audio_path);

				String file_type = URLConnection.guessContentTypeFromName(f.getName());

				if (file_type.substring(0, 5).equalsIgnoreCase("audio")) {

					commonMethods.SetPreferences(activity, "mycliptype", "1");
					commonMethods.SetPreferences(activity, "myclipurl", audio_path);

					GlobalConfig.showToast(activity, "Successfully...");

				} else {
					GlobalConfig.showToast(activity, "Check File Type...");
				}

			}
			if (requestCode == Video_File_Fetch) {

				final Uri uri = data.getData();

				video_path = FileUtils.getPath(activity, uri);

				File f = new File(video_path);

				String file_type = URLConnection.guessContentTypeFromName(f.getName());

				if (file_type.substring(0, 5).equalsIgnoreCase("video")) {

					commonMethods.SetPreferences(activity, "mycliptype", "2");
					commonMethods.SetPreferences(activity, "myclipurl", video_path);

					GlobalConfig.showToast(activity, "Successfully...");

				} else {
					GlobalConfig.showToast(activity, "Check File Type...");
				}

			}

		} else {
			GlobalConfig.showToast(activity, "Cancel");
		}

	}

}