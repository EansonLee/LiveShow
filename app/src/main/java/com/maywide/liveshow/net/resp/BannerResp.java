package com.maywide.liveshow.net.resp;

import java.io.Serializable;

/**
 * Created by user on 2018/11/15.
 */

public class BannerResp implements Serializable {

	private String title;
	private String img;
	private String time;
	private String selectedImgUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSelectedImgUrl() {
		return selectedImgUrl;
	}

	public void setSelectedImgUrl(String selectedImgUrl) {
		this.selectedImgUrl = selectedImgUrl;
	}
}
