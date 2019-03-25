package com.montreal.chat.aa;

import static com.montreal.chat.common.Constants.CPF;
import static com.montreal.chat.common.Constants.EMAIL;
import static com.montreal.chat.common.Constants.NAME;
import static com.montreal.chat.common.Constants.POST;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.montreal.chat.util.Utils;

public class ChatUserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Override
	protected String obtainUsername(HttpServletRequest request) {
		Map<String, String> details = new HashMap<>();

		details.putIfAbsent(NAME, request.getParameter(NAME));
		details.putIfAbsent(EMAIL, request.getParameter(EMAIL));
		details.putIfAbsent(CPF, request.getParameter(CPF));

		return Utils.mapToString(details);
	}

	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return request.getMethod().equals(POST) && (SecurityContextHolder.getContext().getAuthentication() == null || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) && !request.getRequestURL().toString().contains("h2-console");
	}
}
