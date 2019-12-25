package com.maywide.liveshow.net.resp;

import java.io.Serializable;

/**
 * Created by user on 2018/11/14.
 */

public class LoginResp implements Serializable{
	//手机号
	private String mobile;
	//最后一次获得的动态密码
	private String verification;
	//用户密码
	private String password;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
