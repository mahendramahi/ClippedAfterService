package org.ninehertzindia.clipped.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * class is used for saving contact details
 * 
 * @author Pramit Chaturvedi
 * 
 */
public class ContactVO implements Parcelable {

	private int _id;
	private String contactName;
	private String mobile;
	private String contactImageUri;
	private int contactDataType;
	private String contactDataUrl;

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

	public ContactVO(int i, String string, String string2, String string3, int string4, String string5) {
		
		this._id = i;
		this.contactName = string;
		this.mobile = string2;
		this.contactImageUri = string3;
		this.contactDataType = string4;
		this.contactDataUrl = string5;
		
		
	}

	public ContactVO(Parcel in) {
		// This order must match the order in writeToParcel()
		contactName = in.readString();
		mobile = in.readString();
		contactImageUri = in.readString();
		contactDataType = in.readInt();
		contactDataUrl = in.readString();
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
		out.writeInt(contactDataType);
		out.writeString(contactDataUrl);
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

	public int getContactDataType() {
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
	}

	// Just cut and paste this for now
	public static final Parcelable.Creator<ContactVO> CREATOR = new Parcelable.Creator<ContactVO>() {
		public ContactVO createFromParcel(Parcel in) {
			return new ContactVO(in);
		}

		public ContactVO[] newArray(int size) {
			return new ContactVO[size];
		}
	};

}
