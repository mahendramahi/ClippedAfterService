package org.ninehertzindia.clipped;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.ninehertzindia.clipped.database.DatabaseHandler;
import org.ninehertzindia.clipped.util.CommonMethods;
import org.ninehertzindia.clipped.util.ContactData;
import org.ninehertzindia.clipped.util.ContactVO;
import org.ninehertzindia.clipped.util.URLFactory;

import com.google.android.gms.internal.na;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;

public class DownloadDataService extends Service {

	private String popupname;
	private String popupdata;
	private String popupfiletype;
	private String popupmobile;
	private String popupdataname;
	private ContactData contactData;
	OutputStream output;
	private String temp_fileex;
	File root;
	public static File dir;

	private int iFilrTpe;

	public static String MyLocalPath = "/sdcard/Android/data/org.ninehertzindia.clipped/files/DCIM/.Clipped";

	private DatabaseHandler db;

	public static DownloadManager dm;
	private static long myDownloadRefrance;
	public static BroadcastReceiver receiverDownloadComplete;
	public static BroadcastReceiver receiverDownloadNotificationClick;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		db = new DatabaseHandler(getApplicationContext());

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		dm = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// root = android.os.Environment.getExternalStorageDirectory();

		dir = new File(Environment.DIRECTORY_DCIM + "/.Clipped/");
		if (dir.exists() == false) {
			dir.mkdirs();
		}

		System.out.println("hhhhhhhhhhhhDownload Service Start");
		// TODO Auto-generated method stub

