package com.montreal.chat.aa;

import static com.montreal.chat.common.Constants.EMPTY_STRING;
import static com.montreal.chat.util.Utils.stringToMap;
import static java.util.Collections.emptyList;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.montreal.chat.common.Constants;
import com.montreal.chat.service.UserService;
import com.montreal.chat.util.EmailSenderUtil;
import com.montreal.chat.view.dto.ChatUserDTO;

@Component
public class ChatUserAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	UserService userService;

	@Autowired
	Environment env;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (!LoginFieldsValidator.validate((ChatUserDTO) authentication.getDetails())) {
			throw new ChatUserAuthenticationException("");
		}
	}

	@Override
	protected UserDetails retrieveUser(String loginInfo, UsernamePasswordAuthenticationToken authentication) {
		Map<String, String> details = stringToMap(loginInfo);
		ChatUserDTO user = new ChatUserDTO().withCpf(details.get(Constants.CPF)).withEmail(details.get(Constants.EMAIL))
				.withName(details.get(Constants.NAME));
		authentication.setDetails(user);
		return new User(String.valueOf(user.getCpf()), EMPTY_STRING, emptyList());
	}

	@Override
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
			UserDetails user) {
		ChatUserDTO details = (ChatUserDTO) authentication.getDetails();
		ChatUserDTO userDto = userService.doLogin(details);
		if (userDto == null) {
			return null;
		}
		if (details.getId() == null) {
			EmailSenderUtil.sendEmail("infotisolutions@gmail.com", "infotisolutions@gmail.com",
					"Sending with SendGrid is Fun", "text/plain", "and easy to do anywhere, even with Java",
					env.getProperty("SENDGRID_API_KEY"));
		}
		((UsernamePasswordAuthenticationToken) authentication).setDetails(userDto);
		return super.createSuccessAuthentication(principal, authentication, user);	
	}

	public class ChatUserAuthenticationException extends AuthenticationException {
		private static final long serialVersionUID = 1L;

		public ChatUserAuthenticationException(String msg) {
			super(msg);
		}
	}
}
