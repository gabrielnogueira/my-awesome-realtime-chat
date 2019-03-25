package com.montreal.chat.aa;

import static com.montreal.chat.common.Status.OFFLINE;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.montreal.chat.model.repository.UserRepository;
import com.montreal.chat.view.dto.ChatUserDTO;

@Component
public class ChatUserLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	private static final String LOGIN_PATH = "/login.xhtml";

	@Autowired
	UserRepository userRepository;
	
	@Override
	public void onLogoutSuccess(javax.servlet.http.HttpServletRequest request, 
            javax.servlet.http.HttpServletResponse response,
            Authentication authentication)
     throws java.io.IOException,
            javax.servlet.ServletException{
		
		ChatUserDTO userDto = (ChatUserDTO) authentication.getDetails();
		
		Optional<com.montreal.chat.model.entity.User> optUser = userRepository.findById(Long.parseLong(userDto.getId()));

		optUser.ifPresent(user ->{
			userRepository.save(user.withStatus(OFFLINE));
		});
		
		response.sendRedirect(request.getContextPath() + LOGIN_PATH);
	}
}