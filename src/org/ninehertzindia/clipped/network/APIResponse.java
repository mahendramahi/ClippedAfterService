package org.ninehertzindia.clipped.network;

public class APIResponse {
	
	private String response;
	private int code = 404;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}