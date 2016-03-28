package org.ninehertzindia.clipped.util;

import java.util.ArrayList;

public class UsersVO {

	public class Data {
		private String userid;
		// private String country_code;
		// private String country_name;
		private String mobile;
		// private String tem_mobile;
		private String name;
		// private String email;
		// private String gender;
		// private String location;
		// private String lat;
		// private String lng;
		private String image;
		private String thumb_image;
		// private String otp;
		// private String otp_verify;
		// private String status;
		private String dataType;
		private String dataUrl;

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getThumb_image() {
			return thumb_image;
		}

		public void setThumb_image(String thumb_image) {
			this.thumb_image = thumb_image;
		}

		public String getStatus() {
			return status;
		}

		public String getDtdate() {
			return dtdate;
		}

		public void setDtdate(String dtdate) {
			this.dtdate = dtdate;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getDataUrl() {
			return dataUrl;
		}

		public void setDataUrl(String dataUrl) {
			this.dataUrl = dataUrl;
		}

		private String dtdate;

	}

	private String status;
	private String msg;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ArrayList<Data> getData() {
		return data;
	}

	public void setData(ArrayList<Data> data) {
		this.data = data;
	}

	private ArrayList<Data> data;

}
