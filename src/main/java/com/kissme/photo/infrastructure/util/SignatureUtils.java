package com.kissme.photo.infrastructure.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.google.common.base.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public final class SignatureUtils {
	private final static String MAC_NAME = "HmacSHA1";

	/**
	 * 
	 * @param raw
	 * @param secret
	 * @return
	 */
	public static String signature(String raw, String secret) {
		Preconditions.checkNotNull(raw);
		Preconditions.checkNotNull(secret);

		try {

			SecretKeySpec key = createHmacKey(secret);
			return new HMACSHA1Verifier(key).signature(raw);
		} catch (Exception e) {
			throw ExceptionUtils.uncheck(e);
		}

	}

	/**
	 * 
	 * @param toVerify
	 * @param raw
	 * @param secret
	 */
	public static void verify(String toVerify, String raw, String secret) {

		Preconditions.checkNotNull(toVerify);
		Preconditions.checkNotNull(raw);
		Preconditions.checkNotNull(secret);

		try {

			SecretKeySpec key = createHmacKey(secret);
			new HMACSHA1Verifier(key).verify(raw, toVerify);
		} catch (Exception e) {
			throw ExceptionUtils.uncheck(e);
		}
	}

	private static SecretKeySpec createHmacKey(String secret) {
		return new SecretKeySpec(secret.getBytes(), MAC_NAME);
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	@SuppressWarnings("serial")
	final static class InvalidSignatueException extends RuntimeException {

		public InvalidSignatueException(Exception e) {
			super(e);
		}

		public InvalidSignatueException() {
			super();
		}

	}

	final static class HMACSHA1Verifier {
		private final SecretKey key;

		public HMACSHA1Verifier(SecretKey key) {
			Preconditions.checkNotNull(key);
			this.key = key;
		}

		String signature(String raw) {
			try {

				byte[] calculateBytes = doFinal(raw.getBytes());
				return new String(Hex.encodeHex(calculateBytes, true));
			} catch (Exception e) {
				throw ExceptionUtils.uncheck(e);
			}
		}

		private byte[] doFinal(byte[] signatueBytes) throws NoSuchAlgorithmException, InvalidKeyException {
			Mac mac = Mac.getInstance(MAC_NAME);
			mac.init(key);
			return mac.doFinal(signatueBytes);
		}

		void verify(String raw, String signature) {

			try {

				byte[] signatueBytes = Hex.decodeHex(signature.toCharArray());
				byte[] calculateBytes = doFinal(raw.getBytes());
				if (!Arrays.equals(signatueBytes, calculateBytes)) {
					throw new InvalidSignatueException();
				}
			} catch (Exception e) {
				if (e instanceof InvalidSignatueException) {
					throw (InvalidSignatueException) e;
				}

				throw new InvalidSignatueException(e);
			}
		}
	}

	private SignatureUtils() {}
}
