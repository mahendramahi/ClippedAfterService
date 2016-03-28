package org.ninehertzindia.clipped.adapter;

import java.util.ArrayList;

import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.ContactVO;

import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ContactUserAdapter extends BaseAdapter {

	private Activity activity;
	protected CommonMethods commonMethods;
	private FragmentManager fragmentManager;
	private ArrayList<ContactVO> al;

	public ContactUserAdapter(Activity activity, FragmentManager fragmentManager,
			ArrayList<ContactVO> al_phone_object) {
		this.activity = activity;
		this.commonMethods = new CommonMethods(activity, this);
		this.fragmentManager = fragmentManager;
		this.al = al_phone_object;
	}

	class ViewHolder {
		private LinearLayout root;
		public TextView txtName;
		public ImageView imgUserPic;
		public LinearLayout llSendSms;
	}

	@Override
	public int getCount() {

		return al.size();
	}

	@Override
	public ContactVO getItem(int position) {
		return al.get(position);
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
			view = inflater.inflate(R.layout.row_contact_user, null);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			holder.txtName = (TextView) view.findViewById(R.id.txtName);
			holder.imgUserPic = (ImageView) view.findViewById(R.id.imgUserPic);
			holder.llSendSms = (LinearLayout) view.findViewById(R.id.llSendSms);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (position % 2 == 0) {
			holder.root.setBackgroundColor(Color.parseColor("#94e171"));
		} else {
			holder.root.setBackgroundColor(Color.parseColor("#83d161"));
		}

		final ContactVO contactVO = (ContactVO) getItem(position);

		// holder.imgUserPic.setImageResource(R.drawable.activity_img);

		if (contactVO.getContactImageUri() != null) {

			System.out.println("imageuri" + contactVO.getContactImageUri());

			// holder.imgUserPic.setImageURI(Uri.parse(contactVO.getContactImageUri()));
			Picasso.with(activity).load(Uri.parse(contactVO.getContactImageUri())).placeholder(R.drawable.profile_icon).into(holder.imgUserPic);

		}
		holder.txtName.setText(contactVO.getContactName());

		holder.llSendSms.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)

				{
					String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(activity);

					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.setType("text/plain");
					sendIntent.putExtra(Intent.EXTRA_TEXT,
							"Hey join me on Clipped. Download the free app from Google play.  https://play.google.com/store/apps/details?id="+activity.getPackageName());

					if (defaultSmsPackageName != null)

					{
						sendIntent.setPackage(defaultSmsPackageName);
					}
					activity.startActivity(sendIntent);

				} else {
					Intent sendIntent = new Intent(Intent.ACTION_VIEW);
					sendIntent.setData(Uri.parse("sms:"));
					sendIntent.putExtra("sms_body",
							"Hi Join me on Clipped. Download the free app from Google play. https://play.google.com/store/apps/details?id="+activity.getPackageName());
					activity.startActivity(sendIntent);
				}

			}
		});
		return view;

	}

}
