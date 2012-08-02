package com.kissme.photo.domain.app;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * @author loudyn
 * 
 */
public class AppKeys {
	private String appKey;
	private String secretKey;
	private long createTime;
	private long expireTime;

	AppKeys() {
		setCreateTime(System.currentTimeMillis());
	}

	public String getAppKey() {
		return appKey;
	}

	public AppKeys setAppKey(String appKey) {
		this.appKey = appKey;
		return this;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public AppKeys setSecretKey(String secretKey) {
		this.secretKey = secretKey;
		return this;
	}

	public final long getCreateTime() {
		return createTime;
	}

	public final void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	public static class AppKeysGenerator {
		final static String[] KEY_CHARS = new String[] {
				"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
				"u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D",
				"E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
		};

		/**
		 * 
		 * @param orig
		 * @return
		 */
		public static String generateKey(String orig) {
			String md5Hex = DigestUtils.md5Hex(orig);
			String hex = md5Hex;

			String[] results = new String[4];

			for (int i = 0; i < 4; i++) {

				String temp = hex.substring(i * 8, i * 8 + 8);

				long hexAsLong = 0x3FFFFFFF & Long.parseLong(temp, 16);
				String outChars = "";

				for (int j = 0; j < 6; j++) {
					long index = 0x0000003D & hexAsLong;
					outChars += KEY_CHARS[(int) index];
					hexAsLong = hexAsLong >> 5;
				}
				results[i] = outChars;

			}

			return results[0];
		}

		/**
		 * 
		 * @return
		 */
		public static String generateSecret() {
			return UUID.randomUUID().toString().replaceAll("-", "");
		}
	}

}
