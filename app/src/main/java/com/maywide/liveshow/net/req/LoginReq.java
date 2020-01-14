package com.maywide.liveshow.net.req;

/**
 * Created by user on 2018/11/14.
 */

public class LoginReq extends BaseReq{

	//用户密码
	private String password;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