		try {

			popupdata = intent.getStringExtra("data");

			popupfiletype = intent.getStringExtra("filetype");
			popupname = intent.getStringExtra("name");
			popupmobile = intent.getStringExtra("mobile");
			popupdataname = intent.getStringExtra("dataname");

			iFilrTpe = Integer.parseInt(popupfiletype);

			if (popupfiletype.equalsIgnoreCase("1")) {

				temp_fileex = ".mp3";

			}
			if (popupfiletype.equalsIgnoreCase("2")) {

				temp_fileex = ".mp4";

			}

			System.out.println("hhhhh Service:" + popupdata + "filetype:" + popupfiletype + "name:" + popupname
					+ "mobile:" + popupmobile + ":dataname:" + popupdataname);

			/////////////////

			// TEXT

			/////////

			if (popupfiletype.equalsIgnoreCase("3")) {

				System.out.println("hhhhhhhhhhhhhhhh text Ayya file mai");

				int b = db.getContactDataStatus(popupmobile);

				System.out.println("hhhhhhhhhhhhhhhh ContactStatus:" + b);

				System.out.println("hhhhhhhhhhhhhhhh ContactData " + contactData);

				if (b == 0) {

					System.out.println("hhhhhhhhhhhhhhhh  get count  0 hua hai so entry ki gai");

					ContactData contactData = new ContactData(0, null, null, null, 0, null);

					contactData.setContactDataType(3);
					contactData.setContactDataUrl(popupdata);
					contactData.setContactImageUri("");
					contactData.setContactName(popupname);
					contactData.setMobile(popupmobile);

					db.addContactData(contactData);

					System.out.println("hhhhhhhhValue  " + contactData);

					System.out.println("hhhhhhhhhhhhhhhh Data Type " + contactData.getContactDataType());
					System.out.println("hhhhhhhhhhhhhhhh Name" + contactData.getContactName());
					System.out.println("hhhhhhhhhhhhhhhh Mobile" + contactData.getMobile());

				} else {

					System.out.println("hhhhhhhhhhhhhhhh  get count  se jyada hai so update ");

					ContactData lolContactData = db.getContactData(popupmobile);

					lolContactData.setContactDataType(3);
					lolContactData.setContactDataUrl(popupdata);

					try {
						System.out.println("hhhhh " + db.updateContactData(lolContactData));
						db.updateContactData(lolContactData);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// GlobalConfig.showToast(activity, "Successfully Video
					// assigned.");
					System.out
							.println("hhhhh.....after data type" + db.getContactData(popupmobile).getContactDataType());

					try {
						// stopService(new Intent(getBaseContext(),
						// DownloadDataService.class));
						System.out.println("hhhhhhhhhhhhh stop service");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("hhhhhhhhhhhhh stop service" + e);
					}
				}

			} else {

				System.out.println("hhhh Else Call popupdata:" + popupdata + ":dataname:" + popupdataname + ":filetype:"
						+ popupfiletype + ":mobile:" + popupmobile);
				if (popupdata != null) {

					// new DownloadBG().execute();

					/////////////////
					// Audio Video k Ly

					/////////

					int b = db.getContactDataStatus(popupmobile);

					System.out.println("hhhhhhhhhhhhhhhh ContactStatus:" + b);

					System.out.println("hhhhhhhhhhhhhhhh ContactData " + contactData);

					if (b == 0) {

						System.out.println("hhhhhhhhhhhhhhhh  get count  0 hua hai so entry ki gai");

						ContactData contactData = new ContactData(0, null, null, null, 0, null);

						contactData.setContactDataType(iFilrTpe);
						contactData
								.setContactDataUrl("/sdcard/Android/data/org.ninehertzindia.clipped/files/DCIM/.Clipped"
										+ "/" + popupdataname);
						contactData.setContactImageUri("");
						contactData.setContactName(popupname);
						contactData.setMobile(popupmobile);
						// contactData.set_id(1);

						db.addContactData(contactData);

						System.out.println("hhhhhhhhValue  " + contactData);

						System.out.println("hhhhhhhhhhhhhhhh Data Type " + contactData.getContactDataType());
						System.out.println("hhhhhhhhhhhhhhhh Name" + contactData.getContactName());
						System.out.println("hhhhhhhhhhhhhhhh Mobile" + contactData.getMobile());

					} else {

						System.out.println("hhhhhhhhhhhhhhhh  get count  se jyada hai so update ");

						ContactData lolContactData = db.getContactData(popupmobile);

						lolContactData.setContactDataType(Integer.parseInt(popupfiletype));
						lolContactData.setContactDataUrl(MyLocalPath + "/" + popupdataname);

						try {
							System.out.println("hhhhh " + db.updateContactData(lolContactData));
							db.updateContactData(lolContactData);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// GlobalConfig.showToast(activity, "Successfully Video
						// assigned.");
						System.out.println(
								"hhhhh.....after data type" + db.getContactData(popupmobile).getContactDataType());

					}

					System.out.println("hhhh Else not null Call popupdata:" + popupdata + ":dataname:" + popupdataname
							+ ":filetype:" + popupfiletype + ":mobile:" + popupmobile);

					/*
					 * Uri uri = Uri.parse(
					 * "http://outdoorbazzar.com/myfile/myimage/vidhyadhar_nagar_near_fun_cinema.jpg"
					 * );
					 * 
					 * System.out.println("hhhh url"+""+URLFactory.baseUrl +
					 * popupdata);
					 * 
					 * DownloadManager.Request request = new
					 * DownloadManager.Request(uri);
					 * 
					 * request.setDescription("Clipped Data"
					 * ).setTitle("Download");
					 * 
					 * System.out.println("hhhh diractory"+dir+"/");
					 * 
					 * System.out.println("hhhh popupdataname:"+popupdataname);
					 * 
					 * request.setDestinationInExternalFilesDir(this,Environment
					 * .DIRECTORY_DCIM , "mm.jpg");
					 * request.setVisibleInDownloadsUi(true);
					 * request.setAllowedNetworkTypes(DownloadManager.Request.
					 * NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
					 * 
					 * myDownloadRefrance = dm.enqueue(request);
					 */

					AssignedDataDownload(URLFactory.baseUrl + popupdata, popupdataname);

					IntentFilter filter = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);

					receiverDownloadNotificationClick = new BroadcastReceiver() {

						@Override
						public void onReceive(Context context, Intent intent) {

							String extraID = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;

							long[] refrences = intent.getLongArrayExtra(extraID);

							for (long reference : refrences) {
								if (reference == myDownloadRefrance) {

								}
							}

						}

					};
					registerReceiver(receiverDownloadNotificationClick, filter);
					
					
					
					// regist
					// this.
					/*
					 * IntentFilter filter2 = new
					 * IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
					 * 
					 * receiverDownloadComplete = new BroadcastReceiver() {
					 * 
					 * @Override public void onReceive(Context context, Intent
					 * intent) {
					 * 
					 * long refrance =
					 * intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,
					 * -1);
					 * 
					 * if (myDownloadRefrance == refrance) {
					 * 
					 * DownloadManager.Query query = new
					 * DownloadManager.Query();
					 * 
					 * query.setFilterById(refrance);
					 * 
					 * Cursor cursor = dm.query(query);
					 * 
					 * cursor.moveToFirst();
					 * 
					 * int columnIndex =
					 * cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
					 * 
					 * int status = cursor.getInt(columnIndex);
					 * 
					 * int fileNameIndex =
					 * cursor.getColumnIndex(DownloadManager.
					 * COLUMN_LOCAL_FILENAME);
					 * 
					 * String savedFilePath = cursor.getString(fileNameIndex);
					 * 
					 * int columnReason =
					 * cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
					 * 
					 * int reason = cursor.getInt(columnReason); cursor.close();
					 * 
					 * // Switch(status) switch (status) { case
					 * DownloadManager.STATUS_SUCCESSFUL:
					 * 
					 * System.out.println("hhhhhhhhh Successfully");
					 * 
					 * //DownloadManager.
					 * 
					 * break;
					 * 
					 * case DownloadManager.STATUS_FAILED:
					 * 
					 * System.out.println("hhhhhhhhh FAiled");
					 * 
					 * break; case DownloadManager.STATUS_PAUSED:
					 * 
					 * System.out.println("hhhhhhhhh Pause");
					 * 
					 * break; case DownloadManager.STATUS_PENDING:
					 * 
					 * System.out.println("hhhhhhhhh Pending");
					 * 
					 * break; case DownloadManager.STATUS_RUNNING:
					 * 
					 * System.out.println("hhhhhhhhh Running");
					 * 
					 * break; }
					 * 
					 * }
					 * 
					 * 
					 * } };
					 */

				}
			}

		} catch (Exception e) {
		}

		return super.onStartCommand(intent, flags, startId);
	}

