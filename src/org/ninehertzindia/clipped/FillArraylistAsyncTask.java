package org.ninehertzindia.clipped;

import org.ninehertzindia.clipped.util.ContactVO;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import org.ninehertzindia.clipped.R;
import org.ninehertzindia.clipped.database.DatabaseHandler;

import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts.Photo;
import android.util.Log;

public class FillArraylistAsyncTask extends AsyncTask<String, Process, String> {
	private Cursor mCursor;

	private Activity async;
	
	DatabaseHandler db;
	
	
	public FillArraylistAsyncTask(Activity activity) {

		this.async = activity;
		 db= new DatabaseHandler(async);
	
	}
	@Override
	protected void onPreExecute() {
	}// Ending onPreExecute()------------------------------------

	@Override
	protected String doInBackground(String... params) {
		
		
		System.out.println("hhhhhhhhhhhhh Do in BG");

		ContentResolver cr = async.getContentResolver();
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
						System.out.println("hhhhhhhthumbanail" + thumbUri.toString());
						
						
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

							System.out.println("all count contact" + db.getContactVOsCount());
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

	}// Ending onPostExecute()------------------------------
}// ENDING MyAsyncTasks------------------------