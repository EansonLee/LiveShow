package com.maywide.liveshow.net.req;

/**
 * Created by user on 2018/11/14.
 */

public class LoginReq extends BaseReq{

	//登录类型  0验证码 1密码
	private String type;
	//用户密码
	private String password;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
