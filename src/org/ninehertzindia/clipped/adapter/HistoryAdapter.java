package org.ninehertzindia.clipped.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ninehertzindia.clipped.BaseActivity;
import org.ninehertzindia.clipped.CallScreenActivity;
import org.ninehertzindia.clipped.ClippedService;
import org.ninehertzindia.clipped.DemoActivity;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.SinchService;
import org.ninehertzindia.clipped.database.DatabaseHandler;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.GlobalConfig;
import org.ninehertzindia.clipped.util.HistoryVO;
import org.ninehertzindia.clipped.util.URLFactory;

import com.sinch.android.rtc.calling.Call;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
public class HistoryAdapter extends BaseAdapter {

	private Activity activity;
	protected CommonMethods commonMethods;
	private List<HistoryVO> HistoryVo;
	public DateFormat timeFormate, dateFormate;
	Date date1, date2;
	private DatabaseHandler db;
	private String CallTime, CallDate;

	public HistoryAdapter(Activity activity, List<HistoryVO> HistoryVO) {
		this.activity = activity;
		db = new DatabaseHandler(activity);
		commonMethods = new CommonMethods(activity, this);
		this.commonMethods = new CommonMethods(activity, this);
		this.HistoryVo = HistoryVO;

	}

	class ViewHolder {
		private LinearLayout root;
		private ImageView IVProfile;
		private TextView TVName;
		private TextView TVTime;
		private TextView TVDate;
		private TextView TVstatus;

	}

	@Override
	public int getCount() {

		return HistoryVo.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.row_history, null);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			holder.IVProfile = (ImageView) view.findViewById(R.id.imageView1);
			holder.TVName = (TextView) view.findViewById(R.id.textView1);
			holder.TVTime = (TextView) view.findViewById(R.id.txt_time);
			holder.TVDate = (TextView) view.findViewById(R.id.txt_date);
			holder.TVstatus = (TextView) view.findViewById(R.id.txt_call_status);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (position % 2 == 0) {
			holder.root.setBackgroundColor(Color.parseColor("#94e171"));
		} else {
			holder.root.setBackgroundColor(Color.parseColor("#83d160"));
		}

		System.out.println("hhhhhhhhhhh: "
				+ HistoryVo.get(HistoryVo.size() - position - 1).getContactName().equalsIgnoreCase("null"));

		holder.TVName.setText(HistoryVo.get(HistoryVo.size() - position - 1).getContactName());
		holder.TVTime.setText(HistoryVo.get(HistoryVo.size() - position - 1).getContactdate());
		holder.TVDate.setText(HistoryVo.get(HistoryVo.size() - position - 1).getContacttime());

		holder.TVstatus.setText(HistoryVo.get(HistoryVo.size() - position - 1).getContactstatus());
		if (HistoryVo.get(HistoryVo.size() - position - 1).getContactstatus().equalsIgnoreCase("incoming call")) {

			holder.TVstatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_incomingcall, 0, 0, 0);
		} else {
			holder.TVstatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_outgoingcall, 0, 0, 0);
		}
		
		try {
			
		Picasso.with(activity).load(URLFactory.baseUrl + HistoryVo.get(HistoryVo.size() - position - 1).getContactImageUri()).placeholder(R.drawable.profile_icon).into(holder.IVProfile);
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		final int i = HistoryVo.size() - position - 1;
		holder.root.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String name = HistoryVo.get(i).getContactName();
				String mobile = HistoryVo.get(i).getMobile();
				String image = HistoryVo.get(i).getContactImageUri();

				if (commonMethods.getConnectivityStatus()) {
					timeFormate = new SimpleDateFormat("dd/MM/yyyy");
					dateFormate = new SimpleDateFormat("hh:mm aa");

					date1 = new Date();
					date2 = new Date();

					CallTime = "" + timeFormate.format(date1);
					CallDate = "" + dateFormate.format(date2);

					try {
						HistoryVO historyVO = new HistoryVO(0, name, mobile, image, CallDate, CallTime,
								"outgoing call");
						db.addHistoryVO(historyVO);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Call call = ClippedService.mSinchClient.getCallClient().callUser(mobile);
					String callId = call.getCallId();

					Intent callScreen = new Intent(activity, CallScreenActivity.class);
					callScreen.putExtra(ClippedService.CALL_ID, callId);
					callScreen.putExtra("image", image);
					callScreen.putExtra("localname", name);
					activity.startActivity(callScreen);
				} else {
					GlobalConfig.showToast(activity, "NO Internet Connection");
				}

			}
		});

		

		return view;

	}

}
