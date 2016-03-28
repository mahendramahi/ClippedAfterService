package org.ninehertzindia.clipped.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoryVO implements Parcelable {

	private int _id;
	private String contactName;
	private String mobile;
	private String contactImageUri;
	private String contactdate;
	private String contacttime;
	private String contactstatus;
	
	/*private int contactDataType;
	private String contactDataUrl;*/

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		// contactName.replace("'", "\\'");
		this.contactName = contactName;

	}

	public String getContactImageUri() {
		return contactImageUri;
	}

	public void setContactImageUri(String contactImageUri) {
		try {
			this.contactImageUri = contactImageUri == null ? "" : URLDecoder.decode(contactImageUri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HistoryVO(int i, String string, String string2, String string3, String string4, String string5, String string6) {
		
		this._id = i;
		this.contactName = string;
		this.mobile = string2;
		this.contactImageUri = string3;
		this.contactdate = string4;
		this.contacttime = string5;
		this.contactstatus = string6;
		
	}

	public HistoryVO(Parcel in) {
		// This order must match the order in writeToParcel()
		contactName = in.readString();
		mobile = in.readString();
		contactImageUri = in.readString();
		contactdate = in.readString();
		contacttime = in.readString();
		contactstatus = in.readString();
		// Continue doing this for the rest of your member data
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(contactName);
		out.writeString(mobile);
		out.writeString(contactImageUri);
		out.writeString(contactdate);
		out.writeString(contacttime);
		out.writeString(contactstatus);
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/*public int getContactDataType() {
		return contactDataType;
	}

	public void setContactDataType(int contactDataType) {
		this.contactDataType = contactDataType;
	}

	public String getContactDataUrl() {
		return contactDataUrl;
	}

	public void setContactDataUrl(String contactDataUrl) {
		this.contactDataUrl = contactDataUrl;
	}*/

	public String getContacttime() {
		return contacttime;
	}

	public void setContacttime(String contacttime) {
		this.contacttime = contacttime;
	}

	public String getContactdate() {
		return contactdate;
	}

	public void setContactdate(String contactdate) {
		this.contactdate = contactdate;
	}

	public String getContactstatus() {
		return contactstatus;
	}

	public void setContactstatus(String contactstatus) {
		this.contactstatus = contactstatus;
	}

	// Just cut and paste this for now
	public static final Parcelable.Creator<HistoryVO> CREATOR = new Parcelable.Creator<HistoryVO>() {
		public HistoryVO createFromParcel(Parcel in) {
			return new HistoryVO(in);
		}

		public HistoryVO[] newArray(int size) {
			return new HistoryVO[size];
		}
	};

}
