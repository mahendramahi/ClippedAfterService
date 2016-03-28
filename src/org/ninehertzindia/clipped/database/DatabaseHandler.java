package org.ninehertzindia.clipped.database;

import java.util.ArrayList;
import java.util.List;

import org.ninehertzindia.clipped.IncomingCallScreenActivity;
import org.ninehertzindia.clipped.util.ContactData;
import org.ninehertzindia.clipped.util.ContactVO;
import org.ninehertzindia.clipped.util.HistoryVO;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

public class DatabaseHandler extends SQLiteOpenHelper {

	private Activity activity;

	/*
	 * public DatabaseHandler(Activity activity) {
	 * 
	 * this.activity = activity;
	 * 
	 * }
	 */

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "ContactVOsManager";

	// ContactVOs table name
	private static final String TABLE_ContactVOS = "ContactVOs";
	private static final String TABLE_ContactData = "ContactData";
	private static final String TABLE_HISTORY = "History";

	// ContactVOs Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PH_NO = "mobile";
	private static final String KEY_IMAGE_URI = "image_uri";
	private static final String KEY_DataType = "data_type";
	private static final String KEY_DataUrl = "data_url";
	private static final String KEY_TIME = "time";
	private static final String KEY_DATE = "date";
	private static final String KEY_STATUS = "status";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// this.activity = context
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_ContactVOS_TABLE = "CREATE TABLE " + TABLE_ContactVOS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT," + KEY_PH_NO + " TEXT," + KEY_IMAGE_URI + " TEXT," + KEY_DataType + " INTEGER,"
				+ KEY_DataUrl + " TEXT" + ")";
		String CREATE_ContactData_TABLE = "CREATE TABLE " + TABLE_ContactData + "(" + KEY_ID + " INTEGER," + KEY_NAME
				+ " TEXT," + KEY_PH_NO + " TEXT," + KEY_IMAGE_URI + " TEXT," + KEY_DataType + " INTEGER," + KEY_DataUrl
				+ " TEXT" + ")";
		String CREATE_HISTROY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "(" + KEY_ID + " INTEGER," + KEY_NAME + " TEXT,"
				+ KEY_PH_NO + " TEXT," + KEY_IMAGE_URI + " TEXT," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT,"
				+ KEY_STATUS + " TEXT" + ")";