	public class DownloadBG extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			int count;

			if (CommonMethods.getMahiConnectivityStatus()) {

				try {

					System.out.println("hhhhhhh url" + popupdata);
					URL urls = new URL(URLFactory.baseUrl + popupdata);
					URLConnection connection = urls.openConnection();
					connection.connect();

					// this will be useful so that you can show a tipical 0-100%
					// progress bar

					int lenghtOfFile = connection.getContentLength();

					InputStream input = new BufferedInputStream(urls.openStream());
					OutputStream output = new FileOutputStream(dir + "/" + popupdataname);

					byte data[] = new byte[1024];

					long total = 0;

					while ((count = input.read(data)) != -1) {
						total += count;
						// publishing the progress....
						// publishProgress((int) (total * 100 / lenghtOfFile));
						output.write(data, 0, count);
					}

					output.flush();
					output.close();
					input.close();

				} catch (Exception e) {
					System.out.println("hhhhhh" + e);
				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			System.out.println("hhhhhhhh OnPostExecute ");
			super.onPostExecute(result);

			/////////////////
			// Audio Video k Ly

			/////////

			int b = db.getContactDataStatus(popupmobile);

			System.out.println("hhhhhhhhhhhhhhhh ContactStatus:" + b);

			System.out.println("hhhhhhhhhhhhhhhh ContactData " + contactData);

			if (b == 0) {

				System.out.println("hhhhhhhhhhhhhhhh  get count  0 hua hai so entry ki gai");

				ContactData contactData = new ContactData(0, null, null, null, 0, null);

				contactData.setContactDataType(iFilrTpe);
				contactData.setContactDataUrl(dir + "/" + popupdataname);
				contactData.setContactImageUri("");
				contactData.setContactName(popupname);
				contactData.setMobile(popupmobile);
				// contactData.set_id(1);

				db.addContactData(contactData);

				System.out.println("hhhhhhhhValue  " + contactData);

				System.out.println("hhhhhhhhhhhhhhhh Data Type " + contactData.getContactDataType());
				System.out.println("hhhhhhhhhhhhhhhh Name" + contactData.getContactName());
				System.out.println("hhhhhhhhhhhhhhhh Mobile" + contactData.getMobile());

			} else {

				System.out.println("hhhhhhhhhhhhhhhh  get count  se jyada hai so update ");

				ContactData lolContactData = db.getContactData(popupmobile);

				lolContactData.setContactDataType(Integer.parseInt(popupfiletype));
				lolContactData.setContactDataUrl(dir + "/" + popupdataname);

				try {
					System.out.println("hhhhh " + db.updateContactData(lolContactData));
					db.updateContactData(lolContactData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// GlobalConfig.showToast(activity, "Successfully Video
				// assigned.");
				System.out.println("hhhhh.....after data type" + db.getContactData(popupmobile).getContactDataType());

			}
			// db.addContactData();

			try {
				// stopService(new Intent(getBaseContext(),
				// DownloadDataService.class));
				System.out.println("hhhhhhhhhhhhh stop service");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("hhhhhhhhhhhhh stop service" + e);
			}
		}

	}

	public void AssignedDataDownload(String url, String name) {

		Uri uri = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(uri);

		request.setDescription("Download").setTitle("MyClipped");
		request.setDestinationInExternalFilesDir(this, "" + dir, "" + name);

		request.setVisibleInDownloadsUi(true);
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

		myDownloadRefrance = dm.enqueue(request);
	}

}