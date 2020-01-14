package com.maywide.liveshow.net.resp;

import java.io.Serializable;

/**
 * Created by user on 2018/11/14.
 */

public class LoginResp implements Serializable{

	private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
