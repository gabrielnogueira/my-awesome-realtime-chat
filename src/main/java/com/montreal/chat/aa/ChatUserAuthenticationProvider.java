package com.montreal.chat.aa;

import static com.montreal.chat.common.Constants.EMPTY_STRING;
import static com.montreal.chat.util.Utils.stringToMap;
import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

	private static final String CHAT_EMAIL_FROM = "infotisolutions@gmail.com";

	@Autowired
	UserService userService;

	@Autowired
	Environment env;
	
	  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	  DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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
			LocalDateTime dt = LocalDateTime.now();
			EmailSenderUtil.sendEmail(CHAT_EMAIL_FROM, userDto.getEmail(),
					"Cadastro/Login Detectado", "text/plain", "Foi detectado um login utilizando este e-mail juntamente com o CPF: " + userDto.getCpf() + " as " + dt.format(dateFormatter) + "na data " + dt.format(timeFormatter) ,
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
