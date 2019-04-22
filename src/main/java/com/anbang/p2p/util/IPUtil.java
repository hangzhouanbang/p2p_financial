package com.anbang.p2p.util;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {

	public static String getRealIp(HttpServletRequest request) {
		String ip;
		ip = request.getHeader("X-Real-IP");
		if (ip == null) {
			String xip = request.getHeader("x-forwarded-for");
			if (xip != null) {
				String[] ips = xip.split(",");
				ip = ips[0];
			} else {
				ip = request.getRemoteAddr();
			}
		}
		return ip;
	}
}
