package com.montreal.chat.view.jsf.bean;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gson.Gson;
import com.montreal.chat.service.UserService;
import com.montreal.chat.view.dto.ChatMessageDTO;
import com.montreal.chat.view.dto.ChatUserDTO;

@ManagedBean(value = "chat")
@ViewScoped
public class ChatUserBean {
	@Autowired
	UserService userService;

	private ChatUserDTO user;
	private final Gson gsonInstance = new Gson();
	private String selectedContact;
	private String currentMessage;
	public String test2;

	public ChatUserBean() {
	}

	public ChatUserDTO getUser() {
		if (user == null) {
			ChatUserDTO authUser = getAuthUser();
			this.user = authUser.withContacts(
					userService.getContacts().stream().map(user -> new ChatUserDTO(user, Long.valueOf(authUser.getId()))).collect(toList()))
					.withMessages(userService.getPublicMessages().stream().map(message -> new ChatMessageDTO().withContent(message.getContent()).fromUser(new ChatUserDTO(message.getFrom()))).collect(toList()));
		}
		return user;
	}

	public void setUser(ChatUserDTO user) {
		this.user = user;
	}

	public void newContact() {
		if (this.user.getContacts() == null) {
			this.user.setContacts(new ArrayList<ChatUserDTO>());
		}
		ChatMessageDTO message = gsonInstance.fromJson(getFacesParam().get("newContact"), ChatMessageDTO.class);
		this.user.getContacts().add(message.getUserFrom());
	}

	public void updateContacts() {
		ChatMessageDTO message = gsonInstance.fromJson(getFacesParam().get("contactUpdate"), ChatMessageDTO.class);
		if(this.user.getId().equals(message.getUserFrom().getId())) {
			this.user.setStatus(message.getUserFrom().getStatus());
			return;
		}
		this.user.getContacts().stream().filter(contact -> contact.getId().equals(message.getUserFrom().getId()))
				.findFirst().get().setStatus(message.getUserFrom().getStatus());
	}

	public void changeStatus() {
		this.userService.updateUserStatus(String.valueOf(this.user.getId()), getFacesParam().get("newStatus"));
	}

	public void sendNewMessage() {
		try {
			userService.newMessage(this.currentMessage, getFacesParam().get("idTo"));
			this.currentMessage = "";
		} catch (Exception e) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Essa mensagem não pode ser enviada por que contém palavras proibidas"));
		}
	}
	
	public void setCurrentMessage() {
		this.currentMessage = getFacesParam().get("currentMessage");
	}
	
	public String getCurrentMessage() {
		return currentMessage;
	}

	public void setCurrentMessage(String currentMessage) {
		this.currentMessage = currentMessage;
	}

	public void updateMessages() {
		ChatMessageDTO message = gsonInstance.fromJson(getFacesParam().get("newMessageReceived"), ChatMessageDTO.class);
		if(message.getUserTo() == null) {
			this.getUser().getMessages().add(message);
			return;
		}
		if(!(message.getUserFrom().getId().equals(this.user.getId()) || message.getUserTo().getId().equals(this.getUser().getId()))) {
			return;
		}
		Optional<ChatUserDTO> optcontact = this.user.getContacts().stream().filter(contact -> contact.getId().equals(message.getUserFrom().getId()) || contact.getId().equals(message.getUserTo().getId())).findFirst();
		optcontact.ifPresent(contact-> {
			if(contact.getMessages() == null) {
				contact.setMessages(new ArrayList<ChatMessageDTO>());
			}
			contact.getMessages().add(message);
		});
		
	}
	
	public String getNewMessage() {
		return currentMessage;
	}

	public void setNewMessage(String newMessage) {
		this.currentMessage = newMessage;
	}

	public String getSelectedContact() {
		return selectedContact;
	}

	public void setSelectedContact(String selectedContact) {
		this.selectedContact = selectedContact;
	}

	public void selectContact() {
		this.selectedContact = getFacesParam().get("selectedContact");
	}
	
	private ChatUserDTO getAuthUser() {
		return (ChatUserDTO) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	private Map<String, String> getFacesParam() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
}
