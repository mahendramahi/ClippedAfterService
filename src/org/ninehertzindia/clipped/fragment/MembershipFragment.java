package org.ninehertzindia.clipped.fragment;

import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.CustomViewPager;

import com.viewpagerindicator.CirclePageIndicator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint({ "NewApi" })
public class MembershipFragment extends Fragment {
	private Activity activity;
	private CommonMethods commonMethods;
	private View view;
	private TextView middletxt;
	private ImageView imgback;
	CirclePageIndicator mIndicator;
	private CustomViewPager mViewPager;

	public MembershipFragment() {
	}

	public static MembershipFragment newInstance() {
		MembershipFragment fragment = new MembershipFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.membership_plans, container, false);
		setBodyUI();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mViewPager = (CustomViewPager) view.findViewById(R.id.viewPager);
		mViewPager.setAdapter(new MyAdapter(getFragmentManager()));
		mViewPager.setPagingEnabled(true);
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		mIndicator.setViewPager(mViewPager);
		
	}

	public static class MyAdapter extends FragmentStatePagerAdapter {

		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {

			Bundle args;
			switch (position) {
			case 0:
				args = new Bundle();
				args.putInt(MembershipBasicFragment.POSITION_KEY, position);
				return MembershipBasicFragment.newInstance(args);
			/*case 1:
				args = new Bundle();
				args.putInt(MembershipBasicFragment.POSITION_KEY, position);
				return MembershipOnlineFragment.newInstance(args);

			case 2:
				args = new Bundle();
				args.putInt(MembershipBasicFragment.POSITION_KEY, position);
				return MembershipUnlimitddFragment.newInstance(args);*/

			default:
				break;
			}
			return null;

		}
	}

	private void setBodyUI() {
		activity = getActivity();
		imgback = (ImageView) view.findViewById(R.id.imgback);
		imgback.setVisibility(View.VISIBLE);
		imgback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.onBackPressed();

			}
		});
		middletxt = (TextView) view.findViewById(R.id.middletxt);
		middletxt.setText("Membership plans");
		commonMethods = new CommonMethods(activity, this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
