package org.ninehertzindia.clipped.footerfragment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.adapter.ClippedUserAdapter;
import org.ninehertzindia.clipped.adapter.ContactUserAdapter;
import org.ninehertzindia.clipped.database.DatabaseHandler;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.ContactVO;
import org.ninehertzindia.clipped.util.GlobalConfig;
import org.ninehertzindia.clipped.util.SelctedLineareLayout;
import org.ninehertzindia.clipped.util.SignatureCommonMenthods;
import org.ninehertzindia.clipped.util.URLFactory;
import org.ninehertzindia.clipped.util.UsersVO;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts.Photo;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ContactFragment extends Fragment implements OnClickListener {

	private View view;
	private Activity activity;
	private CommonMethods commonMethods;
	private TextView middletxt;
	private SelctedLineareLayout ll_users;
	private LinearLayout ll_clipped_user;
	private LinearLayout ll_contact;
	private ListView lv_contact;
	private ClippedUserAdapter clippedUserAdapter;
	private ContactUserAdapter contactUserAdapter;
	ArrayList<ContactVO> al_phone_object;
	private DatabaseHandler db;
	private List<NameValuePair> nameValuePairs;
	private String str_response;
	private ArrayList<UsersVO> al_users_list = new ArrayList<UsersVO>();
	private ImageView imgEditProfile;

	public static ContactFragment newInstance() {
		ContactFragment fragment = new ContactFragment();
		return fragment;
	}

	public ContactFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.contact_fragment, container, false);

		setBodyUI();
		return view;
	}

	@SuppressLint("ResourceAsColor")
	private void setBodyUI() {
		activity = getActivity();
		imgEditProfile = (ImageView) view.findViewById(R.id.imgEditProfile);
		imgEditProfile.setVisibility(View.VISIBLE);
		imgEditProfile.setBackgroundResource(R.drawable.icon_refresh);
		imgEditProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (commonMethods.getConnectivityStatus()) {
					db.delete();
					CommonMethods.SetPreferences(activity, "response", "");
					new FillArraylistAsyncTask().execute();

				} else {
					GlobalConfig.showToast(getActivity(), getResources().getString(R.string.internet_error_message));
				}

			}
		});
		str_response = CommonMethods.getPreferences(activity, "response");
		al_phone_object = new ArrayList<ContactVO>();
		db = new DatabaseHandler(activity);
		lv_contact = (ListView) view.findViewById(R.id.lv_contact);
		ll_users = (SelctedLineareLayout) view.findViewById(R.id.ll_users);
		ll_users.setselected(0);
		ll_clipped_user = (LinearLayout) view.findViewById(R.id.ll_clipped_user);
		ll_contact = (LinearLayout) view.findViewById(R.id.ll_contact);
		ll_clipped_user.setOnClickListener(this);
		ll_contact.setOnClickListener(this);
		middletxt = (TextView) view.findViewById(R.id.middletxt);
		middletxt.setText("Users");
		commonMethods = new CommonMethods(activity, this);
		SortingArraylist();
		addDataForGetClippedUsers();
		if (str_response.equals("")) {
			if (commonMethods.getConnectivityStatus()) {
				new GetContactAsyncTask().execute();

			} else {
				GlobalConfig.showToast(getActivity(), getResources().getString(R.string.internet_error_message));
			}
		} else {
			try {
				Gson gson = new Gson();

				UsersVO usersVO = gson.fromJson(str_response, UsersVO.class);

				if (usersVO.getStatus().equals("true")) {
					clippedUserAdapter = new ClippedUserAdapter(activity, getFragmentManager(), usersVO.getData());
					lv_contact.setAdapter(clippedUserAdapter);
				}
		
				if(usersVO.getData().toArray().length > 0)
				{
				
				}
				
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	protected void onHiddenFirstShowcase() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Asynctask for get blupey user
	 * 
	 * @author Hem Pc
	 *
	 */

	public class GetContactAsyncTask extends AsyncTask<String, Process, String> {
		ProgressDialog mprogressDialog;
		String response = "", strMsg, strStatus;

		@Override
		protected void onPreExecute() {
			mprogressDialog = new ProgressDialog(activity);
			mprogressDialog.setMessage("Loading...");
			mprogressDialog.setCancelable(false);
			mprogressDialog.setCanceledOnTouchOutside(false);
			mprogressDialog.show();
		}// Ending onPreExecute()------------------------------------

		@Override
		protected String doInBackground(String... params) {
			al_users_list.clear();
			ArrayList<String> al_signature_strings = new ArrayList<String>();

			al_signature_strings.add("contactsearch");
			al_signature_strings.add("userid");
			al_signature_strings.add("contacts");

			String str_signature = SignatureCommonMenthods.getSignatureForAPI(al_signature_strings);

			HttpClient httpclient = new DefaultHttpClient();
			System.out.println("url" + URLFactory.getContactUrl() + "&signature=" + str_signature);
			HttpPost httppost = new HttpPost(URLFactory.getContactUrl() + "&signature=" + str_signature);

			for (int i = 0; i < nameValuePairs.size(); i++) {

				System.out.println("values////Pramit" + nameValuePairs.get(i).getName() + "---"
						+ nameValuePairs.get(i).getValue());

			}

			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response_http = httpclient.execute(httppost);

				response = commonMethods.getStringFromInputStream(response_http.getEntity().getContent());

				System.out.println("contact response pramit/////" + response.toString());

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}// Ending doInBackground()-----------------------------

		@Override
		protected void onPostExecute(String result) {
			mprogressDialog.dismiss();
			System.out.println("Result" + result);
			if (!(result.equals(null))) {
				// shared_contact.edit().putString("response",
				// result.toString())
				// .commit();
				CommonMethods.SetPreferences(activity, "response", result.toString());

				// str_response = _activity.getSharedPreferences("pref_contact",
				// Context.MODE_PRIVATE).getString("response", "");

				str_response = CommonMethods.getPreferences(activity, "response");

				System.out.println("response.............." + str_response);

				try {
					Gson gson = new Gson();

					UsersVO usersVO = gson.fromJson(str_response, UsersVO.class);

					if (usersVO.getStatus().equals("true")) {
						clippedUserAdapter = new ClippedUserAdapter(activity, getFragmentManager(), usersVO.getData());
						lv_contact.setAdapter(clippedUserAdapter);
//						lv_contact.setFastScrollEnabled(true);
					}
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(activity, "Invalid", Toast.LENGTH_SHORT).show();
				// GlobalConfig.showToast(getActivity(), strMsg);
			}

		}// Ending onPostExecute()------------------------------
	}// ENDING MyAsyncTasks------------------------

	private void SortingArraylist() {
		/**
		 * Method for sorting username in generic type arraylist
		 */

		al_phone_object.clear();
		al_phone_object.addAll(db.getAllContactVOs());
		Collections.sort(al_phone_object, new Comparator<ContactVO>() {

			@Override
			public int compare(ContactVO arg0, ContactVO arg1) {
				return compareToIgnoreCase(arg0, arg1);
			}

			@SuppressLint("DefaultLocale")
			private int compareToIgnoreCase(ContactVO arg0, ContactVO arg1) {
				String Name1 = arg0.getContactName().toUpperCase();
				String Name2 = arg1.getContactName().toUpperCase();

				// ascending order
				return Name1.compareTo(Name2);
			}
		});

		System.out.println("arraylist" + al_phone_object.size());
		contactUserAdapter = new ContactUserAdapter(activity, getFragmentManager(), al_phone_object);
		// lv_contact.setAdapter(contactUserAdapter);
		// lv_contact.setFastScrollEnabled(true);

	}

	private void addDataForGetClippedUsers() {
		al_phone_object = new ArrayList<ContactVO>();

		DatabaseHandler db = new DatabaseHandler(getActivity());
		al_phone_object.addAll(db.getAllContactVOs());
		ArrayList<String> al = new ArrayList<String>();
		String contactJsonArray = null;
		for (int i = 0; i < al_phone_object.size(); i++) {
			al.add(al_phone_object.get(i).getMobile());
		}
		contactJsonArray = new Gson().toJson(al);

		// Add your data
		try {
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("userid", CommonMethods.getPreferences(activity, "userid")));
			nameValuePairs.add(new BasicNameValuePair("contacts", contactJsonArray));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_clipped_user:
			imgEditProfile.setVisibility(View.VISIBLE);
			ll_users.setselected(0);
			lv_contact.setAdapter(clippedUserAdapter);
			break;
		case R.id.ll_contact:
			imgEditProfile.setVisibility(View.INVISIBLE);
			ll_users.setselected(2);
			lv_contact.setAdapter(contactUserAdapter);
//			lv_contact.setFastScrollEnabled(true);
			break;
		default:
			break;
		}

	}

	/**
	 * asynctask for load device contacts
	 * 
	 * @author Hem Pc
	 *
	 */
	public class FillArraylistAsyncTask extends AsyncTask<String, Process, String> {
		private Cursor mCursor;
		ProgressDialog mprogressDialog;

		@Override
		protected void onPreExecute() {
			mprogressDialog = new ProgressDialog(activity);//ProgressDialog.show(activity, null, "");
			mprogressDialog.setMessage("Loading...");//setContentView(new ProgressBar(activity));
			mprogressDialog.setCancelable(false);
			mprogressDialog.setCanceledOnTouchOutside(false);
			mprogressDialog.show();
		}// Ending onPreExecute()------------------------------------

		@Override
		protected String doInBackground(String... params) {

			ContentResolver cr = activity.getContentResolver();
			mCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
			if (mCursor.getCount() > 0) {

				while (mCursor.moveToNext()) {

					if (mCursor == null) {
						return null;
					}

					ContactVO contactVO = new ContactVO(0, null, null, null,0,null);

					int numberColumnIndex = 0;

					String id = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts._ID));
					String name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

					contactVO.setContactName(name);
					contactVO.setContactDataType(0);
					contactVO.setContactDataUrl("");
					numberColumnIndex = mCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

					int photoColumnIndex = 0;
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						photoColumnIndex = mCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
					} else {
						photoColumnIndex = mCursor.getColumnIndex(ContactsContract.Contacts._ID);
					}
					if (photoColumnIndex >= 0 && mCursor.getString(photoColumnIndex) != null) {
						String photoData = mCursor.getString(photoColumnIndex);
						// Creates a holder for the URI.
						Uri thumbUri;
						// If Android 3.0 or later
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							// Sets the URI from the incoming
							// PHOTO_THUMBNAIL_URI
							thumbUri = Uri.parse(photoData);
							System.out.println("thumbanail" + thumbUri);
							
							
							if (thumbUri.toString() == null) {
								Uri path = Uri.parse("android.resource://org.ninehertzindia.clipped/" + R.drawable.profile_icon);
								contactVO.setContactImageUri(path.toString());
							}
							else {
								
								contactVO.setContactImageUri(thumbUri.toString());
							}
							
						} else {
							// Prior to Android 3.0, constructs a photo Uri
							// using _ID
							/*
							 * Creates a contact URI from the Contacts content
							 * URI incoming photoData (_ID)
							 */
							@SuppressWarnings("deprecation")
							final Uri contactUri = Uri.withAppendedPath(Contacts.CONTENT_URI, photoData);
							/*
							 * Creates a photo URI by appending the content URI
							 * of Contacts.Photo.
							 */
							thumbUri = Uri.withAppendedPath(contactUri, Photo.CONTENT_DIRECTORY);
							System.out.println("thumbanail" + thumbUri);
							contactVO.setContactImageUri(thumbUri.toString());
						}
					} 

					if (Integer.parseInt(mCursor.getString(numberColumnIndex)) > 0) {

						System.out.println("33333333 Contact Name: " + name);
						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
						while (pCur.moveToNext()) {
							String phone = pCur
									.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
									.replace(" ", "");
							System.out.println("33333333 phone : " + phone);
							if (!phone.contains("#")) {

								phone = phone.replaceAll("[()]", "");
								phone = phone.replaceAll("-", "");

								if (phone.length() == 10) {
									contactVO.setMobile(phone);
									// Inserting Contacts
									Log.d("Insert: ", "Inserting ..");
									db.addContactVO(contactVO);
								} else if (phone.length() > 10) {

									contactVO.setMobile(phone.substring(phone.length() - 10, phone.length()));
									// Inserting Contacts
									Log.d("Insert: ", "Inserting ..");
									db.addContactVO(contactVO);

								}
							}

							break;
						}
						pCur.close();
					}
				}
			}
			return null;
		}// Ending doInBackground()-----------------------------

		@Override
		protected void onPostExecute(String result) {
			mprogressDialog.dismiss();
			addDataForGetClippedUsers();
			if (commonMethods.getConnectivityStatus()) {
				new GetContactAsyncTask().execute();

			} else {
				GlobalConfig.showToast(getActivity(), getResources().getString(R.string.internet_error_message));
			}
		}// Ending onPostExecute()------------------------------
	}// ENDING MyAsyncTasks------------------------

}
