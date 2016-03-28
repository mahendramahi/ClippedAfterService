
package org.ninehertzindia.clipped;

import org.ninehertzindia.clipped.fragment.RegistrationMobileFragment;
import org.ninehertzindia.clipped.fragment.RegistrationOtpFragment;
import org.ninehertzindia.clipped.fragment.RegistrationUserInfoFragment;
import org.ninehertzindia.clipped.fragment.RegistrationUserProfileFragment;
import org.ninehertzindia.clipped.fragment.RegistrationWelcomeFragment;
import org.ninehertzindia.clipped.util.CustomViewPager;
import com.viewpagerindicator.CirclePageIndicator;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

@SuppressLint("InflateParams")
public class RegistrationSlideActivity extends FragmentActivity {

	public static CustomViewPager mPager;
	private PagerAdapter mPagerAdapter;
	ScreenSlidePagerAdapter mAdapter;
	CirclePageIndicator mIndicator;
	Activity activity;

	ActionBar action;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_slide);
		activity = this;

		mPager = (CustomViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		// mPager.setOnPageChangeListener(new
		// ViewPager.SimpleOnPageChangeListener() {
		// @Override
		// public void onPageSelected(int position) {
		//
		// }
		// });

		mAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setOffscreenPageLimit(0);
		mPager.setAdapter(mAdapter);

		mPager.setPagingEnabled(false);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

	}

	/**
	 * A simple pager adapter that represents 5
	 * {@link RegistrationMobileFragment} objects, in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {

			switch (position) {
			case 0:

				return RegistrationMobileFragment.create(position);

			case 1:

				return RegistrationOtpFragment.create(position);

			case 2:
				return RegistrationUserInfoFragment.create(position);

			case 3:
				return RegistrationUserProfileFragment.create(position);
			case 4:
				return RegistrationWelcomeFragment.create(position);
			default:
				break;
			}
			return null;

			// return FragmentRegistrationFirst.create(position);
		}

		@Override
		public int getCount() {
			return 5;
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

	}

	public static void nextScreen(int i) {
		mPager.setCurrentItem(getItem(+i), true);
	}

	private static int getItem(int i) {
		return mPager.getCurrentItem() + i;
	}

	public static void previousScreen(int i) {
		mPager.setCurrentItem(getItem(-i), true);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		System.out.println("hhhhhhh " + mPager.getCurrentItem());
		previousScreen(1);
		if (mPager.getCurrentItem() == 0) {
			super.onBackPressed();

		}

	}

}
