package com.maywide.liveshow.net.resp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2018/11/14.
 */

public class ResponseList<T> implements Serializable {
	private String code;
	private String msg;
	private List<T> data;

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

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
