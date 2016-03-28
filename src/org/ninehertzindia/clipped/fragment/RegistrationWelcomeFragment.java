package org.ninehertzindia.clipped.fragment;

import org.ninehertzindia.clipped.ClippedService;
import org.ninehertzindia.clipped.CommonFragmentActivity;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.RegistrationSlideActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import tyrantgit.explosionfield.ExplosionField;

@SuppressLint("NewApi")
public class RegistrationWelcomeFragment extends Fragment {
	public static final String ARG_PAGE = "page";
	private int mPageNumber;
	private ImageView img_btn_getstarted;
	private ImageView imgback;
	private TextView middletxt;
	private ProgressDialog pd;
	private Animation animation1,animation2;
	private ExplosionField mExplosionField;
	private int i;

	public static RegistrationWelcomeFragment create(int pageNumber) {
		RegistrationWelcomeFragment fragment = new RegistrationWelcomeFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public RegistrationWelcomeFragment() {
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.to_middle);
		animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.from_middle);
	
		
		//	getActivity().getApplicationContext().bindService(new Intent(getActivity(), SinchService.class), (ServiceConnection) this.getActivity(), getActivity().BIND_AUTO_CREATE);
		//getActivity().getApplicationContext().bindService(new Intent(this, SinchService.class), this, BIND_AUTO_CREATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.registration_welcome, container, false);
		mExplosionField = ExplosionField.attach2Window(getActivity());
		//addListener(rootView.findViewById(R.id.root));
		
		

		img_btn_getstarted = (ImageView) rootView.findViewById(R.id.imageView1);
		
		animation1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				img_btn_getstarted.setAnimation(animation2);
				img_btn_getstarted.startAnimation(animation2);
				
			}
		});
		animation2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				 
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				
			}
		});
		
		img_btn_getstarted.clearAnimation();
		
		
		img_btn_getstarted.setAnimation(animation1);
		img_btn_getstarted.startAnimation(animation1);
		
	
		

		img_btn_getstarted.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//View v = rootView.findViewById(R.id.imageView1);
				mExplosionField.explode(v);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							Thread.sleep(900);
							
							getActivity().startService(new Intent(getActivity(), ClippedService.class));
							
							
							Intent intent = new Intent(getActivity(), CommonFragmentActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent);
							
							getActivity().finish();
						
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}).start();
				
				

			}
		});
		middletxt = (TextView) rootView.findViewById(R.id.middletxt);
		middletxt.setText("Registration");
		imgback = (ImageView) rootView.findViewById(R.id.imgback);
		imgback.setVisibility(View.INVISIBLE);

		imgback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				RegistrationSlideActivity.previousScreen(1);

			}
		});

		

		return rootView;
	}

	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
		
		return mPageNumber;
		
	}

}
