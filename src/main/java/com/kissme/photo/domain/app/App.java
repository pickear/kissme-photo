package com.kissme.photo.domain.app;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kissme.photo.domain.AbstractDomain;

/**
 * 
 * @author loudyn
 * 
 */
public class App extends AbstractDomain {

	private static final long serialVersionUID = 1L;
	private String name;
	private String email;
	private String phone;

	private AppKeys keys;

	public String getName() {
		return name;
	}

	public App setName(String name) {
		this.name = name;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public App setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public App setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public AppKeys getKeys() {
		return keys;
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public String getAppKey() {
		if (null == getKeys()) {
			return null;
		}

		return getKeys().getAppKey();
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public String getAppSecret() {
		if (null == getKeys()) {
			return null;
		}

		return getKeys().getSecretKey();
	}

	public App setKeys(AppKeys keys) {
		this.keys = keys;
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public App createKeys() {

		AppKeys keys = new AppKeys();
		String key = AppKeys.AppKeysGenerator.generateKey(getName());
		String secret = AppKeys.AppKeysGenerator.generateSecret();
		keys.setAppKey(key).setSecretKey(secret);
		return setKeys(keys);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isExpire() {
		return System.currentTimeMillis() > getKeys().getExpireTime();
	}

	/**
	 * 
	 * @param expireTime
	 * @param unit
	 * @return
	 */
	public App expireAfter(int expireTime, TimeUnit unit) {
		getKeys().setExpireTime(getKeys().getCreateTime() + unit.toMillis(expireTime));
		return this;
	}

	/**
	 * 
	 * @param years
	 * @return
	 */
	public App expireAfterYears(int years) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getKeys().getCreateTime());
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) + years);
		getKeys().setExpireTime(c.getTimeInMillis());
		return this;
	}

}