		db.execSQL(CREATE_ContactVOS_TABLE);
		db.execSQL(CREATE_ContactData_TABLE);
		db.execSQL(CREATE_HISTROY_TABLE);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ContactVOS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ContactData);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);

		// Create tables again
		onCreate(db);
	}

	// Adding new ContactVO
	public void addContactVO(ContactVO ContactVO) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, ContactVO.getContactName()); // ContactVO Name
		values.put(KEY_PH_NO, ContactVO.getMobile()); // ContactVO Phone
		values.put(KEY_IMAGE_URI, ContactVO.getContactImageUri());// ContactVO
																	// Image uri
		values.put(KEY_DataType, ContactVO.getContactDataType());
		values.put(KEY_DataUrl, ContactVO.getContactDataUrl());
		// Inserting Row
		db.insert(TABLE_ContactVOS, null, values);
		db.close(); // Closing database connection
	}

	// Adding new ContactVO
	public void addHistoryVO(HistoryVO HistoryVO) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, HistoryVO.getContactName()); // ContactVO Name
		values.put(KEY_PH_NO, HistoryVO.getMobile()); // ContactVO Phone
		values.put(KEY_IMAGE_URI, HistoryVO.getContactImageUri());// ContactVO
		// Image uri
		values.put(KEY_DATE, HistoryVO.getContactdate());
		values.put(KEY_TIME, HistoryVO.getContacttime());
		values.put(KEY_STATUS, HistoryVO.getContactstatus());
		// Inserting Row
		db.insert(TABLE_HISTORY, null, values);
		db.close(); // Closing database connection
	}

	// Adding new ContactVO
	public void addContactData(ContactData ContactData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, ContactData.getContactName()); // ContactVO Name
		values.put(KEY_PH_NO, ContactData.getMobile()); // ContactVO Phone
		values.put(KEY_IMAGE_URI, ContactData.getContactImageUri());// ContactVO
		// Image uri
		values.put(KEY_DataType, ContactData.getContactDataType());
		values.put(KEY_DataUrl, ContactData.getContactDataUrl());
		// Inserting Row
		db.insert(TABLE_ContactData, null, values);
		db.close(); // Closing database connection
	}

	public String GetContectName(Context context, String no) {

		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(no));

		String[] Mphone = { PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };

		Cursor c = context.getContentResolver().query(uri, Mphone, null, null, null);

		try {

			if (c.moveToFirst()) {
				String name = c.getString(c.getColumnIndex(PhoneLookup.DISPLAY_NAME));
				return name;
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {

			if (c != null) {
				c.close();
			}
		}

		return no;
	}

	// Getting single ContactVO
	public ContactVO getContactVO(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ContactVOS,
				new String[] { KEY_ID, KEY_NAME, KEY_PH_NO, KEY_IMAGE_URI, KEY_DataType, KEY_DataUrl },
				KEY_PH_NO + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

		// Cursor cursor2 = db.q
		// Cursor cursor2 = db.

		if (cursor != null)
			cursor.moveToFirst();

		// String lol = cursor.getString(1);

		ContactVO contactVO = new ContactVO(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5));
		return contactVO;
		// return lol;
	}

	public HistoryVO getHistoryVO(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_HISTORY,
				new String[] { KEY_ID, KEY_NAME, KEY_PH_NO, KEY_IMAGE_URI, KEY_DATE, KEY_TIME, KEY_STATUS },
				KEY_PH_NO + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

		// Cursor cursor2 = db.q
		// Cursor cursor2 = db.

		if (cursor != null)
			cursor.moveToFirst();

		// String lol = cursor.getString(1);

		HistoryVO historyVO = new HistoryVO(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
				cursor.getString(6));
		return historyVO;
		// return lol;
	}

	// Getting single ContactVO
	public ContactData getContactData(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ContactData,
				new String[] { KEY_ID, KEY_NAME, KEY_PH_NO, KEY_IMAGE_URI, KEY_DataType, KEY_DataUrl },
				KEY_PH_NO + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

		// Cursor cursor2 = db.q
		// Cursor cursor2 = db.

		if (cursor != null)
			cursor.moveToFirst();

		// String lol = cursor.getString(1);

		ContactData contactdata = new ContactData(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
				cursor.getString(3), cursor.getInt(4), cursor.getString(5));
		return contactdata;
		// return lol;
	}

	// Getting single ContactDataStatus
	public int getContactDataStatus(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ContactData,
				new String[] { KEY_ID, KEY_NAME, KEY_PH_NO, KEY_IMAGE_URI, KEY_DataType, KEY_DataUrl },
				KEY_PH_NO + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

		// Cursor cursor2 = db.q
		// Cursor cursor2 = db.

		int i = cursor.getCount();
		/*
		 * if (cursor != null) cursor.moveToFirst();
		 */
		// String lol = cursor.getString(1);

		/*
		 * ContactData contactdata = new
		 * ContactData(Integer.parseInt(cursor.getString(0)),
		 * cursor.getString(1), cursor.getString(2), cursor.getString(3),
		 * cursor.getInt(4), cursor.getString(5)); return contactdata;
		 */
		return i;
	}

	// Getting single HistoryDataStatus
	public int getHistoryDataStatus(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_HISTORY,
				new String[] { KEY_ID, KEY_NAME, KEY_PH_NO, KEY_IMAGE_URI, KEY_DATE, KEY_TIME, KEY_STATUS },
				KEY_PH_NO + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

		// Cursor cursor2 = db.q
		// Cursor cursor2 = db.

		int i = cursor.getCount();
		/*
		 * if (cursor != null) cursor.moveToFirst();
		 */
		// String lol = cursor.getString(1);

		/*
		 * ContactData contactdata = new
		 * ContactData(Integer.parseInt(cursor.getString(0)),
		 * cursor.getString(1), cursor.getString(2), cursor.getString(3),
		 * cursor.getInt(4), cursor.getString(5)); return contactdata;
		 */
		return i;
	}

	// Getting All ContactVOs
	public List<ContactVO> getAllContactVOs() {
		List<ContactVO> ContactVOList = new ArrayList<ContactVO>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ContactVOS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ContactVO ContactVO = new ContactVO(0, selectQuery, selectQuery, selectQuery, 0, selectQuery);
				ContactVO.set_id(Integer.parseInt(cursor.getString(0)));
				ContactVO.setContactName(cursor.getString(1));
				ContactVO.setMobile(cursor.getString(2));
				ContactVO.setContactImageUri(cursor.getString(3));
				ContactVO.setContactDataType(cursor.getInt(4));
				ContactVO.setContactDataUrl(cursor.getString(5));
				// Adding ContactVO to list
				ContactVOList.add(ContactVO);
			} while (cursor.moveToNext());
		}

		// return ContactVO list
		return ContactVOList;
	}

	// Getting All HistoryVO
	public List<HistoryVO> getAllHistoryVOs() {
		List<HistoryVO> HistoryVOList = new ArrayList<HistoryVO>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_HISTORY;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				HistoryVO HistoryVO = new HistoryVO(0, selectQuery, selectQuery, selectQuery, selectQuery, selectQuery,
						selectQuery);
				HistoryVO.set_id(Integer.parseInt("1"));
				HistoryVO.setContactName(cursor.getString(1));
				HistoryVO.setMobile(cursor.getString(2));
				HistoryVO.setContactImageUri(cursor.getString(3));
				HistoryVO.setContactdate(cursor.getString(4));
				HistoryVO.setContacttime(cursor.getString(5));
				HistoryVO.setContactstatus(cursor.getString(6));
				// Adding ContactVO to list
				HistoryVOList.add(HistoryVO);
			} while (cursor.moveToNext());
		}

		// return HistoryVO list
		return HistoryVOList;
	}

	// Getting All ContactVOs
	public List<ContactData> getAllContactData() {
		List<ContactData> ContactDataList = new ArrayList<ContactData>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ContactData;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ContactData ContactVO = new ContactData(0, selectQuery, selectQuery, selectQuery, 0, selectQuery);
				ContactVO.set_id(Integer.parseInt(cursor.getString(0)));
				ContactVO.setContactName(cursor.getString(1));
				ContactVO.setMobile(cursor.getString(2));
				ContactVO.setContactImageUri(cursor.getString(3));
				ContactVO.setContactDataType(cursor.getInt(4));
				ContactVO.setContactDataUrl(cursor.getString(5));
				// Adding ContactVO to list
				ContactDataList.add(ContactVO);
			} while (cursor.moveToNext());
		}

		// return ContactVO list
		return ContactDataList;
	}

	// Updating single ContactVO
	public int updateContactVO(ContactVO ContactVO) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, ContactVO.getContactName());
		values.put(KEY_PH_NO, ContactVO.getMobile());
		values.put(KEY_IMAGE_URI, ContactVO.getContactImageUri());
		values.put(KEY_DataType, ContactVO.getContactDataType());
		values.put(KEY_DataUrl, ContactVO.getContactDataUrl());

		// updating row
		return db.update(TABLE_ContactVOS, values, KEY_PH_NO + " = ?",
				new String[] { String.valueOf(ContactVO.getMobile()) });
	}

	// Updating single HistoryVO
	public int updateHistoryVO(HistoryVO HistoryVO) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, HistoryVO.getContactName());
		values.put(KEY_PH_NO, HistoryVO.getMobile());
		values.put(KEY_IMAGE_URI, HistoryVO.getContactImageUri());
		values.put(KEY_DATE, HistoryVO.getContactdate());
		values.put(KEY_TIME, HistoryVO.getContacttime());
		values.put(KEY_STATUS, HistoryVO.getContactstatus());

		// updating row
		return db.update(TABLE_HISTORY, values, KEY_PH_NO + " = ?",
				new String[] { String.valueOf(HistoryVO.getMobile()) });
	}

	// Updating single ContactData
	public int updateContactData(ContactData ContactData) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, ContactData.getContactName());
		values.put(KEY_PH_NO, ContactData.getMobile());
		values.put(KEY_IMAGE_URI, ContactData.getContactImageUri());
		values.put(KEY_DataType, ContactData.getContactDataType());
		values.put(KEY_DataUrl, ContactData.getContactDataUrl());

		// updating row
		return db.update(TABLE_ContactData, values, KEY_PH_NO + " = ?",
				new String[] { String.valueOf(ContactData.getMobile()) });
	}

	// public int UpdateContactVoByMe(int position,String mobile,String
	// imageuri,int dtattype,String DataUrl)
	// {
	// SQLiteDatabase db = this.getWritableDatabase();
	//
	// ContentValues values = new ContentValues();
	//
	//
	// return dtattype;
	// }
	//

	// Deleting single ContactVO
	public void deleteContactVO(ContactVO ContactVO) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ContactVOS, KEY_ID + " = ?", new String[] { String.valueOf(ContactVO.get_id()) });
		db.close();
	}

	// Deleting single ContactVO
	public void deleteContactData(ContactData ContactData) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ContactData, KEY_ID + " = ?", new String[] { String.valueOf(ContactData.get_id()) });
		db.close();
	}

	// Deleting single HistoryVO
	public void deleteHostoryData(HistoryVO HistoryVO) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_HISTORY, KEY_ID + " = ?", new String[] { String.valueOf(HistoryVO.get_id()) });
		db.close();
	}

	// Delete table
	public void delete() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ContactVOS, null, null);
		db.delete(TABLE_ContactData, null, null);
		// db.delete(TABLE_HISTORY, null, null);
		// db.delete(TABLE_ContactClippedUSer, null, null);
	}

	// Delete table
	public void deleteHistoryTable() {
		SQLiteDatabase db = this.getWritableDatabase();
//		 db.delete(TABLE_ContactVOS, null, null); db.delete(TABLE_ContactData,
//		  null, null);
		 
		db.delete(TABLE_HISTORY, null, null);
		// db.delete(TABLE_ContactClippedUSer, null, null);
	}

	// Getting ContactVOs Count
	public int getContactVOsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_ContactVOS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		// cursor.close();

		// return count
		return cursor.getCount();
	}

	// Getting ContactVOs Count
	public int getContactDatasCount() {
		String countQuery = "SELECT  * FROM " + TABLE_ContactData;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		// cursor.close();

		// return count
		return cursor.getCount();
	}

	// Getting HistoryVOs Count
	public int getHistoryCount() {
		String countQuery = "SELECT  * FROM " + TABLE_HISTORY;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		// cursor.close();

		// return count
		return cursor.getCount();
	}

}
