package com.anbang.p2p.util;

import java.util.Base64;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * base64签名工具
 *
 */
public class HmacSha1Sign {
	/**
	 * 生成签名字段
	 *
	 * @param apiKey
	 *            用户在FaceID控制台可以申请
	 * @param secretKey
	 * @param expired
	 *            签名的有效期，是一个符合 UNIX Epoch 时间戳规范的数值，单位为秒。单次签名时，设置为0。
	 * @return
	 * @throws Exception
	 */
	public static String genSign(String apiKey, String secretKey, long expired) throws Exception {
		long now = System.currentTimeMillis() / 1000;
		int rdm = Math.abs(new Random().nextInt());
		String plainText = String.format("a=%s&b=%d&c=%d&d=%d", apiKey, now + expired, now, rdm);
		byte[] hmacDigest = HmacSha1(plainText, secretKey);
		byte[] signContent = new byte[hmacDigest.length + plainText.getBytes().length];
		System.arraycopy(hmacDigest, 0, signContent, 0, hmacDigest.length);
		System.arraycopy(plainText.getBytes(), 0, signContent, hmacDigest.length, plainText.getBytes().length);
		return encodeToBase64(signContent).replaceAll("[\\s*\t\n\r]", "");
	}

	/**
	 * 生成 base64 编码
	 *
	 * @param binaryData
	 * @return
	 */
	private static String encodeToBase64(byte[] binaryData) {
		String encodedStr = Base64.getEncoder().encodeToString(binaryData);
		return encodedStr;
	}

	/**
	 * 生成 hmacsha1 签名
	 *
	 * @param binaryData
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] HmacSha1(byte[] binaryData, String key) throws Exception {
		Mac mac = Mac.getInstance("HmacSHA1");
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
		mac.init(secretKey);
		byte[] HmacSha1Digest = mac.doFinal(binaryData);
		return HmacSha1Digest;
	}

	/**
	 * 生成 hmacsha1 签名
	 *
	 * @param plainText
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] HmacSha1(String plainText, String key) throws Exception {
		return HmacSha1(plainText.getBytes(), key);
	}
}
