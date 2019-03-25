package com.montreal.chat.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.montreal.chat.common.Status;
import com.montreal.chat.service.UserService;
import com.montreal.chat.view.dto.ChatUserDTO;

@Component
public class WebSocketEventListener {

	private static final String SIMP_USER = "simpUser";

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

	@Autowired
	private UserService userService;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		String userId = getAuthDetails(event).getId();
		if (userId != null) {
			logger.info("User connects : " + userId);
			userService.updateUserStatus(userId, Status.ONLINE.name());
		}
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		String userId = getAuthDetails(event).getId();
		
		if (userId != null) {
			logger.info("User disconnects : " + userId);
			userService.updateUserStatus(userId, Status.OFFLINE.name());
		}
	}
	
	private ChatUserDTO getAuthDetails(AbstractSubProtocolEvent event) {
		return (ChatUserDTO) ((Authentication) StompHeaderAccessor.wrap(event.getMessage()).getHeader(SIMP_USER)).getDetails();
	}
}