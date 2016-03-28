package org.ninehertzindia.clipped.fragment;

import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.util.CommonMethods;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint({ "NewApi" })
public class MembershipUnlimitddFragment extends Fragment {
	private Activity activity;
	private CommonMethods commonMethods;
	private View view;

	public MembershipUnlimitddFragment() {
	}

	public static MembershipUnlimitddFragment newInstance(Bundle args) {
		MembershipUnlimitddFragment fragment = new MembershipUnlimitddFragment();
		return fragment;
	}

	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
		view = paramLayoutInflater.inflate(R.layout.membership_unlimited_plan, paramViewGroup, false);
		setBodyUI();
		return view;
	}

	private void setBodyUI() {
		activity = getActivity();
		commonMethods = new CommonMethods(activity, this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
