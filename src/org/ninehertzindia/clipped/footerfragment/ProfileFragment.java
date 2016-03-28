package org.ninehertzindia.clipped.footerfragment;

import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.fragment.ChangeMobileFragment;
import org.ninehertzindia.clipped.fragment.EditProfileFragment;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.URLFactory;

import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ProfileFragment extends Fragment {

	private Activity activity;
	private CommonMethods commonMethods;
	private View view;
	private TextView middletxt;
	private ImageView imgEditProfile;
	private TextView txt_name;
	private TextView txt_mobile;
	private TextView txt_email;
	private ImageView imgProfile;
	private TextView txt_username;

	public static ProfileFragment newInstance() {

		ProfileFragment fragment = new ProfileFragment();
		return fragment;
	}

	public ProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.profile_fragment, container, false);
		setBodyUI();
		return view;
	}

	@SuppressLint("ResourceAsColor")
	private void setBodyUI() {
		activity = getActivity();

		txt_username = (TextView) view.findViewById(R.id.txt_username);
		txt_name = (TextView) view.findViewById(R.id.txt_name);
		txt_mobile = (TextView) view.findViewById(R.id.txt_mobile);
		txt_email = (TextView) view.findViewById(R.id.txt_email);
		imgProfile = (ImageView) view.findViewById(R.id.imgProfile);

		Picasso.with(activity).load(URLFactory.baseUrl + CommonMethods.getPreferences(activity, "image")).fit()
				.into(imgProfile);

		txt_username.setText(CommonMethods.getPreferences(activity, "name"));
		txt_name.setText(CommonMethods.getPreferences(activity, "name"));
		txt_mobile.setText(CommonMethods.getPreferences(activity, "mobile"));
		txt_email.setText(CommonMethods.getPreferences(activity, "email"));

		imgEditProfile = (ImageView) view.findViewById(R.id.imgEditProfile);
		imgEditProfile.setVisibility(View.VISIBLE);

		imgEditProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction().add(R.id.container, EditProfileFragment.newInstance())
						.addToBackStack(null).commit();

			}
		});

		middletxt = (TextView) view.findViewById(R.id.middletxt);
		middletxt.setText("My Profile");
		commonMethods = new CommonMethods(activity, this);

		view.findViewById(R.id.rlName).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction().add(R.id.container, EditProfileFragment.newInstance())
						.addToBackStack(null).commit();

			}
		});

		view.findViewById(R.id.rlMobile).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction().add(R.id.container, ChangeMobileFragment.newInstance())
						.addToBackStack(null).commit();

			}
		});

		view.findViewById(R.id.rlEmail).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction().add(R.id.container, EditProfileFragment.newInstance())
						.addToBackStack(null).commit();

			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();

		txt_username.setText(CommonMethods.getPreferences(activity, "name"));
		txt_name.setText(CommonMethods.getPreferences(activity, "name"));
		txt_mobile.setText(CommonMethods.getPreferences(activity, "mobile"));
		txt_email.setText(CommonMethods.getPreferences(activity, "email"));
		Picasso.with(activity).load(URLFactory.baseUrl + CommonMethods.getPreferences(activity, "image")).fit()
				.into(imgProfile);
	}

}
