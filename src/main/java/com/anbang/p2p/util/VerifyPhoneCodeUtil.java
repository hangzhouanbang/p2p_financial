package com.anbang.p2p.util;

import java.util.UUID;

public class VerifyPhoneCodeUtil {

	private static String[] chars = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	/**
	 * 只能生成6位字符串，有概率重复
	 */
	public static String generateVerifyCode() {
		StringBuffer buffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 6; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int n = Integer.parseInt(str, 16);
			buffer.append(chars[n % 10]);
		}
		return buffer.toString();
	}
}
