package org.ninehertzindia.clipped.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ninehertzindia.clipped.CallScreenActivity;
import org.ninehertzindia.clipped.ClippedService;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.database.DatabaseHandler;
import org.ninehertzindia.clipped.fragment.UserDetailFragment;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.GlobalConfig;
import org.ninehertzindia.clipped.util.HistoryVO;
import org.ninehertzindia.clipped.util.URLFactory;
import org.ninehertzindia.clipped.util.UsersVO.Data;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ClippedUserAdapter extends BaseAdapter {

	private Activity activity;
	public Call call;
	protected CommonMethods commonMethods;
	FragmentManager fragmentManager;
	private ArrayList<Data> al = new ArrayList<Data>();
	String callId;
	public DateFormat timeFormate, dateFormate;
	Date date1, date2;
	private String CallTime, CallDate;
	private DatabaseHandler db;
	

	public ClippedUserAdapter(Activity activity, FragmentManager fragmentManager, ArrayList<Data> arrayList) {
		this.activity = activity;
		this.commonMethods = new CommonMethods(activity, this);
		db = new DatabaseHandler(activity);
		this.fragmentManager = fragmentManager;
		this.al = arrayList;
	}

	class ViewHolder {
		private LinearLayout root;
		public TextView txtName;
		private TextView ClippedNo;
		public ImageView imgUserPic;
		public LinearLayout llCall, ll1, ll2;
	}

	@Override
	public int getCount() {

		return al.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.row_clipped_user, null);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			holder.txtName = (TextView) view.findViewById(R.id.txtName);
			holder.imgUserPic = (ImageView) view.findViewById(R.id.imgUserPic);
			holder.llCall = (LinearLayout) view.findViewById(R.id.llCall);
			holder.ll1 = (LinearLayout) view.findViewById(R.id.ll1);
			holder.ll2 = (LinearLayout) view.findViewById(R.id.ll2);
			holder.ClippedNo = (TextView) view.findViewById(R.id.txt_time_No);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		// if (CommonMethods.getPreferencesInteger(activity, "showcaseview") ==
		// 0 ) {
		/*
		 * if (position == 0) { System.out.println("hhhhhhh ShowCase Call"); new
		 * ShowcaseView.Builder(activity) .withMaterialShowcase()
		 * .setStyle(R.style.CustomShowcaseTheme2) .setTarget(new
		 * ViewTarget(holder.llCall)) .hideOnTouchOutside()
		 * .setContentTitle("LOL") .setContentText("lol") .build(); }
		 */

		// }

		if (position % 2 == 0) {
			holder.root.setBackgroundColor(Color.parseColor("#94e171"));
		} else {
			holder.root.setBackgroundColor(Color.parseColor("#83d161"));
		}

		holder.txtName.setText(al.get(position).getName());
		holder.ClippedNo.setText(al.get(position).getMobile());
		
		Log.e("hhhh Image Cliped", ""+al.get(position).getImage());
		
		Picasso.with(activity).load(URLFactory.baseUrl + al.get(position).getImage()).placeholder(R.drawable.profile_icon).into(holder.imgUserPic);
		
		holder.ll1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Bundle bundle = new Bundle();
				bundle.putString("image", URLFactory.baseUrl + al.get(position).getImage());
				bundle.putString("name", al.get(position).getName());
				bundle.putString("mobile", al.get(position).getMobile());
				bundle.putString("userid", al.get(position).getUserid());
				new UserDetailFragment();
				UserDetailFragment detailFragment = UserDetailFragment.newInstance();
				detailFragment.setArguments(bundle);

				fragmentManager.beginTransaction().add(R.id.container, detailFragment).addToBackStack(null).commit();

			}
		});
		holder.ll2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Bundle bundle = new Bundle();
				bundle.putString("image", URLFactory.baseUrl + al.get(position).getImage());
				bundle.putString("name", al.get(position).getName());
				bundle.putString("mobile", al.get(position).getMobile());
				bundle.putString("userid", al.get(position).getUserid());
				new UserDetailFragment();
				UserDetailFragment detailFragment = UserDetailFragment.newInstance();
				detailFragment.setArguments(bundle);

				fragmentManager.beginTransaction().add(R.id.container, detailFragment).addToBackStack(null).commit();

			}
		});
		holder.llCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (CommonMethods.getPreferencesInteger(activity, "showcase") == 0) {
					new ShowcaseView.Builder(activity).withMaterialShowcase().setStyle(R.style.CustomShowcaseTheme)
							.setTarget(new ViewTarget(holder.llCall)).hideOnTouchOutside()
							.setContentTitle("Introduction Call Feature").setContentText("Click here for Call User")
							.setShowcaseEventListener(new SimpleShowcaseEventListener() {

						@Override
						public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
							CommonMethods.SetPreferencesInteger(activity, "showcase", 1);
							new ShowcaseView.Builder(activity).withMaterialShowcase()
									.setStyle(R.style.CustomShowcaseTheme2).setTarget(new ViewTarget(holder.ll1))
									.hideOnTouchOutside().setContentTitle("Introduction Clipped Feature")
									.setContentText("Click here to Assigned Clip").build();
							
						}

					}).build();
				}

				else {
					if (commonMethods.getConnectivityStatus())

					{

						timeFormate = new SimpleDateFormat("dd/MM/yyyy");
						dateFormate = new SimpleDateFormat("hh:mm aa");

						date1 = new Date();
						date2 = new Date();

						CallTime = "" + timeFormate.format(date1);
						CallDate = "" + dateFormate.format(date2);

						try {
							HistoryVO historyVO = new HistoryVO(0, al.get(position).getName(),
									al.get(position).getMobile(), al.get(position).getThumb_image(), CallDate, CallTime,
									"outgoing call");
							db.addHistoryVO(historyVO);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Call call = ClippedService.mSinchClient.getCallClient().callUser(al.get(position).getMobile());
						String callId = call.getCallId();

						Intent callScreen = new Intent(activity, CallScreenActivity.class);
						callScreen.putExtra(ClippedService.CALL_ID, callId);
						callScreen.putExtra("localname", al.get(position).getName());
						callScreen.putExtra("image", URLFactory.baseUrl + al.get(position).getImage());
						activity.startActivity(callScreen);
					} else {
						GlobalConfig.showToast(activity, "NO Internet Connection");
					}
				}

				//
				////
				//// call = null;
				////
				////
				//// String temp_user = al.get(position).getName();
				////
				////
				////
				////
				////
				//// if (call == null) {
				////
				////
				//// try {
				//
				// String tt = ClippedService.mSinchClient.getLocalUserId();
				//
				// Log.d("Mahi", ""+tt);
				//
				// call =
				// ClippedService.mSinchClient.getCallClient().callUser(al.get(position).getName());
				//
				// call.addCallListener(new CliippedCal());
				//// } catch (Exception e) {
				//// // TODO Auto-generated catch block
				//// e.printStackTrace();
				//// }
				////
				////
				////
				//// }
				//// else {
				////
				//// }
				//
				// //Call call=
				// ClippedService.mSinchClient.getCallClient().callUser(temp_user);
				//
				//
				// //mSinchClient.getCallClient().callUser(userId);
				// //Call call = ((DemoActivity)
				// activity).getSinchServiceInterface().callUser(al.get(position).getName());
				//
				// try {
				// call =
				// ClippedService.mSinchClient.getCallClient().callUser(al.get(position).getName());
				// callId = call.getCallId();
				//
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// //ClippedService.call.addCallListener(new CliippedCal());
				// Intent callScreen = new Intent(activity,
				// CallScreenActivity.class);
				// callScreen.putExtra(ClippedService.CALL_ID, callId);
				// callScreen.putExtra("image", URLFactory.baseUrl +
				// al.get(position).getImage());
				// activity.startActivity(callScreen);

			}
		});
		return view;

	}

	public static class CliippedCal implements CallListener {

		@Override
		public void onCallEnded(Call arg0) {
			Log.d("Clipped", "onCallEnded");

		}

		@Override
		public void onCallEstablished(Call arg0) {
			Log.d("Clipped", "onCallEstablesed");

		}

		@Override
		public void onCallProgressing(Call arg0) {
			Log.d("Clipped", "onCallProgressing");

		}

		@Override
		public void onShouldSendPushNotification(Call arg0, List<PushPair> arg1) {
			Log.d("Clipped", "onCallShouldSend");

		}

	}

}
