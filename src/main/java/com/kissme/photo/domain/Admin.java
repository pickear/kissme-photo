package com.kissme.photo.domain;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author loudyn
 * 
 */
public class Admin implements Serializable {
	private static final long serialVersionUID = 1L;

	private Admin(String username, String password) {
		this.username = username;
		this.password = password;
	}

	private final String username;
	private final String password;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isValidate() {
		return StringUtils.isNotBlank(getUsername()) && StringUtils.isNotBlank(getPassword());
	}

	/**
	 * 
	 * @return
	 */
	public static Admin get() {
		return new Admin("admin", "admin!?:135");
	}
}
