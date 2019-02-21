package com.anbang.p2p.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.anbang.p2p.cqrs.c.service.UserAuthService;

/**
 * 权限拦截器
 * 
 * @author 林少聪 2018.5.31
 *
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

	@Autowired
	private UserAuthService userAuthService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = request.getParameter("token");
		if (token == null) {
			return false;
		}
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return false;
		}
		return true;
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
