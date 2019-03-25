package com.montreal.chat.view.jsf.bean;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.montreal.chat.service.UserService;
import com.montreal.chat.view.dto.ChatMessageDTO;
import com.montreal.chat.view.dto.ChatUserDTO;

@ManagedBean(value = "chat")
@ViewScoped
public class ChatUserBean {
	private static final String NEW_MESSAGE_RECEIVED = "newMessageReceived";
	private static final String CURRENT_MESSAGE = "currentMessage";
	private static final String ERRO = "Erro!";
	private static final String ID_TO = "idTo";
	private static final String NEW_STATUS = "newStatus";
	private static final String CONTACT_UPDATE = "contactUpdate";
	private static final String NEW_CONTACT = "newContact";
	private static final String SELECTED_CONTACT = "selectedContact";

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
			this.user = userService.getCurrentUser();
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
		ChatMessageDTO message = gsonInstance.fromJson(getFacesParam().get(NEW_CONTACT), ChatMessageDTO.class);
		this.user.getContacts().add(message.getUserFrom());
	}

	public void updateContacts() {
		ChatMessageDTO message = gsonInstance.fromJson(getFacesParam().get(CONTACT_UPDATE), ChatMessageDTO.class);
		if(this.user.getId().equals(message.getUserFrom().getId())) {
			this.user.setStatus(message.getUserFrom().getStatus());
			return;
		}
		this.user.getContacts().stream().filter(contact -> contact.getId().equals(message.getUserFrom().getId()))
				.findFirst().get().setStatus(message.getUserFrom().getStatus());
	}

	public void changeStatus() {
		this.userService.updateUserStatus(String.valueOf(this.user.getId()), getFacesParam().get(NEW_STATUS));
	}

	public void sendNewMessage() {
		try {
			userService.newMessage(this.currentMessage, getFacesParam().get(ID_TO));
			this.currentMessage = "";
		} catch (Exception e) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ERRO, e.getMessage()));
		}
	}
	
	public void setCurrentMessage() {
		this.currentMessage = getFacesParam().get(CURRENT_MESSAGE);
	}
	
	public String getCurrentMessage() {
		return currentMessage;
	}

	public void setCurrentMessage(String currentMessage) {
		this.currentMessage = currentMessage;
	}

	public void updateMessages() {
		ChatMessageDTO message = gsonInstance.fromJson(getFacesParam().get(NEW_MESSAGE_RECEIVED), ChatMessageDTO.class);
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
		this.selectedContact = getFacesParam().get(SELECTED_CONTACT);
	}
	

	private Map<String, String> getFacesParam() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
}
