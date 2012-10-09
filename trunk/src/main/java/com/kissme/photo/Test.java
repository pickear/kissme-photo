package com.kissme.photo;

import com.kissme.photo.infrastructure.util.SignatureUtils;

public class Test {
	public static void main(String[] args) {
		long timestamp = System.currentTimeMillis();
		System.out.println(timestamp);
		String appkey = "R7BJVv";
		String appsecret = "3ff212e3ee4247d1b8182ac4569bcc74";
		String signature = SignatureUtils.signature(String.valueOf(timestamp), appsecret);
		System.out.println("timestamp=" + timestamp + "&signature=" + signature + "&appkey=" + appkey + "&callback=123");
		String adminSignature = SignatureUtils.signature(String.valueOf(timestamp), "admin-admin!?:135");
		System.out.println("timestamp=" + timestamp + "&signature=" + adminSignature + "&callback=123");
	}
}
