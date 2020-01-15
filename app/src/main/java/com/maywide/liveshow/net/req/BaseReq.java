package com.maywide.liveshow.net.req;

/**
 * Created by user on 2018/11/14.
 */

public class BaseReq {
	//手机号
	protected String phone;
	//token
	protected String token;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
