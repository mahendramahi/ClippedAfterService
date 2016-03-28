package org.ninehertzindia.clipped.fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.RegistrationSlideActivity;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.GlobalConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class RegistrationUserInfoFragment extends Fragment {
	public static final String ARG_PAGE = "page";
	EditText et_password;
	private Activity activity;
	private int mPageNumber;
	ViewGroup rootView;
	LinearLayout img_password;
	CommonMethods common;
	SharedPreferences prefs_login;
	protected int TaskType;
	private ImageView imgback;
	private EditText etName;
	private EditText etEmail;
	private TextView middletxt;

	public static RegistrationUserInfoFragment create(int pageNumber) {
		RegistrationUserInfoFragment fragment = new RegistrationUserInfoFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public RegistrationUserInfoFragment() {
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = (ViewGroup) inflater.inflate(R.layout.registration_info_name, container, false);

		setBodyUI();

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (!(CommonMethods.getPreferences(activity, "name").equals(""))) {
			etName.setText(CommonMethods.getPreferences(activity, "name"));
			etEmail.setText(CommonMethods.getPreferences(activity, "email"));
		}
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
		if (menuVisible) {
			if (!(CommonMethods.getPreferences(activity, "name").equals(""))) {
				etName.setText(CommonMethods.getPreferences(activity, "name"));
				etEmail.setText(CommonMethods.getPreferences(activity, "email"));
			}
		}
	}

	private void setBodyUI() {
		activity = getActivity();
		middletxt = (TextView) rootView.findViewById(R.id.middletxt);
		middletxt.setText("Registration");
		imgback = (ImageView) rootView.findViewById(R.id.imgback);
		imgback.setVisibility(View.VISIBLE);
		etName = (EditText) rootView.findViewById(R.id.etName);
		etEmail = (EditText) rootView.findViewById(R.id.etEmail);

		common = new CommonMethods(getActivity(), this);

		imgback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				RegistrationSlideActivity.previousScreen(1);

			}
		});

		rootView.findViewById(R.id.txt_next).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// RegistrationSlideActivity.nextScreen(1);

				checkValidation();

			}
		});

	}

	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
		return mPageNumber;
	}

	private void checkValidation() {
		GlobalConfig.strName = etName.getText().toString();
		GlobalConfig.strEmail = etEmail.getText().toString();

		if (GlobalConfig.strName.equals("")) {
			GlobalConfig.showToast(getActivity(), "Please enter a Name.");
		} else if (!(validateName(GlobalConfig.strName))) {
			GlobalConfig.showToast(getActivity(), "Please enter valid Name.");
		} else if ((!isValidEmail(GlobalConfig.strEmail))) {
			GlobalConfig.showToast(getActivity(), "Please enter valid Email address.");
		} else {
			RegistrationSlideActivity.nextScreen(1);
		}

	}

	/*
	 * this method is used for name validation using pattern class in java
	 */
	public static boolean validateName(final String username) {
		// Pattern pattern = Pattern.compile("^[A-Z0-9_-]?[a-z0-9_-]{3,15}$");
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$");

		Matcher matcher = pattern.matcher(username);
		return matcher.matches();

	}// end validatenameName method

	// validating email id
	public static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}// end isValidEmail() method

}
