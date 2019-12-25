package com.maywide.liveshow.net.req;

/**
 * Created by user on 2018/11/14.
 */

public class BaseReq {
	//版本号
	protected String version = "V1.0.0";
	//手机号
	protected String mobile;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
