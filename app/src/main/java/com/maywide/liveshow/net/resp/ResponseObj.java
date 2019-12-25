package com.maywide.liveshow.net.resp;

import java.io.Serializable;

/**
 * Created by user on 2018/11/14.
 */

public class ResponseObj<T> implements Serializable {
	private String code;
	private String msg;
	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
