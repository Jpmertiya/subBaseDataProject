package com.MyAPi.Entity;

import org.springframework.stereotype.Component;

@Component
public class LoginCred {
	private String login_id;
	private String password;

	public LoginCred(String loginId, String password) {
		super();
		this.login_id = loginId;
		this.password = password;
	}

	public String getLoginId() {
		return login_id;
	}

	public void setLoginId(String loginId) {
		this.login_id = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LoginCred() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "LoginCred [loginId=" + login_id + ", password=" + password + "]";
	}

}
