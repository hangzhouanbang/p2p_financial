package com.anbang.p2p.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anbang.p2p.conf.AdminConfig;
import com.anbang.p2p.web.controller.AdminController;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 管理员拦截器
 *
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

	static String token = AdminController.encode(AdminConfig.ACCOUNT +AdminConfig.PASS);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String loginToken = request.getParameter("token");
		if (StringUtil.isBlank(loginToken)) {
			return false;
		}
		if (token.equals(loginToken)) {
			return true;
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
